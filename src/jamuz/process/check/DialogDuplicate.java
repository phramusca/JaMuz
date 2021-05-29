/*
 * Copyright (C) 2019 phramusca ( https://github.com/phramusca/ )
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
package jamuz.process.check;

import jamuz.utils.Inter;
import java.awt.Dialog;
import java.awt.Dimension;
import javax.swing.JOptionPane;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DialogDuplicate extends javax.swing.JDialog {

	private final DuplicateInfo duplicateInfo;
	private final ICallBackDuplicateDialog callback;
	private final FolderInfo folder;
	
	/**
	 * Creates new form DialogDuplicate
	 * @param parent
	 * @param modal
	 * @param folder
	 * @param duplicateInfo
	 * @param callback
	 */
	public DialogDuplicate(Dialog parent, boolean modal, 
			FolderInfo folder, DuplicateInfo duplicateInfo, ICallBackDuplicateDialog callback) {
		super(parent, modal);
		initComponents();
		this.callback = callback;
		this.folder = folder;
		this.duplicateInfo=duplicateInfo;
		if(folder!=null) {
			jLabelCheckStatus1.setText("<html><b>Folder being checked: </b></html>");
			jLabelCheckStatus2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/"+folder.action.getRes())));
			jLabelCheckStatus2.setBackground(folder.action.getColor());
			jLabelCheckStatus2.setText(folder.action.toString());
			jLabelCheckStatus3.setText(folder.getRelativePath());
			panelDuplicate1.init(folder, duplicateInfo, (DuplicateInfo duplicateInfo1) -> {
				if(duplicateInfo1!=null) {
					initPanel2(duplicateInfo1);
				}
			});
		}
		if(duplicateInfo!=null) {
			initPanel2(duplicateInfo);
		}
	}
	
	private void initPanel2(DuplicateInfo duplicateInfo) {
		jLabelCheckStatusDuplicate1.setText("<html><b>Potential duplicate: </b></html>");
		jLabelCheckStatusDuplicate2.setText("<html><b>"+duplicateInfo.getFolderInfo().getCheckedFlag().toString()+"</b></html>");
		jLabelCheckStatusDuplicate2.setBackground(duplicateInfo.getFolderInfo().getCheckedFlag().getColor());
		jLabelCheckStatusDuplicate3.setText(duplicateInfo.getFolderInfo().getRelativePath());
		
		panelDuplicate2.init(duplicateInfo);
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        panelDuplicate1 = new jamuz.process.check.PanelDuplicate();
        jPanel3 = new javax.swing.JPanel();
        jLabelCheckStatus3 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jButtonCheckOpen = new javax.swing.JButton();
        jButtonCheckSingleFolder = new javax.swing.JButton();
        jButtonCheckDelete1 = new javax.swing.JButton();
        jButtonReplace = new javax.swing.JButton();
        jLabelCheckStatus1 = new javax.swing.JLabel();
        jLabelCheckStatus2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelDuplicate2 = new jamuz.process.check.PanelDuplicate();
        jPanel4 = new javax.swing.JPanel();
        jLabelCheckStatusDuplicate1 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jButtonCheckOpen1 = new javax.swing.JButton();
        jButtonCheckSingleFolder1 = new javax.swing.JButton();
        jButtonCheckDelete = new javax.swing.JButton();
        jButtonNoDuplicate = new javax.swing.JButton();
        jLabelCheckStatusDuplicate2 = new javax.swing.JLabel();
        jLabelCheckStatusDuplicate3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jSplitPane1.setResizeWeight(0.5);

        jLabelCheckStatus3.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckStatus3.setText(" "); // NOI18N
        jLabelCheckStatus3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckStatus3.setOpaque(true);

        jPanel9.setBackground(java.awt.Color.lightGray);
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonCheckOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder.png"))); // NOI18N
        jButtonCheckOpen.setText(Inter.get("Button.Open")); // NOI18N
        jButtonCheckOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckOpenActionPerformed(evt);
            }
        });

        jButtonCheckSingleFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_refresh.png"))); // NOI18N
        jButtonCheckSingleFolder.setText(Inter.get("Button.Refresh")); // NOI18N
        jButtonCheckSingleFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckSingleFolderActionPerformed(evt);
            }
        });

        jButtonCheckDelete1.setBackground(java.awt.Color.black);
        jButtonCheckDelete1.setForeground(java.awt.Color.white);
        jButtonCheckDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonCheckDelete1.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonCheckDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckDelete1ActionPerformed(evt);
            }
        });

        jButtonReplace.setText("Replace =>");
        jButtonReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReplaceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jButtonCheckOpen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckSingleFolder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckDelete1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonReplace)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCheckOpen)
                    .addComponent(jButtonCheckSingleFolder)
                    .addComponent(jButtonCheckDelete1)
                    .addComponent(jButtonReplace))
                .addGap(0, 0, 0))
        );

        jLabelCheckStatus1.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckStatus1.setText(" "); // NOI18N
        jLabelCheckStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckStatus1.setOpaque(true);

        jLabelCheckStatus2.setBackground(java.awt.Color.white);
        jLabelCheckStatus2.setText(" "); // NOI18N
        jLabelCheckStatus2.setIconTextGap(6);
        jLabelCheckStatus2.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCheckStatus3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabelCheckStatus1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCheckStatus2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelCheckStatus2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelCheckStatus1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCheckStatus3)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDuplicate1, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDuplicate1, javax.swing.GroupLayout.DEFAULT_SIZE, 1235, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jLabelCheckStatusDuplicate1.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckStatusDuplicate1.setText(" "); // NOI18N
        jLabelCheckStatusDuplicate1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckStatusDuplicate1.setOpaque(true);

        jPanel10.setBackground(java.awt.Color.lightGray);
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonCheckOpen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder.png"))); // NOI18N
        jButtonCheckOpen1.setText(Inter.get("Button.Open")); // NOI18N
        jButtonCheckOpen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckOpen1ActionPerformed(evt);
            }
        });

        jButtonCheckSingleFolder1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_refresh.png"))); // NOI18N
        jButtonCheckSingleFolder1.setText(Inter.get("Button.Refresh")); // NOI18N
        jButtonCheckSingleFolder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckSingleFolder1ActionPerformed(evt);
            }
        });

        jButtonCheckDelete.setBackground(java.awt.Color.black);
        jButtonCheckDelete.setForeground(java.awt.Color.white);
        jButtonCheckDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonCheckDelete.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonCheckDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckDeleteActionPerformed(evt);
            }
        });

        jButtonNoDuplicate.setText("Not a duplicate");
        jButtonNoDuplicate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNoDuplicateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonCheckOpen1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckSingleFolder1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNoDuplicate)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCheckOpen1)
                    .addComponent(jButtonCheckSingleFolder1)
                    .addComponent(jButtonCheckDelete)
                    .addComponent(jButtonNoDuplicate))
                .addGap(0, 0, 0))
        );

        jLabelCheckStatusDuplicate2.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckStatusDuplicate2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckStatusDuplicate2.setText(" "); // NOI18N
        jLabelCheckStatusDuplicate2.setOpaque(true);

        jLabelCheckStatusDuplicate3.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckStatusDuplicate3.setText(" "); // NOI18N
        jLabelCheckStatusDuplicate3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckStatusDuplicate3.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCheckStatusDuplicate3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabelCheckStatusDuplicate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCheckStatusDuplicate2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelCheckStatusDuplicate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelCheckStatusDuplicate2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCheckStatusDuplicate3)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDuplicate2, javax.swing.GroupLayout.DEFAULT_SIZE, 927, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDuplicate2, javax.swing.GroupLayout.DEFAULT_SIZE, 1235, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNoDuplicateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNoDuplicateActionPerformed
        int n = JOptionPane.showConfirmDialog(
            null, Inter.get("Question.Check.CleanDuplicate"),  //NOI18N
            Inter.get("Label.Confirm"),  //NOI18N
            JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
			this.dispose();
			callback.notAduplicate();
        }
    }//GEN-LAST:event_jButtonNoDuplicateActionPerformed

    private void jButtonCheckDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckDeleteActionPerformed
        int n = JOptionPane.showConfirmDialog(
            null, Inter.get("Question.Check.DeleteFolderContent"),  //NOI18N
            Inter.get("Label.Confirm"),  //NOI18N
            JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            duplicateInfo.getFolderInfo().delete(panelDuplicate2.getProgressBar());
			panelDuplicate2.init(duplicateInfo.getFolderInfo(), null, (DuplicateInfo di) -> {});
            this.dispose();
			callback.notAduplicate();
        }
    }//GEN-LAST:event_jButtonCheckDeleteActionPerformed

    private void jButtonCheckDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckDelete1ActionPerformed
		this.dispose();
		callback.delete();
    }//GEN-LAST:event_jButtonCheckDelete1ActionPerformed

    private void jButtonReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReplaceActionPerformed
		DialogDuplicateReplace.main(this, folder, duplicateInfo, new ICallBackReplace() {
			@Override
			public void replaced() {
				panelDuplicate2.init(duplicateInfo);
			}
		});
    }//GEN-LAST:event_jButtonReplaceActionPerformed

    private void jButtonCheckOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckOpenActionPerformed
        jamuz.utils.Desktop.openFolder(folder.getFullPath());
    }//GEN-LAST:event_jButtonCheckOpenActionPerformed

    private void jButtonCheckSingleFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckSingleFolderActionPerformed
		panelDuplicate1.reCheck(folder, (DuplicateInfo duplicateInfo1) -> {
			if(duplicateInfo1!=null) {
				initPanel2(duplicateInfo1);
			}
		});	
    }//GEN-LAST:event_jButtonCheckSingleFolderActionPerformed

    private void jButtonCheckOpen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckOpen1ActionPerformed
        jamuz.utils.Desktop.openFolder(duplicateInfo.getFolderInfo().getFullPath());
    }//GEN-LAST:event_jButtonCheckOpen1ActionPerformed

    private void jButtonCheckSingleFolder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckSingleFolder1ActionPerformed
        panelDuplicate2.init(duplicateInfo);
    }//GEN-LAST:event_jButtonCheckSingleFolder1ActionPerformed

	/**
	 * @param parent
	 * @param folder
	 * @param duplicateInfo
	 * @param callback
	 */
	public static void main(Dialog parent, FolderInfo folder, 
			DuplicateInfo duplicateInfo, ICallBackDuplicateDialog callback) {
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
			java.util.logging.Logger.getLogger(DialogDuplicate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		
		DialogDuplicate dialog = new DialogDuplicate(parent, true, folder, 
				duplicateInfo, callback);
		Dimension parentSize = (Dimension) parent.getSize().clone();
        parentSize.height = parentSize.height * 85/100;
        parentSize.width = parentSize.width * 95/100;
        dialog.setSize(parentSize);
		dialog.setLocationRelativeTo(parent);				
        dialog.setVisible(true);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCheckDelete;
    private javax.swing.JButton jButtonCheckDelete1;
    private javax.swing.JButton jButtonCheckOpen;
    private javax.swing.JButton jButtonCheckOpen1;
    private javax.swing.JButton jButtonCheckSingleFolder;
    private javax.swing.JButton jButtonCheckSingleFolder1;
    private javax.swing.JButton jButtonNoDuplicate;
    private javax.swing.JButton jButtonReplace;
    private javax.swing.JLabel jLabelCheckStatus1;
    private javax.swing.JLabel jLabelCheckStatus2;
    private javax.swing.JLabel jLabelCheckStatus3;
    private javax.swing.JLabel jLabelCheckStatusDuplicate1;
    private javax.swing.JLabel jLabelCheckStatusDuplicate2;
    private javax.swing.JLabel jLabelCheckStatusDuplicate3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSplitPane jSplitPane1;
    private jamuz.process.check.PanelDuplicate panelDuplicate1;
    private jamuz.process.check.PanelDuplicate panelDuplicate2;
    // End of variables declaration//GEN-END:variables
}
