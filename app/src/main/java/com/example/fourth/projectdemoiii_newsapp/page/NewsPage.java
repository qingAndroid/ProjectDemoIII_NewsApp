package com.example.fourth.projectdemoiii_newsapp.page;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fourth.projectdemoiii_newsapp.HomeActivity;
import com.example.fourth.projectdemoiii_newsapp.bean.Categories;
import com.example.fourth.projectdemoiii_newsapp.bean.MenuTitle;
import com.example.fourth.projectdemoiii_newsapp.constant.Constant;
import com.example.fourth.projectdemoiii_newsapp.menupage.BaseMenuPage;
import com.example.fourth.projectdemoiii_newsapp.menupage.InteractMenuPage;
import com.example.fourth.projectdemoiii_newsapp.menupage.NewsMenuPage;
import com.example.fourth.projectdemoiii_newsapp.menupage.PictureMenuPage;
import com.example.fourth.projectdemoiii_newsapp.menupage.TopicMenuPage;
import com.example.fourth.projectdemoiii_newsapp.uitls.SharedPrefUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fragment.LeftMenuFragment;

/**
 * Created by Administrator on 2016/8/23.
 */
public class NewsPage extends BasePage {
    private static final String TAG = "NewsPage";
    private HomeActivity homeActivity;
    private ArrayList<BaseMenuPage> menuPageslist;

    //在这里page要可以滑出侧边栏----->在radio被点击时设置

    public NewsPage(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        //1、修改page中的标题栏的标题
        tv_pageview_title.setText("新闻中心");
        //2、修改page中 的中间部分要显示的布局：从服务器拿到的数据

        TextView textView = new TextView(mActivity);
        textView.setText("这是新闻中心的page中间部分,没联网...");
        ll_pageview_content.addView(textView);

        //-------------------比其他的page多的部分：要将imageButton设置为可见-----------------------
        bt_pageview_leftbutton.setVisibility(View.VISIBLE);
        /* bt_pageview_rightbutton.setVisibility(View.VISIBLE);*/

        //将BasePage的成员变量mActivity强转为HomeActivity
        homeActivity = (HomeActivity) this.mActivity;

        //给标题栏上的左边的button设置点击事件：弹出或收起侧边栏
        bt_pageview_leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //两种方式都可以
               // homeActivity.slidingMenu.toggle();
                homeActivity.toggleSlidingMenu();
            }
        });
    }

    public void getData(){
        //可能是从缓存中拿，也可能是联网拿

        final String url = Constant.SERVER_ADDR+"/categories.json";
        String jsonFromCache = SharedPrefUtils.getJsonFromCache(url, mActivity);
        if (jsonFromCache.isEmpty()){
            //缓存为空，去服务器上拿json数据，并解析

            Log.i(TAG,"缓存中json数据不为空");
            getDataFromServer(url);
        }else{
            //缓存中json数据不为空，则直接解析从sp中拿到的json数据

            Log.i(TAG,"缓存中json数据为空");
            parseJsonString(jsonFromCache);
        }

    }

    public void getDataFromServer(String url){
        //当用户在contentFragment中点击新闻中心这个radio时，
        // 再去调用这个API去加载网络数据 ，将数据给到侧边栏显示
    //设置联网权限，并打开服务器，下载侧边栏的数据

         final String urlString=url;

        //首先使用开源框架xutils去服务器拿数据
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.i(TAG,responseInfo.result);

                //调用工具类中的API： 将从服务器上成功拿到的数据缓存到SP中
                //NewsPage这里拿到并缓存的json数据 是侧边栏上显示的title和新闻中心的12个新闻分类的indicator，
                //而我们看到的新闻详细内容是在NewsDetailPage中拿到的数据，所以要去NewsDetailPage中缓存数据到sp中

                //以访问json数据的url作为key,以拿到的json数据的String字符串作为Value
                SharedPrefUtils.saveJsonToCache(urlString,responseInfo.result,mActivity);

                //然后将拿到的数据进行Json解析
                parseJsonString(responseInfo.result);
            }
            @Override
            public void onFailure(HttpException e, String s) {
                Log.i(TAG,s);

            }
        });


    }

    public void parseJsonString(String result)  {
        //解析json数据
        //2、使用Gson开源框架  解析Json数据
        Gson gson = new Gson();
        //将解析结果放入bean--->Categories的字节码文件
        Categories categories = gson.fromJson(result, Categories.class);
        //将数据给到侧边栏Fragment显示（调用HomeActivity和LeftMenuFragment的API）
        LeftMenuFragment leftMenuFragment = homeActivity.getLeftMenuFragment();
        leftMenuFragment.setMenuData(categories);

        //当解析完json数据后，就可以初始化中间部分MenuPage的需要显示数据了
        initMenuPage(categories);

//        //1、正常解析json:然后可以将json解析后的结果给到bean--
//        try {
//            JSONObject jsonObject = new JSONObject(result);
//
//            JSONArray data = jsonObject.getJSONArray("data");
//            JSONObject jsonObject1 = data.getJSONObject(0);
//            String title1 = jsonObject1.getString("title");
//
//
//            JSONObject jsonObject2 = data.getJSONObject(1);
//            String title2 = jsonObject2.getString("title");
//            Log.i(TAG,"menu2="+title2);
//
//            JSONObject jsonObject3 = data.getJSONObject(2);
//            String title3 = jsonObject3.getString("title");
//            Log.i(TAG,"menu3="+title3);
//
//            JSONObject jsonObject4 = data.getJSONObject(3);
//            String title4 = jsonObject4.getString("title");
//            Log.i(TAG,"menu4="+title4);
//
//            MenuTitle menuTitle = new MenuTitle(title1,title2,title3,title4);
//
//            LeftMenuFragment leftMenuFragment = homeActivity.getLeftMenuFragment();
//
//            //拿到解析完的数据，
//            //再让侧边栏的listview显示解析出来的数据
//            leftMenuFragment.setMenuData(menuTitle);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    // parseJsonString中调用这个API
    public void initMenuPage(Categories categories){
//这个API： 刚点击新闻中心时，去解析完json数据后，
//  就可以初始化中间部分MenuPage需要显示的视图（在MenuPage中的成员变量）


        //由于4个item有4个不同的MenuPage，-->放入一个List中
        menuPageslist = new ArrayList<>();
        //将传进来的Categories中的menuDataInfo对象给到MenuPage的初始化构造
        menuPageslist.add(new NewsMenuPage(homeActivity,categories.data.get(0)));
        menuPageslist.add(new TopicMenuPage(homeActivity,categories.data.get(1)));
        final PictureMenuPage pictureMenuPage = new PictureMenuPage(homeActivity, categories.data.get(2));
        menuPageslist.add(pictureMenuPage);
        menuPageslist.add(new InteractMenuPage(homeActivity,categories.data.get(3)));

        //侧边栏的组图组图 对应的menupage ：点击选项：给组图 对应的menupage 改变 显示格式：单行变双行
        bt_pageview_rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureMenuPage.changeUI();
            }
        });

        //刚点击新闻中心时，默认显示listview的第一个item对应的MenuPage(LinearLayout)
        changeNewsPageContent(0);
    }

    //暴露API：
    public void changeNewsPageContent(int position){
    //根据侧边栏点击的iten来给这个Page中间显示新闻数据部分的LinearLayout增加View


        //1、先将之前的View移除
        ll_pageview_content.removeAllViews();

        //2、准备要显示的view:从List列表中拿出来MenuPage类中的变量view
        BaseMenuPage menuPage = menuPageslist.get(position);

        //在组图 item对应的右侧的内容，才让选项 这个button 显示出来;因为其他item对应的menupage用不上选项button
        if(position==2){
            bt_pageview_rightbutton.setVisibility(View.VISIBLE);
        }else{
            bt_pageview_rightbutton.setVisibility(View.GONE);
        }

        //3、放入LinearLayout
        ll_pageview_content.addView(menuPage.mMenuPageView);//(textView);

        menuPage.initData();
       //因为需要在 item被点击时，初始化数据每个item对应的Cpntent中间部分的内容，

        //由于这个API在item被点击时调用，所以在这里调用initData()

        // 主要是为了让NewsMenuPage初始化viewpager和indicator的数据
    }
}
