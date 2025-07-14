package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<ItemModel> itemList;
    private OnAddToCartListener onAddToCartListener; // 新增监听器

    public ItemAdapter(List<ItemModel> itemList) {
        this.itemList = itemList;
    }

    public interface OnAddToCartListener {
        void onAddToCart(int position);
    }

    public void setOnAddToCartListener(OnAddToCartListener listener) {
        this.onAddToCartListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.ivIcon.setImageResource(item.getIconResId());
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvPrice.setText(String.format("%.2f", item.getPrice())+"元"); // 显示价格，保留两位小数

        // 设置加入购物车按钮的点击事件
        holder.btnAddToCart.setOnClickListener(v -> {
            if (onAddToCartListener != null) {
                onAddToCartListener.onAddToCart(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice; // 新增价格的 TextView
        Button btnAddToCart;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPrice = itemView.findViewById(R.id.tv_price); // 绑定价格的 TextView
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}