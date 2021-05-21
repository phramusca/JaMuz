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

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableCellListener;
import jamuz.gui.swing.TableColumnModel;
import jamuz.gui.swing.TableValue;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DialogDuplicateReplace extends javax.swing.JDialog {

	private final TableModelReplace model;
	private final TableColumnModel columnModel;
	private final FolderInfo folder;
	private final DuplicateInfo duplicateInfo;
	private final ProgressBar progressBar;
	private final ICallBackReplace callback;
	private Map<String, FolderInfoResult> results;
	
	/**
	 * Creates new form DialogDuplicateReplace
	 * @param parent
	 * @param modal
	 * @param folder
	 * @param duplicateInfo
	 * @param callback
	 */
	public DialogDuplicateReplace(Dialog parent, boolean modal, 
			FolderInfo folder, DuplicateInfo duplicateInfo, ICallBackReplace callback) {
		super(parent, modal);
		initComponents();
		this.duplicateInfo=duplicateInfo;
		this.folder=folder;
		this.callback = callback;
		this.progressBar=(ProgressBar)jProgressBarCheckDialog;
		
		model = new TableModelReplace();
        jTableCheck.setModel(model);
		columnModel = new TableColumnModel();
		
		//Assigning XTableColumnModel to allow show/hide columns
		jTableCheck.setColumnModel(columnModel);
		jTableCheck.createDefaultColumnsFromModel();
		
		TableColumn column;
		//	  "Filename" (new)
		//	  "Filename"
		column = columnModel.getColumn(0);
		column.setMinWidth(100);
		column.setPreferredWidth(200);
		column = columnModel.getColumn(1);
		column.setMinWidth(100);
		column.setPreferredWidth(200);

		//	  "Disc # (new)"
		//	  "Disc #"
		column = columnModel.getColumn(2);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		column = columnModel.getColumn(3);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		
		//	  "Track # (new)"
		//	  "Track #"
		column = columnModel.getColumn(4);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		column = columnModel.getColumn(5);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		
		//	  "Artist (new)"
		//	  "Artist"
		column = columnModel.getColumn(6);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		column = columnModel.getColumn(7);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	  "Title (new)"
		//	  "Title"
		column = columnModel.getColumn(8);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		column = columnModel.getColumn(9);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		 //Set Rating column width
        column = columnModel.getColumn(10);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
		
		//need to change jScrollPane's header height, NOT jTableTags's if not bug !
		Dimension d = jTableCheck.getTableHeader().getPreferredSize();
		d.height = 34;
		jScrollPaneCheckTags.getColumnHeader().setPreferredSize(d);
		
		Action action;
		action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				TableCellListener tcl = (TableCellListener)e.getSource();
				displayMatchTracks(tcl.getColumn());
			}
		};
		TableCellListener tcl = new TableCellListener(jTableCheck, action);	
		
		analyseMatch();
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneCheckTags = new javax.swing.JScrollPane();
        jTableCheck = new jamuz.gui.swing.TableHorizontal();
        jButtonCheckUp = new javax.swing.JButton();
        jButtonCheckDown = new javax.swing.JButton();
        jProgressBarCheckDialog = new jamuz.gui.swing.ProgressBar();
        jButtonCancel = new javax.swing.JButton();
        jButtonReplace = new javax.swing.JButton();
        jButtonSelectAll = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTableCheck.setAutoCreateColumnsFromModel(false);
        jTableCheck.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCheck.setAutoscrolls(false);
        jScrollPaneCheckTags.setViewportView(jTableCheck);

        jButtonCheckUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_up.png"))); // NOI18N
        jButtonCheckUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckUpActionPerformed(evt);
            }
        });

        jButtonCheckDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_down.png"))); // NOI18N
        jButtonCheckDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckDownActionPerformed(evt);
            }
        });

        jProgressBarCheckDialog.setString("null");
        jProgressBarCheckDialog.setStringPainted(true);

        jButtonCancel.setText(Inter.get("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonReplace.setText("Replace");
        jButtonReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReplaceActionPerformed(evt);
            }
        });

        jButtonSelectAll.setText("Select All");
        jButtonSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCheckDown)
                    .addComponent(jButtonCheckUp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCheckTags, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
                    .addComponent(jProgressBarCheckDialog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSelectAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonReplace)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCancel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonReplace)
                    .addComponent(jButtonSelectAll))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarCheckDialog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonCheckUp, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCheckDown, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                    .addComponent(jScrollPaneCheckTags, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCheckUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckUpActionPerformed

        int[] selectedRows = jTableCheck.getSelectedRows();
        int selectedRow;
        ArrayList<Integer> selectedNew= new ArrayList();
        for(int i=0; i<selectedRows.length; i++) {
            selectedRow = selectedRows[i];
            int newRow = selectedRow-1;
            if(newRow>=0) {
                moveCheckRow(selectedRow, newRow);
                selectedNew.add(newRow);
            }
        }
        jTableCheck.clearSelection();
        for(int i : selectedNew) {
            jTableCheck.addRowSelectionInterval(i, i);
        }
    }//GEN-LAST:event_jButtonCheckUpActionPerformed

    private void jButtonCheckDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckDownActionPerformed
        int[] selectedRows = jTableCheck.getSelectedRows();
        int selectedRow;
        ArrayList<Integer> selectedNew= new ArrayList();
        for(int i=selectedRows.length-1; i>=0; i--) {
            selectedRow = selectedRows[i];
            int newRow = selectedRow+1;
            if(newRow>=0) {
                moveCheckRow(selectedRow, newRow);
                selectedNew.add(newRow);
            }
        }
        jTableCheck.clearSelection();
        for(int i : selectedNew) {
            jTableCheck.addRowSelectionInterval(i, i);
        }
    }//GEN-LAST:event_jButtonCheckDownActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

	private void enableGUI(boolean enable) {
		jButtonReplace.setEnabled(enable);
		jButtonCancel.setEnabled(enable);
		jButtonSelectAll.setEnabled(enable);
		jButtonCheckDown.setEnabled(enable);
		jButtonCheckUp.setEnabled(enable);
		jTableCheck.setEnabled(enable);
	}
	
    private void jButtonReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReplaceActionPerformed
		new Thread(() -> {
			enableGUI(false);
			String sourceFullPath = folder.getFullPath();
			String destinationFullPath = duplicateInfo.getFolderInfo().getFullPath();
			List<FileInfoDuplicateReplace> files = model.getFiles();

			int[] selectedRows = jTableCheck.getSelectedRows();
			int selectedRow;

			StringBuilder builder = new StringBuilder("<html>");
			builder.append("Do you want to replace selected files ").append("<br/>")
					.append("from: ").append(sourceFullPath).append("<br/>")
					.append("to : ").append(destinationFullPath).append(" ?")
					.append("</html>");
			int n = JOptionPane.showConfirmDialog(
					null, builder.toString(), //NOI18N
					Inter.get("Label.Confirm"),  //NOI18N
					JOptionPane.YES_NO_OPTION);
			if((n == JOptionPane.YES_OPTION)) {
				progressBar.setup(selectedRows.length);
				for(int i=0; i<selectedRows.length; i++) {
					selectedRow = selectedRows[i];
					FileInfoDuplicateReplace duplicateReplace = files.get(selectedRow);
					if(duplicateReplace.isAudioFile) {
						File sourceFile = new File(FilenameUtils.concat(sourceFullPath, duplicateReplace.getFilename()));
						File destinationFile = new File(FilenameUtils.concat(destinationFullPath, duplicateReplace.getFilename()));
						File replacedFile = null;
						if(!duplicateReplace.filenameDisplay.getValue().equals(TableValue.na)) {
							replacedFile=new File(FilenameUtils.concat(destinationFullPath, duplicateReplace.filenameDisplay.getValue()));
						}
						try {
							if(replacedFile!=null) {
								replacedFile.delete();
							}
							FileUtils.copyFile(sourceFile, destinationFile);
							if(replacedFile!=null) {
								duplicateReplace.updateInDb();
							}
						}
						catch (IOException ex) {
							Logger.getLogger(DialogDuplicateReplace.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
					progressBar.progress(duplicateReplace.getFilename());
				}
				duplicateInfo.getFolderInfo().updateInDb(CheckedFlag.UNCHECKED);
				this.dispose();	
				callback.replaced();
			}
			enableGUI(true);
		}).start();
    }//GEN-LAST:event_jButtonReplaceActionPerformed

    private void jButtonSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectAllActionPerformed
        jTableCheck.selectAll();
    }//GEN-LAST:event_jButtonSelectAllActionPerformed

	private void moveCheckRow(int fromIndex, int toIndex) {
		try {
			model.moveRow(fromIndex, toIndex);
			displayMatchTracks();
		} catch (CloneNotSupportedException ex) {
			Jamuz.getLogger().log(Level.SEVERE, "moveCheckRow", ex); //NOI18N
		}
	}
	
	void displayMatchTracks() {
        analyseMatchTracks();
        model.fireTableDataChanged();
	}
	
	void displayMatchTracks(int colId) {
        //Need to analyse the whole column so that errorLevels are properly set
        analyseMatchTracks(colId);
        model.fireTableDataChanged();
    }
	
	/**
	 * Analyse duplicate againts folder information
     * @param progressBar
	 */
	private void analyseMatch() {
		new Thread(() -> {
			enableGUI(false);
		results = new HashMap<>();
		results.put("filename", new FolderInfoResult());  //NOI18N
        results.put("discNoFull", new FolderInfoResult());  //NOI18N
		results.put("trackNoFull", new FolderInfoResult());  //NOI18N
		results.put("artist", new FolderInfoResult());  //NOI18N
		results.put("title", new FolderInfoResult());  //NOI18N
			
        progressBar.setIndeterminate(Inter.get("Msg.Scan.AnalyzingMatch"));  //NOI18N
		List<FileInfoDisplay> filesDuplicate = duplicateInfo.getFolderInfo().getFilesAudio();
		
        FileInfoDisplay fileDuplicate;
        int i=0;
        model.clear();
		progressBar.setup(folder.getFilesAudio().size());
        for(FileInfoInt fileAudio : folder.getFilesAudio()) {
            if(i<filesDuplicate.size()) {
                fileDuplicate=filesDuplicate.get(i);
            }
            else {
				fileDuplicate = new FileInfoDisplay(new ReleaseMatch.Track(fileAudio.getDiscNo(), fileAudio.getDiscTotal(), 
                        fileAudio.getTrackNo(), fileAudio.getTrackTotal(), 
                        fileAudio.getArtist(), fileAudio.getTitle(), 
                        Long.valueOf(0), ""));
            }

			FileInfoInt fileAudioClone = null;
			try {
				fileAudioClone = (FileInfoInt) fileAudio.clone();
			} catch (CloneNotSupportedException ex) {
				//Should never happen since FileInfoDisplay implements Cloneable
				Jamuz.getLogger().log(Level.SEVERE, "applyMatch()", ex); //NOI18N
			}
            if(fileAudioClone!=null) {
				FileInfoDuplicateReplace fileInfoDuplicateReplace = new FileInfoDuplicateReplace(fileDuplicate);
				fileInfoDuplicateReplace.setFileInfoDisplay(fileAudio);
				fileInfoDuplicateReplace.isAudioFile=true;
				model.addRow(fileInfoDuplicateReplace);
			}
            i++;
			progressBar.progress(fileAudio.toStringQueue());
        }
		progressBar.setIndeterminate("Add potential extra titles");  //NOI18N
        for (int j=i; j<filesDuplicate.size(); j++) {
            fileDuplicate=filesDuplicate.get(j);
            model.addRow(new FileInfoDuplicateReplace(fileDuplicate));
        }
		progressBar.reset();
		displayMatchTracks();
		enableGUI(true);
		}).start();
	}
	
	/**
	 * analyse match tracks
	 */
	public void analyseMatchTracks() {
        //TODO: Get this list from tableModel
		//Restore Folder error levels
        List<Integer> editableColumns = new ArrayList<>();
        editableColumns.add(0);
        editableColumns.add(2);
        editableColumns.add(4);
        editableColumns.add(6);
		editableColumns.add(8);
        for(int colId : editableColumns) {
            results.get(getField(colId)).restoreFolderErrorLevel();  //NOI18N
        }
        //Analyse tracks
		for(int rowId=0; rowId < model.getRowCount(); rowId++) {
			for(int colId : editableColumns) {
                analyseMatchTrack(rowId, colId);
            }
		}
	}
	
	public static String getField(int colId) {
        String field; //NOI18N
        switch (colId) {
			case 0: field="filename";	break; //NOI18N
			case 2: field="discNoFull"; break; //NOI18N
			case 4: field="trackNoFull";	break; //NOI18N
			case 6: field="artist"; break; //NOI18N
			case 8: field="title"; break; //NOI18N
            default: field=""; //NOI18N
		}
        return field;
    }
	
	/**
	 *
	 * @param colId
	 */
	public void analyseMatchTracks(int colId) {
        
        results.get(getField(colId)).restoreFolderErrorLevel();  //NOI18N
		
		for(int rowId=0; rowId < model.getRowCount(); rowId++) {
			analyseMatchTrack(rowId, colId);
		}
    }
    
	//TODO: Mutualize with the same elsewhere
	private void analyseMatchTrack(int rowId, int colId) {
		String field=getField(colId);
        TableValue tagValue = (TableValue) model.getValueAt(rowId, colId+1);
        
        Object newValueObject = model.getValueAt(rowId, colId);
        //TODO: use polymorphism instead
        if (newValueObject instanceof String) {
            String newValue = (String) newValueObject;
            tagValue.setDisplay(results.get(field).analyseTrack(tagValue.getValue(), newValue, field));
        } else if (newValueObject instanceof Float) { //This is for BPM
            Float newValue = (Float) newValueObject;
            Float tagValueFloat;
            try {
                tagValueFloat=Float.parseFloat(tagValue.getValue());
            }
            catch(java.lang.NumberFormatException ex) {
                tagValueFloat=Float.valueOf(0);
            }
            tagValue.setDisplay(results.get(field).analyseTrackBpm(tagValueFloat, newValue));
        } else {
            Popup.error("Unknown class");
        }
	}
	
	/**
	 * @param parent
	 * @param folder
	 * @param duplicateInfo
	 * @param callback
	 */
	public static void main(Dialog parent,
			FolderInfo folder, DuplicateInfo duplicateInfo, ICallBackReplace callback) {
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
			java.util.logging.Logger.getLogger(DialogDuplicateReplace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		DialogDuplicateReplace dialog = new DialogDuplicateReplace(parent, true, folder, 
				duplicateInfo, callback);
		Dimension parentSize = parent.getSize();
		Dimension dimension=new Dimension(
				parentSize.width * 95/100, 
				parentSize.height * 95/100/2);
        dialog.setSize(dimension);
		Point parentLocation = parent.getLocation();
		dialog.setLocation(
				parentLocation.x+(parentSize.width-dimension.width)/2, 
				parentLocation.y+parentSize.height-dimension.height-10);
        dialog.setVisible(true);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonCheckDown;
    private javax.swing.JButton jButtonCheckUp;
    private javax.swing.JButton jButtonReplace;
    private javax.swing.JButton jButtonSelectAll;
    private javax.swing.JProgressBar jProgressBarCheckDialog;
    private javax.swing.JScrollPane jScrollPaneCheckTags;
    private javax.swing.JTable jTableCheck;
    // End of variables declaration//GEN-END:variables
}
