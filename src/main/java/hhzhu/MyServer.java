package hhzhu;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class MyServer {
    ServerSocket serverSocket;
    int port;

    public MyServer(int port)
    {
        this.port = port;
    }

    public void startService() throws Exception
    {
        serverSocket = new ServerSocket(port);
        System.out.println("The server starts, waiting for connection...");

        while(true)
        {
            //Listen for requests
            Socket sock = serverSocket.accept();

            //Create a thread and process the request in the thread
            System.out.println("New connection...");

            ServerConnection connection = new ServerConnection(sock);
            ServiceHandler handler = new ServiceHandler(connection);
            handler.start();
        }
    }
}
