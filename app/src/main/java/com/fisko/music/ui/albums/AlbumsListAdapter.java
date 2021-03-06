package com.fisko.music.ui.albums;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fisko.music.R;
import com.fisko.music.data.Album;
import com.fisko.music.ui.songs.SongsActivity;
import com.fisko.music.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class AlbumsListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Album> mAlbums;
    private Activity mActivity;

    private String mPlayingAlbumId;

    AlbumsListAdapter(Activity activity) {
        mInflater = LayoutInflater.from(activity);
        mAlbums = new ArrayList<>();
        mActivity = activity;
    }

    void replaceData(List<Album> albums) {
        mAlbums = new ArrayList<>(albums);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAlbums.size();
    }

    @Override
    public Album getItem(int position) {
        return mAlbums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        TextView albumName;
        TextView albumArtist;
        ImageView playingIndicator;
        ImageView albumCover;
    }

    void setPlayingAlbum(String albumId) {
        mPlayingAlbumId = albumId;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Album album = getItem(position);
        ViewHolder holder;

        if(view == null || view.getTag() == null) {
            view = mInflater.inflate(R.layout.albums_list_item, parent, false);
            holder = new ViewHolder();

            holder.albumName = (TextView) view.findViewById(R.id.album_name);
            holder.albumArtist = (TextView) view.findViewById(R.id.album_artist);
            holder.playingIndicator = (ImageView) view.findViewById(R.id.album_playing_indicator);
            holder.albumCover = (ImageView) view.findViewById(R.id.album_cover);

            fillHolder(holder, album);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            fillHolder(holder, album);
        }

        view.setClickable(true);
        view.setLongClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum(album);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mActivity.registerForContextMenu(v);
                mActivity.openContextMenu(v);
                return true;
            }
        });

        return view;
    }

    void removeAlbum(Album album) {
        mAlbums.remove(album);
        notifyDataSetChanged();
    }

    private void openAlbum(Album album) {
        Intent intent = new Intent(mActivity, SongsActivity.class);
        intent.putExtra(Constants.ALBUM_BUNDLE.ALBUM, album);
        mActivity.startActivity(intent);
    }

    private void fillHolder(ViewHolder holder, Album album) {
        holder.albumName.setText(album.getName());
        holder.albumArtist.setText(album.getArtist());
        if(album.getId().equals(mPlayingAlbumId)) {
            Drawable drawable = ContextCompat.getDrawable(mActivity, R.drawable.playing_indicator);
            holder.playingIndicator.setImageDrawable(drawable);
        } else {
            holder.playingIndicator.setImageResource(android.R.color.transparent);
        }
        String coverUrl = album.getImagePath();
        Picasso.with(mActivity).load(coverUrl).into(holder.albumCover);
    }


}