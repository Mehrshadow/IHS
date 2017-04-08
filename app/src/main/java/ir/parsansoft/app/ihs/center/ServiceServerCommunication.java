package ir.parsansoft.app.ihs.center;

import android.app.IntentService;
import android.content.Intent;

import ir.parsansoft.app.ihs.center.Server.OnServerConnected;
import ir.parsansoft.app.ihs.center.Server.OnServerDataRecieve;
import ir.parsansoft.app.ihs.center.Server.OnServerDisconnected;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;


public class ServiceServerCommunication extends IntentService {

    public ServiceServerCommunication(String name) {
        super(name);
        connectToServer();
    }

    public ServiceServerCommunication() {
        super("ServiceServerConnection");
        connectToServer();
    }

    private void connectToServer() {
        if (G.server == null) {
            G.server = new Server();
            G.log("ServerCommunication", "Starting connection to server :\r\n");
            G.server.connectToServer();
        }
        G.server.setOnServerConnected(mOnServerConnected);
        G.server.setOnServerDisconnected(mOnServerDisconnected);
        G.server.setOnServerDataRecieve(mOnServerDataRecieve);
    }

    OnServerConnected mOnServerConnected = new OnServerConnected() {
        @Override
        public void onConnect() {
            G.log("ServerCommunication", "ServiceServerCommunication - OnServerConnected");
            SysLog.log("Connected to server.", LogType.SYSTEM_EVENTS, LogOperator.WEB, 0);
            G.ui.runOnServerStatusChanged(1);
        }
    };
    OnServerDisconnected mOnServerDisconnected = new OnServerDisconnected() {

        @Override
        public void onDisconnect() {
            G.log("ServerCommunication", "ServiceServerCommunication - OnServerDisconnected");
            SysLog.log("Disconnected from server.", LogType.SYSTEM_EVENTS, LogOperator.WEB, 0);
            G.ui.runOnServerStatusChanged(0);
        }
    };
    OnServerDataRecieve mOnServerDataRecieve = new OnServerDataRecieve() {

        @Override
        public void onDataRecived(String data) {
            G.log("ServerCommunication", "ServiceServerCommunication - OnServerDataRecieve");
            MessageParser.parseFromServer(data);
        }
    };



    @Override
    protected void onHandleIntent(Intent workIntent) {

    }

}
