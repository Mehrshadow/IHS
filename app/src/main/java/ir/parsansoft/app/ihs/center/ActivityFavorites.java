package ir.parsansoft.app.ihs.center;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import ir.parsansoft.app.ihs.center.AllViews.CO_f_favorites;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewNode;

public class ActivityFavorites extends ActivityEnhanced {

    private AdapterListViewNode grdListAdapter;
    private Database.Node.Struct[] nodes;
    private CO_f_favorites formObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_favorites);
        formObjects = new CO_f_favorites(this);
        translateForm();
        changeMenuIconBySelect(1);
        loadFavorites(true);
        formObjects.grdNodes.setOnTouchListener(null);
        formObjects.btnAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadFavorites(false);
            }
        });
        formObjects.btnFavorites.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadFavorites(true);
            }
        });

//        G.HANDLER.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadFavorites(true);
//            }
//        }, 300);
    }

    private void loadFavorites(boolean onlyFavorites) {
        if (onlyFavorites)
            nodes = Database.Node.select("isFavorite=1");
        else
            nodes = Database.Node.select("");

        grdListAdapter = null;
        if (nodes == null)
            nodes = new Database.Node.Struct[0];

        grdListAdapter = new AdapterListViewNode(G.currentActivity, nodes, true, 4);
        formObjects.grdNodes.setAdapter(grdListAdapter);
        Animation.setListAnimation(this, formObjects.grdNodes);
        //grdListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites(true);
    }

    @Override
    public void translateForm() {
        super.translateForm();
        formObjects.btnFavorites.setText(G.T.getSentence(232));
        formObjects.btnAll.setText(G.T.getSentence(233));
    }
}
