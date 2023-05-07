package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{
    private ArrayList<CartItem> cartItemData;
    private ArrayList<CartItem> cartItemsDataAll;
    private Context context;
    private int lastPosition = -1;

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemAdapter.ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        CartItem currentItem = this.cartItemData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return cartItemData.size();
    }

   CartItemAdapter(Context context, ArrayList<CartItem> itemsData){
        this.cartItemData = itemsData;
        this.cartItemsDataAll = itemsData;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        private TextView priceText;
        private ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titleText = itemView.findViewById(R.id.cartItemTitle);
            this.priceText  = itemView.findViewById(R.id.cartItemPrice);
            this.itemImage  = itemView.findViewById(R.id.cartItemImage);
        }

        public void bindTo(CartItem currentItem) {
            this.titleText.setText(currentItem.getName());
            this.priceText.setText(currentItem.getPrice());

            Glide.with(context).load(currentItem.getImageResource()).into(itemImage);
            itemView.findViewById(R.id.delete).setOnClickListener(view -> ((ShoppingCartActivity)context).deleteItem(currentItem));
        }
    }
}
