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

package jamuz;

import jamuz.process.check.FolderInfoResult;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.process.check.ReplayGain;
import jamuz.process.check.ReplayGain.GainValues;
import jamuz.utils.Popup;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.apache.commons.io.FilenameUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import jamuz.utils.DateTime;
import jamuz.utils.Inter;
import jamuz.utils.StringManager;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.json.simple.JSONValue;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class FileInfoInt extends FileInfo {
//Tag information
	//TODO: Eventually support some more tags
	/**
	 * Artist name
	 */
	protected String artist="";  //NOI18N
	/**
	 * Album name
	 */
	protected String album="";  //NOI18N
    
	/**
	 * Album Artist name
	 */
	protected String albumArtist="";  //NOI18N
	/**
	 * Song title
	 */
	protected String title="";  //NOI18N
    
	/**
	 * Song trackGain #
	 */
	protected int trackNo=-1;

	/**
	 *
	 * @return
	 */
	public int getTrackNo() {
        return trackNo;
    }
    
	/**
	 * Album trackGain total
	 */
	protected int trackTotal=-1;

	/**
	 *
	 * @return
	 */
	public int getTrackTotal() {
        return trackTotal;
    }
    
	/**
	 * Song disc #
	 */
	protected int discNo=-1;

	/**
	 *
	 * @return
	 */
	public int getDiscNo() {
        return discNo;
    }

	/**
	 * Album disc total
	 */
	protected int discTotal=-1;
    
	/**
	 * Song year
	 */
	protected String year="";  //NOI18N
	/**
	 * Song comment
	 */
	protected String comment="";  //NOI18N

	/**
	 *
	 * @return
	 */
	public String getComment() {
        return comment;
    }
    
	//Cover:
	/**
	 * Indicates the number of covers embedded
	 */
    protected int nbCovers=0;

	/**
	 *
	 * @return
	 */
	public int getNbCovers() {
        return nbCovers;
    }
    

    /**
     * Checked flag (OK, Warning, KO, ...)
     */
    private CheckedFlag checkedFlag = CheckedFlag.UNCHECKED;

	/**
	 *
	 * @return
	 */
	public CheckedFlag getCheckedFlag() {
        return checkedFlag;
    }
    
	/**
	 * Cover hash
	 */
	private String coverHash=""; //NOI18N
	private BufferedImage coverImage=null;
	private boolean hasID3v1=false; 
	
//File information
	/**
	 * File bitRate
	 */
	private String bitRate="";  //NOI18N

	/**
	 *
	 * @return
	 */
	public String getBitRate() {
        return bitRate;
    }
	/**
	 * File format
	 */
	private String format="";  //NOI18N

	/**
	 *
	 * @return
	 */
	public String getFormat() {
        return format;
    }
    
	/**
	 * File length
	 */
	protected int length;

	/**
	 *
	 * @return
	 */
	public int getLength() {
        return length;
    }
    
	/**
	 * File length (display format)
	 */
	protected String lengthDisplay="";

	/**
	 *
	 * @param lengthDisplay
	 */
	public void setLengthDisplay(String lengthDisplay) {
        this.lengthDisplay = lengthDisplay;
    }
    
	/**
	 * File size
	 */
	protected long size;

	/**
	 *
	 * @return
	 */
	public long getSize() {
        return size;
    }
    
	/**
	 * File size (display format)
	 */
	protected String sizeDisplay="";

	/**
	 *
	 * @param sizeDisplay
	 */
	public void setSizeDisplay(String sizeDisplay) {
        this.sizeDisplay = sizeDisplay;
    }
    
	/**
	 * File modification datetime
	 */
	protected Date modifDate;
	/**
	 * File root path 
	 * Used for play queue as can be filled up from library or Add tab
	 */
	protected String rootPath="";  //NOI18N
    

	/**
	 * Indicates if file is from library so if rating, genre can be edited
	 */
	private boolean fromLibrary=false;

	/**
	 *
	 * @return
	 */
	public boolean isFromLibrary() {
        return fromLibrary;
    }
    
    private final int copyRight;

	/**
	 *
	 * @return
	 */
	public int getCopyRight() {
        return copyRight;
    }

	/**
	 *
	 */
	protected String lyrics="";
    
	
    /**
     * Used to merge BPM
     * @param file
     * @param bpm
	 * @param genre
     */
    public FileInfoInt(FileInfo file, float bpm, String genre) {
        super(file.idFile, file.getIdPath(), file.relativeFullPath, file.rating, 
				file.getFormattedLastPlayed(), file.getFormattedAddedDate(), 
                file.playCounter, file.sourceName, file.previousPlayCounter, 
				bpm, genre, file.getFormattedRatingModifDate(), 
				file.getFormattedTagsModifDate(),
				file.getFormattedGenreModifDate());
        copyRight=-1;
        this.rootPath = Jamuz.getMachine().getOptionValue("location.library");  //NOI18N
    }

	/**
	 * Used for creating a FileInfoInt while scanning library
	 * Calling getFileInfo() to read tags from file
	 * @param relativeFullPath
	 * @param rootPath  
	 */
	public FileInfoInt(String relativeFullPath, String rootPath) {
		super(-1, -1, relativeFullPath, -1, "1970-01-01 00:00:00", 
				"1970-01-01 00:00:00", 0, "file", 0, 0, "", "", "", "");  //NOI18N
		this.rootPath=rootPath;
		this.modifDate = new Date(getFullPath().lastModified());
        copyRight=-1;
	}
	
    private double albumRating = 0.0;
    private int percentRated = 0;

    /**
     * Get the value of albumRating
     *
     * @return the value of albumRating
     */
    public double getAlbumRating() {
        return albumRating;
    }

	/**
	 *
	 * @return
	 */
	public double getPercentRated() {
        return percentRated;
    }

//	Jamuz.getMachine().getOption("location.library")
	
	/**
	 * Used when retrieving file information from database
	 * @param idFile 
	 * @param idPath 
	 * @param relativePath 
	 * @param filename
	 * @param length 
	 * @param artist
	 * @param format 
	 * @param album
	 * @param size 
	 * @param title
	 * @param BPM 
	 * @param genre
	 * @param year
	 * @param comment 
	 * @param discTotal 
	 * @param albumArtist 
	 * @param bitRate
     * @param nbCovers
	 * @param trackTotal 
	 * @param trackNo 
	 * @param discNo 
	 * @param playCounter
	 * @param addedDate
	 * @param rating
	 * @param lastPlayed
	 * @param modifDate
	 * @param deleted  
     * @param coverHash  
     * @param checkedFlag  
     * @param copyRight  
     * @param albumRating  
     * @param percentRated  
	 * @param rootPath  
	 */
	public FileInfoInt(int idFile, int idPath, String relativePath, 
			String filename, int length, String format, String bitRate, 
			int size, float BPM, String album, String albumArtist, 
			String artist, String comment, int discNo, int discTotal, 
			String genre, int nbCovers, String title, int trackNo, 
			int trackTotal, String year, int playCounter, int rating, 
			String addedDate, String lastPlayed, String modifDate, 
			boolean deleted, String coverHash, CheckedFlag checkedFlag, 
			int copyRight, double albumRating, int percentRated, String rootPath) {
		
		super("file");  //NOI18N
        this.fromLibrary=true;
		this.idFile=idFile;
		this.idPath = idPath;
		this.relativePath=FilenameUtils.separatorsToSystem(relativePath);
		this.filename = filename;
		this.relativeFullPath=this.relativePath+this.filename;
		this.rootPath = rootPath;  //NOI18N
        this.ext=FilenameUtils.getExtension(this.filename).toLowerCase(Locale.ENGLISH);
		
		//Set File (tags) Info
		this.length=length;
		this.lengthDisplay=String.valueOf(this.length);
		this.format=format;
		this.bitRate=bitRate;
		this.size=size;
		this.sizeDisplay=String.valueOf(this.size);
		//Set tags
		this.BPM = BPM;
		this.album=album;
		this.albumArtist=albumArtist;
		this.artist=artist;
		this.comment=comment;
		this.discNo=discNo;
		this.discTotal=discTotal;
		this.genre=genre;
		this.nbCovers=nbCovers;
        this.coverHash=coverHash;
		this.title=title;
		this.trackNo=trackNo;
		this.trackTotal=trackTotal;
		this.year=year;
		
		//Set statistics
		this.playCounter=playCounter;
		this.rating=rating;
		this.addedDate=DateTime.parseSqlUtc(addedDate);
		this.lastPlayed=DateTime.parseSqlUtc(lastPlayed);

		this.modifDate=DateTime.parseSqlUtc(modifDate); 
		this.deleted = deleted;
		this.checkedFlag = checkedFlag;
		//NOT needed here:
//		this.sourceName
//		this.hash
//		this.index
        this.copyRight = copyRight;
        this.albumRating = albumRating;
        this.percentRated = percentRated;
	
	}
	
	/**
	 * Set root path
	 * @param rootPath
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
	public final File getFullPath() {
		return new File(FilenameUtils.concat(this.rootPath, this.relativeFullPath));
	}
	
	/**
	 *
	 * @return
	 */
	public String getLyrics() {
		if(lyrics.equals("")) {
            try {
                AudioFile myAudioFile = AudioFileIO.read(getFullPath());
				Tag tag = myAudioFile.getTag();
				if(tag!=null) {
					if(tag.getFirst(FieldKey.LYRICS)!=null) {
						lyrics = tag.getFirst(FieldKey.LYRICS);
					}
				}
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
//                lyrics=""; //Well, do nothing so. Simply not read, so not good probably
            }
        }
        return lyrics;
    }
    
	//TODO: Make it possible to read cover from a file such as "cover.jpg", "cover.png" or else
	/**
	 * Read cover from tag
	 */
	private void readCoverFromTag() {
		try {
			if(this.nbCovers>0) {
				File currentFile = getFullPath();
                if(currentFile.exists()) {
                    Tag tag;
                    switch (this.ext) {
                        case "mp3": //NOI18N
                            //Read MP3 file
                            MP3File myMP3File = (MP3File)AudioFileIO.read(currentFile);
                            tag = myMP3File.getTag();
                            break;
                        default:
                            //Read audio file
                            AudioFile myAudioFile = AudioFileIO.read(currentFile);
                            tag = myAudioFile.getTag();
                            break;
                    }
                    if(tag!=null) {
                        //Check presence of a cover
                        if(tag.getFirstArtwork()!=null) {
                            this.readCover(tag);
                        }
                    }
                }
			}
		} 
        catch (ClosedByInterruptException ex) {
            //Not doing anything, not a problem, simply occurs when changing from one playlist to another
        }
		catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
            Jamuz.getLogger().log(Level.SEVERE, java.text.MessageFormat.format(Inter.get("Error.ReadingCover"), new Object[] {this.relativeFullPath}));
            //Not poping anoying errors that we cannot fix
//			Popup.error(java.text.MessageFormat.format(Inter.get("Error.ReadingCover"), new Object[] {this.relativeFullPath}), ex);  //NOI18N
		}
        
	}
    
    private void readCoverHash() throws NoSuchAlgorithmException, IOException {
        if(this.coverImage!=null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(this.coverImage, "png", outputStream); //NOI18N
            byte[] data = outputStream.toByteArray();
            MessageDigest md = MessageDigest.getInstance("MD5"); //NOI18N
            md.update(data);
            byte[] hash = md.digest();
            this.coverHash=returnHex(hash);
        }
    }
    
    static String returnHex(byte[] inBytes) {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < inBytes.length; i++) { //for loop ID:1
            builder.append(Integer.toString( ( inBytes[i] & 0xff ) + 0x100, 16).substring( 1 ));
        }                                   // Belongs to for loop ID:1
        return builder.toString();
    }
    
	/**
	 *
	 * @param readCover
	 * @return
	 */
	public boolean readTags(boolean readCover) {
		try {
			File currentFile = getFullPath();
			
			//Getting file information
			this.modifDate = new Date(currentFile.lastModified());
			this.size = currentFile.length();
			this.sizeDisplay=String.valueOf(this.size);
			
			//Read tags, depending on file extension
			AudioHeader myHeader;
			Tag tag;
			switch (this.ext) {
				case "mp3": //NOI18N
					//Read MP3 file
					MP3File myMP3File = (MP3File)AudioFileIO.read(currentFile);
					myHeader = myMP3File.getAudioHeader();
					tag = myMP3File.getTag();
					//Read header
					this.readHeader(myHeader);
					//Read tags
					if(tag!=null) {
						//First get tags from ID3v2
						if(myMP3File.hasID3v2Tag()) {
							AbstractID3v2Tag v2tag  = myMP3File.getID3v2Tag();
							this.readTags(v2tag, readCover);
						}
						//Then, get tags from ID3v1 IF not found in ID3v2
						//Note: not all fields are available in ID3v1 compared to ID3v2
						if (myMP3File.hasID3v1Tag()) {
							this.hasID3v1 = true;
							ID3v1Tag v1Tag = myMP3File.getID3v1Tag();
							if(this.artist.equals("")) {  //NOI18N
								this.artist=v1Tag.getFirst(FieldKey.ARTIST);
							}
							if(this.album.equals("")) {  //NOI18N
								this.album=v1Tag.getFirst(FieldKey.ALBUM);
							}
							if(this.year.equals("")) {  //NOI18N
								this.year=v1Tag.getFirst(FieldKey.YEAR);
							}
							if(this.title.equals("")) {  //NOI18N
								this.title=v1Tag.getFirst(FieldKey.TITLE);
							}
							if(this.trackNo<1) {
								this.trackNo=getInt(v1Tag.getFirst(FieldKey.TRACK));
							}
							if(this.genre.equals("")) {  //NOI18N
								this.genre=v1Tag.getFirst(FieldKey.GENRE);
								//TODO: Translate v1 genres into String
								//http://fr.wikipedia.org/wiki/ID3
								//https://github.com/drogatkin/tiny-codec/blob/master/codecs/APE/src/java/davaguine/jmac/info/ID3Genre.java
							}
							//Reading this in case user do not want to remove comments
							if(this.comment.equals("")) {  //NOI18N
								this.comment=v1Tag.getFirst(FieldKey.COMMENT);
							}
						}
					}
					break;
				default:
					//Read audio file
					AudioFile myAudioFile = AudioFileIO.read(currentFile);
					myHeader = myAudioFile.getAudioHeader();
					tag = myAudioFile.getTag();
					//Read header
					this.readHeader(myHeader);
					//Read tags
					if(tag!=null) {
						this.readTags(tag, readCover);
					}
			}
			return true;
		} catch (IllegalArgumentException | CannotReadException | IOException 
                | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
			//TODO: Find a better way of displaying error
			//Usefull to return sthg then ?
			this.comment="ERROR: "+ex.toString();  //NOI18N
			return false;
		}
	}

	private void readTags(Tag tag, boolean readCover) throws IOException {
		//Get number of covers and read first one as required
		this.nbCovers=tag.getArtworkList().size();
		if(this.nbCovers>0 && readCover) {
			this.readCover(tag);
		}

		this.album=tag.getFirst(FieldKey.ALBUM);
		this.artist=tag.getFirst(FieldKey.ARTIST);
		this.albumArtist=tag.getFirst(FieldKey.ALBUM_ARTIST);
        try {
            this.setBPM(tag.getFirst(FieldKey.BPM).equals("") ? 0 : Float.parseFloat(tag.getFirst(FieldKey.BPM)));
        }
        catch (NumberFormatException ex) {
        }
		this.comment=tag.getFirst(FieldKey.COMMENT);
		this.discNo=getInt(tag.getFirst(FieldKey.DISC_NO));
		this.discTotal=getInt(tag.getFirst(FieldKey.DISC_TOTAL));
		this.genre=tag.getFirst(FieldKey.GENRE);
		this.title=tag.getFirst(FieldKey.TITLE);
		this.year=tag.getFirst(FieldKey.YEAR);
		this.trackTotal=getInt(tag.getFirst(FieldKey.TRACK_TOTAL));
		this.trackNo=getInt(tag.getFirst(FieldKey.TRACK));								
	}
	
	private void readCover(Tag tag) throws IOException {
		Artwork myArt = tag.getFirstArtwork();
        try {
            coverImage=(BufferedImage)myArt.getImage();
            readCoverHash();
        }
        catch(IndexOutOfBoundsException | NullPointerException | NoSuchAlgorithmException ex) {
            //Do nothing
        }
	}
	
	private void readHeader(AudioHeader myHeader) {
		this.bitRate=myHeader.getBitRate();
		//TODO: support channels
//			this.channels=myHeader.getChannels();
		this.format=myHeader.getFormat();
		//TODO: support sample rate
//			this.sampleRate=myHeader.getSampleRate();
		this.length=myHeader.getTrackLength();
		this.lengthDisplay=String.valueOf(this.length);
		//TODO: support isVariableBitRate
//			this.isVariableBitRate=myHeader.isVariableBitRate();
	}
	
	private void setEmptyCover() {
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 40);
		this.coverImage = new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = this.coverImage.createGraphics();
		FontMetrics fm = g.getFontMetrics(f);
		g.setBackground(Color.LIGHT_GRAY);
		g.clearRect(0, 0, this.coverImage.getWidth(), this.coverImage.getHeight());
		g.setFont(f);
		g.setColor(Color.DARK_GRAY);
		
		//TODO: Translate
		String text = "No Cover";
		
		g.drawString(text, (this.coverImage.getWidth()/2)-(text.length()*12),
				(this.coverImage.getHeight()/2)+20);  //NOI18N
	}
	
	private int getInt(String value) {
		int castValue=-1;
		try{
			castValue=Integer.parseInt(value);
		}
		catch (NumberFormatException ex) {
		}
		return castValue;
	}
	
	/**
	 * Restore tags 
	 * @param destExt
	 * @return
	 */
	public boolean restoreTags(String destExt) {
		//Change file extension
		this.ext=destExt;
        this.setFilename(FilenameUtils.getBaseName(this.getFilename())+"."+this.ext);
		this.relativeFullPath=this.relativePath+this.getFilename();
		this.relativePath=FilenameUtils.getFullPath(this.relativeFullPath);
		//Save tags
		return saveTags(true);
	}

    /**
     * Save tags to file
     * @param deleteComment
     * @return
     */
    public boolean saveTags(boolean deleteComment) {
        return this.saveTags(this.artist, this.albumArtist, this.album, this.trackNo, this.trackTotal, this.discNo, this.discTotal, 
			this.genre, this.year, this.coverImage, deleteComment, this.comment, this.title, this.BPM, this.lyrics);  //NOI18N
    }
    
	/**
	 * Convert a BufferedImage to Artwork (in order to save it in file tags)
	 * @param image
	 * @return
	 */
	private Artwork imageToArt(BufferedImage image) {
		Artwork myArt=null;
		//Store cover to a temp file (as dunno how to save it to tag apart from a file)
		String tempCoverFile = System.getProperty("java.io.tmpdir") + File.separator + "JaMuzTempCover.png";   //NOI18N //NOI18N //NOI18N
		File file = new File(tempCoverFile);
        file.delete();
		try {
			if(image!=null) {
				ImageIO.write(image, "png", file);  //NOI18N
				if(file.exists()) {
					myArt = ArtworkFactory.createArtworkFromFile(file);
				}
				return myArt;
			}
			else {
				return null;
			}
		} catch (IOException ex) {
			Popup.error(ex);
			file.delete();
            return null;
		}
	}
    
	/**
	 *
	 * @param artist
	 * @param albumArtist
	 * @param album
	 * @param trackNo
	 * @param trackTotal
	 * @param discNo
	 * @param discTotal
	 * @param genre
	 * @param year
     * @param image
	 * @param deleteComment
	 * @param comment
	 * @param title
	 * @param bpm
	 * @param lyrics
	 * @return
	 */
	protected boolean saveTags(String artist, String albumArtist, String album, 
			int trackNo, int trackTotal, int discNo, int discTotal,
			String genre, String year, BufferedImage image, boolean deleteComment, String comment, String title, float bpm, String lyrics) {
		try {           
            
			File file = getFullPath();
			if(file.length()<=0) {
				return false;
			}
			Artwork artwork = imageToArt(image);
			switch (this.ext) {
				case "mp3": //NOI18N
					MP3File MP3File = (MP3File)AudioFileIO.read(file);
					//Remove ID3v1 tags
					MP3File.setID3v1Tag(null);
					//Create brand new ID3v2 tags
					AbstractID3v2Tag v2tag = new ID3v23Tag();
					MP3File.setID3v2Tag(v2tag);
					this.setTags(v2tag, artist, albumArtist, album, trackNo, trackTotal, discNo, discTotal, genre, year, 
							artwork, deleteComment, comment, title, bpm, lyrics);
					MP3File.commit();
					break;
				default:
					AudioFile audioFile = AudioFileIO.read(file);
					Tag tag = audioFile.getTag();
					this.setTags(tag, artist, albumArtist, album, trackNo, trackTotal, discNo, discTotal, genre, year, 
							artwork, deleteComment, comment, title, bpm, lyrics);
					audioFile.commit();
					break;
			}
			
			if(this.idFile>-1) { //File displayed in player may not be from database (check new)
                Jamuz.getDb().setFileSaved(idFile);
            }
			return true;
		} catch (CannotWriteException | CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
			Popup.error("Error writing tags to \""+getFullPath()+"\"", ex);  //NOI18N
			return false;
		} 
	}
    
	private void setTags (Tag tag, String artist, String albumArtist, String album, int trackNo, int trackTotal, int discNo, int discTotal,
			String genre, String year, Artwork myArt, boolean deleteComment, String comment, String title, float bpm, String lyrics) throws KeyNotFoundException, FieldDataInvalidException {
		
		//Note: Since we are overwritting all tags, we are only inserting non-empty ones
		
		if(!lyrics.equals("")) {  //NOI18N
			tag.setField(FieldKey.LYRICS, lyrics);
		}
		if(!artist.equals("")) {  //NOI18N
			tag.setField(FieldKey.ARTIST, artist);
		}
		if(!albumArtist.equals("")) {  //NOI18N
			tag.setField(FieldKey.ALBUM_ARTIST, albumArtist);
		}
		if(!album.equals("")) {  //NOI18N
			tag.setField(FieldKey.ALBUM, album);
		}
		if(trackNo>0) {
			tag.setField(FieldKey.TRACK, String.valueOf(trackNo));
		}
		if(trackTotal>0) {
			tag.setField(FieldKey.TRACK_TOTAL, String.valueOf(trackTotal));
		}
		if(discNo>0) {
			tag.setField(FieldKey.DISC_NO, String.valueOf(discNo));
		}
		if(discTotal>0) {
			tag.setField(FieldKey.DISC_TOTAL, String.valueOf(discTotal));
		}
		if(!title.equals("")) {  //NOI18N
			tag.setField(FieldKey.TITLE, title);
		}
		if(!genre.equals("")) {  //NOI18N
			tag.setField(FieldKey.GENRE, genre);
		}
		if(!year.equals("")) {  //NOI18N
			tag.setField(FieldKey.YEAR, year);
		}
		if(myArt!=null) {
			tag.setField(myArt);
			this.nbCovers = 1;
		}
		if(!deleteComment && !comment.equals("")) { //NOI18N
			tag.setField(FieldKey.COMMENT, comment);
		}
		if(bpm>0) {  //NOI18N
			tag.setField(FieldKey.BPM, Float.toString(bpm));
		}
	}
	
	/**
	 * Insert tags in database
	 * @return
	 */
	public boolean insertTagsInDb() {
		int [] key = new int[1]; //Hint: Using a int table as cannot pass a simple integer by reference
		boolean result = Jamuz.getDb().insertTags(this, key);
		this.idFile=key[0]; //Get insertion key
		return result;
	}
	
	/**
	 * Update tags in database
	 * @return
	 */
	public boolean updateTagsInDb() {
		return Jamuz.getDb().updateTags(this);
	}
    
    /**
	 *
	 * @return
	 */
	public boolean saveBPMtoFileTags() {
		return this.saveTag(FieldKey.BPM, String.valueOf(getBPM()));
    }
	
	/**
	 *
	 * @param lyrics
	 * @return
	 */
	public boolean saveTagLyrics(String lyrics) {
        this.lyrics=lyrics;
		return this.saveTag(FieldKey.LYRICS, lyrics);
    }
    
	private boolean saveTag(FieldKey key, String value) {
		try {
			File testFile = getFullPath();
			switch (this.ext) {
				case "mp3": //NOI18N
					//Get current tags
					MP3File MP3File = (MP3File)AudioFileIO.read(testFile);
					AbstractID3v2Tag v2tag = MP3File.getID3v2Tag();
					//Set new tag
					v2tag.setField(key, value);
					//Commit
					MP3File.commit();
					break;
				default:
					//Get current tags
					AudioFile audioFile = AudioFileIO.read(testFile);
					Tag tag = audioFile.getTag();
					//Set new tag
					tag.setField(key, value);
					//Commit
					audioFile.commit();
					break;
			}
			return true;
		} catch (CannotReadException | IOException | TagException 
				| ReadOnlyFileException | InvalidAudioFrameException 
				| CannotWriteException ex) {
			Popup.error("Error writing \""+key.toString()+"\" to \""+getFullPath()+"\"", ex);  //NOI18N
			return false;
		}

	}

	/**
	 *
	 * @param genre
	 * @return
	 */
	public boolean updateGenre(String genre) {
		if(this.saveTag(FieldKey.GENRE, genre)) {
			this.genre=genre;
			if(this.idFile>-1) { //File displayed in player may not be from database (check new)
				return Jamuz.getDb().updateGenre(this);
			}
			return true;
		}
		return false;
    }

	/**
	 *
	 * @param rating
	 * @return
	 */
	public boolean updateRating(String rating) {
        this.rating=Integer.valueOf(rating);
		if(this.idFile>-1) { //File displayed in player may not be from database (check new)
			return Jamuz.getDb().updateRating(this);
		}
		return true;
    }
     
	/**
	 * Updates in database
	 * @return
	 */
	public boolean updateInDb() {
		this.modifDate = new Date(getFullPath().lastModified());
		return Jamuz.getDb().updateFileModifDate(this.idFile, this.modifDate, this.getFilename());
	}
	
	/**
	 * Scans for deleted files
	 * @return
	 */
	public boolean scanDeleted() {
		File currentFile = getFullPath();
		if(!currentFile.exists()) {
			if(!Jamuz.getDb().setFileDeleted(this.idFile)) {
				return false;
			}
		}
		return true;
	}
	
    //TODO: Use this function. 
	/**
	 * Save cover to file if it exists
	 * @param overwrite
	 * @return
	 */
	public FileInfoInt saveCoverToFile(boolean overwrite) {
		
		FileInfoInt coverFileInfoInt=null;
		try {
			if(this.nbCovers>0) {
				//Save cover as a file (same name as music file, changing extension to png
				String coverRelativeFullPath = FilenameUtils.removeExtension(this.relativeFullPath)+".png";  //NOI18N
				File file = new File(this.rootPath+coverRelativeFullPath);
				if(!file.exists() || overwrite) {
					ImageIO.write(coverImage, "png", file);  //NOI18N
					coverFileInfoInt = new FileInfoInt(coverRelativeFullPath, this.rootPath);
				}
			}
            return coverFileInfoInt;
		} catch (IOException ex) {
			Popup.error(ex);
		} 
        return null;
	}

    /**
    * Return trackGain number in "xx/yy" format
    * @return
    */
    public String getTrackNoFull() {
        return FolderInfoResult.formatNumber(this.trackNo)+"/"+FolderInfoResult.formatNumber(this.trackTotal);  //NOI18N
    }
	
	/**
	 *
	 * @param trackNoFull
	 */
	public void setTrackNoFull(String trackNoFull) {
		String[] splitted = trackNoFull.split("/");
		if(splitted.length==2) {
			this.trackNo = Integer.valueOf(splitted[0]);
			this.trackTotal = Integer.valueOf(splitted[1]);
		}
	}
	
    /**
    * Return disc number in "xx/yy" format
    * @return
    */
    public String getDiscNoFull() {
        return FolderInfoResult.formatNumber(this.discNo)+"/"+FolderInfoResult.formatNumber(this.discTotal);  //NOI18N
    }
    
	/**
	 *
	 * @param discNoFull
	 */
	public void setDiscNoFull(String discNoFull) {
		String[] splitted = discNoFull.split("/");
		if(splitted.length==2) {
			this.discNo = Integer.valueOf(splitted[0]);
			this.discTotal = Integer.valueOf(splitted[1]);
		}
	}
	
	/**
	 * Replaces %artist%, %albumGain%, ... by their actual values
	 * @param mask
	 * @return
	 */
	public String computeMask(String mask) {
		String strResult=mask;
		
		strResult=strResult.replace("%artist%", StringManager.removeIllegal(this.artist));  //NOI18N
		strResult=strResult.replace("%albumartist%", StringManager.removeIllegal(this.albumArtist));  //NOI18N
		strResult=strResult.replace("%album%", StringManager.removeIllegal(this.album));  //NOI18N
		strResult=strResult.replace("%genre%", StringManager.removeIllegal(this.genre));  //NOI18N

		String titleStr="";  //NOI18N
		if(!this.artist.equals(this.albumArtist)) {
			titleStr=this.artist + " - ";  //NOI18N
		}
		titleStr+=this.title;
		strResult=strResult.replace("%title%", StringManager.removeIllegal(titleStr));  //NOI18N
		
		String trackStr="";  //NOI18N
		if(this.discTotal>1 && this.discNo>0) {
			trackStr="["+this.discNo+"-"+this.discTotal+"] - ";  //NOI18N
		}
		if(this.trackNo>0) {
			trackStr=trackStr+FolderInfoResult.formatNumber(this.trackNo);
		}
		strResult=strResult.replace("%track%", trackStr);  //NOI18N
		
		strResult=FilenameUtils.separatorsToSystem(strResult);
		
		return strResult;
	}

	@Override
	public int compareTo(Object o) {
		
		//ORDER BY discNo, trackNo, filename
		
		if (this.discNo < ((FileInfoInt) o).discNo) return -1;
		if (this.discNo > ((FileInfoInt) o).discNo) return 1;
		
		if (this.trackNo < ((FileInfoInt) o).trackNo) return -1;
		if (this.trackNo > ((FileInfoInt) o).trackNo) return 1;
				
		return this.getFilename().compareTo(((FileInfoInt) o).getFilename());
	}

    /**
	 * Overring method for removing duplicates in playlists ("Inde" match)
	 * @param obj 
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
            return true;
        }
		
		if (obj instanceof FileInfoInt) {
			FileInfoInt thatFileInfo = (FileInfoInt) obj;
			//Not comparing relativeFullPath as it can be in different format (Windows vs Unix)
			//Moreover it is not requiered to compare that value (should be the same)
			boolean isEqual = (this.idFile == thatFileInfo.idFile);
//			isEqual &= (this.playCounter == thatFileInfo.playCounter);
//			isEqual &= (this.addedDate == thatFileInfo.addedDate || (this.addedDate != null && this.addedDate.equals(thatFileInfo.addedDate)));
//			isEqual &= (this.lastPlayed == thatFileInfo.lastPlayed || (this.lastPlayed != null && this.lastPlayed.equals(thatFileInfo.lastPlayed)));
			return isEqual;
		}
		return false;
	}
	
	/**
	 * Overring method as overrided equals()
	 * @return
	 */
	@Override
	public int hashCode() {
		
		  int hash = 7;
		  //Not comparing relativeFullPath as it can be in different format (Windows vs Unix)
		//Moreover it is not requiered to compare that value (should be the same)
//		  hash = 31 * hash + (null == this.relativeFullPath ? 0 : this.relativeFullPath.hashCode());
		  hash = 31 * hash + this.idFile;
//		  hash = 31 * hash + this.playCounter;
//		  hash = 31 * hash + (null == this.addedDate ? 0 : this.addedDate.hashCode());
//		  hash = 31 * hash + (null == this.lastPlayed ? 0 : this.lastPlayed.hashCode());

		  return hash;
	}
    
	/**
	 *
	 * @return
	 */
	public String getTitle() {
        return title;
    }
	
	/**
	 * Returns cover image
	 * @return
	 * @throws OutOfMemoryError  
	 */
	public BufferedImage getCoverImage() {
		if(coverImage==null) {
			//First, try reading from file
			this.readCoverFromTag();
			if(coverImage==null) {
				this.setEmptyCover();
			}
		}
		return coverImage;
	}

	/**
	 * Unsets cover to free memory
	 */
	public void unsetCover() {
        this.coverImage=null;
    }
    
	/**
	 * Returns file modification date in "yyyy-MM-dd HH:mm:ss" format
	 * @return
	 */
	public String getFormattedModifDate() {
		return DateTime.formatUTCtoSqlUTC(this.modifDate);
		
	}
	
	/**
	 * Called dynamically by group function in FolderInfo
	 * WARNING: It may exists no usage of that function (dynamic call)
	 * DO NOT REMOVE before having done a "Find in Projects..." !
	 * @return
	 */
	public boolean hasID3v1() {
		return hasID3v1;
	}
	
    /**
	 * Called dynamically by group function in FolderInfo
	 * WARNING: It may exists no usage of that function (dynamic call)
	 * DO NOT REMOVE before having done a "Find in Projects..." !  
	 * @return
	 */
    public String getCoverHash() {
        return coverHash;
    }

    /**
     * Set cover hash value
     * @param coverHash
     */
    public void setCoverHash(String coverHash) {
        this.coverHash = coverHash;
    }
   
	/**
	 * Called dynamically by group function in FolderInfo
	 * WARNING: It may exists no usage of that function (dynamic call)
	 * DO NOT REMOVE before having done a "Find in Projects..." !
	 * @return
	 */
	public String getYear() {
		return this.year;
	}
	
	/**
	 * Called dynamically by group function in FolderInfo
	 * WARNING: It may exists no usage of that function (dynamic call)
	 * DO NOT REMOVE before having done a "Find in Projects..." ! 
	 * @return
	 */
	public String getArtist() {
		return this.artist;
	}
	
	/**
	 * Called dynamically by group function in FolderInfo
	 * WARNING: It may exists no usage of that function (dynamic call)
	 * DO NOT REMOVE before having done a "Find in Projects..." !   
	 * @return
	 */
	public String getAlbumArtist() {
		return this.albumArtist;
	}
	
	/**
	 * Called dynamically by group function in FolderInfo
	 * WARNING: It may exists no usage of that function (dynamic call)
	 * DO NOT REMOVE before having done a "Find in Projects..." !  
	 * @return
	 */
	public String getAlbum() {
		return this.album;
	}

	/**
	 * Called dynamically by group function in FolderInfo
	 * WARNING: It may exists no usage of that function (dynamic call)
	 * DO NOT REMOVE before having done a "Find in Projects..." !   
	 * @return
	 */
	public int getDiscTotal() {
		return this.discTotal;
	}
	
	/**
	 * Called dynamically by group function in FolderInfo
	 * WARNING: It may exists no usage of that function (dynamic call)
	 * DO NOT REMOVE before having done a "Find in Projects..." !   
	 * @return
	 */
	public String getRelease() {
		String separator="X7IzQsi3";  //NOI18N
		return this.artist + separator + this.album + separator + this.year + separator + this.trackTotal;
	}
	
	
	/**
	 *
	 * @param sayRated
	 */
	
    public void sayRating (boolean sayRated) {
        //TODO: Play a reminder at 1/3 and 2/3 of the trackGain
        
        //TODO: Do this as an option (and review calls before enabling back)
//        if(this.idFile>-1) {
//            if(rating<=0) {
//                playRessouce("HERE COMES A NEW CHALLENGER - YouTube.mp3");
//           }
//           else if(sayRated) {
//               playRessouce(rating+"Star.mp3");
//           }
//        }
    }
    
	/**
	 *
	 * @param filename
	 */
	public void playRessouce(final String filename) {
        //TODO: Use the basic player (if more efficient as do not need advance features fo that)
        
        Thread t = new Thread("Thread.FileInfoInt.playRessouce") {
            @Override
            public void run() {
                File ressource = Jamuz.getFile(filename, "data", "sound", "rating");
                if(ressource.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(ressource);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        AudioDevice audioDevice = new JavaSoundAudioDevice();
                        AdvancedPlayer advancedPlayer = new AdvancedPlayer(bis, audioDevice);
                        advancedPlayer.play();
                    } catch (FileNotFoundException | JavaLayerException ex) {
    //                    Popup.error(Inter.get("Error.Play")+" \""+filePath+"\"", ex);  //NOI18N
                    }
                }
            }
        };
        t.start();
    }
    
	/**
	 *
	 * @return
	 */
	@Override
	public ArrayList<String> getTags() {
        if(tags==null) {
            tags=readTags();
        }
        return tags;
    }
	
	/**
	 *
	 * @param value
	 */
	public void toggleTag(String value) {
        if(tags.contains(value)) {
            tags.remove(value);
        }
        else {
            tags.add(value);
        }
    }
	
	/**
	 *
	 * @return
	 */
	public String toStringQueue() {
        return "<html>" + //NOI18N
                "[" + this.rating + "/5] <b>" + this.title + "</b><BR/>" + //NOI18N
                "<i>" + this.getAlbum() + "</i><BR/>" + //NOI18N
                "" + this.getArtist() + "</html>"; //NOI18N
    }

	public String toJson() {
		return JSONValue.toJSONString(toMap());
	}
	
	@Override
	public Map toMap() {
		Map jsonAsMap = super.toMap();
		jsonAsMap.put("size", size);
		return jsonAsMap;
	}
	
	private GainValues replaygain = new GainValues();
	
	public GainValues getReplayGain(boolean read) {
		if(read || !replaygain.isValid()) {
			replaygain = ReplayGain.read(getFullPath(), ext);
		}
		return replaygain;
	}
	
	public void saveReplayGainToID3(GainValues gv) {
		if(ext.equals("mp3")) {
			try {
				MP3File mp3file = (MP3File)AudioFileIO.read(getFullPath());
				AbstractID3v2Tag tag = mp3file.getID3v2Tag();
				tag.removeFrame("TXXX");
				setCustomID3Tag(mp3file, "REPLAYGAIN_TRACK_GAIN", String.format(Locale.ROOT, "%.2f", gv.getTrackGain())+" dB");
				setCustomID3Tag(mp3file, "REPLAYGAIN_TRACK_PEAK", String.format(Locale.ROOT, "%.6f", gv.trackPeak));
				setCustomID3Tag(mp3file, "REPLAYGAIN_ALBUM_GAIN", String.format(Locale.ROOT, "%.2f", gv.getAlbumGain())+" dB");
				setCustomID3Tag(mp3file, "REPLAYGAIN_ALBUM_PEAK", String.format(Locale.ROOT, "%.6f", gv.albumPeak));
			} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
				Logger.getLogger(FileInfoInt.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	//http://id3.org/id3v2.3.0#User_defined_text_information_frame
	private boolean setCustomID3Tag(MP3File mp3file, String description, String text){
		try {
			AbstractID3v2Frame frame = new ID3v23Frame("TXXX");
			FrameBodyTXXX txxxBody = new FrameBodyTXXX();
			txxxBody.setDescription(description);
			txxxBody.setText(text);
			frame.setBody(txxxBody);
			AbstractID3v2Tag tag = mp3file.getID3v2Tag();
			tag.addField(frame);
			mp3file.commit();
		} catch (CannotWriteException ex) {
			Logger.getLogger(FileInfoInt.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		} catch (FieldDataInvalidException ex) {
			Logger.getLogger(FileInfoInt.class.getName()).log(Level.SEVERE, null, ex);
		}
		return true;
	}
}
