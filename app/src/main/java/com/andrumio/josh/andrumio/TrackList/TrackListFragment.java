package com.andrumio.josh.andrumio.TrackList;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.andrumio.josh.andrumio.App;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoader;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoaderCallback;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoaderLoad;
import com.andrumio.josh.andrumio.R;
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
public class TrackListFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private TrackListAdapter _listAdapter;
    private AsyncLoader<List<ITrack>> _loader;

    public TrackListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_track_list, container, false);
        ((ExpandableListView) view.findViewById(R.id.track_listview)).setOnChildClickListener(this);
        ((ExpandableListView) view.findViewById(R.id.track_listview)).setVisibility(View.GONE);

        _loader = new AsyncLoader<List<ITrack>>(getActivity(),
                new AsyncLoaderLoad() {
                    @Override
                    public List<ITrack> Load() {
                        String artist = getArguments().getString("Artist");
                        return App.GetApp(getActivity()).getClient().getArtistTrackList(artist);
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

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    public void onDataLoaded(List<ITrack> data) {

        getView().findViewById(R.id.track_listview).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        HashSet<String> albums = new HashSet<>();

        HashMap<String, List<ITrack>> albumToSongList = new HashMap<>();

        for (ITrack t : data) {
            String album = t.getAlbumName();
            if (album != null && album.length() > 0) {
                albums.add(album);

                if (!albumToSongList.containsKey(album)) {
                    albumToSongList.put(album, new ArrayList<ITrack>());
                }
                albumToSongList.get(album).add(t);
            }
        }

        final ExpandableListView listview = (ExpandableListView) getView().findViewById(R.id.track_listview);

        ArrayList<String> albumList = new ArrayList<String>(albums);
        Collections.sort(albumList, new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                return c1.compareTo(c2);
            }
        });

        for (String album : albumToSongList.keySet())
        {
            Collections.sort(albumToSongList.get(album), new Comparator<ITrack>() {
                @Override
                public int compare(ITrack lhs, ITrack rhs) {
                    //todo sort by track id
                    return lhs.getTitle().compareTo(rhs.getTitle());
                }
            });
        }

        _listAdapter = new TrackListAdapter(getActivity(), albumList, albumToSongList);
        listview.setAdapter(_listAdapter);
    }
}
