package com.anahitavakoli.ketabjooyan.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.model.Products;
import com.anahitavakoli.ketabjooyan.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {

    EditText inputSearchProduct;
    Button btnSearchProduct;
    RecyclerView recyclerSearchList;
    String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        inputSearchProduct = findViewById(R.id.input_search_product);
        btnSearchProduct = findViewById(R.id.btn_search_product);
        recyclerSearchList = findViewById(R.id.recycler_search_list);
        recyclerSearchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

        btnSearchProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputSearchProduct.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!TextUtils.isEmpty(searchInput)) {
            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products");

            FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(productRef.orderByChild("pname").startAt(searchInput), Products.class).build();

            FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(ProductViewHolder productViewHolder, int i, final Products products) {

                    productViewHolder.txtProductName.setText(products.getPname());
                    productViewHolder.txtProductDescription.setText(products.getPdescription());
                    productViewHolder.txtProductPrice.setText(products.getPrice() + "تومان");

                    Picasso.get().load(products.getImage()).into(productViewHolder.imgProduct);

                    productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentDetail = new Intent(SearchProductsActivity.this, ProductDetailActivity.class);
                            intentDetail.putExtra("pid", products.getPid());
                            startActivity(intentDetail);
                        }
                    });
                }

                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                    ProductViewHolder holder = new ProductViewHolder(view);
                    return holder;
                }
            };

            recyclerSearchList.setAdapter(adapter);
            adapter.startListening();
        }
    }
}
