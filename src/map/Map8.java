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
public class Map8 extends Map implements Serializable {

	public Map8() {
		super();
		int padding = 10;
		int row = 16;
		int col = 21;
		mapState.setCol(col);
		mapState.setRow(row);
		mapState.setBricks(new Brick[row * col]);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < i; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200 + Consts.BRICK_HEIGHT/*(consts.Consts.GAMPLAY_HEIGHT - totalBrickHeight )/ 2 */ + i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				//    if(j<=j)
				//  mapState.getBricks()[i*21 + j].setIsDisplay(false);

			}

			for (int j = i; j < 21; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200 + Consts.BRICK_HEIGHT/*(consts.Consts.GAMPLAY_HEIGHT - totalBrickHeight )/ 2 */ + i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				if (j - 4 <= 16 - i) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
			}
		}
		for (int i = 8; i < 16; i++) {
			for (int j = 0; j < i; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200 + Consts.BRICK_HEIGHT/*(consts.Consts.GAMPLAY_HEIGHT - totalBrickHeight )/ 2 */ + i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				//    if(j<=16-i) mapState.getBricks()[i*21 + j].setIsDisplay(false);

			}

			for (int j = i; j < 21; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200 + Consts.BRICK_HEIGHT/*(consts.Consts.GAMPLAY_HEIGHT - totalBrickHeight )/ 2 */ + i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				//        mapState.getBricks()[i*21 + j].setIsDisplay(false);
				if ((j >= 16 - i)) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
			}
		}
		mapInfo.setName("Map8");
		mapInfo.setType("Small");
		mapInfo.setDes("More Info Here: github.com/tienanhnguyen191999/BTL_LapTrinhMang");
		mapInfo.setImagePreviewPath("/data/mapPreview/map-8.png");
	}
}
