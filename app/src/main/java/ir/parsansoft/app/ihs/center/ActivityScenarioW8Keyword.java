package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario_w7;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;


public class ActivityScenarioW8Keyword extends ActivityEnhanced {

    private CO_f_senario_w7  fo;
    Activity                 form;
    Database.Scenario.Struct scenario = null;
    boolean                  isBusy   = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_senario_w8);
        else
            setContentView(R.layout.f_senario_w8_rtl);
        form = this;
        fo = new CO_f_senario_w7(this);
        changeMenuIconBySelect(3);
        setSideBarVisiblity(false);
        translateForm();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("SCENARIO_ID")) {
                int id = extras.getInt("SCENARIO_ID");
                scenario = Database.Scenario.select(id);
                if (scenario != null)
                    initializeForm();
            }
        }


        fo.btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.log("Goto Next : 8");
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW9Switch.class);
                        intent.putExtra("SCENARIO_ID", scenario.iD);
                        G.currentActivity.startActivity(intent);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                        form.finish();
                    }
                    else
                        isBusy = false;
                }
            }
        });

        fo.btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    if (saveForm()) {
                        Intent intent = new Intent(G.currentActivity, ActivityScenarioW7Op.class);
                        intent.putExtra("SCENARIO_ID", scenario.iD);
                        G.currentActivity.startActivity(intent);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                        form.finish();
                    }
                    else
                        isBusy = false;
                }
            }
        });

        fo.btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                form.finish();
                Animation.doAnimation(Animation_Types.FADE);
            }
        });


        fo.edtKeyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
//                fo.txtStartKey.setText(fo.edtKeyword.getText().toString().trim() + G.SCENARIO_START_SMS_KEYWORD);
//                fo.txtEnableKey.setText(fo.edtKeyword.getText().toString().trim() + G.SCENARIO_ENABLE_SMS_KEYWORD);
//                fo.txtDisableKey.setText(fo.edtKeyword.getText().toString().trim() + G.SCENARIO_DISABLE_SMS_KEYWORD);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fo.txtStartKey.setText(s.toString().trim() + G.SCENARIO_START_SMS_KEYWORD);
                fo.txtEnableKey.setText(s.toString().trim() + G.SCENARIO_ENABLE_SMS_KEYWORD);
                fo.txtDisableKey.setText(s.toString().trim() + G.SCENARIO_DISABLE_SMS_KEYWORD);
            }
        });

        fo.edtKeyword.setFilters(new InputFilter[] {
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                        if(src.equals("")){ // for backspace
                            return src;
                        }else if(src.toString().matches("[a-zA-Z]+")){
                            return src;
                        }
                        else {
                            DialogClass dialogClass = new DialogClass(G.currentActivity);
                            dialogClass.showOk(G.T.getSentence(216),G.T.getSentence(244));
                        }
                        return "";
                    }
                }
        });
    }
    private void initializeForm() {
        if (scenario.preSMSKeywords.length() > 0)
            fo.edtKeyword.setText(scenario.preSMSKeywords);
        else
            fo.edtKeyword.setText(scenario.name.replaceAll("[^A-Za-z0-9]", ""));
        fo.txtStartKey.setText(fo.edtKeyword.getText().toString().trim() + G.SCENARIO_START_SMS_KEYWORD);
        fo.txtEnableKey.setText(fo.edtKeyword.getText().toString().trim() + G.SCENARIO_ENABLE_SMS_KEYWORD);
        fo.txtDisableKey.setText(fo.edtKeyword.getText().toString().trim() + G.SCENARIO_DISABLE_SMS_KEYWORD);
    }
    private boolean validateKeyword() {
        Pattern pattern;
        Matcher matcher;
        String PATTERN = "^[a-zA-Z0-9_.-]*$";
        pattern = Pattern.compile(PATTERN);
        matcher = pattern.matcher(fo.edtKeyword.getText().toString().trim());
        return matcher.matches();
    }
    private boolean saveForm() {

        if ( !validateKeyword()) {
            // Please enter a keyword containing english letters and numbers only
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(513));
            return false;
        }
        scenario.preSMSKeywords = fo.edtKeyword.getText().toString().trim().toLowerCase();
        if (scenario.preSMSKeywords.length() == 0) {
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(576));
            return false;
        }
        Database.Scenario.Struct[] similarKeywordScenarios = Database.Scenario.select("PreSMSKeywords LIKE '" + scenario.preSMSKeywords + "' AND ID!=" + scenario.iD);
        if (similarKeywordScenarios != null) {
            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(580).replace("[1]", similarKeywordScenarios[0].name));
            return false;
        }

        if (scenario.iD == 0) {
            G.log("Add new scenario ... ");
            // Insert into database
            try {
                scenario.iD = (int) Database.Scenario.insert(scenario);
                return true;
            }
            catch (Exception ex) {
                G.printStackTrace(ex);
                return false;
            }
        }
        else {
            // Edit current state
            G.log("Edit the scenario ... sensorNodeId=" + scenario.iD);
            try {
                Database.Scenario.edit(scenario);
                return true;
            }
            catch (Exception ex) {
                G.printStackTrace(ex);
                return false;
            }
        }
    }
    @Override
    public void translateForm() {
        super.translateForm();
        fo.txtHeaderText.setText(G.T.getSentence(557));
        fo.txtlblKeyword.setText(G.T.getSentence(558));
        fo.txtlblEnableKey.setText(G.T.getSentence(115) + " :");
        fo.txtlblDisableKey.setText(G.T.getSentence(116) + " :");
        fo.txtlblStartKey.setText(G.T.getSentence(559));
        fo.btnCancel.setText(G.T.getSentence(120));
        fo.btnBack.setText(G.T.getSentence(104));
        fo.btnNext.setText(G.T.getSentence(103));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }
}
