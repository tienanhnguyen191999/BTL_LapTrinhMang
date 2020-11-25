/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import consts.Consts;
import java.io.Serializable;
import model.Brick;

/**
 *
 * @author tienanh
 */
public class Map6 extends Map implements Serializable{
	public Map6() {
		super();
		int padding = 10;
		int row = 16;
		int col = 21;
		mapState.setCol(col);
		mapState.setRow(row);
		mapState.setBricks(new Brick[row * col]);
		for (int i = 0 ; i < 16 ; i++) {
			for (int j = 0; j < 21; j++){
				Brick tmp = new Brick(Consts.BRICK_WIDTH ,Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j*padding,
                                    200 + i*Consts.BRICK_HEIGHT + i*padding);
				mapState.getBricks()[i*21 + j] = tmp;
				if (i==0||i%2!=0 && j%2!=0) mapState.getBricks()[i*21 + j].setIsDisplay(false);
			}	
		}
		
		// Add Random EnhanceItem
		// Must call this function after initial bricks[]
		mapState.addEnhanceItemInsideBricks();

		mapInfo.setName("Map6");
		mapInfo.setType("Small");
		mapInfo.setDes("More Info Here: github.com/tienanhnguyen191999/BTL_LapTrinhMang");
		mapInfo.setImagePreviewPath("/data/mapPreview/map-6.png");
	}
}
