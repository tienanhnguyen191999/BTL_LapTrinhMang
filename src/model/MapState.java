/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author tienanh
 */
public class MapState implements Serializable{
	private Brick[] bricks;
	private int row;
	private int col;

	public MapState() {
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
}
