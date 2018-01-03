package com.example.fei.zmap_test.common;

/**
 * Created by FEI on 2018/1/3.
 * 城市列表右边的字母表中每一项
 */

public class CityLetterItem {
    private String letters;
    private Integer indexPos;

    public CityLetterItem(String letters, Integer indexPos){
        this.letters = letters;
        this.indexPos = indexPos;
    }

    public Integer getIndexPos() {
        return indexPos;
    }

    public String getLetters() {
        return letters;
    }
}
