package com.example.assessment_2.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assessment_2.R;
import com.example.assessment_2.activity.BrandDetailActivity;
import com.example.assessment_2.adapter.BrandListAdapter;
import com.example.assessment_2.base.IRecyclerViewItemClickListener;
import com.example.assessment_2.model.MotorcycleBrandDto;
import com.example.assessment_2.model.MotorcycleBrandListResponse;
import com.example.assessment_2.util.HttpUtil;
import com.example.assessment_2.util.OkHttpManager;

import java.util.ArrayList;
import java.util.List;

public class BrandFragment extends Fragment {

  private RecyclerView recyclerView;
  private BrandListAdapter adapter;
  private List<MotorcycleBrandDto> list = new ArrayList<>();

  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    //返回当前页面容器layout
    View view = inflater.inflate(R.layout.page_brand, null);
    initRecyclerView(view);
    getList();
    return view;
  }

  private void getList() {
    new OkHttpManager(getActivity(), HttpUtil.BRAND_LIST, null, MotorcycleBrandListResponse.class, true, new OkHttpManager.ResponseCallback() {
      public void onError(int errorType, int errorCode, String errorMsg) {
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
      }

      public void onSuccess(Object response) {
        if (response != null && response instanceof MotorcycleBrandListResponse) {
          MotorcycleBrandListResponse resp = (MotorcycleBrandListResponse) response;
          list.clear();
          list.addAll(resp.data);
          adapter.notifyDataSetChanged();
        }
      }
    }).execute();
  }

  private void initRecyclerView(View view) {
    recyclerView = view.findViewById(R.id.recyclerView);
    adapter = new BrandListAdapter(getContext(), R.layout.item_brand, list);
    //获取layout结构设置 决定布局属性
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    //为recyclerview设置适配器
    recyclerView.setAdapter(adapter);
    adapter.setItemClickListener(new IRecyclerViewItemClickListener() {
      public void onItemClickListener(RecyclerView.ViewHolder holder) {
        MotorcycleBrandDto motorcycleBrandDto = list.get(holder.getLayoutPosition());
        Intent intent = new Intent(getActivity(), BrandDetailActivity.class);
        intent.putExtra("BRAND", motorcycleBrandDto);
        startActivity(intent);
      }
    });
  }
}

