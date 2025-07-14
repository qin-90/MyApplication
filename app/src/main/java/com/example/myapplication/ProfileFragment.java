package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvProfileName = view.findViewById(R.id.tv_profile_name);
        ImageView ivProfileImage = view.findViewById(R.id.iv_profile_image);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button cart = view.findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(),CartActivity.class);
                startActivity(intent);
            }
        });
        // 设置个人信息
        tvProfileName.setText("用户芜湖");
        ivProfileImage.setImageResource(R.drawable.ic_profile);

        return view;
    }
}