package com.example.the.youtubetest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class StartActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView ytpv;
    private YouTubePlayer ytp;
    final String serverKey = "MainActvity와 동일한키";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ytpv = (YouTubePlayerView) findViewById(R.id.youtubeplayer);
        ytpv.initialize(serverKey, this);

    }

    @Override
    public void onInitializationFailure(
            com.google.android.youtube.player.YouTubePlayer.Provider arg0,
            YouTubeInitializationResult arg1) {
        Toast.makeText(this, "Initialization Fail", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onInitializationSuccess(
            com.google.android.youtube.player.YouTubePlayer.Provider arg0,
            YouTubePlayer arg1, boolean arg2) {
        ytp = arg1;

        Intent gt = getIntent();
        ytp.loadVideo(gt.getStringExtra("id"));

    }

}