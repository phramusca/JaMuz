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
package tests.functional;

import jamuz.utils.FileSystem;
import jamuz.utils.XML;
import java.io.File;
import java.util.ArrayList;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Release;
import org.musicbrainz.model.TagWs2;
import org.musicbrainz.model.entity.ReleaseWs2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		
		
		
		
		
		
//		File file = FileSystem.replaceHome("~/.guayadeque/guayadeque.conf");
//		if(!file.exists()) {
//			System.exit(0);
//		}
//		Document doc = XML.open(file.getAbsolutePath());
//		if(doc==null) {
//			System.exit(0);
//		}
//		ArrayList<Element> elements = XML.getElements(doc, "collection");
//		for(Element element : elements) {
//			if(XML.getAttribute(XML.getElement(element, "Type"), "value").equals("0")) {
//				System.out.println("UniqueId:"+XML.getAttribute(XML.getElement(element, "UniqueId"), "value"));
//				System.out.println("Name:"+XML.getAttribute(XML.getElement(element, "Name"), "value"));
//				System.out.println("Path0:"+XML.getAttribute(XML.getElement(element, "Path0"), "value"));
//			}
//		}
	

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
}
