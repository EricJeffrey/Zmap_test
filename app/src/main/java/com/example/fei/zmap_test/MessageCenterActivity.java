package com.example.fei.zmap_test;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fei.zmap_test.customlayout.MessageCenterAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MessageCenterActivity extends AppCompatActivity {
    private TextView errorText;
    private ListView listView;
    private MessageCenterAdapter adapter;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();

        errorText = findViewById(R.id.MessageCenter_error_text);
        listView = findViewById(R.id.MessageCenter_list_view);
        progressBar = findViewById(R.id.MessageCenter_progress_bar);
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> href = new ArrayList<>();
        adapter = new MessageCenterAdapter(MessageCenterActivity.this, R.layout.messages_item_layout, list);
        listView.setAdapter(adapter);
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        //noinspection unchecked
        myAsyncTask.execute(list, href);
    }
    @SuppressLint("StaticFieldLeak")
    class MyAsyncTask extends AsyncTask< ArrayList<String>, Integer, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
            if(!aBoolean) {
                errorText.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                return;
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Boolean doInBackground(ArrayList<String>[] arrayLists) {
            ArrayList<String> titleResults = arrayLists[0];
            ArrayList<String> hrefResults = arrayLists[1];
            Document doc;
            try {
                String url = "https://www.ithome.com";
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla")
                        .timeout(5000)
                        .header("Accept-Language", "zh-CN")
                        .cookie("auth", "token")
                        .get();
                Elements elements =Jsoup.parse(doc.toString()).select("li");
                for(Element element:elements) {
                    if(element.text().contains("今日")){
                        titleResults.add(element.select("a").text());
                        hrefResults.add(element.select("a").attr("href"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
