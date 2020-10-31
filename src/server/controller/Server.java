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
import javax.swing.Timer;
import model.Room;

/**
 *
 * @author tienanh
 */
public class Server {
	private ArrayList<Room> listRoom;
    private ArrayList<ClientThread> players;
	ServerSocket server;
		
	public Server () {
		try {
			server = new ServerSocket(Consts.PORT);
            listRoom = new ArrayList<Room>();
            players = new ArrayList<ClientThread>();
            
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
				
                ClientThread player = new ClientThread(listRoom, instance);
                players.add(player);
				player.start();
				
//				// Init new Room
//				GamePlayThread gameplay = new GamePlayThread();
//				// Init new Thread for current room
//				gameplay.start();
//				gameplay.addPlayterToRoom(player);
//				if ( gameplay.isPlayAble() ) {
//					gameplay.startGame();
//				}
				
			} catch (IOException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
