/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import consts.Consts;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.ClientState;
import model.Room;
import model.SocketIO;

/**
 *
 * @author tienanh
 */
public class LAN extends javax.swing.JFrame {
	private ClientState player;
    private ArrayList<Room> listRoomWaiting;
    private Room selectedRoom;
	private SocketIO socketIO;
	private boolean isRegisterName;
	private DefaultListModel listMapStr;
	
	public LAN(SocketIO socketIO, ClientState player, boolean isRegisterName) {
        this.player = player;
        this.socketIO = socketIO;
        this.isRegisterName = isRegisterName;
		initComponents();
        initMapData();
	}

    public void initMapData(){
		listMapStr = new DefaultListModel();
		getListRoom();
		this.registerMapListEvent();
		jlistRoomWaiting.setModel(listMapStr);
		if (isRegisterName){
			this.tfPlayerName.setText(player.getName());
		}
    }
	
	public void getListRoom () {
		try {
            socketIO.getOutput().writeObject(Consts.GET_LIST_ROOM);
            listRoomWaiting = (ArrayList<Room>)socketIO.getInput().readObject();
            for (Room room : listRoomWaiting){
				String lastRoomName = room.getName() + " ("+ (room.getP2() != null ? 2 : 1)  +"/2)";
                listMapStr.addElement(lastRoomName);
            }
        } catch (IOException ex) {
            Logger.getLogger(LAN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LAN.class.getName()).log(Level.SEVERE, null, ex);
        } 
	}
    
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfPlayerName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlistRoomWaiting = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfRoomName = new javax.swing.JTextField();
        tfRoomCreator = new javax.swing.JTextField();
        tfRoomSpeed = new javax.swing.JTextField();
        imagePreview = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tfRoomDes = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        exitBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setBackground(new java.awt.Color(186, 186, 186));

        jPanel1.setBackground(new java.awt.Color(167, 166, 165));
        jPanel1.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(254, 254, 254));
        jLabel2.setText("Local Network Games");

        jLabel3.setForeground(new java.awt.Color(254, 254, 254));
        jLabel3.setText("Player Name");

        tfPlayerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPlayerNameActionPerformed(evt);
            }
        });
        tfPlayerName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfPlayerNameKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfPlayerNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPlayerNameKeyReleased(evt);
            }
        });

        jLabel4.setText("Games");

        jlistRoomWaiting.setBackground(new java.awt.Color(117, 117, 117));
        jScrollPane1.setViewportView(jlistRoomWaiting);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(tfPlayerName)
                            .addComponent(jScrollPane1))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfPlayerName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(226, 199, 172));

        jLabel6.setText("Map Name:");

        jLabel7.setText("Room Creator:");

        jLabel8.setText("GameSpeed:");

        tfRoomName.setEditable(false);
        tfRoomName.setOpaque(false);

        tfRoomCreator.setEditable(false);

        tfRoomSpeed.setEditable(false);
        tfRoomSpeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfRoomSpeedActionPerformed(evt);
            }
        });

        jLabel10.setText("Description:");

        tfRoomDes.setColumns(20);
        tfRoomDes.setRows(5);
        jScrollPane2.setViewportView(tfRoomDes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(imagePreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfRoomCreator, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                            .addComponent(tfRoomName)
                            .addComponent(tfRoomSpeed, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tfRoomCreator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tfRoomSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(105, 97, 90));

        jLabel1.setForeground(new java.awt.Color(254, 254, 254));
        jLabel1.setText("Local Mutiplayer");

        jButton3.setText("Load Game");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Create Game");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(224, 159, 94));

        jButton1.setText("Join Game");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        exitBtn.setText("Cancel");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String roomName = jlistRoomWaiting.getSelectedValue();
            
            if (roomName == null) {
                JOptionPane.showMessageDialog(null, "No Room Selected!!!");
                return;
            }
            if (tfPlayerName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No Name Provived!!!");
                return;
            }
			
			if (selectedRoom.getP2() != null) {
				JOptionPane.showMessageDialog(null, "This Room Reach Maximum!!!");
                return;
			}
            
			if (!isRegisterName){
				// Set player name
				socketIO.getOutput().writeObject(Consts.SET_PLAYER_NAME);
				socketIO.getOutput().writeObject(tfPlayerName.getText());
				boolean isValidName = (boolean) socketIO.getInput().readObject();
				if (!isValidName){
					JOptionPane.showMessageDialog(null, "Name is registered");
					return;
				}
				isRegisterName = true;
				this.player.setName(tfPlayerName.getText());
			} else if (!tfPlayerName.getText().trim().toLowerCase().equals(player.getName().trim().toLowerCase())) {
				// Update 
				socketIO.getOutput().writeObject(Consts.UPDATE_PLAYER_NAME);
				socketIO.getOutput().writeObject(tfPlayerName.getText());
				boolean isValidName = (boolean) socketIO.getInput().readObject();
				if (!isValidName){
					JOptionPane.showMessageDialog(null, "Name is registered");
					return;
				}
				this.player.setName(tfPlayerName.getName());
			}
			
			// Join room
            socketIO.getOutput().writeObject(Consts.JOIN_ROOM);
            socketIO.getOutput().writeObject(selectedRoom);
            
            // Receive new room state
            Room joinedRoom = (Room) socketIO.getInput().readObject();
			int status = (Integer) socketIO.getInput().readObject();
            if (status == Consts.ROOM_NOT_EXISTS){
				JOptionPane.showMessageDialog(null, "Room not exsits");				
				DefaultListModel newListMap = new DefaultListModel();
				
				socketIO.getOutput().writeObject(Consts.GET_LIST_ROOM);
				listRoomWaiting = (ArrayList<Room>)socketIO.getInput().readObject();
				for (Room room : listRoomWaiting){
					String lastRoomName = room.getName() + " ("+ (room.getP2() != null ? 2 : 1)  +"/2)";
					newListMap.addElement(lastRoomName);
				}
				jlistRoomWaiting.setModel(newListMap);
				return;
			}
            this.dispose();
            new PrepareGame(socketIO, joinedRoom, false).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(LAN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LAN.class.getName()).log(Level.SEVERE, null, ex);
        }
		
    }//GEN-LAST:event_jButton1ActionPerformed

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
		try {
			this.dispose();
			this.socketIO.getSocket().close();
			new Game().setVisible(true);
		} catch (IOException ex) {
			Logger.getLogger(LAN.class.getName()).log(Level.SEVERE, null, ex);
		}
    }//GEN-LAST:event_exitBtnActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            if (tfPlayerName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No Name Provived!!!");
                return;
            }
			if (!isRegisterName || !tfPlayerName.getText().trim().toLowerCase().equals(player.getName())) {
				socketIO.getOutput().writeObject(Consts.SET_PLAYER_NAME);
				socketIO.getOutput().writeObject(tfPlayerName.getText());
				boolean isValidName = (boolean) socketIO.getInput().readObject();
				if (!isValidName){
					JOptionPane.showMessageDialog(null, "Name is Registered");
					return;
				}
				isRegisterName = true;
				this.player.setName(tfPlayerName.getText());
			}
            this.dispose();
            new CreateRoom(socketIO, player).setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(LAN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		JOptionPane.showMessageDialog(null, "Feature in progress");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tfPlayerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPlayerNameActionPerformed
		
    }//GEN-LAST:event_tfPlayerNameActionPerformed

    private void tfPlayerNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPlayerNameKeyPressed
        
    }//GEN-LAST:event_tfPlayerNameKeyPressed

    private void tfPlayerNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPlayerNameKeyTyped
		
    }//GEN-LAST:event_tfPlayerNameKeyTyped

    private void tfPlayerNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPlayerNameKeyReleased
        player.setName(tfPlayerName.getText());
    }//GEN-LAST:event_tfPlayerNameKeyReleased

    private void tfRoomSpeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfRoomSpeedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfRoomSpeedActionPerformed

	private void registerMapListEvent() {
		jlistRoomWaiting.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
					if (jlistRoomWaiting.getSelectedValue() != null){
						String roomName = jlistRoomWaiting.getSelectedValue().trim().split(" ")[0];
						selectedRoom = getRoomByName(roomName);

						ImageIcon icon = new ImageIcon(getClass().getResource(selectedRoom.getMap().getMapInfo().getImagePreviewPath())); 
						Image resize = icon.getImage().getScaledInstance(imagePreview.getWidth(), imagePreview.getHeight(), Image.SCALE_SMOOTH);
						ImageIcon result = new ImageIcon(resize);
						imagePreview.setIcon(result);

						tfRoomCreator.setText(selectedRoom.getP1().getName());
						tfRoomDes.setText(selectedRoom.getMap().getMapInfo().getDes());
						tfRoomName.setText(selectedRoom.getName());
						tfRoomSpeed.setText(new Integer(selectedRoom.getSpeed()).toString());
					}
                }
            }
        });
	}
    
    private Room getRoomByName(String roomName) {
        for (Room room : listRoomWaiting){
            if (room.getName().equals(roomName)){
                return room;
            }
        }
        return null;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitBtn;
    private javax.swing.JLabel imagePreview;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> jlistRoomWaiting;
    private javax.swing.JTextField tfPlayerName;
    private javax.swing.JTextField tfRoomCreator;
    private javax.swing.JTextArea tfRoomDes;
    private javax.swing.JTextField tfRoomName;
    private javax.swing.JTextField tfRoomSpeed;
    // End of variables declaration//GEN-END:variables
}
