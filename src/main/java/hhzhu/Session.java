package hhzhu;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class Session {
    public int id;
    public String userName;

    public Session()
    {
    }

    public Session(int id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    List<JSONObject> geometryJsonList = new ArrayList<>();
    List<JSONObject> geometryJsonListHistory = new ArrayList<>();

    public void put(JSONObject geometryJson)
    {
        synchronized (this)
        {
            geometryJsonList.add(geometryJson);
            geometryJsonListHistory.add(geometryJson);
        }
    }

    public List retrieve()
    {
        synchronized(this)
        {
            List<JSONObject> result = geometryJsonList;
            geometryJsonList = new ArrayList<>();
            return result;
        }
    }

    //get all history
    public List retrieveHistory()
    {
        synchronized(this)
        {
            List<JSONObject> result = geometryJsonListHistory;
            return result;
        }
    }
}
