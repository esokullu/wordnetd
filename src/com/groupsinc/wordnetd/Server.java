package com.groupsinc.wordnetd;

import java.net.*;
import java.io.*;

public final class Server {
    public Server(int portNumber, String dictLocation) {

        boolean listening = true;
        
        try {
        	ServerSocket serverSocket = new ServerSocket(portNumber); 
        	Wordnet wordnet = new Wordnet(dictLocation);
        
            while (listening) {
	            new ServerThread(serverSocket.accept(), wordnet).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}