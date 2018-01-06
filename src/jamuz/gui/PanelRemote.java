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
package jamuz.gui;

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.remote.Client;
import jamuz.remote.ICallBackReception;
import jamuz.remote.Server;
import jamuz.remote.ServerClient;
import jamuz.utils.CrunchifyQRCode;
import jamuz.utils.Encryption;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.Swing;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelRemote extends javax.swing.JPanel {

	private static Server server;
	private static ListModelRemoteClients listModelRemoteClients;
		
	//TODO: use a callback for all usages of PanelMain
	private ICallBackReception callback;

	/**
	 * Creates new form PanelRemote
	 */
	public PanelRemote() {
		initComponents();
		listModelRemoteClients = new ListModelRemoteClients();
		jListRemoteClients.setModel(listModelRemoteClients);
		
		//TODO: Update when changing port;
		StringBuilder IP = new StringBuilder();
		IP.append("<html>Set this in remote: <BR/>");
		try {
			IP.append(getLocalHostLANAddress().getHostAddress()).append(":").append((Integer) jSpinnerPort.getValue()); 
		} catch (UnknownHostException ex) {
			IP.append("Undetermined !");
		}
		IP.append("</html>");
		jLabelIP.setText(IP.toString());
		
		startStopRemoteServer();
	}

	public void setCallback(ICallBackReception callback) {
		this.callback = callback;
	}
	
	class CallBackReception implements ICallBackReception {
		@Override
		public void received(String login, String msg) {
			if(listModelRemoteClients.getClients().contains(login)) {
				callback.received(login, msg);
			}
		}
		
		@Override
		public void authenticated(Client login, ServerClient client) {
			listModelRemoteClients.add(login);
			callback.authenticated(login, client);
		}

		@Override
		public void disconnected(String login) {
			listModelRemoteClients.removeClient(login);
			callback.disconnected(login);
		}
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
	
	public static void sendCover(String login, FileInfoInt displayedFile, int maxWidth) {
		if(server!=null) {
			server.sendCover(login, displayedFile, maxWidth);
		}
	}
	
	public static void sendFile(String login, int id) {
		if(server!=null) {
			FileInfoInt fileInfoInt = Jamuz.getDb().getFile(id);
			if(!server.sendFile(login, fileInfoInt)) {
				//FIXME SYNC Happens when file not found
				// Need to mark as deleted in db 
				// AND somehow remove it from filesToKeep
				//and filesToGet in remote
				Popup.error("Cannot send missing file \""
						+fileInfoInt.getFullPath().getAbsolutePath()+"\"");
			}
		}
	}
	
	public static void send(Map jsonAsMap, boolean isRemote) {
        if(server!=null) {
            server.send(jsonAsMap, isRemote);
        }
    }
	
	public static boolean send(String login, String msg) {
        if(server!=null) {
            return server.send(login, msg);
        }
		return false;
    }
	
	public static boolean send(String login, JSONObject obj) {
        if(server!=null) {
            return server.send(login, obj);
        }
		return false;
    }
	
	public static boolean send(String login, Map jsonAsMap) {
        if(server!=null) {
            return server.send(login, jsonAsMap);
        }
		return false;
    }
	
	public static boolean isConnected(String login) {
		if(server!=null) {
            return server.isConnected(login);
        }
		return false;
	}
	
	public static boolean getDatabase(String login, String path) {
        if(server!=null) {
			return server.getDatabase(login, path);
        }
		return false;
    }
	
	public static boolean sendDatabase(String login, String path) {
        if(server!=null) {
			return server.sendDatabase(login, path);
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

   private void startStopRemoteServer() {
		Swing.enableComponents(jPanelRemote, false);

        for(String client : listModelRemoteClients.getClients()) {
            server.closeClient(client);
        }
		listModelRemoteClients.clear();

        if(jButtonStart.getText().equals(Inter.get("Button.Start"))) {
            CallBackReception callBackReception = new CallBackReception();
            server = new Server((Integer) jSpinnerPort.getValue(), callBackReception);
            if(server.connect()) {
                Swing.enableComponents(jPanelRemote, false);
				jButtonQRcode.setEnabled(true);
                jButtonStart.setText(Inter.get("Button.Pause"));
            }
        }
        else {
            server.close();
            Swing.enableComponents(jPanelRemote, true);
            jButtonStart.setText(Inter.get("Button.Start"));
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

        jPanelRemote = new javax.swing.JPanel();
        jSpinnerPort = new javax.swing.JSpinner();
        jButtonStart = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jCheckBoxServerStartOnStartup = new javax.swing.JCheckBox();
        jLabelIP = new javax.swing.JLabel();
        jButtonQRcode = new javax.swing.JButton();
        jScrollPanePlayerQueue1 = new javax.swing.JScrollPane();
        jListRemoteClients = new javax.swing.JList();

        jPanelRemote.setBorder(javax.swing.BorderFactory.createTitledBorder("Jamuz Remote Server"));

        jSpinnerPort.setModel(new javax.swing.SpinnerNumberModel(2013, 2009, 65535, 1));

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

        jListRemoteClients.setModel(new DefaultListModel());
        jScrollPanePlayerQueue1.setViewportView(jListRemoteClients);

        javax.swing.GroupLayout jPanelRemoteLayout = new javax.swing.GroupLayout(jPanelRemote);
        jPanelRemote.setLayout(jPanelRemoteLayout);
        jPanelRemoteLayout.setHorizontalGroup(
            jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRemoteLayout.createSequentialGroup()
                        .addComponent(jScrollPanePlayerQueue1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanelRemoteLayout.createSequentialGroup()
                        .addComponent(jButtonQRcode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelIP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jCheckBoxServerStartOnStartup, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelRemoteLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonStart))))
        );
        jPanelRemoteLayout.setVerticalGroup(
            jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRemoteLayout.createSequentialGroup()
                .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonStart)
                    .addComponent(jSpinnerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxServerStartOnStartup)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRemoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelIP)
                    .addComponent(jButtonQRcode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPanePlayerQueue1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
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
        try {
            String ip = getLocalHostLANAddress().getHostAddress();
            int port = (Integer) jSpinnerPort.getValue();
            //TODO: Use user's own secret. It also allows to identify users
            String encrypted = Encryption.encrypt(ip+":"+port, "NOTeBrrhzrtestSecretK");
            String url = "JaMuzRemote://"+encrypted;
            BufferedImage bufferedImage = CrunchifyQRCode.createQRcode(url, 250);
            DialogQRcode.main(bufferedImage);
        } catch (UnknownHostException ex) {
        }
    }//GEN-LAST:event_jButtonQRcodeActionPerformed

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        startStopRemoteServer();
    }//GEN-LAST:event_jButtonStartActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonQRcode;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JCheckBox jCheckBoxServerStartOnStartup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelIP;
    private static javax.swing.JList jListRemoteClients;
    private javax.swing.JPanel jPanelRemote;
    private javax.swing.JScrollPane jScrollPanePlayerQueue1;
    private javax.swing.JSpinner jSpinnerPort;
    // End of variables declaration//GEN-END:variables
}
