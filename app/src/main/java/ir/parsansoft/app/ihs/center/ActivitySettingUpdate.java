package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import ir.parsansoft.app.ihs.center.DialogClass.OkListener;
import ir.parsansoft.app.ihs.center.ModuleDownloader.OnDownloadCanceledListener;
import ir.parsansoft.app.ihs.center.ModuleDownloader.OnDownloadCompleteListener;
import ir.parsansoft.app.ihs.center.ModuleDownloader.OnProgressDownloadListener;
import ir.parsansoft.app.ihs.center.ModuleWebservice.WebServiceListener;


public class ActivitySettingUpdate extends ActivitySetting {


    TextView txtDescription, txtNewVersion, txtLastVersion, txtWaranty;
    Button btnUpdate;
    ProgressBar prgPercent;
    TextView txtPercent;
    Button btnCancelDownload;
    LinearLayout layProgress, layStartUpdate;
    String NewUpdateURL, NewVersion;
    String expDate, regDate;
    ModuleDownloader md;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_setting_update);
        changeSlidebarImage(13);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtNewVersion = (TextView) findViewById(R.id.txtNewVersion);
        txtLastVersion = (TextView) findViewById(R.id.txtLastVersion);
        txtWaranty = (TextView) findViewById(R.id.txtWaranty);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnCancelDownload = (Button) findViewById(R.id.btnCancelDownload);
        prgPercent = (ProgressBar) findViewById(R.id.prgPercent);
        txtPercent = (TextView) findViewById(R.id.txtPercent);
        layProgress = (LinearLayout) findViewById(R.id.layProgress);
        layStartUpdate = (LinearLayout) findViewById(R.id.layStartUpdate);
        layProgress.setVisibility(View.GONE);
        translateForm();
        loadNewVersion();
        loadExpDate();
    }

    private void loadNewVersion() {
        final ModuleWebservice mw = new ModuleWebservice();
        mw.addParameter("rt", "getCenterUpdate");
        mw.addParameter("CustomerID", "" + G.setting.customerID);
        mw.addParameter("ExKey", G.setting.exKey);
        mw.addParameter("AppVer", Utility.getApplicationVersionName());
        mw.addParameter("DeviceModel", Utility.getDeviceName());
        mw.setListener(new WebServiceListener() {

            @Override
            public void onFail(int statusCode) {
                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        txtNewVersion.setText(G.T.getSentence(812));
                        btnUpdate.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onDataReceive(String data, boolean cached) {
                try {
                    JSONObject jo = (new JSONArray(data)).getJSONObject(0);
                    final int MessageID = jo.getInt("MessageID");
                    NewVersion = jo.getString("Ver");
                    NewUpdateURL = jo.getString("URL");
                    G.HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            txtNewVersion.setText(G.T.getSentence(815) + " : " + NewVersion);
                            if (MessageID == 0) {// update not found
                                txtNewVersion.setText(G.T.getSentence(812));
                                btnUpdate.setVisibility(View.INVISIBLE);
                            }
                            if (MessageID == 2) {// app is up to date
                                txtNewVersion.setText(G.T.getSentence(813));
                                btnUpdate.setVisibility(View.INVISIBLE);
                            }
                            if (MessageID == 1) { // update available
                                btnUpdate.setVisibility(View.VISIBLE);
                                btnUpdate.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        /**************** Jahanbin *****************/
                                        double appVersion = Double.parseDouble(Utility.getApplicationVersionName());

                                        if (Double.parseDouble(NewVersion) > appVersion)
                                        /**************** Jahanbin *****************/
                                            downloadUpdate();
                                    }
                                });
                                btnCancelDownload.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        if (md != null)
                                            md.cancel();
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    G.HANDLER.post(new Runnable() {

                        @Override
                        public void run() {
                            txtNewVersion.setText(G.T.getSentence(812));
                            btnUpdate.setVisibility(View.INVISIBLE);
                        }
                    });
                    G.printStackTrace(e);
                }
            }
        });
        mw.read();
    }

    private void loadExpDate() {
        final ModuleWebservice mw = new ModuleWebservice();
        mw.addParameter("rt", "getCenterWarentyDate");
        mw.addParameter("CustomerID", "" + G.setting.customerID);
        mw.addParameter("ExKey", G.setting.exKey);
        mw.addParameter("SerialNumber", (new Utility.IPAddress().getWifiMacAddress()).trim().toLowerCase().replace(":", ""));
        mw.setListener(new WebServiceListener() {

            @Override
            public void onFail(int statusCode) {
                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        txtWaranty.setText(".");
                    }
                });
            }

            @Override
            public void onDataReceive(String data, boolean cached) {
                try {
                    JSONObject jo = (new JSONArray(data)).getJSONObject(0);
                    expDate = jo.getString("result");
                    G.HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            String res = G.T.getSentence(834);
                            res = res.replace("[1]", expDate);
                            txtWaranty.setText(res);
                        }
                    });
                } catch (Exception e) {
                    G.HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            txtWaranty.setText("...");
                        }
                    });
                    G.printStackTrace(e);
                }
            }
        });
        mw.read();
    }

    private void downloadUpdate() {
        layProgress.setVisibility(View.VISIBLE);
        layStartUpdate.setVisibility(View.GONE);
        md = new ModuleDownloader();
        md.downloadPath(NewUpdateURL);
        md.filepath(G.DIR_UPDATE + "/" + G.UPDATE_FILE_NAME);
        md.progressListener(new OnProgressDownloadListener() {
            @Override
            public void onProgressDownload(int percent, long downloadedSize, long fileSize) {
                prgPercent.setProgress(percent);
                txtPercent.setText(percent + "%\n"
                        + String.format("%.1f KB", downloadedSize / 1024.0)
                        + " / " + String.format("%.1f KB", fileSize / 1024.0));
            }
        });
        md.completeListener(new OnDownloadCompleteListener() {
            @Override
            public void OnDownloadComplete(String filePath) {
                layProgress.setVisibility(View.GONE);
                layStartUpdate.setVisibility(View.VISIBLE);
                // TODO: Install new app
                try {

                    /******************** Jahanbin **********************/
                    installApplication(filePath);
                    /******************** Jahanbin **********************/

//                    JSONObject json = new JSONObject();
//                    json.put("ACTION", "Update");
//                    json.put("PATH", filePath);
//                    Intent intent = new Intent();
//                    intent.setComponent(new ComponentName("ir.parsansoft.launcher", "ir.parsansoft.launcher.ActivityBundle"));
//                    intent.putExtra("EVENT", json.toString());
//                    startActivity(intent);

                    System.exit(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        });
        md.cancelListener(new OnDownloadCanceledListener() {

            @Override
            public void onDownloadCanceledListener(int errorCode) {
                if (errorCode == 1) { // cancelled manually
                    layProgress.setVisibility(View.GONE);
                    layStartUpdate.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.INVISIBLE);
                    loadNewVersion();
                } else { // cancelled by error
                    DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.cancelable = false;
                    dlg.setOnOkListener(new OkListener() {
                        @Override
                        public void okClick() {
                            layProgress.setVisibility(View.GONE);
                            layStartUpdate.setVisibility(View.VISIBLE);
                            btnUpdate.setVisibility(View.INVISIBLE);
                            loadNewVersion();
                        }
                    });
                    dlg.showOk(G.T.getSentence(818), G.T.getSentence(817));
                }

            }
        });
        md.download();
    }

    /********************
     * Jahanbin
     **********************/
    private void installApplication(String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    /********************
     * Jahanbin
     **********************/

    @Override
    public void translateForm() {
        super.translateForm();
        txtDescription.setText(G.T.getSentence(816));

        PackageInfo pInfo = null;
        try {
            pInfo = G.context.getPackageManager().getPackageInfo(G.context.getPackageName(), 0);
            String version = pInfo.versionName;
            txtLastVersion.setText(G.T.getSentence(814) + " : " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        txtLastVersion.setText(G.T.getSentence(814) + " : " + Utility.getApplicationVersion());
        btnCancelDownload.setText(G.T.getSentence(102));
        btnUpdate.setText(G.T.getSentence(818));
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString()
                        + "/2011.kml");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                G.log("Error: " + e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        @Override
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            // pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            // dismissDialog(progress_bar_type);

        }

    }


}
