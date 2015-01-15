package com.andrumio.josh.andrumio;

import android.app.Application;
import android.content.Context;

import com.andrumio.josh.mpd.Client;
import com.andrumio.josh.mpd.IClient;

/**
 * Created by Josh on 14/01/2015.
 */
public class App extends Application {

    private Client _client;

    public App()
    {
        _client = new Client("Living Room", "192.168.0.11", 6600);
    }

    public IClient getClient()
    {
        return _client;
    }

    public static App GetApp(Context c)
    {
        return (App)c.getApplicationContext();
    }
}
