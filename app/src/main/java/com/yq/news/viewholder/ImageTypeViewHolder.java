package com.yq.news.viewholder;


import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yq.news.R;

/**
 * 热门应用
 */
public class ImageTypeViewHolder extends TypeAbstractViewHolder {

    private ImageView tag_img ;
    /**
     * name
     */
    private TextView tag_name ;
    public ImageTypeViewHolder(@NonNull View itemView) {
        super(itemView);
        tag_img = itemView.findViewById(R.id.tag_img);
        tag_name =  itemView.findViewById(R.id.tag_name);
    }

    @Override
    public void bindHolder(Object obj,int position,int pos) {
        switch (position)
        {
            case 0:
                switch (pos)
                {
                    case 0:
                        tag_img.setImageResource(R.drawable.csgl);
                        break;
                    case 1:
                        tag_img.setImageResource(R.drawable.rwzx);
                        break;
                    case 2:
                        tag_img.setImageResource(R.drawable.qbzx);
                        break;

                }
                break;

            case 1:
                switch (pos)
                {
                    case 0:
                        tag_img.setImageResource(R.drawable.tjzk);
                        break;
                    case 1:
                       tag_img.setImageResource(R.drawable.tjyg);
                        break;
                    case 2:
                       tag_img.setImageResource(R.drawable.tjfk);
                        break;

                }
                break;
        }
    }
}
