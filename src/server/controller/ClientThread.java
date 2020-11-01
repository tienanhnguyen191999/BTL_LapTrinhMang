/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientState;
import model.Room;
import consts.Consts;
import java.io.Serializable;
import model.SocketIO;

/**
 *
 * @author tienanh
 */
public class ClientThread extends Thread implements Serializable{
	private ClientState state;
	private SocketIO socketIO;
    private ArrayList<Room> listRoom;
    private ArrayList<WaitingRoomThread> listRoomThread;
	
	
	public ClientThread(ArrayList<Room> listRoom, 
                        ArrayList<WaitingRoomThread> listRoomThread,
                        Socket socket) {
		try {
            this.listRoom = listRoom;
            this.listRoomThread = listRoomThread;
            
            this.socketIO = new SocketIO();
			this.socketIO.setSocket(socket);
			this.socketIO.setInput(new ObjectInputStream(socket.getInputStream()));
            this.socketIO.setOutput(new ObjectOutputStream(socket.getOutputStream()));
            
            state = new ClientState();
			
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	@Override
	public void run() {
		while(!state.isSocketClose){
            try {
                Integer actionCode = (Integer)socketIO.getInput().readObject();
                System.out.println(actionCode);
                switch(actionCode){
                    case Consts.GET_LIST_ROOM:
                        this.getListRoom();
                        break;
                    case Consts.CREATE_ROOM_CODE:
                        this.createNewRoom();
                        break;
                    case Consts.BAR_MOVE:
                        this.handleBarMove();
                        break;
                    case Consts.JOIN_ROOM:
                        this.joinRoom();
                        break;
                    case Consts.UPDATE_WAITING_ROOM:
                        this.updateWaitingRoom();
                        break;
                    case Consts.START_GAME:
                        System.out.println("HERE");
                        this.startGame();
                        break;
                }
            } catch (IOException ex) {
                System.out.println("Socket Closed");
                this.state.isSocketClose = true;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	}
    
    private void startGame() {
        try {
            System.out.println("HERE");
            Room newRoom = (Room) socketIO.getInput().readObject();
            
            WaitingRoomThread roomThread = this.getWaitingRoomThreadByRoomName(newRoom.getName());
            GamePlayThread gamePlay = new GamePlayThread();
            gamePlay.addPlayterToRoom(roomThread.getP1());
            gamePlay.addPlayterToRoom(roomThread.getP2());
            gamePlay.setMap(roomThread.getRoom().getMap());
            gamePlay.start(); // Active Thread
            
            
            roomThread.getP1().getSocketIO().getOutput().writeObject(Consts.START_GAME);
            roomThread.getP2().getSocketIO().getOutput().writeObject(Consts.START_GAME);
            gamePlay.startGame();
            
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateWaitingRoom() {
        try {
            // Update Room To Host
            Room newRoom = (Room) socketIO.getInput().readObject();
            socketIO.getOutput().writeObject(newRoom);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getListRoom() {
        try {
            socketIO.getOutput().writeObject(listRoom);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	public void handleBarMove(){
        try {
            Integer keycode = (Integer)socketIO.getInput().readObject();
            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    this.getClientState().getBar().setX(
                        this.getClientState().getBar().getX() >
                            this.getClientState().getBar().getSpeed() ?
                            this.getClientState().getBar().getX() -
                                this.getClientState().getBar().getSpeed() : 0
                    );
                    break;
                case KeyEvent.VK_RIGHT:
                    this.getClientState().getBar().setX(
                        this.getClientState().getBar().getX() +
                            this.getClientState().getBar().getWidth() <
                            consts.Consts.SCREEN_WIDTH*3/4 -
                                this.getClientState().getBar().getSpeed() ?
                            this.getClientState().getBar().getX() +
                                this.getClientState().getBar().getSpeed() :
                            consts.Consts.SCREEN_WIDTH*3/4 -
                                this.getClientState().getBar().getWidth()
                    );
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

    private void createNewRoom() {
        try {
            Room newRoom = (Room)socketIO.getInput().readObject();
            // Check room_name is unique
            for (Room tmp : listRoom){
                if (newRoom.getName().trim().toLowerCase()
                    .equals(tmp.getName().trim().toLowerCase())){
                    socketIO.getOutput().writeObject(false);
                    return;
                }
            }
            
            // Success
            socketIO.getOutput().writeObject(true);
            newRoom.setStatus(Consts.WAITING);
            listRoom.add(newRoom);
            
            // Create new waitingRoomThread
            WaitingRoomThread newWaitingRoomThread = new WaitingRoomThread();
            newWaitingRoomThread.setRoom(newRoom);
            newWaitingRoomThread.setP1(this);
            listRoomThread.add(newWaitingRoomThread);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    private void joinRoom() {
        try {
            Room selectedRoom = (Room) socketIO.getInput().readObject();
            ClientState p2 = (ClientState) socketIO.getInput().readObject();
            for (Room room: listRoom){
                if (selectedRoom.getName().trim().toLowerCase().equals(room.getName().trim().toLowerCase())){
                    // update room
                    room.setP2(p2);
                    room.setStatus(Consts.READY);
                    // Send updated Room to sender
					socketIO.getOutput().reset();
                    socketIO.getOutput().writeObject(room);
                    
                    // Add to WaitingRoomThread
                    WaitingRoomThread roomThread = this.getWaitingRoomThreadByRoomName(room.getName());
                    roomThread.setRoom(room);
                    roomThread.setP2(this);
                    
                    // Send update action
                    roomThread.getP1().socketIO.getOutput().writeObject(Consts.UPDATE_WAITING_ROOM);
//                    roomThread.getP1().socketIO.getOutput().writeObject(room);
                    return;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    private WaitingRoomThread getWaitingRoomThreadByRoomName (String roomName) {
        for (WaitingRoomThread tmp : listRoomThread){
            if (tmp.getRoom().getName().trim().equals(roomName)){
                return tmp;
            }
        }
        return null;
    }
    
	public void setClientState(ClientState state) {
		this.state = state;
	}

	public ClientState getClientState() {
		return state;
	}

    public SocketIO getSocketIO() {
        return socketIO;
    }

    public void setSocketIO(SocketIO socketIO) {
        this.socketIO = socketIO;
    }
}
