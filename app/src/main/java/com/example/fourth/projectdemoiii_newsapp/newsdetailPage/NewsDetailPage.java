package com.example.fourth.projectdemoiii_newsapp.newsdetailPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourth.projectdemoiii_newsapp.R;
import com.example.fourth.projectdemoiii_newsapp.ShowNewsActivity;
import com.example.fourth.projectdemoiii_newsapp.bean.Categories;
import com.example.fourth.projectdemoiii_newsapp.bean.NewsDetail;
import com.example.fourth.projectdemoiii_newsapp.constant.Constant;
import com.example.fourth.projectdemoiii_newsapp.uitls.SharedPrefUtils;
import com.example.fourth.projectdemoiii_newsapp.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
//在这个类中封装了****NewsMenuPage视图***中(的ViewPager的)每个page显示的视图View（Viewpager+listview+）
//----------------------------------------------------------------
//所以在这个类写好之后，将NewsMenuPage的viewPager的每个Page的instantiateItem（）
// 填充用的测试用的TextView 改为 这个类中封装的view

//ViewPager显示头条新闻:发现有新的滑动冲突问题，待处理

public class NewsDetailPage  {

     boolean isFromCache=false;

    private static final String TAG ="NewsDetailPage" ;

    //1、这个view不放入构造中，因为不用外界传进来，是要在这个类中创建出来
    public View mNewsDetailPage_view;

    //2、下面两个成员变量要让外界传进来
    public Activity mActivity;//因为下面填充View需要上下文（Activity是上下文的子类）
    public Categories.ChildrenInfo newsDetailInfo;//这个类中包含了要显示的内容的url

    //3、一些变量
    private ViewPager vp_newsdetailpage_topnews;
    private RefreshListView lv_newsdetailpage_newslist;
    private NewsDetail newsDetail;
    private TextView tv_newsdetail_topnewsTitle;
    private CirclePageIndicator indicator_topnews;
    private View header;
    private MyNewsListAdapter myNewsListAdapter;
    private List<NewsDetail.DataBean.NewsBean> listDataSet;

    SharedPreferences sp;
//每个DetailPage都是应该从它的上一级,即NewsMenuPage中拿到要显示的数据的地址（url）
// -----》所以应该是外界传进来的
// 再根据地址url来解析获得的所有数据

    public NewsDetailPage(Categories.ChildrenInfo newsDetailInfo, Activity mActivity) {

        this.newsDetailInfo = newsDetailInfo;
        this.mActivity = mActivity;

        //在创建这个类的时候，就去拿到sp，用来保存等会点击浏览过的新闻item
        sp = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);


        //构造：在这里初始化要显示的view和data
        // ----->因为就应该在创建这个类的时候
        // --->即点击侧边栏的新闻时--->即初始化NewsMenuPage时就需要显示了
        // ---->所以创建这个类的时候，就应该直接将NewsMenuPage的ViewPager的每个page的中的view和数据初始化好
        mNewsDetailPage_view= initView();
        initData();
    }
    public View initView(){

        //将需要的布局填充（包括一个topNews用的ViewPager，listview、Indicator）
        View inflate = View.inflate(mActivity, R.layout.newsdetailpage, null);

        //新闻列表
        lv_newsdetailpage_newslist = (RefreshListView) inflate.findViewById(R.id.lv_newsdetailpage_newslist);

        //--------实现自定义listview的接口中的抽象方法(刷新 加载)
        lv_newsdetailpage_newslist.setMyRefreshListener(new RefreshListView.MyRefreshListener() {
            @Override
            public void onRefreshing() {
                //重新去网络上加载该page对应的url，去获取服务器的最新数据
                refreshNewsData();
            }

            @Override
            public void onLoadMore() {

                //重写抽象方法
                //触底加载更多
                String more = newsDetail.getData().getMore();
                if (more!=null){
                    //去服务器上拿更多数据，在"more"字段，代表一个新的url
                    //和这里解析所有数据initData()的url是不同的
                    //所以重新写API进行到服务器拿数据
                    loadMoreDataFromServer(more);

                }else{
                    Toast.makeText(mActivity,"没有更多数据了，休息一会",Toast.LENGTH_LONG).show();
                    lv_newsdetailpage_newslist.onLoadMoreComplete();
                }
            }
        });

        //---------------设item的点击事件，查看新闻详情
        lv_newsdetailpage_newslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position>=2) {
                    //----------------点击跳到下个Activity,
                    // 并携带要显示的html网页的url过去，让webView控件显示
                    Intent intent = new Intent(mActivity, ShowNewsActivity.class);

                    //应该是从数据列表中拿数据。而不是从newDetail这个java bean中，因为newsDetail中可能因为触底加载更多而改变了bean里的内容
                    String url = listDataSet.get(position-2).getUrl();
                    intent.putExtra("url", url);

                    mActivity.startActivity(intent);

                    //----------在这里记录下用户点击看过的新闻。记录当前news的一个id

                    // readlist.add(id1);
                    //35311,35312 35313
                    //String read="35311,35312,35313,35312,  也要防止重复增加id
                    NewsDetail.DataBean.NewsBean newsBean = listDataSet.get(position-2);
                    int newsid = newsBean.getId();

                    String readlist = sp.getString("readlist", "");
                    boolean contains = readlist.contains(newsid + "");
                    if (!contains){

                        //将之前没有保存过的保存到sp，
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("readlist",newsid+",");
                        boolean commit = edit.commit();

                        //给点击的item 改变颜色
                        //通过传进来的view,代表当前点击item的填充的view
                        TextView tv_newsdetailListview_title = (TextView) view.findViewById(R.id.tv_newsdetailListview_title);

                        tv_newsdetailListview_title.setTextColor(Color.GRAY);

                    }



                }
            }
        });

        //---------------------给listview增加头条新闻图片为header
        header = View.inflate(mActivity, R.layout.item_newdetaillistview_header, null);

        //TopNews的图片
        vp_newsdetailpage_topnews = (ViewPager) header.findViewById(R.id.vp_newsdetailpage_topnews);

        tv_newsdetail_topnewsTitle = (TextView)  header.findViewById(R.id.tv_newsdetail_topnewsTitle);
        //indicator设置所在的viewpager,
        indicator_topnews = (CirclePageIndicator) header.findViewById(R.id.indicator_topnews);

        //把整个header的view设置listview列表的第一个Header item
        lv_newsdetailpage_newslist.addHeaderView(header);

        return inflate;
        //在拿到数据后，给它们俩设置Adapter
    }

    private void loadMoreDataFromServer(String more) {
        String moreUrl = Constant.SERVER_ADDR+more;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

            //去网上拿数据
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                //成功拿到新的数据后，将json数据进行Gson解析
                Gson gson = new Gson();
                newsDetail = gson.fromJson(responseInfo.result, NewsDetail.class);

                List<NewsDetail.DataBean.NewsBean> newsList2 = newsDetail.getData().getNews();
                //将新拿到的数据列表加到listview要显示数据的原来的列表后面
                listDataSet.addAll(newsList2);
                //让listview的适配器更新数据列表
                myNewsListAdapter.notifyDataSetChanged();

                //不管是否成功从服务器上捞到数据，都要将footer隐藏起来
                lv_newsdetailpage_newslist.onLoadMoreComplete();

                Toast.makeText(mActivity, "连接成功，已经拿到新数据", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(HttpException e, String s) {

                //不管是否成功从服务器上捞到数据，都要将footer隐藏起来
                lv_newsdetailpage_newslist.onLoadMoreComplete();

                Toast.makeText(mActivity, "连接失败，稍后再试", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void initData(){

        //解析传进来的成员变量类中的url对应的数据
        //Constant.SERVER_ADD = http://10.0.2.2:8080/zhbj
        String url = Constant.SERVER_ADDR+newsDetailInfo.url;

        String newsDetailjson = SharedPrefUtils.getJsonFromCache(url, mActivity);
        if(newsDetailjson.isEmpty()){
            //到服务器拿数据，并将json数据进行Gson解析到bean中
            getDataFromServer(url);
        }else{
            //从缓存中拿数据
            parseJsonFromString(newsDetailjson);
            //Toast.makeText(mActivity, "缓存数据", Toast.LENGTH_SHORT).show();
        }

        /*Gson gson = new Gson();
        gson.fromJson()*/
    }

    public void refreshNewsData() {

        //拿到传进来的成员变量类中的url对应的数据
        //Constant.SERVER_ADD = http://10.0.2.2:8080/zhbj
        String url = Constant.SERVER_ADDR + newsDetailInfo.url;
       //到服务器拿数据，并将json数据进行Gson解析到bean中
        getDataFromServer(url);

    }


    public void getDataFromServer(String urlparam){

        final String url = urlparam;
        //使用开源框架xUtils去网上拿数据
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //当请求网络数据成功

                //1、listview下拉刷新完成后。回到初始状态
                lv_newsdetailpage_newslist.onRefreshComplete();

                //2、成功拿到数据后，就将数据保存到SP中作为缓存
                SharedPrefUtils.saveJsonToCache(url,responseInfo.result,mActivity);

                //3、解析拿到的json数据
                Log.i(TAG,responseInfo.result);
                parseJsonFromString(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

                //请求网络数据失败
                //listview回到初始状态
                lv_newsdetailpage_newslist.onRefreshComplete();

                Log.i(TAG,s);
                Toast.makeText(mActivity,"加载失败，请稍后再试",Toast.LENGTH_LONG).show();
            }
        });

    }

    public  void parseJsonFromString(String result){

        //Gson解析：  将Json数据 和 java bean的字节码文件传进去
        Gson gson = new Gson();
        newsDetail = gson.fromJson(result, NewsDetail.class);

        //将拿到的bean中的新闻数据放入要显示的所有新闻条目列表中（注意：不包含topnews）
        listDataSet = newsDetail.getData().getNews();

        myNewsListAdapter = new MyNewsListAdapter();

        //将解析完的数据给vp和lv设置Adapter适配器
        lv_newsdetailpage_newslist.setAdapter(myNewsListAdapter);
        vp_newsdetailpage_topnews.setAdapter(new MyTopNewsViewPagerAdapter());


        //这个必须在ViewPager设置完Adapter之后才能设置，否则会报异常
        indicator_topnews.setViewPager(vp_newsdetailpage_topnews);


        //每个TopNews的viewpager改变page时，改变Textview标题
        vp_newsdetailpage_topnews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //改变每个page中图片对应的title
                tv_newsdetail_topnewsTitle.setText(newsDetail.getData().getTopnews().get(position).getTitle());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    class MyTopNewsViewPagerAdapter extends PagerAdapter{


       BitmapUtils bitmapUtils;

        public MyTopNewsViewPagerAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return newsDetail.getData().getTopnews().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;//false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
           /* 使用TextView测试看能否正常显示
           TextView textView = new TextView(mActivity);
            textView.setText("TopNews"+position);*/

       //--------------------------显示图片(将bean中的成员变量改为Public)
           //1、放图片的控件
            ImageView imageView = new ImageView(mActivity);
            //把图片裁剪为合适适合父控件的大小。（可能不会等比例）
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //2、图片的url
            String topimageurl = newsDetail.getData().getTopnews().get(position).getTopimage();
            //3、使用BitmapUtils将图片对应的url显示出来
            bitmapUtils.display(imageView,topimageurl);

            container.addView(imageView);
            return imageView;//super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

           container.removeView((View) object);
            //super.destroyItem(container, position, object);
        }
    }
    class MyNewsListAdapter extends BaseAdapter{

        //点击具体的每条新闻的item，这里会自动缓存，在没有网络的时候能正常显示
        //图片能正常显示，是因为BitmapUtils已经帮我们缓存起来了
        //点击进去看到Webview,正常显示，因为浏览器内核会自动缓存
        BitmapUtils bitmapUtils;

        public MyNewsListAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {

            return listDataSet.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //使用TextView测试看能否正常显示
            /*TextView tv = new TextView(mActivity);
            tv.setText(newsDetail.getData().getNews().get(position).getTitle());*/

            //填充的布局
            View inflate = View.inflate(mActivity, R.layout.item_listview_newsdetail, null);
            ImageView iv_NewsdetailListview_img = (ImageView) inflate.findViewById(R.id.iv_NewsdetailListview_img);
            TextView tv_newsdetailListview_title = (TextView) inflate.findViewById(R.id.tv_newsdetailListview_title);
            TextView tv_newsdetailListview_pubtime = (TextView) inflate.findViewById(R.id.tv_newsdetailListview_pubtime);
            //显示数据
            bitmapUtils.display(iv_NewsdetailListview_img,listDataSet.get(position).getListimage());
            tv_newsdetailListview_title.setText(listDataSet.get(position).getTitle());
            tv_newsdetailListview_pubtime.setText(listDataSet.get(position).getPubdate());



            //对比SP中保存的将之前点击过的item新闻 改变颜色
            String readlist = sp.getString("readlist", "");
            //判断一下这id是否在config里，如果已经包含了，就说明这个新闻已经被user 读过 了。
            boolean contains = readlist.contains(listDataSet.get(position).getId() + "");
            if (contains){
                tv_newsdetailListview_title.setTextColor(Color.GRAY);
            }



            //返回布局
            return inflate;
        }
    }
}
