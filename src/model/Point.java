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
public class Point implements Serializable{

	/**
	 *
	 */
	protected int x, y;

	/**
	 *
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 *
	 */
	public Point() {
	}
	
	/**
	 *
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 *
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 *
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 *
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	
}
