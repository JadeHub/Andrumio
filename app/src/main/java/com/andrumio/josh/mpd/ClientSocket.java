package com.andrumio.josh.mpd;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Josh on 5/01/2015.
 */
public class ClientSocket {

    public interface Callback
    {
        void onReadError(String msg);
    }

    private Socket _socket;
    private String _message;
    private Callback _callback;
    private BufferedReader _socketReader;
    PrintWriter _socketWriter;


    public ClientSocket(Callback callback)
    {
        _callback = callback;
    }

    /*
    public void beginConnect(String host, int port)
    {
        SocketConnectAction action = new SocketConnectAction(host, port);
        action.execute();
    }*/

    public boolean blockingConnect(String host, int port)
    {
        _socketReader = null;
        _socketWriter = null;

        try
        {
            _socketReader = null;
            _socketWriter = null;

            InetAddress address = InetAddress.getByName(host);
            _socket = new Socket();
            _socket.connect(new InetSocketAddress(address, port));
            _socketReader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            _socketWriter = new PrintWriter(_socket.getOutputStream(), true);
            String ver = Recv();

            if(ver.startsWith("OK MPD"))
                return true;

        }
        catch(UnknownHostException e)
        {
            //_errorMsg = e.getMessage();
        }
        catch (IOException e)
        {
           // _errorMsg = e.getMessage();
        }
        return false;
    }

    public void disconnect()
    {
        try {
            if(isConnected())
                _socket.close();

            Boolean b = isConnected();
            Log.i("SOCKET", b.toString());
        }
        catch(Exception e)
        {
            Log.e("", e.getMessage());
        }
    }

    public boolean isConnected()
    {
        return _socket != null && _socket.isConnected() && !_socket.isClosed();
    }

    public String Recv()
    {
        if(!isConnected()) return "";

        try {
            return _socketReader.readLine();
        }
        catch(IOException e)
        {
            _callback.onReadError(e.getMessage());
            disconnect();
        }
        return "";
    }

    public void Send(String s)
    {
        _socketWriter.println(s);
    }
}
