package com.andrumio.josh.mpd.Server;


import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

import com.andrumio.josh.mpd.Client;
import com.andrumio.josh.mpd.IClient;
import com.andrumio.josh.mpd.Status;
import com.andrumio.josh.mpd.TagSet;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Josh on 16/01/2015.
 */
public class Server {

    private class IdleEvent
    {
        public final Status Status;
        public final List<String> Changes;
        public IdleEvent(List<String> changes, Status status)
        {
            Status = status;
            Changes = changes;
        }
    }

    public interface EventListener
    {
        void onEvent(String type, Status status);
    }

    public interface ConnectionListener
    {
        void onConnected();
        void onDisconnected();
        void onConnectionError(String error);
    }

    private List<ConnectionListener> _connectionListeners;
    private List<EventListener> _eventListeners;

    private IClient _listenerClient;
    private IClient _commandClient;
    private ListenerThread _listenerThread;
    private Status _cachedStatus;

    private Handler _handler;

    private PlaylistManager _playlistMgr;

    /**
     * Handles connection callbacks from the listening client
     */
    private class ListenerClientCallback implements IClient.Callback {

        private Server _server;

        public ListenerClientCallback(Server server)
        {
            _server = server;
        }

        @Override
        public void onConnected() {

            _listenerThread.start();

            for(ConnectionListener l : _connectionListeners)
            {
                l.onConnected();
            }

        }

        @Override
        public void onDisconnected() {

            _cachedStatus = null;

            for(ConnectionListener l : _connectionListeners)
            {
                l.onDisconnected();
            }
        }

        @Override
        public void onConnectionError(String err)
        {
            for(ConnectionListener l : _connectionListeners)
            {
                l.onConnectionError(err);
            }
        }
    }

    /**
     * Handles connection callbacks from the command client
     */
    private class CommandClientCallback implements IClient.Callback {

        private Server _server;

        public CommandClientCallback(Server server)
        {
            _server = server;
        }

        @Override
        public void onConnected() {


        }

        @Override
        public void onDisconnected() {


        }

        @Override
        public void onConnectionError(String err)
        {

        }
    }

    public IClient getQueryClient() {return _commandClient;}

    public Server(String host, int port)
    {
        _listenerClient = new Client("listener", host, port);
        _commandClient = new Client("command", host, port);
        _connectionListeners = new ArrayList<ConnectionListener>();
        _eventListeners = new ArrayList<EventListener>();
        _playlistMgr = new PlaylistManager(this);

        _handler = new Handler(Looper.getMainLooper()) {
            /*
             * receive message from listener thread
             */
            @Override
            public void handleMessage(Message inputMessage) {
                IdleEvent idleEvent = (IdleEvent) inputMessage.obj;
                _cachedStatus = idleEvent.Status;
                Log.i("IDLE", idleEvent.Changes.toString());

                for(EventListener l : _eventListeners)
                {
                    if(idleEvent.Changes.size() > 0) {
                        for (String eventType : idleEvent.Changes) {
                            l.onEvent(eventType, idleEvent.Status);
                        }
                    }
                    else
                    {
                        l.onEvent("", idleEvent.Status);
                    }
                }
            }
        };

        _listenerThread = new ListenerThread();
        _listenerClient.connect(new ListenerClientCallback(this));

    }

    public PlaylistManager getPlaylistMgr() {return _playlistMgr;}

    public void addConnectionListener(ConnectionListener l)
    {
        removeConnectionListener(l);
        _connectionListeners.add(l);
    }

    public void removeConnectionListener(ConnectionListener l)
    {
        _connectionListeners.remove(l);
    }

    public void addEventListener(EventListener l)
    {
        removeEventListener(l);
        _eventListeners.add(l);
    }

    public void removeEventListener(EventListener l)
    {
        _eventListeners.remove(l);
    }

    public boolean isConnected()
    {
        return _listenerClient.isConnected();
    }

    public boolean hasCachedStatus() {return _cachedStatus != null;}

    public Status getCachedStatus() {return _cachedStatus;}

    /**
     * Listener thread
     */
    private class ListenerThread extends  Thread
    {
        @Override
        public void run()
        {
            if(!_listenerClient.isConnected())
                return;
            //get an initial status
            Status initialStatus = _listenerClient.getStatus();
            _handler.obtainMessage(1, new IdleEvent(new ArrayList<String>(), initialStatus)).sendToTarget();

            while(true)
            {
                try {
                    if(!_listenerClient.isConnected())
                        break;

                    Log.i("IDLE", "Enter");
                    List<String> changes = _listenerClient.idle();
                    Log.i("IDLE", "Leave");

                    Status status = _listenerClient.getStatus();
                    _handler.obtainMessage(1, new IdleEvent(changes, status)).sendToTarget();
                }
                catch(Exception e)
                {
                    break;
                }
            }
        }
    }

}
