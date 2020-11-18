package map;

import consts.Consts;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
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
public class Map1 extends Map implements Serializable{
	
	public Map1 () {
		super();
		
		// Map state
		int padding = 10;
		int row = 16;
		int col = 21;
		mapState.setBricks(new Brick[col * row]);
		mapState.setCol(col);
		mapState.setRow(row);
		int totalBrickHeight = row * Consts.BRICK_HEIGHT + 2 * padding;
		for (int i = 0 ; i < row ; i++) {
			for (int j = 0; j < col; j++){
				Brick tmp = new Brick(Consts.BRICK_WIDTH ,Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j*padding,
                                    200 + i*Consts.BRICK_HEIGHT + i*padding);
				mapState.getBricks()[i*col + j] = tmp;
			}	
		}
		
		// Map Info
        mapInfo.setName("Map1");
		mapInfo.setType("Small");
		mapInfo.setDes("Des go here");
		mapInfo.setImagePreviewPath("/data/mapPreview/map-1.png");
	}
}
