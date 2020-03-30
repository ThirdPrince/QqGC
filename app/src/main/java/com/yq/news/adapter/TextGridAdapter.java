package com.yq.news.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.yq.news.R;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.GridItem;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * 首页  文本类 群防群控
 */
public class TextGridAdapter implements ItemViewDelegate<GridItem>  {

    private Context mContext ;

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public float mTextSize ;

    public TextGridAdapter(Context context, float textSize)
    {
        mContext = context ;
        mTextSize = textSize ;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.fragment_grid_txt_item;
    }

    @Override
    public boolean isForViewType(GridItem item, int position) {
        //List<ArticleInfo.ImglistBean> list = item.getImglist();
        return item.getType() == 1;
    }

    @Override
    public void convert(ViewHolder holder, GridItem articleInfo, int position) {

       /* TextView title = holder.getView(R.id.article_title);
        title.setText(articleInfo.getTitle());
        Log.e("TextSize",mTextSize+"");
        title.setTextSize(mTextSize);
        holder.setText(R.id.user_name_tv,articleInfo.getDomain());
        holder.setText(R.id.create_time_tv,  RelativeDateFormat.timeStamp2Date(articleInfo.getCreatetime()));*/
    }
}
