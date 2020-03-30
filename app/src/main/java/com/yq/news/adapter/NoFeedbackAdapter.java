package com.yq.news.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yq.news.R;
import com.yq.news.model.FeedBackInfo;
import com.yq.news.model.NoFeedBackInfo;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 *   未反馈
 */
public class NoFeedbackAdapter extends CommonAdapter<NoFeedBackInfo> {



    private Context mContext;


    public NoFeedbackAdapter(Context context, int layoutId, List<NoFeedBackInfo> datas) {
        super(context, layoutId, datas);
        mContext = context ;
    }


    @Override
    public void convert(ViewHolder holder, NoFeedBackInfo feedBackInfo, int position) {



        holder.setText(R.id.no_feed_name_tv,feedBackInfo.getRealname());
        holder.setText(R.id.no_feed_phone_tv,feedBackInfo.getTelphone());

    }
}
