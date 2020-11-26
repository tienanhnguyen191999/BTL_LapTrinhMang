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
import java.io.Serializable;
import javax.swing.ImageIcon;
import model.EnhanceItem;
import model.MapInfo;
/**
 *
 * @author tienanh
 */
public abstract class Map implements Serializable{
    static final long serialVersionUID = 41L;
	protected MapState mapState;
	protected MapInfo mapInfo;

	public Map() {
		mapState = new MapState();
		mapInfo = new MapInfo();
	}
	
	public boolean isNoBrickLeft () {
		return this.mapState.getBricks().length == 0;
	}
	
	public int checkIntersectWithBrick (Ball ball, boolean isP1) {
		Rectangle rectball  = new Rectangle(ball.getX(), ball.getY(), ball.getRadius(), ball.getRadius());
		for (int i = 0 ; i < mapState.getRow() ; i++) {
			for (int j = 0; j < mapState.getCol(); j++){
				if ( this.mapState.getBricks()[i*mapState.getCol() + j].getIsDisplay() ){
					Rectangle curBrick = new Rectangle(this.mapState.getBricks()[i*mapState.getCol()+ j].getX(), this.mapState.getBricks()[i*mapState.getCol()+ j].getY(), mapState.getBricks()[i*mapState.getCol()+ j].getWidth(), mapState.getBricks()[i*mapState.getCol()+ j].getHeight());
					if ( curBrick.intersects(rectball) ){
						// return the ball side that hit
						int side = calculateBallIntersectSide(mapState.getBricks()[i*mapState.getCol()+ j], ball);
						if (side == -1) continue;
						
						// hide brick
						mapState.getBricks()[i*mapState.getCol()+ j].setIsDisplay(false); 
						
						// Append enhanceItem to Ball
						if (mapState.getBricks()[i*mapState.getCol()+ j].getType() != Consts.NORMAL){
							String icon = "_25x25.png";
							int type = Consts.NORMAL;
							switch (mapState.getBricks()[i*mapState.getCol()+ j].getType()) {
								case Consts.ENHANCE_ITEM_BIG_BALL:
									icon = "bigball" + icon;
									type = Consts.ENHANCE_ITEM_BIG_BALL;
									break;
								case Consts.ENHANCE_ITEM_X2_POINT:
									icon = "x2" + icon;
									type = Consts.ENHANCE_ITEM_X2_POINT;
									break;
								case Consts.ENHANCE_ITEM_POWER_BALL:
									icon = "powerball" + icon;
									type = Consts.ENHANCE_ITEM_POWER_BALL;
									break;
								case Consts.ENHANCE_ITEM_LENTHEN_BAR:
									icon = "lengthenbar" + icon;
									type = Consts.ENHANCE_ITEM_LENTHEN_BAR;
									break;
							}
							EnhanceItem enhanceItem = new EnhanceItem(
								isP1 ? Consts.BOTTOM : Consts.TOP, 
								new ImageIcon(getClass().getResource("/data/image/" + icon)),
								type, 
								mapState.getBricks()[i*mapState.getCol()+ j].getX(),
								mapState.getBricks()[i*mapState.getCol()+ j].getY()
							);
							mapState.getEnhanceItems().add(enhanceItem);
						}
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
		}
		return -1;
	}

	public MapState getMapState() {
		return mapState;
	}

	public void setMapState(MapState mapState) {
		this.mapState = mapState;
	}

	public MapInfo getMapInfo() {
		return mapInfo;
	}

	public void setMapInfo(MapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}
}
