package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.adapters.AdapterIoTypesSpinner;

public class ActivityAddNode_IoModule_Device_Select extends ActivityEnhanced {

    AllViews.CO_d_section_add_node_input_output mAdd_node_input_output;
    AdapterIoTypesSpinner mAdapterIoTypesSpinner;
    List<String> types;
    int node_type;
    int ioModuleID = 0;
    int deviceID = 0;

    Database.Node.Struct newNode;
    Database.Node.Struct[] ioNode;

    String outputTypes[] = {G.T.getSentence(1106)
            , G.T.getSentence(1107)
            , G.T.getSentence(1101)
            , G.T.getSentence(1102)
            , G.T.getSentence(1103)
            , G.T.getSentence(1108)
    };
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (G.setting.languageID == 1 || G.setting.languageID == 4)
            setContentView(R.layout.activity_add_node__io_module__device_select);
        else
            setContentView(R.layout.activity_add_node__io_module__device_select);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("IO_NODE_ID")) {
                ioModuleID = extras.getInt("IO_NODE_ID");
                deviceID = extras.getInt("DEVICE_NODE_ID");
                ioNode = Database.Node.select("iD=" + ioModuleID);
                G.log("Node ID=" + ioModuleID);
            }
        }

        setSideBarVisiblity(false);
        G.log("hide Sidebar");
        mAdd_node_input_output = new AllViews.CO_d_section_add_node_input_output(this);

        translateForm();
        initializeForm();
        try {
            if (ioNode != null)
                ip = ioNode[0].iP;

//            if (extras != null && extras.getBoolean("Delete_Last_Node")) {
//                Database.Node.delete(deviceID);
//
//                Database.Switch.delete("nodeID = " + deviceID);
//            }
        } catch (Exception e) {
            G.printStackTrace(e);
        }
    }

    private void initializeForm() {
        loadSpinner();

        mAdd_node_input_output.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (saveForm()) {
                Intent mAdd_node_input_output = new Intent(G.currentActivity, ActivityAddNode_IoMadule_NodeType.class);
                mAdd_node_input_output.putExtra("NODE_Type", node_type);
                mAdd_node_input_output.putExtra("IO_NODE_ID", ioModuleID);
//                mAdd_node_input_output.putExtra("DEVICE_NODE_ID", deviceID);
                G.currentActivity.startActivity(mAdd_node_input_output);
                Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                finish();
//                }
            }
        });

        mAdd_node_input_output.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        G.currentActivity = this;
    }

    private void loadSpinner() {
        types = new ArrayList<>();
        for (int i = 0; i < outputTypes.length; i++) {
            types.add(outputTypes[i]);
        }

        mAdapterIoTypesSpinner = new AdapterIoTypesSpinner(types);
        mAdd_node_input_output.spnTypes.setAdapter(mAdapterIoTypesSpinner);
        mAdd_node_input_output.spnTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        node_type = AllNodes.Node_Type.AIR_CONDITION;
                        break;

                    case 1:
                        node_type = AllNodes.Node_Type.CURTAIN_SWITCH;
                        break;

                    case 2:
                        node_type = AllNodes.Node_Type.SIMPLE_SWITCH_1;
                        break;

                    case 3:
                        node_type = AllNodes.Node_Type.SIMPLE_SWITCH_2;
                        break;

                    case 4:
                        node_type = AllNodes.Node_Type.SIMPLE_SWITCH_3;
                        break;
                    case 5:
                        node_type = AllNodes.Node_Type.WC_SWITCH;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

//    private boolean saveForm() {
//        try {
//            insertFakeNodeToDb();
//            return true;
//        } catch (Exception e) {
//            G.printStackTrace(e);
//            return false;
//        }
//    }

//    private void insertFakeNodeToDb() {
//        newNode = new Database.Node.Struct();
//        newNode.nodeTypeID = node_type;
//        newNode.roomID = AllNodes.myHouseDefaultRoomId;
//        newNode.iP = ioNode[0].iP;
//        int newNodeID = AllNodes.AddNewNode(newNode, 1);
//        deviceID = newNodeID;
//    }

    @Override
    public void translateForm() {
        super.translateForm();
        mAdd_node_input_output.btnCancel.setText(G.T.getSentence(102));
        mAdd_node_input_output.btnNext.setText(G.T.getSentence(103));
        mAdd_node_input_output.txtTitle.setText(G.T.getSentence(855));
    }
}
