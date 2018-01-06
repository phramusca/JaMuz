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
package jamuz.utils;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Swing {

    /**
     * Enable/diable all components in given container (panel, ...) and
     * sub-containers too recursively
     *
     * @param container
     * @param enable
     */
    public static void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container) component, enable);
            }
        }
    }

	/**
	 * Select a folder (open a folder chooser GUI)
	 * @param defaultFolder 
	 * @param title 
	 * @return
	 */
	public static String selectFolder(String defaultFolder, String title) {
		JFileChooser fc = new JFileChooser(defaultFolder);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle(title);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selFile = fc.getSelectedFile();
			return selFile.getAbsolutePath();
        } else {
			return "";  //NOI18N
        }
	}

	/**
	 * Select tab with given title
	 *
	 * @param tabbedPane
	 * @param title
	 */
	public static void selectTab(JTabbedPane tabbedPane, String title) {
		int checkTabIndex = tabbedPane.indexOfTab(title); //NOI18N
		tabbedPane.setSelectedIndex(checkTabIndex);
	}
	
	/**
	 *
	 */
	public enum FileType {

		/**
		 *
		 */
		JPG,

		/**
		 *
		 */
		SQLITE
	}
	
	/**
	 * Select a folder (open a folder chooser GUI)
	 * @param defaultFile 
	 * @return
	 */
	public static String selectile(String defaultFile) {
		return selectile(defaultFile, new ArrayList<>(), "Select file");
	}
	
	/**
	 *
	 * @param defaultFile
	 * @param fileType
	 * @param title
	 * @return
	 */
	public static String selectile(String defaultFile, FileType fileType, String title) {
		List<Swing.FileType> fileTypes = new ArrayList<>();
		fileTypes.add(fileType);
		return selectile(defaultFile, fileTypes, title);
	}
	
	/**
	 * Select a folder (open a folder chooser GUI)
	 * @param defaultFile 
	 * @param fileTypes 
	 * @param title 
	 * @return
	 */
	public static String selectile(String defaultFile, List<FileType> fileTypes, String title) {
		JFileChooser fc = new JFileChooser(defaultFile);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle(title);
		if(fileTypes!=null) {
			for(FileType type : fileTypes) {
				FileFilter filter;
				switch(type) {
					case JPG:
						filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
						fc.addChoosableFileFilter(filter);
						fc.setFileFilter(filter); //TODO: Select only first or last or add method parameter
						break;
					case SQLITE:
						filter = new FileNameExtensionFilter("SQLITE file", "db", "sqlite");
						fc.addChoosableFileFilter(filter);
						fc.setFileFilter(filter);
						break;
				}
			}
//			fc.setAcceptAllFileFilterUsed(false); //TODO: method parameter
			
		}
		
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selFile = fc.getSelectedFile();
			return selFile.getAbsolutePath();
        } else {
			return "";  //NOI18N
        }
	}
}
