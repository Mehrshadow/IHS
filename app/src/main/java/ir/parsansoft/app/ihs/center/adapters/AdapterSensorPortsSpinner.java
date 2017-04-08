package ir.parsansoft.app.ihs.center.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;

import static android.R.attr.port;

public class AdapterSensorPortsSpinner extends BaseAdapter {

    String[] ports;
    public AdapterSensorPortsSpinner(String[] ports) {
        this.ports = ports;
    }

    @Override
    public int getCount() {
        return ports.length;
    }

    @Override
    public String getItem(int position) {
        return ports[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String port = ports[position];
        ViewHolder holder;
        if(convertView == null){
            if (G.setting.languageID == 1 || G.setting.languageID == 4) {
                convertView = LayoutInflater.from(G.context).inflate(R.layout.l_spinner_item, parent, false);
            }
            else {
                convertView = LayoutInflater.from(G.context).inflate(R.layout.l_spinner_item_rtl, parent, false);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt.setText(port);
        return convertView;
    }
    private class ViewHolder {
        TextView txt;
        public ViewHolder(View view) {
            txt = (TextView) view.findViewById(R.id.txt);
        }
    }
}
