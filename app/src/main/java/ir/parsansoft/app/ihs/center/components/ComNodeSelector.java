package ir.parsansoft.app.ihs.center.components;

import ir.parsansoft.app.ihs.center.AllViews.CO_c_comp_node_selector;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.adapters.DialogNodeSelector;
import ir.parsansoft.app.ihs.center.adapters.DialogNodeSelector.DialogClosedListener;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


public class ComNodeSelector extends LinearLayout {

    Context                 context;
    FragmentManager         fm;
    DialogNodeSelector      dlgNodeSelector;
    CO_c_comp_node_selector lo;
    int                     selectedSwitchID = 0;
    Database.Switch.Struct  mySwitch         = null;
    SelectedNodeChange      snc              = null;

    public interface SelectedNodeChange {
        void onSwitchSelected(int SwitchID, String opr, float value);
    }

    public void setOnSwitchSelected(SelectedNodeChange selectedNodeChange) {
        snc = selectedNodeChange;
    }

    public ComNodeSelector(Context context) {
        super(context);
        this.context = context;
        initialize(context);
    }

    public ComNodeSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.c_comp_node_selector, this, true);
        lo = new CO_c_comp_node_selector(view);
        lo.btnSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                selectNewSwitch();
            }
        });
        lo.laySelector.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                selectNewSwitch();
            }
        });
    }

    private void selectNewSwitch() {
        dlgNodeSelector = new DialogNodeSelector();
        dlgNodeSelector.serSelectedSwitchID(selectedSwitchID);
        dlgNodeSelector.setonSwitchSelected(new DialogClosedListener() {
            @Override
            public void onSwitchSelected(int SwitchID, String opr, float value) {
                G.log("Listener Commited ...");
                selectedSwitchID = SwitchID;
                setSwitch(selectedSwitchID);
                if (snc != null)
                    snc.onSwitchSelected(selectedSwitchID, opr,value);
            }

            @Override
            public void onSwitchSelectedW9(int SwitchID, float value, boolean active, int reverseTime, float preValue) {

            }
        });
        dlgNodeSelector.show(G.fragmentManager, null);
    }
    public void setSwitch(int selectedSwitchID) {
        this.mySwitch = Database.Switch.select(selectedSwitchID);
        try {
            lo.txtSwitchName.setText(mySwitch.getFullName());
            lo.imgNodeIcon.setImageBitmap(AssetsManager.getBitmap(G.context, G.DIR_ICONS_NODES + "/" + Database.Node.select(mySwitch.nodeID).icon));
        }
        catch (Exception e) {
            G.printStackTrace(e);
        }
    }
    public Database.Switch.Struct getSelectedSwitch() {
        return mySwitch;
    }

    public int getSelectedSwitchID() {
        return selectedSwitchID;
    }
}
