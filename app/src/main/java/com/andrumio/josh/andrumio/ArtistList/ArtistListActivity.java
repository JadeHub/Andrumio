package com.andrumio.josh.andrumio.ArtistList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.andrumio.josh.andrumio.App;
import com.andrumio.josh.andrumio.AsyncLoader;
import com.andrumio.josh.andrumio.AsyncLoaderCallback;
import com.andrumio.josh.andrumio.AsyncLoaderLoad;
import com.andrumio.josh.andrumio.R;
import com.andrumio.josh.andrumio.TrackList.TrackListActivity;
import com.andrumio.josh.mpd.IArtist;
import com.andrumio.josh.mpd.IClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ArtistListActivity extends ActionBarActivity implements ExpandableListView.OnChildClickListener {

    private ArtistListAdapter _listAdapter;
    private AsyncLoader<List<IArtist>> _loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);

        IClient client = App.GetApp(this).getClient();

        _loader = new AsyncLoader<List<IArtist>>(this,
                new AsyncLoaderLoad() {
                    @Override
                    public List<IArtist> Load() {
                        return App.GetApp(ArtistListActivity.this).getClient().getArtistList();
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

        final ExpandableListView listview = (ExpandableListView) findViewById(R.id.artist_listview);
        listview.setOnChildClickListener(this);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        String name = (String) _listAdapter.getChild(groupPosition, childPosition);

        Intent intent = new Intent(ArtistListActivity.this, TrackListActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_list, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void onDataLoaded(List<IArtist> data) {

        HashSet<Character> chars = new HashSet<>();

        HashMap<Character, List<String>> letterToArtist = new HashMap<>();

        for (IArtist a : data) {
            String name = a.GetName();
            if (name.length() > 0) {
                char c = a.GetName().toUpperCase().charAt(0);
                chars.add(c);

                if (!letterToArtist.containsKey(c)) {
                    letterToArtist.put(c, new ArrayList<String>());
                }
                letterToArtist.get(c).add(a.GetName());
            }
        }

        final ExpandableListView listview = (ExpandableListView) findViewById(R.id.artist_listview);

        ArrayList<Character> charList = new ArrayList<Character>(chars);
        Collections.sort(charList, new Comparator<Character>() {
            @Override
            public int compare(Character c1, Character c2) {
                return c1.compareTo(c2);
            }
        });

        _listAdapter = new ArtistListAdapter(this, charList, letterToArtist);
        listview.setAdapter(_listAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
     //   outState.putSerializable("d", data);
        super.onSaveInstanceState(outState);
    }
}

