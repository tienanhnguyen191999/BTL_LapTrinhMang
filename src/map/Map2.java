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
public class Map2 extends Map implements Serializable{
	
	public Map2() {
		super();
		int padding = 10;
		int row = 16;
		int col = 21;
		mapState.setRow(row);
		mapState.setCol(col);
		mapState.setBricks(new Brick[row * col]);
		for (int i = 0 ; i < row ; i++) {
			for (int j = 0; j < col; j++){
				Brick tmp = new Brick(Consts.BRICK_WIDTH ,Consts.BRICK_HEIGHT, 70+(Consts.BRICK_WIDTH+padding) + j * Consts.BRICK_WIDTH + j*padding
                                    ,200 + i*Consts.BRICK_HEIGHT + i*padding);
				mapState.getBricks()[i*col + j] = tmp;
				if ( (i%3>0 && j%3>0) || j==19||j==20 ) mapState.getBricks()[i*col + j].setIsDisplay(false);
			}	
		}
		
		// Add Random EnhanceItem
		// Must call this function after initial bricks[]
		mapState.addEnhanceItemInsideBricks();
		System.out.println(this.getClass() + "\n" + mapState );
		
		// Map Info
        mapInfo.setName("Map2");
		mapInfo.setType("Medium");
		mapInfo.setDes("Fix this bois");
		mapInfo.setImagePreviewPath("/data/mapPreview/map-2.png");
	}
}
