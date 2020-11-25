/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import consts.Consts;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author tienanh
 */
public class MapState implements Serializable{
	private Brick[] bricks;
	// Use for render falling item (not affect to )
	private ArrayList<EnhanceItem> enhanceItems;
	private int row;
	private int col;

	public MapState() {
		enhanceItems = new ArrayList<EnhanceItem>();
	}

	public ArrayList<EnhanceItem> getEnhanceItems() {
		return enhanceItems;
	}

	public void setEnhanceItems(ArrayList<EnhanceItem> enhanceItems) {
		this.enhanceItems = enhanceItems;
	}
	
	public void addEnhanceItemInsideBricks() {
		// Add Random EnhanceItem
		ArrayList<Integer> indexDisplayBricks = this.getBrickIndexIsDisplay();

		// Random index
		Collections.shuffle(indexDisplayBricks);
		Random rand = new Random();
		
		// 5%  of total display bricks
		int totalEnhanceItems = (int)(indexDisplayBricks.size() * 1);
		for (int i = 0 ; i < totalEnhanceItems; i++){
			// Random 4 power-up
			int enhanceItemType = rand.nextInt(4) + 1001;
			this.getBricks()[indexDisplayBricks.get(i)].setType(enhanceItemType);
		}
	}
	
	public ArrayList<Integer> getBrickIndexIsDisplay () {
		ArrayList<Integer> indexDisplayBricks = new ArrayList<>();
		for (int i = 0 ; i < row; i++) {
			for (int j = 0 ; j < col; j++) {
				if (this.getBricks()[i*col + j].isIsDisplay()){
					indexDisplayBricks.add(i*col + j);
				}
			}
		}
		return indexDisplayBricks;
	}
	
	public Brick[] getBricks() {
		return bricks;
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public void setBricks(Brick[] bricks) {
		this.bricks = bricks;
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0 ; i < row; i++) {
			for (int j = 0 ; j < col; j++) {
				if (this.getBricks()[i*col + j].getIsDisplay()){
					if (this.getBricks()[i*col + j].getType() != Consts.NORMAL){
						str += "=====================================>";
					}
					str += "Index: " + (i*col + j);
					str += " Type: " + this.getBricks()[i*col + j].getType() + '\n';
				}
			}
		}
		return str;
	}
}
