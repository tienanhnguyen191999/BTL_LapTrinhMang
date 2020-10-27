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
import consts.Consts;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientState;
import consts.Consts;

/**
 *
 * This class need 2 player to play
 * 2 socket connection required
 */
public class GamePlayThread extends Thread{
	private int width, height, padding = 50;
	private int menuWidth, menuHeight;
	private int delayTime = 20;
	private boolean isPlay, isGameOver, isInitNewGame;
	private Map1 map;
	
	private ArrayList<ClientThread> arr_player;
	private int times = 0;
	private Timer timer;
	
	GamePlayThread () {
		arr_player = new ArrayList<ClientThread>();
		this.width = Consts.SCREEN_WIDTH * 3/4;
		this.height = Consts.SCREEN_HEIGHT;
		this.menuWidth = Consts.SCREEN_WIDTH * 1/4;
		this.menuHeight = Consts.SCREEN_HEIGHT;
	}
	
	public void initNewGame (){
		isPlay = isGameOver = false;
		isInitNewGame = true;
		
		// Init new Map
		map = new Map1(width, height);
		timer = new Timer(delayTime, handleRerenderEachTime());
		
		// Init new player state
		boolean isPlayer_1 = true;
		for (ClientThread client : arr_player){
			client.setClientState(new ClientState());
			client.getClientState().setPoint(0);
			if (isPlayer_1){
				// Init ball and bar
				isPlayer_1 = false;
				client.getClientState().setBar(new Bar(200, 20, 20,  this.width/2 - 200/2, this.height - 20 - padding));
				client.getClientState().setBall(new Ball(30, -1, -1, this.width/2 - 30/2, this.height - padding - client.getClientState().getBar().getHeight() - 30));
			}else {
				client.getClientState().setBar(new Bar(200, 20, 20,  this.width/2 - 200/2, padding - 20 ));
				client.getClientState().setBall(new Ball(30, 1, 1, this.width/2 - 30/2, padding));
			}
			
		}
	}
	
	/**
	 *
	 * @return
	 */
	public ActionListener handleRerenderEachTime () {
		for (ClientThread player : arr_player){
			if (player.getSocket().isClosed()){
				isPlay = false;
				timer.stop();
			}
		}
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
//				System.out.println("isPlay: " + isPlay);
//				System.out.println("ISGAMEOVER: " + isGameOver);
				if (isPlay){
					// Update ball move
					for (ClientThread player : arr_player){
						player.getClientState().getBall().setX(player.getClientState().getBall().getX() + player.getClientState().getBall().getSpeedX());
						player.getClientState().getBall().setY(player.getClientState().getBall().getY() + player.getClientState().getBall().getSpeedY());
					}
					handleCollision();
					if (map.isNoBrickLeft()) {
						timer.stop();
						isPlay = false;
					}
					
					// Send current state of object to each client
					for (ClientThread player : arr_player){
						try {
							player.getObjectOutput().reset();
							player.getObjectOutput().writeObject(arr_player.get(0).getClientState());
							player.getObjectOutput().writeObject(arr_player.get(1).getClientState());
							player.getObjectOutput().writeObject(map.getBricks());
							System.out.println(player.getName() + ":\n " + player.getClientState());
						} catch (IOException ex) {
							Logger.getLogger(GamePlayThread.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
				}
				
			}
		};
	}
	
		
	private void handleCollision() {
		boolean isP1 = true;
		for (ClientThread player : arr_player){
			Ball ball = player.getClientState().getBall();
			Bar bar = player.getClientState().getBar();
			
			// Check intersect with Edges
			switch ( checkIntersectWithEdges(ball, bar, isP1) ){
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
			switch ( map.checkIntersectWithBrick(ball) ) {
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
			if (isTouchBrick) player.getClientState().setPoint( player.getClientState().getPoint() + 1 );
			
			checkGameOver(player.getClientState(), isP1);
			
			isP1 = !isP1;
		}
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
	
	private void checkGameOver (ClientState player, boolean isBarOnBottom) {
		if (isBarOnBottom && player.getBall().getY() >= height - player.getBar().getHeight() - padding + 1) {
			isGameOver = true;
			isPlay = false;
			return;
		}
		if (!isBarOnBottom && player.getBall().getY()  <= player.getBar().getY() + 1) {
			isGameOver = true;
			isPlay = false;
			return;
		}
	} 
	
	
	/**
	 *
	 * @param player
	 */
	public void addPlayterToRoom (ClientThread player){
		if (arr_player.size() < Consts.MAX_PLAYER){
			arr_player.add(player);
			System.out.println("PLayer number: " + arr_player.size());
		}else {
			System.out.println("!!! Room reach maximum player !!!");
		}
	}
	
	/**
	 *
	 */
	@Override
	public void run() {	
	}
	
	/**
	 *
	 */
	public void startGame () {
		timer.start();
	}
	
	/**
	 *
	 * @return
	 */
	public boolean isPlayAble () {
		if  ( this.arr_player.size() != 2 ) return false;
		this.initNewGame();
		isPlay = true;
		return true;
	}
}
