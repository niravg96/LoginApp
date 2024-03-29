package com.example.loginapp;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class Favourite_Iteam_RecyclerAdapter extends RecyclerView.Adapter<Favourite_Iteam_View_Holder>
{
    List<Favourite_Iteam_Data> list = Collections.emptyList();
    Context context;
    ItemClickListener itemClickListener;

    public Favourite_Iteam_RecyclerAdapter(List<Favourite_Iteam_Data> list, Context context, ItemClickListener itemClickListener) {
        this.list = list;
        this.context = context;
        this.itemClickListener =itemClickListener;
    }
    @NonNull
    @Override
    public Favourite_Iteam_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater inflater =LayoutInflater.from(context);
        View photoview = inflater.inflate(R.layout.favourite_category_views,parent,false);

        Favourite_Iteam_View_Holder viewHolder = new Favourite_Iteam_View_Holder(photoview);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final Favourite_Iteam_View_Holder holder,int position) {

        holder.txtProductId.setText(list.get(position).productId);
        holder.txtProductName.setText(list.get(position).productName);
        holder.IVCancelOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public interface ItemClickListener{

        void onItemClicked(int position);
    }
}
