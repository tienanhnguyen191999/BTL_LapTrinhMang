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

	public MapState() {
	}

	public Brick[] getBricks() {
		return bricks;
	}

	public void setBricks(Brick[] bricks) {
		this.bricks = bricks;
	}
}
