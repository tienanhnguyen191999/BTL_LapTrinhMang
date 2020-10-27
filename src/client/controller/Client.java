/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import java.net.Socket;
import javax.swing.JFrame;
import consts.Consts;
import java.io.IOException;
import consts.Consts;

/**
 *
 * @author tienanh
 */
public class Client {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Socket 
			Socket socket = new Socket(Consts.IP_HOST, Consts.PORT);
			
			// MainFrame
			JFrame mainFrame = new JFrame("Brick Breaker");
			mainFrame.setSize(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setVisible(true);

			// GamePlay
			GamePlay gamePlay = new GamePlay(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT, socket);
			mainFrame.add(gamePlay);
			gamePlay.play();
		} catch (IOException ex) {
			System.out.println("Server not found.");
		}
	}
}
