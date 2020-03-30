package com.yq.news.viewholder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;



public abstract  class TypeAbstractViewHolder extends RecyclerView.ViewHolder {

    public TypeAbstractViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindHolder(Object obj,int outPos,int nestPos);
}
