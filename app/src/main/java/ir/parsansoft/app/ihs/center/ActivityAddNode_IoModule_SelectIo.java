package ir.parsansoft.app.ihs.center;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ir.parsansoft.app.ihs.center.adapters.AdapterListViewNode;

public class ActivityAddNode_IoModule_SelectIo extends ActivityEnhanced {

    AllViews.Co_d_add_Node_IoModule_SelectIo formObj;
    Database.Node.Struct[] nodes;
    private AdapterListViewNode    grdListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formObj = new AllViews.Co_d_add_Node_IoModule_SelectIo(this);
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.activity_add_node__io_module__select_io);
        else
            setContentView(R.layout.activity_add_node__io_module__select_io);

        translateForm();
        initView();

    }

    private void initView() {
        nodes = Database.Node.select("isFavorite=1");

        grdListAdapter = null;
        grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, true);
        formObj.grdNodes.setAdapter(grdListAdapter);
        Animation.setListAnimation(this, formObj.grdNodes);
    }


    @Override
    public void translateForm() {
        super.translateForm();

    }
}
