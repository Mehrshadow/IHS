package ir.parsansoft.app.ihs.center.adapters;

import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.SysLog;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterListViewLog extends ArrayAdapter<Database.Log.Struct> {

    SysLog.LogType[]     logTypes;
    SysLog.LogOperator[] logOperators;


    public AdapterListViewLog(ArrayList<Database.Log.Struct> log) {
        super(G.context, R.layout.l_list_log_item, log);
        logTypes = SysLog.LogType.values();
        logOperators = SysLog.LogOperator.values();
    }

    private class ViewHolder {
        TextView     txtID, txtDate, txtType, txtDescription, txtOperator;
        LinearLayout layout;

        public ViewHolder(View view) {
            txtID = (TextView) view.findViewById(R.id.txtID);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtType = (TextView) view.findViewById(R.id.txtType);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            txtOperator = (TextView) view.findViewById(R.id.txtOperatorID);
            layout = (LinearLayout) view.findViewById(R.id.layMainLog);
        }

        public void fill(ArrayAdapter<Database.Log.Struct> adapter, final Database.Log.Struct log, int position) {
            txtID.setText("" + (position + 1));
            txtDate.setText(log.date);
            switch (logTypes[log.type]) {
                case COMMAND_ERROR:
                    txtType.setText("Error");
                    break;
                case DATA_CHANGE:
                    txtType.setText("Data Change");
                    break;
                case NODE_STATUS:
                    txtType.setText("Status");
                    break;
                case SCENARIO_CHANGE:
                    txtType.setText("Scenario");
                    break;
                case SYSTEM_EVENTS:
                    txtType.setText("System");
                    break;
                case SYSTEM_SETTINGS:
                    txtType.setText("Setting");
                    break;
                case VALUE_CHANGE:
                    txtType.setText("Device Change");
                    break;
            }
            Database.Mobiles.Struct mb;
            switch (logOperators[log.operatorType]) {
                case MOBILE:
                    mb = Database.Mobiles.select(log.operatorID);
                    if (mb == null)
                        txtOperator.setText("Unknown Mobile");
                    else
                        txtOperator.setText("Local Mobile " + mb.name);
                    break;
                case OPERAOR:
                    Database.User.Struct usr = Database.User.select(log.operatorID);
                    if (usr == null)
                        txtOperator.setText("Unknown User");
                    else
                        txtOperator.setText(usr.username);
                    break;
                case NODE:
                    Database.Node.Struct nd = Database.Node.select(log.operatorID);
                    if (nd == null)
                        txtOperator.setText("Unknown Device");
                    else
                        txtOperator.setText("Device: " + nd.name);
                    break;
                case SYSYTEM:
                    txtOperator.setText("System");
                    break;
                case WEB:
                    if (log.operatorID == 0) {
                        txtOperator.setText("Server");
                    }
                    else {
                        mb = Database.Mobiles.select(log.operatorID);
                        if (mb == null)
                            txtOperator.setText("Unknown Mobile");
                        else
                            txtOperator.setText("Remote Mobile " + mb.name);
                    }
                    break;
            }
            txtDescription.setText(log.des);
            layout.setBackgroundColor(((position % 2) == 1) ? G.currentActivity.getResources().getColor(R.color.gridview_odd) : G.currentActivity.getResources().getColor(R.color.gridview_even));
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Database.Log.Struct thisLog = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = G.inflater.inflate(R.layout.l_list_log_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.fill(this, thisLog, position);
        return convertView;
    }

}
