package com.yq.news.treeview.viewbinder;

import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yq.news.R;
import com.yq.news.itf.OnCheckBtnClick;
import com.yq.news.treeview.bean.Dir;

import java.util.ArrayList;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewBinder;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class DirectoryNodeBinder extends TreeViewBinder<DirectoryNodeBinder.ViewHolder> {


    private OnCheckBtnClick onRadioBtnClick ;


    public void setSelectTree(List<Dir> selectTree) {
        this.selectTree = selectTree;
        List<String> selectId = new ArrayList<>();
        for(Dir dir :selectTree)
        {
            selectId.add(dir.id);
        }
        setSelectId(selectId);
    }

    private List<Dir> selectTree = new ArrayList<>();

    public List<String> getSelectId() {
        return selectId;
    }

    public void setSelectId(List<String> selectId) {
        this.selectId = selectId;
    }

    private List<String> selectId = new ArrayList<>();

    public void setOnRadioBtnClick(OnCheckBtnClick onRadioBtnClick) {
        this.onRadioBtnClick = onRadioBtnClick;
    }

    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(ViewHolder holder, int position, TreeNode node) {
        holder.ivArrow.setRotation(0);
        holder.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp);
        int rotateDegree = node.isExpand() ? 90 : 0;
        holder.ivArrow.setRotation(rotateDegree);
        Dir dirNode = (Dir) node.getContent();
        holder.tvName.setText(dirNode.dirName);
        if (node.isLeaf()) {
            holder.ivArrow.setVisibility(View.INVISIBLE);
            holder.check_btn.setClickable(false);
            //holder.check_btn.setVisibility(View.VISIBLE);
        }
        else {

            holder.ivArrow.setVisibility(View.VISIBLE);
            holder.check_btn.setClickable(true);
            holder.check_btn.setChecked(false);
           // holder.check_btn.setVisibility(View.VISIBLE);
        }
        Dir dir = (Dir)node.getContent();
        if(selectId.contains(dir.id))
        {
            holder.check_btn.setChecked(true);
        }else
        {
            holder.check_btn.setChecked(false);
        }

        holder.check_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if(isChecked)
                {
                    if(onRadioBtnClick != null)
                    {
                        if(!node.isLeaf() && buttonView.isPressed()) {
                            onRadioBtnClick.onClick(node,isChecked);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }

    public static class ViewHolder extends TreeViewBinder.ViewHolder {
        private ImageView ivArrow;
        private TextView tvName;

        private CheckBox check_btn ;

        public ViewHolder(View rootView) {
            super(rootView);
            this.ivArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.check_btn= (CheckBox) rootView.findViewById(R.id.check_btn);
        }

        public ImageView getIvArrow() {
            return ivArrow;
        }

        public TextView getTvName() {
            return tvName;
        }
    }
}
