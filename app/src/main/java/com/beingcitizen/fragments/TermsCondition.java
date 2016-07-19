package com.beingcitizen.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beingcitizen.R;

/**
 * Created by saransh on 27-06-2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class TermsCondition extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        WebView browser = (WebView)rootView.findViewById(R.id.webview_terms);
//        //browser.setBackground(getResources().getDrawable(R.drawable.ic_bg_main));
//        browser.setBackgroundResource(R.drawable.ic_bg_main);
//        browser.setBackgroundColor(0x00000000);
//        browser.loadUrl("http://52.74.96.52/terms");
        //  hideNavBar();
        return inflater.inflate(R.layout.fragment_termconditions, container, false);
    }


//    private void hideNavBar() {
//        // TODO Auto-generated method stub
//        int currentapiVersion = Build.VERSION.SDK_INT;
//        if (currentapiVersion <= Build.VERSION_CODES.FROYO){
//            // Do something for froyo and above versions
//        } else if (currentapiVersion== Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            View decorView = getActivity().getWindow().getDecorView();
//            // Hide both the navigation bar and the status bar.
//            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
//            // a general rule, you should design your app to hide the status bar whenever you
//            // hide the navigation bar.
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
//    }

}
