package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.components.AssetsManager;

public class ActivityWelcome6Weather extends ActivityWizard implements OnClickListener {

    boolean isBusy = false;
    private TextView lbl_weatherAPIKey, lbl_cityName, tv_cityName, lbl_currentWeather, tv_currentWeather, lbl_currentTemperature, tv_currentTemperature;
    private EditText et_weatherAPIKey;
    private Button btn_getWeather, btnClose;
    private LinearLayout weatherLayout, mainLayout1, mainLayout2;
    private ImageView img, btnWebsite;
    private WebView webView;
    private RelativeLayout webviewLayout;
    private int margin = 16;
    private static final int CITY = 0, WEATHER = 1, TEMPERATURE = 2, WEATHERCODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            super.setContentView(R.layout.f_setting_weather);
        else
            super.setContentView(R.layout.f_setting_weather_rtl);

        initViews();

        lbl_weatherAPIKey.setText(G.T.getSentence(836));
        btn_getWeather.setText(G.T.getSentence(837));
        lbl_currentWeather.setText(G.T.getSentence(838));
        lbl_currentTemperature.setText(G.T.getSentence(839));
        lbl_cityName.setText(G.T.getSentence(841));

        translateForm();

        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBusy) {
                    isBusy = true;
                    startActivity(new Intent(ActivityWelcome6Weather.this, ActivityWelcome5DateandTime.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        setOnNextListenner(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBusy) {
                    startActivity(new Intent(ActivityWelcome6Weather.this, ActivityWelcome7Specifications.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });
    }

    private void initViews() {
        lbl_weatherAPIKey = (TextView) findViewById(R.id.lbl_apikey);
        lbl_currentWeather = (TextView) findViewById(R.id.lbl_current_weather);
        lbl_currentTemperature = (TextView) findViewById(R.id.lbl_current_temperature);
        lbl_cityName = (TextView) findViewById(R.id.lbl_cityname);

        tv_currentWeather = (TextView) findViewById(R.id.tv_current_weather);
        tv_currentTemperature = (TextView) findViewById(R.id.tv_current_temperature);
        tv_cityName = (TextView) findViewById(R.id.tv_cityname);

        et_weatherAPIKey = (EditText) findViewById(R.id.et_apikey);

        weatherLayout = (LinearLayout) findViewById(R.id.lay_weather);
        webviewLayout = (RelativeLayout) findViewById(R.id.layAccuweather);
        mainLayout1 = (LinearLayout) findViewById(R.id.layOptions);
        mainLayout2 = (LinearLayout) findViewById(R.id.layOptions2);

        img = (ImageView) findViewById(R.id.ic_weather);
        btnWebsite = (ImageView) findViewById(R.id.btnWebsite);
        btnWebsite.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.webViewAccuweather);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        btn_getWeather = (Button) findViewById(R.id.btn_submit);
        btnClose = (Button) findViewById(R.id.btnClose);
        btn_getWeather.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        btnClose.setText(G.T.getSentence(120));
    }



    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }

    private void addAPIKeyToDB() {
        G.setting.weatherAPIKey = et_weatherAPIKey.getText().toString();
        Database.Setting.edit(G.setting);
    }

    private void doSubmit() {
        addAPIKeyToDB();

        ArrayList<Object> condition = new Weather().getConditionData();
        if (condition == null) {
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(759), G.T.getSentence(842));
            return;
        }
        weatherLayout.setVisibility(View.VISIBLE);
        G.log("WEATHERCODE:  " + condition.get(WEATHERCODE).toString());
        String iconName = G.DIR_ICONS_WEATHER + "/" + Database.WeatherTypes.select((int) condition.get(WEATHERCODE)).icon;
        G.log("Icon name:  " + iconName);
        img.setImageBitmap(AssetsManager.getBitmap(G.currentActivity, iconName));
        tv_cityName.setText(condition.get(CITY).toString());
        tv_currentWeather.setText(condition.get(WEATHER).toString());
        tv_currentTemperature.setText(condition.get(TEMPERATURE).toString());
    }

    private void loadWebView() {
        webviewLayout.setVisibility(View.VISIBLE);
        loadLayout();
        webView.loadUrl("http://developer.accuweather.com/");
    }

    private void unloadWebView() {
        unLoadLayout();

        webviewLayout.setVisibility(View.GONE);
    }

    private void loadLayout() {
        LinearLayout.LayoutParams webViewLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        LinearLayout.LayoutParams Main1LayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0);
        LinearLayout.LayoutParams Main2LayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0);
        webViewLayoutParams.setMargins(margin, margin, margin, margin);
        webviewLayout.setLayoutParams(webViewLayoutParams);
        mainLayout1.setLayoutParams(Main1LayoutParams);
        mainLayout2.setLayoutParams(Main2LayoutParams);
    }

    private void unLoadLayout() {
        LinearLayout.LayoutParams webViewLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0);
        LinearLayout.LayoutParams Main1LayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        LinearLayout.LayoutParams Main2LayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        webviewLayout.setLayoutParams(webViewLayoutParams);
        mainLayout1.setLayoutParams(Main1LayoutParams);
        mainLayout2.setLayoutParams(Main2LayoutParams);
        Main2LayoutParams.setMargins(margin, margin, margin, margin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWebsite:
                loadWebView();
                break;
            case R.id.btn_submit:
                doSubmit();
                break;
            case R.id.btnClose:
                unloadWebView();
                break;
        }
    }
}
