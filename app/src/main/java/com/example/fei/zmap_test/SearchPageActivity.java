package com.example.fei.zmap_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class SearchPageActivity extends AppCompatActivity {
    private static final String TAG = "SearchPageActivity";
    private SharedPreferences searchHistorySharedPreference;
    private Map map;
    private Integer mapSize;
    private LinearLayout searchHistoryHolder;
    private Button searchButton;
    private ImageButton cancelInputButton;
    private ImageButton voiceButton;
    private EditText editText;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(TextUtils.isEmpty(s)){
                searchButton.setVisibility(View.VISIBLE);
                voiceButton.setVisibility(View.GONE);
                cancelInputButton.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            if(TextUtils.isEmpty(s)){
                searchButton.setVisibility(View.GONE);
                voiceButton.setVisibility(View.VISIBLE);
                cancelInputButton.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page_layout);
        getSupportActionBar().hide();

        searchButton = (Button) findViewById(R.id.SearchPageActivity_search_button);
        cancelInputButton = (ImageButton) findViewById(R.id.SearchPageActivity_cancel_input_button);
        voiceButton = (ImageButton) findViewById(R.id.SearchPageActivity_voice_search);
        editText = (EditText) findViewById(R.id.SearchPageActivity_search_edit_text);
        searchHistorySharedPreference = this.getSharedPreferences("searchHistoryData", MODE_PRIVATE);
        searchHistoryHolder = (LinearLayout) findViewById(R.id.SearchPageActivity_search_history_item_holder);

        getAndShowSearchHistoryRecord();

        addListener(R.id.SearchPageActivity_cancel_input_button);
        addListener(R.id.SearchPageActivity_search_back_arrow);
        addListener(R.id.SearchPageActivity_search_button);
        addListener(R.id.SearchPageActivity_voice_search);
        addListener(R.id.SearchPageActivity_clear_all_history);
        editText.addTextChangedListener(textWatcher);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    goSearch();
                    return true;
                }
                return false;
            }
        });
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.SearchPageActivity_search_back_arrow:
                        finish();
                        break;
                    case R.id.SearchPageActivity_cancel_input_button:
                        editText.setText("");
                        break;
                    case R.id.SearchPageActivity_search_button:
                        goSearch();
                        break;
                    case R.id.SearchPageActivity_voice_search:
                        Toast.makeText(SearchPageActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SearchPageActivity_clear_all_history:
                        clearHistory();
                        break;
                }
            }
        });
    }
    //获取历史记录
    public void getAndShowSearchHistoryRecord(){
        searchHistoryHolder.removeAllViews();
        map = searchHistorySharedPreference.getAll();
        mapSize = map.size();

        TextView tmp = (TextView) findViewById(R.id.SearchPageActivity_clear_all_history);
        if(mapSize > 0) tmp.setVisibility(View.VISIBLE);
        else tmp.setVisibility(View.GONE);

        for(Object temp : map.entrySet()){
            Map.Entry entry = (Map.Entry<String, ?>) temp;
            if(entry.getKey().equals("mapSize")) continue;
            String text = (String) entry.getValue();
            SearchHistoryItemLayout item = new SearchHistoryItemLayout(this, null, text);
            searchHistoryHolder.addView(item);
        }
    }
    //将此条记录存放入SharedPreference中，重复的不再放入，不记录时间戳（即优先级）
    public void putSearchRecordToSharedPreference(String text){
        if(TextUtils.isEmpty(text)) return;
        if(map.containsValue(text)) return;
        SharedPreferences.Editor editor = searchHistorySharedPreference.edit();
        mapSize += 1;
        editor.putString(mapSize.toString(), text);
        editor.apply();
    }
    //搜索内容，打开搜索结果页面，并将该记录放入SharedPreference里面
    public void goSearch(){
        String text = editText.getText().toString();
        if(TextUtils.isEmpty(text)) return;
        putSearchRecordToSharedPreference(text);
        Intent intent = new Intent(SearchPageActivity.this, SearchResultPage.class);
        intent.putExtra("search_text", editText.getText().toString());
        startActivity(intent);
    }
    //清除ScrollView以及SharedPreference中的历史记录
    public void clearHistory(){
        mapSize = 0;
        map.clear();
        SharedPreferences.Editor editor = searchHistorySharedPreference.edit();
        editor.clear();
        editor.apply();
        getAndShowSearchHistoryRecord();
    }
    public void finish(){
        super.finish();
        overridePendingTransition(Animation.ABSOLUTE, Animation.ABSOLUTE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void onResume(){
        super.onResume();
        getAndShowSearchHistoryRecord();
        editText.setText("");
    }
}
