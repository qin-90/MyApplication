package com.example.myapplication;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rvItems;
    private ItemAdapter itemAdapter;
    private List<ItemModel> itemList;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvItems = view.findViewById(R.id.rv_items);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());

        itemList = new ArrayList<>();
        itemList.add(new ItemModel("黄焖鸡米饭", "又叫香鸡煲、浓汁鸡煲，起源于山东省济南市的鲁菜系家常菜品，主要食材是鸡腿肉，配以青椒、土豆、香菇等焖制而成，味道美妙",
                R.drawable.hmj,15.66));
        itemList.add(new ItemModel("大盘鸡",
                "又名沙湾大盘鸡、辣子炒鸡，是新疆维吾尔自治区塔城地区沙湾市的特色美食，20世纪80年代起源于新疆公路边饭馆的江湖菜", R.drawable.dpj,15.66));
        itemList.add(new ItemModel("红烧肉",
                "一道著名的大众菜肴，各大菜系都有自己特色的红烧肉", R.drawable.hsr,15.66));


        itemAdapter = new ItemAdapter(itemList);
        itemAdapter.setOnAddToCartListener(position -> {
            ItemModel item = itemList.get(position);
            int quantity = databaseHelper.getQuantityInCart(item.getTitle());
            if (quantity == 0) {
                databaseHelper.addItemToCart(item.getTitle(),item.getIconResId(),item.getPrice());
            } else {
                databaseHelper.updateQuantityInCart(item.getTitle(), quantity + 1);
            }
            // 更新列表显示
            item.setQuantity(databaseHelper.getQuantityInCart(item.getTitle()));
            itemAdapter.notifyItemChanged(position);
        });
        rvItems.setAdapter(itemAdapter);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        // 更新列表
        for (ItemModel item : itemList) {
            int quantity = databaseHelper.getQuantityInCart(item.getTitle());
            item.setQuantity(quantity);
        }
        itemAdapter.notifyDataSetChanged();
    }
}