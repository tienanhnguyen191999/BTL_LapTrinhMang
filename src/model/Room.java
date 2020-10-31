/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import map.Map;

/**
 *
 * @author tienanh
 */
public class Room implements Serializable{
	private ClientState p1, p2;
	private Map map;
    private Integer status; // [Waiting, Play, NotPlay]

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
