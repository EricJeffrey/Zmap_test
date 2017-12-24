package com.example.fei.zmap_test;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fei.zmap_test.common.MessageCenterMyAdapter;
import com.example.fei.zmap_test.http.MyWebCrawler;

import java.util.ArrayList;

public class MessageCenterActivity extends AppCompatActivity {
    public static final int ERROR_ON_GET_TITLE = -1;
    public static final int GET_TITLE_START = 1;
    public static final int GETTING_TITLE = 2;
    public static final int GET_TITLE_DOWN = 3;
    TextView errorText;
    ListView listView;
    MessageCenterMyAdapter adapter;
    ProgressBar progressBar;
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
        listView = findViewById(R.id.MessageCenter_list_view);
        ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> hrefs = new ArrayList<>();
        adapter = new MessageCenterMyAdapter(MessageCenterActivity.this, R.layout.messages_item_layout, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MessageCenterActivity.this, ArticleActivity.class);
                intent.putExtra("href", hrefs.get(position));
                startActivity(intent);
            }
        });
        MyWebCrawler.getInstance(handler).getTitleAndHref(list, hrefs);
    }

    /**
     * 成功获取今日新闻的标题及链接
     */
    public void onGetTitleDown(){
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取今日新闻标题链接出错
     */
    public void onGetTitleError(){
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
    }

    /**
     * 开始获取今日新闻标题及链接
     */
    public void onGetTitleStart(){
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
    }
}
