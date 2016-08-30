package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;


import com.example.fourth.projectdemoiii_newsapp.R;
import com.example.fourth.projectdemoiii_newsapp.page.BasePage;
import com.example.fourth.projectdemoiii_newsapp.page.GovernmentPage;
import com.example.fourth.projectdemoiii_newsapp.page.HomePage;
import com.example.fourth.projectdemoiii_newsapp.page.NewsPage;
import com.example.fourth.projectdemoiii_newsapp.page.SettingPage;
import com.example.fourth.projectdemoiii_newsapp.page.SmartServicePage;
import com.example.fourth.projectdemoiii_newsapp.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/23.
 */
///*  1、完成下面一排button，以及点击时和未点击的颜色变化
//*   2、 不同的page类用显示不同的视图，用xml进行填充
//*   3、完成viewpager要显示的5个不同的page，都放入一个page列表中
//*   4、下面一排radiobutton设置点击事件，让每个radio对应自己的page（从page列表中）
//*   5、标题栏的button要设置只在新闻中心对应的page中才显示*/

public class ContentFragment extends Fragment {


    //将准备的pagelist中放入我们的五个page
    ArrayList<BasePage>  pagelist = new ArrayList<BasePage>();
    private NoScrollViewPager vp_fragmentcontent_content;
    private RadioGroup rg_fragmentcontent_bottom;

    //重写onCreateView方法
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //1、将自己写的fragment的xml布局 ,填充到Fragment中，并返回
        View inflate = View.inflate(getActivity(), R.layout.fragment_content, null);
        vp_fragmentcontent_content = (NoScrollViewPager) inflate.findViewById(R.id.vp_fragmentcontent_content);
        rg_fragmentcontent_bottom = (RadioGroup) inflate.findViewById(R.id.rg_fragmentcontent_bottom);

        //2、将准备的pagelist中放入我们的五个page
        pagelist.add(new HomePage(getActivity()));//在这里初始化page
        pagelist.add(new NewsPage(getActivity()));
        pagelist.add(new SmartServicePage(getActivity()));
        pagelist.add(new GovernmentPage(getActivity()));
        pagelist.add(new SettingPage(getActivity()));

        //3、给fragemnt布局中的viewPager控件 设置Adapter ，
        vp_fragmentcontent_content.setAdapter(new ContentPageAdapter());

        //4、给fragemnt布局中的radioGroup设置点击事件
        rg_fragmentcontent_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                //点击radiobutton，来控制当前显示的page
                //设置viewPager中的当前的page只能通过这里的radiobutton来改变(因为滑动事件在自定义viewPager类中被禁用了)

                //传进来的checkId（每个radiobutton对应的资源id）
                switch (checkedId){
                    case R.id.rb_fragmentcontent_home:

                        //当点击button来改变page时，false代表不要出现page之间的平滑过渡，直接从一个page跳到下一个page
                        vp_fragmentcontent_content.setCurrentItem(0,false);
                        //在当前的page上控制slidingmenu是否可以滑出
                        pagelist.get(0).slidingMenuEnable(false);
                        break;
                    case R.id.rb_fragmentcontent_news:
                        vp_fragmentcontent_content.setCurrentItem(1,false);
                        NewsPage newsPage = (NewsPage) pagelist.get(1);
                        newsPage.slidingMenuEnable(true);//只有新闻中心page可以将侧边栏滑出

                        //当点击radio时，才去拿json解析的数据，并让侧边栏的listview显示
                        //newsPage.getDataFromServer();
                        //可能是从缓存中拿，也可能是联网拿-----调用NewsPage中的API
                        newsPage.getData();
                        break;

                    case R.id.rb_fragmentcontent_service:
                        vp_fragmentcontent_content.setCurrentItem(2,false);
                        pagelist.get(2).slidingMenuEnable(false);
                        break;

                    case R.id.rb_fragmentcontent_government:
                        vp_fragmentcontent_content.setCurrentItem(3,false);
                        pagelist.get(3).slidingMenuEnable(false);
                        break;

                    case R.id.rb_fragmentcontent_setting:
                        vp_fragmentcontent_content.setCurrentItem(4,false);
                        pagelist.get(4).slidingMenuEnable(false);
                        break;
                }
            }
        });

        //注意细节：在最开始的时候，就默认选中的radio是“首页”
        rg_fragmentcontent_bottom.check(R.id.rb_fragmentcontent_home);

        return inflate;//super.onCreateView(inflater, container, savedInstanceState);
    }

    class ContentPageAdapter extends PagerAdapter{
  //将pagelist中所有page拿出来显示到viewPager中
        @Override
        public int getCount() {
            return pagelist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //从列表中拿到page需要填充的视图View
            BasePage basePage = pagelist.get(position);
            View view = basePage.mPageView;

            //1、将view加到容器container中
            container.addView(view);

            //2、返回view
            return view;//super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            //从容器中将上上个page的视图移除
            container.removeView((View) object);

            //不能继承父类的，因为会抛出异常
            // super.destroyItem(container, position, object);
        }
    }

    //拿到NewsPage
    public NewsPage getNewsPage(){

        NewsPage newsPage = null;
        if(pagelist!=null&&!pagelist.isEmpty()){

            newsPage = (NewsPage) pagelist.get(1);
        }
        return newsPage;
    }
}
