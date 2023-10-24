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
import jamuz.Options;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.ProgressCellRender;
import jamuz.gui.swing.TableColumnModel;
import jamuz.utils.Popup;
import jamuz.utils.Swing;
import java.awt.Dialog;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogSlsk extends javax.swing.JDialog {

    private final TableModelSlskdSearch tableModelResults;
	private final TableModelSlskdDownload tableModelDownload;
	private final TableColumnModel columnModelResults;
	private final TableColumnModel columnModelDownload;
	private final ProgressBar progressBar;
    private Slsk soulseek;
	private Updater positionUpdater;
	
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
		
		//Setup Search results table
		tableModelResults = new TableModelSlskdSearch();
		jTableResults.setModel(tableModelResults);
		columnModelResults = new TableColumnModel();
		//Assigning XTableColumnModel to allow show/hide columns
		jTableResults.setColumnModel(columnModelResults);
		jTableResults.createDefaultColumnsFromModel();
		setColumn(columnModelResults, 0, 120);	// Date
        setColumn(columnModelResults, 1, 40);	// # files
		setColumn(columnModelResults, 2, 50);	// BitRate
		setColumn(columnModelResults, 3, 50);	// Size
		setColumn(columnModelResults, 4, 50);	// Speed
		setColumn(columnModelResults, 5, 50);	// Free upload spots
		setColumn(columnModelResults, 6, 50);	// Queue length
		setColumn(columnModelResults, 7, 150);	// Username
		setColumn(columnModelResults, 8, 600);	// Path

		//Setup download table
		tableModelDownload = new TableModelSlskdDownload();
		jTableDownload.setModel(tableModelDownload);
		columnModelDownload = new TableColumnModel();
		//Assigning XTableColumnModel to allow show/hide columns
		jTableDownload.setColumnModel(columnModelDownload);
		jTableDownload.createDefaultColumnsFromModel();
		setColumn(columnModelDownload, 0, 120);    // Date
		setColumn(columnModelDownload, 1, 35);     // BitRate
		setColumn(columnModelDownload, 2, 80);     // Length
		setColumn(columnModelDownload, 3, 50);     // Size
		setColumn(columnModelDownload, 4, 300);    // File
        TableColumn column = 
        setColumn(columnModelDownload, 5, 400);     // Completed
		column.setCellRenderer(new ProgressCellRender());
        
		progressBar = (ProgressBar)jProgressBarSlsk;

		try {
            soulseek = new Slsk();
            startSoulseekSearch();
        } catch (IOException | SlskdClient.ServerException ex) {
            Popup.error(ex);
        }
    }
	
	private TableColumn setColumn(TableColumnModel columnModel, int index, int width) {
        TableColumn column = columnModel.getColumn(index);
		column.setMinWidth(width);
        column.setPreferredWidth(width);
        column.setMaxWidth(width*3);
		return column;
    }
	
	/**
	 *
	 * @param enable
	 */
	public void enableRowSorter(boolean enable) {
		if(enable) {
			//Enable row tableSorter (cannot be done if model is empty)
			if(tableModelResults.getRowCount()>0) {
				jTableResults.setAutoCreateRowSorter(true);
				TableRowSorter<TableModelSlskdSearch> tableSorter = new TableRowSorter<>(tableModelResults);
				jTableResults.setRowSorter(tableSorter);
				List <RowSorter.SortKey> sortKeys = new ArrayList<>();

                sortKeys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING)); // Free upload spots
				sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING)); // nb of files
				sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING)); // BitRate
				sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING)); // Speed
				sortKeys.add(new RowSorter.SortKey(6, SortOrder.DESCENDING)); // Queue
				
// FIXME !!!! Filter out locked (done, right?) and without free slots
				
				tableSorter.setSortKeys(sortKeys);
				jTableResults.getSelectionModel().setSelectionInterval(0, 0);
			}
			else {
				jTableResults.setAutoCreateRowSorter(false);
			}
		}
		else {
			jTableResults.setAutoCreateRowSorter(false);
			jTableResults.setRowSorter(null);
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
        jTableResults = new jamuz.gui.swing.TableHorizontal();
        jTextFieldQuery = new javax.swing.JTextField();
        jButtonDownload = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonSearch = new javax.swing.JButton();
        jProgressBarSlsk = new jamuz.gui.swing.ProgressBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDownload = new jamuz.gui.swing.TableHorizontal();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTableResults.setAutoCreateColumnsFromModel(false);
        jTableResults.setModel(new jamuz.soulseek.TableModelSlskdSearch());
        jTableResults.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableResults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableResultsMouseClicked(evt);
            }
        });
        jScrollPaneCheckTags3.setViewportView(jTableResults);

        jTextFieldQuery.setToolTipText("");

        jButtonDownload.setText("Download");
        jButtonDownload.setEnabled(false);
        jButtonDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownloadActionPerformed(evt);
            }
        });

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cancel.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("inter/Bundle"); // NOI18N
        jButtonCancel.setText(bundle.getString("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_plus.png"))); // NOI18N
        jButtonSearch.setText(bundle.getString("Button.Search")); // NOI18N
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jProgressBarSlsk.setMinimumSize(new java.awt.Dimension(1, 23));
        jProgressBarSlsk.setString(" "); // NOI18N
        jProgressBarSlsk.setStringPainted(true);

        jTableDownload.setAutoCreateColumnsFromModel(false);
        jTableDownload.setModel(new jamuz.soulseek.TableModelSlskdDownload());
        jTableDownload.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(jTableDownload);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTextFieldQuery)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSearch))
                    .addComponent(jScrollPaneCheckTags3)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonCancel)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jProgressBarSlsk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1220, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldQuery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarSlsk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonDownload)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
		startSoulseekSearch();
    }//GEN-LAST:event_jButtonSearchActionPerformed

	private void enableSearch(boolean enable) {
		jButtonSearch.setEnabled(enable);
		jTextFieldQuery.setEnabled(enable);
	}
	
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        //FIXME !!! really cancel something (or remove button)
        
        jButtonDownload.setEnabled(false);
		jButtonCancel.setEnabled(false);
		enableSearch(true);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownloadActionPerformed
        new Thread() {
			@Override
			public void run() {
				int selectedRow = jTableResults.getSelectedRow(); 			
                if(selectedRow>=0) { 
                    jButtonDownload.setEnabled(false);
                    selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
                    SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);
                    progressBar.setup(searchResponse.fileCount);
                    if(soulseek.download(searchResponse)) {
                        displayDownloads();
                        jButtonDownload.setEnabled(true); //FIXME !! Do not unable if download has already been started


                        positionUpdater = new Updater();
                        positionUpdater.start();

                        //FIXME !! Add to a list of downloads to be monitored, and on completetion: move to destination & remove transfers on server
                    }
                }
			}
		}.start();
    }//GEN-LAST:event_jButtonDownloadActionPerformed

	/**
	 * Position updater class
	 */
	public class Updater extends Timer {
		
		/**
		 * Update period:
         * TODO: Make this an option
		 */
		private static final long UPDATE_PERIODE = 500;
		
		/**
		 * Starts position updater
		 */
		public void start() {
			schedule(new displayPosition(), 0, UPDATE_PERIODE);
		}

		private class displayPosition extends TimerTask {
			@Override
			public void run() {
				displayDownloads();
			}
		}
	}

    private void stopTimer() {
        if(positionUpdater!=null) {
            positionUpdater.cancel();
            positionUpdater.purge();
        }
    }
    
	private void displaySearchFiles() {
		int selectedRow = jTableResults.getSelectedRow(); 			
		if(selectedRow>=0) { 
			selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
			SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);
			tableModelDownload.clear();
			
			//FIXME !! Need to restart it if download is already started
			stopTimer();
			
			for (SlskdSearchFile file : searchResponse.getFiles()) {
				tableModelDownload.addRow(new SlskFile(file, searchResponse.username, searchResponse.getDate()));
			}
		}
	}
	
	private void displayDownloads() {
		int selectedRow = jTableResults.getSelectedRow(); 			
		if(selectedRow>=0) { 
			selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
			SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);
			SlskdDownloadUser downloads = soulseek.getDownloads(searchResponse);
			if(downloads!=null) {
				String searchPath = searchResponse.getPath();
				List<SlskFile> rows = tableModelDownload.getRows();

				Map<String, SlskdDownloadFile> filteredFiles = downloads.directories
				.stream()
				.filter(directory -> 
					directory.directory.startsWith(searchPath))
				.flatMap(directory -> directory.files.stream())
				.collect(Collectors.toMap(
					SlskdDownloadFile::getKey,
					Function.identity(),
					(existing, replacement) -> existing
				));
                
				for (int i = 0; i < rows.size(); i++) {
					SlskFile rowFile = rows.get(i);

					if(filteredFiles.containsKey(rowFile.getKey())) {
						SlskdDownloadFile filteredFile = filteredFiles.get(rowFile.getKey());
						rowFile.update(filteredFile); //FIXME !!!!! update search result too in tableModel
					} else {
						stopTimer();
                        Popup.info("Download might have been cleaned.");
					}
				}
				tableModelDownload.fireTableDataChanged();
                
                boolean allFilesComplete = filteredFiles.values()
                    .stream()
                    .allMatch(file -> file.percentComplete == 100);
                if(allFilesComplete) {
                    stopTimer();
                    Popup.info("Download complete");
                    
                    //FIXME ! Move files to destination
                    // + enable Cancel (need rewritre)
                    // + Disable changing search result while downloading
                }
			}
		}
	}
	
    private void jTableResultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableResultsMouseClicked
		displaySearchFiles();
    }//GEN-LAST:event_jTableResultsMouseClicked

	private void startSoulseekSearch() {
		new Thread() {
			@Override
			public void run() {
                if(soulseek !=null) {
                    enableRowSorter(false);
                    enableSearch(false);
                    jButtonDownload.setEnabled(false);
                    tableModelResults.clear();
                    progressBar.reset();

                    List<SlskdSearchResponse> searchResponses = soulseek.search(jTextFieldQuery.getText());
                    if(searchResponses!=null) {
                        if(!searchResponses.isEmpty()) {
                            for (SlskdSearchResponse slskdSearchResponse : searchResponses) {
                                tableModelResults.addRow(slskdSearchResponse);
                            }
                            enableRowSorter(true);
                            displaySearchFiles();
                            jButtonDownload.setEnabled(true);
                        } else {
                            Popup.info("No results!");
                            enableSearch(true);
                        }
                    }
                } else {
                    Popup.warning("You are not connected.");
                }
			}
		}.start();
	}
	
    /**
	 * @param parent
	 * @param query
	 * @throws java.io.IOException
	 * @throws jamuz.soulseek.SlskdClient.ServerException
     */
    public static void main(Dialog parent, String query) throws IOException, SlskdClient.ServerException {
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

        DialogSlsk dialog = new DialogSlsk(parent, false, query);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDownload;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JProgressBar jProgressBarSlsk;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneCheckTags3;
    private javax.swing.JTable jTableDownload;
    private javax.swing.JTable jTableResults;
    private javax.swing.JTextField jTextFieldQuery;
    // End of variables declaration//GEN-END:variables
}
