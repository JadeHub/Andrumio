package com.example.josh.lists.AlbumList;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.josh.lists.AlbumList.AlbumAsyncLoader;
import com.example.josh.lists.ArtistList.ArtistListActivity;
import com.example.josh.lists.R;
import com.example.josh.lists.TrackList.TrackListActivity;
import com.josh.mpd.Album;
import com.josh.mpd.IAlbum;

import java.util.List;

public class AlbumListActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<List<IAlbum>>{

    public final static String ARTIST_ID = "com.example.josh.listsAlbumListActivity.ARTIST_ID";

    private ArrayAdapter<IAlbum> _listAdapter;
    private String _artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        Intent intent = getIntent();
        _artistName = intent.getStringExtra(ARTIST_ID);
        if(_artistName.isEmpty())
            return;

        setTitle("Albums: " + _artistName);

        final ListView listview = (ListView) findViewById(R.id.album_listview);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id)
            {

            final IAlbum album = (IAlbum) parent.getItemAtPosition(position);

            Intent intent = new Intent(AlbumListActivity.this, TrackListActivity.class);
            intent.putExtra(TrackListActivity.ALBUM_ID, album.GetName());
            startActivity(intent);

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
        getMenuInflater().inflate(R.menu.menu_album_list, menu);
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
    public Loader<List<IAlbum>> onCreateLoader(int id, Bundle args)
    {
        return new AlbumAsyncLoader(this, ArtistListActivity._client, _artistName);
    }

    @Override
    public void onLoadFinished(Loader<List<IAlbum>> loader, List<IAlbum> data)
    {


        final ListView listview = (ListView) findViewById(R.id.album_listview);

        //_listAdapter = new ArtistListAdapter(this, new ArrayList<Character>(chars), letterToArtist);
        //listview.setAdapter(_listAdapter);

        listview.setAdapter(new ArrayAdapter<IAlbum>(this,
                android.R.layout.simple_list_item_1, data));
    }

    @Override
    public void onLoaderReset(Loader<List<IAlbum>> loader)
    {

    }

}
