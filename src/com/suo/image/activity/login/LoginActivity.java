package com.suo.image.activity.login;

import java.util.List;

import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.suo.demo.R;
import com.suo.image.ImageApp;
import com.suo.image.activity.BaseActivity;
import com.suo.image.activity.Main_new;
import com.suo.image.bean.UserInfo;
import com.suo.image.share.QQUtil;
import com.suo.image.util.Log;
import com.suo.image.util.Preference;
import com.tencent.tauth.Tencent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LoginActivity extends BaseActivity {

    private LinearLayout ll_layout;
    private LinearLayout ll_edit_layout;
    private Button btn_login;
    private Button btn_qq;
    private Button btn_sina;
    private Animation moveUp;
    private EditText et_login_username;
    private EditText et_login_password;
    private int i = 0;
    public Tencent mTencent;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        prepareData();
        initLayout();

    }

    private void prepareData() {
        mTencent = Tencent.createInstance(QQUtil.appId, this);
    }

    private void initLayout() {
        ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        ll_edit_layout = (LinearLayout) findViewById(R.id.ll_edit_layout);
        et_login_username = (EditText) findViewById(R.id.et_login_username);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_qq = (Button) findViewById(R.id.btn_qq);
        btn_sina = (Button) findViewById(R.id.btn_sina);

        ll_layout.setVisibility(View.VISIBLE);
        ViewTreeObserver vto1 = ll_layout.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = ll_layout.getHeight();
                moveUp = new TranslateAnimation(0.0f, 0.0f, height, 0.0f);
                moveUp.setDuration(1500);
                ll_layout.startAnimation(moveUp);//
            }
        });

        btn_login.setOnClickListener(this);
        btn_qq.setOnClickListener(this);
        btn_sina.setOnClickListener(this);
        
        String name = Preference.getString("username");
        if (!TextUtils.isEmpty(name)){
            et_login_username.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                if (i == 0) {
                    ll_edit_layout.setVisibility(View.VISIBLE);
                    i = 1;
                } else {
                    username = et_login_username.getText().toString().trim();
                    password = et_login_password.getText().toString().trim();
                    if(username.equals("")){
                        showToast("请输入您的用户名");
                        return;
                    }
                    
                    if(password.equals("")){
                        showToast("请输入您的密码");
                        return;
                    }
                    BmobUser user = new BmobUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.login(this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Preference.putString("username", username);
                            Preference.putString("password", password);
                            finish();
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
                        }
                        
                        @Override
                        public void onFailure(int arg0, String arg1) {
                            showToast("登陆失败："+arg1);
                        }
                    });
                }

                break;
            case R.id.btn_qq:
                BmobUser.qqLogin(this, QQUtil.appId, new OtherLoginListener() {
                    
                    @Override
                    public void onSuccess(final JSONObject userAuth) {
                        if(userAuth != null){
                            Log.i("login", "QQ登陆成功返回:"+userAuth.toString());
                            BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
                            query.addWhereEqualTo("userId", BmobUser.getCurrentUser(LoginActivity.this).getObjectId());
                            query.findObjects(LoginActivity.this, new FindListener<UserInfo>() {
                                    @Override
                                    public void onSuccess(List<UserInfo> object) {
                                        if (object!=null && object.size()>0){
                                            showToast("登录成功");
                                            ImageApp.getInstance().setUserinfo(object.get(0));
                                            launch(Main_new.class);
                                            finish();
                                        }else{
                                            Intent intent = new Intent(LoginActivity.this, LoginBindActivity.class);
                                            intent.putExtra("userAuth", userAuth.toString());
                                            launch(intent);
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onError(int code, String msg) {
                                        
                                    }
                            });
                        }
                        
//                        associateUser(userAuth);
                    }
                    
                    @Override
                    public void onFailure(int code, String msg) {
                        showToast("第三方登陆失败："+msg);
                    }

                    @Override
                    public void onCancel() {
                        
                    }
                });
                break;
            case R.id.btn_sina:
                showToast("暂不支持微博登录噢！");
                break;

            default:
                break;
        }
    }

    
}
