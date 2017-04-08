package ir.parsansoft.app.ihs.center.components;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import ir.parsansoft.app.ihs.center.AllViews.CO_c_comp_icon_selector_popup;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.adapters.AdapterImageFromAssets;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


// Reference :
// http://stackoverflow.com/questions/13211200/open-images-in-specific-directory-using-gallery-doesnt-work-on-jb
//
public class ComIconSelector extends LinearLayout {

    private String              SCAN_DIR;
    private static final String FILE_TYPE            = null;             // "image/*";
    private Dialog              alertD;

    private String              scanPath             = G.DIR_ICONS_ROOMS;
    private ImageView           imgIconSelectorIndicater;
    private String              selectedIconPath     = "";
    private int                 selectedIconPosition = 0;
    Context                     context;
    private String[]            iconNamesList;
    private String              selectedIconName     = "";


    public ComIconSelector(Context context) {
        super(context);
        this.context = context;
        initialize(context);
    }

    public ComIconSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize(context);
    }

    public ComIconSelector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initialize(context);

    }

    public int getSelectedIconPosition() {
        return selectedIconPosition;
    }
    public String getSelectedIconName() {
        return selectedIconName;
    }

    public void setImageDir(String imageDIR) {
        scanPath = imageDIR;
    }


    private void initialize(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.c_comp_icon_selector, this, true);
        imgIconSelectorIndicater = (ImageView) findViewById(R.id.imgIconSelectorIndicater);
        imgIconSelectorIndicater.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertD = new Dialog(G.currentActivity, android.R.style.Theme_Dialog);
                alertD.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertD.setCancelable(true);
                alertD.setContentView(R.layout.c_comp_icon_selector_popup);
                alertD.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                CO_c_comp_icon_selector_popup popupView = new CO_c_comp_icon_selector_popup(alertD);
                final AdapterImageFromAssets ia = new AdapterImageFromAssets(getBitmapFromAssets(G.context, scanPath), selectedIconPosition);
                Button btnCancel = (Button) alertD.findViewById(R.id.btnCancel);
                btnCancel.setText(G.T.getSentence(102));
                popupView.grdIcons.setAdapter(ia);
                popupView.grdIcons.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Bitmap bitmap = ia.getItem(position);
                        selectedIconPosition = position;
                        imgIconSelectorIndicater.setImageBitmap(bitmap);
                        try {
                            selectedIconName = G.context.getAssets().list(scanPath)[selectedIconPosition];
                        } catch (IOException e) {
                            G.printStackTrace(e);
                            selectedIconName = "";
                        }
                        alertD.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertD.dismiss();
                    }
                });
                alertD.show();
            }
        });
    }

    public void setImageToSelector(String imageName) {
        imgIconSelectorIndicater.setImageBitmap(AssetsManager.getBitmap(G.context, scanPath + "/" + imageName));
        selectedIconPosition = Arrays.asList(getStringFromAssets(G.context, scanPath)).indexOf(imageName);
        selectedIconName = imageName;
    }

    ///برگرداندن نام همه ی زیرمجموعه های یک فولدر
    @Nullable
    private String[] getStringFromAssets(Context context, String path) {
        File[] imageList;
        String[] list;
        int i = 0;
        AssetManager assetManager = context.getAssets();
        try {
            list = context.getAssets().list(path);
            return list;
        }
        catch (IOException e) {}
        return null;
    }

    private File createFileFromInputStream(InputStream inputStream, String fileName) {
        try {
            File f = new File(fileName);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[8];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return f;
        }
        catch (IOException e) {
            //Logging exception
        }
        return null;
    }


    ///برگرداندن  همه ی عکس های زیرمجموعه  یک فولدر در استس 
    private ArrayList<Bitmap> getBitmapFromAssets(Context context, String path) {
        ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
        int i = 0;

        try {
            iconNamesList = context.getAssets().list(path);
            if (iconNamesList.length > 0) {
                for (String file: iconNamesList) {
                    imageList.add(getBitmap(context, path + "/" + iconNamesList[i]));
                    i = i + 1;
                }
            } else {
                return null;
            }
        }
        catch (IOException e) {}
        return imageList;
    }


    ///برگرداندن یک عکس به وسیله نام آن
    private Bitmap getBitmap(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        }
        catch (IOException e) {}
        return bitmap;
    }

}
