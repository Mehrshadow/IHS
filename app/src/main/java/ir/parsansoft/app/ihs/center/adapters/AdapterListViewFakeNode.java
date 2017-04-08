package ir.parsansoft.app.ihs.center.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import ir.parsansoft.app.ihs.center.AllNodes;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;

/**
 * Created by HaMiD on 2/13/2017.
 */

public class AdapterListViewFakeNode extends BaseAdapter {

    Context context;
    Database.Node.Struct[] nodes;
    private boolean        isSettingVisible;

    public AdapterListViewFakeNode(Context context, Database.Node.Struct[] nodes, boolean isSettingVisible) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.nodes = nodes;
        this.isSettingVisible = (G.currentUser != null && G.currentUser.rol == 1) && isSettingVisible;
        ///Change
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            row = new View(context);
            G.log("Node Type ID=" + nodes[position].nodeTypeID);
            switch (nodes[position].nodeTypeID) {
                case AllNodes.Node_Type.SIMPLE_SWITCH_1:
                case AllNodes.Node_Type.SIMPLE_SWITCH_2:
                case AllNodes.Node_Type.SIMPLE_SWITCH_3:
                case AllNodes.Node_Type.AIR_CONDITION:
                case AllNodes.Node_Type.CURTAIN_SWITCH:
                case AllNodes.Node_Type.WC_SWITCH:
                    row = inflater.inflate(R.layout.l_node_simple_key, null);
                    break;
                case AllNodes.Node_Type.SIMPLE_DIMMER_1:
                case AllNodes.Node_Type.SIMPLE_DIMMER_2:
                    row = inflater.inflate(R.layout.l_node_simple_dimmer, null);
                    break;
                default:
                    break;
            }
            if (row != null) {
                G.log("Node ID: " + nodes[position].iD);
                int nodeIndex = G.nodeCommunication.allNodes.indexOfKey(nodes[position].iD);
                G.log("Node index:" + nodeIndex);
                if (nodeIndex >= 0) {
                    int uiIndex = G.nodeCommunication.allNodes.get(nodes[position].iD).addUI(row);
                    G.log("ui Index= " + uiIndex);
                    G.nodeCommunication.allNodes.get(nodes[position].iD).setSettingVisiblity(uiIndex, isSettingVisible);
                }
            }
        }
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
