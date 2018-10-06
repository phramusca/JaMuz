/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package test.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.musicbrainz.MBWS2Exception;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Creation {
    
    /**
	 * Main program.
	 * @param args
     * @throws org.musicbrainz.MBWS2Exception
     * @throws java.io.IOException
     * @throws org.jaudiotagger.audio.exceptions.CannotReadException
     * @throws org.jaudiotagger.tag.TagException
     * @throws org.jaudiotagger.audio.exceptions.ReadOnlyFileException
     * @throws org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
	 */
	public static void main(String[] args) throws MBWS2Exception, IOException, CannotReadException, TagException, 
            ReadOnlyFileException, InvalidAudioFrameException {
        
		//Create new ods (OpenDocument SpreadSheet) album test files
		//This is not up-to-date:
		//- Some versions (tabs) are missing
		//- All tabs will be the same
		//=> Current test files have been manually modified later on.
		//You can use CompareOO to analyse those
		
		
        List<String> mbIds = new ArrayList<>();
//        mbIds.add("9e097b10-8160-491e-a310-e26e54a86a10");
//        mbIds.add("9dc7fe6a-3fa4-4461-8975-ecb7218b39a3");
//        mbIds.add("c212b71b-848c-491c-8ae7-b62a993ae194");
//        mbIds.add("8cfbb741-bd63-449f-9e48-4d234264c8d5");
//        mbIds.add("be04bc1f-fc63-48f5-b1ca-2723f17d241d");
//        mbIds.add("6cc35892-c44f-4aa7-bfee-5f63eca70821");
//        mbIds.add("7598d527-bc8d-4282-a72c-874f335d05ac");
//        mbIds.add("13ca98f6-1a9f-4d76-a3b3-a72a16d91916");
        for(String mbId : mbIds) {
            Album.createFile(mbId);
        }
    }
}
