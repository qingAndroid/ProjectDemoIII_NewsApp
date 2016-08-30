package com.example.fourth.projectdemoiii_newsapp.menupage;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourth.projectdemoiii_newsapp.R;
import com.example.fourth.projectdemoiii_newsapp.bean.Categories;
import com.example.fourth.projectdemoiii_newsapp.bean.PictureNews;
import com.example.fourth.projectdemoiii_newsapp.constant.Constant;
import com.example.fourth.projectdemoiii_newsapp.uitls.MyBitmapUtils;
import com.example.fourth.projectdemoiii_newsapp.uitls.SharedPrefUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by Administrator on 2016/8/24.
 */
public class PictureMenuPage extends BaseMenuPage {

    private GridView gv_picturemenupage_content;
    private ListView lv_picturemenupage_content;
    private PictureNews pictureNews;

    public PictureMenuPage(Activity mActivity, Categories.MenuDataInfo menuDataInfo) {
        super(mActivity, menuDataInfo);
    }

    //初始化时，将json数据传进来，可以拿来显示
    @Override
    public View initView() {

       /* TextView textView = new TextView(mActivity);
        textView.setText(menuDataInfo.title);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLUE);
        return textView;*/

        View inflate = View.inflate(mActivity, R.layout.picture_menupage, null);
        lv_picturemenupage_content = (ListView) inflate.findViewById(R.id.lv_picturemenupage_content);
        gv_picturemenupage_content = (GridView) inflate.findViewById(R.id.gv_picturemenupage_content);

        return inflate;
    }


    @Override
    public void initData() {
        //在NewsPage的changeNewsPageContent()中调用
       final String url  =Constant.SERVER_ADDR + "/photos/photos_1.json";
        String jsonFromCache = SharedPrefUtils.getJsonFromCache(url, mActivity);
        if(jsonFromCache.isEmpty()){

            getDataFromServer(url);
        }else{
            parseJsonString(jsonFromCache);
        }
    }

    private void getDataFromServer(final String url) {
        //使用xutils开源框架从服务器上拿数据 、解析json数据、显示到组图
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET,url ,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        //成功拿到数据，就去解析
                        Toast.makeText(mActivity, "加载成功", Toast.LENGTH_SHORT).show();

                        //从服务器上拿到数据后，就保存到SP缓存中
                        SharedPrefUtils.saveJsonToCache(url,responseInfo.result,mActivity);

                        parseJsonString(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                        Toast.makeText(mActivity, "加载失败，请稍后重试", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void parseJsonString(String result){

        Gson gson = new Gson();
        pictureNews = gson.fromJson(result, PictureNews.class);

        //解析完数据后，给listview 和 gridview设置适配器

        MyPictureListAdapter myPictureListAdapter = new MyPictureListAdapter();
        lv_picturemenupage_content.setAdapter(myPictureListAdapter);
        gv_picturemenupage_content.setAdapter(myPictureListAdapter);
    }

    boolean flag = true;
    public void changeUI(){
        //切换显示的行数，让外界调用（NewsPage）
        //两种显示方式都是本来就存在的，只是隐藏某一个而已
        if (flag) {
            lv_picturemenupage_content.setVisibility(View.INVISIBLE);
            gv_picturemenupage_content.setVisibility(View.VISIBLE);
            flag=false;
        }else{
            lv_picturemenupage_content.setVisibility(View.VISIBLE);
            gv_picturemenupage_content.setVisibility(View.INVISIBLE);
            flag=true;
        }
    }

    class MyPictureListAdapter extends BaseAdapter{

        //BitmapUtils bitmapUtils;

        //使用自定义的Utils来 达到三级缓存的目的
        MyBitmapUtils myBitmapUtils;

        public MyPictureListAdapter() {
            //BitmapUtils = new BitmapUtils(mActivity);
            myBitmapUtils = new MyBitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return pictureNews.getData().getNews().size();
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

            //填充xml布局
            View inflate = View.inflate(mActivity, R.layout.item_listview_picturenews, null);
            ImageView iv_listviewpicturenews_img = (ImageView) inflate.findViewById(R.id.iv_listviewpicturenews_img);
            TextView tv_newsdetailListview_title = (TextView) inflate.findViewById(R.id.tv_listviewpicturenews_title);

            //显示数据
            PictureNews.DataBean.NewsBean newsBean = pictureNews.getData().getNews().get(position);
            String title = newsBean.getTitle();
            String listimageurl = newsBean.getListimage();
//---
            //bitmapUtils.display(iv_listviewpicturenews_img,listimageurl);
            myBitmapUtils.display(iv_listviewpicturenews_img,listimageurl);

            tv_newsdetailListview_title.setText(title);

            return inflate;
        }
    }
}
