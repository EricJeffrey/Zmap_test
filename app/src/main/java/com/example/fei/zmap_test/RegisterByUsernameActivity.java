package com.example.fei.zmap_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fei.zmap_test.HTTP.HTTPCallback;
import com.example.fei.zmap_test.HTTP.HTTPRequest;
import com.example.fei.zmap_test.db.Users;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterByUsernameActivity extends AppCompatActivity implements HTTPCallback {
    public Users resp_user;
    private String url;
    private EditText username;
    private EditText password;//用户名和密码
    public static final int SHOW_RESPONSE = 0;
    private static final String TAG = "RegisterByUsernameActivity";


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
                            if(resp_user.getUser_id()!=0){
                                Toast.makeText(RegisterByUsernameActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                resp_user.save();
                                finish();
                            } else {
                                Toast.makeText(RegisterByUsernameActivity.this,"返回错误，请自行登陆",Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(RegisterByUsernameActivity.this,LoginAccountActivity.class);
                                startActivity(intent);

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
                        Intent intent = new Intent(RegisterByUsernameActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.register_account_button:
                        String username_text = username.getText().toString().trim();
                        String password_text = password.getText().toString().trim();
                        if( username_text.length()>0 && password_text.length()>0){
                            HTTPRequest.getOurInstance().register(RegisterByUsernameActivity.this,username_text,password_text,RegisterByUsernameActivity.this);
//                        sendRequestWithHttpClient();
                        }else {
                            Toast.makeText(RegisterByUsernameActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    /**
     * 回调处理返回数据
     *
     * @param status ：返回状态
     */
    @Override
    public void onFinish(int status) {
        if(status==1){
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, "注册失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }
//    private void sendRequestWithHttpClient(){
//        final String username_text = username.getText().toString().trim();
//        final String password_text = password.getText().toString().trim();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
//                HttpGet httpGet = new HttpGet(url+"/?action=register&username="+username_text+"&password="+password_text);
//                try {
//                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
//                    if((httpResponse.getEntity())!=null){
//                        HttpEntity entity =httpResponse.getEntity();
//                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
//
//                        Message message = new Message();//在子线程中将Message对象发出去
//                        message.what = SHOW_RESPONSE;
//                        message.obj =response;
//                        handler.sendMessage(message);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
}
