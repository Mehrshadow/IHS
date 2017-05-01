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

import java.util.ArrayList;
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
    private List<String> availablePorts;
    private List<String> spinnerPorts;
    private Spinner spnPorts;
    private String Port;

    private AdapterIoTypesSpinner mAdapterIoTypesSpinner;
    private boolean firstTimeLoaded = true;
    private int timesLoaded = 0;
    private ArrayList<String> temp = new ArrayList<>();

    public AdapterFakeNodeSwitches(Context context, Database.Switch.Struct[] switchItems, List<String> spinnerPorts) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.switchItems = switchItems;
        this.spinnerPorts = spinnerPorts;
        availablePorts = new ArrayList<>();
        availablePorts.addAll(spinnerPorts);
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
//            spinnerPorts = new ArrayList<>();
//            spinnerPorts.add("3");
//            spinnerPorts.add("4");
//            spinnerPorts.add("5");
//            spinnerPorts.add("6");
//            spinnerPorts.add("7");
//            spinnerPorts.add("8");
//            spinnerPorts.add("9");
//            spinnerPorts.add("10");
//            spinnerPorts.add("11");
//            spinnerPorts.add("12");
//
//
//            Database.Switch.Struct[] fakeswitches = Database.Switch.select("isIOModuleSwitch=" + 1);
//            if (fakeswitches != null) {
//                for (int i = 0; i < fakeswitches.length; i++) {
//                    if (fakeswitches[i].IOModulePort > 0 &&
//                            fakeswitches[i].IOModulePort < 12) {
//                        spinnerPorts.remove(String.valueOf(fakeswitches[i].IOModulePort));
//                    }
//                }
//            }


            mAdapterIoTypesSpinner = new AdapterIoTypesSpinner(spinnerPorts);
            spnPorts.setAdapter(mAdapterIoTypesSpinner);

            spnPorts.setSelection(mainPosition);

            spnPorts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    // spinner ha be tedad switch ha load mishan, vase in bare aval datashoon hazf nashe
                    // be tedade switch ha niaz darim
                    try {
                        if (timesLoaded > switchItems.length - 1) {

                            switchItems[mainPosition].IOModulePort = Integer.valueOf(spinnerPorts.get(position)) + 2;

                            Port = spinnerPorts.get(position);
                            temp.remove(mainPosition);
                            temp.add(mainPosition, Port);

                            spinnerPorts.clear();
                            spinnerPorts.addAll(availablePorts);
//                            spinnerPorts = new ArrayList<>(availablePorts);
                            spinnerPorts.removeAll(temp);
                            mAdapterIoTypesSpinner.notifyDataSetChanged();
                            notifyDataSetChanged();

                        } else {

                            temp.add(spinnerPorts.get(0));
                            spinnerPorts.remove(0);

                            timesLoaded++;
                        }
                    } catch (Exception e) {
                        G.printStackTrace(e);
                    }
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
