/*
 * Copyright (C) 2016 phramusca ( https://github.com/phramusca/JaMuz/ )
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
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableModel;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogVideoCleanupConfirm extends javax.swing.JDialog {

	private static TableModel tableModel;
	private static ArrayList<FileInfoVideo> filesToCleanup;
	protected static ProgressBar progressBar;
	/**
	 * Creates new form DialogCleanup
	 * @param parent
	 * @param modal
	 * @param filesToanalyze
	 * @param nbSeasonToKeep
	 * @param nbEpisodeToKeep
	 * @param keepCanceled
	 */
	public DialogVideoCleanupConfirm(java.awt.Frame parent, boolean modal, 
			List<VideoAbstract> filesToanalyze, int nbSeasonToKeep, 
			int nbEpisodeToKeep, boolean keepCanceled) {
		super(parent, modal);
		initComponents();
		
		progressBar = (ProgressBar)jProgressBarVideoCleanup;
		
		tableModel = (TableModel) jTableVideoCleanupConfirm.getModel();
		//Set table model
		String[] columnNames = {"Open", "Reason", "S", "E", "Title", "Filename"};  //NOI18N
		Object[][] data = {
			{"Default", "Default", "Default", "Default", "Default", "Default"}  //NOI18N
		};
		tableModel.setModel(columnNames, data);
		//clear the table
        tableModel.clear();
		jTableVideoCleanupConfirm.setEnabled(true);
		
		//Adding columns from model. Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableVideoCleanupConfirm.createDefaultColumnsFromModel();
		// Open folder Button
        TableColumn columnButton = jTableVideoCleanupConfirm.getColumnModel().getColumn(0);
        columnButton.setMinWidth(50);
        columnButton.setMaxWidth(60);
        JButton button = new JButton(""); //NOI18N
		button.setBackground(Color.GRAY);
        columnButton.setCellRenderer((JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column1) -> button);
        columnButton.setCellEditor(new ButtonOpenVideo());
        tableModel.setEditable(new Integer[] {0});
		setColumn(1, 200, 250);
		int maxWidth=30;
        setColumn(2, maxWidth, maxWidth);
        setColumn(3, maxWidth, maxWidth);
		setColumn(4, 200);
//		setColumn(5, 300);
	
		new Thread() {
			@Override
			public void run() {
				progressBar.setup(filesToanalyze.size());
				ArrayList<FileInfoVideo> filesAnalyzed = new ArrayList<>();
				for(VideoAbstract video : filesToanalyze) {
					progressBar.progress(video.getTitle());
					if(!video.isMovie()) {
						filesAnalyzed.addAll(video.getFilesToCleanup(nbSeasonToKeep, 
								nbEpisodeToKeep, keepCanceled)) ;
					}
				}
				progressBar.reset();
				filesToCleanup = filesAnalyzed;
				jTableVideoCleanupConfirm.setRowSorter(null);
				tableModel.clear();
				progressBar.setup(filesToCleanup.size());
				for(FileInfoVideo fileInfoVideo : filesToCleanup) {
					progressBar.progress(fileInfoVideo.getTitle());
					addRow(fileInfoVideo);
				}
				progressBar.reset();
				//Enable row tableSorter (cannot be done if model is empty)
				if(tableModel.getRowCount()>0) {
					jTableVideoCleanupConfirm.setAutoCreateRowSorter(true);
				}
				else {
					jTableVideoCleanupConfirm.setAutoCreateRowSorter(false);
				}
				jTableVideoCleanupConfirm.setEnabled(true);
			}
		}.start();
	}
	
	private static void addRow(FileInfoVideo fileInfoVideo) {
		//"Open", "Reason", "S", "E", "Title", "Filename"
		Object[] donnee = new Object[]{
			"//"+FilenameUtils.concat(
			Boolean.parseBoolean(Jamuz.getOptions().get("video.library.remote"))?
					Jamuz.getOptions().get("video.location.library")
					:Jamuz.getOptions().get("video.rootPath"), 
			fileInfoVideo.getRelativePath()),
			fileInfoVideo.getSourceName(),
			fileInfoVideo.getSeasonNumber(), 
			fileInfoVideo.getEpisodeNumber(), 
			fileInfoVideo.getTitle(),
			fileInfoVideo.getFilename(),
			};
		tableModel.addRow(donnee);
	}
	
	private void setColumn(int index, int width) {
		setColumn(index, width, -1);
	}
	
	private void setColumn(int index, int width, int maxWidth) {
		TableColumn column = jTableVideoCleanupConfirm.getColumnModel().getColumn(index);
		column.setMinWidth(width);
        column.setPreferredWidth(width);
		column.setWidth(width);
		if(maxWidth>=0)	column.setMaxWidth(maxWidth);
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonVideoDoCleanup = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPaneVideoConfirmCleanup = new javax.swing.JScrollPane();
        jTableVideoCleanupConfirm = new javax.swing.JTable();
        jProgressBarVideoCleanup = new jamuz.gui.swing.ProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("List of files to be deleted");

        jButtonVideoDoCleanup.setText("Cleanup");
        jButtonVideoDoCleanup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVideoDoCleanupActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jTableVideoCleanupConfirm.setAutoCreateColumnsFromModel(false);
        jTableVideoCleanupConfirm.setModel(new jamuz.gui.swing.TableModel());
        jTableVideoCleanupConfirm.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPaneVideoConfirmCleanup.setViewportView(jTableVideoCleanupConfirm);

        jProgressBarVideoCleanup.setString("");
        jProgressBarVideoCleanup.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jProgressBarVideoCleanup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVideoDoCleanup))
                    .addComponent(jScrollPaneVideoConfirmCleanup, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneVideoConfirmCleanup, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVideoDoCleanup)
                    .addComponent(cancelButton)
                    .addComponent(jProgressBarVideoCleanup, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void jButtonVideoDoCleanupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVideoDoCleanupActionPerformed
		new Thread(){
			@Override
			public void run() {
				cancelButton.setEnabled(false);
				jButtonVideoDoCleanup.setEnabled(false);
				progressBar.setup(filesToCleanup.size());
				for(FileInfoVideo fileInfoVideo : filesToCleanup) {
					File file = new File(FilenameUtils.concat(
					Boolean.parseBoolean(Jamuz.getOptions().get("video.library.remote"))?
							Jamuz.getOptions().get("video.location.library")
							:Jamuz.getOptions().get("video.rootPath"), 
					fileInfoVideo.getRelativeFullPath()));
					progressBar.progress(fileInfoVideo.getRelativeFullPath());
					Jamuz.getLogger().log(Level.INFO, "Deleting {0}", file.getAbsolutePath());
					file.delete();	

					//TODO: Remove from db (and send db back at some point)
				}
				progressBar.reset();
				dispose();
			}
		}.start();
    }//GEN-LAST:event_jButtonVideoDoCleanupActionPerformed

	/**
	 * @param filesToanalyze
	 * @param nbSeasonToKeep
	 * @param nbEpisodeToKeep
	 * @param keepCanceled
	 */
	public static void main(List<VideoAbstract> filesToanalyze, 
			int nbSeasonToKeep, int nbEpisodeToKeep,
			boolean keepCanceled) {
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
			java.util.logging.Logger.getLogger(DialogVideoCleanupConfirm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(() -> {
			DialogVideoCleanupConfirm dialog = new DialogVideoCleanupConfirm(
					new javax.swing.JFrame(), true, filesToanalyze, 
					nbSeasonToKeep, nbEpisodeToKeep, keepCanceled);
			//Center the dialog
			dialog.setLocationRelativeTo(dialog.getParent());
			dialog.setVisible(true);
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButtonVideoDoCleanup;
    private javax.swing.JProgressBar jProgressBarVideoCleanup;
    private javax.swing.JScrollPane jScrollPaneVideoConfirmCleanup;
    private static javax.swing.JTable jTableVideoCleanupConfirm;
    // End of variables declaration//GEN-END:variables
}
