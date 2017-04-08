package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import java.util.Arrays;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_setting_user;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.adapters.AdapterUserListView;
import ir.parsansoft.app.ihs.center.adapters.AdapterUserListView.OnDeleteListener;
import ir.parsansoft.app.ihs.center.adapters.AdapterUserListView.OnUpdateListener;

public class ActivityWelcome12User extends ActivityWizard {

    String[]               userType = { G.T.getSentence(717), G.T.getSentence(718) };
    CO_f_setting_user      fo;
    ArrayAdapter<String>   spinnerAdapter;
    Database.User.Struct[] users;
    boolean                isBusy   = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            super.setContentView(R.layout.f_welcome_user);
        else
            super.setContentView(R.layout.f_welcome_user_rtl);
        fo = new CO_f_setting_user(this);
        fillSpnUserType();
        refreshList();
        translateForm();
        fo.btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String pass1 = fo.edtPass.getText().toString().trim();
                String pass2 = fo.edtConfirmPass.getText().toString().trim();
                String userName = fo.edtUsername.getText().toString().trim();
                if (userName.length() != 0 && pass1.length() != 0 && pass2.length() != 0)
                {
                    Database.User.Struct[] users = Database.User.select("Username = '" + userName + "'");
                    if (users == null)
                    {
                        if (pass1.equals(pass2))
                        {
                            Database.User.Struct newUser = new Database.User.Struct();
                            newUser.username = fo.edtUsername.getText().toString().trim();
                            newUser.password = pass1;
                            newUser.rol = fo.spnType.getSelectedItemPosition() + 1;
                            Database.User.insert(newUser);
                            fo.edtPass.setText("");
                            fo.edtConfirmPass.setText("");
                            fo.edtUsername.setText("");
                            refreshList();
                        }
                        else
                        {
                            //password ha hamkhani nadarand.
                            new DialogClass(G.currentActivity).showOk(G.T.getSentence(756), G.T.getSentence(795));
                        }
                    }
                    else
                    {
                        // karbar ghablan sabt shode ast
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(756), G.T.getSentence(796));
                    }
                }
                else
                {
                    // namayesh peyghame inke maghadir ra kamel vared konid..
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(756), G.T.getSentence(797));
                }
            }
        });

        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    startActivity(new Intent(G.currentActivity, ActivityWelcome11Scenario.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        setOnNextListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (users == null) {
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(756), G.T.getSentence(797));
                    return;
                }
                if ( !isBusy) {
                    isBusy = true;
                    G.currentUser = users[0];
                    startActivity(new Intent(G.currentActivity, ActivityWelcome13ChargeSMS.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

    }
    private void fillSpnUserType() {
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userType);
        fo.spnType.setAdapter(spinnerAdapter);
    }

    private void refreshList() {
        users = Database.User.select("");
        AdapterUserListView usersAdapter = null;
        if (users != null) {
            usersAdapter = new AdapterUserListView(Arrays.asList(users));
            usersAdapter.setOnDeleteListener(new OnDeleteListener() {
                @Override
                public void onDelete(Database.User.Struct user) {
                    Database.User.delete(user.iD);
                    SysLog.log("User " + user.name + " Deleted.", SysLog.LogType.SYSTEM_SETTINGS, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                    refreshList();
                }
            });

            usersAdapter.setOnUpdateListener(new OnUpdateListener() {
                @Override
                public void onUpdate(Database.User.Struct user) {

                }
            });
        }
        fo.lstUser.setAdapter(usersAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.textView1.setText(G.T.getSentence(713));
        fo.TextView01.setText(G.T.getSentence(714));
        fo.TextView02.setText(G.T.getSentence(715));
        fo.TextView03.setText(G.T.getSentence(716));
        fo.btnSave.setText(G.T.getSentence(124));
        fo.txtWebsiteInfo.setText(G.T.getSentence(808) + "\n" + G.T.getSentence(786) + " : " + G.setting.user + "\n" + G.T.getSentence(787) + " : " + G.setting.pass);

    }
}
