package com.yq.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.yq.news.R;
import com.yq.news.itf.OnNestGridClick;
import com.yq.news.view.RecyclerItemDecoration;
import com.yq.util.UIUtils;


/**
 *  群防群控 主页 adapter
 */
public class MainOutGridAdapter extends RecyclerView.Adapter<MainOutGridAdapter.VerticalViewHolder> {

    private static final String TAG = "MainOutGridAdapter";

    private  Context mContext ;

    private float textSize = 0;

    private MainNestGridAdapter nestGridAdapter ;



  public MainOutGridAdapter(Context  context)
  {
      mContext = context ;
  }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_gc_main_item, viewGroup, false);
        return new VerticalViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder verticalViewHolder, int pos) {

        Log.e(TAG,"pos="+pos);
        nestGridAdapter.setPosition(pos);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder
    {

        /**
         * 标签
         */
        private TextView tag ;

        /**
         * grid
         */
        private RecyclerView recyclerView;

        public VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag);
            recyclerView = itemView.findViewById(R.id.rcy_view);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,12);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    return getLayoutPosition()>2?4:3;
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
             nestGridAdapter = new MainNestGridAdapter(mContext,getLayoutPosition());
            recyclerView.addItemDecoration(new RecyclerItemDecoration(0,
                    0,  UIUtils.dip2px(mContext, 5), UIUtils.dip2px(mContext, 10)));
            recyclerView.setAdapter(nestGridAdapter);
            //gridLayoutManager.setSpanCount(getLayoutPosition()>1?4:3);
            nestGridAdapter.setOnNestGridClick(new OnNestGridClick() {
                @Override
                public void onNestClick(int pos, View view) {
                    ToastUtils.showShort("外层pos="+getAdapterPosition()+":::内层pos"+pos);
                }
            });

        }
    }


}
