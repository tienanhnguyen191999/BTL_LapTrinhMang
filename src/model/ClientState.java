/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author tienanh
 */
public class ClientState implements Serializable{
    static final long serialVersionUID = 42L;
	private ArrayList<EnhanceItem> enhanceItems;
	public boolean isSocketClose;
	private String name;
	private Ball ball;
	private int point;
	private Bar bar;

	public ClientState() {
		enhanceItems = new ArrayList<EnhanceItem>();
	}

	public void setEnhanceItems(ArrayList<EnhanceItem> enhanceItems) {
		this.enhanceItems = enhanceItems;
	}

	public ArrayList<EnhanceItem> getEnhanceItems() {
		return enhanceItems;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 */
	public Ball getBall() {
		return ball;
	}

	/**
	 *
	 * @return
	 */
	public Bar getBar() {
		return bar;
	}

	/**
	 *
	 * @return
	 */
	public int getPoint() {
		return point;
	}

	/**
	 *
	 * @param ball
	 */
	public void setBall(Ball ball) {
		this.ball = ball;
	}

	/**
	 *
	 * @param bar
	 */
	public void setBar(Bar bar) {
		this.bar = bar;
	}

	/**
	 *
	 * @param point
	 */
	public void setPoint(int point) {
		this.point = point;
	}
}
