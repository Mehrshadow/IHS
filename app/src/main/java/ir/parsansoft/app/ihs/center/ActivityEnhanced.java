package ir.parsansoft.app.ihs.center;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

import ir.parsansoft.app.ihs.center.AllViews.CO_master_page_main;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.Database.User.Struct;
import ir.parsansoft.app.ihs.center.LoginDialog.LoginListener;
import ir.parsansoft.app.ihs.center.UI.OnUiChange;
import ir.parsansoft.app.ihs.center.components.AssetsManager;


public class ActivityEnhanced extends FragmentActivity {

    int uiListenerKey = 0;
    CO_master_page_main fo;
    LinearLayout lay;
    WindowManager.LayoutParams mLP;
    View mView;
    WindowManager mWindowManager;
    ViewGroup viewGroup;
    Help.Help_Type help_type;
    int lastLanguageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide soft Keyboard on first time and move layout up
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.setContentView(R.layout.master_page_main);
        lay = (LinearLayout) findViewById(R.id.layForms);
        fo = new CO_master_page_main(this);
        lastLanguageID = G.setting.languageID;

        addUiReferences();

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // DisableAndroidNotificationBarAndHide();
        fo.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                System.exit(0);
                //				Intent intent = new Intent(Intent.ACTION_MAIN);
                //				intent.addCategory(Intent.CATEGORY_HOME);
                //				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //				startActivity(intent);
            }
        });
        fo.imghelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showHelp();
            }
        });
        fo.laybtnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivityMain.class));
                Animation.doAnimation(Animation_Types.FADE);
                finish();
            }
        });
        fo.laybtnDevices.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySections.class));
                Animation.doAnimation(Animation_Types.FADE);
                finish();
            }
        });
        fo.laybtnScenario.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivityScenario.class));
                Animation.doAnimation(Animation_Types.FADE);
                finish();
            }
        });
        fo.laybtnFavorites.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivityFavorites.class));
                Animation.doAnimation(Animation_Types.FADE);
                finish();
            }
        });
        fo.laybtnSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (G.currentUser != null && G.currentUser.rol == 1) {
                    G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingLanguage.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                } else {
                    LoginDialog ldlg = new LoginDialog();
                    ldlg.setLoginListener(new LoginListener() {

                        @Override
                        public boolean onLoginSuccess(Struct user) {
                            if (user.rol == 1) {
                                G.currentUser = user;
                                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingLanguage.class));
                                Animation.doAnimation(Animation_Types.FADE);
                                return false;
                            } else {
                                new DialogClass(G.currentActivity).showOk(G.T.getSentence(216), G.T.getSentence(789));
                                return true;
                            }
                        }

                        @Override
                        public boolean onLoginFailed() {
                            return true;
                        }

                        @Override
                        public boolean onLoginCancel() {
                            return false;
                        }
                    });
                    ldlg.showDialog();
                }
            }
        });
        fo.txtLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LoginDialog ldlg = new LoginDialog();
                ldlg.setLoginListener(new LoginListener() {

                    @Override
                    public boolean onLoginSuccess(Struct user) {
                        G.currentUser = user;
                        fo.txtLogin.setText(user.username);
                        if (!ActivityMain.isInFront) {

                            startActivity(new Intent(G.currentActivity, ActivityMain.class));
                            Animation.doAnimation(Animation_Types.FADE);
                            finish();
                        }

                        return false;
                    }

                    @Override
                    public boolean onLoginFailed() {
                        return true;
                    }

                    @Override
                    public boolean onLoginCancel() {
                        return false;
                    }
                });
                ldlg.showDialog();
            }
        });

        fo.txtTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new DialogClass(G.currentActivity).showOk("Online Mobiles", G.mobileCommunication.getOnlineMobiles());
            }
        });
        fo.img_server_status.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.sand.airdroid");
                    startActivity(launchIntent);
                } catch (Exception e) {
                }
            }
        });
        fo.imgWeatherIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.estrongs.android.pop");
                    startActivity(launchIntent);
                } catch (Exception e) {
                }
            }
        });

        fo.imgAlarmIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("ir.parsansoft.launcher");
                //                startActivity(launchIntent);
                Database.Node.Struct[] dNodes = Database.Node.select("Status=0");
                String s = "";
                DialogClass dlg = new DialogClass(G.currentActivity);
                if (dNodes != null) {
                    s = G.T.getSentence(809);
                    for (int i = 0; i < dNodes.length; i++) {
                        s += "\n" + dNodes[i].getFullName();
                    }
                } else {
                    s = G.T.getSentence(810);
                }
                dlg.showOk(G.T.getSentence(811), s);
            }
        });

        fo.imgKeyboard.setOnClickListener(new OnClickListener() {
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
        fo.imgKeyboard.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                //                Intent intent = new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                //                startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings");
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void setContentView(int id) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(id, lay);

    }

    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
        G.currentClassName = getClass().getSimpleName();
        if (lastLanguageID != G.setting.languageID)
            translateForm();
        if (G.currentUser != null) {
            fo.txtLogin.setText(G.currentUser.username);
        }
        addUiReferences();
        disableAndroidNavigationBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    public void changeLoginUser(int userID) {

    }

    private void clearReferences() {
        // Remove Resources on activity close; 
        G.ui.removeOnUiChaged(uiListenerKey);
    }

    private void addUiReferences() {
        uiListenerKey = G.ui.addOnUiChaged(new OnUiChange() {
            @Override
            public void onTimeChanged(final String newTime) {
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        fo.txtTime.setText(newTime);
                    }
                });
            }

            @Override
            public void onAlarmChanged(final int alarmCount) {
                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        fo.txtAlarmCount.setText("" + alarmCount);
                    }
                });
            }

            @Override
            public void onWeatherChanged(final int weatherCode, final int temp) {
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            fo.txtTemperature.setText(temp + G.T.getSentence(125));
                            String iconName = G.DIR_ICONS_WEATHER + "/" + Database.WeatherTypes.select(weatherCode).icon;
                            G.log("Icon name:  " + iconName);
                            fo.imgWeatherIcon.setImageBitmap(AssetsManager.getBitmap(G.currentActivity, iconName));
                        } catch (Exception e) {
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
                            fo.img_server_status.setImageResource(R.drawable.lay_icon_main_server_indicator_disconnected);
                        } else {
                            fo.img_server_status.setImageResource(R.drawable.lay_icon_main_server_indicator_connected);
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
                                fo.imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level0);
                                break;
                            case 1:
                                fo.imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level1);
                                break;
                            case 2:
                                fo.imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level2);
                                break;
                            case 3:
                                fo.imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level3);
                                break;
                            case 4:
                                fo.imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level4);
                                break;
                            case -1:
                                fo.imgWifiIcon.setImageResource(R.drawable.lay_icon_main_wifi_indicator_dc);
                                break;
                        }
                    }
                });
            }


        }, uiListenerKey);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(0, 1, 1, "Setting").setIcon(drawable.ic_menu_preferences);
        return super.onCreateOptionsMenu(menu);
    }

    public void setSideBarVisiblity(boolean state) {
        if (state)
            fo.laySidebar.setVisibility(View.VISIBLE);
        else
            fo.laySidebar.setVisibility(View.GONE);
        G.log("Sidebar : " + state);
    }

    public void showHelp() {
        String className = this.getClass().getSimpleName();
        String langPath = "";
        switch (G.setting.languageID) {
            case 1:
                langPath = "en";
                break;
            case 2:
                langPath = "fa";
                break;
            case 3:
                langPath = "ar";
                break;
            case 4:
                langPath = "tu";
                break;

            default:
                break;
        }
        try {
            if (Arrays.asList(getAssets().list(G.DIR_HELP + "/" + langPath)).contains(className)) {
                Intent intent = new Intent(this, ActivityHelp.class);
                intent.putExtra("PAGE_NAME", className);
                intent.putExtra("LANG", langPath + "/");
                startActivity(intent);
            }
        } catch (IOException e) {
            G.printStackTrace(e);
        }
    }

    public void setHeaderText(String text) {
        fo.txtHeader.setText(text);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) { //  Hide Keyboard when click on any where
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, ActivityMain.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return true;
        //return (super.onOptionsItemSelected(menuItem));
    }

    public void changeMenuIconBySelect(int position) {
        switch (position) {
            case 0:
                //fo.laybtnFavorites.setBackgroundResource(R.drawable.dropdown_background);
                fo.lay_back_master.setBackgroundResource(R.drawable.lay_back_main);
                fo.lay_back_master.setOnClickListener(null);
                break;
            case 1:
                fo.laybtnFavorites.setBackgroundResource(R.drawable.lay_slidebar_favorites_press);
                fo.lay_back_master.setBackgroundResource(R.drawable.lay_back_favorites);
                fo.laybtnFavorites.setOnClickListener(null);
                break;
            case 2:
                fo.laybtnDevices.setBackgroundResource(R.drawable.lay_slidebar_devices_press);
                fo.lay_back_master.setBackgroundResource(R.drawable.lay_back_devices);
                fo.laybtnDevices.setOnClickListener(null);
                break;
            case 3:
                fo.laybtnScenario.setBackgroundResource(R.drawable.lay_slidebar_scenario_press);
                fo.lay_back_master.setBackgroundResource(R.drawable.lay_back_scenario);
                fo.laybtnScenario.setOnClickListener(null);
                break;
            case 4:
                fo.laybtnSettings.setBackgroundResource(R.drawable.lay_slidebar_setting_press);
                fo.lay_back_master.setBackgroundResource(R.drawable.lay_back_setting);
                fo.laybtnSettings.setOnClickListener(null);
                break;
        }
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
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        manager.addView(view, localLayoutParams);
    }

    private void disableAndroidNavigationBar() {
        // Way 1:
        //        View decorView = getWindow().getDecorView();
        //        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //   Way 2:
        //        View decorView = getWindow().getDecorView();
        //        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //        decorView.setSystemUiVisibility(uiOptions);

        // Way 3:
        View v = this.getWindow().getDecorView();
        v.setSystemUiVisibility(View.GONE);
    }

    public class customViewGroup extends ViewGroup {
        public customViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void hideKeyboard() {
        fo.imgKeyboard.setVisibility(View.INVISIBLE);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
            Toast.makeText(this, "Barcode Scanner detected. Please turn OFF Hardware/Physical keyboard to enable softkeyboard to function.", Toast.LENGTH_LONG).show();
        }
    }

    public void translateForm() {
        fo.txtHome.setText(G.T.getSentence(150));
        fo.txtDevices.setText(G.T.getSentence(151));
        fo.txtScenario.setText(G.T.getSentence(152));
        fo.txtFavorites.setText(G.T.getSentence(153));
        fo.txtSettings.setText(G.T.getSentence(154));
    }
}
