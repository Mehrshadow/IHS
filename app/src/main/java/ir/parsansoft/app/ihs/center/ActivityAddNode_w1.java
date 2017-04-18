package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ir.parsansoft.app.ihs.center.AllNodes.DiscoveryPacket;
import ir.parsansoft.app.ihs.center.AllViews.CO_f_section_add_node_w1;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;

import static ir.parsansoft.app.ihs.center.G.T;


public class ActivityAddNode_w1 extends ActivityEnhanced {

    CO_f_section_add_node_w1 fw1;
    String freeNodeIP = "";
    int maxAllowedScanTime = 100;
    int commandDelay = 1500;
    long currentTime;
    Utility.MyWiFiManager wm;
    ArrayList<Utility.MyWiFiManager.wifiStation> wifiStations;
    boolean doProcess = false;
    boolean doneConfig = false;
    String MainSSID = "";
    String MainSSIDPassword = "";
    Socket socket = null;
    InputStream inputStream;
    OutputStream outputStream;
    DialogClass waiteDLGWithCancel;
    DialogClass msgDLG;
    Database.Node.Struct newNode;
    private final static String initializationKeyword = "Initialize me!";
    private final static String ConfigurationKeyword = "Got it :)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_section_add_node_w1);

        fw1 = new CO_f_section_add_node_w1(this);
        translateForm();
        WifiManager wm2 = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wm = new Utility.MyWiFiManager();
        wm.setWifiTetheringEnabled(false);
        wm.connectToMainAP();
        setSideBarVisiblity(false);
        fw1.btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startAddNewNode();
            }
        });

        fw1.btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
    }

    private boolean prepairNetworkParameters() {
        if (G.networkSetting == null) {
            G.log("Network setting not found !");
            return false;
        }
        if (Utility.MyWiFiManager.isWifiTetheringEnabled()) {
            G.log("Now in WiFi teathernig ... can not continue ! !");
            return false;
        }
        if (!Utility.isNetworkAvailable()) {
            G.log("Wifi is not connected ... can not continue ! !");
            return false;
        }

        long startIP = Utility.IPAddress.ipToInt(G.networkSetting.nodeIPsStart);
        long endIP = Utility.IPAddress.ipToInt(G.networkSetting.nodeIPsEnd);
        long[] IPs = null;
        Database.Node.Struct nodes[] = Database.Node.select("");
        if (nodes != null) {
            IPs = new long[nodes.length];
            for (int i = 0; i < IPs.length; i++) {
                IPs[i] = Utility.IPAddress.ipToInt(nodes[i].iP);
            }
        }
        boolean foundRepeative, foundFree;
        foundFree = false;
        for (long newFreeIP = startIP; newFreeIP <= endIP; newFreeIP++) {
            foundRepeative = false;
            if (IPs != null) {
                for (int i = 0; i < IPs.length; i++) {
                    if (newFreeIP == IPs[i]) {
                        foundRepeative = true;
                        break;
                    }
                }
            }
            if (foundRepeative == false) {
                foundFree = true;
                freeNodeIP = Utility.IPAddress.intToIP(newFreeIP);
                G.log("Found new free IP address:" + freeNodeIP);
                break;
            }
        }
        if (!foundFree) {
            G.log("No free IP address found in the network !");
            return false;
        }

        MainSSID = G.networkSetting.wiFiSSID;
        MainSSIDPassword = G.networkSetting.wiFiSecurityKey;

        return true;
    }

    public void startAddNewNode() {

        final boolean[] clickCancel = {false}; /** baraye inke dialog haye marbut be adame yaftane device ra dgar nadahad, chon dasti cancel shode ast.*/
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!prepairNetworkParameters()) {
                    waiteDLGWithCancel.dissmiss();
                    msgDLG.showOk(T.getSentence(201), T.getSentence(215));
                    return;
                }
                if (G.mobileCommunication != null) {
                    G.mobileCommunication.stopServer();
                }
                if (G.server != null) {
                    G.server.stop();
                }

                wm.createWifiAccessPoint();
                waiteDLGWithCancel.setDialogTitle(T.getSentence(205));

                currentTime = System.currentTimeMillis();
                doProcess = true;
                while (doProcess) {
                    wifiStations = wm.getWifiStations();
                    if (wifiStations.size() > 0)
                        break;
                    SystemClock.sleep(1000);
                    int seccondsPassed = ((int) ((System.currentTimeMillis() - currentTime) / 1000));
                    waiteDLGWithCancel.setDialogText((maxAllowedScanTime - seccondsPassed) + " s");
                    if (System.currentTimeMillis() - currentTime > maxAllowedScanTime * 1000) {
                        doProcess = false;
                        msgDLG.showOk(T.getSentence(201), T.getSentence(206));
                    }
                } // end while

                boolean hasError = false;
                for (int ws = 0; ws < wifiStations.size(); ws++) {
                    waiteDLGWithCancel.setDialogText(T.getSentence(207));
                    final String ClIP = wifiStations.get(ws).ipAddress;
                    SystemClock.sleep(commandDelay);
                    try {
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(ClIP, G.DEFAULT_NODE_LEARN_PORT), 6000);
                        G.log("Socket to node connected.");
                        outputStream = new DataOutputStream(socket.getOutputStream());
                        inputStream = socket.getInputStream();
                    } catch (IOException e1) {
                        G.log("Error: Connection is not stable");
                        G.printStackTrace(e1);
                        hasError = true;
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                        }
                        wifiStations = wm.getWifiStations();
                        continue;
                    }

                    SystemClock.sleep(commandDelay);
                    waiteDLGWithCancel.setDialogTitle(T.getSentence(213));
                    waiteDLGWithCancel.setDialogText(T.getSentence(211));
                    G.log("Sending Discovery");
                    String cmdDiscovery = "BHZDY*"; // Discovery command
                    String result;

                    G.log("Send: " + cmdDiscovery);
                    result = executeCommand(ClIP, cmdDiscovery);

                    G.log("received result: " + result);
                    try {
                        if (result != null && result.length() > 0) {

                            String[] hardwareData_initialization = result.split("#");
                            String hardwareData = hardwareData_initialization[0];
                            String initialization = hardwareData_initialization[1];

                            if (!initialization.equals(initializationKeyword)) {
                                throw new Exception("could not recive data from socket !");
//                                hasError = true;
//                                break;
                            }

                            G.log("hardwareData: " + hardwareData + " , initialization: " + initialization);

                            newNode = new Database.Node.Struct();
                            newNode.iP = freeNodeIP;
                            newNode.mac = wifiStations.get(ws).macAddress;
                            DiscoveryPacket discoveryPacket = new DiscoveryPacket(hardwareData);
                            newNode.nodeTypeID = discoveryPacket.type;
                            newNode.osVer = discoveryPacket.osVer;
                            newNode.serialNumber = discoveryPacket.serial;
                            newNode.roomID = Database.Room.getMax("ID", "").iD;
                            G.log("ActivityAddNode_w1", "Going to configure new Device : IP:" + freeNodeIP + " - Mac:" + wifiStations.get(ws).macAddress + " - Serial: " + newNode.serialNumber);

                            Date dte = new Date(System.currentTimeMillis());
                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                            newNode.regDate = format.format(dte);

                            waiteDLGWithCancel.setDialogText(T.getSentence(208));
                            SystemClock.sleep(3000);
                            String[] p = new String[1];
                            p[0] = "*" + MainSSID + "*WPA2PSK,AES," + MainSSIDPassword + "*STATIC," + freeNodeIP + "," + G.networkSetting.mainSubnetMask + "," + G.networkSetting.mainDefaultGateway + "*TCP,SERVER," + G.DEFAULT_NODE_GET_PORT + "," + G.networkSetting.mainIPAddress + "*END";
                            try {
                                for (int k = 0; k < p.length; k++) {
                                    String data;
                                    G.log("Send:\n--------------\n" + p[k] + "\n-----------------");

                                    data = executeCommand(ClIP, p[k]);


                                    G.log("Received:\n---------------\n" + data + "\n--------------------");
                                    if (data == null || !data.equals(ConfigurationKeyword))
                                        throw new Exception("could not recive data from socket !");
                                    SystemClock.sleep(commandDelay);
                                }

                                doneConfig = true;

                            } catch (Exception e) {
                                waiteDLGWithCancel.setDialogTitle(T.getSentence(216));
                                waiteDLGWithCancel.setDialogText(T.getSentence(209));
                                G.printStackTrace(e);
                                continue;
                            }

                            final Database.Node.Struct duplicated[] = Database.Node.select("SerialNumber LIKE'" + newNode.serialNumber + "'");
                            //G.nodeCommunication.allNodes.put(newNode.iD, new AllNodes.SimpleSwitch(newNode));
                            waiteDLGWithCancel.setDialogText(T.getSentence(210));
                            wm.setWifiTetheringEnabled(false);
                            wm.connectToMainAP();
                            if (duplicated == null) {
                                /**************************** Jahanbin ***************************/
                                SystemClock.sleep(10000);
                                G.log("Connected to main wifi");
                                G.server.connectToServer();
                                G.mobileCommunication.startServer();

                                int newNodeID = AllNodes.AddNewNode(newNode, 0);
                                /**************************** Jahanbin ***************************/

                                G.log("New Node Added with ID :" + newNodeID);

                                if (newNode.nodeTypeID == AllNodes.Node_Type.IOModule)//IO Module
                                {
                                    newNode.roomID = AllNodes.myHouseDefaultRoomId;
                                    Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_IoMadule_Input_Output.class);
                                    fw2.putExtra("NODE_ID", newNodeID);
                                    G.currentActivity.startActivity(fw2);
                                    Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                                    finish();
                                } else {

                                    Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_w2.class);
                                    fw2.putExtra("NODE_ID", newNodeID);
                                    G.currentActivity.startActivity(fw2);
                                    Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                                    finish();
                                }
                            } else {

                                DialogClass dlg = new DialogClass(G.currentActivity);
                                dlg.setOnOkListener(new DialogClass.OkListener() {
                                    @Override
                                    public void okClick() {
                                        replaceNewNodeOverOldNode(duplicated[0]);
                                    }
                                });

                                dlg.showOk(T.getSentence(151), T.getSentence(247));

                            }
                            if (!socket.isClosed())
                                try {
                                    socket.close();
                                } catch (IOException e1) {
                                    G.printStackTrace(e1);
                                }
                            socket = null;
                            hasError = false;
                            break;
                        } else {
                            hasError = true;
                            wifiStations = wm.getWifiStations();
                            continue;
                        }
                    } catch (Exception e) {
                        G.printStackTrace(e);
                    }
                }

                waiteDLGWithCancel.setDialogText(T.getSentence(210));
                wm.setWifiTetheringEnabled(false);
                wm.connectToMainAP();

                // wait 1- second & then start connection to server & mobiles
                // cannot use handler because a dialogue is showing & it crashes if we use handler
                // so we must use the system time to have a 10 sec wait
                currentTime = System.currentTimeMillis();
                doProcess = true;
                while (doProcess) {
                    int seccondsPassed = ((int) ((System.currentTimeMillis() - currentTime) / 1000));
                    if (System.currentTimeMillis() - currentTime > 10 * 1000) {
                        doProcess = false;
                        G.server.connectToServer();
                        G.mobileCommunication.startServer();
                        G.log("************connecting");
                    }
                } // end while

                if (hasError && !clickCancel[0]) {
                    G.log("Could not fine configured device !");
                    msgDLG.showOk(T.getSentence(216), T.getSentence(217));
                } else if (!doneConfig && wifiStations.size() > 0 & !clickCancel[0]) {
                    G.log("Could not configure device !");
                    msgDLG.showOk(T.getSentence(216), T.getSentence(209));
                }

                msgDLG.dissmiss();
                waiteDLGWithCancel.dissmiss();

            }

            /****************** Jahanbin **************************/
            private void replaceNewNodeOverOldNode(Database.Node.Struct oldNode) {
                // Replace new Node over old Node
                newNode.roomID = oldNode.roomID;
                newNode.icon = oldNode.icon;
                newNode.isFavorite = oldNode.isFavorite;
                // ----------------- Delete Old node ---------------
                //  Send message to server and local Mobiles
                if (oldNode.isIoModuleNode != 1) {
                    NetMessage netMessage = new NetMessage();
                    netMessage.data = oldNode.getNodeDataJson();
                    netMessage.action = NetMessage.Delete;
                    netMessage.type = NetMessage.NodeData;
                    netMessage.typeName = NetMessage.NetMessageType.NodeData;
                    netMessage.messageID = netMessage.save();
                    G.mobileCommunication.sendMessage(netMessage);
                    G.server.sendMessage(netMessage);
                    NetMessage netMessage2 = new NetMessage();
                    netMessage2.data = oldNode.getNodeSwitchesDataJson();
                    netMessage2.action = NetMessage.Delete;
                    netMessage2.type = NetMessage.SwitchData;
                    netMessage2.typeName = NetMessage.NetMessageType.SwitchData;
                    netMessage2.messageID = netMessage2.save();
                    G.mobileCommunication.sendMessage(netMessage2);
                    G.server.sendMessage(netMessage2);
                    SysLog.log("Device :" + oldNode.name + " Deleted.", LogType.DATA_CHANGE, LogOperator.NODE, oldNode.iD);

                }
                try {
                    G.nodeCommunication.allNodes.get(oldNode.iD).distroyNode();
                    G.nodeCommunication.allNodes.remove(oldNode.iD);
                    Database.Node.delete(oldNode.iD);
                    Database.Switch.delete("NodeID=" + oldNode.iD);
                } catch (Exception e) {
                    G.log(e.getMessage());
                }
                //--------------------------Create new node ------------------------
                int newNodeID = AllNodes.AddNewNode(newNode, 0);
                newNode.iD = newNodeID;
                G.log("Connected to main wifi");

                G.server.connectToServer();
                G.mobileCommunication.startServer();

                G.log("New Node Added with ID :" + newNodeID);
                waiteDLGWithCancel.showWaite("", T.getSentence(213));
                G.HANDLER.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waiteDLGWithCancel.dissmiss();

                        if (newNode.nodeTypeID == AllNodes.Node_Type.IOModule) {
                            Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_IoMadule_Input_Output.class);
                            fw2.putExtra("NODE_ID", newNode.iD);
                            G.currentActivity.startActivity(fw2);
                        } else {

                            Intent fw2 = new Intent(G.currentActivity, ActivityAddNode_w2.class);
                            fw2.putExtra("NODE_ID", newNode.iD);
                            G.currentActivity.startActivity(fw2);
                        }
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                        finish();
                    }
                }, 10000);
            }

            /****************** Jahanbin **************************/

            private String executeCommand(String ClIP, String command) {

                for (int t = 0; t < 3; t++) { // retrys for 3 times if fail
                    try {
                        G.log("Try :" + t);
                        outputStream.write(command.getBytes());
                        G.HANDLER.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    G.log("No answer has recived. Socket is closing.");
                                    if (socket != null && !socket.isClosed())
                                        socket.close();
                                } catch (Exception e) {
                                    G.printStackTrace(e);
                                }
                            }
                        }, 6 * 1000);

                        byte[] b = new byte[1];
                        String data = "";
                        String initialization = "";

                        int bytesToRead;
                        while ((bytesToRead = socket.getInputStream().read(b)) > 0) {
                            String newChar = new String(b);
                            G.log(b[0] + " " + newChar);
                            //     G.log("Read one byte:" + newChar);
                            if (b[0] == '\r' || b[0] == '\n' || newChar == null) {
                                G.log("End of stream ..");
                                break;
                            }
                            data += newChar;
                        }

                        if (!data.equals(ConfigurationKeyword)) {

                            while ((bytesToRead = socket.getInputStream().read(b)) > 0) {
                                String newChar = new String(b);
                                G.log(b[0] + " " + newChar);
                                //     G.log("Read one byte:" + newChar);
                                if (b[0] == '\r' || b[0] == '\n' || newChar == null) {
                                    G.log("End of stream ..");
                                    break;
                                }
                                initialization += newChar;
                            }
                        }

                        return data + initialization;
                    } catch (IOException e) {
                        G.printStackTrace(e);
                        G.log("Error executeCommand :" + e.getMessage());
                        if (!socket.isClosed())
                            try {
                                socket.close();
                            } catch (IOException e1) {
                                G.printStackTrace(e1);
                            }
                    }
                }
                return null;
            }
        });

        waiteDLGWithCancel = new DialogClass(G.currentActivity);
        msgDLG = new DialogClass(G.currentActivity);
        waiteDLGWithCancel.showWaitWithCancelButton(T.getSentence(203), T.getSentence(204));
        waiteDLGWithCancel.setCancelListener(new DialogClass.CancelListener() {
            @Override
            public void cancelClick() {
                G.log("cancel button press");
                waiteDLGWithCancel.setDialogText(T.getSentence(210));
                wm.setWifiTetheringEnabled(false);
                wm.connectToMainAP();

                // run after 10 seconds of connection
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        G.server.connectToServer();
                        G.mobileCommunication.startServer();
                    }
                }, 10 * 1000);
                G.log("Connected to main wifi");

//                if (finalHasError) {
//                    G.log("Could not fine configured device !");
//                    msgDLG.showOk(G.T.getSentence(216), G.T.getSentence(217));
//                } else if (!doneConfig && wifiStations.size() > 0) {
//                    G.log("Could not configure device !");
//                    msgDLG.showOk(G.T.getSentence(216), G.T.getSentence(209));
//                }
                clickCancel[0] = true;
                if (t.isAlive()) {
                    t.interrupt();
                }
                waiteDLGWithCancel.dissmiss();
            }
        });

        t.start();
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fw1.txtTitle.setText(T.getSentence(201));
        fw1.txtInstruction.setText(T.getSentence(202));
        fw1.txtStatus.setText(T.getSentence(225));
        fw1.btnStart.setText(T.getSentence(122));
        fw1.btnCancel.setText(T.getSentence(102));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }
}
