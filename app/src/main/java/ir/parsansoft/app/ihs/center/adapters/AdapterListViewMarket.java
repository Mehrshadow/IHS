package ir.parsansoft.app.ihs.center.adapters;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.Market;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.Utility;

import java.io.InputStream;
import java.util.ArrayList;

public class AdapterListViewMarket extends BaseAdapter {

    ArrayList<Market> lstMarket;

    public AdapterListViewMarket(ArrayList<Market> market) {
        lstMarket = market;
    }
    @Override
    public int getCount() {
        return lstMarket.size();
    }

    @Override
    public Object getItem(int position) {
        return lstMarket.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = G.inflater.inflate(R.layout.list_market_item, parent, false);
        TextView txtMarket = (TextView) view.findViewById(R.id.txtMarket);
        ImageView imgMarket = (ImageView) view.findViewById(R.id.imgMarket);
        new DownloadImageTask(imgMarket).execute(lstMarket.get(position).imgUrl);
        txtMarket.setText(lstMarket.get(position).name);
        LinearLayout layMarket = (LinearLayout) view.findViewById(R.id.layMarket);
        imgMarket.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(G.currentActivity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_download);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.setCancelable(true);
                TextView txtName = (TextView) dialog.findViewById(R.id.txtName);
                TextView txtDescription = (TextView) dialog.findViewById(R.id.txtDescription);
                ImageView imgQR = (ImageView) dialog.findViewById(R.id.imgQR);
                txtName.setText(lstMarket.get(position).name);
                txtDescription.setText(lstMarket.get(position).url);
                imgQR.setImageBitmap(Utility.makeNewQR(lstMarket.get(position).url));
                dialog.show();
            }
        });
        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {
                G.log("Error Download Image : " + e.getMessage());
                G.printStackTrace(e);
            }
            return mIcon11;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
