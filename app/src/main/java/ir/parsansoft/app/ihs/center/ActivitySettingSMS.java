package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_setting_sms;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.DialogClass.OkCancelInputListener;
import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.ModuleWebservice.WebServiceListener;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioNotifyMobiles;
import ir.parsansoft.app.ihs.center.adapters.AdapterScenarioNotifyMobiles.onDeleteItem;
import ir.parsansoft.app.ihs.center.adapters.AdapterTelCodeSpinner;


public class ActivitySettingSMS extends ActivitySetting {
    CO_f_setting_sms                     fo;
    List<String>                         listMobiles;
    ArrayList<String>                    allMobiles = new ArrayList<String>();
    private AdapterScenarioNotifyMobiles listAdapter;
    AdapterTelCodeSpinner                adapterCode;
    String[]                             country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_setting_sms);
        fo = new CO_f_setting_sms(this);
        changeSlidebarImage(6);

        fo.btnCharge.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(G.currentActivity, ActivityBrowser.class);
                String url = G.URL_Sms_Webservice;
                String languageName = "";
                switch (G.setting.languageID) {
                    case 1:
                        languageName = "en";
                        break;
                    case 2:
                        languageName = "fa";
                        break;
                    case 3:
                        languageName = "ar";
                        break;
                    case 4:
                        languageName = "tr";
                        break;
                    default:
                        languageName = "en";
                        break;
                }

                url = url.replace("[LANG]", languageName);
                url = url.replace("[CUSTOMERID]", "" + G.setting.customerID);
                url = url.replace("[EXKEY]", G.setting.exKey);
                G.log(url);
                intent.putExtra("URL", url);
                G.currentActivity.startActivity(intent);
                Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
            }
        });
        fo.btnVerify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String mNumber;
                mNumber = fo.edtMobileNumber.getText().toString().trim();
                if (mNumber.length() < 8 || !mNumber.matches("\\d+")) {
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(739), G.T.getSentence(740));
                    return;
                }
                if (mNumber.substring(0, 1).equals("0"))
                    mNumber = mNumber.substring(1);

                verifyMobileNumber(fo.txtCountryCode.getText().toString().trim() + mNumber);
            }
        });
        translateForm();
        refreshList();
        loadRemainigSMS();
        fillCountryCode();
    }
    private void fillCountryCode() {
        country = getResources().getStringArray(R.array.countryCodes);
        adapterCode = new AdapterTelCodeSpinner(this, country);
        fo.spnCountries.setAdapter(adapterCode);
        String currentlanguageSign = "IR";
        switch (G.setting.languageID) {
            case 1:
                currentlanguageSign = "GB";
                break;
            case 2:
                currentlanguageSign = "IR";
                break;
            case 3:
                currentlanguageSign = "SA";
                break;
            case 4:
                currentlanguageSign = "TR";
                break;

            default:
                break;
        }

        for (int i = 0; i < country.length; i++) {
            if (country[i].contains(currentlanguageSign)) {
                fo.spnCountries.setSelection(i);
                break;
            }
        }
        fo.spnCountries.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] g = country[position].split(",");
                fo.txtCountryCode.setText("+" + g[0]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fo.txtCountryCode.setText("");
            }
        });
    }


    private void loadRemainigSMS() {
        ModuleWebservice mw = new ModuleWebservice();
        mw.addParameter("rt", "GetRemainSMS");
        mw.addParameter("ExKey", G.setting.exKey);
        mw.addParameter("CustomerID", "" + G.setting.customerID);
        mw.enableCache(false);
        mw.connectionTimeout(10000);
        mw.socketTimeout(5000);
        mw.url(G.URL_Webservice);
        mw.setListener(new WebServiceListener() {
            @Override
            public void onFail(int statusCode) {
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        G.log("ActivitySettingSMS : Loading SMS web service Faild.");
                        fo.txtRcount.setText(G.T.getSentence(733));
                    }
                });
            }


            @Override
            public void onDataReceive(final String data, boolean cached) {
                G.log("ActivitySettingSMS : Data recived from web service- SMS count :" + data);
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jo = (new JSONArray(data)).getJSONObject(0);
                            int rSMS = jo.getInt("Rcount");
                            if (rSMS >= 0) {
                                fo.txtRcount.setText(G.T.getSentence(732) + " : " + rSMS);
                            }
                            else
                                fo.txtRcount.setText(G.T.getSentence(732) + " : 0");
                        }
                        catch (Exception e) {
                            G.log("ActivitySettingSMS : Loading SMS Url Error.");
                            fo.txtRcount.setText(G.T.getSentence(733));
                        }

                    }
                });
            }
        });
        mw.read();
    }


    private void refreshList() {
        ArrayAdapter<String> adapter = null;

        listMobiles = new ArrayList<String>();
        String mns = G.setting.validMobiles;
        G.log("ActivitySettingSMS : All mobile numbers :" + mns);
        if (mns.length() > 1) {
            mns = mns.substring(0, mns.length() - 1);
            String tmp[] = mns.split(";");
            for (int i = 0; i < tmp.length; i++) {
                G.log(tmp[i]);
                listMobiles.add(tmp[i]);
            }
        }
        listAdapter = new AdapterScenarioNotifyMobiles(G.currentActivity, listMobiles);
        listAdapter.setOnDeleteItemListener(new onDeleteItem() {
            @Override
            public void onDeleteItemListener(final int index) {

                DialogClass dlgc = new DialogClass(G.currentActivity);
                dlgc.setOnYesNoListener(new YesNoListener() {

                    @Override
                    public void yesClick() {

                        G.log("ActivitySettingSMS : Item Deleted :" + listMobiles.get(index) + ";");
                        ModuleWebservice mw = new ModuleWebservice();
                        mw.addParameter("rt", "RemoveMobile");
                        mw.addParameter("ExKey", G.setting.exKey);
                        mw.addParameter("CustomerID", "" + G.setting.customerID);
                        mw.addParameter("MobileNumber", listMobiles.get(index));
                        mw.enableCache(false);
                        mw.connectionTimeout(10000);
                        mw.socketTimeout(5000);
                        mw.url(G.URL_Webservice);
                        mw.setListener(new WebServiceListener() {
                            @Override
                            public void onFail(int statusCode) {
                                G.HANDLER.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        G.log("ActivitySettingSMS : Deleting Mobile web service Faild.");
                                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(154), G.T.getSentence(733));
                                    }
                                });
                            }
                            @Override
                            public void onDataReceive(final String data, boolean cached) {
                                G.HANDLER.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jo = (new JSONArray(data)).getJSONObject(0);
                                            int resultID = jo.getInt("MessageID");
                                            if (resultID == 1) { // OK
                                                // Mobile Deleted From Server
                                                listMobiles.remove(index);
                                                G.setting.validMobiles = "";
                                                for (int i = 0; i < listMobiles.size(); i++) {
                                                    G.setting.validMobiles += listMobiles.get(i) + ";";
                                                }
                                                G.log(G.setting.validMobiles);
                                                Database.Setting.edit(G.setting);
                                                refreshList();
                                            }
                                            else
                                                new DialogClass(G.currentActivity).showOk(G.T.getSentence(154), G.T.getSentence(734));
                                        }
                                        catch (Exception e) {
                                            G.log("ActivitySettingSMS : Loading SMS Url Error.");
                                            new DialogClass(G.currentActivity).showOk(G.T.getSentence(154), G.T.getSentence(734));
                                        }
                                    }
                                });
                            }
                        });
                        mw.read();
                    }

                    @Override
                    public void noClick() {

                    }
                });
                dlgc.showYesNo(G.T.getSentence(154), G.T.getSentence(735));
            }
        });
        G.HANDLER.post(new Runnable() {

            @Override
            public void run() {
                fo.lstMobiles.setAdapter(listAdapter);
            }
        });
    }



    DialogClass dlgWaite;
    String      lastVerifyCode;
    String      lastVerifyMobile;

    private void verifyMobileNumber(final String mobileNumber) {
        dlgWaite = new DialogClass(this);
        dlgWaite.showWaite(G.T.getSentence(739), G.T.getSentence(728));
        ModuleWebservice mw = new ModuleWebservice();
        mw.url(G.URL_Webservice);
        mw.addParameter("rt", "SendMobileVerification");
        mw.addParameter("CustomerID", "" + G.setting.customerID);
        mw.addParameter("ExKey", G.setting.exKey);
        mw.addParameter("MobileNumber", mobileNumber);
        mw.setListener(new WebServiceListener() {

            @Override
            public void onFail(int statusCode) {
                dlgWaite.dissmiss();
                new DialogClass(G.currentActivity).showOk(G.T.getSentence(739), G.T.getSentence(741));
            }

            @Override
            public void onDataReceive(String data, boolean cached) {

                dlgWaite.dissmiss();
                try {
                    JSONObject jo = (new JSONArray(data)).getJSONObject(0);
                    int resultID = jo.getInt("MessageID");
                    if (resultID == 1) { // OK
                        lastVerifyMobile = mobileNumber;
                        lastVerifyCode = jo.getString("VerifyCode");
                        DialogClass dlgInput = new DialogClass(G.currentActivity);
                        dlgInput.setOnOkCancelInputListener(new OkCancelInputListener() {

                            @Override
                            public boolean okClick(String input) {
                                if (input.length() == 0 || !input.equalsIgnoreCase(lastVerifyCode)) {
                                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(739), G.T.getSentence(745));
                                    return true;// don allow to close the edit text dialog
                                }
                                registerMobile(mobileNumber);
                                return false; // close the dialog
                            }

                            @Override
                            public boolean cancelClick() {
                                return false; // close the dialog
                            }
                        });
                        dlgInput.showOkCancelInput(G.T.getSentence(739), G.T.getSentence(748));

                    }
                    else if (resultID == 2) { // Not allowed - Repeative
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(739), G.T.getSentence(744));
                        return;
                    }
                    else if (resultID == 3) { //Mobile not supported
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(739), G.T.getSentence(743));
                        return;
                    }
                    else { //Unknown Error
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(739), G.T.getSentence(742));
                        return;
                    }
                }
                catch (Exception e) {
                    G.log("ActivitySettingSMS : Loading SMS Url Error.");
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(154), G.T.getSentence(734));
                }
            }
        });
        mw.read();
    }
    private void registerMobile(final String mobileNumber) {
        dlgWaite.showWaite(G.T.getSentence(739), G.T.getSentence(728));
        ModuleWebservice mw = new ModuleWebservice();
        mw.url(G.URL_Webservice);
        mw.addParameter("rt", "RegisterMobile");
        mw.addParameter("CustomerID", "" + G.setting.customerID);
        mw.addParameter("ExKey", G.setting.exKey);
        mw.addParameter("MobileNumber", mobileNumber);
        mw.setListener(new WebServiceListener() {

            @Override
            public void onFail(int statusCode) {
                DialogClass ynDLG = new DialogClass(G.currentActivity);
                ynDLG.setOnYesNoListener(new YesNoListener() {

                    @Override
                    public void yesClick() {
                        registerMobile(mobileNumber);
                    }

                    @Override
                    public void noClick() {
                        dlgWaite.dissmiss();
                    }
                });
                ynDLG.showYesNo(G.T.getSentence(739), G.T.getSentence(746));
            }

            @Override
            public void onDataReceive(String data, boolean cached) {
                dlgWaite.dissmiss();
                try {
                    JSONObject jo = (new JSONArray(data)).getJSONObject(0);
                    int resultID = jo.getInt("MessageID");
                    if (resultID == 1) { // OK
                        G.setting.validMobiles = G.setting.validMobiles + mobileNumber + ";";
                        Database.Setting.edit(G.setting);
                        refreshList();
                    }
                    else { //Unknown Error
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(739), G.T.getSentence(747));
                        return;
                    }
                }
                catch (Exception e) {
                    G.printStackTrace(e);
                }
            }
        });
        mw.read();

    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.btnCharge.setText(G.T.getSentence(736));
        fo.txtListTitle.setText(G.T.getSentence(737));
        fo.txtCountryCaption.setText(G.T.getSentence(738));
        fo.btnVerify.setText(G.T.getSentence(739));
    }

}
