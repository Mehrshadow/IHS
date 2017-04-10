package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import ir.parsansoft.app.ihs.center.adapters.AdapterIoTypesSpinner;

public class ActivityAddNode_IoMadule_Input_Output extends ActivityEnhanced {

    AllViews.CO_d_section_add_node_input_output mAdd_node_input_output;
    AdapterIoTypesSpinner mAdapterIoTypesSpinner;
    private boolean isInput = true;
    List<String> types;
    int node_type;
    int id = 0;

    Database.Node.Struct newNode;
    Database.Node.Struct[] ioNode;


    String inputTypes[] = {G.T.getSentence(1211)};

    String outputTypes[] = {G.T.getSentence(1106)
            , G.T.getSentence(1107)
//            , G.T.getSentence(1104)
//            , G.T.getSentence(1105)
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
            setContentView(R.layout.activity_add_node__io_madual__input__output);
        else
            setContentView(R.layout.activity_add_node__io_madual__input__output);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("NODE_ID")) {
                id = extras.getInt("NODE_ID");
                ioNode = Database.Node.select("ID=" + id);
                G.log("Node ID=" + id);
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
        } catch (Exception e) {
            G.printStackTrace(e);
        }
    }

    private void initializeForm() {
        loadSpinner();
        mAdd_node_input_output.btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInput = true;
                mAdd_node_input_output.btnInput.setHovered(true);
                mAdd_node_input_output.btnOutput.setHovered(false);
                loadSpinner();
            }
        });

        mAdd_node_input_output.btnOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInput = false;
                mAdd_node_input_output.btnInput.setHovered(false);
                mAdd_node_input_output.btnOutput.setHovered(true);
                loadSpinner();
            }
        });

        mAdd_node_input_output.btnInput.setHovered(true);

        mAdd_node_input_output.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveForm()) {
                    if (node_type == -1) {
                        Intent mAdd_node_input_output = new Intent(G.currentActivity, ActivityAddNode_IOModule_SensorType.class);
//                        mAdd_node_input_output.putExtra("NODE_Type", node_type);
//                        mAdd_node_input_output.putExtra("NODE_ID", id);
                        G.currentActivity.startActivity(mAdd_node_input_output);
                        Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                        finish();
                    } else {
                        Intent mAdd_node_input_output = new Intent(G.currentActivity, ActivityAddNode_IoMadule_NodeType.class);
                        mAdd_node_input_output.putExtra("NODE_Type", node_type);
                        mAdd_node_input_output.putExtra("NODE_ID", id);
                        G.currentActivity.startActivity(mAdd_node_input_output);
                        Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                        finish();
                    }
                }
            }
        });

        mAdd_node_input_output.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadSpinner() {
        if (isInput) {
            types = new ArrayList<>();
            for (int i = 0; i < inputTypes.length; i++) {
                types.add(inputTypes[i]);
            }
//            types = inputTypes;
        } else if (!isInput) {
            types = new ArrayList<>();
            for (int i = 0; i < outputTypes.length; i++) {
                types.add(outputTypes[i]);
            }
//            types = outputTypes;
        }
        mAdapterIoTypesSpinner = new AdapterIoTypesSpinner(types);
        mAdd_node_input_output.spnTypes.setAdapter(mAdapterIoTypesSpinner);
        mAdd_node_input_output.spnTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isInput) {
                    switch (position) {
                        case 0:
                            node_type = AllNodes.Node_Type.AIR_CONDITION;
                            break;

                        case 1:
                            node_type = AllNodes.Node_Type.CURTAIN_SWITCH;
                            break;

//                        case 2:
//                            node_type = AllNodes.Node_Type.SIMPLE_DIMMER_1;
//                            break;
//
//                        case 3:
//                            node_type = AllNodes.Node_Type.SIMPLE_DIMMER_2;
//                            break;

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


                } else if (isInput) {
                    switch (position) {
                        case 0:
                            node_type = -1;//   نود سنسور در اکتیویتی بعدی به دیتابس اضاف میشوند
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean saveForm() {
        if (node_type != -1) {// در صورتی که نود سنسور انتخاب شد ،فعلا وارد دیتابیس نشود
            insertFakeNodeToDb();
            return true;
        } else {
            return true;
        }
    }

    private void insertFakeNodeToDb() {
        newNode = new Database.Node.Struct();
        newNode.nodeTypeID = node_type;
        newNode.roomID = AllNodes.myHouseDefaultRoomId;
        newNode.iP = this.ip;
        int newNodeID = AllNodes.AddNewNode(newNode, 1);
        id = newNodeID;
    }

    @Override
    public void translateForm() {
        super.translateForm();
        mAdd_node_input_output.btnInput.setText(G.T.getSentence(853));
        mAdd_node_input_output.btnOutput.setText(G.T.getSentence(854));
        mAdd_node_input_output.btnCancel.setText(G.T.getSentence(102));
        mAdd_node_input_output.btnNext.setText(G.T.getSentence(103));
        mAdd_node_input_output.txtTitle.setText(G.T.getSentence(855));
    }
}
