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
package jamuz.tests;

import jamuz.process.check.DialogScanner;
import jamuz.process.check.FileInfoDisplay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Release;
import org.musicbrainz.model.TagWs2;
import org.musicbrainz.model.entity.ReleaseWs2;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ManualTesting {

    /**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {

		
		
		
		
//        DialogScanner.main(null, 
//                "/home/raph/Musique/Nouveau/0_pascal/Reggae (JA)/Johnny Clarke = [1976] Authorised Version ~MP3~/[06] Academy Award Version.mp3");
        
        //TODO: Try those to play music on different sound card and more
        //http://www.onjava.com/pub/a/onjava/2004/08/11/javasound-mp3.html?page=2
        //https://www.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
        //http://www.developerfusion.com/article/84314/wired-for-sound/
        //http://www.jsresources.org/faq_audio.html
        
        
            try {
                //http://stackoverflow.com/questions/22438353/java-select-audio-device-and-play-mp3
//Play on a particular sound card (use "aplay -L" for list of sound cards)
				
			// USING mpg123
                
				//Use -C option to control the player
//                java.lang.Process p1 = Runtime.getRuntime().exec(new String[]{"mpg123", "-asysdefault:CARD=Device", "/home/raph/Musique/Archive/2Pac/Greatest Hits/[1-2] - 01 Keep Ya Head Up.mp3"});
                //Play on the default sound card
//                java.lang.Process p2 = Runtime.getRuntime().exec(new String[]{"mpg123", "/home/raph/Musique/Archive/2Pac/Greatest Hits/[1-2] - 03 Temptations.mp3"});
                
			//USING mplayer
				//mplayer -ao alsa:device=sysdefault=Device
//				java.lang.Process p1 = Runtime.getRuntime().exec(new String[]{"mplayer", "-ao", "alsa:device=sysdefault=Device", "/home/raph/Musique/Archive/2Pac/Greatest Hits/[1-2] - 01 Keep Ya Head Up.mp3"});
				//mplayer FLAC -ao alsa:device=sysdefault=Device
				java.lang.Process p1 = Runtime.getRuntime().exec(new String[]{"mplayer", "-ao", "alsa:device=sysdefault=Device", "/home/raph/Musique/Archive/Various Artists/3_60_ Reggae Greats/[1-3] - 10 The Heptones - My Guiding Star.flac"});                
				//Play on the default sound card
                java.lang.Process p2 = Runtime.getRuntime().exec(new String[]{"mplayer", "/home/raph/Musique/Archive/2Pac/Greatest Hits/[1-2] - 03 Temptations.mp3"});
				
				p1.waitFor(30, TimeUnit.SECONDS);
 
            } catch (Exception e) {
                e.printStackTrace();
            }
            //play wav
//                try {
//                java.lang.Process p3 = Runtime.getRuntime().exec(new String[]{"aplay", "audio/audio1.wav", "-Dsysdefault:CARD=Intel"});
//                java.lang.Process p4 = Runtime.getRuntime().exec(new String[]{"aplay", "audio/audio2.wav", "-Dsysdefault:CARD=CA0106"});
//             } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
//        }
        
        
//        try {
//           
//        //https://code.google.com/p/musicbrainzws2-java/wiki/Usage_SubmittingData#Submitting_data    
//        Release controller = new Release();
//
//        //TODO: Move to options when/if integrated into JaMuz
//        controller.getQueryWs().setUsername("phramusca");
//        controller.getQueryWs().setPassword(""); 
//         
//        //TODO: What the fuck must I set in here ???
//        controller.getQueryWs().setClient("JaMuz/0.0.22"); 
//        //            Invalid or not allowed EntityType release
//         
//        controller.getIncludes().setUserRatings(true);
//        controller.getIncludes().setUserTags(true);
//         
////                 83d91898-7763-47d7-b03b-b92132375c47
//        ReleaseWs2 release= controller.lookUp("18ebeb44-7308-4fa1-a5eb-2363366844ef");
//        for (TagWs2 tag : release.getUserTags())
//        {
//            System.out.println(tag.getName());
//        }
//        System.out.println(release.getUserRating().getAverageRating());
//
//        release.getUserRating().setAverageRating(1F);
//        release.getUserTags().clear();
//        release.getUserTags().add(new TagWs2("rock"));
//        release.getUserTags().add(new TagWs2("progressive"));
//
//        controller.postUserRatings(); // TODO: Invalid EntityType :(
//        controller.postUserTags();
//
//        controller.lookUp(release);
//        for (TagWs2 tag : release.getUserTags())
//        {
//            System.out.println(tag.getName());
//        }  
//        System.out.println(release.getUserRating().getAverageRating());
//
//        } catch (MBWS2Exception ex) {
//            Logger.getLogger(ManualTesting.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
        
        
    }
//}
