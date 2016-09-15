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
 *
 * Terms and conditions page.
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

}
