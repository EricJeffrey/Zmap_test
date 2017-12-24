package com.example.fei.zmap_test.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.fei.zmap_test.R;
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

public class HTTPRequest {
    private String url;
    private static final HTTPRequest ourInstance = new HTTPRequest();
    private HTTPCallback callback;
    private static final int LOGIN =2001;  //xxxx=abcd    a:预留   b:1=账户类 2=历史记录类  cd:远程交互标识号
    private static final int REGISTER =2002;
    private static final int GETHISTORY =2103;
    private static final int SETHISTORY =2104;
    private static final int CLEARFISTORY =2105;
    private static final int CHANGEHEADICON =2006;
    private Users resp_user;


    public static HTTPRequest getOurInstance() {

        return ourInstance;
    }

    private HTTPRequest(){

    }

    public HTTPCallback getCallback() {
        return callback;
    }

    /**
     * 登陆
     * @param context: 上下文
     *
     */
    public void login(Context context, final String username , final String password, HTTPCallback cBack) {
        url=context.getString(R.string.URl); //服务器接口地址
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
                        HttpClient httpCient_login = new DefaultHttpClient();  //创建HttpClient对象
                        HttpGet httpGet_login = new HttpGet(url+"/?action=login&username="+username+"&password="+password);
                        try {
                            HttpResponse httpResponse = httpCient_login.execute(httpGet_login);//第三步：执行请求，获取服务器发还的相应对象
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

                        while (resp_user.getStatusCode()==0)
                            Log.e("HTTPR", "等待Username数据写入数据库:"+resp_user.getUsername());
                        //未登陆成功不再进行读取历史记录
                        if(resp_user.getStatusCode()==1){
                            HttpClient httpCient_getHistory = new DefaultHttpClient();  //创建HttpClient对象
                            HttpGet httpGet_getHistory = new HttpGet(url+"/history.php?action=getSearchHistory&id="+resp_user.getUser_id()
                                    +"&username="+resp_user.getUsername());
                            Log.e("HTTPR", "URL:"+httpGet_getHistory.getURI().toString());
                            try {
                                HttpResponse httpResponse = httpCient_getHistory.execute(httpGet_getHistory);//第三步：执行请求，获取服务器发还的相应对象
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
     * 登陆
     * @param context: 上下文
     *
     */
    public void register(Context context, final String username , final String password, HTTPCallback cBack) {
        url=context.getString(R.string.URl); //服务器接口地址
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
                        HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                        HttpGet httpGet = new HttpGet(url+"/?action=register&username="+username+"&password="+password);
                        try {
                            HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
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
    public void setHistory(Context context,final int User_id,final String Username ,final String text, HTTPCallback cBack){
        url=context.getString(R.string.URl); //服务器接口地址
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/history.php?action=setHistory" +
                        "&id="+User_id
                        +"&username="+Username
                        +"&searchHistory="+text);
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
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
     * 清除远端搜索数据
     */
    public void clearHistory(Context context,final int User_id,final String Username ,HTTPCallback cBack) {
        url=context.getString(R.string.URl); //服务器接口地址
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url + "/history.php?action=clearSearchHistory&id=" +User_id
                        + "&username=" + Username);
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
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

    //发送远程更换头像数据
    public void changeHeadIcon(Context context,final String Username,final int id_head,HTTPCallback cBac){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/?action=modifyheadid&username="+Username+"&id_head="+id_head);
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
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
                    default:
                        break;
                }
            }
            return true;
        }
    });
}
