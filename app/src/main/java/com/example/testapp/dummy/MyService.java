package com.example.testapp.dummy;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import com.example.testapp.InvisibleVideoRecorder;

public class MyService extends Service {
    //creating a mediaplayer object
    InvisibleVideoRecorder recorder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}