package ir.parsansoft.app.ihs.center.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;

/**
 * Created by HaMiD on 2/11/2017.
 */

public class AdapterIoTypesSpinner extends BaseAdapter {
    List<String> types ;

    public AdapterIoTypesSpinner(List<String> types) {
        this.types = types;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public String getItem(int position) {
        return types.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        String section = types[position];
        AdapterIoTypesSpinner.ViewHolder holder;
        if(convertView == null){
            if (G.setting.languageID == 1 || G.setting.languageID == 4) {
                convertView = LayoutInflater.from(G.context).inflate(R.layout.l_spinner_item, parent, false);
            }
            else {
                convertView = LayoutInflater.from(G.context).inflate(R.layout.l_spinner_item_rtl, parent, false);
            }
            holder = new AdapterIoTypesSpinner.ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (AdapterIoTypesSpinner.ViewHolder) convertView.getTag();
        }
        holder.txt.setText(types.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView txt;
        public ViewHolder(View view) {
            txt = (TextView) view.findViewById(R.id.txt);
        }
    }
}
