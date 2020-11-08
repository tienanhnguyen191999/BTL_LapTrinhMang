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
import java.awt.Dimension;
import model.SocketIO;

/**
 *
 * @author tienanh
 */
public class Client {
    private SocketIO socketIO;
	private boolean isHost;
	
    public Client(SocketIO socketIO, boolean isHost) {
        this.socketIO = socketIO;
		this.isHost = isHost;
        
        // MainFrame
		JFrame mainFrame = new JFrame("Brick Breaker");
		mainFrame.getContentPane().setPreferredSize(new Dimension(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		mainFrame.pack();
        
        // GamePlay
		GamePlay gamePlay = new GamePlay(socketIO, isHost);
		mainFrame.add(gamePlay);
		gamePlay.requestFocus();
		gamePlay.play();
    }
}
