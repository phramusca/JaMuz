/*
 * Copyright (C) 2020 raph
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
package test.tools;

import jamuz.Keys;
import jamuz.acoustid.AcoustID;
import jamuz.acoustid.ChromaPrint;
import jamuz.utils.Popup;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Recording;
import org.musicbrainz.model.entity.RecordingWs2;

/**
 *
 * @author raph
 */
public class TestAcoustId {
	/**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			
			Keys keys = new Keys("/jamuz/keys.properties");
			if(!keys.read()) {
				Popup.error("Missing keys.properties file from jar package.");
				return;
			}
			
//			String filename = "/home/raph/Bureau/TEST_SHAZAM/[1-25] - 08 Léo Ferré - Cette blessure.mp3";
//			String filename = "/home/raph/Bureau/TEST_SHAZAM/01 Bad.mp3";
//			String filename = "/home/raph/Bureau/TEST_SHAZAM/03 I Shot the Sheriff.mp3";
			String filename = "/home/raph/Bureau/TEST_SHAZAM/WhatIsThis.mp3";
			
			
			ChromaPrint chromaprint = AcoustID.chromaprint(new File(filename), "fpcalc");		
			String bestResultRecordingId = AcoustID.lookup(chromaprint, keys.get("AcoustId"));
					
			Recording recording = new Recording();
			RecordingWs2 lookUp = recording.lookUp(bestResultRecordingId);
			
			System.out.println("BEST RESULT: \""+lookUp.getTitle()+"\" by "+lookUp.getArtistCreditString());
			
		} catch (IOException ex) {
			Logger.getLogger(TestAcoustId.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MBWS2Exception ex) {
			Logger.getLogger(TestAcoustId.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
