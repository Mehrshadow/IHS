package ir.parsansoft.app.ihs.center;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;

public class SampleNode extends ViewGroup {

    protected Socket socket;
    protected Database.Node.Struct myNode;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;
    protected long lastNodeResponseTime = 0;   //last communication to this node as System.Milliseconds
    boolean nodeExixt = true;

    public SampleNode(Context context) {
        super(context);
        lastNodeResponseTime = System.currentTimeMillis();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public static enum NodeMsgType {
        MSG_DISCOVERY,
        MSG_CONFIG,
        MSG_DO_OPERATION,
        MSG_OTHER
    }

    public static enum NodeMsgStatus {
        MSG_STATE_START,
        MSG_STATE_SENDING,
        MSG_STATE_WAITING,
        MSG_STATE_QUEUE,
        MSG_STATE_DONE,
        MSG_STATE_FAILED,
        MSG_STATE_ERROR,
        MSG_STATE_BAD_ANSWER,
        MSG_STATE_FAILED_BUSY
    }

    public static final class NodeMsg {
        NodeMsgType msgType = NodeMsgType.MSG_DO_OPERATION;
        Database.Node.Struct node;
        String sentData;
        String result;
        NodeMsgStatus status;
    }

    public int getNodeID() {
        return myNode.iD;
    }

//    public int getIfNodeIsIOModuleNode() {
//        return myNode.isIoModuleNode;
//    }

    public int addUI(AllViews.CO_l_node_simple_key simple_key) {
        return 0;
    }

    public int addUI(View view) {
        return 0;
    }

    public void refreshNodeStruct() {
        myNode = Database.Node.select(myNode.iD);
    }

    public void setSettingVisiblity(int uiIndex, boolean isVisible) {

    }

    public void setProgressVisiblity(boolean isVisible) {

    }

    public Database.Node.Struct getNodeStruct() {
        return myNode;
    }

    public void executeCommandChangeSwitchValue(String message, NetMessage originalNetMessage, LogOperator op, int operatorID) {

    }

    public void processResult(String result) {

    }

    protected void onSocketConnect() {
        setProgressVisiblity(false);
        if (myNode.status == 0) {
            myNode.status = 1;
            Database.Node.edit(myNode);
            G.bp.refreshAlarmUI();
            SysLog.log("Node " + myNode.name + " connected.", LogType.NODE_STATUS, LogOperator.NODE, myNode.iD);
            //  Send message to server and local Mobiles
            NetMessage netMessage = new NetMessage();
            netMessage.data = myNode.getNodeDataJson();
            netMessage.action = NetMessage.Update;
            netMessage.type = NetMessage.NodeData;
            netMessage.typeName = NetMessageType.NodeData;
            netMessage.messageID = netMessage.save();
            if (G.mobileCommunication != null)
                G.mobileCommunication.sendMessage(netMessage);
            if (G.server != null)
                G.server.sendMessage(netMessage);


        }
        sendMessageToNode("GSFD*"); // Get Status From Device
        G.log("Sending GSFD");
    }

    protected void onSocketDisconnect() {
        setProgressVisiblity(true);
        if (myNode.status == 1) {
            myNode.status = 0;
            Database.Node.edit(myNode);
            G.bp.refreshAlarmUI();
            SysLog.log("Node " + myNode.name + " disconnected.", LogType.NODE_STATUS, LogOperator.NODE, myNode.iD);
            //  Send message to server and local Mobiles
            NetMessage netMessage = new NetMessage();
            netMessage.data = myNode.getNodeDataJson();
            netMessage.action = NetMessage.Update;
            netMessage.type = NetMessage.NodeData;
            netMessage.typeName = NetMessageType.NodeData;
            netMessage.messageID = netMessage.save();
            if (G.mobileCommunication != null)
                G.mobileCommunication.sendMessage(netMessage);
            if (G.server != null)
                G.server.sendMessage(netMessage);
        }
    }

    public void resetUis() {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public boolean isNodeOnline() {
        if (socket != null)
            return socket.isConnected();
        else
            return false;
    }

    //  last communication to this node as System.Milliseconds
    public long getLastCommTime() {
        return lastNodeResponseTime;
    }

    //  last communication to this node as System.Milliseconds
    public void setLastCommTime(long lastCommTime) {
        this.lastNodeResponseTime = lastCommTime;
    }

    protected void connectToNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (nodeExixt) {
                    try {
                        if (Utility.isNetworkAvailable()) {
                            G.log("Try to open socket : Node : " + myNode.iD);

                            ///اگر node متصل به io بود ، سوکت io مربوط به همان node را پیدا میکنیم
                            if (myNode.parentNodeId != 0) {
                                socket = G.findSocketOfIoModuleNode(myNode);
                            } else {
                                socket = new Socket();
                                socket.connect(new InetSocketAddress(myNode.iP, G.DEFAULT_NODE_SEND_PORT), G.DEFAULT_NODE_SOCKET_TIMEOUT);


                                ///اگر نود از نوع io بود سوکت را به لیست سوکت های io اضاف میکنیم
                                if (myNode.nodeTypeID == AllNodes.Node_Type.IOModule) {
                                    G.IOModuleSocket.add(socket);
                                }
                            }
                            outputStream = new DataOutputStream(socket.getOutputStream());
                            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            onSocketConnect();
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
//                            if (myNode.isIoModuleNode != 1)
                            listen();
                            socket = null;
                        }

                    } catch (IOException e) {
                        G.printStackTrace(e);

                    }
                    try {
                        onSocketDisconnect();
                        Thread.sleep(G.DEFAULT_NODE_SOCKET_TIMEOUT);
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public void sendMessageToNode(String command) {
        boolean doProcess;


        if (myNode.parentNodeId != 0) {
            socket = G.findSocketOfIoModuleNode(myNode);

            try {

                if (socket != null)
                    outputStream = new DataOutputStream(socket.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (socket != null && socket.isConnected()) {
            try {
                G.log("Going to write to node " + myNode.iD + "socket: " + command);
                checkResponse();
                //                String password = "!!Abra-Cadabra!!";
                //                String encripterCmd = "";
                //                AESCrypt aescrypt = new AESCrypt(password);
                //                encripterCmd = aescrypt.encrypt(command);
                //encripterCmd = aescrypt.encrypt(command);
                //                G.log("Encripted command:" + encripterCmd);
                outputStream.write(command.getBytes("UTF-8"));
            } catch (Exception e) {
                G.printStackTrace(e);
                G.log("Error executeCommand :" + e.getMessage());
                if (socket != null && socket.isConnected())
                    try {
                        socket.close();
                    } catch (Exception e1) {
                        G.printStackTrace(e1);
                    }
            }
//            try {
//                socket.close();
//                socket = null;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void listen() {
        while (true) {
            try {
                if (socket.isClosed()) {
                    return;
                }
                String encriptedData = "";
                byte[] b = new byte[1];
//                byte[] B = new byte[64];
                byte[] B = new byte[105];
                int i = 0;
                int bytesToRead;
                while ((bytesToRead = socket.getInputStream().read(b)) > 0) {
//                    G.log("" + b[0]);
                    String str = new String(b, "UTF-8");
                    G.log(str);
                    if (b[0] == 13) {
                        G.log("End of stream ..");
                        break;
                    }
                    B[i] = b[0];
                    encriptedData += new String(b);
                    i++;
                }
                if (encriptedData.length() > 0) {
                    lastNodeResponseTime = System.currentTimeMillis();
                    G.log("Node " + myNode.iD + " Receied Data :" + encriptedData);
                    try {
                        //                        String data = "";
                        //                        String password = "0123456789abcdef";
                        //                        
                        //                        CipherSuite cs =new CipherSuite("A", true, 16, , hash, code)
                        //                        
                        //                        
                        //                        
                        //                        AESCrypt aescrypt = new AESCrypt(password);
                        //                        data = aescrypt.decrypt(B);
                        //                        G.toast(data);
                        //                        G.log(data);
                        //                        G.toast(encriptedData);
                        //                        G.log(encriptedData);
                        processResult(encriptedData);
                        myNode.status = 1;
                        setProgressVisiblity(false);
                    } catch (Exception e) {
                        G.printStackTrace(e);
                    }
                } else {
                    G.log("data from Node " + myNode.iD + " is null");
                    break;
                }
            } catch (Exception e) {
                G.log(e.getMessage());
                G.printStackTrace(e);
                return;
            }
        }
        try {
            G.log("Socket Closed");
            socket.close();
        } catch (Exception e) {
            G.printStackTrace(e);
        }
    }

    protected void failedMessage(NetMessage originalNetMessage) {
        //TODO : reply  message to sender
        if (originalNetMessage != null) {
            originalNetMessage.action = NetMessage.Failed;
            originalNetMessage.failedMessage = G.T.getSentence(220);
            G.mobileCommunication.sendMessage(originalNetMessage);
            G.server.sendMessage(originalNetMessage);
        } else {
            G.toast(G.T.getSentence(220));
        }
    }


    private boolean threadIsRunning = false;

    private void checkResponse() {
        if (!threadIsRunning) {
            G.log("Creating a new controller thread to check if socket is connected or not !");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadIsRunning = true;
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() < startTime + G.DEFAULT_NODE_SOCKET_TIMEOUT) {
                        try {
                            G.log("lastNodeResponseTime : " + (lastNodeResponseTime % 100000) / 1000.0);
                            G.log("startTime : " + (startTime % 100000) / 1000.0);
                            G.log("System.currentTimeMillis() : " + (System.currentTimeMillis() % 100000) / 1000.0);
                            if (lastNodeResponseTime > startTime)
                                break;
                            Thread.sleep(500);
                        } catch (Exception e) {
                            G.printStackTrace(e);
                        }
                    }
                    threadIsRunning = false;
                    if (lastNodeResponseTime < startTime) {
                        try {
                            G.log("No packet has recived in a valid time :" + G.DEFAULT_NODE_SOCKET_TIMEOUT + " ms. This is going to disconnect the socket.");
                            socket.close();
                        } catch (Exception e) {
                            G.printStackTrace(e);
                        }
                    }
                }
            }).start();
        }
    }

    public void refreshStatus() {
        try {
            sendMessageToNode("GSFD*"); // Get Status From Device
            G.log("Sending GSFD");
        } catch (Exception e) {
        }
    }

    public void distroyNode() {
        nodeExixt = false;
        try {
            socket.close();
        } catch (Exception e) {
            G.printStackTrace(e);
        }
        resetUis();
        myNode = null;
    }
}
