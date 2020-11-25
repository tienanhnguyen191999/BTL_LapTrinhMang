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
public class Map5 extends Map implements Serializable {

	public Map5() {
		super();
		int padding = 10;
		int row = 16;
		int col = 21;
		mapState.setCol(col);
		mapState.setRow(row);
		mapState.setBricks(new Brick[row * col]);
		//0-7    
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 3; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200 + i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				mapState.getBricks()[i * 21 + j].setIsDisplay(false);
			}
			for (int j = 3; j < 10; j++) {

				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200 + i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;

				if (i + j <= 9) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
			}

			for (int j = 10; j < 18; j++) {

				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200 + i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;

				if (j - 11 >= i) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				} else {
					mapState.getBricks()[i * 21 + j].setIsDisplay(true);
				}
			}

			for (int j = 18; j < 21; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200+ i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				mapState.getBricks()[i * 21 + j].setIsDisplay(false);
			}

		}
		// 8-16
		for (int i = 8; i < 16; i++) {
			for (int j = 0; j < 3; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200+ i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				mapState.getBricks()[i * 21 + j].setIsDisplay(false);
			}

			for (int j = 3; j < 10; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200+ i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				if (i - j >= 6) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
			}

			for (int j = 10; j < 18; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200+ i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				if (i + j <= 25) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(true);
				} else {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
			}

			for (int j = 18; j < 21; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200+ i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				mapState.getBricks()[i * 21 + j].setIsDisplay(false);
			}
		}
		
		// Add Random EnhanceItem
		// Must call this function after initial bricks[]
		mapState.addEnhanceItemInsideBricks();
		
		// Map Info
        mapInfo.setName("Map5");
		mapInfo.setType("Small");
		mapInfo.setDes("More Info Here: github.com/tienanhnguyen191999/BTL_LapTrinhMang");
		mapInfo.setImagePreviewPath("/data/mapPreview/map-5.png");
	}
}
