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
public class Ball extends Point{
	private int radius;
	private int speedX, speedY;

	public Ball(int radius, int speedX, int speedY, int x, int y) {
		super(x, y);
		this.radius = radius;
		this.speedX = speedX;
		this.speedY = speedY;
	}

	public int getSpeedX() {
		return speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	@Override
	public String toString() {
		return x + ": " + y; 
	}
	
	
}
