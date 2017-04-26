package ir.parsansoft.app.ihs.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import ir.parsansoft.app.ihs.center.Animation.Animation_Types;
import ir.parsansoft.app.ihs.center.ModuleWebservice.WebServiceListener;
import ir.parsansoft.app.ihs.center.components.AssetsManager;


public class ActivityAddNode_w4 extends ActivityEnhanced {

    Database.Node.Struct node;
    Button btnNext, btnCancel, btnBack;
    TextView txtTitle, txtSerial, txtRegDate, txtExpDate;
    ImageView imgIcon;
    int id = 0;
    int ioNodeId = 0;
    int sensorNodeId = 0;
    int deviceNodeId = 0;
    String expDate;
    private int node_type;
    private boolean isInEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_section_add_node_w4);
        setSideBarVisiblity(false);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSerial = (TextView) findViewById(R.id.txtSerial);
        txtRegDate = (TextView) findViewById(R.id.txtRegDate);
        txtExpDate = (TextView) findViewById(R.id.txtExpDate);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("NODE_ID") || extras.containsKey("IO_NODE_ID")) {
                id = extras.getInt("NODE_ID");
                isInEditMode = extras.getBoolean("EDIT_MODE");


                ioNodeId = extras.getInt("IO_NODE_ID");
                sensorNodeId = extras.getInt("SENSOR_NODE_ID");
                deviceNodeId = extras.getInt("DEVICE_NODE_ID");
                node_type = extras.getInt("NODE_Type");


                //
                if (id == 0) {// yani az node majazi umadim
                    if (sensorNodeId != 0) {//node majazio sensor ast
                        node = Database.Node.select(sensorNodeId);
                    } else if (deviceNodeId != 0) {//node majazio device ast
                        node = Database.Node.select(deviceNodeId);
                    }

                } else {//az node vaghei umADIM
                    node = Database.Node.select(id);
                }


                G.log("Node ID=" + id);
            }
            if (node != null) {
                loadExpDate();
                txtTitle.setText(G.T.getSentence(127) + "\n" + G.T.getSentence(128) + "  :  " + node.name);
                imgIcon.setImageBitmap(AssetsManager.getBitmap(G.context, G.DIR_ICONS_NODES + "/" + node.icon));
                String strSerial = "";
                if (node.parentNodeId != 0) {
                    strSerial = G.T.getSentence(860) + "\n";
                    Database.Switch.Struct[] currentswitch = Database.Switch.select("nodeID =" + node.iD);
                    for (int i = 0; i < currentswitch.length; i++) {
                        // choon tooye db baraye sensor port haye 13-16 save shode
                        // va mikhaim b karbar az 1-6 neshoon bedim
                        // bayad -12 konim
                        // baraye node haye khorooji ham choon az 3 shoroo shode
                        // bayad -2 beshe

                        if (node.nodeTypeID == AllNodes.Node_Type.Sensor_Magnetic || node.nodeTypeID == AllNodes.Node_Type.Sensor_SMOKE)
                            strSerial += " " + (currentswitch[i].IOModulePort - 12);
                        else
                            strSerial += " " + (currentswitch[i].IOModulePort - 2);
                    }
                    strSerial += "\n" + G.T.getSentence(861);
                } else {
                    strSerial = node.iP;
                    strSerial += "\n" + G.T.getSentence(129) + " :   " + node.serialNumber;
                    strSerial += "\n" + G.T.getSentence(130) + " :   " + G.T.getSentence(Database.NodeType.select(node.nodeTypeID).nameSentenceID);
                    strSerial += "\n" + G.T.getSentence(131) + " :   " + node.osVer;
                }
                txtSerial.setText(strSerial);
                txtRegDate.setText(G.T.getSentence(132) + " :   " + node.regDate);
                txtExpDate.setText(G.T.getSentence(133) + " :   " + node.expDate);

            }
        }
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // age darim edit mikonim dg nabayad berim too select place
                // bayad bargardim b W3
                // vaghti too edit hastim dg data nadarim pass bedim too selectPlace
                // SelectPlace dokme back dare va age bezanim dg chizi poshtesh nis va crash mikone
                if (node.parentNodeId == 0 || isInEditMode) {
                    Intent fw3 = new Intent(G.currentActivity, ActivityAddNode_w3.class);
                    fw3.putExtra("NODE_ID", id);
                    fw3.putExtra("EDIT_MODE", isInEditMode);
//                    fw3.putExtra("SENSOR_NODE_ID", sensorNodeId);
//                    fw3.putExtra("IO_NODE_ID", ioModuleID);
//                    fw3.putExtra("NODE_Type", node_type);
                    G.currentActivity.startActivity(fw3);
                }
                else {
                    Intent fw3 = new Intent(G.currentActivity, ActivityAddNode_IoMadule_SelectPlace.class);
                    fw3.putExtra("DEVICE_NODE_ID", id);
                    fw3.putExtra("SENSOR_NODE_ID", sensorNodeId);
                    fw3.putExtra("IO_NODE_ID", ioNodeId);
                    fw3.putExtra("NODE_Type", node_type);
                    G.currentActivity.startActivity(fw3);
                }
                Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
                finish();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                Animation.doAnimation(Animation_Types.FADE);
            }
        });
        translateForm();
    }

    @Override
    public void translateForm() {
        super.translateForm();
        btnCancel.setText(G.T.getSentence(102));
        btnBack.setText(G.T.getSentence(104));
        btnNext.setText(G.T.getSentence(121));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animation.doAnimation(Animation_Types.FADE_SLIDE_LEFTRIGHT_LEFT);
    }


    private void loadExpDate() {
        final ModuleWebservice mw = new ModuleWebservice();
        mw.addParameter("rt", "getNodeWarentyDate");
        mw.addParameter("CustomerID", "" + G.setting.customerID);
        mw.addParameter("ExKey", G.setting.exKey);
        mw.addParameter("SerialNumber", node.serialNumber);
        mw.setListener(new WebServiceListener() {

            @Override
            public void onFail(int statusCode) {
                G.HANDLER.post(new Runnable() {

                    @Override
                    public void run() {
                        txtExpDate.setText("");
                    }
                });
            }

            @Override
            public void onDataReceive(String data, boolean cached) {
                try {
                    G.log("check warrenty = " + data);
                    JSONObject jo = (new JSONArray(data)).getJSONObject(0);
                    node.expDate = jo.getString("ExpDate");
                    node.regDate = jo.getString("RegDate");
                    Database.Node.edit(node);
                    G.HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            txtExpDate.setText(G.T.getSentence(133) + " :   " + node.expDate);
                        }
                    });
                } catch (Exception e) {
                    G.HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            txtExpDate.setText("...");
                        }
                    });
                    G.printStackTrace(e);
                }
            }
        });
        mw.read();
    }


}
