package ir.parsansoft.app.ihs.center.adapters;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterSpinner extends ArrayAdapter<Database.User.Struct> {

    List<Database.User.Struct> users = new ArrayList<Database.User.Struct>();

    public AdapterSpinner(List<Database.User.Struct> objects) {
        super(G.context, android.R.layout.simple_list_item_1, objects);
        users = objects;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Database.User.Struct getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(G.context);
        label.setTextColor(Color.BLACK);
        Database.User.Struct user = getItem(position);
        label.setText(user.username);
        label.setTypeface(Typeface.MONOSPACE);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(G.context);
        label.setTextColor(Color.BLACK);
        //label.setText(users.toArray(new Database.User.Struct[users.size()])[position].username);
        Database.User.Struct user = getItem(position);
        label.setText(user.username);
        label.setTypeface(Typeface.MONOSPACE);
        return label;
    }

}
