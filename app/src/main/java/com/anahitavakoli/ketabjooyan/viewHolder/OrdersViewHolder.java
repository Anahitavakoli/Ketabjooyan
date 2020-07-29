package com.anahitavakoli.ketabjooyan.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anahitavakoli.ketabjooyan.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder{


    public TextView txtName, txtPhone, txtTotalPrice, txtAddress , txtTimeDate;
    public Button showDetailOrder;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.admin_orders_username);
        txtPhone = itemView.findViewById(R.id.admin_orders_phone);
        txtTotalPrice = itemView.findViewById(R.id.admin_orders_total_price);
        txtAddress = itemView.findViewById(R.id.admin_orders_address);
        txtTimeDate = itemView.findViewById(R.id.admin_orders_date_time);

        showDetailOrder = itemView.findViewById(R.id.btn_show_order_detail);
    }
}
