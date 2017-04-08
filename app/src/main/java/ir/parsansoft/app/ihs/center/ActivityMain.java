package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;

import ir.parsansoft.app.ihs.center.AllViews.CO_activity_main;
import ir.parsansoft.app.ihs.center.Database.User.Struct;
import ir.parsansoft.app.ihs.center.LoginDialog.LoginListener;


public class ActivityMain extends ActivityEnhanced {

    public static boolean isInFront;
    CO_activity_main formObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (G.setting.customerID == 0) {
            G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingLanguage.class));
            Animation.doAnimation(Animation.Animation_Types.FADE);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        G.currentActivity = this;
        formObjects = new CO_activity_main(this);
        translateForm();
        try {
            Utility.MyWiFiManager wm = new Utility.MyWiFiManager();
            wm.connectToMainAP();
        } catch (Exception e) {
            G.printStackTrace(e);
        }
        if (G.serverCommunication == null)
            G.serverCommunication = new ServiceServerCommunication();
        setSideBarVisiblity(false);
        changeMenuIconBySelect(0);
        //setHeaderText("Welcome");
        if (G.currentUser == null) {
            LoginDialog ldlg = new LoginDialog();
            ldlg.setLoginListener(new LoginListener() {
                @Override
                public boolean onLoginSuccess(Struct user) {
                    G.currentUser = user;
                    fo.txtLogin.setText(user.username);
                    return false;
                }

                @Override
                public boolean onLoginFailed() {

                    return true;
                }

                @Override
                public boolean onLoginCancel() {
                    return true;
                }
            });
            ldlg.setCancelable(false);
            ldlg.showDialog();
        }
        formObjects.laybtnDiveces.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySections.class));
                Animation.doAnimation(Animation.Animation_Types.FADE);
            }
        });

        formObjects.laybtnScenario.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivityScenario.class));
                Animation.doAnimation(Animation.Animation_Types.FADE);
            }
        });

        formObjects.laybtnSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (G.currentUser != null && G.currentUser.rol == 1) {
                    G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingLanguage.class));
                    Animation.doAnimation(Animation.Animation_Types.FADE);
                    finish();
                } else {
                    LoginDialog ldlg = new LoginDialog();
                    ldlg.setLoginListener(new LoginListener() {

                        @Override
                        public boolean onLoginSuccess(Struct user) {
                            if (user.rol == 1) {
                                G.currentUser = user;
                                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingLanguage.class));
                                Animation.doAnimation(Animation.Animation_Types.FADE);
                                finish();
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

        formObjects.laybtnFavorites.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivityFavorites.class));
                Animation.doAnimation(Animation.Animation_Types.FADE);
            }
        });

        formObjects.laybtnFavorites.setOnHoverListener(new OnHoverListener() {
            @Override
            public boolean onHover(View arg0, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER)
                    G.playHover();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        isInFront = true;

        G.nodeCommunication.removeAllUis();
        translateForm();
    }

    @Override
    protected void onPause() {
        super.onPause();

        isInFront = false;
    }

    @Override
    public void onBackPressed() {
        //         nothing to do here
        //         really...      
    }

    @Override
    public void translateForm() {
        super.translateForm();
        formObjects.txtDevices.setText(G.T.getSentence(151));
        formObjects.txtScenario.setText(G.T.getSentence(152));
        formObjects.txtSetting.setText(G.T.getSentence(154));
        formObjects.txtFavorites.setText(G.T.getSentence(153));
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        G.log("KEY Press : " + String.valueOf(event.getKeyCode()));
        return super.dispatchKeyEvent(event);
    }
}
