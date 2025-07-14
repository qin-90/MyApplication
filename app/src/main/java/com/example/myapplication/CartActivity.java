package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;
    private DatabaseHelper databaseHelper;
    private TextView tvTotalPrice;
    private List<ItemModel> cartItems;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // 初始化视图
        rvCartItems = findViewById(R.id.rv_cart_items);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        Button btnPay = findViewById(R.id.btn_pay);

        // 初始化数据库助手
        databaseHelper = new DatabaseHelper(this);

        // 从数据库获取购物车中的物品
        cartItems = databaseHelper.getCartItems();

        // 初始化适配器并设置给 RecyclerView
        cartAdapter = new CartAdapter(cartItems, databaseHelper, tvTotalPrice);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(cartAdapter);

        // 初始加载时更新总价格
        cartAdapter.notifyDataSetChanged(); // 触发更新总价格

        // 支付按钮点击事件
        btnPay.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(CartActivity.this, "购物车为空！", Toast.LENGTH_SHORT).show();
            } else {
                // 弹出支付成功的提示
                Toast.makeText(CartActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();

                // 清空购物车
                databaseHelper.clearCart();
                cartItems.clear();
                cartAdapter.notifyDataSetChanged();

                // 跳转到 MainActivity
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 清空返回栈
                startActivity(intent);
            }
        });
    }
}