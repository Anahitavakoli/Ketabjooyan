package com.anahitavakoli.ketabjooyan.admin;

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

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.model.AdminOrders;
import com.anahitavakoli.ketabjooyan.viewHolder.OrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    RecyclerView ordersList;
    DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);


        ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");
        ordersList = findViewById(R.id.recycler_new_orders);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef,AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders, OrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, OrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(OrdersViewHolder ordersViewHolder, final int i, final AdminOrders adminOrders) {

                ordersViewHolder.txtName.setText("نام: " +  adminOrders.getName());
                ordersViewHolder.txtPhone.setText("تلفن: " + adminOrders.getPhone());
                ordersViewHolder.txtTotalPrice.setText("قیمت کل: " +  adminOrders.getTotalAmount());
                ordersViewHolder.txtAddress.setText("آدرس: " + adminOrders.getCity() + " و " + adminOrders.getAddress() );
                ordersViewHolder.txtTimeDate.setText("زمان: " +adminOrders.getTime() + " و " + adminOrders.getDate());

                ordersViewHolder.showDetailOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String userID = getRef(i).getKey();

                        Intent detailIntent = new Intent(AdminNewOrdersActivity.this, AdminUserProductActivity.class);
                        detailIntent.putExtra("uid", userID);
                        startActivity(detailIntent);
                    }
                });

                ordersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "بله",
                                        "خیر"
                                };

                        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("آیا این محصول آماده ارسال است؟");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0)
                                {
                                    String userID = getRef(i).getKey();
                                    removeOrders(userID);

                                }
                                else
                                    {
                                   finish();
                                }
                            }
                        });

                        builder.show();

                    }
                });
            }

            @NonNull
            @Override
            public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item_layout , parent , false);

                OrdersViewHolder holder = new OrdersViewHolder(view);
                return holder;
            }
        };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrders(String uID) {

        ordersRef.child(uID).removeValue();
    }
}
