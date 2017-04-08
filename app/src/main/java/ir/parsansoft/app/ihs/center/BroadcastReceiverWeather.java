package ir.parsansoft.app.ihs.center;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class BroadcastReceiverWeather extends BroadcastReceiver {

    private final static String REQ_WEATHER = "REQUEST_WEATHER";
    private Context context;

    public BroadcastReceiverWeather() {
        context = G.context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //You can do the processing here update the widget/remote views.
        //        G.log("The Alarm Manager has ran the ring !");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int requestCode = extras.getInt(REQ_WEATHER, -1);

            if (requestCode != -1) {

                new Weather().start();
            }
        }
    }

    public void setRepeatAlarm(int requestCode, long startTimeInMillis, long RepeatTimeInMillis) {
        G.log("Set Alarm Manager to ring at next " + startTimeInMillis + " milliseconds then repeat every " + RepeatTimeInMillis + "milliseconds .");
        G.log("Weather broadcast");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadcastReceiverWeather.class);
        intent.putExtra(REQ_WEATHER, requestCode);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, startTimeInMillis, RepeatTimeInMillis, pi);

    }
}
