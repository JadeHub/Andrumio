package com.andrumio.josh.andrumio.TrackList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;

import com.andrumio.josh.andrumio.R;


public class TrackListActivity extends ActionBarActivity {

    public final static String ARTIST_ID = "com.example.josh.TrackListActivity.ARTIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);

        Intent intent = getIntent();
        String artist = intent.getStringExtra(ARTIST_ID);

        // If not already added to the Fragment manager add it. If you don't do this a new Fragment will be added every time this method is called (Such as on orientation change)
        if (savedInstanceState == null) {
            android.app.Fragment f = new TrackListFragment();
            Bundle args = new Bundle();
            args.putString("Artist", artist);
            f.setArguments(args);
            getFragmentManager().beginTransaction().add(android.R.id.content, f).commit();

            setTitle(artist);
        }
    }
}
