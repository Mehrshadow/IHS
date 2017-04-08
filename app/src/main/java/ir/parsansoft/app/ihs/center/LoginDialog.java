package ir.parsansoft.app.ihs.center;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.util.Arrays;

import ir.parsansoft.app.ihs.center.AllViews.CO_d_setting_user_access;
import ir.parsansoft.app.ihs.center.adapters.AdapterUserSpinner;
import ir.parsansoft.app.ihs.center.components.AsteriskPasswordTransformationMethod;

public class LoginDialog {

    private LoginListener          ll;
    private Database.User.Struct[] users;
    CO_d_setting_user_access       dlgObjects;
    private boolean                cancelable = true;

    public LoginDialog() {
        dialog = new Dialog(G.currentActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.d_setting_user_access);
        dlgObjects = new CO_d_setting_user_access(dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setCancelable(false);
        dlgObjects.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dlgObjects.edtPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        ImageView imgKeyboard = (ImageView) dialog.findViewById(R.id.imgKeyboard);
        imgKeyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                InputMethodManager imm = (InputMethodManager) G.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) {
                    CloseKeyBoard();
                } else {
                    OpenKeyBoard();
                }
            }
        });
        imgKeyboard.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                //                Intent intent = new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                //                startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings");
                G.currentActivity.startActivity(intent);
                return false;
            }
        });

    }


    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }
    public interface LoginListener {
        public boolean onLoginSuccess(Database.User.Struct user);
        public boolean onLoginFailed();
        public boolean onLoginCancel();
    }

    public void setLoginListener(LoginListener loginListener) {
        ll = loginListener;
    }
    Dialog dialog;
    public void showDialog() {

        users = Database.User.select("");

        if (users != null) {
            dlgObjects.spnUser.setAdapter(new AdapterUserSpinner(Arrays.asList(users)));
            dlgObjects.btnLogin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Database.User.Struct user = users[dlgObjects.spnUser.getSelectedItemPosition()];
                    if (user.password.equals(dlgObjects.edtPassword.getText().toString()))
                    {
                        if (ll != null && !ll.onLoginSuccess(user))
                            dialog.dismiss();
                    }
                    else // dar surati ke password nadorost bashad
                    {
                        //                                    dialog.hide();
                        //                                    DialogClass dlg = new DialogClass(G.currentActivity);
                        //                                    dlg.setOnOkListener(new OkListener() {
                        //                                        @Override
                        //                                        public void okClick() {
                        //                                            dialog.show();
                        //                                        }
                        //                                    });
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(216), G.T.getSentence(788));

                        if (ll != null && !ll.onLoginFailed())
                            dialog.dismiss();
                    }
                }
            });
            /*if (cancelable)
            {
                dlgObjects.btnCancel.setVisibility(View.VISIBLE);
                dlgObjects.btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (ll != null) {
                            if ( !ll.onLoginCancel()) {
                                dialog.dismiss();
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
            }
            else
                dlgObjects.btnCancel.setVisibility(View.GONE);*/

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            try {
                dialog.show();
            }
            catch (Exception e) {}
        }
        else // dar surati ke hich karbari sabt nashode bashad
        {
            G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingUser.class));
            Animation.doAnimation(Animation.Animation_Types.FADE);
            dialog.dismiss();
        }
    }
    public void OpenKeyBoard() {
        InputMethodManager imm = (InputMethodManager) G.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    //For close keyboard
    public void CloseKeyBoard() {
        G.currentActivity.getWindow().getDecorView().clearFocus();
        InputMethodManager imm = (InputMethodManager) G.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
