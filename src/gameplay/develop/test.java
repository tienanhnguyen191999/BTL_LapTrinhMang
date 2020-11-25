/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameplay.develop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import jdk.internal.util.xml.impl.Pair;

/**
 *
 * @author tienanh
 */
public class test {
	public static void main(String[] args) {
		int row=16, col=21;
		
		// Add Random EnhanceItem
		Random rand = new Random();
		HashSet<Integer> brick_indexs = new HashSet<Integer>();
		int totalItem = (int)(row*col*0.05);
		int item_counter = 0;
		while (totalItem > item_counter){
			int index = rand.nextInt(row*col);
			System.out.println("Generate random number: " + index);
			while (brick_indexs.add(index)){
				index = rand.nextInt(row*col);
				System.out.println("Generate random number: " + index);
			}
			item_counter++;
		}
		display(brick_indexs);
	}
	
	public static void display(HashSet<Integer> hashset) {
		for (int i = 0 ; i < hashset.size(); i++){
			System.out.println(hashset.toArray()[i]);
		}
	}	
}
