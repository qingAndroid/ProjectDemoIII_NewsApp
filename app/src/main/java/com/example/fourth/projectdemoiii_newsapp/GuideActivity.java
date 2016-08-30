package com.example.fourth.projectdemoiii_newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

//实现了Guide页面里的viewPager 和 enter Button
//实现了Guide页面里的PageIndicator

//使用了pageChangeListener()

public class GuideActivity extends AppCompatActivity {

    private static final String TAG = "GuideActivity";
    private ViewPager vp_guideactiviy_guide;
    private Button bt_guideactivity_enter;
    private View rp_guideactivity_oval;
    private LinearLayout ll_guideactivity_indicator;

    private final int PageCount = 3;
    private List<MyPageInfo> myPageInfolist;//每个item对应的page相关的信息类列表
    //要显示的page上的显示视图View(后面用ImageView来放这些图片)
    private int[] imgResIds = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};

    //Adapter中返回的object对象
    class MyPageInfo{
        ImageView pageIv;
        String pageTitle;
        int x;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        getSupportActionBar().hide();


        //进入这个页面，往sharedpreference中保存flag，表示进入过这个页面
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("guide",true);
        edit.commit();


        //找到需要使用的控件
        ll_guideactivity_indicator = (LinearLayout) findViewById(R.id.ll_guideactivity_indicator);
        bt_guideactivity_enter = (Button) findViewById(R.id.bt_guideactivity_enter);
        rp_guideactivity_oval = findViewById(R.id.rp_guideactivity_oval);

        //初始化page的信息列表
        initImageList();
        //初始化页面指示器
        initIndicator();

        //给ViewPager设置适配器
        vp_guideactiviy_guide = (ViewPager) findViewById(R.id.vp_guideactiviy_guide);
        vp_guideactiviy_guide.setAdapter(new MyPagerGuideAdapter());

        //当页面变化时调用
        vp_guideactiviy_guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当当前的页面滑动的时候调用。
                // postion 当前的page是哪个
                // positionOffset 下一个页面拖出来的比例值
                // 下一个页面拖出来的像素值 positionOffsetPixels

                //更改小红点的位置： 和 红点leftmargin的关系 0-0 1-40 2-80。
                //拿到小红点已经有的layoutparameter

                //已经静态写在布局文件xml中的控件，系统已经将它的属性放在一个LayoutParams中，
                // 所以直接拿到，不用new出来
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)rp_guideactivity_oval.getLayoutParams();
                layoutParams.leftMargin=position*40 + (int)(40*positionOffset);//灰色圆点的宽+灰色圆点的间距为40
                rp_guideactivity_oval.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
            //页面发生变化的时候，会传入当前是哪个page
                Log.i(TAG," onPageSelected="+position);

                //在最后一个page，修改Button为可见
                if (position==PageCount-1){
                    bt_guideactivity_enter.setVisibility(View.VISIBLE);
                }else{
                    bt_guideactivity_enter.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
             //就是viewpage当前的滑动时状态。

                // @see ViewPager#SCROLL_STATE_IDLE :静止
                // @see ViewPager#SCROLL_STATE_DRAGGING：拉着拖动的时候
                // @see ViewPager#SCROLL_STATE_SETTLING：松开手之后，回位的状态。
                Log.i(TAG," state="+state);
            }
        });
    }

    //初始化viewPager要显示的page相关的信息类列表
    private void initImageList(){
        //新建 page对应的信息类 列表
        myPageInfolist = new ArrayList<MyPageInfo>();

        for (int i=0;i<PageCount;i++){
            //将图片依次放进imageview中
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imgResIds[i]);

            //准备每个page的信息类（包括要显示的imageview）
            MyPageInfo myPageInfo = new MyPageInfo();
            myPageInfo.pageIv = imageView;
            myPageInfo.pageTitle = "page"+i;
            myPageInfo.x = i;
            //将每个page的信息放进信息列表中,在Adapter中会用到
            myPageInfolist.add(myPageInfo);

        }
    }

    //初始化viewPager的页面指示器
    private void initIndicator(){

        //根据page个数，用代码动态设置页面指示器中灰色圆点View的个数
        for (int i=0;i<PageCount;i++){

            View view = new View(this);
            //单位是像素//设置viewgroup中子控件的宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20,20);
            if (i!=0){
                layoutParams.leftMargin=20;
                //相对于左边控件的间距
                //当前编辑的控件与其父控件之间的距离控制用margin,
                // 当前编辑的控件内部的内容与其当前的控件之间的距离控制用padding.
               /* Padding 为内边框，指该控件内部内容，如文本/图片距离该控件的边距
                Margin 为外边框，指该控件距离父控件的边距*/
            }
            view.setLayoutParams(layoutParams);
            view.setBackgroundResource(R.drawable.graypoint);

            //将灰色圆点view设置给线性布局
            ll_guideactivity_indicator.addView(view);

            //后面在计算红色圆点leftMargin时，
            // 是将原来红色圆点自己的width(20px),和灰色圆点的leftMargin相加
        }
    }

    //viewPager的适配器
    class MyPagerGuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
        //表示有几个page
            return PageCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            // view:当前page上显示的视图view
            // object: instantiateItem 的一个返回值。
            //-------------默认写法： 返回view==object

            MyPageInfo myPageInfo = (MyPageInfo) object;
            return view==myPageInfo.pageIv;
            // 实现者需要去判断这两东西是不是有关联。
            //如果是关联的view和Object ，则每个page包含object的全部信息
        }

        //position-->实例化Item
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //区别于listview的地方是，不光要返回object,还要将view添加到容器中

            //返回一个Object类，里面包含很多信息（包括View）
            //1、 --->这里用从信息类列表中拿出来的myPageInfo对象  代表Object对象
            MyPageInfo myPageInfo = myPageInfolist.get(position);

            //2、给container设置view
            ImageView pageview = myPageInfo.pageIv;
            container.addView(pageview);

            //3、返回object，并在isViewFromObject()中进行判断
            return myPageInfo;
            // return super.instantiateItem(container, position);
        }

        //销毁  每次只保留三个
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            //将传进来object中的view，从container中移除（上上一个page的object的）
            MyPageInfo myPageInfo = (MyPageInfo) object;

            container.removeView(myPageInfo.pageIv);

            //super.destroyItem(container, position, object);
            //不能使用父类的内容，因为会抛出一个异常
        }
    }

    //进入主页面HomeActivity
    public void enterHome(View v){

        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }
}
