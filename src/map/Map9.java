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
public class Map9 extends Map implements Serializable {

	public Map9() {
		super();
		int padding = 10;
		int row = 16;
		int col = 21;
		mapState.setCol(col);
		mapState.setRow(row);
		mapState.setBricks(new Brick[row * col]);
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 21; j++) {
				Brick tmp = new Brick(Consts.BRICK_WIDTH, Consts.BRICK_HEIGHT, 70 + j * Consts.BRICK_WIDTH + j * padding,
					200+ i * Consts.BRICK_HEIGHT + i * padding);
				mapState.getBricks()[i * 21 + j] = tmp;
				//1,
				if (j == 3 || j == 7 || j == 11 || j == 13 || j == 17) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				if (i == 7) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				//2,        
				if (i > 7 && (j == 1 || j == 2 || j == 0)) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				//3,
				if (i < 6 && (j == 1 || j == 2)) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				//4        
				if (i < 7 && j > 11) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				//5        
				if (i > 8 && (j == 4 || j == 6)) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				//6
				if ((i == 1 || i == 2 || i == 4 || i == 5 || i == 6) && j == 5) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				//7
				if ((i == 1 || i == 2 || i == 4 || i == 5 || i == 6 || i == 10 || i == 9 || i == 13 || i == 14 || i == 15) && j == 9) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				//8
				if ((i == 4 || i == 5 || i == 6 || i == 12) && j == 10) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				//9
				if (i > 9 && j == 15) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}
				//10
				if ((i == 8 || i == 9 || i == 10 || i == 13 || i == 14 || i == 15) && j == 19) {
					mapState.getBricks()[i * 21 + j].setIsDisplay(false);
				}

			}
		}

		// Add Random EnhanceItem
		// Must call this function after initial bricks[]
		mapState.addEnhanceItemInsideBricks();
			
		mapInfo.setName("Map9");
		mapInfo.setType("Small");
		mapInfo.setDes("More Info Here: github.com/tienanhnguyen191999/BTL_LapTrinhMang");
		mapInfo.setImagePreviewPath("/data/mapPreview/map-9.png");
	}
}
