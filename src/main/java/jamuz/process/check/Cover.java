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

package jamuz.process.check;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.CoverArtException;
import fm.last.musicbrainz.coverart.CoverArtImage;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;
import jamuz.Jamuz;
import jamuz.utils.ImageUtils;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.Utils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.http.impl.client.DefaultHttpClient;

//TODO: Make some sub-classes (mb, lastfm, file,...)

/**
 * Cover class
 * @author phramusca <phramusca@gmail.com>
 */
public class Cover implements java.lang.Comparable {
	private final CoverType type;
	private String value;
	private final String name;
	private BufferedImage image;
    private String hash=""; //NOI18N
    private List<MbImage> coverArtArchiveList;
	
	//FIXME Z CHECK Make maxSize an option
	private static final int sizeMax = 1000;

    /**
     * Type of cover (MB, tag, file or url (lastfm))
     */
    public enum CoverType {

        /**
         * MusicBrainz
         */
        MB,

        /**
         * Audio file tag
         */
        TAG,

        /**
         * Image file
         */
        FILE,

        /**
         * Last.fm URL
         */
        URL;
    }
    
	/**
	 * Creates a new cover (type "url", "file" or "mb")
	 * @param type
	 * @param value
	 * @param name
	 */
	public Cover(CoverType type, String value, String name) {
		this.type = type;
		this.value = value;
		this.name = name;
        coverArtArchiveList=new ArrayList<>();
	}

	/**
	 * Creates a new cover from tag (image)
	 * @param name
	 * @param image
	 * @param hash
	 */
	public Cover(String name, BufferedImage image, String hash) {
		this.type = CoverType.TAG;  //NOI18N
		this.name = name;
		this.image = ImageUtils.shrinkImage(image, sizeMax);
        this.hash = hash;
        coverArtArchiveList=new ArrayList<>();
	}

	/**
	 * Retrieves the image
	 * @return
	 */
	public BufferedImage getImage() {
        if(image==null) {
			image = ImageUtils.shrinkImage(readImage(), sizeMax); //TODO: make it an option
		}
		return image;
	}
	
    /**
     * Set cover image
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    /**
     * Read images
     */
    public void readImages() {
        if(type.equals(CoverType.MB)) {  //NOI18N
            listCovertArtArchive();
             for(MbImage mbImage : coverArtArchiveList) {
                 mbImage.getImage();
             }
         }
         else {
             getImage();
         }
    }

	private BufferedImage readImage() {
		BufferedImage myImage=null;
		try {
			if(type.equals(CoverType.URL)) {
                myImage = ImageIO.read(Utils.getFinalURL(value));
            }
            else if(type.equals(CoverType.FILE)) {
                File file = new File(this.value); 
                myImage = ImageIO.read(file);
            }
            return myImage;
		} catch (IOException ex) {
            return null;
		}
	}

    /**
     * Get Cover Art Archive (MusicBrainz) list of images
     * @return
     */
    public List<MbImage> getCoverArtArchiveList() {
        return coverArtArchiveList;
    }
    
    /**
     * List Cover Art Archive (MusicBrainz) images
     */
    public void listCovertArtArchive() {
		if(coverArtArchiveList==null) {
            try {
                UUID mbid = UUID.fromString(this.value);
                CoverArt coverArt;
                coverArtArchiveList=new ArrayList<>();
                //Create connection to covertartarchive
				DefaultHttpClient httpClient = Jamuz.getHttpClient()==null?new DefaultHttpClient():Jamuz.getHttpClient();
                
//TODO: Pass httpClient. Seems I got a modified 2.0.1 of coverartarchive-api (n/a in maven) => Use latest 2.1.1 but has breaking changes ...
//                CoverArtArchiveClient client = new DefaultCoverArtArchiveClient(httpClient);
                
                CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();
                
                coverArt = client.getByMbid(mbid);
                if (coverArt != null) {
                    for (CoverArtImage coverArtImage : coverArt.getImages()) {
                        String msg="";  //NOI18N
                        if(coverArtImage.isBack() && coverArtImage.isFront()) {
                            msg+=Inter.get("Cover.Unknown");  //NOI18N
                        }
                        else if(coverArtImage.isFront()) {
                            msg+=Inter.get("Cover.Front");  //NOI18N
                        }
                        else if(coverArtImage.isBack()) {
                            msg+=Inter.get("Cover.Back");  //NOI18N
                        }
                        else {
                            msg+=Inter.get("Cover.Other");  //NOI18N
                        }
                        msg+=" (";  //NOI18N
                        if(!coverArtImage.isApproved()) {
                            msg+=Inter.get("Cover.Not")+" ";  //NOI18N
                        }
                        msg+=Inter.get("Cover.Approved")+")";  //NOI18N
                        if(!coverArtImage.getComment().isBlank()) {  //NOI18N
                            msg+=" ["+coverArtImage.getComment()+"]";  //NOI18N
                        }
                        coverArtArchiveList.add(new MbImage(coverArtImage, msg));
                    }
                }
            } catch (CoverArtException e) {
              Popup.error(e);
            }
        }
	}
    
    /**
     * Cover Art Archive (MusicBrainz) image
     */
    public static class MbImage  {
        private final CoverArtImage coverArtImage;
        private final String msg;
        private BufferedImage image;

        /**
         * Creates a new Cover Art Archive (MusicBrainz) image
         * @param coverArtImage
         * @param msg
         */
        public MbImage(CoverArtImage coverArtImage, String msg) {
            this.coverArtImage = coverArtImage;
            this.msg = msg;
        }

        /**
         * Get message (
         * @return
         */
        public String getMsg() {
            return msg;
        }

        /**
         * Get image
         * @return
         */
        public BufferedImage getImage() {

            if (coverArtImage != null) {
                if(image==null) {
                    try {
                        //TODO: Enable 3 sizes (add to the list so that user can select those)
        //				mbImage.getLargeThumbnail()
        //				mbImage.getSmallThumbnail()
        //				mbImage.getImage()
        //				mbImage.getTypes()
                        image = ImageIO.read(coverArtImage.getLargeThumbnail());
                    } catch (IOException ex) {
                        Popup.error(ex);
                    }
                }
            }
            return image;
        }
    }
    
	/**
	 * Return cover hash value
	 * @return
	 */
	public String getHash() {
        return hash;
    }
    
	/**
	 * Returns cover name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns cover type ("url", "file", "tag" or "mb")
	 * @return
	 */
	public CoverType getType() {
		return type;
	}

	/**
	 * Returns cover value (an url, a file path or a MusicBrainz ID)
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public int compareTo(Object obj) {
        
        if(image==null) {
			return 1;
		}
        int width=image.getWidth();
        int height=image.getHeight();
        int size=width*height; 
        
        Cover other = (Cover) obj;
        if(other.getImage()==null) {
			return -1;
		}
        int otherWidth=other.getImage().getWidth();
        int otherHeight=other.getImage().getHeight();
        int otherSize=otherWidth*otherHeight; 
        
        int maxSize=this.sizeMax;
        maxSize *= maxSize;
        
        if(size>maxSize) {
			return 1;
		}
        if(otherSize>maxSize) {
			return -1;
		}
        
        //Favor square sizes (ex: 300x300)
        //TODO: +10 is not enough (same for CoverType lower down)
//        int diffSize = Math.abs(width-height);
//        if(diffSize>0) size=size-2*diffSize;
        
        if(width==height) {
			size+=10;
		}
        if(otherWidth==otherHeight) {
			otherSize+=10;
		}
        
        //Favor Last FM and MB over file and tag
        if(this.type.equals(CoverType.MB) || this.type.equals(CoverType.URL)) {
			size+=10;
		}
        if(other.type.equals(CoverType.MB) || other.type.equals(CoverType.URL)) {
			otherSize+=10;
		}
        
        if(size>otherSize) {
			return -1;
		}
        if(size<otherSize) {
			return 1;
		}

		return this.name.compareTo(other.name);
	}

	/**
	 *
	 * @param obj
	 * @return
	 */
	@Override
    public boolean equals(Object obj) {
        if(obj==null) {
			return false;
		}
        if(!obj.getClass().equals(this.getClass())) {
			return false;
		}
        return name.equals(((Cover) obj).name);
    }

	/**
	 *
	 * @return
	 */
	@Override
    public int hashCode() {
        int hashCode = 3;
        hashCode = 79 * hashCode + Objects.hashCode(this.name);
        return hashCode;
    }

	/**
	 *
	 * @return
	 */
	public String getSizeHTML() {
        String msg;
        msg="<html>"; //NOI18N
        msg+=getSizeDisplay();
        msg+="</html>";
        return msg;
    }
    
    private String getSizeDisplay() {
        String msg="";
        int width=image.getWidth();
        int height=image.getHeight();
        String size =width+"x"+height; //NOI18N
        if(width<200 || height<200) {
            msg+=FolderInfoResult.colorField(size, 1, false);
        }
        else if(width<100 || height<100) {
            msg+=FolderInfoResult.colorField(size, 2, false);
        }
        else {
            msg+="<b>"+FolderInfoResult.colorField(size, 0, false)+"</b>"; //NOI18N
        }
        return msg;
    }
    
	/**
	 *
	 * @return
	 */
	@Override
    public String toString() {
        String msg;
        msg="<html>"; //NOI18N
        msg+=getSizeDisplay();
        msg+="<BR/>"; //NOI18N
        if(this.type.equals(CoverType.URL)) {
            msg+="<b><font color=\"#005F00\">"+name+"</font></b>";  //NOI18N
        }
        else {
            msg+=name;
        }
        msg+="<BR/><BR/></html>"; //NOI18N
        
        return msg;
    }

}