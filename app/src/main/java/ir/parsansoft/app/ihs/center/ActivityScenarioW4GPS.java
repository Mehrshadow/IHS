package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w4;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;


public class ActivityScenarioW4GPS extends ActivityEnhanced {

    private CO_f_senario_w4 fo;
    Activity form;
    Database.Scenario.Struct scenario = null;
    boolean isMapLoaded = false;
    String[] units = {G.T.getSentence(505), G.T.getSentence(506)};
    ArrayList<Integer> allMobileIDs = new ArrayList<Integer>();
    List<String> listMobiles = null;
    boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_senario_w4);
        else
            setContentView(R.layout.f_senario_w4_rtl);
        fo = new CO_f_senario_w4(this);
        changeMenuIconBySelect(3);
        setSideBarVisiblity(false);
        translateForm();
        form = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("SCENARIO_ID")) {
                int id = extras.getInt("SCENARIO_ID");
                scenario = Database.Scenario.select(id);
            }
        }
        fo.btnSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                double radius = (fo.spnDistance.getSelectedItemPosition() == 0) ? Double.parseDouble(fo.edtRadius.getText().toString()) : Double.parseDouble(fo.edtRadius.getText().toString()) * 1000;
                fo.webMap.loadUrl("javascript:setPosition('" + fo.edtLat.getText().toString() + "','" + fo.edtLong.getText().toString() + "','" + radius + "')");
            }
        });
        fo.swhActive.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                scenario.opPreGPS = isChecked;
                if (!scenario.opPreGPS) {
                    fo.layOptions.setVisibility(View.INVISIBLE);
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    params.weight = 1.0f;
                    //fo.laySwitcher.setLayoutParams(params);
                } else {
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    params.weight = 0.0f;
                    //fo.laySwitcher.setLayoutParams(params);
                    fo.layOptions.setVisibility(View.VISIBLE);
                    if (!isMapLoaded) {
                        loadMap();
                        isMapLoaded = true;
                    }
                }
            }
        });


        fo.rdbtnEntering.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean newState) {
                fo.rdbtnExiting.setChecked(!newState);
            }
        });
        fo.rdbtnExiting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean newState) {
                fo.rdbtnEntering.setChecked(!newState);
            }
        });


        fo.btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW5Weather.class);
                        intent.putExtra("SCENARIO_ID", scenario.iD);
                        G.currentActivity.startActivity(intent);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                        form.finish();
                    } else
                        isBusy = false;
                }
            }
        });

        fo.btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW3Event.class);
                        intent.putExtra("SCENARIO_ID", scenario.iD);
                        G.currentActivity.startActivity(intent);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                        form.finish();
                    } else
                        isBusy = false;
                }
            }
        });
        fo.btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                form.finish();
                Animation.doAnimation(Animation_Types.FADE);
            }
        });

        loadMobiles();
        initializeForm();

    }

    private void initializeForm() {
        fo.swhActive.setChecked(scenario.opPreGPS);
        if (!scenario.opPreGPS) {
            G.log("GPS is Disabled");
            fo.layOptions.setVisibility(View.INVISIBLE);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            //fo.laySwitcher.setLayoutParams(params);
        }


        double latitude = 0;
        double longitude = 0;
        int radius = 500;
        int enteringMode = 1; // 1= Enter   2= Exit
        int mobileID = -1;
        G.log(scenario.gPS_Params);
        if (scenario.gPS_Params.equals("")) {
            latitude = G.setting.gPSLat;
            longitude = G.setting.gPSLon;
            radius = 500;
            enteringMode = 1;
            mobileID = -1;
        } else {
            try {
                JSONObject gpsJ = new JSONObject(scenario.gPS_Params);
                latitude = gpsJ.getDouble("Latitude");
                longitude = gpsJ.getDouble("Longitude");
                radius = gpsJ.getInt("Radius");
                enteringMode = gpsJ.getInt("EnteringMode");
                mobileID = gpsJ.getInt("MobileID");
            } catch (Exception e) {
            }
        }

        fo.edtLat.setText("" + latitude);
        fo.edtLong.setText("" + longitude);
        if (enteringMode == 1) {
            fo.rdbtnEntering.setChecked(true);
        }
        if (enteringMode == 2) {
            fo.rdbtnExiting.setChecked(true);
        }
        G.log("Mobile ID :" + mobileID);
        if (mobileID > -1) {
            // Set combobox selected item to this mobileNumber;
            try {
                fo.spnMobile.setSelection(allMobileIDs.indexOf(mobileID));
            } catch (Exception e) {
                G.printStackTrace(e);
            }
        }
        if (radius % 1000 == 0) {
            fo.edtRadius.setText("" + (int) radius / 1000);
            fo.spnDistance.setSelection(1);
        } else {
            fo.edtRadius.setText("" + radius);
            fo.spnDistance.setSelection(0);
        }


        if (scenario.opPreGPS)
            loadMap();

    }


    private void loadMap() {
        G.log("Url Loading ...");
        String MapURL = G.URL_Map_Webservice;
        fo.webMap.setWebViewClient(new WebViewClient());
        fo.webMap.getSettings().setJavaScriptEnabled(true);
        //fo.webMap.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
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
                int radius = (fo.spnDistance.getSelectedItemPosition() == 0) ? Integer.parseInt(fo.edtRadius.getText().toString()) : Integer.parseInt(fo.edtRadius.getText().toString()) * 1000;
                fo.webMap.loadUrl("javascript:setPosition('" + fo.edtLat.getText().toString() + "','" + fo.edtLong.getText().toString() + "','" + radius + "')");
                /***************************************** Mobarraei ************************************/
            }
        });
        fo.webMap.loadUrl(MapURL);

    }

    private void loadMobiles() {
        ArrayAdapter<String> adapter = null;
        Database.Mobiles.Struct allMobiles[] = Database.Mobiles.select("");
        listMobiles = new ArrayList<String>();

        if (allMobiles != null) {
            for (int i = 0; i < allMobiles.length; i++) {
                allMobileIDs.add(allMobiles[i].iD);
                listMobiles.add(allMobiles[i].name);
            }
        }
        if (G.setting.languageID == 1 || G.setting.languageID == 4) {
            adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item, listMobiles);
            adapter.setDropDownViewResource(R.layout.l_spinner_item);
        } else {
            adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item_rtl, listMobiles);
            adapter.setDropDownViewResource(R.layout.l_spinner_item_rtl);
        }
        fo.spnMobile.setAdapter(adapter);
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // process the html as needed by the app
        }
    }

    private boolean saveForm() {

        if (scenario.opPreGPS) {
            if (fo.spnMobile.getSelectedItemPosition() < 0) {
                // TODO: Alarm : please select a mobile number ...
                return false;
            }

            if (!fo.rdbtnEntering.isChecked() && !fo.rdbtnExiting.isChecked()) {
                // TODO: Alarm : please select Entering mode or esiting mode ...
                return false;
            }
            try {
                if (fo.spnDistance.getSelectedItemPosition() == 0) {// meter selected
                    if (Integer.parseInt(fo.edtRadius.getText().toString()) < 100) {
                        fo.edtRadius.setError(G.T.getSentence(845));
                        return false;
                    }
                } else if (Integer.parseInt(fo.edtRadius.getText().toString()) < 1) return false;
            } catch (Exception e) {
                fo.edtRadius.setError(G.T.getSentence(845));
            }

            JSONObject gpsJ = new JSONObject();
            try {
                gpsJ.put("MobileID", allMobileIDs.get(fo.spnMobile.getSelectedItemPosition()));
                if (fo.rdbtnEntering.isChecked())
                    gpsJ.put("EnteringMode", 1);
                else if (fo.rdbtnExiting.isChecked())
                    gpsJ.put("EnteringMode", 2);
                try {
                    gpsJ.put("Latitude", Double.parseDouble(fo.edtLat.getText().toString()));
                    gpsJ.put("Longitude", Double.parseDouble(fo.edtLong.getText().toString()));
                } catch (Exception e) {
                    // TODO: Alarm : please enter a valid GPS positions ...
                    return false;
                }
                try {

                    if (fo.spnDistance.getSelectedItemPosition() == 1)
                        gpsJ.put("Radius", Integer.parseInt(fo.edtRadius.getText().toString()) * 1000);
                    else
                        gpsJ.put("Radius", Integer.parseInt(fo.edtRadius.getText().toString()));
                } catch (Exception e) {
                    // TODO: Alarm : please enter a valid radius ...
                    return false;
                }
                scenario.gPS_Params = gpsJ.toString();
                G.log(scenario.gPS_Params);
            } catch (JSONException e) {
                G.printStackTrace(e);
            }
        }


        boolean hasActivePre;
        hasActivePre = scenario.opTimeBase || scenario.opPreGPS || scenario.opPreSwitchBase || scenario.opPreTemperature || scenario.opPreWeather;
        if (!hasActivePre)
            scenario.active = false;
        if (scenario.iD == 0) {
            G.log("Add new scenario ... ");
            // Insert into database
            try {
                scenario.iD = (int) Database.Scenario.insert(scenario);
                return true;
            } catch (Exception ex) {
                G.printStackTrace(ex);
                return false;
            }
        } else {
            // Edit current state
            G.log("Edit the scenario ... nodeId=" + scenario.iD);
            try {
                Database.Scenario.edit(scenario);
                return true;
            } catch (Exception ex) {
                G.printStackTrace(ex);
                return false;
            }
        }
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.swhActive.setText(G.T.getSentence(541));
        fo.textView2.setText(G.T.getSentence(542));
        fo.rdbtnEntering.setText(G.T.getSentence(543));
        fo.rdbtnExiting.setText(G.T.getSentence(544));
        fo.btnCancel.setText(G.T.getSentence(120));
        fo.btnBack.setText(G.T.getSentence(104));
        fo.btnNext.setText(G.T.getSentence(103));
        fo.btnSet.setText(G.T.getSentence(123));
        fo.textView1.setText(G.T.getSentence(572));
        fo.TextView01.setText(G.T.getSentence(573));
        fo.TextView02.setText(G.T.getSentence(574));

        loadUnits();
    }

    /*****************************************
     * Mobarraei
     ************************************/
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

    private void loadUnits() {
        ArrayAdapter<String> adapter = null;
        List<String> listDistanceUnits = null;
        listDistanceUnits = new ArrayList<String>();
        if (units != null) {
            for (int i = 0; i < units.length; i++)
                listDistanceUnits.add(units[i]);
        }
        if (G.setting.languageID == 1 || G.setting.languageID == 4) {
            adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item, listDistanceUnits);
            adapter.setDropDownViewResource(R.layout.l_spinner_item);
        } else {
            adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item_rtl, listDistanceUnits);
            adapter.setDropDownViewResource(R.layout.l_spinner_item_rtl);
        }
        fo.spnDistance.setAdapter(adapter);
    }

    /*****************************************
     * Mobarraei
     ************************************/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }
}
