package ir.parsansoft.app.ihs.center;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewBackup;


public class ActivitySettingReset extends ActivitySetting {

    Button btnReset, btnRestore, btnBackup;
    TextView txtResetDescription, txtBackupDescription, txtRestoreDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_setting_reset);
        changeSlidebarImage(14);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnBackup = (Button) findViewById(R.id.btnBackup);
        btnRestore = (Button) findViewById(R.id.btnRestore);
        txtResetDescription = (TextView) findViewById(R.id.txtResetDescription);
        txtBackupDescription = (TextView) findViewById(R.id.txtBackupDescription);
        txtRestoreDescription = (TextView) findViewById(R.id.txtRestoreDescription);
        translateForm();
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final DialogClass dlg = new DialogClass(G.currentActivity);
                dlg.setOnYesNoListener(new YesNoListener() {
                    @Override
                    public void yesClick() {
                        try {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("ir.parsansoft.launcher", "ir.parsansoft.launcher.ActivityBundle"));
                            JSONObject json = new JSONObject();
                            json.put("ACTION", "Reset");
                            json.put("PACKAGE", G.context.getPackageName());
                            json.put("PATH", Environment.getExternalStorageDirectory().getAbsoluteFile() + "/temp/iHsCenter.apk");
                            intent.putExtra("EVENT", json.toString());


                            File f = new File(G.DIR_DATABASE);
                            if (Utility.deleteRecursive(f))
                                Log.i("LOG", "Application Deleted Seccessfully.");


                            startActivity(intent);
                            System.exit(0);
                        }
                        catch (Exception e) {
                            G.printStackTrace(e);
                            dlg.dissmiss();
                        }
                    }
                    @Override
                    public void noClick() {
                        dlg.dissmiss();
                    }
                });
                dlg.showYesNo(G.T.getSentence(767), G.T.getSentence(768));
            }
        });


        btnBackup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dlg = new Dialog(G.currentActivity);
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = G.inflater.inflate(R.layout.dialog_backup, null, false);
                dlg.setCanceledOnTouchOutside(true);
                dlg.setCancelable(true);
                dlg.setContentView(view);
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                TextView txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
                txtTitle.setText(G.T.getSentence(819));
                final EditText edtBackupName = (EditText) dlg.findViewById(R.id.edtBackupName);
                Button btnBackup = (Button) dlg.findViewById(R.id.btnBackup);
                btnBackup.setText(G.T.getSentence(822));
                Button btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
                btnCancel.setText(G.T.getSentence(102));
                btnBackup.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        String fileName = edtBackupName.getText().toString().trim();
                        if (fileName != "") {
                            try {
                                JSONObject json = new JSONObject();
                                json.put("Name", fileName);
                                json.put("Date", new Date());
                                json.put("Ver", G.setting.ver);
                                json.put("CustomerID", G.setting.customerID);
                                byte[] detailByteArray = (json.toString()).getBytes(Charset.forName("UTF-8"));
                                byte[] sizeOfDetailByteArray = intToByteArray(detailByteArray.length);
                                Log.d("Database", "sizeOfDetailByteArray " + fromByteArray(sizeOfDetailByteArray) + "/" + detailByteArray.length);
                                Backup backupObj = new Backup();
                                byte[] combineDetailsArray = backupObj.combineByteArrays(sizeOfDetailByteArray, detailByteArray);
                                Log.d("Database", "combineDetailsArray " + combineDetailsArray.length);
                                byte[] databaseByteArray = backupObj.readBytesFromFile(G.DATABASE_FILE_PATH);
                                byte[] combineByteArray = backupObj.combineByteArrays(combineDetailsArray, databaseByteArray);
                                String filePathToSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BackupIHsApplication/";
                                File file = new File(filePathToSave);
                                if ( !file.exists())
                                    file.mkdirs();
                                backupObj.writeBytesToFile(filePathToSave + Utility.getDateFormated() + ".backup", combineByteArray);
                                dlg.dismiss();
                                G.toast("Backup Finish");
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            DialogClass dlg = new DialogClass(G.currentActivity);
                            dlg.showOk("Warning", "*********");
                        }
                    }
                });

                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dlg.dismiss();
                    }
                });
                dlg.show();
            }
        });

        btnRestore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dlg = new Dialog(G.currentActivity);
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = G.inflater.inflate(R.layout.dialog_backup_list, null, false);
                dlg.setCanceledOnTouchOutside(true);
                dlg.setCancelable(true);
                dlg.setContentView(view);
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                TextView txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
                txtTitle.setText(G.T.getSentence(823));
                ListView lstBackup = (ListView) dlg.findViewById(R.id.lstBackup);
                Button btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
                btnCancel.setText(G.T.getSentence(102));
                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dlg.dismiss();
                    }
                });
                AdapterListViewBackup adapter = new AdapterListViewBackup();
                lstBackup.setAdapter(adapter);
                dlg.show();
            }
        });
    }
    @Override
    public void translateForm() {
        super.translateForm();
        txtResetDescription.setText(G.T.getSentence(766));
        txtBackupDescription.setText(G.T.getSentence(820));
        txtRestoreDescription.setText(G.T.getSentence(821));
        btnReset.setText(G.T.getSentence(767));
        btnBackup.setText(G.T.getSentence(822));
        btnRestore.setText(G.T.getSentence(824));
    }

    private byte[] intToByteArray(final int i) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(i);
        dos.flush();
        return bos.toByteArray();
    }
    private int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }
}
