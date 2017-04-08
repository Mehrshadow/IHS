package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import ir.parsansoft.app.ihs.center.AllViews.CO_l_node_simple_dimmer;
import ir.parsansoft.app.ihs.center.AllViews.CO_l_node_simple_key;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;

import static ir.parsansoft.app.ihs.center.Database.Switch.select;
import static ir.parsansoft.app.ihs.center.G.nodeCommunication;

public class AllNodes {
    public static int uiCount = 0;
    public final static int myHouseDefaultRoomId = -1;

    private SparseArray<CO_l_node_simple_key> coolerUIs;
    ////

    public static final class Switch_Type {
        public static final int SIMPLE_SWITCH = 1;
        public static final int SIMPLE_DIMMER = 2;
        public static final int COOLER_SPEED = 3;
        public static final int COOLER_PUMP = 4;
        public static final int CURTAIN_STATUS = 5;
        public static final int WC_LAMP = 6;

        public static final int COOLER_SPEED_Slow = 7;
        public static final int COOLER_SPEED_Fast = 8;
        public static final int CURTAIN_STATUS_Close = 9;
        public static final int CURTAIN_STATUS_Open = 10;
        //        public static final int CURTAIN_STATUS_Stop = 10;
        public static final int Sensor_NC = 11;
        public static final int Sensor_NO = 12;
    }

    public static final class Node_Type {
        public static final int SIMPLE_SWITCH_1 = 1;
        public static final int SIMPLE_SWITCH_2 = 2;
        public static final int SIMPLE_SWITCH_3 = 3;
        public static final int SIMPLE_DIMMER_1 = 4;
        public static final int SIMPLE_DIMMER_2 = 5;
        public static final int AIR_CONDITION = 6;
        public static final int CURTAIN_SWITCH = 7;
        public static final int WC_SWITCH = 8;

        public static final int IOModule = 9;
        public static final int Sensor_SMOKE = 10;
        public static final int Sensor_Magnetic = 11;
    }

    public static final class Node_Status {
        public static final int DEACTIVE = 0;
        public static final int ACTIVE = 1;
        public static final int IN_LEARN = 2;
        public static final int DAMAGED = 3;
    }

    public enum Node_Model_Name {
        SW1G,
        SW2G,
        SW3G,
        SWD1,
        SWD2,
        SWAC,
        SWCT,
        SWWC,// for WC Switch
        IOModule
    }

    public static final class DiscoveryPacket {
        public int type = 0;
        public String serial = "";
        public String osVer = "";

        public DiscoveryPacket(String packet) {
            try {
                // BHZDY*model*serial*osver
                packet = packet.trim().replace("#", "");
                String[] p = packet.split("\\*");
                if (p[0].equalsIgnoreCase("BHZDY")) {
                    serial = p[2];
                    osVer = p[3];
                    if (p[1].equalsIgnoreCase("SW1G"))
                        type = Node_Type.SIMPLE_SWITCH_1;
                    if (p[1].equalsIgnoreCase("SW2G"))
                        type = Node_Type.SIMPLE_SWITCH_2;
                    if (p[1].equalsIgnoreCase("SW3G"))
                        type = Node_Type.SIMPLE_SWITCH_3;
                    if (p[1].equalsIgnoreCase("SW1D"))
                        type = Node_Type.SIMPLE_DIMMER_1;
                    if (p[1].equalsIgnoreCase("SW2D"))
                        type = Node_Type.SIMPLE_DIMMER_2;
                    if (p[1].equalsIgnoreCase("SWAC"))
                        type = Node_Type.AIR_CONDITION;
                    if (p[1].equalsIgnoreCase("SWCT"))
                        type = Node_Type.CURTAIN_SWITCH;
                    if (p[1].equalsIgnoreCase("SWWC"))
                        type = Node_Type.WC_SWITCH;
                    if (p[1].equalsIgnoreCase("IOMudole"))
                        type = Node_Type.IOModule;
                } else {
                    type = 0;
                }
            } catch (Exception e) {
                type = 0;
                G.printStackTrace(e);
            }
        }
    }

    public static final class SimpleSwitch extends SampleNode {
        private SparseArray<CO_l_node_simple_key> simpleKeyUIs = new SparseArray<CO_l_node_simple_key>();
        private Database.Switch.Struct[] switches;
        private boolean isBusy;

        public SimpleSwitch(Database.Node.Struct node) {
            super(G.context);
            this.myNode = node;
            switches = select("NodeID=" + myNode.iD);
            if (myNode.isIoModuleNode != 1)
                connectToNode();
        }

        @Override
        public void resetUis() {
            for (int i = 0; i < simpleKeyUIs.size(); i++) {
                simpleKeyUIs.removeAt(i);
            }
        }

        @Override
        public int addUI(View view) {
            uiCount++;
            if (simpleKeyUIs.size() > 5) {
                G.log("Deleting some UI references to reduce memory");
                simpleKeyUIs.removeAt(0);
            }
            G.log("This is adding Simple Node UI for node : " + myNode.iD);
            final CO_l_node_simple_key newSimpleKey = new CO_l_node_simple_key(view);
            newSimpleKey.prgDoOperation.setVisibility(View.INVISIBLE);
            switches = select("NodeID=" + myNode.iD);
            if (switches == null) {
                G.log("Switches count is ziro... could not add any UI");
                newSimpleKey.layKey1.setVisibility(View.INVISIBLE);
                newSimpleKey.layKey2.setVisibility(View.INVISIBLE);
                newSimpleKey.layKey3.setVisibility(View.INVISIBLE);
                return 0;
            }
            newSimpleKey.txtNodeName.setText(myNode.name);
            if (switches.length >= 1) {
                newSimpleKey.layKey1.setVisibility(View.VISIBLE);
                newSimpleKey.txtKey1.setText(switches[0].name);
                if (switches[0].value == 0)
                    newSimpleKey.imgKey1.setImageResource(R.drawable.lay_simple_switch_off);
                else
                    newSimpleKey.imgKey1.setImageResource(R.drawable.lay_simple_switch_on);
            } else {
                newSimpleKey.layKey1.setVisibility(View.GONE);
            }
            if (switches.length >= 2) {
                newSimpleKey.layKey2.setVisibility(View.VISIBLE);
                newSimpleKey.txtKey2.setText(switches[1].name);
                if (switches[1].value == 0)
                    newSimpleKey.imgKey2.setImageResource(R.drawable.lay_simple_switch_off);
                else
                    newSimpleKey.imgKey2.setImageResource(R.drawable.lay_simple_switch_on);
            } else {
                newSimpleKey.layKey2.setVisibility(View.GONE);
            }
            if (switches.length >= 3) {
                newSimpleKey.layKey3.setVisibility(View.VISIBLE);
                newSimpleKey.txtKey3.setText(switches[2].name);
                if (switches[2].value == 0)
                    newSimpleKey.imgKey3.setImageResource(R.drawable.lay_simple_switch_off);
                else
                    newSimpleKey.imgKey3.setImageResource(R.drawable.lay_simple_switch_on);
            } else {
                newSimpleKey.layKey3.setVisibility(View.GONE);
            }

            if (myNode.isFavorite)
                newSimpleKey.imgFavorites.setImageResource(R.drawable.icon_fav_full);
            else
                newSimpleKey.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
            newSimpleKey.imgFavorites.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    myNode.isFavorite = !myNode.isFavorite;
                    Database.Node.edit(myNode);
                    if (myNode.isFavorite)
                        newSimpleKey.imgFavorites.setImageResource(R.drawable.icon_fav_full);
                    else
                        newSimpleKey.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
                }
            });
            newSimpleKey.imgbtnEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_w2.class);
                    fw2.putExtra("NODE_ID", myNode.iD);
                    G.currentActivity.startActivity(fw2);
                }
            });
            newSimpleKey.imgKey1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (!isBusy)
                        switchKey(0, LogOperator.OPERAOR, G.currentUser.iD);
                }
            });

            newSimpleKey.imgKey2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (!isBusy)
                        switchKey(1, LogOperator.OPERAOR, G.currentUser.iD);
                }
            });

            newSimpleKey.imgKey3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (!isBusy)
                        switchKey(2, LogOperator.OPERAOR, G.currentUser.iD);
                }
            });
            if (myNode.status == 0)
                newSimpleKey.prgDoOperation.setVisibility(View.VISIBLE);
            else
                newSimpleKey.prgDoOperation.setVisibility(View.INVISIBLE);
            simpleKeyUIs.put(uiCount, newSimpleKey);
            G.log("NodeID=" + myNode.iD + "  ui code : " + uiCount);
            return uiCount;
        }

        @Override
        public void setProgressVisiblity(final boolean visiblity) {

            G.HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < simpleKeyUIs.size(); i++) {
                        if (visiblity == true) {
                            simpleKeyUIs.valueAt(i).prgDoOperation.setVisibility(View.VISIBLE);
                        } else {
                            simpleKeyUIs.valueAt(i).prgDoOperation.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        @Override
        public void setSettingVisiblity(int uiIndex, boolean isVisible) {
            G.log("Ui Index :" + uiIndex);
            if (isVisible) {
                simpleKeyUIs.get(uiIndex).imgbtnEdit.setVisibility(View.VISIBLE);
            } else {
                simpleKeyUIs.get(uiIndex).imgbtnEdit.setVisibility(View.GONE);
            }
        }

        @Override
        public void processResult(String result) {
            G.log("Result is :" + result);
            setLastCommTime(System.currentTimeMillis());

            setProgressVisiblity(false);

            float[] previousValues;

            if (myNode.isIoModuleNode == 1) {

                previousValues = new float[16];
                try {

                    // hame 16 ta port ro mikhoonim va value jadid ro too db save mikonim
                    //value ghabli ro ham darim
                    for (int i = 0; i < switches.length; i++) {
                        int index = 0;
                        if (switches[i].IOModulePort > 9 && switches[i].IOModulePort != 0) {// ex: *101
                            index = result.indexOf("*" + switches[i].IOModulePort, 0) + 3;
                        } else {
                            index = result.indexOf("*" + switches[i].IOModulePort, 0) + 2;
                        }

                        previousValues[switches[i].IOModulePort] = switches[i].value;
                        switches[i].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[i]);
                    }
                } catch (Exception e) {
                    G.printStackTrace(e);
                }

            } else {

                previousValues = new float[3];

                try {
                    setProgressVisiblity(false);

                    if (switches.length == 1) {
                        int index = result.indexOf("*4", 0) + 2;
                        previousValues[0] = switches[0].value;
                        switches[0].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[0]);
                        //                SysLog.log("Change switch " + switches[i].name + " status", SysLog.LogType.DATA_CHANGE, op, operatorID);
                    } else if (switches.length == 2) {
                        int index = result.indexOf("*3", 0) + 2;
                        previousValues[0] = switches[0].value;
                        switches[0].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[0]);
                        index = result.indexOf("*5", 0) + 2;
                        previousValues[1] = switches[1].value;
                        switches[1].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[1]);
                        //                SysLog.log("Change switch " + switches[i].name + " status", SysLog.LogType.DATA_CHANGE, op, operatorID);
                    } else if (switches.length == 3) {
                        int index = result.indexOf("*3", 0) + 2;
                        previousValues[0] = switches[0].value;
                        switches[0].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[0]);
                        index = result.indexOf("*4", 0) + 2;
                        previousValues[1] = switches[1].value;
                        switches[1].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[1]);
                        index = result.indexOf("*5", 0) + 2;
                        previousValues[2] = switches[2].value;
                        switches[2].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[2]);
                    }
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
            }

            try {
                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < simpleKeyUIs.size(); i++) {
                            int k = simpleKeyUIs.keyAt(i);
                            if (switches.length >= 1)
                                if (switches[0].value == 0) {
                                    simpleKeyUIs.get(k).imgKey1.setImageResource(R.drawable.lay_simple_switch_off);
                                } else {
                                    simpleKeyUIs.get(k).imgKey1.setImageResource(R.drawable.lay_simple_switch_on);
                                }
                            if (switches.length >= 2)
                                if (switches[1].value == 0) {
                                    simpleKeyUIs.get(k).imgKey2.setImageResource(R.drawable.lay_simple_switch_off);
                                } else {
                                    simpleKeyUIs.get(k).imgKey2.setImageResource(R.drawable.lay_simple_switch_on);
                                }
                            if (switches.length >= 3)
                                if (switches[2].value == 0) {
                                    simpleKeyUIs.get(k).imgKey3.setImageResource(R.drawable.lay_simple_switch_off);
                                } else {
                                    simpleKeyUIs.get(k).imgKey3.setImageResource(R.drawable.lay_simple_switch_on);
                                }
                        }
                    }
                });

                //  Send message to server and local Mobiles
                NetMessage netMessage = new NetMessage();
                netMessage.data = myNode.getNodeStatusJson(false);// true means > isIOModule = 1
                netMessage.action = NetMessage.Update;
                netMessage.type = NetMessage.SwitchStatus;
                netMessage.typeName = NetMessageType.SwitchStatus;
                netMessage.messageID = netMessage.save();
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);


                // Run scenarios that uses this condition.
                for (int i = 0; i < 3; i++)
                    if (switches.length > i && previousValues[i] != switches[i].value) {
                        G.log("previusValues [" + i + "] =" + previousValues[i] + "   New Value[" + i + "] = " + switches[i].value);
                        G.scenarioBP.runBySwitchStatus(switches[i]);
                    }

            } catch (Exception e) {
                G.printStackTrace(e);
            }
        }

        @Override
        public void executeCommandChangeSwitchValue(String message, NetMessage
                originalNetMessage, LogOperator op, int operatorID) {
            try {
                JSONObject jo = new JSONObject(message);
                float newValue = (float) jo.getDouble("Value");
                String switchCode = jo.getString("SwitchCode");
                for (int i = 0; i < switches.length; i++) {
                    if (switches[i].code.equals(switchCode)) {
                        G.log("Node ID:" + myNode.iD + " SwitchCode:" + switchCode +
                                "  SwitchIndex:" + i + "  Value:" + newValue);
                        switchKey(i, newValue, originalNetMessage, op, operatorID);
                    }
                }
            } catch (JSONException e) {
                G.printStackTrace(e);
            }
        }

        public void switchKey(int switchIndex, LogOperator op, int operatorID) {
            final NodeMsg nodeMsg = new NodeMsg();
            nodeMsg.msgType = NodeMsgType.MSG_DO_OPERATION;
            nodeMsg.node = myNode;
            int newValue;
            if (switches[switchIndex].value == 0)
                newValue = 1;
            else
                newValue = 0;
            switchKey(switchIndex, newValue, null, op, operatorID);
        }

        public void switchKey(final int switchIndex, final float newValue, final NetMessage originalNetMessage, final LogOperator op, final int operatorID) {
            G.log("SwitchKey switchIndex:" + switchIndex + " - New value:" + newValue);
            SysLog.log("SimpleKey:" + myNode.name + " Switch:" + switches[switchIndex].name + " Value Changed from " + switches[switchIndex].value + " to " + newValue, LogType.VALUE_CHANGE, op, operatorID);
            final NodeMsg nodeMsg = new NodeMsg();

            setProgressVisiblity(true);

            if (myNode.isIoModuleNode == 1) {

                nodeMsg.sentData = "*" + switches[switchIndex].IOModulePort + (int) newValue;

            } else {
                switch (myNode.nodeTypeID) {
                    case Node_Type.SIMPLE_SWITCH_1:
                        switch (switchIndex) {
                            case 0:
                                nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SW3G.toString() + "*3x*4" + (int) newValue + "*5x*");
                                break;
                        }
                        break;
                    case Node_Type.SIMPLE_SWITCH_2:
                        switch (switchIndex) {
                            case 0:
                                nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SW3G.toString() + "*3" + (int) newValue + "*4x*5x*");
                                break;
                            case 1:
                                nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SW3G.toString() + "*3x*4x*5" + (int) newValue + "*");
                                break;
                        }
                        break;
                    case Node_Type.SIMPLE_SWITCH_3:
                        switch (switchIndex) {
                            case 0:
                                nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SW3G.toString() + "*3" + (int) newValue + "*4x*5x*");
                                break;
                            case 1:
                                nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SW3G.toString() + "*3x*4" + (int) newValue + "*5x*");
                                break;
                            case 2:
                                nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SW3G.toString() + "*3x*4x*5" + (int) newValue + "*");
                                break;
                        }
                        break;
                }
            }
            nodeMsg.msgType = NodeMsgType.MSG_DO_OPERATION;
            nodeMsg.node = myNode;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMessageToNode(nodeMsg.sentData);
                }
            });
            thread.start();
        }

    }

    public static final class SimpleDimmer extends SampleNode {
        private SparseArray<CO_l_node_simple_dimmer> dimmerUIs = new SparseArray<CO_l_node_simple_dimmer>();
        private Database.Switch.Struct[] switches;
        private boolean isBusy;
        int dimHeight = 160;
        int[] tempVal = new int[2];


        public SimpleDimmer(Database.Node.Struct node) {
            super(G.context);
            this.myNode = node;
            switches = select("NodeID=" + myNode.iD);
            connectToNode();
        }

        @Override
        public void resetUis() {
            for (int i = 0; i < dimmerUIs.size(); i++) {
                dimmerUIs.removeAt(i);
            }
        }

        @Override
        public int addUI(View view) {
            uiCount++;
            if (dimmerUIs.size() > 5) {
                G.log("Deleting some UI references to reduce memory");
                dimmerUIs.removeAt(0);
            }
            final CO_l_node_simple_dimmer newSimpleDim = new CO_l_node_simple_dimmer(view);
            dimHeight = newSimpleDim.layTouch1.getLayoutParams().height;
            switches = select("NodeID=" + myNode.iD);
            G.log("Add new UI for Dimmer");
            if (switches == null) {
                G.log("Switches count is ziro... could not add any UI");
                newSimpleDim.layDimmerOne.setVisibility(View.INVISIBLE);
                newSimpleDim.layDimmerTwo.setVisibility(View.INVISIBLE);
                return 0;
            }
            newSimpleDim.prgDoOperation.setVisibility(View.INVISIBLE);
            newSimpleDim.txtNodeName.setText(myNode.name);
            if (switches.length >= 1) {
                newSimpleDim.txtlbl1.setText((int) switches[0].value + " %");
                newSimpleDim.txtName1.setText(switches[0].name);
                setHeight(newSimpleDim.layGray1, (int) ((100 - ((int) switches[0].value)) * dimHeight / 100));
                setHeight(newSimpleDim.layGreen1, (int) (((int) switches[0].value) * dimHeight / 100));
                setPadding(newSimpleDim.imgGreen1, -(int) ((100 - ((int) switches[0].value)) * dimHeight / 100));
                newSimpleDim.txtlbl1.setText((int) switches[0].value + " %");
                newSimpleDim.layTouch1.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        int y = (int) e.getY();
                        if (y < 0)
                            y = 0;
                        if (y > dimHeight)
                            y = dimHeight;
                        int value = (100 - (y * 100) / dimHeight);
                        G.log(" Dimmer onTouch - y=" + y + "  valu=" + value + " tempVal[0]=" + tempVal[0]);
                        switch (e.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                if (y >= dimHeight * 0.87) {
                                    y = dimHeight;
                                } else if (y >= dimHeight * 0.63 && y < dimHeight * 0.87) {
                                    y = (int) (dimHeight * 0.75);
                                } else if (y >= dimHeight * 0.37 && y < dimHeight * 0.63) {
                                    y = (int) (dimHeight * 0.5);
                                } else if (y >= dimHeight * 0.13 && y < dimHeight * 0.37) {
                                    y = (int) (dimHeight * 0.25);
                                } else if (y < dimHeight * 0.13) {
                                    y = 0;
                                }
                                value = (100 - (y * 100) / dimHeight);
                                tempVal[0] = value;
                                G.log(" Dimmer ACTION_DOWN - y=" + y + "  value=" + value + " tempVal[0]=" + tempVal[0]);
                                draw(y, value);
                                break;
                            case MotionEvent.ACTION_UP:
                                G.log(" Dimmer ACTION_UP - y=" + y + "  value=" + value + " tempVal[0]=" + tempVal[0]);
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                changeValue(tempVal[0], tempVal[1], null, LogOperator.OPERAOR, G.currentUser.iD);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                tempVal[0] = value;
                                G.log(" Dimmer ACTION_MOVE - y=" + y + "  valu=" + value + " tempVal[0]=" + tempVal[0]);
                                draw(y, value);
                                break;
                        }
                        v.onTouchEvent(e);
                        return true;
                    }

                    private void draw(int h, int value) {
                        setHeight(newSimpleDim.layGray1, h);
                        setHeight(newSimpleDim.layGreen1, dimHeight - h);
                        setPadding(newSimpleDim.imgGreen1, -(h));
                        newSimpleDim.txtlbl1.setText(value + " %");
                    }
                });
            }
            if (switches.length >= 2) {
                newSimpleDim.txtName2.setText(switches[1].name);
                setHeight(newSimpleDim.layGray2, (int) ((100 - ((int) switches[1].value)) * dimHeight / 100));
                setHeight(newSimpleDim.layGreen2, (int) (((int) switches[1].value) * dimHeight / 100));
                setPadding(newSimpleDim.imgGreen2, -(int) ((100 - ((int) switches[1].value)) * dimHeight / 100));
                newSimpleDim.txtlbl2.setText((int) switches[1].value + " %");
                newSimpleDim.layTouch2.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        int y = (int) e.getY();
                        if (y < 0)
                            y = 0;
                        if (y > dimHeight)
                            y = dimHeight;
                        int value = (100 - (y * 100) / dimHeight);

                        switch (e.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                if (y >= dimHeight * 0.87) {
                                    y = dimHeight;
                                } else if (y >= dimHeight * 0.63 && y < dimHeight * 0.87) {
                                    y = (int) (dimHeight * 0.75);
                                } else if (y >= dimHeight * 0.37 && y < dimHeight * 0.63) {
                                    y = (int) (dimHeight * 0.5);
                                } else if (y >= dimHeight * 0.13 && y < dimHeight * 0.37) {
                                    y = (int) (dimHeight * 0.25);
                                } else if (y < dimHeight * 0.13) {
                                    y = 0;
                                }
                                value = (100 - (y * 100) / dimHeight);
                                tempVal[1] = value;
                                draw(y, value);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                changeValue(tempVal[0], tempVal[1], null, LogOperator.OPERAOR, G.currentUser.iD);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                tempVal[1] = value;
                                draw(y, value);
                                break;
                        }
                        v.onTouchEvent(e);
                        return true;
                    }

                    private void draw(int h, int value) {
                        setHeight(newSimpleDim.layGray2, h);
                        setHeight(newSimpleDim.layGreen2, dimHeight - h);
                        setPadding(newSimpleDim.imgGreen2, -(h));
                        newSimpleDim.txtlbl2.setText(value + " %");

                    }
                });
            } else {
                newSimpleDim.layDimmerTwo.setVisibility(View.GONE);
            }
            if (myNode.isFavorite)
                newSimpleDim.imgFavorites.setImageResource(R.drawable.icon_fav_full);
            else
                newSimpleDim.imgFavorites.setImageResource(R.drawable.icon_fav_empty);


            newSimpleDim.imgFavorites.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    myNode.isFavorite = !myNode.isFavorite;
                    Database.Node.edit(myNode);
                    if (myNode.isFavorite)
                        newSimpleDim.imgFavorites.setImageResource(R.drawable.icon_fav_full);
                    else
                        newSimpleDim.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
                }
            });
            newSimpleDim.imgbtnEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_w2.class);
                    fw2.putExtra("NODE_ID", myNode.iD);
                    G.currentActivity.startActivity(fw2);
                    Animation.doAnimation(Animation_Types.FADE);
                }
            });

            //            if (myNode.status == 0)
            //                dimmerUIs.prgDoOperation.setVisibility(View.VISIBLE);
            //            else
            //                dimmerUIs.prgDoOperation.setVisibility(View.INVISIBLE);
            G.log("NodeID=" + myNode.iD + "  ui code : " + uiCount);
            dimmerUIs.put(uiCount, newSimpleDim);
            return uiCount;
        }

        private void setPadding(ImageView iv, int y) {
            iv.setPadding(0, y, 0, 0);
        }

        private void setHeight(LinearLayout iv, int y) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, y);
            iv.setLayoutParams(layoutParams);
        }

        @Override
        public void setProgressVisiblity(final boolean visiblity) {

            G.HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < dimmerUIs.size(); i++) {
                        if (visiblity == true) {
                            dimmerUIs.valueAt(i).prgDoOperation.setVisibility(View.VISIBLE);
                        } else {
                            dimmerUIs.valueAt(i).prgDoOperation.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        @Override
        public void setSettingVisiblity(int uiIndex, boolean isVisible) {
            G.log("setSettingVisiblity Ui Index :" + uiIndex);
            if (isVisible) {
                dimmerUIs.get(uiIndex).imgbtnEdit.setVisibility(View.VISIBLE);
            } else {
                dimmerUIs.get(uiIndex).imgbtnEdit.setVisibility(View.GONE);
            }
        }

        @Override
        public void processResult(String result) {
            G.log("Result is :" + result);
            setLastCommTime(System.currentTimeMillis());
            float[] previusValues = new float[3];
            try {

                if (switches.length >= 1) {
                    int index = result.indexOf("*3", 0) + 7;
                    previusValues[0] = switches[0].value;
                    result = result.substring(index, index + 2).replace("%", "").replace(":", "");
                    G.log("New percent is : " + result);
                    switches[0].value = Integer.parseInt(result);
                    switches[0].value = switches[0].value == 0 ? 0 : switches[0].value + 1;
                    Database.Switch.edit(switches[0]);
                    //                SysLog.log("Change switch " + switches[i].name + " status", SysLog.LogType.DATA_CHANGE, op, operatorID);
                }
                if (switches.length >= 2) {
                    int index = result.indexOf("*4", 0) + 7;
                    previusValues[1] = switches[1].value;
                    result = result.substring(index, index + 2).replace("%", "").replace(":", "");
                    G.log("New percent is : " + result);
                    switches[1].value = Integer.parseInt(result);
                    switches[1].value = switches[1].value == 0 ? 0 : switches[1].value + 1;
                    Database.Switch.edit(switches[1]);
                    //                SysLog.log("Change switch " + switches[i].name + " status", SysLog.LogType.DATA_CHANGE, op, operatorID);
                }


                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        int newVal;
                        for (int i = 0; i < dimmerUIs.size(); i++) {
                            G.log("Refresh Dimmer UI " + i + ": switches[0]=" + switches[0].value);
                            dimmerUIs.valueAt(i).txtlbl1.setText((int) switches[0].value + " %");
                            newVal = (int) switches[0].value;
                            setHeight(dimmerUIs.valueAt(i).layGray1, (int) ((100 - newVal) * dimHeight / 100));
                            setHeight(dimmerUIs.valueAt(i).layGreen1, (int) ((newVal) * dimHeight / 100));
                            setPadding(dimmerUIs.valueAt(i).imgGreen1, -(int) ((100 - newVal) * dimHeight / 100));
                            if (switches.length > 1) {
                                dimmerUIs.valueAt(i).txtlbl2.setText((int) switches[1].value + " %");
                                newVal = (int) switches[1].value;
                                setHeight(dimmerUIs.valueAt(i).layGray2, (int) ((100 - newVal) * dimHeight / 100));
                                setHeight(dimmerUIs.valueAt(i).layGreen2, (int) ((newVal) * dimHeight / 100));
                                setPadding(dimmerUIs.valueAt(i).imgGreen2, -(int) ((100 - newVal) * dimHeight / 100));
                            }
                        }
                    }
                });

                //  Send message to server and local Mobiles
                NetMessage netMessage = new NetMessage();
                netMessage.data = myNode.getNodeStatusJson(false);// true means > isIOModule = 1
                netMessage.action = NetMessage.Update;
                netMessage.type = NetMessage.SwitchStatus;
                netMessage.typeName = NetMessageType.SwitchStatus;
                netMessage.messageID = netMessage.save();
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);


                // Run scenarios that uses this condition.
                for (int i = 0; i < 3; i++)
                    if (switches.length > i && previusValues[i] != switches[i].value) {
                        G.log("previusValues [" + i + "] =" + previusValues[i] + "   New Value[" + i + "] = " + switches[i].value);
                        G.scenarioBP.runBySwitchStatus(switches[i]);
                    }

            } catch (Exception e) {
                G.printStackTrace(e);
            }
        }

        @Override
        public void executeCommandChangeSwitchValue(String message, NetMessage originalNetMessage, LogOperator op, int operatorID) {
            try {
                JSONObject jo = new JSONObject(message);
                float newValue = (float) jo.getDouble("Value");
                String switchCode = jo.getString("SwitchCode");
                for (int i = 0; i < switches.length; i++) {
                    if (switches[i].code.equals(switchCode)) {
                        G.log("Node ID:" + myNode.iD + " SwitchCode:" + switchCode +
                                "  SwitchIndex:" + i + "  Value:" + newValue);
                        if (i == 0 && switches.length == 1)
                            changeValue(newValue, 0, originalNetMessage, op, operatorID);
                        else if (i == 0 && switches.length == 2)
                            changeValue(newValue, switches[1].value, originalNetMessage, op, operatorID);
                        else
                            changeValue(switches[0].value, newValue, originalNetMessage, op, operatorID);
                    }
                }
            } catch (JSONException e) {
                G.printStackTrace(e);
            }
        }

        public void changeValue(float value1, float value2, final NetMessage originalNetMessage, final LogOperator op, final int operatorID) {
            final NodeMsg nodeMsg = new NodeMsg();
            nodeMsg.msgType = NodeMsgType.MSG_DO_OPERATION;
            nodeMsg.node = myNode;
            int nv1 = (int) value1;
            int nv2 = (int) value2;
            nv1 = nv1 == 0 ? 0 : nv1 - 1;
            nv2 = nv2 > 0 ? 0 : nv2 - 1;
            if (switches.length == 1) {
                SysLog.log("Dimmer:" + myNode.name + " Value Changed from " + switches[0].value + " to " + value1, LogType.VALUE_CHANGE, op, operatorID);
                nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SWD1.toString() + "*3DIM1:" + (int) nv1 + "%#");
            } else if (switches.length == 2) {
                SysLog.log("Dimmer:" + myNode.name + " Value Changed from (" + switches[0].value + "," + switches[1].value + ") to (" + value1 + "," + value2 + ")", LogType.VALUE_CHANGE, op, operatorID);
                nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SWD1.toString() + "*3DIM1:" + (int) nv1 + "%*4DIM2:" + nv2 + "%#");
            }


            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMessageToNode(nodeMsg.sentData);

                }
            });
            thread.start();
        }
    }

    public static final class CoolerSwitch extends SampleNode {
        private SparseArray<CO_l_node_simple_key> coolerUIs = new SparseArray<CO_l_node_simple_key>();
        private Database.Switch.Struct[] switches;
        private boolean isBusy;


        public CoolerSwitch(Database.Node.Struct node) {
            super(G.context);
            this.myNode = node;
            switches = select("NodeID=" + myNode.iD);
            if (myNode.isIoModuleNode != 1)
                connectToNode();
        }

        @Override
        public void resetUis() {
            for (int i = 0; i < coolerUIs.size(); i++) {
                coolerUIs.removeAt(i);
            }
        }

        @Override
        public int addUI(View view) {
            uiCount++;
            if (coolerUIs.size() > 5) {
                G.log("Deleting some UI references to reduce memory");
                coolerUIs.removeAt(0);
            }
            G.log("This is adding Simple Node UI for node : " + myNode.iD);
            final CO_l_node_simple_key ui = new CO_l_node_simple_key(view);
            ui.prgDoOperation.setVisibility(View.INVISIBLE);
            switches = select("NodeID=" + myNode.iD);
            if (switches == null || switches.length < 2) {
                G.log("Switches count is not enough... could not add any UI");
                ui.layKey1.setVisibility(View.INVISIBLE);
                ui.layKey2.setVisibility(View.INVISIBLE);
                ui.layKey3.setVisibility(View.INVISIBLE);
                return 0;
            }
            ui.txtNodeName.setText(myNode.name);

            ui.txtKey1.setText(G.T.getSentence(120303)); // Fast
            ui.txtKey2.setText(G.T.getSentence(120302)); // Slow
            ui.txtKey3.setText(G.T.getSentence(1204)); // Pump

            if (switches[0].value == 0) {
                ui.imgKey3.setImageResource(R.drawable.lay_cooler_water_off);
            } else if (switches[0].value == 1) {
                ui.imgKey3.setImageResource(R.drawable.lay_cooler_water_on);
            }
            if (switches[1].value == 0) {
                ui.imgKey1.setImageResource(R.drawable.lay_cooler_fast_off);
                ui.imgKey2.setImageResource(R.drawable.lay_cooler_slow_off);
            } else if (switches[1].value == 1) {
                ui.imgKey1.setImageResource(R.drawable.lay_cooler_fast_off);
                ui.imgKey2.setImageResource(R.drawable.lay_cooler_slow_on);
            } else if (switches[1].value == 2) {
                ui.imgKey1.setImageResource(R.drawable.lay_cooler_fast_on);
                ui.imgKey2.setImageResource(R.drawable.lay_cooler_slow_off);
            }

            if (myNode.isFavorite)
                ui.imgFavorites.setImageResource(R.drawable.icon_fav_full);
            else
                ui.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
            ui.imgFavorites.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    myNode.isFavorite = !myNode.isFavorite;
                    Database.Node.edit(myNode);
                    if (myNode.isFavorite)
                        ui.imgFavorites.setImageResource(R.drawable.icon_fav_full);
                    else
                        ui.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
                }
            });

            ui.imgbtnEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_w2.class);
                    fw2.putExtra("NODE_ID", myNode.iD);
                    G.currentActivity.startActivity(fw2);
                }
            });
            ui.imgKey1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (!isBusy)
                        if (myNode.isIoModuleNode == 1) {

                            if (switches[2].value == 1)
                                switchKey(2, 0, null, LogOperator.OPERAOR, G.currentUser.iD);
                            else
                                switchKey(2, 1, null, LogOperator.OPERAOR, G.currentUser.iD);
                        } else {
                            if (switches[1].value == 2)
                                switchKey(1, 0, null, LogOperator.OPERAOR, G.currentUser.iD);
                            else
                                switchKey(1, 1, null, LogOperator.OPERAOR, G.currentUser.iD);
                        }
                }
            });

            ui.imgKey2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {// slow
                    if (!isBusy)
                        if (myNode.isIoModuleNode == 1) {
                            if (switches[1].value == 1)// slow is on
                                switchKey(1, 0, null, LogOperator.OPERAOR, G.currentUser.iD);// slow goes on
                            else
                                switchKey(1, 1, null, LogOperator.OPERAOR, G.currentUser.iD);// slow goes off

                        } else {
                            if (switches[1].value == 1) // slow is off
                                switchKey(1, 0, null, LogOperator.OPERAOR, G.currentUser.iD);
                            else
                                switchKey(1, 1, null, LogOperator.OPERAOR, G.currentUser.iD);
                        }

                }
            });

            ui.imgKey3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (!isBusy)
                        if (myNode.isIoModuleNode == 1) {
                            if (switches[0].value == 1)// water is on
                                switchKey(0, 0, null, LogOperator.OPERAOR, G.currentUser.iD);
                            else
                                switchKey(0, 1, null, LogOperator.OPERAOR, G.currentUser.iD);
                        } else {
                            if (switches[0].value == 0)// water is off
                                switchKey(0, 1, null, LogOperator.OPERAOR, G.currentUser.iD);
                            else
                                switchKey(0, 0, null, LogOperator.OPERAOR, G.currentUser.iD);
                        }

                }
            });
            if (myNode.status == 0)
                ui.prgDoOperation.setVisibility(View.VISIBLE);
            else
                ui.prgDoOperation.setVisibility(View.INVISIBLE);
            coolerUIs.put(uiCount, ui);
            G.log("NodeID=" + myNode.iD + "  ui code : " + uiCount);
            return uiCount;
        }

        @Override
        public void setProgressVisiblity(final boolean visiblity) {

            G.HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < coolerUIs.size(); i++) {
                        if (visiblity == true) {
                            coolerUIs.valueAt(i).prgDoOperation.setVisibility(View.VISIBLE);
                        } else {
                            coolerUIs.valueAt(i).prgDoOperation.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        @Override
        public void setSettingVisiblity(int uiIndex, boolean isVisible) {
            G.log("Ui Index :" + uiIndex);
            if (isVisible) {
                coolerUIs.get(uiIndex).imgbtnEdit.setVisibility(View.VISIBLE);
            } else {
                coolerUIs.get(uiIndex).imgbtnEdit.setVisibility(View.GONE);
            }
        }

        @Override
        public void processResult(String result) {
            G.log("Result is :" + result);
            setLastCommTime(System.currentTimeMillis());

            setProgressVisiblity(false);

            float[] previousValues;

            if (myNode.isIoModuleNode == 1) {

                previousValues = new float[16];
                try {

                    // hame 16 ta port ro mikhoonim va value jadid ro too db save mikonim
                    //value ghabli ro ham darim
                    for (int i = 0; i < switches.length; i++) {
                        int index = 0;
                        if (switches[i].IOModulePort > 9 && switches[i].IOModulePort != 0) {// ex: *101
                            index = result.indexOf("*" + switches[i].IOModulePort, 0) + 3;
                        } else {
                            index = result.indexOf("*" + switches[i].IOModulePort, 0) + 2;
                        }

                        previousValues[switches[i].IOModulePort] = switches[i].value;
                        switches[i].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[i]);
                    }
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
            } else {

                previousValues = new float[2];
//            float[] previusValues = new float[3];
                try {

                    int index = result.indexOf("*3", 0) + 7;
//                int index = result.indexOf("*" + ports.get(0), 0) + 7;
                    previousValues[0] = switches[0].value;
                    String state = result.substring(index, index + 1);
                    if (state.equalsIgnoreCase("0"))
                        switches[0].value = 0;
                    else if (state.equalsIgnoreCase("1"))
                        switches[0].value = 1;
                    Database.Switch.edit(switches[0]);
                    //    SysLog.log("Change switch " + switches[i].name + " status", SysLog.LogType.DATA_CHANGE, op, operatorID);

                    index = result.indexOf("*4", 0) + 5;
//                index = result.indexOf("*" + ports.get(1), 0) + 5;
                    previousValues[1] = switches[1].value;
                    state = result.substring(index, index + 7);
                    if (state.equalsIgnoreCase("AC_STOP"))
                        switches[1].value = 0;
                    else if (state.equalsIgnoreCase("AC_SLOW"))
                        switches[1].value = 1;
                    else if (state.equalsIgnoreCase("AC_FAST"))
                        switches[1].value = 2;
                    Database.Switch.edit(switches[1]);
                    //   SysLog.log("Change switch " + switches[i].name + " status", SysLog.LogType.DATA_CHANGE, op, operatorID);
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
            }

            try {
                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < coolerUIs.size(); i++) {
                            int k = coolerUIs.keyAt(i);
                            if (myNode.isIoModuleNode != 1) {
                                if (switches[1].value == 0 && switches[2].value == 0) {
                                    coolerUIs.get(k).imgKey1.setImageResource(R.drawable.lay_cooler_fast_off);
                                    coolerUIs.get(k).imgKey2.setImageResource(R.drawable.lay_cooler_slow_off);
                                } else if (switches[1].value == 0) {
                                    coolerUIs.get(k).imgKey1.setImageResource(R.drawable.lay_cooler_fast_off);
                                    coolerUIs.get(k).imgKey2.setImageResource(R.drawable.lay_cooler_slow_off);
                                } else if (switches[1].value == 1) {
                                    coolerUIs.get(k).imgKey1.setImageResource(R.drawable.lay_cooler_fast_off);
                                    coolerUIs.get(k).imgKey2.setImageResource(R.drawable.lay_cooler_slow_on);
                                } else if (switches[2].value == 1) {
                                    coolerUIs.get(k).imgKey1.setImageResource(R.drawable.lay_cooler_fast_on);
                                    coolerUIs.get(k).imgKey2.setImageResource(R.drawable.lay_cooler_slow_off);
                                }
                                if (switches[0].value == 0) {
                                    coolerUIs.get(k).imgKey3.setImageResource(R.drawable.lay_cooler_water_off);
                                } else if (switches[0].value == 1) {
                                    coolerUIs.get(k).imgKey3.setImageResource(R.drawable.lay_cooler_water_on);
                                }
                            } else {
                                if (switches[0].value == 0) // water off
                                    coolerUIs.get(k).imgKey3.setImageResource(R.drawable.lay_cooler_water_off);
                                if (switches[0].value == 1) // slow on
                                    coolerUIs.get(k).imgKey3.setImageResource(R.drawable.lay_cooler_water_on);

                                if (switches[1].value == 0 && switches[2].value == 0) {
                                    coolerUIs.get(k).imgKey1.setImageResource(R.drawable.lay_cooler_fast_off);
                                    coolerUIs.get(k).imgKey2.setImageResource(R.drawable.lay_cooler_slow_off);
                                } else if (switches[1].value == 0) {
                                    coolerUIs.get(k).imgKey2.setImageResource(R.drawable.lay_cooler_slow_off);
                                    coolerUIs.get(k).imgKey1.setImageResource(R.drawable.lay_cooler_fast_on);
                                    // slow off
                                } else if (switches[1].value == 1) {
                                    coolerUIs.get(k).imgKey2.setImageResource(R.drawable.lay_cooler_slow_on);
                                    coolerUIs.get(k).imgKey1.setImageResource(R.drawable.lay_cooler_fast_off);
// slow on
                                } else if (switches[2].value == 0) {
                                    coolerUIs.get(k).imgKey1.setImageResource(R.drawable.lay_cooler_fast_off);
                                    coolerUIs.get(k).imgKey2.setImageResource(R.drawable.lay_cooler_slow_on);
                                    // fast off
                                } else if (switches[2].value == 1) {
                                    coolerUIs.get(k).imgKey1.setImageResource(R.drawable.lay_cooler_fast_on);
                                    coolerUIs.get(k).imgKey2.setImageResource(R.drawable.lay_cooler_slow_off);
                                    // fast on
                                }


                            }
                        }
                    }
                });

                //  Send message to server and local Mobiles
                NetMessage netMessage = new NetMessage();
                netMessage.data = myNode.getNodeStatusJson(false);
                netMessage.action = NetMessage.Update;
                netMessage.type = NetMessage.SwitchStatus;
                netMessage.typeName = NetMessageType.SwitchStatus;
                netMessage.messageID = netMessage.save();
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);


                // Run scenarios that uses this condition.
                for (int i = 0; i < 2; i++)
                    if (previousValues[i] != switches[i].value) {
                        G.log("previusValues [" + i + "] =" + previousValues[i] + "   New Value[" + i + "] = " + switches[i].value);
                        G.scenarioBP.runBySwitchStatus(switches[i]);
                    }

            } catch (
                    Exception e)

            {
                G.printStackTrace(e);
            }

        }

        @Override
        public void executeCommandChangeSwitchValue(String message, NetMessage originalNetMessage, LogOperator op, int operatorID) {
            try {
                JSONObject jo = new JSONObject(message);
                float newValue = (float) jo.getDouble("Value");
                String switchCode = jo.getString("SwitchCode");
                for (int i = 0; i < switches.length; i++) {
                    if (switches[i].code.equals(switchCode)) {
                        G.log("Node ID:" + myNode.iD + " SwitchCode:" + switchCode +
                                "  SwitchIndex:" + i + "  Value:" + newValue);
                        switchKey(i, newValue, originalNetMessage, op, operatorID);
                    }
                }
            } catch (JSONException e) {
                G.printStackTrace(e);
            }
        }

        public void switchKey(final int switchIndex, final float newValue, final NetMessage originalNetMessage, final LogOperator op, final int operatorID) {
            G.log("SwitchKey switchIndex:" + switchIndex + " - New value:" + newValue);
            SysLog.log("Cooler:" + myNode.name + " Switch:" + switches[switchIndex].name + " Value Changed from " + switches[switchIndex].value + " to " + newValue, LogType.VALUE_CHANGE, op, operatorID);
            final NodeMsg nodeMsg = new NodeMsg();

            setProgressVisiblity(true);
            if (myNode.isIoModuleNode == 1) {

                //baraye inke dore kam va ziad ham zaman roshan nashavnd
                if (switchIndex == 2) {
                    nodeMsg.sentData = "*" + switches[switchIndex].IOModulePort + (int) newValue
                            + "*" + switches[switchIndex - 1].IOModulePort + 0;
                } else if (switchIndex == 1) {
                    nodeMsg.sentData = "*" + switches[switchIndex].IOModulePort + (int) newValue
                            + "*" + switches[switchIndex + 1].IOModulePort + 0;
                } else if (switchIndex == 0) {
                    nodeMsg.sentData = "*" + switches[switchIndex].IOModulePort + (int) newValue;
                }

            } else {
                switch (switchIndex) {
                    case 0:
                        String currentSpeedStr = "AC_STOP";
                        switch ((int) switches[1].value) {
                            case 0:
                                currentSpeedStr = "AC_STOP";
                                break;
                            case 1:
                                currentSpeedStr = "AC_SLOW";
                                break;
                            case 2:
                                currentSpeedStr = "AC_FAST";
                                break;
                        }
                        nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SWAC.toString() + "*3Pump:" + (int) newValue + "*4AC:" + currentSpeedStr + "#");
                        break;
                    case 1:
                        String newSpeedStr = "AC_STOP";
                        switch ((int) newValue) {
                            case 0:
                                newSpeedStr = "AC_STOP";
                                break;
                            case 1:
                                newSpeedStr = "AC_SLOW";
                                break;
                            case 2:
                                newSpeedStr = "AC_FAST";
                                break;
                        }
                        nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SWAC.toString() + "*3Pump:" + (int) switches[0].value + "*4AC:" + newSpeedStr + "#");
                        break;
                }
            }


            nodeMsg.msgType = NodeMsgType.MSG_DO_OPERATION;
            nodeMsg.node = myNode;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMessageToNode(nodeMsg.sentData);
                }
            });
            thread.start();
        }
    }

    public static final class CurtainSwitch extends SampleNode {
        private SparseArray<CO_l_node_simple_key> curtainUIs = new SparseArray<CO_l_node_simple_key>();
        private Database.Switch.Struct[] switches;
        private boolean isBusy;


        public CurtainSwitch(Database.Node.Struct node) {
            super(G.context);
            this.myNode = node;
            switches = select("NodeID=" + myNode.iD);
            if (myNode.isIoModuleNode != 1)
                connectToNode();
        }

        @Override
        public void resetUis() {
            for (int i = 0; i < curtainUIs.size(); i++) {
                curtainUIs.removeAt(i);
            }
        }

        @Override
        public int addUI(View view) {
            uiCount++;
            if (curtainUIs.size() > 5) {
                G.log("Deleting some UI references to reduce memory");
                curtainUIs.removeAt(0);
            }
            G.log("This is adding Simple Node UI for node : " + myNode.iD);
            final CO_l_node_simple_key ui = new CO_l_node_simple_key(view);
            ui.prgDoOperation.setVisibility(View.INVISIBLE);
            switches = select("NodeID=" + myNode.iD);
            if (switches == null) {
                G.log("Switches count is ziro... could not add any UI");
                ui.layKey1.setVisibility(View.INVISIBLE); // Open
                ui.layKey2.setVisibility(View.INVISIBLE); // Stop
                ui.layKey3.setVisibility(View.INVISIBLE); // Close
                return 0;
            }
            ui.txtNodeName.setText(myNode.name);

//            ui.txtKey1.setText(G.T.getSentence(120503)); // Open
//            ui.txtKey2.setText(G.T.getSentence(120502)); // Stop
//            ui.txtKey3.setText(G.T.getSentence(120501)); // Close
            ui.txtKey1.setText(G.T.getSentence(120502)); // Open
            ui.txtKey2.setText(G.T.getSentence(120503)); // Stop
            ui.txtKey3.setText(G.T.getSentence(120501)); // Close
            if (switches[0].value == 0) {
                ui.imgKey1.setImageResource(R.drawable.lay_curtain_open_off);
                ui.imgKey2.setImageResource(R.drawable.lay_curtain_stop_off);
                ui.imgKey3.setImageResource(R.drawable.lay_curtain_close_on);
            } else if (switches[0].value == 1) {
                ui.imgKey1.setImageResource(R.drawable.lay_curtain_open_on);
                ui.imgKey2.setImageResource(R.drawable.lay_curtain_stop_off);
                ui.imgKey3.setImageResource(R.drawable.lay_curtain_close_off);
            } else if (switches[0].value == 2) {
                ui.imgKey1.setImageResource(R.drawable.lay_curtain_open_off);
                ui.imgKey2.setImageResource(R.drawable.lay_curtain_stop_on);
                ui.imgKey3.setImageResource(R.drawable.lay_curtain_close_off);
            }

            if (myNode.isFavorite)
                ui.imgFavorites.setImageResource(R.drawable.icon_fav_full);
            else
                ui.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
            ui.imgFavorites.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    myNode.isFavorite = !myNode.isFavorite;
                    Database.Node.edit(myNode);
                    if (myNode.isFavorite)
                        ui.imgFavorites.setImageResource(R.drawable.icon_fav_full);
                    else
                        ui.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
                }
            });

            ui.imgbtnEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_w2.class);
                    fw2.putExtra("NODE_ID", myNode.iD);
                    G.currentActivity.startActivity(fw2);
                }
            });
            ui.imgKey1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {//open
                    if (!isBusy)
                        if (myNode.isIoModuleNode == 1)
                            switchKey(0, 1, null, LogOperator.OPERAOR, G.currentUser.iD);
                        else
                            switchKey(2, 1, null, LogOperator.OPERAOR, G.currentUser.iD);
                }

            });

            ui.imgKey2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {//stop
                    if (!isBusy)
                        if (myNode.isIoModuleNode == 1)
                            switchKey(0, 0, null, LogOperator.OPERAOR, G.currentUser.iD);
                        else
                            switchKey(1, 2, null, LogOperator.OPERAOR, G.currentUser.iD);
                }
            });

            ui.imgKey3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {//close
                    if (!isBusy)
                        if (myNode.isIoModuleNode == 1)
                            switchKey(1, 1, null, LogOperator.OPERAOR, G.currentUser.iD);
                        else
//                            switchKey(0, null, LogOperator.OPERAOR, G.currentUser.iD);
                            switchKey(0, 0, null, LogOperator.OPERAOR, G.currentUser.iD);
                }
            });
            if (myNode.status == 0)
                ui.prgDoOperation.setVisibility(View.VISIBLE);
            else
                ui.prgDoOperation.setVisibility(View.INVISIBLE);
            curtainUIs.put(uiCount, ui);
            G.log("NodeID=" + myNode.iD + "  ui code : " + uiCount);
            return uiCount;
        }

        @Override
        public void setProgressVisiblity(final boolean visiblity) {
            G.HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < curtainUIs.size(); i++) {
                        if (visiblity == true) {
                            curtainUIs.valueAt(i).prgDoOperation.setVisibility(View.VISIBLE);
                        } else {
                            curtainUIs.valueAt(i).prgDoOperation.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        @Override
        public void setSettingVisiblity(int uiIndex, boolean isVisible) {
            G.log("Ui Index :" + uiIndex);
            if (isVisible) {
                curtainUIs.get(uiIndex).imgbtnEdit.setVisibility(View.VISIBLE);
            } else {
                curtainUIs.get(uiIndex).imgbtnEdit.setVisibility(View.GONE);
            }
        }

        @Override
        public void executeCommandChangeSwitchValue(String message, NetMessage originalNetMessage, LogOperator op, int operatorID) {
            try {
                JSONObject jo = new JSONObject(message);
                float newValue = (float) jo.getDouble("Value");
                String switchCode = jo.getString("SwitchCode");
                for (int i = 0; i < switches.length; i++) {
                    if (switches[i].code.equals(switchCode)) {
                        G.log("Node ID:" + myNode.iD + " SwitchCode:" + switchCode +
                                "  SwitchIndex:" + i + "  Value:" + newValue);
                        switchKey(i, newValue, originalNetMessage, op, operatorID);

                    }
                }
            } catch (JSONException e) {
                G.printStackTrace(e);
            }
        }

        @Override
        public void processResult(String result) {
            G.log("Result is :" + result);
            // example result *1AS*2IOMudole*30*41*50*60*70*80*91*100*110*120*130*140*150*161*170*181#
            setLastCommTime(System.currentTimeMillis());

            setProgressVisiblity(false);

            float[] previousValues;

            if (myNode.isIoModuleNode == 1) {

                previousValues = new float[16];
                try {

                    // hame 16 ta port ro mikhoonim va value jadid ro too db save mikonim
                    //value ghabli ro ham darim
                    for (int i = 0; i < switches.length; i++) {
                        int index = 0;
                        if (switches[i].IOModulePort > 9 && switches[i].IOModulePort != 0) {// ex: *101
                            index = result.indexOf("*" + switches[i].IOModulePort, 0) + 3;
                        } else {
                            index = result.indexOf("*" + switches[i].IOModulePort, 0) + 2;
                        }

                        previousValues[switches[i].IOModulePort] = switches[i].value;
                        switches[i].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[i]);
                    }
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
            } else {

                previousValues = new float[3];

                try {
                    int index = result.indexOf("*3", 0) + 5;
                    previousValues[0] = switches[0].value;
                    String state = result.substring(index, index + 7);
                    if (state.equalsIgnoreCase("CT_CLOSE"))
                        switches[0].value = 0;
                    else if (state.equalsIgnoreCase("CT_OPEN"))
                        switches[0].value = 1;
                    else if (state.equalsIgnoreCase("CT_STOP"))
                        switches[0].value = 2;
                    Database.Switch.edit(switches[0]);
                    //                SysLog.log("Change switch " + switches[i].name + " status", SysLog.LogType.DATA_CHANGE, op, operatorID);

                } catch (Exception e) {
                    G.printStackTrace(e);
                }
            }
            try {
                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < curtainUIs.size(); i++) {
                            int k = curtainUIs.keyAt(i);

                            if (myNode.isIoModuleNode == 1) {
                                if (switches[0].value == 1 && switches[1].value == 0) {// open bashe
                                    curtainUIs.get(k).imgKey1.setImageResource(R.drawable.lay_curtain_open_on);
                                    curtainUIs.get(k).imgKey2.setImageResource(R.drawable.lay_curtain_stop_off);
                                    curtainUIs.get(k).imgKey3.setImageResource(R.drawable.lay_curtain_close_off);

                                } else if (switches[0].value == 0 && switches[1].value == 1) {// close bashe
                                    curtainUIs.get(k).imgKey1.setImageResource(R.drawable.lay_curtain_open_off);
                                    curtainUIs.get(k).imgKey2.setImageResource(R.drawable.lay_curtain_stop_off);
                                    curtainUIs.get(k).imgKey3.setImageResource(R.drawable.lay_curtain_close_on);
                                } else {// stop bashe
                                    curtainUIs.get(k).imgKey1.setImageResource(R.drawable.lay_curtain_open_off);
                                    curtainUIs.get(k).imgKey2.setImageResource(R.drawable.lay_curtain_stop_on);
                                    curtainUIs.get(k).imgKey3.setImageResource(R.drawable.lay_curtain_close_off);
                                }
                            } else {
                                if (switches[0].value == 0) {
                                    curtainUIs.get(k).imgKey1.setImageResource(R.drawable.lay_curtain_open_off);
                                    curtainUIs.get(k).imgKey2.setImageResource(R.drawable.lay_curtain_stop_off);
                                    curtainUIs.get(k).imgKey3.setImageResource(R.drawable.lay_curtain_close_on);
                                } else if (switches[0].value == 1) {
                                    curtainUIs.get(k).imgKey1.setImageResource(R.drawable.lay_curtain_open_on);
                                    curtainUIs.get(k).imgKey2.setImageResource(R.drawable.lay_curtain_stop_off);
                                    curtainUIs.get(k).imgKey3.setImageResource(R.drawable.lay_curtain_close_off);
                                } else if (switches[0].value == 2) {
                                    curtainUIs.get(k).imgKey1.setImageResource(R.drawable.lay_curtain_open_off);
                                    curtainUIs.get(k).imgKey2.setImageResource(R.drawable.lay_curtain_stop_on);
                                    curtainUIs.get(k).imgKey3.setImageResource(R.drawable.lay_curtain_close_off);
                                }
                            }
                        }
                    }
                });

                //  Send message to server and local Mobiles
                NetMessage netMessage = new NetMessage();
                netMessage.data = myNode.getNodeStatusJson(false);
                netMessage.action = NetMessage.Update;
                netMessage.type = NetMessage.SwitchStatus;
                netMessage.typeName = NetMessageType.SwitchStatus;
                netMessage.messageID = netMessage.save();
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);


                // Run scenarios that uses this condition.
                if (previousValues[0] != switches[0].value) {
                    G.log("previusValues [" + 0 + "] =" + previousValues[0] + "   New Value[" + 0 + "] = " + switches[0].value);
                    G.scenarioBP.runBySwitchStatus(switches[0]);
                }

            } catch (
                    Exception e)

            {
                G.printStackTrace(e);
            }

        }

//        @Override
//        public void executeCommandChangeSwitchValue(String message, NetMessage originalNetMessage, LogOperator op, int operatorID) {
//            try {
//                JSONObject jo = new JSONObject(message);
//                float newValue = (float) jo.getDouble("Value");
//                String switchCode = jo.getString("SwitchCode");
//                for (int i = 0; i < switches.length; i++) {
//                    if (switches[i].code.equals(switchCode)) {
//                        G.log("Node ID:" + myNode.iD + " SwitchCode:" + switchCode +
//                                "  SwitchIndex:" + i + "  Value:" + newValue);
//                        switchKey(newValue, originalNetMessage, op, operatorID);
//                    }
//                }
//            } catch (JSONException e) {
//                G.printStackTrace(e);
//            }
//        }


        public void switchKey(final int switchIndex, final float newValue, final NetMessage originalNetMessage, final LogOperator op, final int operatorID) {
            G.log("SwitchKey - New value:" + newValue);
            SysLog.log("Curtain:" + myNode.name + " Value Changed from " + switches[0].value + " to " + newValue, LogType.VALUE_CHANGE, op, operatorID);

            setProgressVisiblity(true);

            final NodeMsg nodeMsg = new NodeMsg();

            if (myNode.isIoModuleNode == 1) {

                //baraye inke switch baz o baste hamzaman roshan nashavand
                if (switches.length == 2) {
                    if (switchIndex == 1) {
                        nodeMsg.sentData = "*" + switches[switchIndex].IOModulePort + (int) newValue
                                + "*" + switches[switchIndex - 1].IOModulePort + 0;
                    } else if (switchIndex == 0) {
                        nodeMsg.sentData = "*" + switches[switchIndex].IOModulePort + (int) newValue
                                + "*" + switches[switchIndex + 1].IOModulePort + 0;
                    }
                }


            }

//            if (myNode.isIoModuleNode == 1) {
//                ArrayList<Integer> values = new ArrayList<>();
//                switch ((int) newValue) {
//                    case 0:// open
//                        values.add(1);
//                        values.add(0);
//                        break;
//                    case 1://stop
//                        values.add(0);
//                        values.add(0);
//                        break;
//                    case 2:// close
//                        values.add(0);
//                        values.add(1);
//                        break;
//                }
//
//                nodeMsg.sentData = "";
//                for (int i = 0; i < switches.length; i++) {
//                    nodeMsg.sentData += "*" + switches[i].IOModulePort + values.get(i);
//                }
//
//                G.log("sent data to curtain is: " + nodeMsg.sentData);
//            }


            else {
                if (((int) newValue) == 0) {
                    nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SWCT.toString() + "*3CT:CT_CLOSE#");
                } else if (((int) newValue) == 1) {
                    nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SWCT.toString() + "*3CT:CT_OPEN#");
                } else if (((int) newValue) == 2) {
                    nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SWCT.toString() + "*3CT:CT_STOP#");
                }
            }

            nodeMsg.msgType = NodeMsgType.MSG_DO_OPERATION;
            nodeMsg.node = myNode;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMessageToNode(nodeMsg.sentData);
                }
            });
            thread.start();
        }

    }

    public static final class WCSwitch extends SampleNode {
        private SparseArray<CO_l_node_simple_key> WCKeyUIs = new SparseArray<CO_l_node_simple_key>();
        private Database.Switch.Struct[] switches;
        private boolean isBusy;

        public WCSwitch(Database.Node.Struct node) {
            super(G.context);
            this.myNode = node;
            switches = select("NodeID=" + myNode.iD);
            if (myNode.isIoModuleNode != 1)
                connectToNode();
        }

        @Override
        public void resetUis() {
            for (int i = 0; i < WCKeyUIs.size(); i++) {
                WCKeyUIs.removeAt(i);
            }
        }

        @Override
        public int addUI(View view) {
            uiCount++;
            if (WCKeyUIs.size() > 5) {
                G.log("Deleting some UI references to reduce memory");
                WCKeyUIs.removeAt(0);
            }
            G.log("This is adding Simple Node UI for node : " + myNode.iD);
            final CO_l_node_simple_key ui = new CO_l_node_simple_key(view);
            try {
                ui.prgDoOperation.setVisibility(View.INVISIBLE);
            } catch (NullPointerException e) {
                G.log(e.getMessage());
            }
            switches = select("NodeID=" + myNode.iD);
            if (switches == null) {
                G.log("Switches count is not enough... could not add any UI");
                ui.layKey1.setVisibility(View.INVISIBLE);
                ui.layKey2.setVisibility(View.INVISIBLE);
                ui.layKey3.setVisibility(View.INVISIBLE);
                return 0;
            } else {
                ui.layKey3.setVisibility(View.GONE);
                ui.txtKey3.setVisibility(View.GONE);
                ui.layKey2.setVisibility(View.GONE);
                ui.txtKey3.setVisibility(View.GONE);
            }
            ui.txtNodeName.setText(myNode.name);

            ui.txtKey1.setText(G.T.getSentence(1206)); // LAMP

            if (switches[0].value == 0) {
                ui.imgKey1.setImageResource(R.drawable.lay_simple_switch_off);// lamp off
            } else if (switches[0].value == 1) {
                ui.imgKey1.setImageResource(R.drawable.lay_simple_switch_on);// lamp on
            }

            if (myNode.isFavorite)
                ui.imgFavorites.setImageResource(R.drawable.icon_fav_full);
            else
                ui.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
            ui.imgFavorites.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    myNode.isFavorite = !myNode.isFavorite;
                    Database.Node.edit(myNode);
                    if (myNode.isFavorite)
                        ui.imgFavorites.setImageResource(R.drawable.icon_fav_full);
                    else
                        ui.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
                }
            });

            ui.imgbtnEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_w2.class);
                    fw2.putExtra("NODE_ID", myNode.iD);
                    G.currentActivity.startActivity(fw2);
                }
            });

            ui.imgKey1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (!isBusy)
                        if (switches[0].value == 0)
                            switchKey(0, 1, null, LogOperator.OPERAOR, G.currentUser.iD);
                        else if (switches[0].value == 1)
                            switchKey(0, 0, null, LogOperator.OPERAOR, G.currentUser.iD);
                }
            });


            if (myNode.status == 0)
                ui.prgDoOperation.setVisibility(View.VISIBLE);
            else
                ui.prgDoOperation.setVisibility(View.INVISIBLE);
            WCKeyUIs.put(uiCount, ui);
            G.log("NodeID=" + myNode.iD + "  ui code : " + uiCount);
            return uiCount;
        }

        @Override
        public void setProgressVisiblity(final boolean visiblity) {

            G.HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < WCKeyUIs.size(); i++) {
                        if (visiblity == true) {
                            WCKeyUIs.valueAt(i).prgDoOperation.setVisibility(View.VISIBLE);
                        } else {
                            WCKeyUIs.valueAt(i).prgDoOperation.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        @Override
        public void setSettingVisiblity(int uiIndex, boolean isVisible) {
            G.log("Ui Index :" + uiIndex);
            if (isVisible) {
                WCKeyUIs.get(uiIndex).imgbtnEdit.setVisibility(View.VISIBLE);
            } else {
                WCKeyUIs.get(uiIndex).imgbtnEdit.setVisibility(View.GONE);
            }
        }

        @Override
        public void processResult(String result) {
            G.log("Result is :" + result);
            setLastCommTime(System.currentTimeMillis());

            try {

                setProgressVisiblity(false);

                float[] previousValues;

                if (myNode.isIoModuleNode == 1) {

                    previousValues = new float[16];
                    try {

                        // hame 16 ta port ro mikhoonim va value jadid ro too db save mikonim
                        //value ghabli ro ham darim
                        for (int i = 0; i < switches.length; i++) {
                            int index = 0;
                            if (switches[i].IOModulePort > 9 && switches[i].IOModulePort != 0) {// ex: *101
                                index = result.indexOf("*" + switches[i].IOModulePort, 0) + 3;
                            } else {
                                index = result.indexOf("*" + switches[i].IOModulePort, 0) + 2;
                            }

                            previousValues[switches[i].IOModulePort] = switches[i].value;
                            switches[i].value = Integer.parseInt(result.substring(index, index + 1));
                            Database.Switch.edit(switches[i]);
                        }
                    } catch (Exception e) {
                        G.printStackTrace(e);
                    }
                } else {


                    previousValues = new float[2];

//                int index = result.indexOf("*3", 0) + 2;
//                previousValues[0] = switches[0].value;
//                switches[0].value = Integer.parseInt(result.substring(index, index + 1));
//                Database.Switch.edit(switches[0]);
                    int index = result.indexOf("*4", 0) + 2;
                    previousValues[0] = switches[0].value;
                    switches[0].value = Integer.parseInt(result.substring(index, index + 1));
                    Database.Switch.edit(switches[0]);

                }


                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < WCKeyUIs.size(); i++) {
                            int k = WCKeyUIs.keyAt(i);
                            if (switches[0].value == 0) {
                                WCKeyUIs.get(k).imgKey1.setImageResource(R.drawable.lay_simple_switch_off);// lamp off
                            } else if (switches[0].value == 1) {
                                WCKeyUIs.get(k).imgKey1.setImageResource(R.drawable.lay_simple_switch_on);// lamp on
                            }
                        }
                    }
                });


                //  Send message to server and local Mobiles
                NetMessage netMessage = new NetMessage();
                netMessage.data = myNode.getNodeStatusJson(false);// true means > isIOModule = 1
                netMessage.action = NetMessage.Update;
                netMessage.type = NetMessage.SwitchStatus;
                netMessage.typeName = NetMessageType.SwitchStatus;
                netMessage.messageID = netMessage.save();
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);

                // Run scenarios that uses this condition.
                for (int i = 0; i < 3; i++)
                    if (switches.length > i && previousValues[i] != switches[i].value) {
                        G.log("previusValues [" + i + "] =" + previousValues[i] + "   New Value[" + i + "] = " + switches[i].value);
                        G.scenarioBP.runBySwitchStatus(switches[i]);
                    }

            } catch (Exception e) {
                G.printStackTrace(e);
            }

        }

        @Override
        public void executeCommandChangeSwitchValue(String message, NetMessage originalNetMessage, LogOperator op, int operatorID) {
            try {
                JSONObject jo = new JSONObject(message);
                float newValue = (float) jo.getDouble("Value");
                String switchCode = jo.getString("SwitchCode");
                for (int i = 0; i < switches.length; i++) {
                    if (switches[i].code.equals(switchCode)) {
                        G.log("Node ID:" + myNode.iD + " SwitchCode:" + switchCode +
                                "  SwitchIndex:" + i + "  Value:" + newValue);
                        switchKey(i, newValue, originalNetMessage, op, operatorID);
                    }
                }
            } catch (JSONException e) {
                G.printStackTrace(e);
            }
        }

        public void switchKey(int switchIndex, LogOperator op, int operatorID) {
            final NodeMsg nodeMsg = new NodeMsg();
            nodeMsg.msgType = NodeMsgType.MSG_DO_OPERATION;
            nodeMsg.node = myNode;
            int newValue;
            if (switches[switchIndex].value == 0)
                newValue = 1;
            else
                newValue = 0;
            switchKey(switchIndex, newValue, null, op, operatorID);
        }

        public void switchKey(final int switchIndex, final float newValue, final NetMessage originalNetMessage, final LogOperator op, final int operatorID) {
            G.log("SwitchKey switchIndex:" + switchIndex + " - New value:" + newValue);
            SysLog.log("SimpleKey:" + myNode.name + " Switch:" + switches[switchIndex].name + " Value Changed from " + switches[switchIndex].value + " to " + newValue, LogType.VALUE_CHANGE, op, operatorID);

            setProgressVisiblity(true);

            final NodeMsg nodeMsg = new NodeMsg();

            if (myNode.isIoModuleNode == 1) {

                nodeMsg.sentData = "*" + switches[switchIndex].IOModulePort + (int) newValue;

            } else {
                nodeMsg.sentData = ("*1SS*2" + Node_Model_Name.SWWC.toString() + "*3-*4" + (int) newValue + "*5-#");
            }
            nodeMsg.msgType = NodeMsgType.MSG_DO_OPERATION;
            nodeMsg.node = myNode;

            try {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendMessageToNode(nodeMsg.sentData);
                    }
                });
                thread.start();
            } catch (Exception e) {
                G.printStackTrace(e);
            }

        }
    }

    public static final class IOModule extends SampleNode {
        private SparseArray<AllViews.CO_l_node_IoModule> UI = new SparseArray<AllViews.CO_l_node_IoModule>();
        private Database.Switch.Struct[] switches;
        private boolean isBusy;

        public IOModule(Database.Node.Struct node) {
            super(G.context);
            this.myNode = node;
            switches = select("isIOModuleSwitch = 1");
            connectToNode();
        }

        @Override
        public void resetUis() {
            UI = null;
        }


        @Override
        public int addUI(View view) {
            uiCount++;
            if (UI.size() > 10) {
                G.log("Deleting some UI references to reduce memory");
                UI.removeAt(0);
            }
//            if (outputKeyUIs.size() > 6) {
//                G.log("Deleting some UI references to reduce memory");
//                outputKeyUIs.removeAt(0);
//            }
            G.log("This is adding Simple Node UI for node : " + myNode.iD);
            final AllViews.CO_l_node_IoModule newIoModule = new AllViews.CO_l_node_IoModule(view);
            newIoModule.prgDoOperation.setVisibility(View.INVISIBLE);
            switches = Database.Switch.select("NodeID=" + myNode.iD);
            newIoModule.txtNodeName.setText(myNode.name);

            if (myNode.isFavorite)
                newIoModule.imgFavorites.setImageResource(R.drawable.icon_fav_full);
            else
                newIoModule.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
            newIoModule.imgFavorites.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    myNode.isFavorite = !myNode.isFavorite;
                    Database.Node.edit(myNode);
                    if (myNode.isFavorite)
                        newIoModule.imgFavorites.setImageResource(R.drawable.icon_fav_full);
                    else
                        newIoModule.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
                }
            });
            newIoModule.imgbtnEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent fw2 = new Intent(G.currentActivity, ActivityMyHouse.class);
                    fw2.putExtra("NODE_ID", myNode.iD);
                    G.currentActivity.startActivity(fw2);
                }
            });
            if (myNode.status == 0)
                newIoModule.prgDoOperation.setVisibility(View.VISIBLE);
            else
                newIoModule.prgDoOperation.setVisibility(View.INVISIBLE);
            UI.put(uiCount, newIoModule);
            G.log("NodeID=" + myNode.iD + "  ui code : " + uiCount);
            return uiCount;
        }


        @Override
        public void setProgressVisiblity(final boolean visiblity) {

            G.HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < UI.size(); i++) {
                        if (visiblity == true) {
                            UI.valueAt(i).prgDoOperation.setVisibility(View.VISIBLE);
                        } else {
                            UI.valueAt(i).prgDoOperation.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        @Override
        public void setSettingVisiblity(int uiIndex, boolean isVisible) {
            G.log("Ui Index :" + uiIndex);
            if (isVisible) {
                UI.get(uiIndex).imgbtnEdit.setVisibility(View.VISIBLE);
            } else {
                UI.get(uiIndex).imgbtnEdit.setVisibility(View.GONE);
            }
        }

        @Override
        public void processResult(String result) {

            for (int i = 0; i < G.nodeCommunication.allNodes.size(); i++) {
                int key = G.nodeCommunication.allNodes.keyAt(i);

                if (G.nodeCommunication.allNodes.get(key).myNode.isIoModuleNode == 1)
                    G.nodeCommunication.allNodes.get(key).processResult(result);

            }

//                for (int i = 0; i < nodeCommunication.allNodes.size(); i++) {
//
//                    Database.Node.Struct node = G.nodeCommunication.allNodes.get(i).getNodeStruct();
//
//                    try {
//                        if (G.nodeCommunication.allNodes.get(i).getIfNodeIsIOModuleNode() == 1)
//                            G.nodeCommunication.allNodes.get(i).processResult(result);
//                    } catch (Exception e) {
//                        continue;
//                    }
//                }

//            G.log("Result is :" + result);
//            setLastCommTime(System.currentTimeMillis());
//            float[] previusValues = new float[16];
//            try {
//
//                // hame 16 ta port ro mikhoonim va value jadid ro too db save mikonim
//                //value ghabli ro ham darim
//                for (int i = 0; i < switches.length; i++) {
//                    int index = 0;
//                    if (switches[i].IOModulePort > 9 && switches[i].IOModulePort != 0) {// ex: *101
//                        index = result.indexOf("*" + switches[i].IOModulePort, 0) + 3;
//                    } else {
//                        index = result.indexOf("*" + switches[i].IOModulePort, 0) + 2;
//                    }
//
//                    previusValues[switches[i].IOModulePort] = switches[i].value;
//                    switches[i].value = Integer.parseInt(result.substring(index, index + 1));
//                    Database.Switch.edit(switches[i]);
//                }
//
//                //  Send message to server and local Mobiles
//                NetMessage netMessage = new NetMessage();
//                netMessage.data = myNode.getNodeStatusJson(true);// true means > isIOModule = 1
//                netMessage.action = NetMessage.Update;
//                netMessage.type = NetMessage.SwitchStatus;
//                netMessage.typeName = NetMessageType.SwitchStatus;
//                netMessage.messageID = netMessage.save();
//                G.mobileCommunication.sendMessage(netMessage);
//                G.server.sendMessage(netMessage);
//
//                // Run scenarios that uses this condition.
//                for (int i = 0; i < 16; i++)
//                    if (switches.length > i && previusValues[i] != switches[i].value) {
//                        G.log("previusValues [" + i + "] =" + previusValues[i] + "   New Value[" + i + "] = " + switches[i].value);
//                        G.scenarioBP.runBySwitchStatus(switches[i]);
//                    }
//
//            } catch (Exception e) {
//                G.printStackTrace(e);
//            }
        }

        @Override
        public void executeCommandChangeSwitchValue(String message, NetMessage
                originalNetMessage, LogOperator op, int operatorID) {
            try {
                JSONObject jo = new JSONObject(message);
                float newValue = (float) jo.getDouble("Value");
                String switchCode = jo.getString("SwitchCode");
                for (int i = 0; i < switches.length; i++) {
                    if (switches[i].code.equals(switchCode)) {
                        G.log("Node ID:" + myNode.iD + " SwitchCode:" + switchCode +
                                "  SwitchIndex:" + i + "  Value:" + newValue);
                        switchKey(i, newValue, originalNetMessage, op, operatorID);
                    }
                }
            } catch (JSONException e) {
                G.printStackTrace(e);
            }
        }

        public void switchKey(final int switchIndex, final float newValue, final NetMessage originalNetMessage, final LogOperator op, final int operatorID) {
            G.log("SwitchKey switchIndex:" + switchIndex + " - New value:" + newValue);
            SysLog.log("SimpleKey:" + myNode.name + " Switch:" + switches[switchIndex].name + " Value Changed from " + switches[switchIndex].value + " to " + newValue, LogType.VALUE_CHANGE, op, operatorID);
            final NodeMsg nodeMsg = new NodeMsg();

            Database.Switch.Struct[] IOModuleSwitches = getIOModuleSwitches();
            IOModuleSwitches[switchIndex].value = (int) newValue;
            Database.Switch.edit(IOModuleSwitches[switchIndex]);

            nodeMsg.sentData = makeResponseData(IOModuleSwitches);
            nodeMsg.msgType = NodeMsgType.MSG_DO_OPERATION;
            nodeMsg.node = myNode;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMessageToNode(nodeMsg.sentData);
                }
            });
            thread.start();
        }

        /**
         * making response data
         * <p>Example: *30*41*50*...*181#</p>
         */
        private String makeResponseData(Database.Switch.Struct[] IOModuleSwitches) {
            String nodeResponse = "*1SS*2" + Node_Model_Name.IOModule;//beginning of response must be this
            for (int i = 3; i < IOModuleSwitches.length; i++) {
                nodeResponse += "*" + i + IOModuleSwitches[i].value;
            }
            return nodeResponse += "#";// last char must be # to define end part of response
        }

        public static Database.Switch.Struct[] getIOModuleSwitches() {
            return Database.Switch.select("isIOModuleSwitch=1 ORDER BY IOModulePort ASC");
        }

    }

    public static final class Sensore extends SampleNode {
        private SparseArray<CO_l_node_simple_key> simpleKeyUIs = new SparseArray<CO_l_node_simple_key>();
        private Database.Switch.Struct[] switches;
        private boolean isBusy;

        public Sensore(Database.Node.Struct node) {
            super(G.context);
            this.myNode = node;
            switches = select("NodeID=" + myNode.iD);
            if (myNode.isIoModuleNode != 1)
                connectToNode();
        }

        @Override
        public void resetUis() {
            for (int i = 0; i < simpleKeyUIs.size(); i++) {
                simpleKeyUIs.removeAt(i);
            }
        }

        @Override
        public int addUI(View view) {
            uiCount++;
            if (simpleKeyUIs.size() > 5) {
                G.log("Deleting some UI references to reduce memory");
                simpleKeyUIs.removeAt(0);
            }
            G.log("This is adding Simple Node UI for node : " + myNode.iD);
            final CO_l_node_simple_key newSimpleKey = new CO_l_node_simple_key(view);
            newSimpleKey.prgDoOperation.setVisibility(View.INVISIBLE);
            switches = select("NodeID=" + myNode.iD);
            if (switches == null) {
                G.log("Switches count is ziro... could not add any UI");
                newSimpleKey.layKey1.setVisibility(View.INVISIBLE);
                newSimpleKey.layKey2.setVisibility(View.INVISIBLE);
                newSimpleKey.layKey3.setVisibility(View.INVISIBLE);
                return 0;
            }
            newSimpleKey.txtNodeName.setText(myNode.name);
            if (switches.length >= 1) {
                newSimpleKey.layKey1.setVisibility(View.VISIBLE);
                newSimpleKey.txtKey1.setText(switches[0].name);
                if (switches[0].value == 0)
                    newSimpleKey.imgKey1.setImageResource(R.drawable.lay_simple_switch_off);
                else
                    newSimpleKey.imgKey1.setImageResource(R.drawable.lay_simple_switch_on);
            } else {
                newSimpleKey.layKey1.setVisibility(View.GONE);
            }
            if (switches.length >= 2) {
                newSimpleKey.layKey2.setVisibility(View.VISIBLE);
                newSimpleKey.txtKey2.setText(switches[1].name);
                if (switches[1].value == 0)
                    newSimpleKey.imgKey2.setImageResource(R.drawable.lay_simple_switch_off);
                else
                    newSimpleKey.imgKey2.setImageResource(R.drawable.lay_simple_switch_on);
            } else {
                newSimpleKey.layKey2.setVisibility(View.GONE);
            }
            if (switches.length >= 3) {
                newSimpleKey.layKey3.setVisibility(View.VISIBLE);
                newSimpleKey.txtKey3.setText(switches[2].name);
                if (switches[2].value == 0)
                    newSimpleKey.imgKey3.setImageResource(R.drawable.lay_simple_switch_off);
                else
                    newSimpleKey.imgKey3.setImageResource(R.drawable.lay_simple_switch_on);
            } else {
                newSimpleKey.layKey3.setVisibility(View.GONE);
            }

            if (myNode.isFavorite)
                newSimpleKey.imgFavorites.setImageResource(R.drawable.icon_fav_full);
            else
                newSimpleKey.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
            newSimpleKey.imgFavorites.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    myNode.isFavorite = !myNode.isFavorite;
                    Database.Node.edit(myNode);
                    if (myNode.isFavorite)
                        newSimpleKey.imgFavorites.setImageResource(R.drawable.icon_fav_full);
                    else
                        newSimpleKey.imgFavorites.setImageResource(R.drawable.icon_fav_empty);
                }
            });
            newSimpleKey.imgbtnEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_w2.class);
                    fw2.putExtra("NODE_ID", myNode.iD);
                    G.currentActivity.startActivity(fw2);
                }
            });

            if (myNode.status == 0)
                newSimpleKey.prgDoOperation.setVisibility(View.VISIBLE);
            else
                newSimpleKey.prgDoOperation.setVisibility(View.INVISIBLE);
            simpleKeyUIs.put(uiCount, newSimpleKey);
            G.log("NodeID=" + myNode.iD + "  ui code : " + uiCount);
            return uiCount;
        }

        @Override
        public void setProgressVisiblity(final boolean visiblity) {

            G.HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < simpleKeyUIs.size(); i++) {
                        if (visiblity == true) {
                            simpleKeyUIs.valueAt(i).prgDoOperation.setVisibility(View.VISIBLE);
                        } else {
                            simpleKeyUIs.valueAt(i).prgDoOperation.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        @Override
        public void setSettingVisiblity(int uiIndex, boolean isVisible) {
            G.log("Ui Index :" + uiIndex);
            if (isVisible) {
                simpleKeyUIs.get(uiIndex).imgbtnEdit.setVisibility(View.VISIBLE);
            } else {
                simpleKeyUIs.get(uiIndex).imgbtnEdit.setVisibility(View.GONE);
            }
        }

        @Override
        public void processResult(String result) {
            Float[] preValue = new Float[16];
            G.log("Result is :" + result);
            setLastCommTime(System.currentTimeMillis());

            setProgressVisiblity(false);

            float[] previousValues = new float[0];

            if (myNode.isIoModuleNode == 1) {

                previousValues = new float[16];
                try {


                    // hame 16 ta port ro mikhoonim va value jadid ro too db save mikonim
                    //value ghabli ro ham darim
                    for (int i = 0; i < switches.length; i++) {
                        int index = 0;
                        if (switches[i].IOModulePort > 9 && switches[i].IOModulePort != 0) {// ex: *101
                            index = result.indexOf("*" + switches[i].IOModulePort, 0) + 3;
                        } else {
                            index = result.indexOf("*" + switches[i].IOModulePort, 0) + 2;
                        }

                        preValue[i] = previousValues[switches[i].IOModulePort] = switches[i].value;
                        switches[i].value = Integer.parseInt(result.substring(index, index + 1));
                        Database.Switch.edit(switches[i]);
                    }
                } catch (Exception e) {
                    G.printStackTrace(e);
                }

            }

            try {
                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < simpleKeyUIs.size(); i++) {
                            int k = simpleKeyUIs.keyAt(i);
                            if (switches.length >= 1)
                                if (switches[0].value == 0) {
                                    simpleKeyUIs.get(k).imgKey1.setImageResource(R.drawable.lay_simple_switch_off);
                                } else {
                                    simpleKeyUIs.get(k).imgKey1.setImageResource(R.drawable.lay_simple_switch_on);
                                }
                            if (switches.length >= 2)
                                if (switches[1].value == 0) {
                                    simpleKeyUIs.get(k).imgKey2.setImageResource(R.drawable.lay_simple_switch_off);
                                } else {
                                    simpleKeyUIs.get(k).imgKey2.setImageResource(R.drawable.lay_simple_switch_on);
                                }
                            if (switches.length >= 3)
                                if (switches[2].value == 0) {
                                    simpleKeyUIs.get(k).imgKey3.setImageResource(R.drawable.lay_simple_switch_off);
                                } else {
                                    simpleKeyUIs.get(k).imgKey3.setImageResource(R.drawable.lay_simple_switch_on);
                                }
                        }
                    }
                });

                //  Send message to server and local Mobiles
//                NetMessage netMessage = new NetMessage();
//                netMessage.data = myNode.getNodeStatusJson(false);// true means > isIOModule = 1
//                netMessage.action = NetMessage.Update;
//                netMessage.type = NetMessage.SwitchStatus;
//                netMessage.typeName = NetMessageType.SwitchStatus;
//                netMessage.messageID = netMessage.save();
//                G.mobileCommunication.sendMessage(netMessage);
//                G.server.sendMessage(netMessage);


                // Run scenarios that uses this condition.
                for (int i = 0; i < 3; i++)

                    if (switches.length > i && preValue[i] != switches[i].value) {
                        G.log("previusValues [" + i + "] =" + preValue[i] + "   New Value[" + i + "] = " + switches[i].value);
                        G.scenarioBP.runBySwitchStatus(switches[i]);
                    }

            } catch (Exception e) {
                G.printStackTrace(e);
            }
        }

        @Override
        public void executeCommandChangeSwitchValue(String message, NetMessage
                originalNetMessage, LogOperator op, int operatorID) {

        }


    }

    /**
     * isIOModuleNode
     * 0 = false
     * 1 = true
     */
    public static int AddNewNode(Database.Node.Struct newNode, Integer isIOModuleNode) {
        Database.Switch.Struct sw;
        newNode.regDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

        switch (newNode.nodeTypeID) {
            case Node_Type.SIMPLE_SWITCH_1: // Simple 1 switche Node
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1101);
                newNode.status = 1;
                newNode.isVisible = true;
                newNode.isIoModuleNode = isIOModuleNode;
                newNode.iD = (int) Database.Node.insert(newNode);
                sw = new Database.Switch.Struct();
                sw.code = "0";
                sw.nodeID = newNode.iD;
                sw.switchType = Switch_Type.SIMPLE_SWITCH;
                sw.name = newNode.name;
                sw.isIOModuleSwitch = isIOModuleNode;
                Database.Switch.insert(sw);
                nodeCommunication.allNodes.put(newNode.iD, new SimpleSwitch(newNode));
                break;
            case Node_Type.SIMPLE_SWITCH_2: // Simple 2 switche Node
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1102);
                newNode.status = 1;
                newNode.isVisible = true;
                newNode.isIoModuleNode = isIOModuleNode;
                newNode.iD = (int) Database.Node.insert(newNode);
                for (int i = 0; i < 2; i++) {
                    sw = new Database.Switch.Struct();
                    sw.code = "" + i;
                    sw.nodeID = newNode.iD;
                    sw.switchType = Switch_Type.SIMPLE_SWITCH;
                    sw.name = G.T.getSentence(1201) + " " + (i + 1);
                    sw.isIOModuleSwitch = isIOModuleNode;

                    Database.Switch.insert(sw);
                    G.log("Added Switch: " + i + " for node: " + newNode.iD);
                }
                nodeCommunication.allNodes.put(newNode.iD, new SimpleSwitch(newNode));
                break;
            case Node_Type.SIMPLE_SWITCH_3: // Simple 3 switche Node
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1103);
                newNode.status = 1;
                newNode.isVisible = true;
                newNode.isIoModuleNode = isIOModuleNode;
                newNode.iD = (int) Database.Node.insert(newNode);
                for (int i = 0; i < 3; i++) {
                    sw = new Database.Switch.Struct();
                    sw.code = "" + i;
                    sw.nodeID = newNode.iD;
                    sw.switchType = Switch_Type.SIMPLE_SWITCH;
                    sw.name = G.T.getSentence(1201) + " " + (i + 1);
                    sw.isIOModuleSwitch = isIOModuleNode;

                    Database.Switch.insert(sw);
                }
                nodeCommunication.allNodes.put(newNode.iD, new SimpleSwitch(newNode));
                break;
            case Node_Type.SIMPLE_DIMMER_1:
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1104);
                newNode.status = 1;
                newNode.isVisible = true;
                newNode.iD = (int) Database.Node.insert(newNode);
                sw = new Database.Switch.Struct();
                sw.code = "0";
                sw.nodeID = newNode.iD;
                sw.switchType = Switch_Type.SIMPLE_DIMMER;
                sw.name = G.T.getSentence(1202);
                sw.isIOModuleSwitch = isIOModuleNode;

                Database.Switch.insert(sw);
                nodeCommunication.allNodes.put(newNode.iD, new SimpleDimmer(newNode));
                break;
            case Node_Type.SIMPLE_DIMMER_2:
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1105);
                newNode.status = 1;
                newNode.isVisible = true;
                newNode.iD = (int) Database.Node.insert(newNode);
                for (int i = 0; i < 2; i++) {
                    sw = new Database.Switch.Struct();
                    sw.code = "" + i;
                    sw.nodeID = newNode.iD;
                    sw.switchType = Switch_Type.SIMPLE_DIMMER;
                    sw.name = G.T.getSentence(1202) + " " + (i + 1);
                    sw.isIOModuleSwitch = isIOModuleNode;

                    Database.Switch.insert(sw);
                    G.log("Added Dimmer: " + i + " for node: " + newNode.iD);
                }
                nodeCommunication.allNodes.put(newNode.iD, new SimpleDimmer(newNode));
                break;
            case Node_Type.AIR_CONDITION:
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1106);
                newNode.status = 1;
                newNode.isVisible = true;
                newNode.isIoModuleNode = isIOModuleNode;
                newNode.iD = (int) Database.Node.insert(newNode);
                sw = new Database.Switch.Struct();
                sw.nodeID = newNode.iD;

                if (isIOModuleNode == 1) {
                    sw.code = "0";
                    sw.switchType = Switch_Type.COOLER_PUMP;
                    sw.name = G.T.getSentence(1204);
                    sw.isIOModuleSwitch = isIOModuleNode;
                    Database.Switch.insert(sw);

                    sw.code = "1";
                    sw.switchType = Switch_Type.COOLER_SPEED_Slow;
                    sw.name = G.T.getSentence(1212);
                    sw.isIOModuleSwitch = isIOModuleNode;
                    Database.Switch.insert(sw);

                    sw.code = "2";
                    sw.switchType = Switch_Type.COOLER_SPEED_Fast;
                    sw.name = G.T.getSentence(1213);
                    sw.isIOModuleSwitch = isIOModuleNode;
                    Database.Switch.insert(sw);

                } else {
                    sw.code = "0";
                    sw.switchType = Switch_Type.COOLER_PUMP;
                    sw.name = G.T.getSentence(1204);
                    Database.Switch.insert(sw);
                    sw.code = "1";
                    sw.switchType = Switch_Type.COOLER_SPEED;
                    sw.name = G.T.getSentence(1203);

                    Database.Switch.insert(sw);
                }


                G.log("Added Aircondition for node: " + newNode.iD);
                nodeCommunication.allNodes.put(newNode.iD, new CoolerSwitch(newNode));
                break;
            case Node_Type.CURTAIN_SWITCH:
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1107);
                newNode.status = 1;
                newNode.isVisible = true;
                newNode.isIoModuleNode = isIOModuleNode;
                newNode.iD = (int) Database.Node.insert(newNode);
                sw = new Database.Switch.Struct();
                sw.nodeID = newNode.iD;

                if (isIOModuleNode == 1) {
                    sw.code = "0";
                    sw.switchType = Switch_Type.CURTAIN_STATUS_Open;
                    sw.name = G.T.getSentence(120502);
                    sw.isIOModuleSwitch = isIOModuleNode;
                    Database.Switch.insert(sw);

                    sw.code = "1";
                    sw.switchType = Switch_Type.CURTAIN_STATUS_Close;
                    sw.name = G.T.getSentence(120501);
                    sw.isIOModuleSwitch = isIOModuleNode;
                    Database.Switch.insert(sw);
                } else {
                    sw.code = "0";
                    sw.nodeID = newNode.iD;
                    sw.switchType = Switch_Type.CURTAIN_STATUS;
                    sw.name = G.T.getSentence(1205);
                    sw.isIOModuleSwitch = isIOModuleNode;

                    Database.Switch.insert(sw);
                }

                G.log("Added Curtain for node: " + newNode.iD);
                nodeCommunication.allNodes.put(newNode.iD, new CurtainSwitch(newNode));
                break;
            case Node_Type.WC_SWITCH: // WC Switch Node
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1108);
                newNode.status = 1;
                newNode.isVisible = true;
                newNode.isIoModuleNode = isIOModuleNode;
                newNode.iD = (int) Database.Node.insert(newNode);
                sw = new Database.Switch.Struct();
                sw.nodeID = newNode.iD;

                sw.code = "0";
                sw.switchType = Switch_Type.WC_LAMP;
                sw.name = G.T.getSentence(1206);
                sw.isIOModuleSwitch = isIOModuleNode;

                Database.Switch.insert(sw);
                G.log("Added WC for node: " + newNode.iD);
                nodeCommunication.allNodes.put(newNode.iD, new WCSwitch(newNode));
                break;
            case Node_Type.IOModule: // IOModule
                if (newNode.name.length() == 0)
                    newNode.name = "IO";
                newNode.status = 1;
                newNode.isVisible = false;
                newNode.iD = (int) Database.Node.insert(newNode);
                break;
            case Node_Type.Sensor_SMOKE: // Sensor
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1217);
                newNode.status = 1;
                newNode.isIoModuleNode = isIOModuleNode;
                newNode.isVisible = false;
                newNode.iD = (int) Database.Node.insert(newNode);
                sw = new Database.Switch.Struct();
                sw.nodeID = newNode.iD;

                if (newNode.nodeTypeID == Switch_Type.Sensor_NC) {
                    sw.code = "0";
                    sw.switchType = Switch_Type.Sensor_NC;
                    sw.name = G.T.getSentence(1214);
                    sw.isIOModuleSwitch = isIOModuleNode;
                    sw.value = 0;
                } else if (newNode.nodeTypeID == Switch_Type.Sensor_NO) {
                    sw.code = "0";
                    sw.switchType = Switch_Type.Sensor_NO;
                    sw.name = G.T.getSentence(1215);
                    sw.isIOModuleSwitch = isIOModuleNode;
                    sw.value = 1;
                }

                Database.Switch.insert(sw);
                G.log("Added WC for node: " + newNode.iD);
                nodeCommunication.allNodes.put(newNode.iD, new Sensore(newNode));
                break;

            case Node_Type.Sensor_Magnetic: // Sensor
                if (newNode.name.length() == 0)
                    newNode.name = G.T.getSentence(1218);
                newNode.status = 1;
                newNode.isIoModuleNode = isIOModuleNode;
                newNode.isVisible = false;
                newNode.iD = (int) Database.Node.insert(newNode);
                sw = new Database.Switch.Struct();
                sw.nodeID = newNode.iD;
                if (newNode.nodeTypeID == Switch_Type.Sensor_NC) {
                    sw.code = "0";
                    sw.switchType = Switch_Type.Sensor_NC;
                    sw.name = G.T.getSentence(1214);
                    sw.isIOModuleSwitch = isIOModuleNode;
                    sw.value = 0;
                } else if (newNode.nodeTypeID == Switch_Type.Sensor_NO) {
                    sw.code = "0";
                    sw.switchType = Switch_Type.Sensor_NO;
                    sw.name = G.T.getSentence(1215);
                    sw.isIOModuleSwitch = isIOModuleNode;
                    sw.value = 1;
                }
                Database.Switch.insert(sw);
                G.log("Added WC for node: " + newNode.iD);
                nodeCommunication.allNodes.put(newNode.iD, new Sensore(newNode));
                break;
        }
        SysLog.log("New Device added:" + newNode.name, LogType.DATA_CHANGE, LogOperator.NODE, newNode.iD);

        if (newNode.nodeTypeID != Node_Type.IOModule ||
                newNode.nodeTypeID != Node_Type.Sensor_Magnetic ||
                newNode.nodeTypeID != Node_Type.Sensor_SMOKE) {

            NetMessage netMessage = new NetMessage();
            netMessage.data = newNode.getNodeDataJson();
            netMessage.action = NetMessage.Insert;
            netMessage.type = NetMessage.NodeData;
            netMessage.typeName = NetMessageType.NodeData;
            netMessage.messageID = netMessage.save();
            G.mobileCommunication.sendMessage(netMessage);
            G.server.sendMessage(netMessage);

            //  Send message to server and local Mobiles
            NetMessage netMessage2 = new NetMessage();
            netMessage2.data = newNode.getNodeSwitchesDataJson();
            netMessage2.action = NetMessage.Insert;
            netMessage2.type = NetMessage.SwitchData;
            netMessage2.typeName = NetMessageType.SwitchData;
            netMessage2.messageID = netMessage2.save();
            G.mobileCommunication.sendMessage(netMessage2);
            G.server.sendMessage(netMessage2);
        }


        return newNode.iD;
    }
}
