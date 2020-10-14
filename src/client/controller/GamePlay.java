/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import map.Map1;
import model.Ball;
import model.Bar;

/**
 *
 * @author tienanh
 */
public class GamePlay extends JPanel{
	private int width, height, padding = 50;
	private int menuWidth, menuHeight;
	private int delayTime = 7, point_p1 = 0, point_p2 = 0; 
	private boolean isPlay, isInitNewGame, isGameOver = true;
	private Timer timer;
	private Ball ball_p1, ball_p2;
	private Bar bar_p1, bar_p2;
	private Map1 map;
	private Menu menu;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); 
		
		// Ball p1
		g.setColor(Color.RED);
		g.fillOval(ball_p1.getX(), ball_p1.getY(), ball_p1.getRadius(), ball_p1.getRadius());
		
		// Bar p1
		g.setColor(Color.GREEN);
		g.fillRect(bar_p1.getX(), bar_p1.getY(), bar_p1.getWidth(), bar_p1.getHeight());
		
		
		// Ball p2
		g.setColor(Color.BLUE);
		g.fillOval(ball_p2.getX(), ball_p2.getY(), ball_p2.getRadius(), ball_p2.getRadius());

		// Bar p2
		g.setColor(Color.GREEN);
		g.fillRect(bar_p2.getX(), bar_p2.getY(), bar_p2.getWidth(), bar_p2.getHeight());
		
		
		map.draw(g);
		
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
	
	

	public GamePlay(int width, int height) {
		this.width = width * 3/4;
		this.height = height;
		this.menuWidth = width * 1/4;
		this.menuHeight = height;
		initNewGame();
		// handle Bar move
		addKeyListener(this.handleBarMove());
		addMouseListener(this.handleClickEvent());
	}
	
	public void initNewGame (){
		isPlay = isGameOver = false;
		isInitNewGame = true;
		point_p1 = 0;
		point_p2 = 0;
		
		// Init ball and bar
		bar_p1 = new Bar(200, 20, 20,  this.width/2 - 200/2, this.height - 20 - padding);
		bar_p2 = new Bar(200, 20, 20,  this.width/2 - 200/2, padding - 20 );
		ball_p1 = new Ball(30, -1, -1, this.width/2 - 30/2, this.height - padding - bar_p1.getHeight() - 30);
		ball_p2 = new Ball(30, 1, 1, this.width/2 - 30/2, padding);
		
		
		// Init Jpanel
		setSize(this.width + this.menuWidth, this.height + this.menuHeight);
		setBackground(Color.BLACK);
		setFocusable(true);
		setVisible(true);
		
		// Init new Map
		map = new Map1(width, height);
		// Init Menu
		menu = new Menu();

		timer = new Timer(delayTime, handleRerenderEachTime());
		timer.start();
	}
	
	private KeyAdapter handleBarMove() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				boolean isRepaint = true;
				switch ( ke.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						bar_p1.setX(bar_p1.getX() > bar_p1.getSpeed() ? bar_p1.getX() - bar_p1.getSpeed() : 0);
						break;
					case KeyEvent.VK_RIGHT:
						bar_p1.setX(bar_p1.getX() + bar_p1.getWidth() < width - bar_p1.getSpeed() ? bar_p1.getX() + bar_p1.getSpeed() : width - bar_p1.getWidth());
						break;
						
					case KeyEvent.VK_A:
						bar_p2.setX(bar_p2.getX() > bar_p2.getSpeed() ? bar_p2.getX() - bar_p2.getSpeed() : 0);
						break;
					case KeyEvent.VK_D:
						bar_p2.setX(bar_p2.getX() + bar_p2.getWidth() < width - bar_p2.getSpeed() ? bar_p2.getX() + bar_p2.getSpeed() : width - bar_p2.getWidth());
						break;
					case KeyEvent.VK_ENTER:
						isPlay = !isPlay;
						isInitNewGame = false;
						break;
					case KeyEvent.VK_N:
						timer.stop();
						initNewGame();
						break;
					default:
						isRepaint = false;
				}
				if (isRepaint) {
					repaint();
					// For linux os
					if ( System.getProperty("os.name").equals("Linux") ){
						Toolkit.getDefaultToolkit().sync();
					}
				}
			}
		};
	}

	private ActionListener handleRerenderEachTime() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (isPlay) {
					ball_p1.setX(ball_p1.getX() + ball_p1.getSpeedX());
					ball_p1.setY(ball_p1.getY() + ball_p1.getSpeedY());
					
					ball_p2.setX(ball_p2.getX() + ball_p2.getSpeedX());
					ball_p2.setY(ball_p2.getY() + ball_p2.getSpeedY());
					handleCollision();
					repaint();
					// For linux os
					if ( System.getProperty("os.name").equals("Linux") ){
						Toolkit.getDefaultToolkit().sync();
					}
					
					if (map.isNoBrickLeft()) {
						timer.stop();
						isPlay = false;
					}
				}else if ( isGameOver ){
					repaint();
					if ( System.getProperty("os.name").equals("Linux") ){
						Toolkit.getDefaultToolkit().sync();
					}
					timer.stop();
				}
			}
		};
	}
	
	private void handleCollision() {
		switch ( checkIntersectWithEdges(ball_p1, bar_p1, true) ){
			case 1:
				ball_p1.setSpeedY(ball_p1.getSpeedY() * -1);
				break;
			case 2:
				ball_p1.setSpeedX(ball_p1.getSpeedX() * -1);
				break;
			case 3:
				ball_p1.setSpeedY(ball_p1.getSpeedY() * -1);
				break;
			case 4:
				ball_p1.setSpeedX(ball_p1.getSpeedX() * -1);
				break;
			default:
				break;
		}
		
		switch ( checkIntersectWithEdges(ball_p2, bar_p2, false) ){
			case 1:
				ball_p2.setSpeedY(ball_p2.getSpeedY() * -1);
				break;
			case 2:
				ball_p2.setSpeedX(ball_p2.getSpeedX() * -1);
				break;
			case 3:
				ball_p2.setSpeedY(ball_p2.getSpeedY() * -1);
				break;
			case 4:
				ball_p2.setSpeedX(ball_p2.getSpeedX() * -1);
				break;
			default:
				break;
		}
		
		boolean isTouchBrick = true;
		switch ( map.checkIntersectWithBrick(ball_p1) ) {
			case 1:
				ball_p1.setSpeedY(ball_p1.getSpeedY() * -1);
				break;
			case 2:
				ball_p1.setSpeedX(ball_p1.getSpeedX() * -1);
				break;
			case 3:
				ball_p1.setSpeedY(ball_p1.getSpeedY() * -1);
				break;
			case 4:
				ball_p1.setSpeedX(ball_p1.getSpeedX() * -1);
				break;
			default:
				isTouchBrick = false;
				break;
		}
		if (isTouchBrick) point_p1++;
		
		isTouchBrick = true;
		switch ( map.checkIntersectWithBrick(ball_p2) ) {
			case 1:
				ball_p2.setSpeedY(ball_p2.getSpeedY() * -1);
				break;
			case 2:
				ball_p2.setSpeedX(ball_p2.getSpeedX() * -1);
				break;
			case 3:
				ball_p2.setSpeedY(ball_p2.getSpeedY() * -1);
				break;
			case 4:
				ball_p2.setSpeedX(ball_p2.getSpeedX() * -1);
				break;
			default:
				isTouchBrick = false;
				break;
		}
		if (isTouchBrick) point_p2++;
		checkGameOver();
	}
	
	// Ball be "touch" to the ...
	// [
	//	-1 => no collision, 
	//	1 => top, 
	//	2 => right, 
	//	3 => bottom,
	//	4 => left 
	// ]	
	private int checkIntersectWithEdges (Ball ball, Bar bar, boolean isBarOnBottom) {
		if (isBarOnBottom){
			// bottom Bar 
			if ( ( ball.getX() >= bar.getX() && bar.getX() <= bar.getX() + bar.getWidth() ) &&
				( ball.getY() + ball.getRadius() >= bar.getY() && ball.getY() + ball.getRadius() <= bar.getY() + bar.getHeight() ) ){
				   ball.setY(bar.getY() - ball.getRadius());
				   return 3;
			}
			// Top edge
			if ( ball.getY() <= 0 ){
				ball.setY(0);
				return 1;
			}
		}
		else{
			// top Bar 
			if ( ( ball.getX() >= bar.getX() && ball.getX() <= bar.getX() + bar.getWidth() ) &&
				( ball.getY() <= bar.getY() + bar.getHeight() && ball.getY() >= bar.getY() ) ){
				   ball.setY(bar.getY() + bar.getHeight() + 1);
				   return 1;
			}
			// Bottom edge
			if ( ball.getY() + ball.getRadius() >= height ){
				ball.setY(height - ball.getRadius());
				return 3;
			}
		}
		
		// Left edge
		if ( ball.getX() <= 0 ) {
			ball.setX(0);
			return 4;
		}
		
		// Right edge
		if ( ball.getX() + ball.getRadius() >= width ){
			ball.setX(width - ball.getRadius());
			return 2;
		}
		// no collision
		return -1;
	}
	
	private void checkGameOver () {
		if (ball_p1.getY() >= height - bar_p1.getHeight() - padding + 1) {
			isGameOver = true;
			isPlay = false;
			return;
		}
		if (ball_p2.getY()  <= bar_p2.getY() + 1) {
			isGameOver = true;
			isPlay = false;
			return;
		}
	} 
	
	public int getPoint() {
		return point_p1;
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