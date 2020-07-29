package com.anahitavakoli.ketabjooyan.buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.adapters.ProductAdapter;
import com.anahitavakoli.ketabjooyan.admin.AdminProductMaitainActivity;
import com.anahitavakoli.ketabjooyan.info.ProductInfo;
import com.anahitavakoli.ketabjooyan.model.Products;
import com.anahitavakoli.ketabjooyan.prevalent.Prevalent;
import com.anahitavakoli.ketabjooyan.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity {


    private DatabaseReference productRef;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private RecyclerView recyclerView;
    ProductAdapter adapter;
    private FloatingActionButton floatingActionButton;
    RecyclerView.LayoutManager layoutManager;
    private String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        recyclerView = findViewById(R.id.recycler_menu);
        adapter = new ProductAdapter(this, new ProductInfo().getProductInfo());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

//        Intent i = getIntent();
//        Bundle bundle = i.getExtras();
//        if(bundle != null) {
//            type = getIntent().getExtras().get("admin").toString();
//        }
//        Paper.init(this);
//
//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("کتاب جویان");
//        setSupportActionBar(toolbar);
//
//        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        productRef = FirebaseDatabase.getInstance().getReference().child("products");
//
//        floatingActionButton = findViewById(R.id.fab);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!type.equals("admin")) {
//                    Intent intentCartList = new Intent(HomeActivity.this, CartActivity.class);
//                    startActivity(intentCartList);
//                }
//            }
//        });
//
//        View headerView = navigationView.getHeaderView(0);
//        TextView userNameTextView = headerView.findViewById(R.id.username_txt);
//        CircleImageView profileImage = headerView.findViewById(R.id.profile_image);
//
//        if(!type.equals("admin")) {
//            userNameTextView.setText(Prevalent.userUsername);
//            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImage);
//        }
//
//        if(getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR)
//        {
//            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        }
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(productRef, Products.class).build();
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
//
//                productViewHolder.txtProductName.setText(products.getPname());
//                productViewHolder.txtProductDescription.setText(products.getPdescription());
//                productViewHolder.txtProductPrice.setText(products.getPrice() + "تومان");
//
//                Picasso.get().load(products.getImage()).into(productViewHolder.imgProduct);
//
//                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if(type.equals("admin")){
//
//                            Intent intentEditAdmin = new Intent(HomeActivity.this, AdminProductMaitainActivity.class);
//                            intentEditAdmin.putExtra("pid", products.getPid());
//                            startActivity(intentEditAdmin);
//                        }
//                        else {
//                            Intent intentDetail = new Intent(HomeActivity.this, ProductDetailActivity.class);
//                            intentDetail.putExtra("pid", products.getPid());
//                            startActivity(intentDetail);
//                        }
//                    }
//                });
//
//
//            }
//
//            @NonNull
//            @Override
//            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
//                ProductViewHolder holder = new ProductViewHolder(view);
//                return holder;
//            }
//        };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//        int itemClicked = menuItem.getItemId();
//
//        if(itemClicked == R.id.nav_cart) {
//
//            if (!type.equals("admin")) {
//
//                Intent intentCartList = new Intent(HomeActivity.this, CartActivity.class);
//                startActivity(intentCartList);
//            }
//        }
//
//        else if(itemClicked == R.id.nav_search) {
//
//            if (!type.equals("admin")) {
//                Intent intentSearch = new Intent(HomeActivity.this, SearchProductsActivity.class);
//                startActivity(intentSearch);
//            }
//        }
//
//        else if(itemClicked == R.id.nav_category) {
//
//        }
//
//        else if(itemClicked == R.id.nav_tools) {
//
//            if (!type.equals("admin")) {
//                Intent intentSetting = new Intent(HomeActivity.this, SettingActivity.class);
//                startActivity(intentSetting);
//                finish();
//            }
//        }
//
//        else if(itemClicked == R.id.nav_logout) {
//
//            if (!type.equals("admin")) {
//                Paper.book().destroy();
//                Intent intentExit = new Intent(HomeActivity.this, MainActivity.class);
//                intentExit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intentExit);
//                finish();
//            }
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//
//        return false;
//    }
//
//
//    @Override
//    public void onBackPressed() {
//
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
}
