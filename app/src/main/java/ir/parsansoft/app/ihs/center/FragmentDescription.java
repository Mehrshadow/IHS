package ir.parsansoft.app.ihs.center;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentDescription extends FragmentEnhanced {

    TextView txtValue;

    /**
     * On the newInstance function of your fragment you add the arguments you
     * wanna send to it: Create a new instance of DetailsFragment, initialized
     * to show the text at 'index'.
     */
    public static FragmentDescription newInstance(int i) {
        FragmentDescription f = new FragmentDescription();
        Bundle args = new Bundle();
        args.putInt("index", i);
        f.setArguments(args);
        return f;
    }
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
        try {
            int index = G.context.getSharedPreferences("SAVE_DATA", 0).getInt("INDEX", 0);
            if (txtValue != null) {
                if (ActivityScenario.des != null) {
                    txtValue.setText(Html.fromHtml(ActivityScenario.des));
                }
                else if (ActivityWelcome11Scenario.des != null) {
                    txtValue.setText(Html.fromHtml(ActivityWelcome11Scenario.des));
                }

            }
        }
        catch (Exception e) {
            G.printStackTrace(e);
        }
    }

}
