/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.Rectangle;
import model.Ball;
import model.Brick;
import model.MapState;
import consts.Consts;
/**
 *
 * @author tienanh
 */
public abstract class Map {
	protected MapState mapState;

	public Map() {
		mapState = new MapState();
	}
	
	public boolean isNoBrickLeft () {
		return this.mapState.getBricks().length == 0;
	}
	
	public int checkIntersectWithBrick (Ball ball) {
		Rectangle rectball  = new Rectangle(ball.getX(), ball.getY(), ball.getRadius(), ball.getRadius());
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 0; j < 12; j++){
				if ( this.mapState.getBricks()[i*12 + j].getIsDisplay() ){
					Rectangle curBrick = new Rectangle(this.mapState.getBricks()[i*12 + j].getX(), this.mapState.getBricks()[i*12 + j].getY(), mapState.getBricks()[i*12 + j].getWidth(), mapState.getBricks()[i*12 + j].getHeight());
					if ( curBrick.intersects(rectball) ){
						// return the ball side that hit
						int side = calculateBallIntersectSide(mapState.getBricks()[i*12 + j], ball);
						if (side == -1) continue;
						// Disappear brick
						mapState.getBricks()[i*12 + j].setIsDisplay(false); 
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
	//		1 => top, 
	//		2 => right, 
	//		3 => bottom,
	//		4 => left 
	// ]
	public int calculateBallIntersectSide (Brick brick, Ball ball) {
		if ( ball.getSpeedX() > 0 ){
			if (ball.getSpeedY() > 0 
			&&  ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()
			&&  ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()
			) {
				//TOP LEFT
				if ( ball.getY() + ball.getRadius() / 2 >= brick.getY() && ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()) {
					return Consts.RIGHT;
				}
				if ( ball.getX() + ball.getRadius() / 2 >= brick.getX() && ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()) {
					return Consts.BOTTOM;
				}	
			}
			else if (ball.getSpeedY() < 0
			&&  ball.getY() + ball.getRadius() / 2 >= brick.getY()
			&&  ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()
				) {
				// BOTTOM LEFT
				if ( ball.getY() + ball.getRadius() / 2 >= brick.getY() && ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()) {
					return Consts.RIGHT;
				}
				if ( ball.getX() + ball.getRadius() / 2 >= brick.getX() && ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()) {
					return Consts.TOP;
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
					return Consts.LEFT;
				}
				if ( ball.getX() + ball.getRadius() / 2 >= brick.getX() && ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()) {
					return Consts.TOP;
				}
			}
			else if (ball.getSpeedY() > 0
			&&  ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()
			&&  ball.getX() + ball.getRadius() / 2 >= brick.getX() 
				) {
				// TOP RIGHT
				if ( ball.getY() + ball.getRadius() / 2 >= brick.getY() && ball.getY() + ball.getRadius() / 2 <= brick.getY() + brick.getHeight()) {
					return Consts.LEFT;
				}
				if ( ball.getX() + ball.getRadius() / 2 >= brick.getX() && ball.getX() + ball.getRadius() / 2 <= brick.getX() + brick.getWidth()) {
					return Consts.BOTTOM;
				}
			}
			System.out.println("END LEVEL");
		}
		return -1;
	}

	public MapState getMapState() {
		return mapState;
	}

	public void setMapState(MapState mapState) {
		this.mapState = mapState;
	}
}
