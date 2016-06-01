package com.beingcitizen;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.beingcitizen.imageurl.ImageLoader;

/**
 * Created by pankaj on 30/5/16.
 */
public class ImageLoading extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        int loader = R.mipmap.ic_launcher;
        ImageView image = (ImageView)findViewById(R.id.image_load);
        String image_url = "http://api.androidhive.info/images/sample.jpg";

        // ImageLoader class instance
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());

        // whenever you want to load an image from url
        // call DisplayImage function
        // url - image url to load
        // loader - loader image, will be displayed before getting image
        // image - ImageView
        imgLoader.DisplayImage(image_url, loader, image);
        Toast.makeText(ImageLoading.this, "LOading done", Toast.LENGTH_SHORT).show();
    }
}
