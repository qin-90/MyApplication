package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.ItemAdapter;
import com.example.myapplication.ItemModel;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private RecyclerView rvCategoryItems;
    private ItemAdapter itemAdapter;
    private List<ItemModel> itemList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinnerCategory;
    private String selectedCategory = "最新"; // 默认分类

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // 初始化RecyclerView
        rvCategoryItems = view.findViewById(R.id.rv_category_items);
        rvCategoryItems.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseHelper = new DatabaseHelper(getContext());

        // 初始化下拉刷新
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                loadItems(selectedCategory);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // 初始化分类下拉菜单
        spinnerCategory = view.findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                loadItems(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // 加载默认分类数据
        loadItems(selectedCategory);

        return view;
    }

    private void loadItems(String category) {
        // 模拟数据加载
        itemList = new ArrayList<>();
        if (category.equals("最新")) {
            itemList.add(new ItemModel("汉堡", "经美国人改良后的美式汉堡风靡全球，是现代西式快餐中的主要食物。", R.drawable.hbb,15.66));
            itemList.add(new ItemModel("铁板炒饭", "铁板炒饭因粒粒松散、碎金闪烁、鲜美可口而著称，辅以火腿、虾仁、鸡蛋等配菜加上独特的工艺配料，绝对是快餐饮食中的佳品。", R.drawable.tbcf,15.66));
        } else if (category.equals("热门")) {
            itemList.add(new ItemModel("卤味饭", "卤味饭是一道菜品，主料为鸡全翅、鸡蛋等，鲜辣可口。", R.drawable.lwf,15.66));
            itemList.add(new ItemModel("肉夹馍", "肉夹馍是中国传统特色食物之一。名字意为“肉馅的夹馍”。", R.drawable.rjm,15.66));
        }

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

        rvCategoryItems.setAdapter(itemAdapter);
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