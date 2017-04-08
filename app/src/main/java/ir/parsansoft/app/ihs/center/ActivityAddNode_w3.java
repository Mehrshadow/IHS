package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewNode;
import ir.parsansoft.app.ihs.center.adapters.AdapterNodeSwitches;


public class ActivityAddNode_w3 extends ActivityEnhanced {


    private AdapterListViewNode      grdListAdapter;
    private AdapterNodeSwitches      adapterNodeSwitches;
    private Database.Node.Struct[]   nodes;
    private Database.Switch.Struct[] switches;
    ListView                         lstNode, lstNodeSwitches;
    Button                           btnNext, btnCancel, btnBack;
    TextView                         txtTitle;
    int                              id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_section_add_node_w3);
        else
            setContentView(R.layout.f_section_add_node_w3_rtl);

        setSideBarVisiblity(false);

        lstNode = (ListView) findViewById(R.id.lstNode);
        lstNodeSwitches = (ListView) findViewById(R.id.lstNodeSwitches);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("NODE_ID")) {
                id = extras.getInt("NODE_ID");
                nodes = Database.Node.select("ID=" + id);
                G.log("Node ID=" + id);
            }
        }
        if (nodes != null) {
            grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, false);
            lstNode.setAdapter(grdListAdapter);
            switches = Database.Switch.select("NodeID = " + id);
            if (switches != null) {
                adapterNodeSwitches = new AdapterNodeSwitches(this, switches);
                lstNodeSwitches.setAdapter(adapterNodeSwitches);
            }
        }
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (saveForm()) {
                    Intent fw4 = new Intent(G.currentActivity, ActivityAddNode_w4.class);
                    fw4.putExtra("NODE_ID", id);
                    G.currentActivity.startActivity(fw4);
                    Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                    finish();
                }
            }
        });
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (saveForm()) {
                    Intent fw3 = new Intent(G.currentActivity, ActivityAddNode_w2.class);
                    fw3.putExtra("NODE_ID", id);
                    G.currentActivity.startActivity(fw3);
                    Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                    finish();
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
        translateForm();
    }
    @Override
    public void translateForm() {
        super.translateForm();
        txtTitle.setText(G.T.getSentence(226));
        btnCancel.setText(G.T.getSentence(102));
        btnBack.setText(G.T.getSentence(104));
        btnNext.setText(G.T.getSentence(103));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
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
            NetMessage nm = new NetMessage();
            nm.action = NetMessage.Update;
            nm.type = NetMessage.SwitchData;
            nm.typeName = NetMessage.NetMessageType.SwitchData;
            JSONArray ja = new JSONArray();
            Database.Switch.Struct[] switchValues = Database.Switch.select("NodeID = " + id);
            for (int i = 0; i < switchItems.length; i++) {
                switchItems[i].value = switchValues[i].value;
                Database.Switch.edit(switchItems[i]);
                JSONObject jo = new JSONObject();
                try {
                    jo.put("ID", switchItems[i].iD);
                    jo.put("Name", switchItems[i].name);
                    jo.put("Code", switchItems[i].code);
                    jo.put("Value", switchItems[i].value);
                    jo.put("NodeID", switchItems[i].nodeID);
                    ja.put(jo);
                }
                catch (JSONException e) {
                    G.printStackTrace(e);
                }
            }
            nm.data = ja.toString();
            nm.messageID = nm.save();
            SysLog.log("Device " + nodes[0].name + " Edited.", LogType.DATA_CHANGE, LogOperator.NODE, id);
            G.mobileCommunication.sendMessage(nm);
            G.server.sendMessage(nm);
            return true;
        }
        else {
            return false;
        }
    }


}
