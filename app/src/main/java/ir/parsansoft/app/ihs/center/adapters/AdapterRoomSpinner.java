package ir.parsansoft.app.ihs.center.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;

public class AdapterRoomSpinner extends BaseAdapter {

    Database.Room.Struct[] rooms;
    public AdapterRoomSpinner(Database.Room.Struct[] rooms) {
        this.rooms = rooms;
    }

    @Override
    public int getCount() {
        return rooms.length;
    }

    @Override
    public Database.Room.Struct getItem(int position) {
        return rooms[position];
    }

    @Override
    public long getItemId(int position) {
        return rooms[position].iD;
    }

    public int getRoomPositionById(long sectionId) {
        for (int i = 0 ; i < rooms.length ; i++){
            Database.Room.Struct s = rooms[i];
            if (s.iD == sectionId)
                return i;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Database.Room.Struct room = rooms[position];
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
        holder.txt.setText("" + room.name);
        return convertView;
    }
    private class ViewHolder {
        TextView txt;
        public ViewHolder(View view) {
            txt = (TextView) view.findViewById(R.id.txt);
        }
    }
}
