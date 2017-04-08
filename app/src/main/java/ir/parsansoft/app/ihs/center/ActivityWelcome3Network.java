package ir.parsansoft.app.ihs.center;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_setting_network;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.DialogClass.OkListener;
import ir.parsansoft.app.ihs.center.Utility.IPAddress.ipV4Parameters;
import ir.parsansoft.app.ihs.center.adapters.AdapterWIFIList;
import ir.parsansoft.app.ihs.center.adapters.AdapterWIFIList.WiFiSelect;

public class ActivityWelcome3Network extends ActivityWizard {

    CO_f_setting_network fo;
    List<WifiClass>      wifiList          = new ArrayList<WifiClass>();
    List<ScanResult>     scanList;
    AdapterWIFIList      adapter;
    boolean              scanContinues;
    WifiManager          wifiManager;

    boolean              isBusy            = false;
    boolean              configureWiFiDone = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.f_welcome_network);


        fo = new CO_f_setting_network(this);
        translateForm();
        fo.layIP.setVisibility(View.INVISIBLE);
        adapter = new AdapterWIFIList(this, R.layout.l_setting_wifi_signal, wifiList);
        fo.lstWiFi.setAdapter(adapter);
        scanContinues = true;
        registerWiFiBrodcast();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (scanContinues) {
                    try {
                        wifiManager.startScan();
                        //G.log("Wifi scan started...");
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e) {
                        G.printStackTrace(e);
                    }
                }
            }
        }).start();
        // Set wifi settings
        // http://stackoverflow.com/questions/10278461/how-to-configue-a-static-ip-address-netmask-gateway-programmatically-on-androi
        adapter.setOnWiFiSelector(new WiFiSelect() {
            @Override
            public void onWiFiSelected(WifiClass sekectedWiFi) {
                connectToNetwork(sekectedWiFi);
            }
        });

        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !isBusy) {
                    isBusy = true;
                    startActivity(new Intent(ActivityWelcome3Network.this, ActivityWelcome2Screen.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        setOnNextListenner(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !isBusy) {
                    if (Utility.isNetworkAvailable() && configureWiFiDone) {
                        isBusy = true;
                        startActivity(new Intent(ActivityWelcome3Network.this, ActivityWelcome4Location.class));
                        Animation.doAnimation(Animation_Types.FADE);
                        finish();
                    }
                    else
                    {
                        final DialogClass dlg = new DialogClass(G.currentActivity);
                        dlg.setOnOkListener(new OkListener() {
                            @Override
                            public void okClick() {
                                dlg.dissmiss();
                            }
                        });
                        dlg.showOk(G.T.getSentence(760), G.T.getSentence(749));
                    }
                }
            }
        });
    }

    private void connectToNetwork(final WifiClass wifi) {
        if (isBusy)
            return;
        isBusy = true;
        if (wifi.type.contains("WPA")) {
            final Dialog dialog = new Dialog(ActivityWelcome3Network.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.d_connect_wifi);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            final AllViews.CO_d_simple_edit_text dlg = new AllViews.CO_d_simple_edit_text(dialog);
//            dlg.editText1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            dlg.txtTitle.setText(G.T.getSentence(710) + wifi.ssid);
            dlg.txtBody.setText(G.T.getSentence(711));
            dlg.btnPositive.setText(G.T.getSentence(705));
            dlg.btnNegative.setText(G.T.getSentence(102));
            dlg.btnPositive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dlg.editText1.getText().toString().length() < 8) {
                        G.log("Connecting to wifi " + wifi.ssid);
                        fo.txtSSIDName.setText(wifi.ssid);
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(719), G.T.getSentence(730));
                        return;
                    }
                    isBusy = true;
                    configureWiFiDone = false;
                    Utility.forgetWifiNetworks();
                    dialog.dismiss();
                    final Utility.MyWiFiManager myWM = new Utility.MyWiFiManager();
                    myWM.connectToAP(wifi.ssid, dlg.editText1.getText().toString());

                    boolean connected = false;
                    long startTime = System.currentTimeMillis();
                    while (startTime + 15000 > System.currentTimeMillis()) {
                        if (Utility.isNetworkAvailable())
                            break;
                        try {
                            Thread.sleep(150);
                            Thread.yield();
                        }
                        catch (Exception e) {}
                    }
                    // if connecting to this network is true
                    if (Utility.isNetworkAvailable()) {
                        wifi.psk = dlg.editText1.getText().toString();
                        fillRecomendedIP(wifi);
                        fo.btnSave.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                configureNewIP(wifi);
                            }
                        });
                    } else {
                        G.toast("Wifi not connected !");
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(719), G.T.getSentence(720));
                        myWM.connectToMainAP();
                    }
                    isBusy = false;
                }
            });
            dlg.btnNegative.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            isBusy = false;
        }
        isBusy = false;
    }


    @Override
    protected void onPause() {
        scanContinues = false;
        super.onPause();
    };

    private void fillRecomendedIP(final WifiClass wifi) {
        final DialogClass dlgc = new DialogClass(G.currentActivity);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dlgc.setDialogText(G.T.getSentence(728));
                dlgc.setDialogTitle(G.T.getSentence(725));
                dlgc.showWaite();
                int c = 10;
                boolean newIpFound = false;
                while (c > 0) {
                    c--;
                    G.log("Try " + (10 - c) + " to fill ip addresses");
                    try {
                        Thread.sleep(2000);
                    }
                    catch (Exception e) {}
                    final ipV4Parameters mydhcpParameters;
                    mydhcpParameters = Utility.IPAddress.getDhcpIpV4Parameters();
                    if (mydhcpParameters != null)
                    {
                        newIpFound = true;
                        G.log("found DHCP IP Address ...\nIP :" + mydhcpParameters.ipAddress + "\nSubnet:" + mydhcpParameters.subnetMask + "\nGateway:" + mydhcpParameters.defaultGateway + "\nDNS:" + mydhcpParameters.dns1);
                        G.HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                fo.layIP.setVisibility(View.VISIBLE);
                                fo.txtSSIDName.setText(wifi.ssid);
                                fo.edtIP.setText(mydhcpParameters.ipAddress);
                                fo.edtSubnet.setText(mydhcpParameters.subnetMask);
                                fo.edtGateway.setText(mydhcpParameters.defaultGateway);
                                fo.edtDNS1.setText(mydhcpParameters.dns1);
                                //            fo.edtDNS2.setText(mydhcpParameters.dns2);
                            }
                        });
                        break;
                    }
                }

                if ( !newIpFound)
                {
                    G.log("DHCP info could not find any ip ...\nTry an other way ...");
                    final String staticIP = Utility.IPAddress.getIPv4Address();
                    final String staticSubnet = Utility.IPAddress.getSubnetMask();
                    if (staticIP.length() > 0 && staticSubnet.length() > 0) {
                        newIpFound = true;
                        G.log("found STATIC IP Address ...\nIP :" + staticIP + "\nSubnet:" + staticSubnet);
                        G.HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                fo.layIP.setVisibility(View.VISIBLE);
                                fo.txtSSIDName.setText(wifi.ssid);
                                fo.edtIP.setText(staticIP);
                                fo.edtSubnet.setText(staticSubnet);
                                long g = Utility.IPAddress.ipToInt(staticIP);
                                g = g - (g % 256) + 1;
                                String gS = Utility.IPAddress.intToIP(g);
                                fo.edtGateway.setText(gS);
                            }
                        });
                    }
                }
                dlgc.dissmiss();
                if ( !newIpFound) {
                    G.log("could not find any ip ...\n");
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(728), G.T.getSentence(729));
                    G.HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            fo.layIP.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }).start();
    }

    private void configureNewIP(WifiClass wifi) {
        isBusy = true;
        ipV4Parameters newIpParams = new ipV4Parameters();
        newIpParams.ipAddress = fo.edtIP.getText().toString().trim();
        newIpParams.subnetMask = fo.edtSubnet.getText().toString().trim();
        newIpParams.defaultGateway = fo.edtGateway.getText().toString().trim();
        newIpParams.dns1 = fo.edtDNS1.getText().toString().trim();
        int checkIpParamsCorrect = newIpParams.checkIfParamsAreCorrect();
        if (checkIpParamsCorrect > 0) {
            if (Utility.IPAddress.setStaticIpV4Address(newIpParams)) {
                G.networkSetting.mainIPAddress = newIpParams.ipAddress;
                G.networkSetting.mainSubnetMask = newIpParams.subnetMask;
                G.networkSetting.mainDefaultGateway = newIpParams.defaultGateway;
                G.networkSetting.mainDNS1 = newIpParams.dns1;
                G.networkSetting.wiFiSSID = wifi.ssid;
                G.networkSetting.wiFiMac = wifi.mac;
                G.networkSetting.wiFiSecurityKey = wifi.psk;
                G.networkSetting.wiFiSecurityType = wifi.type;

                long networkIP = Utility.IPAddress.ipToInt(newIpParams.ipAddress);
                networkIP = networkIP - (networkIP % 256);

                G.networkSetting.nodeIPsStart = Utility.IPAddress.intToIP(networkIP + 125);
                G.networkSetting.nodeIPsEnd = Utility.IPAddress.intToIP(networkIP + 225);

                Database.NetworkSetting.edit(G.networkSetting);
                fo.layIP.setVisibility(View.INVISIBLE);
                new DialogClass(G.currentActivity).showOk(G.T.getSentence(725), G.T.getSentence(726));
                configureWiFiDone = true;
                //TODO: Send new IP configuration to the server
            }
            else {
                new DialogClass(G.currentActivity).showOk(G.T.getSentence(725), G.T.getSentence(727));
            }
        }
        else
        {
            String ErrMsg = "";
            switch (checkIpParamsCorrect) {
                case -1:
                    ErrMsg = G.T.getSentence(721);
                    break;
                case -2:
                    ErrMsg = G.T.getSentence(722);
                    break;
                case -3:
                    ErrMsg = G.T.getSentence(723);
                    break;
                case -4:
                    ErrMsg = G.T.getSentence(724);
                    break;
                default:
                    break;
            }
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(725), ErrMsg);
        }
        isBusy = false;
    }
    BroadcastReceiver wifiListReceiver;
    private void registerWiFiBrodcast() {
        try {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            wifiListReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //G.log("Broadcast wifi started...");
                    scanList = wifiManager.getScanResults();
                    wifiList.clear();
                    for (ScanResult result: scanList) {
                        double percentage = 0;
                        percentage = WifiManager.calculateSignalLevel(result.level, 100);
                        wifiList.add(new WifiClass(result.SSID, result.BSSID, result.capabilities, (int) percentage));
                    }
                    adapter.notifyDataSetChanged();
                }
            };
            registerReceiver(wifiListReceiver, filter);
        }
        catch (Exception e) {
            G.printStackTrace(e);
        }
    }

    @Override
    protected void onStop()
    {
        try {
            unregisterReceiver(wifiListReceiver);
        }
        catch (Exception e) {}
        super.onStop();
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.txtSubnet.setText(G.T.getSentence(707));
        fo.txtIpAddress.setText(G.T.getSentence(706));
        fo.txtGateway.setText(G.T.getSentence(708));
        fo.txtDNS1.setText(G.T.getSentence(709));
        fo.btnSave.setText(G.T.getSentence(123));
    }
    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }
}
