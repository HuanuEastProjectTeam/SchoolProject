package com.example.andriodcar.Map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.example.andriodcar.R;

import java.util.List;

public class SuggesitionSearchResultActivity extends AppCompatActivity {
    private List<SuggestionResult.SuggestionInfo> searchResult;
    private ListView listView;
    private EditText editText;
    private ArrayAdapter<List> adapter;//创建一个适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggesition_search_result);
        showViewList();//调用展示搜索结果方法
    }
    private void showViewList() {
        //调用地图搜索结果处理方法,返回搜索结果集合
       // listView=findViewById(R.id.listView);//获取ListView组件
        editText=findViewById(R.id.search);//获取搜索组



    }
}
