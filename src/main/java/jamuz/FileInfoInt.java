/*
 * Copyright (C) 2011 phramusca <phramusca@gmail.com>
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

import jamuz.process.sync.SyncStatus;
import jamuz.process.check.FolderInfo;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.process.check.FolderInfoResult;
import jamuz.process.check.ReplayGain;
import jamuz.process.check.ReplayGain.GainValues;
import jamuz.utils.DateTime;
import jamuz.utils.ImageUtils;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.json.simple.JSONValue;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class FileInfoInt extends FileInfo {
//Tag information
	//TODO: Eventually support some more tags

	/**
	 * Artist name
	 */
	protected String artist = "";  //NOI18N

	/**
	 * Album name
	 */
	protected String album = "";  //NOI18N

	/**
	 * Album Artist name
	 */
	protected String albumArtist = "";  //NOI18N
	protected String title = "";  //NOI18N
	protected int trackNo = -1;
	protected SyncStatus status = SyncStatus.INFO;

	public void setStatus(SyncStatus status) {
		this.status = status;
		}
	
    public SyncStatus getStatus() {
        return status;
    }

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
	protected int trackTotal = -1;

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
	protected int discNo = -1;

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
	protected int discTotal = -1;

	/**
	 * Song year
	 */
	protected String year = "";  //NOI18N
	/**
	 * Song comment
	 */
	protected String comment = "";  //NOI18N

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
	protected int nbCovers = 0;

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

	protected Date pathModifDate = new Date(0);
	protected String pathMbid;

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
	private String coverHash = ""; //NOI18N
	private BufferedImage coverImage = null;
	private boolean hasID3v1 = false;

//File information
	/**
	 * File bitRate
	 */
	private String bitRate = "";  //NOI18N

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
	private String format = "";  //NOI18N

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
	protected String lengthDisplay = "";

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
	protected String sizeDisplay = "";

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
	 * File root path Used for play queue as can be filled up from library or
	 * Add tab
	 */
	protected String rootPath = "";  //NOI18N

	/**
	 * Indicates if file is from library so if rating, genre can be edited
	 */
	private boolean fromLibrary = false;

	/**
	 *
	 * @return
	 */
	public boolean isFromLibrary() {
		return fromLibrary;
	}

	private final FolderInfo.CopyRight copyRight; //TODO: Move to FolderInfo (as CheckedFlag)

	/**
	 *
	 * @return
	 */
	public FolderInfo.CopyRight getCopyRight() {
		return copyRight;
	}

	/**
	 *
	 */
	protected String lyrics = "";

	/**
	 * Used to merge BPM
	 *
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
		copyRight = FolderInfo.CopyRight.UNDEFINED;
		this.rootPath = Jamuz.getMachine().getOptionValue("location.library");  //NOI18N
	}

	/**
	 * Used for creating a FileInfoInt while scanning library Calling
	 * getFileInfo() to read tags from file
	 *
	 * @param relativeFullPath
	 * @param rootPath
	 */
	public FileInfoInt(String relativeFullPath, String rootPath) {
		super(-1, -1, relativeFullPath, -1, "1970-01-01 00:00:00",
				"1970-01-01 00:00:00", 0, "file", 0, 0, "", "", "", "");  //NOI18N
		this.rootPath = rootPath;
		this.modifDate = new Date(getFullPath().lastModified());
		copyRight = FolderInfo.CopyRight.UNDEFINED;
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

	/**
	 * Used when retrieving file information from database
	 *
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
	 * @param coverHash
	 * @param checkedFlag
	 * @param copyRight
	 * @param albumRating
	 * @param percentRated
	 * @param rootPath
	 * @param status
	 * @param pathModifDate
	 * @param pathMbid
	 * @param replaygain
	 */
	public FileInfoInt(int idFile, int idPath, String relativePath,
			String filename, int length, String format, String bitRate,
			int size, float BPM, String album, String albumArtist,
			String artist, String comment, int discNo, int discTotal,
			String genre, int nbCovers, String title, int trackNo,
			int trackTotal, String year, int playCounter, int rating,
			String addedDate, String lastPlayed, String modifDate, 
			String coverHash, CheckedFlag checkedFlag,
			FolderInfo.CopyRight copyRight, double albumRating,
			int percentRated, String rootPath, SyncStatus status,
			String pathModifDate, String pathMbid, GainValues replaygain) {

		super("file", FilenameUtils.separatorsToSystem(relativePath) + filename);  //NOI18N
		this.fromLibrary = true;
		this.idFile = idFile;
		this.idPath = idPath;
		this.rootPath = rootPath;
		//Set File info
		this.length = length;
		this.lengthDisplay = String.valueOf(this.length);
		this.format = format;
		this.bitRate = bitRate;
		this.modifDate = DateTime.parseSqlUtc(modifDate);
		this.size = size;
		this.sizeDisplay = String.valueOf(this.size);
		this.BPM = BPM;
		//Set file metadata
		this.album = album;
		this.albumArtist = albumArtist;
		this.artist = artist;
		this.comment = comment;
		this.discNo = discNo;
		this.discTotal = discTotal;
		this.genre = genre;
		this.nbCovers = nbCovers;
		this.coverHash = coverHash;
		this.title = title;
		this.trackNo = trackNo;
		this.trackTotal = trackTotal;
		this.year = year;
		//Set statistics
		this.playCounter = playCounter;
		this.rating = rating;
		this.addedDate = DateTime.parseSqlUtc(addedDate);
		this.lastPlayed = DateTime.parseSqlUtc(lastPlayed);
		//NOT needed here:
//		this.sourceName
//		this.hash
//		this.index

		//From path table
		this.checkedFlag = checkedFlag;
		this.copyRight = copyRight;
		this.albumRating = albumRating;
		this.percentRated = percentRated;
		this.status = status;
		this.pathModifDate = DateTime.parseSqlUtc(pathModifDate);
		this.pathMbid = pathMbid;
		this.replaygain = replaygain;
	}

	/**
	 * Set root path
	 *
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
		if (lyrics.isBlank()) {
			try {
				AudioFile myAudioFile = AudioFileIO.read(getFullPath());
				Tag tag = myAudioFile.getTag();
				if (tag != null) {
					if (tag.getFirst(FieldKey.LYRICS) != null) {
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
	private void readCoverFromMetadata() {
		try {
			if (this.nbCovers > 0) {
				File currentFile = getFullPath();
				if (currentFile.exists()) {
					Tag tag;
					switch (this.ext) {
						case "mp3": //NOI18N
							//Read MP3 file
							MP3File myMP3File = (MP3File) AudioFileIO.read(currentFile);
							tag = myMP3File.getTag();
							break;
						default:
							//Read audio file
							AudioFile myAudioFile = AudioFileIO.read(currentFile);
							tag = myAudioFile.getTag();
							break;
					}
					if (tag != null) {
						//Check presence of a cover
						if (tag.getFirstArtwork() != null) {
							readCover(tag);
						}
					}
				}
			}
		} catch (ClosedByInterruptException ex) {
			//Not doing anything, not a problem, simply occurs when changing from one playlist to another
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
			Jamuz.getLogger().log(Level.SEVERE, java.text.MessageFormat.format(Inter.get("Error.ReadingCover"), new Object[]{this.relativeFullPath}));
			//Not poping anoying errors that we cannot fix
//			Popup.error(java.text.MessageFormat.format(Inter.get("Error.ReadingCover"), new Object[] {this.relativeFullPath}), ex);  //NOI18N
		}

	}

	private void readCoverHash() throws NoSuchAlgorithmException, IOException {
		if (coverImage != null) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(coverImage, "png", outputStream); //NOI18N
			byte[] data = outputStream.toByteArray();
			MessageDigest md = MessageDigest.getInstance("SHA-256"); //NOI18N
			byte[] hash = md.digest(data);
			coverHash = returnHex(hash);
		}
	}

	static String returnHex(byte[] inBytes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < inBytes.length; i++) { //for loop ID:1
			builder.append(Integer.toString((inBytes[i] & 0xff) + 0x100, 16).substring(1));
		}                                   // Belongs to for loop ID:1
		return builder.toString();
	}

	/**
	 *
	 * @param readCover
	 * @return
	 */
	public boolean readMetadata(boolean readCover) {
		try {
			File currentFile = getFullPath();

			//Getting file information
			this.modifDate = new Date(currentFile.lastModified());
			this.size = currentFile.length();
			this.sizeDisplay = String.valueOf(this.size);

			//Read metadata, depending on file extension
			AudioHeader myHeader;
			Tag tag;
			switch (this.ext) {
				case "mp3": //NOI18N
					//Read MP3 file
					MP3File myMP3File = (MP3File) AudioFileIO.read(currentFile);
					myHeader = myMP3File.getAudioHeader();
					tag = myMP3File.getTag();
					//Read header
					this.readHeader(myHeader);
					//Read tags
					if (tag != null) {
						//First get tags from ID3v2
						if (myMP3File.hasID3v2Tag()) {
							AbstractID3v2Tag v2tag = myMP3File.getID3v2Tag();
							this.readTags(v2tag, readCover);
						}
						//Then, get tags from ID3v1 IF not found in ID3v2
						//Note: not all fields are available in ID3v1 compared to ID3v2
						if (myMP3File.hasID3v1Tag()) {
							this.hasID3v1 = true;
							ID3v1Tag v1Tag = myMP3File.getID3v1Tag();
							if (this.artist.isBlank()) {  //NOI18N
								this.artist = v1Tag.getFirst(FieldKey.ARTIST);
							}
							if (this.album.isBlank()) {  //NOI18N
								this.album = v1Tag.getFirst(FieldKey.ALBUM);
							}
							if (this.year.isBlank()) {  //NOI18N
								this.year = v1Tag.getFirst(FieldKey.YEAR);
							}
							if (this.title.isBlank()) {  //NOI18N
								this.title = v1Tag.getFirst(FieldKey.TITLE);
							}
							if (this.trackNo < 1) {
								this.trackNo = getInt(v1Tag.getFirst(FieldKey.TRACK));
							}
							if (this.genre.isBlank()) {  //NOI18N
								this.genre = v1Tag.getFirst(FieldKey.GENRE);
								//TODO: Translate v1 genres into String
								//http://fr.wikipedia.org/wiki/ID3
								//https://github.com/drogatkin/tiny-codec/blob/master/codecs/APE/src/java/davaguine/jmac/info/ID3Genre.java
							}
							//Reading this in case user do not want to remove comments
							if (this.comment.isBlank()) {  //NOI18N
								this.comment = v1Tag.getFirst(FieldKey.COMMENT);
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
					if (tag != null) {
						this.readTags(tag, readCover);
					}
			}
			return true;
		} catch (IllegalArgumentException | CannotReadException | IOException
				| TagException | ReadOnlyFileException | InvalidAudioFrameException
				| OutOfMemoryError ex) {
			//TODO: Find a better way of displaying error
			//Usefull to return sthg then ?
			this.comment = "ERROR: " + ex.toString();  //NOI18N
			return false;
		}
	}

	private void readTags(Tag tag, boolean readCover) throws IOException {
		//Get number of covers and read first one as required
		this.nbCovers = tag.getArtworkList().size();
		if (this.nbCovers > 0 && readCover) {
			this.readCover(tag);
		}

		this.album = tag.getFirst(FieldKey.ALBUM);
		this.artist = tag.getFirst(FieldKey.ARTIST);
		this.albumArtist = tag.getFirst(FieldKey.ALBUM_ARTIST);
		try {
			this.setBPM(tag.getFirst(FieldKey.BPM).isBlank() ? 0 : Float.parseFloat(tag.getFirst(FieldKey.BPM)));
		} catch (NumberFormatException ex) {
		}
		this.comment = tag.getFirst(FieldKey.COMMENT);
		this.discNo = getInt(tag.getFirst(FieldKey.DISC_NO));
		this.discTotal = getInt(tag.getFirst(FieldKey.DISC_TOTAL));
		this.genre = tag.getFirst(FieldKey.GENRE);
		this.title = tag.getFirst(FieldKey.TITLE);
		this.year = tag.getFirst(FieldKey.YEAR);
		this.trackTotal = getInt(tag.getFirst(FieldKey.TRACK_TOTAL));
		this.trackNo = getInt(tag.getFirst(FieldKey.TRACK));
	}

	private void readCover(Tag tag) throws IOException {
		Artwork myArt = tag.getFirstArtwork();
		try {
			coverImage = (BufferedImage) myArt.getImage();
			readCoverHash();
		} catch (IndexOutOfBoundsException | NullPointerException | NoSuchAlgorithmException ex) {
			//Do nothing
		}
	}

	private void readHeader(AudioHeader myHeader) {
		this.bitRate = myHeader.getBitRate();
		//TODO: support channels
//			this.channels=myHeader.getChannels();
		this.format = myHeader.getFormat();
		//TODO: support sample rate
//			this.sampleRate=myHeader.getSampleRate();
		this.length = myHeader.getTrackLength();
		this.lengthDisplay = String.valueOf(this.length);
		//TODO: support isVariableBitRate
//			this.isVariableBitRate=myHeader.isVariableBitRate();
	}

	private void setEmptyCover() {
		coverImage = ImageUtils.getEmptyCover();
	}

	private int getInt(String value) {
		int castValue = -1;
		try {
			castValue = Integer.parseInt(value);
		} catch (NumberFormatException ex) {
		}
		return castValue;
	}

	/**
	 * Save tags to file
	 *
	 * @param deleteComment
	 * @return
	 */
	public boolean saveTags(boolean deleteComment) {
		return this.saveMetadata(this.artist, this.albumArtist, this.album, this.trackNo, this.trackTotal, this.discNo, this.discTotal,
				this.genre, this.year, this.coverImage, deleteComment, this.comment, this.title, this.BPM, this.lyrics);  //NOI18N
	}

	/**
	 * Convert a BufferedImage to Artwork (in order to save it in file tags)
	 *
	 * @param image
	 * @return
	 */
	private Artwork imageToArt(BufferedImage image) {
		Artwork myArt = null;
		//Store cover to a temp file (as dunno how to save it to tag apart from a file)
		String tempCoverFile = System.getProperty("java.io.tmpdir") + File.separator + "JaMuzTempCover.png";   //NOI18N //NOI18N //NOI18N
		File file = new File(tempCoverFile);
		file.delete();
		try {
			if (image != null) {
				ImageIO.write(image, "png", file);  //NOI18N
				if (file.exists()) {
					myArt = ArtworkFactory.createArtworkFromFile(file);
				}
				return myArt;
			} else {
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
	protected boolean saveMetadata(String artist, String albumArtist, String album,
			int trackNo, int trackTotal, int discNo, int discTotal,
			String genre, String year, BufferedImage image, boolean deleteComment, String comment, String title, float bpm, String lyrics) {
		try {

			File file = getFullPath();
			if (file.length() <= 0) {
				return false;
			}
			Artwork artwork = imageToArt(image);
			switch (this.ext) {
				case "mp3": //NOI18N
					MP3File MP3File = (MP3File) AudioFileIO.read(file);
					//Remove ID3v1 tags
					MP3File.setID3v1Tag(null);
					//Create brand new ID3v2 tags
					AbstractID3v2Tag v2tag = new ID3v23Tag();
					MP3File.setID3v2Tag(v2tag);
					this.setMetadata(v2tag, artist, albumArtist, album, trackNo, trackTotal, discNo, discTotal, genre, year,
							artwork, deleteComment, comment, title, bpm, lyrics);
					MP3File.commit();
					break;
				default:
					AudioFile audioFile = AudioFileIO.read(file);
					Tag tag = audioFile.getTag();
					this.setMetadata(tag, artist, albumArtist, album, trackNo, trackTotal, discNo, discTotal, genre, year,
							artwork, deleteComment, comment, title, bpm, lyrics);
					audioFile.commit();
					break;
			}

			if (this.idFile > -1) { //File displayed in player may not be from database (check new)
				Jamuz.getDb().file().lock().setSaved(idFile);
			}
			return true;
		} catch (CannotWriteException | CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | IllegalArgumentException ex) {
			Popup.error("Error writing tags to \"" + getFullPath() + "\"", ex);  //NOI18N
			return false;
		}
	}

	private void setMetadata(Tag tag, String artist, String albumArtist, String album, int trackNo, int trackTotal, int discNo, int discTotal,
			String genre, String year, Artwork myArt, boolean deleteComment, String comment, String title, float bpm, String lyrics) throws KeyNotFoundException, FieldDataInvalidException {

		//Note: Since we are overwritting all tags, we are only inserting non-empty ones
		if (!lyrics.isBlank()) {  //NOI18N
			tag.setField(FieldKey.LYRICS, lyrics);
		}
		if (!artist.isBlank()) {  //NOI18N
			tag.setField(FieldKey.ARTIST, artist);
		}
		if (!albumArtist.isBlank()) {  //NOI18N
			tag.setField(FieldKey.ALBUM_ARTIST, albumArtist);
		}
		if (!album.isBlank()) {  //NOI18N
			tag.setField(FieldKey.ALBUM, album);
		}
		if (trackNo > 0) {
			tag.setField(FieldKey.TRACK, String.valueOf(trackNo));
		}
		if (trackTotal > 0) {
			tag.setField(FieldKey.TRACK_TOTAL, String.valueOf(trackTotal));
		}
		if (discNo > 0) {
			tag.setField(FieldKey.DISC_NO, String.valueOf(discNo));
		}
		if (discTotal > 0) {
			tag.setField(FieldKey.DISC_TOTAL, String.valueOf(discTotal));
		}
		if (!title.isBlank()) {  //NOI18N
			tag.setField(FieldKey.TITLE, title);
		}
		if (!genre.isBlank()) {  //NOI18N
			tag.setField(FieldKey.GENRE, genre);
		}
		if (!year.isBlank()) {  //NOI18N
			tag.setField(FieldKey.YEAR, year);
		}
		if (myArt != null) {
			tag.setField(myArt);
			this.nbCovers = 1;
		}
		if (deleteComment) { //NOI18N
			tag.setField(FieldKey.COMMENT, "");
		} else if (!comment.isBlank()) { //NOI18N
			tag.setField(FieldKey.COMMENT, comment);
		}
		if (bpm > 0) {  //NOI18N
			tag.setField(FieldKey.BPM, Float.toString(bpm));
		}
	}

	/**
	 * Insert tags in database
	 *
	 * @return
	 */
	public boolean insertTagsInDb() {
		int[] key = new int[1]; //Hint: Using a int table as cannot pass a simple integer by reference
		boolean result = Jamuz.getDb().file().lock().insert(this, key);
		this.idFile = key[0]; //Get insertion key
		return result;
	}

	/**
	 * Update tags in database
	 *
	 * @return
	 */
	public boolean updateTagsInDb() {
		return Jamuz.getDb().file().lock().update(this);
	}

	/**
	 *
	 * @return
	 */
	public boolean saveMetadataBPM() {
		return this.saveMetadata(FieldKey.BPM, String.valueOf(getBPM()));
	}

	/**
	 *
	 * @param lyrics
	 * @return
	 */
	public boolean saveMetadataLyrics(String lyrics) {
		this.lyrics = lyrics;
		return this.saveMetadata(FieldKey.LYRICS, lyrics);
	}

	private boolean saveMetadata(FieldKey key, String value) {
		return saveMetadata(new HashMap<FieldKey, String>() {
			{
				put(key, value);
			}
		});
	}

	private boolean saveMetadata(Map<FieldKey, String> keyValues) {
		try {
			File testFile = getFullPath();
			switch (this.ext) {
				case "mp3": //NOI18N
					MP3File MP3File = (MP3File) AudioFileIO.read(testFile);
					AbstractID3v2Tag v2tag = MP3File.getID3v2Tag();
					for (Map.Entry<FieldKey, String> entry : keyValues.entrySet()) {
						v2tag.setField(entry.getKey(), entry.getValue());
					}
					MP3File.commit();
					break;
				default:
					AudioFile audioFile = AudioFileIO.read(testFile);
					Tag tag = audioFile.getTag();
					for (Map.Entry<FieldKey, String> entry : keyValues.entrySet()) {
						tag.setField(entry.getKey(), entry.getValue());
					}
					audioFile.commit();
					break;
			}
			return true;
		} catch (CannotReadException | IOException | TagException
				| ReadOnlyFileException | InvalidAudioFrameException
				| CannotWriteException ex) {
			Popup.error("Error writing \"" + keyValues.toString() + "\" to \"" + getFullPath() + "\"", ex);  //NOI18N
			return false;
		}

	}

	/**
	 *
	 * @param genre
	 * @return
	 */
	public boolean updateGenre(String genre) {
		if (this.saveMetadata(FieldKey.GENRE, genre)) {
			this.genre = genre;
			if (this.idFile > -1) { //File displayed in player may not be from database (check new)
				return Jamuz.getDb().file().lock().updateFileGenre(this);
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
		this.rating = Integer.valueOf(rating);
		if (this.idFile > -1) { //File displayed in player may not be from database (check new)
			return Jamuz.getDb().file().lock().updateRating(this);
		}
		return true;
	}

	/**
	 * Updates in database
	 *
	 * @return
	 */
	public boolean updateInDb() {
		this.modifDate = new Date(getFullPath().lastModified());
		return Jamuz.getDb().file().lock().updateModifDate(this.idFile, this.modifDate, this.getFilename());
	}

	/**
	 * Scans for deleted files
	 *
	 * @return
	 */
	public boolean scanDeleted() {
		File currentFile = getFullPath();
		if (!currentFile.exists()) {
			if (!Jamuz.getDb().file().lock().delete(this.idFile)) {
				return false;
			}
		}
		return true;
	}

	//TODO: Use this function. 
	/**
	 * Save cover to file if it exists
	 *
	 * @param overwrite
	 * @return
	 */
	public FileInfoInt saveCoverToFile(boolean overwrite) {

		FileInfoInt coverFileInfoInt = null;
		try {
			if (this.nbCovers > 0) {
				//Save cover as a file (same name as music file, changing extension to png
				String coverRelativeFullPath = FilenameUtils.removeExtension(this.relativeFullPath) + ".png";  //NOI18N
				File file = new File(this.rootPath + coverRelativeFullPath);
				if (!file.exists() || overwrite) {
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
	 * Return track number in "xx/yy" format
	 *
	 * @return
	 */
	public String getTrackNoFull() {
		return FolderInfoResult.formatNumber(this.trackNo) + "/" + FolderInfoResult.formatNumber(this.trackTotal);  //NOI18N
	}

	/**
	 *
	 * @param trackNoFull
	 */
	public void setTrackNoFull(String trackNoFull) {
		String[] splitted = trackNoFull.split("/");
		if (splitted.length == 2) {
			this.trackNo = Integer.valueOf(splitted[0]);
			this.trackTotal = Integer.valueOf(splitted[1]);
		}
	}

	/**
	 * Return disc number in "xx/yy" format
	 *
	 * @return
	 */
	public String getDiscNoFull() {
		return FolderInfoResult.formatNumber(this.discNo) + "/" + FolderInfoResult.formatNumber(this.discTotal);  //NOI18N
	}

	/**
	 *
	 * @param discNoFull
	 */
	public void setDiscNoFull(String discNoFull) {
		String[] splitted = discNoFull.split("/");
		if (splitted.length == 2) {
			this.discNo = Integer.valueOf(splitted[0]);
			this.discTotal = Integer.valueOf(splitted[1]);
		}
	}

	/**
	 * Replaces %artist%, %albumGain%, ... by their actual values
	 *
	 * @param mask
	 * @param albumArtist
	 * @param album
	 * @param genre
	 * @return
	 */
	public String computeMask(String mask, String albumArtist, String album, String genre) {
		String strResult = mask;

		String artistName = artist.isBlank() ? "{Empty}" : artist;
		String titleName = title.isBlank() ? "{Empty}" : title;
		strResult = strResult.replace("%artist%", StringManager.removeIllegal(artistName));  //NOI18N
		strResult = strResult.replace("%albumartist%", StringManager.removeIllegal(albumArtist));  //NOI18N
		strResult = strResult.replace("%album%", StringManager.removeIllegal(album));  //NOI18N
		strResult = strResult.replace("%genre%", StringManager.removeIllegal(genre));  //NOI18N

		String titleStr = "";  //NOI18N
		if (!artistName.equals(albumArtist)) {
			titleStr = artistName + " - ";  //NOI18N
		}
		titleStr += titleName;
		strResult = strResult.replace("%title%", StringManager.removeIllegal(titleStr));  //NOI18N

		String trackStr = "";  //NOI18N
		if (discTotal > 1 && discNo > 0) {
			trackStr = "[" + discNo + "-" + discTotal + "] - ";  //NOI18N
		}
		if (this.trackNo > 0) {
			trackStr += FolderInfoResult.formatNumber(trackNo);
		}
		strResult = strResult.replace("%track%", trackStr);  //NOI18N

		strResult = FilenameUtils.separatorsToSystem(strResult);

		return strResult;
	}

	@Override
	public int compareTo(Object o) {

		//ORDER BY discNo, trackNo, filename
		if (this.discNo < ((FileInfoInt) o).discNo) {
			return -1;
		}
		if (this.discNo > ((FileInfoInt) o).discNo) {
			return 1;
		}

		if (this.trackNo < ((FileInfoInt) o).trackNo) {
			return -1;
		}
		if (this.trackNo > ((FileInfoInt) o).trackNo) {
			return 1;
		}

		return this.getFilename().compareTo(((FileInfo) o).getFilename());
	}

	/**
	 * Overring method for removing duplicates in playlists ("Inde" match)
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
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
	 *
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
	 *
	 * @return
	 * @throws OutOfMemoryError
	 */
	public BufferedImage getCoverImage() {
		if (coverImage == null) {
			//First, try reading from file
			this.readCoverFromMetadata();
			if (coverImage == null) {
				this.setEmptyCover();
			}
		}
		return coverImage;
	}

	/**
	 * Unsets cover to free memory
	 */
	public void unsetCover() {
		this.coverImage = null;
	}

	/**
	 * Returns file modification date in "yyyy-MM-dd HH:mm:ss" format
	 *
	 * @return
	 */
	public String getFormattedModifDate() {
		return DateTime.formatUTCtoSqlUTC(this.modifDate);

	}

	/**
	 * Called dynamically by group function in FolderInfo WARNING: It may exists
	 * no usage of that function (dynamic call) DO NOT REMOVE before having done
	 * a "Find in Projects..." !
	 *
	 * @return
	 */
	public boolean hasID3v1() {
		return hasID3v1;
	}

	/**
	 * Called dynamically by group function in FolderInfo WARNING: It may exists
	 * no usage of that function (dynamic call) DO NOT REMOVE before having done
	 * a "Find in Projects..." !
	 *
	 * @return
	 */
	public String getCoverHash() {
		return coverHash;
	}

	/**
	 * Set cover hash value
	 *
	 * @param coverHash
	 */
	public void setCoverHash(String coverHash) {
		this.coverHash = coverHash;
	}

	/**
	 * Called dynamically by group function in FolderInfo WARNING: It may exists
	 * no usage of that function (dynamic call) DO NOT REMOVE before having done
	 * a "Find in Projects..." !
	 *
	 * @return
	 */
	public String getYear() {
		return this.year;
	}

	/**
	 * Called dynamically by group function in FolderInfo WARNING: It may exists
	 * no usage of that function (dynamic call) DO NOT REMOVE before having done
	 * a "Find in Projects..." !
	 *
	 * @return
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * Called dynamically by group function in FolderInfo WARNING: It may exists
	 * no usage of that function (dynamic call) DO NOT REMOVE before having done
	 * a "Find in Projects..." !
	 *
	 * @return
	 */
	public String getAlbumArtist() {
		return this.albumArtist;
	}

	/**
	 * Called dynamically by group function in FolderInfo WARNING: It may exists
	 * no usage of that function (dynamic call) DO NOT REMOVE before having done
	 * a "Find in Projects..." !
	 *
	 * @return
	 */
	public String getAlbum() {
		return this.album;
	}

	/**
	 * Called dynamically by group function in FolderInfo WARNING: It may exists
	 * no usage of that function (dynamic call) DO NOT REMOVE before having done
	 * a "Find in Projects..." !
	 *
	 * @return
	 */
	public int getDiscTotal() {
		return this.discTotal;
	}

	/**
	 * Called dynamically by group function in FolderInfo WARNING: It may exists
	 * no usage of that function (dynamic call) DO NOT REMOVE before having done
	 * a "Find in Projects..." !
	 *
	 * @return
	 */
	public String getRelease() {
		String separator = "X7IzQsi3";  //NOI18N
		return this.albumArtist + separator + this.album;
	}

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public void setNbCovers(int nbCovers) {
        this.nbCovers = nbCovers;
    }
    
	/**
	 *
	 * @return
	 */
	@Override
	public ArrayList<String> getTags() {
		if (tags == null) {
			tags = readTags();
		}
		return tags;
	}

	/**
	 *
	 * @param value
	 */
	public void toggleTag(String value) {
		if (tags.contains(value)) {
			tags.remove(value);
		} else {
			tags.add(value);
		}
	}

	/**
	 *
	 * @return
	 */
	public String toStringQueue() {
		return "<html>"
				+ //NOI18N
				"[" + this.rating + "/5] <b>" + this.title + "</b><BR/>"
				+ //NOI18N
				"<i>" + this.getAlbum() + "</i><BR/>"
				+ //NOI18N
				"" + this.getArtist() + "</html>"; //NOI18N
	}

	public String toJson() {
		return JSONValue.toJSONString(toMap());
	}

	@Override
	public Map<String, Object> toMap() {
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonAsMap = (Map<String, Object>) super.toMap();
		jsonAsMap.put("size", size);
		jsonAsMap.put("length", length);
		jsonAsMap.put("album", album);
		jsonAsMap.put("artist", artist);
		jsonAsMap.put("title", title);
		jsonAsMap.put("status", status.name());
		jsonAsMap.put("idPath", idPath);
		jsonAsMap.put("albumArtist", albumArtist);
		jsonAsMap.put("bitRate", bitRate);
		jsonAsMap.put("discNo", discNo);
		jsonAsMap.put("discTotal", discTotal);
		jsonAsMap.put("format", format);
		jsonAsMap.put("trackNo", trackNo);
		jsonAsMap.put("trackTotal", trackTotal);
		jsonAsMap.put("year", year);
		jsonAsMap.put("BPM", BPM);
		jsonAsMap.put("checkedFlag", checkedFlag.name());
		jsonAsMap.put("copyRight", copyRight.name());
		jsonAsMap.put("coverHash", coverHash);
		jsonAsMap.put("modifDate", DateTime.formatUTCtoSqlUTC(modifDate));
		jsonAsMap.put("pathModifDate", DateTime.formatUTCtoSqlUTC(pathModifDate));
		jsonAsMap.put("pathMbid", pathMbid);
		jsonAsMap.put("comment", comment);
		jsonAsMap.put("replaygain", replaygain.toMap());
		return jsonAsMap;
	}

	private GainValues replaygain = new GainValues();

	public GainValues getReplayGain(boolean read) {
		if (read || !replaygain.isValid()) {
			replaygain = ReplayGain.read(getFullPath(), ext);
		}
		return replaygain;
	}

	public void saveReplayGainToID3(GainValues gv) {
		if (ext.equals("mp3")) {
			try {
				MP3File mp3file = (MP3File) AudioFileIO.read(getFullPath());
				AbstractID3v2Tag tag = mp3file.getID3v2Tag();
				tag.removeFrame("TXXX");
				setCustomID3Tag(mp3file, "REPLAYGAIN_TRACK_GAIN", String.format(Locale.ROOT, "%.2f", gv.getTrackGain()) + " dB");
				setCustomID3Tag(mp3file, "REPLAYGAIN_TRACK_PEAK", String.format(Locale.ROOT, "%.6f", gv.trackPeak));
				setCustomID3Tag(mp3file, "REPLAYGAIN_ALBUM_GAIN", String.format(Locale.ROOT, "%.2f", gv.getAlbumGain()) + " dB");
				setCustomID3Tag(mp3file, "REPLAYGAIN_ALBUM_PEAK", String.format(Locale.ROOT, "%.6f", gv.albumPeak));
			} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | IllegalArgumentException ex) {
				Logger.getLogger(FileInfoInt.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	//http://id3.org/id3v2.3.0#User_defined_text_information_frame
	private boolean setCustomID3Tag(MP3File mp3file, String description, String text) {
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

	public void transcode(String destExt) throws IllegalArgumentException, EncoderException {
		transcode(destExt, rootPath);
	}

	public boolean transcodeIfNeeded(String destPath, String destExt) throws IllegalArgumentException, EncoderException, IOException {
		if (transcodeRequired(destPath, destExt)) {
			readMetadata(true);
			getLyrics();
			transcode(destExt, destPath);
			unsetCover();
			return true;
		}
		return false;
	}

	public boolean transcodeRequired(String destPath, String destExt) throws IllegalArgumentException, EncoderException, IOException {
		if (!getExt().equals(destExt)) {
			File transcodedFile = getTranscodedFile(destExt, destPath);
			if (transcodedFile.exists()) {
				Date originalLastModified = new Date(getFullPath().lastModified());
				Date transcodedLastModified = new Date(transcodedFile.lastModified());
				if (originalLastModified.after(transcodedLastModified)) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	public void transcode(String destExt, String destPath) throws IllegalArgumentException, EncoderException {
		AudioAttributes audio = new AudioAttributes();
		audio.setBitRate(192000);
		audio.setChannels(2);
		audio.setSamplingRate(44100);
		EncodingAttributes attrs = new EncodingAttributes();
		switch (destExt) {
			case "ogg": //NOI18N
				//libvorbis works on windows but fails on linux mint
				//vorbis works on both :)
				audio.setCodec("vorbis");  //NOI18N
				//attrs.setFormat("ogg");  //NOI18N
				break;
			case "mp3": //NOI18N
			default: //Encoding to MP3 by default
				destExt = "mp3"; //NOI18N
				audio.setCodec("libmp3lame");  //NOI18N
				//attrs.setFormat("mp3");  //NOI18N
				break;
		}
		attrs.setAudioAttributes(audio);
		File target = getTranscodedFile(destExt, destPath);  //NOI18N
		Encoder encoder = new Encoder();
		encoder.encode(new MultimediaObject(getFullPath()), target, attrs);
		setRootPath(destPath);
		setExt(destExt);
		saveTags(true);
		readMetadata(false); //To get new file information (format, size,...)
	}

	public File getTranscodedFile(String destExt, String destPath) {
		return new File(FilenameUtils.concat(FilenameUtils.getFullPath(FilenameUtils.concat(destPath, this.relativeFullPath)),
				FilenameUtils.getBaseName(getFilename()) + "." + destExt));
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
