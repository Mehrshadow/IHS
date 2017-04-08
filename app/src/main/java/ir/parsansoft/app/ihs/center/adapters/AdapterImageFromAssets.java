package ir.parsansoft.app.ihs.center.adapters;

import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import java.util.List;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class AdapterImageFromAssets extends ArrayAdapter<Bitmap> {

    int positionSelected;

    public AdapterImageFromAssets(List<Bitmap> img, int position) {
        super(G.context, R.layout.l_setting_language_item, img);
        positionSelected = position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Bitmap img = getItem(position);
        ImageView imageView = new ImageView(G.context);
        imageView.setImageBitmap(img);
        imageView.setBackgroundColor((position == positionSelected) ? G.context.getResources().getColor(R.color.orange) : G.context.getResources().getColor(R.color.transparent));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(40, 40));
        return imageView;
    }
}
