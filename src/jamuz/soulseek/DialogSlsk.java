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

import jamuz.Options;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableColumnModel;
import jamuz.utils.DateTime;
import jamuz.utils.Popup;
import jamuz.utils.Swing;
import java.awt.Dialog;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	private Slsk soulseek;
	private final ProgressBar progressBar;
	private Options options;
	
    /**
     * Creates new form DialogSoulseek
     * @param parent
     * @param modal
	 * @param query
	 * @throws java.io.IOException
	 * @throws jamuz.soulseek.SlskdClient.ServerException
     */
    public DialogSlsk(Dialog parent, boolean modal, String query) throws IOException, SlskdClient.ServerException {
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
		setColumn(columnModelResults, 0, 140);	// Date
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
		setColumn(columnModelDownload, 0, 140);	// Date
        setColumn(columnModelDownload, 1, 40);	// BitDepth
		setColumn(columnModelDownload, 2, 50);	// BitRate
		setColumn(columnModelDownload, 3, 50);	// Code
		setColumn(columnModelDownload, 4, 50);	// ext
		setColumn(columnModelDownload, 5, 50);	// Var. Bitrate
		setColumn(columnModelDownload, 6, 50);	// Length
		setColumn(columnModelDownload, 7, 50);	// Sample Rate
		setColumn(columnModelDownload, 8, 50);	// Locked
		setColumn(columnModelDownload, 9, 50);	// State
		setColumn(columnModelDownload, 10, 50);	// Size
		setColumn(columnModelDownload, 11, 50);	// Speed
		setColumn(columnModelDownload, 12, 50);	// Completed
		setColumn(columnModelDownload, 13, 150);	// User
		setColumn(columnModelDownload, 14, 600);	// Path
		setColumn(columnModelDownload, 15, 300);	// More info
		
		
		progressBar = (ProgressBar)jProgressBarSlsk;

		//Get current application folder
		File f = new File(".");  //NOI18N
		String appPath = f.getAbsolutePath();
		appPath = appPath.substring(0, appPath.length() - 1);
		String filename = appPath + "Slsk.properties";
		options = new Options(filename);
		if (options.read()) {
			jTextFieldDownloadedFolder.setText(options.get("slsk.downloaded.folder"));
		}
		if(!(new File(options.get("slsk.downloading.folder"))).exists()) {
			options.set("slsk.downloading.folder", appPath);
		}
		jTextFieldDownloadingFolder.setText(options.get("slsk.downloading.folder"));
		
		soulseek = new Slsk();
		
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                soulseek.cancel();
				options.save();
            }
        });
		
		
		if(new File(options.get("slsk.downloading.folder")).exists()) {
			startSoulseekSearch();
		}
    }
	
	private void setColumn(TableColumnModel columnModel, int index, int width) {
        TableColumn column = columnModel.getColumn(index);
		column.setMinWidth(width);
        column.setPreferredWidth(width);
        column.setMaxWidth(width*3);
    }
	
	//FIXME !! Sort downloads too
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

				sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING)); // nb of files
				sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING)); // BitRate
				sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING)); // Speed
//FIXME !!! Filter by queue then
// FIXME !!!! Filter out locked and without free slots
				
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jButtonDownload = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonSearch = new javax.swing.JButton();
        jTextFieldDownloadingFolder = new javax.swing.JTextField();
        jButtonSelectDownloadingFolder = new javax.swing.JButton();
        jLabelFolder = new javax.swing.JLabel();
        jProgressBarSlsk = new jamuz.gui.swing.ProgressBar();
        jButtonSelectDownloadedFolder = new javax.swing.JButton();
        jTextFieldDownloadedFolder = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
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

        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/search_plus.png"))); // NOI18N
        jButtonSearch.setText(bundle.getString("Button.Search")); // NOI18N
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jTextFieldDownloadingFolder.setEditable(false);
        jTextFieldDownloadingFolder.setToolTipText("");

        jButtonSelectDownloadingFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonSelectDownloadingFolder.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonSelectDownloadingFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectDownloadingFolderActionPerformed(evt);
            }
        });

        jLabelFolder.setToolTipText("");

        jProgressBarSlsk.setMinimumSize(new java.awt.Dimension(1, 23));
        jProgressBarSlsk.setString(" "); // NOI18N
        jProgressBarSlsk.setStringPainted(true);

        jButtonSelectDownloadedFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonSelectDownloadedFolder.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonSelectDownloadedFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectDownloadedFolderActionPerformed(evt);
            }
        });

        jTextFieldDownloadedFolder.setEditable(false);
        jTextFieldDownloadedFolder.setToolTipText("");

        jLabel1.setText("DownloadING folder:");

        jLabel2.setText("DownloadED folder:");

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTextFieldQuery)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSearch))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonSelectDownloadedFolder)
                            .addComponent(jButtonSelectDownloadingFolder))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDownloadingFolder, javax.swing.GroupLayout.DEFAULT_SIZE, 935, Short.MAX_VALUE)
                            .addComponent(jTextFieldDownloadedFolder))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonCancel)
                            .addComponent(jButtonDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabelFolder, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jProgressBarSlsk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1164, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPaneCheckTags3))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jProgressBarSlsk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDownloadingFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSelectDownloadingFolder)
                    .addComponent(jButtonDownload)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jTextFieldDownloadedFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSelectDownloadedFolder)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
		startSoulseekSearch();
    }//GEN-LAST:event_jButtonSearchActionPerformed

	private void enableGui(boolean enable) {
		jButtonSearch.setEnabled(enable);
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
		int selectedRow = jTableResults.getSelectedRow(); 			
		if(selectedRow>=0) { 
			if((new File(jTextFieldDownloadingFolder.getText())).exists()) {
				jButtonDownload.setEnabled(false);
				selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
				SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);
				jLabelFolder.setText(DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN)+" | Download: " + searchResponse.toString());
				progressBar.setup(searchResponse.fileCount);
				if(soulseek.download(searchResponse)) {
					displayDownloads();
					jButtonDownload.setEnabled(true); //FIXME !!!! Do not unable if download has already been started
				}
			}
		}
	}

	private void displaySearchFiles() {
		int selectedRow = jTableResults.getSelectedRow(); 			
		if(selectedRow>=0) { 
			selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
			SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);
			tableModelDownload.clear();
			for (SlskdSearchFile file : searchResponse.files) {
				tableModelDownload.addRow(new SlskFile(file, searchResponse.username));
			}
		}
	}
	
	private void displayDownloads() {
		int selectedRow = jTableResults.getSelectedRow(); 			
		if(selectedRow>=0) { 
			selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
			SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);

			SlskdDownloadUser downloads = soulseek.getDownloads(searchResponse);

			List<String> searchPaths = searchResponse.getPaths();
			List<String> allowedExtensions = new ArrayList<>();
			allowedExtensions.add("mp3");
			allowedExtensions.add("flac");
					
			
			Map<String, SlskdDownloadFile> filteredFiles = downloads.directories
			.stream()
			.filter(directory -> 
				searchPaths.stream().anyMatch(path -> directory.directory.startsWith(path)))
			.flatMap(directory -> directory.files.stream()
//				.filter(file -> allowedExtensions.contains(FilenameUtils.getExtension(file.filename))) //FIXME: Filter by ext, but right after search, not here
			)
			.collect(Collectors.toMap(
				SlskdDownloadFile::getKey, // Key comes from the SlskdDownloadFile
				Function.identity(),
				(existing, replacement) -> existing
			));
			
			List<SlskFile> rows = tableModelDownload.getRows();
			
			for (int i = 0; i < rows.size(); i++) {
				SlskFile rowFile = rows.get(i);
				
				if(filteredFiles.containsKey(rowFile.getKey())) {
					SlskdDownloadFile filteredFile = filteredFiles.get(rowFile.getKey());
					rowFile.update(filteredFile); //FIXME !!!!! update search result too in tableModel
				} else {
					Popup.error("<> files"); //FIXME !! remove this, but before check that it always work, or better use a Map
				}
			}
			tableModelDownload.fireTableDataChanged();
		}
	}
	
	
    private void jButtonSelectDownloadingFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectDownloadingFolderActionPerformed
		if(Swing.selectFolder(jTextFieldDownloadingFolder, "Soulseek temporary download folder", true)) {
			options.set("slsk.downloading.folder", jTextFieldDownloadingFolder.getText());
			
		}
    }//GEN-LAST:event_jButtonSelectDownloadingFolderActionPerformed

    private void jTableResultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableResultsMouseClicked
		displaySearchFiles();
    }//GEN-LAST:event_jTableResultsMouseClicked

    private void jButtonSelectDownloadedFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectDownloadedFolderActionPerformed
        if(Swing.selectFolder(jTextFieldDownloadedFolder, "Soulseek final download folder", true)) {
			options.set("slsk.downloaded.folder", jTextFieldDownloadedFolder.getText());
			
		}
    }//GEN-LAST:event_jButtonSelectDownloadedFolderActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        displayDownloads();
    }//GEN-LAST:event_jButton1ActionPerformed

	private void startSoulseekSearch() {
		new Thread() {
			@Override
			public void run() {
				File SlskDownloadingFolder = new File(jTextFieldDownloadingFolder.getText());
				if(!SlskDownloadingFolder.exists()) {
					return;
				}
				enableRowSorter(false);
				enableGui(false);
				jButtonDownload.setEnabled(false);
				tableModelResults.clear();
				progressBar.reset();
//				PanelMain.setColumnVisible(columnModelResults, SEARCH_SPECIFIC_COLUMNS, true);
				jTextAreaLog.append("Starting search on slskd.\n");
				jLabelFolder.setText(DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN)+" | search: " + jTextFieldQuery.getText());
				String destinationNoTrailingSlash = jTextFieldDownloadingFolder.getText()
						.substring(0, jTextFieldDownloadingFolder.getText().length() - (jTextFieldDownloadingFolder.getText().endsWith("/") ? 1 : 0));
				
				//FIXME ! Start (and check errors) docker image slskd
//				docker run -d \
//					-p 5030:5030 \
//					-p 5031:5031 \
//					-p 50300:50300 \
//					-e SLSKD_REMOTE_CONFIGURATION=true \
//					-e SLSKD_SLSK_USERNAME=xxx \
//					-e SLSKD_SLSK_PASSWORD="xxx" \
//					-e SLSKD_SWAGGER=true \
//					-v /home/xxx/yyy/SlskdData:/app \
//					--name slskd \
//					slskd/slskd:latest
		  
				// docker ps -a 
				
				List<SlskdSearchResponse> searchResponses = soulseek.search(jTextFieldQuery.getText(), destinationNoTrailingSlash);
				for (SlskdSearchResponse slskdSearchResponse : searchResponses) {
					tableModelResults.addRow(slskdSearchResponse);
				}
				enableRowSorter(true);
				jButtonDownload.setEnabled(true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDownload;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonSelectDownloadedFolder;
    private javax.swing.JButton jButtonSelectDownloadingFolder;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelFolder;
    private javax.swing.JProgressBar jProgressBarSlsk;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneCheckTags3;
    private javax.swing.JTable jTableDownload;
    private javax.swing.JTable jTableResults;
    private javax.swing.JTextArea jTextAreaLog;
    private javax.swing.JTextField jTextFieldDownloadedFolder;
    private javax.swing.JTextField jTextFieldDownloadingFolder;
    private javax.swing.JTextField jTextFieldQuery;
    // End of variables declaration//GEN-END:variables
}
