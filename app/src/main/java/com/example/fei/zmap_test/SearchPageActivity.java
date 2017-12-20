package com.example.fei.zmap_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchPageActivity extends AppCompatActivity {
    private static final String TAG = "SearchPageActivity";
    public Users current_user =null;
    private String url;
    private Gson gson;
    private ArrayList <String> historyList;
    private LinearLayout searchHistoryHolder;
    private Button searchButton;
    private ImageButton cancelInputButton;
    private ImageButton voiceButton;
    private EditText editText;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(TextUtils.isEmpty(s)){
                searchButton.setVisibility(View.VISIBLE);
                voiceButton.setVisibility(View.GONE);
                cancelInputButton.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            if(TextUtils.isEmpty(s)){
                searchButton.setVisibility(View.GONE);
                voiceButton.setVisibility(View.VISIBLE);
                cancelInputButton.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page_layout);
        getSupportActionBar().hide();
        url=getString(R.string.URl); //服务器接口地址


        gson = new Gson();
        searchButton = (Button) findViewById(R.id.SearchPageActivity_search_button);
        cancelInputButton = (ImageButton) findViewById(R.id.SearchPageActivity_cancel_input_button);
        voiceButton = (ImageButton) findViewById(R.id.SearchPageActivity_voice_search);
        editText = (EditText) findViewById(R.id.SearchPageActivity_search_edit_text);
 //       searchHistorySharedPreference = this.getSharedPreferences("searchHistoryData", MODE_PRIVATE);
        searchHistoryHolder = (LinearLayout) findViewById(R.id.SearchPageActivity_search_history_item_holder);

        //此处不需要使用此方法，由onResume()避免出现重复渲染
        //getAndShowSearchHistoryRecord();

        addListener(R.id.SearchPageActivity_cancel_input_button);
        addListener(R.id.SearchPageActivity_search_back_arrow);
        addListener(R.id.SearchPageActivity_search_button);
        addListener(R.id.SearchPageActivity_voice_search);
        addListener(R.id.SearchPageActivity_clear_all_history);
        editText.addTextChangedListener(textWatcher);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    goSearch();
                    return true;
                }
                return false;
            }
        });
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.SearchPageActivity_search_back_arrow:
                        finish();
                        break;
                    case R.id.SearchPageActivity_cancel_input_button:
                        editText.setText("");
                        break;
                    case R.id.SearchPageActivity_search_button:
                        goSearch();
                        break;
                    case R.id.SearchPageActivity_voice_search:
                        Toast.makeText(SearchPageActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SearchPageActivity_clear_all_history:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SearchPageActivity.this);
                        dialog.setCancelable(true);
                        dialog.setTitle("清空历史记录？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearHistory();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                }
            }
        });
    }

    //搜索内容，打开搜索结果页面，并将该记录存入数据库中
    public void goSearch(){
        String text = editText.getText().toString();
        if(TextUtils.isEmpty(text)) return;
        putSearchRecordToDatabase(text);
        Intent intent = new Intent(SearchPageActivity.this, SearchResultPage.class);
        startActivity(intent);
    }

    //将记录保存到本地以及远程数据库
    public void putSearchRecordToDatabase(String text){
        if(TextUtils.isEmpty(text)) return;
        current_user= DataSupport.findLast(Users.class);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        historyList=gson.fromJson(current_user.getSearchHistory(), type);
        historyList.add(text);
        current_user.setSearchHistory(gson.toJson(historyList));

        current_user.updateAll("User_id = ?",""+current_user.getUser_id());
        putSearchRecordToOrigin(text); //上传到远程数据库
    }

    //发送远端存储
    public void putSearchRecordToOrigin(final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/history.php?action=setHistory" +
                        "&id="+current_user.getUser_id()
                        +"&username="+current_user.getUsername()
                        +"&searchHistory="+text);
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        //TODO ido 处理增加搜索记录的返回结果，规范化验证逻辑
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //获取历史记录
    public void getAndShowSearchHistoryRecord(){

        current_user= DataSupport.findLast(Users.class);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        historyList=gson.fromJson(current_user.getSearchHistory(), type);  //解析获得的字符串为json

        searchHistoryHolder.removeAllViews();
        int listSize =historyList.size();
        TextView tmp = (TextView) findViewById(R.id.SearchPageActivity_clear_all_history);
        if(listSize > 0) tmp.setVisibility(View.VISIBLE);
        else tmp.setVisibility(View.GONE);
        for(String text : historyList){
            SearchHistoryItemLayout item = new SearchHistoryItemLayout(this, null, text);
            searchHistoryHolder.addView(item);
        }
    }

    //删除所有记录
    public void clearHistory(){
        historyList.clear();
        current_user.setSearchHistory(gson.toJson(historyList));
        current_user.updateAll("User_id = ?",""+current_user.getUser_id());
        sendRequestWithHttpClient_clearHistory();
        getAndShowSearchHistoryRecord();
    }

    public void finish(){
        super.finish();
        overridePendingTransition(Animation.ABSOLUTE, Animation.ABSOLUTE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onResume(){
        super.onResume();
        getAndShowSearchHistoryRecord();
        editText.setText("");
    }

    //清除远端搜索数据
    private void sendRequestWithHttpClient_clearHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url + "/history.php?action=clearSearchHistory&id=" + current_user.getUser_id()
                        + "&username=" + current_user.getUsername());
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if ((httpResponse.getEntity()) != null) {
                        HttpEntity entity = httpResponse.getEntity();
                        //TODO 处理返回值
                        String response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
