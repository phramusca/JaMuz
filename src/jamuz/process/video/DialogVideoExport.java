/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.process.video.PanelVideo;
import jamuz.Jamuz;
import jamuz.gui.DialogOptions;
import java.io.File;
import javax.swing.JTextField;
import jamuz.utils.Inter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogVideoExport extends javax.swing.JDialog {

    /**
     * Creates new form PanelVideoOption
     * @param parent
     * @param modal
     */
    public DialogVideoExport(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        //Display current options
        displayOptions();
    }

    private void displayOptions() {
		String source = Jamuz.getOptions().get("video.source");
		if(source.equals("")) {
			source = Jamuz.getOptions().get("video.rootPath");
		}
        jTextSource.setText(source);
        jTextDestination.setText(Jamuz.getOptions().get("video.destination"));
    }
    
    private void setOptions() {
        Jamuz.getOptions().set("video.source", jTextSource.getText());
        Jamuz.getOptions().set("video.destination", jTextDestination.getText());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabelVideoRootPath1 = new javax.swing.JLabel();
        jLabelDestination = new javax.swing.JLabel();
        jButtonSelectSouce = new javax.swing.JButton();
        jButtonSelectDestination = new javax.swing.JButton();
        jTextSource = new javax.swing.JTextField();
        jTextDestination = new javax.swing.JTextField();
        jButtonCancel = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Export"));

        jLabelVideoRootPath1.setText(Inter.get("Label.Source")); // NOI18N

        jLabelDestination.setText(Inter.get("Label.Destination")); // NOI18N

        jButtonSelectSouce.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonSelectSouce.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonSelectSouce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectSouceActionPerformed(evt);
            }
        });

        jButtonSelectDestination.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonSelectDestination.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonSelectDestination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectDestinationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelVideoRootPath1)
                    .addComponent(jLabelDestination))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextSource, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addComponent(jTextDestination))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSelectSouce, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonSelectDestination, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSelectSouce)
                    .addComponent(jTextSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelVideoRootPath1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSelectDestination)
                    .addComponent(jTextDestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDestination))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        jButtonCancel.setText(bundle.getString("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/external.png"))); // NOI18N
        jButtonSave.setText(bundle.getString("Button.Export")); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSave))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSelectSouceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectSouceActionPerformed
        getFolder(jTextSource);
    }//GEN-LAST:event_jButtonSelectSouceActionPerformed

    private void getFolder(JTextField textField) {
        String selectedFolder=DialogOptions.selectFolder(textField.getText());
        if(!selectedFolder.equals("")) {  //NOI18N
            textField.setText(selectedFolder+File.separator);
        }
    }
    
    private void jButtonSelectDestinationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectDestinationActionPerformed
        getFolder(jTextDestination);
        PanelVideo.diplayLength();
    }//GEN-LAST:event_jButtonSelectDestinationActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
//FIXME: Use a new option video.library.location that points to local video repository
//By default, set it to root path as should be the same if kodi runs in local
//In export, use this as source by default, but allow to change that source temporary
//Use it for deletion and cleanup

//TODO: Check options validity
        setOptions();
        if(Jamuz.getOptions().save()) {
            PanelVideo.export();
            this.dispose();
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DialogVideoExport dialog = new DialogVideoExport(new javax.swing.JFrame(), true);
                //Center the dialog on screen
                dialog.setLocationRelativeTo(dialog.getParent());
                //Display
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSelectDestination;
    private javax.swing.JButton jButtonSelectSouce;
    private javax.swing.JLabel jLabelDestination;
    private javax.swing.JLabel jLabelVideoRootPath1;
    private javax.swing.JPanel jPanel2;
    private static javax.swing.JTextField jTextDestination;
    private javax.swing.JTextField jTextSource;
    // End of variables declaration//GEN-END:variables
}
