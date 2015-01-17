package com.andrumio.josh.andrumio.Playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrumio.josh.andrumio.R;
import com.andrumio.josh.mpd.ITrack;

import java.util.List;

/**
 * Created by Josh on 16/01/2015.
 */
public class PlaylistAdapter extends ArrayAdapter<ITrack> {

    public PlaylistAdapter(Context context, List<ITrack> objects)
    {
        super(context, R.layout.list_row, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if(convertView==null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_row, null);
        }

        ((TextView)vi.findViewById(R.id.txtHeadline)).setText(getItem(position).getTitle());
        ((TextView)vi.findViewById(R.id.txtSubline)).setText(getItem(position).getAlbumName());
        ((ImageView)vi.findViewById(R.id.list_image)).setImageResource(R.drawable.track3);

        return vi;
    }
}
