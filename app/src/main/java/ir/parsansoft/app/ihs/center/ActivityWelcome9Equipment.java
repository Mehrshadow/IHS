package ir.parsansoft.app.ihs.center;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import java.util.ArrayList;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_sections;
import ir.parsansoft.app.ihs.center.AllViews.CO_l_list_sections_room;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.DialogClass.OkListener;
import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewNode;
import ir.parsansoft.app.ihs.center.adapters.AdapterSectionsExpandableList;
import ir.parsansoft.app.ihs.center.adapters.AdapterSectionsExpandableList.onOptionButton;

public class ActivityWelcome9Equipment extends ActivityWizard {

    private AdapterSectionsExpandableList expListAdapter;
    private ArrayList<Database.Section.Struct> expParentItems = new ArrayList<Database.Section.Struct>();
    private ArrayList<Database.Room.Struct[]> expChildItems = new ArrayList<Database.Room.Struct[]>();
    private AdapterListViewNode grdListAdapter;
    private Database.Node.Struct[] nodes;
    private Dialog dlg;
    private CO_f_sections formObjects;
    boolean isBusy = false;

    int                                        selectedRoom    = 0;
    int                                        selectedSection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.f_welcome_equipment);
        formObjects = new CO_f_sections(this);
        translateForm();
        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isBusy) {
                    isBusy = true;
                    startActivity(new Intent(G.currentActivity, ActivityWelcome8FloorandRoom.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        setOnNextListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isBusy) {
                    isBusy = true;
                    startActivity(new Intent(G.currentActivity, ActivityWelcome10Mobile.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        formObjects = new CO_f_sections(this);
        loadSectionsList();

        expListAdapter = new AdapterSectionsExpandableList(expParentItems, expChildItems);
        expListAdapter.hide = true;
        initializeListener();
        expListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        formObjects.elist1.setAdapter(expListAdapter);

        formObjects.elist1.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                CO_l_list_sections_room viewObjects = new CO_l_list_sections_room(v);
                if (parent.isGroupExpanded(groupPosition)) {
                    viewObjects.layBack.setBackgroundResource(R.drawable.lay_sections_exp_head_collapse_selector);
                } else {
                    viewObjects.layBack.setBackgroundResource(R.drawable.lay_sections_exp_head_expand_selector);
                }
                return false;
            }
        });

        // Listview on child click listener
        formObjects.elist1.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                selectedRoom = childPosition;
                selectedSection = groupPosition;
                refreshNodeList();
                return true;
            }
        });

        formObjects.btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startAddNewNode();
            }
        });
    }

    private void initializeListener() {
        expListAdapter.setOnOptionButton(new onOptionButton() {
            @Override
            public void onSectionEdit(Database.Section.Struct section) {

            }

            @Override
            public void onRoomEdit(Database.Room.Struct room) {

            }

            @Override
            public void onRoomDelete(final Database.Room.Struct room) {
                if (Database.Node.select("RoomID=" + room.iD) == null) {
                    // Dialog yes no  238
                    final DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.setOnYesNoListener(new YesNoListener() {
                        @Override
                        public void yesClick() {
                            // Delete room
                            Database.Room.delete(room.iD);
                            dlg.dissmiss();
                            //  Send message to server and local Mobiles
                            NetMessage netMessage = new NetMessage();
                            netMessage.data = room.getRoomDataJson();
                            netMessage.action = NetMessage.Delete;
                            netMessage.type = NetMessage.RoomData;
                            netMessage.typeName = NetMessage.NetMessageType.RoomData;
                            netMessage.messageID = netMessage.save();
                            G.mobileCommunication.sendMessage(netMessage);
                            G.server.sendMessage(netMessage);

                            loadSectionsList();
                            expListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void noClick() {
                            dlg.dissmiss();
                        }
                    });
                    dlg.showYesNo("Message", G.T.getSentence(239));
                } else {
                    // Dialog OK    237
                    final DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.setOnOkListener(new OkListener() {
                        @Override
                        public void okClick() {
                            dlg.dissmiss();
                        }
                    });
                    dlg.showOk("Message", G.T.getSentence(237));
                }
            }

            @Override
            public void onSectionDelete(final Database.Section.Struct section) {
                if (Database.Room.select("SectionID=" + section.iD) == null) {
                    // Dialog yes no  240
                    final DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.setOnYesNoListener(new YesNoListener() {
                        @Override
                        public void yesClick() {
                            // Delete room
                            Database.Section.delete(section.iD);
                            dlg.dissmiss();
                            //  Send message to server and local Mobiles
                            NetMessage netMessage = new NetMessage();
                            netMessage.data = section.getSectionDataJson();
                            netMessage.action = NetMessage.Delete;
                            netMessage.type = NetMessage.SectionData;
                            netMessage.typeName = NetMessageType.SectionData;
                            netMessage.messageID = netMessage.save();
                            G.mobileCommunication.sendMessage(netMessage);
                            G.server.sendMessage(netMessage);
                            loadSectionsList();
                            expListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void noClick() {
                            dlg.dissmiss();
                        }
                    });
                    dlg.showYesNo("Message", G.T.getSentence(240));
                    // Delete Section 
                } else {
                    // Dialog OK   239
                    final DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.setOnOkListener(new OkListener() {
                        @Override
                        public void okClick() {
                            dlg.dissmiss();
                        }
                    });
                    dlg.showOk("Message", G.T.getSentence(238));
                }
            }
        });

    }

    private void loadSectionsList() {
        Database.Section.Struct[] sections;
        sections = Database.Section.select("");
        expParentItems.clear();
        expChildItems.clear();
        if (sections != null) {
            for (int i = 0; i < sections.length; i++) {
                expParentItems.add(sections[i]);
                Database.Room.Struct[] rooms = Database.Room.select("SectionID =" + sections[i].iD);
                expChildItems.add(rooms);
            }
        }
    }

    private void startAddNewNode() {
        if (Database.Section.select("") == null) {
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(201), G.T.getSentence(241));
            return;
        }
        if (Database.Room.select("") == null) {
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(201), G.T.getSentence(242));
            return;
        }
        G.currentActivity.startActivity(new Intent(G.currentActivity, ActivityAddNode_w1.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;

        refreshNodeList();
    }

    private void refreshNodeList() {
        try {
            nodes = Database.Node.select("RoomID=" + expChildItems.get(selectedSection)[selectedRoom].iD);
        }
        catch (Exception e) {
            G.printStackTrace(e);
        }
        grdListAdapter = null;
        if (nodes != null) {
            grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, true);
        }
        formObjects.grdNodes.setAdapter(grdListAdapter);
    }

    @Override
    public void translateForm() {
        super.translateForm();
        formObjects.btnAdd.setText(G.T.getSentence(224));
    }
}
