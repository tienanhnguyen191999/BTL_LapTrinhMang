/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.io.Serializable;
import model.Room;

/**
 *
 * @author tienanh
 * 
 * This class handle data transform in waiting room
 */
public class WaitingRoomThread implements Serializable{
    // Provide Socket
    private ClientThread p1, p2;
    // Data share
    private Room room;

    public WaitingRoomThread() {
    }
    
    public ClientThread getP1() {
        return p1;
    }

    public ClientThread getP2() {
        return p2;
    }

    public Room getRoom() {
        return room;
    }

    public void setP1(ClientThread p1) {
        this.p1 = p1;
    }

    public void setP2(ClientThread p2) {
        this.p2 = p2;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
    
}
