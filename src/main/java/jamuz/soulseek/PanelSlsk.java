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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;
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
    private final TableColumnModel columnModelResults;
    private final TableColumnModel columnModelDownload;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private java.awt.Frame parent;

    /**
     * Update period: TODO: Make this an option
     */
    private static final long UPDATE_PERIODE = 500; //ms

    /**
     * Creates new form PanelRemote
     */
    public PanelSlsk() {
        initComponents();

        //Setup Search results table
        jTableResults.setModel(tableModelResults);
        columnModelResults = new TableColumnModel();
        //Assigning XTableColumnModel to allow show/hide columns
        jTableResults.setColumnModel(columnModelResults);
        jTableResults.createDefaultColumnsFromModel();
        setColumn(columnModelResults, 0, 120);	// Date
        TableColumn columnQueued
                = setColumn(columnModelResults, 1, 50);	// Queued

        setColumn(columnModelResults, 2, 150);	// Search text
        setColumn(columnModelResults, 3, 40);	// # files
        setColumn(columnModelResults, 4, 50);	// BitRate
        setColumn(columnModelResults, 5, 50);	// Size
        setColumn(columnModelResults, 6, 50);	// Speed
        setColumn(columnModelResults, 7, 50);	// Free upload spots
        setColumn(columnModelResults, 8, 50);	// Queue length
        setColumn(columnModelResults, 9, 150);	// Username
        setColumn(columnModelResults, 10, 600);	// Path
        TableColumn column
                = setColumn(columnModelResults, 11, 80);     // Completed
        column.setCellRenderer(new ProgressCellRender());
        columnModelResults.setColumnVisible(columnQueued, false);

        //Setup download table
        jTableDownload.setModel(new TableModelSlskdDownload());
        columnModelDownload = new TableColumnModel();
        //Assigning XTableColumnModel to allow show/hide columns
        jTableDownload.setColumnModel(columnModelDownload);
        jTableDownload.createDefaultColumnsFromModel();
        setColumn(columnModelDownload, 0, 120);    // Date
        setColumn(columnModelDownload, 1, 35);     // BitRate
        setColumn(columnModelDownload, 2, 80);     // Length
        setColumn(columnModelDownload, 3, 50);     // Size
        setColumn(columnModelDownload, 4, 300);    // File
        column
                = setColumn(columnModelDownload, 5, 400);     // Completed
        column.setCellRenderer(new ProgressCellRender());
    }

    public void initExtended(java.awt.Frame parent) {
        this.parent = parent;
        addMenuItem(Inter.get("Slsk.Retry")); //NOI18N
        addMenuItem(Inter.get("Slsk.Remove")); //NOI18N
        MouseListener popupListener = new PopupListener(jPopupMenu1);
        jTableResults.addMouseListener(popupListener);

        startDownloadMonitoring();

        //Restore searches from cache
        File slskCacheFolder = Jamuz.getFile("", "data", "cache", "slsk");
        for (File listFile : slskCacheFolder.listFiles()) {
            if (listFile.exists()) {
                SlskdSearchResponse searchResponse = readJson(listFile);
                if (searchResponse.isCompleted()) {
                    searchResponse.getProgressBar().progress("", 100);
                    searchResponse.files.forEach(f -> f.getProgressBar().progress("", 100));
                }
                tableModelResults.addRow(searchResponse);
            }
        }

        //Get options
        if (readOptions()) {
            boolean onStartup = Boolean.parseBoolean(options.get("slsk.on.startup", "false"));
            File file = new File(options.get("slsk.shared.location"));
            onStartup = onStartup && file.exists();
            if (onStartup) {
                startStopSlsk();
            }
        }
    }

    private boolean readOptions() {
        File propertiesFile = Jamuz.getFile("Slsk.properties");
        if (propertiesFile.exists()) {
            options = new Options(propertiesFile.getAbsolutePath());
            if (options.read()) {
                return true;
            }
        }
        return false;
    }

    private void startDownloadMonitoring() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new DownloadMonitoring(), 0, UPDATE_PERIODE, TimeUnit.MILLISECONDS);
    }

    private void stopAndWaitDownloadMonitoring() {
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private class DownloadMonitoring implements Runnable {

        @Override
        public void run() {

            for (int row = 0; row < tableModelResults.getRows().size(); row++) {
                SlskdSearchResponse searchResponse = tableModelResults.getRow(row);
                if (soulseek != null && !searchResponse.isCompleted()) {
                    //Get downloads for username
                    SlskdDownloadUser downloads = soulseek.getDownloads(searchResponse);
                    if (downloads != null) {
                        // Filter downloads: keep only the ones matching searchResponse (if username is a mess, he could have multiple albums on the same folder)
                        Map<String, SlskdDownloadFile> filteredFiles = downloads.directories
                                .stream()
                                .flatMap(directory -> directory.files.stream()
                                .filter(downloadFile
                                        -> searchResponse.files.stream()
                                        .anyMatch(searchFile -> searchFile.filename.equals(downloadFile.filename))
                                )
                                )
                                .collect(Collectors.toMap(
                                        SlskdDownloadFile::getKey,
                                        Function.identity(),
                                        (existing, replacement) -> existing
                                ));

                        if (searchResponse.getTableModel().getSearchResponse().equals(searchResponse)) {
                            displayDownloadProgress(searchResponse, filteredFiles);
                        }

                        int percentComplete = 0;
                        for (SlskdDownloadFile slskdDownloadFile : filteredFiles.values()) {
                            percentComplete += slskdDownloadFile.percentComplete;
                        }
                        searchResponse.update("", ((int) Math.round(percentComplete)) / filteredFiles.values().size());
                        tableModelResults.fireTableCellUpdated(row, 11);

                        boolean allFilesComplete = filteredFiles.values()
                                .stream()
                                .allMatch(file -> file.percentComplete == 100);
                        if (allFilesComplete) {
                            Location finalDestination = new Location("location.add");
                            if (finalDestination.check()) {
                                try {
                                    //Redisplay (to show 100% on all files) [here all Files are Complete + to do before transfers are deleted]
                                    displayDownloadProgress(searchResponse, filteredFiles);
                                    String sourcePath = Jamuz.getFile("", "slskd", "downloads").getAbsolutePath();
                                    for (SlskdDownloadFile downloadFile : filteredFiles.values()) {
                                        //Copy file
                                        Pair<String, String> directory = soulseek.getDirectory(downloadFile.filename);
                                        String subDirectoryName = directory.getLeft();
                                        String filename = directory.getRight();
                                        File sourceFile = new File(FilenameUtils.concat(FilenameUtils.concat(sourcePath, subDirectoryName), filename));
                                        String newSubDirectoryName = StringManager.removeIllegal(searchResponse.getSearchText() + "--" + searchResponse.username + "--" + searchResponse.getPath());
                                        File destFile = new File(FilenameUtils.concat(FilenameUtils.concat(finalDestination.getValue(), newSubDirectoryName), filename));
                                        if (sourceFile.exists() && (!destFile.exists() || sourceFile.length() != destFile.length())) {
                                            FileUtils.copyFile(sourceFile, destFile);
                                        }
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

        public void displayDownloadProgress(SlskdSearchResponse searchResponse, Map<String, SlskdDownloadFile> filteredFiles) {
            List<SlskdSearchFile> rows = searchResponse.getTableModel().getRows();
            for (int i = 0; i < rows.size(); i++) {
                SlskdSearchFile rowFile = rows.get(i);
                if (filteredFiles.containsKey(rowFile.getKey())) {
                    SlskdDownloadFile filteredFile = filteredFiles.get(rowFile.getKey());
                    rowFile.update(filteredFile);
                }
            }
            searchResponse.getTableModel().fireTableDataChanged();
        }
    }

    private void addMenuItem(String item) {
        JMenuItem menuItem = new JMenuItem(item);
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
    }

    ActionListener menuListener = (ActionEvent e) -> {
        JMenuItem source = (JMenuItem) (e.getSource());
        String sourceTxt = source.getText();
        if (sourceTxt.equals(Inter.get("Slsk.Retry"))) { //NOI18N
            SlskdSearchResponse searchResponse = getSelected();
            if (searchResponse != null) {
                new Thread() {
                    @Override
                    public void run() {
                        stopAndWaitDownloadMonitoring();
                        for (SlskdSearchFile file : searchResponse.files) {
                            if (file.percentComplete < 100) {
                                soulseek.deleteTransfer(searchResponse.username, file);
                            }
                        }
                        soulseek.download(searchResponse);
                        startDownloadMonitoring();
                    }
                }.start();
            }
        } else if (sourceTxt.equals(Inter.get("Slsk.Remove"))) { //NOI18N
            SlskdSearchResponse searchResponse = getSelected();
            if (searchResponse != null) {
                PanelSlsk panelSlsk = this;
                new Thread() {
                    @Override
                    public void run() {
                        stopAndWaitDownloadMonitoring();
                        if (searchResponse.isCompleted()) {
                            File file = getJsonFile(searchResponse);
                            if (file.exists()) {
                                file.delete();
                            }
                            tableModelResults.removeRow(searchResponse);
                        } else {
                            int n = JOptionPane.showConfirmDialog(
                                    panelSlsk, "Annuler et supprimer ?",
                                    Inter.get("Label.Confirm"), //NOI18N
                                    JOptionPane.YES_NO_OPTION);
                            if (n == JOptionPane.YES_OPTION) {
                                for (SlskdSearchFile searchFile : searchResponse.files) {
                                    soulseek.deleteTransfer(searchResponse.username, searchFile);
                                    soulseek.deleteFile(searchFile);
                                }
                                File file = getJsonFile(searchResponse);
                                if (file.exists()) {
                                    file.delete();
                                }
                                tableModelResults.removeRow(searchResponse);
                            }
                        }
                        displaySearchFiles();
                        startDownloadMonitoring();
                    }
                }.start();
            }
        } else {
            Popup.error("Unknown menu item: " + sourceTxt); //NOI18N
        }
    };

    private SlskdSearchResponse getSelected() {
        int selectedRow = jTableResults.getSelectedRow();
        if (selectedRow >= 0) {
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
        column.setMaxWidth(width * 3);
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
        jButtonSlskOptions = new javax.swing.JButton();

        jPanelSlsk.setBorder(javax.swing.BorderFactory.createTitledBorder("Soulseek"));

        jButtonStart.setText(Inter.get("Button.Start")); // NOI18N
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });

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
        jTextAreaLog.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 13)); // NOI18N
        jTextAreaLog.setLineWrap(true);
        jTextAreaLog.setRows(5);
        jScrollPaneLog.setViewportView(jTextAreaLog);

        jPanel1.add(jScrollPaneLog, java.awt.BorderLayout.NORTH);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setPreferredSize(new java.awt.Dimension(0, 0));

        jScrollPaneCheckTags3.setPreferredSize(new java.awt.Dimension(0, 0));

        jTableResults.setAutoCreateColumnsFromModel(false);
        jTableResults.setModel(new jamuz.soulseek.TableModelSlskdSearch());
        jTableResults.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableResults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableResultsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableResultsMousePressed(evt);
            }
        });
        jScrollPaneCheckTags3.setViewportView(jTableResults);

        jSplitPane1.setTopComponent(jScrollPaneCheckTags3);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 0));

        jTableDownload.setAutoCreateColumnsFromModel(false);
        jTableDownload.setModel(new jamuz.soulseek.TableModelSlskdDownload());
        jTableDownload.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(jTableDownload);

        jSplitPane1.setBottomComponent(jScrollPane2);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jButtonSlskOptions.setText("Options");
        jButtonSlskOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSlskOptionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSlskLayout = new javax.swing.GroupLayout(jPanelSlsk);
        jPanelSlsk.setLayout(jPanelSlskLayout);
        jPanelSlskLayout.setHorizontalGroup(
            jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSlskLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelSlskLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 872, Short.MAX_VALUE)
                        .addComponent(jToggleShowLogs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonWebSlskd)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSlskOptions)
                        .addGap(12, 12, 12)
                        .addComponent(jButtonStart)))
                .addContainerGap())
        );
        jPanelSlskLayout.setVerticalGroup(
            jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSlskLayout.createSequentialGroup()
                .addGroup(jPanelSlskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonStart)
                    .addComponent(jButtonWebSlskd)
                    .addComponent(jToggleShowLogs)
                    .addComponent(jButtonSlskOptions))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
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
                enableOptions(false);

                //Re-read options as they could have changed
                if (!readOptions()) {
                    enableStart();
                    return;
                }

                //Start slskd server, if not already running
                SlskdDocker slskdDocker = new SlskdDocker(
                        options.get("slsk.username"),
                        options.get("slsk.password"),
                        Jamuz.getFile("", "slskd").getAbsolutePath(),
                        options.get("slsk.shared.location"),
                        Boolean.parseBoolean(options.get("slsk.reCreate", "true")));

                if (jButtonStart.getText().equals(Inter.get("Button.Start"))) {
                    jButtonStart.setText("Starting ...");
                    jTextAreaLog.setText("Checking slsk status and restart if needed...\n");
                    File file = new File(options.get("slsk.shared.location"));
                    if (!file.exists()) {
                        Popup.warning("Shared folder does not exist: " + file);
                        enableStart();
                    } else if (!slskdDocker.start()) {
                        Popup.warning("Could not start slskd");
                        enableStart();
                    } else {
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
                                        if (Boolean.parseBoolean(options.get("slsk.reCreate", "true"))) {
                                            if (soulseek.rescanShares()) {
                                                options.set("slsk.reCreate", "false");
                                                options.save();
                                            } else {
                                                Popup.error("Could not rescan share. Refer to slsk logs.");
                                            }
                                        }
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
                    }
                } else {
                    slskdDocker.stop();
                    enableStart();
                }
            }
        }.start();
    }

    public void enableGui(boolean enable) {
        jButtonStart.setEnabled(enable);
    }

    public void enableOptions(boolean enable) {
        jButtonSlskOptions.setEnabled(enable);
    }

    private void enableStart() {
        jButtonStart.setText(Inter.get("Button.Start"));
        displayLogs(true);
        enableGui(true);
        enableOptions(true);
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

    private void jToggleShowLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleShowLogsActionPerformed
        displayLogs(jToggleShowLogs.isSelected());
    }//GEN-LAST:event_jToggleShowLogsActionPerformed

    private void jTableResultsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableResultsMousePressed
        // If Right mouse click, select the line under mouse
        if (SwingUtilities.isRightMouseButton(evt)) {
            Point p = evt.getPoint();
            int rowNumber = jTableResults.rowAtPoint(p);
            ListSelectionModel model = jTableResults.getSelectionModel();
            model.setSelectionInterval(rowNumber, rowNumber);
        }
    }//GEN-LAST:event_jTableResultsMousePressed

    private void jButtonSlskOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSlskOptionsActionPerformed
        DialogSlskOption.main(parent);
    }//GEN-LAST:event_jButtonSlskOptionsActionPerformed

    private void displaySearchFiles() {
        SlskdSearchResponse searchResponse = getSelected();
        jTableDownload.setModel((searchResponse != null) ? searchResponse.getTableModel() : new TableModelSlskdDownload());

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
                Type mapType = new TypeToken<SlskdSearchResponse>() {
                }.getType();
                SlskdSearchResponse searchResponse = gson.fromJson(readJson, mapType);
                return searchResponse;
            }
        } catch (IOException ex) {
            Logger.getLogger(PanelSlsk.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    void addDownload(SlskdSearchResponse searchResponse) {
        if (soulseek != null) {

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton jButtonSlskOptions;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JButton jButtonWebSlskd;
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
    private javax.swing.JToggleButton jToggleShowLogs;
    // End of variables declaration//GEN-END:variables

}
