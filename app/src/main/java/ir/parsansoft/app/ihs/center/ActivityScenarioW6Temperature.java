package ir.parsansoft.app.ihs.center;

import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


public class ActivityScenarioW6Temperature extends ActivityEnhanced {

    Database.Scenario.Struct scenario = null;
    Switch                   swhTempreture;
    LinearLayout             layTemprature;
    TextView                 txtTempratureTitle, txtTempratureunit1, txtTempratureUnit2;
    EditText                 edtMinTemp, edtMaxTemp;
    Button                   btnCancel, btnBack, btnNext;
    boolean                  isBusy   = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_senario_w6);
        else
            setContentView(R.layout.f_senario_w6_rtl);
        /************************************/
        swhTempreture = (Switch) findViewById(R.id.swhTempreture);
        layTemprature = (LinearLayout) findViewById(R.id.layTemprature);
        txtTempratureTitle = (TextView) findViewById(R.id.txtTempratureTitle);
        txtTempratureunit1 = (TextView) findViewById(R.id.txtTempratureunit1);
        txtTempratureUnit2 = (TextView) findViewById(R.id.txtTempratureUnit2);
        edtMinTemp = (EditText) findViewById(R.id.edtMinTemp);
        edtMaxTemp = (EditText) findViewById(R.id.edtMaxTemp);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNext = (Button) findViewById(R.id.btnNext);
        /**********************************/
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

        swhTempreture.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                scenario.opPreTemperature = swhTempreture.isChecked();
                if ( !scenario.opPreTemperature)
                    layTemprature.setVisibility(View.INVISIBLE);
                else
                    layTemprature.setVisibility(View.VISIBLE);
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW7Op.class);
                        intent.putExtra("SCENARIO_ID", scenario.iD);
                        G.currentActivity.startActivity(intent);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                        finish();
                    }
                    else
                        isBusy = false;
                }
            }
        });
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW5Weather.class);
                        intent.putExtra("SCENARIO_ID", scenario.iD);
                        G.currentActivity.startActivity(intent);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                        finish();
                    }
                    else
                        isBusy = false;
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
    }

    private void initializeForm() {

        if (scenario.opPreTemperature)
            swhTempreture.setChecked(true);
        else
            layTemprature.setVisibility(View.GONE);

    }
    private boolean saveForm() {

        try {
            scenario.minTemperature = Integer.parseInt(edtMinTemp.getText().toString());
            scenario.maxTemperature = Integer.parseInt(edtMaxTemp.getText().toString());
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
            G.log("Edit the scenario ... nodeId=" + scenario.iD);
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
        swhTempreture.setText(G.T.getSentence(547));
        txtTempratureTitle.setText(G.T.getSentence(550));
        txtTempratureunit1.setText(G.T.getSentence(549));
        txtTempratureUnit2.setText(G.T.getSentence(548));
        btnCancel.setText(G.T.getSentence(120));
        btnBack.setText(G.T.getSentence(104));
        btnNext.setText(G.T.getSentence(103));

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }

}
