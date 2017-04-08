package ir.parsansoft.app.ihs.center;

import android.util.SparseArray;

public class UI {


    private SparseArray<OnUiChange> uics         = new SparseArray<OnUiChange>();
    int                             uics_counter = 0;
    OnScenarioChange                oscl;

    public interface OnUiChange {
        public void onTimeChanged(String newTime);
        public void onAlarmChanged(int alarmCount);
        public void onWeatherChanged(int weatherCode, int temp);
        public void onServerStatusChanged(int status);
        public void onWifiSignalChanged(int level);
    }

    public interface OnScenarioChange {
        public void onOnScenarioChanged(int scenarioID);
    }

    public void setOnScenarioChangeListenner(OnScenarioChange oscl) {
        this.oscl = oscl;
    }

    public void removeOnScenarioChangedListenner() {
        oscl = null;
    }

    public int addOnUiChaged(OnUiChange otc, int lastUiIndex) {
        int new_ui_index = 0;
        if (lastUiIndex > 0 && uics.indexOfKey(lastUiIndex) >= 0)
            new_ui_index = lastUiIndex;
        else {
            uics_counter++;
            new_ui_index = uics_counter;
        }
        uics.put(new_ui_index, otc);
        G.bp.refreshUI();

        if (G.server != null && G.server.isConnected())
            runOnServerStatusChanged(1);
        else
            runOnServerStatusChanged(0);

        return new_ui_index;
    }
    public void removeOnUiChaged(int key) {
        uics.remove(key);
    }

    public void runOnTimeChanged(String newTime) {
        for (int i = 0; i < uics.size(); i++) {
            try {
                uics.get(uics.keyAt(i)).onTimeChanged(newTime);
            }
            catch (Exception e) {
                G.log("Error of runOnTimeChanged at index :" + i + "\n" + e.getMessage());
                G.printStackTrace(e);
                uics.removeAt(i);
                i--;
            }
        }
    }
    public void runOnAlarmChanged(int alarmCount) {
        for (int i = 0; i < uics.size(); i++) {
            try {
                uics.get(uics.keyAt(i)).onAlarmChanged(alarmCount);
            }
            catch (Exception e) {
                G.log("Error of runOnAlarmChanged at index :" + i + "\n" + e.getMessage());
                G.printStackTrace(e);
                uics.removeAt(i);
                i--;
            }
        }
    }
    public void runOnWeatherChanged(int weatherCode, int temp) {
        if (weatherCode == 0)
            return;
        for (int i = 0; i < uics.size(); i++) {
            try {
                G.log("run weather ui: " + i);
                uics.get(uics.keyAt(i)).onWeatherChanged(weatherCode, temp);
            }
            catch (Exception e) {
                G.log("Error of runOnWeatherChanged at index :" + i + "\n" + e.getMessage());
                G.printStackTrace(e);
                uics.removeAt(i);
                i--;
            }
        }
    }
    public void runOnServerStatusChanged(int newStatus) {
        G.log("UI - runOnServerStatusChanged  : new status = " + newStatus);
        for (int i = 0; i < uics.size(); i++) {
            try {
                uics.get(uics.keyAt(i)).onServerStatusChanged(newStatus);
            }
            catch (Exception e) {
                G.log("Error of runOnServerStatusChanged at index :" + i + "\n" + e.getMessage());
                G.printStackTrace(e);
                uics.removeAt(i);
                i--;
            }
        }
    }
    public void runOnWifiSignalChanged(int level) {
        for (int i = 0; i < uics.size(); i++) {
            try {
                uics.get(uics.keyAt(i)).onWifiSignalChanged(level);
            }
            catch (Exception e) {
                G.log("Error of runOnWifiSignalChanged at index :" + i + "\n" + e.getMessage());
                G.printStackTrace(e);
                uics.removeAt(i);
                i--;
            }
        }
    }
    public void runOnScenarioChanged(int scenarioID) {
        if (oscl != null) {
            oscl.onOnScenarioChanged(scenarioID);
        }
    }
}
