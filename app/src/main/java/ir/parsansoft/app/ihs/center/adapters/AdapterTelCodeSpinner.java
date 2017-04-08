package ir.parsansoft.app.ihs.center.adapters;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.components.AssetsManager;
import java.util.Locale;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterTelCodeSpinner extends BaseAdapter {
    String[] country;
    Context  context;

    public AdapterTelCodeSpinner(Context context, String[] objects) {
        this.context = context;
        country = objects;
    }
    @Override
    public int getCount() {
        return country.length;
    }

    @Override
    public Object getItem(int position) {
        return country[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = G.inflater.inflate(R.layout.l_country_code_up, parent, false);
        ImageView imgFlag = (ImageView) view.findViewById(R.id.imgContury);
        TextView txtName = (TextView) view.findViewById(R.id.txtContury);
        TextView txtCode = (TextView) view.findViewById(R.id.txtConturyCode);
        String[] g = country[position].split(",");
        imgFlag.setImageBitmap(AssetsManager.getBitmap(G.context, G.DIR_ICONS_FLAG + "/" + (g[1]).toLowerCase() + ".png"));
        txtName.setText(GetCountryZipCode(g[1]).trim());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = G.inflater.inflate(R.layout.l_country_code_down, parent, false);
        ImageView imgFlag = (ImageView) view.findViewById(R.id.imgContury);
        TextView txtName = (TextView) view.findViewById(R.id.txtContury);
        TextView txtCode = (TextView) view.findViewById(R.id.txtConturyCode);
        String[] g = country[position].split(",");
        imgFlag.setImageBitmap(AssetsManager.getBitmap(G.context, G.DIR_ICONS_FLAG + "/" + (g[1]).toLowerCase() + ".png"));
        txtName.setText(GetCountryZipCode(g[1]).trim());
        txtCode.setText("(+" + g[0] + ")");
        return view;
    }
    private String GetCountryZipCode(String ssid) {
        Locale loc = new Locale("", ssid);
        return loc.getDisplayCountry().trim();
    }
}
