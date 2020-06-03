package com.example.assessment_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.assessment_2.model.AccountItem;
import com.example.assessment_2.model.AppDataFactory;
import com.example.assessment_2.util.HttpUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LoginActivity extends Activity {

    EditText et_UserName;
    EditText et_PassWord;
    Button btn_Login;

    String userName;
    String passWord;

    List<AccountItem> accountList = AppDataFactory.getAccountList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);
        btn_Login = findViewById(R.id.btn_Login);
        et_UserName = findViewById(R.id.et_UserName);
        et_PassWord = findViewById(R.id.et_PassWord);

        initLoginClickListener();
    }

    private void initLoginClickListener() {
        btn_Login.setOnClickListener(view -> {
            String userName_input = et_UserName.getText().toString();
            String passWord_input = et_PassWord.getText().toString();

//            et_UserName.setText("admin");
//            et_PassWord.setText("admin");

//            String userName_input = "admin";
//            String passWord_input = "admin";

//            boolean isUser = false;
//
//            for (AccountItem bean : accountList){
//                String userName = bean.getUserName();
//                String passWord = bean.getPassWord();
//
//                if (userName.equals(userName_input) && passWord.equals(passWord_input)) {
//                   isUser = true;
//                   break;
//                }
//            }
            tryToLogin(userName_input,passWord_input);
//            if(isUser == true) {
//
//            }
//            else
//            {
//                Toast.makeText(this,"User Name or Password wrong", Toast.LENGTH_SHORT).show();
//            }
        });
    }

    /**
     * 进行登录请求的数据库查询
     *
     * @param username 用户名
     * @param password 密码
     */
    private void tryToLogin(String username, String password) {
        String postUrl = HttpUtil.HOST + "api/user/login";
        //1.创建一个队列
        RequestQueue queue = NoHttp.newRequestQueue();
        //2.创建消息请求   参数1:String字符串,传网址  参数2:请求方式
        final Request<JSONObject> request = NoHttp.createJsonObjectRequest(postUrl, RequestMethod.POST);
        //3.利用队列去添加消息请求
        //使用request对象添加上传的对象添加键与值,post方式添加上传的数据
        request.add("username", username);
        request.add("password", password);

        queue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                JSONObject res = response.get();
                try {
                    if (res.getInt("status") == 0) {
                        JSONObject data = res.getJSONObject("data");

                        //登录成功，跳转到菜单展示的主页面
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this,"User Name or Password wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
}
