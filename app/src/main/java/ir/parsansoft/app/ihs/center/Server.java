package ir.parsansoft.app.ihs.center;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import ir.parsansoft.app.ihs.center.NetMessage.NetMessageAction;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;

public class Server {
    private Socket socketToServer;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;
    private int connectionStatus;     //0= Disconnected , 1= Connected , 2= Connecting
    private boolean loop;
    private boolean keepAlive = false;
    Thread t;
    private OnServerConnected mOnServerConnected;
    private OnServerDisconnected mOnServerDisconnected;
    private OnServerDataRecieve mOnServerDataRecieve;

    public interface OnServerConnected {
        void onConnect();
    }

    public interface OnServerDisconnected {
        void onDisconnect();
    }

    public interface OnServerDataRecieve {
        void onDataRecived(String Data);
    }

    public void setOnServerConnected(OnServerConnected eventListener) {
        mOnServerConnected = eventListener;
    }

    public void setOnServerDisconnected(OnServerDisconnected eventListener) {
        mOnServerDisconnected = eventListener;
    }

    public void setOnServerDataRecieve(OnServerDataRecieve eventListener) {
        mOnServerDataRecieve = eventListener;
    }

    public Server() {
    }


    public int getConnectionStatus() {
        return connectionStatus;
    }


    public boolean isConnected() {
        if (connectionStatus == 1)
            return true;
        else
            return false;
    }


    public void connectToServer() {
        if (t == null) {
            t = new Thread() { // Read Data
                @Override
                public void run() {
                    G.log("Server", "New Thread to connect to server ...!");
                    loop = true;
                    while (loop) {
                        try {
                            connectionStatus = 2;
                            G.log("Server", "Try to connect ...");
                            socketToServer = new Socket();
                            socketToServer.connect(new InetSocketAddress(G.getServerIP(), G.getServerPort()), G.DEFAULT_SERVER_SOCKET_TIMEOUT);
                            G.log("Server", "Connected ...");
                            outputStream = new DataOutputStream(socketToServer.getOutputStream());
                            inputStream = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));
                            //                            inputStream = new BufferedReader(new InputStreamReader(socketToServer.getInputStream(), "UTF-8"));
                            connectionStatus = 1;
                            G.log("Server", "Socket to server is open !");
                            String msg = "[{\"CustomerID\":" + G.setting.customerID + ",\"ExKey\":\"" + G.setting.exKey + "\"}]";
                            G.log("Server", msg);
                            if (sendMessage(msg)) {
                                if (mOnServerConnected != null)
                                    mOnServerConnected.onConnect();
                                StartListen();
                            }
                        } catch (Exception e) {
                            G.log("Server", "Error while connecting to server :" + e.getMessage());
                            G.printStackTrace(e);
                        } finally {

                            if (connectionStatus == 1 && mOnServerDisconnected != null)
                                mOnServerDisconnected.onDisconnect();
                            connectionStatus = 0;
                            try {
                                if (!socketToServer.isClosed())
                                    socketToServer.close();
                                socketToServer = null;
                                outputStream = null;
                                inputStream = null;
                            } catch (IOException e1) {
                                G.printStackTrace(e1);
                            } catch (Exception e1) {
                                G.printStackTrace(e1);
                            }
                            try {
                                // waite for a few seccond to next try
                                Thread.sleep(G.DEFAULT_SERVER_CONNECTION_RETRY_TIMEOUT);
                            } catch (InterruptedException e) {
                                G.printStackTrace(e);
                            }
                        }
                    }
                    G.log("Server", "Thread of connection to server closed !");
                    t = null;
                }
            };
            t.start();
        }
    }

    public void stop() {
        loop = false;
        try {
            socketToServer.close();
        } catch (Exception e) {
            G.printStackTrace(e);
        }
    }


    public boolean sendMessage(NetMessage netMessage) {
        return sendMessage(netMessage.getJson());
    }

    public boolean sendMessage(String data) {
        G.log("Server", "Sending data to server :\r\n" + data);
        if (outputStream == null || connectionStatus != 1 || data.equals("")) {
            G.log("Server", "outputStream is null");
            return false;
        }
        try {
            outputStream.write((data + "\n").getBytes("UTF-8"));
            outputStream.flush();
        } catch (Exception e) {
            G.printStackTrace(e);
            try {
                socketToServer.close();
            } catch (Exception e2) {

            }
            return false;
        }
        return true;
    }


    private void StartListen() {
        while (true) {
            if (socketToServer.isClosed()) {
                connectionStatus = 0;
                break;
            }
            String message = "";
            try {
                G.log("Server", "Reading from server socket !");
                //message = inputStream.readLine();

                byte[] b = new byte[1];
                int i = 0;
                int bytesToRead;
                while ((bytesToRead = socketToServer.getInputStream().read(b)) > 0) {
                    G.log("Server", new String(b) + " - " + b[0]);
                    if (b[0] == 13 || b[0] == 10) {
                        G.log("Server", "End of stream ..");
                        break;
                    }
                    message += new String(b);
                    i++;
                }

                G.log("Server", "Message from server :\n" + message);
//                G.toast("Message from server :\n" + message);
            } catch (Exception e1) {
                G.log("Server", "Error while reading from Server Socket !");
                G.printStackTrace(e1);
            }
            if (message == null)
                G.log("Server", "Message from server was null. close connection." + message);
            else if (message.length() == 0)
                G.log("Server", "Message from server was ziro length. close connection." + message);
            if (!(message == null) && !message.equals("")) {
                keepAlive = true;
                if (mOnServerDataRecieve != null)
                    mOnServerDataRecieve.onDataRecived(message);
            } else {
                break;
            }
        }
    }


    public void sendKeepAlive() {
        //  Send message to Server
        NetMessage netMessage = new NetMessage();
        netMessage.data = "";
        netMessage.action = NetMessage.Update;
        netMessage.type = NetMessage.KeepAlive;
        netMessage.typeName = NetMessageType.KeepAlive;
        sendMessage(netMessage);
        keepAlive = false;
        G.HANDLER.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!keepAlive) {
                    try {
                        socketToServer.close();
                    } catch (IOException e) {
                        G.printStackTrace(e);
                    }
                }
            }
        }, G.DEFAULT_SERVER_SOCKET_TIMEOUT);
    }


    //    private String readFromSocket(Socket socket) {
    //        String data = "";
    //        int bytesToRead;
    //        try {
    //            byte[] b = new byte[1];
    //            while ((bytesToRead = socket.getInputStream().read(b)) != -1) {
    //                String newChar = new String(b);
    //                if (newChar.equals("\0"))
    //                    break;
    //                data += newChar;
    //            }
    //        }
    //        catch (IOException e) {
    //            G.printStackTrace(e);
    //        }
    //        return data;
    //    }
}
