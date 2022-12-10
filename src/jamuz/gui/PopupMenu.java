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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
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
	private final PopupMenuListener popupMenuListener;

	/**
	 *
	 * @param jPopupMenu1
	 * @param jTableSelect
	 * @param tableModel
	 * @param fileInfoList
	 * @param mplayer
	 * @param popupMenuListener
	 */
	public PopupMenu(JPopupMenu jPopupMenu1, JTable jTableSelect, 
			TableModel tableModel, ArrayList<FileInfoInt> fileInfoList, 
			Mplayer mplayer, PopupMenuListener popupMenuListener) {
		
		this.jPopupMenu1 = jPopupMenu1;
		this.jTableSelect = jTableSelect;
		this.tableModel = tableModel;
		this.fileInfoList = fileInfoList;
		this.mplayer = mplayer;
		setup();
		this.popupMenuListener = popupMenuListener;
	}
		
	private void setup() {
		
		JMenu menuTrack = new JMenu(Inter.get("Label.Track"));
		menuTrack.add(new JMenuItem(new AbstractAction(Inter.get("MainGUI.jButtonSelectQueue.text")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: Bug when adding to queue. JList is not refreshed
				//Something's wrong b/w model and JList
				FileInfoInt selected = getSelected();
				if(selected!=null) {
					PanelMain.addToQueue(selected, Jamuz.getDb().getRootPath());
				}
			}
		}));
		if(mplayer!=null) {
			menuTrack.add(new JMenuItem(new AbstractAction("Preview") {
				@Override
				public void actionPerformed(ActionEvent e) {
					FileInfoInt selected = getSelected();
					if(selected!=null) {
						//FIXME Z Uncomment 2 below lines
						//	mplayer.setAudioCard((Mplayer.AudioCard)jComboBoxSoundCard.getSelectedItem());
						//	jLabelPreviewDisplay.setText(fileInfoInt.getTrackNo()+" "+fileInfoInt.getTitle());
						if(mplayer!=null) {
							mplayer.play(selected.getFullPath().getAbsolutePath(), false);
						}
					}
				}
			}));
		}
		menuTrack.add(new JMenuItem(new AbstractAction(Inter.get("Label.Delete")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = jTableSelect.getSelectedRow(); 		
				if(selectedRow>=0) {
					int selectedIndex = jTableSelect.convertRowIndexToModel(selectedRow); 
					FileInfoInt selected = fileInfoList.get(selectedIndex);
					int n = JOptionPane.showConfirmDialog( 
						jTableSelect, "Are you sure you want to delete \""+selected.getRelativeFullPath()+"\" FROM FILESYSTEM ?", //NOI18N 
						Inter.get("Label.Confirm"), //NOI18N 
						JOptionPane.YES_NO_OPTION); 
					if (n == JOptionPane.YES_OPTION) { 					
						if(delete(selected, selectedIndex)) {
							fileInfoList.remove(selectedIndex);
						}
					}
				}
			}
		}));
		menuTrack.add(new JMenuItem(new AbstractAction("AcoustID") {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileInfoInt selected = getSelected();
				if(selected!=null) {
					//FIXME Z Do not block GUI.
					Results analyzed = AcoustID.analyze(selected.getFullPath().getAbsolutePath(), Jamuz.getKeys().get("AcoustId"));
					AcoustIdResult best = analyzed.getBest();
					String msg = best==null?"Not found":best.getScore()+": \""+best.getTitle()+"\" by "+best.getArtist();
					Popup.info(msg);
				}
			}
		}));
		jPopupMenu1.add(menuTrack);

		JMenu menuAlbum = new JMenu(Inter.get("Tag.Album"));
		menuAlbum.add(new JMenuItem(new AbstractAction(Inter.get("MainGUI.jButtonSelectQueue.text")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileInfoInt selected = getSelected();
				if(selected!=null) {
					ArrayList<FileInfoInt> albumFiles = new ArrayList<>();
					if(Jamuz.getDb().getFiles(albumFiles, selected.getIdPath())) {
						PanelMain.getQueueModel().clear();
						for(FileInfoInt myFileInfo : albumFiles) {
							PanelMain.addToQueue(myFileInfo, Jamuz.getDb().getRootPath()); 	
						}
					}
					
				}
			}
		}));
		menuAlbum.add(new JMenuItem(new AbstractAction(Inter.get("Label.Check")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileInfoInt selected = getSelected();
				if(selected!=null) {
					PanelCheck.check(jTableSelect, selected.getIdPath());
				}
			}
		}));
		menuAlbum.add(new JMenuItem(new AbstractAction(Inter.get("Label.Delete")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileInfoInt selected = getSelected();
				if(selected!=null) {
					ArrayList<FileInfoInt> albumFiles = new ArrayList<>();
					if(Jamuz.getDb().getFiles(albumFiles, selected.getIdPath())) {
						delete(albumFiles, false); //FIXME Z Add a progress bar
					}
				}
			}
		}));
		jPopupMenu1.add(menuAlbum);
		
		JMenu menuAll = new JMenu(Inter.get("Label.All"));
		menuAll.add(new JMenuItem(new AbstractAction(Inter.get("MainGUI.jButtonSelectQueue.text")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileInfoInt selected = getSelected();
				if(selected!=null) {
					PanelMain.getQueueModel().clear();
					for(FileInfoInt myFileInfo : fileInfoList) {
						PanelMain.addToQueue(myFileInfo, Jamuz.getDb().getRootPath()); 	
					}
				}
			}
		}));
		menuAll.add(new JMenuItem(new AbstractAction(Inter.get("Label.Delete")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete(fileInfoList, true);
			}
		}));
		jPopupMenu1.add(menuAll);
	
		//Add links menu items
        File f = Jamuz.getFile("AudioLinks.txt", "data");
        if(f.exists()) {
            JMenu menuLinks = new JMenu(Inter.get("Label.Links")); //NOI18N
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get(f.getAbsolutePath()), Charset.defaultCharset());
				List<String> urls = new ArrayList<>();
				lines.stream()
						.filter(line -> (line.contains("|")))
						.map(line -> line.split("\\|"))
						.forEachOrdered(splitted -> {
							urls.add(splitted[1]);
							menuLinks.add(new JMenuItem(new AbstractAction(splitted[0]) {
								@Override
								public void actionPerformed(ActionEvent e) {
									FileInfoInt selected = getSelected();
									if(selected!=null) {
										Desktop.openBrowser(splitted[1].replaceAll("<album>",
													selected.getAlbumArtist().concat(" ")
															.concat(selected.getAlbum())));
									}
								}
							}));
						});
				if(urls.size()>1) {
					menuLinks.add(new JMenuItem(new AbstractAction("ALL LINKS") {
								@Override
								public void actionPerformed(ActionEvent e) {
									FileInfoInt selected = getSelected();
									if(selected!=null) {
										urls.forEach(url -> {
											Desktop.openBrowser(url.replaceAll("<album>",
													selected.getAlbumArtist().concat(" ")
															.concat(selected.getAlbum())));
										});
									}
								}
							}));
				}
                jPopupMenu1.add(menuLinks);
            } catch (IOException ex) {
                Jamuz.getLogger().log(Level.SEVERE, null, ex);
            }
        }

		jTableSelect.addMouseListener(new PopupListener());
	}
	
	private FileInfoInt getSelected() {
		int selectedRow = jTableSelect.getSelectedRow(); 		
		FileInfoInt selected = null;
		if(selectedRow>=0) {
			int selectedIndex = jTableSelect.convertRowIndexToModel(selectedRow); 
			selected = fileInfoList.get(selectedIndex);
		}
		return selected;
	}
	
	private boolean delete(FileInfoInt selected, int selectedIndex) {
		File currentFile = selected.getFullPath();
		if(currentFile.exists() 
				&& selected.getFullPath().delete()
				&& Jamuz.getDb().deleteFile(selected.getIdFile())) {
			if(selectedIndex>=0) {
				tableModel.removeRow(selectedIndex);
			}
			return true;
		}
		return false;
	}
	
	private void delete(List<FileInfoInt> fileInfoList, boolean removeFromTableModel) {
		int n = JOptionPane.showConfirmDialog( 
			jTableSelect, "Are you sure that you want to DELETE ALL "+ fileInfoList.size()+" album files FROM FILESYSTEM?", //NOI18N 
			Inter.get("Label.Confirm"), //NOI18N 
			JOptionPane.YES_NO_OPTION); 
		if (n == JOptionPane.YES_OPTION) { 
			n = JOptionPane.showConfirmDialog( 
			jTableSelect, "Are you REALLY SURE that you want to DELETE ALL "+ fileInfoList.size()+" album files FROM FILESYSTEM?", //NOI18N 
			Inter.get("Label.Confirm"), //NOI18N 
			JOptionPane.YES_NO_OPTION); 
			if (n == JOptionPane.YES_OPTION) {
				enableMenu=false;
				if(popupMenuListener.deleteStarted()) {
					new Thread(() -> {
						for (Iterator<FileInfoInt> it = fileInfoList.iterator(); it.hasNext();) {
							FileInfoInt file = it.next();
							if(delete(file, removeFromTableModel?0:-1)) {
								it.remove();
							}
						}
						popupMenuListener.deleteEnded();
						if(!removeFromTableModel) {
							popupMenuListener.refresh();
						}
						enableMenu=true;
					}).start();
				}
			}
		}
	}

	private boolean enableMenu=true;
	void setEnable(boolean enable) {
		enableMenu=enable;
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
			if (e.isPopupTrigger() && enableMenu) {
				jPopupMenu1.show(e.getComponent(),
						   e.getX(), e.getY());
			}
		}
	}
	
	private void selectOnRightClick(java.awt.event.MouseEvent evt) {                                          
        if (SwingUtilities.isRightMouseButton( evt ) && enableMenu) {
            Point p = evt.getPoint();
            int rowNumber = jTableSelect.rowAtPoint( p );
            ListSelectionModel model = jTableSelect.getSelectionModel();
            model.setSelectionInterval( rowNumber, rowNumber );
        }
        //TODO: Use a better listener (onChange) to handle selections using keyboard !
        //Example: http://www.developpez.net/forums/d1141644/java/interfaces-graphiques-java/awt-swing/jtable-lancer-traitement-moment-selection-ligne/
    }
}