package com.example.fei.zmap_test.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.fei.zmap_test.ArticleActivity;
import com.example.fei.zmap_test.MessageCenterActivity;
import com.example.fei.zmap_test.common.ArticleParagraphHolder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by FEI on 2017/12/24.
 * 爬虫程序，爬去IT之家，多线程处理
 */

public class MyWebCrawler {
    private static final String TAG = "MyWebCrawler";
    private Handler handler;
    private static MyWebCrawler webCrawler;

    /**
     * 获取单体
     * @param handler：处理消息的对象
     */
    public static MyWebCrawler getInstance(Handler handler){
        if(webCrawler != null) {
            webCrawler.handler = handler;
            return webCrawler;
        }
        else return new MyWebCrawler(handler);
    }
    private MyWebCrawler(Handler handler){
        this.handler = handler;
    }
    /**
     * 获取IT之家所有今日文章
     * titles = 文章标题
     * hrefs = 对应文章链接
     */
    public void getTitleAndHref(final ArrayList<String> titles, final ArrayList<String> hrefs){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc;
                Message msg = new Message();
                msg.what = MessageCenterActivity.GET_TITLE_START;
                handler.sendMessage(msg);
                try {
                    String URL = "https://www.ithome.com";
                    doc = Jsoup.connect(URL)
                            .userAgent("Mozilla")
                            .timeout(5000)
                            .header("Accept-Language", "zh-CN")
                            .cookie("auth", "token")
                            .get();
                    Elements elements = doc.select("li");
                    for(Element element:elements) {
                        if(element.select("span[class^=date]").text().contains("今日")) {
                            if(!element.select("a").attr("href").contains("lapin")){
                                Elements tmp = element.select("a");
                                titles.add(tmp.text());
                                hrefs.add(tmp.attr("href"));
                            }
                        }
                    }
                    msg = new Message();
                    msg.what = MessageCenterActivity.GET_TITLE_DOWN;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    msg = new Message();
                    msg.what = MessageCenterActivity.ERROR_ON_GET_TITLE;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
    /**
     * 获取每个文章的详细内容
     * @param href：文章对应链接
     */
    public void getTextDetail(final String href, final ArrayList<ArticleParagraphHolder> resultList){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc;
                Message msg = new Message();
                msg.what = ArticleActivity.GET_ARTICLE_START;
                handler.sendMessage(msg);
                try {
                    doc = Jsoup.connect(href)
                            .userAgent("Mozilla")
                            .timeout(5000)
                            .header("Accept-Language", "zh-CN")
                            .cookie("auth", "token")
                            .get();
                    Elements elements = doc.select("div[id^=paragraph]").select("p");
                    for(Element tmp: elements){
                        ArticleParagraphHolder holder;
                        if(tmp.hasText())
                            holder = new ArticleParagraphHolder("        " + tmp.text(), true);
                        else
                            holder = new ArticleParagraphHolder(tmp.select("img").attr("data-original"), false);
                        resultList.add(holder);
                    }
                    msg = new Message();
                    msg.what = ArticleActivity.GET_ARTICLE_DOWN;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    msg = new Message();
                    msg.what = ArticleActivity.ERROR_ON_GET_ARTICLE;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * 根据图片URL获取图片
     * @param holders：保存图片URL的类
     * @param bitmaps：保存获取到的图片到ArrayList中
     */
    public void getImages(final ArrayList<ArticleParagraphHolder> holders, final ArrayList<Bitmap> bitmaps){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg;
                try {
                    for (ArticleParagraphHolder holder : holders) {
                        if (holder.isText()) continue;
                        String url = holder.getDetail();
                        HttpURLConnection connection;
                        connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        connection.setDoInput(true);
                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                            bitmaps.add(bitmap);
                            msg = new Message();
                            msg.what = ArticleActivity.ON_GETTING_IMG;
                            handler.sendMessage(msg);
                        }
                        connection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
