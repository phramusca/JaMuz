/*
 * Copyright (C) 2015 phramusca <phramusca@gmail.com>
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

package jamuz.process.video;

import jamuz.Jamuz;
import jamuz.utils.Inter;
import jamuz.utils.Swing;
import java.awt.Frame;
import javax.swing.JTextField;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DialogVideoOption extends javax.swing.JDialog {

    /**
     * Creates new form PanelVideoOption
     * @param parent
     * @param modal
     */
    public DialogVideoOption(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        //Display current options
        displayOptions();
    }

    private void displayOptions() {
        jTextFieldVideoLocation.setText(Jamuz.getOptions().get("video.dbLocation"));
		jTextFieldVideoRootPath.setText(Jamuz.getOptions().get("video.rootPath"));
        jTextFieldVideoLibraryLocation.setText(Jamuz.getOptions().get("video.location.library"));
        jCheckBoxRemoteRepository.setSelected(Boolean.parseBoolean(Jamuz.getOptions().get("video.library.remote")));
		
		jCheckBoxVideoMoveOnSSH.setSelected(Boolean.parseBoolean(Jamuz.getOptions().get("video.SSH.enabled")));
        jTextFieldVideoSSHip.setText(Jamuz.getOptions().get("video.SSH.IP"));
        jTextFieldVideoSSHuser.setText(Jamuz.getOptions().get("video.SSH.user"));
        jTextFieldVideoSSHpwd.setText(Jamuz.getOptions().get("video.SSH.pwd"));
        
        jTextFieldVideoTMDBlang.setText(Jamuz.getOptions().get("video.themovieDb.language"));
        jTextFieldVideoTMDBuser.setText(Jamuz.getOptions().get("video.themovieDb.user"));
        jTextFieldVideoTMDBpwd.setText(Jamuz.getOptions().get("video.themovieDb.pwd"));
    }
    
    private void setOptions() {
		Jamuz.getOptions().set("video.dbLocation", jTextFieldVideoLocation.getText());
		Jamuz.getOptions().set("video.rootPath", jTextFieldVideoRootPath.getText());
        Jamuz.getOptions().set("video.location.library", jTextFieldVideoLibraryLocation.getText());
		Jamuz.getOptions().set("video.library.remote", Boolean.toString(jCheckBoxRemoteRepository.isSelected()));
		
        Jamuz.getOptions().set("video.SSH.enabled", Boolean.toString(jCheckBoxVideoMoveOnSSH.isSelected()));
        Jamuz.getOptions().set("video.SSH.IP", jTextFieldVideoSSHip.getText());
        Jamuz.getOptions().set("video.SSH.user", jTextFieldVideoSSHuser.getText());
        Jamuz.getOptions().set("video.SSH.pwd", jTextFieldVideoSSHpwd.getText());
        
        Jamuz.getOptions().set("video.themovieDb.language", jTextFieldVideoTMDBlang.getText());
        Jamuz.getOptions().set("video.themovieDb.user", jTextFieldVideoTMDBuser.getText());
        Jamuz.getOptions().set("video.themovieDb.pwd", jTextFieldVideoTMDBpwd.getText());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelVideoLocation = new javax.swing.JLabel();
        jTextFieldVideoLocation = new javax.swing.JTextField();
        jLabelVideoRootPath = new javax.swing.JLabel();
        jTextFieldVideoRootPath = new javax.swing.JTextField();
        jButtonSelectSouce = new javax.swing.JButton();
        jButtonSelectSouce1 = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jPanelTheMovieDb = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldVideoTMDBuser = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldVideoTMDBpwd = new javax.swing.JTextField();
        jTextFieldVideoTMDBlang = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButtonSelectLibraryLocation = new javax.swing.JButton();
        jLabelVideoLibraryLocation = new javax.swing.JLabel();
        jTextFieldVideoLibraryLocation = new javax.swing.JTextField();
        jCheckBoxRemoteRepository = new javax.swing.JCheckBox();
        jTextFieldVideoSSHip = new javax.swing.JTextField();
        jTextFieldVideoSSHuser = new javax.swing.JTextField();
        jTextFieldVideoSSHpwd = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jCheckBoxVideoMoveOnSSH = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Kodi Database"));

        jLabelVideoLocation.setText(Inter.get("Label.Location")); // NOI18N

        jLabelVideoRootPath.setText("Root Path");

        jButtonSelectSouce.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/folder_explore.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("inter/Bundle"); // NOI18N
        jButtonSelectSouce.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonSelectSouce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectSouceActionPerformed(evt);
            }
        });

        jButtonSelectSouce1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/folder_explore.png"))); // NOI18N
        jButtonSelectSouce1.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonSelectSouce1.setEnabled(false);
        jButtonSelectSouce1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectSouce1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelVideoLocation)
                    .addComponent(jLabelVideoRootPath))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldVideoRootPath)
                    .addComponent(jTextFieldVideoLocation, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSelectSouce)
                    .addComponent(jButtonSelectSouce1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelVideoLocation)
                    .addComponent(jTextFieldVideoLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSelectSouce1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldVideoRootPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelVideoRootPath)
                    .addComponent(jButtonSelectSouce))
                .addContainerGap())
        );

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

        jPanelTheMovieDb.setBorder(javax.swing.BorderFactory.createTitledBorder("The Movie Db"));

        jLabel6.setText("User:");

        jLabel7.setText("Password:");

        jLabel5.setText("Language:");

        javax.swing.GroupLayout jPanelTheMovieDbLayout = new javax.swing.GroupLayout(jPanelTheMovieDb);
        jPanelTheMovieDb.setLayout(jPanelTheMovieDbLayout);
        jPanelTheMovieDbLayout.setHorizontalGroup(
            jPanelTheMovieDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTheMovieDbLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldVideoTMDBuser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldVideoTMDBpwd)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldVideoTMDBlang, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelTheMovieDbLayout.setVerticalGroup(
            jPanelTheMovieDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTheMovieDbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTheMovieDbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldVideoTMDBuser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldVideoTMDBpwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldVideoTMDBlang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(java.awt.Color.red);
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Warning: Passwords are stored as plain text in properties file !");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButtonSelectLibraryLocation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/folder_explore.png"))); // NOI18N
        jButtonSelectLibraryLocation.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonSelectLibraryLocation.setEnabled(false);
        jButtonSelectLibraryLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectLibraryLocationActionPerformed(evt);
            }
        });

        jLabelVideoLibraryLocation.setText("Library location");
        jLabelVideoLibraryLocation.setToolTipText("");

        jTextFieldVideoLibraryLocation.setEnabled(false);

        jCheckBoxRemoteRepository.setText("Files are in a remote location");
        jCheckBoxRemoteRepository.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxRemoteRepositoryItemStateChanged(evt);
            }
        });

        jTextFieldVideoSSHip.setEnabled(false);

        jTextFieldVideoSSHuser.setEnabled(false);

        jTextFieldVideoSSHpwd.setEnabled(false);

        jLabel1.setText("IP:");

        jLabel2.setText("User:");

        jLabel3.setText("Password:");

        jCheckBoxVideoMoveOnSSH.setText("Move on SSH");
        jCheckBoxVideoMoveOnSSH.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jCheckBoxRemoteRepository)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jCheckBoxVideoMoveOnSSH)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldVideoSSHip)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(6, 6, 6)
                        .addComponent(jTextFieldVideoSSHuser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldVideoSSHpwd))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelVideoLibraryLocation)
                        .addGap(26, 26, 26)
                        .addComponent(jTextFieldVideoLibraryLocation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSelectLibraryLocation)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxRemoteRepository)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelVideoLibraryLocation)
                    .addComponent(jTextFieldVideoLibraryLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSelectLibraryLocation))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxVideoMoveOnSSH)
                    .addComponent(jTextFieldVideoSSHip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldVideoSSHuser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldVideoSSHpwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSave))
                    .addComponent(jPanelTheMovieDb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelTheMovieDb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonSave))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
  
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        //TODO: Check options validity
        setOptions();
        if(Jamuz.getOptions().save()) {
            this.dispose();
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSelectSouceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectSouceActionPerformed
        getFolder(jTextFieldVideoRootPath, Inter.get("Label.RootPath"));
    }//GEN-LAST:event_jButtonSelectSouceActionPerformed

    private void jButtonSelectLibraryLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectLibraryLocationActionPerformed
        getFolder(jTextFieldVideoLibraryLocation, Inter.get("Label.RootPath"));
    }//GEN-LAST:event_jButtonSelectLibraryLocationActionPerformed

    private void jButtonSelectSouce1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectSouce1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSelectSouce1ActionPerformed

    private void jCheckBoxRemoteRepositoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxRemoteRepositoryItemStateChanged
        enableRemoteRepo(jCheckBoxRemoteRepository.isSelected());
    }//GEN-LAST:event_jCheckBoxRemoteRepositoryItemStateChanged

	private void enableRemoteRepo(boolean enable) {
		jTextFieldVideoLibraryLocation.setEnabled(enable);
		jButtonSelectLibraryLocation.setEnabled(enable);
		jTextFieldVideoSSHip.setEnabled(enable);
		jTextFieldVideoSSHuser.setEnabled(enable);
		jTextFieldVideoSSHpwd.setEnabled(enable);
		jCheckBoxVideoMoveOnSSH.setEnabled(enable);
	}
	
	private void getFolder(JTextField textField, String title) {
        Swing.selectFolder(textField, title, true);
    }
    /**
	 * @param parent
     */
    public static void main(Frame parent) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            Jamuz.getLogger().severe(ex.toString());
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> {
			DialogVideoOption dialog = new DialogVideoOption(parent, true);
			dialog.setLocationRelativeTo(parent);
			dialog.setVisible(true);
		});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSelectLibraryLocation;
    private javax.swing.JButton jButtonSelectSouce;
    private javax.swing.JButton jButtonSelectSouce1;
    private javax.swing.JCheckBox jCheckBoxRemoteRepository;
    private javax.swing.JCheckBox jCheckBoxVideoMoveOnSSH;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelVideoLibraryLocation;
    private javax.swing.JLabel jLabelVideoLocation;
    private javax.swing.JLabel jLabelVideoRootPath;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelTheMovieDb;
    private javax.swing.JTextField jTextFieldVideoLibraryLocation;
    private javax.swing.JTextField jTextFieldVideoLocation;
    private javax.swing.JTextField jTextFieldVideoRootPath;
    private javax.swing.JTextField jTextFieldVideoSSHip;
    private javax.swing.JTextField jTextFieldVideoSSHpwd;
    private javax.swing.JTextField jTextFieldVideoSSHuser;
    private javax.swing.JTextField jTextFieldVideoTMDBlang;
    private javax.swing.JTextField jTextFieldVideoTMDBpwd;
    private javax.swing.JTextField jTextFieldVideoTMDBuser;
    // End of variables declaration//GEN-END:variables
}
