package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w6;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;


public class ActivityScenarioW7Op extends ActivityEnhanced {

    private CO_f_senario_w6  fo;
    Activity                 form;
    Database.Scenario.Struct scenario = null;
    boolean                  isBusy   = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_senario_w7);
        else
            setContentView(R.layout.f_senario_w7_rtl);
        form = this;
        fo = new CO_f_senario_w6(this);
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


        fo.chkAnd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean newState) {
                scenario.opPreAND = newState;
                fo.chkOr.setChecked( !scenario.opPreAND);
            }
        });
        fo.chkOr.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean newState) {
                scenario.opPreAND = !newState;
                fo.chkAnd.setChecked(scenario.opPreAND);
            }
        });

        fo.btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW8Keyword.class);
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
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW6Temperature.class);
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
    private void initializeForm() {
        fo.chkAnd.setChecked(scenario.opPreAND);
        fo.chkOr.setChecked( !scenario.opPreAND);
    }
    private boolean saveForm() {
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
        fo.chkAnd.setText(G.T.getSentence(551));
        fo.txtAndHeader.setText(G.T.getSentence(552));
        fo.txtAndDescription.setText(G.T.getSentence(553));
        fo.chkOr.setText(G.T.getSentence(554));
        fo.txtOrHeader.setText(G.T.getSentence(555));
        fo.txtOrDescription.setText(G.T.getSentence(556));
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
