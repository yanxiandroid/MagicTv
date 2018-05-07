package com.yht.iptv.push;

import android.util.Log;

import org.apache.mina.core.session.IoSession;

/**
 * Created by admin on 2017/7/4.
 */

public class SessionManager {

    private static SessionManager instance;
    private IoSession session;

    private SessionManager() {

    }

    public static SessionManager getInstance(){
        if(instance == null){
            synchronized (SessionManager.class){
                if(instance == null){
                    instance = new SessionManager();
                }
            }
        }

        return instance;
    }


    public void setSession(IoSession session){
        this.session = session;
    }

    public IoSession getSession(){
        return session;
    }

    public void WriteToSession(Object object){
        if(session != null){
            session.write(object);
        }
        else{
            Log.e("mina","session为空");
        }
    }

    public void colseSession(){
        if(session != null){
            session.closeOnFlush();
        }
    }

    public void removeSession(){
        session = null;
    }

}
