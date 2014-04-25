package com.groupsinc.wordnetd;

import java.net.*;
import java.io.*;

public final class ServerThread extends Thread {
    private Socket socket = null;
    private Wordnet wordnet;

    public ServerThread(Socket socket, Wordnet wordnet) {
        super("ServerThread");
        this.socket = socket;
        this.wordnet = wordnet;
    }
    
    public void run() {

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
            String inputLine, outputLine;
            Protocol p = new Protocol(this.wordnet);
            outputLine = p.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = p.processInput(inputLine);
                out.println(outputLine);
                if (outputLine!=null && outputLine.equals("Bye"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}