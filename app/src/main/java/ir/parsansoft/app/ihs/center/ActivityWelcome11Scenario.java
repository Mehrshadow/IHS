package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import ir.parsansoft.app.ihs.center.AllViews.CO_f_senario;
import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.UI.OnScenarioChange;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewScenario;
import ir.parsansoft.app.ihs.center.adapters.AdapterListViewScenario.ScenarioListItemClick;
import ir.parsansoft.app.ihs.center.adapters.AdapterViewPagerScenario;

import java.util.ArrayList;

public class ActivityWelcome11Scenario extends ActivityWizard implements OnClickListener, OnPageChangeListener {

    private CO_f_senario                        fo;
    private AdapterListViewScenario             listAdapter;
    private ArrayList<Database.Scenario.Struct> scenarioItems        = new ArrayList<Database.Scenario.Struct>();
    Database.Scenario.Struct[]                  scenarios;
    private AdapterViewPagerScenario            pagerAdapter;
    LinearLayout                                lay_viewPager;
    LinearLayout[]                              viewPagerTabs;
    public static int                           selectedItemPosition = -1;
    int                                         index                = 0;
    public static String                        des, cond, result;
    boolean                                     isBusy               = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.f_welcome_scenario);
//        fo = new CO_f_senario(this);
        fo = new CO_f_senario(this);
        viewPagerTabs = new LinearLayout[3];
        viewPagerTabs[0] = (LinearLayout) findViewById(R.id.laybtnDescription);
        viewPagerTabs[1] = (LinearLayout) findViewById(R.id.laybtnCondition);
        viewPagerTabs[2] = (LinearLayout) findViewById(R.id.laybtnResult);
        viewPagerTabs[0].setOnClickListener(this);
        viewPagerTabs[1].setOnClickListener(this);
        viewPagerTabs[2].setOnClickListener(this);
        translateForm();
        scenarios = Database.Scenario.select("");
        listAdapter = new AdapterListViewScenario(G.currentActivity, scenarios);
        pagerAdapter = new AdapterViewPagerScenario(getSupportFragmentManager());

        fo.viewPager.setAdapter(pagerAdapter);
        fo.viewPager.setCurrentItem(0);

        fo.viewPager.setOnPageChangeListener(this);
        viewPagerTabs[0].setBackgroundResource(R.drawable.lay_viewpager_header_press);

        fo.lstSenario.setAdapter(listAdapter);
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        G.ui.setOnScenarioChangeListenner(new OnScenarioChange() {

            @Override
            public void onOnScenarioChanged(int scenarioID) {
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        scenarios = Database.Scenario.select("");
                        if (scenarios != null) {
                            listAdapter.refreshScenarios(scenarios);
                        }
                    }
                });
            }
        });
        //fo.txtDetails.setText(Html.fromHtml(getString(R.string.sapleHtml)));
        fo.btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                G.currentActivity.startActivity(new Intent(G.currentActivity, ActivityScenarioW1Name.class));
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        setOnBackListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    startActivity(new Intent(G.currentActivity, ActivityWelcome10Mobile.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });

        setOnNextListenner(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !isBusy) {
                    isBusy = true;
                    startActivity(new Intent(G.currentActivity, ActivityWelcome12User.class));
                    Animation.doAnimation(Animation_Types.FADE);
                    finish();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        des = "";
        cond = "";
        result = "";
        fo.viewPager.setCurrentItem(0);
//        if (listAdapter != null) {
//            listAdapter = null;
//            G.log("On resume accured");
//            scenarios = Database.Scenario.select("");
//            listAdapter = new AdapterListViewScenario(G.currentActivity, scenarios);
//            fo.lstSenario.setAdapter(listAdapter);
        if (listAdapter != null) {
            G.log("On resume accured");
            scenarios = Database.Scenario.select("");
            listAdapter.updateList(scenarios);
            listAdapter.setOnScenarioListItemClick(new ScenarioListItemClick() {
                @Override
                public void onScenarioListItemClick(int scenarioID, int position) {
                    G.log("onScenarioListItemClick");
                    loadScenarioBrif(scenarioID);
                    Database.Scenario.Struct scenario = Database.Scenario.select(scenarioID);
                    des = scenario.getDetailsInHTML(0);
                    cond = scenario.getDetailsInHTML(1);
                    result = scenario.getDetailsInHTML(2);
                    for (int i = 0; i < 3; i++) {
                        FragmentEnhanced frag = pagerAdapter.getRegisteredFragment(i);
                        if (frag != null) {
                            frag.setHtml();
                        }
                    }
                    View v;
                    /********************************************************/
                    for (int i = 0; i < fo.lstSenario.getChildCount(); i++) {
                        v = fo.lstSenario.getChildAt(i);
                        if (v != null) {
                            v.findViewById(R.id.layBack).setBackgroundResource(R.drawable.lay_scenario_list_row_normal);
                        }
                    }
                    selectedItemPosition = position;
                    /********************************************************/
                }
            });
            listAdapter.notifyDataSetChanged();
            /***************************/
            View v = fo.lstSenario.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - fo.lstSenario.getPaddingTop());
            fo.lstSenario.setSelectionFromTop(index, top);
            /*****************************/

        }
    }

    private void loadScenarioBrif(int scenarioID) {
        G.log("Run : loadScenarioBrif");
        Database.Scenario.Struct scenario = Database.Scenario.select(scenarioID);
        if (scenario != null) {
            G.log("Run : setText from Html");
            //fo.txtDetails.setText(Html.fromHtml(scenario.getDetailsInHTML()));
        }
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fo.btnAdd.setText(G.T.getSentence(105));
        fo.txtDescription.setText(G.T.getSentence(624));
        fo.txtCondition.setText(G.T.getSentence(625));
        fo.txtResult.setText(G.T.getSentence(626));
    }

    @Override
    protected void onPause() {
        index = fo.lstSenario.getFirstVisiblePosition();
        des = "";
        cond = "";
        result = "";
        fo.viewPager.setCurrentItem(0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        des = "";
        cond = "";
        result = "";
        selectedItemPosition = -1;
        G.ui.removeOnScenarioChangedListenner();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.laybtnDescription:
                fo.viewPager.setCurrentItem(0);
                changeTabBackground();
                viewPagerTabs[0].setBackgroundResource(R.drawable.lay_viewpager_header_press);
                break;
            case R.id.laybtnCondition:
                fo.viewPager.setCurrentItem(1);
                changeTabBackground();
                viewPagerTabs[1].setBackgroundResource(R.drawable.lay_viewpager_header_press);
                break;
            case R.id.laybtnResult:
                fo.viewPager.setCurrentItem(2);
                changeTabBackground();
                viewPagerTabs[2].setBackgroundResource(R.drawable.lay_viewpager_header_press);
                break;


        }
    }
    private void changeTabBackground() {
        viewPagerTabs[0].setBackgroundResource(R.drawable.viewpager_tabs_selector);
        viewPagerTabs[1].setBackgroundResource(R.drawable.viewpager_tabs_selector);
        viewPagerTabs[2].setBackgroundResource(R.drawable.viewpager_tabs_selector);
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTabBackground();
        switch (position) {
            case 0:
                viewPagerTabs[0].setBackgroundResource(R.drawable.lay_viewpager_header_press);
                break;
            case 1:
                viewPagerTabs[1].setBackgroundResource(R.drawable.lay_viewpager_header_press);
                break;
            case 2:
                viewPagerTabs[2].setBackgroundResource(R.drawable.lay_viewpager_header_press);
                break;

        }
    }
}
