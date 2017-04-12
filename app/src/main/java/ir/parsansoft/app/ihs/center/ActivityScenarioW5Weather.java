package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w5;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioDetailsWeather;


public class ActivityScenarioW5Weather extends ActivityEnhanced {

    private CO_f_senario_w5                         fo;
    Activity                                        form;
    Database.Scenario.Struct                        scenario          = null;
    Database.Section.Struct[]                       sections;
    private AdapterScenarioDetailsWeather           listAdapter;
    private ArrayList<Database.WeatherTypes.Struct> weatherTypesArray = new ArrayList<Database.WeatherTypes.Struct>();
    Database.WeatherTypes.Struct[]                  weatherTypes;
    boolean                                         isBusy            = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_senario_w5);
        else
            setContentView(R.layout.f_senario_w5_rtl);
        form = this;
        fo = new CO_f_senario_w5(this);
        changeMenuIconBySelect(3);
        setSideBarVisiblity(false);
        translateForm();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("SCENARIO_ID")) {
                int id = extras.getInt("SCENARIO_ID");
                scenario = Database.Scenario.select(id);
                if (scenario != null)
                    initializeForm();
            }
        }
        loadWeatherGrid();
        fo.swhActive.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                G.log("check changed ..." + isChecked);
                scenario.opPreWeather = isChecked;
                if ( !scenario.opPreWeather) {
                    fo.layOptions.setVisibility(View.INVISIBLE);
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    params.weight = 1.0f;
                    //fo.laySwitcher.setLayoutParams(params);
                } else {
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    params.weight = 0.0f;
                    //fo.laySwitcher.setLayoutParams(params);
                    fo.layOptions.setVisibility(View.VISIBLE);
                }
            }
        });

        fo.btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW6Temperature.class);
                        intent.putExtra("SCENARIO_ID", scenario.iD);
                        G.currentActivity.startActivity(intent);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                        form.finish();
                    }
                    else
                        isBusy = false;
                }
            }
        });
        fo.btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW4GPS.class);
                        intent.putExtra("SCENARIO_ID", scenario.iD);
                        G.currentActivity.startActivity(intent);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                        form.finish();
                    }
                    else
                        isBusy = false;
                }
            }
        });
        fo.btnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                form.finish();
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
    }


    private void loadWeatherGrid() {
        G.log("Loading weather ...");
        listAdapter = null;
        weatherTypes = Database.WeatherTypes.select("");
        if (weatherTypes != null) {
            listAdapter = new AdapterScenarioDetailsWeather(G.currentActivity, weatherTypes, scenario.weatherTypes);
            if (listAdapter != null) {
                G.log("weathertype counts :" + weatherTypes.length);
                fo.gridView1.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
            }
        }

    }

    private void initializeForm() {
        G.log("weather status : " + scenario.opPreWeather);
        if (scenario.opPreWeather) {
            fo.swhActive.setChecked(true);
        }
        else {
            G.log("ir.parsansoft.app.ihs.center.Weather is Disabled");
            fo.layOptions.setVisibility(View.INVISIBLE);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            //fo.laySwitcher.setLayoutParams(params);
        }
    }
    private boolean saveForm() {

        try {
            scenario.weatherTypes = listAdapter.getSelectedWeathers();
        }
        catch (Exception e) {
            G.printStackTrace(e);
        }

        boolean hasActivePre;
        hasActivePre = scenario.opTimeBase || scenario.opPreGPS || scenario.opPreSwitchBase || scenario.opPreTemperature || scenario.opPreWeather;
        if ( !hasActivePre)
            scenario.active = false;

        if (scenario.iD == 0) {
            G.log("Add new scenario ... ");
            // Insert into database
            try {
                scenario.iD = (int) Database.Scenario.insert(scenario);
                return true;
            }
            catch (Exception ex) {
                G.printStackTrace(ex);
                return false;
            }
        }
        else {
            // Edit current state
            G.log("Edit the scenario ... sensorNodeId=" + scenario.iD);
            try {
                Database.Scenario.edit(scenario);
                return true;
            }
            catch (Exception ex) {
                G.printStackTrace(ex);
                return false;
            }
        }
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.swhActive.setText(G.T.getSentence(545));
        fo.txtWeatherTitle.setText(G.T.getSentence(546));
        fo.btnClose.setText(G.T.getSentence(120));
        fo.btnBack.setText(G.T.getSentence(104));
        fo.btnNext.setText(G.T.getSentence(103));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }
}
