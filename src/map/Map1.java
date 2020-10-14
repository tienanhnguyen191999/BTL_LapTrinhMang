package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import model.Ball;
import model.Brick;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tienanh
 */

/*
	   1200
	**********
	********** 1000
	**********
*/
public class Map1{
	private int TOP = 1, RIGHT = 2, BOTTOM = 3, LEFT = 4;
	public String[] translate = {"","TOP", "RIGHT", "BOTTOM", "LEFT"};
	private int totalBrick;
	private int widthBrick, heightBrick;
	private int padding;
	private Color colorBrick;
	private Brick[] bricks;
	private int gamePlayWidth, gameplayHeight;
	public Map1 (int width,int height) {
		gamePlayWidth = width;
		gameplayHeight = height;
		widthBrick = 80;
		heightBrick = 30;
		padding = 10;
		colorBrick = Color.YELLOW;
		bricks = new Brick[3 * 12];
		int totalBrickHeight = 3 * heightBrick + 2 * padding;
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 0; j < 12; j++){
				Brick tmp = new Brick(widthBrick, heightBrick, 65 + j*widthBrick + j*padding, (gameplayHeight - totalBrickHeight )/ 2 + i*heightBrick + i*padding);
//				if (j % 3 == 0 ) tmp.setIsDisplay(false);
				bricks[i*12 + j] = tmp;
			}
			
		}
	}
	
	public boolean isNoBrickLeft () {
		return bricks.length == 0;
	}
	
	public void draw (Graphics g) {
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 0; j < 12; j++){
				Brick curBrick = bricks[i*12 + j];
				if ( curBrick.getIsDisplay()){
					g.setColor(colorBrick);
					g.fillRect(curBrick.getX(), curBrick.getY(), widthBrick, heightBrick);
				}
			}
		}
	}

	public int checkIntersectWithBrick (Ball ball) {
		Rectangle rectball  = new Rectangle(ball.getX(), ball.getY(), ball.getRadius(), ball.getRadius());
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 0; j < 12; j++){
				if ( bricks[i*12 + j].getIsDisplay() ){
					Rectangle curBrick = new Rectangle(bricks[i*12 + j].getX(), bricks[i*12 + j].getY(), bricks[i*12 + j].getWidth(), bricks[i*12 + j].getHeight());
					if ( curBrick.intersects(rectball) ){
						// return the ball side that hit
						int side = calculateBallIntersectSide(bricks[i*12 + j], ball);
						System.out.println(curBrick.getBounds() + " | " + rectball.getBounds() + " | Side: " + ( side == -1 ? "NO_COLLISION" : translate[side] ) );
						if (side == -1) continue;
						// Disappear brick
						bricks[i*12 + j].setIsDisplay(false); 
						return side;
					}
				}
				
			}
		}
		return -1;
	}
		
	// return side of ball that intersect 
	// [
	//     -1 => no collision, 
	//	1 => top, 
	//	2 => right, 
	//	3 => bottom,
	//	4 => left 
	// ]
	public int calculateBallIntersectSide (Brick brick, Ball ball) {
		if ( ball.getSpeedX() > 0 ){
			if (ball.getSpeedY() > 0 
			&&  ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()
			&&  ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()
			) {
				//TOP LEFT
				if ( ball.getY() + ball.getRadius() / 2 >= brick.getY() && ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()) {
					return RIGHT;
				}
				if ( ball.getX() + ball.getRadius() / 2 >= brick.getX() && ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()) {
					return BOTTOM;
				}	
			}
			else if (ball.getSpeedY() < 0
			&&  ball.getY() + ball.getRadius() / 2 >= brick.getY()
			&&  ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()
				) {
				// BOTTOM LEFT
				if ( ball.getY() + ball.getRadius() / 2 >= brick.getY() && ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()) {
					return RIGHT;
				}
				if ( ball.getX() + ball.getRadius() / 2 >= brick.getX() && ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()) {
					return TOP;
				}
			}
		}
		if ( ball.getSpeedX() < 0 ){
			if (ball.getSpeedY() < 0
			&&  ball.getY() + ball.getRadius() / 2 >= brick.getY()
			&&  ball.getX() + ball.getRadius() / 2 >= brick.getX() 
				) {
				// BOTTOM RIGHT
				if ( ball.getY() + ball.getRadius() / 2 >= brick.getY() && ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()) {
					return LEFT;
				}
				if ( ball.getX() + ball.getRadius() / 2 >= brick.getX() && ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()) {
					return TOP;
				}
			}
			else if (ball.getSpeedY() > 0
			&&  ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()
			&&  ball.getX() + ball.getRadius() / 2 >= brick.getX() 
				) {
				// TOP RIGHT
				if ( ball.getY() + ball.getRadius() / 2 >= brick.getY() && ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()) {
					return LEFT;
				}
				if ( ball.getX() + ball.getRadius() / 2 >= brick.getX() && ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()) {
					return BOTTOM;
				}
			}
			System.out.println("END LEVEL");
		}
		return -1;
	}
}
