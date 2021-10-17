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
package jamuz.acoustid;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.model.entity.RecordingWs2;

/**
 *
 * @author raph
 */
class Recording {
	private final String id;

	Recording(String id) {
	   this.id = id;
	}

	public String getId() {
	   return id;
	}
	
	public AcoustIdResult getMeta() {
		try {
			org.musicbrainz.controller.Recording recording = new org.musicbrainz.controller.Recording();
			RecordingWs2 lookUp = recording.lookUp(id);
			System.out.println("BEST RESULT: \""+lookUp.getTitle()+"\" by "+lookUp.getArtistCreditString());
			return new AcoustIdResult(lookUp.getArtistCreditString(), lookUp.getTitle());
		} catch (MBWS2Exception ex) {
			Logger.getLogger(Recording.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
