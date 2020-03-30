package com.yq.news.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yq.news.R;
import com.yq.news.itf.OnEditClick;
import com.yq.news.model.AccountInfo;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.IndustryBean;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * 账号管理Adapter
 */
public class AccountListAdapter extends CommonAdapter<AccountInfo> {



    private OnEditClick onEditClick ;

    public void setOnEditClick(OnEditClick onEditClick) {
        this.onEditClick = onEditClick;
    }

    public AccountListAdapter(Context context, int layoutId, List<AccountInfo> datas) {
        super(context, layoutId, datas);
    }


    @Override
    public void convert(ViewHolder holder, AccountInfo articleInfo, int position) {


        holder.setText(R.id.nickname_tv,articleInfo.getNickname());
        holder.setText(R.id.accountid_tv,"账号ID："+articleInfo.getAccountid());
        holder.setText(R.id.realname_tv,"账号所属人："+articleInfo.getRealname());
        TextView edit_account  = holder.getView(R.id.edit_account);
        edit_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(onEditClick != null)
             {
                 onEditClick.onClick(articleInfo,edit_account);
             }
            }
        });
    }
}
