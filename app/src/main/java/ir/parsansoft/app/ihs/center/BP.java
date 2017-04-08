package ir.parsansoft.app.ihs.center;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.Calendar;
import java.util.List;

public class BP {
    private Timer      myTimer   = null;
    private Weather    myWeather = null;
    private WifiSignal myWifi    = null;

    public BP() {
        G.log("BP created");
        runAllThreads();
    }

    public void runAllThreads() {
        G.log("runAllThreads Started");
        if (myTimer == null)
            myTimer = new Timer();
        myTimer.start();
        if (myWeather == null)
            myWeather = new Weather();
        myWeather.start();

        if (myWifi == null)
            myWifi = new WifiSignal();
        myWifi.start();
    }
    public void refreshUI() {
        myTimer.refreshTimeUI();
        refreshAlarmUI();
        myWeather.refreshUI();
        myWifi.refreshUI();
    }
    public void restartWeatherAPI() {
        if (myWeather != null) {
//            myWeather.stop();
            myWeather.start();
        }
    }

    class WifiSignal {
        private int signalStrangth = 0;

        public void start() {
            doJob();
        }
        private void doJob() {

            final IntentFilter filters = new IntentFilter();
            filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            filters.addAction("android.net.wifi.STATE_CHANGE");
            G.context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager conMngr = (ConnectivityManager) G.context.getSystemService(G.context.CONNECTIVITY_SERVICE);
                    NetworkInfo wifi = conMngr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if ( !wifi.isConnected()) {
                        signalStrangth = -1;
                        refreshUI();

                    }
                }
            }, filters);

            G.context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    final WifiManager wifi = (WifiManager) G.context.getSystemService(Context.WIFI_SERVICE);
                    List<ScanResult> results = wifi.getScanResults();
                    for (ScanResult result: results) {
                        if (result.BSSID.equals(wifi.getConnectionInfo().getBSSID())) {
                            int level = WifiManager.calculateSignalLevel(wifi.getConnectionInfo().getRssi(), result.level);
                            int difference = level * 100 / result.level;
                            if (difference >= 100)
                                signalStrangth = 4;
                            else if (difference >= 75)
                                signalStrangth = 3;
                            else if (difference >= 50)
                                signalStrangth = 2;
                            else if (difference >= 25)
                                signalStrangth = 1;
                            else if (difference >= 0)
                                signalStrangth = 0;
                            //toast("\nSignal Strong :" + difference + "\nSignal State:" + signalStrangth + "\n Connection Name : " + wifi.getConnectionInfo().getSSID());
                            refreshUI();
                        }
                    }
                }
            }, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        }
        public void refreshUI() {
            if (G.ui != null)
                G.ui.runOnWifiSignalChanged(signalStrangth);
        }
    }

    class Timer {
        private boolean isRunning = false;
        private String  hs;
        private String  ms;
        private int     alarmcount;
        int             lastMin   = 0;

        //        private String  ss;

        public void start() {
            if ( !isRunning) {
                G.log("Timer Started...");
                doJob();
            }
        }
        private void doJob() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isRunning = true;
                    while (isRunning) {
                        Calendar c = Calendar.getInstance();
                        int h = c.get(Calendar.HOUR_OF_DAY);
                        int m = c.get(Calendar.MINUTE);
                        int s = c.get(Calendar.SECOND);
                        if (h > 9)
                            hs = "" + h;
                        else
                            hs = "0" + h;
                        if (m > 9)
                            ms = "" + m;
                        else
                            ms = "0" + m;
                        //                        if (s > 9)
                        //                            ss = "" + s;
                        //                        else
                        //                            ss = "0" + s;                     
                        refreshTimeUI();

                        if (lastMin != m && lastMin % G.DEFAULT_MINUTES_CHECK_NODE_EXIST == 0) // repeats every 1 minutes
                        //if (lastMin != m) // repeats every 1 minutes 
                        {
                            lastMin = m;
                            if (G.nodeCommunication != null) {
                                G.log("Check if all nodes are available?");
                                G.nodeCommunication.checkAllNodes();
                            }
                        }

                        try {
                            Thread.sleep(G.DEFAULT_TIMER_REFRESH);
                        }
                        catch (InterruptedException e) {
                            G.log("Error Timer BP:" + e.getMessage());
                        }
                    }
                }
            }).start();
        }
        public void refreshTimeUI() {
            if (G.ui != null)
                G.ui.runOnTimeChanged(hs + ":" + ms); //+ ":" + ss);
        }


    }

    public int getCurrentTemperature() {
        if (myWeather != null)
            return myWeather.getCurrentTemperature();
        else
            return 0;
    }
    public int getCurrentWeatherCode() {
        if (myWeather != null)
            return myWeather.getCurrentWeatherCode();
        else
            return 0;
    }

    public void refreshAlarmUI() {
        if (G.ui != null) {
            Database.Node.Struct[] dNodes = Database.Node.select("Status = 0");
            if (dNodes == null) {
                G.log("Alarm counts : 0");
                G.ui.runOnAlarmChanged(0);
            }
            else {
                G.log("Alarm counts :" + dNodes.length);
                G.ui.runOnAlarmChanged(dNodes.length);
            }
        }
    }
}
