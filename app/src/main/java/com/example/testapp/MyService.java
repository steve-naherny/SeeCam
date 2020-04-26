package com.example.testapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.KeyEvent;

public class MyService extends Service {
    InvisibleVideoRecorder recorder;
    IntentFilter intent1 = new IntentFilter();
    RemoteControlReceiver volumeReceiver = new RemoteControlReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(volumeReceiver, intent1);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(volumeReceiver);
        super.onDestroy();
    }

    public class RemoteControlReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction()))
            {
                KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (KeyEvent.KEYCODE_VOLUME_UP == event.getKeyCode())
                {
                        if(!recorder.isRunning) {
                            recorder.start();
                        }
                }
                else if(KeyEvent.KEYCODE_VOLUME_DOWN == event.getKeyCode()) {
                    if(recorder.isRunning) {
                        recorder.stop();
                    }
                }
            }
        }
    }
}