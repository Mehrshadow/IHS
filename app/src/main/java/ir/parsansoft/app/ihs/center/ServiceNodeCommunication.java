package ir.parsansoft.app.ihs.center;

import android.util.SparseArray;


public class ServiceNodeCommunication {
    public SparseArray<SampleNode> allNodes;
    public SparseArray<NodeEventSocket> nodeEventSockets;
    private boolean loop;
    static int incomeSocetCount = 0;
    Thread serverThread;

    public void executeCommandChangeSwitchValue(int nodeID, String msg, NetMessage originalNetMessage, SysLog.LogOperator op, int operatorID) {
        //SampleNode myNode;
        allNodes.get(nodeID).executeCommandChangeSwitchValue(msg, originalNetMessage, op, operatorID);
    }

    public ServiceNodeCommunication() {
        allNodes = new SparseArray<SampleNode>();
        Database.Node.Struct[] nodes = Database.Node.select("");
        if (nodes != null)
            for (int i = 0; i < nodes.length; i++) {
                switch (nodes[i].nodeTypeID) {
                    case AllNodes.Node_Type.SIMPLE_SWITCH_1:
                    case AllNodes.Node_Type.SIMPLE_SWITCH_2:
                    case AllNodes.Node_Type.SIMPLE_SWITCH_3:
                        allNodes.put(nodes[i].iD, new AllNodes.SimpleSwitch(nodes[i]));
                        break;
                    case AllNodes.Node_Type.SIMPLE_DIMMER_1:
                    case AllNodes.Node_Type.SIMPLE_DIMMER_2:
                        allNodes.put(nodes[i].iD, new AllNodes.SimpleDimmer(nodes[i]));
                        break;
                    case AllNodes.Node_Type.AIR_CONDITION:
                        allNodes.put(nodes[i].iD, new AllNodes.CoolerSwitch(nodes[i]));
                        break;
                    case AllNodes.Node_Type.CURTAIN_SWITCH:
                        allNodes.put(nodes[i].iD, new AllNodes.CurtainSwitch(nodes[i]));
                        break;
                    case AllNodes.Node_Type.WC_SWITCH:
                        allNodes.put(nodes[i].iD, new AllNodes.WCSwitch(nodes[i]));
                        break;
                    case AllNodes.Node_Type.IOModule:
                        allNodes.put(nodes[i].iD, new AllNodes.IOModule(nodes[i]));
                        break;
                    case AllNodes.Node_Type.Sensor_Magnetic:
                    case AllNodes.Node_Type.Sensor_SMOKE:
                        allNodes.put(nodes[i].iD, new AllNodes.Sensore(nodes[i]));
                        break;
                    default:
                        break;
                }
            }

        //nodeEventSockets = new SparseArray<NodeEventSocket>();
        //        startNodeServer();
    }

    public void removeAllUis() {
        for (int i = 0; i < allNodes.size(); i++) {
            allNodes.get(allNodes.keyAt(i)).resetUis();
        }
        AllNodes.uiCount = 0;
    }

    public void checkAllNodes() {
        for (int i = 0; i < allNodes.size(); i++) {
            allNodes.valueAt(i).refreshStatus();
        }
    }

    //    String sendMessageToNode(Database.Node.Struct node, String command) {
    //        //        String cmd = "GET /" + command + " HTTP/1.1\r\nHost: " + node.iP + ":" + G.DEFAULT_NODE_SEND_PORT + "\r\n\r\n";
    //        String result;
    //        G.log("executing command on : " + node.iP + "\r\n" + command);
    //        result = executeCommand(node.iP, command);
    //        G.log("recieved result:" + result);
    //        if (result != null && result.length() > 0) {
    //            if (result.trim().length() > 1)
    //                return result.trim();
    //        }
    //        else
    //            return null;
    //        return "";
    //    }

    //    public static String executeCommand(String ClIP, String command) {
    //        final Socket socket = new Socket();
    //        InputStream inputStream;
    //
    //        OutputStream outputStream;
    //        boolean doProcess;
    //        for (int t = 0; t < 3; t++) { // retrys for 3 times if fail
    //            try {
    //                G.log("Try to open socket" + t);
    //                socket.connect(new InetSocketAddress(ClIP, G.DEFAULT_NODE_SEND_PORT), G.DEFAULT_NODE_SOCKET_TIMEOUT);
    //
    //                try {
    //                    outputStream = new DataOutputStream(socket.getOutputStream());
    //                    inputStream = socket.getInputStream();
    //                }
    //                catch (IOException e1) {
    //                    G.log("Error: Connection is not stable");
    //                    doProcess = false;
    //                    return "";
    //                }
    //                outputStream.write(command.getBytes());
    //                byte[] b = new byte[1];
    //                String data = "";
    //                int bytesToRead;
    //                G.HANDLER.postDelayed(new Runnable() {
    //
    //                    @Override
    //                    public void run() {
    //                        try {
    //                            if ( !socket.isClosed())
    //                                socket.close();
    //                        }
    //                        catch (IOException e) {
    //                            G.printStackTrace(e);
    //                        }
    //                    }
    //                }, 5000);
    //                while ((bytesToRead = socket.getInputStream().read(b)) > 0) {
    //                    data += new String(b);
    //                }
    //                socket.close();
    //
    //                return data;
    //            }
    //            catch (IOException e) {
    //                G.printStackTrace(e);
    //                G.log("Error executeCommand :" + e.getMessage());
    //                if ( !socket.isClosed())
    //                    try {
    //                        socket.close();
    //                    }
    //                    catch (IOException e1) {
    //                        G.printStackTrace(e);
    //                    }
    //            }
    //        }
    //        return null;
    //    }

    //    public static String extractHttpPachetResult(String httpPacket) {
    //        if (httpPacket != null && httpPacket.length() > 7) {
    //            int start = httpPacket.indexOf("<P>") + 3;
    //            int end = httpPacket.indexOf("</P>");
    //            G.log(httpPacket);
    //            G.log("Start : " + start + "  End :" + end);
    //            if (start < end) {
    //                return (httpPacket.substring(start, end));
    //            }
    //        }
    //        return "";
    //    }


    //    public void startNodeServer() {
    //        serverThread = new Thread(new Runnable() {
    //
    //            @Override
    //            public void run() {
    //                Socket socket = null;
    //                loop = true;
    //                try {
    //                    G.log("Starting Server");
    //                    ServerSocket serverSocket = new ServerSocket(G.DEFAULT_NODE_GET_PORT);
    //                    while (loop) {
    //                        try {
    //                            socket = serverSocket.accept();
    //                            String ipAddress = socket.getRemoteSocketAddress().toString();
    //                            ipAddress = ipAddress.substring(1, ipAddress.indexOf(':'));
    //                            Database.Node.Struct node;
    //                            try {
    //                                node = Database.Node.select("IP LIKE '" + ipAddress + "'")[0];
    //                            }
    //                            catch (Exception e) {
    //                                continue;
    //                            }
    //                            G.log("IP address:" + ipAddress);
    //                            //  outputStream = new DataOutputStream(socket.getOutputStream());
    //                            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    //                            String data = "";
    //                            data = inputStream.readLine();
    //                            allNodes.get(node.iD).setLastCommTime(System.currentTimeMillis());
    //                            if (data == null)
    //                                G.log("data from Node is null");
    //                            else if (data.length() == 0)
    //                                G.log("data from Node is empty");
    //                            else {
    //                                G.log("data from Node " + node.iD + " :\n" + data);
    //                                //                                data = data.substring(data.indexOf('/'));
    //                                //                                data = data.substring(1, data.indexOf(' '));
    //                                G.toast(data);
    //                                NodeMsg msg = new NodeMsg();
    //                                msg.msgType = NodeMsgType.MSG_DO_OPERATION;
    //                                msg.node = node;
    //                                msg.sentData = "";
    //                                msg.result = data;
    //                                msg.status = NodeMsgStatus.MSG_STATE_DONE;
    //                                allNodes.get(node.iD).processResult(msg, LogOperator.NODE, node.iD);
    //                            }
    //                        }
    //                        catch (Exception e) {
    //                            G.log("Exception 1");
    //                            G.printStackTrace(e);
    //                        }
    //                        finally {
    //                            if (socket != null && !socket.isClosed()) {
    //                                try {
    //                                    socket.close();
    //                                }
    //                                catch (Exception e2) {
    //                                    G.log("Exception 2");
    //                                    G.printStackTrace(e2);
    //                                }
    //                            }
    //                            socket = null;
    //                        }
    //                    }
    //                }
    //                catch (IOException e) {
    //                    G.log("Exception 3");
    //                    G.log("Server Closed");
    //                    G.printStackTrace(e);
    //                }
    //            }
    //        });
    //        serverThread.start();
    //    }

    //    public void stopServer() {
    //        try {
    //            loop = false;
    //            serverThread.interrupt();
    //        }
    //        catch (Exception e) {
    //            G.printStackTrace(e);
    //        }
    //    }
}
