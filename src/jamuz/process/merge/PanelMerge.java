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

package jamuz.process.merge;

import jamuz.gui.swing.CheckBoxListItem;
import jamuz.FileInfo;
import jamuz.Jamuz;
import jamuz.gui.DialogOptions;
import jamuz.gui.DialogOptionsNew;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableModel;
import jamuz.utils.Inter;
//TODO: Display lastMergeDate in jListMerge: <Source name> (<lastMergeDate or BETTER formatted interval ex: a week ago, 3 hours ago, ...)>)
/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelMerge extends javax.swing.JPanel {

    private static ProcessMerge processMerge;
    private static TableModel tableModel;

    /**
     * merge process progress bar
     */
    protected static ProgressBar progressBar;
    
    /**
     * Creates new form PanelMerge
     */
    public PanelMerge() {
        initComponents();
    }

    /**
     * GUI extended init
     */
    public void initExtended() {
		//Get jTableMerge model
		tableModel = (TableModel) jTableMerge.getModel();
		//Set table model
		String[] columnNames = {Inter.get("Label.File"), Inter.get("Label.Result"), Inter.get("Stat.PlayCounter")  //NOI18N
				, Inter.get("Stat.Rating"), Inter.get("Stat.Added"), Inter.get("Stat.LastPlayed")};  //NOI18N
		Object[][] data = {
			{"Default", "Default", "Default", "Default", "Default", "Default"}  //NOI18N
		};
		tableModel.setModel(columnNames, data);
		//clear the table
		tableModel.clear();
		
		//Adding columns from model. Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableMerge.createDefaultColumnsFromModel();	
		
		TableColumn column;
		
		//Set File column width
		column = jTableMerge.getColumnModel().getColumn(0);
		column.setMinWidth(100);
		column.setPreferredWidth(200);
		//Set Result column width
		column = jTableMerge.getColumnModel().getColumn(1);
		column.setMinWidth(100);
		column.setPreferredWidth(100);
		//Set "Play counter" column width
		column = jTableMerge.getColumnModel().getColumn(2);
		column.setMinWidth(50);
		column.setMaxWidth(100);
		column.setPreferredWidth(100);
		//Set Rating column width
		column = jTableMerge.getColumnModel().getColumn(3);
		column.setMinWidth(50);
		column.setMaxWidth(100);
		column.setPreferredWidth(50);
		//Set Added column width
		column = jTableMerge.getColumnModel().getColumn(4);
		column.setMinWidth(150);
		column.setMaxWidth(200);
		column.setPreferredWidth(150);
		//Set "Last Played" column width
		column = jTableMerge.getColumnModel().getColumn(5);
		column.setMinWidth(150);
		column.setMaxWidth(200);
		column.setPreferredWidth(150);
        
        PanelMerge.progressBar = (ProgressBar)jProgressBarMerge;

		setOptions();
	}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMerge = new javax.swing.JPanel();
        jPanelMergeProcess = new javax.swing.JPanel();
        jProgressBarMerge = new jamuz.gui.swing.ProgressBar();
        jButtonMergeStart = new javax.swing.JButton();
        jScrollPaneMergeList = new javax.swing.JScrollPane();
        jListMerge = new jamuz.gui.swing.CheckBoxList();
        jScrollPaneMergeResults = new javax.swing.JScrollPane();
        jTableMerge = new javax.swing.JTable();
        jPanelMergeOptions = new javax.swing.JPanel();
        jCheckBoxMergeForce = new javax.swing.JCheckBox();
        jCheckBoxMergeSimulate = new javax.swing.JCheckBox();
        jButtonMergeSources = new javax.swing.JButton();

        jPanelMergeProcess.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Merge Statistics"));

        jProgressBarMerge.setMinimumSize(new java.awt.Dimension(1, 23));
        jProgressBarMerge.setString(" "); // NOI18N
        jProgressBarMerge.setStringPainted(true);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonMergeStart.setText(bundle.getString("Button.Start")); // NOI18N
        jButtonMergeStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMergeStartActionPerformed(evt);
            }
        });

        jListMerge.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        jListMerge.setVerifyInputWhenFocusTarget(false);
        jListMerge.setVisibleRowCount(-1);
        jScrollPaneMergeList.setViewportView(jListMerge);

        javax.swing.GroupLayout jPanelMergeProcessLayout = new javax.swing.GroupLayout(jPanelMergeProcess);
        jPanelMergeProcess.setLayout(jPanelMergeProcessLayout);
        jPanelMergeProcessLayout.setHorizontalGroup(
            jPanelMergeProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMergeProcessLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanelMergeProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBarMerge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelMergeProcessLayout.createSequentialGroup()
                        .addComponent(jButtonMergeStart, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneMergeList))))
        );
        jPanelMergeProcessLayout.setVerticalGroup(
            jPanelMergeProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMergeProcessLayout.createSequentialGroup()
                .addGroup(jPanelMergeProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonMergeStart, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jScrollPaneMergeList))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarMerge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jTableMerge.setAutoCreateColumnsFromModel(false);
        jTableMerge.setModel(new jamuz.gui.swing.TableModel());
        jScrollPaneMergeResults.setViewportView(jTableMerge);

        jPanelMergeOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("Label.Options"))); // NOI18N

        jCheckBoxMergeForce.setText(Inter.get("Msg.Merge.ForceJamuz")); // NOI18N

        jCheckBoxMergeSimulate.setText(Inter.get("Label.Simulate")); // NOI18N
        jCheckBoxMergeSimulate.setToolTipText(Inter.get("Tooltip.Merge.Simulation")); // NOI18N

        jButtonMergeSources.setText(Inter.get("Label.Options")); // NOI18N
        jButtonMergeSources.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMergeSourcesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMergeOptionsLayout = new javax.swing.GroupLayout(jPanelMergeOptions);
        jPanelMergeOptions.setLayout(jPanelMergeOptionsLayout);
        jPanelMergeOptionsLayout.setHorizontalGroup(
            jPanelMergeOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMergeOptionsLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanelMergeOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxMergeForce)
                    .addComponent(jCheckBoxMergeSimulate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonMergeSources, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelMergeOptionsLayout.setVerticalGroup(
            jPanelMergeOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButtonMergeSources, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMergeOptionsLayout.createSequentialGroup()
                .addComponent(jCheckBoxMergeForce)
                .addGap(0, 0, 0)
                .addComponent(jCheckBoxMergeSimulate))
        );

        javax.swing.GroupLayout jPanelMergeLayout = new javax.swing.GroupLayout(jPanelMerge);
        jPanelMerge.setLayout(jPanelMergeLayout);
        jPanelMergeLayout.setHorizontalGroup(
            jPanelMergeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneMergeResults, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMergeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelMergeProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelMergeOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelMergeLayout.setVerticalGroup(
            jPanelMergeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMergeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMergeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelMergeOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelMergeProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneMergeResults, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMerge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelMerge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
	 * Display merge result
	 * @param myFileInfo
	 */
	public static void displayMergeResult(FileInfo myFileInfo) {
		//columnNames = {"File", "Comment", "Play counter", "Rating", "Added", "Last Played"};
		
		Object[] donnee = new Object[]{myFileInfo.getRelativeFullPath(), 
			myFileInfo.getSourceName(), myFileInfo.getPlayCounter(), myFileInfo.getRating(), 
			myFileInfo.getAddedDateLocalTime(), myFileInfo.getLastPlayedLocalTime()};
        	tableModel.addRow(donnee);
	}
    
    	/**
	 * Enable merge
	 * @param enable
	 */
	public static void enableMerge(boolean enable) {
		//TODO: Though jListMerge is disabled, the included checkboxes are not !!
		enableMergeStartButton(enable);
		jListMerge.setEnabled(enable);
		jCheckBoxMergeSimulate.setEnabled(enable);
		jCheckBoxMergeForce.setEnabled(enable);
        enableRowSorter(enable);
		if(enable) {
			jButtonMergeStart.setText(Inter.get("Button.Start"));  //NOI18N
		}
		else {
            tableModel.clear();
			jButtonMergeStart.setText(Inter.get("Button.Abort"));  //NOI18N
		}
	}
    
    private static void enableRowSorter(boolean enable) {
		if(enable) {
			//Enable row tableSorter (cannot be done if model is empty)
			if(tableModel.getRowCount()>0) {
				//Enable auto sorter
				jTableMerge.setAutoCreateRowSorter(true);
				//Sort by action, result
				TableRowSorter<TableModel> tableSorter = new TableRowSorter<>(tableModel);
				jTableMerge.setRowSorter(tableSorter);
				List <RowSorter.SortKey> sortKeys = new ArrayList<>();
				sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
				sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
				tableSorter.setSortKeys(sortKeys);
			}
			else {
				jTableMerge.setAutoCreateRowSorter(false);
			}
		}
		else {
			jTableMerge.setRowSorter(null);
		}
	}
    
    /**
	 * Enable merge start button
	 * @param enabled
	 */
	public static void enableMergeStartButton(boolean enabled) {
		jButtonMergeStart.setEnabled(enabled);
	}
    
    /**
     * fill merge sources list
     */
    public static void setOptions() {
        //Exit if processMerge is running
        if(processMerge!=null) {
            if(processMerge.isAlive()) {
                return;
            }
        }
        //processMerge is not running, so we can set these up
		if(Jamuz.getMachine().getStatSources().size()>0) {
			DefaultListModel myModel = (DefaultListModel) jListMerge.getModel();
			myModel.clear();
			for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
				CheckBoxListItem checkBoxListItem = new CheckBoxListItem(statSource);
				checkBoxListItem.setSelected(statSource.isIsSelected());
				myModel.addElement(checkBoxListItem);
			}
			enableMerge(true);
		}
		else {
			enableMerge(false);
		}
	}
    
    private void jButtonMergeStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMergeStartActionPerformed

        if (jButtonMergeStart.getText().equals(Inter.get("Button.Abort"))) { 			  //NOI18N
            enableMergeStartButton(false);
            jButtonMergeStart.setText(Inter.get("Button.Aborting")); 			  //NOI18N
            processMerge.abort();
        } else {
            enableMerge(false);
            
            DefaultListModel myModel = (DefaultListModel) jListMerge.getModel();
            List dbIndexes = new ArrayList();

            StatSource statSource;

            for (int i = 0; i < myModel.size(); ++i) {
                CheckBoxListItem checkListItem = (CheckBoxListItem) myModel.get(i);
                statSource = (StatSource) checkListItem.getObject();
                if (checkListItem.isSelected()) {
                    dbIndexes.add(new Integer(statSource.getId()));
                }
            }
            processMerge = new ProcessMerge("Thread.PanelMerge.ProcessMerge", dbIndexes, jCheckBoxMergeSimulate.isSelected(), jCheckBoxMergeForce.isSelected());
            processMerge.start();
        }
    }//GEN-LAST:event_jButtonMergeStartActionPerformed

    private void jButtonMergeSourcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMergeSourcesActionPerformed
        DialogOptionsNew.main(Jamuz.getMachine().getName());
    }//GEN-LAST:event_jButtonMergeSourcesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonMergeSources;
    private static javax.swing.JButton jButtonMergeStart;
    private static javax.swing.JCheckBox jCheckBoxMergeForce;
    private static javax.swing.JCheckBox jCheckBoxMergeSimulate;
    private static javax.swing.JList jListMerge;
    private javax.swing.JPanel jPanelMerge;
    private javax.swing.JPanel jPanelMergeOptions;
    private javax.swing.JPanel jPanelMergeProcess;
    private static javax.swing.JProgressBar jProgressBarMerge;
    private javax.swing.JScrollPane jScrollPaneMergeList;
    private javax.swing.JScrollPane jScrollPaneMergeResults;
    private static javax.swing.JTable jTableMerge;
    // End of variables declaration//GEN-END:variables
}
