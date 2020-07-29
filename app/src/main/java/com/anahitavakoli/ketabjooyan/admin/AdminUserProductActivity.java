package com.anahitavakoli.ketabjooyan.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.model.Cart;
import com.anahitavakoli.ketabjooyan.viewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductActivity extends AppCompatActivity {

    private RecyclerView recyclerUserProductList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_product);

        userId = getIntent().getStringExtra("uid");
        System.out.println("@@@@@@@@@@@@@@" + userId);

        recyclerUserProductList = findViewById(R.id.recycler_detail_order_list);
        recyclerUserProductList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerUserProductList.setLayoutManager(layoutManager);
        cartListRef = FirebaseDatabase.getInstance().getReference().child("cart list").child("admin view").child(userId).child("products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef,Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(CartViewHolder cartViewHolder, int i, Cart cart) {

                cartViewHolder.txtProductQuantity.setText("تعداد: " + cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("قیمت: " + cart.getPrice() + "تومان");
                cartViewHolder.txtProductName.setText(cart.getPname());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout , parent , false);

                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerUserProductList.setAdapter(adapter);
        adapter.startListening();

    }
}
