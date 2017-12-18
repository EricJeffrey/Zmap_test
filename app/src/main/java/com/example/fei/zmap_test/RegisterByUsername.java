package com.example.fei.zmap_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RegisterByUsername extends AppCompatActivity {

    private String url;
    private EditText username;
    private EditText password;//用户名和密码
    public static final int SHOW_RESPONSE = 0;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String Response=msg.obj.toString();
                    Toast.makeText(RegisterByUsername.this,Response,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return true;
        }
    }) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_by_username_layout);
        getSupportActionBar().hide();
        url=getString(R.string.URl); //服务器接口地址


        username=(EditText)findViewById(R.id.register_account_username);
        password=(EditText)findViewById(R.id.register_account_password);

        addListener(R.id.go_register_phone_text);
        addListener(R.id.register_account_button);
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.go_register_phone_text:
                        Intent intent = new Intent(RegisterByUsername.this, Register.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.register_account_button:
                        sendRequestWithHttpClient();
                        finish();
                        break;
                }
            }
        });
    }
    private void sendRequestWithHttpClient(){
        final String username_text = username.getText().toString().trim();
        final String password_text = password.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/?action=register&username="+username_text+"&password="+password_text);
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
}
