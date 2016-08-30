package com.example.fourth.projectdemoiii_newsapp.bean;

import android.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
//Gson是通过反射，通过在这个类中的成员变量中，找 和Json中相同字符串的字段， 赋值。
//1. 类里出现的成员变量的命名 需要与json字符串里的 key 一样。如果写错了，不一样会怎样？不会报错，只是不能找到同名的key去给他赋值。
//2. 如果你的bean里面多写了某个字段，也不会报错。不会报错，只是不能找到同名的key去给他赋值。
//3. 如果你的bean里少了某个字段，也不会报错，只是无法得到该字段的值。

public class Categories {
//使用Gson解析，会自动将Json解析的数据放在Java bean中，
// 在点击新闻中心radio-->即在NewsPage中，去使用Gson解析
    //这样，在LeftMenuFragment中，就不需要用titles[]来存放数据了
        //1、在API setMenuData()中，传一个Categories作为参数，就可以获得解析结果了。
        //2、在ListView的Adapter中，拿到每个position对应的categories中的集合data中的MenuDataInfo类，
       //                   从而拿到MenuDataInfo这个bean中title字段代表的值

    public int retcode;

    public ArrayList<MenuDataInfo> data;

    public class MenuDataInfo{
        public int id;
        public String title;
        public int type;
        public String url;
        public String urll;
        public List<ChildrenInfo> children;

        @Override
        public String toString() {
            return "MenuDataInfo{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    ", urll='" + urll + '\'' +
                    '}';
        }
    }

    public class ChildrenInfo{
        //NewsMenuPage的每个page要显示的数据
        public int id;
        public String title;//children数组中的title字段，用来作为中间部分的指示器
        public int type;
        public String url;//viewpager的具体每个page要显示的数据的url
                              // -----根据这个url，找到json数据，并解析放到另一个bean(NewsDetail)中
    }

    @Override
    public String toString() {
        return "Categories{" +
                "retcode=" + retcode +
                ", data=" + data +
                '}';
    }
}
