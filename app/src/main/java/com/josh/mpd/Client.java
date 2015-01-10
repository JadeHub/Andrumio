package com.josh.mpd;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * Created by Josh on 5/01/2015.
 */
public class Client implements IClient, ClientSocket.Callback {

    private final int ARTIST_LIST_REQUEST = 1;

    private final String _host;
    private final int _port;
    private Callback _callback;

    private ClientSocket _socket;

    public Client(String host, int port)
    {
        _socket = new ClientSocket(this);
        _host = host;
        _port = port;
    }

    public void setCallback(Callback c)
    {
        _callback = c;
    }
    public void connect()
    {
        _socket.beginConnect(_host, _port);
    }
    public void disconnect()
    {
        _socket.disconnect();
    }
    public boolean isConnected()
    {
        return _socket.isConnected();
    }

    private boolean IsErrorResult(String line)
    {
        return line.startsWith("ACK");
    }

    private static String ParseTag(String tag, String line)
    {
        if(line.startsWith(tag + ": "))
        {
            return line.substring(tag.length() + 2);
        }
        return "";
    }

    @Override
    public List<ITrack> getTrackList(String album)
    {
        _socket.Send("find album \"" + album + "\"");

        ArrayList<ITrack> results = new ArrayList<>();

        while(true)
        {
            String line = _socket.Recv();
            if(IsErrorResult(line)) return null;
            if(line.startsWith("OK") || line.isEmpty()) break;
            String tag = ParseTag("file", line);
            if(!tag.isEmpty())
            {
                results.add(new Track(tag));
            }
        }
        return results;
    }

    @Override
    public List<IArtist> getArtistList()
    {
        _socket.Send("list artist");

        ArrayList<IArtist> results = new ArrayList<>();

        while(true)
        {
            String line = _socket.Recv();
            if(IsErrorResult(line)) return null;
            if(line.startsWith("OK") || line.isEmpty()) break;
            String tag = ParseTag("Artist", line);
            if(!tag.isEmpty())
            {
                results.add(new Artist(tag));
            }
        }
        return results;
    }

    @Override
    public List<IAlbum> getAlbumList(String artist)
    {
        _socket.Send("list album artist \"" + artist + "\"");

        ArrayList<IAlbum> results = new ArrayList<>();
        while(true)
        {
            String line = _socket.Recv();

            if(IsErrorResult(line))
            {
                return null;
            }

            if(line.startsWith("OK") || line.isEmpty())
            {
                break;
            }
            String tag = ParseTag("Album", line);
            if(!tag.isEmpty())
            {
                if(tag.compareTo("Ten") == 0)
                {
                    int i = 7;
                    Log.i("", "");
                }

                results.add(new Album(tag));
            }
        }
        return results;
    }

    /*********************************************************
     ClientSocket.Callback
    *********************************************************/

    @Override
    public void onConnected()
    {
        Log.i("Socket", "Connected");
        _callback.onConnected();
    }

    @Override
    public void onConnectFailed(String msg)
    {
        Log.e("Socket", "Connect failed: " + msg);
        _callback.onConnectionError(msg);
    }

    @Override
    public void onDisconnected()
    {
        Log.i("Socket", "Disconnected");
        _callback.onDisconnected();
    }

    @Override
    public void onReadError(String msg)
    {
        Log.i("Socket", "Read error: " + msg);
        _callback.onConnectionError(msg);
        _socket.disconnect();
    }
}
