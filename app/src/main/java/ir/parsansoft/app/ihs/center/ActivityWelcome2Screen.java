package ir.parsansoft.app.ihs.center;

import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


public class ActivityWelcome2Screen extends ActivityWizard {


    Button btnPlus, btnMinus, btnApply;
    int    margin = G.setting.displayMargin;
    TextView txtMargin, txtResetDescription;
    boolean  p, m;
    boolean  isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_setting_screen);
        btnMinus = (Button) findViewById(R.id.btnPlus);
        btnPlus = (Button) findViewById(R.id.btnMinus);
        btnApply = (Button) findViewById(R.id.btnApply);
        txtMargin = (TextView) findViewById(R.id.txtMargin);
        txtResetDescription = (TextView) findViewById(R.id.txtResetDescription);
        txtMargin.setText("" + margin);
        btnPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                margin = (margin < 40 ? margin + 1 : 40);
                changeLayoutMargin();
                txtMargin.setText("" + margin);
            }
        });
        btnMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                margin = (margin - 1 > 0 ? margin - 1 : 0);
                changeLayoutMargin();
                txtMargin.setText("" + margin);
            }
        });
        btnApply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                G.setting.displayMargin = margin;
                Database.Setting.edit(G.setting);
            }
        });
        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    startActivity(new Intent(ActivityWelcome2Screen.this, ActivityWelcome1Language.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        setOnNextListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    startActivity(new Intent(ActivityWelcome2Screen.this, ActivityWelcome3Network.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });
        //changeSlidebarImage(2);
        translateForm();

        btnPlus.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        p = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (p) {
                                        Thread.sleep(200);
                                        margin = (margin < 40 ? margin + 1 : 40);
                                        G.HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                changeLayoutMargin();
                                                txtMargin.setText("" + margin);
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
                        p = false;
                        break;
                }
                return false;
            }
        });
        btnMinus.setOnTouchListener(new OnTouchListener() {
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
                                        Thread.sleep(200);
                                        margin = (margin - 1 > 0 ? margin - 1 : 0);
                                        G.HANDLER.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                changeLayoutMargin();
                                                txtMargin.setText("" + margin);
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
    }

    private void changeLayoutMargin() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(margin, margin, margin, margin);
        lay_back_master.setLayoutParams(layoutParams);
    }
    @Override
    public void translateForm() {
        super.translateForm();
        btnApply.setText(G.T.getSentence(123));
        txtResetDescription.setText(G.T.getSentence(805));
    }
}
