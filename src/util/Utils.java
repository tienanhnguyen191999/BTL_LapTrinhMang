/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import java.awt.List;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tienanh
 */
public class Utils {
	public static Color colorMapping(String color){
		switch(color.trim().toUpperCase()) {
			case "GREEN":
				return Color.GREEN;
			case "RED":
				return Color.RED;
			case "BLUE":
				return Color.BLUE;
		}
		return Color.RED;
	}
}
