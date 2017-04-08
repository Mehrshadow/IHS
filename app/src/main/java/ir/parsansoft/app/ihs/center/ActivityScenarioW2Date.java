package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w2;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;


public class ActivityScenarioW2Date extends ActivityEnhanced {

    private CO_f_senario_w2   fo;
    Activity                  form;
    Database.Scenario.Struct  scenario = null;
    LayoutParams params1;
    Resources                 system;
    boolean                   isBusy   = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_senario_w2);
        else
            setContentView(R.layout.f_senario_w2_rtl);
        form = this;
        fo = new CO_f_senario_w2(this);
        changeMenuIconBySelect(3);
        setSideBarVisiblity(false);
        translateForm();
        params1 = (LayoutParams) fo.laySwitcher.getLayoutParams();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("SCENARIO_ID")) {
                int id = extras.getInt("SCENARIO_ID");
                scenario = Database.Scenario.select(id);
            }
        }

        if (scenario != null)
            initializeForm();
        else {
            scenario = new Database.Scenario.Struct();
        }
        if (scenario.iD == 0) {
            for (int i = 0; i < 12; i++)
                fo.chkMonth[i].setChecked(true);
            for (int i = 0; i < 31; i++)
                fo.chkMonthDay[i].setChecked(true);
            for (int i = 0; i < 7; i++)
                fo.chkWeekDay[i].setChecked(true);
        }

        fo.btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW3Event.class);
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
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW1Name.class);
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

        fo.btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                form.finish();
                Animation.doAnimation(Animation_Types.FADE);
            }
        });

        fo.swhActive.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                scenario.opTimeBase = isChecked;
                if ( !scenario.opTimeBase) {
                    fo.layOptions.setVisibility(View.GONE);
                    fo.laySwitcher.setLayoutParams(params1);
                } else {
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    params.weight = 0.0f;
                    fo.laySwitcher.setLayoutParams(params);
                    fo.layOptions.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initializeForm() {
        fo.swhActive.setChecked(scenario.opTimeBase);
        G.log("is Time Based ? " + scenario.opTimeBase);
        if (scenario.opTimeBase) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.weight = 0.0f;
            fo.laySwitcher.setLayoutParams(params);
            fo.layOptions.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < 12; i++)
            if (scenario.months.contains("M" + i))
                fo.chkMonth[i].setChecked(true);
            else
                fo.chkMonth[i].setChecked(false);
        for (int i = 0; i < 31; i++)
            if (scenario.monthDays.contains("D" + i))
                fo.chkMonthDay[i].setChecked(true);
            else
                fo.chkMonthDay[i].setChecked(false);

        for (int i = 0; i < 7; i++) {
            switch (i){
                case 0:
                    if (scenario.weekDays.contains("WD" + 7))
                        fo.chkWeekDay[i].setChecked(true);
                    else
                        fo.chkWeekDay[i].setChecked(false);
                    break;
                default:
                    if (scenario.weekDays.contains("WD" + i))
                        fo.chkWeekDay[i].setChecked(true);
                    else
                        fo.chkWeekDay[i].setChecked(false);
            }
        }
//            if (scenario.weekDays.contains("WD" + i))
//                fo.chkWeekDay[i].setChecked(true);
//            else
//                fo.chkWeekDay[i].setChecked(false);

        if (scenario.startHour.length() > 0) {
            String h = scenario.startHour.substring(0, scenario.startHour.indexOf(":"));
            G.log("Start Hure is:" + h);
            fo.tpStartHore.setCurrentHour(Integer.parseInt(h));
        }
        if (scenario.startHour.length() > 0) {
            String m = scenario.startHour.substring(scenario.startHour.indexOf(":") + 1);
            G.log("Start minutes is:" + m);
            fo.tpStartHore.setCurrentMinute(Integer.parseInt(m));
        }
    }

    private boolean saveForm() {
        String Ms = "";
        for (int i = 0; i < 12; i++)
            if (fo.chkMonth[i].isChecked())
                Ms += "M" + i + ";";
        String Ds = "";
        for (int i = 0; i < 31; i++)
            if (fo.chkMonthDay[i].isChecked())
                Ds += "D" + i + ";";
        String WDs = "";
        for (int i = 0; i < 7; i++)
            if (fo.chkWeekDay[i].isChecked()) {
//                WDs += "WD" + i + ";";

                switch (i){
                    case 0:
                        WDs += "WD" + 7 + ";";
                        break;
                    default:
                        WDs += "WD" + i + ";";
                        break;
                }
            }
        scenario.months = Ms;
        scenario.monthDays = Ds;
        scenario.weekDays = WDs;
        scenario.startHour = fo.tpStartHore.getCurrentHour() + ":" + fo.tpStartHore.getCurrentMinute();

        boolean hasActivePre;
        hasActivePre = scenario.opTimeBase || scenario.opPreGPS || scenario.opPreSwitchBase || scenario.opPreTemperature || scenario.opPreWeather;
        if ( !hasActivePre)
            scenario.active = false;

        if (scenario.iD == 0) {
            G.log("Add new scenario ... ");

            // Insert into database
            try {
                scenario.iD = (int) Database.Scenario.insert(scenario);
                G.scenarioBP.resetTimeBase();
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
            scenario.hasEdited = true;
            try {
                Database.Scenario.edit(scenario);
                G.scenarioBP.resetTimeBase();
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
        fo.swhActive.setText(G.T.getSentence(516));
        fo.txtDaysOfMonth.setText(G.T.getSentence(517));
        for (int i = 0; i < 31; i++) {
            fo.chkMonthDay[i].setText("" + (int) (i + 1));
        }
        fo.txtMonthes.setText(G.T.getSentence(519));
        for (int i = 0; i < 12; i++) {
            fo.chkMonth[i].setText(G.T.getSentence(520 + i));
        }
        fo.txtDaysOfWeek.setText(G.T.getSentence(518));
        for (int i = 0; i < 7; i++) {
            fo.chkWeekDay[i].setText(G.T.getSentence(532 + i));
        }
        fo.txtStartHour.setText(G.T.getSentence(539));
        fo.btnCancel.setText(G.T.getSentence(120));
        fo.btnNext.setText(G.T.getSentence(103));
        fo.btnBack.setText(G.T.getSentence(104));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }
}
