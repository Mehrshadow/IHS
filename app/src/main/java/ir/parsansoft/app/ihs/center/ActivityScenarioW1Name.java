package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w1;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;


public class ActivityScenarioW1Name extends ActivityEnhanced {

    private CO_f_senario_w1 fo;
    Activity form;
    Database.Scenario.Struct scenario = null;
    boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeMenuIconBySelect(3);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_senario_w1);
        else
            setContentView(R.layout.f_senario_w1_rtl);
        form = this;
        fo = new CO_f_senario_w1(this);
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

        fo.btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW2Date.class);
                        intent.putExtra("SCENARIO_ID", scenario.iD);
                        G.currentActivity.startActivity(intent);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
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
    }

    private void initializeForm() {
        fo.edtName.setText(scenario.name);
        fo.edtDes.setText(scenario.des);
    }

    private boolean saveForm() {
        if (scenario == null) {
            scenario = new Database.Scenario.Struct();
            scenario.iD = 0;
        }

        scenario.name = fo.edtName.getText().toString().trim();
        scenario.des = fo.edtDes.getText().toString().trim();
        /**
         * Jahanbin
         **/
        if (!validateName(scenario.name)) return false;
//        if(!isNameUnique(fo.edtName.getText().toString().trim())) return false;
        /**
         * Jahanbin
         **/

        if (scenario.iD == 0) {
            G.log("Add new scenario ... ");
            // Insert into database
            try {
                scenario.iD = (int) Database.Scenario.insert(scenario);
                SysLog.log("Scenario " + scenario.name + " Created.", SysLog.LogType.SCENARIO_CHANGE, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                //  Send message to server and local Mobiles
                NetMessage netMessage = new NetMessage();
                netMessage.data = scenario.getScenarioDataJSON();
                netMessage.action = NetMessage.Insert;
                netMessage.type = NetMessage.ScenarioData;
                netMessage.typeName = NetMessageType.ScenarioData;
                netMessage.messageID = netMessage.save();
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);
                return true;
            } catch (SQLiteConstraintException sqlEx) {
                new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(846));
                G.printStackTrace(sqlEx);
                return false;
            } catch (Exception e) {
                G.printStackTrace(e);
                return false;
            }
        } else {
            // Edit current state
            scenario.hasEdited = true;
            G.log("Edit the scenario ... nodeId=" + scenario.iD);
            try {
                Database.Scenario.edit(scenario);

                //  Send message to server and local Mobiles
                NetMessage netMessage = new NetMessage();
                netMessage.data = scenario.getScenarioDataJSON();
                netMessage.action = NetMessage.Update;
                netMessage.type = NetMessage.ScenarioData;
                netMessage.typeName = NetMessageType.ScenarioData;
                netMessage.messageID = netMessage.save();
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);

                return true;
            } catch (Exception ex) {
                G.printStackTrace(ex);
                return false;
            }
        }
    }

    /**
     * Jahanbin
     **/
    private boolean validateName(String name) {
        if (name.equals("")) {
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(835));
            return false;
        } else return true;
    }

    /**
     * Jahanbin
     **/

    @Override
    public void translateForm() {
        super.translateForm();
        fo.txtSenarioName.setText(G.T.getSentence(514));
        fo.txtDes.setText(G.T.getSentence(515));
        fo.btnCancel.setText(G.T.getSentence(120));
        fo.btnNext.setText(G.T.getSentence(103));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }
}
