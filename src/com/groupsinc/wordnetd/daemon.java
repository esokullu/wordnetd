package com.groupsinc.wordnetd;

import org.apache.commons.cli.*;
import java.util.*;

public class daemon {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("d", true, "dict file location");
		options.addOption("p", true, "server port");
		
		CommandLineParser parser = new BasicParser();
		int port = 4444;
		String dictfile = "dict";

		try {
			
			CommandLine cmd = parser.parse( options, args);
			
			
			if(cmd.hasOption("d")) {
				dictfile = cmd.getOptionValue("d");
			}
			else {
				System.exit(2);
			}
			
			if(cmd.hasOption("p")) {
				port = Integer.parseInt(cmd.getOptionValue("p"));
			}
			else {
				System.exit(3);
			}
			
			new Server(port, dictfile);
			
		}
		catch(ParseException e) {
			System.err.println("Parse Error: "+e.getMessage());
		}
		
		
		
		
	}

}
