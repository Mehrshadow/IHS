package ir.parsansoft.app.ihs.center;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_browser;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ActivityBrowser extends ActivityEnhanced {
    CO_f_browser fo;
    String       homeURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_browser);
        fo = new CO_f_browser(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            homeURL = extras.getString("URL");
            G.log("Browse for URL : " + homeURL);
        }
        setSideBarVisiblity(false);
        initialView();
        translateForm();
    }

    private void initialView() {
        fo.wvSMSPannel.getSettings().setJavaScriptEnabled(true);
        fo.wvSMSPannel.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        fo.wvSMSPannel.setWebViewClient(new SSLTolerentWebViewClient());
        fo.wvSMSPannel.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, final int progress) {
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        fo.prgProgress.setProgress(progress);
                        if (progress == 100)
                            fo.prgProgress.setProgress(0);
                    }
                });
            }
        });
        fo.wvSMSPannel.loadUrl(homeURL);

        fo.btnBrowserBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                fo.wvSMSPannel.goBack();
            }
        });
        fo.btnBrowserForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                fo.wvSMSPannel.goForward();
            }
        });
        fo.btnBrowserRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                fo.wvSMSPannel.loadUrl(fo.wvSMSPannel.getUrl());
            }
        });
        fo.btnBrowserHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                fo.wvSMSPannel.loadUrl(homeURL);
            }
        });
        fo.btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(ActivityBrowser.this, ActivitySettingSMS.class));
                Animation.doAnimation(Animation_Types.FADE);
                finish();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && fo.wvSMSPannel.canGoBack()) {
            //if Back key pressed and webview can navigate to previous page
            fo.wvSMSPannel.goBack();
            // go back to previous page
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            //            startActivity(new Intent(ActivityBrowser.this, ActivitySettingSMS.class));
            finish();
            // finish the activity
        }
        return super.onKeyDown(keyCode, event);
    }


    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

    }


    @Override
    public void translateForm() {
        super.translateForm();
        fo.btnClose.setText(G.T.getSentence(108));
    }


}
