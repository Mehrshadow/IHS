package ir.parsansoft.app.ihs.center.adapters;

import ir.parsansoft.app.ihs.center.AllViews.CO_l_weather_gridview_item;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.Database.WeatherTypes.Struct;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.components.AssetsManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class AdapterScenarioDetailsWeather extends BaseAdapter {

    Context                        context;
    Struct[] weatherTypes;
    String                         selectedWeathers = "";

    public AdapterScenarioDetailsWeather(Context context, Struct[] weatherTypes, String selectedWeathers) {
        //super(context, R.layout.l_node_simple_key, nodes);
        this.context = context;
        this.weatherTypes = weatherTypes;
        this.selectedWeathers = selectedWeathers;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            row = new View(context);
            if (G.setting.languageID == 1 || G.setting.languageID == 4)
                row = inflater.inflate(R.layout.l_weather_gridview_item, null);
            else
                row = inflater.inflate(R.layout.l_weather_gridview_item_rtl, null);
        }
        final CO_l_weather_gridview_item lo = new CO_l_weather_gridview_item(row);
        if (G.setting.languageID == 2 || G.setting.languageID == 3) {
            lo.mainWeatherLayout.setRotationY(180);
        }
        lo.txtWeather.setText(G.T.getSentence(weatherTypes[position].nameSentenceID));
        //        lo.imgWeather.setImageResource(G.getResId(weatherTypes[position].icon));
        lo.imgWeather.setImageBitmap(AssetsManager.getBitmap(context, "my_images/icon/weather/" + weatherTypes[position].icon));
        lo.chkWeather.setChecked(selectedWeathers.contains("W" + weatherTypes[position].iD + ";"));
        lo.mainWeatherLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                G.log("Clicked on : " + position + " " + lo.chkWeather.isChecked());
                lo.chkWeather.setChecked( !lo.chkWeather.isChecked());
            }
        });
        lo.chkWeather.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean newStatus) {
                if (newStatus) {
                    if ( !selectedWeathers.contains("W" + weatherTypes[position].iD))
                        selectedWeathers += "W" + weatherTypes[position].iD + ";";
                }
                else {
                    if (selectedWeathers.contains("W" + weatherTypes[position].iD))
                        selectedWeathers = selectedWeathers.replace("W" + weatherTypes[position].iD + ";", "");
                }
            }
        });
        return row;
    }
    @Override
    public int getCount() {
        int len;
        if (weatherTypes == null)
            len = 0;
        else
            len = weatherTypes.length;
        return len;
    }

    public String getSelectedWeathers() {
        return selectedWeathers;
    }

    @Override
    public Object getItem(int id) {
        return weatherTypes[id];
    }


    @Override
    public long getItemId(int id) {
        return weatherTypes[id].iD;
    }

}
