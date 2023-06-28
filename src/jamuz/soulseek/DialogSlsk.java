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
package jamuz.soulseek;

import jamuz.Jamuz;
import jamuz.Option;
import jamuz.gui.PanelMain;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableColumnModel;
import jamuz.process.check.Location;
import jamuz.utils.DateTime;
import jamuz.utils.FileSystem;
import jamuz.utils.Swing;
import java.awt.Dialog;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogSlsk extends javax.swing.JDialog {

    private final TableModelSlsk tableModelResults;
	private final TableColumnModel columnModel;
	private final Slsk soulseek;
	private ProgressBar progressBar;
	
	private static final int[] SEARCH_SPECIFIC_COLUMNS = new int[] {1, 2, 4, 5, 6, 7};
	
    /**
     * Creates new form DialogSoulseek
     * @param parent
     * @param modal
	 * @param query
     */
    public DialogSlsk(Dialog parent, boolean modal, String query) {
        super(parent, modal);
        initComponents();

		jTextFieldQuery.setText(query);
		
		tableModelResults = new TableModelSlsk();
		jTableSoulseek.setModel(tableModelResults);
		columnModel = new TableColumnModel();
		//Assigning XTableColumnModel to allow show/hide columns
		jTableSoulseek.setColumnModel(columnModel);
		jTableSoulseek.createDefaultColumnsFromModel();
				
		setColumn(0, 140);	// Date
		setColumn(1, 40);	// # downloaded
        setColumn(2, 40);	// # files
		setColumn(3, 80);	// Status
		setColumn(4, 50);	// BitRate
		setColumn(5, 50);	// Size
		setColumn(6, 50);	// Speed
		setColumn(7, 150);	// Username
		setColumn(8, 600);	// Path
		
		progressBar = (ProgressBar)jProgressBarSlsk;

		Location destinationTemp = new Location("location.slsk");
		
		soulseek = new Slsk(new ICallBackSlsk() {
			@Override
			public void progress(String line) {
				jTextAreaLog.append(line+"\n");
				jTextAreaLog.setCaretPosition(jTextAreaLog.getDocument().getLength()-1);
			}

			@Override
			public void error(String message) {
				jLabelFolder.setText("<html>" + DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN)+" | <font color=\"red\">"+message+"</font></html>");
				jTextAreaLog.append(message+"\n");
				jTextAreaLog.setCaretPosition(jTextAreaLog.getDocument().getLength()-1);
			}
			
			@Override
			public void completed() {
				jButtonCancel.setEnabled(false);
				Location finalDestination = new Location("location.add");
				if(finalDestination.check()) {
					progressBar.setup(tableModelResults.getRowCount());
					for (TableEntrySlsk result : tableModelResults.getRows()) {
						File sourceFile = new File(result.getPath());
						String relativeSourcePath = result.getPath().substring(jTextFieldDestinationTemp.getText().length());
						String destPath = FilenameUtils.concat(finalDestination.getValue(), relativeSourcePath);
						File destFile = new File(destPath);
						FileSystem.moveFile(sourceFile, destFile);
						result.setStatus(TableEntrySlsk.Status.Moved);
						result.setPath(destPath);
						replaceResult(result, result.getId(), "");
					}
					SlskDownload sd = new SlskDownload(
							jTextFieldQuery.getText(), 
							soulseek.getFolderBeingDownloaded().getNbOfFiles(), 
							soulseek.getFolderBeingDownloaded().getPath(), 
							soulseek.getFolderBeingDownloaded().getUsername(), 
							destinationTemp.getValue());
					SlskDownload read = sd.read();
					if(read!=null) {
						read.cleanup(destinationTemp.getValue());
					}
					int n = JOptionPane.showConfirmDialog(DialogSlsk.this, 
								"Download completed.\nDo you want to close the window ?",  //NOI18N
								soulseek.getFolderBeingDownloaded().getPath(),  //NOI18N
								JOptionPane.YES_NO_OPTION);
					soulseek.cancel();
					if (n == JOptionPane.YES_OPTION) {
						dispose();
					}
				}
			}

			@Override
			public void addResult(TableEntrySlsk result, String progressMsg) {
				if(!progressMsg.isBlank()) {
					progressBar.progress(progressMsg);
				}
				tableModelResults.addRow(result);
			}

			@Override
			public void replaceResult(TableEntrySlsk result, int row, String progressMsg) {
				if(row <= jTableSoulseek.getRowCount()) {
					progressBar.progress(progressMsg);
					tableModelResults.replaceRow(result, row);
				}
			}

			@Override
			public void enableDownload(Map<String, SlskResultFolder> results) {
				new Thread() {
					@Override
					public void run() {
						for (Map.Entry<String, SlskResultFolder> entry : results.entrySet()) {
							String key = entry.getKey();
							SlskResultFolder soulseekResultFolder = entry.getValue();
							//TODO Soulseek ! Display files in a treeViewTable
							tableModelResults.addRow(
									new TableEntrySlsk(key, 
											TableEntrySlsk.Status.Folder, 
											soulseekResultFolder.folder, 
											soulseekResultFolder.user, 
											soulseekResultFolder.files.size(), 
											soulseekResultFolder.bitrateInKbps,
											soulseekResultFolder.sizeInMb,
											soulseekResultFolder.speedInKbPerSecond));
						}						
						
						for (TableEntrySlsk result : tableModelResults.getRows()) {
							if(result.getStatus().equals(TableEntrySlsk.Status.Folder)) {
								SlskDownload sd = new SlskDownload(
										jTextFieldQuery.getText(), 
										result.getNbOfFiles(), 
										result.getPath(), 
										result.getUser(), 
										destinationTemp.getValue());
								
								SlskDownload read = sd.read();
								if(read!=null) {
									result.setNbDownloaded(read.nbDownloaded);
								}
							}
						}
						enableRowSorter(true);
						tableModelResults.fireTableDataChanged();
						jTableSoulseek.setRowSelectionInterval(0, 0);
						jButtonDownload.setEnabled(true);
						jButtonCancel.setEnabled(true);
					}
				}.start();
			}

			@Override
			public void enableSearch() {
				enableGui(true);
			}

			@Override
			public void noFlacFound() {
				if(destinationTemp.check()) {
					jButtonSearchMp3.setEnabled(true);
					startSoulseekSearch(Slsk.Mode.mp3);
				}
			}
		});
		
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                soulseek.cancel();
            }
        });
		
		
		if(destinationTemp.check()) {
			jTextFieldDestinationTemp.setText(destinationTemp.getValue());
			startSoulseekSearch(Slsk.Mode.flac);
		}
    }
	
	private void setColumn(int index, int width) {
        TableColumn column = columnModel.getColumn(index);
		column.setMinWidth(width);
        column.setPreferredWidth(width);
        column.setMaxWidth(width*3);
    }
	
	/**
	 *
	 * @param enable
	 */
	public void enableRowSorter(boolean enable) {
		if(enable) {
			//Enable row tableSorter (cannot be done if model is empty)
			if(tableModelResults.getRowCount()>0) {
				//Enable auto sorter
				jTableSoulseek.setAutoCreateRowSorter(true);
				//Sort by action, result
				TableRowSorter<TableModelSlsk> tableSorter = new TableRowSorter<>(tableModelResults);
				jTableSoulseek.setRowSorter(tableSorter);
				List <RowSorter.SortKey> sortKeys = new ArrayList<>();

//				setColumn(0, 110);	// Date
//				setColumn(1, 30);	// # downloaded
//				setColumn(2, 30);	// # files
//				setColumn(3, 80);	// Status
//				setColumn(4, 130);	// BitRate
//				setColumn(5, 95);	// Size
//				setColumn(6, 140);	// Speed
//				setColumn(7, 90);	// Username
//				setColumn(8, 300);	// Path
				
				sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING)); // nb of downloaded
				sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING)); // nb of files
				sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING)); // BitRate
				sortKeys.add(new RowSorter.SortKey(6, SortOrder.DESCENDING)); // Speed

				tableSorter.setSortKeys(sortKeys);
				jTableSoulseek.getSelectionModel().setSelectionInterval(0, 0);
			}
			else {
				jTableSoulseek.setAutoCreateRowSorter(false);
			}
		}
		else {
			jTableSoulseek.setAutoCreateRowSorter(false);
			jTableSoulseek.setRowSorter(null);
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

        jScrollPaneCheckTags3 = new javax.swing.JScrollPane();
        jTableSoulseek = new jamuz.gui.swing.TableHorizontal();
        jTextFieldQuery = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jButtonDownload = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonSearchFlac = new javax.swing.JButton();
        jTextFieldDestinationTemp = new javax.swing.JTextField();
        jButtonOptionSelectFolderNew = new javax.swing.JButton();
        jLabelFolder = new javax.swing.JLabel();
        jProgressBarSlsk = new jamuz.gui.swing.ProgressBar();
        jButtonSearchMp3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTableSoulseek.setAutoCreateColumnsFromModel(false);
        jTableSoulseek.setModel(new jamuz.soulseek.TableModelSlsk());
        jTableSoulseek.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableSoulseek.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSoulseekMouseClicked(evt);
            }
        });
        jScrollPaneCheckTags3.setViewportView(jTableSoulseek);

        jTextFieldQuery.setToolTipText("");

        jTextAreaLog.setEditable(false);
        jTextAreaLog.setColumns(20);
        jTextAreaLog.setRows(5);
        jScrollPane1.setViewportView(jTextAreaLog);

        jButtonDownload.setText("Download");
        jButtonDownload.setEnabled(false);
        jButtonDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownloadActionPerformed(evt);
            }
        });

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonCancel.setText(bundle.getString("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonSearchFlac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/search_plus.png"))); // NOI18N
        jButtonSearchFlac.setText(bundle.getString("Button.Search.Flac")); // NOI18N
        jButtonSearchFlac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchFlacActionPerformed(evt);
            }
        });

        jTextFieldDestinationTemp.setEditable(false);
        jTextFieldDestinationTemp.setToolTipText("");

        jButtonOptionSelectFolderNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonOptionSelectFolderNew.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonOptionSelectFolderNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSelectFolderNewActionPerformed(evt);
            }
        });

        jLabelFolder.setToolTipText("");

        jProgressBarSlsk.setMinimumSize(new java.awt.Dimension(1, 23));
        jProgressBarSlsk.setString(" "); // NOI18N
        jProgressBarSlsk.setStringPainted(true);

        jButtonSearchMp3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/search_plus.png"))); // NOI18N
        jButtonSearchMp3.setText(bundle.getString("Button.Search.Mp3")); // NOI18N
        jButtonSearchMp3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchMp3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonOptionSelectFolderNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDestinationTemp)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancel))
                    .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldQuery, javax.swing.GroupLayout.DEFAULT_SIZE, 1025, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSearchFlac)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSearchMp3))
                    .addComponent(jProgressBarSlsk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldQuery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearchFlac)
                    .addComponent(jButtonSearchMp3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarSlsk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonDownload)
                    .addComponent(jButtonCancel)
                    .addComponent(jTextFieldDestinationTemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonOptionSelectFolderNew))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSearchFlacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchFlacActionPerformed
		startSoulseekSearch(Slsk.Mode.flac);
    }//GEN-LAST:event_jButtonSearchFlacActionPerformed

	private void enableGui(boolean enable) {
		jButtonSearchFlac.setEnabled(enable);
		jButtonSearchMp3.setEnabled(enable);
		jTextFieldQuery.setEnabled(enable);
	}
	
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
		jButtonDownload.setEnabled(false);
		jButtonCancel.setEnabled(false);
		soulseek.cancel();
		enableGui(true);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownloadActionPerformed
        startSoulseekDownload();
    }//GEN-LAST:event_jButtonDownloadActionPerformed

	private void startSoulseekDownload() {
		int selectedRow = jTableSoulseek.getSelectedRow(); 			
		if(selectedRow>=0) { 
			Location destinationTemp = new Location("location.slsk");
			if(destinationTemp.check()) {
				jButtonDownload.setEnabled(false);
				selectedRow = jTableSoulseek.convertRowIndexToModel(selectedRow); 
				TableEntrySlsk result = tableModelResults.getRow(selectedRow);
				jLabelFolder.setText(DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN)+" | Download: " + result.toString());
				enableRowSorter(false);
				tableModelResults.clear();
				PanelMain.setColumnVisible(columnModel, SEARCH_SPECIFIC_COLUMNS, false);
				progressBar.setup(result.getNbOfFiles());
				soulseek.sendSelection(result, jTextFieldQuery.getText());
			}
		}
	}
	
	
    private void jButtonOptionSelectFolderNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSelectFolderNewActionPerformed
		if(Swing.selectFolder(jTextFieldDestinationTemp, "Soulseek temporary download folder", true)) {
			Option option = Jamuz.getMachine().getOption("location.slsk");			
			option.setValue(jTextFieldDestinationTemp.getText());
			Jamuz.getDb().updateOption(option, option.getValue());
		}
    }//GEN-LAST:event_jButtonOptionSelectFolderNewActionPerformed

    private void jButtonSearchMp3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchMp3ActionPerformed
        startSoulseekSearch(Slsk.Mode.mp3);
    }//GEN-LAST:event_jButtonSearchMp3ActionPerformed

    private void jTableSoulseekMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSoulseekMouseClicked
        if (evt.getClickCount() == 2 && jButtonDownload.isEnabled()) {
            startSoulseekDownload();
        }
    }//GEN-LAST:event_jTableSoulseekMouseClicked

	private void startSoulseekSearch(Slsk.Mode mode) {
		new Thread() {
			@Override
			public void run() {
				Location destinationTemp = new Location("location.slsk");
				if(!destinationTemp.check()) {
					return;
				}
				enableRowSorter(false);
				enableGui(false);
				jButtonDownload.setEnabled(false);
				tableModelResults.clear();
				progressBar.reset();
				PanelMain.setColumnVisible(columnModel, SEARCH_SPECIFIC_COLUMNS, true);
				jTextAreaLog.append("Starting "+mode.toString().toUpperCase()+" search with soulseek-cli.\n");
				jLabelFolder.setText(DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN)+" | "+mode.toString().toUpperCase()+" search: " + jTextFieldQuery.getText());
				String destinationNoTrailingSlash = jTextFieldDestinationTemp.getText();
				destinationNoTrailingSlash = destinationNoTrailingSlash.substring(0, destinationNoTrailingSlash.length() - (destinationNoTrailingSlash.endsWith("/") ? 1 : 0));
				if(soulseek.download(jTextFieldQuery.getText(), destinationNoTrailingSlash, mode)) {
//					Popup.info("\"download\" completed");
				}
			}
		}.start();
	}
	
    /**
	 * @param parent
	 * @param query
     */
    public static void main(Dialog parent, String query) {
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
            java.util.logging.Logger.getLogger(DialogSlsk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        DialogSlsk dialog = new DialogSlsk(parent, false, query);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDownload;
    private javax.swing.JButton jButtonOptionSelectFolderNew;
    private javax.swing.JButton jButtonSearchFlac;
    private javax.swing.JButton jButtonSearchMp3;
    private javax.swing.JLabel jLabelFolder;
    private javax.swing.JProgressBar jProgressBarSlsk;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneCheckTags3;
    private javax.swing.JTable jTableSoulseek;
    private javax.swing.JTextArea jTextAreaLog;
    private javax.swing.JTextField jTextFieldDestinationTemp;
    private javax.swing.JTextField jTextFieldQuery;
    // End of variables declaration//GEN-END:variables
}
