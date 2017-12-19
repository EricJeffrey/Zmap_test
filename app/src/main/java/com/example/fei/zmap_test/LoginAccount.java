package com.example.fei.zmap_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fei.zmap_test.db.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoginAccount extends AppCompatActivity {
    private String url;
    private EditText username;
    private EditText password;//用户名和密码
    private String username_text;
    private String password_text;
    public static final int SHOW_RESPONSE = 0;
    public Users resp_user;
    private static final String TAG = "LoginAccount";

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String Response=msg.obj.toString();
                    if(!TextUtils.isEmpty(Response)){
                        resp_user=new Users();
                        try {
                            JSONObject userObject = new JSONObject(Response);
                            resp_user.setUser_id(userObject.getInt("id"));
                            resp_user.setUsername(userObject.getString("username"));
                            resp_user.setId_head(userObject.getInt("id_head"));
                            resp_user.setStatusCode(userObject.getInt("statusCode"));
                            if(resp_user.getUser_id() !=0){
                                Toast.makeText(LoginAccount.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                resp_user.save();
                                sendRequestWithHttpClient_history();
                                finish();

                            } else {
                                Toast.makeText(LoginAccount.this,"账号密码错误",Toast.LENGTH_SHORT).show();
                                password.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account_layout);
        getSupportActionBar().hide();
        url=getString(R.string.URl); //服务器接口地址

        username=(EditText)findViewById(R.id.login_account_username);
        password=(EditText)findViewById(R.id.login_account_password);

        addListener(R.id.go_login_phone_text);
        addListener(R.id.find_password);                    //按钮目前只有返回功能
        addListener(R.id.login_account_button);
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.go_login_phone_text:
                        Intent intent = new Intent(LoginAccount.this, LoginPhone.class);
                        startActivity(intent);
                        finish();break;
                    case R.id.find_password:
                        Toast.makeText(LoginAccount.this, "别担心", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.login_account_button:
                        sendRequestWithHttpClient();
                        break;
                }
            }
        });
    }

    private void sendRequestWithHttpClient(){
        username_text = username.getText().toString().trim();
        password_text = password.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/?action=login&username="+username_text+"&password="+password_text);
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        Message message = new Message();//在子线程中将Message对象发出去
                        message.what = SHOW_RESPONSE;
                        message.obj =response;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //用户登陆之后获取数据
    private void sendRequestWithHttpClient_history(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/history.php?action=getSearchHistory&id="+resp_user.getUser_id()
                        +"&username="+resp_user.getUsername());
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        Gson gson =new Gson();
                        Type type = new TypeToken<ArrayList<String>>() {}.getType();
                        ArrayList<String> sList=gson.fromJson(response, type);
                        resp_user.setSearchHistory(sList);
                        resp_user.updateAll();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
