package com.yq.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yq.news.R;
import com.yq.news.itf.OnEditClick;
import com.yq.news.model.AccountInfo;
import com.yq.news.model.MsgInfo;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * 消息
 */
public class MsgListAdapter extends CommonAdapter<MsgInfo> {



    private OnEditClick onEditClick ;

    public void setOnEditClick(OnEditClick onEditClick) {
        this.onEditClick = onEditClick;
    }

    public MsgListAdapter(Context context, int layoutId, List<MsgInfo> datas) {
        super(context, layoutId, datas);
    }


    @Override
    public void convert(ViewHolder holder, MsgInfo articleInfo, int position) {

        holder.setText(R.id.msg_time_tv, RelativeDateFormat.timeStamp2Date(articleInfo.getCreatetime()));
        holder.setText(R.id.msg_content_tv, articleInfo.getContent());
        int type = articleInfo.getType();
        String typeTv = null;
        switch (type) //0：新任务 1：任务即将到期 2：任务变更提醒
        {
            case 0:
                typeTv = "新任务";
                break;
            case 1:
                typeTv = "任务即将到期";
                break;
            case 2:
                typeTv = "任务变更提醒";
                break;
        }
        holder.setText(R.id.msg_title,typeTv);

    }
}
