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
import java.util.Random;
import java.util.concurrent.TimeUnit;
import map.*;

/**
 *
 * This class need 2 player to play 2 socket connection required
 */
public class GamePlayThread extends Thread {
	private int padding = 20;
	private boolean isPlay, isInitNewGame;
	private boolean isSaveGameLoad = false;
	private Map map;

	private ArrayList<ClientThread> arr_player;
	private int speed, gameMode;
	private Timer timer;
	private Random rand;
	private ArrayList<Integer[]> directionPairs;

	GamePlayThread() {
		arr_player = new ArrayList<ClientThread>();
		rand = new Random();
		gameMode = Consts.TWO_BALL;
		
		directionPairs = new ArrayList<Integer[]>();
		initNewDirectionParis();
	}

	public void initNewGame() {
		isPlay = false;
		isInitNewGame = true;
		int delayTime = 30;
		// Init new Map
		timer = new Timer(delayTime, handleRerenderEachTime());

		// 2 ball mode init
		if (!isSaveGameLoad) {
			// Init new player state
			boolean isPlayer_1 = true;
			for (ClientThread client : arr_player) {
				client.getClientState().setPoint(0);
				Color ballColor = client.getClientState().getBall().getColor();
				if (isPlayer_1) {
					// Init ball and bar
					isPlayer_1 = false;
					client.getClientState().setBar(new Bar(
						Consts.BAR_WIDTH,
						Consts.BAR_HEIGHT,
						Consts.BAR_SPEED,
						Consts.GAMPLAY_WIDTH / 2 - Consts.BAR_WIDTH / 2,
						Consts.GAMPLAY_HEIGHT - Consts.BAR_HEIGHT - padding)
					);

					client.getClientState().setBall(new Ball(
						Consts.BALL_RADIUS, -1, -1,
						Consts.GAMPLAY_WIDTH / 2 - Consts.BALL_RADIUS / 2,
						Consts.GAMPLAY_HEIGHT - padding - Consts.BAR_HEIGHT - Consts.BALL_RADIUS)
					);
					changeBallDirection(client.getClientState().getBall());
				} else {
					client.getClientState().setBar(new Bar(
						Consts.BAR_WIDTH,
						Consts.BAR_HEIGHT,
						Consts.BAR_SPEED,
						Consts.GAMPLAY_WIDTH / 2 - Consts.BAR_WIDTH / 2,
						padding)
					);

					client.getClientState().setBall(new Ball(
						Consts.BALL_RADIUS, 1, 1,
						Consts.GAMPLAY_WIDTH / 2 - Consts.BALL_RADIUS / 2,
						padding + Consts.BAR_HEIGHT)
					);
					changeBallDirection(client.getClientState().getBall());
				}
				client.getClientState().getBall().setColor(ballColor);
			}
		}

		// 1 Ball mode init
		if (this.gameMode == Consts.ONE_BALL) {
			try {
				arr_player.get(1).getClientState().setBall(null); // Disable 1 ball;
				arr_player.get(0).getClientState().getBall().setColor(Color.BLUE);

				arr_player.get(1).getSocketIO().getOutput().writeObject(Consts.ONE_BALL);
				arr_player.get(0).getSocketIO().getOutput().writeObject(Consts.ONE_BALL);
			} catch (IOException ex) {
				Logger.getLogger(GamePlayThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public synchronized ActionListener handleRerenderEachTime() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// Check for connection lost
				int index = 0;
				for (ClientThread player : arr_player) {
					if (player.getClientState().isSocketClose) {
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

				if (isPlay || isInitNewGame) {
					isInitNewGame = false;
					// Update ball move
					for (ClientThread player : arr_player) {
						if (player.getClientState().getBall() != null) {
							player.getClientState().getBall().setX(player.getClientState().getBall().getX() + player.getClientState().getBall().getSpeedX());
							player.getClientState().getBall().setY(player.getClientState().getBall().getY() + player.getClientState().getBall().getSpeedY());
						}

					}
					handleCollision();

					// Send current state of object to each client
					for (ClientThread player : arr_player) {
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
						if (arr_player.get(0).getClientState().getBall() != null && checkGameOver(arr_player.get(0).getClientState(), true)) {
							// P1 Lose
							arr_player.get(0).getSocketIO().getOutput().writeObject(Consts.GAME_LOSE);
							arr_player.get(1).getSocketIO().getOutput().writeObject(Consts.GAME_WIN);
							isPlay = false;
						} else if (arr_player.get(1).getClientState().getBall() != null && checkGameOver(arr_player.get(1).getClientState(), false)) {
							// P2 Lose
							arr_player.get(0).getSocketIO().getOutput().writeObject(Consts.GAME_WIN);
							arr_player.get(1).getSocketIO().getOutput().writeObject(Consts.GAME_LOSE);
							isPlay = false;
						}

						if (map.isNoBrickLeft()) {
							if (arr_player.get(0).getClientState().getPoint() > arr_player.get(1).getClientState().getPoint()) {
								arr_player.get(0).getSocketIO().getOutput().writeObject(Consts.GAME_WIN);
								arr_player.get(1).getSocketIO().getOutput().writeObject(Consts.GAME_LOSE);
							} else {
								arr_player.get(0).getSocketIO().getOutput().writeObject(Consts.GAME_LOSE);
								arr_player.get(1).getSocketIO().getOutput().writeObject(Consts.GAME_WIN);
							}
							// Send WIN action code
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
		for (ClientThread player : arr_player) {
			Ball ball = player.getClientState().getBall();
			Bar bar = player.getClientState().getBar();

			// Check intersect with Edges
			if (ball != null && !isInitNewGame) {
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
				switch (map.checkIntersectWithBrick(ball)) {
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

				// This below part for 1 ball mode 
				if (this.gameMode == Consts.ONE_BALL) {
					// Check is touch opponent bar
					checkIntersectWithOpponentBar(ball, arr_player.get(0).getClientState().getBar(), arr_player.get(1).getClientState().getBar(), isP1);
				}

				if (isTouchBrick) {
					player.getClientState().setPoint(player.getClientState().getPoint() + 1);
				}
			}
			isP1 = !isP1;
		}
	}

	private int checkIntersectWithEdges(Ball ball, Bar bar, boolean isBarOnBottom) {
		if (isBarOnBottom) {
			// bottom Bar 
			if ((ball.getX() >= bar.getX() && bar.getX() <= bar.getX() + bar.getWidth())
				&& (ball.getY() + ball.getRadius() >= bar.getY() && ball.getY() + ball.getRadius() <= bar.getY() + bar.getHeight())) {
				ball.setY(bar.getY() - ball.getRadius());
				changeBallDirection(ball);
				return Consts.BOTTOM;
			}
			// Top edge
			if (ball.getY() <= 0) {
				ball.setY(0);
				return Consts.TOP;
			}
			
		} else {
			// top Bar 
			if ((ball.getX() >= bar.getX() && ball.getX() <= bar.getX() + bar.getWidth())
				&& (ball.getY() <= bar.getY() + bar.getHeight() && ball.getY() >= bar.getY())) {
				ball.setY(bar.getY() + bar.getHeight() + 1);
				changeBallDirection(ball);
				return Consts.TOP;
			}
			// Bottom edge
			if (ball.getY() + ball.getRadius() >= Consts.GAMPLAY_HEIGHT) {
				ball.setY(Consts.GAMPLAY_HEIGHT - ball.getRadius());
				return Consts.BOTTOM;
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

	public void checkIntersectWithOpponentBar(Ball ball, Bar bar_p1, Bar bar_p2, boolean is_p1_ball) {
		if (is_p1_ball) {
			if (ball.getY() < bar_p2.getY() + bar_p2.getHeight() && ball.getX() > bar_p2.getX() && ball.getX() < bar_p2.getX() + bar_p2.getWidth() && ball.getSpeedY() < 0) {
				// Set Ball to P2
				this.arr_player.get(1).getClientState().setBall(ball);
				this.arr_player.get(1).getClientState().getBall().setColor(Color.GREEN);
				this.arr_player.get(1).getClientState().getBall().setSpeedY(this.arr_player.get(1).getClientState().getBall().getSpeedY() * -1);
				// Disable p1's ball
				this.arr_player.get(0).getClientState().setBall(null);
			}
		} else {
			if (ball.getY() + ball.getRadius() > bar_p1.getY() && ball.getX() > bar_p1.getX() && ball.getX() < bar_p1.getX() + bar_p1.getWidth() && ball.getSpeedY() > 0) {
				// Set Ball to P1
				this.arr_player.get(0).getClientState().setBall(ball);
				this.arr_player.get(0).getClientState().getBall().setColor(Color.BLUE);
				this.arr_player.get(0).getClientState().getBall().setSpeedY(this.arr_player.get(0).getClientState().getBall().getSpeedY() * -1);
				// Disable p2's ball
				this.arr_player.get(1).getClientState().setBall(null);
			}
		}
	}

	private boolean checkGameOver(ClientState player, boolean isBarOnBottom) {
		return isBarOnBottom ? player.getBall().getY() >= Consts.GAMPLAY_HEIGHT - player.getBar().getHeight() - padding + 1
			: player.getBall().getY() <= player.getBar().getY() + 1;
	}

	public void addPlayterToRoom(ClientThread player) {
		arr_player.add(player);
	}

	@Override
	public void run() {
	}

	public void startGame() {
		this.initNewGame();
		timer.start();

		// Counter before start ~ 5s
		for (int i = 10; i >= 0; i--) {
			try {
				for (ClientThread client : arr_player) {
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
	
	public void changeBallDirection (Ball curBall) {
		int index = rand.nextInt(directionPairs.size());
		// Set new ball direction
		if (curBall.getSpeedX() > 0 ) curBall.setSpeedX(directionPairs.get(index)[0]);
		else curBall.setSpeedX(directionPairs.get(index)[0] * -1);
		if (curBall.getSpeedY() > 0 ) curBall.setSpeedY(directionPairs.get(index)[1]);
		else curBall.setSpeedY(directionPairs.get(index)[1] * -1);
	}

	public void pauseGame() {
		isPlay = false;

	}

	public void playGame() {
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
		initNewDirectionParis();
	}
	
	public void initNewDirectionParis () {
		int start = 1;
		// Prepare
		for (int i = start; i <= speed; i++){
			for (int j = i; j <= speed; j++){
				double condition_to_check = speed - Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
				if ( condition_to_check <= 1 && condition_to_check <= 0) {
					directionPairs.add(new Integer[]{i, j});
					if (i != j) {
						directionPairs.add(new Integer[]{j, i});
					}
				}
			}
		}
	}

	public ArrayList<ClientThread> getArr_player() {
		return arr_player;
	}

	public Map getMap() {
		return map;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public void setIsSaveGameLoad(boolean isSaveGameLoad) {
		this.isSaveGameLoad = isSaveGameLoad;
	}
}
