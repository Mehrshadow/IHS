package ir.parsansoft.app.ihs.center;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import ir.parsansoft.app.ihs.center.NetMessage.NetMessageAction;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;


public class Mobile {

    private int mobileID = 0;
    private String mobileName = "";
    private Socket socket;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;
    private int status = 0;       // 0= disconnected    1= connected    2= Trying
    private String firebaseToken;

    private OnMobileConnected mOnMobileConnected;
    private OnMobileDisconnected mOnMobileDisconnected;
    private OnMobileDataRecieve mOnMobileDataRecieve;

    public interface OnMobileConnected {
        void onEvent(Mobile mobile);
    }

    public interface OnMobileDisconnected {
        void onEvent(Mobile mobile);
    }

    public interface OnMobileDataRecieve {
        void onEvent(Mobile mobile, String data);
    }


    public void setOnMobileConnected(OnMobileConnected eventListener) {
        mOnMobileConnected = eventListener;
    }

    public void setOnMobileDisconnected(OnMobileDisconnected eventListener) {
        mOnMobileDisconnected = eventListener;
    }

    public void setOnMobileDataRecieved(OnMobileDataRecieve eventListener) {
        mOnMobileDataRecieve = eventListener;
    }

    public Mobile(Socket socket) {
        this.socket = socket;
        try {
            G.log("New Mobile is requesting to connect ...");
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            status = 1;
        } catch (IOException e1) {
            G.log("Error: Connection is not stable");
            status = 2;
        }
    }

    private void dataReciever(String data) {

        //        at the first data recieve mobile should send identity information and 
        //        here will check if the mobile is allowed to communicate then call its
        //        mOnMobileConnected event like this :
        //            if (mOnMobileDataRecieve != null)
        //                 mOnMobileDataRecieve.onEvent(data);
        //        after that if the mobile was authenticated this method will call other
        //        functions to process the commands.
        if (mobileID == 0) {
            try {
                JSONArray jArray = new JSONArray(data);
                JSONObject jObject = jArray.getJSONObject(0);
                int id = jObject.getInt("MobileID");
                String exKey = jObject.getString("ExKey");
                G.log("new Mobile ID=" + id);
                Database.Mobiles.Struct newMobile = Database.Mobiles.select(id);
                if (newMobile != null && newMobile.exKey.equals(exKey)) {
                    G.log("new mobile :" + id + " has authorized ");
                    mobileID = id;
                    mobileName = newMobile.name;
                    /*************************************** Jahanbin *****************************************/
                    firebaseToken = newMobile.FirebaseToken;
                    /************************************** Jahanbin **************************************/
                    if (mOnMobileConnected != null)
                        mOnMobileConnected.onEvent(this);
                } else {
                    try {
                        G.log("Mobile is not permitted ! " + id);
                        //  Send message  Mobile
                        NetMessage netMessage = new NetMessage();
                        JSONObject jo = new JSONObject();
                        netMessage.data = "[{\"Message\":\"Not Permitted\"}]";
                        netMessage.type = NetMessage.SyncData;
                        netMessage.typeName = NetMessageType.SyncData;
                        netMessage.action = NetMessage.Delete;
                        sendToMobile((netMessage.getJson() + "\n"));
                        socket.close();
                    } catch (UnsupportedEncodingException e) {
                        G.printStackTrace(e);
                    } catch (IOException e) {
                        G.printStackTrace(e);
                    }
                }
                return;
            } catch (JSONException e) {
                G.printStackTrace(e);
                return;
            }
        }
        //-----------------------------------------------------------------------------
        if (mOnMobileDataRecieve != null)
            mOnMobileDataRecieve.onEvent(this, data);
    }


    public boolean sendToMobile(String data) {
        G.log("Sending data to mobile :" + mobileID + "\r\n" + data);
        if (socket.isClosed()) {
            G.log("Socket to mobile " + mobileID + " is closed");
            return false;
        }
        try {
            outputStream.write((data + "\n").getBytes("UTF-8"));
            outputStream.flush();
        } catch (IOException e) {
            G.printStackTrace(e);
            return false;
        }
        return true;
    }


    public void startlisten() {
        if (status == 1)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    listen();
                    G.log("some Mobile disconnected - ID: " + mobileID);
                    onDisconnect();
                }
            }).start();
    }

    private void listen() {
        while (true) {
            try {
                if (socket.isClosed()) {
                    G.log("Socket " + mobileID + " is disconnected !");
                    status = 0;
                    return;
                }
                status = 1;
                String data = "";
                G.log("waiting for mobile " + mobileID + " to say something ...");
                data = inputStream.readLine();
                G.log("Getting new data from mobile" + mobileID + " :");
                if (!(data == null) && data.length() > 0) {
                    //G.toast(data);
                    G.log("Mobile " + mobileID + " : " + data);
                    dataReciever(data);
                } else {
                    if (data == null)
                        G.log("data from mobile " + mobileID + " is null");
                    else
                        G.log("data from mobile " + mobileID + " is empty");
                    break;
                }
            } catch (IOException e) {
                G.log(e.getMessage());
                G.printStackTrace(e);
                return;
            }
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            G.printStackTrace(e);
        }
    }

    private void onDisconnect() {
        try {
            if (socket.isConnected())
                socket.close();
        } catch (IOException e) {
            G.printStackTrace(e);
        }
        if (mOnMobileDisconnected != null)
            mOnMobileDisconnected.onEvent(this);
    }

    public int getStatus() {
        return status;
    }

    public int getMobileID() {
        return mobileID;
    }

    public String getMobileFirebaseToken() {
        return firebaseToken;
    }

    public String getMobileName() {
        return mobileName;
    }
}
