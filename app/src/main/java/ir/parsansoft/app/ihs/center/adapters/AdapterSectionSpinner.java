package ir.parsansoft.app.ihs.center.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;

public class AdapterSectionSpinner extends BaseAdapter {

    Database.Section.Struct[] sections;
    public AdapterSectionSpinner(Database.Section.Struct[] sections) {
        this.sections = sections;
    }

    @Override
    public int getCount() {
//        try {
            return sections.length;
//        }catch (Exception e){
//            e.printStackTrace();
//            return 0;
//        }
    }

    @Override
    public Database.Section.Struct getItem(int position) {
        return sections[position];
    }

    @Override
    public long getItemId(int position) {
        return sections[position].iD;
    }

    public int getSectionPositionById(long sectionId) {
        for (int i = 0 ; i < sections.length ; i++){
            Database.Section.Struct s = sections[i];
            if (s.iD == sectionId)
                return i;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Database.Section.Struct section = sections[position];
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
        holder.txt.setText("" + section.name);
        return convertView;
    }
    private class ViewHolder {
        TextView txt;
        public ViewHolder(View view) {
            txt = (TextView) view.findViewById(R.id.txt);
        }
    }
}
