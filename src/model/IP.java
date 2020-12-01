/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.regex.Pattern;

/**
 *
 * @author tienanh
 */
public class IP {
	private String ip;

	public IP() {
		ip = "";
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}
	
	public boolean validate () {
		String IP_PATTERN = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
		return Pattern.matches(IP_PATTERN, this.ip);
	}
}
