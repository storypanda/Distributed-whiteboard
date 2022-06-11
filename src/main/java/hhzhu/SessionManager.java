package hhzhu;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class SessionManager {
    public static int SESSION_ID = 1;
    public static SessionManager i = new SessionManager();
    List<Session> sessionList = new LinkedList<>();

    private SessionManager()
    {
    }

    //create a session
    public Session login(String name)
    {
        Session ss = new Session();
        ss.userName = name;
        ss.id = SESSION_ID ++;

        synchronized (sessionList)
        {
            sessionList.add(ss);
        }

        return ss;
    }

    //remove a session
    public void logout(int sessionId)
    {
        synchronized (sessionList)
        {
            Iterator<Session> iter = sessionList.iterator();
            while(iter.hasNext())
            {
                Session ss = iter.next();
                if(ss.id == sessionId)
                {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public void sendAll(JSONObject geometryJson)
    {
        synchronized (sessionList)
        {
            Iterator<Session> iter = sessionList.iterator();
            while(iter.hasNext())
            {
                Session ss = iter.next();
                ss.put(geometryJson);
            }
        }
    }

    //get all session
    public List<Session> getAll()
    {
        synchronized (sessionList)
        {
            return sessionList;
        }
    }
}
