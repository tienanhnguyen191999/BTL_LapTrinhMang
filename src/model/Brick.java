/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author tienanh
 */
public class Brick extends Point {
	private int width;
	private int height;
	private boolean isDisplay;

	public Brick(int width, int height, int x, int y) {
		super(x, y);
		isDisplay = true;
		this.width = width;
		this.height = height;
	}
	
	public Brick() {
	}

	public boolean getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return this.width + " : " + this.height + " : " + isDisplay; //To change body of generated methods, choose Tools | Templates.
	}
	
}
