package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.adapters.AdapterFakeNodeSwitches;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewFakeNode;

public class ActivityAddNode_IoMadule_NodeType extends ActivityEnhanced {

    AllViews.CO_d_section_add_node_NodeType mAdd_node_nodeType;


    private AdapterListViewFakeNode grdListAdapter;
    private AdapterFakeNodeSwitches adapterNodeSwitches;
    private Database.Node.Struct[] nodes;
    private Database.Switch.Struct[] switches;
    ListView lstNode, lstNodeSwitches;
    Button btnNext, btnCancel, btnBack;
    TextView txtTitle;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.activity_add_node__io_madule__node_type);
        else
            setContentView(R.layout.activity_add_node__io_madule__node_type);

        setSideBarVisiblity(false);

        mAdd_node_nodeType = new AllViews.CO_d_section_add_node_NodeType(this);

        mAdd_node_nodeType.lstNode = (ListView) findViewById(R.id.lstNode);
        mAdd_node_nodeType.lstNodeSwitches = (ListView) findViewById(R.id.lstNodeSwitches);
        mAdd_node_nodeType.btnNext = (Button) findViewById(R.id.btnNext);
        mAdd_node_nodeType.btnBack = (Button) findViewById(R.id.btnBack);
        mAdd_node_nodeType.btnCancel = (Button) findViewById(R.id.btnCancel);
        mAdd_node_nodeType.txtTitle = (TextView) findViewById(R.id.txtTitle);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("NODE_ID")) {
                id = extras.getInt("NODE_ID");
                nodes = Database.Node.select("ID=" + id);
                G.log("Node ID=" + id);
            }
        }
        if (nodes != null) {
            grdListAdapter = new AdapterListViewFakeNode(G.currentActivity, nodes, false);
            mAdd_node_nodeType.lstNode.setAdapter(grdListAdapter);
            switches = Database.Switch.select("NodeID = " + id);
            if (switches != null) {
                adapterNodeSwitches = new AdapterFakeNodeSwitches(this, switches, getAvailablePorts());
                mAdd_node_nodeType.lstNodeSwitches.setAdapter(adapterNodeSwitches);
            }
        }
        mAdd_node_nodeType.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (saveForm()) {
                    Intent fw4 = new Intent(G.currentActivity, ActivityAddNode_IoMadule_SelectPlace.class);
                    fw4.putExtra("NODE_ID", id);
                    G.currentActivity.startActivity(fw4);
                    Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                    finish();
                }
            }
        });
        mAdd_node_nodeType.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent fw3 = new Intent(G.currentActivity, ActivityAddNode_IoMadule_Input_Output.class);
                fw3.putExtra("NODE_ID", id);
                Database.Node.delete(id);
                Database.Switch.delete("NodeID=" + id);
                G.currentActivity.startActivity(fw3);
                Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                finish();
            }
        });
        mAdd_node_nodeType.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Database.Node.delete(id);
                Database.Switch.delete("NodeID=" + id);
                finish();
                Animation.doAnimation(Animation.Animation_Types.FADE);
            }
        });
        translateForm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }

    private List<String> getAvailablePorts() {
        try {
            List<String> spinnerPorts = new ArrayList<>();
            List<String> availablePorts = new ArrayList<>();
            availablePorts.add("3");
            availablePorts.add("4");
            availablePorts.add("5");
            availablePorts.add("6");
            availablePorts.add("7");
            availablePorts.add("8");
            availablePorts.add("9");
            availablePorts.add("10");
            availablePorts.add("11");
            availablePorts.add("12");

            Database.Switch.Struct[] fakeswitches = Database.Switch.select("nodeID=" + id);
            if (fakeswitches != null) {
                for (int i = 0; i < fakeswitches.length; i++) {
                    if (fakeswitches[i].IOModulePort > 0 &&
                            fakeswitches[i].IOModulePort < 12) {
                        availablePorts.remove(String.valueOf(fakeswitches[i].IOModulePort));
                    }
                }
            }

            for (int i = 0; i < availablePorts.size(); i++) {
                spinnerPorts.add(String.valueOf(Integer.parseInt(availablePorts.get(i)) - 2));
            }

            //Check Enough Port
            Database.Switch.Struct[] s = Database.Switch.select("NodeID=" + id);
            if (s.length > availablePorts.size()) {
                Intent in_out = new Intent(G.currentActivity, ActivityAddNode_IoMadule_Input_Output.class);
                in_out.putExtra("NODE_ID", nodes[0].iD);
                G.currentActivity.startActivity(in_out);
                finish();
                Animation.doAnimation(Animation.Animation_Types.FADE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(201), G.T.getSentence(858));
                    }
                }, 500);

                return null;
            }
            return spinnerPorts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void translateForm() {
        super.translateForm();
        mAdd_node_nodeType.txtTitle.setText(G.T.getSentence(226));
        mAdd_node_nodeType.btnCancel.setText(G.T.getSentence(102));
        mAdd_node_nodeType.btnBack.setText(G.T.getSentence(104));
        mAdd_node_nodeType.btnNext.setText(G.T.getSentence(103));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
    }

    private boolean saveForm() {
        Database.Switch.Struct[] switchItems = adapterNodeSwitches.getSwitchItems();
        if (switchItems != null && switchItems.length > 0) {
            for (int i = 0; i < switchItems.length; i++) {
                if (switchItems[i].name.trim().length() == 0) {
                    new DialogClass(G.context).showOk(G.T.getSentence(151), G.T.getSentence(221));
                    return false;
                }
            }

            for (int i = 0; i < switchItems.length; i++) {
                switch (switchItems.length) {
                    case 1:
                        break;
                    case 2:
                        if (
                                (switchItems[0].IOModulePort == (switchItems[1].IOModulePort))
                                ) {
                            new DialogClass(G.context).showOk(G.T.getSentence(151), G.T.getSentence(248));
                            return false;
                        }
                        break;
                    case 3:
                        if (
                                (switchItems[0].IOModulePort == (switchItems[1].IOModulePort))
                                        || (switchItems[0].IOModulePort == (switchItems[2].IOModulePort))
                                        || (switchItems[1].IOModulePort == (switchItems[2].IOModulePort))
                                ) {
                            new DialogClass(G.context).showOk(G.T.getSentence(151), G.T.getSentence(248));
                            return false;
                        }
                        break;
                }

            }


            NetMessage nm = new NetMessage();
            nm.action = NetMessage.Update;
            nm.type = NetMessage.SwitchData;
            JSONArray ja = new JSONArray();
            Database.Switch.Struct[] switchValues = Database.Switch.select("NodeID = " + id);
            for (int i = 0; i < switchItems.length; i++) {
                switchItems[i].value = switchValues[i].value;
//                switchItems[i].IOModulePort = switchValues[i].IOModulePort;
                Database.Switch.edit(switchItems[i]);
                JSONObject jo = new JSONObject();
                try {
                    jo.put("ID", switchItems[i].iD);
                    jo.put("Name", switchItems[i].name);
                    jo.put("Code", switchItems[i].code);
                    jo.put("Value", switchItems[i].value);
                    jo.put("NodeID", switchItems[i].nodeID);
                    jo.put("IOModulePort", switchItems[i].IOModulePort);
                    ja.put(jo);
                } catch (JSONException e) {
                    G.printStackTrace(e);
                }
            }
            nm.data = ja.toString();
            nm.messageID = nm.save();
            SysLog.log("Device " + nodes[0].name + " Edited.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.NODE, id);
            G.mobileCommunication.sendMessage(nm);
            G.server.sendMessage(nm);
            return true;
        } else {
            return false;
        }
    }
}
