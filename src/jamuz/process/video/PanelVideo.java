/*
 * Copyright (C) 2014 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz.process.video;

import jamuz.process.check.FolderInfoResult;
import jamuz.Jamuz;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import jamuz.gui.swing.PopupListener;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TriStateCheckBox;
import jamuz.gui.swing.TriStateCheckBox.State;
import jamuz.utils.Desktop;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */


public class PanelVideo extends javax.swing.JPanel {

	/**
	 *
	 */
	protected static ProgressBar progressBar;

    /**
     * Creates new form PanelVideo
     */
    public PanelVideo() {
        initComponents();
        initExtended();
    }
    
	/**
	 *
	 */
	protected static VideoRating[] comboRating;

    private void initExtended() {

        comboRating = new VideoRating[10];
        for (int i=0; i<10; i++) {
            comboRating[i] = new VideoRating(i+1, Inter.get("Label.Video.Rating."+(i+1)));
        }
        
        //Disable until a list has been performed
        jButtonVideoExport.setEnabled(false);
        
        processVideo = new ProcessVideo("Thread.PanelVideo.ProcessVideo");
        jTableVideo.setModel(processVideo.getTableModel());
        jTableVideo.setRowSorter(null);
		//Adding columns from model. Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableVideo.createDefaultColumnsFromModel();
		
        //	0:  "isSelected"
		setColumn(0, 20);
        setColumnIcon(0, "selected.png");
		//	1:  "getThumbnails"
        setColumn(1, IconBufferVideo.ICON_WIDTH);
		//	2:  "getTitle"
		//	3:  "getYear"
		setColumn(3, 55);
        //	4:  "getSynopsis"
        //	5:  size
        setColumn(5, 80);
        //6: Watched
        setColumn(6, 20);
        setColumnIcon(6, "eye.png");
        //7: Rating
        setColumn(7, 50);
        setColumnIcon(7, "1starC.png");
        
        TableColumn column = jTableVideo.getColumnModel().getColumn(7);
		JComboBox comboBox = new JComboBox(comboRating);
		column.setCellEditor(new DefaultCellEditor(comboBox));
        
        //8: WatchList
        setColumn(8, 20);
        setColumnIcon(8, "wishlist_add.png");
        //9: Favorite
        setColumn(9, 20);
        setColumnIcon(9, "heart.png");
        
        //TODO: Gray-out checkbox (or cell) if cell disable
//        column = jTableVideo.getColumnModel().getColumn(9);
//        column.setCellRenderer(new GrayableCheckboxCellRenderer());
//        column = jTableVideo.getColumnModel().getColumn(10);
//        column.setCellRenderer(new GrayableCheckboxCellRenderer());
        
        //TODO: Set default height when browsing FS
        jTableVideo.setRowHeight(IconBufferVideo.ICON_HEIGHT);
        
//        DefaultTableCellRenderer renderer = new TableCellRendererTooltip();
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setVerticalAlignment(SwingConstants.TOP);
        jTableVideo.getColumnModel().getColumn(4).setCellRenderer(renderer);
        jTableVideo.getColumnModel().getColumn(2).setCellRenderer(renderer);        
        
		progressBar = (ProgressBar)jProgressBarVideo;
        
        //Add a list renderer to display genre icons
        //TODO: Make a class for video genre (or make this one or the album one or both more generic)
//        jListVideoGenre.setBackground(Color.WHITE);
//        ListRendererGenre rendererGenre = new ListRendererGenre();
//        rendererGenre.setPreferredSize(new Dimension(0, IconBufferGenre.iconSize));
//        jListVideoGenre.setCellRenderer(rendererGenre);
        
        //Menu listener
        addMenuItem("IMDb"); //NOI18N
		//TODO: How can a (theMovieDb.org) Refresh be done as 
		//
		//addMenuItem("Refresh"); //NOI18N
        addMenuItem(Inter.get("Button.Open"));  //NOI18N
        addMenuItem(Inter.get("Label.Trailer")); //NOI18N
        addMenuItem(Inter.get("Label.Delete")); //NOI18N
        addMenuItem(Inter.get("Label.Homepage")); //NOI18N
        //Add links menu items
        File f = Jamuz.getFile("VideoLinks.txt", "data");
        if(f.exists()) {
            JMenu menuLinks = new JMenu(Inter.get("Label.Links")); //NOI18N
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get(f.getAbsolutePath()), Charset.defaultCharset());
                for(String line : lines) {
                    if(line.contains("|")) {
                        String[] splitted = line.split("\\|");
                        JMenuItem menuItem1 = new JMenuItem(new OpenUrlAction(splitted[0], splitted[1]));
                        menuLinks.add(menuItem1);
                    }
                }
                jPopupMenu1.add(menuLinks);
            } catch (IOException ex) {
                Jamuz.getLogger().log(Level.SEVERE, null, ex);
            }
        }

        //Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener(jPopupMenu1);
        jTableVideo.addMouseListener(popupListener);
        
//        List<String> ratings = Arrays.asList(
//                "0", "1", "2", "3", "4", "5",
//                "6", "7", "8", "9", "10");
//        jListVideoRating.setModel(getModel(ratings, false));

		listDb();
	}
    
    class OpenUrlAction extends AbstractAction {
        
        private final String url;
        
        public OpenUrlAction(String text, String url) {
            super(text, null);
            this.url = url;
//            putValue(SHORT_DESCRIPTION, desc);
//            putValue(MNEMONIC_KEY, mnemonic);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            VideoAbstract fileInfoVideo = getSelected();
			if(fileInfoVideo!=null) {
				Desktop.openBrowser(url.replaceAll("<title>", fileInfoVideo.getTitle()));
			}
        }
    }
    
    class JComponentTableCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
          return (JComponent) value;
        }
      }
    
    private void setColumn(int index, int width) {
        TableColumn column = jTableVideo.getColumnModel().getColumn(index);
		column.setMinWidth(width);
        column.setPreferredWidth(width);
        column.setMaxWidth(width*3);
    }
    
    private void setColumnIcon(int col, String icon) {
        TableCellRenderer rendererHeader = new JComponentTableCellRenderer();
//        Border headerBorder = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0));
        Border headerBorder = javax.swing.BorderFactory.createEtchedBorder();
//                UIManager.getBorder("TableHeader.cellBorder");
        TableColumn column = jTableVideo.getColumnModel().getColumn(col);
        column.setHeaderRenderer(rendererHeader);
        Icon blueIcon = new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/"+icon));
        JLabel label = new JLabel("", blueIcon, JLabel.CENTER);
        label.setBorder(headerBorder);
        column.setHeaderValue(label);
    }
    
    private void addMenuItem(String item) {
        JMenuItem menuItem = new JMenuItem(item);
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
    }
    
    ActionListener menuListener = (ActionEvent e) -> {
		JMenuItem source = (JMenuItem)(e.getSource());
		String sourceTxt=source.getText();
		//                String s = "Action event detected."
		//                           + "\n"
		//                           + "    Event source: " + source.getText()
		//                           + " (an instance of " + getClassName(source) + ")";
		//                Popup.info(s);

		if(sourceTxt.equals("IMDb")) { //NOI18N
			menuVideoIMDb();
		}
		else if(sourceTxt.equals(Inter.get("Button.Open"))) { //NOI18N
			menuVideoOpen();
		}
		else if(sourceTxt.equals(Inter.get("Label.Trailer"))) { //NOI18N
			menuVideoTrailer();
		}
		else if(sourceTxt.equals(Inter.get("Label.Delete"))) { //NOI18N
			menuVideoDelete();
		}
		else if(sourceTxt.equals(Inter.get("Label.Homepage"))) {
			menuVideoHomepage();
		}
		else {
			Popup.error("Unknown menu item: " + sourceTxt); //NOI18N
		}
	};

    private static TableRowSorter<TableModelVideo> tableSorter;
    private static final TableRowFilterVideo filterVideo= new TableRowFilterVideo();
    
    private static void filterVideo() {
        filterVideo(true);
    }
    
    private static void filterVideo(boolean fillLists) {
//        TableRowSorter<TableModelVideo> tableSorter
        //Enable row tableSorter (cannot be done if model is empty)
        if(processVideo.getTableModel().getRowCount()>0) {
            //Enable auto sorter
            jTableVideo.setAutoCreateRowSorter(true);
            //Get sorter
            tableSorter = new TableRowSorter<>(processVideo.getTableModel());
            jTableVideo.setRowSorter(tableSorter);
            //Sort by status, title (Debug display problem before enabling)
//            List <RowSorter.SortKey> sortKeys = new ArrayList<>();
//            sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
//            sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
//            tableSorter.setSortKeys(sortKeys);
            //Désactive le tri pour
//            tableSorter.setSortable(0, false); // Checkbox
            tableSorter.setSortable(1, false); // Thumbnail
            tableSorter.setSortable(4, false); // Synopsis
            //Filter, Apply current filter
            tableSorter.setRowFilter(filterVideo);

            if(fillLists) {
                List<String> genres=new ArrayList<>();
                List<String> mppaRatings=new ArrayList<>();
                List<String> ratings = new ArrayList<>();
                int index;
                for(int i = 0; i < jTableVideo.getRowCount(); i++) {
                    index = jTableVideo.convertRowIndexToModel(i);
                    VideoAbstract fileInfoVideo = processVideo.getTableModel().getFile(index);
                    //Add genres to the list
                    for(String genre : fileInfoVideo.getGenres()) {
                        if(!genres.contains(genre)) {
                            genres.add(genre);
                        }
                    }
                    //Add MppaRating to the list
                    if(!mppaRatings.contains(fileInfoVideo.getMppaRating())) {
                        mppaRatings.add(fileInfoVideo.getMppaRating());
                    }
                    //Add rating to the list
                    if(!ratings.contains(fileInfoVideo.getRating())) {
                        ratings.add(fileInfoVideo.getRating());
                    }
                }

                jListVideoGenre.setModel(getModel(genres));
                jListVideoMppaRating.setModel(getModel(mppaRatings));
                jListVideoRating.setModel(getModel(ratings));
                
                jListVideoGenre.setSelectedIndex(0);
                jListVideoMppaRating.setSelectedIndex(0);
                jListVideoRating.setSelectedIndex(0);
            }
        }
        else {
            jTableVideo.setAutoCreateRowSorter(false);
        }
    }

	/**
	 *
	 * @param list
	 * @return
	 */
	public static DefaultListModel getModel(List<String> list) {
        return getModel(list, true);
    }
    
	/**
	 *
	 * @param list
	 * @param sort
	 * @return
	 */
	public static DefaultListModel getModel(List<String> list, boolean sort) {
        DefaultListModel model = new DefaultListModel();
        if(sort) { Collections.sort(list); }
        model.addElement(Inter.get("Label.All"));
        for(String element : list) {
            model.addElement(element);
        }
        return model;
    }
    
    /**
     * display length
     */
    public static void diplayLength() {
        long selected=processVideo.getTableModel().getLengthSelected();
        long spaceLeft=getSpaceLeft(Jamuz.getOptions().get("video.destination"));
        long afterSpace=spaceLeft - selected;
        String afterSpaceStr=StringManager.humanReadableByteCount(afterSpace, false);
        String status = "<html>"; //NOI18N
        status += "<tr>"; //NOI18N
            status += "<td>"; //NOI18N
                status += "Selected"; //NOI18N
            status += "</td>"; //NOI18N
            status += "<td>"; //NOI18N
                status += StringManager.humanReadableByteCount(selected, false);
            status += "</td>"; //NOI18N
        status += "</tr>"; //NOI18N
        status += "<tr>"; //NOI18N
            status += "<td>"; //NOI18N
                status += "After"; //NOI18N
            status += "</td>"; //NOI18N
            status += "<td>"; //NOI18N
            if(afterSpace<=0) {
                afterSpaceStr=FolderInfoResult.colorField(afterSpaceStr, 2, false);
            }
            else if(afterSpace<100000) { //TODO: Set a proper value (maybe a % of device space ?)
                afterSpaceStr=FolderInfoResult.colorField(afterSpaceStr, 1, false);
            }
                status += afterSpaceStr;
            status += "</td>"; //NOI18N
        status += "</tr>"; //NOI18N
        status += "</html>"; //NOI18N
        
        jLabelVideoStatus.setText(status);
    }

    //TODO: Find all calls to "OtherClass" getSelectedRow 
    //And replace by below function (if table model extended)
    private VideoAbstract getSelected() {
        //Getting selected File 		
		int selectedRow = jTableVideo.getSelectedRow(); 			
		if(selectedRow>=0) { 	
			//convert to model index (if sortable model) 		
			selectedRow = jTableVideo.convertRowIndexToModel(selectedRow); 
            VideoAbstract fileInfoVideo = processVideo.getTableModel().getFile(selectedRow);
            return fileInfoVideo;		
		}
        else { 			
            Popup.info(Inter.get("Error.YouMustSelectArow")); 		  //NOI18N
            return null;
        }
    }
    
    private void menuVideoTrailer() {
        //TODO: Open video within JaMuz
        //http://stackoverflow.com/questions/10392972/how-to-play-flv-mp4-avi-format-video-in-java

        VideoAbstract fileInfoVideo = getSelected();
        if(fileInfoVideo!=null) {
            if(!fileInfoVideo.getTrailerURL().startsWith("{")) { //NOI18N	
				Desktop.openBrowser(fileInfoVideo.getTrailerURL()); 
            }
        }
    }
    
    private void menuVideoOpen() {
        VideoAbstract fileInfoVideo = getSelected();
        if(fileInfoVideo!=null) {
			Desktop.openFolder("//"+FilenameUtils.concat(
			Boolean.parseBoolean(Jamuz.getOptions().get("video.library.remote"))?
					Jamuz.getOptions().get("video.location.library")
					:Jamuz.getOptions().get("video.rootPath"), 
			fileInfoVideo.getRelativeFullPath()));
        }
    }

    private void menuVideoHomepage() {
        VideoAbstract fileInfoVideo = getSelected();
        if(fileInfoVideo!=null) {
			//TODO: Open video within JaMuz
            Desktop.openBrowser(fileInfoVideo.getHomepage());
        }
    }
	
    private void menuVideoDelete() {
        VideoAbstract video = getSelected();
        if(video!=null) {
            int n = JOptionPane.showConfirmDialog(
					null, Inter.get("Question.DeleteVideo"),  //NOI18N
					Inter.get("Label.Confirm"),  //NOI18N
					JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                for(FileInfoVideo fileInfoVideo : video.getFiles().values()) {
                    File videoFile = fileInfoVideo.getVideoFile();
                    boolean isDeleted = videoFile.delete();
                    //TODO: Remove from db (and send db back at some point)
                }
            } 
        }
    }
    
    private void menuVideoIMDb() {
        //TODO: Open within JaMuz
        VideoAbstract fileInfoVideo = getSelected();
        if(fileInfoVideo!=null) {
            Desktop.openBrowser(fileInfoVideo.getImdbURI());
        }
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        btnGroupSelected = new javax.swing.ButtonGroup();
        btnGroupWatched = new javax.swing.ButtonGroup();
        jPanelVideo = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPaneSelectGenre1 = new javax.swing.JScrollPane();
        jListVideoGenre = new javax.swing.JList();
        jLabelVideoStatus = new javax.swing.JLabel();
        jScrollPaneSelectGenre2 = new javax.swing.JScrollPane();
        jListVideoMppaRating = new javax.swing.JList();
        jScrollPaneSelectGenre4 = new javax.swing.JScrollPane();
        jListVideoRating = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jButtonVideoExport = new javax.swing.JButton();
        jButtonVideoList = new javax.swing.JButton();
        jCheckBoxVideoGet = new javax.swing.JCheckBox();
        jCheckBoxVideoMove = new javax.swing.JCheckBox();
        jButtonVideoOptions = new javax.swing.JButton();
        jProgressBarVideo = new jamuz.gui.swing.ProgressBar();
        jScrollPaneCheckTags1 = new javax.swing.JScrollPane();
        jTableVideo = new jamuz.gui.swing.TableHorizontal();
        jLabel5 = new javax.swing.JLabel();
        triStateSelected = new jamuz.gui.swing.TriStateCheckBox();
        jLabel6 = new javax.swing.JLabel();
        triStateWatched = new jamuz.gui.swing.TriStateCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        triStateWatchList = new jamuz.gui.swing.TriStateCheckBox();
        jLabel9 = new javax.swing.JLabel();
        triStateFavorite = new jamuz.gui.swing.TriStateCheckBox();
        triStateRated = new jamuz.gui.swing.TriStateCheckBox();
        jComboBoxFilter = new JComboBox(VideoFilter.values());
        jButtonRefresh = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        triStateLocal = new jamuz.gui.swing.TriStateCheckBox();
        jLabel11 = new javax.swing.JLabel();
        triStateHd = new jamuz.gui.swing.TriStateCheckBox();
        jLabel12 = new javax.swing.JLabel();
        triStateMovies = new jamuz.gui.swing.TriStateCheckBox();
        jCheckBoxVideoTheMovieDb = new javax.swing.JCheckBox();
        jButtonVideoCleanup = new javax.swing.JButton();

        jSplitPane1.setDividerLocation(150);

        jListVideoGenre.setModel(new DefaultListModel());
        jListVideoGenre.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListVideoGenre.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListVideoGenreValueChanged(evt);
            }
        });
        jScrollPaneSelectGenre1.setViewportView(jListVideoGenre);

        jLabelVideoStatus.setText(" "); // NOI18N
        jLabelVideoStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jListVideoMppaRating.setModel(new DefaultListModel());
        jListVideoMppaRating.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListVideoMppaRating.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListVideoMppaRatingValueChanged(evt);
            }
        });
        jScrollPaneSelectGenre2.setViewportView(jListVideoMppaRating);

        jListVideoRating.setModel(new DefaultListModel());
        jListVideoRating.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListVideoRating.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListVideoRatingValueChanged(evt);
            }
        });
        jScrollPaneSelectGenre4.setViewportView(jListVideoRating);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelVideoStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPaneSelectGenre2, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
            .addComponent(jScrollPaneSelectGenre1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jScrollPaneSelectGenre4, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelVideoStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneSelectGenre1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneSelectGenre2, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneSelectGenre4, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel2);

        jButtonVideoExport.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        jButtonVideoExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/external.png"))); // NOI18N
        jButtonVideoExport.setText(Inter.get("Button.Export")+" ..."); // NOI18N
        jButtonVideoExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVideoExportActionPerformed(evt);
            }
        });

        jButtonVideoList.setText(Inter.get("Button.Start")); // NOI18N
        jButtonVideoList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVideoListActionPerformed(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jCheckBoxVideoGet.setText(bundle.getString("MainGUI.jCheckBoxVideoGet.text")); // NOI18N
        jCheckBoxVideoGet.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxVideoGetItemStateChanged(evt);
            }
        });

        jCheckBoxVideoMove.setText(bundle.getString("MainGUI.jCheckBoxVideoMove.text")); // NOI18N
        jCheckBoxVideoMove.setEnabled(false);

        jButtonVideoOptions.setText("Options");
        jButtonVideoOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVideoOptionsActionPerformed(evt);
            }
        });

        jProgressBarVideo.setString("");
        jProgressBarVideo.setStringPainted(true);

        jTableVideo.setAutoCreateColumnsFromModel(false);
        jTableVideo.setModel(new jamuz.process.video.TableModelVideo());
        jTableVideo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableVideo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableVideo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableVideoMousePressed(evt);
            }
        });
        jScrollPaneCheckTags1.setViewportView(jTableVideo);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/selected.png"))); // NOI18N
        jLabel5.setToolTipText("");
        jLabel5.setMaximumSize(new java.awt.Dimension(21, 22));
        jLabel5.setMinimumSize(new java.awt.Dimension(21, 22));
        jLabel5.setPreferredSize(new java.awt.Dimension(21, 22));

        triStateSelected.setText("");
        triStateSelected.setToolTipText("");
        triStateSelected.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triStateSelectedStateChanged(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/eye.png"))); // NOI18N
        jLabel6.setToolTipText("");
        jLabel6.setMaximumSize(new java.awt.Dimension(21, 22));
        jLabel6.setMinimumSize(new java.awt.Dimension(21, 22));
        jLabel6.setPreferredSize(new java.awt.Dimension(21, 22));

        triStateWatched.setText("");
        triStateWatched.setToolTipText("");
        triStateWatched.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        triStateWatched.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triStateWatchedStateChanged(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/1starC.png"))); // NOI18N
        jLabel7.setToolTipText("");
        jLabel7.setMaximumSize(new java.awt.Dimension(21, 22));
        jLabel7.setMinimumSize(new java.awt.Dimension(21, 22));
        jLabel7.setPreferredSize(new java.awt.Dimension(21, 22));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/wishlist_add.png"))); // NOI18N
        jLabel8.setToolTipText("");
        jLabel8.setMaximumSize(new java.awt.Dimension(21, 22));
        jLabel8.setMinimumSize(new java.awt.Dimension(21, 22));
        jLabel8.setPreferredSize(new java.awt.Dimension(21, 22));

        triStateWatchList.setText("");
        triStateWatchList.setToolTipText("");
        triStateWatchList.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        triStateWatchList.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triStateWatchListStateChanged(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/heart.png"))); // NOI18N
        jLabel9.setToolTipText("");
        jLabel9.setMaximumSize(new java.awt.Dimension(21, 22));
        jLabel9.setMinimumSize(new java.awt.Dimension(21, 22));
        jLabel9.setPreferredSize(new java.awt.Dimension(21, 22));

        triStateFavorite.setText("");
        triStateFavorite.setToolTipText("");
        triStateFavorite.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        triStateFavorite.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triStateFavoriteStateChanged(evt);
            }
        });

        triStateRated.setText("");
        triStateRated.setToolTipText("");
        triStateRated.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        triStateRated.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triStateRatedStateChanged(evt);
            }
        });

        jComboBoxFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFilterActionPerformed(evt);
            }
        });

        jButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/update.png"))); // NOI18N
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/file_manager.png"))); // NOI18N
        jLabel10.setToolTipText("");
        jLabel10.setMaximumSize(new java.awt.Dimension(21, 22));
        jLabel10.setMinimumSize(new java.awt.Dimension(21, 22));
        jLabel10.setPreferredSize(new java.awt.Dimension(21, 22));

        triStateLocal.setText("");
        triStateLocal.setToolTipText("");
        triStateLocal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        triStateLocal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triStateLocalStateChanged(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/hd_ready.png"))); // NOI18N
        jLabel11.setToolTipText("");
        jLabel11.setMaximumSize(new java.awt.Dimension(21, 22));
        jLabel11.setMinimumSize(new java.awt.Dimension(21, 22));
        jLabel11.setPreferredSize(new java.awt.Dimension(21, 22));

        triStateHd.setText("");
        triStateHd.setToolTipText("");
        triStateHd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        triStateHd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triStateHdStateChanged(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/movies.png"))); // NOI18N
        jLabel12.setToolTipText("");
        jLabel12.setMaximumSize(new java.awt.Dimension(21, 22));
        jLabel12.setMinimumSize(new java.awt.Dimension(21, 22));
        jLabel12.setPreferredSize(new java.awt.Dimension(21, 22));

        triStateMovies.setText("");
        triStateMovies.setToolTipText("");
        triStateMovies.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        triStateMovies.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triStateMoviesStateChanged(evt);
            }
        });

        jCheckBoxVideoTheMovieDb.setText("TheMovieDb");

        jButtonVideoCleanup.setText("Cleanup ...");
        jButtonVideoCleanup.setToolTipText("");
        jButtonVideoCleanup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVideoCleanupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(triStateSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(triStateMovies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 47, Short.MAX_VALUE)
                        .addComponent(jComboBoxFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(triStateLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(triStateHd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(triStateWatched, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(triStateRated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(triStateWatchList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(triStateFavorite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButtonVideoList)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxVideoGet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBoxVideoMove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxVideoTheMovieDb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonVideoCleanup)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonVideoExport)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonVideoOptions))
                    .addComponent(jScrollPaneCheckTags1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jProgressBarVideo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonVideoList)
                        .addComponent(jCheckBoxVideoGet)
                        .addComponent(jCheckBoxVideoMove)
                        .addComponent(jCheckBoxVideoTheMovieDb))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonVideoExport, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonVideoCleanup)
                        .addComponent(jButtonVideoOptions)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(triStateRated, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(triStateWatched, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(triStateWatchList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(triStateFavorite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(triStateLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(triStateHd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(triStateSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCheckTags1, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(triStateMovies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jSplitPane1.setRightComponent(jPanel1);

        javax.swing.GroupLayout jPanelVideoLayout = new javax.swing.GroupLayout(jPanelVideo);
        jPanelVideo.setLayout(jPanelVideoLayout);
        jPanelVideoLayout.setHorizontalGroup(
            jPanelVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVideoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1)
                .addGap(0, 0, 0))
        );
        jPanelVideoLayout.setVerticalGroup(
            jPanelVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 856, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelVideo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelVideo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonVideoExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVideoExportActionPerformed

        DialogVideoExport.main(null);
    }//GEN-LAST:event_jButtonVideoExportActionPerformed

	/**
	 *
	 */
	public static void export()  {
        List<VideoAbstract> filestoExport = processVideo.getTableModel().getFiles().stream()
				.filter(video -> video.isSelected()).collect(Collectors.toList());
		
		if(filestoExport.size()>0) {
			triStateSelected.setState(State.SELECTED);
			enableProcess(false);
			processVideo.export();
		} else {
			Popup.warning("You should select some files to export first");
		}
    }
    
	/**
	 *
	 * @param enable
	 */
	public static void enableProcess(boolean enable) {
        jButtonVideoList.setEnabled(enable);
        jCheckBoxVideoGet.setEnabled(enable);
        jButtonVideoCleanup.setEnabled(enable);
		jCheckBoxVideoTheMovieDb.setEnabled(enable);
        jButtonVideoExport.setEnabled(enable);
        jButtonVideoOptions.setEnabled(enable);
        triStateSelected.setEnabled(enable);
        triStateWatched.setEnabled(enable);
        triStateWatchList.setEnabled(enable);
        triStateFavorite.setEnabled(enable);
        triStateRated.setEnabled(enable);
        triStateHd.setEnabled(enable);
        triStateLocal.setEnabled(enable);
        triStateMovies.setEnabled(enable);
        jComboBoxFilter.setEnabled(enable);
        jButtonRefresh.setEnabled(enable);

        jListVideoGenre.setEnabled(enable);
        jTableVideo.setEnabled(enable);
        jListVideoMppaRating.setEnabled(enable);
        jListVideoRating.setEnabled(enable);
        
        if(enable) {
            filterVideo();
        }
    }
    
    private void jListVideoGenreValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListVideoGenreValueChanged
        if(jListVideoGenre.getSelectedValue()!=null && !evt.getValueIsAdjusting()) {
            filterVideo.displayByGenre((String) jListVideoGenre.getSelectedValue());
            filterVideo(false);
        }
    }//GEN-LAST:event_jListVideoGenreValueChanged

    private void jButtonVideoListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVideoListActionPerformed
        listDb();
    }//GEN-LAST:event_jButtonVideoListActionPerformed

	private void listDb() {
		triStateSelected.setState(State.ALL);
        triStateWatched.setState(State.ALL);
        triStateWatchList.setState(State.ALL);
        triStateFavorite.setState(State.ALL);
        triStateRated.setState(State.ALL);
        triStateHd.setState(State.ALL);
        triStateLocal.setState(State.ALL);
        triStateMovies.setState(State.ALL);
        enableProcess(false);
        jTableVideo.setRowSorter(null);
        processVideo.listDb(jCheckBoxVideoMove.isSelected(), jCheckBoxVideoGet.isSelected(), jCheckBoxVideoTheMovieDb.isSelected());
	}

    private void jTableVideoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableVideoMousePressed
        // If Right mouse click, select the line under mouse
        if ( SwingUtilities.isRightMouseButton( evt ) )
        {
            Point p = evt.getPoint();
            int rowNumber = jTableVideo.rowAtPoint( p );
            ListSelectionModel model = jTableVideo.getSelectionModel();
            model.setSelectionInterval( rowNumber, rowNumber );
        }
    }//GEN-LAST:event_jTableVideoMousePressed

    private void jButtonVideoOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVideoOptionsActionPerformed
        DialogVideoOption.main(null);
    }//GEN-LAST:event_jButtonVideoOptionsActionPerformed

    private void jListVideoMppaRatingValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListVideoMppaRatingValueChanged
        if(jListVideoMppaRating.getSelectedValue()!=null && !evt.getValueIsAdjusting()) {
            filterVideo.displayByMppaRating((String) jListVideoMppaRating.getSelectedValue());
            filterVideo(false);
        }
    }//GEN-LAST:event_jListVideoMppaRatingValueChanged

    private void triStateSelectedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triStateSelectedStateChanged
        TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
        if(!checkbox.getModel().isArmed()) {
            filterVideo.displaySelected(checkbox.getState());
            filterVideo();
        }
    }//GEN-LAST:event_triStateSelectedStateChanged

    private void triStateWatchedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triStateWatchedStateChanged
        TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
        if(!checkbox.getModel().isArmed()) {
            filterVideo.displayWatched(checkbox.getState());
            filterVideo();
        }
    }//GEN-LAST:event_triStateWatchedStateChanged

    private void triStateWatchListStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triStateWatchListStateChanged
        TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
        if(!checkbox.getModel().isArmed()) {
            filterVideo.displayWatchList(checkbox.getState());
            filterVideo();
        }
    }//GEN-LAST:event_triStateWatchListStateChanged

    private void triStateFavoriteStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triStateFavoriteStateChanged
        TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
        if(!checkbox.getModel().isArmed()) {
            filterVideo.displayFavorite(checkbox.getState());
            filterVideo();
        }
    }//GEN-LAST:event_triStateFavoriteStateChanged

    private void triStateRatedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triStateRatedStateChanged
         TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
        if(!checkbox.getModel().isArmed()) {
            filterVideo.displayRated(checkbox.getState());
            filterVideo();
        }
    }//GEN-LAST:event_triStateRatedStateChanged

    private void jComboBoxFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFilterActionPerformed
        filterVideoTriStates();
    }//GEN-LAST:event_jComboBoxFilterActionPerformed

    private void filterVideoTriStates() {
        if (jComboBoxFilter.getSelectedIndex() >= 0) {
            VideoFilter videoFilter = (VideoFilter) jComboBoxFilter.getSelectedItem();
            switch(videoFilter) {
                case ALL:
                    filterVideo(State.ALL, State.ALL, State.ALL, State.ALL, State.ALL, State.ALL);
                    break;
                case TOBERATED:
                    filterVideo(State.SELECTED, State.UNSELECTED, State.ALL, State.ALL, State.ALL, State.ALL);
                    break;
                case TOCHECK:
                    filterVideo(State.UNSELECTED, State.ALL, State.UNSELECTED, State.ALL, State.SELECTED, State.ALL);
                    break;
                case TOBEDELETED:
					//TODO VIDEO Filters: exclude "Returning Series" in TOBEDELETED
                    filterVideo(State.SELECTED, State.ALL, State.ALL, State.UNSELECTED, State.SELECTED, State.ALL);
                    break;
                case TORETRIEVE:
                    filterVideo(State.UNSELECTED, State.ALL, State.SELECTED, State.ALL, State.UNSELECTED, State.ALL);
                    break;
                case TORETRIEVEHD:
                    filterVideo(State.UNSELECTED, State.ALL, State.SELECTED, State.ALL, State.SELECTED, State.UNSELECTED);
                    break;
                case TOWATCH:
                    filterVideo(State.UNSELECTED, State.ALL, State.SELECTED, State.ALL, State.SELECTED, State.ALL);
					triStateMovies.setState(State.ALL);
                    break;
            }
        }
    }
    
    private void filterVideo(State watched, State rated, State watchlist, State favorite, State local, State hd) {
        triStateSelected.setState(State.ALL);
        triStateWatched.setState(watched);
        triStateRated.setState(rated);
        triStateWatchList.setState(watchlist);
        triStateFavorite.setState(favorite);
        triStateLocal.setState(local);
        triStateHd.setState(hd);
    }
    
    private enum VideoFilter {
        ALL(Inter.get("Label.All")),
        TOBERATED(Inter.get("Label.Video.TOBERATED")),
        TOCHECK(Inter.get("Label.Video.TOCHECK")),
        TOBEDELETED(Inter.get("Label.Video.TOBEDELETED")),
        TORETRIEVE(Inter.get("Label.Video.TORETRIEVE")),
        TORETRIEVEHD(Inter.get("Label.Video.TORETRIEVEHD")),
        TOWATCH(Inter.get("Label.Video.TOWATCH"));
        
        private final String display;
		private VideoFilter(String display) {
			this.display = display;
		}
		@Override
		public String toString() {
			return display;
		}
    }
    
    private void triStateLocalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triStateLocalStateChanged
        TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
        if(!checkbox.getModel().isArmed()) {
            filterVideo.displayLocal(checkbox.getState());
            filterVideo();
        }
    }//GEN-LAST:event_triStateLocalStateChanged

    private void triStateHdStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triStateHdStateChanged
        TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
        if(!checkbox.getModel().isArmed()) {
            filterVideo.displayHD(checkbox.getState());
            filterVideo();
        }
    }//GEN-LAST:event_triStateHdStateChanged

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        filterVideoTriStates();
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void triStateMoviesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triStateMoviesStateChanged
        TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
        if(!checkbox.getModel().isArmed()) {
            filterVideo.displayMovies(checkbox.getState());
            filterVideo();
        }
    }//GEN-LAST:event_triStateMoviesStateChanged

    private void jListVideoRatingValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListVideoRatingValueChanged
        if(jListVideoRating.getSelectedValue()!=null && !evt.getValueIsAdjusting()) {
            filterVideo.displayByRating((String) jListVideoRating.getSelectedValue());
            filterVideo(false);
        }
    }//GEN-LAST:event_jListVideoRatingValueChanged

    private void jCheckBoxVideoGetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxVideoGetItemStateChanged
        //Not allowing move if not getting db first
		//to avoid bad data (even if we can not be sure that retrieved kodi db is up-to-date)
		//+if we move without getting db, it won't be sent back
		jCheckBoxVideoMove.setEnabled(jCheckBoxVideoGet.isSelected());
    }//GEN-LAST:event_jCheckBoxVideoGetItemStateChanged

    private void jButtonVideoCleanupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVideoCleanupActionPerformed
        DialogVideoCleanup.main(processVideo.getTableModel().getFiles());
    }//GEN-LAST:event_jButtonVideoCleanupActionPerformed

    private static long getSpaceLeft(String pathOrFile) {
		if(!new File(pathOrFile).exists()) {
			return 0;
		}
        Path p = Paths.get(pathOrFile); 
        FileSystem fileSystem = FileSystems.getDefault();
        Iterable<FileStore> iterable = fileSystem.getFileStores();
        Iterator<FileStore> it = iterable.iterator();
        while(it.hasNext()) {
            try {
                FileStore fileStore = it.next();
                if (Files.getFileStore(p).equals(fileStore)) { 
                    return fileStore.getUsableSpace();
                }
            } catch (IOException ex) {
                Popup.error(ex);
            }
        }
        return 0;
    }

    private static ProcessVideo processVideo;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupSelected;
    private javax.swing.ButtonGroup btnGroupWatched;
    private static javax.swing.JButton jButtonRefresh;
    private static javax.swing.JButton jButtonVideoCleanup;
    private static javax.swing.JButton jButtonVideoExport;
    private static javax.swing.JButton jButtonVideoList;
    private static javax.swing.JButton jButtonVideoOptions;
    private static javax.swing.JCheckBox jCheckBoxVideoGet;
    private static javax.swing.JCheckBox jCheckBoxVideoMove;
    private static javax.swing.JCheckBox jCheckBoxVideoTheMovieDb;
    private static javax.swing.JComboBox jComboBoxFilter;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private static javax.swing.JLabel jLabelVideoStatus;
    private static javax.swing.JList jListVideoGenre;
    private static javax.swing.JList jListVideoMppaRating;
    private static javax.swing.JList jListVideoRating;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelVideo;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JProgressBar jProgressBarVideo;
    private static javax.swing.JScrollPane jScrollPaneCheckTags1;
    private javax.swing.JScrollPane jScrollPaneSelectGenre1;
    private javax.swing.JScrollPane jScrollPaneSelectGenre2;
    private javax.swing.JScrollPane jScrollPaneSelectGenre4;
    private javax.swing.JSplitPane jSplitPane1;
    private static javax.swing.JTable jTableVideo;
    private static jamuz.gui.swing.TriStateCheckBox triStateFavorite;
    private static jamuz.gui.swing.TriStateCheckBox triStateHd;
    private static jamuz.gui.swing.TriStateCheckBox triStateLocal;
    private static jamuz.gui.swing.TriStateCheckBox triStateMovies;
    private static jamuz.gui.swing.TriStateCheckBox triStateRated;
    private static jamuz.gui.swing.TriStateCheckBox triStateSelected;
    private static jamuz.gui.swing.TriStateCheckBox triStateWatchList;
    private static jamuz.gui.swing.TriStateCheckBox triStateWatched;
    // End of variables declaration//GEN-END:variables

}
