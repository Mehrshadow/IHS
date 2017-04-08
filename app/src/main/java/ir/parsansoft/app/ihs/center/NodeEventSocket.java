package ir.parsansoft.app.ihs.center;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class NodeEventSocket {

    private Socket           socket        = null;
    Database.Node.Struct     node;
    int                      lastEventTime = 0;
    String                   ipAddress;
    NodeEventSocket          me;

    onNewMessageListener     onml          = null;
    onDisconnectListener     ondcl         = null;

    private DataOutputStream outputStream;
    private BufferedReader   inputStream;


    public interface onNewMessageListener {
        public void onNewMessage(NodeEventSocket nodeEventSocket, String message);
    }

    public void setOnNewMessageListener(onNewMessageListener onNewMessageListener) {
        onml = onNewMessageListener;
    }

    public interface onDisconnectListener {
        public void onDisconnect(NodeEventSocket nodeEventSocket);
    }

    public void setOnDisconnectListener(onDisconnectListener onDisconnectListener) {
        ondcl = onDisconnectListener;
    }

    public NodeEventSocket(Socket socket) {
        this.socket = socket;
        ipAddress = socket.getRemoteSocketAddress().toString();
        ipAddress = ipAddress.substring(1, ipAddress.indexOf(':'));
        try {
            node = Database.Node.select("IP LIKE '" + ipAddress + "'")[0];
        }
        catch (Exception e) {
            G.printStackTrace(e);
        }

        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        }
        catch (IOException e) {
            G.printStackTrace(e);
            return;
        }
        me = this;
        listen();
    }

    private void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = "";
                while (true) {
                    //                        byte[] data = new byte[1500];
                    //                        int count = socket.getInputStream().read(data);
                    //                        if (count > 0)
                    //                        {
                    //                            String msg = new String(data, "UTF-8");
                    //                            G.log("Message recived from node !" + msg);
                    //                            G.toast("Message recived from node !" + msg);
                    //                            if (onml != null)
                    //                                onml.onNewMessage(me, msg);
                    //                        }
                    //                        else {
                    //                            //G.log("Empty packet recived");
                    //                        }
                    try {
                        if (socket.isClosed()) {
                            return;
                        }
                        data = inputStream.readLine();
                        if ( !(data == null) && data.length() > 0) {
                            //G.toast(data);
                            if (onml != null)
                                onml.onNewMessage(me, data);
                        }
                        else {
                            if (data == null)
                                G.log("data from Node is null");
                            else
                                G.log("data from Node is empty");
                            break;
                        }
                    }
                    catch (IOException e) {
                        G.log(e.getMessage());
                        G.printStackTrace(e);
                        return;
                    }
                }
                G.log("Socket Closed");
                try {
                    socket.close();
                }
                catch (IOException e) {
                    G.printStackTrace(e);
                }
            }
        }).start();
    }
}
