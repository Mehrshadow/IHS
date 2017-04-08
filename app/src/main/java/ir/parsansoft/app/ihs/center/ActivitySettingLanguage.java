package ir.parsansoft.app.ihs.center;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


public class ActivitySettingLanguage extends ActivitySetting implements OnClickListener {


    Button         btnApply;
    LinearLayout[] lay_languages;
    int            lang = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_setting_language);
        changeSlidebarImage(1);
        btnApply = (Button) findViewById(R.id.btnApply);

        lang = G.setting.languageID;
        lay_languages = new LinearLayout[4];
        lay_languages[0] = (LinearLayout) findViewById(R.id.lay_en);
        lay_languages[1] = (LinearLayout) findViewById(R.id.lay_fa);
        lay_languages[2] = (LinearLayout) findViewById(R.id.lay_ar);
        lay_languages[3] = (LinearLayout) findViewById(R.id.lay_tk);

        lay_languages[0].setOnClickListener(this);
        lay_languages[1].setOnClickListener(this);
        lay_languages[2].setOnClickListener(this);
        lay_languages[3].setOnClickListener(this);

        changeImage(G.setting.languageID);

        btnApply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.setting.languageID = lang;
                Database.Setting.edit(G.setting);
                G.T.changeLanguage(G.setting.languageID);
                changeImage(lang);
                FontsManager.changeFont();
                translateForm();
            }
        });
        translateForm();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_en:
                lang = 1;
                break;
            case R.id.lay_fa:
                lang = 2;
                break;
            case R.id.lay_ar:
                lang = 3;
                break;
            case R.id.lay_tk:
                lang = 4;
                break;
        }
        changeImage(lang);
    }

    public void changeImage(int lang) {
        changeImage();
        switch (lang) {
            case 1:
                lay_languages[0].setBackgroundResource(R.drawable.lay_language_en_press);
                break;
            case 2:
                lay_languages[1].setBackgroundResource(R.drawable.lay_language_fa_press);
                break;
            case 3:
                lay_languages[2].setBackgroundResource(R.drawable.lay_language_ar_press);
                break;
            case 4:
                lay_languages[3].setBackgroundResource(R.drawable.lay_language_tu_press);
                break;
        }
    }

    public void changeImage() {
        lay_languages[0].setBackgroundResource(R.drawable.lay_language_en);
        lay_languages[1].setBackgroundResource(R.drawable.lay_language_fa);
        lay_languages[2].setBackgroundResource(R.drawable.lay_language_ar);
        lay_languages[3].setBackgroundResource(R.drawable.lay_language_tk);
    }

    @Override
    public void translateForm() {
        super.translateForm();
        btnApply.setText(G.T.getSentence(123));
    }
}
