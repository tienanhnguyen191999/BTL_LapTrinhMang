/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.swing.ImageIcon;
import map.Map;

/**
 *
 * @author tienanh
 */
public class Room implements Serializable{
    private String name; // Unique
    private Map map;
    private int speed;
	private ClientState p1, p2;
    private Integer status; // [Waiting, Load, NotPlay]
	private ImageIcon previewForSaveGame;
	private Integer gameMode;

	public Integer getGameMode() {
		return gameMode;
	}

	public ImageIcon getPreviewForSaveGame() {
		return previewForSaveGame;
	}

	public void setPreviewForSaveGame(ImageIcon previewForSaveGame) {
		this.previewForSaveGame = previewForSaveGame;
	}
	
	public void setGameMode(Integer gameMode) {
		this.gameMode = gameMode;
	}

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public ClientState getP1() {
        return p1;
    }

    public ClientState getP2() {
        return p2;
    }

    public Integer getStatus() {
        return status;
    }

    public void setP1(ClientState p1) {
        this.p1 = p1;
    }

    public void setP2(ClientState p2) {
        this.p2 = p2;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
