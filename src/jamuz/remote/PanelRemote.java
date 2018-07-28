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
package jamuz.remote;

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.gui.DialogQRcode;
import jamuz.gui.swing.PopupListener;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.sync.ProcessSync;
import jamuz.utils.CrunchifyQRCode;
import jamuz.utils.Encryption;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelRemote extends javax.swing.JPanel {

	private static Server server;

	/**
	 * Creates new form PanelRemote
	 */
	public PanelRemote() {
		initComponents();
		setIpText();
	}
	
	public void initExtended(ICallBackServer callback) {
		server = new Server((Integer) jSpinnerPort.getValue(), callback);
		jTableRemote.setModel(server.getTableModel());
		jTableRemote.setRowSorter(null);
		//Adding columns from model. Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableRemote.createDefaultColumnsFromModel();
		setColumn(0, 60);
		setColumn(1, 60);
		setColumn(2, 200);
		setColumn(3, 300);
		TableColumn column = jTableRemote.getColumnModel().getColumn(4);
		column.setCellRenderer(new ProgressCellRender());
		
        addMenuItem(Inter.get("Button.Edit")); //NOI18N
        addMenuItem(Inter.get("Button.Export")); //NOI18N
        MouseListener popupListener = new PopupListener(jPopupMenu1);
        jTableRemote.addMouseListener(popupListener);
		
		server.fillClients();
		startStopRemoteServer();
	}
	
	public class ProgressCellRender extends JProgressBar implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {
			ProgressBar progressBar = (ProgressBar) value;
            return progressBar;
        }
    }
	
	private void setColumn(int index, int width) {
        TableColumn column = jTableRemote.getColumnModel().getColumn(index);
		column.setMinWidth(width);
        column.setPreferredWidth(width);
        column.setMaxWidth(width*3);
    }
		
	public static void send(FileInfoInt fileInfo) {    
        Map jsonAsMap = new HashMap();
        jsonAsMap.put("type", "fileInfoInt");
		jsonAsMap.put("coverHash", fileInfo.getCoverHash());
        jsonAsMap.put("rating", fileInfo.getRating());
        jsonAsMap.put("title", fileInfo.getTitle());
        jsonAsMap.put("album", fileInfo.getAlbum());
        jsonAsMap.put("artist", fileInfo.getArtist());
		jsonAsMap.put("genre", fileInfo.getGenre());
        send(jsonAsMap, true);
    }
	
	public static void sendCover(String clientId, FileInfoInt displayedFile, int maxWidth) {
		if(server!=null) {
			server.sendCover(clientId, displayedFile, maxWidth);
		}
	}
	
	public static void send(Map jsonAsMap, boolean isRemote) {
        if(server!=null) {
            server.send(jsonAsMap, isRemote);
        }
    }
	
	public static boolean send(String clientId, JSONObject obj) {
        if(server!=null) {
            return server.send(clientId, obj);
        }
		return false;
    }
	
	public static void send(String clientId, ArrayList<FileInfo> mergeListDbSelected) {
		JSONObject obj = new JSONObject();
		obj.put("type", "mergeListDbSelected");
		JSONArray jsonArray = new JSONArray();
		for (int i=0; i < mergeListDbSelected.size(); i++) {
				jsonArray.add(mergeListDbSelected.get(i).toMap());
		}
		obj.put("files", jsonArray);
		send(clientId, obj);
	}
	
	public static boolean isConnected(String clientId) {
		if(server!=null) {
            return server.isConnected(clientId);
        }
		return false;
	}
	
	/**
	* Returns an <code>InetAddress</code> object encapsulating what is most likely the machine's LAN IP address.
	* <p/>
	* This method is intended for use as a replacement of JDK method <code>InetAddress.getLocalHost</code>, because
	* that method is ambiguous on Linux systems. Linux systems enumerate the loopback network interface the same
	* way as regular LAN network interfaces, but the JDK <code>InetAddress.getLocalHost</code> method does not
	* specify the algorithm used to select the address returned under such circumstances, and will often return the
	* loopback address, which is not valid for network communication. Details
	* <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
	* <p/>
	* This method will scan all IP addresses on all network interfaces on the host machine to determine the IP address
	* most likely to be the machine's LAN address. If the machine has multiple IP addresses, this method will prefer
	* a site-local IP address (e.g. 192.168.x.x or 10.10.x.x, usually IPv4) if the machine has one (and will return the
	* first site-local address if the machine has more than one), but if the machine does not hold a site-local
	* address, this method will return simply the first non-loopback address found (IPv4 or IPv6).
	* <p/>
	* If this method cannot find a non-loopback address using this selection algorithm, it will fall back to
	* calling and returning the result of JDK method <code>InetAddress.getLocalHost</code>.
	* <p/>
	*
	* @throws UnknownHostException If the LAN address of the machine cannot be found.
	*/
   private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
	   try {
		   InetAddress candidateAddress = null;
		   // Iterate all NICs (network interface cards)...
		   for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
			   NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
			   // Iterate all IP addresses assigned to each card...
			   for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
				   InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
				   if (!inetAddr.isLoopbackAddress()) {

					   if (inetAddr.isSiteLocalAddress()) {
						   // Found non-loopback site-local address. Return it immediately...
						   return inetAddr;
					   }
					   else if (candidateAddress == null) {
						   // Found non-loopback address, but not necessarily site-local.
						   // Store it as a candidate to be returned if site-local address is not subsequently found...
						   candidateAddress = inetAddr;
						   // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
						   // only the first. For subsequent iterations, candidate will be non-null.
					   }
				   }
			   }
		   }
		   if (candidateAddress != null) {
			   // We did not find a site-local address, but we found some other non-loopback address.
			   // Server might have a non-site-local address assigned to its NIC (or it might be running
			   // IPv6 which deprecates the "site-local" concept).
			   // Return this non-loopback candidate address...
			   return candidateAddress;
		   }
		   // At this point, we did not find a non-loopback address.
		   // Fall back to returning whatever InetAddress.getLocalHost() returns...
		   InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
		   if (jdkSuppliedAddress == null) {
			   throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
		   }
		   return jdkSuppliedAddress;
	   }
	   catch (SocketException | UnknownHostException e) {
		   UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
		   unknownHostException.initCause(e);
		   throw unknownHostException;
	   }
   }

   private void enableConfig(boolean enable) {
//	   Swing.enableComponents(jPanelRemote, enable);
		jSpinnerPort.setEnabled(enable);
   }
   
   private void setIpText() {
	   StringBuilder IP = new StringBuilder();
		IP.append("<html>Set \"<B>");
		try {
			IP.append(getLocalHostLANAddress().getHostAddress()).append(":").append((Integer) jSpinnerPort.getValue()); 
		} catch (UnknownHostException ex) {
			IP.append("Undetermined !");
		}
		IP.append("\"</B> in JaMuz Remote as <B>\"&lt;IP&gt;:&lt;Port&gt;\"</B> or read QR code with your Android phone.</html>");
		jLabelIP.setText(IP.toString());
   }
   
   private void startStopRemoteServer() {
		enableConfig(false);
		if(server!=null) {
			server.closeClients();
			if(jButtonStart.getText().equals(Inter.get("Button.Start"))) {
				server.setPort((Integer) jSpinnerPort.getValue());
				if(server.connect()) {
					enableConfig(false);
					jButtonQRcode.setEnabled(true);
					jButtonStart.setText(Inter.get("Button.Pause"));
				}
			}
			else {
				server.close();
				enableConfig(true);
				jButtonStart.setText(Inter.get("Button.Start"));
			}
		}
        jButtonStart.setEnabled(true);
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
        jLabel1 = new javax.swing.JLabel();
        jCheckBoxServerStartOnStartup = new javax.swing.JCheckBox();
        jLabelIP = new javax.swing.JLabel();
        jButtonQRcode = new javax.swing.JButton();
        jScrollPaneCheckTags1 = new javax.swing.JScrollPane();
        jTableRemote = new javax.swing.JTable();

        jPanelRemote.setBorder(javax.swing.BorderFactory.createTitledBorder("Jamuz Remote Server"));

        jSpinnerPort.setModel(new javax.swing.SpinnerNumberModel(2013, 2009, 65535, 1));
        jSpinnerPort.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerPortStateChanged(evt);
            }
        });

        jButtonStart.setText(Inter.get("Button.Start")); // NOI18N
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });

        jLabel1.setText(Inter.get("PanelMain.jLabel1.text")); // NOI18N

        jCheckBoxServerStartOnStartup.setSelected(true);
        jCheckBoxServerStartOnStartup.setText(Inter.get("PanelMain.jCheckBoxServerStartOnStartup.text")); // NOI18N
        jCheckBoxServerStartOnStartup.setToolTipText(Inter.get("PanelMain.jCheckBoxServerStartOnStartup.toolTipText")); // NOI18N
        jCheckBoxServerStartOnStartup.setEnabled(false);

        jLabelIP.setText("IP: xxx.xxx.xxx.xxx"); // NOI18N

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

        javax.swing.GroupLayout jPanelRemoteLayout = new javax.swing.GroupLayout(jPanelRemote);
        jPanelRemote.setLayout(jPanelRemoteLayout);
        jPanelRemoteLayout.setHorizontalGroup(
            jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRemoteLayout.createSequentialGroup()
                        .addComponent(jLabelIP)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPaneCheckTags1)
                    .addGroup(jPanelRemoteLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonQRcode)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jCheckBoxServerStartOnStartup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonStart)))
                .addContainerGap())
        );
        jPanelRemoteLayout.setVerticalGroup(
            jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonStart)
                    .addComponent(jSpinnerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jCheckBoxServerStartOnStartup)
                    .addComponent(jButtonQRcode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelIP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
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

    private void jButtonQRcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQRcodeActionPerformed
        try {
            String ip = getLocalHostLANAddress().getHostAddress();
            int port = (Integer) jSpinnerPort.getValue();
            String encrypted = Encryption.encrypt(ip+":"+port, "NOTeBrrhzrtestSecretK");
            String url = "jamuzremote://"+encrypted;
			//http://stackoverflow.com/questions/10258633/android-start-application-from-qr-code-with-params
            BufferedImage bufferedImage = CrunchifyQRCode.createQRcode(url, 250);
            DialogQRcode.main(bufferedImage);
        } catch (UnknownHostException ex) {
        }
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

    private void jSpinnerPortStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerPortStateChanged
        setIpText();
    }//GEN-LAST:event_jSpinnerPortStateChanged

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
			DialogClientInfo.main(clientInfo);
        }
    }
	
	private void menuExport() {
        ClientInfo clientInfo = getSelected();
        if(clientInfo!=null) {
			//FIXME LOW REMOTE allow aborting with another menu item
			ProcessSync processSync = new ProcessSync(
					"Thread.PanelSync.ProcessSync.Remote", 
					clientInfo.getDevice(), clientInfo.getProgressBar());
			processSync.start();
        }
    }
	

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonQRcode;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JCheckBox jCheckBoxServerStartOnStartup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelIP;
    private javax.swing.JPanel jPanelRemote;
    private javax.swing.JPopupMenu jPopupMenu1;
    private static javax.swing.JScrollPane jScrollPaneCheckTags1;
    private javax.swing.JSpinner jSpinnerPort;
    private static javax.swing.JTable jTableRemote;
    // End of variables declaration//GEN-END:variables
}
