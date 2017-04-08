package ir.parsansoft.app.ihs.center.components;

import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import java.util.Calendar;
import java.util.Date;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimePickerComponent extends LinearLayout {

    Calendar cal;
    boolean  b = true;
    boolean  m = true;
    TextView txtHour, txtMin;

    public TimePickerComponent(Context context) {
        super(context);
        initialize();
    }

    public TimePickerComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        G.inflater.inflate(R.layout.c_time_picker, this);
        txtHour = (TextView) findViewById(R.id.txtHour);
        txtMin = (TextView) findViewById(R.id.txtMin);
        Button p_hour = (Button) findViewById(R.id.p_hour);
        Button p_min = (Button) findViewById(R.id.p_min);
        Button m_hour = (Button) findViewById(R.id.m_hour);
        Button m_min = (Button) findViewById(R.id.m_min);
        Date dt = new Date();
        cal = Calendar.getInstance();
        cal.setTime(dt);
        txtHour.setText("" + pad(cal.get(Calendar.HOUR_OF_DAY)));
        txtMin.setText("" + pad(cal.get(Calendar.MINUTE)));

        p_hour.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        b = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (b) {
                                        Thread.sleep(150);
                                        cal.add(Calendar.HOUR, 1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txtHour.setText("" + pad(cal.get(Calendar.HOUR_OF_DAY)));
                                                txtMin.setText("" + pad(cal.get(Calendar.MINUTE)));
                                            }
                                        });
                                    }
                                }
                                catch (InterruptedException e1) {
                                    G.printStackTrace(e1);
                                }
                            }
                        }).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        b = false;
                        break;
                }
                return false;
            }
        });

        p_hour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.HOUR, 1);
                txtHour.setText("" + pad(cal.get(Calendar.HOUR_OF_DAY)));
                txtMin.setText("" + pad(cal.get(Calendar.MINUTE)));
            }
        });
        m_hour.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        b = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (b) {
                                        Thread.sleep(150);
                                        cal.add(Calendar.HOUR, -1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txtHour.setText("" + pad(cal.get(Calendar.HOUR_OF_DAY)));
                                                txtMin.setText("" + pad(cal.get(Calendar.MINUTE)));
                                            }
                                        });
                                    }
                                }
                                catch (InterruptedException e1) {
                                    G.printStackTrace(e1);
                                }
                            }
                        }).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        b = false;
                        break;
                }
                return false;
            }
        });

        m_hour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.HOUR, -1);
                txtHour.setText("" + pad(cal.get(Calendar.HOUR_OF_DAY)));
                txtMin.setText("" + pad(cal.get(Calendar.MINUTE)));
            }
        });
        p_min.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        m = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (m) {
                                        Thread.sleep(150);
                                        cal.add(Calendar.MINUTE, 1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txtHour.setText("" + pad(cal.get(Calendar.HOUR_OF_DAY)));
                                                txtMin.setText("" + pad(cal.get(Calendar.MINUTE)));
                                            }
                                        });
                                    }
                                }
                                catch (InterruptedException e1) {
                                    G.printStackTrace(e1);
                                }
                            }
                        }).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        m = false;
                        break;
                }
                return false;
            }
        });

        p_min.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MINUTE, 1);
                txtHour.setText("" + pad(cal.get(Calendar.HOUR_OF_DAY)));
                txtMin.setText("" + pad(cal.get(Calendar.MINUTE)));
            }
        });
        m_min.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        m = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (m) {
                                        Thread.sleep(150);
                                        cal.add(Calendar.MINUTE, -1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txtHour.setText("" + pad(cal.get(Calendar.HOUR_OF_DAY)));
                                                txtMin.setText("" + pad(cal.get(Calendar.MINUTE)));
                                            }
                                        });
                                    }
                                }
                                catch (InterruptedException e1) {
                                    G.printStackTrace(e1);
                                }
                            }
                        }).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        m = false;
                        break;
                }
                return false;
            }
        });

        m_min.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MINUTE, -1);
                txtHour.setText("" + pad(cal.get(Calendar.HOUR_OF_DAY)));
                txtMin.setText("" + pad(cal.get(Calendar.MINUTE)));
            }
        });
    }

    public Calendar getCalender() {
        return cal;
    }
    public void setCalender(int h, int m) {
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
    }
    public void setCurrentHour(int h) {
        cal.set(Calendar.HOUR_OF_DAY, h);
        txtHour.setText(pad(cal.get(Calendar.HOUR_OF_DAY)));
    }
    public void setCurrentMinute(int m) {
        cal.set(Calendar.MINUTE, m);
        txtMin.setText(pad(cal.get(Calendar.MINUTE)));
    }
    public String getCurrentHour() {
        return pad(cal.get(Calendar.HOUR_OF_DAY));
    }
    public String getCurrentMinute() {
        return pad(cal.get(Calendar.MINUTE));
    }

    private static String pad(int c)
    {
        return c >= 10 ? "" + c : "0" + c;
    }
}
