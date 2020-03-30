package com.yq.news.adapter;

import android.content.Context;
import android.opengl.GLDebugHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.yq.news.R;
import com.yq.news.itf.OnEditClick;
import com.yq.news.itf.OnImageClick;
import com.yq.news.model.AccountInfo;
import com.yq.news.model.FeedBackInfo;
import com.yq.news.model.TaskBean;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 *   未反馈
 */
public class FeedbackAdapter extends CommonAdapter<FeedBackInfo> {



    private Context mContext;

    private OnEditClick onEditClick ;

    private TaskBean taskBean ;

    public void setOnImageClick(OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }

    private OnImageClick onImageClick;


    public void setOnEditClick(OnEditClick onEditClick) {
        this.onEditClick = onEditClick;
    }

    public FeedbackAdapter(Context context, int layoutId, List<FeedBackInfo> datas,TaskBean taskBean) {
        super(context, layoutId, datas);
        mContext = context ;
        this.taskBean = taskBean;
    }


    @Override
    public void convert(ViewHolder holder, FeedBackInfo feedBackInfo, int position) {

        if(!TextUtils.isEmpty(feedBackInfo.getAccountname()))
        {
            holder.setText(R.id.account_nick_tv, feedBackInfo.getAccountname());
        }

        if(!TextUtils.isEmpty(feedBackInfo.getUserAccountId()))
        {
            holder.setText(R.id.account_id_tv, feedBackInfo.getUserAccountId());
        }
        if(!TextUtils.isEmpty(feedBackInfo.getUsername()))
        {
            holder.setText(R.id.feed_owner_tv, feedBackInfo.getUsername());
        }

        holder.setText(R.id.time_tv, RelativeDateFormat.timeStamp2Date(feedBackInfo.getCreatetime()));
        ImageView imageView = holder.getView(R.id.feed_back_img);

        Glide.with(mContext).load(feedBackInfo.getImgPath()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onImageClick != null)
                {
                    onImageClick.onClick(position,v);
                }
            }
        });
        if(!TextUtils.isEmpty(feedBackInfo.getAccountTypeName()))
        {
            holder.setText(R.id.account_type_tv, feedBackInfo.getAccountTypeName());
        }

        TextView edit_feedback  = holder.getView(R.id.edit_feedback);
        if(!ObjectUtils.isEmpty(taskBean))
        {
            edit_feedback.setVisibility(taskBean.getFinished() ==1 ? View.GONE : View.VISIBLE);
        }
        edit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEditClick != null)
                {
                    onEditClick.onClick(feedBackInfo,edit_feedback);
                }
            }
        });

    }
}
