package com.yq.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.luck.picture.lib.tools.DateUtils;
import com.yq.news.R;
import com.yq.news.itf.OnEditClick;
import com.yq.news.model.AccountInfo;
import com.yq.news.model.TaskBean;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 账号管理Adapter
 */
public class TaskListAdapter extends CommonAdapter<TaskBean> {



    private  SimpleDateFormat msFormat = new SimpleDateFormat("mm:ss");

    public TaskListAdapter(Context context, int layoutId, List<TaskBean> datas) {
        super(context, layoutId, datas);
    }


    @Override
    public void convert(ViewHolder holder, TaskBean taskBean, int position) {

        holder.setText(R.id.title,taskBean.getName());
        holder.setText(R.id.time_start_tv, RelativeDateFormat.timeStamp2Date(taskBean.getStarttime()));
        holder.setText(R.id.time_end_tv,RelativeDateFormat.timeStamp2Date(taskBean.getEndtime()));

        TextView feedBack = holder.getView(R.id.feed_back_tv);
        String feedBackStr = taskBean.getFeededcount()+"/"+taskBean.getFeedtotal();
        int highlightIndex = (taskBean.getFeededcount()+"").length();
        SpannableString sp = new SpannableString(feedBackStr);
        //设置背景颜色
        sp.setSpan(new ForegroundColorSpan(Color.GREEN), 0 ,highlightIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        feedBack.setText(sp);

    }
}
