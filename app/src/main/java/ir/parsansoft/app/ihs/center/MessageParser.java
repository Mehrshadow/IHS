package ir.parsansoft.app.ihs.center;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.parsansoft.app.ihs.center.NetMessage.NetMessageAction;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;

public class MessageParser {
    public static void parseFromMobile(int mobileID, String message) {
        //G.toast("parseFromMobile : Data Recived from mobile :" + mobileID + "\n" + message);
        if (message.length() == 0)
            return;
        String c = message.substring(0, 1);
        if (c.equals("{")) //  Json Object 
        {
            try {
                JSONObject jo = new JSONObject(message);
            } catch (JSONException e) {
                G.printStackTrace(e);
            }
        } else if (c.equals("[")) // Json Array
        {
            try {
                JSONArray ja = new JSONArray(message);
                for (int i = 0; i < ja.length(); i++) {
                    NetMessage newNetMessage = new NetMessage(ja.getJSONObject(i).toString());
                    JSONArray dataJArray = new JSONArray(newNetMessage.data);
                    int operatorID = 0;
                    if (newNetMessage.recieverIDs != null) {
                        operatorID = newNetMessage.recieverIDs[0];
                    }
                    switch (newNetMessage.type) {
                        case NetMessage.SwitchStatus://SwitchStatus
                            switch (newNetMessage.action) {
                                case NetMessage.Update:// Update
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        int swcID = msgJ.getInt("ID");
                                        Database.Switch.Struct mySwicth = Database.Switch.select(swcID);
                                        if (mySwicth != null) {
                                            JSONObject jmsg = new JSONObject();
                                            jmsg.put("SwitchCode", mySwicth.code);
                                            jmsg.put("Value", msgJ.getDouble("Value"));
                                            G.nodeCommunication.executeCommandChangeSwitchValue(mySwicth.nodeID, jmsg.toString(), newNetMessage, LogOperator.MOBILE, mobileID);
                                        }
                                    }
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // end  case SwitchStatus:
                        case NetMessage.ScenarioStatus://ScenarioStatus
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        int sID = msgJ.getInt("ID");
                                        Database.Scenario.Struct myScenario = Database.Scenario.select(sID);
                                        if (myScenario != null) {
                                            int newState = msgJ.getInt("Active");
                                            if (newState == 0) {
                                                if (myScenario.active) {
                                                    myScenario.active = false;
                                                    SysLog.log("Scenario " + myScenario.name + " Disabled by Mobile " + mobileID, LogType.SCENARIO_CHANGE, LogOperator.MOBILE, mobileID);
                                                    Database.Scenario.edit(myScenario);
                                                    //  Send message to server and local Mobiles
                                                    NetMessage netMessage = new NetMessage();
                                                    netMessage.data = myScenario.getScenarioStatusJSON(false);
                                                    netMessage.action = NetMessage.Update;
                                                    netMessage.type = NetMessage.ScenarioStatus;
                                                    netMessage.typeName = NetMessageType.ScenarioStatus;
                                                    netMessage.messageID = netMessage.save();
                                                    G.mobileCommunication.sendMessage(netMessage);
                                                    G.server.sendMessage(netMessage);
                                                }
                                            } else if (newState == 1) {
                                                boolean hasActivePre, hasActiveResult;
                                                hasActivePre = myScenario.opTimeBase || myScenario.opPreGPS || myScenario.opPreSwitchBase || myScenario.opPreTemperature || myScenario.opPreWeather;
                                                hasActiveResult = myScenario.opResultNotify || myScenario.opResultSMS || myScenario.opResultSwitch;
                                                if (hasActivePre && hasActiveResult) {
                                                    myScenario.active = true;
                                                    Database.Scenario.edit(myScenario);
                                                    SysLog.log("Scenario " + myScenario.name + " Enabled by Mobile " + mobileID, LogType.SCENARIO_CHANGE, LogOperator.MOBILE, mobileID);
                                                    //  Send message to server and local Mobiles
                                                    NetMessage netMessage = new NetMessage();
                                                    netMessage.data = myScenario.getScenarioStatusJSON(false);
                                                    netMessage.action = NetMessage.Update;
                                                    netMessage.type = NetMessage.ScenarioStatus;
                                                    netMessage.typeName = NetMessageType.ScenarioStatus;
                                                    netMessage.messageID = netMessage.save();
                                                    G.mobileCommunication.sendMessage(netMessage);
                                                    G.server.sendMessage(netMessage);
                                                } else {
                                                    G.log("MessageParser", "Should has at less one active precondition and one active result");
                                                    //  Send message to server and local Mobiles
                                                    NetMessage netMessage = new NetMessage();
                                                    netMessage.data = myScenario.getScenarioStatusJSON(false);
                                                    netMessage.action = NetMessage.Failed;
                                                    netMessage.type = NetMessage.ScenarioStatus;
                                                    netMessage.typeName = NetMessageType.ScenarioStatus;
                                                    netMessage.recieverIDs = newNetMessage.recieverIDs;
                                                    netMessage.messageID = netMessage.save();
                                                    netMessage.failedMessage = G.T.getSentence(577);
                                                    G.mobileCommunication.sendMessage(netMessage);
                                                    G.server.sendMessage(netMessage);
                                                }
                                            } else if (newState == 2) {
                                                G.scenarioBP.runScenario(myScenario);
                                            }
                                        }
                                    }
                                    G.ui.runOnScenarioChanged(-1);
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // end case ScenarioStatus:
                        case NetMessage.SyncData://SyncData
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        int mobileLastmsgID = msgJ.getInt("LastMessageID");
                                        G.log("MessageParser", "syncing data with mobile : " + mobileID + " - mobileLastmsgID:" + mobileLastmsgID);
                                        String appVerCode = msgJ.getString("AppVerCode");
                                        int mobLang = msgJ.getInt("LanguageID");
                                        Database.Message.Struct[] newMessages = Database.Message.select("ID>" + mobileLastmsgID);
                                        if (newMessages != null) {
                                            long centerLastMessageID = 0;
                                            try {
                                                centerLastMessageID = Database.Message.getMax("ID", "").iD;
                                            } catch (Exception e) {
                                                centerLastMessageID = 0;
                                            }
                                            G.log("MessageParser", "centerLastMessageID: " + centerLastMessageID + "   new messages length: " + newMessages.length);
                                            if (centerLastMessageID < mobileLastmsgID) { // در این حالت احتمالا بک آپ برگردانده شده بوده و باید اطلاعات مجدد از اول رفرش شود.
                                                Database.Mobiles.Struct mobile = Database.Mobiles.select(mobileID);
                                                String result = Database.generateNewMobileConfiguration(mobile);
                                                // Send message to local Mobile
                                                NetMessage netMessage = new NetMessage();
                                                netMessage.data = "[" + result + "]";
                                                netMessage.action = NetMessage.Update;
                                                netMessage.type = NetMessage.RefreshData;
                                                netMessage.typeName = NetMessageType.RefreshData;
                                                netMessage.messageID = 0;
                                                netMessage.recieverIDs = newNetMessage.recieverIDs;
                                                G.mobileCommunication.sendMessage(netMessage, mobileID);
                                            } else if (centerLastMessageID - mobileLastmsgID > G.MAX_MESSAGE_DIFFERENCE_TO_REFRESH) { // در این حالت  به دلیل تعداد زیاد پیام همه آنها فرستاده نمی شود و فقط نوتیفای ها فرستاده می شود سپس کل اطلاعات رفرش می شود.
                                                newMessages = Database.Message.select("ID>" + mobileLastmsgID + " AND MessageType=" + NetMessageType.Notify.ordinal());
                                                NetMessageAction[] netMessageActions = NetMessageAction.values();
                                                NetMessageType[] netMessageTypes = NetMessageType.values();
                                                for (int k = 0; newMessages != null && k < newMessages.length; k++) {
                                                    try {
                                                        if (newMessages[k].recieverIDs.length() > 0 && !newMessages[k].recieverIDs.contains(mobileID + " ;"))
                                                            continue;
                                                        //  Send message to local Mobile
                                                        NetMessage netMessage = new NetMessage();
                                                        netMessage.data = newMessages[k].messageText;
                                                        netMessage.action = netMessageActions[newMessages[k].messageAction].ordinal();
                                                        netMessage.type = netMessageTypes[newMessages[k].messageType].ordinal();
                                                        netMessage.typeName = netMessageTypes[newMessages[k].messageType];
                                                        netMessage.messageID = newMessages[k].iD;
                                                        G.mobileCommunication.sendMessage(netMessage, mobileID);
                                                    } catch (Exception e) {
                                                        G.printStackTrace(e);
                                                        break;
                                                    }
                                                }
                                                // send RefreshData
                                                Database.Mobiles.Struct mobile = Database.Mobiles.select(mobileID);
                                                String result = Database.generateNewMobileConfiguration(mobile);
                                                // Send message to local Mobile
                                                NetMessage netMessage = new NetMessage();
                                                netMessage.data = "[" + result + "]";
                                                netMessage.action = NetMessage.Update;
                                                netMessage.type = NetMessage.RefreshData;
                                                netMessage.typeName = NetMessageType.RefreshData;
                                                netMessage.messageID = 0;
                                                netMessage.recieverIDs = newNetMessage.recieverIDs;
                                                G.mobileCommunication.sendMessage(netMessage, mobileID);
                                            } else { // در این حالت تمام پیام های مورد نیاز ارسال می شود.
                                                NetMessageAction[] netMessageActionValues = NetMessageAction.values();
                                                NetMessageType[] netMessageTypeValues = NetMessageType.values();
                                                for (int k = 0; newMessages != null && k < newMessages.length; k++) {
                                                    try {
                                                        if (newMessages[k].recieverIDs.length() > 0 && !newMessages[k].recieverIDs.contains(mobileID + " ;")) {
                                                            continue;
                                                        }
                                                        //  Send message to local Mobile
                                                        NetMessage netMessage = new NetMessage();
                                                        netMessage.data = newMessages[k].messageText;
                                                        netMessage.action = netMessageActionValues[newMessages[k].messageAction].ordinal();
                                                        netMessage.type = netMessageTypeValues[newMessages[k].messageType].ordinal();
                                                        netMessage.typeName = netMessageTypeValues[newMessages[k].messageType];
                                                        netMessage.messageID = newMessages[k].iD;
                                                        G.mobileCommunication.sendMessage(netMessage, mobileID);
                                                    } catch (Exception e) {
                                                        G.printStackTrace(e);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        // end  case SyncData:
                        case NetMessage.RefreshData://RefreshData
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        Database.Mobiles.Struct mobile = Database.Mobiles.select(mobileID);
                                        String result = Database.generateNewMobileConfiguration(mobile);
                                        // Send message to local Mobile
                                        NetMessage netMessage = new NetMessage();
                                        netMessage.data = "[" + result + "]";
                                        netMessage.action = NetMessage.Update;
                                        netMessage.type = NetMessage.RefreshData;
                                        netMessage.typeName = NetMessageType.RefreshData;
                                        netMessage.messageID = 0;
                                        netMessage.recieverIDs = newNetMessage.recieverIDs;
                                        G.mobileCommunication.sendMessage(netMessage, mobileID);
                                        G.log("MessageParser", "Refresh Data has completed .....................");
                                    }
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // end  case RefreshData:
                        case NetMessage.GPSAnnounce://GPSAnnounce
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        int mID = msgJ.getInt("MobileID");
                                        double lat = msgJ.getDouble("Latitude");
                                        double lon = msgJ.getDouble("Longitude");
                                        Database.Mobiles.Struct m = Database.Mobiles.select(mID);
                                        if (m != null) {
                                            m.LastLatitude = lat;
                                            m.LastLongitude = lon;
                                            Database.Mobiles.edit(m);
                                            G.scenarioBP.runByLocation(mID, lat, lon);
                                        }
                                    }
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // end  case GPSAnnounce:
                        case NetMessage.Setting://Setting
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        G.setting.serverSocketIP = msgJ.getString("ServerIP");
                                        G.setting.serverSocketPort = msgJ.getInt("ServerPort");
                                        G.setting.serverURL = msgJ.getString("ServerURL");
                                        Database.Setting.edit(G.setting);
                                        G.server.stop();
                                        G.server.connectToServer();
                                    }
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // end  case Setting:
                        case NetMessage.KeepAlive://KeepAlive
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    //  Send message to local Mobile
                                    NetMessage netMessage = new NetMessage();
                                    netMessage.data = "";
                                    netMessage.action = NetMessage.Update;
                                    netMessage.type = NetMessage.KeepAlive;
                                    netMessage.typeName = NetMessageType.KeepAlive;
                                    G.mobileCommunication.sendMessage(netMessage, mobileID);
                                    break;

                                default:
                                    break;
                            }
                    }
                    // end  case KeepAlive:
                }

            } catch (JSONException e) {
                G.printStackTrace(e);
            }


        } else {
            G.log("MessageParser", "Data from mobile " + mobileID + " is not recognized !\n" + message);
        }
    }

    public static void parseFromServer(String message) {
        G.log("MessageParser", "Pars Data recived from server.");
        if (message.length() == 0)
            return;
        String c = message.substring(0, 1);
        G.log("MessageParser", "c is :" + c);
        if (c.equals("{")) // Json Object 
        {
            try {
                JSONArray jo = new JSONArray(message);

            } catch (JSONException e) {
                G.printStackTrace(e);
            } catch (Exception e) {
                G.printStackTrace(e);
            }
        } else if (c.equals("[")) //  Json Object
        {
            G.log("MessageParser", "Json Array");
            try {
                JSONArray ja = new JSONArray(message);
                G.log("MessageParser", "json Array length = " + ja.length());
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    G.log("MessageParser", "Getting Json Object" + i);
                    NetMessage newNetMessage = new NetMessage(ja.getJSONObject(i).toString());
                    G.log("MessageParser", "newNetMessage Parsed !");
                    JSONArray dataJArray = new JSONArray(newNetMessage.data);
                    int operatorID = 0;
                    if (newNetMessage.recieverIDs != null) {
                        operatorID = newNetMessage.recieverIDs[0];
                    }
                    G.log("MessageParser", "newNetMessage.type = " + newNetMessage.type);
                    switch (newNetMessage.type) {
                        case NetMessage.SwitchStatus://SwitchStatus
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        int swcID = msgJ.getInt("ID");
                                        Database.Switch.Struct mySwicth = Database.Switch.select(swcID);
                                        if (mySwicth != null) {
                                            JSONObject jmsg = new JSONObject();
                                            jmsg.put("SwitchCode", mySwicth.code);
                                            jmsg.put("Value", msgJ.getDouble("Value"));
                                            G.nodeCommunication.executeCommandChangeSwitchValue(mySwicth.nodeID, jmsg.toString(), newNetMessage, LogOperator.WEB, operatorID);
                                        }
                                    }
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // end  case SwitchStatus:
                        case NetMessage.ScenarioStatus://ScenarioStatus
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        int sID = msgJ.getInt("ID");
                                        Database.Scenario.Struct myScenario = Database.Scenario.select(sID);
                                        if (myScenario != null) {
                                            int newState = msgJ.getInt("Active");
                                            if (newState == 0) {
                                                if (myScenario.active) {
                                                    myScenario.active = false;
                                                    Database.Scenario.edit(myScenario);
                                                    SysLog.log("Scenario " + myScenario.name + " Enabled by server", LogType.SCENARIO_CHANGE, LogOperator.WEB, 0);
                                                    //  Send message to server and local Mobiles
                                                    NetMessage netMessage = new NetMessage();
                                                    netMessage.data = myScenario.getScenarioStatusJSON(false);
                                                    netMessage.action = NetMessage.Update;
                                                    netMessage.type = NetMessage.ScenarioStatus;
                                                    netMessage.typeName = NetMessageType.ScenarioStatus;
                                                    netMessage.messageID = netMessage.save();
                                                    G.mobileCommunication.sendMessage(netMessage);
                                                    G.server.sendMessage(netMessage);
                                                }
                                            } else if (newState == 1) {
                                                boolean hasActivePre, hasActiveResult;
                                                hasActivePre = myScenario.opTimeBase || myScenario.opPreGPS || myScenario.opPreSwitchBase || myScenario.opPreTemperature || myScenario.opPreWeather;
                                                hasActiveResult = myScenario.opResultNotify || myScenario.opResultSMS || myScenario.opResultSwitch;
                                                if (hasActivePre && hasActiveResult) {
                                                    myScenario.active = true;
                                                    Database.Scenario.edit(myScenario);
                                                    SysLog.log("Scenario " + myScenario.name + " Disabled by server", LogType.SCENARIO_CHANGE, LogOperator.WEB, 0);
                                                    //  Send message to server and local Mobiles
                                                    NetMessage netMessage = new NetMessage();
                                                    netMessage.data = myScenario.getScenarioStatusJSON(false);
                                                    netMessage.action = NetMessage.Update;
                                                    netMessage.type = NetMessage.ScenarioStatus;
                                                    netMessage.typeName = NetMessageType.ScenarioStatus;
                                                    netMessage.messageID = netMessage.save();
                                                    G.mobileCommunication.sendMessage(netMessage);
                                                    G.server.sendMessage(netMessage);
                                                } else {
                                                    G.log("MessageParser", "Should has at less one active precondition and one active result");
                                                    //  Send message to server and local Mobiles
                                                    NetMessage netMessage = new NetMessage();
                                                    netMessage.data = myScenario.getScenarioStatusJSON(false);
                                                    netMessage.action = NetMessage.Failed;
                                                    netMessage.type = NetMessage.ScenarioStatus;
                                                    netMessage.typeName = NetMessageType.ScenarioStatus;
                                                    netMessage.recieverIDs = newNetMessage.recieverIDs;
                                                    netMessage.messageID = netMessage.save();
                                                    netMessage.failedMessage = G.T.getSentence(577);
                                                    G.mobileCommunication.sendMessage(netMessage);
                                                    G.server.sendMessage(netMessage);
                                                }
                                            } else if (newState == 2) {
                                                G.scenarioBP.runScenario(myScenario);
                                            }
                                        }
                                    }
                                    break;

                                default:
                                    break;
                            }
                            G.ui.runOnScenarioChanged(-1);
                            break;
                        // end case ScenarioStatus:
                        case NetMessage.SyncData://SyncData
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    G.log("MessageParser", "Sync Data");
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        int mobileLastmsgID = msgJ.getInt("LastMessageID");
                                        String appVerCode = msgJ.getString("AppVerCode");
                                        int mobLang = msgJ.getInt("LanguageID");
                                        Database.Message.Struct[] newMessages = Database.Message.select("ID>" + mobileLastmsgID);
                                        if (newMessages != null) {
                                            long centerLastMessageID = 0;
                                            try {
                                                centerLastMessageID = Database.Message.getMax("ID", "").iD;
                                            } catch (Exception e) {
                                                centerLastMessageID = 0;
                                            }
                                            if (centerLastMessageID < mobileLastmsgID) { // در این حالت احتمالا بک آپ برگردانده شده بوده و باید اطلاعات مجدد از اول رفرش شود.
                                                G.log("MessageParser", "Sync Data- Refresh All Data");
                                                String result = Database.generateNewMobileConfiguration(null);
                                                // Send message to local Mobile
                                                NetMessage netMessage = new NetMessage();
                                                netMessage.data = "[" + result + "]";
                                                netMessage.action = NetMessage.Update;
                                                netMessage.type = NetMessage.RefreshData;
                                                netMessage.typeName = NetMessageType.RefreshData;
                                                netMessage.messageID = 0;
                                                netMessage.recieverIDs = newNetMessage.recieverIDs;
                                                G.server.sendMessage(netMessage);
                                            } else if (centerLastMessageID - mobileLastmsgID > G.MAX_MESSAGE_DIFFERENCE_TO_REFRESH) { // در این حالت  به دلیل تعداد زیاد پیام همه آنها فرستاده نمی شود و فقط نوتیفای ها فرستاده می شود سپس کل اطلاعات رفرش می شود.
                                                G.log("MessageParser", "Sync Data- Notify + RefreshData");
                                                newMessages = Database.Message.select("ID>" + mobileLastmsgID + " AND MessageType=" + NetMessageType.Notify.ordinal());
                                                NetMessageAction[] netMessageActionValues = NetMessageAction.values();
                                                NetMessageType[] netMessageTypeValues = NetMessageType.values();
                                                for (int k = 0; newMessages != null && k < newMessages.length; k++) {
                                                    try {
                                                        if (newMessages[k].recieverIDs.length() > 0 && !newMessages[k].recieverIDs.contains(0 + " ;"))
                                                            continue;
                                                        //  Send message to local Mobile
                                                        NetMessage netMessage = new NetMessage();
                                                        netMessage.data = newMessages[k].messageText;
                                                        netMessage.action = netMessageActionValues[newMessages[k].messageAction].ordinal();
                                                        netMessage.type = netMessageTypeValues[newMessages[k].messageType].ordinal();
                                                        netMessage.typeName = netMessageTypeValues[newMessages[k].messageType];
                                                        netMessage.messageID = newMessages[k].iD;
                                                        G.server.sendMessage(netMessage);
                                                    } catch (Exception e) {
                                                        G.printStackTrace(e);
                                                        break;
                                                    }
                                                }
                                                // send RefreshData
                                                String result = Database.generateNewMobileConfiguration(null);
                                                // Send message to local Mobile
                                                NetMessage netMessage = new NetMessage();
                                                netMessage.data = "[" + result + "]";
                                                netMessage.action = NetMessage.Update;
                                                netMessage.type = NetMessage.RefreshData;
                                                netMessage.typeName = NetMessageType.RefreshData;
                                                netMessage.messageID = 0;
                                                netMessage.recieverIDs = newNetMessage.recieverIDs;
                                                G.server.sendMessage(netMessage);
                                            } else { // در این حالت تمام پیام های مورد نیاز ارسال می شود.
                                                G.log("MessageParser", "Sync Data- Send All Packets");
                                                NetMessageAction[] netMessageActionValues = NetMessageAction.values();
                                                NetMessageType[] netMessageTypeValues = NetMessageType.values();
                                                for (int k = 0; newMessages != null && k < newMessages.length; k++) {
                                                    try {
                                                        if (newMessages[k].recieverIDs.length() > 0 && !newMessages[k].recieverIDs.contains(0 + " ;"))
                                                            continue;
                                                        //  Send message to local Mobile
                                                        NetMessage netMessage = new NetMessage();
                                                        netMessage.data = newMessages[k].messageText;
                                                        netMessage.action = netMessageActionValues[newMessages[k].messageAction].ordinal();
                                                        netMessage.type = netMessageTypeValues[newMessages[k].messageType].ordinal();
                                                        netMessage.typeName = netMessageTypeValues[newMessages[k].messageType];
                                                        netMessage.messageID = newMessages[k].iD;
                                                        G.server.sendMessage(netMessage);
                                                    } catch (Exception e) {
                                                        G.printStackTrace(e);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        // end  case SyncData:
                        case NetMessage.RefreshData://RefreshData
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    G.log("refresh data");
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);

                                        JSONArray jsonArray = jo.getJSONArray("ReceiverIDs");
                                        int receiverId = Integer.parseInt(jsonArray.get(0).toString());
                                        int[] receiverIds = new int[1];
                                        receiverIds[0] = receiverId;
                                        Database.Mobiles.Struct mobile = Database.Mobiles.select(receiverId);
                                        String result = Database.generateNewMobileConfiguration(mobile);
                                        NetMessage netMessage = new NetMessage();
                                        netMessage.recieverIDs = receiverIds;
                                        netMessage.data = "[" + result + "]";
                                        netMessage.action = NetMessage.Update;
                                        netMessage.type = NetMessage.RefreshData;
                                        netMessage.typeName = NetMessageType.RefreshData;
                                        netMessage.messageID = 0;
                                        netMessage.recieverIDs = newNetMessage.recieverIDs;
                                        G.server.sendMessage(netMessage);
                                    }
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // end  case RefreshData:
                        case NetMessage.GPSAnnounce://GPSAnnounce
                            switch (newNetMessage.action) {
                                case NetMessage.Update://Update
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        int mID = msgJ.getInt("MobileID");
                                        double lat = msgJ.getDouble("Latitude");
                                        double lon = msgJ.getDouble("Longitude");
                                        Database.Mobiles.Struct m = Database.Mobiles.select(mID);
                                        if (m != null) {
                                            m.LastLatitude = lat;
                                            m.LastLongitude = lon;
                                            Database.Mobiles.edit(m);
                                            G.scenarioBP.runByLocation(mID, lat, lon);
                                        }
                                    }
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // end case GPSAnnounce:
                        case NetMessage.Setting://Setting
                            switch (newNetMessage.action) {
                                case NetMessage.Update:
                                    for (int j = 0; j < dataJArray.length(); j++) {
                                        JSONObject msgJ = dataJArray.getJSONObject(j);
                                        G.setting.serverSocketIP = msgJ.getString("ServerIP");
                                        G.setting.serverSocketPort = msgJ.getInt("ServerPort");
                                        G.setting.serverURL = msgJ.getString("ServerURL");
                                        Database.Setting.edit(G.setting);
                                        G.server.stop();
                                        G.server.connectToServer();
                                    }
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // end  case Setting:
                        case NetMessage.KeepAlive://KeepAlive
                            // Do Nothing !
                            // end  case KeepAlive:
                    }
                }
                G.log("MessageParser", "Message pareser End." + c);
            } catch (JSONException e) {
                G.printStackTrace(e);
            }
        } else {
            G.log("MessageParser", "Data from server is not recognized !\n" + message);
        }
    }
}
