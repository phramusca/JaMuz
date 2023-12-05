/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
package jamuz.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class SSH {

	private Session session;
    private final String adminLogin;
    private final String adminPassword;
    private final String ip;

    /**
     *
     * @param ip
     * @param login
     * @param password
     */
    public SSH(String ip, String login, String password) {
		this.ip = ip;
		this.adminLogin = login;
		this.adminPassword = password;
	}

    /**
     *
     * @return
     */
    public boolean connect() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(this.adminLogin, ip, 22);
            session.setPassword(this.adminPassword);

            //TODO: Avoid using that, and automatically accept fingerprint
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
//            jsch.setKnownHosts("/home/anand/.ssh/known_hosts");
//            jsch.addIdentity("/home/anand/.ssh/id_rsa");

//            session.connect(40000);   // making a connection with timeout.
            session.connect();   // making a connection without timeout.
			
            //TODO: Make Send and Receive functions
            //http://www.jcraft.com/jsch/examples/Exec.java
			
			return session.isConnected();


        } catch (JSchException ex) {
            Popup.error(ex);
			return false;
        }
    }
	
    /**
     *
     * @return
     */
    public boolean isConnected() {
        return session.isConnected();
    }
    
    private static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
        @Override
        public String getPassword(){ return null; }
        @Override
        public void showMessage(String message){
          JOptionPane.showMessageDialog(null, message);
        }
        @Override
        public boolean promptYesNo(String message){
            Object[] options={ Inter.get("Label.Yes"), Inter.get("Label.No") };
            int foo=JOptionPane.showOptionDialog(null, 
                                                 message,
                                                 Inter.get("Label.WARNING"), 
                                                 JOptionPane.DEFAULT_OPTION, 
                                                 JOptionPane.WARNING_MESSAGE,
                                                 null, options, options[0]);
            return foo==0;
        }
        @Override
        public String getPassphrase(){ return null; }
        @Override
        public boolean promptPassphrase(String message){ return false; }
        @Override
        public boolean promptPassword(String message){ return false; }
        @Override
        public String[] promptKeyboardInteractive(String destination,
                                                String name,
                                                String instruction,
                                                String[] prompt,
                                                boolean[] echo){
        return null;
        }
    }

    /**
     * disconnect ssh session
     */
    public void disconnect() {
		session.disconnect();
	}

	/**
	 *
	 * @param source
	 * @param destination
	 * @return
	 */
	public boolean moveFile(String source, String destination) {
        StringBuilder result = new StringBuilder (); //TODO: Sould not this be used ? (does it include error output ?)
        int exitStatus = sendAndReceive("mv \"" + source + "\" \"" + destination + "\"", result); //NOI18N
        return !(exitStatus>0);
    }
    
    /**
     *
     * @param myCmd
     * @param result
     * @return
     */
    public int sendAndReceive(String myCmd, StringBuilder result) {
        InputStream in = null;
   
        try {
            Channel channel = session.openChannel("exec"); //NOI18N
            ((ChannelExec) channel).setCommand(myCmd);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err); 
//TODO: Get error stream to catch messages as :
//mv: impossible d'évaluer «/home/raph/Vidéos/Films/American Bluff [2013].avi»: Aucun fichier ou dossier de ce type
            in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            String line;
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    line=new String(tmp, 0, i);
                    result.append(line);
                }
                if (channel.isClosed()) {
                    int exitStatus=channel.getExitStatus();
                    channel.disconnect();
                    return exitStatus;
                }
            }
        } catch (JSchException | IOException ex) {
            Popup.error(ex);
            return 131;
        } finally {
            if(in!=null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Popup.error(ex);
                    return 132;
                }
            }
        }
    }	
}
