package com.baway.week2exam;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by peng on 2017/10/20.
 */

public class AddDelView extends LinearLayout {
    private TextView num;
    private OnItemClick onItemClick;
    private TextView add;
    private TextView del;

    public interface OnItemClick {
        public void onItemAddClick(int count);

        public void onItemDelClick(int count);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public AddDelView(Context context) {
        this(context, null);
    }

    public AddDelView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.add_del_view, this);
        add = findViewById(R.id.add);
        del = findViewById(R.id.del);
        num = findViewById(R.id.num);
    }

    public void setOnAddClickListener(OnClickListener onClickListener) {
        add.setOnClickListener(onClickListener);
    }

    public void setOnDelClickListener(OnClickListener onClickListener) {
        del.setOnClickListener(onClickListener);
    }

    public void setCount(String count) {
        num.setText(count);
    }

    public String getCount(){
        return num.getText().toString().trim();
    }
}
