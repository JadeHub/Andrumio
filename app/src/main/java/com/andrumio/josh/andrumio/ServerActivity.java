package com.andrumio.josh.andrumio;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.andrumio.josh.andrumio.ArtistList.ArtistListActivity;
import com.andrumio.josh.mpd.Client;
import com.andrumio.josh.mpd.IClient;


public class ServerActivity extends ActionBarActivity {

    private IClient _client;

    private Button _btnArtistList;
    private Button _btnFileList;
    private Button _btnStatus;
    private Button _btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        _client = App.GetApp(this).getClient();

        _btnArtistList = (Button)findViewById(R.id.btnArtistList);
        _btnFileList = (Button)findViewById(R.id.btnFileList);
        _btnStatus = (Button)findViewById(R.id.btnStatus);
        _btnSettings = (Button)findViewById(R.id.btnSettings);

        _btnArtistList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ServerActivity.this, ArtistListActivity.class);
                try {
                    startActivity(intent);
                }
                catch(Exception e)
                {
                    Log.e("", e.getMessage());
                }
            }
        });

        _btnFileList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            }
        });

        _btnStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            }
        });

        _btnSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            }
        });

        if(!_client.isConnected())
        {
            _client.connect(new Client.Callback() {
                @Override
                public void onConnected() {
                    enableButtons();
                }

                @Override
                public void onDisconnected() {
                    enableButtons();
                }
            });
        }



        enableButtons();
    }

    private void enableButtons()
    {
        boolean enable = _client.isConnected();
        _btnArtistList.setEnabled(enable);
        _btnFileList.setEnabled(enable);
        _btnStatus.setEnabled(enable);
        _btnSettings.setEnabled(enable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server, menu);
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
}
