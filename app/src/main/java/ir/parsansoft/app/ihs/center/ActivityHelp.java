package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import ir.parsansoft.app.ihs.center.components.AssetsManager;

import java.util.ArrayList;


public class ActivityHelp extends Activity {

    ImageView imgForward, imgBack, imgHelp,imgClose;
    int       currentImage;

    private enum PAGE_NAME {
        ActivityMain,
        ActivityFavorites
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        imgForward = (ImageView) findViewById(R.id.imgForward);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgHelp.setPadding(0, -getStatusBarHeight(), 0, 0);
        String pageName = getIntent().getExtras().getString("PAGE_NAME");
        String langPath = getIntent().getExtras().getString("LANG");
        PAGE_NAME page = PAGE_NAME.valueOf(pageName);
        String fullPath = G.DIR_HELP + "/" + langPath + pageName;
        final ArrayList<Bitmap> arrayBitmap = AssetsManager.getBitmapFromAssets(this, fullPath);
        imgHelp.setImageBitmap(arrayBitmap.get(0));
        if (arrayBitmap.size() == 1) {
            imgBack.setVisibility(View.INVISIBLE);
            imgForward.setVisibility(View.INVISIBLE);
        }
        else
        {
            imgBack.setVisibility(View.INVISIBLE);
        }
        imgForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImage < arrayBitmap.size() - 1) {
                    currentImage++;
                    imgHelp.setImageBitmap(arrayBitmap.get(currentImage));
                    if (currentImage == arrayBitmap.size() - 1)
                    {
                        imgForward.setVisibility(View.INVISIBLE);
                        imgBack.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        imgBack.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImage > 0) {
                    currentImage--;
                    imgHelp.setImageBitmap(arrayBitmap.get(currentImage));
                    if (currentImage == 0)
                    {
                        imgBack.setVisibility(View.INVISIBLE);
                        imgForward.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        imgForward.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        imgClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
