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
import jamuz.acoustid.AcoustID;
import jamuz.acoustid.AcoustIdResult;
import jamuz.acoustid.Results;
import jamuz.gui.swing.TableModel;
import jamuz.player.Mplayer;
import jamuz.process.check.PanelCheck;
import jamuz.utils.Desktop;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

//TODO: Generalize, and replace PopupListener Or even better extend JPopupMenu

/**
 *
 * @author raph
 */
public class PopupMenu {
	
	JPopupMenu jPopupMenu1;
	JTable jTableSelect;
	TableModel tableModel;
	ArrayList<FileInfoInt> fileInfoList;
	Mplayer mplayer;
	ActionListener menuListener;

	public PopupMenu(JPopupMenu jPopupMenu1, JTable jTableSelect, TableModel tableModel, ArrayList<FileInfoInt> fileInfoList, Mplayer mplayer) {
		this.jPopupMenu1 = jPopupMenu1;
		this.jTableSelect = jTableSelect;
		this.tableModel = tableModel;
		this.fileInfoList = fileInfoList;
		this.mplayer = mplayer;
		setup();
	}
		
	private void setup() {
		
        menuListener = (ActionEvent e) -> {
			int selectedRow = jTableSelect.getSelectedRow(); 		
			if(selectedRow>=0) { 	
				//convert to model index (in case of sortable model) 		
				final int selectedIndex = jTableSelect.convertRowIndexToModel(selectedRow); 
				final FileInfoInt selected = fileInfoList.get(selectedIndex);
				JMenuItem source = (JMenuItem)(e.getSource());
				String sourceTxt=source.getText();
				if(sourceTxt.equals(Inter.get("Button.Edit"))) { //NOI18N
					PanelMain.editLocation(Jamuz.getDb().getRootPath()+selected.getRelativeFullPath());
				}
				else if(sourceTxt.equals(Inter.get("MainGUI.jButtonSelectQueue.text"))) { //NOI18N
					//TODO: Bug when adding to queue. JList is not refreshed
					//Something's wrong b/w model and JList
					PanelMain.addToQueue(selected, Jamuz.getDb().getRootPath());
				}
				else if(sourceTxt.equals(Inter.get("MainGUI.jButtonSelectQueueAll.text"))) { //NOI18N
					PanelMain.getQueueModel().clear();
					for(FileInfoInt myFileInfo : fileInfoList) {
						PanelMain.addToQueue(myFileInfo, Jamuz.getDb().getRootPath()); 	
					}
				}
				else if(sourceTxt.equals("Preview")) { //NOI18N
					//FIXME: Uncomment 2 below lines
					//	mplayer.setAudioCard((Mplayer.AudioCard)jComboBoxSoundCard.getSelectedItem());
					//	jLabelPreviewDisplay.setText(fileInfoInt.getTrackNo()+" "+fileInfoInt.getTitle());
					if(mplayer!=null) {
						mplayer.play(selected.getFullPath().getAbsolutePath(), false);
					}
				}
				else if(sourceTxt.equals(Inter.get("Label.Check"))) { //NOI18N
					PanelCheck.check(selected.getIdPath());	
				} else if(sourceTxt.equals("Delete Selected")) {
					int n = JOptionPane.showConfirmDialog( 
                        null, "Are you sure you want to delete \""+selected.getRelativeFullPath()+"\" FROM FILESYSTEM ?", //NOI18N 
                        Inter.get("Label.Confirm"), //NOI18N 
                        JOptionPane.YES_NO_OPTION); 
					if (n == JOptionPane.YES_OPTION) { 					
						delete(selected, selectedIndex);
					}
				} else if(sourceTxt.equals("Delete All")) {
					int n = JOptionPane.showConfirmDialog( 
                        null, "Are you sure that you want to delete ALL playlist files FROM FILESYSTEM ?", //NOI18N 
                        Inter.get("Label.Confirm"), //NOI18N 
                        JOptionPane.YES_NO_OPTION); 
					if (n == JOptionPane.YES_OPTION) { 
						n = JOptionPane.showConfirmDialog( 
						null, "Are you sure you REALLY SURE that you want to delete ALL playlist files FROM FILESYSTEM ?", //NOI18N 
						Inter.get("Label.Confirm"), //NOI18N 
						JOptionPane.YES_NO_OPTION); 
						if (n == JOptionPane.YES_OPTION) { 
							//FIXME !!! Disable buttons while deleting !!! OR IT CAN MESS EVERYTHING UP
							new Thread(() -> {
								for(FileInfoInt file : fileInfoList) {
									delete(file, 0);
								}
							}).start();
						}
					}
				} else if(sourceTxt.equals("AcoustID")) { //NOI18N
					Results analyzed = AcoustID.analyze(selected.getFullPath().getAbsolutePath(), Jamuz.getKeys().get("AcoustId"));
					AcoustIdResult best = analyzed.getBest();
					String msg = best==null?"Not found":best.getScore()+": \""+best.getTitle()+"\" by "+best.getArtist();
					Popup.info(msg);
				}
				else {
					Popup.error(Inter.get("UNKNOWN MENU ITEM"));
				}
			}
		};
        
		addMenu(Inter.get("MainGUI.jButtonSelectQueue.text"));
		addMenu(Inter.get("MainGUI.jButtonSelectQueueAll.text"));
		if(mplayer!=null) {
			addMenu("Preview");
		}
		addMenu(Inter.get("Label.Check"));
		addMenu("AcoustID");
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
		
		JMenu menuDelete = new JMenu("Delete"); //NOI18N
		JMenuItem menuItem1 = new JMenuItem("Delete Selected");
		menuItem1.addActionListener(menuListener);
		menuDelete.add(menuItem1);
		menuItem1 = new JMenuItem("Delete All");
		menuItem1.addActionListener(menuListener);
		menuDelete.add(menuItem1);
		jPopupMenu1.add(menuDelete);
		
		addMenu(Inter.get("Button.Edit")); //TODO: Add " (external)" to menu name	
		jTableSelect.addMouseListener(new PopupListener() {
			
        });
	}
	
	private void delete(FileInfoInt selected, int selectedIndex) {
		File currentFile = selected.getFullPath();
		if(currentFile.exists() 
				&& selected.getFullPath().delete()
				&& Jamuz.getDb().setFileDeleted(selected.getIdFile())) {
			tableModel.removeRow(selectedIndex);
			fileInfoList.remove(selectedIndex);
		}
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
	
	private void addMenu(String title) {
		JMenuItem  menuItem = new JMenuItem(title); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
	}
	
	class PopupListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			selectOnRightClick(e);
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				jPopupMenu1.show(e.getComponent(),
						   e.getX(), e.getY());
			}
		}
	}
	
	private void selectOnRightClick(java.awt.event.MouseEvent evt) {                                          
		// If Right mouse click, select the line under mouse
        if ( SwingUtilities.isRightMouseButton( evt ) )
        {
            Point p = evt.getPoint();
            int rowNumber = jTableSelect.rowAtPoint( p );
            ListSelectionModel model = jTableSelect.getSelectionModel();
            model.setSelectionInterval( rowNumber, rowNumber );
        }
        //TODO: Use a better listener (onChange) to handle selections using keyboard !
        //Example: http://www.developpez.net/forums/d1141644/java/interfaces-graphiques-java/awt-swing/jtable-lancer-traitement-moment-selection-ligne/
    } 
	
	

	
}
