package ir.parsansoft.app.ihs.center;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

import ir.parsansoft.app.ihs.center.components.DatePickerComponent;
import ir.parsansoft.app.ihs.center.components.TimePickerComponent;


public class ActivitySettingDateTime extends ActivitySetting {
    Button btnApply;
    TimePickerComponent time;
    DatePickerComponent date;
    TextView txtTime;
    TextView txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_setting_datetime);
        changeSlidebarImage(2);
        txtTime = (TextView) findViewById(R.id.txtlblTime);
        txtDate = (TextView) findViewById(R.id.txtDate);
        time = (TimePickerComponent) findViewById(R.id.timePicker);
        date = (DatePickerComponent) findViewById(R.id.datePicker);
        btnApply = (Button) findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Calendar calTime = time.getCalender();
                Calendar calDate = date.getCalender();
                //متد calDate.get(Calendar.MONTH) یک ماه کمتر برمیگرداند. بنابراین +1 شود تا ماه درست تنظیم شود
                changeSystemTime(String.valueOf(calDate.get(Calendar.YEAR)), pad(calDate.get(Calendar.MONTH) + 1), pad(calDate.get(Calendar.DAY_OF_MONTH)), pad(calTime.get(Calendar.HOUR_OF_DAY)), String.valueOf(calTime.get(Calendar.MINUTE)), "00");
            }
        });
        translateForm();
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
        } catch (InterruptedException e) {
            G.printStackTrace(e);
        } catch (IOException e) {
            G.printStackTrace(e);
        }
    }

    private static String pad(int c) {
        return c >= 10 ? "" + c : "0" + c;
    }

    @Override
    public void translateForm() {
        super.translateForm();
        btnApply.setText(G.T.getSentence(123));
        txtTime.setText(G.T.getSentence(828));
        txtDate.setText(G.T.getSentence(829));
    }
}
