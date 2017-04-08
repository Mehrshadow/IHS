package ir.parsansoft.app.ihs.center;

import ir.parsansoft.app.ihs.center.UI.OnUiChange;
import ir.parsansoft.app.ihs.center.components.AssetsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityWizard extends FragmentActivity {

    LinearLayout    layForm, lay_back_master;
    public Button   btnNext, btnBack, btnCancel;
    TextView        txtTime, txtTemperature, txtAlarmCount;
    ImageView       imgWeatherIcon, img_server_status, imgWifiIcon, imgLogo, imgKeyboard;
    OnClickListener lBack;
    OnClickListener lNext;
    int             uiListenerKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.setContentView(R.layout.master_page_wizard);
        initializeView();
        //translateForm();
        //DisableAndroidNotificationBarAndHide();

        uiListenerKey = G.ui.addOnUiChaged(new OnUiChange() {
            @Override
            public void onTimeChanged(final String newTime) {
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        txtTime.setText(newTime);
                    }
                });
            }
            @Override
            public void onAlarmChanged(int alarmCount) {
                txtAlarmCount.setText("" + alarmCount);
            }
            @Override
            public void onWeatherChanged(final int weatherCode, final int temp) {
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            txtTemperature.setText(temp + G.T.getSentence(125));
                            String iconName = G.DIR_ICONS_WEATHER + "/" + Database.WeatherTypes.select(weatherCode).icon;
                            G.log("Icon name:  " + iconName);
                            imgWeatherIcon.setImageBitmap(AssetsManager.getBitmap(G.currentActivity, iconName));
                        }
                        catch (Exception e) {
                            G.printStackTrace(e);
                        }
                    }
                });
            }

            @Override
            public void onServerStatusChanged(final int status) {
                G.log("Server new status is :" + status);

                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        if (status == 0) {
                            img_server_status.setImageResource(R.drawable.lay_icon_main_server_indicator_disconnected);
                        }
                        else
                        {
                            img_server_status.setImageResource(R.drawable.lay_icon_main_server_indicator_connected);
                        }
                    }
                });
            }
            @Override
            public void onWifiSignalChanged(final int level) {
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        switch (level) {
                            case 0:
                                imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level0);
                                break;
                            case 1:
                                imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level1);
                                break;
                            case 2:
                                imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level2);
                                break;
                            case 3:
                                imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level3);
                                break;
                            case 4:
                                imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level4);
                                break;
                            case -1:
                                imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_dc);
                                break;
                        }
                    }
                });
            }
        }, uiListenerKey);

        imgLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.emapps.adbconnect");
                startActivity(launchIntent);
            }
        });
        img_server_status.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.sand.airdroid");
                startActivity(launchIntent);
            }
        });
        imgWeatherIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.estrongs.android.pop");
                startActivity(launchIntent);
            }
        });

        imgKeyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) {
                    CloseKeyBoard();
                } else {
                    OpenKeyBoard();
                }
            }
        });

    }
    public void hideBackButton() {
        btnBack.setVisibility(View.INVISIBLE);
    }
    public void hideNextButton() {
        btnNext.setVisibility(View.INVISIBLE);
    }
    protected void setOnBackListenner(OnClickListener ocl) {
        lBack = ocl;
    }
    protected void setOnNextListenner(OnClickListener ocl) {
        lNext = ocl;
    }
    public void initializeView() {
        lay_back_master = (LinearLayout) findViewById(R.id.lay_back_master);
        layForm = (LinearLayout) findViewById(R.id.layForms);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtAlarmCount = (TextView) findViewById(R.id.txtAlarmCount);
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        imgWeatherIcon = (ImageView) findViewById(R.id.imgWeatherIcon);
        img_server_status = (ImageView) findViewById(R.id.img_server_status);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgWifiIcon = (ImageView) findViewById(R.id.imgWifiIcon);
        imgKeyboard = (ImageView) findViewById(R.id.imgKeyboard);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        int margin = G.setting.displayMargin;
        layoutParams.setMargins(margin, margin, margin, margin);
        lay_back_master.setLayoutParams(layoutParams);
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lNext != null) {
                    lNext.onClick((View) btnNext);
                }
            }
        });
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lBack != null) {
                    lBack.onClick((View) btnBack);
                }
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(G.currentActivity, ActivityMain.class));
            }
        });
    }

    @Override
    public void setContentView(int id) {
        G.inflater.inflate(id, layForm);
    }

    private void DisableAndroidNotificationBarAndHide() {
        WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = getStatusBarHeight();
        customViewGroup view = new customViewGroup(this);
        view.setBackgroundColor(getResources().getColor(R.color.black));
        manager.addView(view, localLayoutParams);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public class customViewGroup extends ViewGroup {
        public customViewGroup(Context context) {
            super(context);
        }
        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {}
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) { //  Hide Keyboard when click on any where
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if ( !outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }

    public void translateForm() {
        btnNext.setText(G.T.getSentence(103));
        btnBack.setText(G.T.getSentence(104));
        btnCancel.setText(G.T.getSentence(102));
    }

    public void hideKeyboard() {
        imgKeyboard.setVisibility(View.INVISIBLE);
    }


    //For open keyboard
    public void OpenKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    //For close keyboard
    public void CloseKeyBoard() {
        getWindow().getDecorView().clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        G.ui.removeOnUiChaged(uiListenerKey);
    }
}
