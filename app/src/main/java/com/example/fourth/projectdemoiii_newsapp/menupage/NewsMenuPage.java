package com.example.fourth.projectdemoiii_newsapp.menupage;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fourth.projectdemoiii_newsapp.R;
import com.example.fourth.projectdemoiii_newsapp.bean.Categories;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/24.
 */
//将新闻中心Page的侧边栏中的新闻item  对应的 page中的LinearLayout（MenuPage）的实现分为几部分：
        // 1、可以用一个ViewPager 实现 ：中间的新闻数据显示的内容可以滑动
        //2、还有，用一个页面指示器indicator（从json拿到的数据）
    //所以，首先写一个xml布局，里面放以上两个东西ViewPager和indicator

//-----------模仿sample中：页面指示器indicator：步骤-----------------
//    首先要放入两行代码：
// 1、一个是找indicator,（findViewById）
// 2、另一个是给indicator设置ViewPager
//3、重写ViewPager的Apater中的getPageTitle()方法:返回从JSon的children字段中拿的title字段的值
//4、设置indicator中的文字点击特效（增加下划线）


//给indicator设置ViewPager就必须是在viewPager已经设置好Adapter之后，才能去设置
//即注意，Indicator的使用，必须要在viewPager设置adapter之后，才能关联到viewPager，

//indicator的特效包括：
// 1、增加下划线：在SampleTabsDefault这个Activity的主题theme上（Manifest中），
            // 去修改我们homeActivity的Manifest中的theme
///2、修改背景为白色：比如在我们的homeActivity的布局文件中的根节点的背景改为白色
// 3、修改指示器的文字颜色：在样式里面可以修改；（样式的各种设置最后显示都是就近原则）
                    //文字颜色是因为我们增加下划线时，才变化的，所以我们修改这个theme的样式
                      // -->这里定义的textColor代表了activity中所有text都是这个颜色
               //------>theme的样式：修改源码Library中的values文件夹下的vpi_styles.xml中的一个样式
                    //在源码中修改字体的textSize、
//                     增加设置textColor、----模仿写selector（在选中和未选中时的颜色）
//                     background(就是下划线的图片)---->模仿写selector(9-patch图片)
//                                               --->修改选中状态的图片为我们自己的图片，没选中的用透明背景

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
        //-------设置每个page的title(即Indicator的每个指示项的文字)
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

            TextView textview = (TextView) newsMenuPageList.get(position);
            //将要显示的View放入容器中
            container.addView(textview);
            //返回一个object
            return textview;//super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((TextView) object);
            //super.destroyItem(container, position, object);
        }
    }
}
