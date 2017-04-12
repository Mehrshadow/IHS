package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityAddNode_IoMadule_Input_Output extends ActivityEnhanced {

    AllViews.CO_d_section_add_node_input_output mAdd_node_input_output;
    int node_type;
    int ioModuleId = 0;

    Database.Node.Struct[] ioNode;

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
                ioModuleId = extras.getInt("NODE_ID");
                ioNode = Database.Node.select("ID=" + ioModuleId);
                G.log("Node ID=" + ioModuleId);
            }
        }

        setSideBarVisiblity(false);
        G.log("hide Sidebar");
        mAdd_node_input_output = new AllViews.CO_d_section_add_node_input_output(this);

        translateForm();
        initializeForm();
    }

    private void initializeForm() {
        mAdd_node_input_output.btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mAdd_node_input_output = new Intent(G.currentActivity, ActivityAddNode_IOModule_SensorType.class);
                mAdd_node_input_output.putExtra("NODE_Type", node_type);
                mAdd_node_input_output.putExtra("IO_NODE_ID", ioModuleId);
                G.currentActivity.startActivity(mAdd_node_input_output);
                Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                finish();
            }
        });

        mAdd_node_input_output.btnOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mAdd_node_input_output = new Intent(G.currentActivity, ActivityAddNode_IoModule_Device_Select.class);
                mAdd_node_input_output.putExtra("NODE_Type", node_type);
                mAdd_node_input_output.putExtra("IO_NODE_ID", ioModuleId);
                G.currentActivity.startActivity(mAdd_node_input_output);
                Animation.doAnimation(Animation.Animation_Types.FADE_SLIDE_LEFTRIGHT_RIGHT);
                finish();
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
    public void translateForm() {
        super.translateForm();
        mAdd_node_input_output.btnInput.setText(G.T.getSentence(853));
        mAdd_node_input_output.btnOutput.setText(G.T.getSentence(854));
        mAdd_node_input_output.btnCancel.setText(G.T.getSentence(102));
        mAdd_node_input_output.txtTitle.setText(G.T.getSentence(855));
    }
}
