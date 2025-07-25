/*
 * Copyright (C) 2011 phramusca <phramusca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jamuz.gui;

import jamuz.Playlist;
import jamuz.Playlist.Field;
import jamuz.Playlist.Order;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

/**
 * JDialog extension to add/modify Stat source
 * @author phramusca <phramusca@gmail.com>
 */
public class DialogPlaylistOrder extends javax.swing.JDialog {

	private final Order order;
	private final int orderIndex;
	private final Playlist playlist;
	
	/** Creates new form StatSourceGUI
	 * @param parent
	 * @param modal  
     * @param playlist  
     * @param orderIndex  
	 */
    public DialogPlaylistOrder(java.awt.Frame parent, boolean modal, Playlist playlist, int orderIndex) {
        super(parent, modal);
        initComponents();

        this.playlist = playlist;
		this.orderIndex = orderIndex;
        if(orderIndex>-1) {
            this.order = playlist.getOrders().get(orderIndex);
        }
        else {
            this.order = new Order(-1, Field.LASTPLAYED, false);
        }
        
		//Fill combo box
//        jComboBoxPlaylistField.setModel(new DefaultComboBoxModel(Field.values()));
        DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBoxPlaylistField.getModel();
        model.removeAllElements();
        for(Field field : Field.values()) {
            if(!(field.equals(Field.PLAYLIST) || field.equals(Field.TAG))) {
                model.addElement(field);
            }
        }

		//Set field
//		int indexField = Arrays.asList(Field.values()).indexOf(this.order.field);
        int indexField = model.getIndexOf(this.order.getField());
		jComboBoxPlaylistField.setSelectedIndex(indexField);
		
		//Set asc/desc
		jComboBoxPlaylistDesc.setSelectedIndex(this.order.isDesc()?1:0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonCancel = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jComboBoxPlaylistField = new javax.swing.JComboBox();
        jComboBoxPlaylistDesc = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("inter/Bundle"); // NOI18N
        setTitle(bundle.getString("DialogPlaylistOrder.title")); // NOI18N
        setModal(true);

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cancel.png"))); // NOI18N
        jButtonCancel.setText(bundle.getString("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        jButtonSave.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jComboBoxPlaylistField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBoxPlaylistDesc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ASC", "DESC" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jComboBoxPlaylistField, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxPlaylistDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 36, Short.MAX_VALUE)
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSave)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxPlaylistField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxPlaylistDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        
		//Get field
		this.order.setField((Playlist.Field)jComboBoxPlaylistField.getSelectedItem());
		//Get asc/desc
		this.order.setDesc(jComboBoxPlaylistDesc.getSelectedIndex()!=0);
		
        if(this.orderIndex>-1) {
			this.playlist.setOrder(this.orderIndex, this.order);
		}
		else {
			this.playlist.addOrder(this.order);
		}

        //Display orders in list
        PanelPlaylists.displayOrders(this.playlist);
		PanelPlaylists.fillPlayList();
		//Close this GUI
		this.dispose();
	}//GEN-LAST:event_jButtonSaveActionPerformed

	private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
		this.dispose();
	}//GEN-LAST:event_jButtonCancelActionPerformed

    /**
	 * Open the GUI 
     * @param playlist
     * @param orderIndex
	 */
    public static void main(final Playlist playlist, final int orderIndex) {
        java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {

                DialogPlaylistOrder dialog = new DialogPlaylistOrder(new javax.swing.JFrame(), true, playlist, orderIndex);
				
				//Set dialog to mouse location
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b  = a.getLocation();
				int x = (int)b.getX();
				int y = (int)b.getY();
				//size of the screen
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				if(x + dialog.getWidth()>screenSize.getWidth() ) {
					x=(int)screenSize.getWidth() - dialog.getWidth();
				}
				if(y + dialog.getHeight()>screenSize.getHeight()) {
					y=(int)screenSize.getHeight()- dialog.getHeight();
				}
				dialog.setLocation(x, y);
				
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox jComboBoxPlaylistDesc;
    private javax.swing.JComboBox jComboBoxPlaylistField;
    // End of variables declaration//GEN-END:variables

}
