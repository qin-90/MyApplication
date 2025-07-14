package com.example.myapplication;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<ItemModel> cartItems;
    private DatabaseHelper databaseHelper;
    private TextView tvTotalPrice; // 总价格的 TextView

    public CartAdapter(List<ItemModel> cartItems, DatabaseHelper databaseHelper, TextView tvTotalPrice) {
        this.cartItems = cartItems;
        this.databaseHelper = databaseHelper;
        this.tvTotalPrice = tvTotalPrice;

        // 在构造函数中直接更新总价格
        updateTotalPrice();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ItemModel item = cartItems.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvPrice.setText(String.format("价格:%.2f元", item.getPrice())); // 显示价格

        // 设置物品图片
        holder.ivItemImage.setImageResource(item.getIconResId());
        Log.d("item.getIconResId()", String.valueOf(item.getIconResId()));

        // 增加数量
        holder.btnIncrease.setOnClickListener(v -> {
            int quantity = item.getQuantity() + 1;
            databaseHelper.updateQuantityInCart(item.getTitle(), quantity);
            item.setQuantity(quantity);
            holder.tvQuantity.setText(String.valueOf(quantity));
            updateTotalPrice(); // 更新总价格
        });

        // 减少数量
        holder.btnDecrease.setOnClickListener(v -> {
            int quantity = item.getQuantity();
            if (quantity > 1) {
                quantity--;
                databaseHelper.updateQuantityInCart(item.getTitle(), quantity);
                item.setQuantity(quantity);
                holder.tvQuantity.setText(String.valueOf(quantity));
            } else {
                databaseHelper.deleteItemFromCart(item.getTitle());
                cartItems.remove(position);
                notifyItemRemoved(position);
            }
            updateTotalPrice(); // 更新总价格
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvQuantity;
        TextView tvPrice;
        Button btnIncrease;
        Button btnDecrease;
        ImageView ivItemImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            ivItemImage = itemView.findViewById(R.id.iv_item_image);
        }
    }

    // 计算并更新总价格
    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (ItemModel item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        tvTotalPrice.setText(String.format("价格: %.2f元", totalPrice));
    }
}