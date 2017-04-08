package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.AllViews.CO_d_simple_spinner;
import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w9;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioNotifyMobiles;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioNotifyMobiles.onDeleteItem;


public class ActivityScenarioW10Notify extends ActivityEnhanced {

    private CO_f_senario_w9              fo;
    Activity                             form;
    Database.Scenario.Struct             scenario     = null;

    Database.Mobiles.Struct[]            mobiles;
    ArrayList<Integer>                   mobilesIDs   = new ArrayList<Integer>();
    ArrayList<Integer>                   allMobileIDs = new ArrayList<Integer>();
    private AdapterScenarioNotifyMobiles listAdapter;
    boolean                              isBusy       = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_senario_w10);
        form = this;
        fo = new CO_f_senario_w9(this);
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
            refreshList();
        }

        fo.swhActive.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                scenario.opResultNotify = isChecked;
                if ( !scenario.opResultNotify) {
                    fo.layOptions.setVisibility(View.INVISIBLE);
                } else {
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
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW11SMS.class);
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
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW9Switch.class);
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

        fo.btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dlg = new Dialog(G.currentActivity);
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dlg.setContentView(R.layout.d_simple_spinner);
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                final CO_d_simple_spinner lo = new CO_d_simple_spinner(dlg);
                lo.txtBody.setText(G.T.getSentence(245));
                lo.txtTitle.setText(G.T.getSentence(246));

                //fill mobile combobox ...
                ArrayAdapter<String> adapter = null;
                List<String> listMobiles = null;
                listMobiles = new ArrayList<String>();
                Database.Mobiles.Struct allMobiles[] = Database.Mobiles.select("");

                if (allMobiles != null)
                {
                    for (int i = 0; i < allMobiles.length; i++)
                    {
                        allMobileIDs.add(allMobiles[i].iD);
                        listMobiles.add(allMobiles[i].name);
                    }
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.l_spinner_item, listMobiles);
                adapter.setDropDownViewResource(R.layout.l_spinner_item);
                lo.spnItems.setAdapter(adapter);
                //------------------------------------------
                lo.btnPositive.setText(G.T.getSentence(101));
                lo.btnNegative.setText(G.T.getSentence(102));
                lo.btnPositive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (lo.spnItems.getSelectedItemPosition() >= 0) {
                            if (scenario.notifyMobileIDs.contains(allMobileIDs.get(lo.spnItems.getSelectedItemPosition()) + ";"))
                            {
                                G.toast(G.T.getSentence(575));
                                return;
                            }
                            scenario.notifyMobileIDs += allMobileIDs.get(lo.spnItems.getSelectedItemPosition()) + ";";
                            dlg.dismiss();
                            refreshList();
                        }
                    }
                });
                lo.btnNegative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dlg.dismiss();
                    }
                });
                dlg.show();
            }
        });
    }
    private void initializeForm() {
        fo.edtAlarmText.setText(scenario.notifyText);
        fo.swhActive.setChecked(scenario.opResultNotify);
        if ( !scenario.opResultNotify)
            fo.layOptions.setVisibility(View.INVISIBLE);
        else
            fo.layOptions.setVisibility(View.VISIBLE);

    }

    private void refreshList() {
        ArrayAdapter<String> adapter = null;
        List<String> listMobiles = null;
        listMobiles = new ArrayList<String>();
        String mns = scenario.notifyMobileIDs.replace(";", ",");
        if (mns.length() > 1)
            mns = mns.substring(0, mns.length() - 1);
        mobilesIDs.clear();
        mobiles = Database.Mobiles.select("ID in(" + mns + ")");
        if (mobiles != null) {
            for (int i = 0; i < mobiles.length; i++) {
                listMobiles.add(mobiles[i].name);
                mobilesIDs.add(mobiles[i].iD);
            }
        }
        listAdapter = new AdapterScenarioNotifyMobiles(G.currentActivity, listMobiles);
        listAdapter.setOnDeleteItemListener(new onDeleteItem() {
            @Override
            public void onDeleteItemListener(int index) {
                G.log("Item Deleted :" + mobilesIDs.get(index) + ";");
                mobilesIDs.remove(index);
                scenario.notifyMobileIDs = "";
                for (int i = 0; i < mobilesIDs.size(); i++) {
                    scenario.notifyMobileIDs += mobilesIDs.get(i) + ";";
                }
                G.log(scenario.notifyMobileIDs);
                refreshList();
            }
        });
        fo.lstMobileDevices.setAdapter(listAdapter);
    }

    private boolean saveForm() {
        scenario.notifyText = fo.edtAlarmText.getText().toString().trim();
        boolean hasActiveResult;
        hasActiveResult = scenario.opResultNotify || scenario.opResultSMS || scenario.opResultSwitch;
        if ( !hasActiveResult)
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
            scenario.hasEdited = true;
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
        fo.swhActive.setText(G.T.getSentence(563));
        fo.txtTitle.setText(G.T.getSentence(564));
        fo.btnAdd.setText(G.T.getSentence(105));
        fo.txtMessageTitle.setText(G.T.getSentence(565));
        fo.btnCancel.setText(G.T.getSentence(120));
        fo.btnBack.setText(G.T.getSentence(104));
        fo.btnNext.setText(G.T.getSentence(103));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }

}
