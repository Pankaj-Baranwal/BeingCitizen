package com.beingcitizen;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by pankaj on 6/6/16.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //final NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        String status = NetworkUtil.getConnectivityStatusString(context);

        /*if (status.contentEquals("Not connected to Internet")){
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Not connected to Internet");
            builder.show();
        }
        */
    }
}