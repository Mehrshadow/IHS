package ir.parsansoft.app.ihs.center.components;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import ir.parsansoft.app.ihs.center.G;

import java.io.*;
import java.util.ArrayList;

public class AssetsManager {


    public static ArrayList<Bitmap> getBitmapFromAssets(Context context, String path) {
        ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
        String[] list;
        int i = 0;
        try {
            list = context.getAssets().list(path);
            if (list.length > 0) {
                for (String file: list) {
                    imageList.add(getBitmap(context, path + "/" + list[i]));
                    i = i + 1;
                }
            } else {
                return null;
            }
        }
        catch (IOException e) {}
        return imageList;
    }
    public static Bitmap getBitmap(Context context, String filePath) {
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
    public static void copyDatabase() {
        File file = new File(G.DATABASE_FILE_PATH);
        boolean replace = !file.exists();
        if (replace) {
            AssetManager assetManager = G.context.getAssets();
            InputStream in = null;
            OutputStream out = null;
            File dir = new File(G.DIR_DATABASE);
            if ( !dir.exists()) {
                dir.mkdirs();
            }
            try {
                in = assetManager.open("db/centerdb.sqlite");
                out = new FileOutputStream(G.DATABASE_FILE_PATH);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            }
            catch (Exception e) {
                Log.e("tag", e.getMessage());
            }
            G.log("COPY");
        } else {
            G.log("EXITS");
        }
    }
}
