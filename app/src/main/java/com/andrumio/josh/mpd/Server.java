package com.andrumio.josh.mpd;


import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

import java.util.List;


/**
 * Created by Josh on 16/01/2015.
 */
public class Server {

    private class IdleEvent
    {
        public final List<String> Changes;

        public IdleEvent(List<String> changes)
        {
            Changes = changes;
        }
    }

    public abstract class ConnectionListener
    {
        public abstract void onConnected();
        public abstract void onDisconnected();
    }

    private IClient _listenerClient;
    private IClient _commandClient;
    private ListenerThread _listenerThread;

    private Handler _handler;


    private class ListenerClientCallback implements IClient.Callback {

        private Server _server;

        public ListenerClientCallback(Server server)
        {
            _server = server;
        }

        @Override
        public void onConnected() {

            _listenerThread.start();
        }

        @Override
        public void onDisconnected() {

            boolean a = _listenerThread.isAlive();
        }

        @Override
        public void onConnectionError(String err)
        {

        }
    }

    public IClient getClient() {return _commandClient;}

    public Server(String host, int port)
    {
        _listenerClient = new Client("listener", host, port);
        _commandClient = new Client("command", host, port);

        _handler = new Handler(Looper.getMainLooper()) {
            /*
             * handleMessage() defines the operations to perform when the
             * Handler receives a new Message to process.
             */
            @Override
            public void handleMessage(Message inputMessage) {

                // Gets the image task from the incoming Message object.
                IdleEvent idleEvent = (IdleEvent) inputMessage.obj;
                Log.i("IDLE", idleEvent.Changes.toString());
            }
        };


        _listenerThread = new ListenerThread();
        _listenerClient.connect(new ListenerClientCallback(this));
    }

    private class ListenerThread extends  Thread
    {
        @Override
        public void run()
        {
            while(true)
            {
                try {
                    if(!_listenerClient.isConnected())
                        break;

                    List<String> changes = _listenerClient.idle();
                    _handler.obtainMessage(1, new IdleEvent(changes)).sendToTarget();
                }
                catch(Exception e)
                {
                    break;
                }
            }
        }
    }


    /*

    private final IClient _client;
    private final IdleThread _idleThread;

    public Server(IClient client)
    {
        _client = client;
        _idleThread = new IdleThread();
        _idleThread.start();
    }


    public void enterIdle()
    {
        synchronized (_idleSignal) {
            _idleSignal.notify();
        }
    }

    public boolean isInIdle()
    {
        return _isInIdle;
    }

    private Object _idleSignal = new Object();

    private  boolean _isInIdle = false;
    private final Lock _idleThreadLock = new ReentrantLock();
    private Condition _wantIdle = _idleThreadLock.newCondition();

    private class IdleThread extends  Thread
    {
        @Override
        public void run()
        {
            while(true)
            {
                {
                  //  _idleThreadLock.lock();
                    try {



                        synchronized (_idleSignal) {
                            _idleSignal.wait();

                            _isInIdle = true;
                        }
                    }
                    catch(InterruptedException r)
                    {

                    }
                    catch(Exception e)
                    {
                        Log.e("", e.getMessage());
                    }
                }
                _client.idle();
                _isInIdle = false;
            }
        }
    }
*/
/*
    public interface IStatus
    {
        void onStatusChanged(TagSet status);
    }

    private final IClient _client;
    private TagSet _status;

    private List<IStatus> _statusListeners;

    public Server(IClient client)
    {
        _client = client;
        _statusListeners = new LinkedList<>();

    }

    private void setStatus(TagSet newStatus)
    {
        if(!newStatus.isIdentical(_status))
        {
            _status = newStatus;
            for(IStatus listener : _statusListeners)
            {
                listener.onStatusChanged(_status);
            }
        }
    }

    public void RequestStatusUpdate()
    {

    }

    public void SubscribeStatus(IStatus listener)
    {
        if(!_statusListeners.contains(listener))
            _statusListeners.add(listener);
    }

    public void UnsubscribeStatus(IStatus listener)
    {
        _statusListeners.remove(listener);
    }

    public class StatusUpdateAction extends AsyncTask<Void, Void, TagSet> {

        @Override
        public TagSet doInBackground(Void... v) {

            return _client.getStatus();
        }

        @Override
        public void onPostExecute(TagSet tags) {

            setStatus(tags);
        }
    }*/
}
