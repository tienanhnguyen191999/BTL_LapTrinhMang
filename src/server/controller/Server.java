/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.net.ServerSocket;
import consts.Consts;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Room;

/**
 *
 * @author tienanh
 */
public class Server {
	private ServerSocket server;

	// Share data
	private ArrayList<Room> listRoom;
    private ArrayList<String> listPlayer;
    private ArrayList<WaitingRoomThread> listWaitingRoom;
    private ArrayList<GamePlayThread> listGamePlay;

	public Server () {
		try {
			server = new ServerSocket(Consts.PORT);
            
            listRoom = new ArrayList<Room>();
            listPlayer = new ArrayList<String>();
            listWaitingRoom = new ArrayList<WaitingRoomThread>();
            listGamePlay = new ArrayList<GamePlayThread>();
            
			System.out.println("Server is running ...");
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	// Main Thread
	public static void main(String[] args) {
		Server serverClass = new Server();
		serverClass.listening();
	}
	
	public void listening () {
		while (true) {
			try {
				Socket instance = server.accept();
                new ClientThread(listPlayer, listRoom, listWaitingRoom, listGamePlay, instance).start();
			} catch (IOException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
