package ir.parsansoft.app.ihs.center;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ir.parsansoft.app.ihs.center.adapters.AdapterListViewNode;


public class ActivityMyHouse extends ActivityEnhanced implements View.OnClickListener {

    private AdapterListViewNode grdListAdapter;
    private Database.Node.Struct[] nodes;
    private Database.Node.Struct[] ioNode;
    private AllViews.CO_f_MyHouse fo;

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.activity_my_house);
        else
            setContentView(R.layout.activity_my_house);

        fo = new AllViews.CO_f_MyHouse(this);
        translateForm();
        setSideBarVisiblity(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("NODE_ID")) {
                id = extras.getInt("NODE_ID");
                ioNode = Database.Node.select("ID=" + id);
                G.log("Node ID=" + id);
            }
        }

        fo.btnDeleteIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                G.log("Going to delete the node :" + nodes[0].iD);
                final DialogClass dlg = new DialogClass(G.currentActivity);
                // check if any of scenarios has used this node?
                final Database.Node.Struct[] ioNode = Database.Node.select("ip='" + nodes[0].iP + "'");
                for (int i = 0; i < ioNode.length; i++) {
                    Database.Switch.Struct[] sws = Database.Switch.select("NodeID=" + ioNode[i].iD);
                    if (sws != null) {
                        String swsIDs = "";
                        for (int k = 0; k < sws.length; k++) {
                            swsIDs += sws[k].iD + ",";
                        }
                        swsIDs = swsIDs.substring(0, swsIDs.length() - 1);
                        Database.PreOperand.Struct snops[] = Database.PreOperand.select("switchID IN (" + swsIDs + ")");
                        Database.Results.Struct snrts[] = Database.Results.select("SwitchID IN (" + swsIDs + ")");
                        String message = "";
                        if (snops != null || snrts != null) {
                            message += G.T.getSentence(826);
                            if (snops != null) {
                                G.log("SELECT * FROM T_PreOperand WHERE SwitchID IN (" + swsIDs + ") : affected rows:" + snops.length);
                                message += "\n";
                                for (int j = 0; j < snops.length; j++) {
                                    Database.Scenario.Struct snr = Database.Scenario.select(snops[j].scenarioID);
                                    if (snr != null)
                                        message += "\n" + G.T.getSentence(601) + " : " + snr.name;
                                }
                            }
                            if (snrts != null) {
                                G.log("SELECT * FROM T_Results WHERE SwitchID IN (" + swsIDs + ") : affected rows:" + snrts.length);
                                message += "\n";
                                for (int h = 0; h < snrts.length; h++) {
                                    Database.Scenario.Struct snr = Database.Scenario.select(snrts[h].scenarioID);
                                    if (snr != null)
                                        message += "\n" + G.T.getSentence(612) + " : " + snr.name;
                                }
                            }
                            dlg.showOk(G.T.getSentence(228), message);
                            return;
                        }
                    }
                }


                dlg.setOnYesNoListener(new DialogClass.YesNoListener() {
                    @Override
                    public void yesClick() {
                        // going to delete selected node and its switch
                        //  Send message to server and local Mobiles
                        //به جای اینکه برای هر موبایل چند پیام حذف نود ارسال شود، یک پیام RefreshData ارسال میکنیم
                        Database.Mobiles.Struct[] mobile = Database.Mobiles.select("");
                        if (mobile != null && mobile.length == 0) {
                            for (int i = 0; i < mobile.length; i++) {
                                String result = Database.generateNewMobileConfiguration(mobile[i]);
                                // Send message to local Mobile
                                NetMessage netMessage = new NetMessage();
                                netMessage.data = "[" + result + "]";
                                netMessage.action = NetMessage.Update;
                                netMessage.type = NetMessage.RefreshData;
                                netMessage.typeName = NetMessage.NetMessageType.RefreshData;
                                netMessage.messageID = 0;
                                G.mobileCommunication.sendMessage(netMessage);
                                G.server.sendMessage(netMessage);
                                G.log("MessageParser", "Refresh Data has completed .....................");
                            }
                        }


                        SysLog.log("Device :" + nodes[0].name + " Deleted.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.NODE, nodes[0].iD);
                        Database.Node.Struct[] ioNodes = Database.Node.select("iP='" + nodes[0].iP + "'" );

                        if (ioNodes != null) {
                            for (int i = 0; i < ioNodes.length; i++) {
                                try {
                                    Database.Node.delete("iD=" + ioNodes[i].iD);
                                    Database.Switch.delete("nodeID='" + ioNodes[i].iP + "'");
                                } catch (Exception e) {
                                    G.printStackTrace(e);
                                    continue;
                                }

                            }
                        }
                        finish();
                    }

                    @Override
                    public void noClick() {
                        dlg.dissmiss();
                    }
                });
                dlg.showYesNo(G.T.getSentence(228), G.T.getSentence(229));


            }

        });


        if (G.currentUser != null && G.currentUser.rol == 1)

        {
            fo.btnAddNode.setVisibility(View.VISIBLE);
            fo.btnAddNode.setOnClickListener(this);
            fo.btnBack.setOnClickListener(this);
        } else

        {
            fo.btnAddNode.setVisibility(View.GONE);
        }

    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.btnAddNode.setText(G.T.getSentence(249));
        fo.btnBack.setText(G.T.getSentence(104));
        fo.btnDeleteIO.setText(G.T.getSentence(251));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ActivityMain.class));
        Animation.doAnimation(Animation.Animation_Types.FADE);
        this.finish();
    }

    private void refreshNodeList() {
        try {
            nodes = Database.Node.select("iP='" + ioNode[0].iP + "' Except Select * From T_Node Where nodeTypeID = " + AllNodes.Node_Type.IOModule);
            G.log("nodes Size  = " + nodes.length);
        } catch (Exception e) {
            G.printStackTrace(e);
        }
        grdListAdapter = null;
        if (nodes != null) {
            grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, true, 4);
//            grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, true);
        }
        fo.grdNodes.setAdapter(grdListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
        G.context = this;
        refreshNodeList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddNode:
                Intent intent = new Intent(G.currentActivity, ActivityAddNode_IoMadule_Input_Output.class);
                intent.putExtra("NODE_ID", id);
                Animation.doAnimation(Animation.Animation_Types.FADE);
                startActivity(intent);
                break;

            case R.id.btnBack:
                startActivity(new Intent(G.currentActivity, ActivitySections.class));
                Animation.doAnimation(Animation.Animation_Types.FADE);
                break;

            case R.id.btn_io_module:
                startActivity(new Intent(G.currentActivity, ActivityAddNode_IoMadule_Input_Output.class));
                Animation.doAnimation(Animation.Animation_Types.FADE);
                break;
        }
    }
}
