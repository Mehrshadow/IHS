package ir.parsansoft.app.ihs.center;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_setting_location;


public class ActivitySettingLocation extends ActivitySetting implements View.OnClickListener {

    private double longitute, latitude;
    private CO_f_setting_location fo;
    private String adr = "";
    private JSONObject locationObject;
    private boolean isLocationFound = false;
    DialogClass dlgc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_setting_location);
        else
            setContentView(R.layout.f_setting_location_rtl);
        changeSlidebarImage(3);
        fo = new CO_f_setting_location(this);

        fo.btnSet.setOnClickListener(this);
        fo.btnSearch.setOnClickListener(this);

        translateForm();
        initializeForm();
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.btnSet.setText(G.T.getSentence(123));
        fo.textView1.setText(G.T.getSentence(572));
        fo.btnSearch.setText(G.T.getSentence(847));
    }

    private void initializeForm() {

        int radius = 500;
        int enteringMode = 1; // 1= Enter   2= Exit
        String mobileNumber = "";

        if (G.setting.gPSLat == 0 || G.setting.gPSLon == 0) {
            G.setting.gPSLat = 35.695643;
            G.setting.gPSLon = 51.423311;
        }
        radius = 1;

        fo.edtLat.setText("" + G.setting.gPSLat);
        fo.edtLong.setText("" + G.setting.gPSLon);
        loadMap();
    }

    private void loadMap() {
        G.log("Url Loading ...");
        String MapURL = G.URL_Map_Webservice;
        fo.webMap.setWebViewClient(new WebViewClient());
        fo.webMap.getSettings().setJavaScriptEnabled(true);
        fo.webMap.getSettings().setGeolocationEnabled(true);
//        fo.webMap.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        fo.webMap.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        /***************************************** Mobarraei ************************************/
        fo.webMap.addJavascriptInterface(new JavaScriptInterface(this), "MyAndroid");

        /***************************************** Mobarraei ************************************/
        fo.webMap.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                G.log("Url Loaded ...");
                /* This call inject JavaScript into the page which just finished loading. */
                //fo.webMap.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                /***************************************** Mobarraei ************************************/
                //fo.webMap.loadUrl("javascript:getPosition()");
                fo.webMap.loadUrl("javascript:setPosition('" + fo.edtLat.getText().toString() + "','" + fo.edtLong.getText().toString() + "','" + 1 + "')");// lat - lng - radious
                /***************************************** Mobarraei ************************************/
            }
        });

        fo.webMap.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        fo.webMap.loadUrl(MapURL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                getLocationInfo(fo.edtLocation.getText().toString());
                break;

            case R.id.btnSet:
                try {
                    G.setting.gPSLat = Double.parseDouble(fo.edtLat.getText().toString());
                    G.setting.gPSLon = Double.parseDouble(fo.edtLong.getText().toString());
                } catch (Exception e) {
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(574), G.T.getSentence(579));
                    return;
                }
                fo.webMap.loadUrl("javascript:setPosition('" + G.setting.gPSLat + "','" + G.setting.gPSLon + "','" + 1 + "')");
                Database.Setting.edit(G.setting);
                break;
        }
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // process the html as needed by the app
        }
    }

    /**
     * search for location name
     */
    public JSONObject getLocationInfo(final String address) {
        dlgc = new DialogClass(G.currentActivity);

//        final JSONObject obj = null;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dlgc.setDialogText(G.T.getSentence(728));
                dlgc.setDialogTitle(G.T.getSentence(753));
                dlgc.showWaite();
            }

            @Override
            protected Void doInBackground(Void... params) {

                StringBuilder stringBuilder = new StringBuilder();
                try {

                    adr = address.replaceAll(" ", "%20");

                    HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + adr + "&sensor=false");
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response;
                    stringBuilder = new StringBuilder();


                    response = client.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    InputStream stream = entity.getContent();
                    int b;
                    while ((b = stream.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    locationObject = new JSONObject(stringBuilder.toString());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                dlgc.dissmiss();

                if (locationObject != null) {
                    if (getLatLong(locationObject)) {

                        dlgc.dissmiss();

                        fo.edtLat.setText(String.valueOf(latitude));
                        fo.edtLong.setText(String.valueOf(longitute));
                        fo.webMap.loadUrl("javascript:setPosition('" + latitude + "','" + longitute + "','" + 1 + "')");
                    } else {
                        dlgc.dissmiss();
                        dlgc = new DialogClass(G.currentActivity);
                        dlgc.showOk(G.T.getSentence(753), G.T.getSentence(848));
                    }
                }
            }
        }.execute();

        return locationObject;
    }

    /**
     * get latitude & longitude based on location json
     */
    public boolean getLatLong(JSONObject jsonObject) {

        try {

            longitute = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        //add other interface methods to be called from JavaScript
        @JavascriptInterface
        public void ReciveValueFromJs(final String lat, final String log, final String r) {
            //do something useful with str
            G.HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    fo.edtLat.setText(lat);
                    fo.edtLong.setText(log);
                    //  fo.edtRadius.setText(r);
                }
            });
        }
    }
}
