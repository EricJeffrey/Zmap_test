package com.example.fei.zmap_test.common;

/**
 * Created by FEI on 2017/12/24.
 * 保存一个文章的文字和图片
 * 未考虑视频
 */

public class ArticleParagraphHolder {
    private String detail;
    private boolean itemIsText;
    public ArticleParagraphHolder(String detail, boolean itemIsText){
        this.detail = detail;
        this.itemIsText = itemIsText;
    }

    /**
     * 获取该项详情
     * @return 文字或者图片链接
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 判断是否是文字
     * @return 如果是文字，返回true；否则返回false
     */
    public boolean isText() {
        return itemIsText;
    }
}
