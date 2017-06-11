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
package jamuz.remote;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpReply;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.FtpletContext;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class FTPServer {
    /**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {

        FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(1234);// set the port of the listener (choose your desired port, not 1234)
		serverFactory.addListener("default", factory.createListener());
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(new File("/home/blablah/myusers.properties"));//choose any. We're telling the FTP-server where to read it's user list
		userManagerFactory.setPasswordEncryptor(new PasswordEncryptor()
		{//We store clear-text passwords in this example

				@Override
				public String encrypt(String password) {
					return password;
				}

				@Override
				public boolean matches(String passwordToCheck, String storedPassword) {
					return passwordToCheck.equals(storedPassword);
				}
			});
			//Let's add a user, since our myusers.properties files is empty on our first test run
			BaseUser user = new BaseUser();
			user.setName("test");
			user.setPassword("test");
			user.setHomeDirectory("/home/raph/Bureau/TEMP-Tablet");
			List<Authority> authorities = new ArrayList<Authority>();
			authorities.add(new WritePermission());
			user.setAuthorities(authorities);
			UserManager um = userManagerFactory.createUserManager();
			try
			{
				um.save(user);//Save the user to the user list on the filesystem
			}
			catch (FtpException e1)
			{
				//Deal with exception as you need
			}
			serverFactory.setUserManager(um);
			Map<String, Ftplet> m = new HashMap<String, Ftplet>();
			m.put("miaFtplet", new Ftplet()
			{

				@Override
				public void init(FtpletContext ftpletContext) throws FtpException {
					//System.out.println("init");
					//System.out.println("Thread #" + Thread.currentThread().getId());
				}

				@Override
				public void destroy() {
					//System.out.println("destroy");
					//System.out.println("Thread #" + Thread.currentThread().getId());
				}

				@Override
				public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException
				{
					//System.out.println("beforeCommand " + session.getUserArgument() + " : " + session.toString() + " | " + request.getArgument() + " : " + request.getCommand() + " : " + request.getRequestLine());
					//System.out.println("Thread #" + Thread.currentThread().getId());

					//do something
					return FtpletResult.DEFAULT;//...or return accordingly
				}

				@Override
				public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException
				{
					//System.out.println("afterCommand " + session.getUserArgument() + " : " + session.toString() + " | " + request.getArgument() + " : " + request.getCommand() + " : " + request.getRequestLine() + " | " + reply.getMessage() + " : " + reply.toString());
					//System.out.println("Thread #" + Thread.currentThread().getId());

					//do something
					return FtpletResult.DEFAULT;//...or return accordingly
				}

				@Override
				public FtpletResult onConnect(FtpSession session) throws FtpException, IOException
				{
					//System.out.println("onConnect " + session.getUserArgument() + " : " + session.toString());
					//System.out.println("Thread #" + Thread.currentThread().getId());

					//do something
					return FtpletResult.DEFAULT;//...or return accordingly
				}

				@Override
				public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException
				{
					//System.out.println("onDisconnect " + session.getUserArgument() + " : " + session.toString());
					//System.out.println("Thread #" + Thread.currentThread().getId());

					//do something
					return FtpletResult.DEFAULT;//...or return accordingly
				}
			});
			serverFactory.setFtplets(m);
			//Map<String, Ftplet> mappa = serverFactory.getFtplets();
			//System.out.println(mappa.size());
			//System.out.println("Thread #" + Thread.currentThread().getId());
			//System.out.println(mappa.toString());
			FtpServer server = serverFactory.createServer();
			try
			{
				server.start();//Your FTP server starts listening for incoming FTP-connections, using the configuration options previously set
			}
			catch (FtpException ex)
			{
				//Deal with exception as you need
			}
    } 
    
}
