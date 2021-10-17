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

package jamuz.process.book;

import jamuz.process.video.*;
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
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
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
import java.awt.Frame;
import java.util.stream.Collectors;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */


public class PanelBook extends javax.swing.JPanel {

	/**
	 *
	 */
	protected static ProgressBar progressBar;

	/**
	 *
	 */
	protected static ProgressBar progressBarTimer;
	private final Frame parent;

    /**
     * Creates new form PanelVideo
	 * @param parent
     */
    public PanelBook(Frame parent) {
        initComponents();
        initExtended();
		this.parent = parent;
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
        jButtonBookExport.setEnabled(false);
        
        processBook = new ProcessBook("Thread.PanelBook.ProcessBook");
        jTableBook.setModel(processBook.getTableModel());
        jTableBook.setRowSorter(null);
		//Adding columns from model. Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableBook.createDefaultColumnsFromModel();
		
        //	0:  "isSelected"
		setColumn(0, 20);
        setColumnIcon(0, "selected.png");
		//	1:  "getThumbnails"
        setColumn(1, IconBufferBook.ICON_WIDTH);
		//	2:  "getTitle"
		//	3:  "Author"
		setColumn(3, 150);
        //	4:  PubDate
        setColumn(4, 80);
		
        jTableBook.setRowHeight(IconBufferBook.ICON_HEIGHT);
        
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setVerticalAlignment(SwingConstants.TOP);
        jTableBook.getColumnModel().getColumn(2).setCellRenderer(renderer);
		
		progressBar = (ProgressBar)jProgressBarVideo;
        progressBarTimer = (ProgressBar)jProgressBarVideoTimer;
        
        //Add a list renderer to display genre icons
        //TODO: Make a class for video genre (or make this one or the album one or both more generic)
//        jListBookTag.setBackground(Color.WHITE);
//        ListRendererGenre rendererGenre = new ListRendererGenre();
//        rendererGenre.setPreferredSize(new Dimension(0, IconBufferGenre.iconSize));
//        jListBookTag.setCellRenderer(rendererGenre);
        
        //Menu listener
        addMenuItem(Inter.get("Button.Open"));  //NOI18N
        //Add links menu items
        File f = Jamuz.getFile("BookLinks.txt", "data");
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
        jTableBook.addMouseListener(popupListener);
        
//        List<String> ratings = Arrays.asList(
//                "0", "1", "2", "3", "4", "5",
//                "6", "7", "8", "9", "10");
//        jListVideoRating.setModel(getModel(ratings, false));

//		listDb();
	}
    
    class OpenUrlAction extends AbstractAction {
        
        private final String url;
        
        OpenUrlAction(String text, String url) {
            super(text, null);
            this.url = url;
//            putValue(SHORT_DESCRIPTION, desc);
//            putValue(MNEMONIC_KEY, mnemonic);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Book book = getSelected();
			if(book!=null) {
				Desktop.openBrowser(url
						.replaceAll("<title>", book.getTitle())
						.replaceAll("<author>", book.getAuthor())
				);
			}
        }
    }
    
    class JComponentTableCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
          return (Component) value;
        }
      }
    
    private void setColumn(int index, int width) {
        TableColumn column = jTableBook.getColumnModel().getColumn(index);
		column.setMinWidth(width);
        column.setPreferredWidth(width);
        column.setMaxWidth(width*3);
    }
    
    private void setColumnIcon(int col, String icon) {
        TableCellRenderer rendererHeader = new JComponentTableCellRenderer();
//        Border headerBorder = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0));
        Border headerBorder = javax.swing.BorderFactory.createEtchedBorder();
//                UIManager.getBorder("TableHeader.cellBorder");
        TableColumn column = jTableBook.getColumnModel().getColumn(col);
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
    
    ActionListener menuListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem)(e.getSource());
            String sourceTxt=source.getText();
			if(sourceTxt.equals(Inter.get("Button.Open"))) { //NOI18N
                menuBookOpen();
            }
            else {
                Popup.error("Unknown menu item: " + sourceTxt); //NOI18N
            }
        }
    };

	private void menuBookOpen() {
        Book book = getSelected();
        if(book!=null) {
			Desktop.openFolder(book.getFullPath());
        }
    }
	
    private static TableRowSorter<TableModelBook> tableSorter;
    private static final TableRowFilterBook filterBook= new TableRowFilterBook();
    
    private static void filterBook() {
        filterBook(true);
    }
    
    private static void filterBook(boolean fillLists) {
        //Enable row tableSorter (cannot be done if model is empty)
        if(processBook.getTableModel().getRowCount()>0) {
            //Enable auto sorter
            jTableBook.setAutoCreateRowSorter(true);
            //Get sorter
            tableSorter = new TableRowSorter<>(processBook.getTableModel());
            jTableBook.setRowSorter(tableSorter);
//            //Sort by title, author (Debug display problem before enabling)
//            List <RowSorter.SortKey> sortKeys = new ArrayList<>();
//            sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
//            sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
//            tableSorter.setSortKeys(sortKeys);
            //Désactive le tri pour
            tableSorter.setSortable(0, false); // Checkbox
            tableSorter.setSortable(1, false); // Thumbnail
            //Filter, Apply current filter
            tableSorter.setRowFilter(filterBook);

            if(fillLists) {
                List<String> tags=new ArrayList<>();
                List<String> ratings = new ArrayList<>();
                int index;
                for(int i = 0; i < jTableBook.getRowCount(); i++) {
                    index = jTableBook.convertRowIndexToModel(i);
                    Book book = processBook.getTableModel().getFile(index);
                    //Add genres to the list
                    for(String tag : book.getTags()) {
                        if(!tags.contains(tag)) {
                            tags.add(tag);
                        }
                    }
                    //Add rating to the list
                    if(!ratings.contains(book.getRating())) {
                        ratings.add(book.getRating());
                    }
                }

                jListBookTag.setModel(getModel(tags));
                jListVideoRating.setModel(getModel(ratings));
                
                jListBookTag.setSelectedIndex(0);
                jListVideoRating.setSelectedIndex(0);
            }
        }
        else {
            jTableBook.setAutoCreateRowSorter(false);
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
        long selected=processBook.getTableModel().getLengthSelected();
        		
		long spaceLeft=getSpaceLeft(Jamuz.getOptions().get("book.destination"));
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
    private Book getSelected() {
        //Getting selected File 		
		int selectedRow = jTableBook.getSelectedRow(); 			
		if(selectedRow>=0) { 	
			//convert to model index (if sortable model) 		
			selectedRow = jTableBook.convertRowIndexToModel(selectedRow); 
            Book book = processBook.getTableModel().getFile(selectedRow);
            return book;		
		}
        else { 			
            Popup.info(Inter.get("Error.YouMustSelectArow")); 		  //NOI18N
            return null;
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
        jListBookTag = new javax.swing.JList();
        jLabelVideoStatus = new javax.swing.JLabel();
        jScrollPaneSelectGenre4 = new javax.swing.JScrollPane();
        jListVideoRating = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jButtonBookExport = new javax.swing.JButton();
        jButtonVideoList = new javax.swing.JButton();
        jCheckBoxVideoGet = new javax.swing.JCheckBox();
        jButtonVideoOptions = new javax.swing.JButton();
        jProgressBarVideo = new jamuz.gui.swing.ProgressBar();
        jScrollPaneCheckTags1 = new javax.swing.JScrollPane();
        jTableBook = new jamuz.gui.swing.TableHorizontal();
        jProgressBarVideoTimer = new jamuz.gui.swing.ProgressBar();
        jLabel5 = new javax.swing.JLabel();
        triStateSelected = new jamuz.gui.swing.TriStateCheckBox();

        jSplitPane1.setDividerLocation(200);

        jListBookTag.setModel(new DefaultListModel());
        jListBookTag.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListBookTag.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListBookTagValueChanged(evt);
            }
        });
        jScrollPaneSelectGenre1.setViewportView(jListBookTag);

        jLabelVideoStatus.setText(" "); // NOI18N
        jLabelVideoStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
            .addComponent(jLabelVideoStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addComponent(jScrollPaneSelectGenre4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jScrollPaneSelectGenre1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelVideoStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneSelectGenre4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneSelectGenre1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel2);

        jButtonBookExport.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        jButtonBookExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/external.png"))); // NOI18N
        jButtonBookExport.setText(Inter.get("Button.Export")+" ..."); // NOI18N
        jButtonBookExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBookExportActionPerformed(evt);
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

        jButtonVideoOptions.setText("Options");
        jButtonVideoOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVideoOptionsActionPerformed(evt);
            }
        });

        jProgressBarVideo.setString("");
        jProgressBarVideo.setStringPainted(true);

        jTableBook.setAutoCreateColumnsFromModel(false);
        jTableBook.setModel(new jamuz.process.book.TableModelBook());
        jTableBook.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableBook.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableBookMousePressed(evt);
            }
        });
        jScrollPaneCheckTags1.setViewportView(jTableBook);

        jProgressBarVideoTimer.setString("");
        jProgressBarVideoTimer.setStringPainted(true);

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCheckTags1, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButtonVideoList))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(triStateSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jProgressBarVideo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jProgressBarVideoTimer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCheckBoxVideoGet)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonBookExport)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonVideoOptions))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonVideoOptions, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonVideoList)
                        .addComponent(jCheckBoxVideoGet))
                    .addComponent(jButtonBookExport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBarVideoTimer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jProgressBarVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(triStateSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
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

    private void jButtonBookExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBookExportActionPerformed

        DialogBookExport.main(parent);
    }//GEN-LAST:event_jButtonBookExportActionPerformed

	/**
	 *
	 */
	public static void export()  {
        List<Book> filestoExport = processBook.getTableModel().getBooks().stream()
				.filter(book -> book.isSelected()).collect(Collectors.toList());
		
		if(filestoExport.size()>0) {
			triStateSelected.setState(State.SELECTED);
			enableProcess(false);
			processBook.export();
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
        jButtonBookExport.setEnabled(enable);
        jButtonVideoOptions.setEnabled(enable);
        triStateSelected.setEnabled(enable);
        jListBookTag.setEnabled(enable);
        jTableBook.setEnabled(enable);
        jListVideoRating.setEnabled(enable);
        
        if(enable) {
            filterBook();
        }
    }
    
    private void jListBookTagValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListBookTagValueChanged
        if(jListBookTag.getSelectedValue()!=null && !evt.getValueIsAdjusting()) {
            filterBook.displayByTag((String) jListBookTag.getSelectedValue());
            filterBook(false);
        }
    }//GEN-LAST:event_jListBookTagValueChanged

    private void jButtonVideoListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVideoListActionPerformed
        listDb();
    }//GEN-LAST:event_jButtonVideoListActionPerformed

	private void listDb() {
		triStateSelected.setState(State.ALL);
        enableProcess(false);
        jTableBook.setRowSorter(null);
        processBook.listDb(jCheckBoxVideoGet.isSelected());
	}
	
    private void jTableBookMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBookMousePressed
        // If Right mouse click, select the line under mouse
        if ( SwingUtilities.isRightMouseButton( evt ) )
        {
            Point p = evt.getPoint();
            int rowNumber = jTableBook.rowAtPoint( p );
            ListSelectionModel model = jTableBook.getSelectionModel();
            model.setSelectionInterval( rowNumber, rowNumber );
        }
    }//GEN-LAST:event_jTableBookMousePressed

    private void jButtonVideoOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVideoOptionsActionPerformed
        DialogBookOption.main(parent);
    }//GEN-LAST:event_jButtonVideoOptionsActionPerformed

    private void triStateSelectedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triStateSelectedStateChanged
        TriStateCheckBox checkbox = (TriStateCheckBox) evt.getSource();
        if(!checkbox.getModel().isArmed()) {
            filterBook.displaySelected(checkbox.getState());
            filterBook();
        }
    }//GEN-LAST:event_triStateSelectedStateChanged
   
    private enum VideoFilter {
        ALL(Inter.get("Label.All")),
        TOBERATED(Inter.get("Label.Video.TOBERATED")),
        TOBEDELETED(Inter.get("Label.Video.TOBEDELETED")),
        TORETRIEVE(Inter.get("Label.Video.TORETRIEVE"));
        
        private final String display;
		private VideoFilter(String display) {
			this.display = display;
		}
		@Override
		public String toString() {
			return display;
		}
    }
    
    private void jListVideoRatingValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListVideoRatingValueChanged
        if(jListVideoRating.getSelectedValue()!=null && !evt.getValueIsAdjusting()) {
            filterBook.displayByRating((String) jListVideoRating.getSelectedValue());
            filterBook(false);
        }
    }//GEN-LAST:event_jListVideoRatingValueChanged

    
    private static long getSpaceLeft(String pathOrFile) {
		if(!new File(pathOrFile).exists()) {
			return 0;
		}
        Path p = Paths.get(pathOrFile); // where you want to write
        FileSystem fileSystem = FileSystems.getDefault();
        Iterable<FileStore> iterable = fileSystem.getFileStores();

        Iterator<FileStore> it = iterable.iterator(); // iterate the FileStore instances
        while(it.hasNext()) {
            try {
                FileStore fileStore = it.next();
                if (Files.getFileStore(p).equals(fileStore)) { // your Path belongs to this FileStore
                    return fileStore.getUsableSpace(); // or maybe getUnallocatedSpace()
                }
            } catch (IOException ex) {
                Popup.error(ex);
            }
        }
        return 0;
    }

    private static ProcessBook processBook;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupSelected;
    private javax.swing.ButtonGroup btnGroupWatched;
    private static javax.swing.JButton jButtonBookExport;
    private static javax.swing.JButton jButtonVideoList;
    private static javax.swing.JButton jButtonVideoOptions;
    private static javax.swing.JCheckBox jCheckBoxVideoGet;
    private javax.swing.JLabel jLabel5;
    private static javax.swing.JLabel jLabelVideoStatus;
    private static javax.swing.JList jListBookTag;
    private static javax.swing.JList jListVideoRating;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelVideo;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JProgressBar jProgressBarVideo;
    private javax.swing.JProgressBar jProgressBarVideoTimer;
    private static javax.swing.JScrollPane jScrollPaneCheckTags1;
    private javax.swing.JScrollPane jScrollPaneSelectGenre1;
    private javax.swing.JScrollPane jScrollPaneSelectGenre4;
    private javax.swing.JSplitPane jSplitPane1;
    private static javax.swing.JTable jTableBook;
    private static jamuz.gui.swing.TriStateCheckBox triStateSelected;
    // End of variables declaration//GEN-END:variables

}
