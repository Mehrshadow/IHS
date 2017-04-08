package ir.parsansoft.app.ihs.center;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import java.util.Arrays;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_setting_user;
import ir.parsansoft.app.ihs.center.adapters.AdapterUserListView;
import ir.parsansoft.app.ihs.center.adapters.AdapterUserListView.OnDeleteListener;
import ir.parsansoft.app.ihs.center.adapters.AdapterUserListView.OnUpdateListener;


public class ActivitySettingUser extends ActivitySetting {


    String[]               userType = { G.T.getSentence(717), G.T.getSentence(718) };
    CO_f_setting_user      fo;
    ArrayAdapter<String>   spinnerAdapter;
    Database.User.Struct[] users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_setting_user);
        else
            setContentView(R.layout.f_setting_user_rtl);

        changeSlidebarImage(7);
        fo = new CO_f_setting_user(this);
        translateForm();
        fillSpnUserType();
        refreshList();
        //        fo.chkShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //			@Override
        //			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
        //				if(isChecked) {
        //					fo.edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //                } else {
        //                	fo.edtPass.setInputType(129);
        //                }
        //			}
        //		});
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
                            SysLog.log("User " + newUser.name + " Added.", SysLog.LogType.SYSTEM_SETTINGS, SysLog.LogOperator.OPERAOR, G.currentUser.iD);
                            fo.edtPass.setText("");
                            fo.edtConfirmPass.setText("");
                            fo.edtUsername.setText("");
                            refreshList();
                        }
                        else
                        {
                            //password ha hamkhani nadarand.
                            DialogClass dlg = new DialogClass(G.currentActivity);
                            dlg.showOk(G.T.getSentence(756), G.T.getSentence(795));
                        }
                    }
                    else
                    {
                        // karbar ghablan sabt shode ast
                        DialogClass dlg = new DialogClass(G.currentActivity);
                        dlg.showOk(G.T.getSentence(756), G.T.getSentence(796));
                    }
                }
                else
                {
                    // namayesh peyghame inke maghadir ra kamel vared konid..
                    DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.showOk(G.T.getSentence(756), G.T.getSentence(797));
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
                public void onUpdate(final Database.User.Struct user) {
                    //refreshList();
                }
            });
        }
        fo.lstUser.setAdapter(usersAdapter);
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
