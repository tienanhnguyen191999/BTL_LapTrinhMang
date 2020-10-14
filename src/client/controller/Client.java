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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tienanh
 */
public class Client {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket(Consts.IP_HOST, Consts.PORT);
			JFrame mainFrame = new JFrame("Brick Breaker");
			mainFrame.setSize(1600, 1000);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setVisible(true);
			
			GamePlay gamePlay = new GamePlay(1600, 1000);
			mainFrame.add(gamePlay);
		} catch (IOException ex) {
			System.out.println("Server not found.");
		}
	}
}
