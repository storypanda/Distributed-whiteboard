package hhzhu;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;


/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class ServerConnection {
    public String charset = "UTF-8";
    public Socket socket;

    private InputStream inputStream ;
    private OutputStream outputStream ;

    public ServerConnection()
    {
    }

    public ServerConnection(Socket socket) throws Exception
    {
        this.socket = socket;
        inputStream= socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    //connect to server
    public void connect(String ip, int port, int timeout) throws Exception
    {
        socket = new Socket();

        socket.connect( new InetSocketAddress(ip,port), timeout);
        inputStream= socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public void close()
    {
        try {
            socket.close();
            socket = null;
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setSoTimeout(int timeout)
    {
        try {
            socket.setSoTimeout(timeout);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    //Keep running until the N bytes are all received
    public int readFully(byte[] data, int off, int N) throws Exception
    {
        int count = 0; //count is the number of bytes that have been received
        while(count < N)
        {
            int remaining = N  - count;
            int numBytes = inputStream.read(data, off + count, remaining);
            if (numBytes < 0) {
                return -1;
            }
            count = count + numBytes;
        }
        return N;
    }

    //Send!!!
    public void send(String msg) throws Exception
    {
        ExchangeProtocol packet = new ExchangeProtocol();
        if(msg == null){
            packet.length = 0;
        }
        else{
            packet.setContent(msg, this.charset);
        }
        send (packet);
    }

    public void send(ExchangeProtocol pkt) throws Exception
    {
        //send head first
        ByteBuffer headBuf = ByteBuffer.allocate(4);
        headBuf.putInt(pkt.length);
        outputStream.write(headBuf.array(), 0, headBuf.position());

        //then send content
        if(pkt.length > 0)
        {
            outputStream.write(pkt.content, 0, pkt.length);
        }
    }

    //Receive!!
    public ExchangeProtocol receive() throws Exception
    {
        ByteBuffer headBuf = ByteBuffer.allocate(4);
        int rc = readFully(headBuf.array(), 0, 4);
        if(rc != 4){
            return null;
        }
        ExchangeProtocol pkt = new ExchangeProtocol();
        pkt.length = headBuf.getInt();

        if(pkt.length > 0)
        {
            pkt.content = new byte[pkt.length];
            rc = readFully(pkt.content, 0, pkt.length);
            if(rc != pkt.length)
                return null;
        }
        return pkt;
    }
}
