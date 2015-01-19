package com.andrumio.josh.andrumio.Playlist;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;

import com.andrumio.josh.andrumio.App;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoader;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoaderCallback;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoaderLoad;
import com.andrumio.josh.andrumio.R;
import com.andrumio.josh.mpd.ITrack;
import com.andrumio.josh.mpd.Server.Server;

import java.util.List;
import com.andrumio.josh.mpd.Server.PlaylistManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment {

    private AsyncLoader<List<ITrack>> _loader;
    private PlaylistAdapter _listAdapter;
    private Server _server;

    public PlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        _server = App.GetApp(getActivity()).getServer();

        _server.getPlaylistMgr().addListener(new PlaylistManager.Listener() {
            @Override
            public void onNewPlaylist(List<ITrack> playlist) {
                onDataLoaded(playlist);
            }
        });

        if(_server.getPlaylistMgr().hasPlaylist())
        {
            _listAdapter = new PlaylistAdapter(getActivity(), _server.getPlaylistMgr().getPlaylist());
            ((ListView) view.findViewById(R.id.playlist_listview)).setAdapter(_listAdapter);
        }

        /*

        _loader = new AsyncLoader<List<ITrack>>(getActivity(),
                new AsyncLoaderLoad() {
                    @Override
                    public List<ITrack> Load() {
                        return App.GetApp(getActivity()).getServer().getQueryClient().getCurrentPlayList();
                    }
                },
                new AsyncLoaderCallback<List<ITrack>>() {
                    @Override
                    public void onLoaded(List<ITrack> result) {
                        onDataLoaded(result);
                    }

                    @Override
                    public void onLoadFailed(Exception e) {

                    }
                });


        _loader.initLoader(getLoaderManager());*/

        registerForContextMenu(view.findViewById(R.id.playlist_listview));

        return view;
    }

    public void onDataLoaded(List<ITrack> data) {

        _listAdapter = new PlaylistAdapter(getActivity(), data);
        try {
            ((ListView) getView().findViewById(R.id.playlist_listview)).setAdapter(_listAdapter);
        }
        catch(Exception e)
        {
            Log.e("", e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ITrack track = (ITrack)_listAdapter.getItem(info.position);
        int menuItemIndex = item.getItemId();
        if(menuItemIndex == R.id.cmd_remove)
        {
            App.GetApp(getActivity()).getServer().getQueryClient().removeFromPlaylist(Integer.parseInt(track.getTag("Id")));
            Log.i("", "asf");
        }
        if(menuItemIndex == R.id.cmd_play_now) {


        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if(v.getId() == R.id.playlist_listview)
        {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu_playlist, menu);

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            ITrack track = (ITrack)_listAdapter.getItem(info.position);
            menu.setHeaderTitle(track.getTitle());
        }
    }
}
