/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import javax.swing.Timer;
import model.Ball;
import model.Bar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientState;
import consts.Consts;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
import map.*;

/**
 *
 * This class need 2 player to play
 * 2 socket connection required
 */
public class GamePlayThread extends Thread{
	private int padding = 20;
	private int delayTime, speed;
	private boolean isPlay, isInitNewGame;
	private Map map;
	
	private ArrayList<ClientThread> arr_player;
	private Timer timer;
	
	GamePlayThread () {
		arr_player = new ArrayList<ClientThread>();
	}
	
	public void initNewGame (){
		isPlay = false;
		isInitNewGame = true;
		delayTime = (11 - speed) * 2 + 5;
		// Init new Map
		timer = new Timer(delayTime, handleRerenderEachTime());
		
		// Init new player state
		boolean isPlayer_1 = true;
		for (ClientThread client : arr_player){
			client.getClientState().setPoint(0);
			Color ballColor = client.getClientState().getBall().getColor();
			if (isPlayer_1){
				// Init ball and bar
				isPlayer_1 = false;
				client.getClientState().setBar(new Bar(
					Consts.BAR_WIDTH, 
					Consts.BAR_HEIGHT, 
					Consts.BAR_SPEED,  
					Consts.GAMPLAY_WIDTH/2 - Consts.BAR_WIDTH / 2, 
					Consts.GAMPLAY_HEIGHT - Consts.BAR_HEIGHT - padding)
				);
				
				client.getClientState().setBall(new Ball(
					Consts.BALL_RADIUS, -1, -1, 
					Consts.GAMPLAY_WIDTH/2 - Consts.BALL_RADIUS / 2, 
					Consts.GAMPLAY_HEIGHT - padding - Consts.BAR_HEIGHT - Consts.BALL_RADIUS)
				);
			}else {
				client.getClientState().setBar(new Bar(
					Consts.BAR_WIDTH, 
					Consts.BAR_HEIGHT, 
					Consts.BAR_SPEED, 
					Consts.GAMPLAY_WIDTH/2 - Consts.BAR_WIDTH / 2,
					padding)
				);
				
				client.getClientState().setBall(new Ball(
					Consts.BALL_RADIUS, 1, 1,
					Consts.GAMPLAY_WIDTH/2 - Consts.BALL_RADIUS / 2, 
					padding + Consts.BAR_HEIGHT)
				);
			}
			client.getClientState().getBall().setColor(ballColor);
		}
	}
	
	public ActionListener handleRerenderEachTime () {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				// Check for connection lost
				int index = 0;
				for (ClientThread player : arr_player){
					if (player.getClientState().isSocketClose){
						try {
							ClientThread connectedPlayer = arr_player.get(index == 0 ? 1 : 0);
							// Set "lost connection" to other player
							connectedPlayer.getSocketIO().getOutput().reset();
							connectedPlayer.getSocketIO().getOutput().writeObject(Consts.OTHER_PLAYER_LOST_CONNECTION);
							connectedPlayer.getSocketIO().getOutput().writeObject(arr_player.get(0).getClientState());
							connectedPlayer.getSocketIO().getOutput().writeObject(arr_player.get(1).getClientState());
							connectedPlayer.getSocketIO().getOutput().writeObject(map.getMapState());
						} catch (IOException ex) {

						}
						isPlay = false;
						timer.stop();
					}
					index++;
				}

				if (isPlay || isInitNewGame){
					isInitNewGame = false;
					// Update ball move
					for (ClientThread player : arr_player){
						player.getClientState().getBall().setX(player.getClientState().getBall().getX() + player.getClientState().getBall().getSpeedX());
						player.getClientState().getBall().setY(player.getClientState().getBall().getY() + player.getClientState().getBall().getSpeedY());
					}
					handleCollision();
					
					// Send current state of object to each client
					for (ClientThread player : arr_player){
						try {
							player.getSocketIO().getOutput().reset();
							player.getSocketIO().getOutput().writeObject(Consts.UPDATE_GAMEPLAY_STATE);
							player.getSocketIO().getOutput().writeObject(arr_player.get(0).getClientState());
							player.getSocketIO().getOutput().writeObject(arr_player.get(1).getClientState());
							player.getSocketIO().getOutput().writeObject(map.getMapState());
						} catch (IOException ex) {
							player.getClientState().isSocketClose = true;
						}
					}
					
					
					// Check game over
					try {
						if ( checkGameOver(arr_player.get(0).getClientState(), true) ){
							// P1 Lose
							arr_player.get(0).getSocketIO().getOutput().writeObject(Consts.GAME_LOSE);
							arr_player.get(1).getSocketIO().getOutput().writeObject(Consts.GAME_WIN);
							isPlay = false;
//							timer.stop();
						}else if ( checkGameOver(arr_player.get(1).getClientState(), false) ){
							// P2 Lose
							arr_player.get(0).getSocketIO().getOutput().writeObject(Consts.GAME_WIN);
							arr_player.get(1).getSocketIO().getOutput().writeObject(Consts.GAME_LOSE);
							isPlay = false;
//							timer.stop();
						}
					
					
						if (map.isNoBrickLeft()) {
							if ( arr_player.get(0).getClientState().getPoint() > arr_player.get(1).getClientState().getPoint()){
								arr_player.get(0).getSocketIO().getOutput().writeObject(Consts.GAME_WIN);
								arr_player.get(1).getSocketIO().getOutput().writeObject(Consts.GAME_LOSE);
							} else {
								arr_player.get(0).getSocketIO().getOutput().writeObject(Consts.GAME_LOSE);
								arr_player.get(1).getSocketIO().getOutput().writeObject(Consts.GAME_WIN);
							}
							// Send WIN action code
							timer.stop();
							isPlay = false;
						} 
					} catch (IOException ex) {
						Logger.getLogger(GamePlayThread.class.getName()).log(Level.SEVERE, null, ex);
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
			if ( ball.getY() + ball.getRadius() >= Consts.GAMPLAY_HEIGHT ){
				ball.setY(Consts.GAMPLAY_HEIGHT - ball.getRadius());
				return 3;
			}
		}
		
		// Left edge
		if ( ball.getX() <= 0 ) {
			ball.setX(0);
			return 4;
		}
		
		// Right edge
		if ( ball.getX() + ball.getRadius() >= Consts.GAMPLAY_WIDTH ){
			ball.setX(Consts.GAMPLAY_WIDTH - ball.getRadius());
			return 2;
		}
		// no collision
		return -1;
	}
	
	private boolean checkGameOver (ClientState player, boolean isBarOnBottom) {
		return isBarOnBottom ?  player.getBall().getY() >= Consts.GAMPLAY_HEIGHT - player.getBar().getHeight() - padding + 1
							 :  player.getBall().getY() <= player.getBar().getY() + 1;
	} 
	
	public void addPlayterToRoom (ClientThread player){
		if (arr_player.size() < Consts.MAX_PLAYER){
			arr_player.add(player);
		}else {
			System.out.println("!!! Room reach maximum player !!!");
		}
	}
	
	@Override
	public void run() {	
	}

	public void startGame () {
        this.initNewGame();
		timer.start();
		
		// Counter before start ~ 5s
		for (int i = 10; i >= 0; i--){
			try {
				for (ClientThread client : arr_player){
					client.getSocketIO().getOutput().writeObject(Consts.COUNTER_BEFORE_START);
					client.getSocketIO().getOutput().writeObject(i);
				}
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(GamePlayThread.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(GamePlayThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		isPlay = true;
	}

    public void setMap(Map map) {
        this.map = map;
    }

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
