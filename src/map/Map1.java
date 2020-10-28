package map;

import consts.Consts;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import model.Ball;
import model.Brick;
import model.MapState;

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
public class Map1 extends Map{
	
	public Map1 () {
		super();
		int padding = 10;
		mapState.setBricks(new Brick[3 * 12]);
		int totalBrickHeight = 3 * Consts.BRICK_HEIGHT + 2 * padding;
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 0; j < 12; j++){
				Brick tmp = new Brick(Consts.BRICK_WIDTH ,Consts.BRICK_HEIGHT, 65 + j * Consts.BRICK_WIDTH + j*padding, (consts.Consts.GAMPLAY_HEIGHT - totalBrickHeight )/ 2 + i*Consts.BRICK_HEIGHT + i*padding);
				mapState.getBricks()[i*12 + j] = tmp;
			}	
		}
	}
}
