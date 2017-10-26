package com.baway.week2exam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView mElv;
    /**
     * 全选
     */
    private CheckBox mCbAll;
    /**
     * 合计：
     */
    private TextView mTvTotal;
    private List<GroupBean> groupList = new ArrayList<>();
    private List<List<ChildBean>> childList = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();
        //给设置ExpandableListView设置数据
        //模拟数据
        initData();
        adapter = new MyAdapter(this, groupList, childList);
        mElv.setGroupIndicator(null);
        mElv.setAdapter(adapter);
        //全部展开
        for (int i = 0; i < groupList.size(); i++) {
            mElv.expandGroup(i);
        }
        //给全选设置点击事件
        mCbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.allChecked(mCbAll.isChecked());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void messageCountEvent(MessageCounEvent msg) {
        mTvTotal.setText("总计：" + msg.getCount() + "个 "+msg.getMoney()+"元");
    }

    @Subscribe
    public void messageEvent(MessageEvent msg) {
        mCbAll.setChecked(msg.isFlag());
    }

    private void initData() {
        for (int i = 0; i < 3; i++) {
            GroupBean groupBean = new GroupBean("商家" + i, false);
            groupList.add(groupBean);
            List<ChildBean> list = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                ChildBean childBean = new ChildBean("商品" + i, false, 12.0f, 1);
                list.add(childBean);
            }
            childList.add(list);
        }
    }

    private void initView() {
        mElv = (ExpandableListView) findViewById(R.id.elv);
        mCbAll = (CheckBox) findViewById(R.id.cbAll);
        mTvTotal = (TextView) findViewById(R.id.tvTotal);
    }


}
