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

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DialogDuplicate extends javax.swing.JDialog {

	/**
	 * Creates new form DialogDuplicate
	 * @param parent
	 * @param modal
	 * @param folder
	 * @param duplicateInfo
	 */
	public DialogDuplicate(java.awt.Frame parent, boolean modal, FolderInfo folder, DuplicateInfo duplicateInfo) {
		super(parent, modal);
		initComponents();
		
		if(folder!=null) {
			panelDuplicate1.init(folder);
		}
		if(duplicateInfo!=null) {
			panelDuplicate2.init(duplicateInfo);
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

        jSplitPane1 = new javax.swing.JSplitPane();
        panelDuplicate1 = new jamuz.process.check.PanelDuplicate();
        panelDuplicate2 = new jamuz.process.check.PanelDuplicate();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jSplitPane1.setLeftComponent(panelDuplicate1);
        jSplitPane1.setRightComponent(panelDuplicate2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1308, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param parentSize
	 * @param folder
	 * @param duplicateInfo
	 */
	public static void main(Dimension parentSize, FolderInfo folder, DuplicateInfo duplicateInfo) {
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
		
		//</editor-fold>

		DialogDuplicate dialog = new DialogDuplicate(new JFrame(), true, folder, duplicateInfo);
				
        //Set dialog size to x% of parent size
        parentSize.height = parentSize.height * 85/100;
        parentSize.width = parentSize.width * 95/100;
        dialog.setSize(parentSize);
        //Center the dialog on screen
        dialog.setLocationRelativeTo(dialog.getParent());
        //Display
        dialog.setVisible(true);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    private jamuz.process.check.PanelDuplicate panelDuplicate1;
    private jamuz.process.check.PanelDuplicate panelDuplicate2;
    // End of variables declaration//GEN-END:variables
}
