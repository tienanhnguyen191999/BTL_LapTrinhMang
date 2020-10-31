/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import consts.Consts;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import map.Map;
import map.Map1;
import model.Brick;
import model.ClientState;
import model.MapState;

/**
 *
 * @author tienanh
 */
public class GamePlay extends JPanel{
	private int width, height, padding = 50;
	private boolean isPlay, isInitNewGame, isGameOver = true;
	private boolean isEnemyDisconnected, isShow = false;
	private MapState mapState;
	
	private ClientState p1, p2;
	private ObjectInputStream inp;
	private ObjectOutputStream out;
	
	ImageIcon disconnectedGif;
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
					Brick curBrick = this.mapState.getBricks()[i*12 + j];
					if ( curBrick.getIsDisplay()){
						g.setColor(Color.YELLOW);
						g.fillRect(curBrick.getX(), curBrick.getY(), Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT);
					}
				}
			}

			// Line separate 
			g.setColor(Color.WHITE);
			g.drawLine(Consts.GAMPLAY_WIDTH, 0, Consts.GAMPLAY_WIDTH, Consts.GAMPLAY_HEIGHT);
			
			// Menu
			// Point p2
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.PLAIN, 20));
			g.drawString(p2.getName() + ": " + p2.getPoint(), Consts.GAMPLAY_WIDTH + 50, 50);

			// Disconnected
			if (p2.isSocketClose){
				disconnectedGif.paintIcon(this, g, Consts.GAMPLAY_WIDTH + 50, 80);
				g.setColor(Color.YELLOW);
				g.drawString("Disconnected", Consts.GAMPLAY_WIDTH + 110, 110);
			}
			
			// Line separate
			g.setColor(Color.WHITE);
			g.drawLine(Consts.GAMPLAY_WIDTH, Consts.GAMPLAY_HEIGHT / 2, Consts.SCREEN_WIDTH, Consts.GAMPLAY_HEIGHT / 2);

			// Point p1
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.PLAIN, 20));
			g.drawString(p1.getName() + ": " + p1.getPoint(), Consts.GAMPLAY_WIDTH + 50, Consts.GAMPLAY_HEIGHT / 2 + 50);
			
			// Disconnected
			if (p1.isSocketClose){
				disconnectedGif.paintIcon(this, g, Consts.GAMPLAY_WIDTH + 50,  Consts.GAMPLAY_HEIGHT / 2 + 80);
				g.setColor(Color.YELLOW);
				g.drawString("Disconnected", Consts.GAMPLAY_WIDTH + 110, Consts.GAMPLAY_HEIGHT / 2 + 110);
			}
			// Pause BTN 
			g.setColor(Color.RED);
			g.drawRect(Consts.GAMPLAY_WIDTH + 20, Consts.GAMPLAY_HEIGHT - 100, 150, 50);
			g.drawString(isInitNewGame ? "Play" :
				isPlay ? "Pause" : "UnPause", Consts.GAMPLAY_WIDTH + 20 + (isInitNewGame ? 45 : isPlay ? 45 : 30), Consts.GAMPLAY_HEIGHT - 100 + 32);

			// Pause BTN 
			g.setColor(Color.RED);
			g.drawRect(Consts.GAMPLAY_WIDTH + 20 + 150  + 50, Consts.GAMPLAY_HEIGHT - 100, 150, 50);
			g.drawString("Exit", Consts.GAMPLAY_WIDTH + 20 + 150  + 50 + 50, Consts.GAMPLAY_HEIGHT - 100 + 32);

			if (isGameOver){
				g.setColor(Color.RED);
				g.setFont(new Font("serif", Font.PLAIN, 50));
				g.drawString("GAME OVER",Consts.GAMPLAY_WIDTH / 2 - 50*3, Consts.GAMPLAY_HEIGHT / 2);
			}
			g.dispose();
		}
	}
	
	public GamePlay(int width, int height, Socket socket) {
		initGif();
		isEnemyDisconnected = false;
		
		p1 = new ClientState();
		p2 = new ClientState();
		isPlay = true;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			inp = new ObjectInputStream(socket.getInputStream());
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
				this.mapState = (MapState)inp.readObject();
				isPlay = true;
				
				repaint();
				
				if ( System.getProperty("os.name").equals("Linux")){
					Toolkit.getDefaultToolkit().sync();
				}
			} catch (IOException ex) {
				Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
			} catch (ClassCastException ex){
				isEnemyDisconnected = true;
				repaint();
				
				if ( System.getProperty("os.name").equals("Linux")){
					Toolkit.getDefaultToolkit().sync();
				}
			}
		}
	}

	public void initNewGame (){
		isPlay = isGameOver = false;
		isInitNewGame = true;
		
		// Init Jpanel
		setSize(Consts.SCREEN_WIDTH, Consts.GAMPLAY_HEIGHT);
		setBackground(Color.BLACK);
		setFocusable(true);
		setVisible(true);
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
                        // Action Code
                        out.writeObject(Consts.BAR_MOVE);
                        // Send data
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

	private void initGif() {
		disconnectedGif = new ImageIcon(getClass().getResource("/data/image/loading_50x50.gif"));
	}
}