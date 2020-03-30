package com.yq.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yq.news.R;
import com.yq.news.itf.OnNestGridClick;
import com.yq.news.viewholder.ImageTypeViewHolder;
import com.yq.news.viewholder.TextTypeViewHolder;
import com.yq.news.viewholder.TypeAbstractViewHolder;


/**
 *  群防群控 Grid
 */
public class MainNestGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MainNestGridAdapter";

    private final int TYPE_IMAGE = 1 ; //热门应用

    private final int TYPE_TEXT = 2; //辖区统计

   private  Context mContext ;


    public int outPos = 0;

    private LayoutInflater inflater ;

    public void setPosition(int position) {
        this.outPos = position;
    }

    public MainNestGridAdapter(Context context, int pos)
    {
        mContext = context ;
        inflater = LayoutInflater.from(mContext);
        this.outPos = pos ;
    }

    private OnNestGridClick onNestGridClick ;

    public void setOnNestGridClick(OnNestGridClick onNestGridClick) {
        this.onNestGridClick = onNestGridClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;
        switch (viewType)
        {
            case TYPE_IMAGE:
                 view = inflater.inflate(R.layout.fragment_grid_pic_item, viewGroup, false);
                return  new ImageTypeViewHolder(view);
            case TYPE_TEXT:
                 view = inflater.inflate(R.layout.fragment_grid_txt_item, viewGroup, false);
                return  new TextTypeViewHolder(view);
        }
        return null;


    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int pos) {

        TypeAbstractViewHolder typeAbstractViewHolder = (TypeAbstractViewHolder)viewHolder;
        typeAbstractViewHolder.bindHolder(null,outPos,pos);
        Log.e(TAG,"pos="+outPos);




    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if(outPos == 2)
            return TYPE_TEXT;
        return TYPE_IMAGE ;
                //super.getItemViewType(position);
    }

   /* class GridHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {



       private ImageView tag_img ;
        *//**
         * name
         *//*
        private TextView tag_name ;



        public GridHolder(@NonNull View itemView) {
            super(itemView);
            tag_img = itemView.findViewById(R.id.tag_img);
            tag_name =  itemView.findViewById(R.id.tag_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(onNestGridClick!= null)
            {
                onNestGridClick.onNestClick(getLayoutPosition(),v);
            }
        }
    }*/


}
