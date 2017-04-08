package ir.parsansoft.app.ihs.center.adapters;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.DialogClass;
import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.components.AsteriskPasswordTransformationMethod;

import java.util.List;

public class AdapterUserListView extends ArrayAdapter<Database.User.Struct> {
    private OnDeleteListener odl = null;
    private OnUpdateListener oul = null;

    public AdapterUserListView(List<Database.User.Struct> objects) {
        super(G.context, R.layout.l_list_setting_user, objects);
    }

    public interface OnDeleteListener {
        public void onDelete(Database.User.Struct user);
    }

    public interface OnUpdateListener {
        public void onUpdate(Database.User.Struct user);
    }

    public void setOnDeleteListener(OnDeleteListener odl) {
        this.odl = odl;
    }

    public void setOnUpdateListener(OnUpdateListener oul) {
        this.oul = oul;
    }

    private class ViewHolder {

        TextView txtUsername;
        ImageView imgEdit, imgDelete;

        public ViewHolder(View view) {
            txtUsername = (TextView) view.findViewById(R.id.txtUsername);
            imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        }

        public void fill(ArrayAdapter<Database.User.Struct> adapter, final Database.User.Struct obj, int position) {
            txtUsername.setText(obj.username);
            imgDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    G.log("Try to delete user with ID:" + obj.iD);
                    if (obj.iD == 1) {
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(216), G.T.getSentence(792));
                        return;
                    }
                    DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.setOnYesNoListener(new YesNoListener() {
                        @Override
                        public void yesClick() {
                            if (odl != null)
                                odl.onDelete(obj);
                        }
                        @Override
                        public void noClick() {

                        }
                    });
                    dlg.showYesNo(G.T.getSentence(756), G.T.getSentence(793));
                }
            });
            imgEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //G.log("Selected User ID:" + obj.iD + "  Current User ID:" + G.currentUser.iD);
                    if (G.currentUser.iD != 1 && obj.iD != G.currentUser.iD) {
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(216), G.T.getSentence(794));
                        return;
                    }
                    final Dialog dialog = new Dialog(G.currentActivity);
                    View view = G.inflater.inflate(R.layout.d_setting_user_change_password, null, false);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(view);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    final EditText edtLastPassword = (EditText) dialog.findViewById(R.id.edtLastPassword);
                    edtLastPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtLastPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    final EditText edtNewPassword = (EditText) dialog.findViewById(R.id.edtNewPassword);
                    edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtNewPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    final EditText edtRePassword = (EditText) dialog.findViewById(R.id.edtRePassword);
                    edtRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtRePassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                    Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                    TextView txtOldPass = (TextView) dialog.findViewById(R.id.txtOldPass);
                    TextView txtNewPass1 = (TextView) dialog.findViewById(R.id.txtNewPass1);
                    TextView txtNewPass2 = (TextView) dialog.findViewById(R.id.txtNewPass2);
                    TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                    txtTitle.setText(G.T.getSentence(798));
                    txtOldPass.setText(G.T.getSentence(799));
                    txtNewPass1.setText(G.T.getSentence(800));
                    txtNewPass2.setText(G.T.getSentence(801));
                    btnOk.setText(G.T.getSentence(126));
                    btnCancel.setText(G.T.getSentence(102));
                    //final Database.User.Struct user = Database.User.select(id);
                    btnOk.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ( !edtLastPassword.getText().toString().trim().equals("") && !edtNewPassword.getText().toString().trim().equals("") && !edtRePassword.getText().toString().trim().equals(""))
                            {
                                Database.User.Struct user = Database.User.select(obj.iD);
                                if (user.password.equals(edtLastPassword.getText().toString().trim()))
                                {
                                    if (edtNewPassword.getText().toString().trim().equals(edtRePassword.getText().toString().trim()) && !edtNewPassword.getText().toString().trim().equals(""))
                                    {
                                        Database.User.edit(user.iD, user.name, user.username, edtNewPassword.getText().toString().trim(), user.rol, user.status);
                                        if (oul != null)
                                            oul.onUpdate(obj);
                                        dialog.dismiss();
                                    }
                                    else
                                    {
                                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(798), G.T.getSentence(795));
                                    }
                                }
                                else
                                {
                                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(798), G.T.getSentence(788));
                                }
                            }
                            else
                            {
                                new DialogClass(G.currentActivity).showOk(G.T.getSentence(798), G.T.getSentence(797));
                            }
                        }
                    });
                    btnCancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            dialog.dismiss();
                        }
                    });
                    //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.show();





                }
            });
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Database.User.Struct obj = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = G.inflater.inflate(R.layout.l_list_setting_user, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.fill(this, obj, position);
        return convertView;
    }

}
