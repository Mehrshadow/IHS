package ir.parsansoft.app.ihs.center.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

import ir.parsansoft.app.ihs.center.AllViews.CO_l_list_sections_room;
import ir.parsansoft.app.ihs.center.AllViews.CO_l_list_sections_section;
import ir.parsansoft.app.ihs.center.Database;
import ir.parsansoft.app.ihs.center.G;
import ir.parsansoft.app.ihs.center.R;
import ir.parsansoft.app.ihs.center.components.AssetsManager;


public class AdapterSectionsExpandableList extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<Database.Room.Struct[]> childtems;
    private LayoutInflater inflater;
    private ArrayList<Database.Section.Struct> parentItems;
    private onOptionButton editListener;
    public boolean hide = false;

    public interface onOptionButton {
        void onRoomEdit(Database.Room.Struct room);

        void onSectionEdit(Database.Section.Struct section);

        void onRoomDelete(Database.Room.Struct room);

        void onSectionDelete(Database.Section.Struct section);
    }

    public void setOnOptionButton(onOptionButton listener) {
        editListener = listener;
    }

    public AdapterSectionsExpandableList(ArrayList<Database.Section.Struct> parents, ArrayList<Database.Room.Struct[]> childern) {
        this.parentItems = parents;
        this.childtems = childern;
    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.inflater = inflater;
        this.activity = activity;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inflater.inflate(R.layout.l_list_sections_room, null);
        G.log("groupPosition: " + groupPosition + "  childPosition  " + childPosition);
        final Database.Room.Struct ch = childtems.get(groupPosition)[childPosition];
        CO_l_list_sections_room viewObjects = new CO_l_list_sections_room(convertView);
        if (!isLastChild)
            viewObjects.layBack.setBackgroundResource(R.drawable.lay_sections_exp_item_middle_selector);
        else
            viewObjects.layBack.setBackgroundResource(R.drawable.lay_sections_exp_item_last_selector);

        if (hide && G.currentUser != null) { /******** Jahanbin *********///IF hide = true OR current user is null it crashes!
            if (G.currentUser.rol != 1){
                viewObjects.imgEdit.setVisibility(View.INVISIBLE);
                viewObjects.imgDelete.setVisibility(View.INVISIBLE);
            }
        }

        viewObjects.lblName.setText(ch.name);
        viewObjects.imgIcon.setImageBitmap(AssetsManager.getBitmap(G.currentActivity, G.DIR_ICONS_ROOMS + "/" + ch.icon));
        viewObjects.imgEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.onRoomEdit(ch);
            }
        });
        viewObjects.imgDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                editListener.onRoomDelete(ch);
            }
        });
        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.l_list_sections_section, null);
        }
        CO_l_list_sections_section viewObjects = new CO_l_list_sections_section(convertView);
        viewObjects.lblName.setText(parentItems.get(groupPosition).name);
        final Database.Section.Struct p = parentItems.get(groupPosition);

        if (isExpanded && getChildrenCount(groupPosition) != 0)
            viewObjects.layBack.setBackgroundResource(R.drawable.lay_sections_exp_head_expand_selector);
        else
            viewObjects.layBack.setBackgroundResource(R.drawable.lay_sections_exp_head_collapse_selector);
        if (hide && G.currentUser != null) {/******** Jahanbin *********///IF hide = true OR current user is null it crashes!
            if (G.currentUser.rol != 1){
                viewObjects.imgEdit.setVisibility(View.INVISIBLE);
                viewObjects.imgDelete.setVisibility(View.INVISIBLE);
            }
        }

        viewObjects.imgIcon.setImageBitmap(AssetsManager.getBitmap(G.currentActivity, G.DIR_ICONS_SECTIONS + "/" + p.icon));
        viewObjects.imgEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.onSectionEdit(p);
            }
        });
        viewObjects.imgDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.onSectionDelete(p);
            }
        });
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childtems.get(groupPosition)[childPosition].iD;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        G.log("groupPosition : " + groupPosition);
        if (childtems.get(groupPosition) == null)
            return 0;
        return childtems.get(groupPosition).length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return parentItems.get(groupPosition).iD;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
