/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consts;

/**
 *
 * @author tienanh
 */
public class Consts {
	public static final int PORT = 1107;
	public static final String IP_HOST = "localhost";
	public static final int MAX_PLAYER = 2;
	
	public static final int SCREEN_WIDTH = 1600;
	public static final int SCREEN_HEIGHT = 800;
	
	public static final int GAMPLAY_WIDTH = 1200;
	public static final int GAMPLAY_HEIGHT = 800;
	
	public static final int BRICK_WIDTH = 80;
	public static final int BRICK_HEIGHT = 30;
	
	public static final int TOP = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;
	public static final int LEFT = 4;
	
	public static final int TOTAL_MAP = 10;
    
    // For thread Listening action
    public static final int CREATE_ROOM_CODE = 1;
    public static final int BAR_MOVE = 2;
    public static final int GET_LIST_ROOM = 3;
    public static final int JOIN_ROOM = 4;
    public static final int UPDATE_WAITING_ROOM = 5;
    public static final int START_GAME = 6; 
    
    // Room Status
    public static final int WAITING = 1;
    public static final int READY = 2;
    public static final int STOP = 3;
    public static final int PAUSE = 4;
    public static final int PLAY = 5;
}


