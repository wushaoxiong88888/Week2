package com.baway.week2exam;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.baway.week2exam.R.id.cb;

/**
 * Created by peng on 2017/10/24.
 */

public class MyAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<GroupBean> groupList;
    private List<List<ChildBean>> childList;
    private int count;

    public MyAdapter(Context context, List<GroupBean> groupList, List<List<ChildBean>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            view = View.inflate(context, R.layout.item, null);
            holder.cb = view.findViewById(cb);
            holder.tv = view.findViewById(R.id.tvName);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (GroupViewHolder) view.getTag();
        }
        //赋值
        GroupBean groupBean = groupList.get(groupPosition);
        holder.cb.setChecked(groupBean.isChecked());
        holder.tv.setText(groupBean.getGroupName());
        //给group的checkbox设置点击事件
        holder.cb.setOnClickListener(new GroupCbOnClickListener(groupPosition));
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            view = View.inflate(context, R.layout.childitem, null);
            holder.cb = view.findViewById(cb);
            holder.tv = view.findViewById(R.id.tvName);
            holder.tvMoney = view.findViewById(R.id.tvPrice);
            holder.adv = view.findViewById(R.id.adv);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ChildViewHolder) view.getTag();
        }
        //赋值
        final ChildBean childBean = childList.get(groupPosition).get(childPosition);
        holder.cb.setChecked(childBean.isChecked());
        holder.tv.setText(childBean.getChildName());
        holder.tvMoney.setText(childBean.getMoney() + "");
        holder.adv.setCount(childBean.getCount() + "");
        holder.cb.setOnClickListener(new ChildCbOnClickListener(groupPosition, childPosition));
        holder.adv.setOnAddClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countStr = holder.adv.getCount();
                int count = Integer.parseInt(countStr);
                holder.adv.setCount(++count + "");
                childBean.setCount(count);
                //发送数量和价钱
                childBean.setChecked(true);
                //判断该商家的所有商品的checkbox是否都选中
                if (isChildChecked(childList.get(groupPosition))) {
                    groupList.get(groupPosition).setChecked(true);
                    MessageEvent msg = new MessageEvent();
                    msg.setFlag(isGroupChecked());
                    EventBus.getDefault().post(msg);
                    notifyDataSetChanged();
                } else {
                    groupList.get(groupPosition).setChecked(false);
                    MessageEvent msg = new MessageEvent();
                    msg.setFlag(false);
                    EventBus.getDefault().post(msg);
                    notifyDataSetChanged();
                    notifyDataSetChanged();
                }
                //计算选中的商品数，并发送到主界面进行显示
                MessageCounEvent msgCount = new MessageCounEvent();
                msgCount.setCount(totalCount());
                msgCount.setMoney(totalMoney());
                EventBus.getDefault().post(msgCount);

            }
        });
        holder.adv.setOnDelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countStr = holder.adv.getCount();
                int count = Integer.parseInt(countStr);
                count = --count < 1 ? 1 : count;
                holder.adv.setCount(count + "");
                childBean.setCount(count);
                if (count > 1) {
                    childBean.setChecked(true);
                }
                //判断该商家的所有商品的checkbox是否都选中
                if (isChildChecked(childList.get(groupPosition))) {
                    groupList.get(groupPosition).setChecked(true);
                    MessageEvent msg = new MessageEvent();
                    msg.setFlag(isGroupChecked());
                    EventBus.getDefault().post(msg);
                    notifyDataSetChanged();
                } else {
                    groupList.get(groupPosition).setChecked(false);
                    MessageEvent msg = new MessageEvent();
                    msg.setFlag(false);
                    EventBus.getDefault().post(msg);
                    notifyDataSetChanged();
                    notifyDataSetChanged();
                }
                //计算选中的商品数，并发送到主界面进行显示
                MessageCounEvent msgCount = new MessageCounEvent();
                msgCount.setCount(totalCount());
                msgCount.setMoney(totalMoney());
                EventBus.getDefault().post(msgCount);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        CheckBox cb;
        TextView tv;
    }

    class ChildViewHolder {
        CheckBox cb;
        TextView tv;
        TextView tvMoney;
        AddDelView adv;
    }

    class ChildCbOnClickListener implements View.OnClickListener {
        private int groupPosition;
        private int childPosition;

        public ChildCbOnClickListener(int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;
                List<ChildBean> childBeen = childList.get(groupPosition);
                ChildBean childBean = childBeen.get(childPosition);
                childBean.setChecked(cb.isChecked());
                //计算选中的商品数，并发送到主界面进行显示
                MessageCounEvent msgCount = new MessageCounEvent();
                msgCount.setCount(totalCount());
                msgCount.setMoney(totalMoney());
                EventBus.getDefault().post(msgCount);
                //判断该商家的所有商品的checkbox是否都选中
                if (isChildChecked(childBeen)) {
                    groupList.get(groupPosition).setChecked(true);
//                    //如果商品全部选中的话，则去判断所有商家是否都选中
//                    if (isGroupChecked()) {
//                        //发送消息去改变全选的状态,变成选中状态
//                    } else {
//                        //发送消息去改变全选的状态，变成未选中状态
//                    }
                    MessageEvent msg = new MessageEvent();
                    msg.setFlag(isGroupChecked());
                    EventBus.getDefault().post(msg);
                    notifyDataSetChanged();
                } else {
                    groupList.get(groupPosition).setChecked(false);
                    MessageEvent msg = new MessageEvent();
                    msg.setFlag(false);
                    EventBus.getDefault().post(msg);
                    notifyDataSetChanged();
                    notifyDataSetChanged();
                }
            }


        }
    }

    /**
     * 判断该商家的所有商品的checkbox是否都选中
     *
     * @return
     */
    private boolean isChildChecked(List<ChildBean> childBeen) {
        for (int i = 0; i < childBeen.size(); i++) {
            ChildBean childBean = childBeen.get(i);
            if (!childBean.isChecked()) {
                return false;
            }
        }
        return true;
    }

    class GroupCbOnClickListener implements View.OnClickListener {
        private int groupPostion;

        public GroupCbOnClickListener(int groupPostion) {
            this.groupPostion = groupPostion;
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CheckBox) {

                //多态，因为我是给checkbox设置的点击事件，所以可以强转成checkbox
                CheckBox cb = (CheckBox) v;
                //根据cb.isChecked()是否选中，给一级列的checkbox改变状态
                groupList.get(groupPostion).setChecked(cb.isChecked());
                List<ChildBean> childBeenList = childList.get(groupPostion);
                for (ChildBean childBean : childBeenList) {
                    childBean.setChecked(cb.isChecked());
                }
                //计算选中的商品数，并发送到主界面进行显示
                MessageCounEvent msgCount = new MessageCounEvent();
                msgCount.setCount(totalCount());
                msgCount.setMoney(totalMoney());
                EventBus.getDefault().post(msgCount);

                //判断其它的商家是否选中
//                if (isGroupChecked()) {
//                    //发送消息去改变全选的状态,变成选中状态
//                    MessageEvent msg = new MessageEvent();
//                    msg.setFlag(true);
//                    EventBus.getDefault().post(msg);
//                } else {
//                    //发送消息去改变全选的状态，变成未选中状态
//                    MessageEvent msg = new MessageEvent();
//                    msg.setFlag(false);
//                    EventBus.getDefault().post(msg);
//                }
                MessageEvent msg = new MessageEvent();
                msg.setFlag(isGroupChecked());
                EventBus.getDefault().post(msg);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 判断其它的商家是否选中
     *
     * @return
     */
    private boolean isGroupChecked() {
        for (GroupBean groupBean : groupList) {
            if (!groupBean.isChecked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 主界面全选按钮的操作
     *
     * @param bool
     */
    public void allChecked(boolean bool) {
        for (int i = 0; i < groupList.size(); i++) {
            groupList.get(i).setChecked(bool);
            for (int j = 0; j < childList.get(i).size(); j++) {
                childList.get(i).get(j).setChecked(bool);
            }
        }
        //计算选中的商品数，并发送到主界面进行显示
        MessageCounEvent msgCount = new MessageCounEvent();
        msgCount.setCount(totalCount());
        msgCount.setMoney(totalMoney());
        EventBus.getDefault().post(msgCount);
        notifyDataSetChanged();

    }

    private float totalMoney() {
        float money = 0f;
        for (int i = 0; i < groupList.size(); i++) {
            for (int j = 0; j < childList.get(i).size(); j++) {
                if (childList.get(i).get(j).isChecked()) {
                    //遍历所有的商品，只要是选中状态的，就计算价格
                    int c = childList.get(i).get(j).getCount();
                    float m = childList.get(i).get(j).getMoney();
                    money += c * m;
                }
            }
        }
        return money;
    }

    private int totalCount() {
        count = 0;
        for (int i = 0; i < groupList.size(); i++) {
            for (int j = 0; j < childList.get(i).size(); j++) {
                if (childList.get(i).get(j).isChecked()) {
                    //遍历所有的商品，只要是选中状态的，就加1
                    count += childList.get(i).get(j).getCount();
                }
            }
        }
        return count;
    }
}
