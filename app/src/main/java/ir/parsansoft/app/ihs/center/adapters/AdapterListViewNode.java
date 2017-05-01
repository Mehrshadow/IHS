package ir.parsansoft.app.ihs.center.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import ir.parsansoft.app.ihs.center.AllNodes;
import ir.parsansoft.app.ihs.center.Database.Node.Struct;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;


public class AdapterListViewNode extends BaseAdapter {

    Context context;
    final Struct[] nodes;
    private boolean isSettingVisible;
    private GridView gridView;

    public AdapterListViewNode(Context context, final Struct[] nodes, boolean isSettingVisible) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.nodes = nodes;
        this.isSettingVisible = (G.currentUser != null && G.currentUser.rol == 1) && isSettingVisible;
        ///Change
    }

    public AdapterListViewNode(Context context, final Struct[] nodes, boolean isSettingVisible, GridView gridView) {
        this.context = context;
        this.nodes = nodes;
        this.isSettingVisible = (G.currentUser != null && G.currentUser.rol == 1) && isSettingVisible;
        this.gridView = gridView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

//        if (position >= 9 && position % 3 == 0) {
//            convertView = null;
//        }
//        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            row = new View(context);  ///////////////////////////
            G.log("Node Type ID=" + nodes[position].nodeTypeID);
            switch (nodes[position].nodeTypeID) {
                case AllNodes.Node_Type.SIMPLE_SWITCH_1:
                case AllNodes.Node_Type.SIMPLE_SWITCH_2:
                case AllNodes.Node_Type.SIMPLE_SWITCH_3:
                case AllNodes.Node_Type.AIR_CONDITION:
                case AllNodes.Node_Type.CURTAIN_SWITCH:
                case AllNodes.Node_Type.WC_SWITCH:
                case AllNodes.Node_Type.Sensor_Magnetic:
                case AllNodes.Node_Type.Sensor_SMOKE:
                    row = inflater.inflate(R.layout.l_node_simple_key, null);
                    break;
                case AllNodes.Node_Type.SIMPLE_DIMMER_1:
                case AllNodes.Node_Type.SIMPLE_DIMMER_2:
                    row = inflater.inflate(R.layout.l_node_simple_dimmer, null);
                    break;
                case AllNodes.Node_Type.IOModule:
                    row = inflater.inflate(R.layout.l_node_io_module, null);
                    break;
                default:
                    row = inflater.inflate(R.layout.l_node_simple_key, null);
                    break;
            }
//            if (row != null) {
//                G.log("Node ID: " + nodes[position].iD);
//
//                int nodeIndex = G.nodeCommunication.allNodes.indexOfKey(nodes[position].iD);
//                G.log("Node index:" + nodeIndex);
////                if (nodeIndex >= 0) {
//                    int uiIndex = G.nodeCommunication.allNodes.get(nodes[position].iD).addUI(row);
//                    G.log("ui Index= " + uiIndex);
//                    G.nodeCommunication.allNodes.get(nodes[position].iD).setSettingVisiblity(uiIndex, isSettingVisible);
////                }
//
//            }
//        } else {
//            row = convertView;
//        }



//        G.log("Scroll: " + gridView.getFirstVisiblePosition());

//        if (row != null) {
        G.log("Node ID: " + nodes[position].iD);

        int nodeIndex = G.nodeCommunication.allNodes.indexOfKey(nodes[position].iD);
        G.log("Node index:" + nodeIndex);
//                if (nodeIndex >= 0) {
        int uiIndex = G.nodeCommunication.allNodes.get(nodes[position].iD).addUI(row);
        G.log("ui Index= " + uiIndex);
        G.nodeCommunication.allNodes.get(nodes[position].iD).setSettingVisiblity(uiIndex, isSettingVisible);
//                }

//        }

        return row;
    }

    @Override
    public int getCount() {
        int len;
        if (nodes == null)
            len = 0;
        else
            len = nodes.length;
        return len;
    }


    @Override
    public Object getItem(int id) {
        return nodes[id];
    }


    @Override
    public long getItemId(int id) {
        return nodes[id].iD;
    }


}
