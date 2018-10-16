/*
 * Copyright (C) 2012 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import java.awt.Color;
import java.util.Objects;

/**
 * Folder information analysis result class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class FolderInfoResult {
		
	/**
	 * The value (raw, not formatted)
	 */
	protected String value = "";  //NOI18N

	/**
	 *
	 * @return
	 */
	public String getValue() {
        return value;
    }

	/**
	 *
	 * @param value
	 */
	public void setValue(String value) {
        this.value = value;
    }
    
    
	/**
	 * Error Level:
	 * 0: OK
	 * 1: Warning
	 * 2: KO
	 */
	protected int errorLevel = 0; //OK by default
	private int errorLevelFolder = 0; //OK by default, to keep errorLevel in case match is original
	/**
	 * Used to diplay tooltip on dedicated swing element
	 */
	protected String tooltip;

	/**
	 * Creates a new FolderInfoResult instance
	 */
	public FolderInfoResult() {
	}

	private void setErrorLevel(int errorlevel) {
		this.errorLevel = errorlevel;
		this.errorLevelFolder = errorlevel;
	}

	/**
	 * Sets result as KO
	 */
	public void setKO() {
		setKO(false);
	}

	/**
	 * Sets result as KO
	 * If match, do not set errorLevelFolder
	 * @param match
	 */
	public void setKO(boolean match) {
		if(match) {
			this.errorLevel=2;
		}
		else {
			this.setErrorLevel(2);
		}
	}

	/**
	 * Sets result as OK. 
	 * Always for match, so not touching errorLevelFolder
	 */
	public void setOK() {
		this.errorLevel=0;
	}

	/**
	 * Sets result as warning
	 */
	public void setWarning() {
		setWarning(false);
	}

	/**
	 * Sets result as warning
	 * If match, do not set errorLevelFolder
	 * @param match
	 */
	public void setWarning(boolean match) {
		if(match) {
			if(this.errorLevel<1) {
				this.errorLevel=1;
			}
		}
		else {
			if(this.errorLevel<1) {
				this.setErrorLevel(1);
			}
		}
	}

	/**
	 * Restores errorLevel to folder one
	 */
	public void restoreFolderErrorLevel() {
		this.errorLevel = this.errorLevelFolder;
	}

	/**
	 * Returns if result is not valid
	 * @return
	 */
	public boolean isNotValid() {
		return (this.errorLevel > 0);
	}

	/**
	 * Returns if result is KO
	 * @return
	 */
	public boolean isKO() {
		return (this.errorLevel > 1);
	}

	/**
	 * Returns if result is a Warning
	 * @return
	 */
	public boolean isWarning() {
		return (this.errorLevel == 1);
	}

	public int getErrorLevel() {
		return errorLevel;
	}

	/**
	 * Get Color to be used for display
	 * @return
	 */
	public Color getDisplayColor() {
		if(this.errorLevel>1) {
			//KO
			return Color.red;
		}
		else if(this.errorLevel>0) {
			//Warning
			return Color.orange;
		}
		else {
			//OK
			return new Color(0, 128, 0);
		}
	}

	/**
	 * Get value HTML formatted based on errorLevel
	 * @return
	 */
	public String getDisplayText() {
		return colorField(this.value, this.errorLevel);
	}
	
	/**
	 * Get tooltip HTML formatted based on errorLevel
	 * @return
	 */
	public String getDisplayToolTip() {
        if(this.tooltip!=null) {
            return colorField(this.tooltip, this.errorLevel);
        }
        else {
            return null; //To remove the tooltip
        }
	}
	
	/**
	 *
	 * @param tagValue
	 * @param matchValue
	 * @param field
	 * @return
	 */
	public String analyseTrack(String tagValue, String matchValue, String field) {
		switch (field) {
			case "discNoFull": //NOI18N
			case "trackNoFull":  //NOI18N
				return this.analyseTrackNumber(tagValue, matchValue);
			case "artist": //NOI18N
			case "title": //NOI18N
			case "albumArtist": //NOI18N
			case "album":  //NOI18N
			case "genre": //NOI18N
				return this.analyseTrackString(tagValue, matchValue);
			case "year":  //NOI18N
				return this.analyseTrackYear(tagValue, matchValue);
			case "comment": //NOI18N
				return this.analyseTrackComment(tagValue, matchValue);
			default:
				return "InvalidFieldShouldNotHappen"; //NOI18N
		}
	}
	
	/**
	 *
	 * @param tagValue
	 * @param matchValue
	 * @return
	 */
	public String analyseTrackBpm(Float tagValue, Float matchValue) {
		
        boolean checkBPM = true; //TODO: Make this an option
        int errLevel;
        if(matchValue.equals(tagValue)) {
            if(tagValue>0) {  //NOI18N
                errLevel=0;
            }
            else {
//                if(checkBPM) {
//                    this.setWarning();
//                }
                errLevel=1;
            }
		}
		else {
			if(checkBPM) {
                this.setKO();
            }
			errLevel=2;
		}
        return colorField(String.valueOf(tagValue), errLevel);
    }
    
	private String analyseTrackComment(String tagValue, String matchValue) {
		
		if(!tagValue.equals(matchValue)) {
			this.setKO(true);
			return colorField(tagValue, 2);
		}
		else if(!tagValue.equals("")) {  //NOI18N
			this.setWarning(true);  //NOI18N
			return colorField(tagValue, 1);
		}
		else {
			return colorField(tagValue, 0);
		}
	}
	
	private String analyseTrackYear(String tagValue, String matchValue) {
        if(tagValue.equals(matchValue)) {
            return colorField(tagValue, this.errorLevel);
        }
        else if(matchValue.equals("")) { //NOI18N
            if(tagValue.matches("\\d{4}")) { //NOI18N
                this.setWarning(true);
                return colorField(tagValue, 1);
            }
            else {
                this.setKO(true);
                return colorField(tagValue, 2);
            }
        }
        else {
            this.setKO(true);
            return colorField(tagValue, 2);
        }
	}
	
	private String analyseTrackNumber(String tagValue, String matchValue) {
		
		String tagNumber=""; //NOI18N
		String tagTotal=""; //NOI18N
		if(tagValue.contains("/")) { //NOI18N
			tagNumber = tagValue.split("/")[0]; //NOI18N
			tagTotal = tagValue.split("/")[1]; //NOI18N
		}
		String matchNumber=""; //NOI18N
		String matchTotal=""; //NOI18N
		if(matchValue.contains("/")) { //NOI18N
			matchNumber = matchValue.split("/")[0]; //NOI18N
			matchTotal = matchValue.split("/")[1]; //NOI18N
		}

		String numberStr;
		String totalStr;
		int errorlevelFormat;
		if(!tagNumber.equals(matchNumber)) {
			errorlevelFormat=2;
			this.setKO(true);
		}
		else {
            tagNumber = tagNumber.equals("") ? "0" : tagNumber;
            if(Integer.valueOf(tagNumber)<=0) {
                errorlevelFormat=2;
                this.setKO();
            }
            else {
                errorlevelFormat=0;
            }
		}
		numberStr=colorField(tagNumber, errorlevelFormat, false);

		if(!tagTotal.equals(matchTotal)) {
			errorlevelFormat=2;
			this.setKO(true);
		}
		else {
            tagTotal = tagTotal.equals("") ? "0" : tagTotal;
            if(Integer.valueOf(tagTotal)<=0) {
                errorlevelFormat=2;
                this.setKO();
            }
            else {
                errorlevelFormat=0;
            }
		}
		totalStr=colorField(tagTotal, errorlevelFormat, false);
		return "<html>"+numberStr+"/"+totalStr+"</html>";  //NOI18N
	}

	private String analyseTrackString(String tagValue, String matchValue) {
		if(matchValue.equals(tagValue)) { 
            if(tagValue.equals("")) {  //NOI18N
                this.setKO();
                return colorField("{Empty}", 2);  //NOI18N
            }
            else {
                return colorField(tagValue, 0);
            }
		}
		else if(matchValue.trim().toLowerCase().equals(tagValue.trim().toLowerCase())) {
			this.setWarning(true);
			return colorField(tagValue, 1);
		}
		else {
			this.setKO(true);
			return colorField(tagValue, 2);
		}
	}
	
    //TODO: This one should not be here ...
	/**
	 * Color text HTML based on errorLevel
	 * @param text
	 * @param errorLevel
	 * @return
	 */
	public static String colorField(String text, int errorLevel) {
		return colorField(text, errorLevel, true);
	}
	
    //TODO: This one should not be here ...
	/**
	 * Color text HTML based on errorLevel
	 * if html param is false, do not enclose within <html> tags
	 * @param text
	 * @param errorLevel
	 * @param html
	 * @return
	 */
	public static String colorField(String text, int errorLevel, boolean html) {
		String color;
		
		if(text==null) {
			text="{null}";  //NOI18N
		}
		else if(text.equals("")) {  //NOI18N
			text="{Empty}";  //NOI18N
		}
		
		switch (errorLevel)
		{
			case 0: color="#32cd32"; break; //lime green	OK  //NOI18N
			case 1: color="#ffa500"; break; //orange		Warning  //NOI18N
			case 2: color="#FF0000"; break; //red			KO  //NOI18N
			case 3: color="#9400d3"; break; //violet		extra value  //NOI18N
			default: color="#ffff00"; break; //yellow		Default, shall not be used  //NOI18N
		}

		String out="";  //NOI18N
		if(html) {
			out+="<html>";  //NOI18N
		}
		out+="<font color=\""+color+"\">"+text+"</font>";  //NOI18N
		if(html) {
			out+="</html>";  //NOI18N
		}
		
		return out;
	}
	
	//TODO: This one should not be here ...
	/**
	 * Format a number
	 * @param value
	 * @return
	 */
	public static String formatNumber(int value) {
		String text;
		try{
			text= String.format("%02d", value);  //NOI18N
		}
		catch (Exception ex) {
			text=String.valueOf(value);
		}
		return text;
	}

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
		
		if (obj instanceof FolderInfoResult) {
			FolderInfoResult thatFolderInfo = (FolderInfoResult) obj;
			//Not comparing relativeFullPath as it can be in different format (Windows vs Unix)
			//Moreover it is not requiered to compare that value (should be the same)
			boolean isEqual = (this.value.equals(thatFolderInfo.value));
			isEqual &= (this.errorLevel == thatFolderInfo.errorLevel);
			return isEqual;
		}
		return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.value);
        hash = 47 * hash + this.errorLevel;
        return hash;
    }

    @Override
    public String toString() {
        return "{" + "value=" + value + ", errorLevel=" + errorLevel + '}';
    }
}
