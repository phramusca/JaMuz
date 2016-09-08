/*
 * Copyright (C) 2016 raph
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

import jamuz.DbConnJaMuz;
import jamuz.DbInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.process.merge.LogText;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author raph
 */
public class CompareDB {
    /**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {

        boolean comparePlayCounter=false;
        boolean compareRating=true;
        boolean compareAddedDate=true;
        boolean compareLastPlayed=false;
        boolean compareBPM=true;
        
        compareDB(
                "/media/raph/Transcend/JaMuz/JaMuz.db.2016-08-26-Rating2OK",
                "/media/raph/Transcend/JaMuz/JaMuz.db",
                "/home/raph/Bureau/LOGS/",
                comparePlayCounter, compareRating, compareAddedDate, compareLastPlayed, compareBPM
        );
    } 
    
    private static void compareDB(String pathDb1, String pathDb2, String pathLogs,
            boolean comparePlayCounter, boolean compareRating, boolean compareAddedDate, boolean compareLastPlayed, boolean compareBPM) {
        //Connecting databases
        DbConnJaMuz db1= getDb(pathDb1);
        DbConnJaMuz db2= getDb(pathDb2);
        db1.getDbConn().connect();
        db2.getDbConn().connect();
       
        //Reading JaMuz options (rootPath is read in getFiles, so this is needed or it fails, though we do not need rootPath in here)
        Jamuz.configure(pathLogs);
        Jamuz.getMachine().read();
        
        //Getting list of files from databases
        ArrayList<FileInfoInt> filesDb1 = new ArrayList<>();
        ArrayList<FileInfoInt> filesDb2 = new ArrayList<>();
        String sql = "SELECT F.*, P.strPath, P.checked, P.copyRight, 0 AS albumRating, 0 AS percentRated "
                + " FROM file F JOIN path P ON F.idPath=P.idPath ";
        db1.getFiles(filesDb1, sql);
        db2.getFiles(filesDb2, sql);
        
        //Converting lists to maps
        Map<Integer,FileInfoInt> files1=toMap(filesDb1);
        Map<Integer,FileInfoInt> files2=toMap(filesDb2);

        logFile = new LogText(pathLogs);
        if(!logFile.createFile(DateTime.getCurrentLocal(DateTime.DateTimeFormat.FILE).concat("-Comparison"))) {
            Popup.error(MessageFormat.format(Inter.get("Error.Merge.CreatingLOG"), new Object[] {DateTime.getCurrentLocal(DateTime.DateTimeFormat.FILE).concat("-Comparison")+".txt"}));  //NOI18N
            System.exit(1);
        }
        logFile.add("File\tValue\t"+pathDb1+"\t"+pathDb2);
        
        //Comparing maps
        compareDB(files1, files2, false, comparePlayCounter, compareRating, compareAddedDate, compareLastPlayed, compareBPM);
        compareDB(files2, files1, true, comparePlayCounter, compareRating, compareAddedDate, compareLastPlayed, compareBPM);
        
        logFile.close();
    }
    
    private static void compareDB(Map<Integer,FileInfoInt> files1, Map<Integer,FileInfoInt> files2, boolean reverse,
            boolean comparePlayCounter, boolean compareRating, boolean compareAddedDate, boolean compareLastPlayed, boolean compareBPM) {
        files1.values().stream().forEach((file1) -> {
            if(files2.containsKey(file1.getIdFile())) {
                FileInfoInt file2 = files2.get(file1.getIdFile());
                
                if(comparePlayCounter && file1.getPlayCounter()!=file2.getPlayCounter()) {
                    addToLog(file1.getRelativeFullPath(), "PlayCounter", String.valueOf(file1.getPlayCounter()), String.valueOf(file2.getPlayCounter()), reverse);
                }
                if(compareRating && file1.getRating()!=file2.getRating()) {
                    addToLog(file1.getRelativeFullPath(), "Rating", String.valueOf(file1.getRating()), String.valueOf(file2.getRating()), reverse);
                }
                if(compareAddedDate && !file1.getAddedDate().equals(file2.getAddedDate())) {
                    addToLog(file1.getRelativeFullPath(), "AddedDate", 
                            DateTime.formatUTC(file1.getAddedDate(), DateTime.DateTimeFormat.HUMAN, false), 
                            DateTime.formatUTC(file2.getAddedDate(), DateTime.DateTimeFormat.HUMAN, false), reverse);
                }
                if(compareLastPlayed && !file1.getLastPlayed().equals(file2.getLastPlayed())) {
                    addToLog(file1.getRelativeFullPath(), "LastPlayed", 
                            DateTime.formatUTC(file1.getLastPlayed(), DateTime.DateTimeFormat.HUMAN, false), 
                            DateTime.formatUTC(file2.getLastPlayed(), DateTime.DateTimeFormat.HUMAN, false), reverse);
                }
                if(compareBPM && file1.getBPM()!=file2.getBPM()) {
                    addToLog(file1.getRelativeFullPath(), "BPM", String.valueOf(file1.getBPM()), String.valueOf(file2.getBPM()), reverse);
                }
            }
            else {
                addToLog(file1.getRelativeFullPath(), "MISSING", "", "X", reverse);
            }
        });
    }
    
    private static LogText logFile;
    
    private static void addToLog(String file, String value, String dB1, String dB2, boolean reverse) {
        String line = file+"\t"+value+"\t";
        line=line.concat(reverse?dB2+"\t"+dB1:dB1+"\t"+dB2);
        logFile.add(line);
        System.out.println(line);
    }
    
    private static DbConnJaMuz getDb(String path) {
        return new DbConnJaMuz(new DbInfo("sqlite", path, "", ""));
    }
    
    private static HashMap<Integer,FileInfoInt> toMap(ArrayList<FileInfoInt> files) {
        HashMap<Integer,FileInfoInt> map = new HashMap<>();
        files.stream().forEach((i) -> {
            map.put(i.getIdFile(),i);
        });
        return map;
    }
}
