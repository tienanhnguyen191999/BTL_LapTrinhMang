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
public class ClientState implements Serializable{
	private Ball ball;
	private Bar bar;
	private int point;
	private String name;
	public boolean isSocketClose;

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
