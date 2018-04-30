package com.patrick;

import java.net.*;
import java.io.*;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        int port = 25;
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            while (listening)
            {
                new SmtpContext(serverSocket.accept()).start();
            }
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
    }
}