package com.example.fei.zmap_test.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by do_pc on 2017/12/10.
 */

public class Users extends DataSupport {
    private int statusCode;

    private int User_id = 0;
    private String username;
    private int id_head;
    private ArrayList<String> searchHistory = new ArrayList<String>();

    public ArrayList<String> getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(ArrayList<String> searchHistory) {
        this.searchHistory = searchHistory;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
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


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
