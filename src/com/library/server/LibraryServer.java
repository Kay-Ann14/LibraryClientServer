
package com.library.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LibraryServer {
    public static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("LibraryServer listening on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                Thread t = new Thread(new ClientHandler(client));
                t.start(); // Runnable for multithreading
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

