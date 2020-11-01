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

/**
 *
 * @author tienanh
 */
public class ClientThread extends Thread{
	private ClientState state;
	private ArrayList<Room> listRoom;
	private Socket socket;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
	
	public ClientThread(ArrayList<Room> listRoom, Socket socket) {
		try {
            this.listRoom = listRoom;
			this.socket = socket;
			state = new ClientState();
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectInput = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	@Override
	public void run() {
		while(!state.isSocketClose){
            try {
                Integer actionCode = (Integer)objectInput.readObject();
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
                }
            } catch (IOException ex) {
                System.out.println("Socket Closed");
                this.state.isSocketClose = true;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	}
    
    private void getListRoom() {
        try {
            objectOutput.writeObject(listRoom);
            objectOutput.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	public void handleBarMove(){
        try {
            Integer keycode = (Integer)objectInput.readObject();
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
            Room newRoom = (Room)objectInput.readObject();
            System.out.println("newRoom"+ newRoom.getName() + "\nMap Link: "+newRoom.getMap().getMapInfo().getImagePreviewPath());
            // Check room_name is unique
            System.out.println("LIST ROOM REGISTERED");
            for (Room tmp : listRoom){
                System.out.println(tmp.getName());
                if (newRoom.getName().trim().toLowerCase()
                    .equals(tmp.getName().trim().toLowerCase())){
                    objectOutput.writeObject(false);
                    return;
                }
            }
            
            objectOutput.writeObject(true);
            newRoom.setStatus(Consts.WAITING);
            listRoom.add(newRoom);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    private void joinRoom() {
        try {
            Room selectedRoom = (Room) objectInput.readObject();
            ClientState p2 = (ClientState) objectInput.readObject();
            for (Room room: listRoom){
                if (selectedRoom.getName().trim().toLowerCase().equals(room.getName().trim().toLowerCase())){
                    room.setP2(p2);
                    room.setStatus(Consts.READY);
					objectOutput.reset();
                    objectOutput.writeObject(room);
                    return;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
	public void setObjectOutput(ObjectOutputStream objectOutput) {
		this.objectOutput = objectOutput;
	}

	public void setObjectInput(ObjectInputStream objectInput) {
		this.objectInput = objectInput;
	}

	public void setClientState(ClientState state) {
		this.state = state;
	}

	public ClientState getClientState() {
		return state;
	}
	
	public boolean isClosed() {
		return socket.isClosed();
	}

	public ObjectOutputStream getObjectOutput() {
		return objectOutput;
	}

	public ObjectInputStream getObjectInput() {
		return objectInput;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Socket getSocket() {
		return socket;
	}
}
