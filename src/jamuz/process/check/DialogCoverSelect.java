/*
 * Copyright (C) 2012 phramusca
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

import jamuz.Jamuz;
import jamuz.gui.PanelCover;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.List;
import jamuz.gui.swing.ListRendererCover;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.SortedListModel;
import jamuz.utils.Inter;

/**
 * A JDialog extension to select an album cover.
 * @author raph
 */
public class DialogCoverSelect extends javax.swing.JDialog {
   
	private final List<Cover> coverList;

    /**
     * Progress Bar to display images retrieving progress
     */
    private final ProgressBar progressBar;
	
	/** Creates new CoverSelectGUI
	 * @param parent
	 * @param modal 
	 * @param myFolderInfo  
	 */
	public DialogCoverSelect(java.awt.Frame parent, boolean modal, FolderInfo myFolderInfo) {
		super(parent, modal);
		initComponents();
		
		coverList=myFolderInfo.getCoverList();

        //Add a list renderer to display albums covers
        jListSelectCover.setBackground(Color.WHITE);
        ListRendererCover renderer = new ListRendererCover();
        jListSelectCover.setCellRenderer(renderer);
        
        model = (SortedListModel<Cover>) jListSelectCover.getModel();
        model.clear();
        
        progressBar = (ProgressBar)jProgressBar;
        
        listCovers();
	}
    
    private final SortedListModel<Cover> model;
    
    private void listCovers() {
         Thread t = new Thread("Thread.CoverSelectGUI.listCovers") {
            @Override
            public void run() {
                progressBar.setup(coverList.size());
                for(final Cover myCover : coverList) {
                    if(myCover.getImage()!=null) {
                        model.add(myCover);
                    }
                    progressBar.progress(Inter.get("Msg.Select.RetrievingCovers")); //NOI18N
                }
//                jListSelectCover.setSelectedIndex(0); //Would be nice but it messes with the display, so better not selecting (no real need anyway)
                progressBar.reset();
            }
        };
        t.start();
        
    }

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar = new jamuz.gui.swing.ProgressBar();
        jButtonOK = new javax.swing.JButton();
        jScrollPaneSelectAlbum = new javax.swing.JScrollPane();
        jListSelectCover = new javax.swing.JList();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        setTitle(bundle.getString("DialogCoverSelect.title")); // NOI18N

        jProgressBar.setString(" "); // NOI18N
        jProgressBar.setStringPainted(true);

        jButtonOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        jButtonOK.setText(bundle.getString("Button.OK")); // NOI18N
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jScrollPaneSelectAlbum.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jListSelectCover.setModel(new SortedListModel());
        jListSelectCover.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneSelectAlbum.setViewportView(jListSelectCover);

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        jButtonCancel.setText(bundle.getString("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneSelectAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOK)
                    .addComponent(jButtonCancel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneSelectAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        //FIXME: Add a "clear image" button AND a "From file" button
        int coverId = jListSelectCover.getSelectedIndex();
        if(coverId>=0) {
            Cover myCover = model.getElementAt(coverId);
            BufferedImage image = myCover.getImage();
            if(image!=null) {
                //Display selected image on MainGUI
                PanelCover mainCoverImg = (PanelCover) DialogCheck.jPanelCheckCoverThumb;
                mainCoverImg.setImage(image);
            }
        }

		this.dispose();
	}//GEN-LAST:event_jButtonOKActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
       this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed
    
	/**
	 * @param myFolderInfo 
     * @param x 
	 */
	public static void main(final FolderInfo myFolderInfo, final Integer x) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {  //NOI18N
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			Jamuz.getLogger().severe(ex.toString());
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				DialogCoverSelect dialog = new DialogCoverSelect(new javax.swing.JFrame(), true, myFolderInfo );
				//Center the dialog
//				dialog.setLocationRelativeTo(dialog.getParent());
                
                //Set dialog to mouse location
                PointerInfo a = MouseInfo.getPointerInfo();
                Point b  = a.getLocation();
//                int x = (int)b.getX();
                int y = (int)b.getY();
                int newX=x-dialog.getWidth();
                //size of the screen
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                if(newX + dialog.getWidth()>screenSize.getWidth() ) {
                    newX=(int)screenSize.getWidth() - dialog.getWidth();
                }
                int newY=y;
                if(newY + dialog.getHeight()>screenSize.getHeight()) {
                    newY=(int)screenSize.getHeight()- dialog.getHeight();
                }
                dialog.setLocation(newX, newY);
                
                dialog.setVisible(true);
			}
		});
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JList jListSelectCover;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPaneSelectAlbum;
    // End of variables declaration//GEN-END:variables
}
