package com.example.codoon.myapplication.Record;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.codoon.myapplication.Adapter.RecordAdapter;
import com.example.codoon.myapplication.DbFlow.MyDataBase;
import com.example.codoon.myapplication.R;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.Record
 * 文件名：RecordActivity
 * 创建时间：2018/10/19 下午2:44
 * 描述：TODO
 */
public class RecordActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private List<MyDataBase> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecordAdapter(this,userList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        getDataFromDB();
    }

    private void getDataFromDB() {
        List<MyDataBase> datas = SQLite.select().from(MyDataBase.class).queryList();
        userList.addAll(datas);
        adapter.notifyDataSetChanged();

    }
}
