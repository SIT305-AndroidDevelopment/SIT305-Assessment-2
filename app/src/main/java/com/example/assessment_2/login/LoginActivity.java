package com.example.assessment_2.login;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assessment_2.R;
import com.example.assessment_2.activity.RegisterActivity;
import com.example.assessment_2.base.BaseActivity;
import com.example.assessment_2.util.HttpUtil;
import com.example.assessment_2.util.OkHttpManager;
import com.example.assessment_2.util.UserInfoManager;

public class LoginActivity extends BaseActivity {

  EditText et_UserName;
  EditText et_PassWord;
  Button btn_Login;

  public void initView(String titleName) {
    super.initView("用户登录");
    btn_Login = findViewById(R.id.btn_Login);
    et_UserName = findViewById(R.id.user_name_et);
    et_PassWord = findViewById(R.id.password_et);
    this.findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Intent mIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(mIntent);
      }
    });
    initLoginClickListener();
  }

  protected int getLayout() {
    return R.layout.page_login;
  }

  private void initLoginClickListener() {
    btn_Login.setOnClickListener(view -> {
      String userName_input = et_UserName.getText().toString();
      String passWord_input = et_PassWord.getText().toString();
      if (userName_input.isEmpty()) {
        Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
        return;
      }
      if (passWord_input.isEmpty()) {
        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
        return;
      }
      tryToLogin(userName_input, passWord_input);
    });
  }

  /**
   * 进行登录请求的数据库查询
   *
   * @param username 用户名
   * @param password 密码
   */
  private void tryToLogin(String username, String password) {
    LoginRequest request = new LoginRequest();
    request.username = username;
    request.password = password;
    new OkHttpManager(LoginActivity.this, HttpUtil.LOGIN, request, LoginResponse.class, true, new OkHttpManager.ResponseCallback() {
      public void onError(int errorType, int errorCode, String errorMsg) {
        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
      }

      public void onSuccess(Object response) {
        if (response != null && response instanceof LoginResponse) {
          UserInfoManager.getInstance().saveUserInfo(((LoginResponse) response).data);
          finish();
        } else {
          Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }
      }
    }).execute();
  }
}
