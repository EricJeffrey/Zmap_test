package com.example.fei.zmap_test.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.fei.zmap_test.R;
import com.example.fei.zmap_test.common.MyApplication;
import com.example.fei.zmap_test.db.Users;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by do_pc on 2017/12/23.
 * 单体类，处理网络访问与数据库
 */

public class HttpRequest {
    private static final String TAG = "HttpRequest";
    private String url = MyApplication.getContext().getResources().getString(R.string.URl);
    private static final HttpRequest ourInstance = new HttpRequest();
    private HttpCallback callback;
    private static final int LOGIN =2001;  //xxxx=abcd    a:预留   b:0=账户类 1=历史记录类 2版本类  cd:远程交互标识号
    private static final int REGISTER =2002;
    private static final int GETHISTORY =2103;
    private static final int SETHISTORY =2104;
    private static final int CLEARFISTORY =2105;
    private static final int CHANGEHEADICON =2006;
    private static final int VERSIONCODE =2207;
    private static final int ERROR_NETWORK =1000;

    private Users resp_user;


    public static HttpRequest getOurInstance() {

        return ourInstance;
    }

    private HttpRequest(){

    }

    public HttpCallback getCallback() {
        return callback;
    }

    /**
     * 登陆
     * @param username  操作用户用户名
     * @param password  操作用户密码
     * @param cBack     实现回调接口的上下文
     */
    public void login(final String username , final String password, HttpCallback cBack) {
        try {
            if(username != null && password !=null){
                if(cBack !=null){
                    this.callback=cBack;
                }
                resp_user = new Users();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient httpClient_login = new DefaultHttpClient();  //创建HttpClient对象
                        HttpGet httpGet_login = new HttpGet(url+"/?action=login&username="+username+"&password="+password);
                        try {
                            HttpResponse httpResponse = httpClient_login.execute(httpGet_login);//第三步：执行请求，获取服务器发还的相应对象
                            if((httpResponse.getEntity())!=null){
                                HttpEntity entity =httpResponse.getEntity();
                                String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                                Message message = new Message();//在子线程中将Message对象发出去
                                message.what =LOGIN;
                                message.obj =response;
                                handler.sendMessage(message);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        for(int i=0;i<3 && resp_user.getUsername()==null;i++){
                            Log.e("HTTPR", "等待Username数据写入数据库:"+resp_user.getUsername());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(resp_user.getUsername()==null){
                            Message message = new Message();//在子线程中将Message对象发出去
                            message.what =ERROR_NETWORK;
                            message.obj =ERROR_NETWORK;
                            handler.sendMessage(message);
                            return;
                        }
                        //未登陆成功不再进行读取历史记录
                        if(resp_user.getStatusCode()==1){
                            HttpClient httpClient_getHistory = new DefaultHttpClient();  //创建HttpClient对象
                            HttpGet httpGet_getHistory = new HttpGet(url+"/history.php?action=getSearchHistory&id="+resp_user.getUser_id()
                                    +"&username="+resp_user.getUsername());
                            Log.e("HTTPR", "URL:"+httpGet_getHistory.getURI().toString());
                            try {
                                HttpResponse httpResponse = httpClient_getHistory.execute(httpGet_getHistory);//第三步：执行请求，获取服务器发还的相应对象
                                if((httpResponse.getEntity())!=null){
                                    HttpEntity entity =httpResponse.getEntity();
                                    String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                                    Log.e("HTTPR", "re:"+response);
                                    Message message = new Message();//在子线程中将Message对象发出去
                                    message.what =GETHISTORY;
                                    message.obj =response;
                                    handler.sendMessage(message);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.e(TAG, "run: " + e);
                            }
                        }
                    }
                }).start();
            }else {
                throw new Exception("context == null OR username&password == null");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 注册
     * @param username  操作用户用户名
     * @param password  操作用户密码
     * @param cBack     实现回调接口的上下文
     */
    public void register(final String username , final String password, HttpCallback cBack) {
        Log.e("HTTPR", "++++"+username+"------"+password);
        try {
            if(username != null && password !=null){
                if(cBack !=null){
                    this.callback=cBack;
                }
                resp_user = new Users();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient httpClient = new DefaultHttpClient();  //创建HttpClient对象
                        HttpGet httpGet = new HttpGet(url+"/?action=register&username="+username+"&password="+password);
                        try {
                            HttpResponse httpResponse = httpClient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                            if((httpResponse.getEntity())!=null){
                                HttpEntity entity =httpResponse.getEntity();
                                String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                                Message message = new Message();//在子线程中将Message对象发出去
                                message.what = REGISTER;
                                message.obj =response;
                                handler.sendMessage(message);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else {
                throw new Exception("context == null OR username&password == null");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将搜索内容发送远端存储
     * @param text：搜索内容
     */
    public void setHistory(final int User_id,final String Username ,final String text, HttpCallback cBack){
        this.callback = cBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/history.php?action=setHistory" +
                        "&id="+User_id
                        +"&username="+Username
                        +"&searchHistory="+text);
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        //TODO ido 处理增加搜索记录的返回结果，规范化验证逻辑
                        Message message = new Message();//在子线程中将Message对象发出去
                        message.what = SETHISTORY;
                        message.obj =response;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 清除远程历史记录
     * @param User_id   操作用户ID
     * @param Username  操作用户用户名
     * @param cBack     实现回调接口上下文
     */
    public void clearHistory(final int User_id,final String Username ,HttpCallback cBack) {
        this.callback = cBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url + "/history.php?action=clearSearchHistory&id=" +User_id
                        + "&username=" + Username);
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if ((httpResponse.getEntity()) != null) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                        //TODO 处理返回值
                        Message message = new Message();//在子线程中将Message对象发出去
                        message.what = CLEARFISTORY;
                        message.obj =response;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 发送远程更换头像数据
     * @param Username  操作用户的用户名
     * @param id_head   操作用户的目的头像ID
     * @param cBack     实现回调接口的上下文
     */
    public void changeHeadIcon(final String Username,final int id_head,HttpCallback cBack){
        this.callback = cBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/?action=modifyheadid&username="+Username+"&id_head="+id_head);
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        //TODO 数据根据返回值设置需要才更加严谨
                        Message message = new Message();//在子线程中将Message对象发出去
                        message.what = CHANGEHEADICON;
                        message.obj =response;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获得更新参数
     * @param version 当前版本
     * @param cBack 实现回调接口的上下文
     */
    public void getUpdateCode(final int version, final HttpCallback cBack){
        this.callback = cBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/version.php?version="+version);
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        Message message = new Message();//在子线程中将Message对象发出去
                        message.what = VERSIONCODE;
                        message.obj =response;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "run: exception is " + e);
                    Message message = new Message();
                    message.what = VERSIONCODE;
                    message.obj ="-1";
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String Response=msg.obj.toString();
            if(!TextUtils.isEmpty(Response)) {
                resp_user = new Users();
                switch (msg.what) {
                    case LOGIN:
                        try {
                            JSONObject userObject = new JSONObject(Response);
                            resp_user.setUser_id(userObject.getInt("id"));
                            resp_user.setUsername(userObject.getString("username"));
                            resp_user.setId_head(userObject.getInt("id_head"));
                            resp_user.setStatusCode(userObject.getInt("statusCode"));
                            if(resp_user.getStatusCode()==1){
                                resp_user.save();   //登陆不成功返回  'statusCode'=>0,'id'=>0,'username'=>$username,'id_head'=>0  username为不成功账户名
                            }
                            callback.onFinish(resp_user.getUser_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case GETHISTORY:
                        try {
                            resp_user.setSearchHistory(Response);
                            Log.e("HTTPR", "+++++" + Response);
                            resp_user.updateAll("User_id > ?", "0");
                            //TODO 处理返回值
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case REGISTER:
                        try {
                            JSONObject userObject = new JSONObject(Response);
                            resp_user.setUser_id(userObject.getInt("id"));
                            resp_user.setUsername(userObject.getString("username"));
                            resp_user.setId_head(userObject.getInt("id_head"));
                            resp_user.setStatusCode(userObject.getInt("statusCode"));
                            resp_user.setSearchHistory(new Gson().toJson(new ArrayList<String>()));
                            if (resp_user.getUser_id() != 0) {
                                resp_user.save();
                                callback.onFinish(1);
                            } else {
                                callback.onFinish(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SETHISTORY:
                        break;
                    case CLEARFISTORY:
                        break;
                    case CHANGEHEADICON:
                        break;
                    case VERSIONCODE:
                        callback.onFinish(Integer.parseInt(Response));
                        break;
                    case ERROR_NETWORK:
                        callback.onFinish(Integer.parseInt(Response));
                    default:
                        break;
                }
            }
            return true;
        }
    });
}
