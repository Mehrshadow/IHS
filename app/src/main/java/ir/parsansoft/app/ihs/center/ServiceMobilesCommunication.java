package ir.parsansoft.app.ihs.center;

import android.app.IntentService;
import android.content.Intent;
import android.util.SparseArray;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import ir.parsansoft.app.ihs.center.Mobile.OnMobileConnected;
import ir.parsansoft.app.ihs.center.Mobile.OnMobileDataRecieve;
import ir.parsansoft.app.ihs.center.Mobile.OnMobileDisconnected;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;

public class ServiceMobilesCommunication extends IntentService {

    private SparseArray<Mobile> onlineMobiles = new SparseArray<Mobile>();
    ServerSocket mobilesSocket = null;

    public ServiceMobilesCommunication(String name) {
        super(name);
        startServer();
    }

    public ServiceMobilesCommunication() {
        super("ServiceMobilesCommunication");
        startServer();
    }

    public String getOnlineMobiles() {
        String onlines = "";
        for (int i = 0; i < onlineMobiles.size(); i++) {
            int mobileID = onlineMobiles.get(onlineMobiles.keyAt(i)).getMobileID();
            try {
                onlines += mobileID + " - " + Database.Mobiles.select(mobileID).name + "\n";
            } catch (Exception e) {
                G.printStackTrace(e);
            }
        }
        return onlines;
    }

    boolean isMobileServerRunning = false;

    public void startServer() {
        if (isMobileServerRunning) {
            G.log("Mobile server is running and can not run again");
            return;
        } else
            new Thread(new Runnable() {
                @Override
                public void run() {
                    G.log("Starting Mobile server");
                    isMobileServerRunning = true;
                    try {
                        mobilesSocket = new ServerSocket(G.DEFAULT_MOBILE_GET_PORT);
                        /*********************************** Jahanbin ***********************************/
                        tellMobilesCenterIsOnline();
                        /*********************************** Jahanbin ***********************************/
                        while (true) {
                            G.log("Listenning to port for any mobile connects !");
                            Socket socket = mobilesSocket.accept();
                            Mobile mobile = new Mobile(socket);
                            mobile.setOnMobileConnected(new OnMobileConnected() {
                                @Override
                                public void onEvent(Mobile mobile) {
                                    int mobileID = mobile.getMobileID();
                                    G.log("online Mobiles count =" + onlineMobiles.size());
                                    G.log("Mobile " + mobileID + " Connected");
                                    SysLog.log("Mobile " + mobile.getMobileName() + " connected.", LogType.SYSTEM_EVENTS, LogOperator.MOBILE, mobileID);
                                    if (onlineMobiles.indexOfKey(mobileID) > 0) {
                                        onlineMobiles.remove(mobileID);
                                    }
                                    onlineMobiles.put(mobileID, mobile);
                                    G.log("online Mobiles count =" + onlineMobiles.size());
                                }
                            });
                            mobile.setOnMobileDisconnected(new OnMobileDisconnected() {

                                @Override
                                public void onEvent(Mobile mobile) {
                                    int mobileID = mobile.getMobileID();
                                    G.log("Mobile " + mobileID + " Disconnected");
                                    SysLog.log("Mobile " + mobile.getMobileName() + " disconnected.", LogType.SYSTEM_EVENTS, LogOperator.MOBILE, mobileID);
                                    if (onlineMobiles.indexOfKey(mobileID) > 0) {
                                        onlineMobiles.remove(mobileID);
                                    }
                                }
                            });
                            mobile.setOnMobileDataRecieved(new OnMobileDataRecieve() {

                                @Override
                                public void onEvent(Mobile mobile, String data) {
                                    MessageParser.parseFromMobile(mobile.getMobileID(), data.trim());
                                }
                            });
                            mobile.startlisten();
                        }
                    } catch (IOException e) {
                        G.printStackTrace(e);
                    } finally {
                        try {
                            isMobileServerRunning = false;
                            mobilesSocket.close();
                        } catch (Exception e) {
                            G.printStackTrace(e);
                        }
                    }
                }
            }).start();
    }

    /**
     * <p> Used to send message & push notification to mobiles</p>
     */
    public void sendMessage(NetMessage message) {
        String msgText = message.getJson();
        if (message.recieverIDs == null) {
            G.log("Sending data to all mobiles !");
            for (int i = 0; i < onlineMobiles.size(); i++) {
                onlineMobiles.valueAt(i).sendToMobile(msgText);
            }
        } else
            for (int i = 0; i < message.recieverIDs.length; i++)
                if (onlineMobiles.indexOfKey(message.recieverIDs[i]) >= 0) {
                    onlineMobiles.get(message.recieverIDs[i]).sendToMobile(msgText);
                }

        sendPushNotificationToAll(message);
    }

    /***********************************
     * Jahanbin
     ***********************************/
    public void sendPushNotificationToAll(NetMessage message) {
        if (message.recieverIDs == null) {
            G.log("Sending data to all mobiles !");
            String notificationTitle = "IHS", notificationBody = "New change";
            boolean send = true;

            switch (message.action) {
                case 1:// Update
                case 5://Failed
                    send = false;
                    break;
                case 4://SocketStart
                    notificationBody = "start";
                    break;
            }
            switch (message.type) {
                case 4://SwitchData
                    send = false;
                    break;
            }
            if (send) {
                ArrayList<Database.Mobiles.Struct> allTokens = Database.Mobiles.getAllTokens();

                if (allTokens.size() != 0) {
                    for (int i = 0; i < allTokens.size(); i++) {
                        G.firebaseNotifier.sendNotification(notificationTitle, notificationBody, allTokens.get(i).FirebaseToken);
                    }
                }
            }
        }
    }

    private void tellMobilesCenterIsOnline() {
        NetMessage netMessage = new NetMessage();
        netMessage.type = NetMessage.SocketStatus;
        netMessage.typeName = NetMessage.NetMessageType.ScenarioStatus;
        netMessage.action = NetMessage.SocketStart;
//        netMessage.messageID = netMessage.save();
        sendPushNotificationToAll(netMessage);
    }

    /***********************************
     * Jahanbin
     ***********************************/

    public void disconnectAll() {
        try {
            Thread.sleep(2 * 1000);// 2 second sleep to let mobiles receive socketStop msg
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < onlineMobiles.size(); i++)
            onlineMobiles.valueAt(i).disconnect();
    }

    public void disconnectMobile(int mobileID) {
        if (onlineMobiles.indexOfKey(mobileID) >= 0)
            onlineMobiles.get(mobileID).disconnect();
    }

    public void stopServer() {
        // telling all mobiles to stop their sockets before disconnecting all
        tellMobilesStopSockets();

        disconnectAll();
        try {
            mobilesSocket.close();
        } catch (Exception e) {
            G.printStackTrace(e);
        }
    }

    private void tellMobilesStopSockets() {
        G.log("Sending data to all mobiles !");
        for (int i = 0; i < onlineMobiles.size(); i++) {
            onlineMobiles.valueAt(i).sendToMobile("stop");
        }
    }

    public void sendMessage(NetMessage message, int MobileID) {
        if (onlineMobiles.indexOfKey(MobileID) >= 0)
            onlineMobiles.get(MobileID).sendToMobile(message.getJson());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

    }

}
