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

public class DatePickerComponent extends LinearLayout {

    Calendar cal;
    boolean  m = true;
    boolean  y = true;
    boolean  d = true;


    public DatePickerComponent(Context context) {
        super(context);
        initialize();
    }

    public DatePickerComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        G.inflater.inflate(R.layout.c_date_picker, this);
        final TextView txt_day = (TextView) findViewById(R.id.txt_day);
        final TextView txt_month = (TextView) findViewById(R.id.txt_month);
        final TextView txt_year = (TextView) findViewById(R.id.txt_year);
        final Button p_day = (Button) findViewById(R.id.p_day);
        Button p_month = (Button) findViewById(R.id.p_month);
        Button p_year = (Button) findViewById(R.id.p_year);
        Button m_day = (Button) findViewById(R.id.m_day);
        Button m_month = (Button) findViewById(R.id.m_month);
        Button m_year = (Button) findViewById(R.id.m_year);
        Date dt = new Date();
        cal = Calendar.getInstance();
        cal.setTime(dt);
        txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
        txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
        txt_year.setText("" + cal.get(Calendar.YEAR));
        p_day.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        d = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (d) {
                                        Thread.sleep(150);
                                        cal.add(Calendar.DATE, 1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                                                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                                                txt_year.setText("" + cal.get(Calendar.YEAR));
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
                        d = false;
                        break;
                }
                return false;
            }
        });
        p_day.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, 1);
                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                txt_year.setText("" + cal.get(Calendar.YEAR));
            }
        });
        m_day.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        d = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (d) {
                                        Thread.sleep(150);
                                        cal.add(Calendar.DATE, -1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                                                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                                                txt_year.setText("" + cal.get(Calendar.YEAR));
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
                        d = false;
                        break;
                }
                return false;
            }
        });
        m_day.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, -1);
                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                txt_year.setText("" + cal.get(Calendar.YEAR));
            }
        });
        p_month.setOnTouchListener(new OnTouchListener() {
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
                                        cal.add(Calendar.MONTH, 1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                                                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                                                txt_year.setText("" + cal.get(Calendar.YEAR));
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
        p_month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                txt_year.setText("" + cal.get(Calendar.YEAR));
            }
        });
        m_month.setOnTouchListener(new OnTouchListener() {
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
                                        cal.add(Calendar.MONTH, -1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                                                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                                                txt_year.setText("" + cal.get(Calendar.YEAR));
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
        m_month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                txt_year.setText("" + cal.get(Calendar.YEAR));
            }
        });
        p_year.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (y) {
                                        Thread.sleep(150);
                                        cal.add(Calendar.YEAR, 1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                                                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                                                txt_year.setText("" + cal.get(Calendar.YEAR));
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
                        y = false;
                        break;
                }
                return false;
            }
        });
        p_year.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.YEAR, 1);
                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                txt_year.setText("" + cal.get(Calendar.YEAR));
            }
        });
        m_year.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (y) {
                                        Thread.sleep(150);
                                        cal.add(Calendar.YEAR, -1);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                                                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                                                txt_year.setText("" + cal.get(Calendar.YEAR));
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
                        y = false;
                        break;
                }
                return false;
            }
        });
        m_year.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.YEAR, -1);
                txt_day.setText("" + pad(cal.get(Calendar.DAY_OF_MONTH)));
                txt_month.setText(G.T.getSentence(cal.get(Calendar.MONTH) + 520));
                txt_year.setText("" + cal.get(Calendar.YEAR));
            }
        });
    }
    public Calendar getCalender() {
        return cal;
    }
    private static String pad(int c)
    {
        return c >= 10 ? "" + c : "0" + c;
    }
}
