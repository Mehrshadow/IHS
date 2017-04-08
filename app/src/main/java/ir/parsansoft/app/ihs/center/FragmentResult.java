package ir.parsansoft.app.ihs.center;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentResult extends FragmentEnhanced {
    TextView txtValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = G.inflater.inflate(R.layout.fragment, container, false);
        txtValue = (TextView) view.findViewById(R.id.txtValue);
        setHtml();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        setHtml();
    }

    @Override
    public void setHtml() {
        int index = G.context.getSharedPreferences("SAVE_DATA", 0).getInt("INDEX", 0);
        if (txtValue != null) {
            if (ActivityScenario.result != null) {
                txtValue.setText(Html.fromHtml(ActivityScenario.result));
            } else if (ActivityWelcome11Scenario.result != null) {
                txtValue.setText(Html.fromHtml(ActivityWelcome11Scenario.result));
            }

        }
    }
}
