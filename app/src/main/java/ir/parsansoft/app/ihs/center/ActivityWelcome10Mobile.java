package ir.parsansoft.app.ihs.center;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_setting_mobile;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.ModuleWebservice.WebServiceListener;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewMarket;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioNotifyMobiles;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioNotifyMobiles.onDeleteItem;

public class ActivityWelcome10Mobile extends ActivityWizard {

    private CO_f_setting_mobile fo;
    private String newExKey;
    private AdapterScenarioNotifyMobiles listAdapter;
    Database.Mobiles.Struct[] mobiles;

    private ServerSocket mobilesSocketListener = null;
    private Socket socket;
    private int socketStatus = 0;    // 0= disconnected    1= connected    2= Trying
    private int progressStatus = 0;
    AdapterListViewMarket adapterMarkets;
    boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.f_setting_mobile);
        fo = new CO_f_setting_mobile(this);
        fo.txtTitleMarket.setVisibility(View.INVISIBLE);
        translateForm();
        refreshList();
        loadMarkets();
        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isBusy) {
                    isBusy = true;
                    startActivity(new Intent(G.currentActivity, ActivityWelcome9Equipment.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        setOnNextListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isBusy) {
                    isBusy = true;
                    startActivity(new Intent(G.currentActivity, ActivityWelcome11Scenario.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });
    }


    private void refreshList() {
        ArrayAdapter<String> adapter = null;
        List<String> listMobiles = null;
        listMobiles = new ArrayList<String>();

        mobiles = Database.Mobiles.select("");
        if (mobiles != null) {
            for (int i = 0; i < mobiles.length; i++) {
                listMobiles.add(mobiles[i].name);
            }
        }
        listAdapter = new AdapterScenarioNotifyMobiles(G.currentActivity, listMobiles);
        listAdapter.setOnDeleteItemListener(new onDeleteItem() {
            @Override
            public void onDeleteItemListener(int index) {
                //check if mobile is in use in the scenarios ....
                Database.Scenario.Struct[] snrs = Database.Scenario.select("GPS_Params LIKE '%" + mobiles[index].name + "%'");
                if (snrs != null) {
                    String scenarioNames = "";
                    for (int i = 0; i < snrs.length; i++)
                        scenarioNames += snrs[i].name + "\n";
                    G.log("can not delete this mobile because this mobile is used by:\n" + scenarioNames);
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(777), G.T.getSentence(804) + "\n" + scenarioNames);
                    return;
                }
                Database.Mobiles.delete(mobiles[index].iD);
                SysLog.log("Mobile " + mobiles[index].name + " Deleted.", SysLog.LogType.SYSTEM_SETTINGS, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                G.log("Item Deleted :" + mobiles[index].iD + ";");
                //  Send message  Mobile
                NetMessage netMessage = new NetMessage();
                JSONObject jo = new JSONObject();
                netMessage.recieverIDs = new int[1];
                netMessage.recieverIDs[0] = mobiles[index].iD;
                netMessage.data = "[" + mobiles[index].getMobileDataJson() + "]";
                netMessage.type = NetMessage.MobileData;
                netMessage.typeName = NetMessageType.MobileData;
                netMessage.action = NetMessage.Delete;
                refreshList();
                G.mobileCommunication.sendMessage(netMessage);
                G.server.sendMessage(netMessage);
            }
        });
        G.HANDLER.post(new Runnable() {

            @Override
            public void run() {
                fo.lstMobiles.setAdapter(listAdapter);
            }
        });
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.txtTitle.setText(G.T.getSentence(763));
        fo.txtTitleMarket.setText(G.T.getSentence(764));
    }


    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    makeNewQR();
                    mobilesSocketListener = new ServerSocket(G.DEFAULT_MOBILE_NEW_GET_PORT);
                    socket = mobilesSocketListener.accept();
                    hideQR();
                    startlisten();
                } catch (IOException e) {
                    G.printStackTrace(e);
                } finally {
                    try {
                        G.log("Socket listenner is closing ...");
                        mobilesSocketListener.close();
                    } catch (Exception e2) {
                        G.printStackTrace(e2);
                    }
                }
            }
        }).start();
    }

    private void startlisten() {
        if (socketStatus == 0)
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            if (socket.isClosed()) {
                                socketStatus = 0;
                                return;
                            }
                            socketStatus = 1;
                            //                            byte[] b = new byte[1];
                            //                            String data = "";
                            //                            int bytesToRead;
                            //                            while ((bytesToRead = socket.getInputStream().read(b)) > 0) {
                            //                                String c = new String(b);
                            //                                if (c.equals("\0") || c.equals("\n")) {
                            //                                    G.log("ziro  or \\n detected");
                            //                                    break;
                            //                                }
                            //                                data += c;
                            //                                G.log(data);
                            //                            }

                            String data = "";
                            int bytesToRead;
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                            String c = "";
                            //while ((c = in.readLine()) != null)                            
                            c = in.readLine();
                            data += c;
                            G.log(data);

                            if (!(data == null) && !data.equals("")) {
                                dataReciever(data);
                            }
                        } catch (Exception e) {
                            G.log(e.getMessage());
                            G.printStackTrace(e);
                            return;
                        }
                    }
                }
            }).start();
    }

    private void dataReciever(String data) {
        try {
            G.log("Data recived :\n" + data);
            JSONObject jObject = new JSONObject(data);
            String cmdType = jObject.getString("Type");
            if (cmdType.equals("RequestRegisterMobile")) {
                progressStatus = 1;
                G.log("Registring new mobile");
                String newMobileName = jObject.getString("MobileName");
                String newMobileExKey = jObject.getString("ExKey");
                String newMobileSerial = jObject.getString("Serial").toLowerCase();
                String firebaseToken = "";

                if (jObject.has("FirebaseToken")) {
                    firebaseToken = jObject.getString("FirebaseToken");
                }
                G.log("newMobileName :" + newMobileName);
                G.log("newMobileExKey :" + newMobileExKey);
                if (!newMobileExKey.equals(newExKey))
                    return;
                Database.Mobiles.Struct newMobile;
                try {
                    newMobile = Database.Mobiles.select("SerialNumber like '" + newMobileSerial + "'")[0];
                } catch (Exception e) {
                    newMobile = null;
                }

                if (newMobile != null) {
                    newMobile.name = newMobileName;
                    Random r = new Random();
                    int aNumber = r.nextInt(10000) * Calendar.getInstance().get(Calendar.MILLISECOND);
                    newMobile.exKey = Utility.getIdHash(aNumber);
                    Database.Mobiles.edit(newMobile);
                } else {
                    newMobile = new Database.Mobiles.Struct();
                    newMobile.serialNumber = newMobileSerial;
                    newMobile.name = newMobileName;
                    newMobile.FirebaseToken = firebaseToken;
                    Random r = new Random();
                    int aNumber = r.nextInt(10000) * Calendar.getInstance().get(Calendar.MILLISECOND);
                    newMobile.exKey = Utility.getIdHash(aNumber);
                    newMobile.iD = (int) Database.Mobiles.insert(newMobile);
                }
                String newMobileFullConfiguration = Database.generateNewMobileConfiguration(newMobile);
                G.log(newMobileFullConfiguration);
                //      Sending setting to socket ...
                /************************ Jahanbin **********************/
                if (!newMobileFullConfiguration.equals("") && canSendToMobiles()) {
                    /************************ Jahanbin **********************/
                    refreshList();
                    SysLog.log("Mobile " + newMobile.name + " Added.", SysLog.LogType.SYSTEM_SETTINGS, SysLog.LogOperator.OPERAOR, 1);
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(105), G.T.getSentence(833).replace("[1]", " [ " + newMobile.name + " ] "));
                    //  Send message  Mobile
                    NetMessage netMessage = new NetMessage();
                    JSONObject jo = new JSONObject();
                    netMessage.recieverIDs = new int[1];
                    netMessage.recieverIDs[0] = 0; // only server needs to know
                    netMessage.data = "[" + newMobile.getMobileDataJson() + "]";
                    netMessage.type = NetMessage.MobileData;
                    netMessage.typeName = NetMessageType.MobileData;
                    netMessage.action = NetMessage.Insert;
                    netMessage.messageID = netMessage.save();
                    G.server.sendMessage(netMessage);
                    sendToMobile(newMobileFullConfiguration);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        G.printStackTrace(e);
                    }
                    startServer();
                } else {
                    Database.Mobiles.delete(newMobile.iD);
                }
            }
        } catch (JSONException e) {
            G.printStackTrace(e);
            //TODO: progress is cancelled ...
        }
    }

    private boolean canSendToMobiles() {

        if (socket.isClosed()) {
            G.log("Socket to new mobile is closed");
            return false;
        }
        try {
            if (socket.getOutputStream() == null)
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean sendToMobile(String data) {
        G.log("Sending data to new mobile :\r\n" + data);
        if (socket.isClosed()) {
            G.log("Socket to new mobile is closed");
            return false;
        }

        try {
//            socket.getOutputStream().write(data.getBytes("UTF-8"));
            socket.getOutputStream().write((data).getBytes());
        } catch (IOException e) {
            G.printStackTrace(e);
            return false;
        }
        return true;
    }

    private void hideQR() {
        G.HANDLER.post(new Runnable() {

            @Override
            public void run() {
                fo.imgQR.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void loadMarkets() {
        ModuleWebservice mw = new ModuleWebservice();
        mw.addParameter("rt", "GetMarket");
        mw.addParameter("CustomerID", "" + G.setting.customerID);
        mw.addParameter("ExKey", G.setting.exKey);
        mw.addParameter("CenterVer", G.setting.ver);
        mw.setListener(new WebServiceListener() {

            @Override
            public void onFail(int statusCode) {
                G.log("Failed to load markets !");
                new DialogClass(G.currentActivity).showOk(G.T.getSentence(754), G.T.getSentence(769));
            }

            @Override
            public void onDataReceive(String data, boolean cached) {
                try {
                    JSONArray ja = new JSONArray(data);
                    final int countMarketItem = ja.length();
                    if (countMarketItem > 0) {
                        ArrayList<Market> markets = new ArrayList<Market>();
                        for (int i = 0; i < ja.length(); i++) {
                            G.log("i:" + i);
                            JSONObject jo = ja.getJSONObject(i);
                            final Market m = new Market();
                            m.name = jo.getString("Name");
                            m.url = jo.getString("AppUrl");
                            m.imgUrl = jo.getString("IconUrl");
                            markets.add(m);

                            G.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    View v = LayoutInflater.from(G.context).inflate(R.layout.list_market_item, fo.layMarkets, false);
                                    TextView txtMarketName = (TextView) v.findViewById(R.id.txtMarket);
                                    ImageView imgMarket = (ImageView) v.findViewById(R.id.imgMarket);
                                    txtMarketName.setText("" + m.name);
                                    Picasso.with(G.context).load(m.imgUrl).into(imgMarket);
                                    v.setLayoutParams(lparams);
                                    fo.layMarkets.addView(v);

                                    v.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Dialog dialog = new Dialog(G.currentActivity);
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.setContentView(R.layout.dialog_download);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                            dialog.setCancelable(true);
                                            TextView txtName = (TextView) dialog.findViewById(R.id.txtName);
                                            TextView txtDescription = (TextView) dialog.findViewById(R.id.txtDescription);
                                            ImageView imgQR = (ImageView) dialog.findViewById(R.id.imgQR);
                                            txtName.setText("" + m.name);
                                            txtDescription.setText(m.url);
                                            Picasso.with(G.context).load(m.imgUrl).into(imgQR);
                                            imgQR.setImageBitmap(Utility.makeNewQR(m.imgUrl));
                                            dialog.show();
                                        }
                                    });
                                }
                            });
                        }
//                        adapterMarkets = new AdapterListViewMarket(markets);
                        fo.txtTitleMarket.setVisibility(View.VISIBLE);
//                        fo.listMarket.setAdapter(adapterMarkets);
//                        G.HANDLER.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                fo.txtTitleMarket.setVisibility(View.VISIBLE);
//                                fo.listMarket.setAdapter(adapterMarkets);
//                                if (countMarketItem < 6) {
//
//                                    LayoutParams lp = new LayoutParams(countMarketItem * 150, LayoutParams.WRAP_CONTENT);
//                                    fo.listMarket.setLayoutParams(lp);
//                                }
//                                else
//                                {
//                                    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//                                    fo.listMarket.setLayoutParams(lp);
//                                }
//
//                            }
//                        });
                    }
                } catch (Exception e) {
                    G.printStackTrace(e);
                }

            }
        });
        mw.read();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeNewQR();
        startServer();
    }

    private void makeNewQR() {
        Random r = new Random();
        int aNumber = r.nextInt(1000); //+1 if high is inclusive
        newExKey = Utility.getIdHash(aNumber);
        String qrText = "{\"CenterIP\":\"" + G.networkSetting.mainIPAddress + "\",\"CenterPort\":" + G.DEFAULT_MOBILE_NEW_GET_PORT + ",\"ExKey\":\"" + newExKey + "\"}";
        final Bitmap b = Utility.makeNewQR(qrText);
        G.HANDLER.post(new Runnable() {
            @Override
            public void run() {
                fo.imgQR.setImageBitmap(b);
                fo.imgQR.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            /*************** Jahanbin **************/
            if (mobilesSocketListener != null)
            /*************** Jahanbin **************/
                mobilesSocketListener.close();
        } catch (IOException e) {
            G.printStackTrace(e);
        }
    }


}
