package ir.parsansoft.app.ihs.center;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.AllViews.CO_d_add_item_to_expandable;
import ir.parsansoft.app.ihs.center.AllViews.CO_d_section_add_room;
import ir.parsansoft.app.ihs.center.AllViews.CO_d_section_add_section;
import ir.parsansoft.app.ihs.center.AllViews.CO_f_sections;
import ir.parsansoft.app.ihs.center.AllViews.CO_l_list_sections_room;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.DialogClass.OkListener;
import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewNode;
import ir.parsansoft.app.ihs.center.adapters.AdapterSectionsExpandableList;
import ir.parsansoft.app.ihs.center.adapters.AdapterSectionsExpandableList.onOptionButton;

public class ActivitySections extends ActivityEnhanced implements OnClickListener {

    private AdapterSectionsExpandableList expListAdapter;
    private ArrayList<Database.Section.Struct> expParentItems = new ArrayList<Database.Section.Struct>();
    private ArrayList<Database.Room.Struct[]> expChildItems = new ArrayList<Database.Room.Struct[]>();
    private AdapterListViewNode grdListAdapter;
    private Database.Node.Struct[] nodes;
    private Dialog dlg;
    private CO_f_sections fo;

    int selectedRoom = 0;
    int selectedSection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_sections);
        fo = new CO_f_sections(this);
        loadSectionsList();
        changeMenuIconBySelect(2);
        translateForm();
        expListAdapter = new AdapterSectionsExpandableList(expParentItems, expChildItems);
        initializeListener();
        expListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        fo.elist1.setAdapter(expListAdapter);

        fo.elist1.setOnGroupClickListener(new OnGroupClickListener() {
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

        // Listview Group expanded listener
        fo.elist1.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(), parentItems.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        fo.elist1.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), parentItems.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();
            }
        });

        // Listview on child click listener
        fo.elist1.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                selectedRoom = childPosition;
                selectedSection = groupPosition;
                refreshNodeList();
                return true;
            }
        });
        if (G.currentUser != null && G.currentUser.rol == 1) {
            fo.btnAdd.setVisibility(View.VISIBLE);
            fo.btnAdd.setOnClickListener(this);
        } else {
            fo.btnAdd.setVisibility(View.GONE);
        }
    }

    private void initializeListener() {
        expListAdapter.setOnOptionButton(new onOptionButton() {
            @Override
            public void onSectionEdit(Database.Section.Struct section) {
                startEditSection(section);
            }

            @Override
            public void onRoomEdit(Database.Room.Struct room) {
                startEditRoom(room);
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
                            SysLog.log("Room " + room.name + " Deleted.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                            dlg.dissmiss();
                            //  Send message to server and local Mobiles
                            NetMessage netMessage = new NetMessage();
                            netMessage.data = room.getRoomDataJson();
                            netMessage.action = NetMessage.Delete;
                            netMessage.type = NetMessage.RoomData;
                            netMessage.typeName = NetMessageType.RoomData;
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
                    dlg.showYesNo(G.T.getSentence(156), G.T.getSentence(239));
                } else {
                    // Dialog OK    237
                    final DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.setOnOkListener(new OkListener() {
                        @Override
                        public void okClick() {
                            dlg.dissmiss();
                        }
                    });
                    dlg.showOk(G.T.getSentence(156), G.T.getSentence(237));
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
                            // Delete section
                            Database.Section.delete(section.iD);
                            SysLog.log("Room " + section.name + " Deleted.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
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
                    dlg.showYesNo(G.T.getSentence(155), G.T.getSentence(240));
                } else {

                    final DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.setOnOkListener(new OkListener() {
                        @Override
                        public void okClick() {
                            dlg.dissmiss();
                        }
                    });
                    dlg.showOk(G.T.getSentence(155), G.T.getSentence(238));
                }
            }
        });
        fo.btnMyHouse.setOnClickListener(this);
        fo.btnIOModule.setOnClickListener(this);
    }

    public void showAddMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add(1, 1, 0, G.T.getSentence(2201));
        popup.getMenu().add(1, 2, 1, G.T.getSentence(2202));
        popup.getMenu().add(1, 3, 2, G.T.getSentence(2203));

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                switch (arg0.getItemId()) {
                    case 1: // Add new Section
                        startAddNewSection();
                        return true;
                    case 2: // Add new Room
                        startAddNewRoom();
                        return true;
                    case 3: // Add new Node
                        startAddNewNode();
                        return true;
                    default:
                        return false;
                }
            }

        });
        popup.show();
    }

    private void startAddNewSection() {
        dlg = new Dialog(G.currentActivity);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            dlg.setContentView(R.layout.d_section_add_section);
        else
            dlg.setContentView(R.layout.d_section_add_section_rtl);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dlg.setCancelable(true);
        final CO_d_section_add_section dlgObjects = new CO_d_section_add_section(dlg);
        try {
            dlgObjects.icnIcon.setImageDir(G.DIR_ICONS_SECTIONS);
            dlgObjects.icnIcon.setImageToSelector(getAssets().list(G.DIR_ICONS_SECTIONS)[0]);
        } catch (IOException e1) {
            G.printStackTrace(e1);
        }
        dlgObjects.btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    String iconName = "";
                    int sort = 0;
                    try {

                        iconName = dlgObjects.icnIcon.getSelectedIconName();//getAssets().list(G.DIR_ICONS_SECTIONS)[dlgObjects.icnIcon.getSelectedIconPosition()];
                    } catch (Exception e) {
                    }
                    try {
                        sort = Database.Section.getMax("Sort", "").sort + 1;
                    } catch (Exception e) {
                    }

                    if (!validateName(dlgObjects.edtName.getText().toString()))
                        return;

                    Database.Section.Struct newSection = new Database.Section.Struct();
                    newSection.name = dlgObjects.edtName.getText().toString();
                    newSection.icon = iconName;
                    newSection.sort = sort;
                    newSection.iD = (int) Database.Section.insert(newSection);
                    SysLog.log("Section " + newSection.name + " Created.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                    //  Send message to server and local Mobiles
                    NetMessage netMessage = new NetMessage();
                    netMessage.data = newSection.getSectionDataJson();
                    netMessage.action = NetMessage.Insert;
                    netMessage.type = NetMessage.SectionData;
                    netMessage.typeName = NetMessageType.SectionData;
                    netMessage.messageID = netMessage.save();
                    G.mobileCommunication.sendMessage(netMessage);
                    G.server.sendMessage(netMessage);
                } catch (SQLiteConstraintException sqlEx) {// نام تکراری
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(156), G.T.getSentence(846));
                    G.printStackTrace(sqlEx);
                    return;
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                dlg.dismiss();
                loadSectionsList();
                expListAdapter.notifyDataSetChanged();
            }
        });
        dlgObjects.btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dlg.dismiss();
            }
        });
        dlg.show();
    }

    private void startAddNewRoom() {
        dlg = new Dialog(G.currentActivity);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setCancelable(true);

        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            dlg.setContentView(R.layout.d_section_add_room);
        else
            dlg.setContentView(R.layout.d_section_add_room_rtl);

        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        final CO_d_section_add_room dlgObjects = new CO_d_section_add_room(dlg);
        try {
            dlgObjects.icnIcon.setImageDir(G.DIR_ICONS_ROOMS);
            dlgObjects.icnIcon.setImageToSelector(getAssets().list(G.DIR_ICONS_ROOMS)[0]);
        } catch (IOException e1) {
            G.printStackTrace(e1);
        }
        List<String> spinnerArray = new ArrayList<String>();
        final List<Integer> spinnerIDs = new ArrayList<Integer>();
        Database.Section.Struct[] sections = Database.Section.select("");
        if (sections == null) {
            final DialogClass dlg = new DialogClass(G.currentActivity);
            dlg.setOnOkListener(new OkListener() {
                @Override
                public void okClick() {
                    dlg.dissmiss();
                }
            });
            dlg.showOk(G.T.getSentence(151), G.T.getSentence(241));
            return;
        }
        for (int i = 0; i < sections.length; i++) {
            spinnerArray.add(sections[i].name);
            spinnerIDs.add(sections[i].iD);
        }

        if (G.setting.languageID == 1 || G.setting.languageID == 4) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.l_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(R.layout.l_spinner_item);
            dlgObjects.spnRooms.setAdapter(adapter);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.l_spinner_item_rtl, spinnerArray);
            adapter.setDropDownViewResource(R.layout.l_spinner_item_rtl);
            dlgObjects.spnRooms.setAdapter(adapter);
        }
        dlgObjects.btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String iconName = "";
                int sort = 0;
                try {

                    iconName = dlgObjects.icnIcon.getSelectedIconName();//getAssets().list(G.DIR_ICONS_ROOMS)[dlgObjects.icnIcon.getSelectedIconPosition()];
                } catch (Exception e) {
                }
                try {
                    try {
                        sort = Database.Section.getMax("Sort", "").sort + 1;
                    } catch (Exception e) {
                    }

                    if (!validateName(dlgObjects.edtName.getText().toString()))
                        return;

                    Database.Room.Struct newRoom = new Database.Room.Struct();
                    newRoom.name = dlgObjects.edtName.getText().toString();
                    newRoom.icon = iconName;
                    newRoom.sort = sort;
                    newRoom.sectionID = spinnerIDs.get((int) dlgObjects.spnRooms.getSelectedItemId());
                    newRoom.iD = (int) Database.Room.insert(newRoom);


                    SysLog.log("Room " + newRoom.name + " Created.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                    //  Send message to server and local Mobiles


                    NetMessage netMessage = new NetMessage();
                    netMessage.data = newRoom.getRoomDataJson();
                    netMessage.action = NetMessage.Insert;
                    netMessage.type = NetMessage.RoomData;
                    netMessage.typeName = NetMessage.NetMessageType.RoomData;
                    netMessage.messageID = netMessage.save();
                    G.mobileCommunication.sendMessage(netMessage);
                    G.server.sendMessage(netMessage);
                } catch (SQLiteConstraintException sqlEx) {// نام تکراری
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(156), G.T.getSentence(846));
                    G.printStackTrace(sqlEx);
                    return;
                } catch (Exception e) {
                }
                dlg.dismiss();
                loadSectionsList();
                expListAdapter.notifyDataSetChanged();
            }
        });
        dlgObjects.btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dlg.dismiss();
            }
        });
        dlg.show();
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

    private boolean validateName(String name) {
        if (name.equals("")) {
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(151), G.T.getSentence(157));
            return false;
        } else return true;
    }

    private void loadSectionsList() {
        Database.Section.Struct[] sections;
        sections = Database.Section.select("ID>0");
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

    private void startEditSection(final Database.Section.Struct section) {
        dlg = new Dialog(G.currentActivity);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            dlg.setContentView(R.layout.d_section_add_section);
        else
            dlg.setContentView(R.layout.d_section_add_section_rtl);

        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dlg.setCancelable(true);
        final CO_d_section_add_section dlgObjects = new CO_d_section_add_section(dlg);
        dlgObjects.edtName.setText(section.name);
        dlgObjects.icnIcon.setImageDir(G.DIR_ICONS_SECTIONS);
        dlgObjects.icnIcon.setImageToSelector(section.icon);

        dlgObjects.btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    section.name = dlgObjects.edtName.getText().toString();
                    section.icon = dlgObjects.icnIcon.getSelectedIconName();//  getAssets().list(G.DIR_ICONS_SECTIONS)[dlgObjects.icnIcon.getSelectedIconPosition()];
                    section.sort = Database.Section.getMax("Sort", "").sort + 1;
                    Database.Section.edit(section);
                    SysLog.log("Section " + section.name + " Edited.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                    //  Send message to server and local Mobiles
                    NetMessage netMessage = new NetMessage();
                    netMessage.data = section.getSectionDataJson();
                    netMessage.action = NetMessage.Update;
                    netMessage.type = NetMessage.SectionData;
                    netMessage.typeName = NetMessageType.SectionData;
                    netMessage.messageID = netMessage.save();
                    G.mobileCommunication.sendMessage(netMessage);
                    G.server.sendMessage(netMessage);
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                dlg.dismiss();
                loadSectionsList();
                expListAdapter.notifyDataSetChanged();
            }
        });
        dlgObjects.btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dlg.dismiss();
            }
        });

        dlg.show();
    }

    private void startEditRoom(final Database.Room.Struct room) {
        dlg = new Dialog(G.currentActivity);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setCancelable(true);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            dlg.setContentView(R.layout.d_section_add_room);
        else
            dlg.setContentView(R.layout.d_section_add_room_rtl);
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        final CO_d_section_add_room dlgObjects = new CO_d_section_add_room(dlg);

        List<String> spinnerArray = new ArrayList<String>();
        final List<Integer> spinnerIDs = new ArrayList<Integer>();
        Database.Section.Struct[] sections = Database.Section.select("");
        for (int i = 0; i < sections.length; i++) {
            spinnerArray.add(sections[i].name);
            spinnerIDs.add(sections[i].iD);
        }
        if (G.setting.languageID == 1 || G.setting.languageID == 4) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.l_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(R.layout.l_spinner_item);
            dlgObjects.spnRooms.setAdapter(adapter);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.l_spinner_item_rtl, spinnerArray);
            adapter.setDropDownViewResource(R.layout.l_spinner_item_rtl);
            dlgObjects.spnRooms.setAdapter(adapter);
        }
        dlgObjects.spnRooms.setSelection(spinnerIDs.indexOf(room.sectionID));
        dlgObjects.edtName.setText(room.name);
        dlgObjects.icnIcon.setImageDir(G.DIR_ICONS_ROOMS);
        dlgObjects.icnIcon.setImageToSelector(room.icon);
        dlgObjects.btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    room.name = dlgObjects.edtName.getText().toString();
                    room.icon = dlgObjects.icnIcon.getSelectedIconName(); //getAssets().list(G.DIR_ICONS_ROOMS)[dlgObjects.icnIcon.getSelectedIconPosition()];
                    room.sort = Database.Section.getMax("Sort", "").sort + 1;
                    room.sectionID = spinnerIDs.get((int) dlgObjects.spnRooms.getSelectedItemId());
                    Database.Room.edit(room);
                    SysLog.log("Room " + room.name + " Edited.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                    //  Send message to server and local Mobiles
                    NetMessage netMessage = new NetMessage();
                    netMessage.data = room.getRoomDataJson();
                    netMessage.action = NetMessage.Update;
                    netMessage.type = NetMessage.RoomData;
                    netMessage.typeName = NetMessage.NetMessageType.RoomData;
                    netMessage.messageID = netMessage.save();
                    G.mobileCommunication.sendMessage(netMessage);
                    G.server.sendMessage(netMessage);
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                dlg.dismiss();
                loadSectionsList();
                expListAdapter.notifyDataSetChanged();
            }
        });
        dlgObjects.btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dlg.dismiss();
            }
        });
        dlg.show();
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.btnAdd.setText(G.T.getSentence(105));
        fo.btnMyHouse.setText(G.T.getSentence(850));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ActivityMain.class));
        Animation.doAnimation(Animation_Types.FADE);
        this.finish();
    }

    private void refreshNodeList() {
        try {
            nodes = Database.Node.select("RoomID=" + expChildItems.get(selectedSection)[selectedRoom].iD+
            " AND isVisible=1");
        } catch (Exception e) {
            G.printStackTrace(e);
        }
        grdListAdapter = null;
        if (nodes != null) {
            grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, true);
        }
        fo.grdNodes.setAdapter(grdListAdapter);
    }

    private void refreshMyHouseNodeList() {
        try {
            nodes = Database.Node.select("RoomID=" + AllNodes.myHouseDefaultRoomId);
        } catch (Exception e) {
            G.printStackTrace(e);
        }
        grdListAdapter = null;
        if (nodes != null) {
            grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, true);
        }
        fo.grdNodes.setAdapter(grdListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNodeList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                //showAddMenu(formObjects.btnAdd);
                final Dialog dialog = new Dialog(ActivitySections.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.d_add_item_to_expandable);
                CO_d_add_item_to_expandable dlgObj = new CO_d_add_item_to_expandable(dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dlgObj.lay_section.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        startAddNewSection();
                        dialog.dismiss();
                    }
                });
                dlgObj.layRoom.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startAddNewRoom();
                        dialog.dismiss();
                    }
                });

                dlgObj.layNode.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startAddNewNode();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.btnMyHouse:
                refreshMyHouseNodeList();
                break;
            case R.id.btn_io_module:
                startActivity(new Intent(G.currentActivity, ActivityMyHouse.class));
                Animation.doAnimation(Animation_Types.FADE);
                break;
        }
    }
}
