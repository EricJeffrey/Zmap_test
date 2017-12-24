package com.example.fei.zmap_test.common;

/**
 * Created by FEI on 2017/12/24.
 * 保存一个文章的文字和图片
 * 此处未考虑视频，故可能出bug。。。
 */

public class ArticleParagraphHolder {
    private String detail;
    private boolean itemIsText;
    public ArticleParagraphHolder(String detail, boolean itemIsText){
        this.detail = detail;
        this.itemIsText = itemIsText;
    }
    public String getDetail() {
        return detail;
    }
    public boolean isText() {
        return itemIsText;
    }
}
