/*
 * Copyright (C) 2018 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package check.code;

import jamuz.utils.FileSystem;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class BundleScanner {
	
	private static ArrayList<String> filesToSearch;
	private static TreeMap<String, KeyResults> translations;
	
	/**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {
		
		filesToSearch=new ArrayList<>();
		browseFS(new File("/home/raph/Documents/04-Creations/Dev/NetBeans/JaMuz/JaMuz_DEV/src/jamuz"));
		
		translations = new TreeMap<>();
		File[] files = FileSystem.replaceHome("/home/raph/Documents/04-Creations/Dev/NetBeans/JaMuz/JaMuz_DEV/src/jamuz")
			.listFiles((File dir, String name) -> (
					name.endsWith(".properties")
					&& name.startsWith("Bundle")));
		int nbFiles=0;
		if (files != null) {
			for (File file : files) {
				readBundle(file.getAbsolutePath());
			}
			nbFiles=files.length;
		}

		for(String file : filesToSearch) {
			try {
				searchInFile(file);
			} catch (IOException ex) {
				Logger.getLogger(BundleScanner.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

//		System.out.println("****************************************************************");
//		System.out.println("******************** " + "All Values" + "********************** ");
//		System.out.println("****************************************************************");
//		
//		for(String key : translations.keySet()) {
//			System.out.println(key);
//			displayResults(key);
//		}
		
//		System.out.println("****************************************************************");
//		System.out.println("!!! WARNING !!! " + "Missing languages !");
//		System.out.println("****************************************************************");
//		
//		for(String key : translations.keySet()) {
//			if(translations.get(key).values.size()!=nbFiles) {
//				System.out.println(key);
//				displayResults(key);
//			}
//		}
		
		System.out.println("****************************************************************");
		System.out.println("!!! WARNING !!! " + "No results !");
		System.out.println("****************************************************************");
		
		for(String key : translations.keySet()) {
			if(translations.get(key).fileResults.size()<1) {
				System.out.println(key);
				displayResults(key);
			}
		}
    }
	
	private static void displayResults(String key) {
		for(String file : translations.get(key).fileResults.keySet()) {
			System.out.println("\t"+file);
			for(Result res : translations.get(key).fileResults.get(file)) {
				System.out.println("\t\t"+res);
			}
		}
		for(String value : translations.get(key).values) {
			System.out.println("\t*** "+value);
		}
	}
	
	private static void readBundle(String path) {
		try {
			String key;
			String value;
			for(String line : FileUtils.readLines(new File(path))) {
				int indexOf = line.indexOf("=");
				if(indexOf>0) {
					key=line.substring(0, indexOf);
					value=line.substring(indexOf+1, line.length());
					
					KeyResults keyArgs;
					if(translations.containsKey(key)) {
						keyArgs = translations.get(key);
					} else {
						keyArgs = new KeyResults();
					}
					keyArgs.values.add(value);
					translations.put(key, keyArgs);
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(BundleScanner.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private static void browseFS(File path) {
        if (path.isDirectory()) {
			File[] files = FileSystem.replaceHome(path)
				.listFiles((File dir, String name) -> 
						(new File(dir, name).isDirectory()
								|| name.endsWith(".java")
								|| name.endsWith(".form") ));
            if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						browseFS(file);
					}
					else {
						filesToSearch.add(file.getAbsolutePath());
					}
				}
			} 
        }
	}
	
	public static void searchInFile(String filePath) throws IOException
    {
        BufferedReader br = null;
		int lineID = 0;
        try
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line;
            while ((line = br.readLine()) != null)
            {
				lineID++;
				for(String key : translations.keySet()) {
					if (line.contains(key))
					{
						KeyResults keyArgs = translations.get(key);
						keyArgs.add(filePath, new Result(lineID, line.trim()));
						translations.put(key, keyArgs);
					}
				}
            }
        }
        finally
        {
            try
            {
                if (br != null)
                    br.close();
            }
            catch (Exception e)
            {
                System.err.println("Exception while closing bufferedreader " + e.toString());
            }
        }
    }
}
