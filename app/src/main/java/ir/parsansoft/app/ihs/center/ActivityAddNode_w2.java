package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;

import ir.parsansoft.app.ihs.center.AllViews.CO_d_section_add_node_w2;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewNode;
import ir.parsansoft.app.ihs.center.adapters.AdapterRoomSpinner;
import ir.parsansoft.app.ihs.center.adapters.AdapterSectionSpinner;

import static android.R.attr.name;


public class ActivityAddNode_w2 extends ActivityEnhanced implements CompoundButton.OnCheckedChangeListener {
    private AdapterListViewNode grdListAdapter;
    private Database.Node.Struct[] nodes;
    CO_d_section_add_node_w2 fw2;

    AdapterSectionSpinner adapterSectionSpinner;
    AdapterRoomSpinner adapterRoomSpinner;

    private boolean isMyHouseCheckBoxChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_section_add_node_w2);
        else
            setContentView(R.layout.f_section_add_node_w2_rtl);

        setSideBarVisiblity(false);
        G.log("hide Sidebar");
        fw2 = new CO_d_section_add_node_w2(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("NODE_ID")) {
                int id = extras.getInt("NODE_ID");
                nodes = Database.Node.select("ID=" + id + " LIMIT 1");
            }
        }

        if (nodes == null) {
            fw2.btnNext.setVisibility(View.INVISIBLE);
            fw2.btnDelete.setVisibility(View.INVISIBLE);
            return;
        }

        grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, false);
        fw2.listView1.setAdapter(grdListAdapter);
        loadSpinners();
        translateForm();
        initializeForm();
    }

    private void initializeForm() {
        fw2.edtNodeName.setText(nodes[0].name);
        fw2.icnNodeIcon.setImageDir(G.DIR_ICONS_NODES);
        fw2.icnNodeIcon.setImageToSelector(nodes[0].icon);
        fw2.checkBoxMyHouse.setOnCheckedChangeListener(this);
    }

    Database.Section.Struct[] sections;
    Database.Room.Struct[] rooms;

    private void changeSpinnersVisibility() {
        if (isMyHouseCheckBoxChecked) {
            fw2.spnSections.setVisibility(View.INVISIBLE);
            fw2.spnRooms.setVisibility(View.INVISIBLE);
            fw2.lblRoom.setVisibility(View.INVISIBLE);
            fw2.lblSection.setVisibility(View.INVISIBLE);
        } else {
            fw2.spnSections.setVisibility(View.VISIBLE);
            fw2.spnRooms.setVisibility(View.VISIBLE);
            fw2.lblRoom.setVisibility(View.VISIBLE);
            fw2.lblSection.setVisibility(View.VISIBLE);
        }
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

        fw2.spnSections.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
                rooms = Database.Room.select("SectionID=" + adapterSectionSpinner.getItemId(position));
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        fw2.btnNext.setOnClickListener(new OnClickListener() {
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
                if (isMyHouseCheckBoxChecked) {
                    nodes[0].roomID = -1;
                } else {
                    nodes[0].roomID = adapterRoomSpinner.getItem(fw2.spnRooms.getSelectedItemPosition()).iD;
                }


                nodes[0].name = fw2.edtNodeName.getText().toString().trim();
                nodes[0].icon = fw2.icnNodeIcon.getSelectedIconName();
                Database.Node.edit(nodes[0]);
                G.nodeCommunication.allNodes.get(nodes[0].iD).refreshNodeStruct();

                SysLog.log("Device :" + nodes[0].name + " Edited.", LogType.DATA_CHANGE, LogOperator.NODE, nodes[0].iD);

                //  Send message to server and local Mobiles
                NetMessage netMessage = new NetMessage();
                netMessage.data = nodes[0].getNodeDataJson();
                netMessage.action = NetMessage.Update;
                netMessage.type = NetMessage.NodeData;
                netMessage.typeName = NetMessageType.NodeData;
                netMessage.messageID = netMessage.save();
                G.log("Send Data to mobiles !");
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);

                if (nodes[0].isIoModuleNode == 1) {
                    Intent fw3 = new Intent(G.currentActivity, ActivityAddNode_w3.class);// now go to add module wizard...
                    fw3.putExtra("NODE_ID", nodes[0].iD);
                    G.currentActivity.startActivity(fw3);
                } else {
                    Intent fw3 = new Intent(G.currentActivity, ActivityAddNode_w3.class);
                    fw3.putExtra("NODE_ID", nodes[0].iD);
                    G.currentActivity.startActivity(fw3);
                }
                Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                finish();
            }
        });
        fw2.btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        fw2.btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                G.log("Going to delete the node :" + nodes[0].iD);
                final DialogClass dlg = new DialogClass(G.currentActivity);
                // check if any of scenarios has used this node?
                Database.Switch.Struct[] sws = Database.Switch.select("NodeID=" + nodes[0].iD);
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


                dlg.setOnYesNoListener(new YesNoListener() {
                    @Override
                    public void yesClick() {
                        // going to delete selected node and its switch
                        //  Send message to server and local Mobiles
                        NetMessage netMessage = new NetMessage();
                        netMessage.data = nodes[0].getNodeDataJson();
                        netMessage.action = NetMessage.Delete;
                        netMessage.type = NetMessage.NodeData;
                        netMessage.typeName = NetMessageType.NodeData;
                        netMessage.messageID = netMessage.save();
                        G.mobileCommunication.sendMessage(netMessage);
                        G.server.sendMessage(netMessage);

                        NetMessage netMessage2 = new NetMessage();
                        netMessage2.data = nodes[0].getNodeSwitchesDataJson();
                        netMessage2.action = NetMessage.Delete;
                        netMessage2.type = NetMessage.SwitchData;
                        netMessage2.typeName = NetMessageType.SwitchData;
                        netMessage2.messageID = netMessage2.save();
                        G.mobileCommunication.sendMessage(netMessage2);
                        G.server.sendMessage(netMessage2);

                        SysLog.log("Device :" + nodes[0].name + " Deleted.", LogType.DATA_CHANGE, LogOperator.NODE, nodes[0].iD);

                        G.nodeCommunication.allNodes.get(nodes[0].iD).distroyNode();
                        G.nodeCommunication.allNodes.remove(nodes[0].iD);
                        Database.Node.delete(nodes[0].iD);
                        Database.Switch.delete("NodeID=" + nodes[0].iD);
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
        fw2.lblMyHouse.setText(G.T.getSentence(849));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isMyHouseCheckBoxChecked = isChecked;
        changeSpinnersVisibility();
    }
}
