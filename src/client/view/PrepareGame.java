/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.controller.Client;
import consts.Consts;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.text.DefaultCaret;
import model.Ball;
import model.Room;
import model.SocketIO;
import util.Utils;

/**
 *
 * @author tienanh
 */
public class PrepareGame extends javax.swing.JFrame {
    private Room room;
    private SocketIO socketIO;
	private boolean isHost;
	
	public PrepareGame(SocketIO socketIO, Room roomInstance, boolean isHost) {
		this.room = roomInstance;
        this.socketIO = socketIO;
		this.isHost = isHost;
		initComponents();
		manualBindEvents();
		initNewRoom();
        // Listening on room state change(p1, p2, ball, bar, message)
        (new Thread() {
                public void run() {
                    try {
                        while(true){
                            Integer actionCode = (Integer)socketIO.getInput().readObject();
							System.out.println(actionCode);
							switch (actionCode){
								case Consts.UPDATE_WAITING_ROOM:
									updateWaitingRoom();
									break;
								case Consts.START_GAME:
									startGame();
									break;
								case Consts.SEND_MESSAGE:
									updateChatBox();
									break;
								case Consts.UPDATE_P1_BALL_COLOR:
									updateP1BallColor();
									break;
								case Consts.UPDATE_P2_BALL_COLOR:
									updateP2BallColor();
									break;
								case Consts.REMOVE_ROOM:
									roomHasBeenRemove();
									break;
								case Consts.OUT_ROOM:
									handleP2OutRoom();
									break;
							}
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        }).start();
	}
	
	public void handleP2OutRoom () {
		try {
			Room updatedRoom = (Room)socketIO.getInput().readObject();
			room = updatedRoom;
			initNewRoom();
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void roomHasBeenRemove () {
		JOptionPane.showMessageDialog(null, "Room has been removed");
		this.dispose();	
		boolean isRegisterName = true;
		new LAN(socketIO, this.room.getP2(), isRegisterName).setVisible(true);
	}
	
	public void manualBindEvents() {
		cbP1BallColor.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				try {
					// Send action code
					socketIO.getOutput().writeObject(Consts.UPDATE_P1_BALL_COLOR);
					
					// Update ball corlor
					socketIO.getOutput().writeObject(cbP1BallColor.getSelectedItem().toString());
				} catch (IOException ex) {
					Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
		cbP2BallColor.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				try {
					// Send action code
					socketIO.getOutput().writeObject(Consts.UPDATE_P2_BALL_COLOR);
					
					// Update ball corlor
					socketIO.getOutput().writeObject(cbP2BallColor.getSelectedItem().toString());
				} catch (IOException ex) {
					Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});	
	}
	
	public void updateP1BallColor () {
		try {
			String color = (String)socketIO.getInput().readObject();
			cbP1BallColor.setSelectedItem(color);
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void updateP2BallColor () {
		try {
			String color = (String)socketIO.getInput().readObject();
			cbP2BallColor.setSelectedItem(color);
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void updateChatBox (){
		try {
			String message = (String)socketIO.getInput().readObject();
			tfChatBox.append(message);
			JScrollBar vertical = jScrollPane2.getVerticalScrollBar();
			vertical.setValue( vertical.getMaximum() );
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	public void startGame () {
		for (Integer i = 5; i > 0 ; i-- ){
			try {
				tfChatBox.append(i.toString() + "\n");
				JScrollBar vertical = jScrollPane2.getVerticalScrollBar();
				vertical.setValue( vertical.getMaximum() );
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		dispose();
		new Client(socketIO, isHost);
	}
	
	public void updateWaitingRoom (){
		try {
			socketIO.getOutput().writeObject(Consts.UPDATE_WAITING_ROOM);
			socketIO.getOutput().writeObject(getRoom());
			Room updatedRoom = (Room)socketIO.getInput().readObject();
			room = updatedRoom;
			initNewRoom();
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

    private void initNewRoom() {
		tfDes.setText(room.getMap().getMapInfo().getDes()); // Listen on P2 Change
		tfGameSpeed.setText(new Integer(room.getSpeed()).toString());
		tfMapSize.setText(room.getMap().getMapInfo().getType());
		tfMapName.setText(room.getName());
		ImageIcon icon = new ImageIcon(getClass().getResource(room.getMap().getMapInfo().getImagePreviewPath()));
		Image resize = icon.getImage().getScaledInstance(imagePreview.getWidth(), imagePreview.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(resize);
		imagePreview.setIcon(result);
		tfP1Name.setText(room.getP1().getName());
		if (room.getP2() != null && room.getP2().getName() != null){
			tfP2Name.setText(room.getP2().getName());
			watingGif.setVisible(false);
		} else {
			tfP2Name.setText("");
			watingGif.setVisible(true);
		}
		
		// Auto Scroll TextArea 
        if (!isHost) {
            btnStart.setVisible(false);
			cbP1BallColor.setEnabled(false);
        } else{
			cbP2BallColor.setEnabled(false);
		}
    }

    public Room getRoom() {
        return room;
    }
    
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tfP1Name = new javax.swing.JTextPane();
        cbP1BallColor = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tfP2Name = new javax.swing.JTextPane();
        cbP2BallColor = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        watingGif = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        imagePreview = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfMapName = new javax.swing.JTextField();
        tfMapSize = new javax.swing.JTextField();
        tfGameSpeed = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tfRoomName = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tfDes = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        tfMessage = new javax.swing.JTextField();
        btnSendMessage = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tfChatBox = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        btnStart = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(167, 166, 165));
        jPanel1.setToolTipText("");

        jLabel9.setText("Player 1");

        tfP1Name.setEditable(false);
        tfP1Name.setToolTipText("");
        jScrollPane4.setViewportView(tfP1Name);

        cbP1BallColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RED", "GREEN", "BLUE" }));
        cbP1BallColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbP1BallColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbP1BallColor, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbP1BallColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tfP2Name.setEditable(false);
        jScrollPane5.setViewportView(tfP2Name);

        cbP2BallColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BLUE", "RED", "GREEN" }));
        cbP2BallColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbP2BallColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbP2BallColor, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbP2BallColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel10.setText("Player 2");

        jLabel12.setText("Ball Color");

        watingGif.setForeground(new java.awt.Color(253, 31, 31));
        watingGif.setText("wating ...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(watingGif)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addGap(206, 206, 206))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(watingGif))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(447, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(226, 199, 172));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imagePreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imagePreview, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setText("Map Name:");

        jLabel7.setText("Map Size");

        jLabel8.setText("GameSpeed:");

        tfMapName.setEditable(false);
        tfMapName.setOpaque(false);

        tfMapSize.setEditable(false);

        tfGameSpeed.setEditable(false);

        jLabel1.setText("Room Name");

        tfRoomName.setText("TienAnh");
        jScrollPane3.setViewportView(tfRoomName);

        jLabel3.setText("Description:");

        tfDes.setEditable(false);
        tfDes.setColumns(20);
        tfDes.setRows(5);
        jScrollPane1.setViewportView(tfDes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfMapSize)
                            .addComponent(tfMapName)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfGameSpeed))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfMapName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tfMapSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tfGameSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(105, 97, 90));

        tfMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfMessageKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfMessageKeyPressed(evt);
            }
        });

        btnSendMessage.setText("Send");
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });

        tfChatBox.setEditable(false);
        tfChatBox.setColumns(20);
        tfChatBox.setRows(5);
        jScrollPane2.setViewportView(tfChatBox);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(tfMessage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSendMessage))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSendMessage))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(224, 159, 94));

        btnStart.setText("Start Game");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        try {
            // Send action code
            socketIO.getOutput().writeObject(Consts.START_GAME);
			// Update ball corlor
			
			this.getRoom().getP1().setBall(new Ball());
			this.getRoom().getP2().setBall(new Ball());

			this.getRoom().getP1().getBall().setColor(Utils.colorMapping(cbP1BallColor.getSelectedItem().toString()));
			this.getRoom().getP2().getBall().setColor(Utils.colorMapping(cbP2BallColor.getSelectedItem().toString()));

			socketIO.getOutput().reset();
			socketIO.getOutput().writeObject(this.getRoom());
			btnStart.setEnabled(false); 
        } catch (IOException ex) {
            Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnStartActionPerformed
	
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		try {
		if (isHost){
			// Remove Room
			socketIO.getOutput().writeObject(Consts.REMOVE_ROOM);
		} else {
			socketIO.getOutput().writeObject(Consts.OUT_ROOM);
		}
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.dispose();		
		boolean isRegisterName = true;
		new LAN(socketIO, this.room.getP1(), isRegisterName).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cbP2BallColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbP2BallColorActionPerformed
		
    }//GEN-LAST:event_cbP2BallColorActionPerformed

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
		sendMessage();
    }//GEN-LAST:event_btnSendMessageActionPerformed

    private void tfMessageKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfMessageKeyTyped

    }//GEN-LAST:event_tfMessageKeyTyped

    private void tfMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfMessageKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
			sendMessage();
		}
    }//GEN-LAST:event_tfMessageKeyPressed

    private void cbP1BallColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbP1BallColorActionPerformed

    }//GEN-LAST:event_cbP1BallColorActionPerformed

	public void sendMessage() {
		String message = tfMessage.getText();
		if (!message.isEmpty()){
			try {
				// Send message
				socketIO.getOutput().writeObject(Consts.SEND_MESSAGE);
				socketIO.getOutput().writeObject(message);
				tfMessage.setText("");
			} catch (IOException ex) {
				Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JButton btnStart;
    private javax.swing.JComboBox<String> cbP1BallColor;
    private javax.swing.JComboBox<String> cbP2BallColor;
    private javax.swing.JLabel imagePreview;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea tfChatBox;
    private javax.swing.JTextArea tfDes;
    private javax.swing.JTextField tfGameSpeed;
    private javax.swing.JTextField tfMapName;
    private javax.swing.JTextField tfMapSize;
    private javax.swing.JTextField tfMessage;
    private javax.swing.JTextPane tfP1Name;
    private javax.swing.JTextPane tfP2Name;
    private javax.swing.JTextPane tfRoomName;
    private javax.swing.JLabel watingGif;
    // End of variables declaration//GEN-END:variables
}
