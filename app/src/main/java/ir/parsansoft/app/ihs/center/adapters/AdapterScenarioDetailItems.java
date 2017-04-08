package ir.parsansoft.app.ihs.center.adapters;

import ir.parsansoft.app.ihs.center.AllViews.CO_l_list_scemario_detail_items;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.Database.PreOperand.Struct;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class AdapterScenarioDetailItems extends BaseAdapter {

    Context                         context;
    Struct[]    scenarioItems;
    CO_l_list_scemario_detail_items lo;

    public AdapterScenarioDetailItems(Context context, Struct[] scenarioItems) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.scenarioItems = scenarioItems;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        G.log("AdapterScenarioDetailItems - Get View ... position :" + position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            row = new View(context);
            row = inflater.inflate(R.layout.l_list_scemario_detail_items, null);
        }
        lo = new CO_l_list_scemario_detail_items(row);
        lo.txtName.setText(scenarioItems[position].getNodeFullName());
        lo.txtValue.setText(G.T.getSentence(501) + " " + scenarioItems[position].operation + " " + scenarioItems[position].getValueName());

        lo.imgIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Database.PreOperand.delete(scenarioItems[position].iD);
                scenarioItems = Database.PreOperand.select("ScenarioID=" + scenarioItems[position].scenarioID);
                notifyDataSetChanged();
            }
        });

        //------------------- this methods must be defined in PreOperands Calss's Struct----------------------------------------------------
        //        public String getValueName() {
        //            String query = "SELECT T_Translation.SentenseText FROM  T_Switch INNER JOIN T_SwitchType ON T_Switch.SwitchType=T_SwitchType.ID INNER JOIN T_ValueType ON T_SwitchType.ValueTypeID=T_ValueType.ID INNER JOIN T_ValueTypeInstances ON T_ValueType.ID=T_ValueTypeInstances.ValueTypeID inner  JOIN T_Translation on T_ValueTypeInstances.SentenceID=T_Translation.SentenseID WHERE " +
        //                    "T_Translation.LangID=" + G.setting.languageID +
        //                    " AND T_Switch.ID=" + switchID +
        //                    " AND T_ValueTypeInstances.Value=" + value;
        //            G.log(query);
        //            Cursor cursor = G.dbObject.rawQuery(query, null);
        //            String result = "" + value;
        //            if (cursor.moveToNext())
        //                result = cursor.getString(cursor.getColumnIndex("SentenseText"));
        //            try {
        //                cursor.close();
        //            }
        //            catch (Exception e) {}
        //            return result;
        //        }
        //
        //        public String getNodeFullName() {
        //            Cursor cursor = G.dbObject.rawQuery("SELECT T_Section.Name  AS SectionName,T_Room.Name AS RoomName, T_Node.Name AS NodeName , T_Switch.Name AS SwitchName FROM T_Switch INNER JOIN T_Node ON T_Switch.NodeID=T_Node.ID INNER JOIN T_ROOM ON T_Node.RoomID=T_Room.ID INNER JOIN T_Section ON T_Room.SectionID= T_Section.ID WHERE " +
        //                    " T_Switch.ID=" + switchID, null);
        //            if (cursor.moveToNext()) {
        //                String fullName;
        //                fullName = cursor.getString(cursor.getColumnIndex("SectionName"));
        //                fullName += " / " + cursor.getString(cursor.getColumnIndex("RoomName"));
        //                fullName += " / " + cursor.getString(cursor.getColumnIndex("NodeName"));
        //                fullName += " / " + cursor.getString(cursor.getColumnIndex("SwitchName"));
        //                try {
        //                    cursor.close();
        //                }
        //                catch (Exception e) {}
        //                return fullName;
        //            }
        //            try {
        //                cursor.close();
        //            }
        //            catch (Exception e) {}
        //            return G.T.getSentence(606604) + " : " + switchID;
        //        }



        return row;
    }
    @Override
    public int getCount() {
        int len;
        if (scenarioItems == null)
            len = 0;
        else
            len = scenarioItems.length;
        return len;
    }


    @Override
    public Object getItem(int id) {
        return scenarioItems[id];
    }


    @Override
    public long getItemId(int id) {
        return scenarioItems[id].iD;
    }

}
