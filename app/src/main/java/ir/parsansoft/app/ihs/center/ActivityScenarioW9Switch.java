package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.List;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w8;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioDetailResults;
import ir.parsansoft.app.ihs.center.adapters.DialogNodeSelector;


public class ActivityScenarioW9Switch extends ActivityEnhanced {

    private CO_f_senario_w8              fo;
    Activity                             form;
    Database.Scenario.Struct             scenario           = null;
    boolean                              isBusy             = false;

    String[]                             units              = { G.T.getSentence(502), G.T.getSentence(503), G.T.getSentence(504) };
    Database.ValueType.Struct            switchValueType;
    Database.ValueTypeInstances.Struct[] valueInstances;
    List<String>                         listValueInstances = null;
    private AdapterScenarioDetailResults listAdapter;
    //private ArrayList<Database.Results.Struct> preOperandsArray   = new ArrayList<Database.Results.Struct>();
    Database.Results.Struct[]            results;

    DialogNodeSelector dialogNodeSelector;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_senario_w9);
        else
            setContentView(R.layout.f_senario_w9_rtl);
        form = this;
        fo = new CO_f_senario_w8(this);
        changeMenuIconBySelect(3);
        setSideBarVisiblity(false);
        translateForm();
        G.fragmentManager = this.getSupportFragmentManager();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("SCENARIO_ID")) {
                int id = extras.getInt("SCENARIO_ID");
                scenario = Database.Scenario.select(id);
                if (scenario != null)
                    initializeForm();
            }
        }

        //fo.layParameters.setVisibility(View.INVISIBLE);


        /** comment */
//        fo.chkReverse.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton arg0, boolean newState) {
//                if (newState)
//                    fo.layReverse.setVisibility(View.VISIBLE);
//                else
//                    fo.layReverse.setVisibility(View.INVISIBLE);
//            }
//        });
//        loadUnits();

        fo.swhActive.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                scenario.opResultSwitch = isChecked;
                if ( !scenario.opResultSwitch) {
                    fo.layOptions.setVisibility(View.INVISIBLE);
                } else {
                    fo.layOptions.setVisibility(View.VISIBLE);
                }
            }
        });

//        fo.comNodeSelector1.setOnSwitchSelected(new SelectedNodeChange() {
//            @Override
//            public void onSwitchSelected(int SwitchID) {
//                loadValues(SwitchID);
//            }
//        });

        fo.btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW10Notify.class);
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
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW8Keyword.class);
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

        fo.btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                dialogNodeSelector = new DialogNodeSelector();
                dialogNodeSelector.setCurrentWizard(9);
                dialogNodeSelector.show(G.fragmentManager, null);
                dialogNodeSelector.setonSwitchSelected(new DialogNodeSelector.DialogClosedListener() {
                    @Override
                    public void onSwitchSelected(int SwitchID, String opr, float value) {

                    }
                    @Override
                    public void onSwitchSelectedW9(int SwitchID, float value, boolean active, int reverseTime, float preValue) {
                        Database.PreOperand.Struct prs[] = Database.PreOperand.select("SwitchID=" + SwitchID + " AND ScenarioID=" + scenario.iD);
                        Database.Results.Struct rsts[] = Database.Results.select("SwitchID=" + SwitchID + " AND ScenarioID=" + scenario.iD);
                        if (prs != null || rsts != null) {
                            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(832));
                            return;
                        }
                        Database.Results.insert(scenario.iD, SwitchID, value, false, reverseTime, 0);
                        refreshList();
                    }
                });

//                int selectedSwitchID = fo.comNodeSelector1.getSelectedSwitchID();
//                if (selectedSwitchID > 0) {
//
//                    Database.PreOperand.Struct prs[] = Database.PreOperand.select("SwitchID=" + selectedSwitchID + " AND ScenarioID=" + scenario.iD);
//                    Database.Results.Struct rsts[] = Database.Results.select("SwitchID=" + selectedSwitchID + " AND ScenarioID=" + scenario.iD);
//                    if (prs != null || rsts != null) {
//                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(832));
//                        return;
//                    }
//
//                    float value;
//                    if (fo.layPerOperandValueRange.getVisibility() == View.VISIBLE) {
//                        if (fo.edtLogicalValue.getText().toString().trim().matches("\\d+\\.?\\d*") || fo.edtLogicalValue.getText().toString().trim().matches("\\.\\d+"))
//                            value = Float.parseFloat(fo.edtLogicalValue.getText().toString());
//                        else
//                        {
//                            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(576));
//                            return;
//                        }
//                    }
//                    else {
//                        if (switchValueType.isNumeric)
//                            value = Float.parseFloat(listValueInstances.get(fo.spnSelectiveValue.getSelectedItemPosition()));
//                        else
//                            value = valueInstances[(int) fo.spnSelectiveValue.getSelectedItemId()].value;
//                    }
//                    int reverseTime = 0;
//                    if (fo.chkReverse.isChecked()) {
//                        int unitIndex = fo.spnTimeUnit.getSelectedItemPosition();
//                        try {
//                            reverseTime = Integer.parseInt(fo.edtReverseTime.getText().toString().trim());
//                            reverseTime = (int) (reverseTime * Math.pow(60, unitIndex));
//                        }
//                        catch (Exception e) {
//                            reverseTime = 0;
//                            fo.chkReverse.setChecked(false);
//                            G.printStackTrace(e);
//                        }
//                        G.log("Reverse time :" + reverseTime);
//                    }
//                    Database.Results.insert(scenario.iD, selectedSwitchID, value, false, reverseTime, 0);
//                    refreshList();
//                }
            }
        });

    }

    private void initializeForm() {
        fo.swhActive.setChecked(scenario.opResultSwitch);
        if ( !scenario.opResultSwitch) {
            fo.layOptions.setVisibility(View.INVISIBLE);
        } else
        {
            fo.layOptions.setVisibility(View.VISIBLE);
        }
//        fo.layParameters.setVisibility(View.INVISIBLE);
        refreshList();
    }
    private boolean saveForm() {
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


//    private void loadUnits() {
//        ArrayAdapter<String> adapter = null;
//        List<String> listOperands = null;
//        listOperands = new ArrayList<String>();
//        if (units != null) {
//            for (int i = 0; i < units.length; i++)
//                listOperands.add(units[i]);
//        }
//        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.l_spinner_item, listOperands);
//        adapter.setDropDownViewResource(R.layout.l_spinner_item);
//        fo.spnTimeUnit.setAdapter(adapter);
//    }

//    private void loadValues(int mySwitchID) {
//        switchValueType = Database.ValueType.select(Database.SwitchType.select(Database.Switch.select(mySwitchID).switchType).valueTypeID);
//        G.log("loadValues(" + mySwitchID + ")");
//        fo.layParameters.setVisibility(View.VISIBLE);
//        fo.layMainReverse.setVisibility(View.VISIBLE);
//        fo.chkReverse.setChecked(false);
//        fo.layReverse.setVisibility(View.INVISIBLE);
//
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


    private void refreshList() {
        results = Database.Results.select("ScenarioID=" + scenario.iD);
        listAdapter = new AdapterScenarioDetailResults(G.currentActivity, results);
        fo.listView1.setAdapter(listAdapter);
        if (listAdapter != null) {
            G.log("Refresh List ...");
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.swhActive.setText(G.T.getSentence(560));
//        fo.txtNumericValue.setText(G.T.getSentence(501));
//        fo.txtSelectiveValue.setText(G.T.getSentence(501));
//        fo.chkReverse.setText(G.T.getSentence(561));
//        fo.txtReverseTime.setText(G.T.getSentence(562));
        fo.btnAdd.setText(G.T.getSentence(105));
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
