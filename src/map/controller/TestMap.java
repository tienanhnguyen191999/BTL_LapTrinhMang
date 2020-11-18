/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.controller;

import client.controller.GamePlay;
import consts.Consts;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import map.*;
import model.Ball;
import model.Bar;
import model.Brick;
import model.ClientState;

/**
 *
 * @author tienanh
 */
public class TestMap extends JPanel {

	private ClientState p1, p2;
	private Map map;

	@Override
	protected void paintComponent(Graphics grphcs) {
		super.paintComponent(grphcs); //To change body of generated methods, choose Tools | Templates.
		this.drawBasicState(grphcs);
	}

	public TestMap(Map map) {
		this.map = map;
		p1 = new ClientState();
		p2 = new ClientState();
		int padding = 20;

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
			Consts.BALL_RADIUS, -1, -1,
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
			Consts.BALL_RADIUS, 1, 1,
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
	}

	public void drawBasicState(Graphics g) {
		// Ball p1
		if (p1.getBall() != null) {
			g.setColor(p1.getBall().getColor());
			g.fillOval(p1.getBall().getX(), p1.getBall().getY(), p1.getBall().getRadius(), p1.getBall().getRadius());
		}

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
		mainFrame.requestFocus();
		
		newMap.capturePreviewImage(index);

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
}
