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
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Josh on 5/01/2015.
 */
public class ClientSocket {

    public interface Callback
    {
        void onConnected();
        void onConnectFailed(String msg);
        void onDisconnected();

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
        _socket = new Socket();
    }

    public void beginConnect(String host, int port)
    {
        SocketConnectAction action = new SocketConnectAction(host, port);
        action.execute();
    }

    public boolean blockingConnect(String host, int port)
    {
        _socketReader = null;
        _socketWriter = null;

        try
        {
            _socketReader = null;
            _socketWriter = null;

            InetAddress address = InetAddress.getByName(host);
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
            if(_socket.isConnected())
                _socket.close();
        }
        catch(Exception e)
        {
            Log.e("", e.getMessage());
        }
    }

    public boolean isConnected()
    {
        return _socket.isConnected();
    }

    public String Recv()
    {
        try {
            return _socketReader.readLine();
        }
        catch(IOException e)
        {
            disconnect();
            _callback.onReadError(e.getMessage());
        }
        return "";
    }

    public void Send(String s)
    {
        _socketWriter.println(s);
    }

    /**************************************/
    /* Connect action
    /**************************************/
    public class SocketConnectAction extends AsyncTask<Void, Void, Boolean>
    {
        private String _errorMsg;
        private final String _host;
        private final int _port;

        public SocketConnectAction(String host, int port)
        {
            _host = host;
            _port = port;
        }

        @Override
        public Boolean doInBackground(Void...v)
        {
            try
            {
                _socketReader = null;
                _socketWriter = null;

                InetAddress address = InetAddress.getByName(_host);
                _socket.connect(new InetSocketAddress(address, _port));
                _socketReader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                _socketWriter = new PrintWriter(_socket.getOutputStream(), true);
                String ver = Recv();

                if(ver.startsWith("OK MPD"))
                    return true;
                _errorMsg = "Not an MPD server.";
            }
            catch(UnknownHostException e)
            {
                _errorMsg = e.getMessage();
            }
            catch (IOException e)
            {
                _errorMsg = e.getMessage();
            }
            return false;
        }

        @Override
        public void onPostExecute(Boolean result)
        {
            if(result)
                _callback.onConnected();
            else
                _callback.onConnectFailed(_errorMsg);
        }
    }
}
