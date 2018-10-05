/*
 * Copyright (C) 2014 phramusca ( https://github.com/phramusca/JaMuz/ )
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
import jamuz.gui.DialogOptions;
import jamuz.gui.PanelMain;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableModelCheck;
import jamuz.gui.swing.WrapLayout;
import jamuz.process.check.ProcessCheck.CheckType;
import jamuz.utils.Inter;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelCheck extends javax.swing.JPanel {

    private static ProcessCheck processCheck;

    /**
     * Folders browse progress bar
     */
    protected static ProgressBar progressBarFolders;
//    protected static ProgressBar progressBarFiles;

    /**
     * Scan progress bar
     */
    protected static ProgressBar progressBarScanSize;

	/**
	 *
	 */
	protected static List<ProgressBar> progressBarListScanDequeue=new ArrayList<>();
    
    /**
     * Analysis progress bar
     */
    protected static ProgressBar progressBarAnalysisSize;

	/**
	 *
	 */
	protected static List<ProgressBar> progressBarListAnalysisDequeue=new ArrayList<>();
    
    /**
     * Actions progress bar
     */
    protected static ProgressBar progressActionsSize;

	/**
	 *
	 */
	protected static ProgressBar progressActionsDequeue;
    
    private JButton jButtonActionInJList;

	/**
	 *
	 */
	public static TableModelCheck tableModelActionQueue = new TableModelCheck();
       
    /**
     * Creates new form PanelCheck
     */
    public PanelCheck() {
        initComponents();
		
		int cores = Runtime.getRuntime().availableProcessors();
		int coresMax = cores-4;
		coresMax=coresMax<2?2:coresMax;
		int coresValue = Math.round(coresMax/2);
		jSpinnerCheckScanNbThreads.setModel(new javax.swing.SpinnerNumberModel(coresValue, 1, coresMax, 1));
		jSpinnerCheckAnalysisNbThreads.setModel(new javax.swing.SpinnerNumberModel(coresValue, 1, coresMax, 1));
		
        processCheck = new ProcessCheck();
        jSpinnerCheckMaxActionsInQueue.setValue(processCheck.getMaxActionQueueSize());
        //Set table model
        jTableCheck.setModel(tableModelActionQueue);

		//Adding columns from model. Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableCheck.createDefaultColumnsFromModel();
		
		TableColumn column;

        // 0: Button		
		column = jTableCheck.getColumnModel().getColumn(0);
		column.setMinWidth(100);
		column.setMaxWidth(100);
		jButtonActionInJList=new JButton(""); //NOI18N
		column.setCellRenderer((JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column1) -> {
			FolderInfo folder = (FolderInfo) value;
			
			String res="/jamuz/ressources/"; //NOI18N
			Color color=Color.WHITE;
			switch(folder.action) {
				case DEL:
					res+="bin.png"; //NOI18N
					color=Color.DARK_GRAY;
					break;
				case KO:
					res+="cancel.png"; //NOI18N
					color=Color.RED;
					break;
				case OK:
					res+="accept.png"; //NOI18N
					color=new Color(0, 128, 0);
					break;
				case WARNING:
					res+="accept.png"; //NOI18N
					color=Color.ORANGE;
					break;
				case SAVE:
					res+="application_form_edit.png"; //NOI18N
					color=new Color(0,153,143);
					break;
				case ANALYZING:
					res+="search_plus.png"; //NOI18N
					color=Color.LIGHT_GRAY;
					break;
				case MANUAL:
					res+="document_todo.png";
					break;
			}
			jButtonActionInJList.setIcon(new javax.swing.ImageIcon(getClass().getResource(res)));
			jButtonActionInJList.setText(folder.action.toString());
			jButtonActionInJList.setBackground(color);
			return jButtonActionInJList;
		});  
		column.setCellEditor(new ButtonCheck());
        
		//	0:  Folder
		column = jTableCheck.getColumnModel().getColumn(0);
		column.setMinWidth(200);
//		column.setMaxWidth(200);
		column.setPreferredWidth(500);
		       
        jTableCheck.setRowHeight(50);
        
        progressBarFolders          = (ProgressBar)jProgressBarFolders;
        
        progressBarScanSize     = (ProgressBar)jProgressBarCheckScanSize;        
        progressBarAnalysisSize     = (ProgressBar)jProgressBarCheckAnalysisSize;
        
        progressActionsSize         = (ProgressBar)jProgressBarCheckActionsSize;
        progressActionsDequeue      = (ProgressBar)jProgressBarCheckActionsDequeue;
    }
    
    public static void setThreadPanels(ProcessCheck.CheckType checkType) {
        jPanelAnalysisMain.setVisible(!(
                checkType.equals(CheckType.SCAN_QUICK) 
                        || checkType.equals(CheckType.SCAN_FULL) 
                        || checkType.equals(CheckType.SCAN_DELETED)));
        
        setThreadPanel(jPanelScan, 
				(int) jSpinnerCheckScanNbThreads.getValue(), 
				progressBarListScanDequeue, 450, true);
        setThreadPanel(jPanelAnalysis, 
				(int) jSpinnerCheckAnalysisNbThreads.getValue(), 
				progressBarListAnalysisDequeue, 80, true);
    }
    
//    private static void setThreadPanel(JPanel panel, int nbThread, List<ProgressBar> progressBarList) {
//        setThreadPanel(panel, nbThread, progressBarList, 42, false);
//    }
    
    private static void setThreadPanel(JPanel panel, int nbThread, List<ProgressBar> progressBarList, int progressBarWidth, boolean setStringPainted) {
        Dimension dim = new Dimension(progressBarWidth, 21);
        panel.setLayout(new WrapLayout(FlowLayout.LEADING, 0, 0));
        progressBarList.clear();
        panel.removeAll();
        for(int i=0;i<nbThread;i++) {
            ProgressBar progressBar = new ProgressBar();
            progressBar.setSize(dim);
            progressBar.setMaximumSize(dim);
            progressBar.setMinimumSize(dim);
            progressBar.setPreferredSize(dim);
            if(setStringPainted) {
                progressBar.setString("");
                progressBar.setStringPainted(true);
            }
            progressBarList.add(progressBar);
            panel.add(progressBar);
        }
        panel.validate();
        panel.repaint();
    }
    
    /**
    * Enables row sorter on Best Of Album table
    * @param enable
    */
   public static void enableRowSorter(boolean enable) {
           if(enable) {
                   //Enable row tableSorter (cannot be done if model is empty)
                   if(tableModelActionQueue.getRowCount()>0) {
           if(jTableCheck.getAutoCreateRowSorter()==false) { //No need if already done, avoid blicking
               //Enable auto sorter
               jTableCheck.setAutoCreateRowSorter(true);
               //Sort by action, result
               TableRowSorter<TableModelCheck> tableSorter = new TableRowSorter<>(tableModelActionQueue);
               jTableCheck.setRowSorter(tableSorter);
               List <RowSorter.SortKey> sortKeys = new ArrayList<>();
               sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
               sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
               tableSorter.setSortKeys(sortKeys);
           }
                   }
                   else {
                           jTableCheck.setAutoCreateRowSorter(false);
                   }
           }
           else {
       jTableCheck.setAutoCreateRowSorter(false);
                   jTableCheck.setRowSorter(null);
           }
   }
    
    /**
     * Add folder to actions queue
     * @param folder
     */
    public static void addToActionQueue(FolderInfo folder) {
        Jamuz.getLogger().log(Level.FINEST, "addToActionQueue({0})", folder.getRelativePath());
       //Add to action queue if not already inside (when pressing a button (OK, KO. ..) on DialogCheck and doActions did not ran through folder yet)
        if(!processCheck.actionQueue.contains(folder)) {
            Jamuz.getLogger().log(Level.FINEST, "NOT FOUND IN QUEUE ({0})", folder.getRelativePath());
            processCheck.actionQueue.add(folder);
        }
        //Reflect folder changes (action) in jTable
        tableModelActionQueue.fireTableDataChanged();
        processCheck.displayActionQueue();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelCheckProcess = new javax.swing.JPanel();
        jProgressBarFolders = new jamuz.gui.swing.ProgressBar();
        jButtonCheckAbort = new javax.swing.JButton();
        jButtonMergeSources = new javax.swing.JButton();
        jButtonCheckNew = new javax.swing.JButton();
        jButtonCheckLibrary = new javax.swing.JButton();
        jButtonScanLibrary = new javax.swing.JButton();
        jCheckBoxCheckFull = new javax.swing.JCheckBox();
        jButtonScanDeleted = new javax.swing.JButton();
        jPanelActionsMain = new javax.swing.JPanel();
        jCheckBoxDoActions = new javax.swing.JCheckBox();
        jProgressBarCheckActionsSize = new jamuz.gui.swing.ProgressBar();
        jCheckBoxCheckKO = new javax.swing.JCheckBox();
        jCheckBoxCheckWarning = new javax.swing.JCheckBox();
        jCheckBoxCheckManual = new javax.swing.JCheckBox();
        jProgressBarCheckActionsDequeue = new jamuz.gui.swing.ProgressBar();
        jScrollPaneCheckTags2 = new javax.swing.JScrollPane();
        jTableCheck = new jamuz.gui.swing.TableHorizontal();
        jSpinnerCheckMaxActionsInQueue = new javax.swing.JSpinner();
        jPanelScanMain = new javax.swing.JPanel();
        jPanelScan = new javax.swing.JPanel();
        jProgressBarCheckScanSize = new jamuz.gui.swing.ProgressBar();
        jSpinnerCheckScanNbThreads = new javax.swing.JSpinner();
        jPanelAnalysisMain = new javax.swing.JPanel();
        jPanelAnalysis = new javax.swing.JPanel();
        jProgressBarCheckAnalysisSize = new jamuz.gui.swing.ProgressBar();
        jSpinnerCheckAnalysisNbThreads = new javax.swing.JSpinner();

        jPanelCheckProcess.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Process")));

        jProgressBarFolders.setString(""); // NOI18N
        jProgressBarFolders.setStringPainted(true);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonCheckAbort.setText(bundle.getString("Button.Abort")); // NOI18N
        jButtonCheckAbort.setEnabled(false);
        jButtonCheckAbort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckAbortActionPerformed(evt);
            }
        });

        jButtonMergeSources.setText(Inter.get("Label.Options")); // NOI18N
        jButtonMergeSources.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMergeSourcesActionPerformed(evt);
            }
        });

        jButtonCheckNew.setText(Inter.get("Label.Scan.CheckNew")); // NOI18N
        jButtonCheckNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckNewActionPerformed(evt);
            }
        });

        jButtonCheckLibrary.setText(Inter.get("Label.Scan.CheckLibrary")); // NOI18N
        jButtonCheckLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckLibraryActionPerformed(evt);
            }
        });

        jButtonScanLibrary.setText(Inter.get("Label.Scan")); // NOI18N
        jButtonScanLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScanLibraryActionPerformed(evt);
            }
        });

        jCheckBoxCheckFull.setText(Inter.get("Label.Scan.Full")); // NOI18N

        jButtonScanDeleted.setText("Scan deleted");
        jButtonScanDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScanDeletedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCheckProcessLayout = new javax.swing.GroupLayout(jPanelCheckProcess);
        jPanelCheckProcess.setLayout(jPanelCheckProcessLayout);
        jPanelCheckProcessLayout.setHorizontalGroup(
            jPanelCheckProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCheckProcessLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonCheckAbort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jButtonCheckNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckLibrary)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarFolders, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonScanLibrary)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonScanDeleted)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxCheckFull)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(jButtonMergeSources, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelCheckProcessLayout.setVerticalGroup(
            jPanelCheckProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCheckProcessLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanelCheckProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonCheckAbort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelCheckProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonMergeSources, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCheckNew)
                        .addComponent(jButtonCheckLibrary)
                        .addComponent(jButtonScanLibrary)
                        .addComponent(jCheckBoxCheckFull)
                        .addComponent(jProgressBarFolders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonScanDeleted)))
                .addContainerGap())
        );

        jPanelActionsMain.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jCheckBoxDoActions.setText("Do actions ?");
        jCheckBoxDoActions.setEnabled(false);
        jCheckBoxDoActions.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxDoActionsItemStateChanged(evt);
            }
        });

        jProgressBarCheckActionsSize.setString("0/0"); // NOI18N
        jProgressBarCheckActionsSize.setStringPainted(true);

        jCheckBoxCheckKO.setText(Inter.get("Button.KO")); // NOI18N
        jCheckBoxCheckKO.setEnabled(false);

        jCheckBoxCheckWarning.setText(Inter.get("Check.OK.Warning")); // NOI18N
        jCheckBoxCheckWarning.setEnabled(false);

        jCheckBoxCheckManual.setText("Manual");
        jCheckBoxCheckManual.setEnabled(false);

        jProgressBarCheckActionsDequeue.setString(""); // NOI18N
        jProgressBarCheckActionsDequeue.setStringPainted(true);

        jTableCheck.setAutoCreateColumnsFromModel(false);
        jTableCheck.setModel(new jamuz.gui.swing.TableModelCheck());
        jTableCheck.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCheck.setAutoscrolls(false);
        jScrollPaneCheckTags2.setViewportView(jTableCheck);

        jSpinnerCheckMaxActionsInQueue.setModel(new javax.swing.SpinnerNumberModel(4, 0, 99, 1));
        jSpinnerCheckMaxActionsInQueue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerCheckMaxActionsInQueueStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanelActionsMainLayout = new javax.swing.GroupLayout(jPanelActionsMain);
        jPanelActionsMain.setLayout(jPanelActionsMainLayout);
        jPanelActionsMainLayout.setHorizontalGroup(
            jPanelActionsMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelActionsMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSpinnerCheckMaxActionsInQueue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxDoActions)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxCheckKO)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxCheckWarning)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxCheckManual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarCheckActionsSize, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarCheckActionsDequeue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPaneCheckTags2)
        );
        jPanelActionsMainLayout.setVerticalGroup(
            jPanelActionsMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelActionsMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelActionsMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxCheckKO)
                    .addComponent(jCheckBoxDoActions)
                    .addComponent(jCheckBoxCheckWarning)
                    .addComponent(jCheckBoxCheckManual)
                    .addComponent(jProgressBarCheckActionsSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBarCheckActionsDequeue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSpinnerCheckMaxActionsInQueue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags2, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
        );

        jPanelScanMain.setBorder(javax.swing.BorderFactory.createTitledBorder("Scan"));

        javax.swing.GroupLayout jPanelScanLayout = new javax.swing.GroupLayout(jPanelScan);
        jPanelScan.setLayout(jPanelScanLayout);
        jPanelScanLayout.setHorizontalGroup(
            jPanelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelScanLayout.setVerticalGroup(
            jPanelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jProgressBarCheckScanSize.setMaximumSize(new java.awt.Dimension(150, 21));
        jProgressBarCheckScanSize.setMinimumSize(new java.awt.Dimension(150, 21));
        jProgressBarCheckScanSize.setString("0/0"); // NOI18N
        jProgressBarCheckScanSize.setStringPainted(true);

        javax.swing.GroupLayout jPanelScanMainLayout = new javax.swing.GroupLayout(jPanelScanMain);
        jPanelScanMain.setLayout(jPanelScanMainLayout);
        jPanelScanMainLayout.setHorizontalGroup(
            jPanelScanMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScanMainLayout.createSequentialGroup()
                .addComponent(jProgressBarCheckScanSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinnerCheckScanNbThreads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanelScan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelScanMainLayout.setVerticalGroup(
            jPanelScanMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScanMainLayout.createSequentialGroup()
                .addGroup(jPanelScanMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jProgressBarCheckScanSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerCheckScanNbThreads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelScan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelAnalysisMain.setBorder(javax.swing.BorderFactory.createTitledBorder("Analysis"));

        jPanelAnalysis.setPreferredSize(new java.awt.Dimension(0, 13));

        javax.swing.GroupLayout jPanelAnalysisLayout = new javax.swing.GroupLayout(jPanelAnalysis);
        jPanelAnalysis.setLayout(jPanelAnalysisLayout);
        jPanelAnalysisLayout.setHorizontalGroup(
            jPanelAnalysisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelAnalysisLayout.setVerticalGroup(
            jPanelAnalysisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jProgressBarCheckAnalysisSize.setMaximumSize(new java.awt.Dimension(150, 21));
        jProgressBarCheckAnalysisSize.setMinimumSize(new java.awt.Dimension(150, 21));
        jProgressBarCheckAnalysisSize.setString("0/0"); // NOI18N
        jProgressBarCheckAnalysisSize.setStringPainted(true);

        javax.swing.GroupLayout jPanelAnalysisMainLayout = new javax.swing.GroupLayout(jPanelAnalysisMain);
        jPanelAnalysisMain.setLayout(jPanelAnalysisMainLayout);
        jPanelAnalysisMainLayout.setHorizontalGroup(
            jPanelAnalysisMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAnalysisMainLayout.createSequentialGroup()
                .addComponent(jProgressBarCheckAnalysisSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinnerCheckAnalysisNbThreads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanelAnalysis, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
        );
        jPanelAnalysisMainLayout.setVerticalGroup(
            jPanelAnalysisMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAnalysisMainLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanelAnalysisMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jProgressBarCheckAnalysisSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerCheckAnalysisNbThreads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelAnalysis, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelActionsMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelCheckProcess, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanelScanMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelAnalysisMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelCheckProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelAnalysisMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelScanMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelActionsMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCheckAbortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckAbortActionPerformed
        enableCheckButtons(false);
        jButtonCheckAbort.setText(Inter.get("Button.Aborting")); 			  //NOI18N
        jButtonCheckAbort.setEnabled(false);
        processCheck.stopCheck();
    }//GEN-LAST:event_jButtonCheckAbortActionPerformed

    private void jButtonMergeSourcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMergeSourcesActionPerformed
        DialogOptions.main(Jamuz.getMachine().getName());
    }//GEN-LAST:event_jButtonMergeSourcesActionPerformed

    private void jCheckBoxDoActionsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxDoActionsItemStateChanged
        if(processCheck!=null) { //Happens before any process has been started
            if(evt.getStateChange()==ItemEvent.SELECTED) {
                enableActionCheckBoxes(false);
                processCheck.startActions(jCheckBoxCheckKO.isSelected(), jCheckBoxCheckWarning.isSelected(), jCheckBoxCheckManual.isSelected());
            }
            else {
                stopActions(true);
            }
        }
    }//GEN-LAST:event_jCheckBoxDoActionsItemStateChanged

    private static void enableActionCheckBoxes(boolean enable) {
        jCheckBoxCheckKO.setEnabled(enable);
        jCheckBoxCheckWarning.setEnabled(enable);
        jCheckBoxCheckManual.setEnabled(enable); 
    }
    
	/**
	 *
	 * @param enableDoActions
	 */
	public static void stopActions(boolean enableDoActions) {
        
        //Abort doActions
        processCheck.stopActions();
        jCheckBoxDoActions.setSelected(false); //TODO: Does not this fire event to stopActions ?

        //Enable OR not doActions (and optionally "Manual", that's for "Check Library")
        jCheckBoxDoActions.setEnabled(enableDoActions); 
        enableActionCheckBoxes(enableDoActions);
        
        //Deselect all anyway, user needs to re-select at each new check, that is wanted behavior
        jCheckBoxCheckKO.setSelected(false);
        jCheckBoxCheckWarning.setSelected(false);
        jCheckBoxCheckManual.setSelected(false);
    }
    
    private void jButtonCheckNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckNewActionPerformed
//        jButton1.setFont(jButton1.getFont().deriveFont(Font.BOLD));
        //TODO: Better derive ? Can it be for size ?
        jButtonCheckNew.setFont(new Font(jButtonCheckNew.getFont().getName(), Font.BOLD, 16));
        startProcess(true, CheckType.CHECK_NEW, -1);
    }//GEN-LAST:event_jButtonCheckNewActionPerformed

    private void jButtonCheckLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckLibraryActionPerformed
//        jButton2.setFont(jButton2.getFont().deriveFont(Font.BOLD));
        jButtonCheckLibrary.setFont(new Font(jButtonCheckLibrary.getFont().getName(), Font.BOLD, 16));
        startProcess(true, CheckType.CHECK_DB, -1);
        jCheckBoxCheckManual.setEnabled(false);
    }//GEN-LAST:event_jButtonCheckLibraryActionPerformed

    private void jButtonScanLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScanLibraryActionPerformed
        CheckType checkedType=(jCheckBoxCheckFull.isSelected()?CheckType.SCAN_FULL:CheckType.SCAN_QUICK);
//        jButton3.setFont(jButton3.getFont().deriveFont(Font.BOLD));
        jButtonScanLibrary.setFont(new Font(jButtonScanLibrary.getFont().getName(), Font.BOLD, 16));
        startProcess(false, checkedType, -1);  
    }//GEN-LAST:event_jButtonScanLibraryActionPerformed

    private void jButtonScanDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScanDeletedActionPerformed
//        jButton3.setFont(jButton3.getFont().deriveFont(Font.BOLD));
        jButtonScanDeleted.setFont(new Font(jButtonScanDeleted.getFont().getName(), Font.BOLD, 16));
        startProcess(false, CheckType.SCAN_DELETED, -1);  
    }//GEN-LAST:event_jButtonScanDeletedActionPerformed

    private void jSpinnerCheckMaxActionsInQueueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerCheckMaxActionsInQueueStateChanged
        processCheck.setMaxActionQueueSize((int) jSpinnerCheckMaxActionsInQueue.getValue());
    }//GEN-LAST:event_jSpinnerCheckMaxActionsInQueueStateChanged

    /**
     * Check given path
     * @param idPath
     */
    public static void check(int idPath) {
        if(!processCheck.isCheckAlive()) {
            startProcess(true, CheckType.CHECK_FOLDER, idPath);
            //Select Check tab
            PanelMain.selectTab(Inter.get("Label.Check")); //NOI18N
        }
    }
    
    private static void startProcess(boolean enableDoActions, ProcessCheck.CheckType checkType, int idPath) {
        enableCheck(false);
        enableRowSorter(false);
        stopActions(enableDoActions);
        if(tableModelActionQueue.getRowCount()>0) {
            int n = JOptionPane.showConfirmDialog(
					null, Inter.get("Question.Check.RemainingActions"),
					Inter.get("Label.Confirm"),  //NOI18N
					JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.NO_OPTION) {
                enableCheck(true);
                return;
            } 
        }
        setThreadPanels(checkType);
        jPanelActionsMain.setVisible(enableDoActions);
        tableModelActionQueue.clear();
        processCheck.startCheck(
				checkType, 
				idPath, 
				(int) jSpinnerCheckAnalysisNbThreads.getValue(), 
				(int) jSpinnerCheckScanNbThreads.getValue());
    }

    
    /**
     * Refresh options in GUI
     */
    public static void setOptions() {
        //Exit if processCheck is running
        if(processCheck!=null) {
            if(processCheck.isCheckAlive()) {
                return;
            }
        }
        //processCheck is not running, so we can set these up
        boolean isMaster=Jamuz.getMachine().getOptionValue("library.isMaster").equals("true");
        jButtonCheckLibrary.setEnabled(isMaster);
        jButtonScanLibrary.setEnabled(isMaster);
        jButtonScanDeleted.setEnabled(isMaster);
	}
    
//    private static String boldHTML(String inStr) {
//		return "<html><b>"+inStr+"</b></html>";  //NOI18N
//	}
    
    /**
	 * Enable check
	 * @param enable
	 */
    public static void enableCheck(boolean enable) {
        if(enable) {
            jPanelScan.removeAll();
            jPanelAnalysis.removeAll();
            jPanelScanMain.setVisible(true);
            jPanelAnalysisMain.setVisible(true);
            jPanelActionsMain.setVisible(true);
            
            
//            jButton1.setFont(jButton1.getFont().deriveFont(Font.PLAIN));
//            jButton2.setFont(jButton2.getFont().deriveFont(Font.PLAIN));
//            jButton3.setFont(jButton3.getFont().deriveFont(Font.PLAIN));
            jButtonCheckNew.setFont(new Font(jButtonCheckNew.getFont().getName(), Font.PLAIN, 12));
            jButtonCheckLibrary.setFont(new Font(jButtonCheckLibrary.getFont().getName(), Font.PLAIN, 12));
            jButtonScanLibrary.setFont(new Font(jButtonScanLibrary.getFont().getName(), Font.PLAIN, 12));
			jButtonScanDeleted.setFont(new Font(jButtonScanDeleted.getFont().getName(), Font.PLAIN, 12));
            jButtonCheckAbort.setText(Inter.get("Button.Abort"));  //NOI18N
            jButtonCheckAbort.setEnabled(false);
            
            PanelCheck.progressBarScanSize.reset();
            PanelCheck.progressBarAnalysisSize.reset();
        }
        enableCheckButtons(enable);
    }

    /**
	 * Enable check start button
	 * @param enabled
	 */
    public static void enableCheckButtons(boolean enabled) {
        jButtonCheckNew.setEnabled(enabled);
        jSpinnerCheckScanNbThreads.setEnabled(enabled);
        jSpinnerCheckAnalysisNbThreads.setEnabled(enabled);
        boolean isMaster=Jamuz.getMachine().getOptionValue("library.isMaster").equals("true");
        jButtonCheckLibrary.setEnabled(enabled && isMaster);
        jButtonScanLibrary.setEnabled(enabled && isMaster);
        jButtonScanDeleted.setEnabled(enabled && isMaster);
        jCheckBoxCheckFull.setEnabled(enabled && isMaster);
    }
    
	/**
	 *
	 */
	public static void enableAbortButton() {
        jButtonCheckAbort.setEnabled(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton jButtonCheckAbort;
    private static javax.swing.JButton jButtonCheckLibrary;
    private static javax.swing.JButton jButtonCheckNew;
    private javax.swing.JButton jButtonMergeSources;
    private static javax.swing.JButton jButtonScanDeleted;
    private static javax.swing.JButton jButtonScanLibrary;
    private static javax.swing.JCheckBox jCheckBoxCheckFull;
    private static javax.swing.JCheckBox jCheckBoxCheckKO;
    private static javax.swing.JCheckBox jCheckBoxCheckManual;
    private static javax.swing.JCheckBox jCheckBoxCheckWarning;
    protected static javax.swing.JCheckBox jCheckBoxDoActions;
    private static javax.swing.JPanel jPanelActionsMain;
    private static javax.swing.JPanel jPanelAnalysis;
    private static javax.swing.JPanel jPanelAnalysisMain;
    protected static javax.swing.JPanel jPanelCheckProcess;
    private static javax.swing.JPanel jPanelScan;
    private static javax.swing.JPanel jPanelScanMain;
    private static javax.swing.JProgressBar jProgressBarCheckActionsDequeue;
    private static javax.swing.JProgressBar jProgressBarCheckActionsSize;
    private static javax.swing.JProgressBar jProgressBarCheckAnalysisSize;
    private static javax.swing.JProgressBar jProgressBarCheckScanSize;
    private static javax.swing.JProgressBar jProgressBarFolders;
    private static javax.swing.JScrollPane jScrollPaneCheckTags2;
    private static javax.swing.JSpinner jSpinnerCheckAnalysisNbThreads;
    private static javax.swing.JSpinner jSpinnerCheckMaxActionsInQueue;
    private static javax.swing.JSpinner jSpinnerCheckScanNbThreads;
    private static javax.swing.JTable jTableCheck;
    // End of variables declaration//GEN-END:variables
}
