/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author tienanh
 */
public class Menu{

	/**
	 *
	 */
	public Menu() {
	}

	/**
	 *
	 * @param g
	 * @param width
	 * @param height
	 * @param point_p1
	 * @param point_p2
	 * @param isPlay
	 * @param isInitNewGame
	 */
	public void draw(Graphics g, int width, int height, int point_p1, int point_p2, boolean isPlay, boolean isInitNewGame){
		// Point p2
		g.setColor(Color.RED);
		g.setFont(new Font("serif", Font.PLAIN, 20));
		g.drawString("Point_p2: " + point_p2, width * 3 + 50, 50);
		
		// Line separate
		g.setColor(Color.WHITE);
		g.drawLine(width * 3, height / 2, width * 4, height / 2);
		
		// Point p2
		g.setColor(Color.RED);
		g.setFont(new Font("serif", Font.PLAIN, 20));
		g.drawString("Point_p1: " + point_p1, width * 3 + 50, height / 2 + 50);
		
		// Pause BTN 
		g.setColor(Color.RED);
		g.drawRect(width * 3 + 20, height - 100, 150, 50);
		g.drawString(isInitNewGame ? "Play" :
			isPlay ? "Pause" : "UnPause", width * 3 + 20 + (isInitNewGame ? 45 : isPlay ? 45 : 30), height - 100 + 32);
		
		// Pause BTN 
		g.setColor(Color.RED);
		g.drawRect(width * 3 + 20 + 150  + 50, height - 100, 150, 50);
		g.drawString("Exit", width * 3 + 20 + 150  + 50 + 50, height - 100 + 32);
	}
}
