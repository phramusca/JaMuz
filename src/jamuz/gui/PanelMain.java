/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.remote.PanelRemote;
import jamuz.process.check.PanelCheck;
import jamuz.process.merge.PanelMerge;
import jamuz.process.sync.PanelSync;
import jamuz.FileInfoInt;
import jamuz.IconBufferCover;
import jamuz.Jamuz;
import jamuz.gui.swing.ListElement;
import jamuz.Main;
import jamuz.Playlist;
import jamuz.gui.swing.ComboBoxRenderer;
import jamuz.utils.OS;
import jamuz.utils.Popup;
import jamuz.gui.swing.TableModel;
import jamuz.gui.swing.TableColumnModel;
import jamuz.gui.swing.ProgressBar;
import jamuz.utils.Inter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import jamuz.gui.swing.ButtonBrowseURL;
import jamuz.gui.swing.TableCellListener;
import jamuz.player.Mplayer;
import jamuz.utils.StringManager;
import jamuz.utils.Swing;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import jamuz.player.MPlaybackListener;
import jamuz.remote.ICallBackServer;

/**
 * Main JaMuz GUI class
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelMain extends javax.swing.JFrame {

    /**
     * Play queue list model
     */
    private static ListModelPlayerQueue queueModel;

	/**
	 *
	 * @return
	 */
	public static ListModelPlayerQueue getQueueModel() {
        return queueModel;
    }
    
//    private static final PlayerMP3 mp3Player = new PlayerMP3();
//    private static final PlayerFlac flacPlayer = new PlayerFlac();
	private static Mplayer MPLAYER;

	/**
	 *
	 */
	protected static DefaultComboBoxModel comboPlaylistsModel = new DefaultComboBoxModel();
    
    /**
     * genre combo values
     */
    protected static String[] comboGenre;

	/**
	 *
	 * @return
	 */
	public static String[] getComboGenre() {
        return comboGenre;
    }
    
    private static ImageIcon[] ratingIcon;
    /**
     * Progress bar for "Best Of" tab
     */
    protected static ProgressBar progressBestOf;

    private static final int[] BASIC_COLS = {0, 1, 2, 3, 4, 5, 12, 17};
    private static final int[] STATS_COLS = {16, 18, 19};
    private static final int[] FILE_COLS = {6, 7, 8, 9, 10};
    private static final int[] EXTRA_COLS = {11, 13, 14, 15, 20};
	private static final int[] RIGHTS_COLS = {21, 22};

    /**
     * Creates new form MainGUI
     */
    public PanelMain() {
		
        comboGenre = new String[1];
        comboGenre[0] = ""; //NOI18N
        initComponents();

        //Change tabs
        setTab("Label.Merge", "arrow_refresh");
        setTab("PanelMain.panelSync.TabConstraints.tabTitle", "arrow_turn_right");
        setTab("Label.Check", "search_plus");
        setTab("PanelMain.panelSelect.TabConstraints.tabTitle", "music");
        setTab("PanelMain.panelPlaylists.TabConstraints.tabTitle", "application_view_list");
        setTab("Label.Lyrics", "text");
        setTab("PanelMain.panelStats.TabConstraints.tabTitle", "statistics");
		setTab("PanelMain.panelRemote.TabConstraints.tabTitle", "android");
        setTab("Label.Options", "selected");
        setTab("PanelMain.panelVideo.TabConstraints.tabTitle", "movies");
		setTab("PanelMain.panelBook.TabConstraints.tabTitle", "book_open");

	//Left pane: player
        //Set queue model
        queueModel = new ListModelPlayerQueue();
        jListPlayerQueue.setModel(queueModel);
        playerInfo = new FramePlayerInfo(getTitle(), queueModel);
		
		fillGenreLists();
        //Empty the FileInfo labels
        jLabelTags.setText("-------------------------");
        jLabelPlayerTitle.setText("Welcome to");  //NOI18N
        jLabelPlayerAlbum.setText("Jamuz");  //NOI18N
        jLabelPlayerArtist.setText("---");  //NOI18N
        jLabelPlayerYear.setText("2014");  //NOI18N
		PanelCover coverImg = (PanelCover) jPanelPlayerCover;
		coverImg.setImage(null);
        //Set rating combobox renderer
        String[] imageNames = {"null", "1star", "2star", "3star", "4star", "5star"}; //NOI18N
        ratingIcon = new ImageIcon[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            ratingIcon[i] = createImageIcon("/jamuz/ressources/" + imageNames[i] + ".png"); //NOI18N
        }
        ComboBoxRenderer renderer = new ComboBoxRenderer(ratingIcon);
        renderer.setPreferredSize(new Dimension(80, 16));
        jComboBoxPlayerRating.setRenderer(renderer);

        jComboBoxPlayerGenre.setEnabled(false);

        jComboBoxPlaylist.setModel(comboPlaylistsModel);
                
		// Disable keyboard edits in the spinner
		JFormattedTextField tf = ((JSpinner.DefaultEditor) jSpinnerVolume.getEditor()).getTextField();
		tf.setEditable(false);
		tf.setBackground(Color.GRAY);
		
		MPLAYER = new Mplayer();
		
		MPlaybackListener mPlaybackListener = new MPlaybackListener() {
			@Override
			public void volumeChanged(float volume) {
				jSpinnerVolume.getModel().setValue(volume);
			}

			@Override
			public void playbackFinished() {
				next();
			}

			@Override
			public void positionChanged(int position, int length) {
				dispMP3progress(position);
			}
		};
		
		MPLAYER.addListener(mPlaybackListener);
		
		//Init tabs
		panelOptions.initExtended();
        panelSync.initExtended();
        panelMerge.initExtended();
        PanelCheck.setOptions(); //Needs to be static (for now at least)
        panelStats.initExtended();
        panelSelect.initExtended();
        panelPlaylists.initExtended();
		panelRemote.initExtended(new CallBackServer());
		
        setKeyBindings();		
    }
	
	class CallBackServer implements ICallBackServer {
		
		@Override
		public void received(String clientId, String msg) {
			if(msg.startsWith("sendCover")) {
				int maxWidth = Integer.parseInt(msg.substring("sendCover".length()));
				PanelRemote.sendCover(clientId, displayedFile, maxWidth);
			}
			else if(msg.startsWith("setPlaylist")) {
				setPlaylist(msg.substring("setPlaylist".length()));
			}
			else {
				switch(msg) { 
					//TODO: Say rating as an option 
					case "setRating1": setRating(1, false); break; 
					case "setRating2": setRating(2, false); break; 
					case "setRating3": setRating(3, false); break; 
					case "setRating4": setRating(4, false); break; 
					case "setRating5": setRating(5, false); break; 
					case "previousTrack": 
						pressButton(jButtonPlayerPrevious);  
						break; 
					case "nextTrack":  
						pressButton(jButtonPlayerNext);  
						break; 
					case "playTrack":  
						pressButton(jButtonPlayerPlay);  
						break; 
					case "clearTracks":  
						pressButton(jButtonPlayerClear);  
						break; 
					case "forward": forward(); break; 
					case "rewind": rewind(); break; 
					case "pullup": moveCursor(0); break; 
					case "volUp":  
					  jSpinnerVolume.setValue( 
						  (float)jSpinnerVolume.getValue()+5.0f);  
					  break; 
					case "volDown":  
					  jSpinnerVolume.setValue( 
						  (float)jSpinnerVolume.getValue()-5.0f);  
					  break; 
					case "sayRating": displayedFile.sayRating(true); break; 
					default: 
						Jamuz.getLogger().warning(msg); 
						break; 
				}
			}
		}

		@Override
		public void connectedRemote(String clientId) {
			sendPlaylists(clientId, jComboBoxPlaylist.getSelectedItem().toString());
			PanelRemote.send(displayedFile);
		}

//		@Override
//		public void disconnectedRemote(String login) {}
//		@Override
//		public void connectedSync(String login) {}
//		@Override
//		public void disconnectedSync(String login) {}
	}

	private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = PanelMain.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return new ImageIcon();
        }
    }

    /**
     * Move to next item in queue (when previous song playback finishes)
     */
    private static void next() {
        //update lastPlayed (now) and playCounter (+1)
		FileInfoInt file = queueModel.getPlayingSong().getFile();
		if(file.isFromLibrary()) {
			Jamuz.getDb().updateLastPlayedAndCounter(file);
			//FIXME LOW PLAYER Do not increase playCounter when moved back on queue and moved forward
			// especially if many back and forward
			//If not, increase playCounter too much
			// => Refer to JaMuzRemote:
//			if(queueHistoryIndex>=0 && (queueHistoryIndex+1)<queueHistory.size()) {
//				queueHistoryIndex++;
//				playHistory();
//			}
//			else {
//				//Update lastPlayed and playCounter
//				displayedTrack.setPlayCounter(displayedTrack.getPlayCounter()+1);
//				displayedTrack.setLastPlayed(new Date());
//				musicLibrary.updateTrack(displayedTrack);
//				playNextOrRandom();
//			}
		}
        //Moving next
        queueModel.next();
    }

    /**
     * Get rating icon (stars)
     *
     * @param rating
     * @return
     */
    public static ImageIcon getRatingIcon(int rating) {
        return ratingIcon[rating];
    }

    /**
     * Add a song to the audio player queue
     *
     * @param fileInfo
     * @param rootPath
     */
    public static void addToQueue(FileInfoInt fileInfo, String rootPath) {
        fileInfo.setRootPath(rootPath);
        ListElement albumElement = new ListElement(fileInfo.toStringQueue(), fileInfo);
        queueModel.add(albumElement);
    }
	
    private void setKeyBindings() {
        Action setRating1 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRating(1, true);
            }
        };
        Action setRating2 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRating(2, true);
            }
        };
        Action setRating3 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRating(3, true);
            }
        };
        Action setRating4 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRating(4, true);
            }
        };
        Action setRating5 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRating(5, true);
            }
        };
        Action previousTrack = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressButton(jButtonPlayerPrevious);
            }
        };
        Action nextTrack = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressButton(jButtonPlayerNext);
            }
        };
        Action playTrack = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressButton(jButtonPlayerPlay);
            }
        };
        Action clearTracks = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressButton(jButtonPlayerClear);
            }
        };
        
        Action forward = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forward();
            }
        };
        Action rewind = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rewind();
            }
        };
        Action pullup = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCursor(0);
            }
        };
        Action sayRating = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayedFile.sayRating(true);
            }
        };

        InputMap inputMap = this.jSplitPaneMain.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, Event.SHIFT_MASK+Event.ALT_MASK), "setRating1");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, Event.SHIFT_MASK+Event.ALT_MASK), "setRating2");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, Event.SHIFT_MASK+Event.ALT_MASK), "setRating3");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.SHIFT_MASK+Event.ALT_MASK), "setRating4");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.SHIFT_MASK+Event.ALT_MASK), "setRating5");
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, Event.SHIFT_MASK+Event.ALT_MASK), "previousTrack");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, Event.SHIFT_MASK+Event.ALT_MASK), "nextTrack");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.SHIFT_MASK+Event.ALT_MASK), "playTrack");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.SHIFT_MASK+Event.ALT_MASK), "clearTracks");
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.SHIFT_MASK+Event.ALT_MASK), "forward");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.SHIFT_MASK+Event.ALT_MASK), "rewind");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.SHIFT_MASK+Event.ALT_MASK), "pullup");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.SHIFT_MASK+Event.ALT_MASK), "sayRating");
        
        this.jSplitPaneMain.getActionMap().put("setRating1", setRating1);
        this.jSplitPaneMain.getActionMap().put("setRating2", setRating2);
        this.jSplitPaneMain.getActionMap().put("setRating3", setRating3);
        this.jSplitPaneMain.getActionMap().put("setRating4", setRating4);
        this.jSplitPaneMain.getActionMap().put("setRating5", setRating5);
        
        this.jSplitPaneMain.getActionMap().put("previousTrack", previousTrack);
        this.jSplitPaneMain.getActionMap().put("nextTrack", nextTrack);
        this.jSplitPaneMain.getActionMap().put("playTrack", playTrack);
        this.jSplitPaneMain.getActionMap().put("clearTracks", clearTracks);
        
        this.jSplitPaneMain.getActionMap().put("forward", forward);
        this.jSplitPaneMain.getActionMap().put("rewind", rewind);
        this.jSplitPaneMain.getActionMap().put("pullup", pullup);
        this.jSplitPaneMain.getActionMap().put("sayRating", sayRating);
    }
    
    private static void moveCursor(int value) {
        if(jSliderPlayerLength.isEnabled()) {
            if(value>=jSliderPlayerLength.getMinimum() && value<jSliderPlayerLength.getMaximum()) {
                jSliderPlayerLength.setValue(value);
            }
        }
    }
    
    private void pressButton(JButton button) {
        if(button.isEnabled()) {
            button.doClick();
        }
    }
    
    private void setPlaylist(String playlist) {
        int index=-1;
        int i=0;
        for(String playlistSource : getPlaylists()) {
            if(playlistSource.equals(playlist)) {
                index=i;
                break;
            }
            i++;
        }
        if(jComboBoxPlaylist.getSelectedItem().toString().equals(playlist)) refreshHiddenQueue=false;
        jComboBoxPlaylist.setSelectedIndex(index);
        refreshHiddenQueue=true;
    }
	
	private void setRating(int rating, boolean sayRated) {
		if(displayedFile.isFromLibrary()) {
            jComboBoxPlayerRating.setSelectedIndex(rating);
            displayedFile.sayRating(sayRated);
            PanelRemote.send(displayedFile);
        }
    }

    public static void fillGenreLists() {
		Jamuz.readGenres();
		
        //Fill genreDisplay combo boxes
		List<String> genreList = Jamuz.getGenres();
        comboGenre = new String[genreList.size() + 1];
        comboGenre[0] = Inter.get("Label.SelectOne");  //NOI18N

        jComboBoxPlayerGenre.setEnabled(false);
        jComboBoxPlayerGenre.removeAllItems();
        jComboBoxPlayerGenre.addItem(Inter.get("Label.SelectOne"));  //NOI18N
        int i = 1;
        for (String myGenre : genreList) {
            jComboBoxPlayerGenre.addItem(myGenre);
            comboGenre[i] = myGenre;
            i++;
        }
        jComboBoxPlayerGenre.setEnabled(displayedFile.isFromLibrary());

		displayFileInfo();
    }


    /**
     * initialize a "select style" table (select and playlist tabs)
     *
     * @param tableModel
     * @param jTable
     * @param tableColumnModel
     */
    public static void initSelectTable(TableModel tableModel, JTable jTable, TableColumnModel tableColumnModel) {

        //Set table model
        String[] columnNames = {Inter.get("Tag.Artist"), Inter.get("Tag.Album"), //NOI18N
			Inter.get("Tag.TrackNo"), Inter.get("Tag.Title"), //NOI18N
			Inter.get("Tag.Genre"), Inter.get("Tag.Year"), //NOI18N
			Inter.get("Tag.BitRate"), Inter.get("Label.File"), //NOI18N
			Inter.get("Tag.Length"), Inter.get("Tag.Format"), //NOI18N
			Inter.get("Tag.Size"), Inter.get("Tag.BPM"), Inter.get("Tag.AlbumArtist"), //NOI18N
			Inter.get("Tag.Comment"), Inter.get("Tag.DiscNo"), Inter.get("Tag.Cover"), //NOI18N
			Inter.get("Stat.PlayCounter"), Inter.get("Stat.Rating"), //NOI18N
			Inter.get("Stat.Added"), Inter.get("Stat.LastPlayed"), //NOI18N
			Inter.get("Stat.Checked"), Inter.get("Label.BestOf.Ownership"), ""};  //NOI18N
        Object[][] data = {
            {"Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default", //NOI18N
                "Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default", //NOI18N
                "Default", "Default", "Default", "Default", "Button"} //NOI18N
        };
        tableModel.setModel(columnNames, data);

        //Assigning XTableColumnModel to allow show/hide columns
        jTable.setColumnModel(tableColumnModel);

		//Adding columns from model. Cannot be done automatically on properties
        // as done, in initComponents, before setColumnModel which removes the columns ...
        jTable.createDefaultColumnsFromModel();

        TableColumn column;
        //Set "Artist" column width
        column = jTable.getColumnModel().getColumn(0);
        column.setMinWidth(50);
        column.setPreferredWidth(100);
        //Set Album column width
        column = jTable.getColumnModel().getColumn(1);
        column.setMinWidth(50);
        column.setPreferredWidth(100);
        //Set Track # column width
        column = jTable.getColumnModel().getColumn(2);
        column.setMinWidth(55);
        column.setMaxWidth(55);
        //Set Title column width
        column = jTable.getColumnModel().getColumn(3);
        column.setMinWidth(50);
        column.setPreferredWidth(10);
        //Set Genre column width
        column = jTable.getColumnModel().getColumn(4);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set Year column width
        column = jTable.getColumnModel().getColumn(5);
        column.setMinWidth(55);
        column.setMaxWidth(55);
        //Set BitRate column width
        column = jTable.getColumnModel().getColumn(6);
        column.setMinWidth(55);
        column.setMaxWidth(55);
        //Set File column width
        column = jTable.getColumnModel().getColumn(7);
        column.setPreferredWidth(10);
		//"Length", "Format", "SBPMDisplay", "BPM", "Album Artist", "Comment", "Disc #"
        //Set Length column width
        column = jTable.getColumnModel().getColumn(8);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set Format column width
        column = jTable.getColumnModel().getColumn(9);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set Size column width
        column = jTable.getColumnModel().getColumn(10);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set BPM column width
        column = jTable.getColumnModel().getColumn(11);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set "Album Artist" column width
        column = jTable.getColumnModel().getColumn(12);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set Comment column width
        column = jTable.getColumnModel().getColumn(13);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set "Disc #" column width
        column = jTable.getColumnModel().getColumn(14);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);

		//"Cover", "Play counter", "Rating", "Added", "Last Played"};
        //Set "Cover" column width
        column = jTable.getColumnModel().getColumn(15);
        column.setMinWidth(IconBufferCover.getCoverIconSize());
        column.setMaxWidth(IconBufferCover.getCoverIconSize());

        //Set "Play counter" column width
        column = jTable.getColumnModel().getColumn(16);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set Rating column width
        column = jTable.getColumnModel().getColumn(17);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set Added column width
        column = jTable.getColumnModel().getColumn(18);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);
        //Set "Last Played" column width
        column = jTable.getColumnModel().getColumn(19);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        column.setPreferredWidth(100);

        //TODO: Move CopyRight combobox to album folder as it is a path attribute
        //TODO: Create an Amazon button in album list too, and change the one in this table 
		//for searching MP3 single
        //For the 2 above, this means, changing album jlist 
		// (which has a special way of filling) to jtable !!
        //+managing displaying columns or not (copyright and amazon buttons have to be optionnal)
        
        // 21: Combobox
        column = jTable.getColumnModel().getColumn(21);
        final JComboBox comboBox = new JComboBox(PanelSelect.comboCopyRights);
        column.setCellEditor(new DefaultCellEditor(comboBox));
        //set its width
        column.setMinWidth(130);
        column.setMaxWidth(200);
        column.setPreferredWidth(130);

		Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                if (tcl.getColumn() == 21) { //ComboBox is here
                    int copyRight = Arrays.<String>asList(PanelSelect.comboCopyRights).indexOf(tcl.getNewValue());
                    FileInfoInt myFileInfo = PanelSelect.getFileInfoList().get(tcl.getRow());
                    Jamuz.getDb().updateCopyRight(myFileInfo.getIdPath(), copyRight);
                    PanelSelect.refreshTable();
                }
            }
        };
        TableCellListener tcl = new TableCellListener(jTable, action);
		
        // 22: Button
        column = jTable.getColumnModel().getColumn(22);
        column.setMinWidth(50);
        column.setMaxWidth(200);
        JButton button = new JButton("Amazon"); //NOI18N
        column.setCellRenderer((JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column1) -> button);
        column.setCellEditor(new ButtonBrowseURL());
        
        //Combobox and button needs to be editable to work
        tableModel.setEditable(new Integer[] {21, 22});
        
        //Display defaults columns (only basic by default)
        setBasicVisible(tableColumnModel, true);
        setExtraVisible(tableColumnModel, false);
        setStatsVisible(tableColumnModel, false);
        setFileVisible(tableColumnModel, false);
		setRightsVisible(tableColumnModel, false);
    }

//    private static JButton button;

    /**
     * Set options
     *
     * @return
     */
    public static boolean setOptions() {
        if (!Jamuz.getMachine().read()) {
            return false;
        }
        //TODO OPTIONS Use listeners to address the following (and more)
		//Rationalize to avoid potential errors
        //(especially when called from a process, this would not do the trick, 
		//need a way to bypass process check)
        PanelMerge.setOptions();
        PanelCheck.setOptions();
        PanelSync.setOptions();
        return true;
    }

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPaneMain = new javax.swing.JSplitPane();
        jTabbedPane = new javax.swing.JTabbedPane();
        panelSelect = new jamuz.gui.PanelSelect();
        panelMerge = new jamuz.process.merge.PanelMerge();
        panelSync = new jamuz.process.sync.PanelSync();
        panelPlaylists = new jamuz.gui.PanelPlaylists();
        panelLyrics = new jamuz.gui.PanelLyrics();
        panelStats = new jamuz.gui.PanelStats();
        panelRemote = new jamuz.remote.PanelRemote();
        panelOptions = new jamuz.gui.PanelOptions();
        panelVideo = new jamuz.process.video.PanelVideo();
        panelBook = new jamuz.process.book.PanelBook();
        jPanelPlayer = new javax.swing.JPanel();
        jLabelPlayerTitle = new javax.swing.JLabel();
        jLabelPlayerAlbum = new javax.swing.JLabel();
        jLabelPlayerArtist = new javax.swing.JLabel();
        jScrollPanePlayerQueue = new javax.swing.JScrollPane();
        jListPlayerQueue = new javax.swing.JList();
        jButtonPlayerClear = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jSliderPlayerLength = new javax.swing.JSlider();
        jLabelPlayerTimeTotal = new javax.swing.JLabel();
        jButtonPlayerPrevious = new javax.swing.JButton();
        jButtonPlayerPlay = new javax.swing.JButton();
        jButtonPlayerNext = new javax.swing.JButton();
        jSpinnerVolume = new javax.swing.JSpinner();
        jToggleButtonPlayerInfo = new javax.swing.JToggleButton();
        jPanelPlayerCoverContainer = new javax.swing.JPanel();
        jPanelPlayerCover = new jamuz.gui.PanelCover();
        jPanel2 = new javax.swing.JPanel();
        jLabelPlayerYear = new javax.swing.JLabel();
        jComboBoxPlayerGenre = new javax.swing.JComboBox();
        jComboBoxPlayerRating = new javax.swing.JComboBox();
        jLabelTags = new javax.swing.JLabel();
        jButtonTags = new javax.swing.JButton();
        jButtonCheckUp = new javax.swing.JButton();
        jButtonCheckDown = new javax.swing.JButton();
        jComboBoxPlaylist = new javax.swing.JComboBox();
        jButtonRefreshHiddenQueue = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JaMuz"); // NOI18N
        setExtendedState(1);

        jSplitPaneMain.setOneTouchExpandable(true);

        jTabbedPane.setMinimumSize(new java.awt.Dimension(0, 0));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jTabbedPane.addTab(bundle.getString("PanelMain.panelSelect.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/music.png")), panelSelect); // NOI18N
        jTabbedPane.addTab(bundle.getString("Label.Merge"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_refresh.png")), panelMerge); // NOI18N
        jTabbedPane.addTab(bundle.getString("PanelMain.panelSync.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_turn_right.png")), panelSync); // NOI18N
        jTabbedPane.addTab(bundle.getString("Label.Check"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/search_plus.png")), panelCheck); // NOI18N
        jTabbedPane.addTab(Inter.get("PanelMain.panelPlaylists.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_view_list.png")), panelPlaylists); // NOI18N
        jTabbedPane.addTab(bundle.getString("Label.Lyrics"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/text.png")), panelLyrics); // NOI18N
        jTabbedPane.addTab(bundle.getString("PanelMain.panelStats.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/statistics.png")), panelStats); // NOI18N
        jTabbedPane.addTab(Inter.get("PanelMain.panelRemote.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/android.png")), panelRemote); // NOI18N
        jTabbedPane.addTab(Inter.get("Label.Options"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/selected.png")), panelOptions); // NOI18N
        jTabbedPane.addTab(Inter.get("PanelMain.panelVideo.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/movies.png")), panelVideo); // NOI18N
        jTabbedPane.addTab(Inter.get("PanelMain.panelBook.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/book_open.png")), panelBook); // NOI18N

        jSplitPaneMain.setRightComponent(jTabbedPane);

        jLabelPlayerTitle.setFont(new java.awt.Font("DejaVu Sans", 1, 15)); // NOI18N
        jLabelPlayerTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPlayerTitle.setText("Track Title"); // NOI18N

        jLabelPlayerAlbum.setFont(new java.awt.Font("DejaVu Sans", 2, 15)); // NOI18N
        jLabelPlayerAlbum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPlayerAlbum.setText("Track Album"); // NOI18N

        jLabelPlayerArtist.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPlayerArtist.setText("Track Artist"); // NOI18N

        jListPlayerQueue.setModel(new DefaultListModel());
        jListPlayerQueue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListPlayerQueueMouseClicked(evt);
            }
        });
        jListPlayerQueue.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListPlayerQueueValueChanged(evt);
            }
        });
        jScrollPanePlayerQueue.setViewportView(jListPlayerQueue);

        jButtonPlayerClear.setText(bundle.getString("Button.Clear")); // NOI18N
        jButtonPlayerClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayerClearActionPerformed(evt);
            }
        });

        jLabelPlayerTimeEllapsed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPlayerTimeEllapsed.setText("00:00"); // NOI18N

        jSliderPlayerLength.setValue(0);
        jSliderPlayerLength.setEnabled(false);
        jSliderPlayerLength.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderPlayerLengthStateChanged(evt);
            }
        });

        jLabelPlayerTimeTotal.setText("00:00"); // NOI18N

        jButtonPlayerPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/resultset-premier-icone-4160-16.png"))); // NOI18N
        jButtonPlayerPrevious.setEnabled(false);
        jButtonPlayerPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayerPreviousActionPerformed(evt);
            }
        });

        jButtonPlayerPlay.setText(bundle.getString("Button.Play")); // NOI18N
        jButtonPlayerPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayerPlayActionPerformed(evt);
            }
        });

        jButtonPlayerNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/last-resultset-icone-5121-16.png"))); // NOI18N
        jButtonPlayerNext.setEnabled(false);
        jButtonPlayerNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayerNextActionPerformed(evt);
            }
        });

        jSpinnerVolume.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(5.0f)));
        jSpinnerVolume.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerVolumeStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabelPlayerTimeEllapsed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSliderPlayerLength, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelPlayerTimeTotal))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonPlayerPrevious)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPlayerPlay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPlayerNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSpinnerVolume, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPlayerTimeEllapsed)
                    .addComponent(jSliderPlayerLength, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPlayerTimeTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonPlayerPlay, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonPlayerNext)
                        .addGap(1, 1, 1))
                    .addComponent(jButtonPlayerPrevious, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSpinnerVolume, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jToggleButtonPlayerInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/external.png"))); // NOI18N
        jToggleButtonPlayerInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonPlayerInfoActionPerformed(evt);
            }
        });

        jPanelPlayerCover.setMaximumSize(new java.awt.Dimension(150, 150));
        jPanelPlayerCover.setMinimumSize(new java.awt.Dimension(150, 150));
        jPanelPlayerCover.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanelPlayerCover.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelPlayerCoverMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPlayerCoverLayout = new javax.swing.GroupLayout(jPanelPlayerCover);
        jPanelPlayerCover.setLayout(jPanelPlayerCoverLayout);
        jPanelPlayerCoverLayout.setHorizontalGroup(
            jPanelPlayerCoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 158, Short.MAX_VALUE)
        );
        jPanelPlayerCoverLayout.setVerticalGroup(
            jPanelPlayerCoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelPlayerCoverContainerLayout = new javax.swing.GroupLayout(jPanelPlayerCoverContainer);
        jPanelPlayerCoverContainer.setLayout(jPanelPlayerCoverContainerLayout);
        jPanelPlayerCoverContainerLayout.setHorizontalGroup(
            jPanelPlayerCoverContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPlayerCoverContainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelPlayerCover, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelPlayerCoverContainerLayout.setVerticalGroup(
            jPanelPlayerCoverContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlayerCoverContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPlayerCover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelPlayerYear.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabelPlayerYear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPlayerYear.setText("2014"); // NOI18N

        jComboBoxPlayerGenre.setEnabled(false);
        jComboBoxPlayerGenre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPlayerGenreActionPerformed(evt);
            }
        });

        jComboBoxPlayerRating.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5" }));
        jComboBoxPlayerRating.setEnabled(false);
        jComboBoxPlayerRating.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPlayerRatingActionPerformed(evt);
            }
        });

        jLabelTags.setFont(new java.awt.Font("Monospaced", 3, 13)); // NOI18N
        jLabelTags.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTags.setText("Tags tags TAGS"); // NOI18N

        jButtonTags.setText(Inter.get("Label.Tags")); // NOI18N
        jButtonTags.setEnabled(false);
        jButtonTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTagsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelTags, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jComboBoxPlayerGenre, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBoxPlayerRating, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jLabelPlayerYear, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonTags)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jComboBoxPlayerGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxPlayerRating)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelPlayerYear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonTags)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTags)
                .addGap(0, 0, 0))
        );

        jButtonCheckUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_up.png"))); // NOI18N
        jButtonCheckUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckUpActionPerformed(evt);
            }
        });

        jButtonCheckDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_down.png"))); // NOI18N
        jButtonCheckDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckDownActionPerformed(evt);
            }
        });

        jComboBoxPlaylist.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Playlist sample 1", "Playlist sample 2", "Playlist sample 3", "Playlist sample 4" }));
        jComboBoxPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPlaylistActionPerformed(evt);
            }
        });

        jButtonRefreshHiddenQueue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/update.png"))); // NOI18N
        jButtonRefreshHiddenQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshHiddenQueueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPlayerLayout = new javax.swing.GroupLayout(jPanelPlayer);
        jPanelPlayer.setLayout(jPanelPlayerLayout);
        jPanelPlayerLayout.setHorizontalGroup(
            jPanelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelPlayerArtist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabelPlayerAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelPlayerCoverContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPanePlayerQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanelPlayerLayout.createSequentialGroup()
                .addComponent(jButtonPlayerClear)
                .addGap(18, 18, 18)
                .addComponent(jButtonCheckUp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckDown)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToggleButtonPlayerInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanelPlayerLayout.createSequentialGroup()
                .addComponent(jComboBoxPlaylist, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRefreshHiddenQueue))
            .addComponent(jLabelPlayerTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelPlayerLayout.setVerticalGroup(
            jPanelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlayerLayout.createSequentialGroup()
                .addComponent(jPanelPlayerCoverContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelPlayerTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelPlayerAlbum)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelPlayerArtist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPanePlayerQueue, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonRefreshHiddenQueue)
                    .addComponent(jComboBoxPlaylist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonPlayerClear)
                    .addComponent(jToggleButtonPlayerInfo)
                    .addGroup(jPanelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButtonCheckDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCheckUp))))
        );

        jSplitPaneMain.setLeftComponent(jPanelPlayer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1098, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Display MP3 play position
     *
     * @param currentPosition
     */
    private static void dispMP3progress(int currentPosition) {
        if (jSliderPlayerLength.isEnabled()) {
            isManual = false;
            jSliderPlayerLength.setValue(currentPosition);
            isManual = true;
            jLabelPlayerTimeEllapsed.setText(StringManager.secondsToMMSS(currentPosition));
            playerInfo.dispMP3progress(currentPosition);
            sendPosition(currentPosition);
        }
    }
	
    /**
     * Enable/disable previous and next buttons
     *
     * @param previous
     * @param next
     */
    public static void enablePreviousAndNext(boolean previous, boolean next) {
        jButtonPlayerPrevious.setEnabled(previous);
        jButtonPlayerNext.setEnabled(next || (fileInfoHiddenQueue.size()>0 && jButtonPlayerPlay.getText().equals(Inter.get("Button.Pause"))));
    }

    /**
     * fill queue (clear played leaving a given number of those and add some new
     * until a given size is reached)
     */
    public static void fillQueue() {
        //TODO: Make those application options
        int nbFilesInQueue = 5;
        int nbFilesPlayed = 2;

        //Cleanup queue (and leave some items as set in options)
        queueModel.clearButLeave(nbFilesPlayed);

//        for(int i=0; i<queueModel.getSize(); i++) {
//            ListElement listElement = (ListElement) queueModel.getElementAt(i);
//        }
        
        for(ListElement listElement : queueModel.getQueue()) {
            fileInfoHiddenQueue.remove(listElement.getFile());
        }
        
        //Fillup queue until reached "nbFilesInQueue"
        if (fileInfoHiddenQueue.size() > 0) {
            while (queueModel.getSize() < nbFilesInQueue) {
                //TODO: Eventually Replace arraylist with a fifo queue and manage thread safety
                FileInfoInt myFileInfo = fileInfoHiddenQueue.get(0);
                fileInfoHiddenQueue.remove(0);
                PanelMain.addToQueue(myFileInfo, Jamuz.getDb().getRootPath());
            }
        }
    }

    /**
     * Fill audio player queue then plays selected song in queue
     * @param resume
     */
    public static void playSelected(boolean resume) {
        fillQueue();
        if (queueModel.getSize() > 0) {
            jButtonPlayerPlay.setText(Inter.get("Button.Pause"));  //NOI18N
            queueModel.removeBullet();
            int selected = jListPlayerQueue.getSelectedIndex();
            if (selected < 0) {
                selected = 0;
                jListPlayerQueue.setSelectedIndex(0);
				resume=false;
            }
            queueModel.setPlayingIndex(selected);
            play(resume);
        }
    }

    /**
     * fill queue, play selected file and display lyrics
     * @param resume
     */
    public static void play(boolean resume) {

        fillQueue();
        queueModel.enablePreviousAndNext();
        queueModel.addBullet();
        FileInfoInt myFileInfo = queueModel.getPlayingSong().getFile();
        //TODO: Allow to edit rating and genre using a context menu on jList queue (NOT on select table)
        jListPlayerQueue.setSelectedIndex(queueModel.getPlayingIndex());
        playerInfo.setSelected(queueModel.getPlayingIndex());
        displayFileInfo(queueModel.getPlayingSong().getFile(), true);
        //TODO: Manage 2 Players instances for transitions
        isManual = false;
        jSliderPlayerLength.setMaximum(myFileInfo.getLength());
        isManual = true;
        jLabelPlayerTimeTotal.setText(StringManager.secondsToMMSS(myFileInfo.getLength()));
        playerInfo.setMax(myFileInfo.getLength());
        String audioFileName = myFileInfo.getFullPath().getAbsolutePath();
        myFileInfo.sayRating(false);
        boolean enablejSliderPlayerLength=true;
		
		MPLAYER.play(audioFileName, resume);
		
        String lyrics = myFileInfo.getLyrics();
        Color textColor=Color.RED;
        if (!lyrics.equals("")) {  //NOI18N
			textColor=new Color(0, 128, 0);
        } 
        setTab("Label.Lyrics", "text", textColor);
        PanelLyrics.setText(lyrics);
        jSliderPlayerLength.setEnabled(enablejSliderPlayerLength);
    }
    
	/**
	 * Select tab with given title
	 *
	 * @param title
	 */
	public static void selectTab(String title) {
		Swing.selectTab(jTabbedPane, title);
	}
	
    private static void setTab(String title, String iconName) {
        setTab(title, iconName, null);
    }
    
    private static void setTab(String title, String iconName, Color textColor) {
        int index = jTabbedPane.indexOfTab(Inter.get(title)); //NOI18N
        JLabel label = new JLabel(Inter.get(title));
        Icon icon = new ImageIcon(PanelMain.class.getResource("/jamuz/ressources/"+iconName+".png"));
        label.setIcon(icon);
        if(textColor!=null) {
            label.setForeground(textColor);
        }
        label.setIconTextGap(5);
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        jTabbedPane.setTabComponentAt(index, label);
    }

	/**
	 *
	 */
	public static void stopMplayer() {
		if(MPLAYER!=null) {
			MPLAYER.stop();
		}
	}
	
    /**
     * stop player
     */
    public static void pause() {
        playerInfo.setMax(0);
        jSliderPlayerLength.setEnabled(false);
        queueModel.removeBullet();

		MPLAYER.pause();
		
        queueModel.setPlayingIndex(-1);
        jButtonPlayerPlay.setText(Inter.get("Button.Play"));  //NOI18N
        queueModel.enablePreviousAndNext();
        FileInfoInt fileInfoInt = new FileInfoInt("", ""); //NOI18N
        //TODO: Create a JaMuz logo to display there
//        displayFileInfo(fileInfoInt, false);
        playerInfo.displayFileInfo(fileInfoInt);
    }

    //TODO: Move to another class
    /**
     * Edit path with : - easytag under linux - mp3tag under windows Yes, hard-
     * coded, but can be improved ...
     *
     * @param location
     */
    public static void editLocation(String location) {
		//TODO: allow user to select its desired application
        //	maybe make a default list, based on windows/linux
        //	maybe detect what is installed on PC against allowed application list
        try {
            Runtime rt = Runtime.getRuntime();
            String cmd = "";  //NOI18N
            if (OS.isUnix()) {
                cmd = "easytag";  //NOI18N
            } else if (OS.isWindows()) {
                cmd = "C:\\Program Files\\Mp3tag\\Mp3tag.exe";  //NOI18N
            }
            rt.exec(new String[]{cmd, location});
        } catch (IOException ex) {
            Popup.error(ex);
        }
    }


    private void jButtonPlayerClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayerClearActionPerformed
        queueModel.clear();
    }//GEN-LAST:event_jButtonPlayerClearActionPerformed

    private void jButtonPlayerNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayerNextActionPerformed
//        queueModel.next();
        next();
    }//GEN-LAST:event_jButtonPlayerNextActionPerformed

    private void jButtonPlayerPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayerPreviousActionPerformed
        queueModel.previous();
    }//GEN-LAST:event_jButtonPlayerPreviousActionPerformed

    private void jListPlayerQueueValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListPlayerQueueValueChanged
//        if(!evt.getValueIsAdjusting()) {
//            int selected = jListPlayerQueue.getSelectedIndex();
//            if (selected > -1) {
//                FileInfoInt fileInfo = ((ListElement) queueModel.getElementAt(selected)).getFile();
//                displayFileInfo(fileInfo, (selected == queueModel.getPlayingIndex()));
//            }
//        }
    }//GEN-LAST:event_jListPlayerQueueValueChanged

    private void jListPlayerQueueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListPlayerQueueMouseClicked
        if (evt.getClickCount() == 2) {
            playSelected(false);
        }
    }//GEN-LAST:event_jListPlayerQueueMouseClicked

    private void jPanelPlayerCoverMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelPlayerCoverMousePressed
        PanelCover coverImg = (PanelCover) jPanelPlayerCover;
        DialogCoverDisplay.main(coverImg.getImage());
    }//GEN-LAST:event_jPanelPlayerCoverMousePressed

    private void jButtonPlayerPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayerPlayActionPerformed
		if (jButtonPlayerPlay.getText().equals(Inter.get("Button.Pause"))) { //NOI18N
			pause();
        } else {
			//resume
            playSelected(true);
        }
    }//GEN-LAST:event_jButtonPlayerPlayActionPerformed

	//False by default as jSliderPlayerLength is set to 0 within 
    //GUI auto-generated code, and it is an internal hange
    private static boolean isManual = false;

    private void jSliderPlayerLengthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderPlayerLengthStateChanged
        JSlider source = (JSlider) evt.getSource();
        int position = (int) source.getValue();
        if (!source.getValueIsAdjusting()) {
            if (isManual) {
				MPLAYER.setPosition(position);
//                mp3Player.setPosition(position);
                MPLAYER.positionLock = false;
//				mp3Player.positionLock = false;
            }
        } else {
            jLabelPlayerTimeEllapsed.setText(StringManager.secondsToMMSS(position));
            //TODO: Use a real java Lock
            MPLAYER.positionLock = true;
//			mp3Player.positionLock = true;
        }
    }//GEN-LAST:event_jSliderPlayerLengthStateChanged



    /**
     * add row to a 'select style' table (select and playlist tabs)
     *
     * @param tableModel
     * @param myFileInfo
     */
    public static void addRowSelect(TableModel tableModel, FileInfoInt myFileInfo) {
        //TODO: OK as long as each album only have one cover as they are loaded in ListModelSelector
        //Will be a problem for "Various Albums" (ie: Singles) ... need to make a load thread (as in ListModelSelector)
        ImageIcon coverIcon = IconBufferCover.getCoverIcon(myFileInfo, false); 
        ImageIcon rating = PanelMain.getRatingIcon(myFileInfo.getRating());

        Object[] donnee = new Object[]{myFileInfo.getArtist(), myFileInfo.getAlbum(), myFileInfo.getTrackNoFull(),
            myFileInfo.getTitle(), myFileInfo.getGenre(), myFileInfo.getYear(), myFileInfo.getBitRate(), myFileInfo.getFilename(),
            StringManager.secondsToMMSS(myFileInfo.getLength()), myFileInfo.getFormat(), StringManager.humanReadableByteCount(myFileInfo.getSize(), false),
            myFileInfo.getBPM(), myFileInfo.getAlbumArtist(),
            myFileInfo.getComment(), myFileInfo.getDiscNoFull(), coverIcon,
            myFileInfo.getPlayCounter(), rating, myFileInfo.getAddedDateLocalTime(),
            myFileInfo.getLastPlayedLocalTime(), myFileInfo.getCheckedFlag().toString(), PanelSelect.comboCopyRights[myFileInfo.getCopyRight()],
            "http://www.amazon.fr/gp/search?ie=UTF8&camp=1642&creative=6746&index=aps&linkCode=ur2&tag=ja097-21&keywords=" + (myFileInfo.getArtist() + " " + myFileInfo.getAlbum()).replaceAll(" ", "+")};
            //TODO: Support other amazon locations (.com, .co.uk,...)
            //TODO: Support more vendors
        
        tableModel.addRow(donnee);
    }

    private void jComboBoxPlayerGenreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPlayerGenreActionPerformed
        if (jComboBoxPlayerGenre.isEnabled()) {
            String genre = jComboBoxPlayerGenre.getSelectedItem().toString();
            if (!genre.equals(comboGenre[0])) {
                displayedFile.updateGenre(genre);
                queueModel.refreshPlayingFile();
                PanelSelect.fillSelectTable();
            }
        }
    }//GEN-LAST:event_jComboBoxPlayerGenreActionPerformed

    private void jComboBoxPlayerRatingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPlayerRatingActionPerformed
        if (jComboBoxPlayerRating.isEnabled()) {
            String rating = jComboBoxPlayerRating.getSelectedItem().toString();
            displayedFile.updateRating(rating);
            queueModel.getPlayingSong().setDisplay(rating);
            
            queueModel.refreshPlayingFile();
            PanelSelect.fillSelectTable();
        }
    }//GEN-LAST:event_jComboBoxPlayerRatingActionPerformed


    private void jToggleButtonPlayerInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonPlayerInfoActionPerformed
        playerInfo.setVisible(!playerInfo.isVisible());
    }//GEN-LAST:event_jToggleButtonPlayerInfoActionPerformed

    private void jButtonCheckUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckUpActionPerformed

        int[] selectedRows = jListPlayerQueue.getSelectedIndices();
        int selectedRow;
        ArrayList<Integer> selectedNew= new ArrayList();
        for(int i=0; i<selectedRows.length; i++) {
            selectedRow = selectedRows[i];
            int newRow = selectedRow-1;
            if(newRow>=0) {
                moveQueueRow(selectedRow, newRow);
                selectedNew.add(newRow);
            }
        }
        jListPlayerQueue.clearSelection();
//        for(int i : selectedNew) {
            jListPlayerQueue.setSelectedIndices(convertIntegers(selectedNew));
//        }
//        
//        
////        int[] selectedRows = jListPlayerQueue.getSelectedRows();
//        int selectedRow = jListPlayerQueue.getSelectedIndex();
////        ArrayList<Integer> selectedNew= new ArrayList();
////        for(int i=0; i<selectedRows.length; i++) {
////            selectedRow = queueModel.getPlayingIndex();
//        if(selectedRow>=0) {
//            int newRow = selectedRow-1;
//            if(newRow>=0) {
////                queueModel.moveRow(selectedRow, newRow);
//                moveQueueRow(selectedRow, newRow);
////                selectedNew.add(newRow);
//                jListPlayerQueue.setSelectedIndex(newRow);
//            }
////        }
////        jTableCheck.clearSelection();
////        for(int i : selectedNew) {
////            jTableCheck.addRowSelectionInterval(i, i);
////        }
//        }
    }//GEN-LAST:event_jButtonCheckUpActionPerformed

    private static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i);
        }
        return ret;
    }
    
    private void moveQueueRow(int selectedRow, int newRow) {
		try {
            queueModel.moveRow(selectedRow, newRow);
		} catch (CloneNotSupportedException ex) {
			Jamuz.getLogger().log(Level.SEVERE, "moveCheckRow", ex); //NOI18N
		}
	}
    
    private void jButtonCheckDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckDownActionPerformed
        int[] selectedRows = jListPlayerQueue.getSelectedIndices();
        int selectedRow;
        ArrayList<Integer> selectedNew= new ArrayList();
        for(int i=selectedRows.length-1; i>=0; i--) {
            selectedRow = selectedRows[i];
            int newRow = selectedRow+1;
            if(newRow>=0) {
                moveQueueRow(selectedRow, newRow);
                selectedNew.add(newRow);
            }
        }
        jListPlayerQueue.clearSelection();
//        for(int i : selectedNew) {
            jListPlayerQueue.setSelectedIndices(convertIntegers(selectedNew));
//        }


//        int selectedRow = jListPlayerQueue.getSelectedIndex();
////        ArrayList<Integer> selectedNew= new ArrayList();
////        for(int i=0; i<selectedRows.length; i++) {
////            selectedRow = queueModel.getPlayingIndex();
//        if(selectedRow>=0) {
//            int newRow = selectedRow+1;
//            if(newRow>=0) {
////                queueModel.moveRow(selectedRow, newRow);
//                moveQueueRow(selectedRow, newRow);
////                selectedNew.add(newRow);
//            }
//            jListPlayerQueue.setSelectedIndex(newRow);
//        }
//        int[] selectedRows = jTableCheck.getSelectedRows();
//        int selectedRow;
//        ArrayList<Integer> selectedNew= new ArrayList();
//        for(int i=selectedRows.length-1; i>=0; i--) {
//            selectedRow = selectedRows[i];
//            int newRow = selectedRow+1;
//            if(newRow>=0) {
//                moveQueueRow(selectedRow, newRow);
//                selectedNew.add(newRow);
//            }
//        }
//        jTableCheck.clearSelection();
//        for(int i : selectedNew) {
//            jTableCheck.addRowSelectionInterval(i, i);
//        }
    }//GEN-LAST:event_jButtonCheckDownActionPerformed

    private static boolean refreshHiddenQueue=true;
    
    private void jComboBoxPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPlaylistActionPerformed
        if(refreshHiddenQueue) refreshHiddenQueue(false);
    }//GEN-LAST:event_jComboBoxPlaylistActionPerformed

    public static void refreshHiddenQueue(boolean wait) {
        Thread fillQueue = new Thread("Thread.PanelMain.playlist.getFiles") {
			@Override
			public void run() {
				fileInfoHiddenQueue.clear();
				queueModel.clearNotPlayed();
				if (jComboBoxPlaylist.getSelectedIndex() > 1) {
						//Get playlist's files
						Playlist playlist = (Playlist) jComboBoxPlaylist.getSelectedItem();
						playlist.getFiles(fileInfoHiddenQueue);
						fillQueue();
						queueModel.enablePreviousAndNext();
				} 
				else if(jComboBoxPlaylist.getSelectedIndex()==1) {
					fileInfoHiddenQueue = new ArrayList<>(PanelSelect.getFileInfoList());
					Collections.shuffle(fileInfoHiddenQueue);
					fillQueue();
				}
			}};
		fillQueue.start();
		if(wait) {
			try {
				fillQueue.join(10000);
			} catch (InterruptedException ex) {
				Popup.error(ex);
			}
		}
    }
    
	/**
	 *
	 */
	
    
    private void jButtonRefreshHiddenQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshHiddenQueueActionPerformed
        refreshHiddenQueue(false);
    }//GEN-LAST:event_jButtonRefreshHiddenQueueActionPerformed

    private void jButtonTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTagsActionPerformed
        DialogTag.main(displayedFile);
    }//GEN-LAST:event_jButtonTagsActionPerformed

    private void jSpinnerVolumeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerVolumeStateChanged
        MPLAYER.setVolume((float)jSpinnerVolume.getValue());
    }//GEN-LAST:event_jSpinnerVolumeStateChanged

	/**
	 *
	 */
	public static void forward() {
        moveCursor(jSliderPlayerLength.getValue() + (jSliderPlayerLength.getMaximum()/10));
    }
    
	/**
	 *
	 */
	public void rewind() {
        moveCursor(jSliderPlayerLength.getValue() - (jSliderPlayerLength.getMaximum()/10));
    }

    private static ArrayList<FileInfoInt> fileInfoHiddenQueue = new ArrayList<>();
    
    /**
     * Unset external player info (fullscreen) when leaving it
     */
    public static void unsetToggleButtonPlayerInfo() {
        jToggleButtonPlayerInfo.setSelected(false);
    }

    private static FileInfoInt displayedFile = new FileInfoInt("", "");

    private static FramePlayerInfo playerInfo;

	/**
	 *
	 */
	public static void displayFileInfo() {
        FileInfoInt playingFile = queueModel.getPlayingSong()==null?displayedFile:queueModel.getPlayingSong().getFile();
		//In case we have stopped/paused so it gets refreshed anyway
        displayFileInfo(playingFile, true);
    }
    
    //TODO: This is called too many times sometimes
    //TODO: Only call to display current played song, not other one selected from queue
    /**
     * Displays file info
     *
     * @param fileInfo
     * @param isPlaying
     */
    private static void displayFileInfo(FileInfoInt fileInfo, boolean isPlaying) {
        try {

            displayedFile = fileInfo;
            if (isPlaying) {
//                jButtonDisplayCurrent.setForeground(Color.black);
                jSliderPlayerLength.setEnabled(true);
            } else {
//                jButtonDisplayCurrent.setForeground(Color.red);
                jSliderPlayerLength.setEnabled(false);
            }
            StringBuilder builder = new StringBuilder();
			displayedFile.readTags(); //In case of merge change
            for(String tag : displayedFile.getTags()) {
                builder.append(tag).append(" ");
            }
            jLabelTags.setText(toHTML(builder.toString()));
            
            jLabelPlayerTitle.setText(toHTML(fileInfo.getTitle()));
            jLabelPlayerAlbum.setText(toHTML(fileInfo.getAlbum()));
            jLabelPlayerArtist.setText(toHTML(fileInfo.getArtist()));

            jComboBoxPlayerGenre.setEnabled(false);
            jComboBoxPlayerGenre.setSelectedItem(getGenre(fileInfo.getGenre()));
            jComboBoxPlayerGenre.setEnabled(fileInfo.isFromLibrary());

            jComboBoxPlayerRating.setEnabled(false);
            jComboBoxPlayerRating.setSelectedIndex(fileInfo.getRating());
            jComboBoxPlayerRating.setEnabled(fileInfo.isFromLibrary());
            
            jButtonTags.setEnabled(fileInfo.isFromLibrary());

            jLabelPlayerYear.setText(fileInfo.getYear());  //NOI18N
            PanelCover coverImg = (PanelCover) jPanelPlayerCover;
            coverImg.setImage(fileInfo.getCoverImage());

            if (isPlaying) {
                playerInfo.displayFileInfo(fileInfo);
                PanelRemote.send(fileInfo);
            }
        } catch (Exception ex) {
            Popup.error(ex);
        }
    }

    private static long startTime=System.currentTimeMillis();
    private static void sendPosition(int currentPosition) {
        long currentTime=System.currentTimeMillis();
        if(currentTime-startTime>1000) {
            Map jsonAsMap = new HashMap();
            jsonAsMap.put("type", "currentPosition");
            jsonAsMap.put("currentPosition", currentPosition);
            jsonAsMap.put("total", displayedFile.getLength());
            PanelRemote.send(jsonAsMap, true);
            startTime=System.currentTimeMillis();
        }
    }
    
    private static void sendPlaylists(String clientId, String selectedPlaylist) {
		JSONArray list = new JSONArray();
		for(String playlist : getPlaylists()) {
			list.add(playlist);
		}
		JSONObject obj = new JSONObject();
		obj.put("type", "playlists");
		obj.put("playlists", list);
		obj.put("selectedPlaylist", selectedPlaylist);
		PanelRemote.send(clientId, obj);
    }
	
	/**
	 *
	 * @return
	 */
	public static List<String> getPlaylists() {
        ArrayList<String> list = new ArrayList<>();
        for (int index = 0; index < comboPlaylistsModel.getSize(); index++) {
            Object msg = comboPlaylistsModel.getElementAt(index);
            if (msg instanceof String) {
                list.add((String) msg);
            } else if (msg instanceof Playlist) {
                list.add(((Playlist) msg).getName());
            } 
        }
        return list;
    }

    //TODO: Move to a dedicated class
    /**
     * checks if genre exists in genre combo and return "select one" value (the
     * first in combo)
     *
     * @param genre
     * @return
     */
    public static String getGenre(String genre) {
        //Convert genre to comboBox
        int idGenre = Arrays.<String>asList(PanelMain.comboGenre).indexOf(genre);
        if (idGenre < 0) {
            idGenre = 0;
        }
        return comboGenre[idGenre];
    }

    private static String toHTML(String text) {
        return "<html>" + text + "</html>";  //NOI18N
    }
	
	//TODO: Move those setXXXVisible methods to separate class (not TableColumnModel)
    /**
     * set basic colums visible/unvisible
     *
     * @param myXTableColumnModel
     * @param state
     */
    public static void setBasicVisible(TableColumnModel myXTableColumnModel, boolean state) {
        setColumnVisible(myXTableColumnModel, BASIC_COLS, state);
    }

    /**
     * set file colums visible/unvisible
     *
     * @param myXTableColumnModel
     * @param state
     */
    public static void setFileVisible(TableColumnModel myXTableColumnModel, boolean state) {
        setColumnVisible(myXTableColumnModel, FILE_COLS, state);
    }

    /**
     * set extra colums visible/unvisible
     *
     * @param myXTableColumnModel
     * @param state
     */
    public static void setExtraVisible(TableColumnModel myXTableColumnModel, boolean state) {
        setColumnVisible(myXTableColumnModel, EXTRA_COLS, state);
    }
	
	/**
     * set extra colums visible/unvisible
     *
     * @param myXTableColumnModel
     * @param state
     */
    public static void setRightsVisible(TableColumnModel myXTableColumnModel, boolean state) {
        setColumnVisible(myXTableColumnModel, RIGHTS_COLS, state);
    }

    /**
     * set stats colums visible/unvisible
     *
     * @param myXTableColumnModel
     * @param state
     */
    public static void setStatsVisible(TableColumnModel myXTableColumnModel, boolean state) {
        setColumnVisible(myXTableColumnModel, STATS_COLS, state);
    }

    /**
     * set given colums visible/unvisible
     *
     * @param myXTableColumnModel
     * @param start
     * @param end
     * @param state
     */
    public static void setColumnVisible(TableColumnModel myXTableColumnModel, int start, int end, boolean state) {
        for (int i = start; i < (end + 1); i++) {
            setColumnVisible(myXTableColumnModel, i, state);
        }
    }

    /**
     * set given colums visible/unvisible
     *
     * @param myXTableColumnModel
     * @param columns
     * @param state
     */
    public static void setColumnVisible(TableColumnModel myXTableColumnModel, int[] columns, boolean state) {
        for (int column : columns) {
            setColumnVisible(myXTableColumnModel, column, state);
        }
    }

    /**
     * set given colum visible/unvisible
     *
     * @param myXTableColumnModel
     * @param index
     * @param state
     */
    public static void setColumnVisible(TableColumnModel myXTableColumnModel, int index, boolean state) {
        TableColumn column = myXTableColumnModel.getColumnByModelIndex(index);
        myXTableColumnModel.setColumnVisible(column, state);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        	/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {  //NOI18N
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            Jamuz.getLogger().severe(ex.toString());
        }
		//</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
			PanelMain panel = new PanelMain();
			//Center
			panel.setLocationRelativeTo(null);
			//Maximize
			panel.setExtendedState(PanelMain.MAXIMIZED_BOTH);
			//Set titleDisplay with version and JaMuz database location
			String version = Main.class.getPackage().getImplementationVersion();
			String title1 = panel.getTitle() + " " + version; //NOI18N
			panel.setTitle(title1 + " [" + Jamuz.getDb().getDbConn().getInfo().getLocationOri() + "]"); //NOI18N
			panel.setVisible(true);
		});
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCheckDown;
    private javax.swing.JButton jButtonCheckUp;
    private javax.swing.JButton jButtonPlayerClear;
    private static javax.swing.JButton jButtonPlayerNext;
    protected static javax.swing.JButton jButtonPlayerPlay;
    private static javax.swing.JButton jButtonPlayerPrevious;
    private javax.swing.JButton jButtonRefreshHiddenQueue;
    private static javax.swing.JButton jButtonTags;
    private static javax.swing.JComboBox jComboBoxPlayerGenre;
    private static javax.swing.JComboBox jComboBoxPlayerRating;
    private static javax.swing.JComboBox jComboBoxPlaylist;
    private static javax.swing.JLabel jLabelPlayerAlbum;
    private static javax.swing.JLabel jLabelPlayerArtist;
    protected static final javax.swing.JLabel jLabelPlayerTimeEllapsed = new javax.swing.JLabel();
    private static javax.swing.JLabel jLabelPlayerTimeTotal;
    private static javax.swing.JLabel jLabelPlayerTitle;
    private static javax.swing.JLabel jLabelPlayerYear;
    private static javax.swing.JLabel jLabelTags;
    private static javax.swing.JList jListPlayerQueue;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelPlayer;
    public static javax.swing.JPanel jPanelPlayerCover;
    private javax.swing.JPanel jPanelPlayerCoverContainer;
    private javax.swing.JScrollPane jScrollPanePlayerQueue;
    private static javax.swing.JSlider jSliderPlayerLength;
    private static javax.swing.JSpinner jSpinnerVolume;
    private javax.swing.JSplitPane jSplitPaneMain;
    private static javax.swing.JTabbedPane jTabbedPane;
    private static javax.swing.JToggleButton jToggleButtonPlayerInfo;
    private jamuz.process.book.PanelBook panelBook;
    protected final jamuz.process.check.PanelCheck panelCheck = new jamuz.process.check.PanelCheck();
    private jamuz.gui.PanelLyrics panelLyrics;
    private jamuz.process.merge.PanelMerge panelMerge;
    private jamuz.gui.PanelOptions panelOptions;
    private jamuz.gui.PanelPlaylists panelPlaylists;
    private jamuz.remote.PanelRemote panelRemote;
    private jamuz.gui.PanelSelect panelSelect;
    private jamuz.gui.PanelStats panelStats;
    private jamuz.process.sync.PanelSync panelSync;
    private jamuz.process.video.PanelVideo panelVideo;
    // End of variables declaration//GEN-END:variables
}
