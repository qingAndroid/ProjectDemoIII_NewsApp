package com.example.fourth.projectdemoiii_newsapp.menupage;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.fourth.projectdemoiii_newsapp.bean.Categories;

/**
 * Created by Administrator on 2016/8/24.
 */
public class PictureMenuPage extends BaseMenuPage {

    public PictureMenuPage(Activity mActivity, Categories.MenuDataInfo menuDataInfo) {
        super(mActivity, menuDataInfo);
    }

    //初始化时，将json数据传进来，可以拿来显示
    @Override
    public View initView() {

        TextView textView = new TextView(mActivity);
        textView.setText(menuDataInfo.title);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLUE);
        return textView;
    }

    @Override
    public void initData() {

    }
}
