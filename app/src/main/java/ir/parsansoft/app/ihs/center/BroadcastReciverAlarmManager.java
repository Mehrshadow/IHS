package ir.parsansoft.app.ihs.center;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class BroadcastReciverAlarmManager extends BroadcastReceiver {

    private final static String DES_TIME = "DESCRIPTION";
    private final static String REQ_TIME = "REQUEST_CODE";
    private Context context;

    public BroadcastReciverAlarmManager() {
        context = G.context;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        //You can do the processing here update the widget/remote views.
        //        G.log("The Alarm Manager has ran the ring !");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int requestCode = extras.getInt(REQ_TIME, -1);

            String description = extras.getString(DES_TIME, "");
            //            G.log("requestCode : " + requestCode);
            //            G.log("description : " + description);
            G.scenarioBP.runByTime(requestCode);
        }
    }

    public void SetRepeatAlarm(int requestCode, String description, long startTimeInMillis, long RepeatTimeInMillis) {
        G.log("Set Alarm Manager to ring at next " + startTimeInMillis + " milliseconds then repeat every " + RepeatTimeInMillis + "milliseconds .");

        //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);
        G.log("Milliseconds : " + sdf.format(resultdate));
        //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadcastReciverAlarmManager.class);
        intent.putExtra(DES_TIME, description);
        intent.putExtra(REQ_TIME, requestCode);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, startTimeInMillis, RepeatTimeInMillis, pi);

    }

    public void CancelAlarm(int requestCode) {
        Intent intent = new Intent(context, BroadcastReciverAlarmManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(int requestCode, String description, long RepeatTimeInMillis) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadcastReciverAlarmManager.class);
        intent.putExtra(DES_TIME, description);
        intent.putExtra(REQ_TIME, requestCode);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, RepeatTimeInMillis, pi);
    }
}
