/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author tienanh
 */
public class SocketIO {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public void setInput(ObjectInputStream input) {
        this.input = input;
    }

   
}
