package com.baway.week2exam;

/**
 * Created by peng on 2017/10/24.
 */

public class GroupBean {
    private String groupName;
    private boolean checked;


    public GroupBean() {
    }

    public GroupBean(String groupName, boolean checked) {
        this.groupName = groupName;
        this.checked = checked;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
