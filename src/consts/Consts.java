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
	public static final String IP_HOST = "localhost";
	public static final int PORT = 1107;
	
	public static final int SCREEN_WIDTH = 1600;
	public static final int SCREEN_HEIGHT = 800;
	
	public static final int GAMPLAY_WIDTH = 1200;
	public static final int GAMPLAY_HEIGHT = 800;
	
	// Default scale = 1
	public static final int BRICK_WIDTH = 40;
	public static final int BRICK_HEIGHT = 15;
	public static final int BAR_WIDTH = 100;
	public static final int BAR_HEIGHT = 10;
	public static final int BALL_RADIUS = 15;
	
	// Default Speed
	public static final int BAR_SPEED = 20;
	
	public static final int TOP = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;
	public static final int LEFT = 4;
	
	public static final int TOTAL_MAP = 10;
    
    // For thread Listening action
    public static final int CREATE_ROOM_CODE = 101;
    public static final int BAR_MOVE = 102;
    public static final int GET_LIST_ROOM = 103;
    public static final int JOIN_ROOM = 104;
    public static final int UPDATE_WAITING_ROOM = 105;
    public static final int START_GAME = 106; 
	public static final int SEND_MESSAGE = 107;
	public static final int UPDATE_P1_BALL_COLOR = 108;
	public static final int UPDATE_P2_BALL_COLOR = 109;
	public static final int REMOVE_ROOM = 110;
	public static final int OUT_ROOM = 111;
	public static final int ROOM_NOT_EXISTS = 112;
	public static final int ROOM_EXISTS = 113;
	public static final int GAME_PAUSE = 114;
	public static final int GAME_UNPAUSE = 115;
	public static final int SET_PLAYER_NAME = 116;
	public static final int UPDATE_PLAYER_NAME = 117;
    public static final int SAVE_GAME = 118;
    public static final int LOAD_GAME = 119;

	// For Gameplay Thread
	public static final int COUNTER_BEFORE_START = 8; 
	public static final int UPDATE_GAMEPLAY_STATE = 9; 
	public static final int OTHER_PLAYER_LOST_CONNECTION = 10;
	public static final int GAME_LOSE = 11;
	public static final int GAME_WIN = 12;
	
	
    // Room Status
    public static final int WAITING = 1;
    public static final int READY = 2;
    public static final int STOP = 3;
    public static final int PAUSE = 4;
    public static final int PLAY = 5;
    public static final int LOADED_ROOM = 6;
	
	// Game Mode 
	public static final int TWO_BALL = 51;
	public static final int ONE_BALL = 52;
	
	// Enhance Item
	public static final int NORMAL = 1000;
	public static final int ENHANCE_ITEM_BIG_BALL = 1001;
	public static final int ENHANCE_ITEM_POWER_BALL = 1002;
	public static final int ENHANCE_ITEM_LENTHEN_BAR= 1003;
	public static final int ENHANCE_ITEM_X2_POINT = 1004;
	public static final int ENHANCE_ITEM_LAST = 10020;
}


