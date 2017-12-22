package com.example.fei.zmap_test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.fei.zmap_test.commonJavaClass.SearchResultItem;
import com.example.fei.zmap_test.customLayout.SearchHistoryItemLayout;
import com.example.fei.zmap_test.db.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchPageActivity extends AppCompatActivity {
    private static final String TAG = "SearchPageActivity";
    public Users current_user =null;
    private String url;
    private Gson gson;
    private ArrayList <String> historyList;
    private LinearLayout searchHistoryHolder;
    private Button searchButton;
    private ImageButton cancelInputButton;
    private ImageButton voiceButton;
    private EditText editText;

    private List<SearchResultItem> itemList;
    private SearchResultListAdapter resultListAdapter;
    private ListView searchResultListView;
    private ScrollView typeButtonHistoryView;
    private TextView nothingTextView;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(TextUtils.isEmpty(s)){
                showSearchWidget();
            }
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            if(TextUtils.isEmpty(s)){
               hideSearchWidget();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        url=getString(R.string.URl); //服务器接口地址

        gson = new Gson();
        searchButton =  findViewById(R.id.SearchPageActivity_search_button);
        cancelInputButton =  findViewById(R.id.SearchPageActivity_cancel_input_button);
        voiceButton =  findViewById(R.id.SearchPageActivity_voice_search);
        editText =  findViewById(R.id.SearchPageActivity_search_edit_text);
        searchHistoryHolder =  findViewById(R.id.SearchPageActivity_search_history_item_holder);
        typeButtonHistoryView = findViewById(R.id.SearchPageActivity_type_button_search_history_layout);

        initSearchResultList();
        nothingTextView = findViewById(R.id.SearchPageActivity_nothing_searched);

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

    /**
     * 初始化listView并为其添加子项添加监听器
     */
    public void initSearchResultList(){
        searchResultListView = findViewById(R.id.SearchPageActivity_search_result_list);
        itemList = new ArrayList<>();
        resultListAdapter = new SearchResultListAdapter(SearchPageActivity.this, R.layout.search_history_result_item_layout, itemList);
        searchResultListView.setAdapter(resultListAdapter);
        searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResultListAdapter adapter = (SearchResultListAdapter)searchResultListView.getAdapter();
                Intent intent = new Intent();
                intent.putExtra("poiData", adapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 在输入框更改内容后变为空时调用
     * 使搜索按钮，搜索结果列表变为隐藏状态
     * 使搜索历史出现
     */
    public void hideSearchWidget(){
        searchButton.setVisibility(View.GONE);
        voiceButton.setVisibility(View.VISIBLE);
        cancelInputButton.setVisibility(View.GONE);
        if(searchResultListView != null && searchResultListView.getVisibility() == View.VISIBLE){
            searchResultListView.setVisibility(View.GONE);
            typeButtonHistoryView.setVisibility(View.VISIBLE);
        }
        if(nothingTextView != null && nothingTextView.getVisibility() == View.VISIBLE) nothingTextView.setVisibility(View.GONE);
        getAndShowSearchHistoryRecord();
    }

    /**
     * 在输入框从空变为含有内容时调用
     * 使搜索控件出现
     */
    public void showSearchWidget(){
        searchButton.setVisibility(View.VISIBLE);
        voiceButton.setVisibility(View.GONE);
        cancelInputButton.setVisibility(View.VISIBLE);
        if(nothingTextView != null && nothingTextView.getVisibility() == View.VISIBLE) nothingTextView.setVisibility(View.GONE);
    }

    /**
     * 为可见的控件添加监听器
     * @param res：控件的id
     */
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.SearchPageActivity_search_back_arrow:
                        onBackPressed();
                        break;
                    case R.id.SearchPageActivity_cancel_input_button:
                        editText.setText("");
                        break;
                    case R.id.SearchPageActivity_search_button:
                        if(nothingTextView != null && nothingTextView.getVisibility() == View.VISIBLE) nothingTextView.setVisibility(View.GONE);
                        goSearch();
                        break;
                    case R.id.SearchPageActivity_voice_search:
                        Toast.makeText(SearchPageActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SearchPageActivity_clear_all_history:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SearchPageActivity.this);
                        dialog.setCancelable(true);
                        dialog.setTitle("清空历史记录？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearHistory();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                }
            }
        });
    }

    /**
     * 保存搜索记录
     * 打开搜索页面
     */
    public void goSearch(){
        String text = editText.getText().toString();
        if(TextUtils.isEmpty(text)) return;
        putSearchRecordToDatabase(text);
        getSearchResultAndUpdateList(text);
    }

    /**
     * 获取搜索结果并更新listView
     * @param text：搜索内容
     */
    public void getSearchResultAndUpdateList(String text){
        typeButtonHistoryView.setVisibility(View.GONE);
        searchResultListView.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        PoiSearch.OnPoiSearchListener onPoiSearchListener = new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if(i != 1000){
                    Toast.makeText(SearchPageActivity.this, "搜索出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<PoiItem> poiItems = poiResult.getPois();
                if(poiItems.size() == 0){
                    nothingTextView.setVisibility(View.VISIBLE);
                }
                else{
                    itemList.clear();
                    for (int j = 0; j < poiItems.size(); j++) {
                        PoiItem poiItem = poiItems.get(j);
                        LatLonPoint point = poiItem.getLatLonPoint();
                        SearchResultItem item = new SearchResultItem(new LatLng(point.getLatitude(), point.getLongitude()), poiItem.getTitle());
                        itemList.add(item);
                    }
                    resultListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        };
        PoiSearch.Query query = new PoiSearch.Query(text, "", "郑州");
        PoiSearch search = new PoiSearch(SearchPageActivity.this, query);
        search.setOnPoiSearchListener(onPoiSearchListener);
        search.searchPOIAsyn();
    }

    /**
     * 将记录保存到本地以及远程数据库
     * @param text：搜索内容
     */
    public void putSearchRecordToDatabase(String text){
        if(TextUtils.isEmpty(text)) return;
        current_user= DataSupport.findLast(Users.class);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        historyList=gson.fromJson(current_user.getSearchHistory(), type);
        if(historyList.contains(text)) return;
        historyList.add(text);
        current_user.setSearchHistory(gson.toJson(historyList));

        current_user.updateAll("User_id = ?",""+current_user.getUser_id());
        putSearchRecordToOrigin(text); //上传到远程数据库
    }

    /**
     * 将搜索内容发送远端存储
     * @param text：搜索内容
     */
    public void putSearchRecordToOrigin(final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/history.php?action=setHistory" +
                        "&id="+current_user.getUser_id()
                        +"&username="+current_user.getUsername()
                        +"&searchHistory="+text);
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        Log.d(TAG, "run: " + response.charAt(3));
                        //TODO ido 处理增加搜索记录的返回结果，规范化验证逻辑
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 从数据库获取历史记录
     */
    public void getAndShowSearchHistoryRecord(){

        current_user= DataSupport.findLast(Users.class);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        historyList=gson.fromJson(current_user.getSearchHistory(), type);  //解析获得的字符串为json

        searchHistoryHolder.removeAllViews();
        int listSize =historyList.size();
        TextView tmp =  findViewById(R.id.SearchPageActivity_clear_all_history);
        if(listSize > 0) tmp.setVisibility(View.VISIBLE);
        else tmp.setVisibility(View.GONE);
        Collections.reverse(historyList);
        for(String text : historyList ){
            final SearchHistoryItemLayout item = new SearchHistoryItemLayout(this, null, text);
            item.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSearchResultAndUpdateList(item.getItemText());
                    editText.setText(item.getItemText());
                }
            });
            searchHistoryHolder.addView(item);
        }
        Collections.reverse(historyList);
    }

    /**
     * 删除所有记录
     */
    public void clearHistory(){
        historyList.clear();
        current_user.setSearchHistory(gson.toJson(historyList));
        current_user.updateAll("User_id = ?",""+current_user.getUser_id());
        sendRequestWithHttpClient_clearHistory();
        getAndShowSearchHistoryRecord();
    }

    public void finish(){
        super.finish();
        overridePendingTransition(Animation.ABSOLUTE, Animation.ABSOLUTE);
    }

    /**
     * 如果搜索列表可见-》隐藏
     * 否则销毁活动，返回数据
     */
    @Override
    public void onBackPressed() {
        if(searchResultListView != null && searchResultListView.getVisibility() == View.VISIBLE){
            searchResultListView.setVisibility(View.GONE);
            typeButtonHistoryView.setVisibility(View.VISIBLE);
            editText.setText("");
            if(nothingTextView != null && nothingTextView.getVisibility() == View.VISIBLE) nothingTextView.setVisibility(View.GONE);
            getAndShowSearchHistoryRecord();
        }
        else {   //销毁活动
            Intent intent = new Intent();
            intent.putExtra("poiData", new SearchResultItem(new LatLng(0.0, 0.0), "NONE"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onResume(){
        super.onResume();
        getAndShowSearchHistoryRecord();
        editText.setText("");
    }

    /**
     * 清除远端搜索数据
     */
    private void sendRequestWithHttpClient_clearHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url + "/history.php?action=clearSearchHistory&id=" + current_user.getUser_id()
                        + "&username=" + current_user.getUsername());
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if ((httpResponse.getEntity()) != null) {
                        HttpEntity entity = httpResponse.getEntity();
                        //TODO 处理返回值
                        String response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                        Log.d(TAG, "run: " + response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    class SearchResultListAdapter extends ArrayAdapter<SearchResultItem> {
        private int resourceId;

        SearchResultListAdapter(Context context, int resourceId, List<SearchResultItem> objects){
            super(context, resourceId, objects);
            this.resourceId = resourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            SearchResultItem item = getItem(position);
            View view;
            ViewHolder viewHolder;
            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                view.findViewById(R.id.search_history_item).setFocusable(false);
                viewHolder = new ViewHolder();
                viewHolder.result_text = view.findViewById(R.id.search_history_result_item_text);
                view.setTag(viewHolder);
            }
            else{
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            if(item != null) viewHolder.result_text.setText(item.getItemDetail());
            else viewHolder.result_text.setText("");
            return view;

        }
        class ViewHolder {
            TextView result_text;
        }
    }

}
