package ir.parsansoft.app.ihs.center.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import ir.parsansoft.app.ihs.center.AllViews.CO_l_setting_wifi_signal;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.WifiClass;

import java.util.List;

public class AdapterWIFIList extends ArrayAdapter<WifiClass> {

    Context    context;
    WiFiSelect ws;

    public AdapterWIFIList(Context context, int resource, List<WifiClass> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    public interface WiFiSelect {
        public void onWiFiSelected(WifiClass sekectedWiFi);
    }

    public void setOnWiFiSelector(WiFiSelect wifiSelector) {
        ws = wifiSelector;
    }

    private class ViewHolder {
        CO_l_setting_wifi_signal lo;
        ImageView                imgSignal;
        LinearLayout             layBack;

        public ViewHolder(View v) {
            lo = new CO_l_setting_wifi_signal(v);
            lo.btnConnect.setText(G.T.getSentence(705));
            imgSignal = (ImageView) v.findViewById(R.id.imgIcon);
            layBack = (LinearLayout) v.findViewById(R.id.layBack);
        }

        public void fill(ArrayAdapter<WifiClass> adapter, final WifiClass wifi, int position) {
            lo.txtSSID.setText(wifi.ssid);
            if (G.networkSetting.wiFiSSID.equals(wifi.ssid))
                layBack.setBackgroundColor(Color.parseColor("#FFFFB400"));
            else
                layBack.setBackgroundColor(Color.parseColor("#00000000"));
            lo.txtDes.setText(wifi.type + "" + wifi.level);

            if (wifi.level >= 90 && wifi.level <= 100) {
                imgSignal.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level4);
            } else if (wifi.level >= 78 && wifi.level < 90) {
                imgSignal.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level3);
            } else if (wifi.level >= 65 && wifi.level < 78) {
                imgSignal.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level2);
            } else if (wifi.level >= 0 && wifi.level < 65) {
                imgSignal.setImageResource(R.drawable.lay_icon_main_wifi_indicator_level1);
            }

            lo.btnConnect.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ws != null)
                        ws.onWiFiSelected(wifi);
                }
            });
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WifiClass obj = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.l_setting_wifi_signal, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.fill(this, obj, position);
        return convertView;
    }

}
