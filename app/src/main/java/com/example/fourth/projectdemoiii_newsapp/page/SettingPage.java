package com.example.fourth.projectdemoiii_newsapp.page;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/23.
 */
public class SettingPage extends BasePage {

    public SettingPage(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        //1、修改page中的标题栏的标题
        tv_pageview_title.setText("设置");
        //2、修改page中 的中间部分要显示的布局：从服务器拿到的数据

        TextView textView = new TextView(mActivity);
        textView.setText("这是设置的page中间部分");
        ll_pageview_content.addView(textView);
    }
}
