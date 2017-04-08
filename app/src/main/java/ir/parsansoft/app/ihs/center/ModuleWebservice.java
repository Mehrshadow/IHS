package ir.parsansoft.app.ihs.center;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class ModuleWebservice {

    public static final int PROTOCOL_EXCEPTION = -1;
    public static final int IO_EXCEPTION       = -2;
    public static final int UNKNOWN_EXCEPTION  = -3;

    public static interface WebServiceListener {
        public void onDataReceive(String data, boolean cached);
        public void onFail(int statusCode);
    }

    private String                   cacheFileName;
    private String                   url                = G.URL_Webservice;
    private ArrayList<NameValuePair> inputArguments;
    private WebServiceListener       listener;
    private boolean                  enableCache        = false;
    private String                   cacheDir           = G.DIR_CACHE;
    private long                     cacheExpireTime    = 1800;
    private int                      connectionTimeout  = 3000;
    private int                      socketTimeout      = 10000;
    private boolean                  deleteOldCache     = true;
    private boolean                  useOnlineDataFirst = false;


    /**
     * @value webservice full path.
     */
    public ModuleWebservice url(String value) {
        url = value;
        return this;
    }


    /**
     * @value Defines a new listener and runs when data recived
     */
    public ModuleWebservice setListener(WebServiceListener value) {
        listener = value;
        return this;
    }


    /**
     * @value path without last directory seprator <br/>
     *        ex: <b>/sdcard/your_project/cacheDir
     */
    public ModuleWebservice cacheDir(String value) {
        cacheDir = value;
        return this;
    }


    public ModuleWebservice enableCache(boolean value) {
        enableCache = value;
        return this;
    }


    public ModuleWebservice deleteOldCache(boolean value) {
        deleteOldCache = value;
        return this;
    }


    /**
     * if enable this , allways checks online data then if chache was enabled uses the cache.
     * but if disable : while the cache expire time not arrived , uses the cache first.
     * Default : false
     */
    public ModuleWebservice useOnlineDataFirst(boolean value) {
        useOnlineDataFirst = value;
        return this;
    }


    /**
     * @value Seconds for the cache to be valid <br/>
     *        ex: <b>3600</b> for one hure
     *        Default : 1800 seconds
     */
    public ModuleWebservice cacheExpireTime(long value) {
        cacheExpireTime = value;
        return this;
    }

    /**
     * @value Seconds for the cache to be valid <br/>
     *        ex: <b>3600</b> for one hure
     *        Default : 1800 seconds
     */
    public ModuleWebservice addParameter(String parameter, String value) {
        if (inputArguments == null) {
            inputArguments = new ArrayList<NameValuePair>();
        }
        inputArguments.add(new BasicNameValuePair(parameter, value));
        G.log("Parameter Added:     " + parameter + ":" + value);
        return this;
    }


    /**
     * @value Time to try start reading webservice <br/>
     *        <b> int value</b> in miliseconds
     *        Default : 3000 Miliseconds
     */
    public ModuleWebservice connectionTimeout(int value) {
        connectionTimeout = value;
        return this;
    }


    /**
     * @value Time to allow continue reading webservice <br/>
     *        <b> int value</b> in miliseconds
     *        Default : 10000 Miliseconds
     */
    public ModuleWebservice socketTimeout(int value) {
        socketTimeout = value;
        return this;
    }


    /**
     * Starts reading webservice <br/>
     * <b>Set other parameters before calling this nethod.</b>
     * if there was a valid data in cache returns the cache else reads from net.
     * the result will be available in <b>listener.onDataReceive</b> or if fails will be available in <b>listener.onFail</b>
     */
    public void read() {
        String data = null;
        if (useOnlineDataFirst) {
            G.log("Use Online First");
            readFromNet();
        }
        else {
            if (enableCache) {
                G.log("Use Cache First");
                cacheFileName = createCacheFileName();
                File file = new File(cacheFileName);
                if (file.exists())
                    data = readFromCache();
                else
                    G.log("File not Exist");
            }
            if (data == null) {
                readFromNet();
            } else {
                if (listener != null) {
                    listener.onDataReceive(data, true);
                    return;
                }
            }

        }
    }


    /**
     * Starts reading webservice <br/>
     * <b>Forces reading from net</b>
     */
    public void readFromNet() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                String data = null;
                try {
                    HttpParams params = new BasicHttpParams();

                    HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
                    HttpConnectionParams.setSoTimeout(params, socketTimeout);
                    HttpClient client = new DefaultHttpClient(params);
                    //                    String pars= "?";
                    //                    for (int i = 0; i < inputArguments.size(); i++)
                    //                    {
                    //                        pars += inputArguments.get(i).getName() + "=" + inputArguments.get(i).getValue() + "&";
                    //                    }
                    //                    if (pars.length() > 1)
                    //                        pars = pars.substring(0, pars.length() - 1);
                    //                    else
                    //                        pars = "";
                    //                    url = url + pars;
                    //                    url = url.trim().replace(" ", "%20");
                    //                    G.log("\n\n*****************************\n\nUrl :  " + url + "\n*****************************");
                    //                    url = Utilities.NormalizeURL.normalize(url);
                    //                    String normalURL = url;
                    //                    //URI uri = URI.create(normalURL);
                    //                    //normalURL = normalURL.getScheme()+"://" + normalURL.getPath() + normalURL.getQuery() + normalURL.getFragment();
                    G.log("Url :  " + url);

                    HttpPost request = new HttpPost(url);
                    request.setEntity(new UrlEncodedFormEntity(inputArguments, "UTF-8"));
                    HttpResponse httpResponse = (HttpResponse) client.execute(request);
                    data = streamToString(httpResponse.getEntity().getContent());
                    if (data == null)
                        data = "";
                    G.log("data: " + data);
                    if (enableCache) {
                        cacheFileName = createCacheFileName();
                        saveToCache(System.currentTimeMillis(), data);
                    }
                    if (listener != null) {
                        listener.onDataReceive(data, false);
                        return;
                    }
                }
                catch (ClientProtocolException e) {
                    G.log("Client Protocole : " + e.getMessage());
                    G.printStackTrace(e);
                    if (enableCache) {
                        cacheFileName = createCacheFileName();
                        data = readFromCache();
                        if ((listener != null) && (data != null)) {
                            listener.onDataReceive(data, true);
                            return;
                        }
                    }
                    if (listener != null) {
                        listener.onFail(PROTOCOL_EXCEPTION);
                    }
                }
                catch (IOException e) {
                    G.printStackTrace(e);
                    G.log("IO : " + e.getMessage());
                    if (enableCache) {
                        cacheFileName = createCacheFileName();
                        data = readFromCache();
                        if ((listener != null) && (data != null)) {
                            listener.onDataReceive(data, true);
                            return;
                        }
                    }
                    if (listener != null) {
                        listener.onFail(IO_EXCEPTION);
                    }
                }
                catch (Exception e) {
                    G.log("Other :sor" + e.getMessage());
                    G.printStackTrace(e);
                    if (enableCache) {

                        cacheFileName = createCacheFileName();
                        data = readFromCache();
                        if ((listener != null) && (data != null)) {
                            listener.onDataReceive(data, true);
                            return;
                        }
                    }
                    if (listener != null) {
                        listener.onFail(UNKNOWN_EXCEPTION);
                    }
                }
            }
        });
        thread.start();
    }


    private String createCacheFileName() {
        String output = url;
        for (NameValuePair input: inputArguments) {
            output += ";" + input.getName() + ":" + input.getValue();
        }

        String sha1 = sha1(output);
        return sha1 + ".dat";
    }

    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();


    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    private String sha1(String toHash) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            hash = bytesToHex(bytes);
        }
        catch (NoSuchAlgorithmException e) {
            G.printStackTrace(e);
        }
        catch (UnsupportedEncodingException e) {
            G.printStackTrace(e);
        }

        return hash;
    }


    private String streamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuffer = new StringBuilder();

        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append((line + "\n"));
            }
        }
        catch (IOException e) {
            G.printStackTrace(e);
        } finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                G.printStackTrace(e);
            }
        }
        return stringBuffer.toString();
    }


    private void saveToCache(long when, String data) {

        ObjectOutputStream outputStream = null;
        try {
            File wallpaperDirectory = new File(cacheDir);
            wallpaperDirectory.mkdirs();

            outputStream = new ObjectOutputStream(new FileOutputStream(cacheDir + "/" + cacheFileName));
            outputStream.writeLong(when);
            outputStream.writeInt(data.length() * 2);
            outputStream.write(data.getBytes());
            byte[] b = new byte[2];
            b[0] = (byte) 5;
            b[1] = (byte) 8;
            outputStream.write(b);
            G.log("Save " + data.length() * 2 + " bytes to cache :" + data);
        }
        catch (StreamCorruptedException e) {
            G.printStackTrace(e);
        }
        catch (FileNotFoundException e) {
            G.printStackTrace(e);
        }
        catch (IOException e) {
            G.printStackTrace(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                }
                catch (IOException e) {
                    G.printStackTrace(e);
                }
                try {
                    outputStream.close();
                }
                catch (IOException e) {
                    G.printStackTrace(e);
                }
            }
        }
    }


    private String readFromCache() {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(cacheDir + "/" + cacheFileName));
            long when = inputStream.readLong();
            long now = System.currentTimeMillis();
            if (now - when > cacheExpireTime * 1000) {
                if (deleteOldCache) {
                    new File(cacheDir + "/" + cacheFileName).delete();
                    return null;
                }
            }
            int bytesLength = inputStream.readInt();
            byte[] buffer = new byte[bytesLength];
            inputStream.read(buffer);
            byte[] b = new byte[2];
            b[0] = (byte) 5;
            b[1] = (byte) 8;
            String endOfFile = new String(b);
            String output = new String(buffer);
            G.log("" + buffer[buffer.length - 1]);
            int indx = output.indexOf(endOfFile);
            if (indx > 0)
                output = output.substring(0, indx);
            G.log("read " + bytesLength + " bytes from cache :" + output);
            G.log("Output Length is :" + output.length());
            return output;
        }
        catch (StreamCorruptedException e) {
            G.printStackTrace(e);
        }
        catch (FileNotFoundException e) {
            G.printStackTrace(e);
        }
        catch (IOException e) {
            G.printStackTrace(e);
        }
        catch (Exception e) {
            G.printStackTrace(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                G.printStackTrace(e);
            }
        }
        return null;
    }
}
