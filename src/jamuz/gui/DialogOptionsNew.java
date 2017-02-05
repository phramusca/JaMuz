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
package jamuz.gui;

import jamuz.Jamuz;
import jamuz.Machine;
import jamuz.Option;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.utils.Inter;
import jamuz.utils.Swing;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogOptionsNew extends javax.swing.JDialog {

	/**
	 * A return status code - returned if Cancel button has been pressed
	 */
	public static final int RET_CANCEL = 0;
	/**
	 * A return status code - returned if OK button has been pressed
	 */
	public static final int RET_OK = 1;

	private static String machineName;
	private static Machine selOptions;
	
	/**
	 * Creates new form NewOkCancelDialog
	 * @param parent
	 * @param modal
	 * @param myMachineName
	 */
	public DialogOptionsNew(java.awt.Frame parent, boolean modal, String myMachineName) {
		super(parent, modal);
		initComponents();
		machineName=myMachineName;

		// Close the dialog when Esc is pressed
		String cancelName = "cancel";
		InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
		ActionMap actionMap = getRootPane().getActionMap();
		actionMap.put(cancelName, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doClose(RET_CANCEL);
			}
		});
		
		displayOptions();
	}

	/**
	 * Displays options and stat sources
	 */
	public static void displayOptions() {
		//Get selected machine name options
		selOptions = new Machine(machineName);
		selOptions.read();
		
        jTextFieldDescription.setText(selOptions.getDescription());
		jLabelDescription.setText("<html><u><i>Description</i></u> : "
				+MessageFormat.format(Inter.get("Options.Comment.Description"), 
						machineName)+"</html>");
        jButtonOptionSaveDescription.setEnabled(true);
        jButtonOptionSaveSource.setEnabled(true);

		Option option = selOptions.getOption("library.isMaster");
		setOption("location.library", jTextFieldOptionLocationLibrary, jLabelOptionLocationLibrary);
		jCheckBoxOptionLibraryIsMaster.setSelected(option.getValue().equals("true"));
		jCheckBoxOptionLibraryIsMaster.setText(option.getComment());
		
		setOption("location.add", jTextFieldOptionLocationAdd, jLabelOptionLocationAdd);
		setOption("location.ko", jTextFieldOptionLocationKO, jLabelOptionLocationKO);
		setOption("location.manual", jTextFieldOptionLocationManual, jLabelOptionLocationManual);
		setOption("location.ok", jTextFieldOptionLocationOK, jLabelOptionLocationOK);
		setOption("location.mask", jTextFieldOptionMask, jLabelOptionMask);
		setOption("files.audio", jTextFieldOptionsFilesAudio, jLabelOptionsFilesAudio);
		setOption("files.convert", jTextFieldOptionsFilesConvert, jLabelOptionsFilesConvert);
		setOption("files.delete", jTextFieldOptionsFilesDelete, jLabelOptionsFilesDelete);
		setOption("files.image", jTextFieldOptionsFilesImage, jLabelOptionsFilesImage);
		setOption("log.count", jTextFieldOptionsLogCount, jLabelOptionsLogCount);
		setOption("log.level", jTextFieldOptionsLogLevel, jLabelOptionsLogLevel);
		setOption("log.limit", jTextFieldOptionsLogLimit, jLabelOptionsLogLimit);
		setOption("network.proxy", jTextFieldOptionsProxy, jLabelOptionsProxy);
		
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
	
	private static void setOption(String id, JTextField textField, JLabel jLabel) {
		Option option = selOptions.getOption(id);
		textField.setText(option.getValue());
		jLabel.setText(option.getComment());
	}
	
	/**
	 * @return the return status of this dialog - one of RET_OK or RET_CANCEL
	 */
	public int getReturnStatus() {
		return returnStatus;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jTextFieldDescription = new javax.swing.JTextField();
        jLabelDescription = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jButtonOptionSaveSource = new javax.swing.JButton();
        jComboBoxDevice = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jTextFieldOptionLocationLibrary = new javax.swing.JTextField();
        jButtonOptionSelectFolder = new javax.swing.JButton();
        jLabelOptionLocationLibrary = new javax.swing.JLabel();
        jCheckBoxOptionLibraryIsMaster = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jTextFieldOptionLocationAdd = new javax.swing.JTextField();
        jButtonOptionSelectFolder1 = new javax.swing.JButton();
        jLabelOptionLocationAdd = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jTextFieldOptionLocationManual = new javax.swing.JTextField();
        jButtonOptionSelectFolder2 = new javax.swing.JButton();
        jLabelOptionLocationManual = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jTextFieldOptionLocationOK = new javax.swing.JTextField();
        jButtonOptionSelectFolder3 = new javax.swing.JButton();
        jLabelOptionLocationOK = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jTextFieldOptionLocationKO = new javax.swing.JTextField();
        jButtonOptionSelectFolder6 = new javax.swing.JButton();
        jLabelOptionLocationKO = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jPanelStatSources = new javax.swing.JPanel();
        jScrollPaneStatSouces = new javax.swing.JScrollPane();
        jListStatSources = new javax.swing.JList();
        jButtonStatSouceAdd = new javax.swing.JButton();
        jButtonStatSouceEdit = new javax.swing.JButton();
        jButtonStatSouceDel = new javax.swing.JButton();
        jPanelStatSources1 = new javax.swing.JPanel();
        jScrollPaneStatSouces1 = new javax.swing.JScrollPane();
        jListDevices = new javax.swing.JList();
        jButtonDeviceAdd = new javax.swing.JButton();
        jButtonDeviceEdit = new javax.swing.JButton();
        jButtonDeviceDel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jTextFieldOptionMask = new javax.swing.JTextField();
        jLabelOptionMask = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jTextFieldOptionsFilesAudio = new javax.swing.JTextField();
        jLabelOptionsFilesAudio = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jTextFieldOptionsFilesImage = new javax.swing.JTextField();
        jLabelOptionsFilesImage = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jTextFieldOptionsFilesConvert = new javax.swing.JTextField();
        jLabelOptionsFilesConvert = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jTextFieldOptionsFilesDelete = new javax.swing.JTextField();
        jLabelOptionsFilesDelete = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jTextFieldOptionsLogLimit = new javax.swing.JTextField();
        jLabelOptionsLogLimit = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jTextFieldOptionsLogLevel = new javax.swing.JTextField();
        jLabelOptionsLogLevel = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jTextFieldOptionsLogCount = new javax.swing.JTextField();
        jLabelOptionsLogCount = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jTextFieldOptionsProxy = new javax.swing.JTextField();
        jLabelOptionsProxy = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jButtonOptionSaveDescription = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelDescription.setText("{0} is not very easy machine name to remember. Type a meaningfull name (ex: \"My PC at work\", My laptop\"):");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldDescription)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabelDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 66, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelDescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setEnabled(false);

        jButtonOptionSaveSource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonOptionSaveSource.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonOptionSaveSource.setEnabled(false);
        jButtonOptionSaveSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSaveSourceActionPerformed(evt);
            }
        });

        jComboBoxDevice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBoxDevice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOptionSaveSource)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOptionSaveSource)
                    .addComponent(jComboBoxDevice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldOptionLocationLibrary.setText("jTextField1");

        jButtonOptionSelectFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonOptionSelectFolder.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonOptionSelectFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSelectFolderActionPerformed(evt);
            }
        });

        jLabelOptionLocationLibrary.setText(Inter.get("Options.Comment.location.library")); // NOI18N

        jCheckBoxOptionLibraryIsMaster.setText("Master library ?");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxOptionLibraryIsMaster, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextFieldOptionLocationLibrary)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionSelectFolder))
                    .addComponent(jLabelOptionLocationLibrary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelOptionLocationLibrary)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOptionSelectFolder)
                    .addComponent(jTextFieldOptionLocationLibrary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxOptionLibraryIsMaster)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(140, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Machine and Library", jPanel4);

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldOptionLocationAdd.setText("jTextField1");

        jButtonOptionSelectFolder1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonOptionSelectFolder1.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonOptionSelectFolder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSelectFolder1ActionPerformed(evt);
            }
        });

        jLabelOptionLocationAdd.setText(Inter.get("Options.Comment.location.add")); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jTextFieldOptionLocationAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionSelectFolder1))
                    .addComponent(jLabelOptionLocationAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionLocationAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOptionSelectFolder1)
                    .addComponent(jTextFieldOptionLocationAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldOptionLocationManual.setText("jTextField1");

        jButtonOptionSelectFolder2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonOptionSelectFolder2.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonOptionSelectFolder2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSelectFolder2ActionPerformed(evt);
            }
        });

        jLabelOptionLocationManual.setText(Inter.get("Options.Comment.location.manual")); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jTextFieldOptionLocationManual)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionSelectFolder2))
                    .addComponent(jLabelOptionLocationManual, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionLocationManual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOptionSelectFolder2)
                    .addComponent(jTextFieldOptionLocationManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldOptionLocationOK.setText("jTextField1");

        jButtonOptionSelectFolder3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonOptionSelectFolder3.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonOptionSelectFolder3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSelectFolder3ActionPerformed(evt);
            }
        });

        jLabelOptionLocationOK.setText("Location OK");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jTextFieldOptionLocationOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionSelectFolder3))
                    .addComponent(jLabelOptionLocationOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionLocationOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOptionSelectFolder3)
                    .addComponent(jTextFieldOptionLocationOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldOptionLocationKO.setText("jTextField1");

        jButtonOptionSelectFolder6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder_explore.png"))); // NOI18N
        jButtonOptionSelectFolder6.setText(bundle.getString("Button.Select")); // NOI18N
        jButtonOptionSelectFolder6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSelectFolder6ActionPerformed(evt);
            }
        });

        jLabelOptionLocationKO.setText(Inter.get("Options.Comment.location.ko")); // NOI18N

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addComponent(jTextFieldOptionLocationKO)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionSelectFolder6))
                    .addComponent(jLabelOptionLocationKO, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionLocationKO)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOptionSelectFolder6)
                    .addComponent(jTextFieldOptionLocationKO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Locations Check", jPanel1);

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
                .addComponent(jScrollPaneStatSouces, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
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
                        .addComponent(jButtonStatSouceDel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPaneStatSouces))
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
                .addComponent(jScrollPaneStatSouces1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
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
                        .addComponent(jButtonDeviceDel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPaneStatSouces1))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelStatSources, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanelStatSources1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelStatSources1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelStatSources, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Source & Devices", jPanel22);

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelOptionMask.setText("jLabelOptionMask");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOptionMask, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jTextFieldOptionMask)
                        .addContainerGap())))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelOptionMask)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionMask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelOptionsFilesAudio.setText(Inter.get("Options.Comment.files.audio")); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOptionsFilesAudio, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .addComponent(jTextFieldOptionsFilesAudio))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionsFilesAudio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionsFilesAudio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelOptionsFilesImage.setText(Inter.get("Options.Comment.files.image")); // NOI18N

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOptionsFilesImage, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .addComponent(jTextFieldOptionsFilesImage))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionsFilesImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionsFilesImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelOptionsFilesConvert.setText("Files convert");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOptionsFilesConvert, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .addComponent(jTextFieldOptionsFilesConvert))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionsFilesConvert)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionsFilesConvert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelOptionsFilesDelete.setText(Inter.get("Options.Comment.files.delete")); // NOI18N

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOptionsFilesDelete, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .addComponent(jTextFieldOptionsFilesDelete))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionsFilesDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionsFilesDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Check options", jPanel2);

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldOptionsLogLimit.setText("jTextField1");

        jLabelOptionsLogLimit.setText("Log size");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOptionsLogLimit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldOptionsLogLimit))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionsLogLimit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionsLogLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldOptionsLogLevel.setText("jTextField1");

        jLabelOptionsLogLevel.setText("Log level");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOptionsLogLevel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldOptionsLogLevel))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionsLogLevel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionsLogLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldOptionsLogCount.setText("jTextField1");

        jLabelOptionsLogCount.setText(Inter.get("Options.Comment.log.count")); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOptionsLogCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldOptionsLogCount))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionsLogCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionsLogCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldOptionsProxy.setText("jTextField1");

        jLabelOptionsProxy.setText(Inter.get("Options.Comment.network.proxy")); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOptionsProxy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldOptionsProxy))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOptionsProxy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionsProxy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(119, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Proxy & Log", jPanel11);

        jButton1.setText("Create shortcut");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setText(" Bla bla bla : ADD EXPLAINATIONS");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(286, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Installation", jPanel21);

        jButtonOptionSaveDescription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        jButtonOptionSaveDescription.setText(bundle.getString("DialogOptions.jButtonOptionSaveDescription.text")); // NOI18N
        jButtonOptionSaveDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSaveDescriptionActionPerformed(evt);
            }
        });

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        jButtonCancel.setText(Inter.get("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOptionSaveDescription)
                .addContainerGap())
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOptionSaveDescription)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * Closes the dialog
	 */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
		doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jButtonOptionSelectFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSelectFolderActionPerformed
        selectFolder(jTextFieldOptionLocationLibrary);
    }//GEN-LAST:event_jButtonOptionSelectFolderActionPerformed

	private void selectFolder(JTextField textField) {
		String selectedFolder=Swing.selectFolder(textField.getText());
        if(!selectedFolder.equals("")) {  //NOI18N
            textField.setText(selectedFolder);
        }
	}
	
    private void jButtonOptionSaveDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSaveDescriptionActionPerformed
        
		selOptions.getOption("library.isMaster").setValue(jCheckBoxOptionLibraryIsMaster.isSelected()?"true":"false");
		selOptions.getOption("location.library").setValue(jTextFieldOptionLocationLibrary.getText());
		selOptions.getOption("location.add").setValue(jTextFieldOptionLocationAdd.getText());
		selOptions.getOption("location.ko").setValue(jTextFieldOptionLocationKO.getText());
		selOptions.getOption("location.manual").setValue(jTextFieldOptionLocationManual.getText());
		selOptions.getOption("location.ok").setValue(jTextFieldOptionLocationOK.getText());
		selOptions.getOption("location.mask").setValue(jTextFieldOptionMask.getText());
		selOptions.getOption("files.audio").setValue(jTextFieldOptionsFilesAudio.getText());
		selOptions.getOption("files.convert").setValue(jTextFieldOptionsFilesConvert.getText());
		selOptions.getOption("files.delete").setValue(jTextFieldOptionsFilesDelete.getText());
		selOptions.getOption("files.image").setValue(jTextFieldOptionsFilesImage.getText());
		selOptions.getOption("log.count").setValue(jTextFieldOptionsLogCount.getText());
		selOptions.getOption("log.level").setValue(jTextFieldOptionsLogLevel.getText());
		selOptions.getOption("log.limit").setValue(jTextFieldOptionsLogLimit.getText());
		selOptions.getOption("network.proxy").setValue(jTextFieldOptionsProxy.getText());
		
		Jamuz.getDb().setOptions(selOptions);
		Jamuz.getDb().updateMachine(selOptions.getOption(0).getIdMachine(), jTextFieldDescription.getText());
		
		doClose(RET_OK);
    }//GEN-LAST:event_jButtonOptionSaveDescriptionActionPerformed

    private void jButtonOptionSaveSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSaveSourceActionPerformed
        if(jComboBoxDevice.getSelectedIndex()>0) {
            Device device = (Device) jComboBoxDevice.getSelectedItem();
            //FIXME: OPTIONS NEW: Set (and use!) this device into machine options somehow
            //and use it as media library source
            // + disable button while saving as for "Description"
            //jButtonOptionSaveSource.setEnabled(false);
        }
        else if(jComboBoxDevice.getSelectedIndex()>-1) {
            //FIXME: OPTIONS NEW: Insert a "Whole library" item (default value, current way) on top of combobox
        }

    }//GEN-LAST:event_jButtonOptionSaveSourceActionPerformed

    private void jButtonOptionSelectFolder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSelectFolder1ActionPerformed
        selectFolder(jTextFieldOptionLocationAdd);
    }//GEN-LAST:event_jButtonOptionSelectFolder1ActionPerformed

    private void jButtonOptionSelectFolder2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSelectFolder2ActionPerformed
        selectFolder(jTextFieldOptionLocationManual);
    }//GEN-LAST:event_jButtonOptionSelectFolder2ActionPerformed

    private void jButtonOptionSelectFolder3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSelectFolder3ActionPerformed
		selectFolder(jTextFieldOptionLocationOK);
    }//GEN-LAST:event_jButtonOptionSelectFolder3ActionPerformed

    private void jButtonOptionSelectFolder6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSelectFolder6ActionPerformed
        selectFolder(jTextFieldOptionLocationKO);
    }//GEN-LAST:event_jButtonOptionSelectFolder6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
		//For Windows:
		//https://github.com/BlackOverlord666/mslinks
		//OR https://github.com/jimmc/jshortcut
		//For linux: ? Simply create a .desktop file ? Is that good for all ditributions ?
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonStatSouceAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStatSouceAddActionPerformed
        DialogStatSource.main(new StatSource(machineName));
    }//GEN-LAST:event_jButtonStatSouceAddActionPerformed

    private void jButtonStatSouceEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStatSouceEditActionPerformed
        if(jListStatSources.getSelectedIndex()>-1) {
            StatSource statSource = (StatSource) jListStatSources.getSelectedValue();
            DialogStatSource.main(statSource);
        }
    }//GEN-LAST:event_jButtonStatSouceEditActionPerformed

    private void jButtonStatSouceDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStatSouceDelActionPerformed
        if(jListStatSources.getSelectedIndex()>-1) {
            int n = JOptionPane.showConfirmDialog(
                this, Inter.get("Question.DeleteStatSource"),  //NOI18N
                Inter.get("Label.Confirm"),  //NOI18N
                JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                StatSource statSource = (StatSource) jListStatSources.getSelectedValue();
                Jamuz.getDb().deleteStatSource(statSource.getId());
                displayOptions();
            }
        }
    }//GEN-LAST:event_jButtonStatSouceDelActionPerformed

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
                displayOptions();
            }
        }
    }//GEN-LAST:event_jButtonDeviceDelActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_jButtonCancelActionPerformed
	
	private void doClose(int retStatus) {
		returnStatus = retStatus;
		setVisible(false);
		dispose();
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
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(DialogOptionsNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(DialogOptionsNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(DialogOptionsNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(DialogOptionsNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(() -> {
			DialogOptionsNew dialog = new DialogOptionsNew(new javax.swing.JFrame(), true, machineName);
			dialog.setTitle(Inter.get("Label.Options") + " - " + machineName);  //NOI18N
			//Center the dialog
			dialog.setLocationRelativeTo(dialog.getParent());
			dialog.setVisible(true);
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDeviceAdd;
    private javax.swing.JButton jButtonDeviceDel;
    private javax.swing.JButton jButtonDeviceEdit;
    private static javax.swing.JButton jButtonOptionSaveDescription;
    private static javax.swing.JButton jButtonOptionSaveSource;
    private javax.swing.JButton jButtonOptionSelectFolder;
    private javax.swing.JButton jButtonOptionSelectFolder1;
    private javax.swing.JButton jButtonOptionSelectFolder2;
    private javax.swing.JButton jButtonOptionSelectFolder3;
    private javax.swing.JButton jButtonOptionSelectFolder6;
    private javax.swing.JButton jButtonStatSouceAdd;
    private javax.swing.JButton jButtonStatSouceDel;
    private javax.swing.JButton jButtonStatSouceEdit;
    private static javax.swing.JCheckBox jCheckBoxOptionLibraryIsMaster;
    private static javax.swing.JComboBox jComboBoxDevice;
    private javax.swing.JLabel jLabel7;
    private static javax.swing.JLabel jLabelDescription;
    private static javax.swing.JLabel jLabelOptionLocationAdd;
    private static javax.swing.JLabel jLabelOptionLocationKO;
    private static javax.swing.JLabel jLabelOptionLocationLibrary;
    private static javax.swing.JLabel jLabelOptionLocationManual;
    private static javax.swing.JLabel jLabelOptionLocationOK;
    private static javax.swing.JLabel jLabelOptionMask;
    private static javax.swing.JLabel jLabelOptionsFilesAudio;
    private static javax.swing.JLabel jLabelOptionsFilesConvert;
    private static javax.swing.JLabel jLabelOptionsFilesDelete;
    private static javax.swing.JLabel jLabelOptionsFilesImage;
    private static javax.swing.JLabel jLabelOptionsLogCount;
    private static javax.swing.JLabel jLabelOptionsLogLevel;
    private static javax.swing.JLabel jLabelOptionsLogLimit;
    private static javax.swing.JLabel jLabelOptionsProxy;
    private static javax.swing.JList jListDevices;
    private static javax.swing.JList jListStatSources;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelStatSources;
    private javax.swing.JPanel jPanelStatSources1;
    private javax.swing.JScrollPane jScrollPaneStatSouces;
    private javax.swing.JScrollPane jScrollPaneStatSouces1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private static javax.swing.JTextField jTextFieldDescription;
    private static javax.swing.JTextField jTextFieldOptionLocationAdd;
    private static javax.swing.JTextField jTextFieldOptionLocationKO;
    private static javax.swing.JTextField jTextFieldOptionLocationLibrary;
    private static javax.swing.JTextField jTextFieldOptionLocationManual;
    private static javax.swing.JTextField jTextFieldOptionLocationOK;
    private static javax.swing.JTextField jTextFieldOptionMask;
    private static javax.swing.JTextField jTextFieldOptionsFilesAudio;
    private static javax.swing.JTextField jTextFieldOptionsFilesConvert;
    private static javax.swing.JTextField jTextFieldOptionsFilesDelete;
    private static javax.swing.JTextField jTextFieldOptionsFilesImage;
    private static javax.swing.JTextField jTextFieldOptionsLogCount;
    private static javax.swing.JTextField jTextFieldOptionsLogLevel;
    private static javax.swing.JTextField jTextFieldOptionsLogLimit;
    private static javax.swing.JTextField jTextFieldOptionsProxy;
    // End of variables declaration//GEN-END:variables

	private int returnStatus = RET_CANCEL;
}
