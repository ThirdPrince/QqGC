package com.yq.news.treeview.bean;

import android.util.Log;

import com.yq.news.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class Dir implements LayoutItemType {

    private static final String TAG = "Dir";

    public String dirName;

    public  String id ;

    public Dir(String dirName,String id) {
        this.dirName = dirName;
        this.id= id;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(id);
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEquals = this.id.equals(((Dir)obj).id);
        Log.e(TAG,"isEquals::"+isEquals);
        return isEquals;
    }
}
