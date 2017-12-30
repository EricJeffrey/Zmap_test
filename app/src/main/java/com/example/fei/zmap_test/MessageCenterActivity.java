package com.example.fei.zmap_test;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fei.zmap_test.common.MessageCenterMyAdapter;
import com.example.fei.zmap_test.http.MyWebCrawler;

import java.util.ArrayList;

/**
 * 消息中心
 * 显示从网页www.ithome.com获得的新闻数据
 */
public class MessageCenterActivity extends AppCompatActivity {
    public static final int ERROR_ON_GET_TITLE = -1;
    public static final int GET_TITLE_START = 1;
    public static final int GETTING_TITLE = 2;
    public static final int GET_TITLE_DOWN = 3;
    private TextView errorText;
    private RecyclerView recyclerView;
    private MessageCenterMyAdapter adapter;
    private ProgressBar progressBar;
    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case GET_TITLE_START:
                    onGetTitleStart();
                    break;
                case GETTING_TITLE:
                    break;
                case GET_TITLE_DOWN:
                    onGetTitleDown();
                    break;
                case ERROR_ON_GET_TITLE:
                    onGetTitleError();
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();

        errorText = findViewById(R.id.MessageCenter_error_text);
        progressBar = findViewById(R.id.MessageCenter_progress_bar);
        recyclerView = findViewById(R.id.MessageCenter_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> href = new ArrayList<>();
        adapter = new MessageCenterMyAdapter(title, href);
        recyclerView.setAdapter(adapter);
        MyWebCrawler.getInstance(handler).getTitleAndHref(title, href);
    }

    /**
     * 成功获取今日新闻的标题及链接
     */
    public void onGetTitleDown(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取今日新闻标题链接出错
     */
    public void onGetTitleError(){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
    }

    /**
     * 开始获取今日新闻标题及链接
     */
    public void onGetTitleStart(){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
    }
}
