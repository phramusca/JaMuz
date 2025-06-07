/*
 * Copyright (C) 2020 phramusca <phramusca@gmail.com>
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
import jamuz.acoustid.Results;
import jamuz.utils.Popup;
import java.util.ArrayList;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class TestAcoustId {

	/**
	 * Main program.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		Keys keys = new Keys("/jamuz/keys.properties");
		if (!keys.read()) {
			Popup.error("Missing keys.properties file from jar package.");
			return;
		}

		ArrayList<String> filenames = new ArrayList<>();
		filenames.add("/home/raph/Musique/Archive/Various Artists/Various Albums/05 Jive Bunny & The Mastermixer - Swing The Mood.mp3");
		filenames.add("/home/raph/Musique/Archive/Various Artists/Various Albums/(Jive Bunny and the Master Mix - Let's twist again.mp3");

//		filenames.add("/home/raph/Bureau/TEST_SHAZAM/[1-25] - 08 Léo Ferré - Cette blessure.mp3");
//		filenames.add("/home/raph/Bureau/TEST_SHAZAM/01 Bad.mp3");
//		filenames.add("/home/raph/Bureau/TEST_SHAZAM/03 I Shot the Sheriff.mp3");
//		filenames.add("/home/raph/Bureau/TEST_SHAZAM/WhatIsThis.mp3");
		ArrayList<Results> results = new ArrayList<>();
		for (String filename : filenames) {
			results.add(AcoustID.analyze(filename, keys.get("AcoustId")));
		}

		Popup.info("AcoustId analysis complete");
	}
}
