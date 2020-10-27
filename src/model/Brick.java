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
public class Brick extends Point implements Serializable {
	private int width;
	private int height;
	private boolean isDisplay;

	/**
	 *
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 */
	public Brick(int width, int height, int x, int y) {
		super(x, y);
		isDisplay = true;
		this.width = width;
		this.height = height;
	}
	
	/**
	 *
	 */
	public Brick() {
	}

	/**
	 *
	 * @return
	 */
	public boolean getIsDisplay() {
		return isDisplay;
	}

	/**
	 *
	 * @param isDisplay
	 */
	public void setIsDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
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

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return this.width + " : " + this.height + " : " + isDisplay; //To change body of generated methods, choose Tools | Templates.
	}
	
}
