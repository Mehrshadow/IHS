package ir.parsansoft.app.ihs.center.adapters;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import ir.parsansoft.app.ihs.center.AllNodes;
import ir.parsansoft.app.ihs.center.AllViews.CO_d_fragment_base;
import ir.parsansoft.app.ihs.center.AllViews.CO_d_simple_spinner;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.DialogClass;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DialogNodeSelector extends DialogFragment {

    FragmentPagerAdapter adapter;
    View rootView;
    CO_d_fragment_base lo;
    int selectedSwitchID = 0;
    static Dialog dialog;
    static ViewPager viewPager;
    static DialogClosedListener dcl = null;
    public static int currentWizard = 0;

    public DialogNodeSelector() {

    }

    public void setCurrentWizard(int currentWizard) {
        this.currentWizard = currentWizard;
    }

    @Override
    public void onStart() {

        //dialog.getWindow().setLayout(400, 200);
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = 350;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public void serSelectedSwitchID(int selectedSwitchID) {
        this.selectedSwitchID = selectedSwitchID;
    }

    public interface DialogClosedListener {
        void onSwitchSelected(int SwitchID, String opr, float value);

        void onSwitchSelectedW9(int SwitchID, float value, boolean active, int reverseTime, float preValue);
    }

    public void setonSwitchSelected(DialogClosedListener dialogClosedListener) {
        dcl = dialogClosedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        //dialog = new Dialog(G.currentActivity, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(100, 200);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.d_fragment_base, container, false);
        lo = new CO_d_fragment_base(rootView);
        viewPager = lo.pager;
        if (currentWizard == 3)
            adapter = new AdapterFragment(getChildFragmentManager(), selectedSwitchID);
        else
            adapter = new AdapterFragmentWizard9(getChildFragmentManager(), selectedSwitchID);
        viewPager.setAdapter(adapter);
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
    }

    private static class AdapterFragment extends FragmentPagerAdapter {
        private static int mySectionID = 0;
        private static int myRoomID = 0;
        private static int myNodeID = 0;
        private static int mySwitchID = 0;
        private static Database.Section.Struct[] sections;
        private static Database.Room.Struct[] rooms;
        private static Database.Node.Struct[] nodes;
        private static Database.Switch.Struct[] switches;

        public AdapterFragment(FragmentManager fm, int switchID) {
            super(fm);
            mySwitchID = switchID;
            if (mySwitchID > 0) {
                myNodeID = Database.Switch.select(mySwitchID).nodeID;
                myRoomID = Database.Node.select(myNodeID).roomID;
                mySectionID = Database.Room.select(myRoomID).sectionID;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FragmentSection fsc = new FragmentSection();
                    return fsc;
                case 1:
                    FragmentRoom fr = new FragmentRoom();
                    return fr;
                case 2:
                    FragmentNode fnd = new FragmentNode();
                    return fnd;
                case 3:
                    FragmentSwitch fsw = new FragmentSwitch();
                    return fsw;
                case 4:
                    FragmentSetValue fsv = new FragmentSetValue();
                    return fsv;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }


        public static class FragmentSection extends Fragment {
            CO_d_simple_spinner ldo;
            int selectedIndex = -1;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.d_simple_spinner, container, false);
                ldo = new CO_d_simple_spinner(view);
                ldo.txtTitle.setText(G.T.getSentence(2201));
                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                ldo.btnPositive.setText(G.T.getSentence(103));
                ldo.btnNegative.setText(G.T.getSentence(102));

                sections = Database.Section.select("");
                G.log("load Sections ....");
                if (sections != null && sections.length > 0) {
                    ArrayAdapter<String> adapter;
                    List<String> list;
                    list = new ArrayList<>();
                    for (int i = 0; i < sections.length; i++) {
                        list.add(sections[i].name);
                        if (sections[i].iD == mySectionID)
                            selectedIndex = i;
                        else //add by payam
                            selectedIndex = 0;
                    }
                    adapter = new ArrayAdapter<>(G.context, R.layout.l_spinner_item, list);
                    adapter.setDropDownViewResource(R.layout.l_spinner_item);
                    if (ldo.spnItems != null)
                        ldo.spnItems.setAdapter(adapter);
                    ldo.btnPositive.setVisibility(View.VISIBLE);
                    if (selectedIndex >= 0)
                        ldo.spnItems.setSelection(selectedIndex);
                } else
                    ldo.btnPositive.setVisibility(View.INVISIBLE);

                ldo.spnItems.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedIndex = position;
                        mySectionID = sections[selectedIndex].iD;
                        G.log("Section : " + mySectionID + " - " + sections[selectedIndex].name + " Selected");
                        ldo.btnPositive.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        ldo.btnPositive.setVisibility(View.INVISIBLE);
                    }
                });

                ldo.btnPositive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        G.log("Section : " + mySectionID + " - " + sections[selectedIndex].name + " Clicked Next");
                        viewPager.setCurrentItem(1);
                    }
                });

                ldo.btnNegative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // Close the dialog
                        dialog.dismiss();
                    }
                });
                return view;
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    // load data here
                } else {
                    // fragment is no longer visible
                }
            }
        }

        public static class FragmentRoom extends Fragment {
            CO_d_simple_spinner ldo;
            int selectedIndex = -1;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.d_simple_spinner, container, false);
                ldo = new CO_d_simple_spinner(view);
                ldo.txtTitle.setText(G.T.getSentence(2202));
                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                ldo.btnPositive.setText(G.T.getSentence(103));
                ldo.btnNegative.setText(G.T.getSentence(104));
                ldo.layOptions.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                    }
                });
                return view;
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    rooms = Database.Room.select("SectionID = " + mySectionID);
                    G.log("load Rooms ....");
                    ArrayAdapter<String> adapter;
                    List<String> list = new ArrayList<>();
                    if (rooms != null) {
                        if (rooms.length > 0) {

                            for (int i = 0; i < rooms.length; i++) {
                                list.add(rooms[i].name);
                                if (rooms[i].iD == myRoomID)
                                    selectedIndex = i;
                                else //add by payam
                                    selectedIndex = 0;
                            }
                            adapter = new ArrayAdapter<>(G.context, R.layout.l_spinner_item, list);
                            adapter.setDropDownViewResource(R.layout.l_spinner_item);
                            ldo.spnItems.setAdapter(adapter);
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                            if (selectedIndex >= 0)
                                ldo.spnItems.setSelection(selectedIndex);
                        } else
                            ldo.btnPositive.setVisibility(View.INVISIBLE);
                    } else {
                        list.clear();
                        ldo.btnPositive.setVisibility(View.INVISIBLE);
                        adapter = new ArrayAdapter<>(G.context, R.layout.l_spinner_item, list);
                        adapter.setDropDownViewResource(R.layout.l_spinner_item);
                        ldo.spnItems.setAdapter(adapter);
                    }

                    ldo.spnItems.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedIndex = position;
                            myRoomID = rooms[selectedIndex].iD;
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            ldo.btnPositive.setVisibility(View.INVISIBLE);
                        }
                    });

                    ldo.btnPositive.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            viewPager.setCurrentItem(2);
                        }
                    });

                    ldo.btnNegative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            viewPager.setCurrentItem(0);
                        }
                    });
                } else {
                    G.log("Un Loaded");
                }
            }
        }

        public static class FragmentNode extends Fragment {
            CO_d_simple_spinner ldo;
            int selectedIndex = -1;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.d_simple_spinner, container, false);
                ldo = new CO_d_simple_spinner(view);
                ldo.txtTitle.setText(G.T.getSentence(2203));
                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                ldo.btnPositive.setText(G.T.getSentence(103));
                ldo.btnNegative.setText(G.T.getSentence(104));
                setUserVisibleHint(true);
                return view;
            }


            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                ArrayAdapter<String> adapter;
                List<String> list = new ArrayList<>();
                if (isVisibleToUser) {
                    G.log("DialogNodeSelector : FragmentNode is visible");
                    nodes = Database.Node.select("RoomID=" + myRoomID);
                    G.log("load Nodes ....");
                    if (nodes != null) {
                        if (nodes.length > 0) {
                            for (int i = 0; i < nodes.length; i++) {
                                list.add(nodes[i].name);
                                if (nodes[i].iD == myNodeID)
                                    selectedIndex = i;
                                else //add by payam
                                    selectedIndex = 0;
                            }
                            adapter = new ArrayAdapter<>(G.context, R.layout.l_spinner_item, list);
                            adapter.setDropDownViewResource(R.layout.l_spinner_item);
                            ldo.spnItems.setAdapter(adapter);
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                            if (selectedIndex >= 0)
                                ldo.spnItems.setSelection(selectedIndex);
                        } else
                            ldo.btnPositive.setVisibility(View.INVISIBLE);
                    } else {
                        list.clear();
                        ldo.btnPositive.setVisibility(View.INVISIBLE);
                        adapter = new ArrayAdapter<>(G.context, R.layout.l_spinner_item, list);
                        adapter.setDropDownViewResource(R.layout.l_spinner_item);
                        ldo.spnItems.setAdapter(adapter);
                    }
                    ldo.spnItems.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedIndex = position;
                            myNodeID = nodes[selectedIndex].iD;
                            switches = Database.Switch.select("NodeID=" + myNodeID);
                            if (switches != null && switches.length > 0) {
                                if (switches.length > 1) {
                                    ldo.btnPositive.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            viewPager.setCurrentItem(3);
                                        }
                                    });
                                } else {
                                    try {
                                        mySwitchID = switches[0].iD;
                                        ldo.btnPositive.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View arg0) {
//                                                if (dcl != null)
//                                                    dcl.onSwitchSelected(mySwitchID);
//                                                dialog.dismiss();
                                                mySwitchID = switches[0].iD;
                                                viewPager.setCurrentItem(4);
                                            }
                                        });
                                    } catch (Exception e) {
                                        G.printStackTrace(e);
                                    }
                                }
                                ldo.btnPositive.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            ldo.btnPositive.setVisibility(View.INVISIBLE);
                        }
                    });
                    ldo.btnNegative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            viewPager.setCurrentItem(1);
                        }
                    });

                } else {
                    // fragment is no longer visible
                }
            }
        }

        public static class FragmentSwitch extends Fragment {
            CO_d_simple_spinner ldo;
            int selectedIndex = -1;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.d_simple_spinner, container, false);
                ldo = new CO_d_simple_spinner(view);
                ldo.txtTitle.setText(G.T.getSentence(2203));
                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                ldo.btnPositive.setText(G.T.getSentence(103));
                ldo.btnNegative.setText(G.T.getSentence(104));
                return view;
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    switches = Database.Switch.select("NodeID=" + myNodeID);
                    G.log("load Switches ....");
                    if (switches != null)
                        if (switches.length > 0) {
                            ArrayAdapter<String> adapter;
                            List<String> list;
                            list = new ArrayList<String>();
                            for (int i = 0; i < switches.length; i++) {
                                list.add(switches[i].name);
                                if (switches[i].iD == mySwitchID)
                                    selectedIndex = i;
                            }
                            adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item, list);
                            adapter.setDropDownViewResource(R.layout.l_spinner_item);
                            ldo.spnItems.setAdapter(adapter);
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                            if (selectedIndex >= 0)
                                ldo.spnItems.setSelection(selectedIndex);
                        } else
                            ldo.btnPositive.setVisibility(View.INVISIBLE);

                    ldo.spnItems.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedIndex = position;
                            mySwitchID = switches[selectedIndex].iD;
//                            ldo.btnPositive.setText(G.T.getSentence(101));
//                            ldo.btnPositive.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View arg0) {
//                                    if (dcl != null)
//                                        dcl.onSwitchSelected(mySwitchID);
//                                    dialog.dismiss();
//                                }
//                            });
//                            ldo.btnPositive.setText(G.T.getSentence(101));
                            ldo.btnPositive.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    viewPager.setCurrentItem(4);
                                }
                            });
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            ldo.btnPositive.setVisibility(View.INVISIBLE);
                        }
                    });


                    ldo.btnNegative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            viewPager.setCurrentItem(2);
                        }
                    });
                } else {
                    // fragment is no longer visible
                }
            }
        }

        /**
         * Edit by Payam
         **/
        public static class FragmentSetValue extends Fragment {
            //CO_d_simple_spinner ldo;
            LinearLayout layPerOperandValueRange, layPerOperandValueSelective;
            Spinner spnLogicalSign, spnSelectiveValue;
            TextView txtValue, txtBody, txtTitle, TextView04;
            EditText edtLogicalValue;
            Button btnPositive, btnNegative;
            View view;
            int selectedIndex = -1;
            Database.ValueType.Struct switchValueType;
            List<String> listValueInstances = null;
            Database.ValueTypeInstances.Struct[] valueInstances;
            String[] logicalSigns = {"=", ">", ">=", "<", "<=", "!="};

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                G.log("FragmentSetValue : onCreateView");
                if (G.setting.languageID == 1 || G.setting.languageID == 4)
                    view = inflater.inflate(R.layout.d_simple_spinner_set_value, container, false);
                else
                    view = inflater.inflate(R.layout.d_simple_spinner_set_value_rtl, container, false);

                layPerOperandValueRange = (LinearLayout) view.findViewById(R.id.layPerOperandValueRange);
                layPerOperandValueSelective = (LinearLayout) view.findViewById(R.id.layPerOperandValueSelective);
                spnLogicalSign = (Spinner) view.findViewById(R.id.spnLogicalSign);
                spnSelectiveValue = (Spinner) view.findViewById(R.id.spnSelectiveValue);
                txtValue = (TextView) view.findViewById(R.id.txtValue);
                TextView04 = (TextView) view.findViewById(R.id.TextView04);
                txtBody = (TextView) view.findViewById(R.id.txtBody);
                txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                edtLogicalValue = (EditText) view.findViewById(R.id.edtLogicalValue);
                btnPositive = (Button) view.findViewById(R.id.btnPositive);
                btnNegative = (Button) view.findViewById(R.id.btnNegative);
//                ldo = new CO_d_simple_spinner(view);
//                ldo.txtTitle.setText(G.T.getSentence(2203));
//                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                btnPositive.setText(G.T.getSentence(101));
                btnNegative.setText(G.T.getSentence(104));
                TextView04.setText(G.T.getSentence(501));
                txtValue.setText(G.T.getSentence(501));
                setUserVisibleHint(true);
                return view;
            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                G.log("FragmentSetValue : onCreate");
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    if (view != null) {
                        loadlogicalSigns();
                        final Database.Switch.Struct currentSwitch = Database.Switch.select(mySwitchID);
                        if (currentSwitch != null) {
                            loadValues(currentSwitch.iD);
                            txtTitle.setText(currentSwitch.name);
                            txtBody.setText("");
                            G.log("ScenarioBP >>AllNodes.Switch_Type >> "+currentSwitch.switchType );
                            if (currentSwitch.switchType == AllNodes.Switch_Type.Sensor_NO) {
                                if (dcl != null)
                                    G.log("ScenarioBP >>AllNodes.Switch_Type.Sensor_NO" );
                                    dcl.onSwitchSelected(mySwitchID, "=", 0);
                                dialog.dismiss();
                            }

                            if (currentSwitch.switchType == AllNodes.Switch_Type.Sensor_NC) {
                                if (dcl != null)
                                    G.log("ScenarioBP >>AllNodes.Switch_Type.Sensor_NC" );
                                dcl.onSwitchSelected(mySwitchID, "=", 1);
                                dialog.dismiss();
                            }


                            btnPositive.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    float value;
                                    String logicalSing;
                                    if (layPerOperandValueRange.getVisibility() == View.VISIBLE) {
                                        if (edtLogicalValue.getText().toString().trim().matches("\\d+\\.?\\d*") || edtLogicalValue.getText().toString().trim().matches("\\.\\d+"))
                                            value = Float.parseFloat(edtLogicalValue.getText().toString());
                                        else {
                                            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(576));
                                            return;
                                        }
                                        logicalSing = logicalSigns[(int) spnLogicalSign.getSelectedItemId()].toString();
                                    } else {
                                        if (switchValueType.isNumeric)
                                            value = Float.parseFloat(listValueInstances.get(spnSelectiveValue.getSelectedItemPosition()));
                                        else
                                            value = valueInstances[(int) spnSelectiveValue.getSelectedItemId()].value;
                                        logicalSing = "=";
                                    }


                                    if (dcl != null)
                                        dcl.onSwitchSelected(mySwitchID, logicalSing, value);
                                    dialog.dismiss();

                                }
                            });

                            btnNegative.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    if (switches.length > 1) {
                                        viewPager.setCurrentItem(3);
                                    } else {
                                        viewPager.setCurrentItem(2);
                                    }
                                }
                            });
                        }
                    }

                } else {
                    // fragment is no longer visible
                }
            }

            private void loadValues(int mySwitchID) {
                switchValueType = Database.ValueType.select(Database.SwitchType.select(Database.Switch.select(mySwitchID).switchType).valueTypeID);
                G.log("loadValues(" + mySwitchID + ")");
                if (switchValueType.isSelectable) {
                    G.log("switchValueType.isSelectable = true");
                    layPerOperandValueRange.setVisibility(View.GONE);
                    layPerOperandValueSelective.setVisibility(View.VISIBLE);
                    loadSelectiveValue(switchValueType);
                } else {
                    G.log("switchValueType.isSelectable = true");
                    layPerOperandValueRange.setVisibility(View.VISIBLE);
                    layPerOperandValueSelective.setVisibility(View.GONE);
                }
            }

            private void loadSelectiveValue(Database.ValueType.Struct valueType) {
                G.log("loadSelectiveValue( ID = " + valueType.iD + " )");
                ArrayAdapter<String> adapter = null;

                listValueInstances = new ArrayList<String>();
                if (valueType.isNumeric) {
                    G.log("valueType is Numeric");
                    for (double f = valueType.startValue; f <= valueType.endValue; f += valueType.valueStep) {
                        String s;
                        if (f == (int) f)
                            s = ("" + (int) f);
                        else {
                            BigDecimal src = new BigDecimal(f);
                            BigDecimal a = src.remainder(BigDecimal.ONE);
                            s = "" + a;
                        }
                        listValueInstances.add(s);
                    }
                } else {
                    G.log("valueType is not Numeric");
                    valueInstances = Database.ValueTypeInstances.select("ValueTypeID=" + valueType.iD);
                    for (int i = 0; i < valueInstances.length; i++)
                        listValueInstances.add(G.T.getSentence(valueInstances[i].sentenceID));
                }
                adapter = new ArrayAdapter<String>(G.context.getApplicationContext(), R.layout.l_spinner_item, listValueInstances);
                adapter.setDropDownViewResource(R.layout.l_spinner_item);
                spnSelectiveValue.setAdapter(adapter);
            }

            private void loadlogicalSigns() {
                ArrayAdapter<String> adapter = null;
                List<String> listOperands = null;
                listOperands = new ArrayList<String>();
                if (logicalSigns != null) {
                    for (int i = 0; i < logicalSigns.length; i++)
                        listOperands.add(logicalSigns[i]);
                }
                adapter = new ArrayAdapter<String>(G.context.getApplicationContext(), R.layout.l_spinner_item, listOperands);
                adapter.setDropDownViewResource(R.layout.l_spinner_item);
                spnLogicalSign.setAdapter(adapter);
            }
        }

        /**
         * Edit by Payam
         **/
        public static class FragmentSetValueAndReverse extends Fragment {
            //CO_d_simple_spinner ldo;
            LinearLayout layPerOperandValueRange, layPerOperandValueSelective, layReverse;
            Spinner spnSelectiveValue, spnTimeUnit;
            TextView txtNumericValue, txtSelectiveValue, txtTitle, txtBody, txtReverseTime;
            EditText edtLogicalValue, edtReverseTime;
            Button btnPositive, btnNegative;
            CheckBox chkReverse;
            View view;
            int selectedIndex = -1;
            Database.ValueType.Struct switchValueType;
            List<String> listValueInstances = null;
            Database.ValueTypeInstances.Struct[] valueInstances;
            String[] logicalSigns = {"=", ">", ">=", "<", "<=", "!="};

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                G.log("FragmentSetValue : onCreateView");
                if (G.setting.languageID == 1 || G.setting.languageID == 4)
                    view = inflater.inflate(R.layout.d_simple_spinner_set_value_w9_rtl, container, false);
                else
                    view = inflater.inflate(R.layout.d_simple_spinner_set_value_w9_rtl, container, false);

                layPerOperandValueRange = (LinearLayout) view.findViewById(R.id.layPerOperandValueRange);
                layPerOperandValueSelective = (LinearLayout) view.findViewById(R.id.layPerOperandValueSelective);
                layReverse = (LinearLayout) view.findViewById(R.id.layReverse);
                spnTimeUnit = (Spinner) view.findViewById(R.id.spnTimeUnit);
                spnSelectiveValue = (Spinner) view.findViewById(R.id.spnSelectiveValue);
                txtNumericValue = (TextView) view.findViewById(R.id.txtNumericValue);
                txtSelectiveValue = (TextView) view.findViewById(R.id.txtSelectiveValue);
//                txtBody = (TextView) view.findViewById(R.id.txtBody);
                txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                txtReverseTime = (TextView) view.findViewById(R.id.txtReverseTime);
                edtLogicalValue = (EditText) view.findViewById(R.id.edtLogicalValue);
                edtReverseTime = (EditText) view.findViewById(R.id.edtReverseTime);
                btnPositive = (Button) view.findViewById(R.id.btnPositive);
                btnNegative = (Button) view.findViewById(R.id.btnNegative);
                chkReverse = (CheckBox) view.findViewById(R.id.chkReverse);
//                ldo = new CO_d_simple_spinner(view);
//                ldo.txtTitle.setText(G.T.getSentence(2203));
//                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                btnPositive.setText(G.T.getSentence(101));
                btnNegative.setText(G.T.getSentence(104));
                txtSelectiveValue.setText(G.T.getSentence(501));
                txtNumericValue.setText(G.T.getSentence(501));
                setUserVisibleHint(true);
                return view;
            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                G.log("FragmentSetValue : onCreate");
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    if (view != null) {
                        final Database.Switch.Struct currentSwitch = Database.Switch.select(mySwitchID);
                        if (currentSwitch != null) {
//                            loadValues(currentSwitch.iD);
                            txtTitle.setText(currentSwitch.name);
//                            txtBody.setText("");
                            btnPositive.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            btnNegative.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    if (switches.length > 1) {
                                        viewPager.setCurrentItem(3);
                                    } else {
                                        viewPager.setCurrentItem(2);
                                    }
                                }
                            });
                        }
                    }

                } else {
                    // fragment is no longer visible
                }
            }
        }
    }


    /*******************************************************************************************/
    /*******************************************************************************************/
    /*******************************************************************************************/
    /*******************************************************************************************/
    /*******************************************************************************************/

    private static class AdapterFragmentWizard9 extends FragmentPagerAdapter {
        private static int mySectionID = 0;
        private static int myRoomID = 0;
        private static int myNodeID = 0;
        private static int mySwitchID = 0;
        private static Database.Section.Struct[] sections;
        private static Database.Room.Struct[] rooms;
        private static Database.Node.Struct[] nodes;
        private static Database.Switch.Struct[] switches;
        private static float valueOfSwitch;

        public AdapterFragmentWizard9(FragmentManager fm, int switchID) {
            super(fm);
            mySwitchID = switchID;
            if (mySwitchID > 0) {
                myNodeID = Database.Switch.select(mySwitchID).nodeID;
                myRoomID = Database.Node.select(myNodeID).roomID;
                mySectionID = Database.Room.select(myRoomID).sectionID;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FragmentSection fsc = new FragmentSection();
                    return fsc;
                case 1:
                    FragmentRoom fr = new FragmentRoom();
                    return fr;
                case 2:
                    FragmentNode fnd = new FragmentNode();
                    return fnd;
                case 3:
                    FragmentSwitch fsw = new FragmentSwitch();
                    return fsw;
                case 4:
                    FragmentSetValue fsv = new FragmentSetValue();
                    return fsv;
                case 5:
                    FragmentSetReverse fsr = new FragmentSetReverse();
                    return fsr;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }


        public static class FragmentSection extends Fragment {
            CO_d_simple_spinner ldo;
            int selectedIndex = -1;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.d_simple_spinner, container, false);
                ldo = new CO_d_simple_spinner(view);
                ldo.txtTitle.setText(G.T.getSentence(2201));
                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                ldo.btnPositive.setText(G.T.getSentence(103));
                ldo.btnNegative.setText(G.T.getSentence(102));

                sections = Database.Section.select("");
                G.log("load Sections ....");
                if (sections != null && sections.length > 0) {
                    ArrayAdapter<String> adapter;
                    List<String> list;
                    list = new ArrayList<String>();
                    for (int i = 0; i < sections.length; i++) {
                        list.add(sections[i].name);
                        if (sections[i].iD == mySectionID)
                            selectedIndex = i;
                        else
                            selectedIndex = 0;
                    }
                    adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item, list);
                    adapter.setDropDownViewResource(R.layout.l_spinner_item);
                    if (ldo.spnItems != null)
                        ldo.spnItems.setAdapter(adapter);
                    ldo.btnPositive.setVisibility(View.VISIBLE);
                    if (selectedIndex >= 0)
                        ldo.spnItems.setSelection(selectedIndex);
                } else
                    ldo.btnPositive.setVisibility(View.INVISIBLE);

                ldo.spnItems.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedIndex = position;
                        mySectionID = sections[selectedIndex].iD;
                        G.log("Section : " + mySectionID + " - " + sections[selectedIndex].name + " Selected");
                        ldo.btnPositive.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        ldo.btnPositive.setVisibility(View.INVISIBLE);
                    }
                });

                ldo.btnPositive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        G.log("Section : " + mySectionID + " - " + sections[selectedIndex].name + " Clicked Next");
                        viewPager.setCurrentItem(1);
                    }
                });

                ldo.btnNegative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // Close the dialog
                        dialog.dismiss();
                    }
                });
                return view;
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    // load data here
                } else {
                    // fragment is no longer visible
                }
            }
        }

        public static class FragmentRoom extends Fragment {
            CO_d_simple_spinner ldo;
            int selectedIndex = -1;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.d_simple_spinner, container, false);
                ldo = new CO_d_simple_spinner(view);
                ldo.txtTitle.setText(G.T.getSentence(2202));
                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                ldo.btnPositive.setText(G.T.getSentence(103));
                ldo.btnNegative.setText(G.T.getSentence(104));
                ldo.layOptions.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                    }
                });
                return view;
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    rooms = Database.Room.select("SectionID = " + mySectionID);
                    G.log("load Rooms ....");
                    if (rooms != null)
                        if (rooms.length > 0) {
                            ArrayAdapter<String> adapter;
                            List<String> list;
                            list = new ArrayList<String>();
                            for (int i = 0; i < rooms.length; i++) {
                                list.add(rooms[i].name);
                                if (rooms[i].iD == myRoomID)
                                    selectedIndex = i;
                                else
                                    selectedIndex = 0;
                            }
                            adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item, list);
                            adapter.setDropDownViewResource(R.layout.l_spinner_item);
                            ldo.spnItems.setAdapter(adapter);
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                            if (selectedIndex >= 0)
                                ldo.spnItems.setSelection(selectedIndex);
                        } else
                            ldo.btnPositive.setVisibility(View.INVISIBLE);

                    ldo.spnItems.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedIndex = position;
                            myRoomID = rooms[selectedIndex].iD;
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            ldo.btnPositive.setVisibility(View.INVISIBLE);
                        }
                    });

                    ldo.btnPositive.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            viewPager.setCurrentItem(2);
                        }
                    });

                    ldo.btnNegative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            viewPager.setCurrentItem(0);
                        }
                    });
                } else {
                    G.log("Un Loaded");
                }
            }
        }

        public static class FragmentNode extends Fragment {
            CO_d_simple_spinner ldo;
            int selectedIndex = -1;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.d_simple_spinner, container, false);
                ldo = new CO_d_simple_spinner(view);
                ldo.txtTitle.setText(G.T.getSentence(2203));
                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                ldo.btnPositive.setText(G.T.getSentence(103));
                ldo.btnNegative.setText(G.T.getSentence(104));
                setUserVisibleHint(true);
                return view;
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    nodes = Database.Node.select("RoomID = " + myRoomID);
                    G.log("load Nodes ....");
                    ArrayAdapter<String> adapter;
                    List<String> list = new ArrayList<>();
                    if (nodes != null) {
                        if (nodes.length > 0) {
                            for (int i = 0; i < nodes.length; i++) {
                                list.add(nodes[i].name);
                                if (nodes[i].iD == myNodeID)
                                    selectedIndex = i;
                                else
                                    selectedIndex = 0;
                            }
                            adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item, list);
                            adapter.setDropDownViewResource(R.layout.l_spinner_item);
                            ldo.spnItems.setAdapter(adapter);
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                            if (selectedIndex >= 0)
                                ldo.spnItems.setSelection(selectedIndex);
                        } else
                            ldo.btnPositive.setVisibility(View.INVISIBLE);
                    } else {
                        /** dar surati ke hich*/
                        list.clear();
                        adapter = new ArrayAdapter<>(G.context, R.layout.l_spinner_item, list);
                        adapter.setDropDownViewResource(R.layout.l_spinner_item);
                        ldo.spnItems.setAdapter(adapter);
                        ldo.btnPositive.setVisibility(View.INVISIBLE);
                    }

                    ldo.spnItems.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedIndex = position;
                            myNodeID = nodes[selectedIndex].iD;
                            switches = Database.Switch.select("NodeID=" + myNodeID);
                            if (switches != null && switches.length > 0) {
                                if (switches.length > 1) {
                                    ldo.btnPositive.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            viewPager.setCurrentItem(3);
                                        }
                                    });
                                } else {
                                    try {
                                        mySwitchID = switches[0].iD;
                                        ldo.btnPositive.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View arg0) {
//                                                if (dcl != null)
//                                                    dcl.onSwitchSelected(mySwitchID);
//                                                dialog.dismiss();
                                                mySwitchID = switches[0].iD;
                                                viewPager.setCurrentItem(4);
                                            }
                                        });
                                    } catch (Exception e) {
                                        G.printStackTrace(e);
                                    }
                                }
                                ldo.btnPositive.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            ldo.btnPositive.setVisibility(View.INVISIBLE);
                        }
                    });

                    ldo.btnNegative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            viewPager.setCurrentItem(1);
                        }
                    });
                } else {
                    // fragment is no longer visible
                }
            }
        }

        public static class FragmentSwitch extends Fragment {
            CO_d_simple_spinner ldo;
            int selectedIndex = -1;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.d_simple_spinner, container, false);
                ldo = new CO_d_simple_spinner(view);
                ldo.txtTitle.setText(G.T.getSentence(2203));
                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                ldo.btnPositive.setText(G.T.getSentence(103));
                ldo.btnNegative.setText(G.T.getSentence(104));
                return view;
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    switches = Database.Switch.select("NodeID=" + myNodeID);
                    G.log("load Switches ....");
                    if (switches != null)
                        if (switches.length > 0) {
                            ArrayAdapter<String> adapter;
                            List<String> list;
                            list = new ArrayList<String>();
                            for (int i = 0; i < switches.length; i++) {
                                list.add(switches[i].name);
                                if (switches[i].iD == mySwitchID)
                                    selectedIndex = i;
                                else
                                    selectedIndex = 0;
                            }
                            adapter = new ArrayAdapter<String>(G.context, R.layout.l_spinner_item, list);
                            adapter.setDropDownViewResource(R.layout.l_spinner_item);
                            ldo.spnItems.setAdapter(adapter);
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                            if (selectedIndex >= 0)
                                ldo.spnItems.setSelection(selectedIndex);
                        } else
                            ldo.btnPositive.setVisibility(View.INVISIBLE);

                    ldo.spnItems.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedIndex = position;
                            mySwitchID = switches[selectedIndex].iD;
//                            ldo.btnPositive.setText(G.T.getSentence(101));
//                            ldo.btnPositive.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View arg0) {
//                                    if (dcl != null)
//                                        dcl.onSwitchSelected(mySwitchID);
//                                    dialog.dismiss();
//                                }
//                            });
//                            ldo.btnPositive.setText(G.T.getSentence(101));
                            ldo.btnPositive.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    viewPager.setCurrentItem(4);
                                }
                            });
                            ldo.btnPositive.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            ldo.btnPositive.setVisibility(View.INVISIBLE);
                        }
                    });


                    ldo.btnNegative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            viewPager.setCurrentItem(2);
                        }
                    });
                } else {
                    // fragment is no longer visible
                }
            }
        }

        /**
         * Edit by Payam
         **/
        public static class FragmentSetValue extends Fragment {
            LinearLayout layPerOperandValueRange, layPerOperandValueSelective;
            Spinner spnSelectiveValue;
            TextView txtSelectiveValue, txtBody, txtTitle, txtNumericValue;
            EditText edtLogicalValue;
            Button btnPositive, btnNegative;
            View view;
            int selectedIndex = -1;
            Database.ValueType.Struct switchValueType;
            List<String> listValueInstances = null;
            Database.ValueTypeInstances.Struct[] valueInstances;
            String[] logicalSigns = {"=", ">", ">=", "<", "<=", "!="};

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                G.log("FragmentSetValue : onCreateView");
                if (G.setting.languageID == 1 || G.setting.languageID == 4)
                    view = inflater.inflate(R.layout.d_simple_spinner_set_value_w9, container, false);
                else
                    view = inflater.inflate(R.layout.d_simple_spinner_set_value_w9_rtl, container, false);

                layPerOperandValueRange = (LinearLayout) view.findViewById(R.id.layPerOperandValueRange);
                layPerOperandValueSelective = (LinearLayout) view.findViewById(R.id.layPerOperandValueSelective);
                spnSelectiveValue = (Spinner) view.findViewById(R.id.spnSelectiveValue);
                txtBody = (TextView) view.findViewById(R.id.txtBody);
                txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                txtSelectiveValue = (TextView) view.findViewById(R.id.txtSelectiveValue);
                txtNumericValue = (TextView) view.findViewById(R.id.txtNumericValue);
                edtLogicalValue = (EditText) view.findViewById(R.id.edtLogicalValue);
                btnPositive = (Button) view.findViewById(R.id.btnPositive);
                btnNegative = (Button) view.findViewById(R.id.btnNegative);
//                ldo = new CO_d_simple_spinner(view);
//                ldo.txtTitle.setText(G.T.getSentence(2203));
//                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                btnPositive.setText(G.T.getSentence(103));
                btnNegative.setText(G.T.getSentence(104));
                txtSelectiveValue.setText(G.T.getSentence(501));
                txtNumericValue.setText(G.T.getSentence(501));
                setUserVisibleHint(true);
                return view;
            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                G.log("FragmentSetValue : onCreate");
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    if (view != null) {
                        final Database.Switch.Struct currentSwitch = Database.Switch.select(mySwitchID);
                        if (currentSwitch != null) {
                            loadValues(currentSwitch.iD);
                            txtTitle.setText(currentSwitch.name);
                            txtBody.setText("");

                            btnPositive.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    String logicalSing;
                                    if (layPerOperandValueRange.getVisibility() == View.VISIBLE) {
                                        if (edtLogicalValue.getText().toString().trim().matches("\\d+\\.?\\d*") || edtLogicalValue.getText().toString().trim().matches("\\.\\d+"))
                                            valueOfSwitch = Float.parseFloat(edtLogicalValue.getText().toString());
                                        else {
                                            new DialogClass(G.currentActivity).showOk(G.T.getSentence(152), G.T.getSentence(576));
                                            return;
                                        }
                                    } else {
                                        if (switchValueType.isNumeric)
                                            valueOfSwitch = Float.parseFloat(listValueInstances.get(spnSelectiveValue.getSelectedItemPosition()));
                                        else
                                            valueOfSwitch = valueInstances[(int) spnSelectiveValue.getSelectedItemId()].value;
                                    }

                                    viewPager.setCurrentItem(5);
                                }
                            });

                            btnNegative.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    if (switches.length > 1) {
                                        viewPager.setCurrentItem(3);
                                    } else {
                                        viewPager.setCurrentItem(2);
                                    }
                                }
                            });
                        }
                    }

                } else {
                    // fragment is no longer visible
                }
            }

            private void loadValues(int mySwitchID) {
                switchValueType = Database.ValueType.select(Database.SwitchType.select(Database.Switch.select(mySwitchID).switchType).valueTypeID);
                G.log("loadValues(" + mySwitchID + ")");
                if (switchValueType.isSelectable) {
                    G.log("switchValueType.isSelectable = true");
                    layPerOperandValueRange.setVisibility(View.GONE);
                    layPerOperandValueSelective.setVisibility(View.VISIBLE);
                    loadSelectiveValue(switchValueType);
                } else {
                    G.log("switchValueType.isSelectable = true");
                    layPerOperandValueRange.setVisibility(View.VISIBLE);
                    layPerOperandValueSelective.setVisibility(View.GONE);
                }
            }

            private void loadSelectiveValue(Database.ValueType.Struct valueType) {
                G.log("loadSelectiveValue( ID = " + valueType.iD + " )");
                ArrayAdapter<String> adapter = null;

                listValueInstances = new ArrayList<String>();
                if (valueType.isNumeric) {
                    G.log("valueType is Numeric");
                    for (double f = valueType.startValue; f <= valueType.endValue; f += valueType.valueStep) {
                        String s;
                        if (f == (int) f)
                            s = ("" + (int) f);
                        else {
                            BigDecimal src = new BigDecimal(f);
                            BigDecimal a = src.remainder(BigDecimal.ONE);
                            s = "" + a;
                        }
                        listValueInstances.add(s);
                    }
                } else {
                    G.log("valueType is not Numeric");
                    valueInstances = Database.ValueTypeInstances.select("ValueTypeID=" + valueType.iD);
                    for (int i = 0; i < valueInstances.length; i++)
                        listValueInstances.add(G.T.getSentence(valueInstances[i].sentenceID));
                }
                adapter = new ArrayAdapter<String>(G.context.getApplicationContext(), R.layout.l_spinner_item, listValueInstances);
                adapter.setDropDownViewResource(R.layout.l_spinner_item);
                spnSelectiveValue.setAdapter(adapter);
            }
        }

        /**
         * Edit by Payam
         **/
        public static class FragmentSetReverse extends Fragment {
            //CO_d_simple_spinner ldo;
            LinearLayout layReverse;
            Spinner spnTimeUnit;
            TextView txtTitle, txtBody, txtReverseTime;
            EditText edtReverseTime;
            Button btnPositive, btnNegative;
            CheckBox chkReverse;
            View view;
            int selectedIndex = -1;
            Database.ValueType.Struct switchValueType;
            List<String> listValueInstances = null;
            Database.ValueTypeInstances.Struct[] valueInstances;
            String[] units = {G.T.getSentence(502), G.T.getSentence(503), G.T.getSentence(504)};

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                G.log("FragmentSetValue : onCreateView");
                if (G.setting.languageID == 1 || G.setting.languageID == 4)
                    view = inflater.inflate(R.layout.d_simple_spinner_set_reverse_w9, container, false);
                else
                    view = inflater.inflate(R.layout.d_simple_spinner_set_reverse_w9_rtl, container, false);

                layReverse = (LinearLayout) view.findViewById(R.id.layReverse);
                spnTimeUnit = (Spinner) view.findViewById(R.id.spnTimeUnit);
                txtBody = (TextView) view.findViewById(R.id.txtBody);
                txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                txtReverseTime = (TextView) view.findViewById(R.id.txtReverseTime);
                edtReverseTime = (EditText) view.findViewById(R.id.edtReverseTime);
                txtBody = (TextView) view.findViewById(R.id.txtBody);
                btnPositive = (Button) view.findViewById(R.id.btnPositive);
                btnNegative = (Button) view.findViewById(R.id.btnNegative);
                chkReverse = (CheckBox) view.findViewById(R.id.chkReverse);
//                ldo = new CO_d_simple_spinner(view);
//                ldo.txtTitle.setText(G.T.getSentence(2203));
//                ldo.txtBody.setText(G.T.getSentence(512).replace("[1]", ldo.txtTitle.getText()));
                layReverse.setVisibility(View.INVISIBLE);
                btnPositive.setText(G.T.getSentence(101));
                btnNegative.setText(G.T.getSentence(104));
                txtReverseTime.setText(G.T.getSentence(562));
                chkReverse.setText(G.T.getSentence(561));
                setUserVisibleHint(true);
                return view;
            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                G.log("FragmentSetValue : onCreate");
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    if (view != null) {
                        final Database.Switch.Struct currentSwitch = Database.Switch.select(mySwitchID);
                        if (currentSwitch != null) {
                            txtTitle.setText(currentSwitch.name);
                            txtBody.setText("");
                            loadUnits();
                            chkReverse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton arg0, boolean newState) {
                                    if (newState)
                                        layReverse.setVisibility(View.VISIBLE);
                                    else
                                        layReverse.setVisibility(View.INVISIBLE);
                                }
                            });


                            btnPositive.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    int reverseTime = 0;
                                    if (chkReverse.isChecked()) {
                                        int unitIndex = spnTimeUnit.getSelectedItemPosition();
                                        try {
                                            reverseTime = Integer.parseInt(edtReverseTime.getText().toString().trim());
                                            reverseTime = (int) (reverseTime * Math.pow(60, unitIndex));
                                        } catch (Exception e) {
                                            reverseTime = 0;
                                            chkReverse.setChecked(false);
                                            G.printStackTrace(e);
                                        }
                                        G.log("Reverse time :" + reverseTime);
                                    }


                                    if (dcl != null)
                                        dcl.onSwitchSelectedW9(mySwitchID, valueOfSwitch, false, reverseTime, 0);
                                    dialog.dismiss();

                                }
                            });

                            btnNegative.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    if (switches.length > 1) {
                                        viewPager.setCurrentItem(5);
                                    } else {
                                        viewPager.setCurrentItem(4);
                                    }
                                }
                            });
                        }
                    }

                } else {
                    // fragment is no longer visible
                }
            }


            private void loadUnits() {
                ArrayAdapter<String> adapter = null;
                List<String> listOperands = null;
                listOperands = new ArrayList<String>();
                if (units != null) {
                    for (int i = 0; i < units.length; i++)
                        listOperands.add(units[i]);
                }
                adapter = new ArrayAdapter<String>(G.context.getApplicationContext(), R.layout.l_spinner_item, listOperands);
                adapter.setDropDownViewResource(R.layout.l_spinner_item);
                if (view != null)
                    spnTimeUnit.setAdapter(adapter);
            }
        }
    }


}
