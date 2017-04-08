package ir.parsansoft.app.ihs.center.components;

import ir.parsansoft.app.ihs.center.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


public class ComNodeSimpleSwitch extends LinearLayout {

    public ComNodeSimpleSwitch(Context context) {
        super(context);
        initialize(context);
    }


    public ComNodeSimpleSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }


    public ComNodeSimpleSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }


    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.l_node_simple_key, this, true);

    }
}
