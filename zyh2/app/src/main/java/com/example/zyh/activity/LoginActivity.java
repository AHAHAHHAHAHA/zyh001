package com.example.zyh.activity;


import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.zyh.R;
import com.example.zyh.comm.BaseActivity;
import com.example.zyh.comm.Constants;
import com.example.zyh.http.ProgressDialogSubscriber;
import com.example.zyh.http.entity.MemberEntity;
import com.example.zyh.http.presenter.MemberPresenter;
import com.example.zyh.utils.SystemConfig;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity{
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.iv_back)
    void close() {
        finish();
    }

    @OnClick(R.id.bt_login)
    void login() {
        String userName = etUsername.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            toastShort("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            toastShort("请输入密码");
            return;
        }

        MemberPresenter.login(new ProgressDialogSubscriber<MemberEntity>(this) {
            @Override
            public void onNext(MemberEntity memberEntity) {
                //保存登录状态
                SystemConfig.setLogin(true);
                //弹出登录成功提示
                toastShort("登录成功");
                //保存登录账户的信息
                SystemConfig.setLoginUserName(memberEntity.uname);
                SystemConfig.setLoginUserEmail(memberEntity.email);
                SystemConfig.setLoginUserHead(memberEntity.image);

                sendLoginBroadcast();

                //返回数据，只有调用了setResult，在调用的地方才会回调onActivityResult方法
                setResult(RESULT_OK);
                finish();
            }
        }, userName, pwd);
    }

    private void sendLoginBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_LOGIN);
        intent.putExtra("my_data","这是数据");
        sendBroadcast(intent);
    }
}
