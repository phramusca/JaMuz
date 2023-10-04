/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.Jamuz;
import jamuz.Playlist;
import jamuz.process.sync.Device;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.Swing;
import java.awt.Dialog;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import org.apache.commons.io.FilenameUtils;

/**
 * JDialog extension to add/modify Stat source
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogDevice extends javax.swing.JDialog {

	private final Device device;
	
	
	/** Creates new form StatSourceGUI
	 * @param parent
	 * @param modal
	 * @param device  
	 */
    public DialogDevice(Dialog parent, boolean modal, Device device) {
        super(parent, modal);
        initComponents();
		
		jTextName.setText(device.getName());
		
		DefaultComboBoxModel comboModelPlaylist = (DefaultComboBoxModel) jComboBoxPlaylist.getModel();
		comboModelPlaylist.removeAllElements();
		//Add the default (Full library) playlist
		comboModelPlaylist.addElement(Jamuz.getPlaylist(0));
		
		for(Playlist playlist : Jamuz.getPlaylists()) {
			comboModelPlaylist.addElement(playlist);
		}
		jComboBoxPlaylist.setSelectedItem(device.getPlaylist());
	
		jTextFieldSource.setText(device.getSource());
		jTextFieldDestination.setText(device.getDestination());
				
		this.device = device;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelName = new javax.swing.JLabel();
        jTextName = new javax.swing.JTextField();
        jButtonCancel = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jLabelPlaylist = new javax.swing.JLabel();
        jComboBoxPlaylist = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldDestination = new javax.swing.JTextField();
        jButtonSelectDest = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldSource = new javax.swing.JTextField();
        jButtonSelectSource = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        setTitle(bundle.getString("DialogDevice.title")); // NOI18N
        setModal(true);

        jLabelName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelName.setText(bundle.getString("Label.Name")); // NOI18N

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        jButtonCancel.setText(bundle.getString("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        jButtonSave.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jLabelPlaylist.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelPlaylist.setText(bundle.getString("Label.Playlist")); // NOI18N

        jComboBoxPlaylist.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText(bundle.getString("Label.Destination")); // NOI18N

        jButtonSelectDest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonSelectDest.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonSelectDest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectDestActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(bundle.getString("DialogOptions.jLabel2.text")); // NOI18N

        jTextFieldSource.setEditable(false);

        jButtonSelectSource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonSelectSource.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonSelectSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectSourceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabelPlaylist, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 327, Short.MAX_VALUE)
                        .addComponent(jButtonCancel)
                        .addGap(10, 10, 10)
                        .addComponent(jButtonSave))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldDestination, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSelectDest))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldSource, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSelectSource))
                    .addComponent(jTextName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBoxPlaylist, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPlaylist)
                    .addComponent(jComboBoxPlaylist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSelectSource)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSelectDest)
                    .addComponent(jLabel1))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonSave))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
		String name = jTextName.getText();
		String source = jTextFieldSource.getText();
		String destination = jTextFieldDestination.getText();
		
		if(name.isBlank() || source.isBlank() || destination.isBlank()) {  //NOI18N
			Popup.warning(Inter.get("Error.AllFieldsMustBeSet")); //NOI18N
		}
		else {
			this.device.setName(name);
			this.device.setSource(FilenameUtils.normalizeNoEndSeparator(source)+File.separator);
			this.device.setDestination(FilenameUtils.normalizeNoEndSeparator(destination)+File.separator);
			Playlist playlist = (Playlist) jComboBoxPlaylist.getSelectedItem();
			this.device.setIdPlaylist(playlist.getId());
			
			if(Jamuz.getDb().updateDevice(this.device)) {
				this.dispose();
				DialogOptions.displayDevices();
			}
			else {
				Popup.warning(Inter.get("Error.Saving")); //NOI18N
			}
		}
	}//GEN-LAST:event_jButtonSaveActionPerformed

	private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
		this.dispose();
	}//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonSelectDestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectDestActionPerformed
		Swing.selectFolder(jTextFieldDestination, Inter.get("Label.Destination"), false);
    }//GEN-LAST:event_jButtonSelectDestActionPerformed

    private void jButtonSelectSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectSourceActionPerformed
		Swing.selectFolder(jTextFieldSource, Inter.get("Label.Source"), false);
    }//GEN-LAST:event_jButtonSelectSourceActionPerformed

    /**
	 * Open the GUI
	 * @param parent
	 * @param device 
	 */
    public static void main(Dialog parent, final Device device) {
        java.awt.EventQueue.invokeLater(() -> {
			DialogDevice dialog = new DialogDevice(parent, true, device);
			dialog.setLocationRelativeTo(parent);
			dialog.setVisible(true);
		});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSelectDest;
    private javax.swing.JButton jButtonSelectSource;
    private javax.swing.JComboBox jComboBoxPlaylist;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPlaylist;
    private javax.swing.JTextField jTextFieldDestination;
    private javax.swing.JTextField jTextFieldSource;
    private static javax.swing.JTextField jTextName;
    // End of variables declaration//GEN-END:variables

}
