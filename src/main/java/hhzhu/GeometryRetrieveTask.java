package hhzhu;

import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class GeometryRetrieveTask extends Thread{
    LongConnection longConnection;
    WhiteBoard whiteBoard;
    int count = 0;

    public GeometryRetrieveTask( WhiteBoard whiteBoard, LongConnection longConnection)
    {
        this.whiteBoard = whiteBoard;
        this.longConnection = longConnection;
    }

    public void run()
    {
        while(true)
        {
            //query every second
            try { sleep(1000); } catch(Exception e) {}

            try
            {
                if(count == 0){
                    queryHistory();
                    count = 1;
                }
                query();
                queryCollaborator();
            } catch (Exception e)
            {
                e.printStackTrace();
                break;
            }
        }
    }

    private void query() throws Exception
    {
        JSONObject jreq = new JSONObject();
        jreq.put("cmd", "query");

        JSONObject jresp = longConnection.send(jreq);
        int status = jresp.getInt("status");
        String reason = jresp.optString("reason","");

        if(status != 0)
        {
            throw new Exception(reason);
        }
        else
        {
            JSONArray jdata = jresp.getJSONArray("data");
            for(int i=0; i<jdata.length(); i++)
            {
                JSONObject msg = jdata.getJSONObject(i);
                showMessage(msg );
            }
        }
    }

    //query history
    private void queryHistory() throws Exception
    {
        JSONObject jreq = new JSONObject();
        jreq.put("cmd", "queryHistory");

        JSONObject jresp = longConnection.send(jreq);
        int status = jresp.getInt("status");
        String reason = jresp.optString("reason","");

        if(status != 0)
        {
            throw new Exception(reason);
        }
        else
        {
            JSONArray jdata = jresp.getJSONArray("data");
            for(int i=0; i<jdata.length(); i++)
            {
                JSONObject msg = jdata.getJSONObject(i);
                showMessage(msg );
            }
        }
    }

    //query collaborator
    private void queryCollaborator() throws Exception
    {
        JSONObject jreq = new JSONObject();
        String collaboratorText = null;
        jreq.put("cmd", "queryCollaborator");

        JSONObject jresp = longConnection.send(jreq);
        int status = jresp.getInt("status");
        String reason = jresp.optString("reason","");

        if(status != 0)
        {
            throw new Exception(reason);
        }
        else
        {
            JSONArray jdata = jresp.getJSONArray("data");
            for(int i=0; i<jdata.length(); i++)
            {
                JSONObject collaborator = jdata.getJSONObject(i);
                String userName = collaborator.getString("userName");
                if (collaboratorText == null) {
                    collaboratorText = userName;
                }else {
                    collaboratorText = collaboratorText + " " + userName;
                }
            }
        }
        showCollaborator(collaboratorText);
    }

    private void showMessage(JSONObject msg )
    {
        Platform.runLater( ()->{
            whiteBoard.drawGeometry(msg);;
        });
    }

    private void showCollaborator(String collaborator )
    {
        Platform.runLater( ()->{
            whiteBoard.showCollaborator(collaborator);;
        });
    }
}
