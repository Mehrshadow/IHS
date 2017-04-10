package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.adapters.AdapterIoTypesSpinner;
import ir.parsansoft.app.ihs.center.adapters.AdapterSensorPortsSpinner;

import static ir.parsansoft.app.ihs.center.Database.Switch.select;


public class ActivityAddNode_IOModule_SensorType extends ActivityEnhanced implements CompoundButton.OnCheckedChangeListener {


//    private Database.Node.Struct[] nodes;

    Database.Node.Struct newNode;


    private int sensorPort = 0;
    private int sensorModel; /*= AllNodes.Switch_Type.Sensor_NC;*/
    private int sensorType = AllNodes.Node_Type.Sensor_SMOKE;
    int nodeId;

    private String sensorName = "";
    Database.Switch.Struct[] switchItems;


    private AdapterIoTypesSpinner mAdapterIoTypesSpinnerToPorts;
    private AdapterIoTypesSpinner mAdapterIoTypesSpinnerToModel;
    private AdapterIoTypesSpinner mAdapterIoTypesSpinnerToType;
    AllViews.CO_d_section_add_node_Sensor fw;

    AdapterSensorPortsSpinner adapterSensorPorts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        G.currentActivity = this;
        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.f_section_add_node_sensor);
        else
            setContentView(R.layout.f_section_add_node_sensor_rtl);

        setSideBarVisiblity(false);
        G.log("hide Sidebar");
        fw = new AllViews.CO_d_section_add_node_Sensor(this);

        try {
//            Bundle extras = getIntent().getExtras();
//            if (extras != null) {
//                if (extras.containsKey("NODE_ID")) {// Sensor ID
//                    nodeId = extras.getInt("NODE_ID");
//                    nodes = Database.Node.select("ID=" + nodeId + " LIMIT 1");//Sensor is fetched
//                    switchItems = select("NodeID=" + nodes[0].iD);
//
//                }
//            }

//            if (nodes == null) {
//                fw.btnNext.setVisibility(View.INVISIBLE);
//                return;
//            }
            fw.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (saveForm()) {
                        Intent fw4 = new Intent(G.currentActivity, ActivityAddNode_IoMadule_SelectPlace.class);// now go to add module wizard...
                        fw4.putExtra("NODE_ID", nodeId);
                        G.currentActivity.startActivity(fw4);
                        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                        finish();
                    } else {
                        new DialogClass(G.currentActivity).showOk(G.T.getSentence(201), G.T.getSentence(856));
                    }

                }
            });
            fw.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Database.Node.delete(nodeId);
                    Database.Switch.delete("NodeID=" + nodeId);
                    finish();
                    Animation.doAnimation(Animation.Animation_Types.FADE);
                }
            });
            fw.btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in_out = new Intent(G.currentActivity, ActivityAddNode_IoMadule_Input_Output.class);
                    Database.Node.delete(nodeId);
                    Database.Switch.delete("NodeID=" + nodeId);
                    G.currentActivity.startActivity(in_out);
                    Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadPortsSpinner();
        loadSensorModelSpinner();
        loadSensorTypeSpinner();
        translateForm();
        initializeForm();
    }


    private void initializeForm() {
        fw.lblSensorName.setText(G.T.getSentence(1208));
        fw.lblSensorType.setText(G.T.getSentence(1209));
        fw.lblSensorPort.setText(G.T.getSentence(1210));
        fw.lblSensorModel.setText(G.T.getSentence(1216));
    }

    private List<String> getAvailablePorts() {
        List<String> availablePorts;
        availablePorts = new ArrayList<>();
        availablePorts.add("13");
        availablePorts.add("14");
        availablePorts.add("15");
        availablePorts.add("16");
        availablePorts.add("17");
        availablePorts.add("18");

        Database.Switch.Struct[] fakeswitches = Database.Switch.select("isIOModuleSwitch=" + 1);
        if (fakeswitches != null) {
            for (int i = 0; i < fakeswitches.length; i++) {
                if (checkPorts(fakeswitches[i])) {
                    availablePorts.remove(String.valueOf(fakeswitches[i].IOModulePort));
                }
            }
        }
        if (availablePorts.size() == 0) {
            Intent in_out = new Intent(G.currentActivity, ActivityAddNode_IoMadule_Input_Output.class);// now go to add module wizard...
            in_out.putExtra("NODE_ID", nodeId);
            G.currentActivity.startActivity(in_out);
            finish();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new DialogClass(G.currentActivity).showOk(G.T.getSentence(201), G.T.getSentence(857));
                }
            }, 500);
        }
        sensorPort = Integer.valueOf(availablePorts.get(0));
        return availablePorts;
    }

    private void loadPortsSpinner() {
        try {
            mAdapterIoTypesSpinnerToPorts = new AdapterIoTypesSpinner(getAvailablePorts());
            fw.spnSensorPort.setAdapter(mAdapterIoTypesSpinnerToPorts);
            fw.spnSensorPort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sensorPort = Integer.valueOf(getAvailablePorts().get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void loadSensorModelSpinner() {
        try {
            List<String> sensorModels = new ArrayList<>();

            sensorModels.add("NC");
            sensorModels.add("NO");

            mAdapterIoTypesSpinnerToModel = new AdapterIoTypesSpinner(sensorModels);
            fw.spnSensorModel.setAdapter(mAdapterIoTypesSpinnerToModel);
            fw.spnSensorModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            sensorModel = AllNodes.Switch_Type.Sensor_NC;
                            break;
                        case 1:
                            sensorModel = AllNodes.Switch_Type.Sensor_NO;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadSensorTypeSpinner() {
        try {
            List<String> sensorTypes = new ArrayList<>();

            sensorTypes.add(G.T.getSentence(1217));
            sensorTypes.add(G.T.getSentence(1218));

            mAdapterIoTypesSpinnerToType = new AdapterIoTypesSpinner(sensorTypes);
            fw.spnSensorType.setAdapter(mAdapterIoTypesSpinnerToType);
            fw.spnSensorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            sensorType = AllNodes.Node_Type.Sensor_SMOKE;
                            break;
                        case 1:
                            sensorType = AllNodes.Node_Type.Sensor_Magnetic;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkPorts(Database.Switch.Struct fakeswitches) {
        if (fakeswitches.IOModulePort > 12) {
            if (
                    fakeswitches.IOModulePort == 13 ||
                            fakeswitches.IOModulePort == 14 ||
                            fakeswitches.IOModulePort == 15 ||
                            fakeswitches.IOModulePort == 16 ||
                            fakeswitches.IOModulePort == 17 ||
                            fakeswitches.IOModulePort == 18
                    ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void translateForm() {
        super.translateForm();
        fw.btnCancel.setText(G.T.getSentence(102));
        fw.btnNext.setText(G.T.getSentence(103));
        fw.txtTitle.setText(G.T.getSentence(859));
        fw.btnBack.setText(G.T.getSentence(104));
//        fw.lblMyHouse.setText(G.T.getSentence(849));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE);
    }

    private boolean saveForm() {


        try {
            sensorName = fw.etSensorName.getText().toString();
            if (sensorPort == 0) {
                return false;
            }
            if (sensorModel == 0) {
                return false;
            }
            if (sensorName.equals("")) {
                return false;
            }
            insertFakeNodeToDb();

            switchItems = select("NodeID=" + nodeId);

            switchItems[0].isIOModuleSwitch = 1;
            switchItems[0].name = sensorName;
            switchItems[0].switchType = sensorModel;
            if (sensorModel == AllNodes.Switch_Type.Sensor_NC) {
                switchItems[0].value = 0;
            } else if (sensorModel == AllNodes.Switch_Type.Sensor_NO) {
                switchItems[0].value = 1;
            }
            switchItems[0].IOModulePort = sensorPort;
            Database.Switch.edit(switchItems[0]);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void insertFakeNodeToDb() {
        String ip = "";
        try {
            Database.Node.Struct[] IONode = Database.Node.select("nodeTypeID = 9");// Io ماژول را پیدا میکنیم

            if (IONode != null)
                ip = IONode[0].iP;
        } catch (Exception e) {
            G.printStackTrace(e);
        }
        newNode = new Database.Node.Struct();
        newNode.nodeTypeID = sensorType;
        newNode.roomID = AllNodes.myHouseDefaultRoomId;
        newNode.iP = ip;
        int newNodeID = AllNodes.AddNewNode(newNode, 1);
        nodeId = newNodeID;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        changeSpinnersVisibility(isChecked);
    }
}
