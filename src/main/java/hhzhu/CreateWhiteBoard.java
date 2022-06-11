package hhzhu;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class CreateWhiteBoard {
    public static void main(String[] args) throws Exception {
        //args 0:serverIPAddress 1:serverPort
        try {
            Integer.parseInt(args[1]);
        }catch (Exception e){
            System.out.println("Port number should be in range:(0 - 65353)!");
            return;
        }
        int port = Integer.parseInt(args[1]);
        MyServer server = new MyServer(port);
        server.startService();
    }
}
