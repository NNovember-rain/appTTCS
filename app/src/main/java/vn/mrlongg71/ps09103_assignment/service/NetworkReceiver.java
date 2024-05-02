package vn.mrlongg71.ps09103_assignment.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.developer.kalert.KAlertDialog;

import org.greenrobot.eventbus.EventBus;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;

public class NetworkReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            EventBus.getDefault().post(new EventConnect(true));
            Toasty.success(context,"Connected", Toasty.LENGTH_LONG).show();

        }else {
            EventBus.getDefault().post(new EventConnect(false));
            Toasty.error(context,"No Connected!!",Toasty.LENGTH_LONG).show();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.book)
                            .setContentTitle("Error")
                            .setContentText("No connected!!!");
            NotificationManager mNotificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(001, mBuilder.build());

        }

        }


    public static class EventConnect {
        boolean connect = false;

        public EventConnect(boolean connect) {
            this.connect = connect;
        }

        public boolean isConnect() {
            return connect;
        }
    }

}
