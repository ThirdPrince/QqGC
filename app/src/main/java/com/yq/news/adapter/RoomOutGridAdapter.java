package com.yq.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.yq.news.R;
import com.yq.news.itf.OnNestGridClick;
import com.yq.news.view.GridDividerItemDecoration;
import com.yq.news.view.RecyclerItemDecoration;
import com.yq.util.UIUtils;

import static java.security.AccessController.getContext;


/**
 *  场所详情 房间 外层
 */
public class RoomOutGridAdapter extends RecyclerView.Adapter<RoomOutGridAdapter.VerticalViewHolder> {

    private static final String TAG = "RoomOutGridAdapter";

    private  Context mContext ;


    private  RoomNestGridAdapter nestGridAdapter ;



  public RoomOutGridAdapter(Context  context)
  {
      mContext = context ;
  }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_gc_room_item, viewGroup, false);
        return new VerticalViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder verticalViewHolder, int pos) {

        nestGridAdapter.setPosition(pos);
    }

    @Override
    public int getItemCount() {
        return 13;
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
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(new RecyclerItemDecoration(0,
                    0,  UIUtils.dip2px(mContext, 4), UIUtils.dip2px(mContext, 10)));
            //recyclerView.addItemDecoration(new GridDividerItemDecoration(mContext, UIUtils.dip2px(mContext,20), UIUtils.dip2px(mContext,20),true));
             nestGridAdapter = new RoomNestGridAdapter(mContext,getLayoutPosition());
            recyclerView.setAdapter(nestGridAdapter);
            nestGridAdapter.setOnNestGridClick(new OnNestGridClick() {
                @Override
                public void onNestClick(int pos, View view) {
                    ToastUtils.showShort("外层pos="+getLayoutPosition()+":::内层pos"+pos);
                }
            });

        }
    }


}
