package com.yq.news.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yq.news.R;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.GridItem;
import com.yq.news.net.ApiUtils;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * 首页  群防群控
 */
public class ImageGridAdapter implements ItemViewDelegate<GridItem>  {

    private static final String TAG = "ImagePageAdapter";

    private Context mContext ;
    private RequestOptions options ;

    private String imageUrl = "";
    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public float mTextSize ;
    public ImageGridAdapter(Context context, float textSize)
    {
        mContext = context ;
        mTextSize = textSize ;
        options = new RequestOptions();
        options.placeholder(R.drawable.news_icon );
        if(ApiUtils.BASE_URL.startsWith("https"))
        {
            imageUrl = ApiUtils.BASE_URL;
        }else
        {
            imageUrl = ApiUtils.BASE_ADDRESS;
        }

    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.fragment_grid_pic_item;
    }

    @Override
    public boolean isForViewType(GridItem item, int position) {
        //List<ArticleInfo.ImglistBean> list = item.getImglist();
        return item.getType()==2 ;
    }

    @Override
    public void convert(ViewHolder holder, GridItem articleInfo, int position) {
       /* ImageView imageView = holder.getView(R.id.article_img);
        List<ArticleInfo.ImglistBean> list = articleInfo.getImglist();

        if(list != null && list.size()>0)
        {

            //imageUrl = list.get(0).getImgurl();
            Glide.with(mContext)
                    .load(imageUrl+list.get(0).getImgurl())
                    .apply(options)
                    .into(imageView);
           // Glide.with(mContext).load(ApiUtils.BASE_ADDRESS + imageUrl).placeholder(R.drawable.news_icon).error(R.drawable.news_icon).into(imageView);
        }else
        {

        }
        TextView title = holder.getView(R.id.article_title);
        title.setText(articleInfo.getTitle());
        title.setTextSize(mTextSize);
        holder.setText(R.id.user_name_tv,articleInfo.getDomain());
        holder.setText(R.id.create_time_tv,  RelativeDateFormat.timeStamp2Date(articleInfo.getCreatetime()));*/
    }
}
