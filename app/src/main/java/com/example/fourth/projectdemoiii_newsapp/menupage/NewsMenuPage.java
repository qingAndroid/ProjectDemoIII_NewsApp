package com.example.fourth.projectdemoiii_newsapp.menupage;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fourth.projectdemoiii_newsapp.HomeActivity;
import com.example.fourth.projectdemoiii_newsapp.R;
import com.example.fourth.projectdemoiii_newsapp.bean.Categories;
import com.example.fourth.projectdemoiii_newsapp.newsdetailPage.NewsDetailPage;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/24.
 */
//Day4
//介绍了滑动事件的分发机制，处理了我们app中的滑动冲突问题。\/

//把新闻详情页加上ViewPager，显示头条新闻。发现有新的滑动冲突问题，待处理。\/

//解决了多级ViewPager上的滑动冲突问题。初步实现了新闻页的显示效果。
    //Day5
    //部分实现了ListView下拉刷新的功能。
    //实现了listvie的触底自动加载更多。并实现了点击进入新闻的详情页。
    //Day6
    //实现了组图页面的显示。
public class NewsMenuPage extends BaseMenuPage {

    private TabPageIndicator indicator_newsmenupage_title;
    private ViewPager vp_newsmenupage_content ;
    private ArrayList<View> newsMenuPageList;

    //初始化时，将json数据传进来，可以拿来显示
    public NewsMenuPage(Activity mActivity, Categories.MenuDataInfo menuDataInfo) {
        super(mActivity, menuDataInfo);
    }


    @Override
    public View initView() {
        //测试用的TextView
        //TextView textView = new TextView(mActivity);
//        textView.setText(menuDataInfo.title);
//        textView.setTextSize(30);
//        textView.setTextColor(Color.RED);

        //在初始化中间部分显示的视图时，将这个xml填充过去
        View inflate = View.inflate(mActivity, R.layout.newsmenupage, null);//

        indicator_newsmenupage_title = (TabPageIndicator) inflate.findViewById(R.id.indicator_newsmenupage_title);
        vp_newsmenupage_content   =  (ViewPager) inflate.findViewById(R.id.vp_newsmenupage_content);

        //不使用事件分发机制去禁止slidingmenu滑动(在这个menupage的自定义viewpager中)
        // 而是使用HomeActivity的API：所以在page1之后的所有page:,禁止Slidingmenu滑动
        vp_newsmenupage_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //重写API
                HomeActivity homeActivity = (HomeActivity) NewsMenuPage.this.mActivity;

                if(position==0){
                    //侧边栏可以滑出
                    homeActivity.setSlidingMenuEnable(true);
                }else{
                    //侧边栏不可以滑动
                    homeActivity.setSlidingMenuEnable(false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return inflate;
    }


    @Override
    public void initData() {
//初始化 指示器 和 viewpage要显示的数据
        //--->这是在点击item“新闻”时，显示出来的MenuPage
        //--->所以可以在点击item监听的地方（LeftMenuFragment）调用这个API之后，再去初始化
        //---->但是与这个NewsMenuPage有联系的是NewsPage
        //---->在NewsPage中和LeftMenuFragment中item点击时一起调用的是API：changeNewsPageContent()
        ////---->所以在NewsPage中的这个位置将NewsMenuPage的这个API调用一下

        //list:放ViewPager要显示的page，个数是解析出来的集合中元素个数
        newsMenuPageList = new ArrayList<>();

        //显示的page个数是解析出来的集合中元素个数
        for (int i=0;i<menuDataInfo.children.size();i++){

            //依次从解析好的json数据中拿到----"children"字段中----每个数组的---title字段的值
            Categories.ChildrenInfo childrenInfo = menuDataInfo.children.get(i);

            //现在Viewpager中的page里显示一个TextView测试一下
            TextView textView = new TextView(mActivity);
            textView.setText(childrenInfo.title);
            textView.setTextColor(Color.CYAN);
            textView.setTextSize(50);
            textView.setGravity(Gravity.CENTER);

            //往中间部分的page列表中放入要显示的View
            newsMenuPageList.add(textView);
        }

    //--------------------给ViewPager设置适配器（就是设置要显示的page）
        vp_newsmenupage_content.setAdapter(new NewsMenuPageAdapter());
    //--------------------注意，Indicator的使用，必须要在ViewPager设置adapter之后，才能关联到ViewPager
        indicator_newsmenupage_title.setViewPager(vp_newsmenupage_content);

    }

    class NewsMenuPageAdapter extends PagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
        //-Indicator------设置每个page的title(即Indicator的每个指示项的文字)
            //重写这个方法：显示json中的数据bean中的数据作为Indicator
            return menuDataInfo.children.get(position).title;//super.getPageTitle(position);
        }

        @Override
        public int getCount() {
            //显示的page个数是解析出来的集合中元素个数
            return newsMenuPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
        //从page集合list中拿出View显示到每个page上

            /*TextView textview = (TextView) newsMenuPageList.get(position);
            //将要显示的View放入容器中
            container.addView(textview);*/

            NewsDetailPage newsDetailPage =
                    new NewsDetailPage(menuDataInfo.children.get(position), mActivity);
            View mNewsDetailPage_view = newsDetailPage.mNewsDetailPage_view;
            //1、将要显示的View放入容器中
            container.addView(mNewsDetailPage_view);

            //2、返回一个object
            return mNewsDetailPage_view;//super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
            //super.destroyItem(container, position, object);
        }
    }
}
