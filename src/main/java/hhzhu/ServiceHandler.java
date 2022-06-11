package hhzhu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class ServiceHandler extends Thread{
    ServerConnection conn;
    Session session;

    public ServiceHandler(ServerConnection conn)
    {
        this.conn = conn;
    }

    @Override
    public void run()
    {
        //Maximum idle time is 1 minute
        conn.setSoTimeout(1000 * 30);

        while(true)
        {
            try {
                ExchangeProtocol requestPkt = conn.receive();
                String request = requestPkt.toString("UTF-8");

                //According to different requests, make corresponding responses
                String response = handleRequest( request );
                conn.send(response);
            }
            catch(SocketException e)
            {
                System.out.println("Connection closed!" + e.getMessage());
                break;
            }
            catch(SocketTimeoutException e)
            {
                System.out.println("The connection is idle!");
                break;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                break;
            }
        }

        conn.close();

        if(session!=null)
        {
            SessionManager.i.logout(session.id);
            System.out.println("User " + session.userName + " offline\n");
        }

        System.out.println("** connection closed ! \n");
    }

    private String handleRequest(String request)
    {
        JSONObject requestJson = new JSONObject(request);
        String cmd = requestJson.getString("cmd").trim();
        System.out.println("GOT: " + cmd);


        JSONObject responseJson = new JSONObject();
        if(cmd.equals("login"))
        {
            String name = requestJson.getString("name");
            this.session = SessionManager.i.login(name);

            JSONObject jdata = new JSONObject();
            jdata.put("session", session.id);

            responseJson.put("status", 0);
            responseJson.put("data", jdata);
        }
        else if(cmd.equals("sendAll"))
        {
            Double startPointX = requestJson.optDouble("startPointX",0.0);
            Double startPointY = requestJson.optDouble("startPointY", 0.0);
            Double endPointX = requestJson.optDouble("endPointX", 0.0);
            Double endPointY = requestJson.optDouble("endPointY", 0.0);
            Double lineWidth = requestJson.optDouble("lineWidth", 0.0);
            String color = requestJson.optString("color", "0x000000ff");
            String text = requestJson.optString("text", "");
            String type = requestJson.optString("type", "");

            JSONObject msg = new JSONObject();
            msg.put("session" , session.id);
            msg.put("name", session.userName);
            msg.put("startPointX", startPointX);
            msg.put("startPointY", startPointY);
            msg.put("endPointX", endPointX);
            msg.put("endPointY", endPointY);
            msg.put("lineWidth", lineWidth);
            msg.put("color", color);
            msg.put("text", text);
            msg.put("type", type);

            SessionManager.i.sendAll(msg);

            responseJson.put("status", 0);
        }
        else if(cmd.equals("query"))
        {
            List<JSONObject> msgList = session.retrieve();
            JSONArray jdata = new JSONArray();
            for(JSONObject msg : msgList)
            {
                jdata.put( msg );
            }

            responseJson.put("status", 0);
            responseJson.put("data", jdata);
        }
        else if(cmd.equals("queryHistory"))
        {
            if(SessionManager.i.getAll().size()>=1){
                Session oldSession = SessionManager.i.getAll().get(0);
                List<JSONObject> msgList = oldSession.retrieveHistory();
                JSONArray jdata = new JSONArray();
                for(JSONObject msg : msgList)
                {
                    jdata.put( msg );
                }

                responseJson.put("status", 0);
                responseJson.put("data", jdata);
            }
        }
        else if(cmd.equals("queryCollaborator"))
        {
            int sessionSize = SessionManager.i.getAll().size();
            JSONArray jdata = new JSONArray();
            for(int i=0; i<sessionSize; i++)
            {
                Session s = SessionManager.i.getAll().get(i);
                JSONObject collaborator = new JSONObject();
                collaborator.put("sessionId", s.id);
                collaborator.put("userName", s.userName);
                jdata.put(collaborator);
            }
            responseJson.put("status", 0);
            responseJson.put("data", jdata);
        }
        else
        {
            responseJson.put("status", -1);
            responseJson.put("reason", "unsupported command type: " + cmd);
        }

        return responseJson.toString(2);
    }
}
