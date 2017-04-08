package ir.parsansoft.app.ihs.center.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ir.parsansoft.app.ihs.center.AllViews.CO_l_simple_list_item;
import ir.parsansoft.app.ihs.center.DialogClass;
import ir.parsansoft.app.ihs.center.DialogClass.YesNoListener;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;

import java.util.List;


public class AdapterScenarioNotifyMobiles extends BaseAdapter {

    Context               context;
    CO_l_simple_list_item lo;
    onDeleteItem          odi;
    List<String>          items;

    public AdapterScenarioNotifyMobiles(Context context, List<String> listMobiles) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.items = listMobiles;
    }

    public interface onDeleteItem {
        public void onDeleteItemListener(int index);
    }

    public void setOnDeleteItemListener(onDeleteItem odi) {
        this.odi = odi;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            row = new View(context);
            row = inflater.inflate(R.layout.l_simple_list_item, null);
        }
        lo = new CO_l_simple_list_item(row);
        lo.txtName.setText(items.get(position));
        lo.imgDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                G.log("Item : " + position + " Delete button clicked.");
                /*****/
                final DialogClass dlg = new DialogClass(G.currentActivity);
                dlg.showYesNo(G.T.getSentence(777), G.T.getSentence(803));
                dlg.setOnYesNoListener(new YesNoListener() {

                    @Override
                    public void yesClick() {
                        odi.onDeleteItemListener(position);
                        //						 dlg.dismiss();
                    }

                    @Override
                    public void noClick() {
                        //						dlg.dismiss();
                    }
                });
                /****/
            }
        });
        return row;
    }
    @Override
    public int getCount() {
        int len;
        if (items == null)
            len = 0;
        else
            len = items.size();
        return len;
    }


    @Override
    public Object getItem(int id) {
        return items.get(id);
    }


    @Override
    public long getItemId(int id) {
        return id;
    }
}
