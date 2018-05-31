package com.appsinventiv.onlineshopping.Category;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.appsinventiv.onlineshopping.Adapter.CategoryAdapter;
import com.appsinventiv.onlineshopping.Model.CategoryItem;
import com.appsinventiv.onlineshopping.R;

import java.util.ArrayList;
import java.util.List;

public class MainCategory extends AppCompatActivity {
    public static Activity fa;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CategoryAdapter adapter;
    List<CategoryItem> itemList = new ArrayList<CategoryItem>();
    String intentFromFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
        MainCategory.this.setTitle("Choose category ");

        fa = this;
        String category = "main";
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CategoryAdapter(itemList, MainCategory.this, MainCategory.this, category);
        recyclerView.setAdapter(adapter);


        Intent i = getIntent();
        intentFromFilters = i.getStringExtra("fromFilters");

        CategoryItem categoryItem;

        if (intentFromFilters != null) {
            categoryItem = new CategoryItem("All ads", R.drawable.all_ads_category);
            itemList.add(categoryItem);

            categoryItem = new CategoryItem("Mobiles", R.drawable.ic_mobile);
            itemList.add(categoryItem);
        } else {
            categoryItem = new CategoryItem("Mobiles", R.drawable.ic_mobile);
            itemList.add(categoryItem);
        }
        categoryItem = new CategoryItem("Electronics", R.drawable.ic_electronics);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Vehicles", R.drawable.ic_vehicle);
        itemList.add(categoryItem);



        categoryItem = new CategoryItem("Pets", R.drawable.ic_pets);
        itemList.add(categoryItem);


        categoryItem = new CategoryItem("Fashion", R.drawable.ic_fashion);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Kids", R.drawable.ic_kids);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Apartments", R.drawable.ic_apartments);
        itemList.add(categoryItem);


        categoryItem = new CategoryItem("Furniture", R.drawable.ic_furniture);
        itemList.add(categoryItem);

        categoryItem = new CategoryItem("Services", R.drawable.ic_services);
        itemList.add(categoryItem);



        adapter.notifyDataSetChanged();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
