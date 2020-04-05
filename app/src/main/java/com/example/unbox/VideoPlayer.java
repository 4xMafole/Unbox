package com.example.unbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayer extends AppCompatActivity
{

    //variables za kuplay video
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private long playbackPosition = 0;
    private int currentWindow = 0;

    //variables za kuhamishwa (recyclerview and data in modules)
    private RecyclerView _recyclerView;
    private List<Module> _moduleList = new ArrayList<>();
    private ModuleAdapter _moduleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        //Get the player view from the layout.
        playerView = findViewById(R.id.video_view);

        initRecycledData();
    }

    //Creating an exoplayer
    private void initializePlayer()
    {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd());
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        }

        playerView.setPlayer(player);

        //adding media source file
        Uri uri = Uri.parse(getString(R.string.media_url_dash));
        MediaSource mediaSource = buildMediaSource(uri);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    //Creating a media source. Our player needs some content to play.
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "Unbox");
        DashMediaSource.Factory mediaSourceFactory = new DashMediaSource.Factory(dataSourceFactory);
        return mediaSourceFactory.createMediaSource(uri);

    }

    //onStart and onResume controls the video play depending on the SDK (sdk 24 and above supports multi-windows)
    @Override
    public void onStart()
    {
        super.onStart();
        if (Util.SDK_INT >= 24)
        {
            initializePlayer();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    /*hideSystemUi is a helper method
     *Called onResume method
     *It helps to have full screen experience
     */

    @SuppressLint("InlinedApi")
    private void hideSystemUi()
    {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    //release the player while storing play/pause, current window index and currently playback position.
    private void releasePlayer()
    {
        if (player != null)
        {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    private void initRecycledData()
    {
        _recyclerView = findViewById(R.id.recyclerview);

        _moduleAdapter = new ModuleAdapter(_moduleList);

        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(getApplicationContext());

        _recyclerView.setLayoutManager(_layoutManager);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _recyclerView.setAdapter(_moduleAdapter);

        _recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), _recyclerView, new RecyclerTouchListener.ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                //here you will set video urls which will will update the videos on the list. God bless these moves.
                Module _module = _moduleList.get(position);
                Toast.makeText(VideoPlayer.this, _module.getModule() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position)
            {

            }
        }));

        prepareModuleData();
    }

    private void prepareModuleData()
    {
        Module _module = new Module("Overview", "02:49");
        _moduleList.add(_module);

        _module = new Module("Storage/Warehouse", "03:23");
        _moduleList.add(_module);
        _module = new Module("Repetition", "02:03");
        _moduleList.add(_module);
        _module = new Module("Decision Making", "02:59");
        _moduleList.add(_module);
        _module = new Module("List Creation", "05:45");
        _moduleList.add(_module);
        _module = new Module("Grouping Instructions", "04:40");
        _moduleList.add(_module);
        _module = new Module("Mistakes Avoidance", "03:32");
        _moduleList.add(_module);
        _module = new Module("Computer Language usage", "03:40");
        _moduleList.add(_module);

        _moduleAdapter.notifyDataSetChanged();
    }
}
