/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameplay.develop;

import client.controller.GamePlay;
import consts.Consts;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import map.*;
import model.Ball;
import model.Bar;
import model.Brick;
import model.ClientState;
import server.controller.ClientThread;

/**
 *
 * @author tienanh
 */
public class TestMap extends JPanel {

	private ClientState p1, p2, p3;
	private Map map;
	private boolean isPlay;
	private ImageIcon itemBigBallIcon;
	
	private Timer timer;

	@Override
	protected void paintComponent(Graphics grphcs) {
		super.paintComponent(grphcs); //To change body of generated methods, choose Tools | Templates.
		this.drawBasicState(grphcs);
	}

	public TestMap(Map map) {
		isPlay = true;
		this.map = map;
		p1 = new ClientState();
		p2 = new ClientState();
		p3 = new ClientState();
		int padding = 20;

		itemBigBallIcon = new ImageIcon(getClass().getResource("/data/image/test_25x25.png"));
		
		
		// Init p1, p2
		p1.setPoint(0);
		// Init ball and bar
		p1.setBar(new Bar(
			Consts.BAR_WIDTH,
			Consts.BAR_HEIGHT,
			Consts.BAR_SPEED,
			Consts.GAMPLAY_WIDTH / 2 - Consts.BAR_WIDTH / 2,
			Consts.GAMPLAY_HEIGHT - Consts.BAR_HEIGHT - padding)
		);

		p1.setBall(new Ball(
			Consts.BALL_RADIUS, 3, -4,
			Consts.GAMPLAY_WIDTH / 2 - Consts.BALL_RADIUS / 2,
			Consts.GAMPLAY_HEIGHT - padding - Consts.BAR_HEIGHT - Consts.BALL_RADIUS)
		);
		p1.getBall().setColor(Color.RED);

		p2.setBar(new Bar(
			Consts.BAR_WIDTH,
			Consts.BAR_HEIGHT,
			Consts.BAR_SPEED,
			Consts.GAMPLAY_WIDTH / 2 - Consts.BAR_WIDTH / 2,
			padding)
		);
		p2.setBall(new Ball(
			Consts.BALL_RADIUS, 4, 4,
			Consts.GAMPLAY_WIDTH / 2 - Consts.BALL_RADIUS / 2,
			padding + Consts.BAR_HEIGHT)
		);
		p2.getBall().setColor(Color.BLUE);

		// Init Jpanel
		setSize(Consts.SCREEN_WIDTH, Consts.GAMPLAY_HEIGHT);
		setPreferredSize(new Dimension(Consts.SCREEN_WIDTH, Consts.GAMPLAY_HEIGHT));
		setBackground(Color.BLACK);
		setFocusable(true);
		setVisible(true);

		addKeyListener(this.handleBarMove());
		timer = new Timer(20, handleRerenderEachTime());
		timer.start();
	}

	private void handleCollision() {
	
		for (int i = 1 ; i <= 2; i++) {
			Ball ball;
			Bar bar;
			if (i == 1) {
				ball = p1.getBall();
				bar = p1.getBar();
			} else {
				ball = p2.getBall();
				bar = p2.getBar();
			} 
			boolean isP1 = true;
			// Check intersect with Edges
			if (ball != null) {
				switch (checkIntersectWithEdges(ball, bar, isP1)) {
					case 1:
						ball.setSpeedY(ball.getSpeedY() * -1);
						break;
					case 2:
						ball.setSpeedX(ball.getSpeedX() * -1);
						break;
					case 3:
						ball.setSpeedY(ball.getSpeedY() * -1);
						break;
					case 4:
						ball.setSpeedX(ball.getSpeedX() * -1);
						break;
					default:
						break;
				}

				// Check intersect with bricks
				boolean isTouchBrick = true;
				switch (map.checkIntersectWithBrick(ball, isP1)) {
					case 1:
						ball.setSpeedY(ball.getSpeedY() * -1);
						break;
					case 2:
						ball.setSpeedX(ball.getSpeedX() * -1);
						break;
					case 3:
						ball.setSpeedY(ball.getSpeedY() * -1);
						break;
					case 4:
						ball.setSpeedX(ball.getSpeedX() * -1);
						break;
					default:
						isTouchBrick = false;
						break;
				}

		}
		
		}
	}

	private int checkIntersectWithEdges(Ball ball, Bar bar, boolean isBarOnBottom) {
		if (isBarOnBottom) {
			// bottom Bar 
			if ((ball.getX() >= bar.getX() && bar.getX() <= bar.getX() + bar.getWidth())
				&& (ball.getY() + ball.getRadius() >= bar.getY() && ball.getY() + ball.getRadius() <= bar.getY() + bar.getHeight())) {
				ball.setY(bar.getY() - ball.getRadius());
				return 3;
			}
			// Top edge
			if (ball.getY() <= 0) {
				ball.setY(0);
				return 1;
			}
		} else {
			// top Bar 
			if ((ball.getX() >= bar.getX() && ball.getX() <= bar.getX() + bar.getWidth())
				&& (ball.getY() <= bar.getY() + bar.getHeight() && ball.getY() >= bar.getY())) {
				ball.setY(bar.getY() + bar.getHeight() + 1);
				return 1;
			}
			// Bottom edge
			if (ball.getY() + ball.getRadius() >= Consts.GAMPLAY_HEIGHT) {
				ball.setY(Consts.GAMPLAY_HEIGHT - ball.getRadius());
				return 3;
			}
		}

		// Left edge
		if (ball.getX() <= 0) {
			ball.setX(0);
			return 4;
		}

		// Right edge
		if (ball.getX() + ball.getRadius() >= Consts.GAMPLAY_WIDTH) {
			ball.setX(Consts.GAMPLAY_WIDTH - ball.getRadius());
			return 2;
		}
		// no collision
		return -1;
	}

	private KeyAdapter handleBarMove() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				boolean isRepaint = true;
				switch (ke.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						p1.getBar().setX(
							p1.getBar().getX()
							> p1.getBar().getSpeed()
							? p1.getBar().getX()
							- p1.getBar().getSpeed() : 0
						);
						break;
					case KeyEvent.VK_RIGHT:
						p1.getBar().setX(
							p1.getBar().getX()
							+ p1.getBar().getWidth()
							< consts.Consts.SCREEN_WIDTH * 3 / 4
							- p1.getBar().getSpeed()
							? p1.getBar().getX()
							+ p1.getBar().getSpeed()
							: consts.Consts.SCREEN_WIDTH * 3 / 4
							- p1.getBar().getWidth()
						);
						break;

					case KeyEvent.VK_ENTER:
						isPlay = !isPlay;
						break;
					default:
						isRepaint = false;
				}
			}
		};
	}

	public ActionListener handleRerenderEachTime() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (isPlay) {
					p1.getBall().setX(p1.getBall().getX() + p1.getBall().getSpeedX());
					p1.getBall().setY(p1.getBall().getY() + p1.getBall().getSpeedY());

					p2.getBall().setX(p2.getBall().getX() + p2.getBall().getSpeedX());
					p2.getBall().setY(p2.getBall().getY() + p2.getBall().getSpeedY());
					handleCollision();
					customRepaint();
				}
			}

		};
	}

	public void customRepaint() {
		repaint();
		// For linux system.
		if (System.getProperty("os.name").equals("Linux")) {
			Toolkit.getDefaultToolkit().sync();
		}
	}

	public void drawBasicState(Graphics g) {
		// Ball p1
		if (p1.getBall() != null) {
			g.setColor(p1.getBall().getColor());
			g.fillOval(p1.getBall().getX(), p1.getBall().getY(), p1.getBall().getRadius(), p1.getBall().getRadius());
		}
		
		
		itemBigBallIcon.paintIcon(this, g, Consts.GAMPLAY_WIDTH + 50, 80);
		
// Bar p1
		g.setColor(Color.GREEN);
		g.fillRect(p1.getBar().getX(), p1.getBar().getY(), p1.getBar().getWidth(), p1.getBar().getHeight());

		// Ball p2
		if (p2.getBall() != null) {
			g.setColor(p2.getBall().getColor());
			g.fillOval(p2.getBall().getX(), p2.getBall().getY(), p2.getBall().getRadius(), p2.getBall().getRadius());
		}

		// Bar p2	
		g.setColor(Color.GREEN);
		g.fillRect(p2.getBar().getX(), p2.getBar().getY(), p2.getBar().getWidth(), p2.getBar().getHeight());

		// Draw brick
		for (int i = 0; i < map.getMapState().getRow(); i++) {
			for (int j = 0; j < map.getMapState().getCol(); j++) {
				Brick curBrick = this.map.getMapState().getBricks()[i * map.getMapState().getCol() + j];
				if (curBrick.getIsDisplay()) {
					g.setColor(Color.YELLOW);
					g.fillRect(curBrick.getX(), curBrick.getY(), Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT);
				}
			}
		}
		// Line separate 
		g.setColor(Color.WHITE);
		g.drawLine(Consts.GAMPLAY_WIDTH, 0, Consts.GAMPLAY_WIDTH, Consts.GAMPLAY_HEIGHT);
	}

	public void capturePreviewImage(int index) {
		try {
			// Capture preview Image
			BufferedImage img = new BufferedImage(Consts.GAMPLAY_WIDTH, Consts.GAMPLAY_HEIGHT, BufferedImage.TYPE_INT_RGB);
			this.paint(img.getGraphics());
			File outputfile = new File("src/data/mapPreview/map-" + index + ".png");
			ImageIO.write(img, "png", outputfile);
		} catch (IOException ex) {
			Logger.getLogger(TestMap.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {

		// MainFrame
		JFrame mainFrame = new JFrame("Brick Breaker");
		mainFrame.getContentPane().setPreferredSize(new Dimension(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		mainFrame.pack();

		// Change Bellow
		Map map = new Map9();
		int index = 9;
		// Change Above

		// GamePlay
		TestMap newMap = new TestMap(map);
		mainFrame.add(newMap);
		if (!System.getProperty("os.name").equals("Linux")) {
			mainFrame.requestFocus();
		}

//		newMap.capturePreviewImage(index);
	}
}
