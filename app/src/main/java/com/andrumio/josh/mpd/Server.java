package com.andrumio.josh.mpd;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josh on 16/01/2015.
 */
public class Server {

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
    }
}
