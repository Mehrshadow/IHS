package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityStarter extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        G.log("Database Version "+G.dbObject.getVersion());

        if (G.setting.customerID > 0) {
            startActivity(new Intent(ActivityStarter.this, ActivityMain.class));
            finish();
        } else {
            startActivity(new Intent(ActivityStarter.this, ActivityWelcome1Language.class));
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }
}
