package ir.parsansoft.app.ihs.center;

import android.app.AlarmManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import ir.parsansoft.app.ihs.center.ModuleWebservice.WebServiceListener;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.SysLog.LogOperator;


public class ScenarioBP {


    public ScenarioBP() {
        // initialize scenario background parameters....
        G.log("initialize scenario background parameters....");
        resetTimeBase();
    }

    public void resetTimeBase() {
        G.log("Resetting Time Base Scenario BG !");
        Database.Scenario.Struct[] allTimedScenarios = Database.Scenario.select("OpTimeBase=1 AND Active=1");
        if (allTimedScenarios != null) {
            Calendar c = Calendar.getInstance();
            int currentHour = c.get(Calendar.HOUR_OF_DAY);
            int currentMinute = c.get(Calendar.MINUTE);
            int currentSecond = c.get(Calendar.SECOND);
            int currentMilli = c.get(Calendar.MILLISECOND);
            long todayTimeInMillis = (currentHour * 3600 + currentMinute * 60 + currentSecond) * 1000 + currentMilli;

            for (int i = 0; i < allTimedScenarios.length; i++) {
                G.log("set alarm for scenario :" + allTimedScenarios[i].name);
                String t[];
                int h;
                int m;
                t = allTimedScenarios[i].startHour.split(":");
                h = Integer.parseInt(t[0]);
                m = Integer.parseInt(t[1]);
                long scenarioTimeInMillis = (h * 3600 + m * 60) * 1000;
                long nextFirstRunTime;
                G.log("Diff Millis : " + (scenarioTimeInMillis - todayTimeInMillis));
                if (scenarioTimeInMillis > todayTimeInMillis) {

                    nextFirstRunTime = System.currentTimeMillis() + scenarioTimeInMillis - todayTimeInMillis;
                } else {
                    nextFirstRunTime = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY - todayTimeInMillis + scenarioTimeInMillis;
                }
                G.alarmScenario.SetRepeatAlarm(allTimedScenarios[i].iD, "" + allTimedScenarios[i].iD, nextFirstRunTime, AlarmManager.INTERVAL_DAY);
            }
        }
    }

    private boolean checkConditionTime(Database.Scenario.Struct scenario) {
        Calendar calendar = Calendar.getInstance();
        int week_day = calendar.get(Calendar.DAY_OF_WEEK);
        int month_day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        G.log("calendar week_day =" + week_day);
        G.log("calendar month_day =" + month_day);
        G.log("calendar month =" + month);
        G.log("scenario.months =" + scenario.months);
        G.log("scenario.monthDays =" + scenario.monthDays);
        G.log("scenario.weekDays =" + scenario.weekDays);
        boolean canRun;
        if (scenario.opTimeBase
                && scenario.months.contains("M" + month)
                && scenario.monthDays.contains("D" + (month_day - 1))
//                && scenario.weekDays.contains("WD" + (week_day - 1)))
                && scenario.weekDays.contains("WD" + (week_day)))
            canRun = true;
        else
            canRun = false;
        G.log("checkConditionTime(scenario : " + scenario.iD + " )= " + canRun);
        return canRun;
    }

    private boolean checkConditionSwitchStatus(Database.Scenario.Struct scenario) {
        if (!scenario.opPreSwitchBase)
            return true;

        G.log("checkConditionSwitchStatus scenario : " + scenario);
        Database.PreOperand.Struct[] po = Database.PreOperand.select("ScenarioID=" + scenario.iD);
        if (po != null) {
            for (int i = 0; i < po.length; i++) {
                Database.Switch.Struct sw = Database.Switch.select(po[i].switchID);
                if (sw == null || !compaireValues(po[i].value, sw.value, po[i].operation)) {
                    G.log("ConditionSwitchStatus scenario : " + scenario + "= false");
                    if (scenario.opPreAND)
                        return false;
                }
            }
            G.log("ConditionSwitchStatus scenario : " + scenario + "= true");
            return true;
        }
        G.log("ConditionSwitchStatus scenario : " + scenario + "= false");
        return false;
    }

    private boolean checkConditionLocation(Database.Scenario.Struct scenario) {

        if (!scenario.opPreGPS)
            return true;
        G.log("checkConditionLocation scenario : " + scenario);
        String gpsJ = scenario.gPS_Params;
        JSONObject jo;
        try {
            jo = new JSONObject(gpsJ);
            Database.Mobiles.Struct mobile = Database.Mobiles.select(jo.getInt("MobileID"));
            double lat, lon;
            int r;
            lat = jo.getDouble("Latitude");//scenario lat
            lon = jo.getDouble("Longitude");// scenario lon
            r = jo.getInt("Radius");
            double rDif = Utility.GPS.getDistanceInM(mobile.LastLatitude, mobile.LastLongitude, lat, lon);
            boolean result = false;
            switch (jo.getInt("EnteringMode")) {
                case 1: // Entering the area
                    if (r > rDif && !scenario.isStartedOnGPS) {// we are inside & scenario not started yet
                        result = scenario.isStartedOnGPS = true;
                    }
                    break;
                case 2: // Exiting the area
                    if (r <= rDif && !scenario.isStartedOnGPS) {// we are outside & scenario not started yet
                        result = scenario.isStartedOnGPS = true;
                    }
                    break;
            }
            G.log("ConditionLocation scenario : " + scenario + "= " + result);
            Database.Scenario.edit(scenario);
            return result;
        } catch (Exception e) {
            G.printStackTrace(e);
        }
        G.log("ConditionLocation scenario : " + scenario + "= false");
        return false;
    }

    private boolean checkConditionWeather(Database.Scenario.Struct scenario) {
        if (!scenario.opPreWeather)
            return true;
        G.log("checkConditionWeather scenario : " + scenario);
        if (scenario.weatherTypes.contains("W" + G.bp.getCurrentWeatherCode() + ";")) {
            G.log("ConditionWeather scenario : " + scenario + " = true");
            return true;
        } else {
            G.log("ConditionWeather scenario : " + scenario + " = true");
            return false;
        }
    }

    private boolean checkConditionTemperature(Database.Scenario.Struct scenario) {
        if (!scenario.opPreTemperature)
            return true;
        G.log("checkConditionTemperature scenario : " + scenario);
        if (G.bp.getCurrentTemperature() >= scenario.minTemperature && G.bp.getCurrentTemperature() <= scenario.maxTemperature) {
            G.log("ConditionTemperature scenario : " + scenario + " = true");
            return true;
        } else {
            G.log("ConditionTemperature scenario : " + scenario + " = false");
            return false;
        }
    }

    //****************************************
    public void runByTime(int scenarioID) {
        G.log("set Scenario BP run by time listenner");
        G.log("Timer Alarm: " + scenarioID + " has ringed now !");
        Database.Scenario.Struct scenario = Database.Scenario.select(scenarioID);
        if (scenario != null && scenario.active && scenario.opTimeBase) {
            boolean canRun = false;
            canRun = checkConditionTime(scenario);
            if (canRun && scenario.opPreAND) {
                canRun = canRun && checkConditionSwitchStatus(scenario) && checkConditionLocation(scenario) && checkConditionTemperature(scenario) && checkConditionWeather(scenario);
            }
            G.log("runByTime- scenario : " + scenarioID + " can run = " + canRun);
            if (canRun)
                runScenario(scenario);
        }
    }

    public void runBySwitchStatus(Database.Switch.Struct changedSwitch) {
        G.log("trying to find any scenario has condition of SwitchStatus: switch ID:" + changedSwitch.iD + "  Value:" + changedSwitch.value);
        //  select all scenarios that has a condition like this switch and timebase is off !
        Database.PreOperand.Struct[] preOperands = Database.PreOperand.select("SwitchID=" + changedSwitch.iD);
        if (preOperands == null)
            return;
        G.log("Found : " + preOperands.length + " scenarios that can start !");
        for (int i = 0; i < preOperands.length; i++) {
            G.log("ScenarioBP : preOperands[i].value = " + preOperands[i].value);
            G.log("ScenarioBP : changedSwitch.value = " + changedSwitch.value);
            G.log("ScenarioBP : preOperands[i].operation = " + preOperands[i].operation);
            if (compaireValues(preOperands[i].value, changedSwitch.value, preOperands[i].operation)) {
                Database.Scenario.Struct scenario = Database.Scenario.select(preOperands[i].scenarioID);
                if (scenario != null && scenario.active && scenario.opPreSwitchBase) {
                    boolean canRun = false;
                    canRun = checkConditionSwitchStatus(scenario);
                    if (canRun && scenario.opPreAND) {
                        canRun = /*canRun && */checkConditionLocation(scenario) && checkConditionTemperature(scenario) && checkConditionWeather(scenario);
                    }
                    G.log("runBySwitchStatus - scenario : " + scenario.iD + " can run = " + canRun);
                    if (canRun)
                        runScenario(scenario);
                }
            }
        }
    }

    public void runByLocation(int mobileID, double latitude, double longitude) {
        //  select all scenarios that has a condition like this mobile Location and timebase is off !
        G.log("trying to find any scenario has condition of Location: Mobile ID:" + mobileID + "  latitude:" + latitude + "  longitude:" + longitude);
        Database.Scenario.Struct[] scenarios = Database.Scenario.select("Active=1 AND OpPreGPS=1 AND GPS_Params LIKE '%\"MobileID\":" + mobileID + "%'");
        if (scenarios == null)
            return;
        for (int i = 0; i < scenarios.length; i++) {
            try {
                JSONObject jo = new JSONObject(scenarios[i].gPS_Params);
                double sLat, sLon;
                int sR, sEnteringMode;
                sLat = jo.getDouble("Latitude");
                sLon = jo.getDouble("Longitude");
                sEnteringMode = jo.getInt("EnteringMode");
                sR = jo.getInt("Radius");

                int distance = Utility.GPS.getDistanceInM(sLat, sLon, latitude, longitude);
                if ((sEnteringMode == 1 && distance > sR) || (sEnteringMode == 2 && distance < sR)) {
                    scenarios[i].isStartedOnGPS = false;
                    Database.Scenario.edit(scenarios[i]);
                    G.log("gps not ok");
                    continue;
                }
                boolean canRun;
                canRun = checkConditionLocation(scenarios[i]);
                if (canRun && scenarios[i].opPreAND) {
                    canRun = canRun && checkConditionSwitchStatus(scenarios[i]) && checkConditionTemperature(scenarios[i]) && checkConditionWeather(scenarios[i]);
                }
                G.log("runByLocation - scenario : " + scenarios[i].iD + " can run = " + canRun);
                if (canRun)
                    runScenario(scenarios[i]);
            } catch (Exception e) {
            }
        }
    }

    public void runByWeather(int newWeatherID) {
        G.log("trying to find any scenario has condition of ir.parsansoft.app.ihs.center.Weather ID:" + newWeatherID);
        //  select all scenarios that has a condition like this weather and timebase is off !
        Database.Scenario.Struct[] scenarios = Database.Scenario.select("Active=1 AND OpPreWeather=1 AND WeatherTypes LIKE '%W" + newWeatherID + ";%'");
        if (scenarios == null)
            return;
        for (int i = 0; i < scenarios.length; i++) {
            try {
                boolean canRun = false;
                canRun = checkConditionWeather(scenarios[i]);
                if (canRun && scenarios[i].opPreAND) {
                    canRun = canRun && checkConditionSwitchStatus(scenarios[i]) && checkConditionTemperature(scenarios[i]) && checkConditionSwitchStatus(scenarios[i]);
                }
                G.log("runByWeather - scenario : " + scenarios[i].iD + " can run = " + canRun);
                if (canRun)
                    runScenario(scenarios[i]);
            } catch (Exception e) {
            }
        }
    }

    public void runByTemperature(int newTemp) {
        G.log("trying to find any scenario has condition of Temperature:" + newTemp);
        //  select all scenarios that has a condition like this temperature and timebase is off !
        Database.Scenario.Struct[] scenarios = Database.Scenario.select("Active=1 AND OpPreTemperature=1 AND MinTemperature<=" + newTemp + " AND MaxTemperature>=" + newTemp);
        if (scenarios == null)
            return;
        for (int i = 0; i < scenarios.length; i++) {
            try {
                boolean canRun = false;
                canRun = checkConditionTemperature(scenarios[i]);
                if (canRun && scenarios[i].opPreAND) {
                    canRun = canRun && checkConditionSwitchStatus(scenarios[i]) && checkConditionWeather(scenarios[i]) && checkConditionSwitchStatus(scenarios[i]);
                }
                G.log("runByTemperature - scenario : " + scenarios[i].iD + " can run = " + canRun);
                if (canRun)
                    runScenario(scenarios[i]);
            } catch (Exception e) {
            }
        }
    }

    public void runBySMS(String smsText) {
        // select all scenarios that has a condition like this SMS pattern
    }

    //----------------------------------------
    public void runScenario(Database.Scenario.Struct scenario) {
        G.log("Scenario " + scenario.iD + " has started now!");

        if (scenario.opResultSwitch) {
            try {
                Database.Results.Struct[] results = Database.Results.select("ScenarioID=" + scenario.iD);
                if (results != null) {
                    for (int i = 0; i < results.length; i++) {
                        doSwitchResult(results[i]);
                    }
                }
            } catch (Exception e) {
                G.printStackTrace(e);
            }
        }

        if (scenario.opResultNotify && scenario.notifyMobileIDs.length() > 0) {
            try {
                G.log("Try to send notify to mobiles...");
                sendAlarm(scenario.iD, scenario.notifyMobileIDs, "[{\"Ring\":2,\"NotifyTitle\":\"" + scenario.name + "\",\"NotifyText\":\"" + scenario.notifyText + "\"}]");
            } catch (Exception e) {
                G.printStackTrace(e);
            }
        }

        if (scenario.opResultSMS && scenario.mobileNumbers.length() > 0) {
            try {
                String[] mobiles = scenario.mobileNumbers.split(";");
                for (int i = 0; i < mobiles.length; i++) {
                    sendSMS(scenario.iD, mobiles[i], scenario.sMStext);
                }
            } catch (Exception e) {
                G.printStackTrace(e);
            }
        }
    }

    //----------------------------------------
    private void doSwitchResult(final Database.Results.Struct result) {
        G.log("doSwitchResult");
        final Database.Switch.Struct sw = Database.Switch.select(result.switchID);
        G.log("Try to change switch " + sw.getFullName() + " status to new state:" + result.value);
        changeSwitchStatus(sw, result.value);
        if (result.reverseTime > 0) {
            result.preValue = sw.value;
            result.active = true;
            Database.Results.edit(result);
            G.HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    G.log("Try to change switch " + sw.getFullName() + " status to previous state:" + result.preValue);
                    changeSwitchStatus(sw, result.preValue);
                    result.active = false;
                    Database.Results.edit(result);
                }
            }, result.reverseTime * 1000);
        }
    }

    private void changeSwitchStatus(Database.Switch.Struct sw, float value) {
        G.log("changeSwitchStatus" + sw.name + "  : " + value);
        try {
            JSONObject jmsg = new JSONObject();
            jmsg.put("SwitchCode", sw.code);
            jmsg.put("Value", value);
            G.nodeCommunication.executeCommandChangeSwitchValue(sw.nodeID, jmsg.toString(), null, LogOperator.SYSYTEM, 0);
        } catch (Exception e) {
            G.printStackTrace(e);
        }
    }

    private void sendAlarm(int scenarioID, String mobileIDs, String message) {
        G.log("Sendig Notify to mobile :" + mobileIDs + " for scenario ID:" + scenarioID);


        try {
            String[] mobiles = mobileIDs.split(";");
            int mIDs[] = new int[mobiles.length];
            for (int i = 0; i < mobiles.length; i++) {
                try {
                    mIDs[i] = Integer.parseInt(mobiles[i]);
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
            }

            //  Send message to server and local Mobiles
            NetMessage netMessage = new NetMessage();
            netMessage.data = message;
            netMessage.type = NetMessage.Notify;
            netMessage.typeName = NetMessageType.Notify;
            netMessage.recieverIDs = mIDs;
            netMessage.messageID = netMessage.save();
            G.mobileCommunication.sendMessage(netMessage);
            G.server.sendMessage(netMessage);
        } catch (Exception e) {
            G.printStackTrace(e);
        }
    }

    private void sendSMS(int scenarioID, final String mobileNumber, final String message) {
        G.log("Sendig SMS to mobile :" + mobileNumber + " for scenario ID:" + scenarioID);
        ModuleWebservice mw = new ModuleWebservice();
        mw.addParameter("rt", "SendSMS");
        mw.addParameter("CustomerID", "" + G.setting.customerID);
        mw.addParameter("ExKey", G.setting.exKey);
        mw.addParameter("MobileNumber", mobileNumber);
        mw.addParameter("SMSText", message);

        mw.enableCache(false);
        mw.connectionTimeout(10000);
        mw.socketTimeout(5000);
        mw.url(G.URL_Webservice);
        mw.setListener(new WebServiceListener() {
            @Override
            public void onFail(int statusCode) {
                SysLog.log("Sending SMS to " + mobileNumber + "faild:\"" + message + "\"", SysLog.LogType.SYSTEM_EVENTS, LogOperator.SYSYTEM, 0);
            }

            @Override
            public void onDataReceive(String data, boolean cached) {
                try {
                    JSONObject jo = (new JSONArray(data)).getJSONObject(0);
                    int messageID = jo.getInt("MessageID");
                    if (messageID == 1) {
                        G.log("SMS Sent successfully");
                        SysLog.log("SMS Sent successfully to: " + mobileNumber + " Message: \"" + message + "\"", SysLog.LogType.SYSTEM_EVENTS, LogOperator.SYSYTEM, 0);
                    } else if (messageID == 2) {
                        G.log("SMS was not sent successfully because low sms balance");
                        SysLog.log("SMS was not sent successfully to: " + mobileNumber + " Message: \"" + message + "\"\nSMS pannel has low balance.", SysLog.LogType.SYSTEM_EVENTS, LogOperator.SYSYTEM, 0);
                    } else if (messageID == 3) {
                        G.log("SMS was not sent successfully because customer or mobile is not permitted.");
                        SysLog.log("SMS was not sent successfully to: " + mobileNumber + " Message: \"" + message + "\"\nCustomer or mobile number is not permitted.", SysLog.LogType.SYSTEM_EVENTS, LogOperator.SYSYTEM, 0);
                    }
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
            }
        });
        mw.read();
    }
    //------------------------------------------------------------------


    private boolean compaireValues(float val1, float val2, String operation) {
        G.log("Compare values : " + val1 + "  " + operation + "  " + val2);
        if (operation.equals("="))
            return val1 == val2;
        if (operation.equals("<"))
            return val1 < val2;
        if (operation.equals("<="))
            return val1 <= val2;
        if (operation.equals(">"))
            return val1 > val2;
        if (operation.equals(">="))
            return val1 >= val2;
        if (operation.equals("!="))
            return val1 != val2;
        G.log("Cannot compare values : " + val1 + "  " + operation + "  " + val2);
        return false;
    }
}
