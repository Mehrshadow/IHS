package ir.parsansoft.app.ihs.center.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import ir.parsansoft.app.ihs.center.AllNodes;
import ir.parsansoft.app.ihs.center.AllViews;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.Database.Node.Struct;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;

import static ir.parsansoft.app.ihs.center.G.inflater;


public class AdapterListViewNode extends ArrayAdapter<Database.Node.Struct> {

    Context context;
    private boolean isSettingVisible;
    private int columnCount;
    private int uiIndex;

    public AdapterListViewNode(Context context, Struct[] nodes, boolean isSettingVisible) {
        super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.isSettingVisible = (G.currentUser != null && G.currentUser.rol == 1) && isSettingVisible;
    }

    /**
     * @param columnCount tedad setoone gridview has, mikhaim vase zaribaye in adad view ro null kopnim
     *                    ta 2ros load beshe
     */
    public AdapterListViewNode(Context context, Struct[] nodes, boolean isSettingVisible, int columnCount) {
        super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.isSettingVisible = (G.currentUser != null && G.currentUser.rol == 1) && isSettingVisible;
        this.columnCount = columnCount;
    }

    // A wrong way we did!!
    // bazi az gridview ha 4 tai hastan baziashoon 3 tai
    // vase in k item haye gridview 2ros load beshan majboor shodim be soorate dasti, az jai k gridview scroll mikhore
    // view item ha ro bayad null konim ta load beshan
    // vase grid 3 tai item haye zaribe 3 va gridview 4tai item haye zaribe 4
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        AllViews.CO_l_node_simple_key newSimpleKey = null;
        AllViews.CO_l_node_simple_dimmer newDimmer = null;
        AllViews.CO_l_node_IoModule newIOModule = null;

        View row = convertView;

        Database.Node.Struct node = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            G.log("*****Node Type ID=" + node.nodeTypeID);
            switch (node.nodeTypeID) {
                case AllNodes.Node_Type.SIMPLE_SWITCH_1:
                case AllNodes.Node_Type.SIMPLE_SWITCH_2:
                case AllNodes.Node_Type.SIMPLE_SWITCH_3:
                case AllNodes.Node_Type.AIR_CONDITION:
                case AllNodes.Node_Type.CURTAIN_SWITCH:
                case AllNodes.Node_Type.WC_SWITCH:
                case AllNodes.Node_Type.Sensor_Magnetic:
                case AllNodes.Node_Type.Sensor_SMOKE:
                    row = inflater.inflate(R.layout.l_node_simple_key, parent, false);
                    newSimpleKey = new AllViews.CO_l_node_simple_key(row);
                    row.setTag(newSimpleKey);
                    G.log("*** position = " + position + " simple key");
                    break;
                case AllNodes.Node_Type.SIMPLE_DIMMER_1:
                case AllNodes.Node_Type.SIMPLE_DIMMER_2:
                    row = inflater.inflate(R.layout.l_node_simple_dimmer, parent, false);
                    newDimmer = new AllViews.CO_l_node_simple_dimmer(row);
                    row.setTag(newDimmer);
                    G.log("*** position = " + position + " dimmer");
                    break;
                case AllNodes.Node_Type.IOModule:
                    row = inflater.inflate(R.layout.l_node_io_module, parent, false);
                    newIOModule = new AllViews.CO_l_node_IoModule(row);
                    row.setTag(newIOModule);
                    G.log("*** position = " + position + " IO Module");
                    break;
                default:
                    break;
            }
        } else {
            switch (node.nodeTypeID) {
                case AllNodes.Node_Type.SIMPLE_SWITCH_1:
                case AllNodes.Node_Type.SIMPLE_SWITCH_2:
                case AllNodes.Node_Type.SIMPLE_SWITCH_3:
                case AllNodes.Node_Type.AIR_CONDITION:
                case AllNodes.Node_Type.CURTAIN_SWITCH:
                case AllNodes.Node_Type.WC_SWITCH:
                case AllNodes.Node_Type.Sensor_Magnetic:
                case AllNodes.Node_Type.Sensor_SMOKE:
                    try {
                        newSimpleKey = (AllViews.CO_l_node_simple_key) row.getTag();
                    } catch (ClassCastException e) {
                        row = inflater.inflate(R.layout.l_node_simple_key, parent, false);
                        newSimpleKey = new AllViews.CO_l_node_simple_key(row);
                        row.setTag(newSimpleKey);
                    }
                    break;
                case AllNodes.Node_Type.SIMPLE_DIMMER_1:
                case AllNodes.Node_Type.SIMPLE_DIMMER_2:
                    try {
                        newDimmer = (AllViews.CO_l_node_simple_dimmer) row.getTag();
                    } catch (ClassCastException e) {
                        row = inflater.inflate(R.layout.l_node_simple_dimmer, parent, false);
                        newDimmer = new AllViews.CO_l_node_simple_dimmer(row);
                        row.setTag(newDimmer);
                    }
                    break;
                case AllNodes.Node_Type.IOModule:
                    try {
                        newIOModule = (AllViews.CO_l_node_IoModule) row.getTag();
                    } catch (ClassCastException e) {
                        row = inflater.inflate(R.layout.l_node_io_module, parent, false);
                        newIOModule = new AllViews.CO_l_node_IoModule(row);
                        row.setTag(newIOModule);
                    }
                    break;
            }
            G.log("*** position = " + position + " full");
        }

        G.log("Node ID: " + node.iD);

        int nodeIndex = G.nodeCommunication.allNodes.indexOfKey(node.iD);
        G.log("Node index:" + nodeIndex);
        if (nodeIndex >= 0) {
            switch (node.nodeTypeID) {
                case AllNodes.Node_Type.SIMPLE_SWITCH_1:
                case AllNodes.Node_Type.SIMPLE_SWITCH_2:
                case AllNodes.Node_Type.SIMPLE_SWITCH_3:
                case AllNodes.Node_Type.AIR_CONDITION:
                case AllNodes.Node_Type.CURTAIN_SWITCH:
                case AllNodes.Node_Type.WC_SWITCH:
                case AllNodes.Node_Type.Sensor_Magnetic:
                case AllNodes.Node_Type.Sensor_SMOKE:
                    uiIndex = G.nodeCommunication.allNodes.get(node.iD).addUI(newSimpleKey);
                    G.log("*** position = " + position + " simple key");
                    break;
                case AllNodes.Node_Type.SIMPLE_DIMMER_1:
                case AllNodes.Node_Type.SIMPLE_DIMMER_2:
                    uiIndex = G.nodeCommunication.allNodes.get(node.iD).addUI(newDimmer);
                    G.log("*** position = " + position + " dimmer");
                    break;
                case AllNodes.Node_Type.IOModule:
                    uiIndex = G.nodeCommunication.allNodes.get(node.iD).addUI(newIOModule);
                    G.log("*** position = " + position + " IO Module");
                    break;
                default:
                    break;

            }

            G.log("ui Index= " + uiIndex);
            G.nodeCommunication.allNodes.get(node.iD).setSettingVisiblity(uiIndex, isSettingVisible);
        }
        return row;
    }
}
