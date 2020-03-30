package com.yq.news.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yq.news.R;
import com.yq.news.itf.OnNestGridClick;


/**
 *  场所详情 房间 nestGrid 内层
 */
public class RoomNestGridAdapter extends RecyclerView.Adapter<RoomNestGridAdapter.GridHolder> {

    private static final String TAG = "NestGridRoomAdapter";

    private  Context mContext ;


    public int position = 0;

    public void setPosition(int position) {
        this.position = position;
    }

    public RoomNestGridAdapter(Context context, int pos)
    {
        mContext = context ;
        this.position = pos ;
    }

    private OnNestGridClick onNestGridClick ;

    public void setOnNestGridClick(OnNestGridClick onNestGridClick) {
        this.onNestGridClick = onNestGridClick;
    }

    @NonNull
    @Override
    public GridHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_grid_room_item, viewGroup, false);
        return new GridHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull GridHolder gridHolder, int pos) {


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class GridHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {



       private TextView room_num ;
        /**
         * name
         */
        private TextView people_num ;



        public GridHolder(@NonNull View itemView) {
            super(itemView);
            room_num = itemView.findViewById(R.id.room_num);
            people_num =  itemView.findViewById(R.id.people_num);
           /* Drawable leftDrawable = null;
            leftDrawable = mContext.getResources().getDrawable(R.drawable.dot_focus);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                    leftDrawable.getMinimumHeight());
            room_num.setCompoundDrawables(leftDrawable, null,null , null);*/
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(onNestGridClick!= null)
            {
                onNestGridClick.onNestClick(getLayoutPosition(),v);
            }
        }
    }


}
