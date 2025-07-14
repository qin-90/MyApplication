package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_PHONE = "phone";

    private static final String TABLE_CART = "cart";
    private static final String COLUMN_CART_ITEM_ID = "item_id";
    private static final String COLUMN_CART_TITLE = "title";
    private static final String COLUMN_CART_QUANTITY = "quantity";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT NOT NULL, " +
            COLUMN_PASSWORD + " TEXT NOT NULL, " +
            COLUMN_AGE + " INTEGER, " +
            COLUMN_PHONE + " TEXT);";

    private static final String COLUMN_CART_ICON_RES_ID = "icon_res_id";

    private static final String COLUMN_CART_PRICE = "price";

    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + " (" +
            COLUMN_CART_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CART_TITLE + " TEXT NOT NULL, " +
            COLUMN_CART_QUANTITY + " INTEGER NOT NULL, " +
            COLUMN_CART_ICON_RES_ID + " INTEGER NOT NULL, " +
            COLUMN_CART_PRICE + " REAL NOT NULL);"; // 新增价格字段
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_CART); // 创建购物车表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART); // 删除购物车表
        onCreate(db);
    }

    public boolean insertUser(String username, String password, int age, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + COLUMN_AGE + ", " + COLUMN_PHONE + ") VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, username);
        statement.bindString(2, password);
        statement.bindLong(3, age);
        statement.bindString(4, phone);
        long result = statement.executeInsert();
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        String[] columns = {COLUMN_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " =? AND " + COLUMN_PASSWORD + " =?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }
    public boolean addItemToCart(String title, int iconResId, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_CART + " (" + COLUMN_CART_TITLE + ", " + COLUMN_CART_QUANTITY + ", " + COLUMN_CART_ICON_RES_ID + ", " + COLUMN_CART_PRICE + ") VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, title);
        statement.bindLong(2, 1); // 默认数量为 1
        statement.bindLong(3, iconResId); // 存储图片资源 ID
        statement.bindDouble(4, price); // 存储价格
        long result = statement.executeInsert();
        return result != -1;
    }

    // 更新购物车中的物品数量
    public boolean updateQuantityInCart(String title, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, quantity);
        String whereClause = COLUMN_CART_TITLE + " = ?";
        String[] whereArgs = {title};
        int result = db.update(TABLE_CART, values, whereClause, whereArgs);
        return result > 0;
    }

    // 获取购物车中的物品数量
    public int getQuantityInCart(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_CART_QUANTITY};
        String selection = COLUMN_CART_TITLE + " = ?";
        String[] selectionArgs = {title};
        Cursor cursor = db.query(TABLE_CART, columns, selection, selectionArgs, null, null, null);
        int quantity = 0;
        if (cursor != null && cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
        }
        cursor.close();
        return quantity;
    }

    public List<ItemModel> getCartItems() {
        List<ItemModel> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_CART_TITLE, COLUMN_CART_QUANTITY, COLUMN_CART_ICON_RES_ID, COLUMN_CART_PRICE};
        Cursor cursor = db.query(TABLE_CART, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_TITLE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
                int iconResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ICON_RES_ID));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CART_PRICE)); // 获取价格
                ItemModel item = new ItemModel(title, "", iconResId, price); // 传递价格
                item.setQuantity(quantity);
                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    // 删除购物车中的物品
    public boolean deleteItemFromCart(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_CART_TITLE + " = ?";
        String[] whereArgs = {title};
        int result = db.delete(TABLE_CART, whereClause, whereArgs);
        return result > 0;
    }
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CART);
    }
}