package com.appsinventiv.onlineshopping.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.onlineshopping.Activities.Filters;
import com.appsinventiv.onlineshopping.Activities.SubmitAd;
import com.appsinventiv.onlineshopping.Category.MainCategory;
import com.appsinventiv.onlineshopping.Model.CategoryItem;
import com.appsinventiv.onlineshopping.R;

import java.util.List;

/**
 * Created by AliAh on 04/01/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    List<CategoryItem> itemList;
    Activity activity;
    Context ctx;
    LayoutInflater mLInflater;
    String category;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView category_image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.category_title);
            category_image=view.findViewById(R.id.category_image);

        }
    }

    public CategoryAdapter(List<CategoryItem> itemList, Activity activity, Context ctx,String category) {
        this.itemList = itemList;
        this.activity = activity;
        this.ctx = ctx;
        this.category=category;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CategoryItem categoryItem = itemList.get(position);
        holder.title.setText(categoryItem.getItemName());
        holder.category_image.setImageResource(categoryItem.getIcon());


        if(category.equals("child")){
            holder.category_image.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(category.equals("main")) {

                        SubmitAd.mainCategory = categoryItem.getItemName();
                        Filters.mainCategory=categoryItem.getItemName();
//                        Toast.makeText(activity, ""+categoryItem.getItemName(), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(ctx, ChildCategory.class);
//                        i.putExtra("mainCategory", position);
//
//                        ctx.startActivity(i);
                        MainCategory.fa.finish();
                    }
//                    else if(category.equals("child")){
//                        holder.category_image.setVisibility(View.GONE);
//
//
//                        SubmitAd.childCategory=categoryItem.getItemName();
//
////                        Toast.makeText(activity, ""+categoryItem.getItemName(), Toast.LENGTH_SHORT).show();
//
//                        MainCategory.fa.finish();
//                        ChildCategory.fa.finish();
//
//
////                    }if(category.equals("subchild")){
////                        SubmitAd.subChild=categoryItem.getItemName();
////
//////                        Toast.makeText(activity, ""+categoryItem.getItemName(), Toast.LENGTH_SHORT).show();
////                        MainCategory.fa.finish();
////                        ChildCategory.fa.finish();
//////                        SubChild.fa.finish();
//
//
//
//
//                    }
                }
            });

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
