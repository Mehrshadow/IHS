package ir.parsansoft.app.ihs.center;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jahanbin on 1/17/2017.
 */

public class Firebase {

    boolean isSent = false;

    public boolean sendNotification(String title, String body, String token) {

        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            jsonObject.put("to", token);
            jsonObject.put("priority", "high");

            jsonObject.put("notification", data);
            data.put("title", title);
            data.put("body", body);

        } catch (JSONException e) {
            e.printStackTrace();

            isSent = false;
        }

        return callFirebaseAPI(jsonObject);
    }

    private boolean callFirebaseAPI(JSONObject jsonObject) {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        final MediaType type = MediaType.parse("application/json");
        String json = jsonObject.toString();

        // Authorization >> https://console.firebase.google.com/project/ihsmobile-4da34/settings/cloudmessaging
        final Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Authorization", "key=" + G.FirebaseAPIKEY)
                .post(RequestBody.create(type, json))
                .url(G.FirebaseAPIURL)
                .build();

        Log.d("TAGG", json);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // if date & time is not correct
                G.printStackTrace(e);
                if (e.getMessage().contains("ExtCertPathValidatorException")){
                    final DialogClass dlg = new DialogClass(G.currentActivity);
                    dlg.cancelable = false;
                    dlg.setOnOkListener(new DialogClass.OkListener() {
                        @Override
                        public void okClick() {
                            dlg.dissmiss();
                        }
                    });
                    dlg.showOk(G.T.getSentence(216), G.T.getSentence(844));

                    return;
                }
//                if (e.equals(javax.net.ssl.SSLHandshakeException))
                G.log("FirebaseNotification Failure");
                G.log(e.getMessage());
                isSent = false;

//                deleteMobileWithExpiredToken(token);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {

                if (response.message().equals("OK"))// it was sent successfully
                {
                    G.log("FirebaseNotification message: " + response.message());
                    isSent = true;
                }

            }
        });
        return isSent;
    }

    private void deleteMobileWithExpiredToken(String token) {
        Database.Mobiles.Struct newMobile;
        try {
            newMobile = Database.Mobiles.select("FirebaseToken like '" + token + "'")[0];
            Database.Mobiles.select(newMobile.iD);
        } catch (Exception ex) {
            G.printStackTrace(ex);
        }
    }

}
