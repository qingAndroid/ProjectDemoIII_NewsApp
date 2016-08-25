package com.example.fourth.projectdemoiii_newsapp.page;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/23.
 */
public class SmartServicePage extends BasePage {

    public SmartServicePage(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        //1、修改page中的标题栏的标题
        tv_pageview_title.setText("智慧服务");
        //2、修改page中 的中间部分要显示的布局：从服务器拿到的数据

        TextView textView = new TextView(mActivity);
        textView.setText("这是智慧服务的page");
        ll_pageview_content.addView(textView);
    }
}
