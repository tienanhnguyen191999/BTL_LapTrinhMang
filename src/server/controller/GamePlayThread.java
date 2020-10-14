/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import javax.swing.Timer;
import map.Map1;
import model.Ball;
import model.Bar;

/**
 *
 * @author tienanh
 */
public class GamePlayThread extends Thread{
	private int width, height, paddingBottom = 50;
	private int menuWidth, menuHeight;
	private int delayTime = 1, point = 0; 
	private boolean isPlay, isGameOver = true;
	private Ball ball_p1, ball_p2;
	private Bar bar_p1, bar_p2;
	private Map1 map;
	
	@Override
	public void run() {
		super.run(); //To change body of generated methods, choose Tools | Templates.
	}
}
