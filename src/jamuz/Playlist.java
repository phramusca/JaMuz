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

package jamuz;

import jamuz.process.check.FolderInfo.CheckedFlag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import jamuz.utils.Inter;
import java.util.Objects;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Playlist implements Comparable {

	private boolean hidden;

	public boolean isHidden() {
		return hidden;
	}
	
	/**
	 * Playlist ID
	 */
	private int id;

    public int getId() {
        return id;
    }
	
    /**
	 * Name
	 */
	private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	/**
	 * Limit number of results ?
	 */
	private boolean limit;

    public boolean isLimit() {
        return limit;
    }

    public void setLimit(boolean limit) {
        this.limit = limit;
    }
    
	/**
	 * Limit value (if limit=true)
	 */
	private int limitValue;

    public int getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(int limitValue) {
        this.limitValue = limitValue;
    }
    
	/**
	 * Limit unit: length, size, number of files (if limit=true)
	 */
	private LimitUnit limitUnit;

    public LimitUnit getLimitUnit() {
        return limitUnit;
    }

    public void setLimitUnit(LimitUnit limitUnit) {
        this.limitUnit = limitUnit;
    }
    
	/**
	 * Random results ?
	 */
	private boolean random;

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }
    
    /**
	 * Not yet used. See XBMC playlists for information.
	 */
	private Type type; //TODO: Eventually, use this

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
	/**
	 * Match type for filters (AND, OR, INDE)
	 */
	private Match match;

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
    
	private final ArrayList<Filter> filters;
	private final ArrayList<Order> orders;
    
	/**
	 * Creates a playlist from database
	 * @param id
	 * @param name
	 * @param limit
	 * @param limitValue
	 * @param limitUnit
	 * @param random
	 * @param type
	 * @param match
	 */
	public Playlist(int id, String name, boolean limit, int limitValue, LimitUnit limitUnit, boolean random, 
            Type type, Match match, boolean hidden) {
		this.id=id;
		this.hidden=hidden;
		this.name=name;
		this.limit = limit;
		this.limitValue=limitValue;
		this.limitUnit = limitUnit;
		this.random = random;
        this.type=type;
        this.match=match;
		this.filters = new ArrayList<>();
        this.orders = new ArrayList<>();
	}
	
	/**
	 * Updates playlist in database
	 * @return
	 */
	public boolean update() {
		return Jamuz.getDb().updatePlaylist(this);
	}
	
	/**
	 * Inserts playlist in database
	 * @return
	 */
	public boolean insert() {
		return Jamuz.getDb().insertPlaylist(this);
	}
	
	/**
	 * Deletes playlist from database
	 * @return
	 */
	public boolean delete() {
		return Jamuz.getDb().deletePlaylist(id);
	}

	@Override
	public String toString() {
		
//		String output = this.name;
//		if(this.limit) {
//			output += " (" + this.limitValue + " " + this.limitUnit; //NOI18N //NOI18N
//			if(this.random) {
//				output += ", random"; //NOI18N
//			}
//			output += ")"; //NOI18N
//		}
        //toString is used in player combobox and using the above will cause mismath with android remote
        //Moreover better to let user choose its preffered name
		return this.getName();
	}

    @Override
    public int compareTo(Object o) {
        return (this.name.compareTo(((Playlist) o).name));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Playlist other = (Playlist) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
    
    private static class FileInfoIntComparator implements Comparator {

        private final ArrayList<Order> orders;
        
        public FileInfoIntComparator(ArrayList<Order> orders) {
            this.orders = orders;
        }
        
        @Override
        public int compare(Object o1, Object o2) {
            
            FileInfoInt f1 = (FileInfoInt) o1;
            FileInfoInt f2 = (FileInfoInt) o2;
            int i;
            for(Order order : this.orders) {
                i=0;
                switch(order.field) {
                    case GENRE: 
                        i = order.desc ? f2.genre.compareTo(f1.genre) : f1.genre.compareTo(f2.genre);
                        if(i!=0) { return i; }
                    case RATING: 
                        if (f1.rating < f2.rating) i = order.desc ? 1 : -1;
                        if (f1.rating > f2.rating) i = order.desc ? -1 : 1;
                         if(i!=0) { return i; }
                    case ALBUMRATING:
                        if (f1.getAlbumRating() < f2.getAlbumRating()) i = order.desc ? 1 : -1;
                        if (f1.getAlbumRating() > f2.getAlbumRating()) i = order.desc ? -1 : 1;
                         if(i!=0) { return i; }
                    case PERCENTRATED:
                        if (f1.getPercentRated()< f2.getPercentRated()) i = order.desc ? 1 : -1;
                        if (f1.getPercentRated() > f2.getPercentRated()) i = order.desc ? -1 : 1;
                         if(i!=0) { return i; }
                    case PLAYLIST: 
                        //not handled, not displayed in GUI anyway
                    case ALBUM: 
                        i = order.desc ? f2.getAlbum().compareTo(f1.getAlbum()) : f1.getAlbum().compareTo(f2.getAlbum());
                        if(i!=0) { return i; }
                    case ARTIST: 
                        i = order.desc ? f2.getArtist().compareTo(f1.getArtist()) : f1.getArtist().compareTo(f2.getArtist());
                        if(i!=0) { return i; }
                    case ALBUMARTIST: 
                        i = order.desc ? f2.albumArtist.compareTo(f1.albumArtist) : f1.albumArtist.compareTo(f2.albumArtist);
                        if(i!=0) { return i; }
                    case TITLE: 
                        i = order.desc ? f2.title.compareTo(f1.title) : f1.title.compareTo(f2.title);
                        if(i!=0) { return i; }
                    case YEAR: 
                        i = order.desc ? f2.year.compareTo(f1.year) : f1.year.compareTo(f2.year);
                        if(i!=0) { return i; }
                    case TRACKNO: 
                        if (f1.trackNo < f2.trackNo) i = order.desc ? 1 : -1;
                        if (f1.trackNo > f2.trackNo) i = order.desc ? -1 : 1;
                         if(i!=0) { return i; }
                    case PLAYCOUNTER: 
                        if (f1.playCounter < f2.playCounter) i = order.desc ? 1 : -1;
                        if (f1.playCounter > f2.playCounter) i = order.desc ? -1 : 1;
                         if(i!=0) { return i; }
                    case LASTPLAYED: 
                        i = order.desc ? f2.lastPlayed.compareTo(f1.lastPlayed) : f1.lastPlayed.compareTo(f2.lastPlayed);
                        if(i!=0) { return i; }
                }
            }
            return 0; //By default: no sorting if none requested
        }
    }
    
	/**
	 * Get files for that playlist
	 * @param fileInfoList
	 * @return
	 */
	public boolean getFiles(ArrayList<FileInfoInt> fileInfoList) {

        ArrayList<FileInfoInt> fileInfoListTemp = new ArrayList<>();
        
        if(this.match.equals(Match.Inde)) { //Allows each sub-playlist to be limited (in "OR" mode limit is not performed in sub_playlists)
            ArrayList<Playlist> nonRandomLists = new ArrayList<>();
            ArrayList<Playlist> randomLists = new ArrayList<>();
            ArrayList<Playlist> lists = new ArrayList<>();
         
            for(Filter filter : this.getFilters()) {
                if(filter.field.equals(Field.PLAYLIST)) {
                    //Get playlist from that playlist
                    Playlist playlist = Jamuz.getPlaylist(Integer.parseInt(filter.value));
                    if(playlist.random) {
                        randomLists.add(playlist);
                    }
                    else {
                        nonRandomLists.add(playlist);
                    }
                }
                //Else, we should not have any other filters in this mode as per GUI
            }
            lists.addAll(nonRandomLists);
            lists.addAll(randomLists);
            for(Playlist playlist : lists) {
                    ArrayList<FileInfoInt> fileInfoListTempSubPlaylist = new ArrayList<>();
                    if(!playlist.getFiles(fileInfoListTempSubPlaylist)) {
                        return false;
                    }
                    fileInfoListTemp.addAll(fileInfoListTempSubPlaylist);
            }
            //Remove potential duplicates 
//            fileInfoListTemp = new ArrayList(new HashSet(fileInfoListTemp)); //no order
            fileInfoListTemp = new ArrayList(new LinkedHashSet(fileInfoListTemp)); //If you need to preserve the order use 'LinkedHashSet'

            // Order by
            if(this.random) {
                Collections.shuffle(fileInfoListTemp);
            }
            else {
                Collections.sort(fileInfoListTemp, new FileInfoIntComparator(this.orders));
            }
            
        }
        else {
            String sql = "SELECT F.*, P.strPath, P.checked, P.copyRight, P.albumRating, P.percentRated "
            + "FROM file F " //NOI18N //NOI18N
            + "JOIN (" +
                "	SELECT path.*, ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, \n" +
                "	ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated\n" +
                "	FROM path JOIN file ON path.idPath=file.idPath GROUP BY path.idPath \n" +
                ") P ON F.idPath=P.idPath "
            + "WHERE F.deleted=0 ";  //NOI18N //NOI18N
            sql += this.getSqlWhere(); //NOI18N
            Jamuz.getLogger().finest(sql);
            
             //Get files for current playlist
            if(!Jamuz.getDb().getFiles(fileInfoListTemp, sql)) {
                return false;
            }
        }   

        //Limit
        if(this.limit && this.match.equals(Match.Inde) && this.limitUnit.equals(LimitUnit.files)) {
            int max = this.limitValue;
            if(max>fileInfoListTemp.size()) {
                max = fileInfoListTemp.size();
            }
            
            fileInfoList.addAll(fileInfoListTemp.subList(0, max));
            return true;
        }
        else if(this.limit && !this.limitUnit.equals(LimitUnit.files)) { //files limit done in SQL query
            if(this.limitUnit.equals(LimitUnit.Gio) || this.limitUnit.equals(LimitUnit.Mio)) {
                double maxSizeOctet=0;
                //http://fr.wikipedia.org/wiki/Gibioctet#Multiples_normalis.C3.A9s
                double limitValueDouble = (double) this.limitValue;
                switch (this.limitUnit) {
                    case Gio:
                        maxSizeOctet=limitValueDouble*1073741824;
                        break;
                    case Mio:
                        maxSizeOctet=limitValueDouble*1048576;
                        break;
                }

                double sizeTotal=0;
                for (FileInfoInt fileInfo : fileInfoListTemp) {
                    sizeTotal+=fileInfo.size;
                    if(sizeTotal>maxSizeOctet) {
                        break;
                    }
                    fileInfoList.add(fileInfo);
                }
            }
            else if(this.limitUnit.equals(LimitUnit.minutes) || this.limitUnit.equals(LimitUnit.hours)) {
                double maxLengthSeconds=0;
                switch (this.limitUnit) {
                    case minutes:
                        maxLengthSeconds=(double)this.limitValue*60;
                        break;
                    case hours:
                        maxLengthSeconds=(double)this.limitValue*60*60;
                        break;
                }

                double lengthTotal=0;
                for (FileInfoInt fileInfo : fileInfoListTemp) {
                    lengthTotal+=fileInfo.length;
                    if(lengthTotal>maxLengthSeconds) {
                        break;
                    }
                    fileInfoList.add(fileInfo);
                }
            }
            return true;
        }
        else {
            fileInfoList.addAll(fileInfoListTemp);
            return true;
        }
	}
	
	private String getSqlWhere() {
		
        String sql=""; //NOI18N
        
        if(this.getFilters().size()>0) {
            sql+=" AND ( "; //NOI18N
            for(Filter filter : this.getFilters().subList(0, this.getFilters().size()-1)) {
                sql+=filter.getQuery();
                sql+=" "+this.match.getSqlValue()+" "; //NOI18N
            }
            sql+=this.getFilters().get(this.getFilters().size()-1).getQuery()+")"; //NOI18N
        }
        
        if(this.random) {
            sql+=" ORDER BY RANDOM() "; //NOI18N
        }
        else {
            if(this.getOrders().size()>0) {
                sql+=" ORDER BY "; //NOI18N
                for(Order order : this.getOrders()) {
                    sql+=order.getQuery();
                }
                sql=sql.substring(0, sql.length()-1);
            }
        }

        if(this.limit && this.limitUnit.equals(LimitUnit.files)) {
            sql+=" LIMIT 0, "+this.limitValue; //NOI18N
        }
        
		return sql;
	}

	/**
	 * Set a filter
	 * @param filterIndex
	 * @param filter
	 */
	public void setFilter(int filterIndex, Filter filter) {
		this.filters.set(filterIndex, filter);
	}
	
	/**
	 * Adds a filter
	 * @param filter
	 */
	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}
	
	/**
	 * Removes a filter
	 * @param index
	 */
	public void removeFilter(int index) {
		this.filters.remove(index);
	}
	
	/**
	 * Get filters
	 * @return
	 */
	public ArrayList<Filter> getFilters() {
		return filters;
	}

	/**
	 * Set an order
	 * @param orderIndex
	 * @param order
	 */
	public void setOrder(int orderIndex, Order order) {
		this.orders.set(orderIndex, order);
	}
	
    /**
	 * Add an order
	 * @param order
	 */
	public void addOrder(Order order) {
		this.orders.add(order);
	}
	
	/**
	 * Remove an order
	 * @param index
	 */
	public void removeOrder(int index) {
		this.orders.remove(index);
	}
    
    /**
	 * Get orders
	 * @return
	 */
	public ArrayList<Order> getOrders() {
        return orders;
    }
    
    /**
	 * Limit unit
	 */
	public enum LimitUnit {
		/**
		 * Size in Gio
		 */
		Gio,
		/**
		 * Size in Mio
		 */
		Mio,
		/**
		 * Number of files
		 */
		files,
		/**
		 * Length in minutes
		 */
		minutes,
		/**
		 * Length in hours
		 */
		hours;
	}
    
	/**
	 * Playlist type, not yet used
	 */
	public enum Type {
		/**
		 * Songs, default 
		 */
		Songs,
		/**
		 * Albums, not yet supported
		 */
		Albums,
		/**
		 * Artists, not yet supported
		 */
		Artists;
	}
	
	/**
	 * Match filters mode (AND, OR, INDE)
	 */
	public enum Match {
		/**
		 * All filters must match (AND)
		 */
		All(Inter.get("Playlist.Match.All"), "AND"), //NOI18N
		/**
		 * At least one filter must match (OR)
		 */
		One(Inter.get("Playlist.Match.One"), "OR"), //NOI18N
        /**
		 * Filters are independant (sub-playlists)
		 */
		Inde(Inter.get("Playlist.Match.Inde"), ""); //NOI18N
        
        private final String display;
		private final String sqlValue;
		
        private Match(String display, String sqlValue) {
            this.display = display;
			this.sqlValue = sqlValue;
		}

        @Override
		public String toString() {
			return display;
		}
        
        /**
		 * Return SQL value
		 * @return
		 */
		public String getSqlValue() {
            return sqlValue;
        }
	}
	
	/**
	 * Tag field
	 */
	public enum Field {
		/**
		 * Genre
		 */
		GENRE(Inter.get("Tag.Genre"), "F.genre"),  //NOI18N
        
        //TODO: Add a Score or recommended :
        
//select round((julianday('now') - julianday(lastPlayed))) AS NbJoursPasJoue, 
//round((julianday('now') - julianday(lastPlayed))) AS NbJoursDsLib, 
//playcounter, rating, lastPlayed, 
//(((julianday('now') - julianday(lastPlayed))*rating*(julianday('now') - julianday(addeddate)))/playCounter) as score, 
//albumArtist, album, artist, title 
//from file 
//order by (((julianday('now') - julianday(lastPlayed))*rating*(julianday('now') - julianday(addeddate)))/playCounter) DESC 
        
		/**
		 * Rating
		 */
		RATING(Inter.get("Stat.Rating"), "F.rating"), //NOI18N
        ALBUMRATING(Inter.get("Tag.Album")+" "+Inter.get("Stat.Rating"), "albumRating"), //NOI18N
        PERCENTRATED(Inter.get("Stat.PercentRated"), "percentRated"), //NOI18N
		/**
		 * Playlist (special case if Match.Inde)
		 */
		PLAYLIST(Inter.get("Label.Playlist"), "F.idFile"), //NOI18N
		/**
		 * Album
		 */
		ALBUM(Inter.get("Tag.Album"), "F.album"),  //NOI18N
		/**
		 * Artist
		 */
		ARTIST(Inter.get("Tag.Artist"), "F.artist"), //NOI18N
		/**
		 * Album Artist
		 */
		ALBUMARTIST(Inter.get("Tag.AlbumArtist"), "F.albumArtist"), //NOI18N
		/**
		 * Title
		 */
		TITLE(Inter.get("Tag.Title"), "F.title"), //NOI18N
		/**
		 * Year
		 */
		YEAR(Inter.get("Tag.Year"), "F.year"), //NOI18N
		/**
		 * Track #
		 */
		TRACKNO(Inter.get("Tag.TrackNo"), "F.trackNo"), //NOI18N
		/**
		 * Play counter
		 */
		PLAYCOUNTER(Inter.get("Stat.PlayCounter"), "F.playCounter"), //NOI18N
		/**
		 * Last played
		 */
		LASTPLAYED(Inter.get("Stat.LastPlayed"), "F.lastPlayed"), //NOI18N

        /**
         * Added date
         */
        
        ADDEDDATE(Inter.get("Stat.Added"), "F.addedDate"), //NOI18N
        
		/**
		 * Checked flag
		 */
		
        CHECKEDFLAG(Inter.get("Stat.Checked"), "P.checked");

        //FIXME: Store lyrics in DB ! (we don't need to re-read each time AND we can filter here in playlists)
//,     LYRICS(Inter.get("Label.Lyrics"), ""); //NOI18N
		//+ We don't want to remove the lyrics when saving the tags, if not retrieved first
        
        //TODO: Add other fields from file and path tables:
        //Bitrate, fichier, path, fullpath (concat), durée, format ?, taille ?, BPM, Commentaire ?, discNo, 
        //Pochette (taille=>a sauvegarder dans base)

		private final String display;
		private final String sqlValue;
		private Field(String display, String sqlValue) {
			this.display = display;
			this.sqlValue = sqlValue;
		}
		@Override
		public String toString() {
			return display;
		}

		/**
		 * Return SQL value
		 * @return
		 */
		public String getSqlValue() {
			return sqlValue;
		}
	}
	
	/**
	 * Operator for filters
	 */
	public enum Operator {
		/**
		 * String contains
		 */
		CONTAINS(Inter.get("Label.Playlist.Operator.CONTAINS")), //NOI18N
		/**
		 * String does not contain
		 */
		DOESNOTCONTAIN(Inter.get("Label.Playlist.Operator.DOESNOTCONTAIN")), //NOI18N
		/**
		 * Value (text) is
		 */
		IS(Inter.get("Label.Playlist.Operator.IS")), //NOI18N
		/**
		 * Value (text) is not
		 */
		ISNOT(Inter.get("Label.Playlist.Operator.ISNOT")), //NOI18N
        /**
		 * Value (numerical) is 
		 */
		NUMIS(Inter.get("Label.Playlist.Operator.IS")), //NOI18N
		/**
		 * Value (numerical) is not
		 */
		NUMISNOT(Inter.get("Label.Playlist.Operator.ISNOT")), //NOI18N
		/**
		 * String starts with
		 */
		STARTSWITH(Inter.get("Label.Playlist.Operator.STARTSWITH")), //NOI18N
		/**
		 * String ends with
		 */
		ENDSWITH(Inter.get("Label.Playlist.Operator.ENDSWITH")), //NOI18N
        /**
		 * Value is less than
		 */
		LESSTHAN(Inter.get("Label.Playlist.Operator.LESSTHAN")), //NOI18N
		/**
		 * Value is greater than
		 */
		GREATERTHAN(Inter.get("Label.Playlist.Operator.GREATERTHAN")), //NOI18N
		/**
		 * Value is less than
		 */
		DATELESSTHAN(Inter.get("Label.Playlist.Operator.LESSTHAN")), //NOI18N
		/**
		 * Value is greater than
		 */
		DATEGREATERTHAN(Inter.get("Label.Playlist.Operator.GREATERTHAN")); //NOI18N
		
		private final String display;
		private Operator(String display) {
			this.display = display;
		}
		@Override
		public String toString() {
			return display;
		}
	};
	
	/**
	 * A playlist filter
	 */
	public static class Filter {
		private final int id;
		/**
		 * Field
		 */
		private Field field;
		/**
		 * Operator
		 */
		private Operator operator;
		/**
		 * Value
		 */
		private String value;
        
		/**
		 * Creates a filter
		 * @param id
		 * @param field
		 * @param operator
		 * @param value
		 */
		public Filter(int id, Field field, Operator operator, String value) {
			this.id=id;
			this.field = field;
			this.operator = operator;
			this.value = value;
		}

		/**
		 * Get filter ID
		 * @return
		 */
		public int getId() {
			return id;
		}
		
        /**
		 * Get field as string
		 * @return
		 */
		public String getFieldName() {
            return field.name();
        }

        public Field getField() {
            return field;
        }
        
        

        public void setField(Field field) {
            this.field = field;
        }

        /**
		 * Get operator as string
		 * @return
		 */
		public Operator getOperator() {
            return operator;
        }
        
        public String getOperatorName() {
            return operator.name();
        }
        
        public void setOperator(Operator operator) {
            this.operator = operator;
        }

        /**
		 * Get value
		 * @return
		 */
		public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

		/**
		 * Get SQL query
		 * @return
		 */
		public String getQuery() {

			String sql = "("+this.field.getSqlValue(); //NOI18N
			
            if(this.field.equals(Field.PLAYLIST)) {
                Playlist playlist = Jamuz.getPlaylist(Integer.parseInt(this.value));
                //TODO: use a view: https://www.sqlite.org/lang_createview.html
				sql+=" IN (SELECT F.idFile FROM file F JOIN ("
                        + " SELECT path.*, ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, \n" 
                        + " ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated\n"
                        + "     FROM path JOIN file ON path.idPath=file.idPath GROUP BY path.idPath \n" 
                        + ") P ON F.idPath=P.idPath "
						+ "WHERE F.deleted=0 "+playlist.getSqlWhere()+")"; //NOI18N //NOI18N
            }
            else {
                switch (this.operator) {
                    case CONTAINS:
                        sql+=" LIKE \"%"+this.value+"%\""; //NOI18N
                        break;
                    case DOESNOTCONTAIN:
                        sql+=" NOT LIKE \"%"+this.value+"%\""; //NOI18N
                        break;
                    case IS:
                        sql+=" = \""+this.value+"\""; //NOI18N
                        break;
                    case ISNOT:
                        sql+=" <> \""+this.value+"\""; //NOI18N
                        break;
                    case NUMIS:
                        sql+=" = "+this.value; //NOI18N
                        break;
                    case NUMISNOT:
                        sql+=" <> "+this.value; //NOI18N
                        break;
                    case STARTSWITH:
                        sql+=" LIKE \""+this.value+"%\""; //NOI18N
                        break;
                    case ENDSWITH:
                        sql+=" LIKE \"%"+this.value+"\""; //NOI18N
                        break;
                    case LESSTHAN:
                        sql+=" < "+this.value; //NOI18N
                        break;
                    case GREATERTHAN:
                        sql+=" > "+this.value; //NOI18N
                        break;
                    case DATELESSTHAN:
                        sql+=" < datetime(\""+this.value+"\", \"utc\")"; //NOI18N
                        break;
                    case DATEGREATERTHAN:
                        sql+=" > datetime(\""+this.value+"\", \"utc\")"; //NOI18N
                        break;
                }
            }
			sql+=")"; //NOI18N

			return sql;
		}
	
		@Override
		public String toString() {
			String valueToReturn = this.value;
			if(this.field.equals(Field.PLAYLIST)) {
				valueToReturn = Jamuz.getPlaylist(Integer.valueOf(this.value)).name;
			}
			if(this.field.equals(Field.CHECKEDFLAG)) {
				valueToReturn = CheckedFlag.values()[Integer.valueOf(this.value)].toString();
			}
            switch(field) {
                case ALBUMRATING:
                case PERCENTRATED:
                case PLAYCOUNTER:
                case RATING:
                case TRACKNO:
                case YEAR:
                    return this.field + " " + this.operator + " " + this.value + ""; //NOI18N 
                default:
                    return this.field + " " + this.operator + " \"" + valueToReturn + "\""; //NOI18N 
            }
		}
	}
	
	/**
	 * Order by
	 */
	public static class Order {
        private final int id;
		/**
		 * Order descending ?
		 */
		private boolean desc;
		/**
		 * Field
		 */
		private Field field;

        /**
		 * Creates an order
		 * @param id
		 * @param field
		 * @param desc
		 */
		public Order(int id, Field field, boolean desc) {
            this.id = id;
            this.field = field;
            this.desc=desc;
        }
        
        /**
		 * Get field as string
		 * @return
		 */
		public String getFieldName() {
            return field.name();
        }

        public Field getField() {
            return field;
        }

        public void setDesc(boolean desc) {
            this.desc = desc;
        }

        public void setField(Field field) {
            this.field = field;
        }

        /**
		 * Return true if descending order
		 * @return
		 */
		public boolean isDesc() {
            return desc;
        }
        
        /**
		 * Get SQL query
		 * @return
		 */
		public String getQuery() {
            String sql=""; //NOI18N
            sql+=this.field.sqlValue;
            if(this.desc) {
                sql+=" DESC"; //NOI18N
            }
			sql+=","; //NOI18N
            return sql;
        }
        
        @Override
		public String toString() {
			String s = this.field.toString();
            if(this.desc) {
                s+=" DESC"; //NOI18N
            }
            else {
                s+=" ASC"; //NOI18N
            }
            return s;
		}
	}

}
