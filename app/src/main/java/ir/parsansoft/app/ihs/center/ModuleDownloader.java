package ir.parsansoft.app.ihs.center;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class ModuleDownloader {

    private long                       downloadedSize;
    private long                       totalSize;
    private int                        percent;
    private boolean                    isCancelled          = false;
    private static final int           DOWNLOAD_BUFFER_SIZE = 8 * 1024;

    private String                     downloadPath;
    private String                     filepath;
    private OnProgressDownloadListener progressListener;
    private OnDownloadCompleteListener completeListener;
    private OnDownloadCanceledListener cancelListener;
    private boolean                    simulate             = false;


    public interface OnDownloadCompleteListener {
        public void OnDownloadComplete(String filePath);
    }

    public interface OnProgressDownloadListener {
        public void onProgressDownload(int percent, long downloadedSize, long fileSize);
    }

    public interface OnDownloadCanceledListener {
        public void onDownloadCanceledListener(int errorCode);
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }


    public long getTotalSize() {
        return totalSize;
    }


    public int getPercent() {
        return percent;
    }


    private long calculateFileSize() throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(downloadPath);
        HttpResponse execute = client.execute(httpGet);
        return execute.getEntity().getContentLength();
    }


    public ModuleDownloader download() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    G.log(downloadPath + "\n-----\n" + filepath);
                    URL url = new URL(downloadPath);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.connect();

                    totalSize = calculateFileSize();

                    //  Create directory
                    String dirPath;
                    dirPath = filepath.substring(0, filepath.lastIndexOf("/"));
                    File dir = new File(dirPath);
                    dir.mkdirs();

                    File file = new File(filepath);
                    if (file.exists()) {
                        G.log("Deleting File .................");
                        file.delete();
                    }

                    //FileOutputStream outputStream = new FileOutputStream(filepath);

                    File f = new File(filepath);
                    f.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(f);

                    //  InputStream inputStream = connection.getInputStream();
                    InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                    byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
                    int len = 0;
                    downloadedSize = 0;
                    while ( !isCancelled && ((len = inputStream.read(buffer)) > 0)) {
                        outputStream.write(buffer, 0, len);
                        downloadedSize += len;
                        G.log("downloaded : " + downloadedSize + " -  total : " + totalSize + " len = " + len);
                        percent = (int) (100.0f * (float) downloadedSize / totalSize);
                        if (progressListener != null) {
                            final long finalDownloadedSize = downloadedSize;
                            G.HANDLER.post(new Runnable() {

                                @Override
                                public void run() {
                                    progressListener.onProgressDownload(percent, finalDownloadedSize, totalSize);
                                }
                            });
                        }

                        if (simulate) {
                            try {
                                Thread.sleep(100);
                            }
                            catch (InterruptedException e) {
                                G.printStackTrace(e);
                            }
                        }
                    }

                    outputStream.close();
                    if (isCancelled)
                    {
                        if (file.exists()) {
                            file.delete();
                        }
                        if (cancelListener != null)
                            G.HANDLER.post(new Runnable() {

                                @Override
                                public void run() {
                                    cancelListener.onDownloadCanceledListener(1); // 1= user request
                                }
                            });
                    }
                    else
                        G.HANDLER.post(new Runnable() {

                            @Override
                            public void run() {
                                completeListener.OnDownloadComplete(filepath);
                            }
                        });
                }
                catch (MalformedURLException e) {
                    G.printStackTrace(e);
                    if (cancelListener != null)
                        G.HANDLER.post(new Runnable() {

                            @Override
                            public void run() {
                                cancelListener.onDownloadCanceledListener(0); // 0= unknown error
                            }
                        });
                }
                catch (IOException e) {
                    G.printStackTrace(e);
                    if (cancelListener != null)
                        G.HANDLER.post(new Runnable() {

                            @Override
                            public void run() {
                                cancelListener.onDownloadCanceledListener(2); // 2= download error 
                            }
                        });
                }
            }
        });
        thread.start();

        return this;
    }


    public ModuleDownloader downloadPath(String value) {
        downloadPath = value;
        return this;
    }


    public ModuleDownloader filepath(String value) {
        filepath = value;
        return this;
    }


    public ModuleDownloader progressListener(OnProgressDownloadListener value) {
        progressListener = value;
        return this;
    }


    public ModuleDownloader completeListener(OnDownloadCompleteListener value) {
        completeListener = value;
        return this;
    }
    public ModuleDownloader cancelListener(OnDownloadCanceledListener value) {
        cancelListener = value;
        return this;
    }

    public ModuleDownloader simulate(boolean value) {
        simulate = value;
        return this;
    }


    public void cancel() {
        isCancelled = true;
    }
}
