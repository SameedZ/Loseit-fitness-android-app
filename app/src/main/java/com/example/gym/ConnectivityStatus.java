package com.example.gym;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityStatus {
    Context context;

    public ConnectivityStatus(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getContext().getSystemService(this.getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isconnected = activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
        return isconnected;
    }
}
