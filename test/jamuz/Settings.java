/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz;

import jamuz.process.merge.StatSource;
import jamuz.gui.PanelMain;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import jamuz.utils.FileSystem;
import jamuz.utils.Inter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Settings {
    private static final String testRootPath = "/home/raph/Documents/04-Creations/Dev/NetBeans/JaMuz/TestsJunit/"; //Must have trailing separator (/ or \\)
    
    public static String getMusicFolder() {
        return testRootPath + "Musique" + File.separator;
    }
    
    public static String getRessourcesPath() {
        return testRootPath+File.separator+"Ressources"+File.separator;
    }
    
    public static String getAppFolder() {
        return testRootPath + "AppFolder" + File.separator;
    }
    
    public static String getStatSourcesFolder() {
        return testRootPath + "StatSources" + File.separator;
    }
    
    public static BufferedImage getTestCover() {
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 50);
        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = image.createGraphics();
        FontMetrics fm = g.getFontMetrics(f);
        g.setBackground(Color.YELLOW);
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
        g.setBackground(Color.WHITE);
        g.clearRect(20, 20, image.getWidth() -40, image.getHeight()-40);
        g.setFont(f);
        g.setColor(Color.BLUE);
        g.drawString("TEST COVER", 40, 180); 
        
        return image;
    }
    
    /**
     * Sets up application and options
     * @throws Exception
     */
    public static void setupApplication() throws Exception {
        //Refresh database (overwrite db test file with reference db)
        File source = new File(getRessourcesPath()+"JaMuz_Minimal.db");
        File destination = new File(getAppFolder()+"JaMuz_Test.db");
        FileSystem.copyFile(source, destination);
        
        //Configure application
        if(!Jamuz.configure(getAppFolder())) {
            throw new Exception("Error configuring test");
        }
        
        //Set options for current machine
        for(Option option : Jamuz.getMachine().getOptions()) {
            Jamuz.getDb().setOption(option, getOptionValue(option.getId()));
        }

        //Read created options
        Jamuz.getMachine().read();

        //RE-Create folders as needed
        deleteAndMakeFolder("location.add");
        deleteAndMakeFolder("location.library");
        deleteAndMakeFolder("location.ok");
        deleteAndMakeFolder("location.ko");
    }

    public static void addStatSource(String name, int idStatement, String rootPath, int idDevice) throws IOException {
        File sourceFile = new File(getRessourcesPath()+"statSources/"+name);
        String destination=getStatSourcesFolder()+name;
        File destinationFile = new File(destination);
        FileSystem.copyFile(sourceFile, destinationFile);
        //Define stat source in Jamuz dB
        Jamuz.getDb().setStatSource(new StatSource(-1, name, idStatement, destination, "", "", 
                rootPath, Jamuz.getMachine().getName(), idDevice, true, ""));
    }
    
    private static void deleteAndMakeFolder(String optionId) throws IOException {
        File file = new File(Jamuz.getMachine().getOptionValue(optionId));
        FileUtils.deleteDirectory(file);
        file.mkdir();
    }

    private static String getOptionValue(String optionId) {
        switch (optionId)  {
            case "location.library": return Settings.getMusicFolder() + "Archive" + File.separator;
            case "library.isMaster": return "true";
            case "location.add" : return Settings.getMusicFolder() + "Nouveau" + File.separator;
            case "location.ok" : return Settings.getMusicFolder() + "Archive" + File.separator;
            case "location.ko" : return Settings.getMusicFolder() + "Nouveau-KO" + File.separator;
            case "location.manual" : return Settings.getMusicFolder() + "Nouveau-Manuel" + File.separator;
            case "location.mask": return "%albumartist%/%album%/%track% %title%";
            case "log.level" : return "ALL";
            case "log.limit" : return "5242880";
            case "log.count" : return "20";
            case "files.audio" : return "mp3";
            case "files.image" : return "png,jpg,jpeg,bmp,gif";
            case "files.convert" : return "wma:mp3,ogg:mp3,m4a:mp3,mpc:mp3";
            case "files.delete" : return "db,ini,txt,m3u,pls,htm,html,doc,nfo,url";
            case "network.proxy" : return "";
        }
        return "";
    }
    
    public static void startGUI(String tab) throws InterruptedException {
        PanelMain.main(null); 
        //Need to wait for GUI to load
        //TODO: should not have to do this: need to use some kind of delegates to call back GUI from model
        //BUT it allows to see test progress and eventually to test GUI ...
        Thread.sleep(3000); //ms
        PanelMain.selectTab(Inter.get(tab));
    }
}
