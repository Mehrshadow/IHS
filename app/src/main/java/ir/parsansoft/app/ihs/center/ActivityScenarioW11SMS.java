package ir.parsansoft.app.ihs.center;

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
import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w10;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioNotifyMobiles;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioNotifyMobiles.onDeleteItem;


public class ActivityScenarioW11SMS extends ActivityEnhanced {

    private CO_f_senario_w10             fo;
    Database.Scenario.Struct             scenario   = null;
    boolean                              isBusy     = false;
    List<String>                         listMobiles;
    ArrayList<String>                    allMobiles = new ArrayList<String>();
    private AdapterScenarioNotifyMobiles listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_senario_w11);
        fo = new CO_f_senario_w10(this);
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
                scenario.opResultSMS = isChecked;
                if ( !scenario.opResultSMS) {
                    fo.layOptions.setVisibility(View.INVISIBLE);
                } else {
                    fo.layOptions.setVisibility(View.VISIBLE);
                }
            }
        });

        fo.btnFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (saveForm()) {
                    finish();
                    Animation.doAnimation(Animation_Types.FADE);
                }
            }
        });

        fo.btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW10Notify.class);
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
        fo.btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
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


                ArrayAdapter<String> adapter = null;
                List<String> listMobiles = null;
                listMobiles = new ArrayList<String>();

                if (G.setting.validMobiles.length() > 0)
                {
                    G.log(G.setting.validMobiles);
                    allMobiles.clear();
                    String tmp[] = G.setting.validMobiles.substring(0, G.setting.validMobiles.length() - 1).split(";");
                    for (int i = 0; i < tmp.length; i++) {
                        G.log(tmp[i]);
                        allMobiles.add(tmp[i]);
                    }
                }
                adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item, allMobiles);
                adapter.setDropDownViewResource(R.layout.l_spinner_item);
                lo.spnItems.setAdapter(adapter);
                lo.btnPositive.setText(G.T.getSentence(101));
                lo.btnNegative.setText(G.T.getSentence(102));
                lo.btnPositive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (allMobiles.size() > 0) {
                            String selectedNumber = allMobiles.get(lo.spnItems.getSelectedItemPosition());
                            G.log(selectedNumber);
                            if (scenario.mobileNumbers.contains(selectedNumber + ";"))
                            {
                                G.toast(G.T.getSentence(575));
                                return;
                            }
                            scenario.mobileNumbers += selectedNumber + ";";
                            G.log("All mobiles " + scenario.mobileNumbers);

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
        fo.swhActive.setChecked(scenario.opResultSMS);
        if ( !scenario.opResultSMS)
            fo.layOptions.setVisibility(View.INVISIBLE);
        else
            fo.layOptions.setVisibility(View.VISIBLE);
        fo.edtAlarmText.setText(scenario.sMStext);
    }

    private void refreshList() {
        ArrayAdapter<String> adapter = null;

        listMobiles = new ArrayList<String>();
        String mns = scenario.mobileNumbers;
        G.log("All mobile numbers :" + mns);
        if (mns.length() > 1) {
            mns = mns.substring(0, mns.length() - 1);
            String tmp[] = mns.split(";");
            for (int i = 0; i < tmp.length; i++) {
                G.log(tmp[i]);
                listMobiles.add(tmp[i]);
            }
        }
        listAdapter = new AdapterScenarioNotifyMobiles(G.currentActivity, listMobiles);
        listAdapter.setOnDeleteItemListener(new onDeleteItem() {
            @Override
            public void onDeleteItemListener(int index) {
                G.log("Item Deleted :" + listMobiles.get(index) + ";");
                listMobiles.remove(index);
                scenario.mobileNumbers = "";
                for (int i = 0; i < listMobiles.size(); i++) {
                    scenario.mobileNumbers += listMobiles.get(i) + ";";
                }
                G.log(scenario.mobileNumbers);
                refreshList();
            }
        });
        fo.lstMobileDevices.setAdapter(listAdapter);
    }

    private boolean saveForm() {
        scenario.sMStext = fo.edtAlarmText.getText().toString().trim();
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
        fo.swhActive.setText(G.T.getSentence(566));
        fo.txtTitle.setText(G.T.getSentence(567));
        fo.btnAdd.setText(G.T.getSentence(105));
        fo.txtMessageTitle.setText(G.T.getSentence(569));
        fo.btnCancel.setText(G.T.getSentence(120));
        fo.btnBack.setText(G.T.getSentence(104));
        fo.btnFinish.setText(G.T.getSentence(101));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }
}
