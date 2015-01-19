package com.andrumio.josh.mpd.Server;

import android.os.AsyncTask;

import com.andrumio.josh.mpd.IArtist;
import com.andrumio.josh.mpd.ITrack;
import com.andrumio.josh.mpd.Status;
import com.andrumio.josh.mpd.TagSet;

import java.util.List;

/**
 * Created by Josh on 18/01/2015.
 */
public class PlaylistManager {

    public interface Listener
    {
        void onNewPlaylist(List<ITrack> playlist);
    }

    private final Server mServer;
    private int mVersion;
    private List<ITrack> mPlaylist;

    private Listener mListener;

    public PlaylistManager(Server server)
    {
        mServer = server;
        mVersion = -1;
        mServer.addEventListener(new Server.EventListener() {
            @Override
            public void onEvent(String type, Status status) {
                if(status.getPlaylistVersion() > mVersion)
                {
                    requestPlaylist();
                }
            }
        });

    //    requestPlaylist();
    }

    public void addListener(Listener l)
    {
        mListener = l;
    }


    private void requestPlaylist()
    {
        UpdatePlaylistTask task = new UpdatePlaylistTask();
        task.execute();
    }

    private void onNewPlaylist(List<ITrack> list)
    {
        mVersion = mServer.getCachedStatus().getPlaylistVersion();


        int idxOld = 0;
        int idxNew = 0;

     //   while(idxOld < mPlaylist.size() && idxNew < list.size())
        {
           // if
        }


        mPlaylist = list;
        if(mListener != null)
            mListener.onNewPlaylist(mPlaylist);
    }

    public List<ITrack> getPlaylist() {return mPlaylist;}
    public boolean hasPlaylist() {return mPlaylist != null;}

    /**************************************/
    /* Connect action
    /**************************************/
    public class UpdatePlaylistTask extends AsyncTask<Void, Void, List<ITrack>>
    {
        @Override
        public List<ITrack> doInBackground(Void...v)
        {


            return mServer.getQueryClient().getCurrentPlayList();
        }

        @Override
        public void onPostExecute(List<ITrack> result)
        {
            onNewPlaylist(result);
        }
    }
}
