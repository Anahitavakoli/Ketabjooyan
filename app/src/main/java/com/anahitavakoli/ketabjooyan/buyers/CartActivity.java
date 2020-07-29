package com.anahitavakoli.ketabjooyan.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.model.Cart;
import com.anahitavakoli.ketabjooyan.prevalent.Prevalent;
import com.anahitavakoli.ketabjooyan.viewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private TextView txtTotalPrice,txtMsg1;
    private Button nextProcess;
    private RecyclerView.LayoutManager layoutManager;

    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recycler_cart_list);
        txtTotalPrice = findViewById(R.id.txt_total_price);
        nextProcess = findViewById(R.id.btn_next1);
        txtMsg1 = findViewById(R.id.msg1);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(layoutManager);
        recyclerViewCart.setHasFixedSize(true);

        nextProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                nextIntent.putExtra("total price", String.valueOf(totalPrice));
                startActivity(nextIntent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderStatus();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart list");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("user view")
        .child(Prevalent.currentOnlineUser.getPhoneNumber()).child("products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(CartViewHolder cartViewHolder, int i, final Cart cart) {

                cartViewHolder.txtProductQuantity.setText("تعداد: " + cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("قیمت: " + cart.getPrice() + "تومان");
                cartViewHolder.txtProductName.setText(cart.getPname());

                int oneProductPrice = (Integer.valueOf(cart.getPrice())) * (Integer.valueOf(cart.getQuantity()));

                totalPrice = totalPrice + oneProductPrice;

                txtTotalPrice.setText("قیمت کل: " + String.valueOf(totalPrice) + " تومان");

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "ویرایش",
                                        "حذف"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("جزئیات محصول: ");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    Intent editIntent = new Intent(CartActivity.this , ProductDetailActivity.class);
                                    editIntent.putExtra("pid", cart.getPid());
                                    startActivity(editIntent);
                                }

                                else if(which == 1){
                                    cartListRef.child("user view").child(Prevalent.currentOnlineUser.getPhoneNumber()).child("products")
                                            .child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                cartListRef.child("admin view").child(Prevalent.currentOnlineUser.getPhoneNumber()).child("products")
                                                        .child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(CartActivity.this, "سفارش پاک شد", Toast.LENGTH_SHORT).show();
                                                            Intent  removeIntent = new Intent(CartActivity.this , HomeActivity.class);
                                                            startActivity(removeIntent);
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    });
                                }
                            }
                        });

                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout , parent , false);

                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerViewCart.setAdapter(adapter);
        adapter.startListening();

    }

    private void checkOrderStatus(){

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevalent.currentOnlineUser.getPhoneNumber());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String shippingStatus = dataSnapshot.child("state").getValue().toString();
                    String shippingName = dataSnapshot.child("name").getValue().toString();

                    if(shippingStatus.equals("shipped")){

                        txtTotalPrice.setText( shippingName + "عزیز! \n " + "سفارش شما ارسال شده است");
                        recyclerViewCart.setVisibility(View.GONE);
                        nextProcess.setVisibility(View.GONE);
                        txtMsg1.setText("به زودی سفارش خود را درب منزل دریافت خواهید کرد...");
                        txtMsg1.setVisibility(View.VISIBLE);
                    }

                    else if(shippingStatus.equals("not shipped")){
                        txtTotalPrice.setText( shippingName + "عزیز! \n " + "سفارش شما ارسال نشده است");
                        recyclerViewCart.setVisibility(View.GONE);
                        nextProcess.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
