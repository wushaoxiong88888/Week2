package com.baway.week2exam;

/**
 * Created by peng on 2017/10/24.
 */

public class ChildBean {
    private String childName;
    private boolean checked;
    private float money;
    private int count;

    public ChildBean(String childName, boolean checked, float money, int count) {
        this.childName = childName;
        this.checked = checked;
        this.money = money;
        this.count = count;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
