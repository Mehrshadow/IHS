package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.DialogClass.OkListener;
import ir.parsansoft.app.ihs.center.ModuleWebservice.WebServiceListener;
import ir.parsansoft.app.ihs.center.adapters.AdapterTelCodeSpinner;
import ir.parsansoft.app.ihs.center.adapters.AdapterUserListView;

public class ActivityWelcome7Specifications extends ActivityWizard {


    EditText              edtName, edtEmail, edtTel;
    MaskedEditText        edtSMSVerifyCode;
    Spinner               spnCode;
    LinearLayout          btn_sms_verify_header, btn_internet_verify_header;
    LinearLayout          lay_sms_verify, lay_internet_verify;
    Button                btnInternetVerify, btnSMSVerify;
    TextView              txtFullName, txtCode, txtEmail, txtTel, txtSMSDescription, txtRequestCode;
    TextView              txtlblSms, txtlblInternet, txtCountry;
    AdapterTelCodeSpinner adapterTelCode;
    AdapterUserListView   adapterUser;
    String[]              country;
    String                serialNumber;
    boolean               isBusy = false;
    DialogClass dlgClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            super.setContentView(R.layout.f_welcome_specifications);
        else
            super.setContentView(R.layout.f_welcome_specifications_rtl);

        txtlblInternet = (TextView) findViewById(R.id.txtlblInternet);
        txtlblSms = (TextView) findViewById(R.id.txtlblSms);
        txtCountry = (TextView) findViewById(R.id.txtCountry);
        txtFullName = (TextView) findViewById(R.id.txtFullName);
        txtCode = (TextView) findViewById(R.id.txtCode);
        txtRequestCode = (TextView) findViewById(R.id.txtRequestCode);
        txtSMSDescription = (TextView) findViewById(R.id.txtSMSDescription);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtTel = (TextView) findViewById(R.id.txtTel);
        spnCode = (Spinner) findViewById(R.id.spnCode);
        edtName = (EditText) findViewById(R.id.edtName);
        edtSMSVerifyCode = (MaskedEditText) findViewById(R.id.edtSMSVerifyCode);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTel = (EditText) findViewById(R.id.edtTel);
        btnInternetVerify = (Button) findViewById(R.id.btnInternetVerify);
        btnSMSVerify = (Button) findViewById(R.id.btnSMSVerify);
        btn_sms_verify_header = (LinearLayout) findViewById(R.id.btn_sms_verify);
        btn_internet_verify_header = (LinearLayout) findViewById(R.id.btn_internet_verify);
        lay_sms_verify = (LinearLayout) findViewById(R.id.lay_sms_verify);
        lay_internet_verify = (LinearLayout) findViewById(R.id.lay_internet_verify);
        btn_internet_verify_header.setBackgroundResource(R.drawable.lay_viewpager_header_press);
        lay_sms_verify.setVisibility(View.INVISIBLE);
        translateForm();
        fillCountryCode();
        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    startActivity(new Intent(G.currentActivity, ActivityWelcome6Weather.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        serialNumber = (new Utility.IPAddress().getWifiMacAddress()).trim().toLowerCase().replace(":", "");
        if (serialNumber.isEmpty()) {
            G.log("Could not find a valid serial number ! ");
            return;
        }

        txtRequestCode.setText("Reg: " + GenerateSMSRequestCode(serialNumber));
        hideNextButton();
        btnInternetVerify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String errMsg = "";
                if (edtName.getText().toString().trim().length() < 5) {
                    errMsg += G.T.getSentence(771) + "\n";
                }
                if ( !Utility.validateEmail(edtEmail.getText().toString().trim())) {
                    errMsg += G.T.getSentence(772) + "\n";
                }
                if (edtTel.getText().toString().trim().length() < 9 || !edtTel.getText().toString().trim().matches("\\d+")) {
                    errMsg += G.T.getSentence(773) + "\n";
                }
                if (errMsg.length() > 0)
                {
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(759), errMsg);
                    return;
                }
                if(dlgClass == null)
                    dlgClass = new DialogClass(G.currentActivity);
                dlgClass.showWaite("",G.T.getSentence(203));
                registerOnline();
            }
        });
        btnSMSVerify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (registerSMS()) {
                        startActivity(new Intent(G.currentActivity, ActivityWelcome8FloorandRoom.class));
                        Animation.doAnimation(Animation_Types.FADE);
                        finish();
                    }
                    else
                        isBusy = false;
                }
            }
        });

        spnCode.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] g = country[position].split(",");
                txtCode.setText("+" + g[0] + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_sms_verify_header.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                lay_internet_verify.setVisibility(View.GONE);
                lay_sms_verify.setVisibility(View.VISIBLE);
                btn_sms_verify_header.setBackgroundResource(R.drawable.lay_viewpager_header_press);
                btn_internet_verify_header.setBackgroundResource(R.drawable.viewpager_tabs_selector);
            }
        });
        btn_internet_verify_header.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                lay_sms_verify.setVisibility(View.GONE);
                lay_internet_verify.setVisibility(View.VISIBLE);
                btn_sms_verify_header.setBackgroundResource(R.drawable.viewpager_tabs_selector);
                btn_internet_verify_header.setBackgroundResource(R.drawable.lay_viewpager_header_press);
            }
        });
    }

    private void registerOnline() {
        ModuleWebservice mw = new ModuleWebservice();
        mw.addParameter("rt", "RegisterCustomer");
        mw.addParameter("Name", edtName.getText().toString().trim());
        mw.addParameter("Email", edtEmail.getText().toString().trim());
        mw.addParameter("Mobile", txtCode.getText().toString() + edtTel.getText().toString().trim());
        mw.addParameter("Latitude", "" + G.setting.gPSLat);
        mw.addParameter("Longitude", "" + G.setting.gPSLon);
        mw.addParameter("Language", "" + G.setting.languageID);
        mw.addParameter("SerialNumber", (new Utility.IPAddress().getWifiMacAddress()).trim().toLowerCase().replace(":", ""));
        //        try {
        //            mw.addParameter("CenterVer", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        //        }
        //        catch (NameNotFoundException e) {
        //            mw.addParameter("CenterVer", "Version Error");
        //        }
        mw.setListener(new WebServiceListener() {

            @Override
            public void onFail(int statusCode) {
                dlgClass.dissmiss();
                new DialogClass(G.currentActivity).showOk(G.T.getSentence(759), G.T.getSentence(769));
            }

            @Override
            public void onDataReceive(String data, boolean cached) {
                try {

                    dlgClass.dissmiss();

                    final JSONObject jo = (new JSONArray(data)).getJSONObject(0);
                    int cID = jo.getInt("CustomerID");
                    String exKey = jo.getString("ExKey");

                    if (cID > 0) {
                        G.setting.customerID = cID;
                        G.setting.exKey = exKey;

                        try {
                            String regDate = jo.getString("RegDate");
                            String expDate = jo.getString("ExpireDate");
                            //  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            G.setting.regDate = regDate;
                            G.setting.guaranteeDate = expDate;
                        }
                        catch (Exception e) {
                            G.printStackTrace(e);
                        }
                        Database.Setting.edit(G.setting);
                        if (G.serverCommunication == null)
                            G.serverCommunication = new ServiceServerCommunication();
//                        G.bp.restartWeatherAPI();
                        G.HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                String RegUsername = "";
                                String RegPassword = "";
                                try {
                                    RegUsername = jo.getString("UserName");
                                    RegPassword = jo.getString("Pass");
                                    G.setting.user = RegUsername;
                                    G.setting.pass = RegPassword;
                                    Database.Setting.edit(G.setting);
                                    DialogClass dlgOK;
                                    dlgOK = new DialogClass(G.currentActivity);
                                    dlgOK.cancelable = false;
                                    dlgOK.setOnOkListener(new OkListener() {
                                        @Override
                                        public void okClick() {
                                            startActivity(new Intent(G.currentActivity, ActivityWelcome8FloorandRoom.class));
                                            Animation.doAnimation(Animation_Types.FADE);
                                            finish();
                                        }
                                    });
                                    dlgOK.showOk(G.T.getSentence(782), G.T.getSentence(806) + "\n Username : " + RegUsername + "\nPassword : " + RegPassword + "\n" + G.T.getSentence(807));
                                }
                                catch (Exception e) {
                                    G.printStackTrace(e);
                                }
                            }
                        });
                        return;
                    }
                }
                catch (Exception e) {
                    G.printStackTrace(e);
                }
                new DialogClass(G.currentActivity).showOk(G.T.getSentence(759), G.T.getSentence(770));
            }
        });
        mw.read();
    }
    private String GenerateSMSRequestCode(String serialNumber) {
        String md5 = Utility.MD5(serialNumber);
        if (md5.length() > 4)
            md5 = md5.substring(md5.length() - 4);
        String tempstring = serialNumber + md5;
        String tempstring2 = "";
        for (int i = 0; i < tempstring.length(); i += 4)
        {
            tempstring2 += " " + tempstring.substring(i, Math.min(tempstring.length(), i + 4));
        }
        return tempstring2;
    }

    private boolean registerSMS() {


        String userInput = edtSMSVerifyCode.getText().toString().replace(" ", "").toLowerCase().trim();
        if (userInput.length() != 28) {
            G.log("Length of string is not valid");
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(780), G.T.getSentence(784));
            return false;
        }


        String sendSMS_Control = userInput.substring(0, 2);
        String customerID = userInput.substring(2, 8);
        String exKey = userInput.substring(8, 24);
        String recievedSMS_Control = userInput.substring(24, 28);
        G.log("sendSMS_Control : " + sendSMS_Control);
        G.log("customerID : " + customerID);
        G.log("exKey : " + exKey);
        G.log("recievedSMS_Control : " + recievedSMS_Control);

        //          sendSMS_Control   customerID   exKey    recievedSMS_Control
        //        (2* md5 send sms) + customerid + exkey +  (4* md5 all)
        //               2          +     6      +   16  +         4
        //        ---------------------------------------------
        //        HELP :
        //
        //        4* md5 =  4 righter digit of md5 of an string.
        //
        //
        //        Example :
        //            
        //        Device Mac  = 6C:FA:A7:5D:B3:DA
        //
        //        serial      = 6cfaa75db3da
        //
        //        MD5 serial  = 6acfd29738eda348ca5d85c8ad6a6d79
        //        send sms    = 6cfaa75db3da + 6d79 
        //                    = 6cfaa75db3da6d79
        //                    *** to beter show, reformat it and add "Reg:" ***
        //                    = Reg: 6cfa a75d b3da 6d79
        //
        //        answer :
        //        md5 send sms    = 6345e3b4611867c0dd11e6830dba7bfd
        //        customer sensorNodeId     = 3254
        //        Hex customer sensorNodeId = CB6  =  000CB6  = 000cb6
        //        exKey           = 69d01ea112c7d2d2
        //
        //        answer SMS      = fd + 000cb6 + 69d01ea112c7d2d2 + fc28
        //                        = fd000cb669d01ea112c7d2d2fc28
        //        for better read = fd00 - 0cb6 - 69d0 - 1ea1 - 12c7 - d2d2 - fc28




        String md5_sendSMS = Utility.MD5(GenerateSMSRequestCode(serialNumber).replace(" ", ""));
        if (md5_sendSMS.length() > 2)
            md5_sendSMS = md5_sendSMS.substring(md5_sendSMS.length() - 2);
        G.log("md5_sendSMS :" + md5_sendSMS);

        if ( !md5_sendSMS.equalsIgnoreCase(sendSMS_Control)) {
            G.log("The enterred key is not valid. Please check your send and reciecved sms.");
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(780), G.T.getSentence(785));
            return false;
        }

        String tempText = sendSMS_Control + customerID + exKey;
        String md5_recivedSMS = Utility.MD5(tempText);
        if (md5_recivedSMS.length() > 4)
            md5_recivedSMS = md5_recivedSMS.substring(md5_recivedSMS.length() - 4);
        G.log("md5_recivedSMS :" + md5_recivedSMS);
        if ( !md5_recivedSMS.equalsIgnoreCase(recievedSMS_Control)) {
            G.log("The enterred key is not valid. Please check your send and reciecved sms.");
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(780), G.T.getSentence(785));
            return false;
        }

        G.setting.customerID = Integer.parseInt(customerID);
        G.setting.exKey = exKey;

        Database.Setting.edit(G.setting);
        return true;
    }


    private void fillCountryCode() {
        country = getResources().getStringArray(R.array.countryCodes);
        adapterTelCode = new AdapterTelCodeSpinner(this, country);

        if (adapterTelCode != null)
            spnCode.setAdapter(adapterTelCode);

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
                spnCode.setSelection(i);
                break;
            }
        }
        spnCode.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] g = country[position].split(",");
                txtCode.setText("+" + g[0]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtCode.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }
    @Override
    public void translateForm() {
        super.translateForm();
        txtlblInternet.setText(G.T.getSentence(778));
        txtlblSms.setText(G.T.getSentence(780));
        txtSMSDescription.setText(G.T.getSentence(781));
        txtFullName.setText(G.T.getSentence(774));
        txtEmail.setText(G.T.getSentence(775));
        txtCountry.setText(G.T.getSentence(776));
        txtTel.setText(G.T.getSentence(777));
        btnInternetVerify.setText(G.T.getSentence(782));
        btnSMSVerify.setText(G.T.getSentence(783));
    }
}
