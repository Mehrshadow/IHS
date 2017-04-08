package ir.parsansoft.app.ihs.center;

import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ActivityWelcome1Language extends ActivityWizard implements OnClickListener {

    boolean isBusy = false;
    LinearLayout layEnglish, layFarsi, layArabic, layTurk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_welcome_language);
        initialize();
        changeImage(G.setting.languageID);
        translateForm();
    }

    public void initialize() {

        layEnglish = (LinearLayout) findViewById(R.id.lay_en);
        layFarsi = (LinearLayout) findViewById(R.id.lay_fa);
        layArabic = (LinearLayout) findViewById(R.id.lay_ar);
        layTurk = (LinearLayout) findViewById(R.id.lay_tk);

        layEnglish.setOnClickListener(ActivityWelcome1Language.this);
        layFarsi.setOnClickListener(ActivityWelcome1Language.this);
        layArabic.setOnClickListener(ActivityWelcome1Language.this);
        layTurk.setOnClickListener(ActivityWelcome1Language.this);

        G.currentUser = Database.User.select(1);

        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
        setOnNextListenner(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !isBusy) {
                    isBusy = true;
                    Database.Setting.edit(G.setting);
                    G.T.changeLanguage(G.setting.languageID);
                    startActivity(new Intent(ActivityWelcome1Language.this, ActivityWelcome2Screen.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });
        hideBackButton();
    }
    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_en:
                G.setting.languageID = 1;
                break;
            case R.id.lay_fa:
                G.setting.languageID = 2;
                break;
            case R.id.lay_ar:
                G.setting.languageID = 3;
                break;
            case R.id.lay_tk:
                G.setting.languageID = 4;
                break;
        }
        FontsManager.changeFont();
        changeImage(G.setting.languageID);
        Utility.changeKeyboardLanguages();
        G.T.changeLanguage(G.setting.languageID);
        translateForm();
    }

    public void changeImage() {
        layEnglish.setBackgroundResource(R.drawable.lay_language_en);
        layFarsi.setBackgroundResource(R.drawable.lay_language_fa);
        layArabic.setBackgroundResource(R.drawable.lay_language_ar);
        layTurk.setBackgroundResource(R.drawable.lay_language_tk);
    }

    public void changeImage(int lang) {
        changeImage();
        switch (lang) {
            case 1:
                layEnglish.setBackgroundResource(R.drawable.lay_language_en_press);
                break;
            case 2:
                layFarsi.setBackgroundResource(R.drawable.lay_language_fa_press);
                break;
            case 3:
                layArabic.setBackgroundResource(R.drawable.lay_language_ar_press);
                break;
            case 4:
                layTurk.setBackgroundResource(R.drawable.lay_language_tu_press);
                break;
        }
    }

    @Override
    public void translateForm() {
        super.translateForm();
    }

    @Override
    public void onBackPressed() {
        // nothing to do here
        // really
    }
}
