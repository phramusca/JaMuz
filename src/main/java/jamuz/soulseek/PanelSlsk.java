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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jamuz.Jamuz;
import jamuz.Options;
import jamuz.gui.swing.PopupListener;
import jamuz.gui.swing.ProgressCellRender;
import jamuz.gui.swing.TableColumnModel;
import jamuz.process.check.Location;
import jamuz.utils.Desktop;
import jamuz.utils.FileSystem;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelSlsk extends javax.swing.JPanel {

	private Slsk soulseek;
    private Options options;
    private final TableModelSlskdSearch tableModelResults = new TableModelSlskdSearch();
	private TableModelSlskdDownload tableModelDownload;
	private final TableColumnModel columnModelResults;
	private final TableColumnModel columnModelDownload;
    private final Updater positionUpdater;
	
    /**
	 * Creates new form PanelRemote
	 */
	public PanelSlsk() {
		initComponents();
        
        //Setup Search results table
//		tableModelResults = new TableModelSlskdSearch();
		jTableResults.setModel(tableModelResults);
		columnModelResults = new TableColumnModel();
		//Assigning XTableColumnModel to allow show/hide columns
		jTableResults.setColumnModel(columnModelResults);
		jTableResults.createDefaultColumnsFromModel();
		setColumn(columnModelResults, 0, 120);	// Date
        TableColumn columnQueued = 
        setColumn(columnModelResults, 1, 50);	// Queued
        
        setColumn(columnModelResults, 2, 150);	// Search text
        setColumn(columnModelResults, 3, 40);	// # files
		setColumn(columnModelResults, 4, 50);	// BitRate
		setColumn(columnModelResults, 5, 50);	// Size
		setColumn(columnModelResults, 6, 50);	// Speed
		setColumn(columnModelResults, 7, 50);	// Free upload spots
		setColumn(columnModelResults, 8, 50);	// Queue length
		setColumn(columnModelResults, 9, 150);	// Username
		setColumn(columnModelResults, 10, 600);	// Path
        TableColumn column = 
        setColumn(columnModelResults, 11, 80);     // Completed
        column.setCellRenderer(new ProgressCellRender());
        columnModelResults.setColumnVisible(columnQueued, false);
        
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
        File propertiesFile = Jamuz.getFile("Slsk.properties");
        boolean onStartup = false;
        if(propertiesFile.exists()) {
            options = new Options(propertiesFile.getAbsolutePath());
            if(options.read()) {
                jTextFieldUsername.setText(options.get("slsk.username"));
                jTextFieldPassword.setText(options.get("slsk.password"));
                onStartup = Boolean.parseBoolean(options.get("slsk.on.startup", "false"));
                jCheckBoxServerStartOnStartup.setSelected(onStartup);
            }
        }
        
        addMenuItem(Inter.get("Slsk.Retry")); //NOI18N
        addMenuItem(Inter.get("Slsk.Remove")); //NOI18N
        MouseListener popupListener = new PopupListener(jPopupMenu1);
        jTableResults.addMouseListener(popupListener);
        
        positionUpdater = new Updater();
        positionUpdater.start();
        
        //Restore searches from cache
        File slskCacheFolder = Jamuz.getFile("", "data", "cache", "slsk");
        for (File listFile : slskCacheFolder.listFiles()) {
            if(listFile.exists()) {
                SlskdSearchResponse searchResponse = readJson(listFile);
                if(searchResponse.isCompleted()) {
                    searchResponse.getProgressBar().progress("", 100);
                    searchResponse.files.forEach(f -> f.getProgressBar().progress("", 100));
                }
                tableModelResults.addRow(searchResponse);
            }
        }
        
        if(onStartup) {
			startStopSlsk();
		}
	}

    private void addMenuItem(String item) {
        JMenuItem menuItem = new JMenuItem(item);
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
    }
    
    ActionListener menuListener = (ActionEvent e) -> {
		JMenuItem source = (JMenuItem)(e.getSource());
		String sourceTxt=source.getText();
		if(sourceTxt.equals(Inter.get("Slsk.Retry"))) { //NOI18N
			SlskdSearchResponse searchResponse = getSelected();
            if(searchResponse!=null) {
                SwingUtilities.invokeLater(() -> {
                    searchResponse.setProcessed(true);
                    for (SlskdSearchFile file : searchResponse.files) {
                        if (file.percentComplete<100) {
                            soulseek.deleteTransfer(searchResponse.username, file);  
                        }
                    }
                    soulseek.download(searchResponse);
                    searchResponse.setProcessed(false);
                });
            }
		}
		else if(sourceTxt.equals(Inter.get("Slsk.Remove"))) { //NOI18N
			SlskdSearchResponse searchResponse = getSelected();
            if(searchResponse!=null) {
                SwingUtilities.invokeLater(() -> {
                    searchResponse.setProcessed(true);
                    if(searchResponse.isCompleted()) {
                        File file = getJsonFile(searchResponse);
                        if(file.exists()) {
                            file.delete();
                        }
                        tableModelResults.removeRow(searchResponse);
                    } else {
                        int n = JOptionPane.showConfirmDialog(
                                this, "Annuler et supprimer ?",
                                Inter.get("Label.Confirm"),  //NOI18N
                                JOptionPane.YES_NO_OPTION);
                        if (n == JOptionPane.YES_OPTION) {
                            for (SlskdSearchFile searchFile : searchResponse.files) {
                                soulseek.deleteTransfer(searchResponse.username, searchFile);
                                soulseek.deleteFile(searchFile);
                            }
                            File file = getJsonFile(searchResponse);
                            if(file.exists()) {
                                file.delete();
                            }
                            tableModelResults.removeRow(searchResponse);
                        }                            
                    }
                    displaySearchFiles();
                    searchResponse.setProcessed(false);
                });
            }
		}
		else {
			Popup.error("Unknown menu item: " + sourceTxt); //NOI18N
		}
	};
    
    private SlskdSearchResponse getSelected() {
        int selectedRow = jTableResults.getSelectedRow(); 			
		if(selectedRow>=0) { 
			selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
			SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);
            return searchResponse;
		}
        return null;
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanelSlsk = new javax.swing.JPanel();
        jButtonStart = new javax.swing.JButton();
        jCheckBoxServerStartOnStartup = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldPassword = new jamuz.gui.swing.PasswordFieldWithToggle();
        jButtonWebSlskd = new javax.swing.JButton();
        jToggleShowLogs = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPaneLog = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPaneCheckTags3 = new javax.swing.JScrollPane();
        jTableResults = new jamuz.gui.swing.TableHorizontal();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDownload = new jamuz.gui.swing.TableHorizontal();

        jPanelSlsk.setBorder(javax.swing.BorderFactory.createTitledBorder("Soulseek"));

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

        jToggleShowLogs.setSelected(true);
        jToggleShowLogs.setText("Log");
        jToggleShowLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleShowLogsActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPaneLog.setMinimumSize(new java.awt.Dimension(200, 200));
        jScrollPaneLog.setName(""); // NOI18N
        jScrollPaneLog.setPreferredSize(new java.awt.Dimension(232, 200));

        jTextAreaLog.setEditable(false);
        jTextAreaLog.setColumns(20);
        jTextAreaLog.setLineWrap(true);
        jTextAreaLog.setRows(5);
        jTextAreaLog.setText("Thisiid zdojaprg ajerpga ra ergn,");
        jScrollPaneLog.setViewportView(jTextAreaLog);

        jPanel1.add(jScrollPaneLog, java.awt.BorderLayout.NORTH);

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setPreferredSize(new java.awt.Dimension(0, 0));

        jTableResults.setAutoCreateColumnsFromModel(false);
        jTableResults.setModel(new jamuz.soulseek.TableModelSlskdSearch());
        jTableResults.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableResults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableResultsMouseClicked(evt);
            }
        });
        jScrollPaneCheckTags3.setViewportView(jTableResults);

        jSplitPane1.setTopComponent(jScrollPaneCheckTags3);

        jTableDownload.setAutoCreateColumnsFromModel(false);
        jTableDownload.setModel(new jamuz.soulseek.TableModelSlskdDownload());
        jTableDownload.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(jTableDownload);

        jSplitPane1.setRightComponent(jScrollPane2);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanelSlskLayout = new javax.swing.GroupLayout(jPanelSlsk);
        jPanelSlsk.setLayout(jPanelSlskLayout);
        jPanelSlskLayout.setHorizontalGroup(
            jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSlskLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelSlskLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBoxServerStartOnStartup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
                        .addComponent(jToggleShowLogs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonWebSlskd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonStart)))
                .addContainerGap())
        );
        jPanelSlskLayout.setVerticalGroup(
            jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSlskLayout.createSequentialGroup()
                .addGroup(jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBoxServerStartOnStartup)
                        .addComponent(jButtonStart)
                        .addComponent(jButtonWebSlskd)
                        .addComponent(jToggleShowLogs)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelSlsk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelSlsk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        startStopSlsk();
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void startStopSlsk() {
        new Thread() {
            @Override
            public void run() {
                displayLogs(true);
                enableGui(false);

                boolean reCreate = (!options.get("slsk.username").equals(jTextFieldUsername.getText()) || !options.get("slsk.password").equals(jTextFieldPassword.getText()));
                
                //Start slskd server, if not already running
                SlskdDocker slskdDocker = new SlskdDocker(
                        jTextFieldUsername.getText(), 
                        jTextFieldPassword.getText(), 
                        Jamuz.getFile("", "slskd").getAbsolutePath(), 
                        Jamuz.getMachine().getOptionValue("location.library"), reCreate);

                if(jButtonStart.getText().equals(Inter.get("Button.Start"))) {
                    jButtonStart.setText("Starting ...");
                    jTextAreaLog.setText("Checking slsk status and restart if needed...\n");
                    if(!slskdDocker.start()) {
                        Popup.warning("Could not start slskd");
                    }

                    //Save options
                    options.set("slsk.username", jTextFieldUsername.getText());
                    options.set("slsk.password", jTextFieldPassword.getText());
                    options.save();
                    
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
                            displayLogs(false);
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
    }
    
    public void enableGui(boolean enable) {
        jButtonStart.setEnabled(enable);
        jTextFieldUsername.setEnabled(enable);
        jTextFieldPassword.setEnabled(enable);
        jCheckBoxServerStartOnStartup.setEnabled(enable);
    }
    
    private void enableStart() {
        jButtonStart.setText(Inter.get("Button.Start"));
        displayLogs(true);
        enableGui(true);
    }
    
    private void appendText(String text) {
        jTextAreaLog.append(text);
        jTextAreaLog.setCaretPosition(jTextAreaLog.getDocument().getLength()); //Scroll to bottom
    }
    
    private void jTableResultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableResultsMouseClicked
        displaySearchFiles();
    }//GEN-LAST:event_jTableResultsMouseClicked

    private void jButtonWebSlskdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWebSlskdActionPerformed
        Desktop.openBrowser("http://localhost:5030");
    }//GEN-LAST:event_jButtonWebSlskdActionPerformed

    private void jCheckBoxServerStartOnStartupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxServerStartOnStartupItemStateChanged
        options.set("slsk.on.startup", String.valueOf(Boolean.valueOf(jCheckBoxServerStartOnStartup.isSelected())));
		options.save();
    }//GEN-LAST:event_jCheckBoxServerStartOnStartupItemStateChanged

    private void jToggleShowLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleShowLogsActionPerformed
        displayLogs(jToggleShowLogs.isSelected());
    }//GEN-LAST:event_jToggleShowLogsActionPerformed
	
    private void displaySearchFiles() {
        SlskdSearchResponse searchResponse = getSelected();
        if(searchResponse!=null) {
            tableModelDownload = searchResponse.getTableModel();
        } else {
            tableModelDownload = new TableModelSlskdDownload();
        }
        jTableDownload.setModel(tableModelDownload);
	}
    
    private void saveJson(SlskdSearchResponse searchResponse) {
        try {
            Gson gson = new Gson();
            File file = getJsonFile(searchResponse);
            FileSystem.writeTextFile(file, gson.toJson(searchResponse));
        } catch (IOException ex) {
            Logger.getLogger(PanelSlsk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private File getJsonFile(SlskdSearchResponse searchResponse) {
        String uniqueFilename = StringManager.removeIllegal(searchResponse.getSearchText() + "_" + searchResponse.getDate() + "_" + searchResponse.username) + ".json";
        return Jamuz.getFile(uniqueFilename, "data", "cache", "slsk");
    }
    
    private SlskdSearchResponse readJson(File listFile) {
        try {
            String readJson = FileSystem.readTextFile(listFile);
            if (!readJson.equals("")) {
                Gson gson = new Gson();
                Type mapType = new TypeToken<SlskdSearchResponse>(){}.getType();
                SlskdSearchResponse searchResponse = gson.fromJson(readJson, mapType);
                return searchResponse;
            }
        } catch (IOException ex) {
            Logger.getLogger(PanelSlsk.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    void addDownload(SlskdSearchResponse searchResponse) {
        if(soulseek!=null) {

            saveJson(searchResponse);
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

    private void displayLogs(boolean b) {
        jToggleShowLogs.setSelected(b);
        jScrollPaneLog.setVisible(b);
        this.revalidate();
        this.repaint();
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
                    if(soulseek!=null && !searchResponse.isCompleted() && ! searchResponse.isProcessed()) {
                        //Get downloads for username
                        SlskdDownloadUser downloads = soulseek.getDownloads(searchResponse);
                        if(downloads!=null) {
                            // Filter downloads: keep only the ones matching searchResponse (if username is a mess, he could have multiple albums on the same folder)
                            Map<String, SlskdDownloadFile> filteredFiles = downloads.directories
                                    .stream()
                                    .flatMap(directory -> directory.files.stream()
                                            .filter(downloadFile ->
                                                    searchResponse.files.stream()
                                                            .anyMatch(searchFile -> searchFile.filename.equals(downloadFile.filename))
                                            )
                                    )
                                    .collect(Collectors.toMap(
                                            SlskdDownloadFile::getKey,
                                            Function.identity(),
                                            (existing, replacement) -> existing
                                    ));

                            if(tableModelDownload.getSearchResponse() != null && tableModelDownload.getSearchResponse().equals(searchResponse)) {
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
                            tableModelResults.fireTableCellUpdated(row, 11);
                            
                            boolean allFilesComplete = filteredFiles.values()
                                .stream()
                                .allMatch(file -> file.percentComplete == 100);
                            if(allFilesComplete) {
                                Location finalDestination = new Location("location.add");
                                if(finalDestination.check()) {
                                    try {
                                        for (SlskdDownloadFile downloadFile : filteredFiles.values()) {
                                            
                                            //Copy file
                                            Pair<String, String> directory = soulseek.getDirectory(downloadFile.filename);
                                            String subDirectoryName = directory.getLeft();
                                            String filename = directory.getRight();
                                            
                                            String sourcePath = Jamuz.getFile("", "slskd", "downloads").getAbsolutePath();
                                            File sourceFile = new File(FilenameUtils.concat(FilenameUtils.concat(sourcePath, subDirectoryName), filename));
                                            String newSubDirectoryName = StringManager.removeIllegal(searchResponse.getSearchText() + "--" + searchResponse.username + "--" + searchResponse.getPath());
                                            File destFile = new File(FilenameUtils.concat(FilenameUtils.concat(finalDestination.getValue(), newSubDirectoryName), filename));
                                            FileUtils.copyFile(sourceFile, destFile);
                                            
                                            //Delete transfer
                                            soulseek.deleteTransfer(downloadFile);
                                            //Delete file
                                            soulseek.deleteFile(downloadFile);
                                        }
                                        searchResponse.setCompleted();
                                        saveJson(searchResponse);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelSlsk;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneCheckTags3;
    private javax.swing.JScrollPane jScrollPaneLog;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTableDownload;
    private javax.swing.JTable jTableResults;
    private javax.swing.JTextArea jTextAreaLog;
    private jamuz.gui.swing.PasswordFieldWithToggle jTextFieldPassword;
    private javax.swing.JTextField jTextFieldUsername;
    private javax.swing.JToggleButton jToggleShowLogs;
    // End of variables declaration//GEN-END:variables

    
}
