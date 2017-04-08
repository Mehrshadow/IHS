package ir.parsansoft.app.ihs.center.adapters;

import ir.parsansoft.app.ihs.center.ActivitySettingReset;
import ir.parsansoft.app.ihs.center.Backup;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.DialogClass;
import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.NetMessage;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageAction;
import ir.parsansoft.app.ihs.center.NetMessage.NetMessageType;
import ir.parsansoft.app.ihs.center.R;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterListViewBackup extends BaseAdapter {

    ArrayList<String> backups   = new ArrayList<String>();
    ArrayList<String> files     = new ArrayList<String>();
    Backup            backupObj = new Backup();

    public AdapterListViewBackup() {
        File backupFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BackupIHsApplication/");
        if ( !backupFolder.exists())
            backupFolder.mkdirs();
        backups = getList(backupFolder);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String backup = backups.get(position);
        convertView = G.inflater.inflate(R.layout.l_backup_item, parent, false);
        TextView txtBackupName, txtBackupDate;
        Button btnDelete, btnRestore;
        LinearLayout layout;
        txtBackupName = (TextView) convertView.findViewById(R.id.txtBackupName);
        txtBackupDate = (TextView) convertView.findViewById(R.id.txtBackupDate);
        layout = (LinearLayout) convertView.findViewById(R.id.layBack);
        btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
        btnDelete.setText(G.T.getSentence(106));
        btnRestore = (Button) convertView.findViewById(R.id.btnRestore);
        btnRestore.setText(G.T.getSentence(824));
        JSONObject json;
        String name = "";
        try {
            json = new JSONObject(backup);
            name = json.getString("Name");
            String date = json.getString("Date");
            txtBackupName.setText(name);
            txtBackupDate.setText(date);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        final String currentName = files.get(position);
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final DialogClass dlg = new DialogClass(G.currentActivity);
                dlg.setOnYesNoListener(new YesNoListener() {
                    @Override
                    public void yesClick() {
                        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/BackupIHsApplication/" + currentName);
                        file.delete();
                        removeBackup(position);
                    }
                    @Override
                    public void noClick() {}
                });
                dlg.showYesNo(G.T.getSentence(822), G.T.getSentence(830));
            }
        });

        btnRestore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final DialogClass dlg = new DialogClass(G.currentActivity);
                dlg.setOnYesNoListener(new YesNoListener() {
                    @Override
                    public void yesClick() {
                        File fileCurrentDatabase = new File(G.DATABASE_FILE_PATH);
                        fileCurrentDatabase.delete();
                        try {
                            backupObj.writeBytesToFile(G.DATABASE_FILE_PATH, getDatabaseFromByteArray(currentName));
                            Database.InitializeDB();
                            G.setting = Database.Setting.select("")[0];
                            G.T.changeLanguage(G.setting.languageID);


                            String result = Database.generateNewMobileConfiguration(null);
                            // Send message to local Mobile
                            NetMessage netMessage = new NetMessage();
                            netMessage.data = result;
                            netMessage.action = NetMessage.Update;
                            netMessage.type = NetMessage.RefreshData;
                            netMessage.typeName = NetMessageType.RefreshData;
                            netMessage.messageID = 0;
                            netMessage.recieverIDs = null;
                            G.server.sendMessage(netMessage);
                            G.mobileCommunication.sendMessage(netMessage);

                            dlg.dissmiss();
                            G.toast("Restore successfully");
                            G.currentActivity.startActivity(new Intent(G.currentActivity, ActivitySettingReset.class));
                            G.currentActivity.overridePendingTransition(R.anim.fast_change_activity_show, R.anim.fast_change_activity_hide);
                            G.currentActivity.finishAffinity();
                            G.currentActivity.finish();
                            G.HANDLER.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    System.exit(0);
                                }
                            }, 1000);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void noClick() {}
                });
                dlg.showYesNo(G.T.getSentence(822), G.T.getSentence(831));
            }
        });
        return convertView;
    }
    private ArrayList<String> getList(File parentDir) {
        ArrayList<String> inFiles = new ArrayList<String>();
        Backup backupObj = new Backup();
        String[] fileNames = parentDir.list();
        if (fileNames.length != 0)
            for (String fileName: fileNames) {
                if (fileName.toLowerCase().endsWith(".backup")) {
                    files.add(fileName);
                    String detail = getJsonDetailFromByteArray(fileName);
                    inFiles.add(detail);
                }
            }
        else
            G.toast("No restore file found");
        return inFiles;
    }
    public void removeBackup(int position) {
        backups.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return backups.size();
    }

    @Override
    public String getItem(int position) {
        return backups.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    private int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    private int getSizeOfDetailByteArray(String fileName) {
        try {
            byte[] size = backupObj.readBytesFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BackupIHsApplication/" + fileName, 0, 4);
            int sizeDetailArray = fromByteArray(size);
            return sizeDetailArray;
        }
        catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private String getJsonDetailFromByteArray(String fileName) {
        try {
            int length = getSizeOfDetailByteArray(fileName);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BackupIHsApplication/" + fileName;
            String detail = new String(backupObj.readBytesFromFile(path, 4, length), "UTF-8");
            return detail;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private byte[] getDatabaseFromByteArray(String fileName) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BackupIHsApplication/" + fileName;
        File file = new File(path);
        int sizeOfCombineFile = (int) file.length();
        int sizeOfUnDatabase = getSizeOfDetailByteArray(fileName) + 4;
        int sizeOfDatabaseFile = sizeOfCombineFile - sizeOfUnDatabase;
        try {
            return backupObj.readBytesFromFile(path, sizeOfUnDatabase, sizeOfDatabaseFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
