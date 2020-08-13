/*
 * Copyright (C) 2020 raph
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
import jamuz.gui.swing.PopupListener;
import jamuz.player.Mplayer;
import jamuz.process.check.PanelCheck;
import jamuz.utils.Desktop;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 *
 * @author raph
 */
public class PopupMenu {
	
	JPopupMenu jPopupMenu1;
	JTable jTableSelect;
	ArrayList<FileInfoInt> fileInfoList;
	Mplayer mplayer;

	public PopupMenu(JPopupMenu jPopupMenu1, JTable jTableSelect, ArrayList<FileInfoInt> fileInfoList, Mplayer mplayer) {
		this.jPopupMenu1 = jPopupMenu1;
		this.jTableSelect = jTableSelect;
		this.fileInfoList = fileInfoList;
		this.mplayer = mplayer;
		setup();
	}
		
	private void setup() {
		//Menu listener
        ActionListener menuListener = (ActionEvent e) -> {
			JMenuItem source = (JMenuItem)(e.getSource());
			String sourceTxt=source.getText();
			if(sourceTxt.equals(Inter.get("Button.Edit"))) { //NOI18N
				menuEdit();
			}
			else if(sourceTxt.equals(Inter.get("MainGUI.jButtonSelectQueue.text"))) { //NOI18N
				menuQueue();
			}
			else if(sourceTxt.equals(Inter.get("MainGUI.jButtonSelectQueueAll.text"))) { //NOI18N
				menuQueueAll();
			}
			else if(sourceTxt.equals("Preview")) { //NOI18N
				menuPreview();
			}
			else if(sourceTxt.equals(Inter.get("Label.Check"))) { //NOI18N
				menuCheck();
			}
			else {
				Popup.error(Inter.get("UNKNOWN MENU ITEM"));
			}
		};
        
        JMenuItem  menuItem = new JMenuItem(Inter.get("MainGUI.jButtonSelectQueue.text")); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
        menuItem = new JMenuItem(Inter.get("MainGUI.jButtonSelectQueueAll.text")); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
		if(mplayer!=null) {
			menuItem = new JMenuItem("Preview"); //NOI18N
			menuItem.addActionListener(menuListener);
			jPopupMenu1.add(menuItem);
		}
        menuItem = new JMenuItem(Inter.get("Label.Check")); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
		//Add links menu items
        File f = Jamuz.getFile("AudioLinks.txt", "data");
        if(f.exists()) {
            JMenu menuLinks = new JMenu(Inter.get("Label.Links")); //NOI18N
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get(f.getAbsolutePath()), Charset.defaultCharset());
                for(String line : lines) {
                    if(line.contains("|")) {
                        String[] splitted = line.split("\\|");
                        JMenuItem menuItem1 = new JMenuItem(new PopupMenu.OpenUrlAction(splitted[0], splitted[1]));
                        menuLinks.add(menuItem1);
                    }
                }
                jPopupMenu1.add(menuLinks);
            } catch (IOException ex) {
                Jamuz.getLogger().log(Level.SEVERE, null, ex);
            }
        }
		//TODO: Add " (external)" to menu name
		menuItem = new JMenuItem(Inter.get("Button.Edit")); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
 
        //Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener(jPopupMenu1);
        jTableSelect.addMouseListener(popupListener);
	}
	
	class OpenUrlAction extends AbstractAction {
        
        private final String url;
        
        public OpenUrlAction(String text, String url) {
            super(text, null);
            this.url = url;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = jTableSelect.getSelectedRow(); 		
			if(selectedRow>=0) { 	
				//convert to model index (as sortable model) 		
				selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
				FileInfoInt myFileInfo = fileInfoList.get(selectedRow);

				Desktop.openBrowser(url.replaceAll("<album>", 
						myFileInfo.getAlbumArtist().concat(" ")
								.concat(myFileInfo.getAlbum())));			 		
			}
        }
    }
	
	private void menuQueue() {
        //Getting selected File 		
		int selectedRow = jTableSelect.getSelectedRow(); 			
		if(selectedRow>=0) { 	
			//convert to model index (as sortable model) 		
			selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
			FileInfoInt myFileInfo = fileInfoList.get(selectedRow); 	
            
            //TODO: Bug when adding to queue. JList is not refreshed
            //Something's wrong b/w model and JList
			PanelMain.addToQueue(myFileInfo, Jamuz.getDb().getRootPath()); 		
		}
    }
    
    private void menuQueueAll() {
        //Getting selected File 		
		int selectedRow = jTableSelect.getSelectedRow(); 			
		if(selectedRow>=0) {	
            PanelMain.getQueueModel().clear();
            for(FileInfoInt myFileInfo : fileInfoList) {
                PanelMain.addToQueue(myFileInfo, Jamuz.getDb().getRootPath()); 	
            }
		}
    }
    
    private void menuEdit() {
        //Getting selected File 		
		int selectedRow = jTableSelect.getSelectedRow(); 		
		if(selectedRow>=0) { 	
			//convert to model index (as sortable model) 		
			selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
			FileInfoInt myFileInfo = fileInfoList.get(selectedRow);
			PanelMain.editLocation(Jamuz.getDb().getRootPath()+myFileInfo.getRelativeFullPath());	  			 		
		}
    }
	
	private void menuPreview() {
		//Getting selected File 		
		int selectedRow = jTableSelect.getSelectedRow(); 			
		if(selectedRow>=0) { 	
			//convert to model index (as sortable model) 		
			selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
			FileInfoInt fileInfoInt = fileInfoList.get(selectedRow); 	
			//FIXME: Uncomment 2 below lines
//			mplayer.setAudioCard((Mplayer.AudioCard)jComboBoxSoundCard.getSelectedItem());
//			jLabelPreviewDisplay.setText(fileInfoInt.getTrackNo()+" "+fileInfoInt.getTitle());
			if(mplayer!=null) {
				mplayer.play(fileInfoInt.getFullPath().getAbsolutePath(), false);
			}
		}
	}
	
	 private void menuCheck() {
        int selectedRow = jTableSelect.getSelectedRow(); 		
		if(selectedRow>=0) { 	
			//convert to model index (as sortable model) 		
			selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
			FileInfoInt myFileInfo = fileInfoList.get(selectedRow);

            PanelCheck.check(myFileInfo.getIdPath());			 		
		}
    }
	
}
