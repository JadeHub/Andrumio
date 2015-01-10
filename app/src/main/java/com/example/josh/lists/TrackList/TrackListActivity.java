package com.example.josh.lists.TrackList;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.josh.lists.AlbumList.AlbumAsyncLoader;
import com.example.josh.lists.ArtistList.ArtistListActivity;
import com.example.josh.lists.R;
import com.josh.mpd.ITrack;
import com.josh.mpd.Track;

import java.util.ArrayList;
import java.util.List;


public class TrackListActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<List<ITrack>>{

    public final static String ARTIST_ID = "com.example.josh.TrackListActivity.ARTIST";
    public final static String ALBUM_ID = "com.example.josh.lists.TrackListActivity.ALBUM_ID";

    private ArrayAdapter<ITrack> _listAdapter;
    private String _albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);

        Intent intent = getIntent();

        _albumName = intent.getStringExtra(ALBUM_ID);

        setTitle("Tracks: " + _albumName);


        final ListView listview = (ListView) findViewById(R.id.song_listview);
        /*_listAdapter = new TrackListAdaptor(this,
                android.R.layout.simple_list_item_1, _tracks);
        listview.setAdapter(_listAdapter);*/
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id)
            {
                /*final Track item = (Track) parent.getItemAtPosition(position);*/
                finish();
            }
        });

        try {
            getLoaderManager().initLoader(0, null, this);
        } catch (Exception e) {

            Log.e("", e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_list, menu);
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
        if(id == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<ITrack>> onCreateLoader(int id, Bundle args)
    {
        return new TrackAsyncLoader(this, ArtistListActivity._client, _albumName);

    }

    @Override
    public void onLoadFinished(Loader<List<ITrack>> loader, List<ITrack> data)
    {
        final ListView listview = (ListView) findViewById(R.id.song_listview);
        _listAdapter = new ArrayAdapter<ITrack>(this,
                android.R.layout.simple_list_item_1, data);
        listview.setAdapter(_listAdapter);


        //_listAdapter = new ArtistListAdapter(this, new ArrayList<Character>(chars), letterToArtist);
        //listview.setAdapter(_listAdapter);
/*
        listview.setAdapter(new ArrayAdapter<Album>(this,
                android.R.layout.simple_list_item_1, data));
                */
    }

    @Override
    public void onLoaderReset(Loader<List<ITrack>> loader)
    {

    }
/*
    private class TrackListAdaptor extends ArrayAdapter<ITrack>
    {
        public TrackListAdaptor(Context context, int id, List<ITrack> tracks)
        {
            super(context, id, tracks);
        }

        @Override
        public View getView(int position,  View view, ViewGroup parent)
        {
            // inflate the layout for each item of listView

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.track_list_item_view, null);

            // get the reference of textViews
            TextView textViewConatctNumber=(TextView)view.findViewById(R.id.textViewSMSSender);
            TextView textViewSMSBody=(TextView)view.findViewById(R.id.textViewMessageBody);


            // Set the Sender number and smsBody to respective TextViews
            textViewConatctNumber.setText(getItem(position).GetName());
            textViewSMSBody.setText("blah");

            return view;
        }
    }*/
}
