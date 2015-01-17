package com.andrumio.josh.andrumio.ArtistList;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.andrumio.josh.andrumio.App;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoader;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoaderCallback;
import com.andrumio.josh.andrumio.AsycLoader.AsyncLoaderLoad;
import com.andrumio.josh.andrumio.R;
import com.andrumio.josh.andrumio.TrackList.TrackListActivity;
import com.andrumio.josh.mpd.IArtist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistListFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private ArtistListAdapter _listAdapter;
    private AsyncLoader<List<IArtist>> _loader;

    public ArtistListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_artist_list, container, false);

        setHasOptionsMenu(true);

        ExpandableListView listView = ((ExpandableListView) v.findViewById(R.id.artist_listview1));
        listView.setVisibility(View.GONE);
        listView.setOnChildClickListener(this);

        _loader = new AsyncLoader<List<IArtist>>(getActivity(),
                new AsyncLoaderLoad() {
                    @Override
                    public List<IArtist> Load() {
                        return App.GetApp(getActivity()).getServer().getClient().getArtistList();
                    }
                },
                new AsyncLoaderCallback<List<IArtist>>() {
                    @Override
                    public void onLoaded(List<IArtist> result) {
                        onDataLoaded(result);
                    }

                    @Override
                    public void onLoadFailed(Exception e) {

                    }
                });

        _loader.initLoader(getLoaderManager());
        return v;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                int childPosition, long id)
    {
        String name = (String) _listAdapter.getChild(groupPosition, childPosition);

        Intent intent = new Intent(getActivity(), TrackListActivity.class);
        intent.putExtra(TrackListActivity.ARTIST_ID, name);
        try {
            startActivity(intent);
        }
        catch(Exception e)
        {
            Log.e("", e.getMessage());
        }
        return true;
    }

    public void onDataLoaded(List<IArtist> data) {

        getView().findViewById(R.id.artist_listview1).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        HashSet<String> chars = new HashSet<>();

        HashMap<String, List<String>> letterToArtist = new HashMap<>();

        for (IArtist a : data) {
            String name = a.GetName();
            if (name.length() > 0)
            {
                String key = "Artists: " + a.GetName().toUpperCase().charAt(0);

                chars.add(key);

                if (!letterToArtist.containsKey(key)) {
                    letterToArtist.put(key, new ArrayList<String>());
                }
                letterToArtist.get(key).add(a.GetName());
            }
        }

        final ExpandableListView listview = (ExpandableListView) getView().findViewById(R.id.artist_listview1);

        ArrayList<String> artistList = new ArrayList<String>(chars);

        Collections.sort(artistList, new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                return c1.compareTo(c2);
            }
        });

        _listAdapter = new ArtistListAdapter(getActivity(), artistList, letterToArtist);
        listview.setAdapter(_listAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  inflater.inflate(R.menu.menu_artist_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
