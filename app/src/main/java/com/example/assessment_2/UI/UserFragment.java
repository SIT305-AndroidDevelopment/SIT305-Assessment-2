package com.example.assessment_2.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.assessment_2.R;

public class UserFragment extends Fragment {
    View view;
    TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_home, container, false);
//        title = view.findViewById(R.id.txt_home_title);
        //title.setText("我是" + getResources().getString(R.string.txt_nav_home));

//生成json
//        List<StoreBean> storeBeans = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            StoreBean storeBean = new StoreBean();
//            storeBean.setLatitude(1.1);
//            storeBean.setLongtitude(2.2);
//            storeBean.setName("3");
//            storeBeans.add(storeBean);
//        }
////
//        Gson gson = new Gson();
//        String toJson = gson.toJson(storeBeans);
//
        return view;
    }

    public void Load() {
        //title.setText("当前时间：\n" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

    }

}
