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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author tienanh
 */
public class Server {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(Consts.PORT);
			System.out.println("Server is running ...");
			
			// Init new Room
			GamePlayThread gameplay = new GamePlayThread();
			// Init new Thread for current room
			gameplay.start();
			while (true) {
				Socket instance = server.accept();
				ClientThread player = new ClientThread(instance);
				player.start();
				gameplay.addPlayterToRoom(player);
				if ( gameplay.isPlayAble() ) {
					gameplay.startGame();
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
