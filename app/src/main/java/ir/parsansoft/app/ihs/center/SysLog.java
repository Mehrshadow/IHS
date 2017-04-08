package ir.parsansoft.app.ihs.center;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class SysLog {

    public static enum LogOperator {
        SYSYTEM,
        NODE,
        OPERAOR,
        MOBILE,
        WEB,
    }

    public static enum LogType {
        VALUE_CHANGE, // switch value changes / mobile locations / sensors ...
        COMMAND_ERROR, // error while doing some job.
        NODE_STATUS, // when a node exits from network range. (effects on alarms) 
        SYSTEM_EVENTS, // turn on-off / network errors / ... 
        SYSTEM_SETTINGS, // wifi / location / ...
        DATA_CHANGE, // section / room / node /  ...
        SCENARIO_CHANGE // Add / edit / delete / start / on-off / ...
    }

    //    public static class LogType {
    //        int VALUE_CHANGE=1; // switch value changes / mobile locations / sensors ...
    //        int COMMAND_ERROR=2; // error while doing some job.
    //        int NODE_STATUS=3; // when a node exits from network range. (effects on alarms) 
    //        int SYSTEM_EVENTS=4; // turn on-off / network errors / ... 
    //        SYSTEM_SETTINGS, // wifi / location / ...
    //        DATA_CHANGE, // section / room / node /  ...
    //        SCENARIO_CHANGE // Add / edit / delete / start / on-off / ...
    //    }


    public static void log(String desc, LogType type, LogOperator operator, int operatorID) {
        if (desc == null)
            desc = "";
        Database.Log.Struct l = new Database.Log.Struct();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        l.date = sdfDate.format(Calendar.getInstance().getTime());
        l.des = desc;
        l.type = type.ordinal();
        l.operatorType = operator.ordinal();
        l.operatorID = operatorID;
        try {
            Database.Log.insert(l);
        }
        catch (Exception e) {
            G.printStackTrace(e);
        }
    }
}
