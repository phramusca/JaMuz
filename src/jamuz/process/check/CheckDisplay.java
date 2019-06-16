/*
 * Copyright (C) 2019 phramusca ( https://github.com/phramusca/ )
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
package jamuz.process.check;

import jamuz.IconBufferCover;
import jamuz.gui.DialogCoverDisplay;
import jamuz.gui.PanelCover;
import jamuz.gui.PanelMain;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableCellListener;
import jamuz.gui.swing.TableColumnModel;
import jamuz.gui.swing.TriStateCheckBox;
import jamuz.utils.Inter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class CheckDisplay {

	private final JComboBox jComboBoxCheckDuplicates;
	private final JComboBox jComboBoxCheckMatches;
	boolean enableCombo=false;
	private final JLabel jLabelArtist;
	private final JLabel jLabelTitle;

	/**
	 *
	 * @param folder
	 * @param progressBar
	 * @param jCheckBoxCheckAlbumArtistDisplay
	 * @param jCheckBoxCheckAlbumDisplay
	 * @param jCheckBoxCheckArtistDisplay
	 * @param jCheckBoxCheckBPMDisplay
	 * @param jCheckBoxCheckBitRateDisplay
	 * @param jCheckBoxCheckCommentDisplay
	 * @param jCheckBoxCheckCoverDisplay
	 * @param jCheckBoxCheckFormatDisplay
	 * @param jCheckBoxCheckGenreDisplay
	 * @param jCheckBoxCheckLengthDisplay
	 * @param jCheckBoxCheckSizeDisplay
	 * @param jCheckBoxCheckYearDisplay
	 * @param jCheckCheckTitleDisplay
	 * @param jLabelCheckAlbumArtistTag
	 * @param jLabelCheckAlbumTag
	 * @param jLabelCheckNbTracks
	 * @param jLabelCheckYearTag
	 * @param jLabelCheckID3v1Tag
	 * @param jLabelCheckMeanBitRateTag
	 * @param jLabelCheckNbFiles
	 * @param jLabelCheckReplayGainTag
	 * @param jLabelCheckGenre
	 * @param jLabelCoverInfo
	 * @param jPanelCheckCoverThumb
	 * @param jScrollPaneCheckTags
	 * @param jTableCheck
	 * @param jComboBoxCheckDuplicates
	 * @param jComboBoxCheckMatches
	 * @param jLabelArtist
	 * @param jLabelTitle
	 */
	public CheckDisplay(FolderInfo folder, ProgressBar progressBar, JCheckBox jCheckBoxCheckAlbumArtistDisplay, JCheckBox jCheckBoxCheckAlbumDisplay, TriStateCheckBox jCheckBoxCheckArtistDisplay, JCheckBox jCheckBoxCheckBPMDisplay, JCheckBox jCheckBoxCheckBitRateDisplay, JCheckBox jCheckBoxCheckCommentDisplay, JCheckBox jCheckBoxCheckCoverDisplay, JCheckBox jCheckBoxCheckFormatDisplay, JCheckBox jCheckBoxCheckGenreDisplay, JCheckBox jCheckBoxCheckLengthDisplay, JCheckBox jCheckBoxCheckSizeDisplay, JCheckBox jCheckBoxCheckYearDisplay, TriStateCheckBox jCheckCheckTitleDisplay, JLabel jLabelCheckAlbumArtistTag, JLabel jLabelCheckAlbumTag, JLabel jLabelCheckNbTracks, JLabel jLabelCheckYearTag, JLabel jLabelCheckID3v1Tag, JLabel jLabelCheckMeanBitRateTag, JLabel jLabelCheckNbFiles, JLabel jLabelCheckReplayGainTag, JLabel jLabelCheckGenre, JLabel jLabelCoverInfo, JPanel jPanelCheckCoverThumb, JScrollPane jScrollPaneCheckTags, JTable jTableCheck, JComboBox jComboBoxCheckDuplicates, JComboBox jComboBoxCheckMatches, JLabel jLabelArtist, JLabel jLabelTitle) {
		this.folder = folder;
		this.progressBar = progressBar;
		this.jCheckBoxCheckAlbumArtistDisplay = jCheckBoxCheckAlbumArtistDisplay;
		this.jCheckBoxCheckAlbumDisplay = jCheckBoxCheckAlbumDisplay;
		this.jCheckBoxCheckArtistDisplay = jCheckBoxCheckArtistDisplay;
		this.jCheckBoxCheckBPMDisplay = jCheckBoxCheckBPMDisplay;
		this.jCheckBoxCheckBitRateDisplay = jCheckBoxCheckBitRateDisplay;
		this.jCheckBoxCheckCommentDisplay = jCheckBoxCheckCommentDisplay;
		this.jCheckBoxCheckCoverDisplay = jCheckBoxCheckCoverDisplay;
		this.jCheckBoxCheckFormatDisplay = jCheckBoxCheckFormatDisplay;
		this.jCheckBoxCheckGenreDisplay = jCheckBoxCheckGenreDisplay;
		this.jCheckBoxCheckLengthDisplay = jCheckBoxCheckLengthDisplay;
		this.jCheckBoxCheckSizeDisplay = jCheckBoxCheckSizeDisplay;
		this.jCheckBoxCheckYearDisplay = jCheckBoxCheckYearDisplay;
		this.jCheckCheckTitleDisplay = jCheckCheckTitleDisplay;
		this.jLabelCheckAlbumArtistTag = jLabelCheckAlbumArtistTag;
		this.jLabelCheckAlbumTag = jLabelCheckAlbumTag;
		this.jLabelCheckNbTracks = jLabelCheckNbTracks;
		this.jLabelCheckYearTag = jLabelCheckYearTag;
		this.jLabelCheckID3v1Tag = jLabelCheckID3v1Tag;
		this.jLabelCheckMeanBitRateTag = jLabelCheckMeanBitRateTag;
		this.jLabelCheckNbFiles = jLabelCheckNbFiles;
		this.jLabelCheckReplayGainTag = jLabelCheckReplayGainTag;
		this.jLabelCheckGenre = jLabelCheckGenre;
		this.jLabelCoverInfo = jLabelCoverInfo;
		this.jPanelCheckCoverThumb = jPanelCheckCoverThumb;
		this.jScrollPaneCheckTags = jScrollPaneCheckTags;
		this.jTableCheck = jTableCheck;
		
		initComponents();
		this.jComboBoxCheckDuplicates = jComboBoxCheckDuplicates;
		this.jComboBoxCheckMatches = jComboBoxCheckMatches;
		this.jLabelArtist = jLabelArtist;
		this.jLabelTitle = jLabelTitle;
	}
	
	private void initComponents() {
        
        //Set table's model
        jTableCheck.setModel(folder.getFilesAudioTableModel());
        
		columnModel = new TableColumnModel();
		
		//Assigning XTableColumnModel to allow show/hide columns
		jTableCheck.setColumnModel(columnModel);
		//Adding columns from model
		jTableCheck.createDefaultColumnsFromModel();
		
		TableColumn column;

		//	0:  "Filename"
		column = columnModel.getColumn(0);
		column.setMinWidth(100);

		//TODO CHECK Display "Disc # (new)" and "Track # (new)" ONLY if related result is not OK (Same as tristate check boxes - artitst and title)
		
		//	1:  "Disc # (new)"
		//	2:  "Disc #"
		column = columnModel.getColumn(1);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		column = columnModel.getColumn(2);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		
		//	3:  "Track # (new)"
		//	4:  "Track #"
		column = columnModel.getColumn(3);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		column = columnModel.getColumn(4);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		
		//	5:  "Artist (new)"
		//	6:  "Artist"
		column = columnModel.getColumn(5);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		column = columnModel.getColumn(6);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	7:  "Title (new)"
		column = columnModel.getColumn(7);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	8:  "Title"
		column = columnModel.getColumn(8);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	9:  "Genre (new)"
			//Render "Genre" column with a combo box
		column = columnModel.getColumn(9);
		JComboBox comboBox = new JComboBox(PanelMain.getComboGenre());
		column.setCellEditor(new DefaultCellEditor(comboBox));
			//set its width
		column.setMinWidth(100);
		column.setMaxWidth(200);
		column.setPreferredWidth(100);
		//	10: "Genre"
		column = columnModel.getColumn(10);
		column.setMinWidth(80);
		column.setPreferredWidth(80);
		
		//	11: "Album (new)"
		//	12: "Album"
		column = columnModel.getColumn(11);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		column = columnModel.getColumn(12);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	13: "Year (new)"
		//	14: "Year"
		column = columnModel.getColumn(13);
		column.setMinWidth(50);
		column.setPreferredWidth(50);
		column = columnModel.getColumn(14);
		column.setMinWidth(50);
		column.setPreferredWidth(50);
		
		//	15: "BitRate"
		column = columnModel.getColumn(15);
		column.setMinWidth(50);
		column.setPreferredWidth(50);
		
		//	16: "Length"
		column = columnModel.getColumn(16);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	17: "Format"
		column = columnModel.getColumn(17);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	18: "Size"
		column = columnModel.getColumn(18);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	19: "Album Artist (new)"
		//	20: "Album Artist"
		column = columnModel.getColumn(19);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		column = columnModel.getColumn(20);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	21: "Comment (new)"
		//	22: "Comment"
		column = columnModel.getColumn(21);
		column.setMinWidth(20);
		column.setPreferredWidth(20);
		column = columnModel.getColumn(22);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	23: "Cover"
		column = columnModel.getColumn(23);
		column.setMinWidth(IconBufferCover.getCoverIconSize());
		column.setMaxWidth(IconBufferCover.getCoverIconSize());
		jTableCheck.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int col = jTableCheck.convertColumnIndexToModel(jTableCheck.columnAtPoint(evt.getPoint()));
				if (col == 23) {
                    //Getting selected File
                    int selectedRow = jTableCheck.getSelectedRow();
                    //TODO: Does not work after having used moveRow function:
                    //Find a way to get corresponding rowIndex in getFilesAudio(),
                    //maybe based on filename
                    selectedRow = jTableCheck.convertRowIndexToModel(selectedRow);
//                    FileInfoDisplay file = folder.getFilesAudioTableModel().getFiles().get(selectedRow);
                    
                    if(selectedRow<folder.getFilesAudio().size()) { 
                        //Getting from there as it contains the covers
                        DialogCoverDisplay.main(folder.getFilesAudio().get(selectedRow).getCoverImage()); 	
                    }
				}
			}
		});
		
		//	24: "BPMDisplay"
		//	25: "BPMDisplay"
		column = columnModel.getColumn(24);
		column.setMinWidth(40);
		column.setPreferredWidth(40);
		column = columnModel.getColumn(25);
		column.setMinWidth(40);
		column.setPreferredWidth(40);
		
		//Hide all columns except permanent ones (filename, track#, trackTotal, disc#, discTotal)
		PanelMain.setColumnVisible(columnModel, 5, 25, false);
		
		//need to change jScrollPane's header height, NOT jTableTags's if not bug !
		Dimension d = jTableCheck.getTableHeader().getPreferredSize();
		d.height = 34;
		jScrollPaneCheckTags.getColumnHeader().setPreferredSize(d);
		
		Action action;
		action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				TableCellListener tcl = (TableCellListener)e.getSource();
				displayMatchTracks(tcl.getColumn());
			}
		};
		TableCellListener tcl = new TableCellListener(jTableCheck, action);	

		jCheckBoxCheckGenreDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			//	9:  "Genre (new)"
			//	10: "Genre"
			PanelMain.setColumnVisible(columnModel, 9, 10, jCheckBoxCheckGenreDisplay.isSelected());
		});
		
		jCheckBoxCheckLengthDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			//	16: "Length"
			PanelMain.setColumnVisible(columnModel, 16, jCheckBoxCheckLengthDisplay.isSelected());
		});
		
		jCheckBoxCheckFormatDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			//	17: "Format"
			PanelMain.setColumnVisible(columnModel, 17, jCheckBoxCheckFormatDisplay.isSelected());
		});
		
		jCheckBoxCheckYearDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			//	13: "Year (new)"
			//	14: "Year"
			PanelMain.setColumnVisible(columnModel, 13, 14, jCheckBoxCheckYearDisplay.isSelected());
		});
		
		jCheckBoxCheckAlbumArtistDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			//	19: "Album Artist (new)"
			//	20: "Album Artist"
			PanelMain.setColumnVisible(columnModel, 19, 20, jCheckBoxCheckAlbumArtistDisplay.isSelected());
		});
		
		jCheckBoxCheckAlbumDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			//	11: "Album (new)"
			//	12: "Album"
			PanelMain.setColumnVisible(columnModel, 11, 12, jCheckBoxCheckAlbumDisplay.isSelected());
		});
		
		jCheckBoxCheckSizeDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			 //	18: "Size"
        PanelMain.setColumnVisible(columnModel, 18, jCheckBoxCheckSizeDisplay.isSelected());
		});
		
		jCheckBoxCheckBitRateDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			 //	15: "BitRate"
        PanelMain.setColumnVisible(columnModel, 15, jCheckBoxCheckBitRateDisplay.isSelected());
		});
		
		jCheckBoxCheckBPMDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			//	24: "BPM (new)"
        //	25: "BPM"
        PanelMain.setColumnVisible(columnModel, 24, 25, jCheckBoxCheckBPMDisplay.isSelected());
		});
		
		jCheckBoxCheckCommentDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			//	21: "Comment (new)"
        //	22: "Comment"
        PanelMain.setColumnVisible(columnModel, 21, 22, jCheckBoxCheckCommentDisplay.isSelected());
		});
		
		jCheckBoxCheckCoverDisplay.addItemListener((java.awt.event.ItemEvent evt) -> {
			//	23: "Cover"
			PanelMain.setColumnVisible(columnModel, 23, jCheckBoxCheckCoverDisplay.isSelected());
			if(jCheckBoxCheckCoverDisplay.isSelected()) {
				jTableCheck.setRowHeight(IconBufferCover.getCoverIconSize());
			}
			else {
				jTableCheck.setRowHeight(16);
			}
		});
		
		jCheckBoxCheckArtistDisplay.addChangeListener((javax.swing.event.ChangeEvent evt) -> {
			//	5:  "Artist (new)"
			//	6:  "Artist"
			TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
			if(!checkbox.getModel().isArmed()) {
				switch(checkbox.getState()) {
					case ALL:
						PanelMain.setColumnVisible(columnModel, 5, false);
						PanelMain.setColumnVisible(columnModel, 6, true);
						break;
					case SELECTED:
						PanelMain.setColumnVisible(columnModel, 5, 6, true);
						break;
					case UNSELECTED:
						PanelMain.setColumnVisible(columnModel, 5, 6, false);
						break;
				}
			}
		});
		
		jCheckCheckTitleDisplay.addChangeListener((javax.swing.event.ChangeEvent evt) -> {
			//	7:  "Title (new)"
			//	8:  "Title"
			TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
			if(!checkbox.getModel().isArmed()) {
				switch(checkbox.getState()) {
					case ALL:
						PanelMain.setColumnVisible(columnModel, 7, false);
						PanelMain.setColumnVisible(columnModel, 8, true);
						break;
					case SELECTED:
						PanelMain.setColumnVisible(columnModel, 7, 8, true);
						break;
					case UNSELECTED:
						PanelMain.setColumnVisible(columnModel, 7, 8, false);
						break;
				}
			}
		});
		
		jPanelCheckCoverThumb.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DialogCoverSelect.main(folder, 
					(int)jLabelCoverInfo.getLocationOnScreen().getX(), 
					(BufferedImage image) -> {
						PanelCover coverImg = (PanelCover) jPanelCheckCoverThumb;
						coverImg.setImage(image);
				});
            }
        });
	}
    
	void displayMatches() {
		jComboBoxCheckMatches.removeAllItems();
		//Add matches
        List<ReleaseMatch> matches = folder.getMatches();
        if(matches!=null) {
            for(ReleaseMatch releaseMatch : matches) {
                jComboBoxCheckMatches.addItem("<html>"+releaseMatch.toString()+"</html>"); //NOI18N
            }
        }
		//Add originals
		for(ReleaseMatch myMatch : folder.getOriginals()) {
			jComboBoxCheckMatches.addItem("<html>"+myMatch.toString()+"</html>"); //NOI18N
		}
        
	}
	
	void displayMatchTracks() {
        folder.analyseMatchTracks();
        folder.getFilesAudioTableModel().fireTableDataChanged();
        displayMatchColumns(folder.getResults());
	}
	
    void displayMatchTracks(int colId) {
        //Need to analyse the whole column so that errorLevels are properly set
        folder.analyseMatchTracks(colId);
        folder.getFilesAudioTableModel().fireTableDataChanged();
        displayMatchColumn(folder.getResults(), colId);
    }
	
	void displayMatch(int matchId, DuplicateInfo diToSelect) {
		jComboBoxCheckDuplicates.setEnabled(false);
        ReleaseMatch match = folder.getMatch(matchId);//TODO: support match==null (should not happen)
        Map<String, FolderInfoResult> results = folder.getResults(); 
        folder.analyseMatch(matchId, progressBar);

    //DUPLICATES
        jComboBoxCheckDuplicates.removeAllItems();
        if(match!=null && match.getDuplicates()!=null) {
			if(match.getDuplicates().size()>0) {
				for(DuplicateInfo duplicate : match.getDuplicates()) {
					jComboBoxCheckDuplicates.addItem(duplicate);
				}
			}
			else {
				jComboBoxCheckDuplicates.addItem(FolderInfoResult.colorField(Inter.get("Label.None"),0));  //NOI18N
			}
        }
		if(diToSelect!=null) {
			jComboBoxCheckDuplicates.setSelectedItem(diToSelect);
		}
		progressBar.setIndeterminate(Inter.get("Msg.Scan.AnalyzingMatch"));  //NOI18N
		enableCombo=true;
		
		//TRACKS (ARTIST, TITLE, ...)
        //Number of files vs tracks
        FolderInfoResult result = results.get("nbFiles");  //NOI18N
        jLabelCheckNbFiles.setText(result.getDisplayText());
        jLabelCheckNbFiles.setToolTipText(result.getDisplayToolTip());
        if(match!=null) {
            List<ReleaseMatch.Track> tracks=match.getTracks(progressBar); 
            jLabelCheckNbTracks.setText(String.valueOf(tracks.size()));
        }
        else {
            jLabelCheckNbTracks.setText("");
        }
        
        //Display all tracks 
        displayMatchTracks();

        jLabelCheckAlbumTag.setText(results.get("album").getDisplayText()); //NOI18N
        jLabelCheckAlbumArtistTag.setText(results.get("albumArtist").getDisplayText()); //NOI18N
        jLabelCheckYearTag.setText(results.get("year").getDisplayText());
        jLabelCheckYearTag.setToolTipText(results.get("year").getDisplayToolTip());

        progressBar.reset();
		jComboBoxCheckDuplicates.setEnabled(true);
	}
		
    /**
	 * Displays a folder in check tab
	 */
	void displayFolder(){

		if(folder!=null) {
			progressBar.setIndeterminate(""); //NOI18N
			FolderInfoResult result;

		//Number of tracks
			result = folder.getResults().get("nbFiles");  //NOI18N
			jLabelCheckNbFiles.setText(result.getDisplayText());
			jLabelCheckNbFiles.setToolTipText(result.getDisplayToolTip());
			//Also displayed in displayMatch
		
			if(folder.getFilesAudio().size()<=0) { //No supported audio files on folder
				jTableCheck.setEnabled(true);
				
				unSelectCheckBoxes();
				
				//Hide track & disc # columns
				PanelMain.setColumnVisible(columnModel, 1, 4, false);
				
				progressBar.reset();
			}
			else {
				//Display track & disc # columns
				PanelMain.setColumnVisible(columnModel, 1, 4, true);
				
			//HasID3v1
				result = folder.getResults().get("hasID3v1");  //NOI18N
				jLabelCheckID3v1Tag.setText(result.getDisplayText());

			//isReplayGainDone
				result = folder.getResults().get("isReplayGainDone");  //NOI18N
				jLabelCheckReplayGainTag.setText(result.getDisplayText());
				
			//GENRE
				result = folder.getResults().get("genre"); //NOI18N
                setAddCheckBox(jCheckBoxCheckGenreDisplay, result);  //NOI18N
				if(jLabelCheckGenre!=null) {
					jLabelCheckGenre.setText(result.getDisplayText());
				}

			//COMMENT
//				setAddCheckBox(jCheckBoxCheckCommentDisplay, folder.getResults().get("comment"));  //NOI18N

			//BITRATE
				result = folder.getResults().get("bitRate"); //NOI18N
                setAddCheckBox(jCheckBoxCheckBitRateDisplay, result);  //NOI18N
				jLabelCheckMeanBitRateTag.setText(result.getDisplayText());

			//FORMAT
				setAddCheckBox(jCheckBoxCheckFormatDisplay, folder.getResults().get("format"));  //NOI18N

			//BPM
//				setAddCheckBox(jCheckBoxCheckBPMDisplay, folder.getResults().get("bpm"));  //NOI18N

			//LENGTH
				setAddCheckBox(jCheckBoxCheckLengthDisplay, folder.getResults().get("length"));  //NOI18N

			//SIZE
				setAddCheckBox(jCheckBoxCheckSizeDisplay, folder.getResults().get("size"));  //NOI18N

			//COVER
                result=folder.getResults().get("cover"); //NOI18N
				setAddCheckBox(jCheckBoxCheckCoverDisplay, result, false);  //NOI18N
				
				//By default, display cover selected for Saving 
                BufferedImage myImage;
				if(folder.getNewImage()!=null && folder.action.equals(ProcessCheck.Action.SAVE)) {
					myImage = folder.getNewImage();
				}
				else {
					myImage = folder.getFirstCoverFromTags();
				}
				PanelCover coverImg = (PanelCover) jPanelCheckCoverThumb;
				coverImg.setImage(myImage);
				if(myImage!=null) {
					jLabelCoverInfo.setText(result.getDisplayText());
				}
				else {
					if(folder.getFilesImage().size()>0) {
						jPanelCheckCoverThumb.setEnabled(true);
						jLabelCoverInfo.setText(FolderInfoResult.colorField(Inter.get("Label.Select"), 2)); //NOI18N
					}
					else {
						jPanelCheckCoverThumb.setEnabled(false);
						jLabelCoverInfo.setText(FolderInfoResult.colorField(Inter.get("Label.None"), 2)); //NOI18N
					}
				}
				
				displayMatches();
				
				progressBar.reset();
			}
		}
	}
    
	void unSelectCheckBoxes() {
		unSelectCheckBox(jCheckBoxCheckArtistDisplay);
		unSelectCheckBox(jCheckCheckTitleDisplay);
		unSelectCheckBox(jCheckBoxCheckGenreDisplay);
		unSelectCheckBox(jCheckBoxCheckAlbumDisplay);
		unSelectCheckBox(jCheckBoxCheckAlbumArtistDisplay);
		unSelectCheckBox(jCheckBoxCheckYearDisplay);
		unSelectCheckBox(jCheckBoxCheckCoverDisplay);
		unSelectCheckBox(jCheckBoxCheckFormatDisplay);
		unSelectCheckBox(jCheckBoxCheckBitRateDisplay);
		unSelectCheckBox(jCheckBoxCheckLengthDisplay);
		unSelectCheckBox(jCheckBoxCheckSizeDisplay);
		unSelectCheckBox(jCheckBoxCheckCommentDisplay);
		unSelectCheckBox(jCheckBoxCheckBPMDisplay);
	}  
    
    private void unSelectCheckBox(JCheckBox checkbox) {
		checkbox.setSelected(false);
		checkbox.setForeground(Color.BLACK);
	}  
    
    private void displayMatchColumn(Map<String, FolderInfoResult> results, int colId) {
		
		javax.swing.JCheckBox checkbox=null;
		TriStateCheckBox triStateCheckBox=null;
		JLabel jLabel=null;
        boolean select=true;
		switch (colId) {
//			case 1: field="discNoFull";	break;
//			case 3: field="trackNoFull"; break;
			case 5: 
				triStateCheckBox=jCheckBoxCheckArtistDisplay; 
				jLabel=jLabelArtist;
				break;
			case 7: 
				triStateCheckBox=jCheckCheckTitleDisplay; 
				jLabel=jLabelTitle;
				break;
			case 9: checkbox=jCheckBoxCheckGenreDisplay; break;
			case 11: checkbox=jCheckBoxCheckAlbumDisplay; break; 
			case 13: checkbox=jCheckBoxCheckYearDisplay; break;
			case 19: checkbox=jCheckBoxCheckAlbumArtistDisplay; break;
			case 21: checkbox=jCheckBoxCheckCommentDisplay; select=false; break;
			case 24: checkbox=jCheckBoxCheckBPMDisplay; break;
		}
		FolderInfoResult result = results.get(FolderInfo.getField(colId));
		if(triStateCheckBox!=null) {
			setAddCheckBox(triStateCheckBox, jLabel, result, select);
		} else {
			setAddCheckBox(checkbox, result, select);  //NOI18N
		}
		
	}
        
    void displayMatchColumns(Map<String, FolderInfoResult> results) {
		Integer[] columns = {5, 7, 9, 11, 13, 19, 21, 24};
        for(int colId : columns) {
			displayMatchColumn(results, colId);
        }
	}

    private void setAddCheckBox(JCheckBox checkbox, FolderInfoResult result) {
        setAddCheckBox(checkbox, result, true);
    }
    
	private void setAddCheckBox(TriStateCheckBox checkbox, JLabel jLabel, FolderInfoResult result, boolean select) {
		if(checkbox!=null) {
			if(select) {
				if(result.isNotValid()) {
					checkbox.setState(TriStateCheckBox.State.SELECTED);
				} else {
					checkbox.setState(TriStateCheckBox.State.ALL);
				}
			}
			jLabel.setForeground(result.getDisplayColor());
		}
	}
	
    private void setAddCheckBox(JCheckBox checkbox, FolderInfoResult result, boolean select) {
		if(checkbox!=null) {
            if(select) {
                checkbox.setSelected(result.isNotValid());
            }
			checkbox.setForeground(result.getDisplayColor());
		}
	}

	private final FolderInfo folder;
	private final ProgressBar progressBar;
	private TableColumnModel columnModel;
	
	private final javax.swing.JCheckBox jCheckBoxCheckAlbumArtistDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckAlbumDisplay;
    private final TriStateCheckBox jCheckBoxCheckArtistDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckBPMDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckBitRateDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckCommentDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckCoverDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckFormatDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckGenreDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckLengthDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckSizeDisplay;
    private final javax.swing.JCheckBox jCheckBoxCheckYearDisplay;
    private final TriStateCheckBox jCheckCheckTitleDisplay;
	private final javax.swing.JLabel jLabelCheckAlbumArtistTag;
    private final javax.swing.JLabel jLabelCheckAlbumTag;
    private final javax.swing.JLabel jLabelCheckNbTracks;
	private final javax.swing.JLabel jLabelCheckYearTag;
	private final javax.swing.JLabel jLabelCheckID3v1Tag;
    private final javax.swing.JLabel jLabelCheckMeanBitRateTag;
    private final javax.swing.JLabel jLabelCheckNbFiles;
    private final javax.swing.JLabel jLabelCheckReplayGainTag;
	private final javax.swing.JLabel jLabelCheckGenre;
    private final javax.swing.JLabel jLabelCoverInfo;
    private final javax.swing.JPanel jPanelCheckCoverThumb;
    private final javax.swing.JScrollPane jScrollPaneCheckTags;
    private final javax.swing.JTable jTableCheck;
}