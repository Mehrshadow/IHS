package ir.parsansoft.app.ihs.center;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Database {

    public static void InitializeDB() {
        /*try {

        	String myDBFILE = G.DIR_DATABASE_FILE;
        	G.dbObject = SQLiteDatabase.openOrCreateDatabase(myDBFILE, null);

        } catch (Exception e) {
        	G.printStackTrace(e);
        }*/
        try {
            //            String myDBFILE = G.DATABASE_FILE_PATH;
            //            File myDbFile = new File(myDBFILE);
            //            if ( !myDbFile.exists())
            //            {
            //                File myDbFolder = new File(G.DIR_DATABASE);
            //                myDbFolder.mkdirs();
            //                myDbFile.createNewFile();
            //            }
            G.dbObject = SQLiteDatabase.openOrCreateDatabase(G.DATABASE_FILE_PATH, null);

        } catch (Exception e) {
            G.log("Database could not open!");
            G.printStackTrace(e);
        }
    }

    public static String generateNewMobileConfiguration(Mobiles.Struct newMobile) {

        //  Generate Json of :
        //        Setting ...
        //        All rooms
        //        All Sections
        //        All Nodes
        //        All Switches
        //        All Scenarios

        Section.Struct[] sections = Section.select("");
        JSONArray sectionsJSON = new JSONArray();
        if (sections != null)
            for (int i = 0; i < sections.length; i++) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("ID", sections[i].iD);
                    jo.put("Sort", sections[i].sort);
                    jo.put("Icon", sections[i].icon);
                    jo.put("Name", sections[i].name);
                    sectionsJSON.put(jo);
                } catch (JSONException e) {
                    G.printStackTrace(e);
                    G.log("JSON Creation of Sections faild ....");
                    sectionsJSON = null;
                    break;
                }
            }
        Room.Struct[] rooms = Room.select("");
        JSONArray roomsJSON = new JSONArray();
        if (rooms != null)
            for (int i = 0; i < rooms.length; i++) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("ID", rooms[i].iD);
                    jo.put("Sort", rooms[i].sort);
                    jo.put("Icon", rooms[i].icon);
                    jo.put("Name", rooms[i].name);
                    jo.put("SectionID", rooms[i].sectionID);
                    roomsJSON.put(jo);
                } catch (JSONException e) {
                    G.printStackTrace(e);
                    G.log("JSON Creation of Rooms faild ....");
                    roomsJSON = null;
                    break;
                }
            }
        Node.Struct[] nodes = Node.select("");
        JSONArray nodesJSON = new JSONArray();
        if (nodes != null)
            for (int i = 0; i < nodes.length; i++) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("ID", nodes[i].iD);
                    jo.put("IsFavorite", nodes[i].isFavorite);
                    jo.put("NodeType", nodes[i].nodeTypeID);
                    jo.put("RoomID", nodes[i].roomID);
                    jo.put("Status", nodes[i].status);
                    jo.put("Icon", nodes[i].icon);
                    jo.put("Name", nodes[i].name);
                    jo.put("RegisterDate", nodes[i].regDate);
                    jo.put("SerialNumber", nodes[i].serialNumber);
                    nodesJSON.put(jo);
                } catch (JSONException e) {
                    G.printStackTrace(e);
                    G.log("JSON Creation of Nodes faild ....");
                    nodesJSON = null;
                    break;
                }
            }
        Switch.Struct[] switches = Switch.select("");
        JSONArray switchesSON = new JSONArray();
        if (switches != null)
            for (int i = 0; i < switches.length; i++) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("ID", switches[i].iD);
                    jo.put("NodeID", switches[i].nodeID);
                    jo.put("SwitchType", switches[i].switchType);
                    jo.put("Code", switches[i].code);
                    jo.put("Name", switches[i].name);
                    jo.put("Value", switches[i].value);
                    switchesSON.put(jo);
                } catch (JSONException e) {
                    G.printStackTrace(e);
                    G.log("JSON Creation of Switches faild ....");
                    switchesSON = null;
                    break;
                }
            }
        Scenario.Struct[] scenarios = Scenario.select("");
        JSONArray scenariosJSON = new JSONArray();
        if (scenarios != null)
            for (int i = 0; i < scenarios.length; i++) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("ID", scenarios[i].iD);
                    boolean hasActivePre, hasActiveResult;
                    hasActivePre = scenarios[i].opTimeBase || scenarios[i].opPreGPS || scenarios[i].opPreSwitchBase || scenarios[i].opPreTemperature || scenarios[i].opPreWeather;
                    hasActiveResult = scenarios[i].opResultNotify || scenarios[i].opResultSMS || scenarios[i].opResultSwitch;
                    if (scenarios[i].active)
                        jo.put("Active", 1);
                    else if (hasActivePre && hasActiveResult)
                        jo.put("Active", 0);
                    else
                        jo.put("Active", -1);
                    //jo.put("Active", scenarios[i].active);
                    jo.put("IsStarted", scenarios[i].isStarted);
                    if (scenarios[i].opPreGPS)
                        jo.put("GPS_Params", scenarios[i].gPS_Params);
                    else
                        jo.put("GPS_Params", "");
                    jo.put("Name", scenarios[i].name);
                    jo.put("Des", scenarios[i].des);
                    jo.put("DetailsDescription", scenarios[i].getDetailsInHTML(0));
                    jo.put("DetailsConditions", scenarios[i].getDetailsInHTML(1));
                    jo.put("DetailsResults", scenarios[i].getDetailsInHTML(2));
                    jo.put("smsKey", scenarios[i].preSMSKeywords);
                    scenariosJSON.put(jo);
                } catch (JSONException e) {
                    G.printStackTrace(e);
                    G.log("JSON Creation of Scenario faild ....");
                    scenariosJSON = null;
                    break;
                }
            }

        JSONArray settingJSON = new JSONArray();
        JSONObject jo = new JSONObject();
        try {
            jo.put("CustomerID", G.setting.customerID);
            jo.put("CustomerName", G.setting.customerName);
            jo.put("ServerIP", G.setting.serverSocketIP);
//            jo.put("ServerIP", "192.168.1.35");
//            jo.put("ServerPort", G.setting.serverSocketPort);
            jo.put("ServerPort", 8089);
            jo.put("Ver", Utility.getApplicationVersionName());
            jo.put("CenterIP", G.networkSetting.mainIPAddress);
            jo.put("CenterPort", G.DEFAULT_MOBILE_GET_PORT);
            jo.put("WiFiSSID", G.networkSetting.wiFiSSID);
            jo.put("WiFiMac", G.networkSetting.wiFiMac);
            if (newMobile != null) {
                jo.put("ExKey", newMobile.exKey);
                jo.put("MobileID", newMobile.iD);
            }
            int lastmessageID = 0;
            try {
                lastmessageID = Message.getMax("ID", "").iD;
            } catch (Exception e) {
            }
            jo.put("LastMessageID", lastmessageID);
            settingJSON.put(jo);
        } catch (JSONException e) {
            G.printStackTrace(e);
            G.log("JSON Creation of Setting faild ....");
            settingJSON = null;
        }

        if (sectionsJSON != null && roomsJSON != null && nodesJSON != null && switchesSON != null && scenariosJSON != null && settingJSON != null) {
            String strConfiguration = "{"
                    + "\"Sections\":" + sectionsJSON.toString() + ","
                    + "\"Rooms\":" + roomsJSON.toString() + ","
                    + "\"Nodes\":" + nodesJSON.toString() + ","
                    + "\"Switches\":" + switchesSON.toString() + ","
                    + "\"Scenarios\":" + scenariosJSON.toString() + ","
                    + "\"Setting\":" + settingJSON.toString()
                    + "}";
            return strConfiguration;
        } else {
            return "";
        }
    }

    public static class Setting {
        public static class Struct {
            public String ver = "";
            public String serverURL = "";
            public String serverSocketIP = "";
            public int serverSocketPort = 0;
            public String user = "";
            public String pass = "";
            public String exKey = "";
            public int customerID = 0;
            public String customerName = "";
            public String regDate = "";
            public String country = "";
            public String state = "";
            public String city = "";
            public double gPSLat = 0;
            public double gPSLon = 0;
            public int displayMargin = 0;
            public int languageID = 0;
            public String guaranteeDate = "";
            public String validMobiles = "";
            /***********************
             * Jahanbin
             ***********************/
            public String weatherAPIKey = "";
            /*********************** Jahanbin***********************/
        }

        public static long insert(Struct mySetting) {
            ContentValues Values = new ContentValues();
            Values.put("ServerURL", mySetting.serverURL);
            Values.put("ServerSocketIP", mySetting.serverSocketIP);
            Values.put("ServerSocketPort", mySetting.serverSocketPort);
            Values.put("User", mySetting.user);
            Values.put("Pass", mySetting.pass);
            Values.put("ExKey", mySetting.exKey);
            Values.put("CustomerID", mySetting.customerID);
            Values.put("CustomerName", mySetting.customerName);
            Values.put("RegDate", mySetting.regDate.toString());
            Values.put("Country", mySetting.country);
            Values.put("State", mySetting.state);
            Values.put("City", mySetting.city);
            Values.put("GPSLat", mySetting.gPSLat);
            Values.put("GPSLon", mySetting.gPSLon);
            Values.put("DisplayMargin", mySetting.displayMargin);
            Values.put("LanguageID", mySetting.languageID);
            Values.put("GuaranteeDate", mySetting.guaranteeDate.toString());
            Values.put("ValidMobiles", mySetting.validMobiles);
            /*********************** Jahanbin***********************/
            Values.put("WeatherAPIKey", mySetting.weatherAPIKey);
            /*********************** Jahanbin***********************/
            return G.dbObject.insert("T_Setting", null, Values);
        }

        public static long insert(String serverURL, String serverSocketIP, int serverSocketPort, String user, String pass, String exKey, int customerID, String customerName, String regDate, String country, String state, String city, double gPSLat, double gPSLon, int displayMargin, int languageID, String guaranteeDate, String validMobiles, String weatherAPIKey) {
            ContentValues Values = new ContentValues();
            Values.put("ServerURL", serverURL);
            Values.put("ServerSocketIP", serverSocketIP);
            Values.put("ServerSocketPort", serverSocketPort);
            Values.put("User", user);
            Values.put("Pass", pass);
            Values.put("ExKey", exKey);
            Values.put("CustomerID", customerID);
            Values.put("CustomerName", customerName);
            Values.put("RegDate", regDate);
            Values.put("Country", country);
            Values.put("State", state);
            Values.put("City", city);
            Values.put("GPSLat", gPSLat);
            Values.put("GPSLon", gPSLon);
            Values.put("DisplayMargin", displayMargin);
            Values.put("LanguageID", languageID);
            Values.put("GuaranteeDate", guaranteeDate.toString());
            Values.put("ValidMobiles", validMobiles);
            /*********************** Jahanbin***********************/
            Values.put("WeatherAPIKey", weatherAPIKey);
            /*********************** Jahanbin***********************/
            return G.dbObject.insert("T_Setting", null, Values);
        }

        public static int edit(Struct mySetting) {
            ContentValues Values = new ContentValues();
            Values.put("ServerURL", mySetting.serverURL);
            Values.put("ServerSocketIP", mySetting.serverSocketIP);
            Values.put("ServerSocketPort", mySetting.serverSocketPort);
            Values.put("User", mySetting.user);
            Values.put("Pass", mySetting.pass);
            Values.put("ExKey", mySetting.exKey);
            Values.put("CustomerID", mySetting.customerID);
            Values.put("CustomerName", mySetting.customerName);
            Values.put("RegDate", mySetting.regDate.toString());
            Values.put("Country", mySetting.country);
            Values.put("State", mySetting.state);
            Values.put("City", mySetting.city);
            Values.put("GPSLat", mySetting.gPSLat);
            Values.put("GPSLon", mySetting.gPSLon);
            Values.put("DisplayMargin", mySetting.displayMargin);
            Values.put("LanguageID", mySetting.languageID);
            Values.put("GuaranteeDate", mySetting.guaranteeDate.toString());
            Values.put("ValidMobiles", mySetting.validMobiles);
            /*********************** Jahanbin***********************/
            Values.put("WeatherAPIKey", mySetting.weatherAPIKey);
            /*********************** Jahanbin***********************/
            return G.dbObject.update("T_Setting", Values, "Ver=" + mySetting.ver, null);
        }

        public static int edit(String ver, String serverURL, String serverSocketIP, int serverSocketPort, String pass, String user, String exKey, int customerID, String customerName, String regDate, String country, String state, String city, double gPSLat, double gPSLon, int displayMargin, int languageID, String guaranteeDate, String validMobiles, String weatherAPIKey) {
            ContentValues Values = new ContentValues();
            Values.put("ServerURL", serverURL);
            Values.put("ServerSocketIP", serverSocketIP);
            Values.put("ServerSocketPort", serverSocketPort);
            Values.put("User", user);
            Values.put("Pass", pass);
            Values.put("ExKey", exKey);
            Values.put("CustomerID", customerID);
            Values.put("CustomerName", customerName);
            Values.put("RegDate", regDate.toString());
            Values.put("Country", country);
            Values.put("State", state);
            Values.put("City", city);
            Values.put("GPSLat", gPSLat);
            Values.put("GPSLon", gPSLon);
            Values.put("DisplayMargin", displayMargin);
            Values.put("LanguageID", languageID);
            Values.put("GuaranteeDate", guaranteeDate.toString());
            Values.put("ValidMobiles", validMobiles);
            /*********************** Jahanbin***********************/
            Values.put("WeatherAPIKey", weatherAPIKey);
            /*********************** Jahanbin***********************/
            return G.dbObject.update("T_Setting", Values, "Ver=" + ver, null);
        }

        public static int delete(String ver) {
            return G.dbObject.delete("T_Setting", "Ver=" + ver, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Setting", "", null);
        }

        public static Struct select() {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Setting", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.ver = cursor.getString(cursor.getColumnIndex("Ver"));
                selectedRow.serverURL = cursor.getString(cursor.getColumnIndex("ServerURL"));
                selectedRow.serverSocketIP = cursor.getString(cursor.getColumnIndex("ServerSocketIP"));
                selectedRow.serverSocketPort = cursor.getInt(cursor.getColumnIndex("ServerSocketPort"));
                selectedRow.user = cursor.getString(cursor.getColumnIndex("User"));
                selectedRow.pass = cursor.getString(cursor.getColumnIndex("Pass"));
                selectedRow.exKey = cursor.getString(cursor.getColumnIndex("ExKey"));
                selectedRow.customerID = cursor.getInt(cursor.getColumnIndex("CustomerID"));
                selectedRow.customerName = cursor.getString(cursor.getColumnIndex("CustomerName"));
                selectedRow.regDate = cursor.getString(cursor.getColumnIndex("RegDate"));
                selectedRow.country = cursor.getString(cursor.getColumnIndex("Country"));
                selectedRow.state = cursor.getString(cursor.getColumnIndex("State"));
                selectedRow.city = cursor.getString(cursor.getColumnIndex("City"));
                selectedRow.gPSLat = cursor.getDouble(cursor.getColumnIndex("GPSLat"));
                selectedRow.gPSLon = cursor.getDouble(cursor.getColumnIndex("GPSLon"));
                selectedRow.displayMargin = cursor.getInt(cursor.getColumnIndex("DisplayMargin"));
                selectedRow.languageID = cursor.getInt(cursor.getColumnIndex("LanguageID"));
                selectedRow.guaranteeDate = cursor.getString(cursor.getColumnIndex("GuaranteeDate"));
                selectedRow.validMobiles = cursor.getString(cursor.getColumnIndex("ValidMobiles"));
                /*********************** Jahanbin***********************/
                selectedRow.weatherAPIKey = cursor.getString(cursor.getColumnIndex("WeatherAPIKey"));
                /*********************** Jahanbin***********************/
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Setting" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.ver = cursor.getString(cursor.getColumnIndex("Ver"));
                selectedRow.serverURL = cursor.getString(cursor.getColumnIndex("ServerURL"));
                selectedRow.serverSocketIP = cursor.getString(cursor.getColumnIndex("ServerSocketIP"));
                selectedRow.serverSocketPort = cursor.getInt(cursor.getColumnIndex("ServerSocketPort"));
                selectedRow.user = cursor.getString(cursor.getColumnIndex("User"));
                selectedRow.pass = cursor.getString(cursor.getColumnIndex("Pass"));
                selectedRow.exKey = cursor.getString(cursor.getColumnIndex("ExKey"));
                selectedRow.customerID = cursor.getInt(cursor.getColumnIndex("CustomerID"));
                selectedRow.customerName = cursor.getString(cursor.getColumnIndex("CustomerName"));
                selectedRow.regDate = cursor.getString(cursor.getColumnIndex("RegDate"));
                selectedRow.country = cursor.getString(cursor.getColumnIndex("Country"));
                selectedRow.state = cursor.getString(cursor.getColumnIndex("State"));
                selectedRow.city = cursor.getString(cursor.getColumnIndex("City"));
                selectedRow.gPSLat = cursor.getDouble(cursor.getColumnIndex("GPSLat"));
                selectedRow.gPSLon = cursor.getDouble(cursor.getColumnIndex("GPSLon"));
                selectedRow.displayMargin = cursor.getInt(cursor.getColumnIndex("DisplayMargin"));
                selectedRow.languageID = cursor.getInt(cursor.getColumnIndex("LanguageID"));
                selectedRow.guaranteeDate = cursor.getString(cursor.getColumnIndex("GuaranteeDate"));
                selectedRow.validMobiles = cursor.getString(cursor.getColumnIndex("ValidMobiles"));
                /*********************** Jahanbin***********************/
                selectedRow.weatherAPIKey = cursor.getString(cursor.getColumnIndex("WeatherAPIKey"));
                /*********************** Jahanbin***********************/
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Setting" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.ver = cursor.getString(cursor.getColumnIndex("Ver"));
                selectedRow.serverURL = cursor.getString(cursor.getColumnIndex("ServerURL"));
                selectedRow.serverSocketIP = cursor.getString(cursor.getColumnIndex("ServerSocketIP"));
                selectedRow.serverSocketPort = cursor.getInt(cursor.getColumnIndex("ServerSocketPort"));
                selectedRow.user = cursor.getString(cursor.getColumnIndex("User"));
                selectedRow.pass = cursor.getString(cursor.getColumnIndex("Pass"));
                selectedRow.exKey = cursor.getString(cursor.getColumnIndex("ExKey"));
                selectedRow.customerID = cursor.getInt(cursor.getColumnIndex("CustomerID"));
                selectedRow.customerName = cursor.getString(cursor.getColumnIndex("CustomerName"));
                selectedRow.regDate = cursor.getString(cursor.getColumnIndex("RegDate"));
                selectedRow.country = cursor.getString(cursor.getColumnIndex("Country"));
                selectedRow.state = cursor.getString(cursor.getColumnIndex("State"));
                selectedRow.city = cursor.getString(cursor.getColumnIndex("City"));
                selectedRow.gPSLat = cursor.getDouble(cursor.getColumnIndex("GPSLat"));
                selectedRow.gPSLon = cursor.getDouble(cursor.getColumnIndex("GPSLon"));
                selectedRow.displayMargin = cursor.getInt(cursor.getColumnIndex("DisplayMargin"));
                selectedRow.languageID = cursor.getInt(cursor.getColumnIndex("LanguageID"));
                selectedRow.guaranteeDate = cursor.getString(cursor.getColumnIndex("GuaranteeDate"));
                selectedRow.validMobiles = cursor.getString(cursor.getColumnIndex("ValidMobiles"));
                /*********************** Jahanbin***********************/
                selectedRow.weatherAPIKey = cursor.getString(cursor.getColumnIndex("WeatherAPIKey"));
                /*********************** Jahanbin***********************/
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class User {
        public static class Struct {
            public int iD = 0;
            public String name = "";
            public String username = "";
            public String password = "";
            public int rol = 0;
            public int status = 0;
        }

        public static long insert(Struct myUser) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myUser.name);
            Values.put("Username", myUser.username);
            Values.put("Password", myUser.password);
            Values.put("Rol", myUser.rol);
            Values.put("Status", myUser.status);
            return G.dbObject.insert("T_User", null, Values);
        }

        public static long insert(String name, String username, String password, int rol, int status) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("Username", username);
            Values.put("Password", password);
            Values.put("Rol", rol);
            Values.put("Status", status);
            return G.dbObject.insert("T_User", null, Values);
        }

        public static int edit(Struct myUser) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myUser.name);
            Values.put("Username", myUser.username);
            Values.put("Password", myUser.password);
            Values.put("Rol", myUser.rol);
            Values.put("Status", myUser.status);
            return G.dbObject.update("T_User", Values, "ID=" + myUser.iD, null);
        }

        public static int edit(int iD, String name, String username, String password, int rol, int status) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("Username", username);
            Values.put("Password", password);
            Values.put("Rol", rol);
            Values.put("Status", status);
            return G.dbObject.update("T_User", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_User", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_User", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_User WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.username = cursor.getString(cursor.getColumnIndex("Username"));
                selectedRow.password = cursor.getString(cursor.getColumnIndex("Password"));
                selectedRow.rol = cursor.getInt(cursor.getColumnIndex("Rol"));
                selectedRow.status = cursor.getInt(cursor.getColumnIndex("Status"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_User" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.username = cursor.getString(cursor.getColumnIndex("Username"));
                selectedRow.password = cursor.getString(cursor.getColumnIndex("Password"));
                selectedRow.rol = cursor.getInt(cursor.getColumnIndex("Rol"));
                selectedRow.status = cursor.getInt(cursor.getColumnIndex("Status"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_User" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.username = cursor.getString(cursor.getColumnIndex("Username"));
                selectedRow.password = cursor.getString(cursor.getColumnIndex("Password"));
                selectedRow.rol = cursor.getInt(cursor.getColumnIndex("Rol"));
                selectedRow.status = cursor.getInt(cursor.getColumnIndex("Status"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Section {
        public static class Struct {
            public int iD = 0;
            public String name = "";
            public String icon = "";
            public int sort = 0;

            public String getSectionDataJson() {
                String scenarioDataJSON;
                JSONArray ja = new JSONArray();
                try {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("ID", iD);
                    jsonObj.put("Name", name);
                    jsonObj.put("Icon", icon);
                    jsonObj.put("Sort", sort);
                    ja.put(jsonObj);
                } catch (Exception e) {
                }
                scenarioDataJSON = ja.toString();
                return ja.toString();
            }
        }

        public static long insert(Struct mySection) {
            ContentValues Values = new ContentValues();
            Values.put("Name", mySection.name);
            Values.put("Icon", mySection.icon);
            Values.put("Sort", mySection.sort);
            return G.dbObject.insertOrThrow("T_Section", null, Values);
        }

        public static long insert(String name, String icon, int sort) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("Icon", icon);
            Values.put("Sort", sort);
            return G.dbObject.insert("T_Section", null, Values);
        }

        public static int edit(Struct mySection) {
            ContentValues Values = new ContentValues();
            Values.put("Name", mySection.name);
            Values.put("Icon", mySection.icon);
            Values.put("Sort", mySection.sort);
            return G.dbObject.update("T_Section", Values, "ID=" + mySection.iD, null);
        }

        public static int edit(int iD, String name, String icon, int sort) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("Icon", icon);
            Values.put("Sort", sort);
            return G.dbObject.update("T_Section", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Section", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Section", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Section WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.sort = cursor.getInt(cursor.getColumnIndex("Sort"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Section" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.sort = cursor.getInt(cursor.getColumnIndex("Sort"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Section" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.sort = cursor.getInt(cursor.getColumnIndex("Sort"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class NodeType {
        public static class Struct {
            public int iD = 0;
            public int typeCode = 0;
            public int nameSentenceID = 0;
            public String icon = "";
            public String commandtext = "";
            public String uIFormName = "";
        }

        public static long insert(Struct myNodeType) {
            ContentValues Values = new ContentValues();
            Values.put("TypeCode", myNodeType.typeCode);
            Values.put("NameSentenceID", myNodeType.nameSentenceID);
            Values.put("Icon", myNodeType.icon);
            Values.put("Commandtext", myNodeType.commandtext);
            Values.put("UIFormName", myNodeType.uIFormName);
            return G.dbObject.insert("T_NodeType", null, Values);
        }

        public static long insert(int typeCode, int nameSentenceID, String icon, String commandtext, String uIFormName) {
            ContentValues Values = new ContentValues();
            Values.put("TypeCode", typeCode);
            Values.put("NameSentenceID", nameSentenceID);
            Values.put("Icon", icon);
            Values.put("Commandtext", commandtext);
            Values.put("UIFormName", uIFormName);
            return G.dbObject.insert("T_NodeType", null, Values);
        }

        public static int edit(Struct myNodeType) {
            ContentValues Values = new ContentValues();
            Values.put("TypeCode", myNodeType.typeCode);
            Values.put("NameSentenceID", myNodeType.nameSentenceID);
            Values.put("Icon", myNodeType.icon);
            Values.put("Commandtext", myNodeType.commandtext);
            Values.put("UIFormName", myNodeType.uIFormName);
            return G.dbObject.update("T_NodeType", Values, "ID=" + myNodeType.iD, null);
        }

        public static int edit(int iD, int typeCode, int nameSentenceID, String icon, String commandtext, String uIFormName) {
            ContentValues Values = new ContentValues();
            Values.put("TypeCode", typeCode);
            Values.put("NameSentenceID", nameSentenceID);
            Values.put("Icon", icon);
            Values.put("Commandtext", commandtext);
            Values.put("UIFormName", uIFormName);
            return G.dbObject.update("T_NodeType", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_NodeType", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_NodeType", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_NodeType WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.typeCode = cursor.getInt(cursor.getColumnIndex("TypeCode"));
                selectedRow.nameSentenceID = cursor.getInt(cursor.getColumnIndex("NameSentenceID"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.commandtext = cursor.getString(cursor.getColumnIndex("Commandtext"));
                selectedRow.uIFormName = cursor.getString(cursor.getColumnIndex("UIFormName"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_NodeType" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.typeCode = cursor.getInt(cursor.getColumnIndex("TypeCode"));
                selectedRow.nameSentenceID = cursor.getInt(cursor.getColumnIndex("NameSentenceID"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.commandtext = cursor.getString(cursor.getColumnIndex("Commandtext"));
                selectedRow.uIFormName = cursor.getString(cursor.getColumnIndex("UIFormName"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_NodeType" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.typeCode = cursor.getInt(cursor.getColumnIndex("TypeCode"));
                selectedRow.nameSentenceID = cursor.getInt(cursor.getColumnIndex("NameSentenceID"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.commandtext = cursor.getString(cursor.getColumnIndex("Commandtext"));
                selectedRow.uIFormName = cursor.getString(cursor.getColumnIndex("UIFormName"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class ValueTypeInstances {
        public static class Struct {
            public int iD = 0;
            public int valueTypeID = 0;
            public float value = 0;
            public int sentenceID = 0;
        }

        public static long insert(Struct myValueTypeInstances) {
            ContentValues Values = new ContentValues();
            Values.put("ValueTypeID", myValueTypeInstances.valueTypeID);
            Values.put("Value", myValueTypeInstances.value);
            Values.put("SentenceID", myValueTypeInstances.sentenceID);
            return G.dbObject.insert("T_ValueTypeInstances", null, Values);
        }

        public static long insert(int valueTypeID, float value, int sentenceID) {
            ContentValues Values = new ContentValues();
            Values.put("ValueTypeID", valueTypeID);
            Values.put("Value", value);
            Values.put("SentenceID", sentenceID);
            return G.dbObject.insert("T_ValueTypeInstances", null, Values);
        }

        public static int edit(Struct myValueTypeInstances) {
            ContentValues Values = new ContentValues();
            Values.put("ValueTypeID", myValueTypeInstances.valueTypeID);
            Values.put("Value", myValueTypeInstances.value);
            Values.put("SentenceID", myValueTypeInstances.sentenceID);
            return G.dbObject.update("T_ValueTypeInstances", Values, "ID=" + myValueTypeInstances.iD, null);
        }

        public static int edit(int iD, int valueTypeID, float value, int sentenceID) {
            ContentValues Values = new ContentValues();
            Values.put("ValueTypeID", valueTypeID);
            Values.put("Value", value);
            Values.put("SentenceID", sentenceID);
            return G.dbObject.update("T_ValueTypeInstances", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_ValueTypeInstances", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_ValueTypeInstances", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_ValueTypeInstances WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.valueTypeID = cursor.getInt(cursor.getColumnIndex("ValueTypeID"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.sentenceID = cursor.getInt(cursor.getColumnIndex("SentenceID"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_ValueTypeInstances" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.valueTypeID = cursor.getInt(cursor.getColumnIndex("ValueTypeID"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.sentenceID = cursor.getInt(cursor.getColumnIndex("SentenceID"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_ValueTypeInstances" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.valueTypeID = cursor.getInt(cursor.getColumnIndex("ValueTypeID"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.sentenceID = cursor.getInt(cursor.getColumnIndex("SentenceID"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class WeatherTypes {
        public static class Struct {
            public int iD = 0;
            public int nameSentenceID = 0;
            public String icon = "";
            public String weatherCode = "";
        }

        public static long insert(Struct myWeatherTypes) {
            ContentValues Values = new ContentValues();
            Values.put("NameSentenceID", myWeatherTypes.nameSentenceID);
            Values.put("Icon", myWeatherTypes.icon);
            Values.put("weatherCode", myWeatherTypes.weatherCode);
            return G.dbObject.insert("T_WeatherTypes", null, Values);
        }

        public static long insert(int nameSentenceID, String icon, String weatherCode) {
            ContentValues Values = new ContentValues();
            Values.put("NameSentenceID", nameSentenceID);
            Values.put("Icon", icon);
            Values.put("weatherCode", weatherCode);
            return G.dbObject.insert("T_WeatherTypes", null, Values);
        }

        public static int edit(Struct myWeatherTypes) {
            ContentValues Values = new ContentValues();
            Values.put("NameSentenceID", myWeatherTypes.nameSentenceID);
            Values.put("Icon", myWeatherTypes.icon);
            Values.put("weatherCode", myWeatherTypes.weatherCode);
            return G.dbObject.update("T_WeatherTypes", Values, "ID=" + myWeatherTypes.iD, null);
        }

        public static int edit(int iD, int nameSentenceID, String icon, String weatherCode) {
            ContentValues Values = new ContentValues();
            Values.put("NameSentenceID", nameSentenceID);
            Values.put("Icon", icon);
            Values.put("weatherCode", weatherCode);
            return G.dbObject.update("T_WeatherTypes", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_WeatherTypes", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_WeatherTypes", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_WeatherTypes WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.nameSentenceID = cursor.getInt(cursor.getColumnIndex("NameSentenceID"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.weatherCode = cursor.getString(cursor.getColumnIndex("weatherCode"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_WeatherTypes" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.nameSentenceID = cursor.getInt(cursor.getColumnIndex("NameSentenceID"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.weatherCode = cursor.getString(cursor.getColumnIndex("weatherCode"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_WeatherTypes" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.nameSentenceID = cursor.getInt(cursor.getColumnIndex("NameSentenceID"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.weatherCode = cursor.getString(cursor.getColumnIndex("weatherCode"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Mobiles {
        public static class Struct {
            public int iD = 0;
            public String name = "";
            public String serialNumber = "";
            public String exKey = "";
            public int socketIdentiity = 0;
            public double LastLatitude = 0;
            public double LastLongitude = 0;
            public String FirebaseToken = "";

            public String getMobileDataJson() {
                try {
                    JSONObject joMobile = new JSONObject();
                    joMobile.put("ID", iD);
                    joMobile.put("ExKey", exKey);
                    joMobile.put("Name", name);
                    joMobile.put("Serial", serialNumber);
                    joMobile.put("Latitude", LastLatitude);
                    joMobile.put("Longitude", LastLongitude);
                    joMobile.put("FirebaseToken", FirebaseToken);
                    return joMobile.toString();
                } catch (JSONException e) {
                    G.printStackTrace(e);
                    return "{}";
                }
            }
        }

        public static long insert(Struct myMobiles) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myMobiles.name);
            Values.put("SerialNumber", myMobiles.serialNumber);
            Values.put("ExKey", myMobiles.exKey);
            Values.put("SocketIdentiity", myMobiles.socketIdentiity);
            Values.put("LastLatitude", myMobiles.LastLatitude);
            Values.put("LastLongitude", myMobiles.LastLongitude);
            Values.put("FirebaseToken", myMobiles.FirebaseToken);
            return G.dbObject.insert("T_Mobiles", null, Values);
        }

        public static long insert(String name, String serialNumber, String exKey, int socketIdentiity, double LastLatitude, double LastLongitude, String firebaseToken) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("SerialNumber", serialNumber);
            Values.put("ExKey", exKey);
            Values.put("SocketIdentiity", socketIdentiity);
            Values.put("LastLatitude", LastLatitude);
            Values.put("LastLongitude", LastLongitude);
            Values.put("FirebaseToken", firebaseToken);
            return G.dbObject.insert("T_Mobiles", null, Values);
        }

        public static int edit(Struct myMobiles) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myMobiles.name);
            Values.put("SerialNumber", myMobiles.serialNumber);
            Values.put("ExKey", myMobiles.exKey);
            Values.put("SocketIdentiity", myMobiles.socketIdentiity);
            Values.put("LastLatitude", myMobiles.LastLatitude);
            Values.put("LastLongitude", myMobiles.LastLongitude);
            Values.put("FirebaseToken", myMobiles.FirebaseToken);
            return G.dbObject.update("T_Mobiles", Values, "ID=" + myMobiles.iD, null);
        }

        public static int edit(int iD, String name, String serialNumber, String exKey, int socketIdentiity, double LastLatitude, double LastLongitude, String firebaseToken) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("SerialNumber", serialNumber);
            Values.put("ExKey", exKey);
            Values.put("SocketIdentiity", socketIdentiity);
            Values.put("LastLatitude", LastLatitude);
            Values.put("LastLongitude", LastLongitude);
            Values.put("FirebaseToken", firebaseToken);
            return G.dbObject.update("T_Mobiles", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Mobiles", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Mobiles", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Mobiles WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.serialNumber = cursor.getString(cursor.getColumnIndex("SerialNumber"));
                selectedRow.exKey = cursor.getString(cursor.getColumnIndex("ExKey"));
                selectedRow.socketIdentiity = cursor.getInt(cursor.getColumnIndex("SocketIdentiity"));
                selectedRow.LastLatitude = cursor.getDouble(cursor.getColumnIndex("LastLatitude"));
                selectedRow.LastLongitude = cursor.getDouble(cursor.getColumnIndex("LastLongitude"));
                selectedRow.FirebaseToken = cursor.getString(cursor.getColumnIndex("FirebaseToken"));

            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        /**********************************
         * Jahanbin
         ******************************************/
        public static ArrayList<Struct> getAllTokens() {
            ArrayList<Mobiles.Struct> selectedRows = new ArrayList<>();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Mobiles", null);
            if (cursor.moveToFirst()) {
                do {
                    Struct selectedRow = new Struct();
                    selectedRow.FirebaseToken = cursor.getString(cursor.getColumnIndex("FirebaseToken"));
                    selectedRows.add(selectedRow);
                } while (cursor.moveToNext());
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRows;
        }

        /**********************************
         * Jahanbin
         ******************************************/

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            G.log("SELECT * FROM T_Mobiles" + whereQuery);
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Mobiles" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.serialNumber = cursor.getString(cursor.getColumnIndex("SerialNumber"));
                selectedRow.exKey = cursor.getString(cursor.getColumnIndex("ExKey"));
                selectedRow.socketIdentiity = cursor.getInt(cursor.getColumnIndex("SocketIdentiity"));
                selectedRow.LastLatitude = cursor.getDouble(cursor.getColumnIndex("LastLatitude"));
                selectedRow.LastLongitude = cursor.getDouble(cursor.getColumnIndex("LastLongitude"));
                selectedRow.FirebaseToken = cursor.getString(cursor.getColumnIndex("FirebaseToken"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Mobiles" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.serialNumber = cursor.getString(cursor.getColumnIndex("SerialNumber"));
                selectedRow.exKey = cursor.getString(cursor.getColumnIndex("ExKey"));
                selectedRow.socketIdentiity = cursor.getInt(cursor.getColumnIndex("SocketIdentiity"));
                selectedRow.LastLatitude = cursor.getDouble(cursor.getColumnIndex("LastLatitude"));
                selectedRow.LastLongitude = cursor.getDouble(cursor.getColumnIndex("LastLongitude"));
                selectedRow.FirebaseToken = cursor.getString(cursor.getColumnIndex("FirebaseToken"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Log {
        public static class Struct {
            public int iD = 0;
            public int type = 0;
            public String date = ""; //= Calendar.getInstance().getTime();
            public String des = "";
            public int operatorType = 0;
            public int operatorID = 0;
        }

        public static long insert(Struct myLog) {
            ContentValues Values = new ContentValues();
            Values.put("Type", myLog.type);
            Values.put("Date", myLog.date);
            Values.put("Des", myLog.des);
            Values.put("OperatorType", myLog.operatorType);
            Values.put("OperatorID", myLog.operatorID);
            return G.dbObject.insert("T_Log", null, Values);
        }

        public static long insert(int type, String date, String des, int operatorType, int operatorID) {
            ContentValues Values = new ContentValues();
            Values.put("Type", type);
            Values.put("Date", date.toString());
            Values.put("Des", des);
            Values.put("OperatorType", operatorType);
            Values.put("OperatorID", operatorID);
            return G.dbObject.insert("T_Log", null, Values);
        }

        public static int edit(Struct myLog) {
            ContentValues Values = new ContentValues();
            Values.put("Type", myLog.type);
            Values.put("Date", myLog.date);
            Values.put("Des", myLog.des);
            Values.put("OperatorType", myLog.operatorType);
            Values.put("OperatorID", myLog.operatorID);
            return G.dbObject.update("T_Log", Values, "ID=" + myLog.iD, null);
        }

        public static int edit(int iD, int type, String date, String des, int operatorType, int operatorID) {
            ContentValues Values = new ContentValues();
            Values.put("Type", type);
            Values.put("Date", date.toString());
            Values.put("Des", des);
            Values.put("OperatorType", operatorType);
            Values.put("OperatorID", operatorID);
            return G.dbObject.update("T_Log", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Log", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Log", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Log WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.type = cursor.getInt(cursor.getColumnIndex("Type"));
                selectedRow.date = cursor.getString(cursor.getColumnIndex("Date"));
                selectedRow.des = cursor.getString(cursor.getColumnIndex("Des"));
                selectedRow.operatorType = cursor.getInt(cursor.getColumnIndex("OperatorType"));
                selectedRow.operatorID = cursor.getInt(cursor.getColumnIndex("OperatorID"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Log" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.type = cursor.getInt(cursor.getColumnIndex("Type"));
                selectedRow.date = cursor.getString(cursor.getColumnIndex("Date"));
                selectedRow.des = cursor.getString(cursor.getColumnIndex("Des"));
                selectedRow.operatorType = cursor.getInt(cursor.getColumnIndex("OperatorType"));
                selectedRow.operatorID = cursor.getInt(cursor.getColumnIndex("OperatorID"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Log" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.type = cursor.getInt(cursor.getColumnIndex("Type"));
                selectedRow.date = cursor.getString(cursor.getColumnIndex("Date"));
                selectedRow.des = cursor.getString(cursor.getColumnIndex("Des"));
                selectedRow.operatorType = cursor.getInt(cursor.getColumnIndex("OperatorType"));
                selectedRow.operatorID = cursor.getInt(cursor.getColumnIndex("OperatorID"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class NetworkSetting {
        public static class Struct {
            public int iD = 0;
            public String wiFiSSID = "";
            public String wiFiSecurityType = "";
            public String wiFiSecurityKey = "";
            public String wiFiMac = "";
            public String mainIPAddress = "";
            public String mainSubnetMask = "";
            public String mainDefaultGateway = "";
            public String mainDNS1 = "";
            public String mainDNS2 = "";
            public String nodeIPsStart = "";
            public String nodeIPsEnd = "";
            public String learnSSID = "";
            public String learnSecurityKey = "";
            public String learnIPAddress = "";
            public String learnSubnetMask = "";
        }

        public static long insert(Struct myNetworkSetting) {
            ContentValues Values = new ContentValues();
            Values.put("WiFiSSID", myNetworkSetting.wiFiSSID);
            Values.put("WiFiSecurityType", myNetworkSetting.wiFiSecurityType);
            Values.put("WiFiSecurityKey", myNetworkSetting.wiFiSecurityKey);
            Values.put("WiFiMac", myNetworkSetting.wiFiMac);
            Values.put("MainIPAddress", myNetworkSetting.mainIPAddress);
            Values.put("MainSubnetMask", myNetworkSetting.mainSubnetMask);
            Values.put("MainDefaultGateway", myNetworkSetting.mainDefaultGateway);
            Values.put("MainDNS1", myNetworkSetting.mainDNS1);
            Values.put("MainDNS2", myNetworkSetting.mainDNS2);
            Values.put("NodeIPsStart", myNetworkSetting.nodeIPsStart);
            Values.put("NodeIPsEnd", myNetworkSetting.nodeIPsEnd);
            Values.put("LearnSSID", myNetworkSetting.learnSSID);
            Values.put("LearnSecurityKey", myNetworkSetting.learnSecurityKey);
            Values.put("LearnIPAddress", myNetworkSetting.learnIPAddress);
            Values.put("LearnSubnetMask", myNetworkSetting.learnSubnetMask);
            return G.dbObject.insert("T_NetworkSetting", null, Values);
        }

        public static long insert(String wiFiSSID, String wiFiSecurityType, String wiFiSecurityKey, String wiFiMac, String mainIPAddress, String mainSubnetMask, String mainDefaultGateway, String mainDNS1, String mainDNS2, String nodeIPsStart, String nodeIPsEnd, String learnSSID, String learnSecurityKey, String learnIPAddress, String learnSubnetMask) {
            ContentValues Values = new ContentValues();
            Values.put("WiFiSSID", wiFiSSID);
            Values.put("WiFiSecurityType", wiFiSecurityType);
            Values.put("WiFiSecurityKey", wiFiSecurityKey);
            Values.put("WiFiMac", wiFiMac);
            Values.put("MainIPAddress", mainIPAddress);
            Values.put("MainSubnetMask", mainSubnetMask);
            Values.put("MainDefaultGateway", mainDefaultGateway);
            Values.put("MainDNS1", mainDNS1);
            Values.put("MainDNS2", mainDNS2);
            Values.put("NodeIPsStart", nodeIPsStart);
            Values.put("NodeIPsEnd", nodeIPsEnd);
            Values.put("LearnSSID", learnSSID);
            Values.put("LearnSecurityKey", learnSecurityKey);
            Values.put("LearnIPAddress", learnIPAddress);
            Values.put("LearnSubnetMask", learnSubnetMask);
            return G.dbObject.insert("T_NetworkSetting", null, Values);
        }

        public static int edit(Struct myNetworkSetting) {
            ContentValues Values = new ContentValues();
            Values.put("WiFiSSID", myNetworkSetting.wiFiSSID);
            Values.put("WiFiSecurityType", myNetworkSetting.wiFiSecurityType);
            Values.put("WiFiSecurityKey", myNetworkSetting.wiFiSecurityKey);
            Values.put("WiFiMac", myNetworkSetting.wiFiMac);
            Values.put("MainIPAddress", myNetworkSetting.mainIPAddress);
            Values.put("MainSubnetMask", myNetworkSetting.mainSubnetMask);
            Values.put("MainDefaultGateway", myNetworkSetting.mainDefaultGateway);
            Values.put("MainDNS1", myNetworkSetting.mainDNS1);
            Values.put("MainDNS2", myNetworkSetting.mainDNS2);
            Values.put("NodeIPsStart", myNetworkSetting.nodeIPsStart);
            Values.put("NodeIPsEnd", myNetworkSetting.nodeIPsEnd);
            Values.put("LearnSSID", myNetworkSetting.learnSSID);
            Values.put("LearnSecurityKey", myNetworkSetting.learnSecurityKey);
            Values.put("LearnIPAddress", myNetworkSetting.learnIPAddress);
            Values.put("LearnSubnetMask", myNetworkSetting.learnSubnetMask);
            G.toast("Network setting saved . SSID=" + myNetworkSetting.wiFiSSID);
            return G.dbObject.update("T_NetworkSetting", Values, "ID=" + myNetworkSetting.iD, null);
        }

        public static int edit(int iD, String wiFiSSID, String wiFiSecurityType, String wiFiSecurityKey, String wiFiMac, String mainIPAddress, String mainSubnetMask, String mainDefaultGateway, String mainDNS1, String mainDNS2, String nodeIPsStart, String nodeIPsEnd, String learnSSID, String learnSecurityKey, String learnIPAddress, String learnSubnetMask) {
            ContentValues Values = new ContentValues();
            Values.put("WiFiSSID", wiFiSSID);
            Values.put("WiFiSecurityType", wiFiSecurityType);
            Values.put("WiFiSecurityKey", wiFiSecurityKey);
            Values.put("WiFiMac", wiFiMac);
            Values.put("MainIPAddress", mainIPAddress);
            Values.put("MainSubnetMask", mainSubnetMask);
            Values.put("MainDefaultGateway", mainDefaultGateway);
            Values.put("MainDNS1", mainDNS1);
            Values.put("MainDNS2", mainDNS2);
            Values.put("NodeIPsStart", nodeIPsStart);
            Values.put("NodeIPsEnd", nodeIPsEnd);
            Values.put("LearnSSID", learnSSID);
            Values.put("LearnSecurityKey", learnSecurityKey);
            Values.put("LearnIPAddress", learnIPAddress);
            Values.put("LearnSubnetMask", learnSubnetMask);
            return G.dbObject.update("T_NetworkSetting", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_NetworkSetting", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_NetworkSetting", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_NetworkSetting WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.wiFiSSID = cursor.getString(cursor.getColumnIndex("WiFiSSID"));
                selectedRow.wiFiSecurityType = cursor.getString(cursor.getColumnIndex("WiFiSecurityType"));
                selectedRow.wiFiSecurityKey = cursor.getString(cursor.getColumnIndex("WiFiSecurityKey"));
                selectedRow.wiFiMac = cursor.getString(cursor.getColumnIndex("WiFiMac"));
                selectedRow.mainIPAddress = cursor.getString(cursor.getColumnIndex("MainIPAddress"));
                selectedRow.mainSubnetMask = cursor.getString(cursor.getColumnIndex("MainSubnetMask"));
                selectedRow.mainDefaultGateway = cursor.getString(cursor.getColumnIndex("MainDefaultGateway"));
                selectedRow.mainDNS1 = cursor.getString(cursor.getColumnIndex("MainDNS1"));
                selectedRow.mainDNS2 = cursor.getString(cursor.getColumnIndex("MainDNS2"));
                selectedRow.nodeIPsStart = cursor.getString(cursor.getColumnIndex("NodeIPsStart"));
                selectedRow.nodeIPsEnd = cursor.getString(cursor.getColumnIndex("NodeIPsEnd"));
                selectedRow.learnSSID = cursor.getString(cursor.getColumnIndex("LearnSSID"));
                selectedRow.learnSecurityKey = cursor.getString(cursor.getColumnIndex("LearnSecurityKey"));
                selectedRow.learnIPAddress = cursor.getString(cursor.getColumnIndex("LearnIPAddress"));
                selectedRow.learnSubnetMask = cursor.getString(cursor.getColumnIndex("LearnSubnetMask"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_NetworkSetting" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.wiFiSSID = cursor.getString(cursor.getColumnIndex("WiFiSSID"));
                selectedRow.wiFiSecurityType = cursor.getString(cursor.getColumnIndex("WiFiSecurityType"));
                selectedRow.wiFiSecurityKey = cursor.getString(cursor.getColumnIndex("WiFiSecurityKey"));
                selectedRow.wiFiMac = cursor.getString(cursor.getColumnIndex("WiFiMac"));
                selectedRow.mainIPAddress = cursor.getString(cursor.getColumnIndex("MainIPAddress"));
                selectedRow.mainSubnetMask = cursor.getString(cursor.getColumnIndex("MainSubnetMask"));
                selectedRow.mainDefaultGateway = cursor.getString(cursor.getColumnIndex("MainDefaultGateway"));
                selectedRow.mainDNS1 = cursor.getString(cursor.getColumnIndex("MainDNS1"));
                selectedRow.mainDNS2 = cursor.getString(cursor.getColumnIndex("MainDNS2"));
                selectedRow.nodeIPsStart = cursor.getString(cursor.getColumnIndex("NodeIPsStart"));
                selectedRow.nodeIPsEnd = cursor.getString(cursor.getColumnIndex("NodeIPsEnd"));
                selectedRow.learnSSID = cursor.getString(cursor.getColumnIndex("LearnSSID"));
                selectedRow.learnSecurityKey = cursor.getString(cursor.getColumnIndex("LearnSecurityKey"));
                selectedRow.learnIPAddress = cursor.getString(cursor.getColumnIndex("LearnIPAddress"));
                selectedRow.learnSubnetMask = cursor.getString(cursor.getColumnIndex("LearnSubnetMask"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_NetworkSetting" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.wiFiSSID = cursor.getString(cursor.getColumnIndex("WiFiSSID"));
                selectedRow.wiFiSecurityType = cursor.getString(cursor.getColumnIndex("WiFiSecurityType"));
                selectedRow.wiFiSecurityKey = cursor.getString(cursor.getColumnIndex("WiFiSecurityKey"));
                selectedRow.wiFiMac = cursor.getString(cursor.getColumnIndex("WiFiMac"));
                selectedRow.mainIPAddress = cursor.getString(cursor.getColumnIndex("MainIPAddress"));
                selectedRow.mainSubnetMask = cursor.getString(cursor.getColumnIndex("MainSubnetMask"));
                selectedRow.mainDefaultGateway = cursor.getString(cursor.getColumnIndex("MainDefaultGateway"));
                selectedRow.mainDNS1 = cursor.getString(cursor.getColumnIndex("MainDNS1"));
                selectedRow.mainDNS2 = cursor.getString(cursor.getColumnIndex("MainDNS2"));
                selectedRow.nodeIPsStart = cursor.getString(cursor.getColumnIndex("NodeIPsStart"));
                selectedRow.nodeIPsEnd = cursor.getString(cursor.getColumnIndex("NodeIPsEnd"));
                selectedRow.learnSSID = cursor.getString(cursor.getColumnIndex("LearnSSID"));
                selectedRow.learnSecurityKey = cursor.getString(cursor.getColumnIndex("LearnSecurityKey"));
                selectedRow.learnIPAddress = cursor.getString(cursor.getColumnIndex("LearnIPAddress"));
                selectedRow.learnSubnetMask = cursor.getString(cursor.getColumnIndex("LearnSubnetMask"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Room {
        public static class Struct {
            public int iD = 0;
            public String name = "";
            public String icon = "";
            public int sort = 0;
            public int sectionID = 0;

            public String getRoomDataJson() {
                String scenarioDataJSON;
                JSONArray ja = new JSONArray();
                try {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("ID", iD);
                    jsonObj.put("Name", name);
                    jsonObj.put("Icon", icon);
                    jsonObj.put("Sort", sort);
                    jsonObj.put("SectionID", sectionID);
                    ja.put(jsonObj);
                } catch (Exception e) {
                }
                scenarioDataJSON = ja.toString();
                return ja.toString();
            }
        }

        public static long insert(Struct myRoom) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myRoom.name);
            Values.put("Icon", myRoom.icon);
            Values.put("Sort", myRoom.sort);
            Values.put("SectionID", myRoom.sectionID);
            return G.dbObject.insertOrThrow("T_Room", null, Values);
        }

        public static long insert(String name, String desc, String icon, int sort, int sectionID) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("Desc", desc);
            Values.put("Icon", icon);
            Values.put("Sort", sort);
            Values.put("SectionID", sectionID);
            return G.dbObject.insertOrThrow("T_Room", null, Values);
        }

        public static int edit(Struct myRoom) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myRoom.name);
            Values.put("Icon", myRoom.icon);
            Values.put("Sort", myRoom.sort);
            Values.put("SectionID", myRoom.sectionID);
            return G.dbObject.update("T_Room", Values, "ID=" + myRoom.iD, null);
        }

        public static int edit(int iD, String name, String desc, String icon, int sort, int sectionID) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("Desc", desc);
            Values.put("Icon", icon);
            Values.put("Sort", sort);
            Values.put("SectionID", sectionID);
            return G.dbObject.update("T_Room", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Room", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Room", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Room WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.sort = cursor.getInt(cursor.getColumnIndex("Sort"));
                selectedRow.sectionID = cursor.getInt(cursor.getColumnIndex("SectionID"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Room" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.sort = cursor.getInt(cursor.getColumnIndex("Sort"));
                selectedRow.sectionID = cursor.getInt(cursor.getColumnIndex("SectionID"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Room" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.sort = cursor.getInt(cursor.getColumnIndex("Sort"));
                selectedRow.sectionID = cursor.getInt(cursor.getColumnIndex("SectionID"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Language {
        public static class Struct {
            public int iD = 0;
            public String name = "";
            public boolean rTL = false;
            public String kBSign = "";
        }

        public static long insert(Struct myLanguage) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myLanguage.name);
            Values.put("RTL", myLanguage.rTL);
            Values.put("KBSign", myLanguage.kBSign);
            return G.dbObject.insert("T_Language", null, Values);
        }

        public static long insert(String name, boolean rTL, String kBSign) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("RTL", rTL);
            Values.put("KBSign", kBSign);
            return G.dbObject.insert("T_Language", null, Values);
        }

        public static int edit(Struct myLanguage) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myLanguage.name);
            Values.put("RTL", myLanguage.rTL);
            Values.put("KBSign", myLanguage.kBSign);
            return G.dbObject.update("T_Language", Values, "ID=" + myLanguage.iD, null);
        }

        public static int edit(int iD, String name, boolean rTL, String kBSign) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("RTL", rTL);
            Values.put("KBSign", kBSign);
            return G.dbObject.update("T_Language", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Language", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Language", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Language WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.rTL = cursor.getInt(cursor.getColumnIndex("RTL")) != 0;
                selectedRow.kBSign = cursor.getString(cursor.getColumnIndex("KBSign"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Language" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.rTL = cursor.getInt(cursor.getColumnIndex("RTL")) != 0;
                selectedRow.kBSign = cursor.getString(cursor.getColumnIndex("KBSign"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Language" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.rTL = cursor.getInt(cursor.getColumnIndex("RTL")) != 0;
                selectedRow.kBSign = cursor.getString(cursor.getColumnIndex("KBSign"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Message {
        public static class Struct {
            public int iD = 0;
            public int recordId = 0;
            public int messageType = 0;
            public int messageAction = 0;
            public String messageText = "";
            public String recieverIDs = "";
            public String msgDateTime = "";
        }

        public static long insert(Struct myMessage) {
            ContentValues Values = new ContentValues();
            Values.put("RecordId", myMessage.recordId);
            Values.put("MessageType", myMessage.messageType);
            Values.put("Action", myMessage.messageAction);
            Values.put("MessageText", myMessage.messageText);
            Values.put("RecieverIDs", myMessage.recieverIDs);
            Values.put("MsgDateTime", myMessage.msgDateTime);
            return G.dbObject.insert("T_Message", null, Values);
        }

        public static long insert(int recordId, int messageType, String messageText, String recieverIDs, int messageAction, String msgDateTime) {
            ContentValues Values = new ContentValues();
            Values.put("RecordId", recordId);
            Values.put("MessageType", messageType);
            Values.put("Action", messageAction);
            Values.put("MessageText", messageText);
            Values.put("RecieverIDs", recieverIDs);
            Values.put("MsgDateTime", msgDateTime);
            return G.dbObject.insert("T_Message", null, Values);
        }

        public static int edit(Struct myMessage) {
            ContentValues Values = new ContentValues();
            Values.put("RecordId", myMessage.recordId);
            Values.put("MessageType", myMessage.messageType);
            Values.put("Action", myMessage.messageAction);
            Values.put("MessageText", myMessage.messageText);
            Values.put("RecieverIDs", myMessage.recieverIDs);
            Values.put("MsgDateTime", myMessage.msgDateTime);
            return G.dbObject.update("T_Message", Values, "ID=" + myMessage.iD, null);
        }

        public static int edit(int iD, int recordId, int messageType, String messageText, String recieverIDs, int messageAction, String msgDateTime) {
            ContentValues Values = new ContentValues();
            Values.put("RecordId", recordId);
            Values.put("MessageType", messageType);
            Values.put("Action", messageAction);
            Values.put("MessageText", messageText);
            Values.put("RecieverIDs", recieverIDs);
            Values.put("MsgDateTime", msgDateTime);
            return G.dbObject.update("T_Message", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Message", "ID=" + iD, null);
        }

        public static void delete(int type, int recordId) {
            G.dbObject.execSQL("DELETE FROM T_Message WHERE ID =(SELECT ID from T_Message Where RecordId = "
                    + recordId + " AND MessageType = "
                    + type + " ORDER BY ID ASC LIMIT 1)");
        }

        public static int delete() {
            return G.dbObject.delete("T_Message", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Message WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.recordId = cursor.getInt(cursor.getColumnIndex("RecordId"));
                selectedRow.messageType = cursor.getInt(cursor.getColumnIndex("MessageType"));
                selectedRow.messageAction = cursor.getInt(cursor.getColumnIndex("Action"));
                selectedRow.messageText = cursor.getString(cursor.getColumnIndex("MessageText"));
                selectedRow.recieverIDs = cursor.getString(cursor.getColumnIndex("RecieverIDs"));
                selectedRow.msgDateTime = cursor.getString(cursor.getColumnIndex("MsgDateTime"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Message" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.recordId = cursor.getInt(cursor.getColumnIndex("RecordId"));
                selectedRow.messageType = cursor.getInt(cursor.getColumnIndex("MessageType"));
                selectedRow.messageAction = cursor.getInt(cursor.getColumnIndex("Action"));
                selectedRow.messageText = cursor.getString(cursor.getColumnIndex("MessageText"));
                selectedRow.recieverIDs = cursor.getString(cursor.getColumnIndex("RecieverIDs"));
                selectedRow.msgDateTime = cursor.getString(cursor.getColumnIndex("MsgDateTime"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Message" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.recordId = cursor.getInt(cursor.getColumnIndex("RecordId"));
                selectedRow.messageType = cursor.getInt(cursor.getColumnIndex("MessageType"));
                selectedRow.messageAction = cursor.getInt(cursor.getColumnIndex("Action"));
                selectedRow.messageText = cursor.getString(cursor.getColumnIndex("MessageText"));
                selectedRow.recieverIDs = cursor.getString(cursor.getColumnIndex("RecieverIDs"));
                selectedRow.msgDateTime = cursor.getString(cursor.getColumnIndex("MsgDateTime"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Node {
        public static class Struct {
            public int iD = 0;
            public String iP = "";
            public String mac = "";
            public int roomID = 0;
            public int nodeTypeID = 0;
            public String icon = "";
            public String name = "";
            public int status = 0;
            public String expDate = "";
            public String regDate = "";
            public String serialNumber = "";
            public String osVer = "";
            public String buildnumber = "";
            public int lastSecKey = 0;
            public boolean isFavorite = false;
            public boolean isVisible = false;
            public String AvailablePorts = "";
            public int isIoModuleNode;

            public String getFullName() {
                Cursor cursor = G.dbObject.rawQuery("SELECT T_Section.Name  AS SectionName,T_Room.Name AS RoomName, T_Node.Name AS NodeName FROM T_Node INNER JOIN T_ROOM ON T_Node.RoomID=T_Room.ID INNER JOIN T_Section ON T_Room.SectionID= T_Section.ID WHERE " +
                        " T_Node.ID=" + iD, null);
                if (cursor.moveToNext()) {
                    String fullName;
                    fullName = cursor.getString(cursor.getColumnIndex("SectionName"));
                    fullName += " / " + cursor.getString(cursor.getColumnIndex("RoomName"));
                    String nName = cursor.getString(cursor.getColumnIndex("NodeName"));
                    fullName += " / " + nName;
                    try {
                        cursor.close();
                    } catch (Exception e) {
                    }
                    return fullName;
                }
                try {
                    cursor.close();
                } catch (Exception e) {
                }
                return G.T.getSentence(606604) + " : " + iD;
            }

            // age io module bashe bayad kelidaye makhsoose khodesh ro bekeshim biroon
            // age nabashe ye selecte sade mizanim
            public String getNodeStatusJson(boolean ioModule) {
                Switch.Struct[] switches;
//                if (ioModule)
//                    switches = Switch.select("isIOModuleSwitch = 1");
//                else
                switches = Switch.select("NodeID=" + iD);

                JSONArray ja = new JSONArray();
                for (int i = 0; i < switches.length; i++) {
                    try {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("ID", switches[i].iD);
                        jsonObj.put("Value", switches[i].value);
                        ja.put(jsonObj);
                    } catch (Exception e) {
                    }
                }
                return ja.toString();
            }

            public String getNodeDataJson() {
                JSONArray ja = new JSONArray();
                try {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("ID", iD);
                    jsonObj.put("Name", name);
                    jsonObj.put("Icon", icon);
                    jsonObj.put("RoomID", roomID);
                    jsonObj.put("Status", status);
                    jsonObj.put("RegisterDate", regDate);
                    jsonObj.put("NodeTypeID", nodeTypeID);
                    jsonObj.put("SerialNumber", serialNumber);
                    jsonObj.put("isIOModuleNode", isIoModuleNode);

                    ja.put(jsonObj);
                } catch (Exception e) {
                }
                return ja.toString();

            }

            public String getNodeSwitchesDataJson() {
                Switch.Struct[] switches = Switch.select("NodeID=" + iD);
                JSONArray ja = new JSONArray();

                if (switches == null) return "";

                for (int i = 0; i < switches.length; i++) {
                    try {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("ID", switches[i].iD);
                        jsonObj.put("Name", switches[i].name);
                        jsonObj.put("Code", switches[i].code);
                        jsonObj.put("Value", switches[i].value);
                        jsonObj.put("NodeID", switches[i].nodeID);
                        ja.put(jsonObj);
                    } catch (Exception e) {
                    }
                }
                return ja.toString();
            }

        }

        public static long insert(Struct myNode) {
            G.log("Inserting new node with IP :" + myNode.iP + " Type: " + myNode.nodeTypeID);
            ContentValues Values = new ContentValues();
            Values.put("IP", myNode.iP);
            Values.put("Mac", myNode.mac);
            Values.put("Serialnumber", myNode.serialNumber);
            Values.put("RoomID", myNode.roomID);
            Values.put("NodeTypeID", myNode.nodeTypeID);
            Values.put("Icon", myNode.icon);
            Values.put("Name", myNode.name);
            Values.put("Status", myNode.status);
            Values.put("ExpDate", myNode.expDate);
            Values.put("RegDate", myNode.regDate);
            Values.put("BuildNumber", myNode.osVer);
            Values.put("OsVer", myNode.buildnumber);
            Values.put("isVisible", myNode.isVisible);
            Values.put("LastSecKey", myNode.lastSecKey);
            Values.put("isFavorite", myNode.isFavorite);
            Values.put("AvailablePorts", myNode.AvailablePorts);
            Values.put("isIoModuleNode", myNode.isIoModuleNode);

            return G.dbObject.insert("T_Node", null, Values);
        }

        public static long insert(String iP, String mac, String serialNumber, int roomID, int nodeTypeID, String icon, String name, int status, String expDate, String regDate, String buildNumber, String osVer, int lastSecKey, boolean isFavorite, String availablePorts, int inIoModuleNode, boolean isVisible) {
            ContentValues Values = new ContentValues();
            Values.put("IP", iP);
            Values.put("Mac", mac);
            Values.put("Serialnumber", serialNumber);
            Values.put("RoomID", roomID);
            Values.put("NodeTypeID", nodeTypeID);
            Values.put("Icon", icon);
            Values.put("Name", name);
            Values.put("Status", status);
            Values.put("ExpDate", expDate);
            Values.put("RegDate", regDate);
            Values.put("BuildNumber", buildNumber);
            Values.put("OsVer", osVer);
            Values.put("LastSecKey", lastSecKey);
            Values.put("isFavorite", isFavorite);
            Values.put("isVisible", isVisible);
            Values.put("AvailablePorts", availablePorts);
            Values.put("isIoModuleNode", inIoModuleNode);
            return G.dbObject.insert("T_Node", null, Values);
        }

        public static int edit(Struct myNode) {
            ContentValues Values = new ContentValues();
            Values.put("IP", myNode.iP);
            Values.put("Mac", myNode.mac);
            Values.put("Serialnumber", myNode.serialNumber);
            Values.put("RoomID", myNode.roomID);
            Values.put("NodeTypeID", myNode.nodeTypeID);
            Values.put("Icon", myNode.icon);
            Values.put("Name", myNode.name);
            Values.put("Status", myNode.status);
            Values.put("ExpDate", myNode.expDate.toString());
            Values.put("BuildNumber", myNode.buildnumber);
            Values.put("OsVer", myNode.osVer);
            Values.put("isVisible", myNode.isVisible);
            Values.put("LastSecKey", myNode.lastSecKey);
            Values.put("isFavorite", myNode.isFavorite);
            Values.put("AvailablePorts", myNode.AvailablePorts);
            Values.put("isIoModuleNode", myNode.isIoModuleNode);

            return G.dbObject.update("T_Node", Values, "ID=" + myNode.iD, null);
        }

        public static int edit(int iD, String iP, String mac, String serialNumber, int roomID, int nodeTypeID, String icon, String name, int status, String expDate, String regDate, String buildNumber, String osVer, int lastSecKey, boolean isFavorite, String availablePorts, int isIoModuleNode, boolean isVisible) {
            ContentValues Values = new ContentValues();
            Values.put("IP", iP);
            Values.put("Mac", mac);
            Values.put("Serialnumber", serialNumber);
            Values.put("RoomID", roomID);
            Values.put("NodeTypeID", nodeTypeID);
            Values.put("Icon", icon);
            Values.put("Name", name);
            Values.put("Status", status);
            Values.put("ExpDate", expDate);
            Values.put("RegDate", regDate);
            Values.put("BuildNumber", buildNumber);
            Values.put("OsVer", osVer);
            Values.put("isVisible", isVisible);
            Values.put("LastSecKey", lastSecKey);
            Values.put("isFavorite", isFavorite);
            Values.put("AvailablePorts", availablePorts);
            Values.put("isIoModuleNode", isIoModuleNode);
            return G.dbObject.update("T_Node", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Node", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Node", "", null);
        }

        public static int delete(String whereCriteria) {
            return G.dbObject.delete("T_Node", whereCriteria, null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Node WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.iP = cursor.getString(cursor.getColumnIndex("IP"));
                selectedRow.mac = cursor.getString(cursor.getColumnIndex("Mac"));
                selectedRow.roomID = cursor.getInt(cursor.getColumnIndex("RoomID"));
                selectedRow.nodeTypeID = cursor.getInt(cursor.getColumnIndex("NodeTypeID"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.status = cursor.getInt(cursor.getColumnIndex("Status"));
                selectedRow.expDate = cursor.getString(cursor.getColumnIndex("ExpDate"));
                selectedRow.regDate = cursor.getString(cursor.getColumnIndex("RegDate"));
                selectedRow.serialNumber = cursor.getString(cursor.getColumnIndex("SerialNumber"));
                selectedRow.buildnumber = cursor.getString(cursor.getColumnIndex("BuildNumber"));
                selectedRow.osVer = cursor.getString(cursor.getColumnIndex("OsVer"));
                selectedRow.lastSecKey = cursor.getInt(cursor.getColumnIndex("LastSecKey"));
                selectedRow.isFavorite = cursor.getInt(cursor.getColumnIndex("isFavorite")) != 0;
                selectedRow.isVisible = cursor.getInt(cursor.getColumnIndex("isVisible")) != 0;
                selectedRow.AvailablePorts = cursor.getString(cursor.getColumnIndex("AvailablePorts"));
                selectedRow.isIoModuleNode = cursor.getInt(cursor.getColumnIndex("isIoModuleNode"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Node" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.iP = cursor.getString(cursor.getColumnIndex("IP"));
                selectedRow.mac = cursor.getString(cursor.getColumnIndex("Mac"));
                selectedRow.roomID = cursor.getInt(cursor.getColumnIndex("RoomID"));
                selectedRow.nodeTypeID = cursor.getInt(cursor.getColumnIndex("NodeTypeID"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.status = cursor.getInt(cursor.getColumnIndex("Status"));
                selectedRow.expDate = cursor.getString(cursor.getColumnIndex("ExpDate"));
                selectedRow.regDate = cursor.getString(cursor.getColumnIndex("RegDate"));
                selectedRow.serialNumber = cursor.getString(cursor.getColumnIndex("SerialNumber"));
                selectedRow.buildnumber = cursor.getString(cursor.getColumnIndex("BuildNumber"));
                selectedRow.osVer = cursor.getString(cursor.getColumnIndex("OsVer"));
                selectedRow.lastSecKey = cursor.getInt(cursor.getColumnIndex("LastSecKey"));
                selectedRow.isFavorite = cursor.getInt(cursor.getColumnIndex("isFavorite")) != 0;
                selectedRow.isVisible = cursor.getInt(cursor.getColumnIndex("isVisible")) != 0;
                selectedRow.AvailablePorts = cursor.getString(cursor.getColumnIndex("AvailablePorts"));
                selectedRow.isIoModuleNode = cursor.getInt(cursor.getColumnIndex("isIoModuleNode"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Node" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.iP = cursor.getString(cursor.getColumnIndex("IP"));
                selectedRow.mac = cursor.getString(cursor.getColumnIndex("Mac"));
                selectedRow.roomID = cursor.getInt(cursor.getColumnIndex("RoomID"));
                selectedRow.nodeTypeID = cursor.getInt(cursor.getColumnIndex("NodeTypeID"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.status = cursor.getInt(cursor.getColumnIndex("Status"));
                selectedRow.expDate = cursor.getString(cursor.getColumnIndex("ExpDate"));
                selectedRow.regDate = cursor.getString(cursor.getColumnIndex("RegDate"));
                selectedRow.serialNumber = cursor.getString(cursor.getColumnIndex("SerialNumber"));
                selectedRow.buildnumber = cursor.getString(cursor.getColumnIndex("BuildNumber"));
                selectedRow.osVer = cursor.getString(cursor.getColumnIndex("OsVer"));
                selectedRow.lastSecKey = cursor.getInt(cursor.getColumnIndex("LastSecKey"));
                selectedRow.isFavorite = cursor.getInt(cursor.getColumnIndex("isFavorite")) != 0;
                selectedRow.AvailablePorts = cursor.getString(cursor.getColumnIndex("AvailablePorts"));
                selectedRow.isIoModuleNode = cursor.getInt(cursor.getColumnIndex("isIoModuleNode"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    //    public static class Translation {
    //        public static class Struct {
    //            public int    iD           = 0;
    //            public int    langID       = 0;
    //            public int    sentenseID   = 0;
    //            public String sentenseText = "";
    //        }
    //
    //        public static long insert(Struct myTranslation) {
    //            ContentValues Values = new ContentValues();
    //            Values.put("LangID", myTranslation.langID);
    //            Values.put("SentenseID", myTranslation.sentenseID);
    //            Values.put("SentenseText", myTranslation.sentenseText);
    //            return G.dbObject.insert("T_Translation", null, Values);
    //        }
    //        public static long insert(int langID, int sentenseID, String sentenseText) {
    //            ContentValues Values = new ContentValues();
    //            Values.put("LangID", langID);
    //            Values.put("SentenseID", sentenseID);
    //            Values.put("SentenseText", sentenseText);
    //            return G.dbObject.insert("T_Translation", null, Values);
    //        }
    //        public static int edit(Struct myTranslation) {
    //            ContentValues Values = new ContentValues();
    //            Values.put("LangID", myTranslation.langID);
    //            Values.put("SentenseID", myTranslation.sentenseID);
    //            Values.put("SentenseText", myTranslation.sentenseText);
    //            return G.dbObject.update("T_Translation", Values, "ID=" + myTranslation.iD, null);
    //        }
    //        public static int edit(int iD, int langID, int sentenseID, String sentenseText) {
    //            ContentValues Values = new ContentValues();
    //            Values.put("LangID", langID);
    //            Values.put("SentenseID", sentenseID);
    //            Values.put("SentenseText", sentenseText);
    //            return G.dbObject.update("T_Translation", Values, "ID=" + iD, null);
    //        }
    //        public static int delete(int iD) {
    //            return G.dbObject.delete("T_Translation", "ID=" + iD, null);
    //        }
    //        public static int delete() {
    //            return G.dbObject.delete("T_Translation", "", null);
    //        }
    //        public static Struct select(int iD) {
    //            Struct selectedRow = null;
    //            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Translation WHERE ID=" + iD, null);
    //            if (cursor.moveToNext()) {
    //                selectedRow = new Struct();
    //                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
    //                selectedRow.langID = cursor.getInt(cursor.getColumnIndex("LangID"));
    //                selectedRow.sentenseID = cursor.getInt(cursor.getColumnIndex("SentenseID"));
    //                selectedRow.sentenseText = cursor.getString(cursor.getColumnIndex("SentenseText"));
    //            }
    //            try {
    //                cursor.close();
    //            }
    //            catch (Exception e) {
    //                G.printStackTrace(e);
    //            }
    //            return selectedRow;
    //        }
    //
    //
    //        public static Struct[] select(String whereCriteria) {
    //            String whereQuery = "";
    //            if (whereCriteria.trim().length() > 0)
    //                whereQuery = " WHERE " + whereCriteria.trim();
    //            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Translation" + whereQuery, null);
    //            if (cursor.getCount() < 1) {
    //                try {
    //                    cursor.close();
    //                }
    //                catch (Exception e) {
    //                    G.printStackTrace(e);
    //                }
    //                return null;
    //            }
    //            Struct[] result = new Struct[cursor.getCount()];
    //            int i = 0;
    //            while (cursor.moveToNext()) {
    //                Struct selectedRow = new Struct();
    //                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
    //                selectedRow.langID = cursor.getInt(cursor.getColumnIndex("LangID"));
    //                selectedRow.sentenseID = cursor.getInt(cursor.getColumnIndex("SentenseID"));
    //                selectedRow.sentenseText = cursor.getString(cursor.getColumnIndex("SentenseText"));
    //                result[i] = selectedRow;
    //                i++;
    //            }
    //            try {
    //                cursor.close();
    //            }
    //            catch (Exception e) {
    //                G.printStackTrace(e);
    //            }
    //            return result;
    //        }
    //
    //        public static Struct getMax(String field, String whereCriteria) {
    //            String whereQuery = "";
    //            Struct selectedRow = null;
    //            if (whereCriteria.trim().length() > 0)
    //                whereQuery = " WHERE " + whereCriteria.trim();
    //            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Translation" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
    //            if (cursor.moveToNext()) {
    //                selectedRow = new Struct();
    //                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
    //                selectedRow.langID = cursor.getInt(cursor.getColumnIndex("LangID"));
    //                selectedRow.sentenseID = cursor.getInt(cursor.getColumnIndex("SentenseID"));
    //                selectedRow.sentenseText = cursor.getString(cursor.getColumnIndex("SentenseText"));
    //            }
    //            try {
    //                cursor.close();
    //            }
    //            catch (Exception e) {
    //                G.printStackTrace(e);
    //            }
    //            return selectedRow;
    //        }
    //    }

    public static class ValueType {
        public static class Struct {
            public int iD = 0;
            public String name = "";
            public boolean isNumeric = false;
            public boolean isSelectable = false;
            public float startValue = 0;
            public float endValue = 0;
            public float valueStep = 0;
        }

        public static long insert(Struct myValueType) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myValueType.name);
            Values.put("IsNumeric", myValueType.isNumeric);
            Values.put("IsSelectable", myValueType.isSelectable);
            Values.put("StartValue", myValueType.startValue);
            Values.put("EndValue", myValueType.endValue);
            Values.put("ValueStep", myValueType.valueStep);
            return G.dbObject.insert("T_ValueType", null, Values);
        }

        public static long insert(String name, boolean isNumeric, boolean isSelectable, float startValue, float endValue, float valueStep) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("IsNumeric", isNumeric);
            Values.put("IsSelectable", isSelectable);
            Values.put("StartValue", startValue);
            Values.put("EndValue", endValue);
            Values.put("ValueStep", valueStep);
            return G.dbObject.insert("T_ValueType", null, Values);
        }

        public static int edit(Struct myValueType) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myValueType.name);
            Values.put("IsNumeric", myValueType.isNumeric);
            Values.put("IsSelectable", myValueType.isSelectable);
            Values.put("StartValue", myValueType.startValue);
            Values.put("EndValue", myValueType.endValue);
            Values.put("ValueStep", myValueType.valueStep);
            return G.dbObject.update("T_ValueType", Values, "ID=" + myValueType.iD, null);
        }

        public static int edit(int iD, String name, boolean isNumeric, boolean isSelectable, float startValue, float endValue, float valueStep) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("IsNumeric", isNumeric);
            Values.put("IsSelectable", isSelectable);
            Values.put("StartValue", startValue);
            Values.put("EndValue", endValue);
            Values.put("ValueStep", valueStep);
            return G.dbObject.update("T_ValueType", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_ValueType", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_ValueType", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_ValueType WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.isNumeric = cursor.getInt(cursor.getColumnIndex("IsNumeric")) != 0;
                selectedRow.isSelectable = cursor.getInt(cursor.getColumnIndex("IsSelectable")) != 0;
                selectedRow.startValue = cursor.getFloat(cursor.getColumnIndex("StartValue"));
                selectedRow.endValue = cursor.getFloat(cursor.getColumnIndex("EndValue"));
                selectedRow.valueStep = cursor.getFloat(cursor.getColumnIndex("ValueStep"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_ValueType" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.isNumeric = cursor.getInt(cursor.getColumnIndex("IsNumeric")) != 0;
                selectedRow.isSelectable = cursor.getInt(cursor.getColumnIndex("IsSelectable")) != 0;
                selectedRow.startValue = cursor.getFloat(cursor.getColumnIndex("StartValue"));
                selectedRow.endValue = cursor.getFloat(cursor.getColumnIndex("EndValue"));
                selectedRow.valueStep = cursor.getFloat(cursor.getColumnIndex("ValueStep"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_ValueType" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.isNumeric = cursor.getInt(cursor.getColumnIndex("IsNumeric")) != 0;
                selectedRow.isSelectable = cursor.getInt(cursor.getColumnIndex("IsSelectable")) != 0;
                selectedRow.startValue = cursor.getFloat(cursor.getColumnIndex("StartValue"));
                selectedRow.endValue = cursor.getFloat(cursor.getColumnIndex("EndValue"));
                selectedRow.valueStep = cursor.getFloat(cursor.getColumnIndex("ValueStep"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Scenario {
        public static class Struct {
            public int iD = 0;
            public String name = "";
            public boolean active = false;
            public String des = "";
            public boolean opTimeBase = false;
            public String months = "M0;M1;M2;M3;M4;M5;M6;M7;M8;M9;M10;M11;";
            public String monthDays = "D0;D1;D2;D3;D4;D5;D6;D7;D8;D9;D10;D11;D12;D13;D14;D15;D16;D17;D18;D19;D20;D21;D22;D23;D24;D25;D26;D27;D28;D29;D30;";
            public String weekDays = "WD7;WD1;WD2;WD3;WD4;WD5;WD6;";
            public String startHour = "";
            public boolean opPreSwitchBase = false;
            public boolean opPreGPS = false;
            public boolean opPreSMS = false;
            public String gPS_Params = "";
            public boolean opPreWeather = false;
            public String weatherTypes = "";
            public boolean opPreTemperature = false;
            public int minTemperature = 0;
            public int maxTemperature = 0;
            public String preSMSKeywords = "";
            public boolean opResultSwitch = false;
            public boolean opResultNotify = false;
            public String notifyMobileIDs = "";
            public String notifyText = "";
            public boolean opResultSMS = false;
            public String mobileNumbers = "";
            public String sMStext = "";
            public boolean isStarted = false;
            public boolean isStartedOnGPS = false;
            public boolean opPreAND = false;
            public boolean hasEdited = false;

            public String getDetailsInHTML(int type) {

                String colorHead = "#8CBF45";
                String colorText = "#ffffff";
                String colorAlarm = "#8CBF45";

                String ms = "";


                //                <html>
                //                <head>
                //                	<title></title>
                //                </head>
                //                <body>
                //                String ms = "";

                ms += "<html>\n";
                ms += "<head>\n";
                ms += "<title></title>\n";
                ms += "</head>\n";
                ms += "<body>\n";

                switch (type) {
                    case 0: // Description
                        //                <h2>نام سناريو :</h2>
                        ms += "<small><b>";
                        ms += "<font color=\"" + colorHead + "\">\n";
                        ms += G.T.getSentence(601) + " :\n";
                        ms += "</font>\n";
                        ms += "</b></small><br>";

                        //     <p>سناريو يک</p>
                        ms += "<small><strong>";
                        ms += "<font color=\"" + colorText + "\">\n";
                        ms += name;
                        ms += "</font>\n";
                        ms += "</strong></small><br>";

                        //	 	<h2>توضيحات :</h2>
                        ms += "<br><small><font color=\"" + colorHead + "\">\n";
                        ms += G.T.getSentence(602) + " :\n";
                        ms += "</font></small><br>\n";

                        //	    <p>توضيحات سناريو ....</p>
                        ms += "<small><small><font color=\"" + colorText + "\">\n";
                        ms += des;
                        ms += "</font></small></small><br>\n";
                        ms += "<small><small><font color=\"" + colorHead + "\">\n";


                        if (opPreAND) {
                            String temp = "";
                            String logicKeyword = " " + G.T.getSentence(622) + " ";
                            if (opTimeBase)
                                temp += G.T.getSentence(604) + logicKeyword;
                            if (opPreSwitchBase)
                                temp += G.T.getSentence(605) + logicKeyword;
                            if (opPreGPS)
                                temp += G.T.getSentence(606) + logicKeyword;
                            if (opPreWeather)
                                temp += G.T.getSentence(609) + logicKeyword;
                            if (temp.length() > 0)
                                temp = temp.substring(0, temp.length() - logicKeyword.length());
                            temp = "</font><font color=\"" + colorText + "\">" + temp + "</font><font color=\"" + colorHead + "\">";
                            ms += G.T.getSentence(620).replace("[1]", temp);
                        } else //  OR
                        {
                            String temp = "";
                            String logicKeyword = " " + G.T.getSentence(623) + " ";
                            if (opTimeBase)
                                temp += G.T.getSentence(604) + logicKeyword;
                            if (opPreSwitchBase)
                                temp += G.T.getSentence(605) + logicKeyword;
                            if (opPreGPS)
                                temp += G.T.getSentence(606) + logicKeyword;
                            if (opPreWeather)
                                temp += G.T.getSentence(609) + logicKeyword;
                            if (temp.length() > 0)
                                temp = temp.substring(0, temp.length() - logicKeyword.length());
                            temp = "</font><font color=\"" + colorText + "\">" + temp + "</font><font color=\"" + colorHead + "\">";
                            ms += G.T.getSentence(621).replace("[1]", temp);
                        }
                        ms += "</font></small></small>\n";
                        break;

                    case 1: //  Conditions
                        //              <h2>
                        //              <font color="#FF0000">
                        //              شرايط سناريو:
                        //              </font>
                        //              </h2>
                        /***********************/
                        //	                ms += "<small><p>\n";
                        //	                ms += "<font color=" + "\"" + colorAlarm + "\">\n";
                        //	                ms += G.T.getSentence(603) + ": \n"; // شرايط سناريو
                        //	                ms += "</font>\n";
                        //	                ms += "</p></small>\n";
                        /*************************/

                        if (opTimeBase) {
                            //                <h3>
                            //                <font color="#000080">
                            //                شرط زمان :
                            //                </font>
                            //                </h3>
                            ms += "<small>\n";
                            ms += "<font color=" + "\"" + colorHead + "\">\n";
                            ms += G.T.getSentence(604) + " :"; // شرط زمان
                            ms += "</font>\n";
                            ms += "</small><br>\n";

                            //                <p><b>
                            //                روز هاي ماه :(
                            //                </b>
                            //                1،2،3،4،5،6،7....
                            //                <b>
                            //                )
                            //                </b><br />
                            if (monthDays.length() > 0) {
                                ms += "<small><small><p><font color=\"" + colorText + "\">\n";
                                ms += "<b>\n";
                                ms += G.T.getSentence(517) + " :( \n";// روزهاي  ماه
                                String md[] = monthDays.substring(0, monthDays.length() - 1).split(";");
                                ms += "</b>\n";
                                String temp = "";
                                for (int i = 0; i < md.length; i++) {
                                    try {
                                        temp += (Integer.parseInt(md[i].replace("D", "")) + 1) + " ,";
                                    } catch (Exception e) {
                                    }
                                }
                                if (temp.length() > 0)
                                    ms += temp.substring(0, temp.length() - 1);
                                ms += "<b>)</b>";
                                ms += "</font></p></small></small>\n";
                            }
                            //                <b>
                            //                روزهاي هفته : (
                            //                </b>
                            //                شنبه،يکشنبه،دوشنبه....
                            //                <b>
                            //                )
                            //                </b><br />

                            if (weekDays.length() > 0) {
                                ms += "<small><small><p><font color=\"" + colorText + "\">\n";
                                ms += "<b>\n";
                                ms += G.T.getSentence(518) + " :( \n";
                                String wd[] = weekDays.substring(0, weekDays.length() - 1).split(";");
                                ms += "</b>\n";
                                String temp = "";
                                for (int i = 0; i < wd.length; i++) {
                                    try {
                                        temp += G.T.getSentence(Integer.parseInt(wd[i].replace("WD", "")) + 532) + " ,";
                                    } catch (Exception e) {
                                    }
                                }
                                if (temp.length() > 0)
                                    ms += temp.substring(0, temp.length() - 1);
                                ms += "<b>)</b>";
                                ms += "</font></p></small></small>\n";
                            }

                            //                <b>
                            //                ماه هاي سال : (
                            //                </b>
                            //                ژانويه ، فوريه....
                            //                <b>
                            //                )
                            //                </b><br />

                            if (months.length() > 0) {
                                ms += "<small><small><p><font color=\"" + colorText + "\">\n";
                                ms += "<b>\n";
                                ms += G.T.getSentence(519) + " :( \n";
                                String wd[] = months.substring(0, months.length() - 1).split(";");
                                ms += "</b>\n";
                                String temp = "";
                                for (int i = 0; i < wd.length; i++) {
                                    try {
                                        temp += G.T.getSentence(Integer.parseInt(wd[i].replace("M", "")) + 520) + " ,";
                                    } catch (Exception e) {
                                    }
                                }
                                if (temp.length() > 0)
                                    ms += temp.substring(0, temp.length() - 1);
                                ms += "<b>)</b>";
                                ms += "</font></p></small></small>\n";
                            }

                            //                <b>
                            //                ساعت شروع : 
                            //                </b>
                            //                11:25
                            //                </p>

                            ms += "<small><small><font color=\"" + colorText + "\"><b>\n";

                            // ساعت شروع
                            ms += G.T.getSentence(539) + " : \n";
                            ms += "</b>";
                            ms += startHour;
                            ms += "</font></small></small>";
                        }


                        if (opPreSwitchBase) {

                            PreOperand.Struct op[] = PreOperand.select("ScenarioID=" + iD);
                            if (op != null) {
                                ms += "<br><small>\n";
                                ms += "<font color =\"" + colorHead + "\">\n";
                                ms += G.T.getSentence(605) + " : \n"; // شرط تغيير وضعيت تجهيزات
                                ms += "</font>";
                                ms += "</small><br>";
                                ms += "<small><small><font color =\"" + colorText + "\">\n";
                                for (int i = 0; i < op.length; i++) {
                                    ms += op[i].getNodeFullName() + "  " + op[i].operation + "  <b>" + op[i].getValueName() + "</b><br/>";
                                }
                                ms += "</font></small></small><br>";
                            }
                        }

                        if (opPreGPS) {
                            double latitude = 0;
                            double longitude = 0;
                            int radius = 500;
                            int enteringMode = 1;
                            int mobileNumber = -1;
                            String mobileName = "";
                            try {
                                JSONObject gpsJ = new JSONObject(gPS_Params);
                                latitude = gpsJ.getDouble("Latitude");
                                longitude = gpsJ.getDouble("Longitude");
                                radius = gpsJ.getInt("Radius");
                                enteringMode = gpsJ.getInt("EnteringMode"); // 1= Enter   2= Exit
                                mobileNumber = gpsJ.getInt("MobileID");
                                mobileName = Mobiles.select(mobileNumber).name;
                            } catch (Exception e) {

                            }

                            //                <h3><span style="color:#000080;">شرط موقعيت جغرافيايي :</span></h3>

                            ms += "<br><small><font color =\"" + colorHead + "\">" + G.T.getSentence(606) + ":</font></small><br>\n";

                            ms += "<small><small><font color =\"" + colorText + "\">";
                            if (enteringMode == 1) {
                                ms += G.T.getSentence(607).replace("[1]", "<u>" + mobileName + " </u>").replace("[2]", "" + radius) + "<br/>\n";
                            } else if (enteringMode == 2) {
                                ms += G.T.getSentence(608).replace("[1]", "<u>" + mobileName + " </u>").replace("[2]", "" + radius) + "<br/>\n";
                            }
                            //                طول جغرافيايي:&nbsp; <b>52.5635435</b><br />
                            ms += G.T.getSentence(572) + " : <b>" + latitude + "</b><br/>\n";
                            //                عرض جغرافيايي:<b> 39.6546544</b><br />
                            ms += G.T.getSentence(573) + " : <b>" + longitude + "</b><br/>\n";
                            ms += "</font></small></small><br>";
                        }
                        if (opPreWeather) {
                            //                <h3><span style="color:#000080;">شرط آب و هوايي:</span></h3>
                            ms += "<br><small><font color =\"" + colorHead + "\">" + G.T.getSentence(609) + ":</font></small><br>\n";

                            //                <p>آب و هوا هاي انتخاب شده :<br />
                            /********************************************/
                            //	                    ms += "<small><font color =\"" + colorText + "\">" + G.T.getSentence(610) + "</font></small><br/>";
                            /********************************************/
                            //                <b>صاف ، ابري ، نيمه ابري و ...</b><br />
                            if (weatherTypes.length() > 0) {
                                ms += "<small><small><font color=\"" + colorText + "\">\n";
                                //	                        ms += "<b>\n";
                                String wd[] = weatherTypes.substring(0, weatherTypes.length() - 1).split(";");
                                //	                        ms += "</b>\n";
                                String temp = "";
                                for (int i = 0; i < wd.length; i++) {
                                    try {
                                        temp += G.T.getSentence(Integer.parseInt(wd[i].replace("W", "")) + 400) + " ,";
                                    } catch (Exception e) {
                                    }
                                }
                                if (temp.length() > 0)
                                    ms += temp.substring(0, temp.length() - 1);
                                ms += "</font></small></small><br>\n";
                            }

                            if (opPreTemperature) {
                                //                دماي هوا بين <b>25 </b>تا <b>30 </b>درجه سانتيگراد باشد.</p>
                                ms += "<small><small><font color=\"" + colorText + "\">\n";
                                ms += G.T.getSentence(611).replace("[1]", "<b>" + minTemperature + "</b>").replace("[2]", "<b>" + maxTemperature + "</b>") + "\n";
                                ms += "</font></small></small><br>\n";
                            }
                        }
                        break;
                    case 2: // Results

                        //                <h2><span style="color:#FF0000;">نتايج سناريو:</span></h2>

                        /****************************************************/
                        //	                ms += "<h2><font color =\"" + colorAlarm + "\">" + G.T.getSentence(612) + ":</font></h2>\n";
                        /***************************************************/
                        if (opResultSwitch) {
                            Results.Struct[] rs = Results.select("ScenarioID=" + iD);
                            if (rs != null) {
                                //                      <h3><span style="color:#000080;">تغيير وضعيت دستگاه ها:</span></h3>
                                ms += "<small><font color =\"" + colorHead + "\">" + G.T.getSentence(613) + ":</font></small><br>\n";
                                ms += "<small><small><font color =\"" + colorText + "\">";
                                String temp = "";
                                for (int i = 0; i < rs.length; i++) {
                                    temp += rs[i].getNodeFullName() + " = " + rs[i].getValueName() + "<br/>";
                                    if (rs[i].reverseTime > 0)
                                        temp += G.T.getSentence(511) + " " + Utility.readTime(rs[i].reverseTime) + "<br/>";
                                }
                                ms += temp;
                                ms += "</font></small></small>";
                            }

                        }
                        if (opResultNotify) {
                            /// ersal hoshdar
                            ms += "<small><font color =\"" + colorHead + "\">" + G.T.getSentence(618) + ":</font></small><br>\n";
                            String tmp_mns = notifyMobileIDs.replace(";", ",");
                            if (tmp_mns.length() > 1)
                                tmp_mns = tmp_mns.substring(0, tmp_mns.length() - 1);
                            Mobiles.Struct[] mobiles = Mobiles.select("ID in(" + tmp_mns + ")");
                            String temp = "";
                            if (mobiles != null) {
                                for (int i = 0; i < mobiles.length; i++) {
                                    temp += "<u>" + mobiles[i].name + "</u> ,";
                                }
                            }
                            if (temp.length() > 0)
                                temp = temp.substring(0, temp.length() - 1);
                            ///matn zir onvan ersal hoshdar
                            ms += "<small><small><font color =\"" + colorText + "\">" + G.T.getSentence(615).replace("[1]", temp) + "</font></small></small><br/>\n";
                            //                متن هشدار</p>
                            ms += "<small><small><font color =\"" + colorText + "\">" + notifyText + "</font></small></small><br>\n";
                        }
                        if (opResultSMS) {
                            if (mobileNumbers.length() > 1) {
                                ms += "<small><font color =\"" + colorHead + "\">" + G.T.getSentence(619) + ":</font></small><br>\n";
                                String tmp_mns = "";
                                tmp_mns = mobileNumbers.replace(";", ",");
                                tmp_mns = tmp_mns.substring(0, tmp_mns.length() - 1);
                                ms += "<small><small><font color =\"" + colorText + "\">" + G.T.getSentence(616).replace("[1]", "<u>" + tmp_mns + "</u>") + "<br/>\n";
                                ms += sMStext + "</font></small></small>\n";
                            }
                        }
                        //                <h2><span style="color:#006400;">شما با ارسال متن هاي زير به صورت پيامک مي توانيد اين سناريو را کنترل نماييد.</span></h2>
                        ms += "<br/><small><font color =\"" + colorAlarm + "\">" + G.T.getSentence(617) + "</font></small><br>\n";
                        ms += "<small><small><font color =\"" + colorText + "\">";
                        ms += G.T.getSentence(559) + ":    ";
                        ms += "<b>" + preSMSKeywords + "*start" + "</b><br/>\n";
                        ms += G.T.getSentence(570) + ":    ";
                        ms += "<b>" + preSMSKeywords + "*on" + "</b><br/>\n";
                        ms += G.T.getSentence(571) + ":    ";
                        ms += "<b>" + preSMSKeywords + "*off" + "</b><br/>\n";
                        ms += "</font></small></small>";
                        break;

                    default:
                        //                <h2>نام سناريو :</h2>
                        //                <p>سناريو يک</p>
                        //                <h2>توضيحات :</h2>
                        //                <p>توضيحات سناريو ....</p>

                        ms += "<h2><font color=\"" + colorHead + "\">\n";
                        ms += G.T.getSentence(601) + " :\n";
                        ms += "</font></h2>\n";
                        ms += "<p><font color=\"" + colorText + "\">\n";
                        ms += name;
                        ms += "</font></p>\n";
                        ms += "<h2><font color=\"" + colorHead + "\">\n";
                        ms += G.T.getSentence(602) + " :\n";
                        ms += "</font></h2>\n";
                        ms += "<p><font color=\"" + colorText + "\">\n";
                        ms += des;
                        ms += "</font></p>\n";

                        ms += "<h3><font color=\"" + colorHead + "\">\n";
                        if (opPreAND) {
                            String temp = "";
                            String logicKeyword = " " + G.T.getSentence(622) + " ";
                            if (opTimeBase)
                                temp += G.T.getSentence(604) + logicKeyword;
                            if (opPreSwitchBase)
                                temp += G.T.getSentence(605) + logicKeyword;
                            if (opPreGPS)
                                temp += G.T.getSentence(606) + logicKeyword;
                            if (opPreWeather)
                                temp += G.T.getSentence(609) + logicKeyword;
                            if (temp.length() > 0)
                                temp = temp.substring(0, temp.length() - logicKeyword.length());
                            temp = "</font><font color=\"" + colorText + "\">" + temp + "</font><font color=\"" + colorHead + "\">";
                            ms += G.T.getSentence(620).replace("[1]", temp);
                        } else //  OR
                        {
                            String temp = "";
                            String logicKeyword = " " + G.T.getSentence(623) + " ";
                            if (opTimeBase)
                                temp += G.T.getSentence(604) + logicKeyword;
                            if (opPreSwitchBase)
                                temp += G.T.getSentence(605) + logicKeyword;
                            if (opPreGPS)
                                temp += G.T.getSentence(606) + logicKeyword;
                            if (opPreWeather)
                                temp += G.T.getSentence(609) + logicKeyword;
                            if (temp.length() > 0)
                                temp = temp.substring(0, temp.length() - logicKeyword.length());
                            temp = "</font><font color=\"" + colorText + "\">" + temp + "</font><font color=\"" + colorHead + "\">";
                            ms += G.T.getSentence(621).replace("[1]", temp);
                        }
                        ms += "</font></h3>\n";
                        //              <h2>
                        //              <font color="#FF0000">
                        //              شرايط سناريو:
                        //              </font>
                        //              </h2>

                        ms += "<h2>\n";
                        ms += "<font color=" + "\"" + colorAlarm + "\">\n";
                        ms += G.T.getSentence(603) + " : \n"; // شرايط سناريو
                        ms += "</font>\n";
                        ms += "</h2>\n";


                        if (opTimeBase) {
                            //                <h3>
                            //                <font color="#000080">
                            //                شرط زمان :
                            //                </font>
                            //                </h3>
                            ms += "<h3>\n";
                            ms += "<font color=" + "\"" + colorHead + "\">\n";
                            ms += G.T.getSentence(604) + " :"; // شرط زمان
                            ms += "</font>\n";
                            ms += "</h3>\n";

                            //                <p><b>
                            //                روز هاي ماه :(
                            //                </b>
                            //                1،2،3،4،5،6،7....
                            //                <b>
                            //                )
                            //                </b><br />
                            if (monthDays.length() > 0) {
                                ms += "<p><font color=\"" + colorText + "\">\n";
                                ms += "<b>\n";
                                ms += G.T.getSentence(517) + " :( \n";// روزهاي  ماه
                                String md[] = monthDays.substring(0, monthDays.length() - 1).split(";");
                                ms += "</b>\n";
                                String temp = "";
                                for (int i = 0; i < md.length; i++) {
                                    try {
                                        temp += (Integer.parseInt(md[i].replace("D", "")) + 1) + " ,";
                                    } catch (Exception e) {
                                    }
                                }
                                if (temp.length() > 0)
                                    ms += temp.substring(0, temp.length() - 1);
                                ms += "<b>)</b>";
                                ms += "</font></p>\n";
                            }
                            //                <b>
                            //                روزهاي هفته : (
                            //                </b>
                            //                شنبه،يکشنبه،دوشنبه....
                            //                <b>
                            //                )
                            //                </b><br />

                            if (weekDays.length() > 0) {
                                ms += "<p><font color=\"" + colorText + "\">\n";
                                ms += "<b>\n";
                                ms += G.T.getSentence(518) + " :( \n";
                                String wd[] = weekDays.substring(0, weekDays.length() - 1).split(";");
                                ms += "</b>\n";
                                String temp = "";
                                for (int i = 0; i < wd.length; i++) {
                                    try {
                                        temp += G.T.getSentence(Integer.parseInt(wd[i].replace("WD", "")) + 532) + " ,";
                                    } catch (Exception e) {
                                    }
                                }
                                if (temp.length() > 0)
                                    ms += temp.substring(0, temp.length() - 1);
                                ms += "<b>)</b>";
                                ms += "</font></p>\n";
                            }

                            //                <b>
                            //                ماه هاي سال : (
                            //                </b>
                            //                ژانويه ، فوريه....
                            //                <b>
                            //                )
                            //                </b><br />

                            if (months.length() > 0) {
                                ms += "<p><font color=\"" + colorText + "\">\n";
                                ms += "<b>\n";
                                ms += G.T.getSentence(519) + " :( \n";
                                String wd[] = months.substring(0, months.length() - 1).split(";");
                                ms += "</b>\n";
                                String temp = "";
                                for (int i = 0; i < wd.length; i++) {
                                    try {
                                        temp += G.T.getSentence(Integer.parseInt(wd[i].replace("M", "")) + 520) + " ,";
                                    } catch (Exception e) {
                                    }
                                }
                                if (temp.length() > 0)
                                    ms += temp.substring(0, temp.length() - 1);
                                ms += "<b>)</b>";
                                ms += "</font></p>\n";
                            }

                            //                <b>
                            //                ساعت شروع : 
                            //                </b>
                            //                11:25
                            //                </p>

                            ms += "<font color=\"" + colorText + "\"><b>\n";
                            ms += G.T.getSentence(539) + " : \n"; // ساعت شروع
                            ms += "</b>";
                            ms += startHour;
                            ms += "</font>";
                        }


                        if (opPreSwitchBase) {

                            PreOperand.Struct op[] = PreOperand.select("ScenarioID=" + iD);
                            if (op != null) {
                                ms += "<h3>\n";
                                ms += "<font color =\"" + colorHead + "\">\n";
                                ms += G.T.getSentence(605) + " : \n"; // شرط تغيير وضعيت تجهيزات
                                ms += "</font>";
                                ms += "</h3>";
                                ms += "<font color =\"" + colorText + "\">\n";
                                for (int i = 0; i < op.length; i++) {
                                    ms += op[i].getNodeFullName() + "  " + op[i].operation + "  <b>" + op[i].getValueName() + "</b><br/>";
                                }
                                ms += "</font>";
                            }
                        }

                        if (opPreGPS) {
                            double latitude = 0;
                            double longitude = 0;
                            int radius = 500;
                            int enteringMode = 1;
                            int mobileNumber = -1;
                            String mobileName = "";
                            try {
                                JSONObject gpsJ = new JSONObject(gPS_Params);
                                latitude = gpsJ.getDouble("Latitude");
                                longitude = gpsJ.getDouble("Longitude");
                                radius = gpsJ.getInt("Radius");
                                enteringMode = gpsJ.getInt("EnteringMode"); // 1= Enter   2= Exit
                                mobileNumber = gpsJ.getInt("MobileID");
                                mobileName = Mobiles.select(mobileNumber).name;
                            } catch (Exception e) {

                            }

                            //                <h3><span style="color:#000080;">شرط موقعيت جغرافيايي :</span></h3>
                            ms += "<h3><font color =\"" + colorHead + "\">" + G.T.getSentence(606) + ":</font></h3>\n";
                            ms += "<font color =\"" + colorText + "\">";
                            if (enteringMode == 1) {
                                ms += G.T.getSentence(607).replace("[1]", "<u>" + mobileName + " </u>").replace("[2]", "" + radius) + "<br/>\n";
                            } else if (enteringMode == 2) {
                                ms += G.T.getSentence(608).replace("[1]", "<u>" + mobileName + " </u>").replace("[2]", "" + radius) + "<br/>\n";
                            }
                            //                طول جغرافيايي:&nbsp; <b>52.5635435</b><br />
                            ms += G.T.getSentence(572) + " : <b>" + latitude + "</b><br/>\n";
                            //                عرض جغرافيايي:<b> 39.6546544</b><br />
                            ms += G.T.getSentence(573) + " : <b>" + longitude + "</b><br/>\n";
                            ms += "</font>";
                        }
                        if (opPreWeather) {
                            //                <h3><span style="color:#000080;">شرط آب و هوايي:</span></h3>
                            ms += "<h3><font color =\"" + colorHead + "\">" + G.T.getSentence(609) + ":</font></h3>\n";
                            //                <p>آب و هوا هاي انتخاب شده :<br />
                            ms += "<small><p><font color =\"" + colorText + "\">" + G.T.getSentence(610) + "</font></small><br/>";
                            //                <b>صاف ، ابري ، نيمه ابري و ...</b><br />
                            if (weatherTypes.length() > 0) {
                                ms += "<small><font color=\"" + colorText + "\">\n";
                                //		                        ms += "<b>\n";
                                String wd[] = weatherTypes.substring(0, weatherTypes.length() - 1).split(";");
                                //		                        ms += "</b>\n";
                                String temp = "";
                                for (int i = 0; i < wd.length; i++) {
                                    try {
                                        temp += G.T.getSentence(Integer.parseInt(wd[i].replace("W", "")) + 400) + " ,";
                                    } catch (Exception e) {
                                    }
                                }
                                if (temp.length() > 0)
                                    ms += temp.substring(0, temp.length() - 1);
                                ms += "</font></small>\n";
                            }

                            if (opPreTemperature) {
                                //                دماي هوا بين <b>25 </b>تا <b>30 </b>درجه سانتيگراد باشد.</p>
                                ms += "<p><font color=\"" + colorText + "\">\n";
                                ms += G.T.getSentence(611).replace("[1]", "<b>" + minTemperature + "</b>").replace("[2]", "<b>" + maxTemperature + "</b>") + "\n";
                                ms += "</font></p>\n";
                            }
                        }
                        //		                <h2><span style="color:#FF0000;">نتايج سناريو:</span></h2>
                        ms += "<h2><font color =\"" + colorAlarm + "\">" + G.T.getSentence(612) + ":</font></h2>\n";
                        if (opResultSwitch) {
                            Results.Struct[] rs = Results.select("ScenarioID=" + iD);
                            if (rs != null) {
                                //                      <h3><span style="color:#000080;">تغيير وضعيت دستگاه ها:</span></h3>
                                ms += "<h3><font color =\"" + colorHead + "\">" + G.T.getSentence(613) + ":</font></h3>\n";
                                ms += "<font color =\"" + colorText + "\">";
                                String temp = "";
                                for (int i = 0; i < rs.length; i++) {
                                    temp += rs[i].getNodeFullName() + " = " + rs[i].getValueName() + "<br/>";
                                    if (rs[i].reverseTime > 0)
                                        temp += G.T.getSentence(511) + " " + Utility.readTime(rs[i].reverseTime) + "<br/>";
                                }
                                ms += temp;
                                ms += "</font>";
                            }

                        }
                        if (opResultNotify) {
                            ms += "<h3><font color =\"" + colorHead + "\">" + G.T.getSentence(618) + ":</font></h3>\n";
                            ms += "<p>";
                            String tmp_mns = notifyMobileIDs.replace(";", ",");
                            if (tmp_mns.length() > 1)
                                tmp_mns = tmp_mns.substring(0, tmp_mns.length() - 1);
                            Mobiles.Struct[] mobiles = Mobiles.select("ID in(" + tmp_mns + ")");
                            String temp = "";
                            if (mobiles != null) {
                                for (int i = 0; i < mobiles.length; i++) {
                                    temp += "<u>" + mobiles[i].name + "</u> ,";
                                }
                            }
                            if (temp.length() > 0)
                                temp = temp.substring(0, temp.length() - 1);
                            ms += "<font color =\"" + colorText + "\">" + G.T.getSentence(615).replace("[1]", temp) + "<br/>\n";
                            //                متن هشدار</p>
                            ms += notifyText + "</font></p><br>\n";
                        }
                        if (opResultSMS) {
                            if (mobileNumbers.length() > 1) {
                                ms += "<h3><font color =\"" + colorHead + "\">" + G.T.getSentence(619) + ":</font></h3>\n";
                                ms += "<p>";
                                String tmp_mns = "";
                                tmp_mns = mobileNumbers.replace(";", ",");
                                tmp_mns = tmp_mns.substring(0, tmp_mns.length() - 1);
                                ms += "<font color =\"" + colorText + "\">" + G.T.getSentence(616).replace("[1]", "<u>" + tmp_mns + "</u>") + "<br/>\n";
                                ms += sMStext + "</font></p>\n";
                            }
                        }
                        //                <h2><span style="color:#006400;">شما با ارسال متن هاي زير به صورت پيامک مي توانيد اين سناريو را کنترل نماييد.</span></h2>
                        ms += "<br/><small><font color =\"" + colorAlarm + "\">" + G.T.getSentence(617) + "</font></small><br>\n";
                        ms += "<small><font color =\"" + colorText + "\">";
                        ms += G.T.getSentence(559) + ":    ";
                        ms += "<b>" + preSMSKeywords + "*start" + "</b><br/>\n";
                        ms += G.T.getSentence(570) + ":    ";
                        ms += "<b>" + preSMSKeywords + "*on" + "</b><br/>\n";
                        ms += G.T.getSentence(571) + ":    ";
                        ms += "<b>" + preSMSKeywords + "*off" + "</b><br/>\n";
                        ms += "</font></small>";
                        break;

                }
                //                </body></html>
                ms += "</body></html>\n";
                return ms;

            }

            public String getScenarioDataJSON() {
                JSONArray ja = new JSONArray();
                try {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("ID", iD);
                    jsonObj.put("Name", name);
                    jsonObj.put("Des", des);
                    if (opPreGPS)
                        jsonObj.put("GPS_Params", gPS_Params);
                    else
                        jsonObj.put("GPS_Params", "");
                    jsonObj.put("DetailsDescription", getDetailsInHTML(0));
                    jsonObj.put("DetailsConditions", getDetailsInHTML(1));
                    jsonObj.put("DetailsResults", getDetailsInHTML(2));
                    boolean hasActivePre, hasActiveResult;
                    hasActivePre = opTimeBase || opPreGPS || opPreSwitchBase || opPreTemperature || opPreWeather;
                    hasActiveResult = opResultNotify || opResultSMS || opResultSwitch;
                    if (active)
                        jsonObj.put("Active", 1);
                    else if (hasActivePre && hasActiveResult)
                        jsonObj.put("Active", 0);
                    else
                        jsonObj.put("Active", -1);
                    jsonObj.put("smsKey", preSMSKeywords);
                    ja.put(jsonObj);
                } catch (Exception e) {
                }
                return ja.toString();
            }

            public String getScenarioStatusJSON(boolean isRun) {
                JSONArray ja = new JSONArray();
                try {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("ID", iD);
                    if (isRun)
                        jsonObj.put("Active", 2);
                    else if (active)
                        jsonObj.put("Active", 1);
                    else
                        jsonObj.put("Active", 0);
                    ja.put(jsonObj);
                } catch (Exception e) {
                }
                return ja.toString();
            }
        }

        public static long insert(Struct myScenario) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myScenario.name);
            Values.put("Active", myScenario.active);
            Values.put("Des", myScenario.des);
            Values.put("OpTimeBase", myScenario.opTimeBase);
            Values.put("Months", myScenario.months);
            Values.put("MonthDays", myScenario.monthDays);
            Values.put("WeekDays", myScenario.weekDays);
            Values.put("StartHour", myScenario.startHour);
            Values.put("OpPreSwitchBase", myScenario.opPreSwitchBase);
            Values.put("OpPreGPS", myScenario.opPreGPS);
            Values.put("OpPreSMS", myScenario.opPreSMS);
            Values.put("GPS_Params", myScenario.gPS_Params);
            Values.put("OpPreWeather", myScenario.opPreWeather);
            Values.put("WeatherTypes", myScenario.weatherTypes);
            Values.put("OpPreTemperature", myScenario.opPreTemperature);
            Values.put("MinTemperature", myScenario.minTemperature);
            Values.put("MaxTemperature", myScenario.maxTemperature);
            Values.put("PreSMSKeywords", myScenario.preSMSKeywords);
            Values.put("OpResultSwitch", myScenario.opResultSwitch);
            Values.put("OpResultNotify", myScenario.opResultNotify);
            Values.put("NotifyMobileIDs", myScenario.notifyMobileIDs);
            Values.put("NotifyText", myScenario.notifyText);
            Values.put("OpResultSMS", myScenario.opResultSMS);
            Values.put("Mobilenumbers", myScenario.mobileNumbers);
            Values.put("SMStext", myScenario.sMStext);
            Values.put("isStarted", myScenario.isStarted);
            Values.put("OpPreAND", myScenario.opPreAND);
            Values.put("isStartedOnGPS", myScenario.isStartedOnGPS);
            Values.put("hasEdited", myScenario.hasEdited);
            return G.dbObject.insertOrThrow("T_Scenario", null, Values);
        }

        public static long insert(String name, boolean active, String des, boolean opTimeBase, String months, String monthDays, String weekDays, String startHour, boolean opPreSwitchBase, boolean opPreGPS, boolean opPreSMS, String gPS_Params, boolean opPreWeather, String weatherTypes, boolean opPreTemperature, int minTemperature, int maxTemperature, String preSMSKeywords, boolean opResultSwitch, boolean opResultNotify, String notifyMobileIDs, String notifyText, boolean opResultSMS, String mobilenumbers, String sMStext, boolean isStarted, boolean opPreAND, boolean isStartedOnGPS, boolean hasEdited) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("Active", active);
            Values.put("Des", des);
            Values.put("OpTimeBase", opTimeBase);
            Values.put("Months", months);
            Values.put("MonthDays", monthDays);
            Values.put("WeekDays", weekDays);
            Values.put("StartHour", startHour);
            Values.put("OpPreSwitchBase", opPreSwitchBase);
            Values.put("OpPreGPS", opPreGPS);
            Values.put("OpPreSMS", opPreSMS);
            Values.put("GPS_Params", gPS_Params);
            Values.put("OpPreWeather", opPreWeather);
            Values.put("WeatherTypes", weatherTypes);
            Values.put("OpPreTemperature", opPreTemperature);
            Values.put("MinTemperature", minTemperature);
            Values.put("MaxTemperature", maxTemperature);
            Values.put("PreSMSKeywords", preSMSKeywords);
            Values.put("OpResultSwitch", opResultSwitch);
            Values.put("OpResultNotify", opResultNotify);
            Values.put("NotifyMobileIDs", notifyMobileIDs);
            Values.put("NotifyText", notifyText);
            Values.put("OpResultSMS", opResultSMS);
            Values.put("Mobilenumbers", mobilenumbers);
            Values.put("SMStext", sMStext);
            Values.put("isStarted", isStarted);
            Values.put("OpPreAND", opPreAND);
            Values.put("isStartedOnGPS", isStartedOnGPS);
            Values.put("hasEdited", hasEdited);
            return G.dbObject.insert("T_Scenario", null, Values);
        }

        public static int edit(Struct myScenario) {
            ContentValues Values = new ContentValues();
            Values.put("Name", myScenario.name);
            Values.put("Active", myScenario.active);
            Values.put("Des", myScenario.des);
            Values.put("OpTimeBase", myScenario.opTimeBase);
            Values.put("Months", myScenario.months);
            Values.put("MonthDays", myScenario.monthDays);
            Values.put("WeekDays", myScenario.weekDays);
            Values.put("StartHour", myScenario.startHour);
            Values.put("OpPreSwitchBase", myScenario.opPreSwitchBase);
            Values.put("OpPreGPS", myScenario.opPreGPS);
            Values.put("OpPreSMS", myScenario.opPreSMS);
            Values.put("GPS_Params", myScenario.gPS_Params);
            Values.put("OpPreWeather", myScenario.opPreWeather);
            Values.put("WeatherTypes", myScenario.weatherTypes);
            Values.put("OpPreTemperature", myScenario.opPreTemperature);
            Values.put("MinTemperature", myScenario.minTemperature);
            Values.put("MaxTemperature", myScenario.maxTemperature);
            Values.put("PreSMSKeywords", myScenario.preSMSKeywords);
            Values.put("OpResultSwitch", myScenario.opResultSwitch);
            Values.put("OpResultNotify", myScenario.opResultNotify);
            Values.put("NotifyMobileIDs", myScenario.notifyMobileIDs);
            Values.put("NotifyText", myScenario.notifyText);
            Values.put("OpResultSMS", myScenario.opResultSMS);
            Values.put("Mobilenumbers", myScenario.mobileNumbers);
            Values.put("SMStext", myScenario.sMStext);
            Values.put("isStarted", myScenario.isStarted);
            Values.put("OpPreAND", myScenario.opPreAND);
            Values.put("isStartedOnGPS", myScenario.isStartedOnGPS);
            Values.put("hasEdited", myScenario.hasEdited);
            return G.dbObject.update("T_Scenario", Values, "ID=" + myScenario.iD, null);
        }

        public static int edit(int iD, String name, boolean active, String des, boolean opTimeBase, String months, String monthDays, String weekDays, String startHour, boolean opPreSwitchBase, boolean opPreGPS, boolean opPreSMS, String gPS_Params, boolean opPreWeather, String weatherTypes, boolean opPreTemperature, int minTemperature, int maxTemperature, String preSMSKeywords, boolean opResultSwitch, boolean opResultNotify, String notifyMobileIDs, String notifyText, boolean opResultSMS, String mobilenumbers, String sMStext, boolean isStarted, boolean opPreAND, boolean isStartedOnGPS, boolean hasEdited) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            Values.put("Active", active);
            Values.put("Des", des);
            Values.put("OpTimeBase", opTimeBase);
            Values.put("Months", months);
            Values.put("MonthDays", monthDays);
            Values.put("WeekDays", weekDays);
            Values.put("StartHour", startHour);
            Values.put("OpPreSwitchBase", opPreSwitchBase);
            Values.put("OpPreGPS", opPreGPS);
            Values.put("OpPreSMS", opPreSMS);
            Values.put("GPS_Params", gPS_Params);
            Values.put("OpPreWeather", opPreWeather);
            Values.put("WeatherTypes", weatherTypes);
            Values.put("OpPreTemperature", opPreTemperature);
            Values.put("MinTemperature", minTemperature);
            Values.put("MaxTemperature", maxTemperature);
            Values.put("PreSMSKeywords", preSMSKeywords);
            Values.put("OpResultSwitch", opResultSwitch);
            Values.put("OpResultNotify", opResultNotify);
            Values.put("NotifyMobileIDs", notifyMobileIDs);
            Values.put("NotifyText", notifyText);
            Values.put("OpResultSMS", opResultSMS);
            Values.put("Mobilenumbers", mobilenumbers);
            Values.put("SMStext", sMStext);
            Values.put("isStarted", isStarted);
            Values.put("OpPreAND", opPreAND);
            Values.put("isStartedOnGPS", isStartedOnGPS);
            Values.put("hasEdited", hasEdited);
            return G.dbObject.update("T_Scenario", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Scenario", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Scenario", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Scenario WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.active = cursor.getInt(cursor.getColumnIndex("Active")) != 0;
                selectedRow.des = cursor.getString(cursor.getColumnIndex("Des"));
                selectedRow.opTimeBase = cursor.getInt(cursor.getColumnIndex("OpTimeBase")) != 0;
                selectedRow.months = cursor.getString(cursor.getColumnIndex("Months"));
                selectedRow.monthDays = cursor.getString(cursor.getColumnIndex("MonthDays"));
                selectedRow.weekDays = cursor.getString(cursor.getColumnIndex("WeekDays"));
                selectedRow.startHour = cursor.getString(cursor.getColumnIndex("StartHour"));
                selectedRow.opPreSwitchBase = cursor.getInt(cursor.getColumnIndex("OpPreSwitchBase")) != 0;
                selectedRow.opPreGPS = cursor.getInt(cursor.getColumnIndex("OpPreGPS")) != 0;
                selectedRow.opPreSMS = cursor.getInt(cursor.getColumnIndex("OpPreSMS")) != 0;
                selectedRow.gPS_Params = cursor.getString(cursor.getColumnIndex("GPS_Params"));
                selectedRow.opPreWeather = cursor.getInt(cursor.getColumnIndex("OpPreWeather")) != 0;
                selectedRow.weatherTypes = cursor.getString(cursor.getColumnIndex("WeatherTypes"));
                selectedRow.opPreTemperature = cursor.getInt(cursor.getColumnIndex("OpPreTemperature")) != 0;
                selectedRow.minTemperature = cursor.getInt(cursor.getColumnIndex("MinTemperature"));
                selectedRow.maxTemperature = cursor.getInt(cursor.getColumnIndex("MaxTemperature"));
                selectedRow.preSMSKeywords = cursor.getString(cursor.getColumnIndex("PreSMSKeywords"));
                selectedRow.opResultSwitch = cursor.getInt(cursor.getColumnIndex("OpResultSwitch")) != 0;
                selectedRow.opResultNotify = cursor.getInt(cursor.getColumnIndex("OpResultNotify")) != 0;
                selectedRow.notifyMobileIDs = cursor.getString(cursor.getColumnIndex("NotifyMobileIDs"));
                selectedRow.notifyText = cursor.getString(cursor.getColumnIndex("NotifyText"));
                selectedRow.opResultSMS = cursor.getInt(cursor.getColumnIndex("OpResultSMS")) != 0;
                selectedRow.mobileNumbers = cursor.getString(cursor.getColumnIndex("Mobilenumbers"));
                selectedRow.sMStext = cursor.getString(cursor.getColumnIndex("SMStext"));
                selectedRow.isStarted = cursor.getInt(cursor.getColumnIndex("isStarted")) != 0;
                selectedRow.opPreAND = cursor.getInt(cursor.getColumnIndex("OpPreAND")) != 0;
                selectedRow.isStartedOnGPS = cursor.getInt(cursor.getColumnIndex("isStartedOnGPS")) != 0;
                selectedRow.hasEdited = cursor.getInt(cursor.getColumnIndex("hasEdited")) != 0;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Scenario" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.active = cursor.getInt(cursor.getColumnIndex("Active")) != 0;
                selectedRow.des = cursor.getString(cursor.getColumnIndex("Des"));
                selectedRow.opTimeBase = cursor.getInt(cursor.getColumnIndex("OpTimeBase")) != 0;
                selectedRow.months = cursor.getString(cursor.getColumnIndex("Months"));
                selectedRow.monthDays = cursor.getString(cursor.getColumnIndex("MonthDays"));
                selectedRow.weekDays = cursor.getString(cursor.getColumnIndex("WeekDays"));
                selectedRow.startHour = cursor.getString(cursor.getColumnIndex("StartHour"));
                selectedRow.opPreSwitchBase = cursor.getInt(cursor.getColumnIndex("OpPreSwitchBase")) != 0;
                selectedRow.opPreGPS = cursor.getInt(cursor.getColumnIndex("OpPreGPS")) != 0;
                selectedRow.opPreSMS = cursor.getInt(cursor.getColumnIndex("OpPreSMS")) != 0;
                selectedRow.gPS_Params = cursor.getString(cursor.getColumnIndex("GPS_Params"));
                selectedRow.opPreWeather = cursor.getInt(cursor.getColumnIndex("OpPreWeather")) != 0;
                selectedRow.weatherTypes = cursor.getString(cursor.getColumnIndex("WeatherTypes"));
                selectedRow.opPreTemperature = cursor.getInt(cursor.getColumnIndex("OpPreTemperature")) != 0;
                selectedRow.minTemperature = cursor.getInt(cursor.getColumnIndex("MinTemperature"));
                selectedRow.maxTemperature = cursor.getInt(cursor.getColumnIndex("MaxTemperature"));
                selectedRow.preSMSKeywords = cursor.getString(cursor.getColumnIndex("PreSMSKeywords"));
                selectedRow.opResultSwitch = cursor.getInt(cursor.getColumnIndex("OpResultSwitch")) != 0;
                selectedRow.opResultNotify = cursor.getInt(cursor.getColumnIndex("OpResultNotify")) != 0;
                selectedRow.notifyMobileIDs = cursor.getString(cursor.getColumnIndex("NotifyMobileIDs"));
                selectedRow.notifyText = cursor.getString(cursor.getColumnIndex("NotifyText"));
                selectedRow.opResultSMS = cursor.getInt(cursor.getColumnIndex("OpResultSMS")) != 0;
                selectedRow.mobileNumbers = cursor.getString(cursor.getColumnIndex("Mobilenumbers"));
                selectedRow.sMStext = cursor.getString(cursor.getColumnIndex("SMStext"));
                selectedRow.isStarted = cursor.getInt(cursor.getColumnIndex("isStarted")) != 0;
                selectedRow.opPreAND = cursor.getInt(cursor.getColumnIndex("OpPreAND")) != 0;
                selectedRow.isStartedOnGPS = cursor.getInt(cursor.getColumnIndex("isStartedOnGPS")) != 0;
                selectedRow.hasEdited = cursor.getInt(cursor.getColumnIndex("hasEdited")) != 0;
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Scenario" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.active = cursor.getInt(cursor.getColumnIndex("Active")) != 0;
                selectedRow.des = cursor.getString(cursor.getColumnIndex("Des"));
                selectedRow.opTimeBase = cursor.getInt(cursor.getColumnIndex("OpTimeBase")) != 0;
                selectedRow.months = cursor.getString(cursor.getColumnIndex("Months"));
                selectedRow.monthDays = cursor.getString(cursor.getColumnIndex("MonthDays"));
                selectedRow.weekDays = cursor.getString(cursor.getColumnIndex("WeekDays"));
                selectedRow.startHour = cursor.getString(cursor.getColumnIndex("StartHour"));
                selectedRow.opPreSwitchBase = cursor.getInt(cursor.getColumnIndex("OpPreSwitchBase")) != 0;
                selectedRow.opPreGPS = cursor.getInt(cursor.getColumnIndex("OpPreGPS")) != 0;
                selectedRow.opPreSMS = cursor.getInt(cursor.getColumnIndex("OpPreSMS")) != 0;
                selectedRow.gPS_Params = cursor.getString(cursor.getColumnIndex("GPS_Params"));
                selectedRow.opPreWeather = cursor.getInt(cursor.getColumnIndex("OpPreWeather")) != 0;
                selectedRow.weatherTypes = cursor.getString(cursor.getColumnIndex("WeatherTypes"));
                selectedRow.opPreTemperature = cursor.getInt(cursor.getColumnIndex("OpPreTemperature")) != 0;
                selectedRow.minTemperature = cursor.getInt(cursor.getColumnIndex("MinTemperature"));
                selectedRow.maxTemperature = cursor.getInt(cursor.getColumnIndex("MaxTemperature"));
                selectedRow.preSMSKeywords = cursor.getString(cursor.getColumnIndex("PreSMSKeywords"));
                selectedRow.opResultSwitch = cursor.getInt(cursor.getColumnIndex("OpResultSwitch")) != 0;
                selectedRow.opResultNotify = cursor.getInt(cursor.getColumnIndex("OpResultNotify")) != 0;
                selectedRow.notifyMobileIDs = cursor.getString(cursor.getColumnIndex("NotifyMobileIDs"));
                selectedRow.notifyText = cursor.getString(cursor.getColumnIndex("NotifyText"));
                selectedRow.opResultSMS = cursor.getInt(cursor.getColumnIndex("OpResultSMS")) != 0;
                selectedRow.mobileNumbers = cursor.getString(cursor.getColumnIndex("Mobilenumbers"));
                selectedRow.sMStext = cursor.getString(cursor.getColumnIndex("SMStext"));
                selectedRow.isStarted = cursor.getInt(cursor.getColumnIndex("isStarted")) != 0;
                selectedRow.opPreAND = cursor.getInt(cursor.getColumnIndex("OpPreAND")) != 0;
                selectedRow.isStartedOnGPS = cursor.getInt(cursor.getColumnIndex("isStartedOnGPS")) != 0;
                selectedRow.hasEdited = cursor.getInt(cursor.getColumnIndex("hasEdited")) != 0;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class PreOperand {
        public static class Struct {
            public int iD = 0;
            public int switchID = 0;
            public String operation = "";
            public float value = 0;
            public int scenarioID = 0;

            public String getValueName() {
                G.log("Database-PreOperand", "getValueName - switchID =" + switchID + " value=" + value);
                String query = "SELECT T_ValueTypeInstances.SentenceID FROM  T_Switch INNER JOIN T_SwitchType ON T_Switch.SwitchType=T_SwitchType.ID INNER JOIN T_ValueTypeInstances ON T_SwitchType.ValueTypeID=T_ValueTypeInstances.ValueTypeID WHERE " +
                        " T_Switch.ID=" + switchID +
                        " AND T_ValueTypeInstances.Value=" + value;
                G.log("Database-PreOperand", query);
                Cursor cursor = G.dbObject.rawQuery(query, null);
                int result = 0;
                String valueText = "";
                if (cursor.moveToNext()) {
                    G.log("Database-PreOperand", "getValueName-Try to get any result");
                    result = cursor.getInt(cursor.getColumnIndex("SentenceID"));
                    G.log("Database-PreOperand", "getValueName- got any result");
                    valueText = G.T.getSentence(result);
                } else {
                    valueText = "" + value;
                }
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return valueText;
            }

            public String getNodeFullName() {
                Cursor cursor = G.dbObject.rawQuery("SELECT T_Section.Name  AS SectionName,T_Room.Name AS RoomName, T_Node.Name AS NodeName , T_Switch.Name AS SwitchName FROM T_Switch INNER JOIN T_Node ON T_Switch.NodeID=T_Node.ID INNER JOIN T_ROOM ON T_Node.RoomID=T_Room.ID INNER JOIN T_Section ON T_Room.SectionID= T_Section.ID WHERE " +
                        " T_Switch.ID=" + switchID, null);
                if (cursor.moveToNext()) {
                    String fullName;
                    fullName = cursor.getString(cursor.getColumnIndex("SectionName"));
                    fullName += " / " + cursor.getString(cursor.getColumnIndex("RoomName"));
                    String nName = cursor.getString(cursor.getColumnIndex("NodeName"));
                    String sName = cursor.getString(cursor.getColumnIndex("SwitchName"));
                    fullName += " / " + nName;
                    if (!nName.equals(sName))
                        fullName += " / " + sName;
                    try {
                        cursor.close();
                    } catch (Exception e) {
                    }
                    return fullName;
                }
                try {
                    cursor.close();
                } catch (Exception e) {
                }
                return G.T.getSentence(606604) + " : " + switchID;
            }
        }

        public static long insert(Struct myPreOperand) {
            ContentValues Values = new ContentValues();
            Values.put("SwitchID", myPreOperand.switchID);
            Values.put("Operation", myPreOperand.operation);
            Values.put("Value", myPreOperand.value);
            Values.put("ScenarioID", myPreOperand.scenarioID);
            return G.dbObject.insert("T_PreOperand", null, Values);
        }

        public static long insert(int switchID, String operation, float value, int scenarioID) {
            ContentValues Values = new ContentValues();
            Values.put("SwitchID", switchID);
            Values.put("Operation", operation);
            Values.put("Value", value);
            Values.put("ScenarioID", scenarioID);
            return G.dbObject.insert("T_PreOperand", null, Values);
        }

        public static int edit(Struct myPreOperand) {
            ContentValues Values = new ContentValues();
            Values.put("SwitchID", myPreOperand.switchID);
            Values.put("Operation", myPreOperand.operation);
            Values.put("Value", myPreOperand.value);
            Values.put("ScenarioID", myPreOperand.scenarioID);
            return G.dbObject.update("T_PreOperand", Values, "ID=" + myPreOperand.iD, null);
        }

        public static int edit(int iD, int switchID, String operation, float value, int scenarioID) {
            ContentValues Values = new ContentValues();
            Values.put("SwitchID", switchID);
            Values.put("Operation", operation);
            Values.put("Value", value);
            Values.put("ScenarioID", scenarioID);
            return G.dbObject.update("T_PreOperand", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_PreOperand", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_PreOperand", "", null);
        }

        public static int delete(String conditions) {
            return G.dbObject.delete("T_PreOperand", conditions, null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_PreOperand WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.switchID = cursor.getInt(cursor.getColumnIndex("SwitchID"));
                selectedRow.operation = cursor.getString(cursor.getColumnIndex("Operation"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.scenarioID = cursor.getInt(cursor.getColumnIndex("ScenarioID"));

            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_PreOperand" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.switchID = cursor.getInt(cursor.getColumnIndex("SwitchID"));
                selectedRow.operation = cursor.getString(cursor.getColumnIndex("Operation"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.scenarioID = cursor.getInt(cursor.getColumnIndex("ScenarioID"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_PreOperand" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.switchID = cursor.getInt(cursor.getColumnIndex("SwitchID"));
                selectedRow.operation = cursor.getString(cursor.getColumnIndex("Operation"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.scenarioID = cursor.getInt(cursor.getColumnIndex("ScenarioID"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Results {
        public static class Struct {
            public int iD = 0;
            public int scenarioID = 0;
            public int switchID = 0;
            public float value = 0;
            public boolean active = false;
            public int reverseTime = 0;
            public float preValue = 0;

            public String getValueName() {
                String query = "SELECT T_ValueTypeInstances.SentenceID FROM  T_Switch INNER JOIN T_SwitchType ON T_Switch.SwitchType=T_SwitchType.ID INNER JOIN T_ValueType ON T_SwitchType.ValueTypeID=T_ValueType.ID INNER JOIN T_ValueTypeInstances ON T_ValueType.ID=T_ValueTypeInstances.ValueTypeID  WHERE " +
                        " T_Switch.ID=" + switchID +
                        " AND T_ValueTypeInstances.Value=" + value;
                G.log(query);
                Cursor cursor = G.dbObject.rawQuery(query, null);
                int result = 0;
                String ValueText = "";
                if (cursor.moveToNext()) {
                    result = cursor.getInt(cursor.getColumnIndex("SentenceID"));
                    ValueText = G.T.getSentence(result);
                } else {
                    ValueText = "" + value;
                }
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return ValueText;
            }

            public String getNodeFullName() {
                Cursor cursor = G.dbObject.rawQuery("SELECT T_Section.Name  AS SectionName,T_Room.Name AS RoomName, T_Node.Name AS NodeName , T_Switch.Name AS SwitchName FROM T_Switch INNER JOIN T_Node ON T_Switch.NodeID=T_Node.ID INNER JOIN T_ROOM ON T_Node.RoomID=T_Room.ID INNER JOIN T_Section ON T_Room.SectionID= T_Section.ID WHERE " +
                        " T_Switch.ID=" + switchID, null);
                if (cursor.moveToNext()) {
                    String fullName;
                    fullName = cursor.getString(cursor.getColumnIndex("SectionName"));
                    fullName += " / " + cursor.getString(cursor.getColumnIndex("RoomName"));
                    fullName += " / " + cursor.getString(cursor.getColumnIndex("NodeName"));
                    fullName += " / " + cursor.getString(cursor.getColumnIndex("SwitchName"));
                    try {
                        cursor.close();
                    } catch (Exception e) {
                    }
                    return fullName;
                }
                try {
                    cursor.close();
                } catch (Exception e) {
                }
                return G.T.getSentence(606604) + " : " + switchID;
            }
        }

        public static long insert(Struct myResults) {
            ContentValues Values = new ContentValues();
            Values.put("ScenarioID", myResults.scenarioID);
            Values.put("SwitchID", myResults.switchID);
            Values.put("Value", myResults.value);
            Values.put("Active", myResults.active);
            Values.put("ReverseTime", myResults.reverseTime);
            Values.put("PreValue", myResults.preValue);
            return G.dbObject.insert("T_Results", null, Values);
        }

        public static long insert(int scenarioID, int switchID, float value, boolean active, int reverseTime, float preValue) {
            ContentValues Values = new ContentValues();
            Values.put("ScenarioID", scenarioID);
            Values.put("SwitchID", switchID);
            Values.put("Value", value);
            Values.put("Active", active);
            Values.put("ReverseTime", reverseTime);
            Values.put("PreValue", preValue);
            return G.dbObject.insert("T_Results", null, Values);
        }

        public static int edit(Struct myResults) {
            ContentValues Values = new ContentValues();
            Values.put("ScenarioID", myResults.scenarioID);
            Values.put("SwitchID", myResults.switchID);
            Values.put("Value", myResults.value);
            Values.put("Active", myResults.active);
            Values.put("ReverseTime", myResults.reverseTime);
            Values.put("PreValue", myResults.preValue);
            return G.dbObject.update("T_Results", Values, "ID=" + myResults.iD, null);
        }

        public static int edit(int iD, int scenarioID, int switchID, float value, boolean active, int reverseTime, float preValue) {
            ContentValues Values = new ContentValues();
            Values.put("ScenarioID", scenarioID);
            Values.put("SwitchID", switchID);
            Values.put("Value", value);
            Values.put("Active", active);
            Values.put("ReverseTime", reverseTime);
            Values.put("PreValue", preValue);
            return G.dbObject.update("T_Results", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Results", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Results", "", null);
        }

        public static int delete(String conditions) {
            return G.dbObject.delete("T_Results", conditions, null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Results WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.scenarioID = cursor.getInt(cursor.getColumnIndex("ScenarioID"));
                selectedRow.switchID = cursor.getInt(cursor.getColumnIndex("SwitchID"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.active = cursor.getInt(cursor.getColumnIndex("Active")) != 0;
                selectedRow.reverseTime = cursor.getInt(cursor.getColumnIndex("ReverseTime"));
                selectedRow.preValue = cursor.getFloat(cursor.getColumnIndex("PreValue"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Results" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.scenarioID = cursor.getInt(cursor.getColumnIndex("ScenarioID"));
                selectedRow.switchID = cursor.getInt(cursor.getColumnIndex("SwitchID"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.active = cursor.getInt(cursor.getColumnIndex("Active")) != 0;
                selectedRow.reverseTime = cursor.getInt(cursor.getColumnIndex("ReverseTime"));
                selectedRow.preValue = cursor.getFloat(cursor.getColumnIndex("PreValue"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Results" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.scenarioID = cursor.getInt(cursor.getColumnIndex("ScenarioID"));
                selectedRow.switchID = cursor.getInt(cursor.getColumnIndex("SwitchID"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.active = cursor.getInt(cursor.getColumnIndex("Active")) != 0;
                selectedRow.reverseTime = cursor.getInt(cursor.getColumnIndex("ReverseTime"));
                selectedRow.preValue = cursor.getFloat(cursor.getColumnIndex("PreValue"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class SwitchType {
        public static class Struct {
            public int iD = 0;
            public int code = 0;
            public String icon = "";
            public int nameSentenceID = 0;
            public int valueTypeID = 0;
            public float defaultValue = 0;
        }

        public static long insert(Struct mySwitchType) {
            ContentValues Values = new ContentValues();
            Values.put("Code", mySwitchType.code);
            Values.put("Icon", mySwitchType.icon);
            Values.put("NameSentenceID", mySwitchType.nameSentenceID);
            Values.put("ValueTypeID", mySwitchType.valueTypeID);
            Values.put("DefaultValue", mySwitchType.defaultValue);
            return G.dbObject.insert("T_SwitchType", null, Values);
        }

        public static long insert(int code, String icon, int nameSentenceID, int valueTypeID, float defaultValue) {
            ContentValues Values = new ContentValues();
            Values.put("Code", code);
            Values.put("Icon", icon);
            Values.put("NameSentenceID", nameSentenceID);
            Values.put("ValueTypeID", valueTypeID);
            Values.put("DefaultValue", defaultValue);
            return G.dbObject.insert("T_SwitchType", null, Values);
        }

        public static int edit(Struct mySwitchType) {
            ContentValues Values = new ContentValues();
            Values.put("Code", mySwitchType.code);
            Values.put("Icon", mySwitchType.icon);
            Values.put("NameSentenceID", mySwitchType.nameSentenceID);
            Values.put("ValueTypeID", mySwitchType.valueTypeID);
            Values.put("DefaultValue", mySwitchType.defaultValue);
            return G.dbObject.update("T_SwitchType", Values, "ID=" + mySwitchType.iD, null);
        }

        public static int edit(int iD, int code, String icon, int nameSentenceID, int valueTypeID, float defaultValue) {
            ContentValues Values = new ContentValues();
            Values.put("Code", code);
            Values.put("Icon", icon);
            Values.put("NameSentenceID", nameSentenceID);
            Values.put("ValueTypeID", valueTypeID);
            Values.put("DefaultValue", defaultValue);
            return G.dbObject.update("T_SwitchType", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_SwitchType", "ID=" + iD, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_SwitchType", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_SwitchType WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.code = cursor.getInt(cursor.getColumnIndex("Code"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.nameSentenceID = cursor.getInt(cursor.getColumnIndex("NameSentenceID"));
                selectedRow.valueTypeID = cursor.getInt(cursor.getColumnIndex("ValueTypeID"));
                selectedRow.defaultValue = cursor.getFloat(cursor.getColumnIndex("DefaultValue"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_SwitchType" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.code = cursor.getInt(cursor.getColumnIndex("Code"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.nameSentenceID = cursor.getInt(cursor.getColumnIndex("NameSentenceID"));
                selectedRow.valueTypeID = cursor.getInt(cursor.getColumnIndex("ValueTypeID"));
                selectedRow.defaultValue = cursor.getFloat(cursor.getColumnIndex("DefaultValue"));
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_SwitchType" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.code = cursor.getInt(cursor.getColumnIndex("Code"));
                selectedRow.icon = cursor.getString(cursor.getColumnIndex("Icon"));
                selectedRow.nameSentenceID = cursor.getInt(cursor.getColumnIndex("NameSentenceID"));
                selectedRow.valueTypeID = cursor.getInt(cursor.getColumnIndex("ValueTypeID"));
                selectedRow.defaultValue = cursor.getFloat(cursor.getColumnIndex("DefaultValue"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }
    }

    public static class Switch {
        public static class Struct {
            public int iD = 0;
            public String code = "";
            public String name = "";
            public float value = 0;
            public int nodeID = 0;
            public int switchType = 0;
            public boolean enableGraphing = false;
            public int isIOModuleSwitch = 0;
            public int IOModulePort = 0;

            public String getFullName() {
                Cursor cursor = G.dbObject.rawQuery("SELECT T_Section.Name  AS SectionName,T_Room.Name AS RoomName, T_Node.Name AS NodeName , T_Switch.Name AS SwitchName FROM T_Switch INNER JOIN T_Node ON T_Switch.NodeID=T_Node.ID INNER JOIN T_ROOM ON T_Node.RoomID=T_Room.ID INNER JOIN T_Section ON T_Room.SectionID= T_Section.ID WHERE " +
                        " T_Switch.ID=" + iD, null);
                if (cursor.moveToNext()) {
                    String fullName;
                    fullName = cursor.getString(cursor.getColumnIndex("SectionName"));
                    fullName += " / " + cursor.getString(cursor.getColumnIndex("RoomName"));
                    String nName = cursor.getString(cursor.getColumnIndex("NodeName"));
                    String sName = cursor.getString(cursor.getColumnIndex("SwitchName"));
                    fullName += " / " + nName;
                    if (!nName.equals(sName))
                        fullName += " / " + sName;
                    try {
                        cursor.close();
                    } catch (Exception e) {
                    }
                    return fullName;
                }
                try {
                    cursor.close();
                } catch (Exception e) {
                }
                return G.T.getSentence(606604) + " : " + iD;
            }
        }

        public static long insert(Struct mySwitch) {
            ContentValues Values = new ContentValues();
            Values.put("Code", mySwitch.code);
            Values.put("Name", mySwitch.name);
            Values.put("Value", mySwitch.value);
            Values.put("NodeID", mySwitch.nodeID);
            Values.put("SwitchType", mySwitch.switchType);
            Values.put("EnableGraphing", mySwitch.enableGraphing);
            Values.put("isIOModuleSwitch", mySwitch.isIOModuleSwitch);
            Values.put("IOModulePort", mySwitch.IOModulePort);
            return G.dbObject.insert("T_Switch", null, Values);
        }

        public static long insert(String code, String name, float value, int nodeID, int switchType, boolean enableGraphing, boolean isIOModuleSwitch, String IOModulePort) {
            ContentValues Values = new ContentValues();
            Values.put("Code", code);
            Values.put("Name", name);
            Values.put("Value", value);
            Values.put("NodeID", nodeID);
            Values.put("SwitchType", switchType);
            Values.put("EnableGraphing", enableGraphing);
            Values.put("isIOModuleSwitch", isIOModuleSwitch);
            Values.put("IOModulePort", IOModulePort);
            return G.dbObject.insert("T_Switch", null, Values);
        }

        public static int edit(Struct mySwitch) {
            ContentValues Values = new ContentValues();
            Values.put("Code", mySwitch.code);
            Values.put("Name", mySwitch.name);
            Values.put("Value", mySwitch.value);
            Values.put("NodeID", mySwitch.nodeID);
            Values.put("SwitchType", mySwitch.switchType);
            Values.put("EnableGraphing", mySwitch.enableGraphing);
            Values.put("isIOModuleSwitch", mySwitch.isIOModuleSwitch);
            Values.put("IOModulePort", mySwitch.IOModulePort);
            return G.dbObject.update("T_Switch", Values, "ID=" + mySwitch.iD, null);
        }

        public static int edit(int iD, String code, String name, float value, int nodeID, int switchType, boolean enableGraphing, boolean isIOModuleSwitch, String IOModulePort) {
            ContentValues Values = new ContentValues();
            Values.put("Code", code);
            Values.put("Name", name);
            Values.put("Value", value);
            Values.put("NodeID", nodeID);
            Values.put("SwitchType", switchType);
            Values.put("EnableGraphing", enableGraphing);
            Values.put("isIOModuleSwitch", isIOModuleSwitch);
            Values.put("IOModulePort", IOModulePort);
            return G.dbObject.update("T_Switch", Values, "ID=" + iD, null);
        }

        public static int editValue(int iD, float value) {
            ContentValues Values = new ContentValues();
            Values.put("Value", value);
            return G.dbObject.update("T_Switch", Values, "ID=" + iD, null);
        }

        public static int editName(int iD, String name) {
            ContentValues Values = new ContentValues();
            Values.put("Name", name);
            return G.dbObject.update("T_Switch", Values, "ID=" + iD, null);
        }

        public static int edit(int iD, float value) {
            ContentValues Values = new ContentValues();
            Values.put("Value", value);
            return G.dbObject.update("T_Switch", Values, "ID=" + iD, null);
        }

        public static int delete(int iD) {
            return G.dbObject.delete("T_Switch", "ID=" + iD, null);
        }

        public static int delete(String criteria) {
            return G.dbObject.delete("T_Switch", criteria, null);
        }

        public static int delete() {
            return G.dbObject.delete("T_Switch", "", null);
        }

        public static Struct select(int iD) {
            Struct selectedRow = null;
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Switch WHERE ID=" + iD, null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.code = cursor.getString(cursor.getColumnIndex("Code"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.nodeID = cursor.getInt(cursor.getColumnIndex("NodeID"));
                selectedRow.switchType = cursor.getInt(cursor.getColumnIndex("SwitchType"));
                selectedRow.enableGraphing = cursor.getInt(cursor.getColumnIndex("EnableGraphing")) != 0;
                selectedRow.isIOModuleSwitch = cursor.getInt(cursor.getColumnIndex("isIOModuleSwitch"));
                selectedRow.IOModulePort = cursor.getInt(cursor.getColumnIndex("IOModulePort"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }

        public static Struct[] select(String whereCriteria) {
            String whereQuery = "";
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Switch" + whereQuery, null);
            if (cursor.getCount() < 1) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
                return null;
            }
            Struct[] result = new Struct[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Struct selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.code = cursor.getString(cursor.getColumnIndex("Code"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.nodeID = cursor.getInt(cursor.getColumnIndex("NodeID"));
                selectedRow.switchType = cursor.getInt(cursor.getColumnIndex("SwitchType"));
                selectedRow.IOModulePort = cursor.getInt(cursor.getColumnIndex("IOModulePort"));
                selectedRow.isIOModuleSwitch = cursor.getInt(cursor.getColumnIndex("isIOModuleSwitch"));
                selectedRow.enableGraphing = cursor.getInt(cursor.getColumnIndex("EnableGraphing")) != 0;
                result[i] = selectedRow;
                i++;
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return result;
        }

        public static Struct getMax(String field, String whereCriteria) {
            String whereQuery = "";
            Struct selectedRow = null;
            if (whereCriteria.trim().length() > 0)
                whereQuery = " WHERE " + whereCriteria.trim();
            Cursor cursor = G.dbObject.rawQuery("SELECT * FROM T_Switch" + whereQuery + " ORDER BY " + field + " DESC  LIMIT 1", null);
            if (cursor.moveToNext()) {
                selectedRow = new Struct();
                selectedRow.iD = cursor.getInt(cursor.getColumnIndex("ID"));
                selectedRow.code = cursor.getString(cursor.getColumnIndex("Code"));
                selectedRow.name = cursor.getString(cursor.getColumnIndex("Name"));
                selectedRow.value = cursor.getFloat(cursor.getColumnIndex("Value"));
                selectedRow.nodeID = cursor.getInt(cursor.getColumnIndex("NodeID"));
                selectedRow.switchType = cursor.getInt(cursor.getColumnIndex("SwitchType"));
                selectedRow.enableGraphing = cursor.getInt(cursor.getColumnIndex("EnableGraphing")) != 0;
                selectedRow.isIOModuleSwitch = cursor.getInt(cursor.getColumnIndex("isIOModuleSwitch"));
                selectedRow.IOModulePort = cursor.getInt(cursor.getColumnIndex("IOModulePort"));
            }
            try {
                cursor.close();
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return selectedRow;
        }


    }

}
