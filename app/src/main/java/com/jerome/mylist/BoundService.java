package com.jerome.mylist;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class BoundService extends Service {
    private final IBinder binder = new ServiceBinder();
    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class ServiceBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }
    public void makeToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}