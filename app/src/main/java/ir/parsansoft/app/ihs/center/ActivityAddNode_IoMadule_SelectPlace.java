package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import ir.parsansoft.app.ihs.center.adapters.AdapterListViewNode;
import ir.parsansoft.app.ihs.center.adapters.AdapterRoomSpinner;
import ir.parsansoft.app.ihs.center.adapters.AdapterSectionSpinner;

/**
 * Created by HaMiD on 2/14/2017.
 */

public class ActivityAddNode_IoMadule_SelectPlace extends ActivityEnhanced {
    private AdapterListViewNode grdListAdapter;
    private Database.Node.Struct[] nodes;
    AllViews.CO_d_section_add_node_w2 fw2;

    AdapterSectionSpinner adapterSectionSpinner;
    AdapterRoomSpinner adapterRoomSpinner;
    int sensorNodeId;
    int deviceNodeId;
    int ioNodeId;
    int node_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_section_add_node_w2);
        else
            setContentView(R.layout.f_section_add_node_w2_rtl);

        setSideBarVisiblity(false);
        G.log("hide Sidebar");
        fw2 = new AllViews.CO_d_section_add_node_w2(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            sensorNodeId = extras.getInt("SENSOR_NODE_ID");
            deviceNodeId = extras.getInt("DEVICE_NODE_ID");
            ioNodeId = extras.getInt("IO_NODE_ID");

            if (sensorNodeId != 0) {
                nodes = Database.Node.select("iD=" + sensorNodeId + " LIMIT 1");

            } else if (deviceNodeId != 0) {
                nodes = Database.Node.select("iD=" + deviceNodeId + " LIMIT 1");

            }
            node_type = extras.getInt("NODE_Type");
        }

        if (nodes == null) {
            fw2.btnNext.setVisibility(View.INVISIBLE);
            fw2.btnDelete.setVisibility(View.INVISIBLE);
            return;
        }

        grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, false);
        fw2.listView1.setAdapter(grdListAdapter);
        try {
            loadSpinners();

        } catch (Exception e) {
            e.printStackTrace();
        }
        translateForm();
        initializeForm();

    }

    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }

    private void initializeForm() {
        fw2.edtNodeName.setText(nodes[0].name);
        fw2.icnNodeIcon.setImageDir(G.DIR_ICONS_NODES);
        fw2.icnNodeIcon.setImageToSelector(nodes[0].icon);
//        fw2.checkBoxMyHouse.setOnCheckedChangeListener(this);

        fw2.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (nodes == null)
                    return;
//                if (fw2.checkBoxMyHouse.isChecked()) {
//                    nodes[0].roomID = AllNodes.myHouseDefaultRoomId;// -1
//                } else {
                if (fw2.spnRooms.getSelectedItemPosition() < 0) {
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(201), G.T.getSentence(218));
                    return;
                }
                if (fw2.edtNodeName.getText().toString().trim().length() == 0) {
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(201), G.T.getSentence(219));
                    return;
                }


                nodes[0].roomID = adapterRoomSpinner.getItem(fw2.spnRooms.getSelectedItemPosition()).iD;
                nodes[0].name = fw2.edtNodeName.getText().toString().trim();
                nodes[0].icon = fw2.icnNodeIcon.getSelectedIconName();
                Database.Node.edit(nodes[0]);
                G.nodeCommunication.allNodes.get(nodes[0].iD).refreshNodeStruct();

                SysLog.log("Device :" + nodes[0].name + " Edited.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.NODE, nodes[0].iD);

                //  Send message to server and local Mobiles
                NetMessage netMessage = new NetMessage();
                netMessage.data = nodes[0].getNodeDataJson();
                netMessage.action = NetMessage.Update;
                netMessage.type = NetMessage.NodeData;
                netMessage.typeName = NetMessage.NetMessageType.NodeData;
                netMessage.messageID = netMessage.save();
                G.log("Send Data to mobiles !");
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);

//                if (nodes[0].nodeTypeID == AllNodes.Node_Type.IOModule) {
                Intent fw3 = new Intent(G.currentActivity, ActivityAddNode_w4.class);// now go to add module wizard...
                fw3.putExtra("SENSOR_NODE_ID", sensorNodeId);
                fw3.putExtra("DEVICE_NODE_ID", deviceNodeId);
                fw3.putExtra("IO_NODE_ID", ioNodeId);
                fw3.putExtra("NODE_Type", node_type);

                G.currentActivity.startActivity(fw3);
//                } else {
//                    Intent fw3 = new Intent(G.currentActivity, ActivityAddNode_w3.class);
//                    fw3.putExtra("SENSOR_NODE_ID", nodes[0].iD);
//                    G.currentActivity.startActivity(fw3);
//                }
                Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                finish();
            }
        });

        fw2.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (node_type == AllNodes.Node_Type.Sensor_Magnetic | node_type == AllNodes.Node_Type.Sensor_SMOKE) {
                    Intent mAdd_node_input_output = new Intent(G.currentActivity, ActivityAddNode_IOModule_SensorType.class);
                    mAdd_node_input_output.putExtra("SENSOR_NODE_ID", sensorNodeId);
                    mAdd_node_input_output.putExtra("IO_NODE_ID", ioNodeId);
                    mAdd_node_input_output.putExtra("NODE_Type", node_type);
                    try {
                        Database.Node.delete(sensorNodeId);
                        Database.Switch.delete("nodeID=" + sensorNodeId);
                    } catch (Exception e) {
                        G.printStackTrace(e);
                    }

                    G.currentActivity.startActivity(mAdd_node_input_output);
                } else {
                    Intent mAdd_node_input_output = new Intent(G.currentActivity, ActivityAddNode_IoMadule_NodeType.class);
                    mAdd_node_input_output.putExtra("NODE_Type", node_type);
                    mAdd_node_input_output.putExtra("IO_NODE_ID", ioNodeId);
                    mAdd_node_input_output.putExtra("DEVICE_NODE_ID", deviceNodeId);
                    G.currentActivity.startActivity(mAdd_node_input_output);
                }

                Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                finish();
            }
        });

        fw2.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                Database.Node.delete(sensorNodeId);
//                Database.Switch.delete("NodeID=" + sensorNodeId);
                finish();
                Animation.doAnimation(Animation.Animation_Types.FADE);
            }
        });
        fw2.btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                G.log("Going to delete the node :" + nodes[0].iD);
                final DialogClass dlg = new DialogClass(G.currentActivity);
                // check if any of scenarios has used this node?
                Database.Switch.Struct[] sws = Database.Switch.select("nodeID=" + nodes[0].iD);
                if (sws != null) {
                    String swsIDs = "";
                    for (int i = 0; i < sws.length; i++) {
                        swsIDs += sws[i].iD + ",";
                    }
                    swsIDs = swsIDs.substring(0, swsIDs.length() - 1);
                    Database.PreOperand.Struct snops[] = Database.PreOperand.select("SwitchID IN (" + swsIDs + ")");
                    Database.Results.Struct snrts[] = Database.Results.select("SwitchID IN (" + swsIDs + ")");
                    String message = "";
                    if (snops != null || snrts != null) {
                        message += G.T.getSentence(826);
                        if (snops != null) {
                            G.log("SELECT * FROM T_PreOperand WHERE SwitchID IN (" + swsIDs + ") : affected rows:" + snops.length);
                            message += "\n";
                            for (int i = 0; i < snops.length; i++) {
                                Database.Scenario.Struct snr = Database.Scenario.select(snops[i].scenarioID);
                                if (snr != null)
                                    message += "\n" + G.T.getSentence(601) + " : " + snr.name;
                            }
                        }
                        if (snrts != null) {
                            G.log("SELECT * FROM T_Results WHERE SwitchID IN (" + swsIDs + ") : affected rows:" + snrts.length);
                            message += "\n";
                            for (int i = 0; i < snrts.length; i++) {
                                Database.Scenario.Struct snr = Database.Scenario.select(snrts[i].scenarioID);
                                if (snr != null)
                                    message += "\n" + G.T.getSentence(612) + " : " + snr.name;
                            }
                        }
                        dlg.showOk(G.T.getSentence(228), message);
                        return;
                    }
                }


                dlg.setOnYesNoListener(new DialogClass.YesNoListener() {
                    @Override
                    public void yesClick() {
                        // going to delete selected node and its switch
                        //  Send message to server and local Mobiles
                        NetMessage netMessage = new NetMessage();
                        netMessage.data = nodes[0].getNodeDataJson();
                        netMessage.action = NetMessage.Delete;
                        netMessage.type = NetMessage.NodeData;
                        netMessage.typeName = NetMessage.NetMessageType.NodeData;
                        netMessage.messageID = netMessage.save();
                        G.mobileCommunication.sendMessage(netMessage);
                        G.server.sendMessage(netMessage);

                        NetMessage netMessage2 = new NetMessage();
                        netMessage2.data = nodes[0].getNodeSwitchesDataJson();
                        netMessage2.action = NetMessage.Delete;
                        netMessage2.type = NetMessage.SwitchData;
                        netMessage2.typeName = NetMessage.NetMessageType.SwitchData;
                        netMessage2.messageID = netMessage2.save();
                        G.mobileCommunication.sendMessage(netMessage2);
                        G.server.sendMessage(netMessage2);

                        SysLog.log("Device :" + nodes[0].name + " Deleted.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.NODE, nodes[0].iD);

                        G.nodeCommunication.allNodes.get(nodes[0].iD).distroyNode();
                        G.nodeCommunication.allNodes.remove(nodes[0].iD);
                        Database.Node.delete(nodes[0].iD);
                        Database.Switch.delete("nodeID=" + nodes[0].iD);
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
    }

    Database.Section.Struct[] sections;
    Database.Room.Struct[] rooms;

    private void changeSpinnersVisibility() {
        fw2.spnSections.setVisibility(View.VISIBLE);
        fw2.spnRooms.setVisibility(View.VISIBLE);
        fw2.lblRoom.setVisibility(View.VISIBLE);
        fw2.lblSection.setVisibility(View.VISIBLE);

    }

    private void loadSpinners() {
        sections = Database.Section.select("");

        adapterSectionSpinner = new AdapterSectionSpinner(sections);
        fw2.spnSections.setAdapter(adapterSectionSpinner);


        /***/
        // gereftane section marbut be room morede nazar
        final Database.Room.Struct[] currentRoom = Database.Room.select("iD =" + nodes[0].roomID + " LIMIT 1");
        G.log("Payam : currentRoom name is " + currentRoom[0].name + " and section sensorNodeId is " + currentRoom[0].sectionID);
        fw2.spnSections.setSelection(adapterSectionSpinner.getSectionPositionById(currentRoom[0].sectionID));

        fw2.spnSections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
                rooms = Database.Room.select("sectionID=" + adapterSectionSpinner.getItemId(position));
                if (rooms != null) {
                    adapterRoomSpinner = new AdapterRoomSpinner(rooms);
                    fw2.spnRooms.setAdapter(adapterRoomSpinner);
                    adapterRoomSpinner.notifyDataSetChanged();
                    fw2.spnRooms.setSelection(adapterRoomSpinner.getRoomPositionById(currentRoom[0].iD));
                } else {
                    rooms = new Database.Room.Struct[0];
                    adapterRoomSpinner = new AdapterRoomSpinner(rooms);
                    fw2.spnRooms.setAdapter(adapterRoomSpinner);
                    adapterRoomSpinner.notifyDataSetChanged();
                }

                nodes[0].roomID = adapterRoomSpinner.getItem(fw2.spnRooms.getSelectedItemPosition()).iD;
                nodes[0].name = fw2.edtNodeName.getText().toString().trim();
                nodes[0].icon = fw2.icnNodeIcon.getSelectedIconName();
                Database.Node.edit(nodes[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


    }

    @Override
    public void translateForm() {
        super.translateForm();
        fw2.lblSection.setText(G.T.getSentence(2201));
        fw2.lblRoom.setText(G.T.getSentence(2202));
        fw2.lblName.setText(G.T.getSentence(230));
        fw2.btnCancel.setText(G.T.getSentence(102));
        fw2.btnNext.setText(G.T.getSentence(103));
        fw2.txtTitle.setText(G.T.getSentence(227));
        fw2.btnDelete.setText(G.T.getSentence(106));
//        fw2.checkBoxMyHouse.setText(G.T.getSentence(849));
        fw2.lblMyHouse.setText(G.T.getSentence(849));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation.Animation_Types.FADE);
    }
}
