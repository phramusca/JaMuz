/*
 * Copyright (C) 2017 phramusca <phramusca@gmail.com>
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
package jamuz.remote;

import jamuz.Jamuz;
import jamuz.gui.DialogQRcode;
import jamuz.gui.swing.PopupListener;
import jamuz.gui.swing.ProgressCellRender;
import jamuz.process.sync.ICallBackSync;
import jamuz.process.sync.ProcessSync;
import jamuz.utils.Encryption;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.QRCode;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class PanelRemote extends javax.swing.JPanel {

	private static Server server;
	private Frame parent;

	/**
	 * Creates new form PanelRemote
	 */
	public PanelRemote() {
		initComponents();
	}

	/**
	 *
	 * @param parent
	 */
	public void initExtended(Frame parent) {
		this.parent = parent;
		boolean onStartup = Boolean.parseBoolean(Jamuz.getOptions().get("server.on.startup", "false"));
		jCheckBoxServerStartOnStartup.setSelected(onStartup);
		
		boolean enableNewClients = Boolean.parseBoolean(Jamuz.getOptions().get("server.enable.new.clients", "false"));
		jCheckBoxEnableByDefault.setSelected(enableNewClients);
		
        String getPort = Jamuz.getOptions().get("server.port", "2013");
		int port = getPort.isBlank()?2013:Integer.parseInt(Jamuz.getOptions().get("server.port", "2013"));
		jSpinnerPort.setValue(port);
		server = new Server(port);

		jTableRemote.setModel(server.getTableModel());
		jTableRemote.setRowSorter(null);
		//Adding columns from model. Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableRemote.createDefaultColumnsFromModel();
		setColumn(0, 60);
		setColumn(1, 200);
		setColumn(2, 300);
		TableColumn column = jTableRemote.getColumnModel().getColumn(3);
		column.setCellRenderer(new ProgressCellRender());
		
        addMenuItem(Inter.get("Button.Edit")); //NOI18N
        addMenuItem(Inter.get("Button.Export")); //NOI18N
        MouseListener popupListener = new PopupListener(jPopupMenu1);
        jTableRemote.addMouseListener(popupListener);
		
        fillIPsCombo();
        
		server.fillClients();
		if(onStartup) {
			startStopRemoteServer();
		}
	}
	
	private void setColumn(int index, int width) {
        TableColumn column = jTableRemote.getColumnModel().getColumn(index);
		column.setMinWidth(width);
        column.setPreferredWidth(width);
        column.setMaxWidth(width*3);
    }

	private Map<String, InetAddress> getIPs() throws UnknownHostException {
		try {
            Map<String, InetAddress> IPs = new LinkedHashMap<>();

			//First try using this method
			//https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
			try(final DatagramSocket socket = new DatagramSocket()){
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
				System.out.println("Socket trick: "+socket.getLocalAddress().getHostAddress());
                IPs.put("Pref. Outbound", socket.getLocalAddress());
			}
			//TODO: Support MacOS:
//			Socket socket = new Socket();
//			socket.connect(new InetSocketAddress("google.com", 80));
//			System.out.println(socket.getLocalAddress());
		   
			//Then, loop on network interfaces but hard to determine which one is best (includes wifi, lan and docker)
			System.out.println("Listing interfaces:");
			for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();	   
				for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress() && inetAddr.isSiteLocalAddress()) {
						System.out.println("\t"+iface.getDisplayName()+": "+inetAddr.getHostAddress());
						IPs.put(iface.getDisplayName(), inetAddr);
					}
				}
			}
		   
			//Finally, get the IP provided by java but it usually returns loopback ip so useless
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			System.out.println("JDK supplied: "+jdkSuppliedAddress.getHostAddress());
            IPs.put("JDK supplied", jdkSuppliedAddress);
		   
            return IPs;
		}
		catch (SocketException | UnknownHostException e) {
		   UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
		   unknownHostException.initCause(e);
		   throw unknownHostException;
		}
	}

    private void fillIPsCombo() {
        try {
            jButtonRefreshIP.setEnabled(false);
            jButtonQRcode.setEnabled(false);
            Map<String, InetAddress> IPs = getIPs();
            DefaultComboBoxModel comboModelPlaylist = (DefaultComboBoxModel) jComboBoxIP.getModel();
            comboModelPlaylist.removeAllElements();
            for (Map.Entry<String, InetAddress> entry : IPs.entrySet()) {
                comboModelPlaylist.addElement(entry);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(PanelRemote.class.getName()).log(Level.SEVERE, null, ex);
        }
        jButtonRefreshIP.setEnabled(true);
        jButtonQRcode.setEnabled(true);
    }
    
	private void enableConfig(boolean enable) {
		jSpinnerPort.setEnabled(enable);
	}
      
	private void startStopRemoteServer() {
		enableConfig(false);
		if(server!=null) {
			if(jButtonStart.getText().equals(Inter.get("Button.Start"))) {
				server.setPort((Integer) jSpinnerPort.getValue());
				if(server.connect()) {
					Jamuz.getOptions().set("server.port", String.valueOf(server.getPort()));
					Jamuz.getOptions().save();
					enableConfig(false);
					jButtonStart.setText(Inter.get("Button.Pause"));
				} else {
					enableConfig(true);
				}
			}
			else {
				server.close();
				jButtonStart.setText(Inter.get("Button.Start"));
				enableConfig(true);
			}
		}
        jButtonStart.setEnabled(true);
	}
   
    public void sendPosition(int position) {
        server.sendPosition(position);
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
        jPanelRemote = new javax.swing.JPanel();
        jSpinnerPort = new javax.swing.JSpinner();
        jButtonStart = new javax.swing.JButton();
        jCheckBoxServerStartOnStartup = new javax.swing.JCheckBox();
        jButtonQRcode = new javax.swing.JButton();
        jScrollPaneCheckTags1 = new javax.swing.JScrollPane();
        jTableRemote = new javax.swing.JTable();
        jButtonRefreshIP = new javax.swing.JButton();
        jCheckBoxEnableByDefault = new javax.swing.JCheckBox();
        jComboBoxIP = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();

        jPanelRemote.setBorder(javax.swing.BorderFactory.createTitledBorder("Jamuz Remote Server"));

        jSpinnerPort.setModel(new javax.swing.SpinnerNumberModel(2013, 2009, 65535, 1));

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

        jButtonQRcode.setText(Inter.get("PanelMain.jButtonQRcode.text")); // NOI18N
        jButtonQRcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQRcodeActionPerformed(evt);
            }
        });

        jTableRemote.setAutoCreateColumnsFromModel(false);
        jTableRemote.setModel(new jamuz.remote.TableModelRemote());
        jTableRemote.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableRemote.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableRemote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableRemoteMousePressed(evt);
            }
        });
        jScrollPaneCheckTags1.setViewportView(jTableRemote);

        jButtonRefreshIP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/update.png"))); // NOI18N
        jButtonRefreshIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshIPActionPerformed(evt);
            }
        });

        jCheckBoxEnableByDefault.setSelected(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("inter/Bundle"); // NOI18N
        jCheckBoxEnableByDefault.setText(bundle.getString("PanelMain.jCheckBoxEnableByDefault.text")); // NOI18N
        jCheckBoxEnableByDefault.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxEnableByDefaultItemStateChanged(evt);
            }
        });

        jComboBoxIP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText(":");

        javax.swing.GroupLayout jPanelRemoteLayout = new javax.swing.GroupLayout(jPanelRemote);
        jPanelRemote.setLayout(jPanelRemoteLayout);
        jPanelRemoteLayout.setHorizontalGroup(
            jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCheckTags1)
                    .addGroup(jPanelRemoteLayout.createSequentialGroup()
                        .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                                .addComponent(jButtonRefreshIP)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxIP, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinnerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonQRcode))
                            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                                .addComponent(jCheckBoxServerStartOnStartup)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBoxEnableByDefault)))
                        .addGap(32, 399, Short.MAX_VALUE)
                        .addComponent(jButtonStart, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelRemoteLayout.setVerticalGroup(
            jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelRemoteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinnerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jButtonQRcode)
                            .addComponent(jButtonRefreshIP, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBoxServerStartOnStartup)
                            .addComponent(jCheckBoxEnableByDefault)))
                    .addComponent(jButtonStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelRemote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelRemote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonQRcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQRcodeActionPerformed
        Map.Entry<String, InetAddress> selectedItem = (Map.Entry<String, InetAddress>) jComboBoxIP.getSelectedItem();
        int port = (Integer) jSpinnerPort.getValue();
        String encrypted = Encryption.encrypt(selectedItem.getValue().getHostAddress()+":"+port, "NOTeBrrhzrtestSecretK");
        String url = "jamuzremote://"+encrypted;
        //http://stackoverflow.com/questions/10258633/android-start-application-from-qr-code-with-params
        BufferedImage bufferedImage = QRCode.create(url, 250);
        DialogQRcode.main(parent, bufferedImage);
    }//GEN-LAST:event_jButtonQRcodeActionPerformed

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        startStopRemoteServer();
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void jTableRemoteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRemoteMousePressed
        // If Right mouse click, select the line under mouse
        if (SwingUtilities.isRightMouseButton( evt ) )
        {
            Point p = evt.getPoint();
            int rowNumber = jTableRemote.rowAtPoint( p );
            ListSelectionModel model = jTableRemote.getSelectionModel();
            model.setSelectionInterval( rowNumber, rowNumber );
        }
    }//GEN-LAST:event_jTableRemoteMousePressed

    private void jCheckBoxServerStartOnStartupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxServerStartOnStartupItemStateChanged
        Jamuz.getOptions().set("server.on.startup", String.valueOf(Boolean.valueOf(jCheckBoxServerStartOnStartup.isSelected())));
		Jamuz.getOptions().save();
    }//GEN-LAST:event_jCheckBoxServerStartOnStartupItemStateChanged

    private void jButtonRefreshIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshIPActionPerformed
//        setIpText();
        fillIPsCombo();
    }//GEN-LAST:event_jButtonRefreshIPActionPerformed

    private void jCheckBoxEnableByDefaultItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxEnableByDefaultItemStateChanged
        Jamuz.getOptions().set("server.enable.new.clients", String.valueOf(Boolean.valueOf(jCheckBoxEnableByDefault.isSelected())));
		Jamuz.getOptions().save();
		
    }//GEN-LAST:event_jCheckBoxEnableByDefaultItemStateChanged

	private ClientInfo getSelected() {
		int selectedRow = jTableRemote.getSelectedRow(); 			
		if(selectedRow>=0) { 	
			//convert to model index (as sortable model) 		
			selectedRow = jTableRemote.convertRowIndexToModel(selectedRow); 
			return server.getTableModel().getClient(selectedRow);
		}
		return null;
	}
	
	private void addMenuItem(String item) {
        JMenuItem menuItem = new JMenuItem(item);
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
    }
    
    ActionListener menuListener = (ActionEvent e) -> {
		JMenuItem source = (JMenuItem)(e.getSource());
		String sourceTxt=source.getText();
		if(sourceTxt.equals(Inter.get("Button.Edit"))) { //NOI18N
			menuEdit();
		}
		else if(sourceTxt.equals(Inter.get("Button.Export"))) { //NOI18N
			menuExport();
		}
		else {
			Popup.error("Unknown menu item: " + sourceTxt); //NOI18N
		}
	};
	
	private void menuEdit() {
        ClientInfo clientInfo = getSelected();
        if(clientInfo!=null) {
			DialogClientInfo.main(parent, clientInfo);
        }
    }
	
	private void menuExport() {
        ClientInfo clientInfo = getSelected();
        if(clientInfo!=null) {
			//FIXME Z SERVER allow aborting with another menu item
			ProcessSync processSync = new ProcessSync(
					"Thread.PanelSync.ProcessSync.Remote", 
					clientInfo.getDevice(), clientInfo.getProgressBar(), 
					new CallBackSync());
			processSync.start();
        }
    }
	
	class CallBackSync implements ICallBackSync {

		@Override
		public void refresh() {
			//TODO: Refresh only concerned cell (progressBar of given login)
			server.getTableModel().fireTableDataChanged();
		}

		@Override
		public void enable() {}

		@Override
		public void enableButton(boolean enable) {}

		@Override
		public void addRow(String file, int idIcon) {}

		@Override
		public void addRow(String file, String msg) {}
	}
	
	static void refreshList() {
		//TODO: Refresh only concerned cell(s)
		server.getTableModel().fireTableDataChanged();
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonQRcode;
    private javax.swing.JButton jButtonRefreshIP;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JCheckBox jCheckBoxEnableByDefault;
    private javax.swing.JCheckBox jCheckBoxServerStartOnStartup;
    private javax.swing.JComboBox<String> jComboBoxIP;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanelRemote;
    private javax.swing.JPopupMenu jPopupMenu1;
    private static javax.swing.JScrollPane jScrollPaneCheckTags1;
    private javax.swing.JSpinner jSpinnerPort;
    private static javax.swing.JTable jTableRemote;
    // End of variables declaration//GEN-END:variables
}
