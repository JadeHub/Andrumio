package com.andrumio.josh.andrumio.TrackList;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.andrumio.josh.andrumio.ArtistList.ArtistAsyncLoader;
import com.andrumio.josh.andrumio.ArtistList.ArtistListActivity;
import com.andrumio.josh.andrumio.ArtistList.ArtistListAdapter;
import com.andrumio.josh.andrumio.R;
import com.andrumio.josh.mpd.ITrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TrackListActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<List<ITrack>>, ExpandableListView.OnChildClickListener {

    public final static String ARTIST_ID = "com.example.josh.TrackListActivity.ARTIST";

    private TrackListAdapter _listAdapter;
    private String _artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);

        Intent intent = getIntent();
        _artist = intent.getStringExtra(ARTIST_ID);
        ((ExpandableListView) findViewById(R.id.track_listview)).setOnChildClickListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        ITrack track = (ITrack) _listAdapter.getChild(groupPosition, childPosition);
/*
        Intent intent = new Intent(ArtistListActivity.this, AlbumListActivity.class);
        intent.putExtra(AlbumListActivity.ARTIST_ID, name);
        try {
            startActivity(intent);
        }
        catch(Exception e)
        {
            Log.e("", e.getMessage());
        }*/
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track_list, menu);
        return true;
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

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<ITrack>> onCreateLoader(int id, Bundle args) {
        return new TrackAsyncLoader(this, ArtistListActivity._client, _artist);
    }

    @Override
    public void onLoadFinished(Loader<List<ITrack>> loader, List<ITrack> data) {
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

        final ExpandableListView listview = (ExpandableListView) findViewById(R.id.track_listview);

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

        _listAdapter = new TrackListAdapter(this, albumList, albumToSongList);
        listview.setAdapter(_listAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<ITrack>> loader) {

    }
}
