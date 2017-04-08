package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w3;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioDetailItems;
import ir.parsansoft.app.ihs.center.adapters.DialogNodeSelector;


public class ActivityScenarioW3Event extends ActivityEnhanced {

    private CO_f_senario_w3                       fo;
    Activity                                      form;
    Database.Scenario.Struct                      scenario           = null;
    Database.Section.Struct[]                     sections;
    Database.Room.Struct[]                        rooms;
    Database.Node.Struct[]                        nodes;
    Database.Switch.Struct[]                      switches;
    String[]                                      logicalSigns       = { "=", ">", ">=", "<", "<=", "!=" };
    Database.ValueType.Struct                     switchValueType;
    Database.ValueTypeInstances.Struct[]          valueInstances;
    List<String>                                  listValueInstances = null;
    private AdapterScenarioDetailItems            listAdapter;
    private ArrayList<Database.PreOperand.Struct> preOperandsArray   = new ArrayList<Database.PreOperand.Struct>();
    Database.PreOperand.Struct[]                  preOperand;
    boolean                                       isBusy             = false;
    DialogNodeSelector dialogNodeSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_senario_w3);
        else
            setContentView(R.layout.f_senario_w3_rtl);
        form = this;
        fo = new CO_f_senario_w3(this);
        changeMenuIconBySelect(3);
        setSideBarVisiblity(false);
        translateForm();
        Bundle extras = getIntent().getExtras();
        G.fragmentManager = this.getSupportFragmentManager();
        if (extras != null) {
            if (extras.containsKey("SCENARIO_ID")) {
                int id = extras.getInt("SCENARIO_ID");
                scenario = Database.Scenario.select(id);
                //                if (scenario != null)
                //                    initializeForm();
            }
        }


//        fo.layPerOperandValueRange.setVisibility(View.GONE);
//        fo.layPerOperandValueSelective.setVisibility(View.GONE);
//        fo.btnAdd.setVisibility(View.GONE);
        fo.btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW4GPS.class);
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
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW2Date.class);
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
                scenario.opPreSwitchBase = isChecked;
                if (!scenario.opPreSwitchBase) {
                    fo.layOptions.setVisibility(View.INVISIBLE);
                } else {
                    fo.layOptions.setVisibility(View.VISIBLE);
                }
            }
        });

//        fo.comNodeSelector1.setOnSwitchSelected(new SelectedNodeChange() {
//            @Override
//            public void onSwitchSelected(int SwitchID, String opr, float value) {
//                Database.PreOperand.insert(SwitchID, opr, value, scenario.iD);
//                refreshList();
//            }
//        });

        fo.btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogNodeSelector = new DialogNodeSelector();
                dialogNodeSelector.setCurrentWizard(3);
                dialogNodeSelector.show(G.fragmentManager, null);
                dialogNodeSelector.setonSwitchSelected(new DialogNodeSelector.DialogClosedListener() {
                    @Override
                    public void onSwitchSelected(int SwitchID, String opr, float value) {
                        Database.PreOperand.Struct prs[] = Database.PreOperand.select("SwitchID=" + SwitchID + " AND ScenarioID=" + scenario.iD);
                        Database.Results.Struct rsts[] = Database.Results.select("SwitchID=" + SwitchID + " AND ScenarioID=" + scenario.iD);
                        if (prs != null || rsts != null) {
                            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(832));
                            return;
                        }
                        Database.PreOperand.insert(SwitchID, opr, value, scenario.iD);
                        refreshList();
                    }

                    @Override
                    public void onSwitchSelectedW9(int SwitchID, float value, boolean active, int reverseTime, float preValue) {

                    }
                });
            }
        });
        initializeForm();
    }
    private void initializeForm() {
        fo.swhActive.setChecked(scenario.opPreSwitchBase);
        if ( !scenario.opPreSwitchBase) {
            fo.layOptions.setVisibility(View.INVISIBLE);
        }
        //loadlogicalSigns();
        refreshList();
    }

    private boolean saveForm() {
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

    private void refreshList() {
        preOperand = Database.PreOperand.select("ScenarioID=" + scenario.iD);
        listAdapter = new AdapterScenarioDetailItems(G.currentActivity, preOperand);
        fo.lstPreoperands.setAdapter(listAdapter);
        if (listAdapter != null) {
            G.log("Refresh List ...");
            listAdapter.notifyDataSetChanged();
        }
    }



//    private void loadlogicalSigns() {
//        ArrayAdapter<String> adapter = null;
//        List<String> listOperands = null;
//        listOperands = new ArrayList<String>();
//        if (logicalSigns != null) {
//            for (int i = 0; i < logicalSigns.length; i++)
//                listOperands.add(logicalSigns[i]);
//        }
//        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.l_spinner_item, listOperands);
//        adapter.setDropDownViewResource(R.layout.l_spinner_item);
//        fo.spnLogicalSign.setAdapter(adapter);
//    }
//
//    private void loadValues(int mySwitchID) {
//        switchValueType = Database.ValueType.select(Database.SwitchType.select(Database.Switch.select(mySwitchID).switchType).valueTypeID);
//        G.log("loadValues(" + mySwitchID + ")");
//        if (switchValueType.isSelectable)
//        {
//            G.log("switchValueType.isSelectable = true");
//            fo.layPerOperandValueRange.setVisibility(View.GONE);
//            fo.layPerOperandValueSelective.setVisibility(View.VISIBLE);
//            loadSelectiveValue(switchValueType);
//        }
//        else
//        {
//            G.log("switchValueType.isSelectable = true");
//            fo.layPerOperandValueRange.setVisibility(View.VISIBLE);
//            fo.layPerOperandValueSelective.setVisibility(View.GONE);
//        }
//    }
//    private void loadSelectiveValue(Database.ValueType.Struct valueType) {
//        G.log("loadSelectiveValue( ID = " + valueType.iD + " )");
//        ArrayAdapter<String> adapter = null;
//
//        listValueInstances = new ArrayList<String>();
//        if (valueType.isNumeric) {
//            G.log("valueType is Numeric");
//            for (double f = valueType.startValue; f <= valueType.endValue; f += valueType.valueStep)
//            {
//                String s;
//                if (f == (int) f)
//                    s = ("" + (int) f);
//                else {
//                    BigDecimal src = new BigDecimal(f);
//                    BigDecimal a = src.remainder(BigDecimal.ONE);
//                    s = "" + a;
//                }
//                listValueInstances.add(s);
//            }
//        }
//        else {
//            G.log("valueType is not Numeric");
//            valueInstances = Database.ValueTypeInstances.select("ValueTypeID=" + valueType.iD);
//            for (int i = 0; i < valueInstances.length; i++)
//                listValueInstances.add(G.T.getSentence(valueInstances[i].sentenceID));
//        }
//        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.l_spinner_item, listValueInstances);
//        adapter.setDropDownViewResource(R.layout.l_spinner_item);
//        fo.spnSelectiveValue.setAdapter(adapter);
//    }




    @Override
    public void translateForm() {
        super.translateForm();
        fo.swhActive.setText(G.T.getSentence(540));
//        fo.TextView07.setText(G.T.getSentence(501));
//        fo.TextView04.setText(G.T.getSentence(501));
        fo.btnAdd.setText(G.T.getSentence(105));
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
