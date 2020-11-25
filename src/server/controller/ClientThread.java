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
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import model.SocketIO;

/**
 *
 * @author tienanh
 */
public class ClientThread extends Thread implements Serializable{
	private ClientState state;
	private SocketIO socketIO;
	
	private Room selectedRoom;
	private WaitingRoomThread selectedRoomThread;
	private GamePlayThread selectedGamePlay;
	
    private ArrayList<Room> listRoom;
	private ArrayList<String> listPlayer;
    private ArrayList<WaitingRoomThread> listRoomThread;
	private ArrayList<GamePlayThread> listGamePlay;
	
	public ClientThread(
			ArrayList<String> listPlayer, 
			ArrayList<Room> listRoom, 
			ArrayList<WaitingRoomThread> listRoomThread,
			ArrayList<GamePlayThread> listGamePlay,
            Socket socket) 
		{
		try {
            this.listRoom = listRoom;
            this.listRoomThread = listRoomThread;
			this.listGamePlay= listGamePlay;
			this.listPlayer = listPlayer;
			
            this.state = new ClientState();
            this.socketIO = new SocketIO();
			this.socketIO.setSocket(socket);
			this.socketIO.setInput(new ObjectInputStream(socket.getInputStream()));
            this.socketIO.setOutput(new ObjectOutputStream(socket.getOutputStream()));
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
                        this.startGame();
                        break;
					case Consts.SEND_MESSAGE:
                        this.sendMessage();
                        break;
					case Consts.UPDATE_P1_BALL_COLOR:
						handleUpdateP1BallColor();
						break;
					case Consts.UPDATE_P2_BALL_COLOR:
						handleUpdateP2BallColor();
						break;
					case Consts.REMOVE_ROOM:
						handleRemoveRoom();
						break;
					case Consts.OUT_ROOM:
						handleOutRoom();
						break;
					case Consts.GAME_PAUSE:
						handleGamePause();
						break;
					case Consts.GAME_UNPAUSE:
						handleGameUnPause();
						break;
					case Consts.SET_PLAYER_NAME:
						setPlayerName();
						break;
					case Consts.UPDATE_PLAYER_NAME:
						updatePlayerName();
						break;
                    case Consts.SAVE_GAME:
                        saveGame();
                        break;
                    case Consts.LOAD_GAME:
                        loadGame();
                        break;
                }
            } catch (IOException ex) {
                System.out.println("Socket [ "+ Thread.currentThread().getName() +" ] Closed");
                this.state.isSocketClose = true;
				removePlayerFromRoom();
				removePlayerFromListPLayer();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	}
	
	public void removePlayerFromListPLayer () {
		int index = 0;
		for (String clientName : listPlayer){
			if (clientName.toLowerCase().trim().equals(this.getClientState().getName().toLowerCase().trim())){
				listPlayer.remove(index);
				return;
			}
			index++;
		}
	}
	
    public void loadGame () {
        try {
            Room saveRoom = (Room)socketIO.getInput().readObject();
            saveRoom.setStatus(Consts.LOADED_ROOM);
            listRoom.add(saveRoom);
			selectedRoom = saveRoom;
            
            // Create new waitingRoomThread
            selectedRoomThread = new WaitingRoomThread();
            selectedRoomThread.setRoom(saveRoom);
            selectedRoomThread.setP1(this);
			selectedRoomThread.getP1().setClientState(saveRoom.getP1());
            listRoomThread.add(selectedRoomThread);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveGame () {
        try {
            if (selectedGamePlay == null){
				selectedGamePlay = this.getGamePlayByPlayerName(this.state.getName());
			}
            // Get File Name
            String fileName = (String) socketIO.getInput().readObject();
            
            // Save to file
            ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
			Room saveRoom = new Room();
			saveRoom.setMap(selectedGamePlay.getMap());
			saveRoom.setP1(selectedGamePlay.getArr_player().get(0).getClientState());
			saveRoom.setP2(selectedGamePlay.getArr_player().get(1).getClientState());
			saveRoom.setSpeed(selectedGamePlay.getSpeed());
            outFile.writeObject(saveRoom);
            outFile.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
	public void updatePlayerName () {
		try {
			int index = 0;
			int save_index = 0;
			String name = (String) socketIO.getInput().readObject();
			for (String tmp : listPlayer){
				if (tmp.trim().toLowerCase().equals(this.state.getName().trim().toLowerCase())){
					save_index = index;
				}else if( tmp.trim().toLowerCase().equals(name.trim().toLowerCase())){
					// Name is regitered
					socketIO.getOutput().writeObject(false);
					return;
				}
				index ++;
			}
			socketIO.getOutput().writeObject(true);
			this.state.setName(name);
			listPlayer.set(save_index, name.trim().toLowerCase());
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void setPlayerName () { 
		try {
			String name = (String) socketIO.getInput().readObject();
			for (String tmp : listPlayer){
				System.out.println("tmp.getClientState().getName(): " + tmp);
				if (tmp.trim().toLowerCase().equals(name.trim().toLowerCase())){
					// Name is regitered
					socketIO.getOutput().writeObject(false);
					return;
				}
			}
			// Pass
			socketIO.getOutput().writeObject(true);
			this.state.setName(name);
			listPlayer.add(name.trim().toLowerCase());
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void handleGameUnPause () {		
		try {
			if (selectedGamePlay == null){
				selectedGamePlay = this.getGamePlayByPlayerName(this.state.getName());
			}
			
			for (int i = 3 ; i >= 0 ; i--){
				selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(Consts.COUNTER_BEFORE_START);
                selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(i);
				selectedRoomThread.getP1().getSocketIO().getOutput().reset();
                
                selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(Consts.COUNTER_BEFORE_START);			
				selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(i);
				selectedRoomThread.getP2().getSocketIO().getOutput().reset();
				TimeUnit.SECONDS.sleep(1);
			}
			
			selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(Consts.GAME_UNPAUSE);
			selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(Consts.GAME_UNPAUSE);
			selectedGamePlay.playGame();
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void handleGamePause () {
		try {
			if (selectedGamePlay == null){
				selectedGamePlay = this.getGamePlayByPlayerName(this.state.getName());
			}
			selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(Consts.GAME_PAUSE);
			selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(Consts.GAME_PAUSE);
			selectedGamePlay.pauseGame();
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void handleOutRoom () {
		try {
			selectedRoomThread.setP2(null);
			selectedRoomThread.getP1().getSocketIO().getOutput().reset();
			selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(Consts.OUT_ROOM);
			selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(selectedRoom);
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void handleRemoveRoom () {
		if (selectedRoomThread.getP2() != null){
			try {
				selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(Consts.REMOVE_ROOM);
			} catch (IOException ex) {
				Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		try {
			selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(Consts.REMOVE_ROOM);
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
		// remove room
		listRoom.remove(getRoomIndexByName(selectedRoomThread.getRoom().getName()));
		listRoomThread.remove(getRoomThreadIndexByRoomName(selectedRoomThread.getRoom().getName()));
	}
	
	public void removePlayerFromRoom () {
		try {
			for (WaitingRoomThread roomThread : listRoomThread){
				// If player is room host => remove room, kick p2 
				if (roomThread.getRoom().getP1().getName() == state.getName()){
					if (roomThread.getRoom().getP2() != null){
						roomThread.getP2().getSocketIO().getOutput().writeObject(Consts.REMOVE_ROOM);
					}
					// remove room
					listRoom.remove(getRoomIndexByName(roomThread.getRoom().getName()));
					listRoomThread.remove(getRoomThreadIndexByRoomName(roomThread.getRoom().getName()));
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
    
	private void handleUpdateP1BallColor () {
		try {
			String color = (String) socketIO.getInput().readObject();
			selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(Consts.UPDATE_P1_BALL_COLOR);
			selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(color);
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void handleUpdateP2BallColor () {
		try {
			String color = (String) socketIO.getInput().readObject();
			selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(Consts.UPDATE_P2_BALL_COLOR);
			selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(color);
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void sendMessage() {
		try {
			String message = (String) socketIO.getInput().readObject();
			message = state.getName() + ": " + message + "\n";
			
			// Send flag
            selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(Consts.SEND_MESSAGE);
            selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(Consts.SEND_MESSAGE);
			
			// Send data
            selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(message);
            selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(message);
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
    private void startGame() {
        try {
            Room newRoom = (Room) socketIO.getInput().readObject();
            
            selectedRoomThread = this.getWaitingRoomThreadByRoomName(newRoom.getName());
			selectedRoomThread.getP1().setClientState(newRoom.getP1());
			selectedRoomThread.getP2().setClientState(newRoom.getP2());
			if (selectedRoom.getStatus() == Consts.LOADED_ROOM){
				selectedRoomThread.getP1().setClientState(selectedRoomThread.getRoom().getP1());
				selectedRoomThread.getP2().setClientState(selectedRoomThread.getRoom().getP2());
			}
			
			GamePlayThread selectedGamePlay = new GamePlayThread();
            selectedGamePlay.addPlayterToRoom(selectedRoomThread.getP1());
            selectedGamePlay.addPlayterToRoom(selectedRoomThread.getP2());
			selectedGamePlay.setSpeed(newRoom.getSpeed());
            selectedGamePlay.setMap(selectedRoomThread.getRoom().getMap());
			// Load Save Game
			if (selectedRoom.getStatus() == Consts.LOADED_ROOM){
				selectedGamePlay.setIsSaveGameLoad(true);
			}
			// One Ball mode
			if (selectedRoom.getStatus() != Consts.LOADED_ROOM && selectedRoom.getGameMode() == Consts.ONE_BALL){
				selectedGamePlay.setGameMode(Consts.ONE_BALL);
			}
			selectedGamePlay.start(); // Active Thread
            selectedRoomThread.getP1().getSocketIO().getOutput().writeObject(Consts.START_GAME);
            selectedRoomThread.getP2().getSocketIO().getOutput().writeObject(Consts.START_GAME);
            selectedGamePlay.startGame();

			// Add room to list
			listGamePlay.add(selectedGamePlay);
			// remove room
			listRoom.remove(getRoomIndexByName(selectedRoomThread.getRoom().getName()));
            listRoomThread.remove(getRoomThreadIndexByRoomName(selectedRoomThread.getRoom().getName()));
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
			newRoom = this.getWaitingRoomThreadByRoomName(newRoom.getName()).getRoom();
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
			newRoom.setStatus(Consts.WAITING);
            
            // Success
			
            socketIO.getOutput().writeObject(true);
            selectedRoom = newRoom;
            listRoom.add(newRoom);
            
            // Create new waitingRoomThread
            selectedRoomThread = new WaitingRoomThread();
            selectedRoomThread.setRoom(newRoom);
            selectedRoomThread.setP1(this);
			selectedRoomThread.getP1().setClientState(selectedRoomThread.getRoom().getP1());
            listRoomThread.add(selectedRoomThread);
			
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    private void joinRoom() {
        try {
            Room selectedRoom = (Room) socketIO.getInput().readObject();
			
            // Join to save Room
            if (selectedRoom.getStatus() == Consts.LOADED_ROOM){
				// Update player name
                selectedRoomThread = this.getWaitingRoomThreadByRoomName(selectedRoom.getName());
                selectedRoomThread.setP2(this);
				selectedRoomThread.getP2().getClientState().setName(this.getClientState().getName());
				selectedRoomThread.getRoom().getP2().setName(this.getClientState().getName());
				
				// Send updated Room to sender
				socketIO.getOutput().reset();
                socketIO.getOutput().writeObject(selectedRoomThread.getRoom());
				
                
                selectedRoomThread.getP2().socketIO.getOutput().writeObject(Consts.ROOM_EXISTS);
                selectedRoomThread.getP1().socketIO.getOutput().writeObject(Consts.UPDATE_WAITING_ROOM);
                return;
            }
            
            // Join to normal Room
            for (Room room: listRoom){
                if (selectedRoom.getName().trim().toLowerCase().equals(room.getName().trim().toLowerCase())){
                    // update room
                    room.setP2(this.getClientState());
                    room.setStatus(Consts.READY);
                    // Send updated Room to sender
					socketIO.getOutput().reset();
                    socketIO.getOutput().writeObject(room);
                    
                    // Add to WaitingRoomThread
                    selectedRoomThread = this.getWaitingRoomThreadByRoomName(room.getName());
                    selectedRoomThread.setRoom(room);
                    selectedRoomThread.setP2(this);
					selectedRoomThread.getP2().setClientState(selectedRoomThread.getRoom().getP2());
					selectedRoomThread.getP2().socketIO.getOutput().writeObject(Consts.ROOM_EXISTS);
                    
                    // Send update action
                    selectedRoomThread.getP1().socketIO.getOutput().writeObject(Consts.UPDATE_WAITING_ROOM);
                    return;
                }
            }
			
			// If Room notexists
			socketIO.getOutput().writeObject(selectedRoom);
			socketIO.getOutput().writeObject(Consts.ROOM_NOT_EXISTS);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

	private GamePlayThread getGamePlayByPlayerName (String name) {
        for (GamePlayThread gameplay : listGamePlay){
            if (gameplay.getArr_player().get(1).getClientState().getName().equals(name)){
                return gameplay;
            }
        }
		
		for (GamePlayThread gameplay : listGamePlay){
            if (gameplay.getArr_player().get(0).getClientState().getName().equals(name)){
                return gameplay;
            }
        }
        return null;
    }
	
    private WaitingRoomThread getWaitingRoomThreadByRoomName (String roomName) {
        for (WaitingRoomThread tmp : listRoomThread){
            if (tmp.getRoom().getName().trim().equals(roomName)){
                return tmp;
            }
        }
        return null;
    }
	
	private int getRoomThreadIndexByRoomName (String roomName) {
		int i = 0;
        for (WaitingRoomThread tmp : listRoomThread){
            if (tmp.getRoom().getName().trim().equals(roomName)){
                return i;
            }
			i++;
        }
        return -1;
    }

	private int getRoomIndexByName (String roomName) {
		int i = 0;
        for (Room tmp : listRoom){
            if (tmp.getName().trim().equals(roomName)){
                return i;
            }
			i++;
        }
        return -1;
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
