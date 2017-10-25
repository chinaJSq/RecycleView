package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.view.EndlessRecyclerOnScrollListener;
import com.view.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecycle =null;
    private List<String> mDate =null;
    private MyAdapter mMyAdapter = null;
    private SwipeRefreshLayout mRefreshlayout =null;
    private UpDateTask mUpDateTask =null;
    private LinearLayoutManager linearLayoutManager =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDate();//初始化数据
        iniView();//初始化布局
        initEven();//初始化事件

    }

    private void initEven() {
        mMyAdapter.setOnItemClickLitener(new MyAdapter.OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, int position)
            {
                Toast.makeText(MainActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,TabbarActivity.class);
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                Toast.makeText(MainActivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
                mMyAdapter.removeData(position);
            }
        });

        mRecycle.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {

                        simulateLoadMoreData();
            }
        });
    }
    private void simulateLoadMoreData() {

        loadMoreData();
        mMyAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "Load Finished!", Toast.LENGTH_SHORT).show();
    }

    private void loadMoreData() {
        List<String> moreList = new ArrayList<>();
        moreList.add("加载更多的数据");
        moreList.add("加载更多的数据");
        mDate.addAll(moreList);
    }

    private void iniView() {
        mRecycle = (RecyclerView) findViewById(R.id.rc_recycle);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(linearLayoutManager);
        mMyAdapter = new MyAdapter(this,mDate);
        mRecycle.setAdapter(mMyAdapter);
        mRecycle.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecycle.setItemAnimator(new DefaultItemAnimator());
        mRefreshlayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout_grid);
        mRefreshlayout.setOnRefreshListener(MainActivity.this);

    }

    private void initDate() {
        mDate = new ArrayList<>();
        for(int i='a';i<'z';i++){
            mDate.add(String.valueOf((char)i));
        }
    }

    @Override
    public void onRefresh() {
        mUpDateTask = new UpDateTask();
        mUpDateTask.execute();
    }
    private  class UpDateTask extends AsyncTask<Void,Void,List<String>>{

        public UpDateTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            mMyAdapter.addItems(strings);
            mRefreshlayout.setRefreshing(false);
            mRecycle.scrollToPosition(0);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(List<String> strings) {
            super.onCancelled(strings);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            List<String> strings = new ArrayList<>();
            strings.add("新数据1");
            strings.add("新数据2");
            return strings;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUpDateTask!=null){
            mUpDateTask.onCancelled();
            mUpDateTask.cancel(true);
        }
    }
}
