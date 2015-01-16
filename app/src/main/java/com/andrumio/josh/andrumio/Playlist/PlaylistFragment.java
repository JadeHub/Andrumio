package com.andrumio.josh.andrumio.Playlist;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.andrumio.josh.andrumio.App;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoader;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoaderCallback;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoaderLoad;
import com.andrumio.josh.andrumio.R;
import com.andrumio.josh.andrumio.TrackList.TrackListAdapter;
import com.andrumio.josh.mpd.ITrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment {

    private AsyncLoader<List<ITrack>> _loader;
    private ArrayAdapter<ITrack> _listAdapter;

    public PlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        _loader = new AsyncLoader<List<ITrack>>(getActivity(),
                new AsyncLoaderLoad() {
                    @Override
                    public List<ITrack> Load() {
                        return App.GetApp(getActivity()).getClient().getCurrentPlayList();
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


        _loader.initLoader(getLoaderManager());

        return view;
    }

    public void onDataLoaded(List<ITrack> data) {

        _listAdapter = new ArrayAdapter<ITrack>(getActivity(), android.R.layout.simple_list_item_activated_1, data);
        ((ListView)getView().findViewById(R.id.playlist_listview)).setAdapter(_listAdapter);
    }
}
