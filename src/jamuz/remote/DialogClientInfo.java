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

package jamuz.remote;

import jamuz.Jamuz;
import jamuz.Playlist;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.awt.Frame;
import javax.swing.DefaultComboBoxModel;

/**
 * JDialog extension to add/modify Stat source
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogClientInfo extends javax.swing.JDialog {

	private final ClientInfo clientInfo;
	
	
	/** Creates new form StatSourceGUI
	 * @param parent
	 * @param modal
	 * @param clientInfo  
	 */
    public DialogClientInfo(java.awt.Frame parent, boolean modal, ClientInfo clientInfo) {
        super(parent, modal);
        initComponents();
		
		jTextFieldLogin.setText(clientInfo.getLogin());
		jTextFieldRootPath.setText(clientInfo.getRootPath());
		jTextName.setText(clientInfo.getName());
		jTextFieldPwd.setText(clientInfo.getPwd());
		jCheckBoxEnabled.setSelected(clientInfo.isEnabled());
		DefaultComboBoxModel comboModelPlaylist = (DefaultComboBoxModel) jComboBoxPlaylist.getModel();
		comboModelPlaylist.removeAllElements();
		comboModelPlaylist.addElement(Jamuz.getPlaylist(0));
		
		for(Playlist playlist : Jamuz.getPlaylists()) {
			comboModelPlaylist.addElement(playlist);
		}
		jComboBoxPlaylist.setSelectedItem(clientInfo.getPlaylist());
	
		this.clientInfo = clientInfo;
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
        jTextFieldPwd = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldLogin = new javax.swing.JTextField();
        jLabelPwd = new javax.swing.JLabel();
        jTextFieldRootPath = new javax.swing.JTextField();
        jLabelRootPath = new javax.swing.JLabel();
        jCheckBoxEnabled = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        setTitle(bundle.getString("DialogClientInfo.title")); // NOI18N
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

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(Inter.get("DialogClientInfo.jLabel3.text")); // NOI18N

        jTextFieldLogin.setEditable(false);

        jLabelPwd.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelPwd.setText(Inter.get("DialogClientInfo.jLabelPwd.text")); // NOI18N

        jTextFieldRootPath.setEditable(false);

        jLabelRootPath.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelRootPath.setText(Inter.get("Label.RootPath")); // NOI18N

        jCheckBoxEnabled.setText(Inter.get("DialogClientInfo.jCheckBoxEnabled.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelPlaylist, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelRootPath, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelPwd, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 171, Short.MAX_VALUE)
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxEnabled)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSave))
                    .addComponent(jTextFieldRootPath)
                    .addComponent(jTextName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBoxPlaylist, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldPwd)
                    .addComponent(jTextFieldLogin, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRootPath)
                    .addComponent(jTextFieldRootPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPwd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPlaylist)
                    .addComponent(jComboBoxPlaylist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonCancel)
                        .addComponent(jButtonSave))
                    .addComponent(jCheckBoxEnabled))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

		String name = jTextName.getText();
		String pwd = jTextFieldPwd.getText(); //TODO: Strength password required & do save hashed if to be used
		if(name.isBlank() || pwd.isBlank()) {  //NOI18N
			Popup.warning(Inter.get("Error.AllFieldsMustBeSet")); //NOI18N
		}
		else {
			this.clientInfo.setName(name);
			this.clientInfo.setPwd(pwd);
			Playlist playlist = (Playlist) jComboBoxPlaylist.getSelectedItem();
			this.clientInfo.getDevice().setIdPlaylist(playlist.getId());
			this.clientInfo.getStatSource().getSource().setRootPath(jTextFieldRootPath.getText());
			this.clientInfo.enable(jCheckBoxEnabled.isSelected());
			
			if(Jamuz.getDb().updateClient(this.clientInfo)) {
				this.dispose();
				PanelRemote.refreshList();
			}
			else {
				Popup.warning(Inter.get("Error.Saving")); //NOI18N
			}
		}
	}//GEN-LAST:event_jButtonSaveActionPerformed

	private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
		this.dispose();
	}//GEN-LAST:event_jButtonCancelActionPerformed

    /**
	 * Open the GUI
	 * @param parent
	 * @param clientInfo 
	 */
    public static void main(Frame parent, final ClientInfo clientInfo) {
        java.awt.EventQueue.invokeLater(() -> {
			DialogClientInfo dialog = new DialogClientInfo(parent, true, clientInfo);
			dialog.setLocationRelativeTo(parent);
			dialog.setVisible(true);
		});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxEnabled;
    private javax.swing.JComboBox jComboBoxPlaylist;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPlaylist;
    private javax.swing.JLabel jLabelPwd;
    private javax.swing.JLabel jLabelRootPath;
    private javax.swing.JTextField jTextFieldLogin;
    private javax.swing.JTextField jTextFieldPwd;
    private javax.swing.JTextField jTextFieldRootPath;
    private static javax.swing.JTextField jTextName;
    // End of variables declaration//GEN-END:variables

}
