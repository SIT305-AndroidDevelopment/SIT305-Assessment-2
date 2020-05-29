package com.example.assessment_2.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.assessment_2.R;

import static android.widget.Toast.*;

public class logInFragment extends Fragment {
    View view;
    TextView title;
    EditText name;
    EditText pass;
    Toast mToast;
    FragmentActivity mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContext = getActivity();
        this.mToast = Toast.makeText();
        name = (EditText) inflater.inflate(R.layout.page_login,container,false);
        pass = (EditText) inflater.inflate(R.layout.page_login,container,false);
//
//
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

    public void check(View v){
        String uname = "Chase";
        String upass = "1234";
        String user = name.getText().toString().trim();
        String pwd = pass.getText().toString().trim();
        if(user.equals(uname)&&pwd.equals(upass))
        {
            makeText(mContext,"successful", LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(mContext,"sorry", LENGTH_SHORT).show();
        }

    }
    public void Load() {
        //title.setText("当前时间：\n" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

    }

}
