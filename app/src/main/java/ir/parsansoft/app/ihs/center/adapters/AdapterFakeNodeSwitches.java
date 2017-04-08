package ir.parsansoft.app.ihs.center.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;

/**
 * Created by HaMiD on 2/13/2017.
 */

public class AdapterFakeNodeSwitches extends BaseAdapter {

    Context context;
    Database.Switch.Struct[] switchItems;
    private List<String> allPorts;
    private List<String> selectedPorts;
    private List<String> availablePorts;
    private int portCount;
    private Spinner spnPorts;
    private int Port;

    private AdapterIoTypesSpinner mAdapterIoTypesSpinner;

    public AdapterFakeNodeSwitches(Context context, Database.Switch.Struct[] switchItems,List<String> availablePorts) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.switchItems = switchItems;
        this.availablePorts = availablePorts;
    }

    public Database.Switch.Struct[] getSwitchItems() {
        return switchItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            View row = convertView;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                G.log("AdapterFakeNodeSwitches : Get View ... position :" + position);
                row = new View(context);
                row = inflater.inflate(R.layout.l_list_node_fake_switches, null);

                TextView txtPorts = (TextView) row.findViewById(R.id.txtPorts);
                spnPorts = (Spinner) row.findViewById(R.id.spnPorts);
                loadSpinner(position);

                txtPorts.setText(G.T.getSentence(852));

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    private void loadSpinner(final int mainPosition) {
        try {
//            availablePorts = new ArrayList<>();
//            availablePorts.add("3");
//            availablePorts.add("4");
//            availablePorts.add("5");
//            availablePorts.add("6");
//            availablePorts.add("7");
//            availablePorts.add("8");
//            availablePorts.add("9");
//            availablePorts.add("10");
//            availablePorts.add("11");
//            availablePorts.add("12");
//
//
//            Database.Switch.Struct[] fakeswitches = Database.Switch.select("isIOModuleSwitch=" + 1);
//            if (fakeswitches != null) {
//                for (int i = 0; i < fakeswitches.length; i++) {
//                    if (fakeswitches[i].IOModulePort > 0 &&
//                            fakeswitches[i].IOModulePort < 12) {
//                        availablePorts.remove(String.valueOf(fakeswitches[i].IOModulePort));
//                    }
//                }
//            }

            mAdapterIoTypesSpinner = new AdapterIoTypesSpinner(availablePorts);
            spnPorts.setAdapter(mAdapterIoTypesSpinner);

            spnPorts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switchItems[mainPosition].IOModulePort = Integer.valueOf(availablePorts.get(position));

                    Port = Integer.valueOf(availablePorts.get(position));
//                    selectedPorts.add(String.valueOf(Port));
                    availablePorts.remove(new Integer(Port));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
