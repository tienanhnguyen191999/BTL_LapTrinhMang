/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import consts.Consts;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import map.Map1;
import model.Ball;
import model.Bar;
import model.Brick;
import model.ClientState;

/**
 *
 * @author tienanh
 */
public class GamePlay extends JPanel{
	private int width, height, padding = 50;
	private int menuWidth, menuHeight;
	private int delayTime = 7, point_p1 = 0, point_p2 = 0; 
	private boolean isPlay, isInitNewGame, isGameOver = true;
	private Brick[] bricks;
	
	private ClientState p1, p2;
	private Map1 map;
	private Menu menu;
	private ObjectInputStream inp;
	private ObjectOutputStream out;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); 
		
		if (isPlay){
		// Ball p1
		g.setColor(Color.RED);
		g.fillOval(p1.getBall().getX(), p1.getBall().getY(), p1.getBall().getRadius(), p1.getBall().getRadius());
		
		// Bar p1
		g.setColor(Color.GREEN);
		g.fillRect(p1.getBar().getX(), p1.getBar().getY(), p1.getBar().getWidth(), p1.getBar().getHeight());
		
		
		// Ball p2
		g.setColor(Color.BLUE);
		g.fillOval(p2.getBall().getX(), p2.getBall().getY(), p2.getBall().getRadius(), p2.getBall().getRadius());

		// Bar p2
		g.setColor(Color.GREEN);
		g.fillRect(p2.getBar().getX(), p2.getBar().getY(), p2.getBar().getWidth(), p2.getBar().getHeight());
		
		// Draw brick
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 0; j < 12; j++){
				Brick curBrick = bricks[i*12 + j];
				if ( curBrick.getIsDisplay()){
					g.setColor(Color.YELLOW);
					g.fillRect(curBrick.getX(), curBrick.getY(), Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT);
				}
			}
		}
		
		// Line separate 
		g.setColor(Color.WHITE);
		g.drawLine(width, 0, width, height);
		// Menu
		menu.draw(g, menuWidth, menuHeight,point_p1, point_p2, isPlay, isInitNewGame);
		if (isGameOver){
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.PLAIN, 50));
			g.drawString("GAME OVER",width / 2 - 50*3, height / 2);
		}
		g.dispose();
		}
	}
	
	/**
	 *
	 * @param width
	 * @param height
	 * @param socket
	 */
	public GamePlay(int width, int height, Socket socket) {
		p1 = new ClientState();
		p2 = new ClientState();
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			inp = new ObjectInputStream(socket.getInputStream());
			
			this.width = width * 3/4;
			this.height = height;
			this.menuWidth = width * 1/4;
			this.menuHeight = height;
			initNewGame();
			// handle Bar move
			addKeyListener(this.handleBarMove());
			addMouseListener(this.handleClickEvent());
		} catch (IOException ex) {
			Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void play () {
		while (true){
			try {
				p1 = (ClientState)inp.readObject();
				p2 = (ClientState)inp.readObject();
				bricks = (Brick[])inp.readObject();
				isPlay = true;
				repaint();
				if ( System.getProperty("os.name").equals("Linux") ){
					Toolkit.getDefaultToolkit().sync();
				}
			} catch (IOException ex) {
				Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	/**
	 *
	 */
	public void initNewGame (){
		isPlay = isGameOver = false;
		isInitNewGame = true;
		
		// Init Jpanel
		setSize(this.width + this.menuWidth, this.height + this.menuHeight);
		setBackground(Color.BLACK);
		setFocusable(true);
		setVisible(true);
		
		// Init new Map
		map = new Map1(width, height);
		// Init Menu
		menu = new Menu();
	}
	
	private KeyAdapter handleBarMove() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				try {
					ArrayList<Integer> validKeyCode  = new ArrayList<Integer>();
					validKeyCode.add(KeyEvent.VK_LEFT);
					validKeyCode.add(KeyEvent.VK_RIGHT);
					validKeyCode.add(KeyEvent.VK_ENTER);
					if (validKeyCode.contains(ke.getKeyCode())){
						out.writeObject(ke.getKeyCode());
					}
				} catch (Exception ex) {
					Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		};
	}

	private MouseListener handleClickEvent() {
		JPanel self = this;
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getX() >= width + 20 && me.getX() <= width + 20 + 150 &&
				    me.getY() >= height - 100 && me.getY() <= height - 50){
					System.out.println("Pause event trigger");
					isPlay = !isPlay;
					isInitNewGame = false;
				}else if (me.getX() >= width + 220 && me.getX() <= width + 220 + 150 &&
				    me.getY() >= height - 100 && me.getY() <= height - 50) {
					System.out.println("Exit event trigger");
					JFrame parent = (JFrame)self.getTopLevelAncestor();
					parent.dispose();
					System.exit(0);
				}
			}
		};
	}
}