package ir.parsansoft.app.ihs.center;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        Intent myIntent = new Intent(context, ActivityMain.class);
        //		myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(myIntent);
        }
        catch (Exception e) {}
    }

}
