/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.view.component.CustomMessageDialog;
import client.controller.Client;
import client.view.component.CustomMessageWithoutLogoDialog;
import consts.Consts;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
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
	private Thread listenThread;
	private boolean isTerminate;

	public PrepareGame(SocketIO socketIO, Room roomInstance, boolean isHost) {
		this.isTerminate = false;
		this.room = roomInstance;
		this.socketIO = socketIO;
		this.isHost = isHost;
		initComponents();
		setTranparencyEffect();
		manualBindEvents();
		initNewRoom();
		// Listening on room state change(p1, p2, ball, bar, message)
		listenThread = (new Thread() {
			public void run() {
				try {
					while (!isTerminate) {
						System.out.println("ON Listeing...");
						Integer actionCode = (Integer) socketIO.getInput().readObject();
						System.out.println(actionCode);
						switch (actionCode) {
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
		});
		listenThread.start();
	}

	public void setTranparencyEffect() {
		// Set tranparency
		jPanel1.setBackground(new Color(254, 254, 254, 30));
		jPanel2.setBackground(new Color(254, 254, 254, 30));
		jPanel3.setBackground(new Color(254, 254, 254, 30));
		jPanel4.setBackground(new Color(254, 254, 254, 30));
		jPanel7.setBackground(new Color(254, 254, 254, 50));
		jPanel8.setBackground(new Color(254, 254, 254, 50));

		cbP1BallColor.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setBackground(new Color(0, 0, 0));
				setForeground(new Color(254, 254, 254));
				super.paint(g);
			}
		;
		});
		
		cbP2BallColor.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setBackground(new Color(0, 0, 0));
				setForeground(new Color(254, 254, 254));
				super.paint(g);
			}
		;
	}

	);
	}
	
	public void handleP2OutRoom() {
		isTerminate = true;
		this.dispose();
		new LAN(socketIO, this.room.getP2(), true).setVisible(true);
	}

	public void roomHasBeenRemove() {
		isTerminate = true;
		this.dispose();
		boolean isRegisterName = true;
		new LAN(socketIO, this.room.getP1(), isRegisterName).setVisible(true);
	}

	public void manualBindEvents() {
		cbP1BallColor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
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
		cbP2BallColor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
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

	public void updateP1BallColor() {
		try {
			String color = (String) socketIO.getInput().readObject();
			cbP1BallColor.setSelectedItem(color);
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void updateP2BallColor() {
		try {
			String color = (String) socketIO.getInput().readObject();
			cbP2BallColor.setSelectedItem(color);
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void updateChatBox() {
		try {
			String message = (String) socketIO.getInput().readObject();
			tfChatBox.append(message);
			JScrollBar vertical = jScrollPane2.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void startGame() {
		isTerminate = true;
		btnStart.setEnabled(false);
		btnSendMessage.setEnabled(false);
		btnExit.setEnabled(false);
		for (Integer i = 5; i > 0; i--) {
			try {
				tfChatBox.append(i.toString() + "\n");
				JScrollBar vertical = jScrollPane2.getVerticalScrollBar();
				vertical.setValue(vertical.getMaximum());
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		dispose();
		new Client(socketIO, isHost);
	}

	public void updateWaitingRoom() {
		try {
			socketIO.getOutput().writeObject(Consts.UPDATE_WAITING_ROOM);
			socketIO.getOutput().writeObject(getRoom());
			Room updatedRoom = (Room) socketIO.getInput().readObject();
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
		tfGameMode.setText(room.getGameMode() != null ? room.getGameMode() == Consts.TWO_BALL ? "2 Ball" : "1 Ball" : "2 Ball");
		tfMapName.setText(room.getName());
		ImageIcon icon = new ImageIcon(getClass().getResource(room.getMap().getMapInfo().getImagePreviewPath()));
		Image resize = icon.getImage().getScaledInstance(imagePreview.getWidth(), imagePreview.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(resize);
		imagePreview.setIcon(result);
		tfP1Name.setText(room.getP1().getName());
		if (room.getP2() != null && room.getP2().getName() != null) {
			watingGif.setVisible(false);
			tfP2Name.setText(room.getP2().getName());
			repaint();
		} else {
			watingGif.setVisible(true);
			tfP2Name.setText("");
			repaint();
		}

		// display function for each player
		if (!isHost) {
			btnStart.setVisible(false);
			cbP1BallColor.setEnabled(false);
		} else {
			cbP2BallColor.setEnabled(false);
		}
	}

	public Room getRoom() {
		return room;
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        tfP2Name = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbP2BallColor = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        watingGif = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        tfP1Name = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cbP1BallColor = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfMapName = new javax.swing.JTextField();
        tfGameMode = new javax.swing.JTextField();
        tfGameSpeed = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tfDes = new javax.swing.JTextArea();
        imagePreview = new javax.swing.JLabel();
        tfRoomName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        tfMessage = new javax.swing.JTextField();
        btnSendMessage = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tfChatBox = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        btnStart = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setMaximumSize(new java.awt.Dimension(1200, 800));
        jPanel5.setMinimumSize(new java.awt.Dimension(1200, 800));
        jPanel5.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(167, 166, 165));
        jPanel1.setToolTipText("");

        tfP2Name.setEditable(false);
        tfP2Name.setBackground(new java.awt.Color(1, 1, 1));
        tfP2Name.setForeground(new java.awt.Color(254, 254, 254));

        jLabel9.setFont(new java.awt.Font("LifeCraft", 0, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(236, 202, 47));
        jLabel9.setText("Player 2");

        cbP2BallColor.setBackground(new java.awt.Color(1, 1, 1));
        cbP2BallColor.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        cbP2BallColor.setForeground(new java.awt.Color(254, 254, 254));
        cbP2BallColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BLUE", "RED", "GREEN" }));
        cbP2BallColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbP2BallColorActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("LifeCraft", 0, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(236, 202, 47));
        jLabel12.setText("Ball Color");

        watingGif.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        watingGif.setForeground(new java.awt.Color(253, 31, 31));
        watingGif.setText("wating ...");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(watingGif)
                        .addGap(285, 285, 285))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(tfP2Name, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(cbP2BallColor, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(watingGif))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfP2Name, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbP2BallColor))
                .addContainerGap())
        );

        tfP1Name.setEditable(false);
        tfP1Name.setBackground(new java.awt.Color(1, 1, 1));
        tfP1Name.setForeground(new java.awt.Color(254, 254, 254));

        jLabel11.setFont(new java.awt.Font("LifeCraft", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(236, 202, 47));
        jLabel11.setText("Player 1");

        cbP1BallColor.setBackground(new java.awt.Color(1, 1, 1));
        cbP1BallColor.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        cbP1BallColor.setForeground(new java.awt.Color(254, 254, 254));
        cbP1BallColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RED", "GREEN", "BLUE" }));
        cbP1BallColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbP1BallColorActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("LifeCraft", 0, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(236, 202, 47));
        jLabel14.setText("Ball Color");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addGap(350, 350, 350))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(tfP1Name, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(cbP1BallColor, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfP1Name, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbP1BallColor))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(344, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel1);
        jPanel1.setBounds(10, 10, 710, 550);

        jPanel2.setBackground(new java.awt.Color(226, 199, 172));
        jPanel2.setForeground(new java.awt.Color(236, 202, 47));

        jLabel6.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(236, 202, 47));
        jLabel6.setText("Map Name:");

        jLabel7.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(236, 202, 47));
        jLabel7.setText("GameMode");

        jLabel8.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(236, 202, 47));
        jLabel8.setText("GameSpeed:");

        tfMapName.setEditable(false);
        tfMapName.setBackground(new java.awt.Color(1, 1, 1));
        tfMapName.setForeground(new java.awt.Color(254, 254, 254));
        tfMapName.setOpaque(false);

        tfGameMode.setEditable(false);
        tfGameMode.setBackground(new java.awt.Color(1, 1, 1));
        tfGameMode.setForeground(new java.awt.Color(254, 254, 254));

        tfGameSpeed.setEditable(false);
        tfGameSpeed.setBackground(new java.awt.Color(1, 1, 1));
        tfGameSpeed.setForeground(new java.awt.Color(254, 254, 254));

        jLabel1.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(236, 202, 47));
        jLabel1.setText("Room Name");

        jLabel3.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(236, 202, 47));
        jLabel3.setText("Description:");

        tfDes.setEditable(false);
        tfDes.setBackground(new java.awt.Color(1, 1, 1));
        tfDes.setColumns(20);
        tfDes.setForeground(new java.awt.Color(254, 254, 254));
        tfDes.setLineWrap(true);
        tfDes.setRows(5);
        jScrollPane1.setViewportView(tfDes);

        tfRoomName.setEditable(false);
        tfRoomName.setBackground(new java.awt.Color(1, 1, 1));
        tfRoomName.setForeground(new java.awt.Color(254, 254, 254));
        tfRoomName.setText("TienAnh");

        jButton1.setBackground(new java.awt.Color(1, 1, 1));
        jButton1.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(254, 254, 254));
        jButton1.setText("IP");
        jButton1.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(254, 254, 254)));
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfGameMode)
                            .addComponent(tfMapName)))
                    .addComponent(imagePreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(19, 19, 19)
                        .addComponent(tfGameSpeed))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfRoomName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(imagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(tfMapName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfGameMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tfGameSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.add(jPanel2);
        jPanel2.setBounds(728, 12, 460, 610);

        jPanel3.setBackground(new java.awt.Color(105, 97, 90));

        tfMessage.setBackground(new java.awt.Color(1, 1, 1));
        tfMessage.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        tfMessage.setForeground(new java.awt.Color(254, 254, 254));
        tfMessage.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        tfMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfMessageActionPerformed(evt);
            }
        });
        tfMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfMessageKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfMessageKeyPressed(evt);
            }
        });

        btnSendMessage.setBackground(new java.awt.Color(1, 1, 1));
        btnSendMessage.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        btnSendMessage.setForeground(new java.awt.Color(254, 254, 254));
        btnSendMessage.setText("Send");
        btnSendMessage.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(254, 254, 254)));
        btnSendMessage.setContentAreaFilled(false);
        btnSendMessage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSendMessage.setOpaque(true);
        btnSendMessage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSendMessageMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSendMessageMouseEntered(evt);
            }
        });
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });

        tfChatBox.setEditable(false);
        tfChatBox.setBackground(new java.awt.Color(1, 1, 1));
        tfChatBox.setColumns(20);
        tfChatBox.setFont(new java.awt.Font("LifeCraft", 0, 18)); // NOI18N
        tfChatBox.setForeground(new java.awt.Color(254, 254, 254));
        tfChatBox.setLineWrap(true);
        tfChatBox.setRows(5);
        jScrollPane2.setViewportView(tfChatBox);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(tfMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSendMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfMessage)
                    .addComponent(btnSendMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5.add(jPanel3);
        jPanel3.setBounds(10, 570, 710, 220);

        jPanel4.setBackground(new java.awt.Color(224, 159, 94));

        btnStart.setBackground(new java.awt.Color(1, 1, 1));
        btnStart.setFont(new java.awt.Font("LifeCraft", 0, 24)); // NOI18N
        btnStart.setForeground(new java.awt.Color(254, 254, 254));
        btnStart.setText("Start Game");
        btnStart.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(254, 254, 254)));
        btnStart.setContentAreaFilled(false);
        btnStart.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStart.setOpaque(true);
        btnStart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnStartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnStartMouseEntered(evt);
            }
        });
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(1, 1, 1));
        btnExit.setFont(new java.awt.Font("LifeCraft", 0, 24)); // NOI18N
        btnExit.setForeground(new java.awt.Color(254, 254, 254));
        btnExit.setText("Cancel");
        btnExit.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(254, 254, 254)));
        btnExit.setContentAreaFilled(false);
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.setOpaque(true);
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExitMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExitMouseEntered(evt);
            }
        });
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.add(jPanel4);
        jPanel4.setBounds(730, 630, 460, 160);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/data/image/bacground_1200x800.jpeg"))); // NOI18N
        jLabel2.setText("jLabel2");
        jLabel2.setMaximumSize(new java.awt.Dimension(1200, 800));
        jLabel2.setMinimumSize(new java.awt.Dimension(1200, 800));
        jLabel2.setPreferredSize(new java.awt.Dimension(1200, 800));
        jPanel5.add(jLabel2);
        jLabel2.setBounds(0, 0, 1200, 800);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
		try {
			if (room.getP2() == null) {
				new CustomMessageDialog().showMessage("Waiting for P2 ...");
				return;
			}
			// Send action code
			socketIO.getOutput().writeObject(Consts.START_GAME);
			// Update ball corlor

			this.getRoom().getP1().setBall(new Ball());
			this.getRoom().getP2().setBall(new Ball());

			this.getRoom().getP1().getBall().setColor(Utils.colorMapping(cbP1BallColor.getSelectedItem().toString()));
			this.getRoom().getP2().getBall().setColor(Utils.colorMapping(cbP2BallColor.getSelectedItem().toString()));

			socketIO.getOutput().reset();
			socketIO.getOutput().writeObject(this.getRoom());

		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
		try {
			if (isHost) {
				// Remove Room
				socketIO.getOutput().writeObject(Consts.REMOVE_ROOM);
			} else {
				socketIO.getOutput().writeObject(Consts.OUT_ROOM);
			}
		} catch (IOException ex) {
			Logger.getLogger(PrepareGame.class.getName()).log(Level.SEVERE, null, ex);
		}
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
		sendMessage();
    }//GEN-LAST:event_btnSendMessageActionPerformed

    private void tfMessageKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfMessageKeyTyped

    }//GEN-LAST:event_tfMessageKeyTyped

    private void tfMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfMessageKeyPressed
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			sendMessage();
		}
    }//GEN-LAST:event_tfMessageKeyPressed

    private void cbP2BallColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbP2BallColorActionPerformed

    }//GEN-LAST:event_cbP2BallColorActionPerformed

    private void btnStartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStartMouseEntered
		addHoverEffect(btnStart);
    }//GEN-LAST:event_btnStartMouseEntered

    private void btnExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseEntered
		addHoverEffect(btnExit);
    }//GEN-LAST:event_btnExitMouseEntered

    private void btnSendMessageMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendMessageMouseEntered
		addHoverEffect(btnSendMessage);
    }//GEN-LAST:event_btnSendMessageMouseEntered

    private void btnStartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStartMouseExited
		removeHoverEffect(btnStart);
    }//GEN-LAST:event_btnStartMouseExited

    private void btnExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseExited
		removeHoverEffect(btnExit);
    }//GEN-LAST:event_btnExitMouseExited

    private void btnSendMessageMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendMessageMouseExited
		removeHoverEffect(btnSendMessage);
    }//GEN-LAST:event_btnSendMessageMouseExited

    private void tfMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfMessageActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_tfMessageActionPerformed

    private void cbP1BallColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbP1BallColorActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cbP1BallColorActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		new CustomMessageWithoutLogoDialog(null, false).showMessage(util.Utils.getLocalIP());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
		addHoverEffect(jButton1);
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
        removeHoverEffect(jButton1);
    }//GEN-LAST:event_jButton1MouseExited

	public void addHoverEffect(JButton jButton) {
		if (jButton.isEnabled()) {
			jButton.setForeground(new Color(0, 0, 0));
			jButton.setBackground(new Color(254, 254, 254));
		} else {
			jButton.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		}
	}

	public void removeHoverEffect(JButton jButton) {
		jButton.setForeground(new java.awt.Color(254, 254, 254));
		jButton.setBackground(new Color(0, 0, 0));
	}

	public void sendMessage() {
		String message = tfMessage.getText();
		if (!message.isEmpty()) {
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
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JButton btnStart;
    private javax.swing.JComboBox<String> cbP1BallColor;
    private javax.swing.JComboBox<String> cbP2BallColor;
    private javax.swing.JLabel imagePreview;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea tfChatBox;
    private javax.swing.JTextArea tfDes;
    private javax.swing.JTextField tfGameMode;
    private javax.swing.JTextField tfGameSpeed;
    private javax.swing.JTextField tfMapName;
    private javax.swing.JTextField tfMessage;
    private javax.swing.JTextField tfP1Name;
    private javax.swing.JTextField tfP2Name;
    private javax.swing.JTextField tfRoomName;
    private javax.swing.JLabel watingGif;
    // End of variables declaration//GEN-END:variables
}
