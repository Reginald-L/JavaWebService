package com.reggie.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.tree.FixedHeightLayoutCache;

/**
 * @ClassName WebServer
 * @Description WebServer
 * @Author Reggie
 * @Date 08/12/2020 16:49
 * @Version 1.0
 */

public class WebServer {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    public WebServer() {
        try {
            serverSocket = new ServerSocket(8080);
            threadPool = Executors.newFixedThreadPool(200);
        } catch (IOException e) {
            System.err.println("------------  There is an error when construct a ServerSocket  ------------");
            e.printStackTrace();
        }
    }

    private void start() {
        System.out.println("WebServer starts doing the start() ................");
        try {
            while(true) {
            	System.out.println("waiting a client to connect to the server ................");
                Socket socket = serverSocket.accept();
                System.out.println("There is a client has already connected to the server!!!");

//                saveRequest(socket);
                
                ClientHandler handler = new ClientHandler(socket);
                threadPool.execute(handler);
//                Thread t = new Thread(handler);
//                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveRequest(Socket socket) throws IOException {
        System.out.println("------------  Starting saving requests  ------------");
        InputStream inputStream = socket.getInputStream();
        int d = -1;
        FileOutputStream outputStream = new FileOutputStream("http-request.txt");
        while ((d = inputStream.read()) != -1) {
            outputStream.write((char) d);
        }
        System.out.println("------------  Saving requests successfully  ------------");
    }

    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}
