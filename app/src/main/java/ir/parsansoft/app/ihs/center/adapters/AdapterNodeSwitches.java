package ir.parsansoft.app.ihs.center.adapters;

import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;


public class AdapterNodeSwitches extends BaseAdapter {

    Context                  context;
    Database.Switch.Struct[] switchItems;

    public AdapterNodeSwitches(Context context, Database.Switch.Struct[] switchItems) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.switchItems = switchItems;
    }
    public Database.Switch.Struct[] getSwitchItems() {
        return switchItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            G.log("AdapterNodeSwitches : Get View ... position :" + position);
            row = new View(context);
            row = inflater.inflate(R.layout.l_list_node_switches, null);
            TextView txtName = (TextView) row.findViewById(R.id.txtName);
            EditText edtName = (EditText) row.findViewById(R.id.edtName);
            Database.SwitchType.Struct type = Database.SwitchType.select(switchItems[position].switchType);
            txtName.setText(G.T.getSentence(type.nameSentenceID));
            edtName.setText(switchItems[position].name);
            edtName.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    switchItems[position].name = s.toString();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        return row;
    }
    @Override
    public int getCount() {
        int len;
        if (switchItems == null)
            len = 0;
        else
            len = switchItems.length;
        return len;
    }


    @Override
    public Object getItem(int id) {
        return switchItems[id];
    }


    @Override
    public long getItemId(int id) {
        return switchItems[id].iD;
    }

}
