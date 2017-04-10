package ir.parsansoft.app.ihs.center;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import ir.parsansoft.app.ihs.center.components.AsteriskPasswordTransformationMethod;
import ir.parsansoft.app.ihs.center.components.HorizontialListView;
import ir.parsansoft.app.ihs.center.components.TimePickerComponent;

import static ir.parsansoft.app.ihs.center.R.id.btnCancel;

public class AllViews {
    public static final class CO_activity_main {
        public LinearLayout laybtnFavorites;
        public ImageView imgFavorites;
        public TextView txtFavorites;
        public LinearLayout laybtnDiveces;
        public ImageView imgDevices;
        public TextView txtDevices;
        public LinearLayout laybtnScenario;
        public ImageView imgScenario;
        public TextView txtScenario;
        public LinearLayout laybtnSetting;
        public ImageView imgSetting;
        public TextView txtSetting;

        public CO_activity_main(Context context) {
            Activity activity = (Activity) context;
            laybtnFavorites = (LinearLayout) activity.findViewById(R.id.laybtnFavorites);
            txtFavorites = (TextView) activity.findViewById(R.id.txtFavorites);
            laybtnDiveces = (LinearLayout) activity.findViewById(R.id.laybtnDiveces);
            txtDevices = (TextView) activity.findViewById(R.id.txtDevices);
            laybtnScenario = (LinearLayout) activity.findViewById(R.id.laybtnScenario);
            txtScenario = (TextView) activity.findViewById(R.id.txtScenario);
            laybtnSetting = (LinearLayout) activity.findViewById(R.id.laybtnSetting);
            txtSetting = (TextView) activity.findViewById(R.id.txtSetting);
            intializeVariables();
        }

        public CO_activity_main(Dialog dlg) {
            laybtnFavorites = (LinearLayout) dlg.findViewById(R.id.laybtnFavorites);
            txtFavorites = (TextView) dlg.findViewById(R.id.txtFavorites);
            laybtnDiveces = (LinearLayout) dlg.findViewById(R.id.laybtnDiveces);
            txtDevices = (TextView) dlg.findViewById(R.id.txtDevices);
            laybtnScenario = (LinearLayout) dlg.findViewById(R.id.laybtnScenario);
            txtScenario = (TextView) dlg.findViewById(R.id.txtScenario);
            laybtnSetting = (LinearLayout) dlg.findViewById(R.id.laybtnSetting);
            txtSetting = (TextView) dlg.findViewById(R.id.txtSetting);
            intializeVariables();

        }

        public CO_activity_main(View view) {
            laybtnFavorites = (LinearLayout) view.findViewById(R.id.laybtnFavorites);
            txtFavorites = (TextView) view.findViewById(R.id.txtFavorites);
            laybtnDiveces = (LinearLayout) view.findViewById(R.id.laybtnDiveces);
            txtDevices = (TextView) view.findViewById(R.id.txtDevices);
            laybtnScenario = (LinearLayout) view.findViewById(R.id.laybtnScenario);
            txtScenario = (TextView) view.findViewById(R.id.txtScenario);
            laybtnSetting = (LinearLayout) view.findViewById(R.id.laybtnSetting);
            txtSetting = (TextView) view.findViewById(R.id.txtSetting);
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_c_comp_icon_selector {
        public ImageView imgIconSelectorIndicater;

        public CO_c_comp_icon_selector(Context context) {
            Activity activity = (Activity) context;
            imgIconSelectorIndicater = (ImageView) activity.findViewById(R.id.imgIconSelectorIndicater);
            intializeVariables();

        }

        public CO_c_comp_icon_selector(Dialog dlg) {
            imgIconSelectorIndicater = (ImageView) dlg.findViewById(R.id.imgIconSelectorIndicater);
            intializeVariables();

        }

        public CO_c_comp_icon_selector(View view) {
            imgIconSelectorIndicater = (ImageView) view.findViewById(R.id.imgIconSelectorIndicater);
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_c_comp_icon_selector_popup {
        public TextView d_section_section_l2r_txtName;
        public GridView grdIcons;

        public CO_c_comp_icon_selector_popup(Context context) {
            Activity activity = (Activity) context;
            grdIcons = (GridView) activity.findViewById(R.id.grdIcons);
            intializeVariables();

        }

        public CO_c_comp_icon_selector_popup(Dialog dlg) {
            grdIcons = (GridView) dlg.findViewById(R.id.grdIcons);
            intializeVariables();

        }

        public CO_c_comp_icon_selector_popup(View view) {
            grdIcons = (GridView) view.findViewById(R.id.grdIcons);
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_d_fragment_base {
        public ViewPager pager;

        public CO_d_fragment_base(Context context) {
            Activity activity = (Activity) context;
            pager = (ViewPager) activity.findViewById(R.id.pager);
            intializeVariables();

        }

        public CO_d_fragment_base(Dialog dlg) {
            pager = (ViewPager) dlg.findViewById(R.id.pager);
            intializeVariables();

        }

        public CO_d_fragment_base(View view) {
            pager = (ViewPager) view.findViewById(R.id.pager);
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_f_section_add_node_w1 {
        public LinearLayout layOptions;
        public LinearLayout layHeadr;
        public TextView txtTitle;
        public LinearLayout layStart;
        public TextView txtInstruction;
        public TextView txtStatus;
        public Button btnStart;
        public LinearLayout layProgress;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView txtLanguage;

        public CO_f_section_add_node_w1(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            layHeadr = (LinearLayout) activity.findViewById(R.id.layHeadr);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            layStart = (LinearLayout) activity.findViewById(R.id.layStart);
            txtInstruction = (TextView) activity.findViewById(R.id.txtInstruction);
            txtStatus = (TextView) activity.findViewById(R.id.txtStatus);
            btnStart = (Button) activity.findViewById(R.id.btnStart);
            layProgress = (LinearLayout) activity.findViewById(R.id.layProgress);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            txtLanguage = (TextView) activity.findViewById(R.id.txtLanguage);
            intializeVariables();

        }

        public CO_f_section_add_node_w1(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            layHeadr = (LinearLayout) dlg.findViewById(R.id.layHeadr);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            layStart = (LinearLayout) dlg.findViewById(R.id.layStart);
            txtInstruction = (TextView) dlg.findViewById(R.id.txtInstruction);
            txtStatus = (TextView) dlg.findViewById(R.id.txtStatus);
            btnStart = (Button) dlg.findViewById(R.id.btnStart);
            layProgress = (LinearLayout) dlg.findViewById(R.id.layProgress);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            txtLanguage = (TextView) dlg.findViewById(R.id.txtLanguage);
            intializeVariables();

        }

        public CO_f_section_add_node_w1(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            layHeadr = (LinearLayout) view.findViewById(R.id.layHeadr);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            layStart = (LinearLayout) view.findViewById(R.id.layStart);
            txtInstruction = (TextView) view.findViewById(R.id.txtInstruction);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            btnStart = (Button) view.findViewById(R.id.btnStart);
            layProgress = (LinearLayout) view.findViewById(R.id.layProgress);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            txtLanguage = (TextView) view.findViewById(R.id.txtLanguage);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_d_section_add_node_w2 {
        public LinearLayout layOptions;
        public ListView listView1;
        public ScrollView scrollView1;
        public LinearLayout laySection;
        public TextView lblSection;
        public Spinner spnSections;
        public LinearLayout layRoom;
        public TextView lblRoom;
        public Spinner spnRooms;
        public ir.parsansoft.app.ihs.center.components.ComIconSelector icnNodeIcon;
        public TextView lblName;
        public EditText edtNodeName;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView textView1;
        public TextView txtTitle;
        public Button btnNext, btnDelete;
        public CheckBox checkBoxMyHouse;
        public TextView lblMyHouse;

        public CO_d_section_add_node_w2(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            listView1 = (ListView) activity.findViewById(R.id.lstLanguage);
            scrollView1 = (ScrollView) activity.findViewById(R.id.scrollView1);
            laySection = (LinearLayout) activity.findViewById(R.id.laySection);
            lblSection = (TextView) activity.findViewById(R.id.lblSection);
            spnSections = (Spinner) activity.findViewById(R.id.spnSections);
            layRoom = (LinearLayout) activity.findViewById(R.id.layRoom);
            lblRoom = (TextView) activity.findViewById(R.id.lblRoom);
            spnRooms = (Spinner) activity.findViewById(R.id.spnRooms);
            icnNodeIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) activity.findViewById(R.id.icnNodeIcon);
            lblName = (TextView) activity.findViewById(R.id.txtUsername);
            edtNodeName = (EditText) activity.findViewById(R.id.edtNodeName);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            btnDelete = (Button) activity.findViewById(R.id.btnDelete);
            checkBoxMyHouse = (CheckBox) activity.findViewById(R.id.checkMyHouse);
            lblMyHouse = (TextView) activity.findViewById(R.id.lblMyHouse);
            intializeVariables();

        }

        public CO_d_section_add_node_w2(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            listView1 = (ListView) dlg.findViewById(R.id.lstLanguage);
            scrollView1 = (ScrollView) dlg.findViewById(R.id.scrollView1);
            laySection = (LinearLayout) dlg.findViewById(R.id.laySection);
            lblSection = (TextView) dlg.findViewById(R.id.lblSection);
            spnSections = (Spinner) dlg.findViewById(R.id.spnSections);
            layRoom = (LinearLayout) dlg.findViewById(R.id.layRoom);
            lblRoom = (TextView) dlg.findViewById(R.id.lblRoom);
            spnRooms = (Spinner) dlg.findViewById(R.id.spnRooms);
            icnNodeIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) dlg.findViewById(R.id.icnNodeIcon);
            lblName = (TextView) dlg.findViewById(R.id.txtUsername);
            edtNodeName = (EditText) dlg.findViewById(R.id.edtNodeName);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            btnDelete = (Button) dlg.findViewById(R.id.btnDelete);
            checkBoxMyHouse = (CheckBox) dlg.findViewById(R.id.checkMyHouse);
            lblMyHouse = (TextView) dlg.findViewById(R.id.lblMyHouse);
            intializeVariables();

        }

        public CO_d_section_add_node_w2(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            listView1 = (ListView) view.findViewById(R.id.lstLanguage);
            scrollView1 = (ScrollView) view.findViewById(R.id.scrollView1);
            laySection = (LinearLayout) view.findViewById(R.id.laySection);
            lblSection = (TextView) view.findViewById(R.id.lblSection);
            spnSections = (Spinner) view.findViewById(R.id.spnSections);
            layRoom = (LinearLayout) view.findViewById(R.id.layRoom);
            lblRoom = (TextView) view.findViewById(R.id.lblRoom);
            spnRooms = (Spinner) view.findViewById(R.id.spnRooms);
            icnNodeIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) view.findViewById(R.id.icnNodeIcon);
            lblName = (TextView) view.findViewById(R.id.txtUsername);
            edtNodeName = (EditText) view.findViewById(R.id.edtNodeName);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            btnDelete = (Button) view.findViewById(R.id.btnDelete);
            checkBoxMyHouse = (CheckBox) view.findViewById(R.id.checkMyHouse);
            lblMyHouse = (TextView) view.findViewById(R.id.lblMyHouse);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_d_section_add_IOModuleNode_Select_Place {
        public LinearLayout layOptions;
        public ListView listView1;
        public ScrollView scrollView1;
        public LinearLayout laySection;
        public TextView lblSection;
        public Spinner spnSections;
        public LinearLayout layRoom;
        public TextView lblRoom;
        public Spinner spnRooms;
        public ir.parsansoft.app.ihs.center.components.ComIconSelector icnNodeIcon;
        public TextView lblName;
        public EditText edtNodeName;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView textView1;
        public TextView txtTitle;
        public Button btnNext, btnDelete, btnBack;
        public CheckBox checkBoxMyHouse;
        public TextView lblMyHouse;

        public CO_d_section_add_IOModuleNode_Select_Place(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            listView1 = (ListView) activity.findViewById(R.id.lstLanguage);
            scrollView1 = (ScrollView) activity.findViewById(R.id.scrollView1);
            laySection = (LinearLayout) activity.findViewById(R.id.laySection);
            lblSection = (TextView) activity.findViewById(R.id.lblSection);
            spnSections = (Spinner) activity.findViewById(R.id.spnSections);
            layRoom = (LinearLayout) activity.findViewById(R.id.layRoom);
            lblRoom = (TextView) activity.findViewById(R.id.lblRoom);
            spnRooms = (Spinner) activity.findViewById(R.id.spnRooms);
            icnNodeIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) activity.findViewById(R.id.icnNodeIcon);
            lblName = (TextView) activity.findViewById(R.id.txtUsername);
            edtNodeName = (EditText) activity.findViewById(R.id.edtNodeName);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnDelete = (Button) activity.findViewById(R.id.btnDelete);
            checkBoxMyHouse = (CheckBox) activity.findViewById(R.id.checkMyHouse);
            lblMyHouse = (TextView) activity.findViewById(R.id.lblMyHouse);
            intializeVariables();

        }

        public CO_d_section_add_IOModuleNode_Select_Place(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            listView1 = (ListView) dlg.findViewById(R.id.lstLanguage);
            scrollView1 = (ScrollView) dlg.findViewById(R.id.scrollView1);
            laySection = (LinearLayout) dlg.findViewById(R.id.laySection);
            lblSection = (TextView) dlg.findViewById(R.id.lblSection);
            spnSections = (Spinner) dlg.findViewById(R.id.spnSections);
            layRoom = (LinearLayout) dlg.findViewById(R.id.layRoom);
            lblRoom = (TextView) dlg.findViewById(R.id.lblRoom);
            spnRooms = (Spinner) dlg.findViewById(R.id.spnRooms);
            icnNodeIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) dlg.findViewById(R.id.icnNodeIcon);
            lblName = (TextView) dlg.findViewById(R.id.txtUsername);
            edtNodeName = (EditText) dlg.findViewById(R.id.edtNodeName);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);
            btnDelete = (Button) dlg.findViewById(R.id.btnDelete);
            checkBoxMyHouse = (CheckBox) dlg.findViewById(R.id.checkMyHouse);
            lblMyHouse = (TextView) dlg.findViewById(R.id.lblMyHouse);
            intializeVariables();

        }

        public CO_d_section_add_IOModuleNode_Select_Place(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            listView1 = (ListView) view.findViewById(R.id.lstLanguage);
            scrollView1 = (ScrollView) view.findViewById(R.id.scrollView1);
            laySection = (LinearLayout) view.findViewById(R.id.laySection);
            lblSection = (TextView) view.findViewById(R.id.lblSection);
            spnSections = (Spinner) view.findViewById(R.id.spnSections);
            layRoom = (LinearLayout) view.findViewById(R.id.layRoom);
            lblRoom = (TextView) view.findViewById(R.id.lblRoom);
            spnRooms = (Spinner) view.findViewById(R.id.spnRooms);
            icnNodeIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) view.findViewById(R.id.icnNodeIcon);
            lblName = (TextView) view.findViewById(R.id.txtUsername);
            edtNodeName = (EditText) view.findViewById(R.id.edtNodeName);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            btnBack = (Button) view.findViewById(R.id.btnBack);
            btnDelete = (Button) view.findViewById(R.id.btnDelete);
            checkBoxMyHouse = (CheckBox) view.findViewById(R.id.checkMyHouse);
            lblMyHouse = (TextView) view.findViewById(R.id.lblMyHouse);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_d_section_add_node_w3 {
        public ImageView imgIcon;
        public TextView txtHeader;
        public TextView txtNote;
        public TextView txtSerial;
        public TextView txtRegDate;
        public TextView txtExpDate;
        public TextView textView2;
        public Button btnFinished;
        public TextView TextView01;

        public CO_d_section_add_node_w3(Context context) {
            Activity activity = (Activity) context;
            imgIcon = (ImageView) activity.findViewById(R.id.imgIcon);
            txtHeader = (TextView) activity.findViewById(R.id.txtHeader);
            txtNote = (TextView) activity.findViewById(R.id.txtNote);
            txtSerial = (TextView) activity.findViewById(R.id.txtSerial);
            txtRegDate = (TextView) activity.findViewById(R.id.txtRegDate);
            txtExpDate = (TextView) activity.findViewById(R.id.txtExpDate);
            textView2 = (TextView) activity.findViewById(R.id.textView2);
            btnFinished = (Button) activity.findViewById(R.id.btnFinished);
            TextView01 = (TextView) activity.findViewById(R.id.TextView01);
            intializeVariables();

        }

        public CO_d_section_add_node_w3(Dialog dlg) {
            imgIcon = (ImageView) dlg.findViewById(R.id.imgIcon);
            txtHeader = (TextView) dlg.findViewById(R.id.txtHeader);
            txtNote = (TextView) dlg.findViewById(R.id.txtNote);
            txtSerial = (TextView) dlg.findViewById(R.id.txtSerial);
            txtRegDate = (TextView) dlg.findViewById(R.id.txtRegDate);
            txtExpDate = (TextView) dlg.findViewById(R.id.txtExpDate);
            textView2 = (TextView) dlg.findViewById(R.id.textView2);
            btnFinished = (Button) dlg.findViewById(R.id.btnFinished);
            TextView01 = (TextView) dlg.findViewById(R.id.TextView01);
            intializeVariables();

        }

        public CO_d_section_add_node_w3(View view) {
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            txtHeader = (TextView) view.findViewById(R.id.txtHeader);
            txtNote = (TextView) view.findViewById(R.id.txtNote);
            txtSerial = (TextView) view.findViewById(R.id.txtSerial);
            txtRegDate = (TextView) view.findViewById(R.id.txtRegDate);
            txtExpDate = (TextView) view.findViewById(R.id.txtExpDate);
            textView2 = (TextView) view.findViewById(R.id.textView2);
            btnFinished = (Button) view.findViewById(R.id.btnFinished);
            TextView01 = (TextView) view.findViewById(R.id.TextView01);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_d_section_add_node_Sensor {
        public TextView lblSensorName;
        public TextView txtTitle;
        public TextView lblSensorType;
        public TextView lblSensorModel;
        public TextView lblSensorPort;
        public TextView lblSensorConrtol;
        public EditText etSensorName;
        public Spinner spnSensorModel;
        public Spinner spnSensorType;
        public Spinner spnSensorPort;
        public Button btnNext,btnCancel,btnBack;
//        public CheckBox chkBoxPort1, chkBoxPort2, chkBoxPort3, chkBoxPort4,
//                chkBoxPort5, chkBoxPort6, chkBoxPort7, chkBoxPort8, chkBoxPort9, chkBoxPort10;

        public CO_d_section_add_node_Sensor(Context context) {
            Activity activity = (Activity) context;

            lblSensorName = (TextView) activity.findViewById(R.id.lblSensorName);
            lblSensorType = (TextView) activity.findViewById(R.id.lblSenorType);
            lblSensorPort = (TextView) activity.findViewById(R.id.lblSensorPort);
            lblSensorModel = (TextView) activity.findViewById(R.id.lblSenorModel);
            lblSensorType = (TextView) activity.findViewById(R.id.lblSenorType);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            etSensorName = (EditText) activity.findViewById(R.id.edtSensorName);
            spnSensorModel = (Spinner) activity.findViewById(R.id.spnSensorModel);
            spnSensorType = (Spinner) activity.findViewById(R.id.spnSensorType);
            spnSensorPort = (Spinner) activity.findViewById(R.id.spnPorts);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
//            chkBoxPort1 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort1);
//            chkBoxPort2 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort2);
//            chkBoxPort3 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort3);
//            chkBoxPort4 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort4);
//            chkBoxPort5 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort5);
//            chkBoxPort6 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort6);
//            chkBoxPort7 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort7);
//            chkBoxPort8 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort8);
//            chkBoxPort9 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort9);
//            chkBoxPort10 = (CheckBox)activity.findViewById(R.nodeId.chkBoxPort10);

            intializeVariables();
        }

        public CO_d_section_add_node_Sensor(Dialog dlg) {
            lblSensorName = (TextView) dlg.findViewById(R.id.lblSensorName);
            lblSensorType = (TextView) dlg.findViewById(R.id.lblSenorType);
            lblSensorPort = (TextView) dlg.findViewById(R.id.lblSensorPort);
            etSensorName = (EditText) dlg.findViewById(R.id.edtSensorName);
            spnSensorModel = (Spinner) dlg.findViewById(R.id.spnSensorType);
            spnSensorPort = (Spinner) dlg.findViewById(R.id.spnPorts);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);

            intializeVariables();

        }

        public CO_d_section_add_node_Sensor(View view) {
            lblSensorName = (TextView) view.findViewById(R.id.lblSensorName);
            lblSensorType = (TextView) view.findViewById(R.id.lblSenorType);
            lblSensorPort = (TextView) view.findViewById(R.id.lblSensorPort);
            etSensorName = (EditText) view.findViewById(R.id.edtSensorName);
            spnSensorModel = (Spinner) view.findViewById(R.id.spnSensorType);
            spnSensorPort = (Spinner) view.findViewById(R.id.spnPorts);
            btnNext = (Button) view.findViewById(R.id.btnNext);

            intializeVariables();
        }

        private void intializeVariables() {

        }
    }

    public static final class CO_d_section_add_room {
        public LinearLayout layRoom;
        public TextView lblSection;
        public TextView txtTitle;
        public Spinner spnRooms;
        public ir.parsansoft.app.ihs.center.components.ComIconSelector icnIcon;
        public TextView lblName;
        public EditText edtName;
        public TextView textView2;
        public Button btnCancel;
        public Button btnOK;
        public TextView TextView01;

        public CO_d_section_add_room(Context context) {
            Activity activity = (Activity) context;
            layRoom = (LinearLayout) activity.findViewById(R.id.layRoom);
            lblSection = (TextView) activity.findViewById(R.id.lblSection);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            spnRooms = (Spinner) activity.findViewById(R.id.spnRooms);
            icnIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) activity.findViewById(R.id.icnIcon);
            lblName = (TextView) activity.findViewById(R.id.txtUsername);
            edtName = (EditText) activity.findViewById(R.id.edtName);
            textView2 = (TextView) activity.findViewById(R.id.textView2);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            btnOK = (Button) activity.findViewById(R.id.btnOK);
            TextView01 = (TextView) activity.findViewById(R.id.TextView01);
            intializeVariables();

        }

        public CO_d_section_add_room(Dialog dlg) {
            layRoom = (LinearLayout) dlg.findViewById(R.id.layRoom);
            lblSection = (TextView) dlg.findViewById(R.id.lblSection);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            spnRooms = (Spinner) dlg.findViewById(R.id.spnRooms);
            icnIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) dlg.findViewById(R.id.icnIcon);
            lblName = (TextView) dlg.findViewById(R.id.txtUsername);
            edtName = (EditText) dlg.findViewById(R.id.edtName);
            textView2 = (TextView) dlg.findViewById(R.id.textView2);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            btnOK = (Button) dlg.findViewById(R.id.btnOK);
            TextView01 = (TextView) dlg.findViewById(R.id.TextView01);
            intializeVariables();

        }

        public CO_d_section_add_room(View view) {
            layRoom = (LinearLayout) view.findViewById(R.id.layRoom);
            lblSection = (TextView) view.findViewById(R.id.lblSection);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            spnRooms = (Spinner) view.findViewById(R.id.spnRooms);
            icnIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) view.findViewById(R.id.icnIcon);
            lblName = (TextView) view.findViewById(R.id.txtUsername);
            edtName = (EditText) view.findViewById(R.id.edtName);
            textView2 = (TextView) view.findViewById(R.id.textView2);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            btnOK = (Button) view.findViewById(R.id.btnOK);
            TextView01 = (TextView) view.findViewById(R.id.TextView01);
            intializeVariables();
        }

        private void intializeVariables() {
            txtTitle.setText(G.T.getSentence(156));
            lblSection.setText(G.T.getSentence(155));
            lblName.setText(G.T.getSentence(236));
            btnCancel.setText(G.T.getSentence(102));
            btnOK.setText(G.T.getSentence(101));
        }
    }

    public static final class CO_d_section_add_section {
        public TextView txtTitle;
        public ir.parsansoft.app.ihs.center.components.ComIconSelector icnIcon;
        public TextView lblName;
        public EditText edtName;
        public TextView textView2;
        public Button btnCancel;
        public Button btnOK;
        public TextView TextView01;

        public CO_d_section_add_section(Context context) {
            Activity activity = (Activity) context;
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            icnIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) activity.findViewById(R.id.icnIcon);
            lblName = (TextView) activity.findViewById(R.id.txtUsername);
            edtName = (EditText) activity.findViewById(R.id.edtName);
            textView2 = (TextView) activity.findViewById(R.id.textView2);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            btnOK = (Button) activity.findViewById(R.id.btnOK);
            TextView01 = (TextView) activity.findViewById(R.id.TextView01);
            intializeVariables();

        }

        public CO_d_section_add_section(Dialog dlg) {
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            icnIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) dlg.findViewById(R.id.icnIcon);
            lblName = (TextView) dlg.findViewById(R.id.txtUsername);
            edtName = (EditText) dlg.findViewById(R.id.edtName);
            textView2 = (TextView) dlg.findViewById(R.id.textView2);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            btnOK = (Button) dlg.findViewById(R.id.btnOK);
            TextView01 = (TextView) dlg.findViewById(R.id.TextView01);
            intializeVariables();

        }

        public CO_d_section_add_section(View view) {
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            icnIcon = (ir.parsansoft.app.ihs.center.components.ComIconSelector) view.findViewById(R.id.icnIcon);
            lblName = (TextView) view.findViewById(R.id.txtUsername);
            edtName = (EditText) view.findViewById(R.id.edtName);
            textView2 = (TextView) view.findViewById(R.id.textView2);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            btnOK = (Button) view.findViewById(R.id.btnOK);
            TextView01 = (TextView) view.findViewById(R.id.TextView01);
            intializeVariables();

        }

        private void intializeVariables() {
            txtTitle.setText(G.T.getSentence(155));
            lblName.setText(G.T.getSentence(235));
            btnCancel.setText(G.T.getSentence(104));
            btnOK.setText(G.T.getSentence(101));
        }
    }

    public static final class CO_c_comp_node_selector {
        public LinearLayout laySelector;
        public LinearLayout component_Layout;
        public ImageView imgNodeIcon;
        public TextView txtSwitchName;
        public Button btnSelect;

        public CO_c_comp_node_selector(Context context) {
            Activity activity = (Activity) context;
            component_Layout = (LinearLayout) activity.findViewById(R.id.component_Layout);
            laySelector = (LinearLayout) activity.findViewById(R.id.laySelector);
            imgNodeIcon = (ImageView) activity.findViewById(R.id.imgNodeIcon);
            txtSwitchName = (TextView) activity.findViewById(R.id.txtSwitchName);
            btnSelect = (Button) activity.findViewById(R.id.btnSelect);
            intializeVariables();

        }

        public CO_c_comp_node_selector(Dialog dlg) {
            component_Layout = (LinearLayout) dlg.findViewById(R.id.component_Layout);
            laySelector = (LinearLayout) dlg.findViewById(R.id.laySelector);
            imgNodeIcon = (ImageView) dlg.findViewById(R.id.imgNodeIcon);
            txtSwitchName = (TextView) dlg.findViewById(R.id.txtSwitchName);
            btnSelect = (Button) dlg.findViewById(R.id.btnSelect);
            intializeVariables();

        }

        public CO_c_comp_node_selector(View view) {
            component_Layout = (LinearLayout) view.findViewById(R.id.component_Layout);
            laySelector = (LinearLayout) view.findViewById(R.id.laySelector);
            imgNodeIcon = (ImageView) view.findViewById(R.id.imgNodeIcon);
            txtSwitchName = (TextView) view.findViewById(R.id.txtSwitchName);
            btnSelect = (Button) view.findViewById(R.id.btnSelect);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_c_comp_node_selector_dialog {
        public LinearLayout layOptions;
        public TextView txtTitle;
        public TextView txtBody;
        public TextView txtValueLabel;
        public Spinner spnSections;
        public EditText edtValue;
        public LinearLayout layNavigation;
        public Button btnNegative;
        public TextView textView1;
        public Button btnPositive;

        public CO_c_comp_node_selector_dialog(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            txtBody = (TextView) activity.findViewById(R.id.txtBody);
            txtValueLabel = (TextView) activity.findViewById(R.id.txtValueLabel);
            spnSections = (Spinner) activity.findViewById(R.id.spnSections);
            edtValue = (EditText) activity.findViewById(R.id.edtValue);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnNegative = (Button) activity.findViewById(R.id.btnNegative);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            btnPositive = (Button) activity.findViewById(R.id.btnPositive);
            intializeVariables();

        }

        public CO_c_comp_node_selector_dialog(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            txtBody = (TextView) dlg.findViewById(R.id.txtBody);
            txtValueLabel = (TextView) dlg.findViewById(R.id.txtValueLabel);
            spnSections = (Spinner) dlg.findViewById(R.id.spnSections);
            edtValue = (EditText) dlg.findViewById(R.id.edtValue);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnNegative = (Button) dlg.findViewById(R.id.btnNegative);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            btnPositive = (Button) dlg.findViewById(R.id.btnPositive);
            intializeVariables();

        }

        public CO_c_comp_node_selector_dialog(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtBody = (TextView) view.findViewById(R.id.txtBody);
            txtValueLabel = (TextView) view.findViewById(R.id.txtValueLabel);
            spnSections = (Spinner) view.findViewById(R.id.spnSections);
            edtValue = (EditText) view.findViewById(R.id.edtValue);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnNegative = (Button) view.findViewById(R.id.btnNegative);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            btnPositive = (Button) view.findViewById(R.id.btnPositive);
            intializeVariables();

        }

        private void intializeVariables() {
            txtTitle.setText(G.T.getSentence(379101));
            txtBody.setText(G.T.getSentence(379102));
            txtValueLabel.setText(G.T.getSentence(379103));
            btnNegative.setText(G.T.getSentence(379104));
            btnPositive.setText(G.T.getSentence(379105));
        }
    }

    public static final class CO_d_simple_spinner {
        public LinearLayout layOptions;
        public TextView txtTitle;
        public TextView txtBody;
        public Spinner spnItems;
        public LinearLayout layNavigation;
        public Button btnNegative;
        public TextView textView1;
        public Button btnPositive;

        public CO_d_simple_spinner(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            txtBody = (TextView) activity.findViewById(R.id.txtBody);
            spnItems = (Spinner) activity.findViewById(R.id.spnItems);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnNegative = (Button) activity.findViewById(R.id.btnNegative);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            btnPositive = (Button) activity.findViewById(R.id.btnPositive);
            intializeVariables();

        }

        public CO_d_simple_spinner(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            txtBody = (TextView) dlg.findViewById(R.id.txtBody);
            spnItems = (Spinner) dlg.findViewById(R.id.spnItems);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnNegative = (Button) dlg.findViewById(R.id.btnNegative);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            btnPositive = (Button) dlg.findViewById(R.id.btnPositive);
            intializeVariables();

        }

        public CO_d_simple_spinner(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtBody = (TextView) view.findViewById(R.id.txtBody);
            spnItems = (Spinner) view.findViewById(R.id.spnItems);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnNegative = (Button) view.findViewById(R.id.btnNegative);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            btnPositive = (Button) view.findViewById(R.id.btnPositive);
            intializeVariables();

        }

        private void intializeVariables() {
            //            txtTitle.setText(G.T.getSentence(148901));
            //            UiEditorHelper.editUiText(txtTitle, 148901);
            //            txtBody.setText(G.T.getSentence(148902));
            //            UiEditorHelper.editUiText(txtBody, 148902);
            //            btnNegative.setText(G.T.getSentence(148903));
            //            UiEditorHelper.editUiText(btnNegative, 148903);
            //            btnPositive.setText(G.T.getSentence(148904));
            //            UiEditorHelper.editUiText(btnPositive, 148904);
        }
    }

    public static final class CO_f_favorites {
        public Button btnFavorites;
        public Button btnAll;
        public GridView grdNodes;

        public CO_f_favorites(Context context) {
            Activity activity = (Activity) context;
            btnFavorites = (Button) activity.findViewById(R.id.btnFavorites);
            btnAll = (Button) activity.findViewById(R.id.btnAll);
            grdNodes = (GridView) activity.findViewById(R.id.grdNodes);
            intializeVariables();

        }

        public CO_f_favorites(Dialog dlg) {
            btnFavorites = (Button) dlg.findViewById(R.id.btnFavorites);
            btnAll = (Button) dlg.findViewById(R.id.btnAll);
            grdNodes = (GridView) dlg.findViewById(R.id.grdNodes);
            intializeVariables();

        }

        public CO_f_favorites(View view) {
            btnFavorites = (Button) view.findViewById(R.id.btnFavorites);
            btnAll = (Button) view.findViewById(R.id.btnAll);
            grdNodes = (GridView) view.findViewById(R.id.grdNodes);
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_f_sections {
        public ExpandableListView elist1;
        public Button btnAdd;
        public Button btnAddSection;
        public Button btnAddRoom;
        public Button btnMyHouse;
        public GridView grdNodes;
        public ImageView btnIOModule;

        public CO_f_sections(Context context) {
            Activity activity = (Activity) context;
            elist1 = (ExpandableListView) activity.findViewById(R.id.elist1);
            btnAdd = (Button) activity.findViewById(R.id.btnAdd);
            btnAddSection = (Button) activity.findViewById(R.id.btnAddSection);
            btnMyHouse = (Button) activity.findViewById(R.id.btnMyHouse);
            btnAddRoom = (Button) activity.findViewById(R.id.btnAddRoom);
            grdNodes = (GridView) activity.findViewById(R.id.grdNodes);
            btnIOModule = (ImageView) activity.findViewById(R.id.btn_io_module);
            intializeVariables();

        }

        public CO_f_sections(Dialog dlg) {
            elist1 = (ExpandableListView) dlg.findViewById(R.id.elist1);
            btnAdd = (Button) dlg.findViewById(R.id.btnAdd);
            btnMyHouse = (Button) dlg.findViewById(R.id.btnMyHouse);
            btnAddSection = (Button) dlg.findViewById(R.id.btnAddSection);
            btnAddRoom = (Button) dlg.findViewById(R.id.btnAddRoom);
            btnMyHouse = (Button) dlg.findViewById(R.id.btn_search);
            grdNodes = (GridView) dlg.findViewById(R.id.grdNodes);
            btnIOModule = (ImageView) dlg.findViewById(R.id.btn_io_module);
            intializeVariables();

        }

        public CO_f_sections(View view) {
            elist1 = (ExpandableListView) view.findViewById(R.id.elist1);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            btnAddSection = (Button) view.findViewById(R.id.btnAddSection);
            btnAddRoom = (Button) view.findViewById(R.id.btnAddRoom);
            btnMyHouse = (Button) view.findViewById(R.id.btnMyHouse);
            grdNodes = (GridView) view.findViewById(R.id.grdNodes);
            btnIOModule = (ImageView) view.findViewById(R.id.btn_io_module);
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_f_MyHouse{

        public Button btnAddNode;
        public Button btnBack;
        public Button btnDeleteIO;
        public Button btnAddSection;
        public Button btnAddRoom;
        public GridView grdNodes;
        public ImageView btnIOModule;
        public LinearLayout layIoNode;

        public CO_f_MyHouse(Context context) {
            Activity activity = (Activity) context;
            layIoNode = (LinearLayout)activity.findViewById(R.id.layIoNode);
            btnAddNode = (Button) activity.findViewById(R.id.btnAddNode);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnDeleteIO = (Button) activity.findViewById(R.id.btnDeleteIO);
            btnAddSection = (Button) activity.findViewById(R.id.btnAddSection);
            btnAddRoom = (Button) activity.findViewById(R.id.btnAddRoom);
            grdNodes = (GridView) activity.findViewById(R.id.grdNodes);
            btnIOModule = (ImageView) activity.findViewById(R.id.btn_io_module);

        }

    }

    public static final class CO_f_setting_location {
        public LinearLayout layOptions;
        public TextView textView1;
        public EditText edtLat;
        public EditText edtLocation;
        public TextView TextView01;
        public TextView locationLbl;
        public EditText edtLong;
        public Button btnSet;
        public Button btnSearch;
        public WebView webMap;

        public CO_f_setting_location(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            edtLat = (EditText) activity.findViewById(R.id.edtLat);
            edtLocation = (EditText) activity.findViewById(R.id.etLocation);
            TextView01 = (TextView) activity.findViewById(R.id.TextView01);
            locationLbl = (TextView) activity.findViewById(R.id.locationLbl);
            edtLong = (EditText) activity.findViewById(R.id.edtLong);
            btnSet = (Button) activity.findViewById(R.id.btnSet);
            btnSearch = (Button) activity.findViewById(R.id.btn_search);
            webMap = (WebView) activity.findViewById(R.id.webMap);
            intializeVariables();

        }

        public CO_f_setting_location(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            edtLat = (EditText) dlg.findViewById(R.id.edtLat);
            edtLocation = (EditText) dlg.findViewById(R.id.etLocation);
            TextView01 = (TextView) dlg.findViewById(R.id.TextView01);
            locationLbl = (TextView) dlg.findViewById(R.id.locationLbl);
            edtLong = (EditText) dlg.findViewById(R.id.edtLong);
            btnSet = (Button) dlg.findViewById(R.id.btnSet);
            btnSearch = (Button) dlg.findViewById(R.id.btn_search);
            webMap = (WebView) dlg.findViewById(R.id.webMap);
            intializeVariables();

        }

        public CO_f_setting_location(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            edtLat = (EditText) view.findViewById(R.id.edtLat);
            edtLocation = (EditText) view.findViewById(R.id.etLocation);
            TextView01 = (TextView) view.findViewById(R.id.TextView01);
            locationLbl = (TextView) view.findViewById(R.id.locationLbl);
            edtLong = (EditText) view.findViewById(R.id.edtLong);
            btnSet = (Button) view.findViewById(R.id.btnSet);
            btnSearch = (Button) view.findViewById(R.id.btn_search);
            webMap = (WebView) view.findViewById(R.id.webMap);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_setting_mobile {
        public LinearLayout laySettingMobile;
        public LinearLayout layMarkets;
        public ListView lstMobiles;
        public LinearLayout layQR;
        public TextView txtTitle, txtTitleMarket;
        public HorizontialListView listMarket;
        public ImageView imgQR;

        public CO_f_setting_mobile(Context context) {
            Activity activity = (Activity) context;
            layMarkets = (LinearLayout) activity.findViewById(R.id.layMarkets);
            lstMobiles = (ListView) activity.findViewById(R.id.lstMobiles);
            layQR = (LinearLayout) activity.findViewById(R.id.layQR);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            txtTitleMarket = (TextView) activity.findViewById(R.id.txtTitleMarket);
            imgQR = (ImageView) activity.findViewById(R.id.imgQR);
//            listMarket = (HorizontialListView) activity.findViewById(R.nodeId.lstMarket);
            intializeVariables();

        }

        public CO_f_setting_mobile(Dialog dlg) {
            layMarkets = (LinearLayout) dlg.findViewById(R.id.layMarkets);
//            lstMobiles = (ListView) dlg.findViewById(R.nodeId.lstMobiles);
            layQR = (LinearLayout) dlg.findViewById(R.id.layQR);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            txtTitleMarket = (TextView) dlg.findViewById(R.id.txtTitleMarket);
            imgQR = (ImageView) dlg.findViewById(R.id.imgQR);
            intializeVariables();

        }

        public CO_f_setting_mobile(View view) {
            layMarkets = (LinearLayout) view.findViewById(R.id.layMarkets);
//            lstMobiles = (ListView) view.findViewById(R.nodeId.lstMobiles);
            layQR = (LinearLayout) view.findViewById(R.id.layQR);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtTitleMarket = (TextView) view.findViewById(R.id.txtTitleMarket);
            imgQR = (ImageView) view.findViewById(R.id.imgQR);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_senario {
        public ListView lstSenario;
        public Button btnAdd;
        public ViewPager viewPager;
        public TextView txtDescription, txtCondition, txtResult;

        public CO_f_senario(Context context) {
            Activity activity = (Activity) context;
            lstSenario = (ListView) activity.findViewById(R.id.lstSenario);
            btnAdd = (Button) activity.findViewById(R.id.btnAdd);
            viewPager = (ViewPager) activity.findViewById(R.id.viewpager);
            txtDescription = (TextView) activity.findViewById(R.id.txtDescription);
            txtCondition = (TextView) activity.findViewById(R.id.txtCondition);
            txtResult = (TextView) activity.findViewById(R.id.txtResult);
            intializeVariables();

        }

        public CO_f_senario(Dialog dlg) {
            lstSenario = (ListView) dlg.findViewById(R.id.lstSenario);
            btnAdd = (Button) dlg.findViewById(R.id.btnAdd);
            txtDescription = (TextView) dlg.findViewById(R.id.txtDescription);
            txtCondition = (TextView) dlg.findViewById(R.id.txtCondition);
            txtResult = (TextView) dlg.findViewById(R.id.txtResult);
            intializeVariables();

        }

        public CO_f_senario(View view) {
            lstSenario = (ListView) view.findViewById(R.id.lstSenario);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            txtCondition = (TextView) view.findViewById(R.id.txtCondition);
            txtResult = (TextView) view.findViewById(R.id.txtResult);
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_f_senario_w1 {
        public LinearLayout layOptions;
        public TextView txtSenarioName;
        public EditText edtName;
        public TextView txtDes;
        public EditText edtDes;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView textView1;
        public Button btnNext;

        public CO_f_senario_w1(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            txtSenarioName = (TextView) activity.findViewById(R.id.txtSenarioName);
            edtName = (EditText) activity.findViewById(R.id.edtName);
            txtDes = (TextView) activity.findViewById(R.id.txtDes);
            edtDes = (EditText) activity.findViewById(R.id.edtDes);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            intializeVariables();
        }

        public CO_f_senario_w1(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            txtSenarioName = (TextView) dlg.findViewById(R.id.txtSenarioName);
            edtName = (EditText) dlg.findViewById(R.id.edtName);
            txtDes = (TextView) dlg.findViewById(R.id.txtDes);
            edtDes = (EditText) dlg.findViewById(R.id.edtDes);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            intializeVariables();
        }

        public CO_f_senario_w1(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            txtSenarioName = (TextView) view.findViewById(R.id.txtSenarioName);
            edtName = (EditText) view.findViewById(R.id.edtName);
            txtDes = (TextView) view.findViewById(R.id.txtDes);
            edtDes = (EditText) view.findViewById(R.id.edtDes);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            intializeVariables();
        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_senario_w10 {
        public LinearLayout laySwitcher;
        public Switch swhActive;
        public LinearLayout layOptions;
        public TextView txtTitle;
        public ListView lstMobileDevices;
        public Button btnAdd;
        public TextView txtMessageTitle;
        public EditText edtAlarmText;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView TextView05;
        public Button btnBack;
        public Button btnFinish;

        public CO_f_senario_w10(Context context) {
            Activity activity = (Activity) context;
            laySwitcher = (LinearLayout) activity.findViewById(R.id.laySwitcher);
            swhActive = (Switch) activity.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            lstMobileDevices = (ListView) activity.findViewById(R.id.lstUser);
            btnAdd = (Button) activity.findViewById(R.id.btnAdd);
            txtMessageTitle = (TextView) activity.findViewById(R.id.txtMessageTitle);
            edtAlarmText = (EditText) activity.findViewById(R.id.edtAlarmText);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) activity.findViewById(R.nodeId.TextView05);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnFinish = (Button) activity.findViewById(R.id.btnFinish);
            intializeVariables();

        }

        public CO_f_senario_w10(Dialog dlg) {
            laySwitcher = (LinearLayout) dlg.findViewById(R.id.laySwitcher);
            swhActive = (Switch) dlg.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            lstMobileDevices = (ListView) dlg.findViewById(R.id.lstUser);
            btnAdd = (Button) dlg.findViewById(R.id.btnAdd);
            txtMessageTitle = (TextView) dlg.findViewById(R.id.txtMessageTitle);
            edtAlarmText = (EditText) dlg.findViewById(R.id.edtAlarmText);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) dlg.findViewById(R.nodeId.TextView05);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);
            btnFinish = (Button) dlg.findViewById(R.id.btnFinish);
            intializeVariables();

        }

        public CO_f_senario_w10(View view) {
            laySwitcher = (LinearLayout) view.findViewById(R.id.laySwitcher);
            swhActive = (Switch) view.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            lstMobileDevices = (ListView) view.findViewById(R.id.lstUser);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            txtMessageTitle = (TextView) view.findViewById(R.id.txtMessageTitle);
            edtAlarmText = (EditText) view.findViewById(R.id.edtAlarmText);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) view.findViewById(R.nodeId.TextView05);
            btnBack = (Button) view.findViewById(R.id.btnBack);
            btnFinish = (Button) view.findViewById(R.id.btnFinish);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_senario_w2 {
        public Switch swhActive;
        public LinearLayout laySwitcher;
        public LinearLayout layOptions;
        public ScrollView scrollView1;
        public TextView txtDaysOfMonth;
        public GridLayout GridLayout2;
        public CheckBox[] chkMonthDay = new CheckBox[31];
        public GridLayout GridLayout1;
        public CheckBox[] chkMonth = new CheckBox[12];
        public TextView txtMonthes;
        public TextView txtDaysOfWeek;
        public GridLayout GridLayout3;
        public CheckBox[] chkWeekDay = new CheckBox[7];
        public TextView txtStartHour;
        public TimePickerComponent tpStartHore;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView textView1;
        public Button btnNext;
        public Button btnBack;

        public CO_f_senario_w2(Context context) {
            Activity activity = (Activity) context;
            swhActive = (Switch) activity.findViewById(R.id.swhActive);
            laySwitcher = (LinearLayout) activity.findViewById(R.id.layOptions);
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            scrollView1 = (ScrollView) activity.findViewById(R.id.scrollView1);
            txtDaysOfMonth = (TextView) activity.findViewById(R.id.txtDaysOfMonth);
            GridLayout2 = (GridLayout) activity.findViewById(R.id.GridLayout2);
            for (int i = 0; i < 12; i++)
                chkMonth[i] = (CheckBox) activity.findViewById(R.id.chkMonth1 + i);
            for (int i = 0; i < 31; i++)
                chkMonthDay[i] = (CheckBox) activity.findViewById(R.id.chkMonthDay1 + i);
            for (int i = 0; i < 7; i++)
                chkWeekDay[i] = (CheckBox) activity.findViewById(R.id.chkWeekDay1 + i);
            GridLayout1 = (GridLayout) activity.findViewById(R.id.GridLayout1);
            txtMonthes = (TextView) activity.findViewById(R.id.txtMonthes);
            txtDaysOfWeek = (TextView) activity.findViewById(R.id.txtDaysOfWeek);
            GridLayout3 = (GridLayout) activity.findViewById(R.id.GridLayout3);
            txtStartHour = (TextView) activity.findViewById(R.id.txtStartHour);
            tpStartHore = (TimePickerComponent) activity.findViewById(R.id.tpStartHore);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            btnBack = (Button) activity.findViewById(R.id.btnBack);

            intializeVariables();

        }

        public CO_f_senario_w2(Dialog dlg) {
            swhActive = (Switch) dlg.findViewById(R.id.swhActive);
            laySwitcher = (LinearLayout) dlg.findViewById(R.id.layOptions);
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            scrollView1 = (ScrollView) dlg.findViewById(R.id.scrollView1);
            txtDaysOfMonth = (TextView) dlg.findViewById(R.id.txtDaysOfMonth);
            GridLayout2 = (GridLayout) dlg.findViewById(R.id.GridLayout2);
            for (int i = 0; i < 12; i++)
                chkMonth[i] = (CheckBox) dlg.findViewById(R.id.chkMonth1 + i);
            for (int i = 0; i < 31; i++)
                chkMonthDay[i] = (CheckBox) dlg.findViewById(R.id.chkMonthDay1 + i);
            for (int i = 0; i < 7; i++)
                chkWeekDay[i] = (CheckBox) dlg.findViewById(R.id.chkWeekDay1 + i);
            GridLayout1 = (GridLayout) dlg.findViewById(R.id.GridLayout1);
            txtMonthes = (TextView) dlg.findViewById(R.id.txtMonthes);
            txtDaysOfWeek = (TextView) dlg.findViewById(R.id.txtDaysOfWeek);
            GridLayout3 = (GridLayout) dlg.findViewById(R.id.GridLayout3);
            txtStartHour = (TextView) dlg.findViewById(R.id.txtStartHour);
            tpStartHore = (TimePickerComponent) dlg.findViewById(R.id.tpStartHore);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);

            intializeVariables();

        }

        public CO_f_senario_w2(View view) {
            swhActive = (Switch) view.findViewById(R.id.swhActive);
            laySwitcher = (LinearLayout) view.findViewById(R.id.layOptions);
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            scrollView1 = (ScrollView) view.findViewById(R.id.scrollView1);
            txtDaysOfMonth = (TextView) view.findViewById(R.id.txtDaysOfMonth);
            GridLayout2 = (GridLayout) view.findViewById(R.id.GridLayout2);
            GridLayout1 = (GridLayout) view.findViewById(R.id.GridLayout1);
            txtDaysOfWeek = (TextView) view.findViewById(R.id.txtDaysOfWeek);
            GridLayout3 = (GridLayout) view.findViewById(R.id.GridLayout3);
            for (int i = 0; i < 12; i++)
                chkMonth[i] = (CheckBox) view.findViewById(R.id.chkMonth1 + i);
            for (int i = 0; i < 31; i++)
                chkMonthDay[i] = (CheckBox) view.findViewById(R.id.chkMonthDay1 + i);
            for (int i = 0; i < 7; i++)
                chkWeekDay[i] = (CheckBox) view.findViewById(R.id.chkWeekDay1 + i);
            txtStartHour = (TextView) view.findViewById(R.id.txtStartHour);
            tpStartHore = (TimePickerComponent) view.findViewById(R.id.tpStartHore);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            btnBack = (Button) view.findViewById(R.id.btnBack);

            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_senario_w3 {
        public LinearLayout laySwitcher;
        public Switch swhActive;
        public LinearLayout layOptions;
        public ListView lstPreoperands;
        //        public ScrollView                                              scrollView1;
        public LinearLayout LinearLayout02;
        //        public ir.parsansoft.app.ihs.center.components.ComNodeSelector comNodeSelector1;
//        public LinearLayout                                            layPerOperandValueRange;
//        public TextView                                                TextView07;
//        public Spinner                                                 spnLogicalSign;
//        public EditText                                                edtLogicalValue;
//        public LinearLayout                                            layPerOperandValueSelective;
//        public TextView                                                TextView04;
//        public Spinner                                                 spnSelectiveValue;
        public Button btnAdd;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView TextView05;
        public Button btnBack;
        public Button btnNext;

        public CO_f_senario_w3(Context context) {
            Activity activity = (Activity) context;
            laySwitcher = (LinearLayout) activity.findViewById(R.id.laySwitcher);
            swhActive = (Switch) activity.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            lstPreoperands = (ListView) activity.findViewById(R.id.lstPreoperands);
//            scrollView1 = (ScrollView) activity.findViewById(R.nodeId.scrollView1);
            LinearLayout02 = (LinearLayout) activity.findViewById(R.id.LinearLayout02);
//            comNodeSelector1 = (ir.parsansoft.app.ihs.center.components.ComNodeSelector) activity.findViewById(R.nodeId.comNodeSelector1);
//            layPerOperandValueRange = (LinearLayout) activity.findViewById(R.nodeId.layPerOperandValueRange);
//            TextView07 = (TextView) activity.findViewById(R.nodeId.TextView07);
//            spnLogicalSign = (Spinner) activity.findViewById(R.nodeId.spnLogicalSign);
//            edtLogicalValue = (EditText) activity.findViewById(R.nodeId.edtLogicalValue);
//            layPerOperandValueSelective = (LinearLayout) activity.findViewById(R.nodeId.layPerOperandValueSelective);
//            TextView04 = (TextView) activity.findViewById(R.nodeId.TextView04);
//            spnSelectiveValue = (Spinner) activity.findViewById(R.nodeId.spnSelectiveValue);
            btnAdd = (Button) activity.findViewById(R.id.btnAdd);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) activity.findViewById(R.nodeId.TextView05);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w3(Dialog dlg) {
            laySwitcher = (LinearLayout) dlg.findViewById(R.id.laySwitcher);
            swhActive = (Switch) dlg.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            lstPreoperands = (ListView) dlg.findViewById(R.id.lstPreoperands);
//            scrollView1 = (ScrollView) dlg.findViewById(R.nodeId.scrollView1);
            LinearLayout02 = (LinearLayout) dlg.findViewById(R.id.LinearLayout02);
//            comNodeSelector1 = (ir.parsansoft.app.ihs.center.components.ComNodeSelector) dlg.findViewById(R.nodeId.comNodeSelector1);
//            layPerOperandValueRange = (LinearLayout) dlg.findViewById(R.nodeId.layPerOperandValueRange);
//            TextView07 = (TextView) dlg.findViewById(R.nodeId.TextView07);
//            spnLogicalSign = (Spinner) dlg.findViewById(R.nodeId.spnLogicalSign);
//            edtLogicalValue = (EditText) dlg.findViewById(R.nodeId.edtLogicalValue);
//            layPerOperandValueSelective = (LinearLayout) dlg.findViewById(R.nodeId.layPerOperandValueSelective);
//            TextView04 = (TextView) dlg.findViewById(R.nodeId.TextView04);
//            spnSelectiveValue = (Spinner) dlg.findViewById(R.nodeId.spnSelectiveValue);
            btnAdd = (Button) dlg.findViewById(R.id.btnAdd);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) dlg.findViewById(R.nodeId.TextView05);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w3(View view) {
            laySwitcher = (LinearLayout) view.findViewById(R.id.laySwitcher);
            swhActive = (Switch) view.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            lstPreoperands = (ListView) view.findViewById(R.id.lstPreoperands);
//            scrollView1 = (ScrollView) view.findViewById(R.nodeId.scrollView1);
            LinearLayout02 = (LinearLayout) view.findViewById(R.id.LinearLayout02);
//            comNodeSelector1 = (ir.parsansoft.app.ihs.center.components.ComNodeSelector) view.findViewById(R.nodeId.comNodeSelector1);
//            layPerOperandValueRange = (LinearLayout) view.findViewById(R.nodeId.layPerOperandValueRange);
//            TextView07 = (TextView) view.findViewById(R.nodeId.TextView07);
//            spnLogicalSign = (Spinner) view.findViewById(R.nodeId.spnLogicalSign);
//            edtLogicalValue = (EditText) view.findViewById(R.nodeId.edtLogicalValue);
//            layPerOperandValueSelective = (LinearLayout) view.findViewById(R.nodeId.layPerOperandValueSelective);
//            TextView04 = (TextView) view.findViewById(R.nodeId.TextView04);
//            spnSelectiveValue = (Spinner) view.findViewById(R.nodeId.spnSelectiveValue);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) view.findViewById(R.nodeId.TextView05);
            btnBack = (Button) view.findViewById(R.id.btnBack);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_senario_w4 {
        public LinearLayout laySwitcher;
        public Switch swhActive;
        public LinearLayout layOptions;
        public TextView textView2;
        public Spinner spnMobile;
        public RadioButton rdbtnEntering;
        public RadioButton rdbtnExiting;
        public TextView textView1;
        public EditText edtLat;
        public TextView TextView01;
        public EditText edtLong;
        public TextView TextView02;
        public EditText edtRadius;
        public Spinner spnDistance;
        public Button btnSet;
        public WebView webMap;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView TextView05;
        public Button btnBack;
        public Button btnNext;

        public CO_f_senario_w4(Context context) {
            Activity activity = (Activity) context;
            laySwitcher = (LinearLayout) activity.findViewById(R.id.laySwitcher);
            swhActive = (Switch) activity.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            textView2 = (TextView) activity.findViewById(R.id.textView2);
            spnMobile = (Spinner) activity.findViewById(R.id.spnMobile);
            rdbtnEntering = (RadioButton) activity.findViewById(R.id.rdbtnEntering);
            rdbtnExiting = (RadioButton) activity.findViewById(R.id.rdbtnExiting);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            edtLat = (EditText) activity.findViewById(R.id.edtLat);
            TextView01 = (TextView) activity.findViewById(R.id.TextView01);
            edtLong = (EditText) activity.findViewById(R.id.edtLong);
            TextView02 = (TextView) activity.findViewById(R.id.TextView02);
            edtRadius = (EditText) activity.findViewById(R.id.edtRadius);
            spnDistance = (Spinner) activity.findViewById(R.id.spnDistance);
            btnSet = (Button) activity.findViewById(R.id.btnSet);
            webMap = (WebView) activity.findViewById(R.id.webMap);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) activity.findViewById(R.nodeId.TextView05);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w4(Dialog dlg) {
            laySwitcher = (LinearLayout) dlg.findViewById(R.id.laySwitcher);
            swhActive = (Switch) dlg.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            textView2 = (TextView) dlg.findViewById(R.id.textView2);
            spnMobile = (Spinner) dlg.findViewById(R.id.spnMobile);
            rdbtnEntering = (RadioButton) dlg.findViewById(R.id.rdbtnEntering);
            rdbtnExiting = (RadioButton) dlg.findViewById(R.id.rdbtnExiting);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            edtLat = (EditText) dlg.findViewById(R.id.edtLat);
            TextView01 = (TextView) dlg.findViewById(R.id.TextView01);
            edtLong = (EditText) dlg.findViewById(R.id.edtLong);
            TextView02 = (TextView) dlg.findViewById(R.id.TextView02);
            edtRadius = (EditText) dlg.findViewById(R.id.edtRadius);
            spnDistance = (Spinner) dlg.findViewById(R.id.spnDistance);
            btnSet = (Button) dlg.findViewById(R.id.btnSet);
            webMap = (WebView) dlg.findViewById(R.id.webMap);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) dlg.findViewById(R.nodeId.TextView05);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w4(View view) {
            laySwitcher = (LinearLayout) view.findViewById(R.id.laySwitcher);
            swhActive = (Switch) view.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            textView2 = (TextView) view.findViewById(R.id.textView2);
            spnMobile = (Spinner) view.findViewById(R.id.spnMobile);
            rdbtnEntering = (RadioButton) view.findViewById(R.id.rdbtnEntering);
            rdbtnExiting = (RadioButton) view.findViewById(R.id.rdbtnExiting);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            edtLat = (EditText) view.findViewById(R.id.edtLat);
            TextView01 = (TextView) view.findViewById(R.id.TextView01);
            edtLong = (EditText) view.findViewById(R.id.edtLong);
            TextView02 = (TextView) view.findViewById(R.id.TextView02);
            edtRadius = (EditText) view.findViewById(R.id.edtRadius);
            spnDistance = (Spinner) view.findViewById(R.id.spnDistance);
            btnSet = (Button) view.findViewById(R.id.btnSet);
            webMap = (WebView) view.findViewById(R.id.webMap);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) view.findViewById(R.nodeId.TextView05);
            btnBack = (Button) view.findViewById(R.id.btnBack);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            intializeVariables();

        }

        private void intializeVariables() {
            swhActive.setText(G.T.getSentence(608801));

        }
    }

    public static final class CO_f_senario_w5 {
        public LinearLayout laySwitcher;
        public Switch swhActive;
        public LinearLayout layOptions;
        public TextView txtWeatherTitle;
        public GridView gridView1;
        public LinearLayout layNavigation;
        public Button btnClose;
        public Button btnBack;
        public Button btnNext;

        public CO_f_senario_w5(Context context) {
            Activity activity = (Activity) context;
            laySwitcher = (LinearLayout) activity.findViewById(R.id.laySwitcher);
            swhActive = (Switch) activity.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            txtWeatherTitle = (TextView) activity.findViewById(R.id.txtWeatherTitle);
            gridView1 = (GridView) activity.findViewById(R.id.gridView1);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnClose = (Button) activity.findViewById(btnCancel);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w5(Dialog dlg) {
            laySwitcher = (LinearLayout) dlg.findViewById(R.id.laySwitcher);
            swhActive = (Switch) dlg.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            txtWeatherTitle = (TextView) dlg.findViewById(R.id.txtWeatherTitle);
            gridView1 = (GridView) dlg.findViewById(R.id.gridView1);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnClose = (Button) dlg.findViewById(btnCancel);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w5(View view) {
            laySwitcher = (LinearLayout) view.findViewById(R.id.laySwitcher);
            swhActive = (Switch) view.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            txtWeatherTitle = (TextView) view.findViewById(R.id.txtWeatherTitle);
            gridView1 = (GridView) view.findViewById(R.id.gridView1);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnClose = (Button) view.findViewById(btnCancel);
            btnBack = (Button) view.findViewById(R.id.btnBack);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_senario_w6 {
        public LinearLayout layOptions;
        public ScrollView scrollView1;
        public RadioButton chkAnd;
        public TextView txtAndHeader;
        public TextView txtAndDescription;
        public RadioButton chkOr;
        public TextView txtOrHeader;
        public TextView txtOrDescription;
        public LinearLayout layNavigation;
        public Button btnClose;
        public TextView textView1;
        public Button btnBack;
        public Button btnNext;

        public CO_f_senario_w6(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            scrollView1 = (ScrollView) activity.findViewById(R.id.scrollView1);
            chkAnd = (RadioButton) activity.findViewById(R.id.chkAnd);
            txtAndHeader = (TextView) activity.findViewById(R.id.txtAndHeader);
            txtAndDescription = (TextView) activity.findViewById(R.id.txtAndDescription);
            chkOr = (RadioButton) activity.findViewById(R.id.chkOr);
            txtOrHeader = (TextView) activity.findViewById(R.id.txtOrHeader);
            txtOrDescription = (TextView) activity.findViewById(R.id.txtOrDescription);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnClose = (Button) activity.findViewById(btnCancel);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w6(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            scrollView1 = (ScrollView) dlg.findViewById(R.id.scrollView1);
            chkAnd = (RadioButton) dlg.findViewById(R.id.chkAnd);
            txtAndHeader = (TextView) dlg.findViewById(R.id.txtAndHeader);
            txtAndDescription = (TextView) dlg.findViewById(R.id.txtAndDescription);
            chkOr = (RadioButton) dlg.findViewById(R.id.chkOr);
            txtOrHeader = (TextView) dlg.findViewById(R.id.txtOrHeader);
            txtOrDescription = (TextView) dlg.findViewById(R.id.txtOrDescription);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnClose = (Button) dlg.findViewById(btnCancel);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w6(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            scrollView1 = (ScrollView) view.findViewById(R.id.scrollView1);
            chkAnd = (RadioButton) view.findViewById(R.id.chkAnd);
            txtAndHeader = (TextView) view.findViewById(R.id.txtAndHeader);
            txtAndDescription = (TextView) view.findViewById(R.id.txtAndDescription);
            chkOr = (RadioButton) view.findViewById(R.id.chkOr);
            txtOrHeader = (TextView) view.findViewById(R.id.txtOrHeader);
            txtOrDescription = (TextView) view.findViewById(R.id.txtOrDescription);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnClose = (Button) view.findViewById(btnCancel);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            btnBack = (Button) view.findViewById(R.id.btnBack);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_d_add_item_to_expandable {
        public LinearLayout lay_section;
        public TextView txtSection;
        public LinearLayout layRoom;
        public TextView txtRoom;
        public LinearLayout layNode;
        public TextView txtNode;

        public CO_d_add_item_to_expandable(Context context) {
            Activity activity = (Activity) context;
            lay_section = (LinearLayout) activity.findViewById(R.id.lay_section);
            txtSection = (TextView) activity.findViewById(R.id.txtSection);
            layRoom = (LinearLayout) activity.findViewById(R.id.layRoom);
            txtRoom = (TextView) activity.findViewById(R.id.txtRoom);
            layNode = (LinearLayout) activity.findViewById(R.id.layNode);
            txtNode = (TextView) activity.findViewById(R.id.txtNode);
            intializeVariables();

        }

        public CO_d_add_item_to_expandable(Dialog dlg) {
            lay_section = (LinearLayout) dlg.findViewById(R.id.lay_section);
            txtSection = (TextView) dlg.findViewById(R.id.txtSection);
            layRoom = (LinearLayout) dlg.findViewById(R.id.layRoom);
            txtRoom = (TextView) dlg.findViewById(R.id.txtRoom);
            layNode = (LinearLayout) dlg.findViewById(R.id.layNode);
            txtNode = (TextView) dlg.findViewById(R.id.txtNode);
            intializeVariables();

        }

        public CO_d_add_item_to_expandable(View view) {
            lay_section = (LinearLayout) view.findViewById(R.id.lay_section);
            txtSection = (TextView) view.findViewById(R.id.txtSection);
            layRoom = (LinearLayout) view.findViewById(R.id.layRoom);
            txtRoom = (TextView) view.findViewById(R.id.txtRoom);
            layNode = (LinearLayout) view.findViewById(R.id.layNode);
            txtNode = (TextView) view.findViewById(R.id.txtNode);
            intializeVariables();

        }

        private void intializeVariables() {
            txtSection.setText(G.T.getSentence(155));
            txtRoom.setText(G.T.getSentence(156));
            txtNode.setText(G.T.getSentence(151));
        }
    }

    public static final class CO_f_senario_w7 {
        public LinearLayout layOptions;
        public ScrollView scrollView1;
        public TextView txtHeaderText;
        public TextView txtlblKeyword;
        public EditText edtKeyword;
        public TextView txtlblEnableKey;
        public TextView txtEnableKey;
        public TextView txtDisableKey;
        public TextView txtlblStartKey;
        public TextView txtlblDisableKey;
        public TextView txtStartKey;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView textView1;
        public Button btnBack;
        public Button btnNext;

        public CO_f_senario_w7(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            scrollView1 = (ScrollView) activity.findViewById(R.id.scrollView1);
            txtHeaderText = (TextView) activity.findViewById(R.id.txtHeaderText);
            txtlblKeyword = (TextView) activity.findViewById(R.id.txtlblKeyword);
            edtKeyword = (EditText) activity.findViewById(R.id.edtKeyword);
            txtlblEnableKey = (TextView) activity.findViewById(R.id.txtlblEnableKey);
            txtEnableKey = (TextView) activity.findViewById(R.id.txtEnableKey);
            txtDisableKey = (TextView) activity.findViewById(R.id.txtDisableKey);
            txtlblStartKey = (TextView) activity.findViewById(R.id.txtlblStartKey);
            txtlblDisableKey = (TextView) activity.findViewById(R.id.txtlblDisableKey);
            txtStartKey = (TextView) activity.findViewById(R.id.txtStartKey);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w7(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            scrollView1 = (ScrollView) dlg.findViewById(R.id.scrollView1);
            txtHeaderText = (TextView) dlg.findViewById(R.id.txtHeaderText);
            txtlblKeyword = (TextView) dlg.findViewById(R.id.txtlblKeyword);
            edtKeyword = (EditText) dlg.findViewById(R.id.edtKeyword);
            txtlblEnableKey = (TextView) dlg.findViewById(R.id.txtlblEnableKey);
            txtEnableKey = (TextView) dlg.findViewById(R.id.txtEnableKey);
            txtDisableKey = (TextView) dlg.findViewById(R.id.txtDisableKey);
            txtlblStartKey = (TextView) dlg.findViewById(R.id.txtlblStartKey);
            txtlblDisableKey = (TextView) dlg.findViewById(R.id.txtlblDisableKey);
            txtStartKey = (TextView) dlg.findViewById(R.id.txtStartKey);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w7(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            scrollView1 = (ScrollView) view.findViewById(R.id.scrollView1);
            txtHeaderText = (TextView) view.findViewById(R.id.txtHeaderText);
            txtlblKeyword = (TextView) view.findViewById(R.id.txtlblKeyword);
            edtKeyword = (EditText) view.findViewById(R.id.edtKeyword);
            txtlblEnableKey = (TextView) view.findViewById(R.id.txtlblEnableKey);
            txtEnableKey = (TextView) view.findViewById(R.id.txtEnableKey);
            txtDisableKey = (TextView) view.findViewById(R.id.txtDisableKey);
            txtlblStartKey = (TextView) view.findViewById(R.id.txtlblStartKey);
            txtlblDisableKey = (TextView) view.findViewById(R.id.txtlblDisableKey);
            txtStartKey = (TextView) view.findViewById(R.id.txtStartKey);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            btnBack = (Button) view.findViewById(R.id.btnBack);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_senario_w8 {
        public LinearLayout laySwitcher;
        public Switch swhActive;
        public ListView listView1;
        //        public ScrollView                                              scrollView1;
//        public ir.parsansoft.app.ihs.center.components.ComNodeSelector comNodeSelector1;
//        public LinearLayout                                            layPerOperandValueRange;
        public LinearLayout layOptions;
        //        public LinearLayout                                            layParameters;
//        public TextView                                                txtNumericValue;
//        public EditText                                                edtLogicalValue;
//        public LinearLayout                                            layPerOperandValueSelective;
//        public TextView                                                txtSelectiveValue;
//        public Spinner                                                 spnSelectiveValue;
//        public LinearLayout                                            layMainReverse;
//        public CheckBox                                                chkReverse;
//        public LinearLayout                                            layReverse;
//        public TextView                                                txtReverseTime;
//        public EditText                                                edtReverseTime;
//        public Spinner                                                 spnTimeUnit;
        public Button btnAdd;
        public LinearLayout layNavigation;
        public Button btnClose;
        public TextView textView1;
        public Button btnBack;
        public Button btnNext;

        public CO_f_senario_w8(Context context) {
            Activity activity = (Activity) context;
            laySwitcher = (LinearLayout) activity.findViewById(R.id.laySwitcher);
            swhActive = (Switch) activity.findViewById(R.id.swhActive);
            listView1 = (ListView) activity.findViewById(R.id.lstLanguage);
//            scrollView1 = (ScrollView) activity.findViewById(R.nodeId.scrollView1);
//            comNodeSelector1 = (ir.parsansoft.app.ihs.center.components.ComNodeSelector) activity.findViewById(R.nodeId.comNodeSelector1);
//            layPerOperandValueRange = (LinearLayout) activity.findViewById(R.nodeId.layPerOperandValueRange);
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
//            layParameters = (LinearLayout) activity.findViewById(R.nodeId.layParameters);
//            txtNumericValue = (TextView) activity.findViewById(R.nodeId.txtNumericValue);
//            edtLogicalValue = (EditText) activity.findViewById(R.nodeId.edtLogicalValue);
//            layPerOperandValueSelective = (LinearLayout) activity.findViewById(R.nodeId.layPerOperandValueSelective);
//            txtSelectiveValue = (TextView) activity.findViewById(R.nodeId.txtSelectiveValue);
//            spnSelectiveValue = (Spinner) activity.findViewById(R.nodeId.spnSelectiveValue);
//            layMainReverse = (LinearLayout) activity.findViewById(R.nodeId.layMainReverse);
//            chkReverse = (CheckBox) activity.findViewById(R.nodeId.chkReverse);
//            layReverse = (LinearLayout) activity.findViewById(R.nodeId.layReverse);
//            txtReverseTime = (TextView) activity.findViewById(R.nodeId.txtReverseTime);
//            edtReverseTime = (EditText) activity.findViewById(R.nodeId.edtReverseTime);
//            spnTimeUnit = (Spinner) activity.findViewById(R.nodeId.spnTimeUnit);
            btnAdd = (Button) activity.findViewById(R.id.btnAdd);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnClose = (Button) activity.findViewById(btnCancel);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w8(Dialog dlg) {
            laySwitcher = (LinearLayout) dlg.findViewById(R.id.laySwitcher);
            swhActive = (Switch) dlg.findViewById(R.id.swhActive);
            listView1 = (ListView) dlg.findViewById(R.id.lstLanguage);
//            scrollView1 = (ScrollView) dlg.findViewById(R.nodeId.scrollView1);
//            comNodeSelector1 = (ir.parsansoft.app.ihs.center.components.ComNodeSelector) dlg.findViewById(R.nodeId.comNodeSelector1);
//            layPerOperandValueRange = (LinearLayout) dlg.findViewById(R.nodeId.layPerOperandValueRange);
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
//            layParameters = (LinearLayout) dlg.findViewById(R.nodeId.layParameters);
//            txtNumericValue = (TextView) dlg.findViewById(R.nodeId.txtNumericValue);
//            edtLogicalValue = (EditText) dlg.findViewById(R.nodeId.edtLogicalValue);
//            layPerOperandValueSelective = (LinearLayout) dlg.findViewById(R.nodeId.layPerOperandValueSelective);
//            txtSelectiveValue = (TextView) dlg.findViewById(R.nodeId.txtSelectiveValue);
//            spnSelectiveValue = (Spinner) dlg.findViewById(R.nodeId.spnSelectiveValue);
//            layMainReverse = (LinearLayout) dlg.findViewById(R.nodeId.layMainReverse);
//            chkReverse = (CheckBox) dlg.findViewById(R.nodeId.chkReverse);
//            layReverse = (LinearLayout) dlg.findViewById(R.nodeId.layReverse);
//            txtReverseTime = (TextView) dlg.findViewById(R.nodeId.txtReverseTime);
//            edtReverseTime = (EditText) dlg.findViewById(R.nodeId.edtReverseTime);
//            spnTimeUnit = (Spinner) dlg.findViewById(R.nodeId.spnTimeUnit);
            btnAdd = (Button) dlg.findViewById(R.id.btnAdd);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnClose = (Button) dlg.findViewById(btnCancel);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w8(View view) {
            laySwitcher = (LinearLayout) view.findViewById(R.id.laySwitcher);
            swhActive = (Switch) view.findViewById(R.id.swhActive);
            listView1 = (ListView) view.findViewById(R.id.lstLanguage);
//            scrollView1 = (ScrollView) view.findViewById(R.nodeId.scrollView1);
//            comNodeSelector1 = (ir.parsansoft.app.ihs.center.components.ComNodeSelector) view.findViewById(R.nodeId.comNodeSelector1);
//            layPerOperandValueRange = (LinearLayout) view.findViewById(R.nodeId.layPerOperandValueRange);
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
//            layParameters = (LinearLayout) view.findViewById(R.nodeId.layParameters);
//            txtNumericValue = (TextView) view.findViewById(R.nodeId.txtNumericValue);
//            edtLogicalValue = (EditText) view.findViewById(R.nodeId.edtLogicalValue);
//            layPerOperandValueSelective = (LinearLayout) view.findViewById(R.nodeId.layPerOperandValueSelective);
//            txtSelectiveValue = (TextView) view.findViewById(R.nodeId.txtSelectiveValue);
//            spnSelectiveValue = (Spinner) view.findViewById(R.nodeId.spnSelectiveValue);
//            layMainReverse = (LinearLayout) view.findViewById(R.nodeId.layMainReverse);
//            chkReverse = (CheckBox) view.findViewById(R.nodeId.chkReverse);
//            layReverse = (LinearLayout) view.findViewById(R.nodeId.layReverse);
//            txtReverseTime = (TextView) view.findViewById(R.nodeId.txtReverseTime);
//            edtReverseTime = (EditText) view.findViewById(R.nodeId.edtReverseTime);
//            spnTimeUnit = (Spinner) view.findViewById(R.nodeId.spnTimeUnit);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnClose = (Button) view.findViewById(btnCancel);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            btnBack = (Button) view.findViewById(R.id.btnBack);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_senario_w9 {
        public LinearLayout laySwitcher;
        public Switch swhActive;
        public LinearLayout layOptions;
        public TextView txtTitle;
        public ListView lstMobileDevices;
        public Button btnAdd;
        public TextView txtMessageTitle;
        public EditText edtAlarmText;
        public LinearLayout layNavigation;
        public Button btnCancel;
        public TextView TextView05;
        public Button btnBack;
        public Button btnNext;

        public CO_f_senario_w9(Context context) {
            Activity activity = (Activity) context;
            laySwitcher = (LinearLayout) activity.findViewById(R.id.laySwitcher);
            swhActive = (Switch) activity.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            lstMobileDevices = (ListView) activity.findViewById(R.id.lstUser);
            btnAdd = (Button) activity.findViewById(R.id.btnAdd);
            txtMessageTitle = (TextView) activity.findViewById(R.id.txtMessageTitle);
            edtAlarmText = (EditText) activity.findViewById(R.id.edtAlarmText);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) activity.findViewById(R.nodeId.TextView05);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w9(Dialog dlg) {
            laySwitcher = (LinearLayout) dlg.findViewById(R.id.laySwitcher);
            swhActive = (Switch) dlg.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            lstMobileDevices = (ListView) dlg.findViewById(R.id.lstUser);
            btnAdd = (Button) dlg.findViewById(R.id.btnAdd);
            txtMessageTitle = (TextView) dlg.findViewById(R.id.txtMessageTitle);
            edtAlarmText = (EditText) dlg.findViewById(R.id.edtAlarmText);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnCancel = (Button) dlg.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) dlg.findViewById(R.nodeId.TextView05);
            btnBack = (Button) dlg.findViewById(R.id.btnBack);
            btnNext = (Button) dlg.findViewById(R.id.btnNext);
            intializeVariables();

        }

        public CO_f_senario_w9(View view) {
            laySwitcher = (LinearLayout) view.findViewById(R.id.laySwitcher);
            swhActive = (Switch) view.findViewById(R.id.swhActive);
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            lstMobileDevices = (ListView) view.findViewById(R.id.lstUser);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            txtMessageTitle = (TextView) view.findViewById(R.id.txtMessageTitle);
            edtAlarmText = (EditText) view.findViewById(R.id.edtAlarmText);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            //TextView05 = (TextView) view.findViewById(R.nodeId.TextView05);
            btnBack = (Button) view.findViewById(R.id.btnBack);
            btnNext = (Button) view.findViewById(R.id.btnNext);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_setting_network {
        public ListView lstWiFi;
        public LinearLayout layIP;
        public TextView txtSSID;
        public TextView txtSSIDName;
        public TextView txtIpAddress;
        public EditText edtIP;
        public TextView txtSubnet;
        public EditText edtSubnet;
        public TextView txtGateway;
        public EditText edtGateway;
        public TextView txtDNS1;
        public EditText edtDNS1;
        public Button btnSave;

        public CO_f_setting_network(Context context) {
            Activity activity = (Activity) context;
            lstWiFi = (ListView) activity.findViewById(R.id.lstWiFi);
            layIP = (LinearLayout) activity.findViewById(R.id.layIP);
            txtSSID = (TextView) activity.findViewById(R.id.txtSSID);
            txtSSIDName = (TextView) activity.findViewById(R.id.txtSSIDName);
            txtIpAddress = (TextView) activity.findViewById(R.id.txtIpAddress);
            edtIP = (EditText) activity.findViewById(R.id.edtIP);
            txtSubnet = (TextView) activity.findViewById(R.id.txtSubnet);
            edtSubnet = (EditText) activity.findViewById(R.id.edtSubnet);
            txtGateway = (TextView) activity.findViewById(R.id.txtGateway);
            edtGateway = (EditText) activity.findViewById(R.id.edtGateway);
            txtDNS1 = (TextView) activity.findViewById(R.id.txtDNS1);
            edtDNS1 = (EditText) activity.findViewById(R.id.edtDNS1);
            btnSave = (Button) activity.findViewById(R.id.btnSave);
            intializeVariables();

        }

        public CO_f_setting_network(Dialog dlg) {
            lstWiFi = (ListView) dlg.findViewById(R.id.lstWiFi);
            layIP = (LinearLayout) dlg.findViewById(R.id.layIP);
            txtSSID = (TextView) dlg.findViewById(R.id.txtSSID);
            txtSSIDName = (TextView) dlg.findViewById(R.id.txtSSIDName);
            txtIpAddress = (TextView) dlg.findViewById(R.id.txtIpAddress);
            edtIP = (EditText) dlg.findViewById(R.id.edtIP);
            txtSubnet = (TextView) dlg.findViewById(R.id.txtSubnet);
            edtSubnet = (EditText) dlg.findViewById(R.id.edtSubnet);
            txtGateway = (TextView) dlg.findViewById(R.id.txtGateway);
            edtGateway = (EditText) dlg.findViewById(R.id.edtGateway);
            txtDNS1 = (TextView) dlg.findViewById(R.id.txtDNS1);
            edtDNS1 = (EditText) dlg.findViewById(R.id.edtDNS1);
            btnSave = (Button) dlg.findViewById(R.id.btnSave);
            intializeVariables();

        }

        public CO_f_setting_network(View view) {
            lstWiFi = (ListView) view.findViewById(R.id.lstWiFi);
            layIP = (LinearLayout) view.findViewById(R.id.layIP);
            txtSSID = (TextView) view.findViewById(R.id.txtSSID);
            txtSSIDName = (TextView) view.findViewById(R.id.txtSSIDName);
            txtIpAddress = (TextView) view.findViewById(R.id.txtIpAddress);
            edtIP = (EditText) view.findViewById(R.id.edtIP);
            txtSubnet = (TextView) view.findViewById(R.id.txtSubnet);
            edtSubnet = (EditText) view.findViewById(R.id.edtSubnet);
            txtGateway = (TextView) view.findViewById(R.id.txtGateway);
            edtGateway = (EditText) view.findViewById(R.id.edtGateway);
            txtDNS1 = (TextView) view.findViewById(R.id.txtDNS1);
            edtDNS1 = (EditText) view.findViewById(R.id.edtDNS1);
            btnSave = (Button) view.findViewById(R.id.btnSave);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_l_simple_list_item {
        public LinearLayout layBack;
        public TextView txtName;
        public ImageView imgDelete;

        public CO_l_simple_list_item(Context context) {
            Activity activity = (Activity) context;
            layBack = (LinearLayout) activity.findViewById(R.id.layBack);
            txtName = (TextView) activity.findViewById(R.id.txtName);
            imgDelete = (ImageView) activity.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        public CO_l_simple_list_item(Dialog dlg) {
            layBack = (LinearLayout) dlg.findViewById(R.id.layBack);
            txtName = (TextView) dlg.findViewById(R.id.txtName);
            imgDelete = (ImageView) dlg.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        public CO_l_simple_list_item(View view) {
            layBack = (LinearLayout) view.findViewById(R.id.layBack);
            txtName = (TextView) view.findViewById(R.id.txtName);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_l_setting_wifi_signal {
        public LinearLayout layBack;
        public ImageView imgIcon;
        public TextView txtSSID;
        public TextView txtDes;
        public Button btnConnect;

        public CO_l_setting_wifi_signal(Context context) {
            Activity activity = (Activity) context;
            layBack = (LinearLayout) activity.findViewById(R.id.layBack);
            imgIcon = (ImageView) activity.findViewById(R.id.imgIcon);
            txtSSID = (TextView) activity.findViewById(R.id.txtSSID);
            txtDes = (TextView) activity.findViewById(R.id.txtDes);
            btnConnect = (Button) activity.findViewById(R.id.btnConnect);
            intializeVariables();

        }

        public CO_l_setting_wifi_signal(Dialog dlg) {
            layBack = (LinearLayout) dlg.findViewById(R.id.layBack);
            imgIcon = (ImageView) dlg.findViewById(R.id.imgIcon);
            txtSSID = (TextView) dlg.findViewById(R.id.txtSSID);
            txtDes = (TextView) dlg.findViewById(R.id.txtDes);
            btnConnect = (Button) dlg.findViewById(R.id.btnConnect);
            intializeVariables();

        }

        public CO_l_setting_wifi_signal(View view) {
            layBack = (LinearLayout) view.findViewById(R.id.layBack);
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            txtSSID = (TextView) view.findViewById(R.id.txtSSID);
            txtDes = (TextView) view.findViewById(R.id.txtDes);
            btnConnect = (Button) view.findViewById(R.id.btnConnect);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_d_simple_edit_text {
        public LinearLayout layOptions;
        public TextView txtTitle;
        public TextView txtBody;
        public EditText editText1;
        public LinearLayout layNavigation;
        public Button btnNegative;
        public TextView textView1;
        public Button btnPositive;

        public CO_d_simple_edit_text(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            txtBody = (TextView) activity.findViewById(R.id.txtBody);
            editText1 = (EditText) activity.findViewById(R.id.editText1);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
            btnNegative = (Button) activity.findViewById(R.id.btnNegative);
            textView1 = (TextView) activity.findViewById(R.id.txtLanguage);
            btnPositive = (Button) activity.findViewById(R.id.btnPositive);
            intializeVariables();

        }

        public CO_d_simple_edit_text(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            txtTitle = (TextView) dlg.findViewById(R.id.txtTitle);
            txtBody = (TextView) dlg.findViewById(R.id.txtBody);
            editText1 = (EditText) dlg.findViewById(R.id.editText1);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
            btnNegative = (Button) dlg.findViewById(R.id.btnNegative);
            textView1 = (TextView) dlg.findViewById(R.id.txtLanguage);
            btnPositive = (Button) dlg.findViewById(R.id.btnPositive);
            intializeVariables();

        }

        public CO_d_simple_edit_text(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtBody = (TextView) view.findViewById(R.id.txtBody);
            editText1 = (EditText) view.findViewById(R.id.editText1);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
            btnNegative = (Button) view.findViewById(R.id.btnNegative);
            textView1 = (TextView) view.findViewById(R.id.txtLanguage);
            btnPositive = (Button) view.findViewById(R.id.btnPositive);
            intializeVariables();

        }

        private void intializeVariables() {
            btnNegative.setText(G.T.getSentence(104));
            btnPositive.setText(G.T.getSentence(103));
        }
    }

    public static final class CO_l_list_scemario {
        public LinearLayout layBack;
        public Switch swhEnabled;
        public TextView txtName;
        public ImageView imgBtnEdit;
        public ImageView imgRun;
        public ImageView imgBtnDelete;

        public CO_l_list_scemario(Context context) {
            Activity activity = (Activity) context;
            layBack = (LinearLayout) activity.findViewById(R.id.layBack);
            swhEnabled = (Switch) activity.findViewById(R.id.swhEnabled);
            txtName = (TextView) activity.findViewById(R.id.txtName);
            imgBtnEdit = (ImageView) activity.findViewById(R.id.imgBtnEdit);
            imgRun = (ImageView) activity.findViewById(R.id.imgRun);
            imgBtnDelete = (ImageView) activity.findViewById(R.id.imgBtnDelete);
            intializeVariables();
        }

        public CO_l_list_scemario(Dialog dlg) {
            layBack = (LinearLayout) dlg.findViewById(R.id.layBack);
            swhEnabled = (Switch) dlg.findViewById(R.id.swhEnabled);
            txtName = (TextView) dlg.findViewById(R.id.txtName);
            imgBtnEdit = (ImageView) dlg.findViewById(R.id.imgBtnEdit);
            imgRun = (ImageView) dlg.findViewById(R.id.imgRun);
            imgBtnDelete = (ImageView) dlg.findViewById(R.id.imgBtnDelete);
            intializeVariables();
        }

        public CO_l_list_scemario(View view) {
            layBack = (LinearLayout) view.findViewById(R.id.layBack);
            swhEnabled = (Switch) view.findViewById(R.id.swhEnabled);
            txtName = (TextView) view.findViewById(R.id.txtName);
            imgBtnEdit = (ImageView) view.findViewById(R.id.imgBtnEdit);
            imgRun = (ImageView) view.findViewById(R.id.imgRun);
            imgBtnDelete = (ImageView) view.findViewById(R.id.imgBtnDelete);
            intializeVariables();
        }

        private void intializeVariables() {
        }
    }

    public static final class CO_l_list_scemario_detail_items {
        public LinearLayout layBack;
        public ImageView imgIcon;
        public TextView txtName;
        public TextView txtValue;

        public CO_l_list_scemario_detail_items(Context context) {
            Activity activity = (Activity) context;
            layBack = (LinearLayout) activity.findViewById(R.id.layBack);
            imgIcon = (ImageView) activity.findViewById(R.id.imgIcon);
            txtName = (TextView) activity.findViewById(R.id.txtName);
            txtValue = (TextView) activity.findViewById(R.id.txtValue);
            intializeVariables();
        }

        public CO_l_list_scemario_detail_items(Dialog dlg) {
            layBack = (LinearLayout) dlg.findViewById(R.id.layBack);
            imgIcon = (ImageView) dlg.findViewById(R.id.imgIcon);
            txtName = (TextView) dlg.findViewById(R.id.txtName);
            txtValue = (TextView) dlg.findViewById(R.id.txtValue);
            intializeVariables();

        }

        public CO_l_list_scemario_detail_items(View view) {
            layBack = (LinearLayout) view.findViewById(R.id.layBack);
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtValue = (TextView) view.findViewById(R.id.txtValue);
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_f_browser {
        public Button btnBrowserBack;
        public Button btnBrowserForward;
        public Button btnBrowserRefresh;
        public Button btnBrowserHome;
        public Button btnClose;
        public ProgressBar prgProgress;
        public WebView wvSMSPannel;

        public CO_f_browser(Context context) {
            Activity activity = (Activity) context;
            btnBrowserBack = (Button) activity.findViewById(R.id.btnBrowserBack);
            btnBrowserForward = (Button) activity.findViewById(R.id.btnBrowserForward);
            btnBrowserRefresh = (Button) activity.findViewById(R.id.btnBrowserRefresh);
            btnBrowserHome = (Button) activity.findViewById(R.id.btnBrowserHome);
            btnClose = (Button) activity.findViewById(R.id.btnClose);
            prgProgress = (ProgressBar) activity.findViewById(R.id.prgProgress);
            wvSMSPannel = (WebView) activity.findViewById(R.id.wvSMSPannel);
            intializeVariables();

        }

        public CO_f_browser(Dialog dlg) {
            btnBrowserBack = (Button) dlg.findViewById(R.id.btnBrowserBack);
            btnBrowserForward = (Button) dlg.findViewById(R.id.btnBrowserForward);
            btnBrowserRefresh = (Button) dlg.findViewById(R.id.btnBrowserRefresh);
            btnBrowserHome = (Button) dlg.findViewById(R.id.btnBrowserHome);
            btnClose = (Button) dlg.findViewById(R.id.btnClose);
            prgProgress = (ProgressBar) dlg.findViewById(R.id.prgProgress);
            wvSMSPannel = (WebView) dlg.findViewById(R.id.wvSMSPannel);
            intializeVariables();

        }

        public CO_f_browser(View view) {
            btnBrowserBack = (Button) view.findViewById(R.id.btnBrowserBack);
            btnBrowserForward = (Button) view.findViewById(R.id.btnBrowserForward);
            btnBrowserRefresh = (Button) view.findViewById(R.id.btnBrowserRefresh);
            btnBrowserHome = (Button) view.findViewById(R.id.btnBrowserHome);
            btnClose = (Button) view.findViewById(R.id.btnClose);
            prgProgress = (ProgressBar) view.findViewById(R.id.prgProgress);
            wvSMSPannel = (WebView) view.findViewById(R.id.wvSMSPannel);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_l_list_sections_room {
        public LinearLayout layBack;
        public ImageView imgIcon;
        public TextView lblName;
        public ImageView imgEdit;
        public ImageView imgDelete;

        public CO_l_list_sections_room(Context context) {
            Activity activity = (Activity) context;
            layBack = (LinearLayout) activity.findViewById(R.id.layBack);
            imgIcon = (ImageView) activity.findViewById(R.id.imgIcon);
            lblName = (TextView) activity.findViewById(R.id.txtUsername);
            imgEdit = (ImageView) activity.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) activity.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        public CO_l_list_sections_room(Dialog dlg) {
            layBack = (LinearLayout) dlg.findViewById(R.id.layBack);
            imgIcon = (ImageView) dlg.findViewById(R.id.imgIcon);
            lblName = (TextView) dlg.findViewById(R.id.txtUsername);
            imgEdit = (ImageView) dlg.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) dlg.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        public CO_l_list_sections_room(View view) {
            layBack = (LinearLayout) view.findViewById(R.id.layBack);
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            lblName = (TextView) view.findViewById(R.id.txtUsername);
            imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_l_list_sections_section {
        public LinearLayout layBack;
        public ImageView imgIcon;
        public TextView lblName;
        public TextView txtFill;
        public ImageView imgEdit;
        public ImageView imgDelete;

        public CO_l_list_sections_section(Context context) {
            Activity activity = (Activity) context;
            layBack = (LinearLayout) activity.findViewById(R.id.layBack);
            imgIcon = (ImageView) activity.findViewById(R.id.imgIcon);
            lblName = (TextView) activity.findViewById(R.id.txtUsername);
            txtFill = (TextView) activity.findViewById(R.id.txtFill);
            imgEdit = (ImageView) activity.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) activity.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        public CO_l_list_sections_section(Dialog dlg) {
            layBack = (LinearLayout) dlg.findViewById(R.id.layBack);
            imgIcon = (ImageView) dlg.findViewById(R.id.imgIcon);
            lblName = (TextView) dlg.findViewById(R.id.txtUsername);
            txtFill = (TextView) dlg.findViewById(R.id.txtFill);
            imgEdit = (ImageView) dlg.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) dlg.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        public CO_l_list_sections_section(View view) {
            layBack = (LinearLayout) view.findViewById(R.id.layBack);
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            lblName = (TextView) view.findViewById(R.id.txtUsername);
            txtFill = (TextView) view.findViewById(R.id.txtFill);
            imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_l_node_simple_dimmer {

        public LinearLayout layDimmerOne, layGray1, layGreen1;
        public ViewGroup layTouch1;
        public ImageView imgGray1, imgGreen1;
        public LinearLayout layTouch2, layDimmerTwo, layGray2, layGreen2;
        public ImageView imgGray2, imgGreen2;
        public TextView txtlbl1, txtlbl2;
        public TextView txtName1, txtName2;
        public LinearLayout laySetting;
        public ProgressBar prgDoOperation;
        public TextView txtNodeName;
        public ImageView imgFavorites;
        public ImageView imgbtnEdit;

        public CO_l_node_simple_dimmer(Context context) {
            Activity activity = (Activity) context;
            laySetting = (LinearLayout) activity.findViewById(R.id.laySetting);
            prgDoOperation = (ProgressBar) activity.findViewById(R.id.prgDoOperation);
            txtNodeName = (TextView) activity.findViewById(R.id.txtNodeName);
            imgFavorites = (ImageView) activity.findViewById(R.id.imgFavorites);
            imgbtnEdit = (ImageView) activity.findViewById(R.id.imgbtnEdit);
            layTouch1 = (ViewGroup) activity.findViewById(R.id.layTouch1);
            layDimmerOne = (LinearLayout) activity.findViewById(R.id.dimmerOne);
            layGray1 = (LinearLayout) activity.findViewById(R.id.layGray1);
            layGreen1 = (LinearLayout) activity.findViewById(R.id.layGreen1);
            imgGray1 = (ImageView) activity.findViewById(R.id.imgGray1);
            imgGreen1 = (ImageView) activity.findViewById(R.id.imgGreen1);
            txtlbl1 = (TextView) activity.findViewById(R.id.txtlbl1);
            layTouch2 = (LinearLayout) activity.findViewById(R.id.layTouch2);
            layDimmerTwo = (LinearLayout) activity.findViewById(R.id.dimmerTwo);
            layGray2 = (LinearLayout) activity.findViewById(R.id.layGray2);
            layGreen2 = (LinearLayout) activity.findViewById(R.id.layGreen2);
            imgGray2 = (ImageView) activity.findViewById(R.id.imgGray2);
            imgGreen2 = (ImageView) activity.findViewById(R.id.imgGreen2);
            txtlbl2 = (TextView) activity.findViewById(R.id.txtlbl2);
            txtName1 = (TextView) activity.findViewById(R.id.txtName1);
            txtName2 = (TextView) activity.findViewById(R.id.txtName2);
            intializeVariables();
        }

        public CO_l_node_simple_dimmer(Dialog dlg) {
            laySetting = (LinearLayout) dlg.findViewById(R.id.laySetting);
            prgDoOperation = (ProgressBar) dlg.findViewById(R.id.prgDoOperation);
            txtNodeName = (TextView) dlg.findViewById(R.id.txtNodeName);
            imgFavorites = (ImageView) dlg.findViewById(R.id.imgFavorites);
            imgbtnEdit = (ImageView) dlg.findViewById(R.id.imgbtnEdit);
            layTouch1 = (ViewGroup) dlg.findViewById(R.id.layTouch1);
            layDimmerOne = (LinearLayout) dlg.findViewById(R.id.dimmerOne);
            layGray1 = (LinearLayout) dlg.findViewById(R.id.layGray1);
            layGreen1 = (LinearLayout) dlg.findViewById(R.id.layGreen1);
            imgGray1 = (ImageView) dlg.findViewById(R.id.imgGray1);
            imgGreen1 = (ImageView) dlg.findViewById(R.id.imgGreen1);
            txtlbl1 = (TextView) dlg.findViewById(R.id.txtlbl1);
            layTouch2 = (LinearLayout) dlg.findViewById(R.id.layTouch2);
            layDimmerTwo = (LinearLayout) dlg.findViewById(R.id.dimmerTwo);
            layGray2 = (LinearLayout) dlg.findViewById(R.id.layGray2);
            layGreen2 = (LinearLayout) dlg.findViewById(R.id.layGreen2);
            imgGray2 = (ImageView) dlg.findViewById(R.id.imgGray2);
            imgGreen2 = (ImageView) dlg.findViewById(R.id.imgGreen2);
            txtlbl2 = (TextView) dlg.findViewById(R.id.txtlbl2);
            txtName1 = (TextView) dlg.findViewById(R.id.txtName1);
            txtName2 = (TextView) dlg.findViewById(R.id.txtName2);
            intializeVariables();
        }

        public CO_l_node_simple_dimmer(View view) {
            laySetting = (LinearLayout) view.findViewById(R.id.laySetting);
            prgDoOperation = (ProgressBar) view.findViewById(R.id.prgDoOperation);
            txtNodeName = (TextView) view.findViewById(R.id.txtNodeName);
            imgFavorites = (ImageView) view.findViewById(R.id.imgFavorites);
            imgbtnEdit = (ImageView) view.findViewById(R.id.imgbtnEdit);
            layTouch1 = (ViewGroup) view.findViewById(R.id.layTouch1);
            layDimmerOne = (LinearLayout) view.findViewById(R.id.dimmerOne);
            layGray1 = (LinearLayout) view.findViewById(R.id.layGray1);
            layGreen1 = (LinearLayout) view.findViewById(R.id.layGreen1);
            imgGray1 = (ImageView) view.findViewById(R.id.imgGray1);
            imgGreen1 = (ImageView) view.findViewById(R.id.imgGreen1);
            txtlbl1 = (TextView) view.findViewById(R.id.txtlbl1);
            layTouch2 = (LinearLayout) view.findViewById(R.id.layTouch2);
            layDimmerTwo = (LinearLayout) view.findViewById(R.id.dimmerTwo);
            layGray2 = (LinearLayout) view.findViewById(R.id.layGray2);
            layGreen2 = (LinearLayout) view.findViewById(R.id.layGreen2);
            imgGray2 = (ImageView) view.findViewById(R.id.imgGray2);
            imgGreen2 = (ImageView) view.findViewById(R.id.imgGreen2);
            txtlbl2 = (TextView) view.findViewById(R.id.txtlbl2);
            txtName1 = (TextView) view.findViewById(R.id.txtName1);
            txtName2 = (TextView) view.findViewById(R.id.txtName2);
            intializeVariables();
        }

        private void intializeVariables() {
        }
    }

    public static final class CO_l_node_simple_key {
        public LinearLayout layKey1;
        public ImageView imgKey1;
        public TextView txtKey1;
        public LinearLayout layKey2;
        public ImageView imgKey2;
        public TextView txtKey2;
        public LinearLayout layKey3;
        public ImageView imgKey3;
        public TextView txtKey3;
        public LinearLayout laySetting;
        public ProgressBar prgDoOperation;
        public TextView txtNodeName;
        public ImageView imgbtnEdit;
        public ImageView imgFavorites;

        public CO_l_node_simple_key(Context context) {
            Activity activity = (Activity) context;
            layKey1 = (LinearLayout) activity.findViewById(R.id.layKey1);
            imgKey1 = (ImageView) activity.findViewById(R.id.imgKey1);
            txtKey1 = (TextView) activity.findViewById(R.id.txtKey1);
            layKey2 = (LinearLayout) activity.findViewById(R.id.layKey2);
            imgKey2 = (ImageView) activity.findViewById(R.id.imgKey2);
            txtKey2 = (TextView) activity.findViewById(R.id.txtKey2);
            layKey3 = (LinearLayout) activity.findViewById(R.id.layKey3);
            imgKey3 = (ImageView) activity.findViewById(R.id.imgKey3);
            txtKey3 = (TextView) activity.findViewById(R.id.txtKey3);
            laySetting = (LinearLayout) activity.findViewById(R.id.laySetting);
            prgDoOperation = (ProgressBar) activity.findViewById(R.id.prgDoOperation);
            txtNodeName = (TextView) activity.findViewById(R.id.txtNodeName);
            imgbtnEdit = (ImageView) activity.findViewById(R.id.imgbtnEdit);
            imgFavorites = (ImageView) activity.findViewById(R.id.imgFavorites);
            intializeVariables();
        }

        public CO_l_node_simple_key(Dialog dlg) {
            layKey1 = (LinearLayout) dlg.findViewById(R.id.layKey1);
            imgKey1 = (ImageView) dlg.findViewById(R.id.imgKey1);
            txtKey1 = (TextView) dlg.findViewById(R.id.txtKey1);
            layKey2 = (LinearLayout) dlg.findViewById(R.id.layKey2);
            imgKey2 = (ImageView) dlg.findViewById(R.id.imgKey2);
            txtKey2 = (TextView) dlg.findViewById(R.id.txtKey2);
            layKey3 = (LinearLayout) dlg.findViewById(R.id.layKey3);
            imgKey3 = (ImageView) dlg.findViewById(R.id.imgKey3);
            txtKey3 = (TextView) dlg.findViewById(R.id.txtKey3);
            laySetting = (LinearLayout) dlg.findViewById(R.id.laySetting);
            prgDoOperation = (ProgressBar) dlg.findViewById(R.id.prgDoOperation);
            txtNodeName = (TextView) dlg.findViewById(R.id.txtNodeName);
            imgbtnEdit = (ImageView) dlg.findViewById(R.id.imgbtnEdit);
            imgFavorites = (ImageView) dlg.findViewById(R.id.imgFavorites);
            intializeVariables();
        }

        public CO_l_node_simple_key(View view) {
            layKey1 = (LinearLayout) view.findViewById(R.id.layKey1);
            imgKey1 = (ImageView) view.findViewById(R.id.imgKey1);
            txtKey1 = (TextView) view.findViewById(R.id.txtKey1);
            layKey2 = (LinearLayout) view.findViewById(R.id.layKey2);
            imgKey2 = (ImageView) view.findViewById(R.id.imgKey2);
            txtKey2 = (TextView) view.findViewById(R.id.txtKey2);
            layKey3 = (LinearLayout) view.findViewById(R.id.layKey3);
            imgKey3 = (ImageView) view.findViewById(R.id.imgKey3);
            txtKey3 = (TextView) view.findViewById(R.id.txtKey3);
            laySetting = (LinearLayout) view.findViewById(R.id.laySetting);
            prgDoOperation = (ProgressBar) view.findViewById(R.id.prgDoOperation);
            txtNodeName = (TextView) view.findViewById(R.id.txtNodeName);
            imgbtnEdit = (ImageView) view.findViewById(R.id.imgbtnEdit);
            imgFavorites = (ImageView) view.findViewById(R.id.imgFavorites);
            intializeVariables();
        }

        private void intializeVariables() {

        }
    }

    public static final class CO_l_node_IoModule {
        public LinearLayout layKey1;
        public ImageView imgKey1;
        public TextView txtKey1;
        public LinearLayout layKey2;
        public ImageView imgKey2;
        public TextView txtKey2;
        public LinearLayout layKey3;
        public ImageView imgKey3;
        public TextView txtKey3;
        public LinearLayout laySetting;
        public ProgressBar prgDoOperation;
        public TextView txtNodeName;
        public ImageView imgbtnEdit;
        public ImageView imgFavorites;
        public LinearLayout layMain;

        public CO_l_node_IoModule(Context context) {
            Activity activity = (Activity) context;
            layKey1 = (LinearLayout) activity.findViewById(R.id.layKey1);
            imgKey1 = (ImageView) activity.findViewById(R.id.imgKey1);
            txtKey1 = (TextView) activity.findViewById(R.id.txtKey1);
            layKey2 = (LinearLayout) activity.findViewById(R.id.layKey2);
            imgKey2 = (ImageView) activity.findViewById(R.id.imgKey2);
            txtKey2 = (TextView) activity.findViewById(R.id.txtKey2);
            layKey3 = (LinearLayout) activity.findViewById(R.id.layKey3);
            imgKey3 = (ImageView) activity.findViewById(R.id.imgKey3);
            txtKey3 = (TextView) activity.findViewById(R.id.txtKey3);
            laySetting = (LinearLayout) activity.findViewById(R.id.laySetting);
            prgDoOperation = (ProgressBar) activity.findViewById(R.id.prgDoOperation);
            txtNodeName = (TextView) activity.findViewById(R.id.txtNodeName);
            imgbtnEdit = (ImageView) activity.findViewById(R.id.imgbtnEdit);
            imgFavorites = (ImageView) activity.findViewById(R.id.imgFavorites);
            layMain = (LinearLayout)activity.findViewById(R.id.layMain);
            intializeVariables();
        }

        public CO_l_node_IoModule(Dialog dlg) {
            layKey1 = (LinearLayout) dlg.findViewById(R.id.layKey1);
            imgKey1 = (ImageView) dlg.findViewById(R.id.imgKey1);
            txtKey1 = (TextView) dlg.findViewById(R.id.txtKey1);
            layKey2 = (LinearLayout) dlg.findViewById(R.id.layKey2);
            imgKey2 = (ImageView) dlg.findViewById(R.id.imgKey2);
            txtKey2 = (TextView) dlg.findViewById(R.id.txtKey2);
            layKey3 = (LinearLayout) dlg.findViewById(R.id.layKey3);
            imgKey3 = (ImageView) dlg.findViewById(R.id.imgKey3);
            txtKey3 = (TextView) dlg.findViewById(R.id.txtKey3);
            laySetting = (LinearLayout) dlg.findViewById(R.id.laySetting);
            prgDoOperation = (ProgressBar) dlg.findViewById(R.id.prgDoOperation);
            txtNodeName = (TextView) dlg.findViewById(R.id.txtNodeName);
            imgbtnEdit = (ImageView) dlg.findViewById(R.id.imgbtnEdit);
            imgFavorites = (ImageView) dlg.findViewById(R.id.imgFavorites);
            intializeVariables();
        }

        public CO_l_node_IoModule(View view) {
            layKey1 = (LinearLayout) view.findViewById(R.id.layKey1);
            imgKey1 = (ImageView) view.findViewById(R.id.imgKey1);
            txtKey1 = (TextView) view.findViewById(R.id.txtKey1);
            layKey2 = (LinearLayout) view.findViewById(R.id.layKey2);
            imgKey2 = (ImageView) view.findViewById(R.id.imgKey2);
            txtKey2 = (TextView) view.findViewById(R.id.txtKey2);
            layKey3 = (LinearLayout) view.findViewById(R.id.layKey3);
            imgKey3 = (ImageView) view.findViewById(R.id.imgKey3);
            txtKey3 = (TextView) view.findViewById(R.id.txtKey3);
            laySetting = (LinearLayout) view.findViewById(R.id.laySetting);
            prgDoOperation = (ProgressBar) view.findViewById(R.id.prgDoOperation);
            txtNodeName = (TextView) view.findViewById(R.id.txtNodeName);
            imgbtnEdit = (ImageView) view.findViewById(R.id.imgbtnEdit);
            imgFavorites = (ImageView) view.findViewById(R.id.imgFavorites);
            intializeVariables();
        }

        private void intializeVariables() {

        }
    }

    public static final class CO_d_section_add_node_Device_Select {

        public Button btnCancel;
        public Button btnNext;
        public Button btnBack;
        public Spinner spnTypes;
        public TextView txtTitle;

        public CO_d_section_add_node_Device_Select(Context context) {
            Activity activity = (Activity) context;
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            spnTypes = (Spinner) activity.findViewById(R.id.spnTypes);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            intializeVariables();

        }

        private void intializeVariables() {
//            txtTitle.setText(G.T.getSentence(155));
//            lblName.setText(G.T.getSentence(235));
//            btnCancel.setText(G.T.getSentence(104));
//            btnOK.setText(G.T.getSentence(101));
        }
    }

    public static final class CO_d_section_add_node_input_output {

        public Button btnCancel;
        public Button btnNext;
        public Button btnInput;
        public Button btnOutput;
        public Spinner spnTypes;
        public TextView txtTitle;

        public CO_d_section_add_node_input_output(Context context) {
            Activity activity = (Activity) context;
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            btnInput = (Button) activity.findViewById(R.id.btnInput);
            btnOutput = (Button) activity.findViewById(R.id.btnOutput);
            spnTypes = (Spinner) activity.findViewById(R.id.spnTypes);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            intializeVariables();


        }

        private void intializeVariables() {
//            txtTitle.setText(G.T.getSentence(155));
//            lblName.setText(G.T.getSentence(235));
//            btnCancel.setText(G.T.getSentence(104));
//            btnOK.setText(G.T.getSentence(101));
        }
    }

    public static final class CO_d_section_add_node_NodeType {

        public ListView lstNode;
        public ListView lstNodeSwitches;
        public TextView txtTitle;
        public TextView txtLanguage;
        public Button btnNext;
        public Button btnBack;
        public Button btnCancel;

        public CO_d_section_add_node_NodeType(Context context) {
            Activity activity = (Activity) context;
            lstNode = (ListView) activity.findViewById(R.id.lstNode);
            lstNodeSwitches = (ListView) activity.findViewById(R.id.lstNodeSwitches);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            txtLanguage = (TextView) activity.findViewById(R.id.txtLanguage);
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);

        }

    }

    public static final class Co_d_add_Node_IoModule_SelectIo{

        public GridView grdNodes;
        public TextView txtTitle;
        public Button btnNext;
        public Button btnBack;
        public Button btnCancel;

        public Co_d_add_Node_IoModule_SelectIo(Activity activity){
            btnNext = (Button) activity.findViewById(R.id.btnNext);
            btnBack = (Button) activity.findViewById(R.id.btnBack);
            btnCancel = (Button) activity.findViewById(R.id.btnCancel);
            txtTitle = (TextView) activity.findViewById(R.id.txtTitle);
            grdNodes = (GridView)  activity.findViewById(R.id.grdNodes);

        }

    }

    public static final class CO_l_spinner_item {
        public CO_l_spinner_item(Context context) {
            Activity activity = (Activity) context;
            intializeVariables();

        }

        public CO_l_spinner_item(Dialog dlg) {
            intializeVariables();

        }

        public CO_l_spinner_item(View view) {
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_l_weather_gridview_item {
        public LinearLayout mainWeatherLayout;
        public CheckBox chkWeather;
        public ImageView imgWeather;
        public TextView txtWeather;

        public CO_l_weather_gridview_item(Context context) {
            Activity activity = (Activity) context;
            mainWeatherLayout = (LinearLayout) activity.findViewById(R.id.mainWeatherLayout);
            chkWeather = (CheckBox) activity.findViewById(R.id.chkWeather);
            imgWeather = (ImageView) activity.findViewById(R.id.imgWeather);
            txtWeather = (TextView) activity.findViewById(R.id.txtWeather);
            intializeVariables();

        }

        public CO_l_weather_gridview_item(Dialog dlg) {
            mainWeatherLayout = (LinearLayout) dlg.findViewById(R.id.mainWeatherLayout);
            chkWeather = (CheckBox) dlg.findViewById(R.id.chkWeather);
            imgWeather = (ImageView) dlg.findViewById(R.id.imgWeather);
            txtWeather = (TextView) dlg.findViewById(R.id.txtWeather);
            intializeVariables();

        }

        public CO_l_weather_gridview_item(View view) {
            mainWeatherLayout = (LinearLayout) view.findViewById(R.id.mainWeatherLayout);
            chkWeather = (CheckBox) view.findViewById(R.id.chkWeather);
            imgWeather = (ImageView) view.findViewById(R.id.imgWeather);
            txtWeather = (TextView) view.findViewById(R.id.txtWeather);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_master_page_main {
        public LinearLayout layToolbar;
        public ImageView imgLogo;
        public TextView txtName;
        public TextView txtHeader;
        public ImageView imgWifiIcon;
        public ImageView img_server_status;
        public ImageView imghelp;
        public ImageView imgKeyboard;
        public LinearLayout imgAlarmIcon;
        public TextView txtAlarmCount;
        public ImageView imgWeatherIcon;
        public TextView txtTemperature;
        public TextView txtTime;
        public LinearLayout layForms;
        public LinearLayout laySidebar;
        public LinearLayout laybtnHome;
        public LinearLayout lay_back_master;
        public TextView txtHome;
        public LinearLayout laybtnFavorites;
        public TextView txtFavorites;
        public LinearLayout laybtnDevices;
        public TextView txtDevices;
        public LinearLayout laybtnScenario;
        public TextView txtScenario;
        public LinearLayout laybtnSettings;
        public TextView txtSettings;
        public TextView txtLogin;

        public CO_master_page_main(Context context) {
            Activity activity = (Activity) context;
            layToolbar = (LinearLayout) activity.findViewById(R.id.layToolbar);
            imgLogo = (ImageView) activity.findViewById(R.id.imgLogo);
            txtName = (TextView) activity.findViewById(R.id.txtName);
            txtHeader = (TextView) activity.findViewById(R.id.txtHeader);
            imgWifiIcon = (ImageView) activity.findViewById(R.id.imgWifiIcon);
            imgKeyboard = (ImageView) activity.findViewById(R.id.imgKeyboard);
            img_server_status = (ImageView) activity.findViewById(R.id.img_server_status);
            imghelp = (ImageView) activity.findViewById(R.id.imgHelp);
            imgAlarmIcon = (LinearLayout) activity.findViewById(R.id.imgAlarmIcon);
            txtAlarmCount = (TextView) activity.findViewById(R.id.txtAlarmCount);
            imgWeatherIcon = (ImageView) activity.findViewById(R.id.imgWeatherIcon);
            txtTemperature = (TextView) activity.findViewById(R.id.txtTemperature);
            txtTime = (TextView) activity.findViewById(R.id.txtTime);
            layForms = (LinearLayout) activity.findViewById(R.id.layForms);
            laySidebar = (LinearLayout) activity.findViewById(R.id.laySidebar);
            laybtnHome = (LinearLayout) activity.findViewById(R.id.laybtnHome);
            lay_back_master = (LinearLayout) activity.findViewById(R.id.lay_back_master);
            txtHome = (TextView) activity.findViewById(R.id.txtHome);
            laybtnFavorites = (LinearLayout) activity.findViewById(R.id.laybtnFavorites);
            txtFavorites = (TextView) activity.findViewById(R.id.txtFavorites);
            laybtnDevices = (LinearLayout) activity.findViewById(R.id.laybtnDevices);
            txtDevices = (TextView) activity.findViewById(R.id.txtDevices);
            laybtnScenario = (LinearLayout) activity.findViewById(R.id.laybtnScenario);
            txtScenario = (TextView) activity.findViewById(R.id.txtScenario);
            laybtnSettings = (LinearLayout) activity.findViewById(R.id.laybtnSettings);
            txtSettings = (TextView) activity.findViewById(R.id.txtSettings);
            txtLogin = (TextView) activity.findViewById(R.id.txtLogin);
            intializeVariables();

        }

        public CO_master_page_main(Dialog dlg) {
            layToolbar = (LinearLayout) dlg.findViewById(R.id.layToolbar);
            imgLogo = (ImageView) dlg.findViewById(R.id.imgLogo);
            img_server_status = (ImageView) dlg.findViewById(R.id.img_server_status);
            imghelp = (ImageView) dlg.findViewById(R.id.imgHelp);
            txtName = (TextView) dlg.findViewById(R.id.txtName);
            txtHeader = (TextView) dlg.findViewById(R.id.txtHeader);
            imgWifiIcon = (ImageView) dlg.findViewById(R.id.imgWifiIcon);
            imgAlarmIcon = (LinearLayout) dlg.findViewById(R.id.imgAlarmIcon);
            imgKeyboard = (ImageView) dlg.findViewById(R.id.imgKeyboard);
            txtAlarmCount = (TextView) dlg.findViewById(R.id.txtAlarmCount);
            imgWeatherIcon = (ImageView) dlg.findViewById(R.id.imgWeatherIcon);
            txtTemperature = (TextView) dlg.findViewById(R.id.txtTemperature);
            txtTime = (TextView) dlg.findViewById(R.id.txtTime);
            layForms = (LinearLayout) dlg.findViewById(R.id.layForms);
            laySidebar = (LinearLayout) dlg.findViewById(R.id.laySidebar);
            laybtnHome = (LinearLayout) dlg.findViewById(R.id.laybtnHome);
            lay_back_master = (LinearLayout) dlg.findViewById(R.id.lay_back_master);
            txtHome = (TextView) dlg.findViewById(R.id.txtHome);
            laybtnFavorites = (LinearLayout) dlg.findViewById(R.id.laybtnFavorites);
            txtFavorites = (TextView) dlg.findViewById(R.id.txtFavorites);
            laybtnDevices = (LinearLayout) dlg.findViewById(R.id.laybtnDevices);
            txtDevices = (TextView) dlg.findViewById(R.id.txtDevices);
            laybtnScenario = (LinearLayout) dlg.findViewById(R.id.laybtnScenario);
            laybtnSettings = (LinearLayout) dlg.findViewById(R.id.laybtnSettings);
            txtSettings = (TextView) dlg.findViewById(R.id.txtSettings);
            txtLogin = (TextView) dlg.findViewById(R.id.txtLogin);
            intializeVariables();

        }

        public CO_master_page_main(View view) {
            layToolbar = (LinearLayout) view.findViewById(R.id.layToolbar);
            imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
            img_server_status = (ImageView) view.findViewById(R.id.img_server_status);
            imghelp = (ImageView) view.findViewById(R.id.imgHelp);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtHeader = (TextView) view.findViewById(R.id.txtHeader);
            imgWifiIcon = (ImageView) view.findViewById(R.id.imgWifiIcon);
            imgAlarmIcon = (LinearLayout) view.findViewById(R.id.imgAlarmIcon);
            txtAlarmCount = (TextView) view.findViewById(R.id.txtAlarmCount);
            imgWeatherIcon = (ImageView) view.findViewById(R.id.imgWeatherIcon);
            txtTemperature = (TextView) view.findViewById(R.id.txtTemperature);
            imgKeyboard = (ImageView) view.findViewById(R.id.imgKeyboard);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            layForms = (LinearLayout) view.findViewById(R.id.layForms);
            laySidebar = (LinearLayout) view.findViewById(R.id.laySidebar);
            laybtnHome = (LinearLayout) view.findViewById(R.id.laybtnHome);
            lay_back_master = (LinearLayout) view.findViewById(R.id.lay_back_master);
            txtHome = (TextView) view.findViewById(R.id.txtHome);
            laybtnFavorites = (LinearLayout) view.findViewById(R.id.laybtnFavorites);
            txtFavorites = (TextView) view.findViewById(R.id.txtFavorites);
            laybtnDevices = (LinearLayout) view.findViewById(R.id.laybtnDevices);
            txtDevices = (TextView) view.findViewById(R.id.txtDevices);
            laybtnScenario = (LinearLayout) view.findViewById(R.id.laybtnScenario);
            txtScenario = (TextView) view.findViewById(R.id.txtScenario);
            laybtnSettings = (LinearLayout) view.findViewById(R.id.laybtnSettings);
            txtSettings = (TextView) view.findViewById(R.id.txtSettings);
            txtLogin = (TextView) view.findViewById(R.id.txtLogin);
            intializeVariables();
        }

        private void intializeVariables() {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            int margin = G.setting.displayMargin;
            layoutParams.setMargins(margin, margin, margin, margin);
            lay_back_master.setLayoutParams(layoutParams);
        }
    }

    public static final class CO_master_setting {
        public LinearLayout layHeader;
        public LinearLayout lay_btn_language, lay_btn_datetime, lay_btn_location, lay_btn_mobile,
                lay_btn_sms, lay_btn_users, lay_btn_log, lay_btn_support, lay_btn_warranty,
                lay_btn_network, lay_btn_display, lay_btn_reset, lay_btn_update, lay_btn_weather;
        public TextView txt_btn_language, txt_btn_datetime, txt_btn_location, txt_btn_mobile, txt_btn_weather,
                txt_btn_sms, txt_btn_users, txt_btn_log, txt_btn_support, txt_btn_warranty,
                txt_btn_network, txt_btn_display, txt_btn_reset, txt_btn_update;
        public LinearLayout laySettings;
        public LinearLayout layFrame;
        public Button btnReturn;

        public CO_master_setting(Context context) {
            Activity activity = (Activity) context;
            layHeader = (LinearLayout) activity.findViewById(R.id.layHeader);
            lay_btn_language = (LinearLayout) activity.findViewById(R.id.lay_btn_language);
            lay_btn_datetime = (LinearLayout) activity.findViewById(R.id.lay_btn_Datetime);
            lay_btn_location = (LinearLayout) activity.findViewById(R.id.lay_btn_Location);
            lay_btn_mobile = (LinearLayout) activity.findViewById(R.id.lay_btn_Mobile);
            lay_btn_sms = (LinearLayout) activity.findViewById(R.id.lay_btn_SMS);
            lay_btn_users = (LinearLayout) activity.findViewById(R.id.lay_btn_Users);
            lay_btn_log = (LinearLayout) activity.findViewById(R.id.lay_btn_Log);
            lay_btn_support = (LinearLayout) activity.findViewById(R.id.lay_btn_Support);
            lay_btn_warranty = (LinearLayout) activity.findViewById(R.id.lay_btn_Warranty);
            lay_btn_network = (LinearLayout) activity.findViewById(R.id.lay_btn_Network);
            lay_btn_display = (LinearLayout) activity.findViewById(R.id.lay_btn_Display);
            lay_btn_reset = (LinearLayout) activity.findViewById(R.id.lay_btn_Reset);
            lay_btn_update = (LinearLayout) activity.findViewById(R.id.lay_btn_Update);
            txt_btn_language = (TextView) activity.findViewById(R.id.txt_btn_language);
            txt_btn_datetime = (TextView) activity.findViewById(R.id.txt_btn_Datetime);
            txt_btn_location = (TextView) activity.findViewById(R.id.txt_btn_Location);
            txt_btn_mobile = (TextView) activity.findViewById(R.id.txt_btn_Mobile);
            txt_btn_sms = (TextView) activity.findViewById(R.id.txt_btn_SMS);
            txt_btn_users = (TextView) activity.findViewById(R.id.txt_btn_Users);
            txt_btn_log = (TextView) activity.findViewById(R.id.txt_btn_Log);
            txt_btn_support = (TextView) activity.findViewById(R.id.txt_btn_Support);
            txt_btn_warranty = (TextView) activity.findViewById(R.id.txt_btn_Warranty);
            txt_btn_network = (TextView) activity.findViewById(R.id.txt_btn_Network);
            txt_btn_display = (TextView) activity.findViewById(R.id.txt_btn_Display);
            txt_btn_reset = (TextView) activity.findViewById(R.id.txt_btn_Reset);
            txt_btn_update = (TextView) activity.findViewById(R.id.txt_btn_Update);
            txt_btn_weather = (TextView) activity.findViewById(R.id.txt_btn_Weather);
            laySettings = (LinearLayout) activity.findViewById(R.id.laySettings);
            layFrame = (LinearLayout) activity.findViewById(R.id.layFrame);
            btnReturn = (Button) activity.findViewById(R.id.btnReturn);
            lay_btn_weather = (LinearLayout) activity.findViewById(R.id.lay_btn_Weather);
            intializeVariables();
        }

        public CO_master_setting(Dialog dlg) {
            layHeader = (LinearLayout) dlg.findViewById(R.id.layHeader);
            laySettings = (LinearLayout) dlg.findViewById(R.id.laySettings);
            intializeVariables();
        }

        public CO_master_setting(View view) {
            layHeader = (LinearLayout) view.findViewById(R.id.layHeader);
            laySettings = (LinearLayout) view.findViewById(R.id.laySettings);
            intializeVariables();
        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_setting_user {
        public LinearLayout layOptions;
        public ListView lstUser;
        public TextView textView1;
        public EditText edtUsername;
        public TextView TextView01;
        public EditText edtPass;
        public TextView TextView02;
        public EditText edtConfirmPass;
        public TextView TextView03;
        public Spinner spnType;
        public Button btnSave;
        public CheckBox chkShowPassword;
        public LinearLayout layWebsiteInfo;
        public TextView txtWebsiteInfo;

        public CO_f_setting_user(Context context) {
            Activity activity = (Activity) context;
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            lstUser = (ListView) activity.findViewById(R.id.lstUser);
            textView1 = (TextView) activity.findViewById(R.id.textView1);
            edtUsername = (EditText) activity.findViewById(R.id.edtUsername);
            TextView01 = (TextView) activity.findViewById(R.id.TextView01);
            edtPass = (EditText) activity.findViewById(R.id.edtPass);
            edtPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            TextView02 = (TextView) activity.findViewById(R.id.TextView02);
            edtConfirmPass = (EditText) activity.findViewById(R.id.edtConfirmPass);
            edtConfirmPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            edtConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            TextView03 = (TextView) activity.findViewById(R.id.TextView03);
            spnType = (Spinner) activity.findViewById(R.id.spnType);
            btnSave = (Button) activity.findViewById(R.id.btnSave);
            layWebsiteInfo = (LinearLayout) activity.findViewById(R.id.layWebsiteInfo);
            txtWebsiteInfo = (TextView) activity.findViewById(R.id.txtWebsiteInfo);
            intializeVariables();

        }

        public CO_f_setting_user(Dialog dlg) {
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            lstUser = (ListView) dlg.findViewById(R.id.lstUser);
            textView1 = (TextView) dlg.findViewById(R.id.textView1);
            edtUsername = (EditText) dlg.findViewById(R.id.edtUsername);
            TextView01 = (TextView) dlg.findViewById(R.id.TextView01);
            edtPass = (EditText) dlg.findViewById(R.id.edtPass);
            edtPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            TextView02 = (TextView) dlg.findViewById(R.id.TextView02);
            edtConfirmPass = (EditText) dlg.findViewById(R.id.edtConfirmPass);
            edtConfirmPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            edtConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            TextView03 = (TextView) dlg.findViewById(R.id.TextView03);
            spnType = (Spinner) dlg.findViewById(R.id.spnType);
            btnSave = (Button) dlg.findViewById(R.id.btnSave);
            layWebsiteInfo = (LinearLayout) dlg.findViewById(R.id.layWebsiteInfo);
            txtWebsiteInfo = (TextView) dlg.findViewById(R.id.txtWebsiteInfo);
            intializeVariables();
        }

        public CO_f_setting_user(View view) {
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            lstUser = (ListView) view.findViewById(R.id.lstUser);
            textView1 = (TextView) view.findViewById(R.id.textView1);
            edtUsername = (EditText) view.findViewById(R.id.edtUsername);
            TextView01 = (TextView) view.findViewById(R.id.TextView01);
            edtPass = (EditText) view.findViewById(R.id.edtPass);
            edtPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            TextView02 = (TextView) view.findViewById(R.id.TextView02);
            edtConfirmPass = (EditText) view.findViewById(R.id.edtConfirmPass);
            edtConfirmPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            edtConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            TextView03 = (TextView) view.findViewById(R.id.TextView03);
            spnType = (Spinner) view.findViewById(R.id.spnType);
            btnSave = (Button) view.findViewById(R.id.btnSave);
            layWebsiteInfo = (LinearLayout) view.findViewById(R.id.layWebsiteInfo);
            txtWebsiteInfo = (TextView) view.findViewById(R.id.txtWebsiteInfo);
            intializeVariables();
        }

        private void intializeVariables() {

        }
    }

    public static final class CO_f_setting_sms {
        public LinearLayout laySMSCharge;
        public LinearLayout layRcount;
        public TextView txtRcount;
        public Button btnCharge;
        public LinearLayout layList;
        public TextView txtListTitle;
        public ListView lstMobiles;
        public LinearLayout layVerify;
        public TextView txtCountryCaption;
        public Spinner spnCountries;
        public TextView txtCountryCode;
        public EditText edtMobileNumber;
        public Button btnVerify;

        public CO_f_setting_sms(Context context) {
            Activity activity = (Activity) context;
            laySMSCharge = (LinearLayout) activity.findViewById(R.id.laySMSCharge);
            layRcount = (LinearLayout) activity.findViewById(R.id.layRcount);
            txtRcount = (TextView) activity.findViewById(R.id.txtRcount);
            btnCharge = (Button) activity.findViewById(R.id.btnCharge);
            layList = (LinearLayout) activity.findViewById(R.id.layList);
            txtListTitle = (TextView) activity.findViewById(R.id.txtListTitle);
            lstMobiles = (ListView) activity.findViewById(R.id.lstMobiles);
            layVerify = (LinearLayout) activity.findViewById(R.id.layVerify);
            txtCountryCaption = (TextView) activity.findViewById(R.id.txtCountryCaption);
            spnCountries = (Spinner) activity.findViewById(R.id.spnCountries);
            txtCountryCode = (TextView) activity.findViewById(R.id.txtCountryCode);
            edtMobileNumber = (EditText) activity.findViewById(R.id.edtMobileNumber);
            btnVerify = (Button) activity.findViewById(R.id.btnVerify);
            intializeVariables();

        }

        public CO_f_setting_sms(Dialog dlg) {
            laySMSCharge = (LinearLayout) dlg.findViewById(R.id.laySMSCharge);
            layRcount = (LinearLayout) dlg.findViewById(R.id.layRcount);
            txtRcount = (TextView) dlg.findViewById(R.id.txtRcount);
            btnCharge = (Button) dlg.findViewById(R.id.btnCharge);
            layList = (LinearLayout) dlg.findViewById(R.id.layList);
            txtListTitle = (TextView) dlg.findViewById(R.id.txtListTitle);
            lstMobiles = (ListView) dlg.findViewById(R.id.lstMobiles);
            layVerify = (LinearLayout) dlg.findViewById(R.id.layVerify);
            txtCountryCaption = (TextView) dlg.findViewById(R.id.txtCountryCaption);
            spnCountries = (Spinner) dlg.findViewById(R.id.spnCountries);
            txtCountryCode = (TextView) dlg.findViewById(R.id.txtCountryCode);
            edtMobileNumber = (EditText) dlg.findViewById(R.id.edtMobileNumber);
            btnVerify = (Button) dlg.findViewById(R.id.btnVerify);
            intializeVariables();

        }

        public CO_f_setting_sms(View view) {
            laySMSCharge = (LinearLayout) view.findViewById(R.id.laySMSCharge);
            layRcount = (LinearLayout) view.findViewById(R.id.layRcount);
            txtRcount = (TextView) view.findViewById(R.id.txtRcount);
            btnCharge = (Button) view.findViewById(R.id.btnCharge);
            layList = (LinearLayout) view.findViewById(R.id.layList);
            txtListTitle = (TextView) view.findViewById(R.id.txtListTitle);
            lstMobiles = (ListView) view.findViewById(R.id.lstMobiles);
            layVerify = (LinearLayout) view.findViewById(R.id.layVerify);
            txtCountryCaption = (TextView) view.findViewById(R.id.txtCountryCaption);
            spnCountries = (Spinner) view.findViewById(R.id.spnCountries);
            txtCountryCode = (TextView) view.findViewById(R.id.txtCountryCode);
            edtMobileNumber = (EditText) view.findViewById(R.id.edtMobileNumber);
            btnVerify = (Button) view.findViewById(R.id.btnVerify);
            intializeVariables();

        }

        private void intializeVariables() {
        }
    }

    public static final class CO_l_list_setting_user {
        public LinearLayout layBack;
        public TextView txtUsername;
        public TextView txtFill;
        public ImageView imgEdit;
        public ImageView imgDelete;

        public CO_l_list_setting_user(Context context) {
            Activity activity = (Activity) context;
            layBack = (LinearLayout) activity.findViewById(R.id.layBack);
            txtUsername = (TextView) activity.findViewById(R.id.txtUsername);
            txtFill = (TextView) activity.findViewById(R.id.txtFill);
            imgEdit = (ImageView) activity.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) activity.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        public CO_l_list_setting_user(Dialog dlg) {
            layBack = (LinearLayout) dlg.findViewById(R.id.layBack);
            txtUsername = (TextView) dlg.findViewById(R.id.txtUsername);
            txtFill = (TextView) dlg.findViewById(R.id.txtFill);
            imgEdit = (ImageView) dlg.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) dlg.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        public CO_l_list_setting_user(View view) {
            layBack = (LinearLayout) view.findViewById(R.id.layBack);
            txtUsername = (TextView) view.findViewById(R.id.txtUsername);
            txtFill = (TextView) view.findViewById(R.id.txtFill);
            imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            intializeVariables();

        }

        private void intializeVariables() {

        }
    }

    public static final class CO_d_setting_user_access {
        public LinearLayout layMain;
        public LinearLayout layOptions;
        public TextView txtHeader;
        public TextView txtUsername;
        public Spinner spnUser;
        public TextView txtPassword;
        public EditText edtPassword;
        public LinearLayout layNavigation;
        //        public Button       btnCancel;
        public TextView txtLanguage;
        public Button btnLogin;

        public CO_d_setting_user_access(Context context) {
            Activity activity = (Activity) context;
            layMain = (LinearLayout) activity.findViewById(R.id.layMain);
            layOptions = (LinearLayout) activity.findViewById(R.id.layOptions);
            txtHeader = (TextView) activity.findViewById(R.id.txtHeader);
            txtUsername = (TextView) activity.findViewById(R.id.txtUsername);
            spnUser = (Spinner) activity.findViewById(R.id.spnUser);
            txtPassword = (TextView) activity.findViewById(R.id.txtPassword);
            edtPassword = (EditText) activity.findViewById(R.id.edtPassword);
            layNavigation = (LinearLayout) activity.findViewById(R.id.layNavigation);
//            btnCancel = (Button) activity.findViewById(R.nodeId.btnCancel);
            txtLanguage = (TextView) activity.findViewById(R.id.txtLanguage);
            btnLogin = (Button) activity.findViewById(R.id.btnLogin);
            intializeVariables();

        }

        public CO_d_setting_user_access(Dialog dlg) {
            layMain = (LinearLayout) dlg.findViewById(R.id.layMain);
            layOptions = (LinearLayout) dlg.findViewById(R.id.layOptions);
            txtHeader = (TextView) dlg.findViewById(R.id.txtHeader);
            txtUsername = (TextView) dlg.findViewById(R.id.txtUsername);
            spnUser = (Spinner) dlg.findViewById(R.id.spnUser);
            txtPassword = (TextView) dlg.findViewById(R.id.txtPassword);
            edtPassword = (EditText) dlg.findViewById(R.id.edtPassword);
            layNavigation = (LinearLayout) dlg.findViewById(R.id.layNavigation);
//            btnCancel = (Button) dlg.findViewById(btnCancel);
            txtLanguage = (TextView) dlg.findViewById(R.id.txtLanguage);
            btnLogin = (Button) dlg.findViewById(R.id.btnLogin);
            intializeVariables();

        }

        public CO_d_setting_user_access(View view) {
            layMain = (LinearLayout) view.findViewById(R.id.layMain);
            layOptions = (LinearLayout) view.findViewById(R.id.layOptions);
            txtHeader = (TextView) view.findViewById(R.id.txtHeader);
            txtUsername = (TextView) view.findViewById(R.id.txtUsername);
            spnUser = (Spinner) view.findViewById(R.id.spnUser);
            txtPassword = (TextView) view.findViewById(R.id.txtPassword);
            edtPassword = (EditText) view.findViewById(R.id.edtPassword);
            layNavigation = (LinearLayout) view.findViewById(R.id.layNavigation);
//            btnCancel = (Button) view.findViewById(btnCancel);
            txtLanguage = (TextView) view.findViewById(R.id.txtLanguage);
            btnLogin = (Button) view.findViewById(R.id.btnLogin);
            intializeVariables();

        }

        private void intializeVariables() {
            txtHeader.setText(G.T.getSentence(790));
            txtUsername.setText(G.T.getSentence(786));
            txtPassword.setText(G.T.getSentence(787));
//            btnCancel.setText(G.T.getSentence(102));
            btnLogin.setText(G.T.getSentence(791));
        }
    }
}
