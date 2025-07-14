package com.example.myapplication;


import android.util.Log;
public class ItemModel {
    private String title;
    private String description;
    private int iconResId;
    private int quantity; // 数量
    private double price; // 新增价格字段

    public ItemModel(String title, String description, int iconResId, double price) {
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
        this.price = price; // 初始化价格
        this.quantity = 0; // 默认数量为 0
    }

    // Getter 和 Setter 方法
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}