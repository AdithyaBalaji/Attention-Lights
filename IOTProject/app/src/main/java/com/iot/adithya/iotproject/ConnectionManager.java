package com.iot.adithya.iotproject;


import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Adithya on 18-Jun-16.
 */
public class ConnectionManager {
   private Socket sock=null;

    public ConnectionManager(String ip,int port) throws Exception{
        sock=new Socket(ip,port);
    }

    public void sendString(String data) throws Exception{
        OutputStream os = sock.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.write(data);
        osw.flush();
        osw.close();
    }

    public boolean isConnected(){
        if(sock.isConnected()){
            return true;
        }else
            return false;
    }
    public void closeConnection() throws Exception{
        sock.close();
    }



}
