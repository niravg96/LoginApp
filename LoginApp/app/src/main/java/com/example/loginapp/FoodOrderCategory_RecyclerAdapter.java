package com.example.loginapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodOrderCategory_RecyclerAdapter extends RecyclerView.Adapter<FoodOrderCategory_RecyclerAdapter.FoodOrderCategory_Holder> {

    FoodOrderCategory_Items foodOrderCategoryItems;
    List<FoodOrderCategory_Data> allUsersList;
    Context context;

    ItemClickedlistener itemClickedlistener;


    public FoodOrderCategory_RecyclerAdapter(FoodOrderCategory_Items foodOrderCategoryItems,Context context, List<FoodOrderCategory_Data> allUsersList,ItemClickedlistener itemClickedlistener) {
        this.foodOrderCategoryItems = foodOrderCategoryItems;
        this.context = context;
        this.allUsersList = allUsersList;
        this.itemClickedlistener =itemClickedlistener;
    }
    @NonNull
    @Override
    public FoodOrderCategory_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodOrderCategory_Holder(LayoutInflater.from(foodOrderCategoryItems).inflate(R.layout.food_category_items_views,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull FoodOrderCategory_Holder holder, int position) {

        holder.txtProductId.setText(allUsersList.get(position).getId());
        holder.txtProductName.setText(allUsersList.get(position).getProduct_name());
        holder.txtProductDescription.setText(allUsersList.get(position).getDescription());
        holder.txtPrice.setText(allUsersList.get(position).getRate());

        if(allUsersList.get(position).isfavourite)
        {
            holder.IVFavouriteItem.setImageResource(R.drawable.fullheart);
        }
        else
        {
            holder.IVFavouriteItem.setImageResource(R.drawable.favourite_icon);
        }
        holder.IVFavouriteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(allUsersList.get(position).isfavourite)
                {
                    itemClickedlistener.onItemClicked(position,"UNFAVOURITE");
                }
                else
                {
                    itemClickedlistener.onItemClicked(position,"FAVOURITE");
                }
            }
        });
    }
    @Override
    public int getItemCount() {

        return allUsersList.size();
    }
    class FoodOrderCategory_Holder extends RecyclerView.ViewHolder{

        TextView txtProductName,txtProductDescription,txtPrice,txtProductId;
        Button btnFoodAdd;
        ImageView IVFavouriteItem;


        public FoodOrderCategory_Holder(@NonNull View itemView) {
            super(itemView);

            txtProductId =itemView.findViewById(R.id.FoodProductId);
            txtProductName=itemView.findViewById(R.id.FoodProductName);
            txtProductDescription=itemView.findViewById(R.id.FoodProductDescription);
            txtPrice=itemView.findViewById(R.id.FoodProductPrice);
            btnFoodAdd=itemView.findViewById(R.id.FoodAddButton);
            IVFavouriteItem=itemView.findViewById(R.id.favourite_food_icon);
        }
    }
    public interface ItemClickedlistener
    {
        void onItemClicked(int position,String choice);
    }
}
