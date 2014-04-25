package com.groupsinc.wordnetd;

import java.net.*;
import java.util.List;
import java.io.*;
import java.lang.*;


public final class Protocol {
    
    private Wordnet wordnet;
    
    public Protocol(Wordnet wordnet) {
    	this.wordnet = wordnet;
    }

    public String processInput(String theInput) {
        String theOutput = new String();
        
        if(theInput==null) {
        	theOutput = "Unknown command";
        	return theOutput;
        }
        
        theInput = theInput.toLowerCase();
        String type = "";
        
        if(theInput.startsWith("synonym ")) {
        	theInput = theInput.replaceFirst("synonym ", "");
        	if(theInput.startsWith("-noun- ")) {
        		type = "noun";
        		theInput = theInput.replaceFirst("-noun- ", "");
        	}
        	else if(theInput.startsWith("-verb- ")) {
        		type = "verb";
        		theInput = theInput.replaceFirst("-verb- ", "");
        	}
        	for(String synonym: this.wordnet.getSynonyms(theInput, type)) {
        		theOutput += synonym + "\n";
        	}
        }
        else if(theInput.startsWith("hypernym ")) {
        	theInput = theInput.replaceFirst("hypernym ", "");
        	if(theInput.startsWith("-noun- ")) {
        		type = "noun";
        		theInput = theInput.replaceFirst("-noun- ", "");
        	}
        	else if(theInput.startsWith("-verb- ")) {
        		type = "verb";
        		theInput = theInput.replaceFirst("-verb- ", "");
        	}
        	for(String hypernym: this.wordnet.getHypernyms(theInput, type)) {
        		theOutput += hypernym + "\n";
        	}
        }
        else if(theInput.startsWith("polysemy ")) {
        	theInput = theInput.replaceFirst("polysemy ", "");
			for(String polysemy: this.wordnet.getPolysemy(theInput)) {
				theOutput += polysemy + "\n";
			}
        }
        else if(theInput.startsWith("top_polysemy ")) {
        	theInput = theInput.replaceFirst("top_polysemy ", "");
			theOutput = this.wordnet.getTopPolysemy(theInput);
        }
        else if(theInput.equals("bye")) {
        	theOutput = "Bye";
        }
        else {
        	theOutput = "Unknown command";
        }
        
        return theOutput;
    }
}