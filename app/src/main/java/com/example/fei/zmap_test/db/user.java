package com.example.fei.zmap_test.db;

/**
 * Created by do_pc on 2017/12/10.
 */

public class user {

    private int id = 0;
    private String username;
    private int id_head;
    private String searchHistory;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_head() {
        return id_head;
    }

    public void setId_head(int id_head) {
        this.id_head = id_head;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(String searchHistory) {
        this.searchHistory = searchHistory;
    }
}


