package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.components.DatePickerComponent;
import ir.parsansoft.app.ihs.center.components.TimePickerComponent;

public class ActivityWelcome5DateandTime extends ActivityWizard {

    boolean             isBusy = false;
    TimePickerComponent time;
    DatePickerComponent date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.f_welcome_datetime);
        time = (TimePickerComponent) findViewById(R.id.timePicker);
        date = (DatePickerComponent) findViewById(R.id.datePicker);
        TextView txtTime = (TextView) findViewById(R.id.txtlblTime);
        TextView txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime.setText(G.T.getSentence(828));
        txtDate.setText(G.T.getSentence(829));
        translateForm();

        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isBusy) {
                    isBusy = true;
                    startActivity(new Intent(ActivityWelcome5DateandTime.this, ActivityWelcome4Location.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        setOnNextListenner(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isBusy) {
                    isBusy = true;
                    final Calendar calTime = time.getCalender();
                    final Calendar calDate = date.getCalender();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            changeSystemTime(String.valueOf(calDate.get(Calendar.YEAR)), pad(calDate.get(Calendar.MONTH) + 1), pad(calDate.get(Calendar.DAY_OF_MONTH)), pad(calTime.get(Calendar.HOUR_OF_DAY)), String.valueOf(calTime.get(Calendar.MINUTE)), "00");
                        }
                    }).start();
                    startActivity(new Intent(ActivityWelcome5DateandTime.this, ActivityWelcome6Weather.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }
    private void changeSystemTime(String year, String month, String day, String hour, String minute, String second) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String command = "date -s " + year + month + day + "." + hour + minute + second + "\n";
            os.writeBytes(command);
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        }
        catch (InterruptedException e) {
            G.printStackTrace(e);
        }
        catch (IOException e) {
            G.printStackTrace(e);
        }
    }

    private static String pad(int c)
    {
        return c >= 10 ? "" + c : "0" + c;
    }
}
