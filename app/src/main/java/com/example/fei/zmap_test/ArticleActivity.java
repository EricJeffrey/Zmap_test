package com.example.fei.zmap_test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fei.zmap_test.common.ArticleParagraphHolder;
import com.example.fei.zmap_test.customlayout.TitleLayout;
import com.example.fei.zmap_test.http.MyWebCrawler;

import java.util.ArrayList;

/**
 * 消息中心每个新闻的界面
 * 使用ScrollView+TextView+ImageView排版
 */
public class ArticleActivity extends AppCompatActivity {
    public static final int GET_ARTICLE_DOWN = 3;
    public static final int ERROR_ON_GET_ARTICLE = -1;
    public static final int GETTING_ARTICLE = 2;
    public static final int GET_ARTICLE_START = 1;
    public static final int ON_GETTING_IMG = 4;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case GET_ARTICLE_START:
                    onGetArticleStart();
                    break;
                case GETTING_ARTICLE:
                    break;
                case GET_ARTICLE_DOWN:
                    onGetArticleDown();
                    break;
                case ERROR_ON_GET_ARTICLE:
                    onGetArticleError();
                    break;
                case ON_GETTING_IMG:
                    onGettingImg();
                    break;
            }
            return true;
        }
    });
    private LinearLayout articleViewHolder;
    private TextView errorText;
    private ProgressBar progressBar;
    private ArrayList<ArticleParagraphHolder> articleParagraphHolders;
    private ArrayList<View> imgViews;
    private ArrayList<Bitmap> bitmaps;
    private int currentUpdateIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();

        Intent intent = getIntent();
        String href = intent.getStringExtra("href");
        String title = intent.getStringExtra("title");
        TitleLayout titleLayout = findViewById(R.id.article_detail_title);
        titleLayout.setTitle_text(title);

        articleViewHolder = findViewById(R.id.article_holder_view);
        errorText = findViewById(R.id.article_error_text);
        progressBar = findViewById(R.id.article_progress_bar);
        imgViews = new ArrayList<>();
        bitmaps = new ArrayList<>();

        articleParagraphHolders = new ArrayList<>();
        MyWebCrawler.getInstance(handler).getTextDetail(href, articleParagraphHolders);
    }

    /**
     * 加载文章详细内容
     */
    public void updateArticle(){
        for(ArticleParagraphHolder holder: articleParagraphHolders){
            View view = LayoutInflater.from(ArticleActivity.this).inflate(R.layout.article_detail_views, articleViewHolder, false);
            if(holder.isText()){
                view.findViewById(R.id.article_detail_image_view).setVisibility(View.GONE);
                TextView textView = view.findViewById(R.id.article_detail_text_view);
                textView.setText(holder.getDetail());
            }
            else{
                ImageView imageView = view.findViewById(R.id.article_detail_image_view);
                imgViews.add(imageView);
            }
            articleViewHolder.addView(view);
        }
        MyWebCrawler.getInstance(handler).getImages(articleParagraphHolders, bitmaps);
    }

    /**
     * 成功获取文章内容
     */
    public void onGetArticleDown(){
        errorText.setVisibility(View.GONE);
        articleViewHolder.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        updateArticle();
    }

    /**
     * 获取文章详情出错
     */
    public void onGetArticleError(){
        errorText.setVisibility(View.VISIBLE);
        articleViewHolder.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 开始获取文章详情
     */
    public void onGetArticleStart(){
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        articleViewHolder.setVisibility(View.GONE);
    }

    /**
     * 开始加载图片信息
     */
    public void onGettingImg(){
        int sz = currentUpdateIndex++;
        ImageView view = (ImageView)imgViews.get(sz);
        view.setImageBitmap(bitmaps.get(sz));
    }
}
