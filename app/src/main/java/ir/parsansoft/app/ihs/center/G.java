package ir.parsansoft.app.ihs.center;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;
import ir.parsansoft.app.ihs.center.SysLog.LogType;
import ir.parsansoft.app.ihs.center.Utility.MyWiFiManager;
import ir.parsansoft.app.ihs.center.components.AssetsManager;


public class G extends Application {

    public static Context context;
    public static LayoutInflater inflater;
    public static SharedPreferences preferences;
    public static Activity currentActivity;
    public static String currentClassName;
    public static AlarmManager alarmManager;
    public static SQLiteDatabase dbObject;
    public static Server server;
    public static ServiceNodeCommunication nodeCommunication;
    public static ServiceServerCommunication serverCommunication;
    public static ServiceMobilesCommunication mobileCommunication;
    public static Database.Setting.Struct setting;
    public static Database.NetworkSetting.Struct networkSetting;
    public static FragmentManager fragmentManager;
    public static UI ui;
    public static BP bp;
    public static Translation T;
    public static ScenarioBP scenarioBP;
    public static BroadcastReciverAlarmManager alarmScenario;
    public static SoundPool sp;
    public static int soundHover;
    public static Database.User.Struct currentUser = null;
    public static Firebase firebaseNotifier;

    //    public static final String                   DIR_ASSETS_IMAGES                       = "my_images";
    //    public static final String                   DIR_ASSETS_NODE_IMAGES                  = DIR_ASSETS_IMAGES + "/icon/node";
    //    public static final String                   DIR_ASSETS_WEATHER_IMAGES               = DIR_ASSETS_IMAGES + "/icon/weather";
    public static String DIR_SDCARD;
    public static String DIR_APP;
    public static String DIR_DATABASE;
    public static String DATABASE_FILE_PATH;
    public static String DIR_CACHE;
    public static String DIR_UPDATE;
    public static String DIR_ICONS;
    public static String DIR_HELP;
    public static String DIR_ICONS_SECTIONS;
    public static String DIR_ICONS_ROOMS;
    public static String DIR_ICONS_NODES;
    public static String DIR_ICONS_FLAG;
    public static String DIR_ICONS_UI;
    public static String DIR_ICONS_WEATHER;
    public static final String UPDATE_FILE_NAME = "UPDATE_CENTER.apk";

    public static String URL_Webservice;
    public static String URL_Weather_Webservice;
    public static String URL_Map_Webservice;
    public static String URL_Sms_Webservice;

    public static final int DEFAULT_LANGUAGE_ID = 1;
    public static final Handler HANDLER = new Handler();


    public static final int DEFAULT_MINUTES_CHECK_NODE_EXIST = 1;
    public static final int DEFAULT_NODE_SOCKET_TIMEOUT = 5000;
    public static final int DEFAULT_NODE_SEND_PORT = 54123;
    public static final int DEFAULT_NODE_GET_PORT = 54123;
    public static final int DEFAULT_NODE_LEARN_PORT = 54123;
    public static final int DEFAULT_MOBILE_GET_PORT = 54127;
    public static final int DEFAULT_TIMER_REFRESH = 5 * 1000;
    public static final int DEFAULT_MOBILE_NEW_GET_PORT = 54128;
    public static final int DEFAULT_SERVER_CONNECTION_RETRY_TIMEOUT = 10 * 1000;
    public static final int DEFAULT_SERVER_SOCKET_TIMEOUT = 8000;
    public static final int DEFAULT_ALARM_LATENCY_TIME = 10 * 1000;          // in milliseconds
    public static final int MAX_MESSAGE_DIFFERENCE_TO_REFRESH = 50;                 // in milliseconds
    public static List<Socket> IOModuleSocket;
    public Intent serviceServerConnection;
    public static final String SCENARIO_START_SMS_KEYWORD = "*start";
    public static final String SCENARIO_ENABLE_SMS_KEYWORD = "*on";
    public static final String SCENARIO_DISABLE_SMS_KEYWORD = "*off";
    public static final int WEATHERARARMCODE = 110011;
    public static final String FirebaseAPIURL = "https://fcm.googleapis.com/fcm/send";
    public static final String FirebaseAPIKEY = "AIzaSyApgExV9B9BdzWlEJcS15f1MpxHZt_oO_0";


    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        log("Initializing G class ...");

        initializeVariables();
        /*************************************************** Mobarraei ***********************************************************/
        AssetsManager.copyDatabase();
        //Settings.Global.putInt(getContentResolver(), Global.TRANSITION_ANIMATION_SCALE, 1);
        /*************************************************** Mobarraei ***********************************************************/
        Database.InitializeDB();
        SysLog.log("Program Start.", LogType.SYSTEM_EVENTS, LogOperator.SYSYTEM, 0);
        networkSetting = Database.NetworkSetting.select("")[0];
        setting = Database.Setting.select("")[0];
        T = new Translation();
        MyWiFiManager mwm = new MyWiFiManager();
        mwm.connectToMainAP();
        FontsManager.changeFont();
        /********************************/

        //---------Start Background Services-----------
        //        serviceServerConnection = new Intent(context, ServiceServerConnection.class);
        //        context.startService(serviceServerConnection);
        //---------------------------------------------
        configureURLs();
        nodeCommunication = new ServiceNodeCommunication();
        mobileCommunication = new ServiceMobilesCommunication();
        firebaseNotifier = new Firebase();
        ui = new UI();
        scenarioBP = new ScenarioBP();
        G.bp = new BP();
        new BroadcastReceiverWeather().setRepeatAlarm(WEATHERARARMCODE, System.currentTimeMillis(), AlarmManager.INTERVAL_HALF_HOUR);
        IOModuleSocket = new ArrayList<>();
    }

    private void initializeVariables() {

        context = this.getApplicationContext();

        DIR_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
        DIR_APP = "/data/data/" + context.getPackageName(); // = DIR_SDCARD + "/ihs";
        DIR_DATABASE = DIR_APP + "/db";
        DATABASE_FILE_PATH = DIR_DATABASE + "/" + "centerdb.sqlite";
        DIR_CACHE = DIR_APP + "/cache";
        DIR_UPDATE = DIR_SDCARD + "/Update";
        DIR_HELP = "my_images/help";
        DIR_ICONS = "my_images/icon";
        DIR_ICONS_SECTIONS = DIR_ICONS + "/section";
        DIR_ICONS_ROOMS = DIR_ICONS + "/room";
        DIR_ICONS_NODES = DIR_ICONS + "/node";
        DIR_ICONS_FLAG = DIR_ICONS + "/flag";
        DIR_ICONS_UI = DIR_ICONS + "/ui";
        DIR_ICONS_WEATHER = DIR_ICONS + "/weather";

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundHover = sp.load(this, R.raw.hover_sound, 1);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmScenario = new BroadcastReciverAlarmManager();
    }

    public final static void log(String logText) {
        if (logText != null) {
            String className = "";
            if (G.currentActivity != null)
                className = " - " + G.currentActivity.getLocalClassName();
            Log.i("LOG" + className, logText);
        }
    }

    public final static void log(String tag, String logText) {
        if (logText != null && tag != null)
            Log.i(tag, logText);
    }

    public final static void printStackTrace(Exception exception) {
//        toast(exception.getMessage());
        G.log("** " + exception.getMessage());
        exception.printStackTrace();
    }

    public final static void toast(final String text) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * tabdil address url mesle www.google.com be ip
     */
    public static String getServerIP() {
        InetAddress address;
        String ip = G.setting.serverSocketIP; // "5.144.128.181";
        try {
            address = InetAddress.getByName(new URL("http://" + G.setting.serverURL).getHost());
            ip = address.getHostAddress();
            if (!G.setting.serverSocketIP.equals(ip)) {
                G.setting.serverSocketIP = ip;
                Database.Setting.edit(G.setting);
            }
        } catch (UnknownHostException e) {
            G.printStackTrace(e);
        } catch (MalformedURLException e) {
            G.printStackTrace(e);
        }
//        return "192.168.1.35";//ip;
        return ip;
    }

    public static int getServerPort() {
        return G.setting.serverSocketPort;
    }

    public static void configureURLs() {
        URL_Webservice = "http://service." + G.setting.serverURL + "/service.aspx";
        URL_Weather_Webservice = "http://service." + G.setting.serverURL + "/service.aspx";
        URL_Map_Webservice = "http://service." + G.setting.serverURL + "/map/";
        URL_Sms_Webservice = "http://user." + G.setting.serverURL + "/[LANG]/service/sms?customerid=[CUSTOMERID]&exkey=[EXKEY]";

//                URL_Webservice = "http://192.168.1.35:205/service.aspx";
//                URL_Weather_Webservice = "http://192.168.1.111:200/service.aspx";
//                URL_Map_Webservice = "http://192.168.1.111:200/map/";
//                URL_Sms_Webservice = "http://192.168.1.111:200/[LANG]/service/sms?customerid=[CUSTOMERID]&exkey=[EXKEY]";
    }

    public static int getIconResource(String name) {
        return G.context.getResources().getIdentifier(name, "drawable", G.context.getPackageName());
    }

    public static void playHover() {
        sp.play(soundHover, 1, 1, 0, 0, 1);
    }

    public static Socket findSocketOfIoModuleNode(Database.Node.Struct node) {
        for (int i = 0; i < G.IOModuleSocket.size(); i++) {
            try {
                String ipAddress = G.IOModuleSocket.get(i).getInetAddress().toString();
                if (ipAddress.equals("/" + node.iP)) {
                    if (!G.IOModuleSocket.get(i).isConnected() || G.IOModuleSocket.get(i).isClosed()) {
                        G.IOModuleSocket.get(i).close();

                        /// اگر سوکت قطع شده بود سوکت را از لیست حذف  میکنیم و دوباره سوکت میزنیم
                        G.IOModuleSocket.remove(G.IOModuleSocket.get(i));
                        Socket socket = new Socket();
//                            G.IOModuleSocket.get(i).connect(new InetSocketAddress(ipAddress, G.DEFAULT_NODE_SEND_PORT), G.DEFAULT_NODE_SOCKET_TIMEOUT);

                        /// 5 بار تلاش میکنیم تا متصل شویم
                        boolean loop = socket.isConnected();
                        int k = 0;
                        while (!loop) {
                            k++;
                            socket.connect(new InetSocketAddress(ipAddress, G.DEFAULT_NODE_SEND_PORT),500);
                            Thread.sleep(500);
                            if (socket.isConnected() || k == 5) {
                                loop = true;
                            }
                        }
                        if (socket.isConnected()) {
                            /// سوکت جدید را به لیست اضاف میکنیم
                            G.IOModuleSocket.add(socket);
                            return socket;
                        }
                    }
                    return G.IOModuleSocket.get(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    //    public static String getSentence(int sentenceID) {
    //        Database.Translation.Struct[] sentence;
    //        sentence = Database.Translation.select("langID=" + G.setting.languageID + " AND SentenseID=" + sentenceID);
    //        if (sentence == null)
    //        {
    //            sentence = Database.Translation.select("langID=" + DEFAULT_LANGUAGE_ID + " AND SentenseID=" + sentenceID);
    //            if (sentence == null)
    //                return "NT:" + sentenceID;
    //        }
    //        return sentence[0].sentenseText;
    //    }


    public final static Bitmap getRoomIconBitmap(String icon) {
        File f = new File(DIR_ICONS_ROOMS + "/" + icon);
        if (f.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            return myBitmap;
        }
        return null;
    }

    public static boolean isRtL() {
        return false;
    }

    public final static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            G.printStackTrace(e);
        }
        return "";
    }

    public final static String inputstreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            G.printStackTrace(e);
        }
        return "";
    }

    public static int getResId(String resName) {
        int resID = 0;
        try {
            resID = G.context.getResources().getIdentifier(resName, "drawable", G.context.getPackageName());
        } catch (Exception e) {
            G.printStackTrace(e);
        }
        return resID;
    }

    public static String getHttpPacket(String rawData, String clientIP, int portnumber) {
        return "GET /" + rawData + " HTTP/1.1\r\nHost: " + clientIP + ":" + portnumber + "\r\n\r\n";
    }
}
