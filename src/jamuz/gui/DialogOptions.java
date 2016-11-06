/*
 * Copyright (C) 2012 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz.gui;

import jamuz.process.sync.Device;
import jamuz.Jamuz;
import jamuz.Machine;
import jamuz.Option;
import jamuz.process.merge.StatSource;
import jamuz.gui.swing.TableModel;
import jamuz.utils.Inter;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;

/**
 * JDialog extension to display options and stat sources for given machine
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogOptions extends javax.swing.JDialog {

	private static TableModel tableModelOptions;
	private static Machine selOptions;
	private static Option selOption;
	private static String machineName;
	
	/** Creates new form OptionsMachinesGUI
	 * @param parent 
	 * @param modal
	 * @param myMachineName  
	 */
	public DialogOptions(java.awt.Frame parent, boolean modal, String myMachineName) {
		super(parent, modal);
		initComponents();
        
		machineName=myMachineName;
		this.setTitle(Inter.get("Label.Options") + " - " + machineName);  //NOI18N
        
        jComboBoxDevice.setModel(new DefaultComboBoxModel(Jamuz.getMachine().getDevices().toArray()));
        
		//Get jTableOptions model
        tableModelOptions = (TableModel) jTableOptions.getModel();
		//Set table model
		String[] columnNames = {Inter.get("Label.ID"), Inter.get("Label.Value")};  //NOI18N
		Object[][] data = {
			{"Default", "Default"}  //NOI18N
		};
		tableModelOptions.setModel(columnNames, data);
		//clear the table
        tableModelOptions.clear();
		jTableOptions.setEnabled(true);
		
		//TODO (a faire pour les autres jTable aussi):
		//Utiliser un SelectionListener au lieu du mouseClick (qui marche bien mais oublie les 
		//selections au clavier)
//		tableModelOptions.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
//            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
//                jListAlbumValueChanged(evt);
//            }
//        });
		
		
		//Adding columns from model. Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableOptions.createDefaultColumnsFromModel();
		TableColumn column;
        //Set "ID" column width
        column = jTableOptions.getColumnModel().getColumn(0);
		column.setMinWidth(120);
        column.setPreferredWidth(120);
		//Set "Value" column width
        column = jTableOptions.getColumnModel().getColumn(1);
		column.setMinWidth(300);
        column.setPreferredWidth(1000);
		
		display();
    }
	
    
    
	/**
	 * Displays options and stat sources
	 */
	public static void display() {
		//Get selected machine name options
		selOptions = new Machine(machineName);
		selOptions.read();
		
        jTextFieldDescription.setText(selOptions.getDescription());
        jButtonOptionSaveDescription.setEnabled(true);
        jButtonOptionSaveSource.setEnabled(true);
		//Display options
		jTableOptions.setRowSorter(null);
		tableModelOptions.clear();
		selOptions.getOptions().stream().forEach((myOption) -> {
			addRowOption(myOption);
		});
		//Enable row tableSorter (cannot be done if model is empty)
		if(tableModelOptions.getRowCount()>0) {
			jTableOptions.setAutoCreateRowSorter(true);
		}
		else {
			jTableOptions.setAutoCreateRowSorter(false);
		}
		jTableOptions.setEnabled(true);
		
		//Show stat source list for selected machine
		DefaultListModel listModel=(DefaultListModel) jListStatSources.getModel();
		listModel.clear();
		//TODO: Order by name
		selOptions.getStatSources().stream().forEach((statSource) -> {
			listModel.addElement(statSource);
		});
		//Show device list for selected machine
		DefaultListModel devicesModel=(DefaultListModel) jListDevices.getModel();
		devicesModel.clear();
		selOptions.getDevices().stream().forEach((device) -> {
			devicesModel.addElement(device);
		});
	}
	
	private static void addRowOption(Option myOption) {
		Object[] donnee = new Object[]{myOption.getId(), myOption.getValue()};
		tableModelOptions.addRow(donnee);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelStatSources = new javax.swing.JPanel();
        jScrollPaneStatSouces = new javax.swing.JScrollPane();
        jListStatSources = new javax.swing.JList();
        jButtonStatSouceAdd = new javax.swing.JButton();
        jButtonStatSouceEdit = new javax.swing.JButton();
        jButtonStatSouceDel = new javax.swing.JButton();
        jPanelOptions = new javax.swing.JPanel();
        jScrollPaneOptions = new javax.swing.JScrollPane();
        jTableOptions = new javax.swing.JTable();
        jLabelOptionComment = new javax.swing.JLabel();
        jTextFieldOptionValue = new javax.swing.JTextField();
        jCheckBoxOptionBool = new javax.swing.JCheckBox();
        jButtonOptionSelectFolder = new javax.swing.JButton();
        jButtonOptionSave = new javax.swing.JButton();
        jPanelStatSources1 = new javax.swing.JPanel();
        jScrollPaneStatSouces1 = new javax.swing.JScrollPane();
        jListDevices = new javax.swing.JList();
        jButtonDeviceAdd = new javax.swing.JButton();
        jButtonDeviceEdit = new javax.swing.JButton();
        jButtonDeviceDel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButtonOptionSaveDescription = new javax.swing.JButton();
        jTextFieldDescription = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButtonOptionSaveSource = new javax.swing.JButton();
        jComboBoxDevice = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jPanelStatSources.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("DialogOptions.jPanelStatSources.border.title"))); // NOI18N

        jListStatSources.setModel(new DefaultListModel());
        jScrollPaneStatSouces.setViewportView(jListStatSources);

        jButtonStatSouceAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/add.png"))); // NOI18N
        jButtonStatSouceAdd.setText(bundle.getString("Button.Add")); // NOI18N
        jButtonStatSouceAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStatSouceAddActionPerformed(evt);
            }
        });

        jButtonStatSouceEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_form_edit.png"))); // NOI18N
        jButtonStatSouceEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonStatSouceEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStatSouceEditActionPerformed(evt);
            }
        });

        jButtonStatSouceDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonStatSouceDel.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonStatSouceDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStatSouceDelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelStatSourcesLayout = new javax.swing.GroupLayout(jPanelStatSources);
        jPanelStatSources.setLayout(jPanelStatSourcesLayout);
        jPanelStatSourcesLayout.setHorizontalGroup(
            jPanelStatSourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStatSourcesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneStatSouces, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelStatSourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonStatSouceAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonStatSouceEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonStatSouceDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelStatSourcesLayout.setVerticalGroup(
            jPanelStatSourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStatSourcesLayout.createSequentialGroup()
                .addGroup(jPanelStatSourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelStatSourcesLayout.createSequentialGroup()
                        .addComponent(jButtonStatSouceAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonStatSouceEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonStatSouceDel))
                    .addComponent(jScrollPaneStatSouces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanelOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("Label.Options"))); // NOI18N

        jTableOptions.setAutoCreateColumnsFromModel(false);
        jTableOptions.setModel(new jamuz.gui.swing.TableModel());
        jTableOptions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableOptions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableOptions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableOptionsMouseClicked(evt);
            }
        });
        jScrollPaneOptions.setViewportView(jTableOptions);

        jLabelOptionComment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabelOptionComment.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jTextFieldOptionValue.setEditable(false);

        jCheckBoxOptionBool.setEnabled(false);
        jCheckBoxOptionBool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxOptionBoolActionPerformed(evt);
            }
        });

        jButtonOptionSelectFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonOptionSelectFolder.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonOptionSelectFolder.setEnabled(false);
        jButtonOptionSelectFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSelectFolderActionPerformed(evt);
            }
        });

        jButtonOptionSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        jButtonOptionSave.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonOptionSave.setEnabled(false);
        jButtonOptionSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelOptionsLayout = new javax.swing.GroupLayout(jPanelOptions);
        jPanelOptions.setLayout(jPanelOptionsLayout);
        jPanelOptionsLayout.setHorizontalGroup(
            jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOptionsLayout.createSequentialGroup()
                        .addComponent(jTextFieldOptionValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxOptionBool)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionSelectFolder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionSave))
                    .addComponent(jLabelOptionComment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPaneOptions))
                .addContainerGap())
        );
        jPanelOptionsLayout.setVerticalGroup(
            jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneOptions, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelOptionComment, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldOptionValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonOptionSelectFolder)
                            .addComponent(jButtonOptionSave))
                        .addComponent(jCheckBoxOptionBool)))
                .addContainerGap())
        );

        jPanelStatSources1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("DialogOptions.jPanelStatSources1.border.title"))); // NOI18N

        jListDevices.setModel(new DefaultListModel());
        jScrollPaneStatSouces1.setViewportView(jListDevices);

        jButtonDeviceAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/add.png"))); // NOI18N
        jButtonDeviceAdd.setText(bundle.getString("Button.Add")); // NOI18N
        jButtonDeviceAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeviceAddActionPerformed(evt);
            }
        });

        jButtonDeviceEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_form_edit.png"))); // NOI18N
        jButtonDeviceEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonDeviceEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeviceEditActionPerformed(evt);
            }
        });

        jButtonDeviceDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonDeviceDel.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonDeviceDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeviceDelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelStatSources1Layout = new javax.swing.GroupLayout(jPanelStatSources1);
        jPanelStatSources1.setLayout(jPanelStatSources1Layout);
        jPanelStatSources1Layout.setHorizontalGroup(
            jPanelStatSources1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStatSources1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneStatSouces1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelStatSources1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonDeviceAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonDeviceEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonDeviceDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelStatSources1Layout.setVerticalGroup(
            jPanelStatSources1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStatSources1Layout.createSequentialGroup()
                .addGroup(jPanelStatSources1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelStatSources1Layout.createSequentialGroup()
                        .addComponent(jButtonDeviceAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeviceEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeviceDel))
                    .addComponent(jScrollPaneStatSouces1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(Inter.get("DialogOptions.jPanel1.border.title"))); // NOI18N

        jButtonOptionSaveDescription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        jButtonOptionSaveDescription.setText(bundle.getString("DialogOptions.jButtonOptionSaveDescription.text")); // NOI18N
        jButtonOptionSaveDescription.setEnabled(false);
        jButtonOptionSaveDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSaveDescriptionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextFieldDescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOptionSaveDescription)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonOptionSaveDescription))
                .addGap(0, 0, 0))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(Inter.get("DialogOptions.jPanel2.border.title"))); // NOI18N

        jButtonOptionSaveSource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        jButtonOptionSaveSource.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonOptionSaveSource.setEnabled(false);
        jButtonOptionSaveSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSaveSourceActionPerformed(evt);
            }
        });

        jComboBoxDevice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBoxDevice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOptionSaveSource)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOptionSaveSource)
                    .addComponent(jComboBoxDevice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelStatSources, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelStatSources1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelOptions, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelStatSources1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelStatSources, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jTableOptionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableOptionsMouseClicked
		//Getting selected File
		int selectedRow = jTableOptions.getSelectedRow();
		//convert to model index (as sortable model)
		selectedRow = jTableOptions.convertRowIndexToModel(selectedRow);
		if(selectedRow>=0) {
			selOption = selOptions.getOption(selectedRow);

			jTextFieldOptionValue.setText(selOption.getValue());
			jLabelOptionComment.setText(selOption.getComment());
			//Enable select folder only if option type is "path"
			jButtonOptionSelectFolder.setEnabled(selOption.getType().equals("path"));  //NOI18N
			//Enable check box only if option type is "bool"
			jCheckBoxOptionBool.setEnabled(selOption.getType().equals("bool"));  //NOI18N
			//Set editable option value textbox only if option type is NOT "bool"
			jTextFieldOptionValue.setEditable(!selOption.getType().equals("bool"));  //NOI18N
			if (selOption.getType().equals("bool")) {  //NOI18N
				jCheckBoxOptionBool.setSelected(Boolean.valueOf(selOption.getValue()));
			}
			
			jButtonOptionSave.setEnabled(true);
		}
	}//GEN-LAST:event_jTableOptionsMouseClicked

	private void jButtonOptionSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSaveActionPerformed
		jButtonOptionSave.setEnabled(false);
		if(Jamuz.getDb().setOption(selOption, jTextFieldOptionValue.getText())) {
			display();
			jTextFieldOptionValue.setText("");  //NOI18N
			jTextFieldOptionValue.setEditable(false);
			jCheckBoxOptionBool.setEnabled(false);
		}
	}//GEN-LAST:event_jButtonOptionSaveActionPerformed

	private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
		PanelMain.fillMachineList(); //In case description changed
        if(Jamuz.getMachine().getName().equals(machineName)) { 
			//Re-reading options if we updated current machine's ones
			PanelMain.readOptions();
		}
	}//GEN-LAST:event_formWindowClosed

	private void jButtonStatSouceEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStatSouceEditActionPerformed
		if(jListStatSources.getSelectedIndex()>-1) {
			StatSource statSource = (StatSource) jListStatSources.getSelectedValue();
			DialogStatSource.main(statSource);
		}
	}//GEN-LAST:event_jButtonStatSouceEditActionPerformed

	private void jButtonStatSouceAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStatSouceAddActionPerformed
		DialogStatSource.main(new StatSource(machineName));
	}//GEN-LAST:event_jButtonStatSouceAddActionPerformed

	private void jButtonStatSouceDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStatSouceDelActionPerformed
		if(jListStatSources.getSelectedIndex()>-1) {
			int n = JOptionPane.showConfirmDialog(
								this, Inter.get("Question.DeleteStatSource"),  //NOI18N
								Inter.get("Label.Confirm"),  //NOI18N
								JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				StatSource statSource = (StatSource) jListStatSources.getSelectedValue();
				Jamuz.getDb().deleteStatSource(statSource.getId());
				display();
			} 
		}
	}//GEN-LAST:event_jButtonStatSouceDelActionPerformed

	private void jButtonOptionSelectFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSelectFolderActionPerformed
		String selectedFolder=selectFolder(jTextFieldOptionValue.getText());
		if(!selectedFolder.equals("")) {  //NOI18N
			jTextFieldOptionValue.setText(selectedFolder); 
		}
	}//GEN-LAST:event_jButtonOptionSelectFolderActionPerformed

    private void jCheckBoxOptionBoolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxOptionBoolActionPerformed
        //TODO: Does the hint to do the following instead actually does the same
		//Or was there a trick for doing so ? Or is Boolean.toString simply new ?
		//Boolean.toString(jCheckBoxOptionBool.isSelected());
		String s = new Boolean(jCheckBoxOptionBool.isSelected()).toString();
		jTextFieldOptionValue.setText(s);
    }//GEN-LAST:event_jCheckBoxOptionBoolActionPerformed

    private void jButtonDeviceAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeviceAddActionPerformed
        DialogOptionDevice.main(new Device(machineName));
    }//GEN-LAST:event_jButtonDeviceAddActionPerformed

    private void jButtonDeviceEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeviceEditActionPerformed
        if(jListDevices.getSelectedIndex()>-1) {
			Device device = (Device) jListDevices.getSelectedValue();
			DialogOptionDevice.main(device);
		}
    }//GEN-LAST:event_jButtonDeviceEditActionPerformed

    private void jButtonDeviceDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeviceDelActionPerformed
        if(jListDevices.getSelectedIndex()>-1) {
			int n = JOptionPane.showConfirmDialog(
								this, Inter.get("Question.DeleteDevice"),  //NOI18N
								Inter.get("Label.Confirm"),  //NOI18N
								JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				Device device = (Device) jListDevices.getSelectedValue();
				Jamuz.getDb().deleteDevice(device.getId());
				display();
			} 
		}
    }//GEN-LAST:event_jButtonDeviceDelActionPerformed

    private void jButtonOptionSaveDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSaveDescriptionActionPerformed
        jButtonOptionSaveDescription.setEnabled(false);
		if(Jamuz.getDb().updateMachine(selOptions.getOption(0).getIdMachine(), jTextFieldDescription.getText())) {
			display();
            jButtonOptionSaveDescription.setEnabled(true);
		}
    }//GEN-LAST:event_jButtonOptionSaveDescriptionActionPerformed

    private void jButtonOptionSaveSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSaveSourceActionPerformed
        if(jComboBoxDevice.getSelectedIndex()>0) {
            Device device = (Device) jComboBoxDevice.getSelectedItem();
            //TODO: Set (and use!) this device into machine options somehow 
            //and use it as media library source
            // + disable button while saving as for "Description"
            //jButtonOptionSaveSource.setEnabled(false);
        }
        else if(jComboBoxDevice.getSelectedIndex()>-1) {
            //TODO: Insert a "Whole library" item (default value, current way) on top of combobox
        }
        
    }//GEN-LAST:event_jButtonOptionSaveSourceActionPerformed

	//TODO: Move to a generic class
	/**
	 * Select a folder (open a folder chooser GUI)
	 * @param defaultFolder 
	 * @return
	 */
	public static String selectFolder(String defaultFolder) {
		JFileChooser fc = new JFileChooser(defaultFolder);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selFile = fc.getSelectedFile();
			return selFile.getAbsolutePath();
        } else {
			return "";  //NOI18N
        }
	}
	
	/**
	 * @param machineName 
	 */
	public static void main(final String machineName) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {  //NOI18N
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			Jamuz.getLogger().severe(ex.toString());
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> {
			DialogOptions dialog = new DialogOptions(new javax.swing.JFrame(), true, machineName);
			//Center the dialog
			dialog.setLocationRelativeTo(dialog.getParent());
			dialog.setVisible(true);
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDeviceAdd;
    private javax.swing.JButton jButtonDeviceDel;
    private javax.swing.JButton jButtonDeviceEdit;
    private javax.swing.JButton jButtonOptionSave;
    private static javax.swing.JButton jButtonOptionSaveDescription;
    private static javax.swing.JButton jButtonOptionSaveSource;
    private javax.swing.JButton jButtonOptionSelectFolder;
    private javax.swing.JButton jButtonStatSouceAdd;
    private javax.swing.JButton jButtonStatSouceDel;
    private javax.swing.JButton jButtonStatSouceEdit;
    private javax.swing.JCheckBox jCheckBoxOptionBool;
    private static javax.swing.JComboBox jComboBoxDevice;
    private javax.swing.JLabel jLabelOptionComment;
    private static javax.swing.JList jListDevices;
    private static javax.swing.JList jListStatSources;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelOptions;
    private javax.swing.JPanel jPanelStatSources;
    private javax.swing.JPanel jPanelStatSources1;
    private javax.swing.JScrollPane jScrollPaneOptions;
    private javax.swing.JScrollPane jScrollPaneStatSouces;
    private javax.swing.JScrollPane jScrollPaneStatSouces1;
    private static javax.swing.JTable jTableOptions;
    private static javax.swing.JTextField jTextFieldDescription;
    private static javax.swing.JTextField jTextFieldOptionValue;
    // End of variables declaration//GEN-END:variables
}
