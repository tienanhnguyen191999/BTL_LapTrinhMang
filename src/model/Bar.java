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
public class Bar extends Point implements Serializable {
	private int width;
	private int height;
	private int speed;

	/**
	 *
	 * @param width
	 * @param height
	 * @param speed
	 * @param x
	 * @param y
	 */
	public Bar(int width, int height, int speed, int x, int y) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.speed = speed;
	}

	/**
	 *
	 * @return
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 *
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 *
	 */
	public Bar() {
	}
	
	/**
	 *
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 *
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 *
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 *
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	
	
}
