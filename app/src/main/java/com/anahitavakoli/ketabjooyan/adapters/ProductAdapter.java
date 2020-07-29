package com.anahitavakoli.ketabjooyan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.interfaces.ItemClickListener;
import com.anahitavakoli.ketabjooyan.model.Products;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<Products> productsInfo;
    LayoutInflater inflater;


    public ProductAdapter(Context context, List<Products> productInfo) {

        this.context = context;
        this.productsInfo = productInfo;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.product_items_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtProductName.setText(productsInfo.get(position).getPname());
        holder.txtProductDescription.setText(productsInfo.get(position).getPdescription());
        holder.txtProductPrice.setText(productsInfo.get(position).getPrice());
//        holder.imgProduct.setImageURI(productsInfo.get(position).getImage());


    }

    @Override
    public int getItemCount() {
        return productsInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ItemClickListener listener;
        public TextView txtProductName, txtProductDescription, txtProductPrice;
        public ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            imgProduct = itemView.findViewById(R.id.product_image);
            txtProductName = itemView.findViewById(R.id.product_name);
            txtProductDescription = itemView.findViewById(R.id.product_desciption);
            txtProductPrice = itemView.findViewById(R.id.product_price);
        }

        public void setItemClickListener(ItemClickListener listener)
        {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition(),false);
        }
    }
}
