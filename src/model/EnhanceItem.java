/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author tienanh
 */
public class EnhanceItem extends Point implements Serializable{
	private int fallingTo; // Top or Bottom
	private ImageIcon icon; // Icon
	private int type;  // Power-up type 
	private int remainingTime;

	public EnhanceItem(int fallingTo, ImageIcon icon, int type, int x, int y) {
		super(x, y);
		this.fallingTo = fallingTo;
		this.icon = icon;
		this.type = type;
		remainingTime = 0;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}
	
	public void setFallingTo(int fallingTo) {
		this.fallingTo = fallingTo;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFallingTo() {
		return fallingTo;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public int getType() {
		return type;
	}
}
