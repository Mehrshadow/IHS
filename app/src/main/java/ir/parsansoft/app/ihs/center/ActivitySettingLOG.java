package ir.parsansoft.app.ihs.center;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

import ir.parsansoft.app.ihs.center.adapters.AdapterListViewLog;


public class ActivitySettingLOG extends ActivitySetting {

    ListView           listLog;
    AdapterListViewLog adapterLog;
    Spinner            spnDate, spnOperator, spnType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_setting_log);
        changeSlidebarImage(8);
        translateForm();
        /*********** Initialize Spinners **********/
        spnDate = (Spinner) findViewById(R.id.spnDate);
        spnOperator = (Spinner) findViewById(R.id.spnOperator);
        spnType = (Spinner) findViewById(R.id.spnType);
        /*********** Initialize Spinners **********/

        try {
            /*********** Initialize ListView **********/
            listLog = (ListView) findViewById(R.id.listLog);
            ArrayList<Database.Log.Struct> list = new ArrayList<Database.Log.Struct>(Arrays.asList(Database.Log.select("1=1 ORDER BY ID DESC LIMIT 500")));
            adapterLog = new AdapterListViewLog(list);
            listLog.setAdapter(adapterLog);
            /*********** Initialize ListView **********/
        }
        catch (Exception e) {
            G.printStackTrace(e);
        }
    }



    @Override
    public void translateForm() {
        super.translateForm();
    }
}
