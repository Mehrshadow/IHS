package ir.parsansoft.app.ihs.center;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.AllViews.CO_d_section_add_room;
import ir.parsansoft.app.ihs.center.AllViews.CO_d_section_add_section;
import ir.parsansoft.app.ihs.center.AllViews.CO_f_sections;
import ir.parsansoft.app.ihs.center.AllViews.CO_l_list_sections_room;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.DialogClass.OkListener;
import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewNode;
import ir.parsansoft.app.ihs.center.adapters.AdapterSectionsExpandableList;
import ir.parsansoft.app.ihs.center.adapters.AdapterSectionsExpandableList.onOptionButton;

public class ActivityWelcome8FloorandRoom extends ActivityWizard {

    private AdapterSectionsExpandableList expListAdapter;
    private ArrayList<Database.Section.Struct> expParentItems = new ArrayList<Database.Section.Struct>();
    private ArrayList<Database.Room.Struct[]> expChildItems = new ArrayList<Database.Room.Struct[]>();
    private AdapterListViewNode grdListAdapter;
    private Database.Node.Struct[] nodes;
    private Dialog dlg;
    private CO_f_sections formObjects;
    boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.f_welcome_floorandroom);
        formObjects = new CO_f_sections(this);
        translateForm();
        hideBackButton();
        G.bp.restartWeatherAPI();
        G.bp.runAllThreads();
        loadSectionsList();
        expListAdapter = new AdapterSectionsExpandableList(expParentItems, expChildItems);
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

        formObjects.btnAddRoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddNewRoom();
            }
        });

        formObjects.btnAddSection.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startAddNewSection();
            }
        });
        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isBusy) {
                    isBusy = true;
                    startActivity(new Intent(G.currentActivity, ActivityWelcome7Specifications.class));
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
                    Database.Room.Struct[] rooms = Database.Room.select("");
                    if (rooms != null && rooms.length != 1) {// we have 1 room that is my house >> roomId = -1
                        startActivity(new Intent(G.currentActivity, ActivityWelcome9Equipment.class));
                        Animation.doAnimation(Animation_Types.FADE);
                        finish();
                        btnNext.setEnabled(false);
                    } else {
                        isBusy = false;
                        DialogClass dlg = new DialogClass(G.currentActivity);
                        dlg.showOk(G.T.getSentence(156), G.T.getSentence(242));
                    }
                }
            }
        });
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
                            dlg.dissmiss();

                            /************************* Jahanbin********************************/
                            NetMessage netMessage = new NetMessage();
                            netMessage.data = room.getRoomDataJson();
                            netMessage.action = NetMessage.Delete;
                            netMessage.type = NetMessage.RoomData;
                            netMessage.typeName = NetMessage.NetMessageType.RoomData;
                            netMessage.messageID = netMessage.save();
                            G.mobileCommunication.sendMessage(netMessage);
                            G.server.sendMessage(netMessage);
                            /************************* Jahanbin********************************/

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
                            // Delete room
                            Database.Section.delete(section.iD);
                            dlg.dissmiss();

                            /********************* Jahanbin ************************/
                            NetMessage netMessage = new NetMessage();
                            netMessage.data = section.getSectionDataJson();
                            netMessage.action = NetMessage.Delete;
                            netMessage.type = NetMessage.SectionData;
                            netMessage.typeName = NetMessage.NetMessageType.SectionData;
                            netMessage.messageID = netMessage.save();
                            G.mobileCommunication.sendMessage(netMessage);
                            G.server.sendMessage(netMessage);
                            /********************* Jahanbin ************************/

                            loadSectionsList();
                            expListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void noClick() {
                            dlg.dissmiss();
                        }
                    });
                    dlg.showYesNo(G.T.getSentence(155), G.T.getSentence(240));
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
                    dlg.showOk(G.T.getSentence(155), G.T.getSentence(238));
                }
            }
        });

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
                    netMessage.typeName = NetMessage.NetMessageType.SectionData;
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

    // to prevent inserting empty name
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
                    section.icon = dlgObjects.icnIcon.getSelectedIconName();//getAssets().list(G.DIR_ICONS_SECTIONS)[dlgObjects.icnIcon.getSelectedIconPosition()];
                    section.sort = Database.Section.getMax("Sort", "").sort + 1;
                    Database.Section.edit(section);
                    SysLog.log("Section " + section.name + " Edited.", SysLog.LogType.DATA_CHANGE, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                    //  Send message to server and local Mobiles
                    NetMessage netMessage = new NetMessage();
                    netMessage.data = section.getSectionDataJson();
                    netMessage.action = NetMessage.Update;
                    netMessage.type = NetMessage.SectionData;
                    netMessage.typeName = NetMessage.NetMessageType.SectionData;
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
                    room.icon = dlgObjects.icnIcon.getSelectedIconName(); // getAssets().list(G.DIR_ICONS_ROOMS)[dlgObjects.icnIcon.getSelectedIconPosition()];
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
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }

    @Override
    public void translateForm() {
        super.translateForm();
        formObjects.btnAddSection.setText(G.T.getSentence(222));
        formObjects.btnAddRoom.setText(G.T.getSentence(223));
    }
}
