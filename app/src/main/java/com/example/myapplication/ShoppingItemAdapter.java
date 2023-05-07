package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> implements Filterable{
    private ArrayList<ShoppingItem> shoppingItemData;
    private ArrayList<ShoppingItem> shoppingItemsDataAll;
    private Context context;
    private int lastPosition = -1;
    private Filter ShoppingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ShoppingItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = shoppingItemsDataAll.size();
                results.values = shoppingItemsDataAll;
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(ShoppingItem item : shoppingItemsDataAll){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = shoppingItemsDataAll.size();
                results.values = shoppingItemsDataAll;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            shoppingItemData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    ShoppingItemAdapter(Context context, ArrayList<ShoppingItem> itemsData){
        this.shoppingItemData = itemsData;
        this.shoppingItemsDataAll = itemsData;
        this.context = context;
    }

    @NonNull
    @Override
    public ShoppingItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder( ShoppingItemAdapter.ViewHolder holder, int position) {
        ShoppingItem currentItem = this.shoppingItemData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return shoppingItemData.size();
    }

    @Override
    public Filter getFilter() {
        return ShoppingFilter;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        private TextView infoText;
        private TextView priceText;
        private ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titleText = itemView.findViewById(R.id.itemTitle);
            this.infoText  = itemView.findViewById(R.id.subTitle);
            this.priceText  = itemView.findViewById(R.id.price);
            this.itemImage  = itemView.findViewById(R.id.itemImage);

        }

        public void bindTo(ShoppingItem currentItem) {
            this.titleText.setText(currentItem.getName());
            this.infoText.setText(currentItem.getInfo());
            this.priceText.setText(currentItem.getPrice());

            Glide.with(context).load(currentItem.getImageResource()).into(itemImage);
            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.button_scale);
                    v.startAnimation(animation);
                    ((ShopListActivity) context).updateAlertIcon(currentItem);
                }
            });
        }
    }
}

