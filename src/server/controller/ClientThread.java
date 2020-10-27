/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Ball;
import model.Bar;
import model.ClientState;

/**
 *
 * @author tienanh
 */
public class ClientThread extends Thread{
	private ClientState state;
	private DataOutputStream output;
	private DataInputStream input;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
	private Socket socket;
	
	public ClientThread(Socket socket) {
		try {
			this.socket = socket;
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.flush();
			objectInput = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	@Override
	public void run() {
		Thread.currentThread().getName();
		while(true){
			try {
				Integer keycode  = (Integer)objectInput.readObject();
				switch (keycode) {
					case KeyEvent.VK_LEFT:
						this.getClientState().getBar().setX(
							this.getClientState().getBar().getX() > 
							this.getClientState().getBar().getSpeed() ? 
							this.getClientState().getBar().getX() - 
							this.getClientState().getBar().getSpeed() : 0
						);
						break;
					case KeyEvent.VK_RIGHT:
						this.getClientState().getBar().setX(
							this.getClientState().getBar().getX() +
							this.getClientState().getBar().getWidth() < 
							consts.Consts.SCREEN_WIDTH*3/4 - 
							this.getClientState().getBar().getSpeed() ? 
							this.getClientState().getBar().getX() +
							this.getClientState().getBar().getSpeed() :
							consts.Consts.SCREEN_WIDTH*3/4 - 
							this.getClientState().getBar().getWidth()
						);
						break;
				}
			} catch (IOException ex) {
				Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 *
	 * @param objectOutput
	 */
	public void setObjectOutput(ObjectOutputStream objectOutput) {
		this.objectOutput = objectOutput;
	}

	/**
	 *
	 * @param objectInput
	 */
	public void setObjectInput(ObjectInputStream objectInput) {
		this.objectInput = objectInput;
	}

	/**
	 *
	 * @param state
	 */
	public void setClientState(ClientState state) {
		this.state = state;
	}

	/**
	 *
	 * @return
	 */
	public ClientState getClientState() {
		return state;
	}
	
	/**
	 *
	 * @return
	 */
	public boolean isClosed() {
		return socket.isClosed();
	}

	/**
	 *
	 * @param socket
	 */
	

	/**
	 *
	 * @return
	 */
	public ObjectOutputStream getObjectOutput() {
		return objectOutput;
	}

	/**
	 *
	 * @return
	 */
	public ObjectInputStream getObjectInput() {
		return objectInput;
	}

	/**
	 *
	 * @param output
	 */
	public void setOutput(DataOutputStream output) {
		this.output = output;
	}

	/**
	 *
	 * @param input
	 */
	public void setInput(DataInputStream input) {
		this.input = input;
	}

	/**
	 *
	 * @param socket
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 *
	 * @return
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 *
	 * @return
	 */
	public DataOutputStream getOutput () {
		return output;
	}
	
	/**
	 *
	 * @return
	 */
	public DataInputStream getInput () {
		return input;
	}
}
