package com.beingcitizen.beingcitizen;

/**
 * Created by saransh on 14-06-2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.beingcitizen.R;

import org.json.JSONObject;

public class splashscreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    android.widget.VideoView video_player_view;
    DisplayMetrics dm;
    SurfaceView sur_view;
    Thread th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hideSystemUI();
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(R.layout.splashscreen);
        video_player_view = (VideoView) findViewById(R.id.videoView);
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        video_player_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        video_player_view.setMinimumHeight(dm.heightPixels);
                        video_player_view.setMinimumWidth(dm.widthPixels);

                    }
                });
            }
        });

        /* th= new Thread(new Runnable() {
            @Override
            public void run() {*/

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                video_player_view.setMinimumHeight(dm.heightPixels);
                video_player_view.setMinimumWidth(dm.widthPixels);

                String path1 = "android.resource://" + getPackageName() + "/" + R.raw.crowd;
                // String path1="http://192.168.0.116:1935/live/myStream/playlist.m3u8?DVR";
                Uri uri = Uri.parse(path1);
                video_player_view.setVideoURI(uri);
                video_player_view.start();
                video_player_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        video_player_view.start();

                    }
                });
            }
        });








        JSONObject jsonobject;
        jsonobject = JSONfunctions.getJSONfromURL("http://beingcitizen.com:2082/cpsess5911541994/3rdparty/phpMyAdmin/index.php");




        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(splashscreen.this, LoginMain.class);
                startActivity(i);
                //overridePendingTransition(R.layout.fade_in, R.layout.fade_out);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}