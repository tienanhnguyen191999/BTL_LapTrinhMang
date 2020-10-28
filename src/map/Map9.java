/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import consts.Consts;
import model.Brick;

/**
 *
 * @author tienanh
 */
public class Map9 extends Map{
	public Map9() {
		super();
		int padding = 10;
		mapState.setBricks(new Brick[3 * 12]);
		int totalBrickHeight = 3 * Consts.BRICK_HEIGHT + 2 * padding;
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 0; j < 12; j++){
				Brick tmp = new Brick(Consts.BRICK_WIDTH ,Consts.BRICK_HEIGHT, 65 + j * Consts.BRICK_WIDTH + j*padding, (consts.Consts.GAMPLAY_HEIGHT - totalBrickHeight )/ 2 + i*Consts.BRICK_HEIGHT + i*padding);
				mapState.getBricks()[i*12 + j] = tmp;
				if ( j % 3 == 0 ) mapState.getBricks()[i*12 + j].setIsDisplay(false);
			}	
		}
	}
}
