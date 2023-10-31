/*
 * Copyright (C) 2017 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import jamuz.Jamuz;
import jamuz.Options;
import jamuz.gui.swing.ProgressCellRender;
import jamuz.gui.swing.TableColumnModel;
import jamuz.process.check.Location;
import jamuz.utils.Desktop;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelSlsk extends javax.swing.JPanel {

	private Slsk soulseek;
    private Options options;
    private final TableModelSlskdSearch tableModelResults;
	private TableModelSlskdDownload tableModelDownload;
	private final TableColumnModel columnModelResults;
	private final TableColumnModel columnModelDownload;
    private final Updater positionUpdater;
    private final Base64 encoder = new Base64();

	/**
	 * Creates new form PanelRemote
	 */
	public PanelSlsk() {
		initComponents();
        
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
        TableColumn column = 
        setColumn(columnModelResults, 9, 80);     // Completed
        column.setCellRenderer(new ProgressCellRender());

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
        column = 
        setColumn(columnModelDownload, 5, 400);     // Completed
		column.setCellRenderer(new ProgressCellRender());
        
        jTextFieldPassword.setToolTipText("Warning: Password is stored as plain text in properties file !");
        
        //Get and display options
        File file = Jamuz.getFile("Slsk.properties");
        if(file.exists()) {
            options = new Options(file.getAbsolutePath());
            if(options.read()) {
                jTextFieldUsername.setText(options.get("slsk.username"));
                jTextFieldPassword.setText(options.get("slsk.password"));

                //FIXME !!! slskd onStartup
                boolean onStartup = Boolean.parseBoolean(options.get("server.on.startup", "false"));
                jCheckBoxServerStartOnStartup.setSelected(onStartup);
            }
        }
        
        positionUpdater = new Updater();
        positionUpdater.start();
	}

    private TableColumn setColumn(TableColumnModel columnModel, int index, int width) {
        TableColumn column = columnModel.getColumn(index);
		column.setMinWidth(width);
        column.setPreferredWidth(width);
        column.setMaxWidth(width*3);
		return column;
    }
   
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelRemote = new javax.swing.JPanel();
        jButtonStart = new javax.swing.JButton();
        jCheckBoxServerStartOnStartup = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldPassword = new jamuz.gui.swing.PasswordFieldWithToggle();
        jButtonWebSlskd = new javax.swing.JButton();
        jSplitPaneLogs = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPaneCheckTags3 = new javax.swing.JScrollPane();
        jTableResults = new jamuz.gui.swing.TableHorizontal();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDownload = new jamuz.gui.swing.TableHorizontal();

        jPanelRemote.setBorder(javax.swing.BorderFactory.createTitledBorder("Soulseek"));

        jButtonStart.setText(Inter.get("Button.Start")); // NOI18N
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });

        jCheckBoxServerStartOnStartup.setSelected(true);
        jCheckBoxServerStartOnStartup.setText(Inter.get("PanelMain.jCheckBoxServerStartOnStartup.text")); // NOI18N
        jCheckBoxServerStartOnStartup.setToolTipText(Inter.get("PanelMain.jCheckBoxServerStartOnStartup.toolTipText")); // NOI18N
        jCheckBoxServerStartOnStartup.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxServerStartOnStartupItemStateChanged(evt);
            }
        });

        jLabel3.setText("Username");

        jLabel4.setText("Password");

        jTextFieldPassword.setToolTipText("");

        jButtonWebSlskd.setText("Web Interface");
        jButtonWebSlskd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWebSlskdActionPerformed(evt);
            }
        });

        jSplitPaneLogs.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTextAreaLog.setColumns(20);
        jTextAreaLog.setRows(5);
        jScrollPane1.setViewportView(jTextAreaLog);

        jSplitPaneLogs.setTopComponent(jScrollPane1);

        jTableResults.setAutoCreateColumnsFromModel(false);
        jTableResults.setModel(new jamuz.soulseek.TableModelSlskdSearch());
        jTableResults.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableResults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableResultsMouseClicked(evt);
            }
        });
        jScrollPaneCheckTags3.setViewportView(jTableResults);

        jTableDownload.setAutoCreateColumnsFromModel(false);
        jTableDownload.setModel(new jamuz.soulseek.TableModelSlskdDownload());
        jTableDownload.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(jTableDownload);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.DEFAULT_SIZE, 1149, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
        );

        jSplitPaneLogs.setRightComponent(jPanel2);

        javax.swing.GroupLayout jPanelRemoteLayout = new javax.swing.GroupLayout(jPanelRemote);
        jPanelRemote.setLayout(jPanelRemoteLayout);
        jPanelRemoteLayout.setHorizontalGroup(
            jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRemoteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBoxServerStartOnStartup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonWebSlskd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonStart))
                    .addComponent(jSplitPaneLogs))
                .addContainerGap())
        );
        jPanelRemoteLayout.setVerticalGroup(
            jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBoxServerStartOnStartup)
                        .addComponent(jButtonStart)
                        .addComponent(jButtonWebSlskd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPaneLogs, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelRemote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelRemote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        new Thread() {
            @Override
            public void run() {
                jSplitPaneLogs.setDividerLocation(1.0);
                enableGui(false);

                //Start slskd server, if not already running
                SlskdDocker slskdDocker = new SlskdDocker(
                        jTextFieldUsername.getText(), 
                        jTextFieldPassword.getText(), 
                        Jamuz.getFile("", "slskd").getAbsolutePath(), 
                        Jamuz.getMachine().getOptionValue("location.library"));

                if(jButtonStart.getText().equals(Inter.get("Button.Start"))) {
                    jButtonStart.setText("Starting ...");
                    jTextAreaLog.setText("Checking slsk status and restart if needed...\n");
                    if(!slskdDocker.start()) {
                        Popup.warning("Could not start slskd");
                    }

                    //Wait for container to be healthy and display logs
                    SwingWorker<String, String> worker = new SwingWorker<>() {
                        @Override
                        protected String doInBackground() {
                            return slskdDocker.checkContainerHealthAndFetchLogs(new ResultCallback<Frame>() {
                                @Override
                                public void onStart(Closeable closeable) {
                                    appendText("Starting ...\n");
                                }

                                @Override
                                public void onNext(com.github.dockerjava.api.model.Frame object) {
                                    appendText(new String(object.getPayload()));
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    appendText("ERROR: " + throwable.getLocalizedMessage() + "\n");
                                    enableStart();
                                }

                                @Override
                                public void onComplete() {
                                    appendText("Complete !\n");
                                    enableStart();
                                }

                                @Override
                                public void close() throws IOException {
                                    appendText("Closed.\n");
                                }
                            });
                        }

                        //TODO: This is never called. How to call it and can it replace ResultCallback ?
                        @Override
                        protected void process(java.util.List<String> chunks) {
                            for (String log : chunks) {
                                appendText(log + "\n");
                            }
                        }

                        @Override
                        protected void done() {
                            try {
                                String result = get();
                                appendText(result);

                                try {
                                    soulseek = new Slsk();
                                } catch (IOException | SlskdClient.ServerException ex) {
                                    Popup.error(ex);
                                }
                            } catch (InterruptedException | ExecutionException ex) {
                                appendText("Error: " + ex.getMessage());
                            }
                            jButtonStart.setText(Inter.get("Button.Stop"));
                            jSplitPaneLogs.setDividerLocation(0.0);
                            enableGui(true);
                        }
                    };
                    worker.execute();
                } else {
                    slskdDocker.stop();
                    enableStart();
                }
            }
        }.start();
    }//GEN-LAST:event_jButtonStartActionPerformed

    public void enableGui(boolean enable) {
        jButtonStart.setEnabled(enable);
        jTextFieldUsername.setEnabled(enable);
        jTextFieldPassword.setEnabled(enable);
        jCheckBoxServerStartOnStartup.setEnabled(enable);
    }
    
    private void enableStart() {
        jButtonStart.setText(Inter.get("Button.Start"));
        jSplitPaneLogs.setDividerLocation(1.0);
        enableGui(true);
    }
    
    private void appendText(String text) {
        jTextAreaLog.append(text);
        jTextAreaLog.setCaretPosition(jTextAreaLog.getDocument().getLength()); //Scroll to bottom
    }
    
    private void jCheckBoxServerStartOnStartupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxServerStartOnStartupItemStateChanged
//        Jamuz.getOptions().set("server.on.startup", String.valueOf(Boolean.valueOf(jCheckBoxServerStartOnStartup.isSelected())));
//		Jamuz.getOptions().save();
    }//GEN-LAST:event_jCheckBoxServerStartOnStartupItemStateChanged

    private void jTableResultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableResultsMouseClicked
        displaySearchFiles();
    }//GEN-LAST:event_jTableResultsMouseClicked

    private void jButtonWebSlskdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWebSlskdActionPerformed
        Desktop.openBrowser("http://localhost:5030");
    }//GEN-LAST:event_jButtonWebSlskdActionPerformed
	
    private void displaySearchFiles() {
		int selectedRow = jTableResults.getSelectedRow(); 			
		if(selectedRow>=0) { 
			selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
			SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);
            
            tableModelDownload = searchResponse.getTableModel();
            jTableDownload.setModel(tableModelDownload);
//            tableModelDownload.clear();
//			for (SlskdSearchFile file : searchResponse.getFiles()) {
//				tableModelDownload.addRow(new SlskFile(file, searchResponse.username, searchResponse.getDate()));
//			}
		}
	}
    
    void addDownload(SlskdSearchResponse searchResponse) {
        if(soulseek!=null) {
            //Add search result
            tableModelResults.addRow(searchResponse);

            //Select it and display files
            int lastModelIndex = tableModelResults.getRowCount() - 1;
            int lastViewIndex = jTableResults.convertRowIndexToView(lastModelIndex);
            jTableResults.setRowSelectionInterval(lastViewIndex, lastViewIndex);
            displaySearchFiles();

            //Start download
            soulseek.download(searchResponse);
        } else {
            Popup.warning("You must connect first!");
        }
    }
    
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
                
                for (int row = 0; row < tableModelResults.getRows().size(); row++) {
                    SlskdSearchResponse searchResponse = tableModelResults.getRow(row);
                    if(soulseek!=null && !searchResponse.isCompleted()) {
                        //Get downloads for username
                        SlskdDownloadUser downloads = soulseek.getDownloads(searchResponse);
                        if(downloads!=null) {
                            
                            //FIXME !! We have several searchResponse having the same username and path but different file list (user is a mess and put multiple albums on the same folder)
                            // => This results in bad percentages (searhc response view), and probably other glitches
                            // ====>> So, split by username and path in Panel's search results table, no more by search ? (THINK TWICE)
                            
                            
                            //Filter downloads: keep only the ones from the same path
                            Map<String, SlskdDownloadFile> filteredFiles = downloads.directories
                            .stream()
                            .filter(directory -> 
                                directory.directory.startsWith(searchResponse.getPath()))
                            .flatMap(directory -> directory.files.stream())
                            .collect(Collectors.toMap(
                                SlskdDownloadFile::getKey,
                                Function.identity(),
                                (existing, replacement) -> existing
                            ));

                            if(tableModelDownload.getSearchResponse().equals(searchResponse)) {
                                List<SlskdSearchFile> rows = tableModelDownload.getRows();
                                for (int i = 0; i < rows.size(); i++) {
                                    SlskdSearchFile rowFile = rows.get(i);
                                    if(filteredFiles.containsKey(rowFile.getKey())) {
                                        SlskdDownloadFile filteredFile = filteredFiles.get(rowFile.getKey());
                                        rowFile.update(filteredFile);
                                    }
                                }
                                tableModelDownload.fireTableDataChanged();
                            }
                            
                            int percentComplete=0;
                            for (SlskdDownloadFile slskdDownloadFile : filteredFiles.values()) {
                                percentComplete+=slskdDownloadFile.percentComplete;
                            }
                            searchResponse.update("", ((int) Math.round(percentComplete)) / filteredFiles.values().size() );
                            tableModelResults.fireTableCellUpdated(row, 9);
                            
                            boolean allFilesComplete = filteredFiles.values()
                                .stream()
                                .allMatch(file -> file.percentComplete == 100);
                            if(allFilesComplete) {
                                Location finalDestination = new Location("location.add");
                                if(finalDestination.check()) {
                                    SlskdDownloadFile get = filteredFiles.values().stream().findFirst().get();
                                    String[] split = get.filename.split("\\\\");
                                    String subDirectoryName = split[split.length-2];
                                    String sourcePath = Jamuz.getFile("", "slskd", "downloads").getAbsolutePath();
                                    File sourceFile = new File(FilenameUtils.concat(sourcePath, subDirectoryName));
                                    File destFile = new File(FilenameUtils.concat(finalDestination.getValue(), subDirectoryName));

                                    try {
                                        FileUtils.copyDirectory(sourceFile, destFile);
                                        for (SlskdDownloadFile downloadFile : filteredFiles.values()) {
                                            soulseek.deleteTransfer(downloadFile);
                                        }
                                        String base64subDir = encoder.encodeToString(subDirectoryName.getBytes());
                                        soulseek.deleteDirectory(base64subDir);
                                        searchResponse.setCompleted();
                                    } catch (IOException ex) {
                                        Logger.getLogger(PanelSlsk.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                    }
                }
			}
		}
	}
	   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonStart;
    private javax.swing.JButton jButtonWebSlskd;
    private javax.swing.JCheckBox jCheckBoxServerStartOnStartup;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelRemote;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneCheckTags3;
    private javax.swing.JSplitPane jSplitPaneLogs;
    private javax.swing.JTable jTableDownload;
    private javax.swing.JTable jTableResults;
    private javax.swing.JTextArea jTextAreaLog;
    private jamuz.gui.swing.PasswordFieldWithToggle jTextFieldPassword;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables

    
}
