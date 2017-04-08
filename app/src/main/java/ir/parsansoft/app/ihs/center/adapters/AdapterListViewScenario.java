package ir.parsansoft.app.ihs.center.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import ir.parsansoft.app.ihs.center.ActivityScenario;
import ir.parsansoft.app.ihs.center.ActivityScenarioW1Name;
import ir.parsansoft.app.ihs.center.AllViews.CO_l_list_scemario;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.Database.Scenario.Struct;
import ir.parsansoft.app.ihs.center.DialogClass;
import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.NetMessage;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.SysLog;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;


public class AdapterListViewScenario extends BaseAdapter {

    Context context;
    Struct[] scenarios;
    ScenarioListItemClick slic;


    public AdapterListViewScenario(Context context, Struct[] scenarios) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.scenarios = scenarios;
    }

    public interface ScenarioListItemClick {
        public void onScenarioListItemClick(int scenarioID, int position);
    }

    public void setOnScenarioListItemClick(ScenarioListItemClick slic) {
        G.log("new ScenarioListItemClick loaded ...");
        this.slic = slic;
    }

    public void refreshScenarios(Struct[] scenarios) {
        this.scenarios = scenarios;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View row = inflater.inflate(R.layout.l_list_scemario_2, convertView,false);
        View row = LayoutInflater.from(G.context).inflate(R.layout.l_list_scemario_2, parent, false);
//        if (convertView == null) {
//            row = new View(context);
//
//        }
        final CO_l_list_scemario lo = new CO_l_list_scemario(row);
        lo.swhEnabled.setChecked(scenarios[position].active);
        lo.txtName.setText(scenarios[position].name);
        if (G.currentUser != null && G.currentUser.rol == 1) {
            lo.imgBtnEdit.setVisibility(View.VISIBLE);
            lo.imgBtnEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(G.currentActivity, ActivityScenarioW1Name.class);
                    intent.putExtra("SCENARIO_ID", scenarios[position].iD);
                    G.currentActivity.startActivity(intent);
                }
            });
            lo.imgBtnDelete.setVisibility(View.VISIBLE);
            lo.imgBtnDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    G.log("Deleted Scenario row :" + position + " - ID =" + scenarios[position].iD + " -  Name : " + scenarios[position].name);
                    DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.setOnYesNoListener(new YesNoListener() {
                        @Override
                        public void yesClick() {
                            //  Send message to server and local Mobiles
                            NetMessage netMessage = new NetMessage();
                            netMessage.data = scenarios[position].getScenarioDataJSON();
                            netMessage.action = NetMessage.Delete;
                            netMessage.type = NetMessage.ScenarioData;
                            netMessage.typeName = NetMessageType.ScenarioData;
                            netMessage.messageID = netMessage.save();
                            G.mobileCommunication.sendMessage(netMessage);
                            G.server.sendMessage(netMessage);

                            Database.Scenario.delete(scenarios[position].iD);
                            Database.PreOperand.delete("ScenarioID=" + scenarios[position].iD);
                            Database.Results.delete("ScenarioID=" + scenarios[position].iD);
                            SysLog.log("Scenario Deleted.", LogType.SCENARIO_CHANGE, LogOperator.OPERAOR, 0);
                            scenarios = Database.Scenario.select("");
                            notifyDataSetChanged();
                            if (scenarios != null && scenarios.length > 0) {
                                if (slic != null) {
                                    slic.onScenarioListItemClick(scenarios[0].iD, 0);
                                    lo.layBack.setBackgroundResource(R.drawable.lay_scenario_list_row_selected);
                                }
                            } else {
                                if (slic != null) {
                                    slic.onScenarioListItemClick(-1, -1);
                                }
                            }
                        }

                        @Override
                        public void noClick() {
                        }
                    });
                    dlg.showYesNo(G.T.getSentence(152), G.T.getSentence(578));
                }
            });
            lo.swhEnabled.setVisibility(View.VISIBLE);
            lo.swhEnabled.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean newState) {
                    if (newState) {
                        if (checkCanEnable(position, lo)) {
                            G.log("Reverse Activated Scenario row :" + position + " - ID =" + scenarios[position].iD + " -  Name : " + scenarios[position].name);
                            scenarios[position].active = true;
                            Database.Scenario.edit(scenarios[position]);
                            G.scenarioBP.resetTimeBase();
                            //  Send message to server and local Mobiles
                            NetMessage netMessage = new NetMessage();
                            netMessage.data = scenarios[position].getScenarioStatusJSON(false);
                            netMessage.action = NetMessage.Update;
                            netMessage.type = NetMessage.ScenarioStatus;
                            netMessage.typeName = NetMessageType.ScenarioStatus;
                            netMessage.messageID = netMessage.save();
                            G.mobileCommunication.sendMessage(netMessage);
                            G.server.sendMessage(netMessage);
                        } else {
                            G.log("Should has at least one active precondition and one active result");
                            lo.swhEnabled.setChecked(false);
                            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(577));
                        }
                    } else {
//                        if(scenarios.length != 0){
                        scenarios[position].active = false;
                        Database.Scenario.edit(scenarios[position]);
                        //  Send message to server and local Mobiles
                        NetMessage netMessage = new NetMessage();
                        netMessage.data = scenarios[position].getScenarioStatusJSON(false);
                        netMessage.action = NetMessage.Update;
                        netMessage.type = NetMessage.ScenarioStatus;
                        netMessage.typeName = NetMessageType.ScenarioStatus;
                        netMessage.messageID = netMessage.save();
                        G.mobileCommunication.sendMessage(netMessage);
                        G.server.sendMessage(netMessage);
//                        }
                    }
                }
            });
        } else {
            lo.imgBtnEdit.setVisibility(View.GONE);
            lo.imgBtnDelete.setVisibility(View.GONE);
            lo.swhEnabled.setVisibility(View.GONE);
        }

        lo.imgRun.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.scenarioBP.runScenario(scenarios[position]);
            }
        });

        if (ActivityScenario.selectedItemPosition != -1 && ActivityScenario.selectedItemPosition == position) {
            lo.layBack.setBackgroundResource(R.drawable.lay_scenario_list_row_selected);
        } else
            lo.layBack.setBackgroundResource(R.drawable.lay_scenario_list_row_normal);

        lo.layBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.log("Run :    lo.layBack.setOnClickListener: " + scenarios[position].iD);
                if (slic != null) {
                    slic.onScenarioListItemClick(scenarios[position].iD, position);
                    lo.layBack.setBackgroundResource(R.drawable.lay_scenario_list_row_selected);
                }
            }
        });
        return row;
    }

    @Override
    public int getCount() {
        int len;
        if (scenarios == null)
            len = 0;
        else
            len = scenarios.length;
        return len;
    }


    @Override
    public Object getItem(int id) {
        return scenarios[id];
    }


    @Override
    public long getItemId(int id) {
        return scenarios[id].iD;
    }


    private boolean checkCanEnable(int position, CO_l_list_scemario lo) {
        boolean hasActivePre, hasActiveResult;
        hasActivePre = scenarios[position].opTimeBase || scenarios[position].opPreGPS || scenarios[position].opPreSwitchBase || scenarios[position].opPreTemperature || scenarios[position].opPreWeather;
        hasActiveResult = scenarios[position].opResultNotify || scenarios[position].opResultSMS || scenarios[position].opResultSwitch;
        if (hasActivePre && hasActiveResult)
            return true;
        else
            return false;
    }

    public void updateList(Struct[] scenarios) {
        this.scenarios = scenarios;
        notifyDataSetChanged();
    }
}
