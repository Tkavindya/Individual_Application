package com.example.assignment_individual;

import static android.os.Build.VERSION_CODES.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class activity extends AppCompatActivity implements ShoeItemAdapter.ShoeClickedListeners {

    private RecyclerView recyclerView;
    private List<ShoeItem> shoeItemList;
    private ShoeItemAdapter adapter;
    private CartViewModel viewModel;
    private List<ShoeCart> shoeCartList;
    private CoordinatorLayout coordinatorLayout;
    private ImageView cartImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

        initializeVariables();
        setUpList();

        adapter.setShoeItemList(shoeItemList);
        recyclerView.setAdapter(adapter);


        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity.this, CartActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getAllCartItems().observe(this, new Observer<List<ShoeCart>>() {
            @Override
            public void onChanged(List<ShoeCart> shoeCarts) {
                shoeCartList.addAll(shoeCarts);
            }
        });
    }

    private void setUpList() {
        shoeItemList.add(new ShoeItem("Dunk", "Nike", R.drawable.nike_revolution_road, 3200));
        shoeItemList.add(new ShoeItem("Blaza", "NIKE", R.drawable.flex_run_road_running, 2000));
        shoeItemList.add(new ShoeItem("Cortez", "NIKE", R.drawable.nikecourt_zoom_vapor_cage, 2800));
        shoeItemList.add(new ShoeItem("Gazelle", "ADIDAS", R.drawable.adidas_eq_run, 1900));
        shoeItemList.add(new ShoeItem("Ozelia", "ADIDAS", R.drawable.adidas_ozelia_shoes_grey, 2000));
        shoeItemList.add(new ShoeItem("Questar", "ADIDAS", R.drawable.adidas_questar_shoes, 2200));
        shoeItemList.add(new ShoeItem("Questar", "ADIDAS", R.drawable.adidas_questar_shoes, 2500));
        shoeItemList.add(new ShoeItem("Ultraboost", "ADIDAS", R.drawable.adidas_ultraboost, 1950));

    }

    private void initializeVariables() {

        cartImageView = findViewById(R.id.cartIv);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        shoeCartList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        shoeItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ShoeItemAdapter(this);

    }

    @Override
    public void onCardClicked(ShoeItem shoe) {

        Intent intent = new Intent(activity.this, DetailedActivity.class);
        intent.putExtra("shoeItem", shoe);
        startActivity(intent);
    }

    @Override
    public void onAddToCartBtnClicked(ShoeItem shoeItem) {
        ShoeCart shoeCart = new ShoeCart();
        shoeCart.setShoeName(shoeItem.getShoeName());
        shoeCart.setShoeBrandName(shoeItem.getShoeBrandName());
        shoeCart.setShoePrice(shoeItem.getShoePrice());
        shoeCart.setShoeImage(shoeItem.getShoeImage());

        final int[] quantity = {1};
        final int[] id = new int[1];

        if (!shoeCartList.isEmpty()) {
            for (int i = 0; i < shoeCartList.size(); i++) {
                if (shoeCart.getShoeName().equals(shoeCartList.get(i).getShoeName())) {
                    quantity[0] = shoeCartList.get(i).getQuantity();
                    quantity[0]++;
                    id[0] = shoeCartList.get(i).getId();
                }
            }
        }

        Log.d("TAG", "onAddToCartBtnClicked: " + quantity[0]);

        if (quantity[0] == 1) {
            shoeCart.setQuantity(quantity[0]);
            shoeCart.setTotalItemPrice(quantity[0] * shoeCart.getShoePrice());
            viewModel.insertCartItem(shoeCart);
        } else {
            viewModel.updateQuantity(id[0], quantity[0]);
            viewModel.updatePrice(id[0], quantity[0] * shoeCart.getShoePrice());
        }

        makeSnackBar("Item Added To Cart");
    }

    private void makeSnackBar(String msg) {
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT)
                .setAction("Go to Cart", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(activity.this, CartActivity.class));
                    }
                }).show();
    }
}