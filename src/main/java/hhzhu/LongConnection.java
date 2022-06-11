package hhzhu;

import javafx.application.Platform;
import org.json.JSONObject;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class LongConnection {
    private String server;
    private int port;
    ServerConnection connection = null;

    public LongConnection(String server ,int port)
    {
        this.server = server;
        this.port = port;
    }

    public void send(JSONObject request, Callback callback)
    {
        SendTask task = new SendTask();
        task.request = request;
        task.callback = callback;
        task.start();
    }

    //Synchronized
    public synchronized JSONObject send(JSONObject request) throws Exception
    {
        //It will connect to the server on the first call
        if(connection == null)
        {
            connection = new ServerConnection();
            connection.connect(server, port,3000);
        }
        connection.send(request.toString(2));

        //Receive the response
        ExchangeProtocol response = connection.receive();
        return new JSONObject(response.toString("UTF-8"));
    }

    //callback in gui
    public interface Callback
    {
        public void onResult(int status, String reason, Object data);
    }


    ///////////////////////////////////////
    private class SendTask extends Thread
    {
        JSONObject request;
        Callback callback;

        public void run()
        {
            int status = -1;
            String reason = null;
            Object data = null;

            try {
                JSONObject jresp = send(request);
                status = jresp.getInt("status");
                reason = jresp.optString("reason","");
                data = jresp.opt("data");

            }catch(Exception e)
            {
                connection.close();
                connection = null;

                status = -1;
                reason = e.getMessage();
                if(reason == null) reason = e.getClass().getName();
            }

            // Long connection: connect once, use multiple times ( will not close the connection immediately )
            // conn.close();

            processResult(status, reason, data);
            System.out.println("Thread " +  "SendTask" + "exiting.");
        }

        private void processResult(int status, String reason, Object data)
        {
            if(callback != null)
            {
                Platform.runLater( ()->{
                    callback.onResult(status, reason, data);
                });
            }
        }
    }
}
