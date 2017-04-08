package ir.parsansoft.app.ihs.center;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class NetMessage {

    public enum NetMessageType {
        SwitchStatus,//0
        RoomData,//1
        SectionData,//2
        NodeData,//3
        SwitchData,//4
        ScenarioStatus,//5
        ScenarioData,//6
        MobileData,//7
        Setting,//8
        SyncData,//9
        RefreshData,//10
        Notify,//11
        GPSAnnounce,//12
        KeepAlive,//13
        SocketStatus //14
    }


    public enum NetMessageAction {
        Delete,//0
        Update,//1
        Insert,//2
        SocketStop,//3
        SocketStart,//4
        Failed//5
    }

    public final static int SwitchStatus = 0;
    public final static int RoomData = 1;
    public final static int SectionData = 2;
    public final static int NodeData = 3;
    public final static int SwitchData = 4;
    public final static int ScenarioStatus = 5;
    public final static int ScenarioData = 6;
    public final static int MobileData = 7;
    public final static int Setting = 8;
    public final static int SyncData = 9;
    public final static int RefreshData = 10;
    public final static int Notify = 11;
    public final static int GPSAnnounce = 12;
    public final static int KeepAlive = 13;
    public final static int SocketStatus = 14;

    public final static int Delete = 0;
    public final static int Update = 1;
    public final static int Insert = 2;
    public final static int SocketStop = 3;
    public final static int SocketStart = 4;
    public final static int Failed = 5;

    public int messageID = 0;
    public int[] recieverIDs = null;                   // if (recieverIDs == null)  will send to all
    public int type = NetMessage.SyncData;
    public NetMessageType typeName = NetMessageType.SyncData;
    //    public NetMessageAction action        = NetMessageAction.Update;
    public int action = NetMessage.Update;
    public String data = "";                     // Json
    public int recordId = 0;
    public String failedMessage = null;
    public java.util.Date datetime = null;


    public NetMessage() {

    }

    public NetMessage(String inputJson) {
        setJson(inputJson);
    }

    public String getJson() {
        G.log("NetMessage", "getJson");
        String recieverIDsJSON;
        JSONArray jsonArrRIDs = new JSONArray();
        if (recieverIDs != null) {
            for (int i = 0; i < recieverIDs.length; i++) {
                try {
                    jsonArrRIDs.put(recieverIDs[i]);
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
            }
        }
        String outJson = "";
        JSONObject jo = new JSONObject();
        try {

            jo.put("MessageID", messageID);
            jo.put("ReceiverIDs", jsonArrRIDs);
            jo.put("Type", type);
            jo.put("Action", action);

            switch (type) {
                case NetMessage.SwitchStatus:
                    jo.put("SwitchStatus", new JSONArray(data));
                    break;
                case NetMessage.RoomData:
                    jo.put("RoomData", new JSONArray(data));
                    break;
                case NetMessage.SectionData:
                    jo.put("SectionData", new JSONArray(data));
                    break;
                case NetMessage.NodeData:
                    jo.put("NodeData", new JSONArray(data));
                    break;
                case NetMessage.SwitchData:
                    jo.put("SwitchData", new JSONArray(data));
                    break;
                case NetMessage.ScenarioStatus:
                    jo.put("ScenarioStatus", new JSONArray(data));
                    break;
                case NetMessage.ScenarioData:
                    jo.put("ScenarioData", new JSONArray(data));
                    break;
                case NetMessage.MobileData:
                    jo.put("MobileData", new JSONArray(data));
                    break;
                case NetMessage.Setting:
                    jo.put("Setting", new JSONArray(data));
                    break;
                case NetMessage.SyncData:
                    jo.put("SyncData", new JSONArray(data));
                    break;
                case NetMessage.RefreshData:
                    jo.put("RefreshData", new JSONArray(data));
                    break;
                case NetMessage.Notify:
                    jo.put("Notify", new JSONArray(data));
                    break;
                case NetMessage.GPSAnnounce:
                    jo.put("GPSAnnounce", new JSONArray(data));
                    break;
                case NetMessage.KeepAlive:
                    jo.put("KeepAlive", new JSONArray(data));
                    break;
                case NetMessage.SocketStatus:
                    jo.put("SocketStatus", new JSONArray(data));
                    break;
            }
            if (datetime == null)
                datetime = new java.util.Date(System.currentTimeMillis());
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
            String strDate = sdfDate.format(datetime);
            jo.put("Date", strDate);
            if (failedMessage != null)
                jo.put("FailedMessage", failedMessage);
            outJson = "[" + jo.toString() + "]";
        } catch (JSONException e) {
            G.printStackTrace(e);
        }
        return outJson;
    }

    public boolean setJson(String json) {
        try {
            G.log("NetMessage", "Set Json 1");
            JSONObject jo = new JSONObject(json);
            G.log("NetMessage", "Set Json 2");
            try {
                messageID = Database.Message.getMax("ID", "").iD;
            } catch (Exception e) {
                G.printStackTrace(e);
                messageID = 0;
            }
            G.log("NetMessage", "Set Json 3");
            if (jo.has("recieverIDs")) {
                G.log("NetMessage", "Set Json 4");
                JSONArray joRid = jo.getJSONArray("RecieverIDs");
                G.log("NetMessage", "Set Json 5");
                recieverIDs = new int[joRid.length()];
                G.log("NetMessage", "Set Json 6");
                for (int i = 0; i < joRid.length(); i++) {
                    recieverIDs[i] = joRid.getInt(i);
                }
                G.log("NetMessage", "Set Json 7");
            } else
                recieverIDs = null;
//            type = NetMessageType.valueOf(jo.getString("Type"));
            type = jo.getInt("Type");
            G.log("NetMessage", "Set Json 8");
            if (jo.has("Action"))
//                action = NetMessageAction.valueOf(jo.getString("Action"));
                action = jo.getInt("Action");
            G.log("NetMessage", "Set Json 9");
            data = jo.getString(NetMessageType.values()[type].name());
            G.log("NetMessage", "Set Json 10");
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                datetime = sdf.parse(jo.getString("Date"));
                G.log("NetMessage", "Set Json 11");
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            if (jo.has("FailedMessage")) {
                failedMessage = jo.getString("FailedMessage");
                G.log("NetMessage", "Set Json 12");
            }
            G.log("NetMessage", "Set Json 13");
            return true;
        } catch (JSONException e) {
            G.log("NetMessage", "Set Json 14");
            G.printStackTrace(e);
        }
        return false;
    }

    public int save() {
        if (!(action == NetMessage.Failed)) {
            datetime = Calendar.getInstance().getTime();
            String sReciverIDs = "";
            if (recieverIDs != null) {
                G.log("Setting reciver IDs to Netmessage");
                for (int i = 0; i < recieverIDs.length; i++) {
                    sReciverIDs += recieverIDs[i] + " ;";
                }
                G.log("recieverIDs=" + sReciverIDs);
            }
            try {
                JSONArray recordIdArray = new JSONArray(data);
                JSONObject recordIdObject = recordIdArray.getJSONObject(0);
                recordId = Integer.parseInt(recordIdObject.get("ID").toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (action == NetMessage.Insert) {
                    messageID = (int) Database.Message.insert(recordId, type, data, sReciverIDs, action, sdf.format(datetime.getTime()));
                } else {
                    messageID = (int) Database.Message.insert(recordId, type, data, sReciverIDs, action, sdf.format(datetime.getTime()));
                    Database.Message.delete(type, recordId);
                }
            } catch (Exception e) {
                G.printStackTrace(e);
            }
        } else
            messageID = -1;
        return messageID;
    }
}