package ir.parsansoft.app.ihs.center.adapters;

import ir.parsansoft.app.ihs.center.AllViews.CO_l_list_scemario_detail_items;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.Database.Results.Struct;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class AdapterScenarioResults extends BaseAdapter {

    Context                         context;
    Struct[]       scenarioItems;
    CO_l_list_scemario_detail_items lo;

    public AdapterScenarioResults(Context context, Struct[] scenarioItems) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.scenarioItems = scenarioItems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        G.log("AdapterScenarioResults : Get View ... position :" + position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            row = new View(context);
            row = inflater.inflate(R.layout.l_list_scemario_detail_items, null);
        }
        lo = new CO_l_list_scemario_detail_items(row);
        lo.txtName.setText(scenarioItems[position].getNodeFullName());
        lo.txtValue.setText(scenarioItems[position].getValueName());
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
