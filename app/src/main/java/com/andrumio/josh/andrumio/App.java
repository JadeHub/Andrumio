package com.andrumio.josh.andrumio;

import android.app.Application;
import android.content.Context;

import com.andrumio.josh.mpd.Client;
import com.andrumio.josh.mpd.IClient;
import com.andrumio.josh.mpd.Server;

/**
 * Created by Josh on 14/01/2015.
 */
public class App extends Application {

    private Client _client;
    private Server _server;

    public App()
    {
        _server = new Server("192.168.0.11", 6600);

    }

    public Server getServer()
    {
        return _server;
    }

    public static App GetApp(Context c)
    {
        return (App)c.getApplicationContext();
    }
}
