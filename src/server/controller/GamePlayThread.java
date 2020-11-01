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
import map.*;

/**
 *
 * This class need 2 player to play
 * 2 socket connection required
 */
public class GamePlayThread extends Thread{
	private int padding = 50;
	private int delayTime = 20;
	private boolean isPlay, isGameOver, isInitNewGame;
	private Map map;
	
	private ArrayList<ClientThread> arr_player;
	private Timer timer;
	
	GamePlayThread () {
		arr_player = new ArrayList<ClientThread>();
	}
	
	public void initNewGame (){
		isPlay = isGameOver = false;
		isInitNewGame = true;
		
		// Init new Map
		timer = new Timer(delayTime, handleRerenderEachTime());
		
		// Init new player state
		boolean isPlayer_1 = true;
		for (ClientThread client : arr_player){
			client.getClientState().setPoint(0);
			if (isPlayer_1){
				// Init ball and bar
				isPlayer_1 = false;
				client.getClientState().setName("TienAnh");
				client.getClientState().setBar(new Bar(200, 20, 20,  Consts.GAMPLAY_WIDTH/2 - 200/2, Consts.GAMPLAY_HEIGHT - 20 - padding));
				client.getClientState().setBall(new Ball(30, -1, -1, Consts.GAMPLAY_WIDTH/2 - 30/2, Consts.GAMPLAY_HEIGHT - padding - client.getClientState().getBar().getHeight() - 30));
			}else {
				client.getClientState().setName("ThanhTrung");
				client.getClientState().setBar(new Bar(200, 20, 20,  Consts.GAMPLAY_WIDTH/2 - 200/2, padding - 20 ));
				client.getClientState().setBall(new Ball(30, 1, 1, Consts.GAMPLAY_WIDTH/2 - 30/2, padding));
			}
			
		}
	}
	
	public ActionListener handleRerenderEachTime () {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				int index = 0;
				for (ClientThread player : arr_player){
					if (player.getClientState().isSocketClose){
						try {
							ClientThread connectedPlayer = arr_player.get(index == 0 ? 1 : 0);
							// Set "lost connection" to other player
							connectedPlayer.getSocketIO().getOutput().reset();
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
							player.getSocketIO().getOutput().reset();
							player.getSocketIO().getOutput().writeObject(arr_player.get(0).getClientState());
							player.getSocketIO().getOutput().writeObject(arr_player.get(1).getClientState());
							player.getSocketIO().getOutput().writeObject(map.getMapState());
						} catch (IOException ex) {
							player.getClientState().isSocketClose = true;
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
	
	private void checkGameOver (ClientState player, boolean isBarOnBottom) {
		if (isBarOnBottom && player.getBall().getY() >= Consts.GAMPLAY_HEIGHT - player.getBar().getHeight() - padding + 1) {
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
	
	public void addPlayterToRoom (ClientThread player){
		if (arr_player.size() < Consts.MAX_PLAYER){
			arr_player.add(player);
			System.out.println("PLayer number: " + arr_player.size());
		}else {
			System.out.println("!!! Room reach maximum player !!!");
		}
	}
	
	@Override
	public void run() {	
	}

	public void startGame () {
        this.initNewGame();
		isPlay = true;
		timer.start();
	}

    public void setMap(Map map) {
        this.map = map;
    }
}
