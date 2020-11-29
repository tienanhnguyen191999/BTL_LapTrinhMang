/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import client.view.Game;
import gameplay.develop.test;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 *
 * @author tienanh
 */
public class Utils {

	public static Color colorMapping(String color) {
		switch (color.trim().toUpperCase()) {
			case "GREEN":
				return Color.GREEN;
			case "RED":
				return Color.RED;
			case "BLUE":
				return Color.BLUE;
		}
		return Color.RED;
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, f);
			}
		}
	}

	public static void registerFont() {
		try {
			InputStream is = Game.class.getResourceAsStream("/data/fonts/LifeCraft_Font.ttf");
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (Exception ex) {

		}
	}
	
	public static String getLocalIP () {
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			String ip = socket.getLocalAddress().getHostAddress();
			return ip;
		} catch (SocketException ex) {
			Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnknownHostException ex) {
			Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}
}
