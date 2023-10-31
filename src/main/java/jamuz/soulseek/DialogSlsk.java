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

import jamuz.gui.swing.ProgressCellRender;
import jamuz.gui.swing.TableColumnModel;
import jamuz.utils.Popup;
import java.awt.Dialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private final PanelSlsk panelSlsk;
	
    /**
     * Creates new form DialogSoulseek
     * @param panelSlsk
     * @param parent
     * @param modal
	 * @param query
     */
    public DialogSlsk(PanelSlsk panelSlsk, Dialog parent, boolean modal, String query) {
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
        TableColumn column = 
        setColumn(columnModelResults, 9, 80);     // Completed
        column.setCellRenderer(new ProgressCellRender());
        columnModelResults.setColumnVisible(column, false);
        
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
        columnModelDownload.setColumnVisible(column, false);
        
		try {
            soulseek = new Slsk();
            startSoulseekSearch();
        } catch (IOException | SlskdClient.ServerException ex) {
            Popup.error(ex);
        }
        this.panelSlsk = panelSlsk;
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
        jButtonAddToDownloads = new javax.swing.JButton();
        jButtonSearch = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDownload = new jamuz.gui.swing.TableHorizontal();
        jLabelInfo = new javax.swing.JLabel();

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

        jButtonAddToDownloads.setText("Add to downloads");
        jButtonAddToDownloads.setEnabled(false);
        jButtonAddToDownloads.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddToDownloadsActionPerformed(evt);
            }
        });

        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_plus.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("inter/Bundle"); // NOI18N
        jButtonSearch.setText(bundle.getString("Button.Search")); // NOI18N
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTextFieldQuery)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSearch))
                    .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.DEFAULT_SIZE, 1220, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAddToDownloads))
                    .addComponent(jLabelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jLabelInfo)
                .addGap(2, 2, 2)
                .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAddToDownloads)
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
	
    private void jButtonAddToDownloadsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddToDownloadsActionPerformed
        new Thread() {
			@Override
			public void run() {
				int selectedRow = jTableResults.getSelectedRow(); 			
                if(selectedRow>=0) { 
                    jButtonAddToDownloads.setEnabled(false);
                    selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
                    SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);
                    panelSlsk.addDownload(searchResponse);
                    //FIXME ! Re-enable the "Add to downloads" button, but only if not yet added 
                    // => need to store that info, and display it on jTableResults (warning: same model as in Panel so will need to add and hide that column there
                 }
			}
		}.start();
    }//GEN-LAST:event_jButtonAddToDownloadsActionPerformed

	private void displaySearchFiles() {
		int selectedRow = jTableResults.getSelectedRow(); 			
		if(selectedRow>=0) { 
			selectedRow = jTableResults.convertRowIndexToModel(selectedRow); 
			SlskdSearchResponse searchResponse = tableModelResults.getRow(selectedRow);
			tableModelDownload.clear();
			for (SlskdSearchFile file : searchResponse.getFiles()) {
				tableModelDownload.addRow(file);
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
                    jButtonAddToDownloads.setEnabled(false);
                    tableModelResults.clear();
                    jLabelInfo.setText("Searching...");

                    List<SlskdSearchResponse> searchResponses = soulseek.search(jTextFieldQuery.getText(), jLabelInfo);
                    if(searchResponses!=null) {
                        if(!searchResponses.isEmpty()) {
                            for (SlskdSearchResponse slskdSearchResponse : searchResponses) {
                                tableModelResults.addRow(slskdSearchResponse);
                            }
                            enableRowSorter(true);
                            displaySearchFiles();
                            jButtonAddToDownloads.setEnabled(true);
                        } else {
                            jLabelInfo.setText("No results!");
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
     * @param panelSlsk
	 * @param parent
	 * @param query
	 * @throws java.io.IOException
	 * @throws jamuz.soulseek.SlskdClient.ServerException
     */
    public static void main(PanelSlsk panelSlsk, Dialog parent, String query) throws IOException, SlskdClient.ServerException {
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

        DialogSlsk dialog = new DialogSlsk(panelSlsk, parent, false, query);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddToDownloads;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JLabel jLabelInfo;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneCheckTags3;
    private javax.swing.JTable jTableDownload;
    private javax.swing.JTable jTableResults;
    private javax.swing.JTextField jTextFieldQuery;
    // End of variables declaration//GEN-END:variables
}
