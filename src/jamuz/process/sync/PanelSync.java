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

package jamuz.process.sync;

import jamuz.Jamuz;
import jamuz.gui.DialogOptions;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.TableColumn;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableModel;
import jamuz.utils.Inter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelSync extends javax.swing.JPanel {

    private static ProcessSync processSync;
    private static TableModel tableModel;

    /**
     * progress bar
     */
    protected static ProgressBar progressBar;
    
    /**
     * Creates new form PanelSync
     */
    public PanelSync() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelSync = new javax.swing.JPanel();
        jScrollPaneCheckTags3 = new javax.swing.JScrollPane();
        jTableSync = new jamuz.gui.swing.TableHorizontal();
        jPanelSyncProcess = new javax.swing.JPanel();
        jButtonSyncStart = new javax.swing.JButton();
        jProgressBarSync = new jamuz.gui.swing.ProgressBar();
        jComboBoxDevice = new javax.swing.JComboBox();
        jButtonMergeSources = new javax.swing.JButton();

        jTableSync.setAutoCreateColumnsFromModel(false);
        jTableSync.setModel(new jamuz.gui.swing.TableModel());
        jTableSync.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPaneCheckTags3.setViewportView(jTableSync);

        jPanelSyncProcess.setBorder(javax.swing.BorderFactory.createTitledBorder("Export files"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonSyncStart.setText(bundle.getString("Button.Start")); // NOI18N
        jButtonSyncStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSyncStartActionPerformed(evt);
            }
        });

        jProgressBarSync.setString(""); // NOI18N
        jProgressBarSync.setStringPainted(true);

        jComboBoxDevice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButtonMergeSources.setText(Inter.get("Label.Options")); // NOI18N
        jButtonMergeSources.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMergeSourcesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSyncProcessLayout = new javax.swing.GroupLayout(jPanelSyncProcess);
        jPanelSyncProcess.setLayout(jPanelSyncProcessLayout);
        jPanelSyncProcessLayout.setHorizontalGroup(
            jPanelSyncProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSyncProcessLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jButtonSyncStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxDevice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarSync, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonMergeSources))
        );
        jPanelSyncProcessLayout.setVerticalGroup(
            jPanelSyncProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSyncProcessLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanelSyncProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSyncStart)
                    .addComponent(jComboBoxDevice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jProgressBarSync, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonMergeSources, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanelSyncLayout = new javax.swing.GroupLayout(jPanelSync);
        jPanelSync.setLayout(jPanelSyncLayout);
        jPanelSyncLayout.setHorizontalGroup(
            jPanelSyncLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSyncLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelSyncProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
        );
        jPanelSyncLayout.setVerticalGroup(
            jPanelSyncLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSyncLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelSyncProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 503, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelSync, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 246, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanelSync, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * extended init
     */
    public void initExtended() {

        icons.add(new ImageIcon(getClass().getResource("/jamuz/ressources/delete.png")));
        icons.add(new ImageIcon(getClass().getResource("/jamuz/ressources/add.png")));
        icons.add(new ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png")));
        
		//Add a listener to scroll to the end whenever a row is added to the table
		jTableSync.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				jTableSync.scrollRectToVisible(jTableSync.getCellRect(jTableSync.getRowCount()-1, 0, true));
			}
		});
		
		//Get jTableSync model
		tableModel = (TableModel) jTableSync.getModel();
		//Set the model
		
		//TODO: Add more columns: playCounter, lastPlayed, rating ...
		
		String[] columnNames =  { "", Inter.get("Label.File")};  //NOI18N

		Object[][] data = {
			{"Default", "Default"}  //NOI18N
		};
		tableModel.setModel(columnNames, data);

		//Clear the table
		tableModel.clear();
		//Adding columns from model. Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableSync.createDefaultColumnsFromModel();
		
		TableColumn column;
		
        //Set "Cover" column width
        column = jTableSync.getColumnModel().getColumn(0);
        column.setMinWidth(icons.get(0).getIconWidth()+5);
        column.setMaxWidth(icons.get(0).getIconWidth()+5);

		PanelSync.progressBar = (ProgressBar)jProgressBarSync;
		
		setCombo();
	}
    
    /**
     * set device combo box model
     */
    public static void setOptions() {
        //Exit if processCheck is running
        if(processSync!=null) {
            if(processSync.isAlive()) {
                return;
            }
        }
        //processSync is not running, so we can set these up
        setCombo();
    }
	
	private static void setCombo() {
		jComboBoxDevice.setModel(new DefaultComboBoxModel(Jamuz.getMachine().getDevices().toArray()));
	}
    
    /**
	 * Add a row to the sync table
	 * @param file
     * @param idIcon
	 */
	public static void addRowSync(String file, int idIcon) {
		Object[] donnee = new Object[]{ icons.get(idIcon), file };
		tableModel.addRow(donnee);
	}
    
    /**
     *
     * @param file
     * @param msg
     */
    public static void addRowSync(String file, String msg) {
		Object[] donnee = new Object[]{ icons.get(2), msg + " : " + file };
		tableModel.addRow(donnee);
	}
    
    private static final List<ImageIcon> icons=new ArrayList<>();
    
    	/**
	 * Enable sync start button
	 * @param enabled
	 */
	public static void enableSyncStartButton(boolean enabled) {
		jButtonSyncStart.setEnabled(enabled);
	}
    
    	/**
	 * Enable sync
	 * @param enable
	 */
	public static void enableSync(boolean enable) {
		enableSyncStartButton(enable);
		jComboBoxDevice.setEnabled(enable);
	
		jTableSync.setEnabled(enable);
		if(enable) {
            setOptions(); //In case options have changed during sync process
			jButtonSyncStart.setText(Inter.get("Button.Start"));  //NOI18N
		}
		else {
			jButtonSyncStart.setText(Inter.get("Button.Abort"));  //NOI18N
		}
	}
    
    private void jButtonSyncStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSyncStartActionPerformed

        if (jButtonSyncStart.getText().equals(Inter.get("Button.Abort"))) { 			  //NOI18N
            enableSyncStartButton(false);
            jButtonSyncStart.setText(Inter.get("Button.Aborting")); 			  //NOI18N
            processSync.abort();
        } else {
            if(jComboBoxDevice.getSelectedIndex()>-1) {
                enableSync(false);
                tableModel.clear();
                Device device = (Device) jComboBoxDevice.getSelectedItem();
                processSync = new ProcessSync("Thread.PanelSync.ProcessSync", device);
                processSync.start();
            }
        }
    }//GEN-LAST:event_jButtonSyncStartActionPerformed

    private void jButtonMergeSourcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMergeSourcesActionPerformed
        DialogOptions.main(Jamuz.getMachine().getName());
    }//GEN-LAST:event_jButtonMergeSourcesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonMergeSources;
    private static javax.swing.JButton jButtonSyncStart;
    private static javax.swing.JComboBox jComboBoxDevice;
    private javax.swing.JPanel jPanelSync;
    private javax.swing.JPanel jPanelSyncProcess;
    private static javax.swing.JProgressBar jProgressBarSync;
    private static javax.swing.JScrollPane jScrollPaneCheckTags3;
    private static javax.swing.JTable jTableSync;
    // End of variables declaration//GEN-END:variables
}