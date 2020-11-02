/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author tienanh
 */
public class Ball extends Point implements Serializable{
	private int radius;
	private int speedX, speedY;
	private Color color;

	/**
	 *
	 * @param radius
	 * @param speedX
	 * @param speedY
	 * @param x
	 * @param y
	 */
	public Ball(int radius, int speedX, int speedY, int x, int y) {
		super(x, y);
		this.radius = radius;
		this.speedX = speedX;
		this.speedY = speedY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 *
	 * @return
	 */
	public int getSpeedX() {
		return speedX;
	}

	/**
	 *
	 * @return
	 */
	public int getSpeedY() {
		return speedY;
	}

	/**
	 *
	 * @param speedX
	 */
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	/**
	 *
	 * @param speedY
	 */
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	
	/**
	 *
	 * @return
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 *
	 * @param radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return x + ": " + y; 
	}
	
	
}
