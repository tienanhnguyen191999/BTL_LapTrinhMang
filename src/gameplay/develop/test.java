/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameplay.develop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jdk.internal.util.xml.impl.Pair;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tienanh
 */
public class test {

	public static void main(String[] args) {
		
	}

	public static void display(HashSet<Integer> hashset) {
		for (int i = 0; i < hashset.size(); i++) {
			System.out.println(hashset.toArray()[i]);
		}
	}
}
