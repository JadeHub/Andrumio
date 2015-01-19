package com.andrumio.josh.mpd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Josh on 5/01/2015.
 */
public class Client implements IClient, ClientSocket.Callback {

    private final String _name;
    private final String _host;
    private final int _port;
    private Callback _callback;

    private ClientSocket _socket;

    public Client(String name, String host, int port)
    {
        _name = name;
        _socket = new ClientSocket(this);
        _host = host;
        _port = port;
    }

    @Override
    public void connect(Callback c) {
        _callback = c;
        ConnectAction action = new ConnectAction(_host, _port);
        action.execute();
    }

    @Override
    public String getName(){
        return _name;
    }

    @Override
    public String getHostName(){
        return _host;
    }

    @Override
    public int getPort(){
        return _port;
    }

    @Override
    public void disconnect()
    {
        _socket.disconnect();
    }

    @Override
    public boolean isConnected()
    {
        return _socket.isConnected();
    }


    private static boolean IsErrorResult(String line)
    {
        return line.startsWith("ACK") || line.length() == 0;
    }

    private static String parseTag(String line, String tag)
    {
        if(line.startsWith(tag + ": "))
        {
            return line.substring(tag.length() + 2);
        }
        return "";
    }

    private static String parseTagName(String line)
    {
        int idx = line.indexOf(": ");
        if(idx == -1) return "";

        return line.substring(0, idx);
    }

    private static boolean isTag(String line, String name)
    {
        return line.startsWith(name + ": ");
    }

    private List<ITrack> receiveTrackList()
    {
        ArrayList<ITrack> results = new ArrayList<>();

        HashMap<String, String> currentTrackTags = new HashMap<>();

        while(true)
        {
            String line = _socket.Recv();
            if(IsErrorResult(line)) return null;
            if(line.startsWith("OK") || line.isEmpty())
            {
                //Store the current values
                if(!currentTrackTags.isEmpty())
                {
                    if( currentTrackTags.containsKey(TagNames.TITLE)) {
                        ITrack track = new Track(currentTrackTags);
                        results.add(track);
                    }
                    else {

                        Log.w("MPD", "Discarding track missing Title tag. " + currentTrackTags.get("file"));
                    }
                }
                break;
            }

            if(isTag(line, "file"))
            {
                //File tag indicates a new Track

                //Store the current values
                if(!currentTrackTags.isEmpty())
                {
                    if( currentTrackTags.containsKey(TagNames.TITLE)) {
                        ITrack track = new Track(currentTrackTags);
                        results.add(track);
                    }
                    else {

                        Log.w("MPD", "Discarding track missing Title tag. " + currentTrackTags.get("file"));
                    }
                }
                //reset for next track
                currentTrackTags.clear();
            }

            String tagName = parseTagName(line);
            if(!tagName.isEmpty())
            {
                String value = parseTag(line, tagName);
                if(!value.isEmpty())
                {
                    currentTrackTags.put(tagName, value);
                }
            }
        }
        return results;
    }

    private TagSet recvTagSet()
    {
        HashMap<String, String> tags = new HashMap<>();
        while(true) {
            String line = _socket.Recv();
            if (IsErrorResult(line)) return null;
            if (line.startsWith("OK") || line.isEmpty()) {
                break;
            }
            String tagName = parseTagName(line);
            if(!tagName.isEmpty())
            {
                String value = parseTag(line, tagName);
                if(!value.isEmpty())
                {
                    tags.put(tagName, value);
                }
            }
        }
        return new TagSet(tags);
    }

    @Override
    public Status getStatus()
    {
        if(!_socket.isConnected())
            _socket.blockingConnect(_host, _port);
        _socket.Send("status");
        return new Status(recvTagSet());
    }

    @Override
    public List<ITrack> getCurrentPlayList()
    {
        if(!_socket.isConnected())
            _socket.blockingConnect(_host, _port);
        _socket.Send("playlistinfo");
        return receiveTrackList();
    }

    @Override
    public List<ITrack> getArtistTrackList(String artist)
    {
        if(!_socket.isConnected())
            _socket.blockingConnect(_host, _port);
        _socket.Send("find artist \"" + artist + "\"");
        return receiveTrackList();
    }

    @Override
    public void endIdle() {
        _socket.Send("noidle");
    }

    @Override
    public List<String> idle() {
        _socket.Send("idle");

        HashSet<String> result = new HashSet<>();

        while(true) {
            String line = _socket.Recv();
            if (IsErrorResult(line)) return null;
            if (line.startsWith("OK") || line.isEmpty()) break;
            if(isTag(line, TagNames.CHANGED))
                result.add(parseTag(line, TagNames.CHANGED));
        }
        return new ArrayList<String>(result);
    }

    @Override
    public List<IArtist> getArtistList()
    {
        if(!_socket.isConnected())
            _socket.blockingConnect(_host, _port);
        _socket.Send("list artist");

        ArrayList<IArtist> results = new ArrayList<>();

        while(true)
        {
            String line = _socket.Recv();
            if(IsErrorResult(line)) return null;
            if(line.startsWith("OK") || line.isEmpty()) break;
            String tag = parseTag(line, TagNames.ARTIST);
            if(!tag.isEmpty())
            {
                results.add(new Artist(tag));
            }
        }
        return results;
    }

    @Override
    public boolean removeFromPlaylist(int id)
    {
        if(!_socket.isConnected())
            _socket.blockingConnect(_host, _port);
        _socket.Send("delete id " + id);

        try {

            String line = _socket.Recv();
            return !IsErrorResult(line);
        }
        catch(Exception e)
        {
            Log.e("", e.getMessage());
        }
        return false;
    }

    /*********************************************************
     ClientSocket.Callback
    *********************************************************/

    @Override
    public void onReadError(String msg)
    {
        Log.i("Socket", "Read error: " + msg);
        _callback.onConnectionError(msg);
        _socket.disconnect();
    }


    /**************************************/
    /* Connect action
    /**************************************/
    public class ConnectAction extends AsyncTask<Void, Void, Boolean>
    {
        private String _errorMsg;
        private final String _host;
        private final int _port;

        public ConnectAction(String host, int port)
        {
            _host = host;
            _port = port;
        }

        @Override
        public Boolean doInBackground(Void...v)
        {
            return _socket.blockingConnect(_host, _port);
        }

        @Override
        public void onPostExecute(Boolean result)
        {
            if(result)
                _callback.onConnected();
            else
                _callback.onConnectionError(_errorMsg);
        }
    }
}
