package ir.parsansoft.app.ihs.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import ir.parsansoft.app.ihs.center.AllViews.CO_master_setting;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;

public class ActivitySetting extends ActivityEnhanced {

    CO_master_setting fo_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.setContentView(R.layout.master_setting);
        fo_setting = new CO_master_setting(this);
        changeMenuIconBySelect(4);
        setSideBarVisiblity(false);

        fo_setting.lay_btn_network.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingNetwork.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        fo_setting.lay_btn_mobile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingMobile.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        /******************************** Jahanbin ****************************/
        fo_setting.lay_btn_weather.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingWeather.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        /******************************** Jahanbin ****************************/

        fo_setting.lay_btn_location.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingLocation.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });

        fo_setting.lay_btn_users.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingUser.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });

        fo_setting.lay_btn_language.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingLanguage.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        fo_setting.lay_btn_users.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingUser.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        fo_setting.lay_btn_sms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingSMS.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        fo_setting.lay_btn_reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingReset.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });

        fo_setting.lay_btn_log.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingLOG.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        fo_setting.lay_btn_datetime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingDateTime.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        fo_setting.lay_btn_display.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingScreen.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });


        fo_setting.lay_btn_update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingUpdate.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        fo_setting.btnReturn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }
    @Override
    public void setContentView(int id) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(id, fo_setting.laySettings);
    }



    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;

    }

    private void clearReferences() {
        // TODO : Remove Resources on activity close; 
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo_setting.txt_btn_language.setText(G.T.getSentence(751));
        fo_setting.txt_btn_datetime.setText(G.T.getSentence(752));
        fo_setting.txt_btn_location.setText(G.T.getSentence(753));
        fo_setting.txt_btn_mobile.setText(G.T.getSentence(754));
        fo_setting.txt_btn_sms.setText(G.T.getSentence(755));
        fo_setting.txt_btn_users.setText(G.T.getSentence(756));
        fo_setting.txt_btn_log.setText(G.T.getSentence(757));
        fo_setting.txt_btn_support.setText(G.T.getSentence(758));
        fo_setting.txt_btn_warranty.setText(G.T.getSentence(759));
        fo_setting.txt_btn_network.setText(G.T.getSentence(760));
        fo_setting.txt_btn_display.setText(G.T.getSentence(761));
        fo_setting.txt_btn_update.setText(G.T.getSentence(818));
        fo_setting.txt_btn_reset.setText(G.T.getSentence(762));
        fo_setting.btnReturn.setText(G.T.getSentence(108));
        /****************** Jahanbin **********************/
        fo_setting.txt_btn_weather.setText(G.T.getSentence(843));
        /****************** Jahanbin **********************/
    }

    private void changeSlidebarImage() {
        fo_setting.lay_btn_language.setBackgroundResource(R.drawable.lay_setting_icon_language_selector);
        fo_setting.lay_btn_datetime.setBackgroundResource(R.drawable.lay_setting_icon_datetime_selector);
        fo_setting.lay_btn_location.setBackgroundResource(R.drawable.lay_setting_icon_location_selector);
        fo_setting.lay_btn_mobile.setBackgroundResource(R.drawable.lay_setting_icon_mobile_selector);
        fo_setting.lay_btn_sms.setBackgroundResource(R.drawable.lay_setting_icon_sms_selector);
        fo_setting.lay_btn_users.setBackgroundResource(R.drawable.lay_setting_icon_users_selector);
        fo_setting.lay_btn_log.setBackgroundResource(R.drawable.lay_setting_icon_log_selector);
        fo_setting.lay_btn_warranty.setBackgroundResource(R.drawable.lay_setting_icon_warranty_selector);
        fo_setting.lay_btn_network.setBackgroundResource(R.drawable.lay_setting_icon_network_selector);
        fo_setting.lay_btn_display.setBackgroundResource(R.drawable.lay_setting_icon_display_selector);
        fo_setting.lay_btn_reset.setBackgroundResource(R.drawable.lay_setting_icon_reset_selector);
        /************************* jahanbin *****************************/
        fo_setting.lay_btn_weather.setBackgroundResource(R.drawable.lay_setting_icon_weather_selector);
        /************************* jahanbin *****************************/
    }
    public void changeSlidebarImage(int position) {
        changeSlidebarImage();
        switch (position) {
            case 1:
                fo_setting.lay_btn_language.setBackgroundResource(R.drawable.lay_setting_icon_language_press);
                fo_setting.lay_btn_language.setOnClickListener(null);
                break;
            case 2:
                fo_setting.lay_btn_datetime.setBackgroundResource(R.drawable.lay_setting_icon_datetime_press);
                fo_setting.lay_btn_datetime.setOnClickListener(null);
                break;
            case 3:
                fo_setting.lay_btn_location.setBackgroundResource(R.drawable.lay_setting_icon_location_press);
                fo_setting.lay_btn_location.setOnClickListener(null);
                break;
            /************************* jahanbin *****************************/
            case 4:
                fo_setting.lay_btn_weather.setBackgroundResource(R.drawable.lay_setting_icon_weather_press);
                fo_setting.lay_btn_weather.setOnClickListener(null);
                break;
            /************************* jahanbin *****************************/
            case 5:
                fo_setting.lay_btn_mobile.setBackgroundResource(R.drawable.lay_setting_icon_mobile_press);
                fo_setting.lay_btn_mobile.setOnClickListener(null);
                break;
            case 6:
                fo_setting.lay_btn_sms.setBackgroundResource(R.drawable.lay_setting_icon_sms_press);
                fo_setting.lay_btn_sms.setOnClickListener(null);
                break;
            case 7:
                fo_setting.lay_btn_users.setBackgroundResource(R.drawable.lay_setting_icon_users_press);
                fo_setting.lay_btn_users.setOnClickListener(null);
                break;
            case 8:
                fo_setting.lay_btn_log.setBackgroundResource(R.drawable.lay_setting_icon_log_press);
                fo_setting.lay_btn_log.setOnClickListener(null);
                break;
            case 9:
                fo_setting.lay_btn_support.setBackgroundResource(R.drawable.lay_setting_icon_support_press);
                fo_setting.lay_btn_support.setOnClickListener(null);
                break;
            case 10:
                fo_setting.lay_btn_warranty.setBackgroundResource(R.drawable.lay_setting_icon_warranty_press);
                fo_setting.lay_btn_warranty.setOnClickListener(null);
                break;
            case 11:
                fo_setting.lay_btn_network.setBackgroundResource(R.drawable.lay_setting_icon_network_press);
                fo_setting.lay_btn_network.setOnClickListener(null);
                break;
            case 12:
                fo_setting.lay_btn_display.setBackgroundResource(R.drawable.lay_setting_icon_display_press);
                fo_setting.lay_btn_display.setOnClickListener(null);
                break;
            case 13:
                fo_setting.lay_btn_update.setBackgroundResource(R.drawable.lay_setting_icon_update_press);
                fo_setting.lay_btn_update.setOnClickListener(null);
                break;
            case 14:
                fo_setting.lay_btn_reset.setBackgroundResource(R.drawable.lay_setting_icon_reset_press);
                fo_setting.lay_btn_reset.setOnClickListener(null);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        //        final DialogClass dlg = new DialogClass(G.currentActivity);
        //        dlg.setOnYesNoListener(new YesNoListener(
        //                ) {
        //
        //                    @Override
        //                    public void yesClick() {
        //                        G.currentActivity.startActivity(new Intent(G.currentActivity, ActivityMain.class));
        //                        Animation.doAnimation(Animation.Animation_Types.FADE);
        //                    }
        //
        //                    @Override
        //                    public void noClick() {
        //                        dlg.dissmiss();
        //                    }
        //                });
        //        dlg.showYesNo("Message", "Exit???");

        G.currentActivity.startActivity(new Intent(G.currentActivity, ActivityMain.class));
        Animation.doAnimation(Animation_Types.FADE);
        finish();
    }
}
