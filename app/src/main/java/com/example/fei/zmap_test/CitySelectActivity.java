package com.example.fei.zmap_test;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.fei.zmap_test.common.CityLetterItem;
import com.example.fei.zmap_test.common.CityLettersAdapter;
import com.example.fei.zmap_test.common.CityListAdapter;
import com.example.fei.zmap_test.common.CityListItem;

import java.util.ArrayList;

public class CitySelectActivity extends AppCompatActivity {
    private ArrayList<CityListItem> cityListItems;
    private ArrayList<CityLetterItem> cityLetterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();

        init();

        RecyclerView recyclerView = findViewById(R.id.CitySelect_recycler_view);
        CityListAdapter listAdapter = new CityListAdapter(cityListItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CitySelectActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerView letterRecyclerView = findViewById(R.id.CitySelect_right_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CitySelectActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CityLettersAdapter lettersAdapter = new CityLettersAdapter(cityLetterItems, recyclerView);
        letterRecyclerView.setLayoutManager(layoutManager);
        letterRecyclerView.setAdapter(lettersAdapter);

    }
    public void init(){
        ArrayList<String> cityIndexLetters = new ArrayList<>();
        cityListItems = new ArrayList<>();
        cityLetterItems = new ArrayList<>();
        ArrayList<Integer> cityIndexPositions = new ArrayList<>();
        String[] cityNames = getResources().getStringArray(R.array.cities);

        for(int i = 0; i < cityNames.length; i++){
            String tmp = cityNames[i].trim();
            if(tmp.length() == 1){
                cityIndexLetters.add(tmp);
                cityIndexPositions.add(i);
                cityListItems.add(new CityListItem(tmp, true));
            }
            else
                cityListItems.add(new CityListItem(tmp, false));
        }
        for(int i = 0; i < cityIndexLetters.size(); i++){
            cityLetterItems.add(new CityLetterItem(cityIndexLetters.get(i), cityIndexPositions.get(i)));
        }
    }
}
