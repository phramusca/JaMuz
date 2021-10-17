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

import jamuz.gui.swing.TableModelGeneric;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */

public class TableModelBook extends TableModelGeneric {

    private List<Book> files;
    private int nbSelected;
	private long lengthAll;
	private long lengthSelected;
    
    /**
	 * Create the table model
	 */
	public TableModelBook() {
        this.files = new ArrayList<>();
        
        //Set column names
        this.setColumnNames(new String [] {
            "", //NOI18N
            "", //NOI18N
            Inter.get("Tag.Title"), 
			"Author", //NOI18N
            "PubDate", 
        });

		this.fireTableStructureChanged();
	}

    @Override
    public boolean isCellEditable(int row, int col){
		if(col==0) { //Selected checkbox
            return true;
        }
//        else if(col==7 || col==8 || col==9) { //TheMovieDb: Rating, WatchList and Favorite
//            Book book = files.get(row);
//            if(book.getMyMovieDb().getId()!=0) {
//                return true;
//            }
//        }
		return false;
    }
    
	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isCellEnabled(int row, int col) {
        return false;
    }
    
	/**
	 * Return list of files
	 * @return
	 */
	public List<Book> getBooks() {
		return files;
	}

    /**
     * get list of files
     * @param index
     * @return
     */
    public Book getFile(int index) {
        return this.files.get(index);
    }

	
	 /**
     * Return files' total length
     * @return
     */
    public long getLengthAll() {
        return lengthAll;
    }
    /**
     *
     * @return
     */
    public int getNbSelected() {
        return nbSelected;
    }
    
    @Override
    public int getRowCount() {
        return this.files.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = files.get(rowIndex);

        switch (columnIndex) {
			
			case 0: return book.isSelected();
            case 1: return (book.getThumbnail(false)!= null ? book.getThumbnail(false): new ImageIcon());
            case 2: return book; //need to return object for the filter (.toString() is auto anyway)
            case 3: return book.getAuthor_sort();
            case 4: return book.getPubdate();
		}
        return null;
    }
    
    /**
     * Sets given cell value
	 * @param value
	 * @param row
	 * @param col
	 */
    @Override
    public void setValueAt(Object value, int row, int col) {
		Book book = files.get(row);

        switch (col) {
            case 0: 
                select(book, (boolean)value);
                break;
//            case 7:
//                book.setRating((VideoRating) value);
//                break;
		}
    }

	/**
	 *
	 * @param book
	 * @param selected
	 */
	public void select(Book book, boolean selected) {
        book.setSelected(selected);
        if(selected) {
			lengthSelected+=book.getLength().getLength(); //Add 
            nbSelected+=1;
        }
        else {
			lengthSelected-=book.getLength().getLength(); //Substract
            nbSelected-=1;
        }
        PanelBook.diplayLength(); //as it has changed, easier than a listener
    }
    
	/**
     * Return selected file's length
     * @return
     */
    public long getLengthSelected() {
        return lengthSelected;
    }
	
    /**
	* Returns given column's data class
    * @param col
     * @return 
    */
    @Override
    public Class getColumnClass(int col){
        //Note: since all data on a given column are all the same
		//we return data class of given column first row
        return this.getValueAt(0, col).getClass();
    }

	/**
	 * Clears the table
	 */
	public void clear() {
        this.files = new ArrayList<>();
		this.lengthAll=0;
        this.nbSelected=0;
		this.lengthSelected=0;
        //Update table
        this.fireTableDataChanged();
    }
	
	/**
    * Add a row to the table
	 * @param file
    */
    public void addRow(Book file){
		this.files.add(file);
		this.lengthAll+=file.getLength().getLength();
		//Update table
		this.fireTableDataChanged();
    }

	/**
	 *
	 * @param file
	 */
	public void removeRow(Book file){
		this.files.remove(file);
		this.lengthAll-=file.getLength().getLength();
		//Update table
		this.fireTableDataChanged();
    }
    
    private static LoadIconsThread tLoadIcons;

    /**
     * Load thimbnails
     */
    public void loadThumbnails() {
        //Stop any previously running thread and wait for it to end
		if(tLoadIcons!=null) {
            tLoadIcons.abort();
			try {
				tLoadIcons.join();
			} catch (InterruptedException ex) {
				Popup.error(ex);
			}
		}

		// Démarrage du thread
		tLoadIcons = new LoadIconsThread("Thread.TableModelVideo.loadThumbnails");
		tLoadIcons.start();
	}
    
    private class LoadIconsThread extends ProcessAbstract {

        LoadIconsThread(String name) {
            super(name);
        }
		
        @Override
		public void run() {
            try {
                loadIcons();
            }
            catch (InterruptedException ex) {
                Popup.info(Inter.get("Msg.Process.Aborted"));  //NOI18N
            } 
		}
	}
    
    private void loadIcons() throws InterruptedException {
        for(Book book : this.files) {
            tLoadIcons.checkAbort();
            book.getThumbnail(true);
            this.fireTableDataChanged();
        }
    }
    
}
