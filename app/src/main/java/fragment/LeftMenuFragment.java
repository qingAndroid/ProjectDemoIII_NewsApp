package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fourth.projectdemoiii_newsapp.HomeActivity;
import com.example.fourth.projectdemoiii_newsapp.R;
import com.example.fourth.projectdemoiii_newsapp.bean.Categories;
import com.example.fourth.projectdemoiii_newsapp.bean.MenuTitle;
import com.example.fourth.projectdemoiii_newsapp.page.NewsPage;


/**
 * Created by Administrator on 2016/8/23.
 */

//一、联网拿到侧边栏的数据 （使用xutils的jar），再json解析  (正常json解析或者使用Gson的jar）

//二、侧边栏的listview显示效果

//早上：一、实现了通过侧边栏的menu去切换newsPage里中间不同的menuPage。（以homeactivity为中介）

    //二、NewsMenuPage里引入ViewPager和ViewPagerIndicator(开源框架)实现



public class LeftMenuFragment extends Fragment {

    private MenuTitle titles;//listview要显示的所有数据的两种bean
    private Categories categories;//存放Gson解析json数据的bean
    private ListView lv_fragmentLeftMenu_menu;
    private MyLeftMenuAdapter myLeftMenuAdapter;//便于刷新数据集
    private int currentposition;
    private HomeActivity homeactivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeactivity = (HomeActivity) getActivity();
        View inflate = View.inflate(getActivity(), R.layout.fragment_leftmenu, null);
        lv_fragmentLeftMenu_menu = (ListView) inflate.findViewById(R.id.lv_fragmentLeftMenu_menu);

        //给listview中的item设置点击事件
        lv_fragmentLeftMenu_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记住当前点击的位置position，
                currentposition = position;
                //刷新数据集：会在adapter中改每个item，修改点击后的item的颜色
                myLeftMenuAdapter.notifyDataSetChanged();

                //在这里修改newspage中的View
                //调用HomeActivity的API、ContentFragment的API来获得NewsPage，
                ContentFragment contentFragment = homeactivity.getContentFragment();
                NewsPage newsPage = contentFragment.getNewsPage();
                if (newsPage!=null){
                    newsPage.changeNewsPageContent(position);
                }


                //每次点击item，就将侧边栏收起
                // homeactivity.toggleSlidingMenu();
                homeactivity.toggle();



            }
        });

        return inflate;//super.onCreateView(inflater, container, savedInstanceState);
    }

    //API： 两种方式将从json解析完的数据给到listview显示出来

    public void setMenuData(MenuTitle menuTitle){
        //--------1、使用正常方式解析Json
         //1、拿到listview要显示的数据：将传进来的MenuTitle（bean）赋值给成员变量MenuTitle
        titles = menuTitle;
        //2、给listview设置适配器
        lv_fragmentLeftMenu_menu.setAdapter(new MyLeftMenuAdapter());
    }

    public void setMenuData(Categories categories){
        //--------2、使用开源框架Gson解析Json数据
        //1、拿到数据
        this.categories=categories;

        //2、给listview设置适配器
        myLeftMenuAdapter = new MyLeftMenuAdapter();
        lv_fragmentLeftMenu_menu.setAdapter(myLeftMenuAdapter);
    }

    class MyLeftMenuAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 4;
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
         //1、拿到正常 方式去解析json 的结果bean，并显示
            //            TextView textView = new TextView(getActivity());
            //            //从titles中拿出所有的成员变量，给到textview显示
            //            switch (position){
            //                case 0:
            //                    textView.setText(titles.menutitle1);
            //                    break;
            //                case  1:
            //                    textView.setText(titles.menutitle2);
            //
            //                    break;
            //                case  2:
            //                    textView.setText(titles.menutitle3);
            //                    break;
            //                case  3:
            //                    textView.setText(titles.menutitle4);
            //                    break;
            //            }
            //            return textView;

            Categories.MenuDataInfo menuDataInfo = categories.data.get(position);
            //2、拿到用Gson解析json的结果bean
            View inflate = View.inflate(getActivity(), R.layout.item_menulist, null);
            TextView tv_itemmenulist_menutitle = (TextView) inflate.findViewById(R.id.tv_itemmenulist_menutitle);

            //将bean里的数据放到每个item中的textview中显示
            tv_itemmenulist_menutitle.setText(menuDataInfo.title);

            //-----------优化侧边栏显示的效果（颜色、图片）,选择器selector
            //设置当前点击的item为红色，没有点击为白色（首先要在item点击监听处记住当前点击的是哪一个item）
            if(currentposition!=position){
                //textview和radiogroup不同，不会只将其余未点击的自动设置。
                tv_itemmenulist_menutitle.setEnabled(false);
            }else{
                tv_itemmenulist_menutitle.setEnabled(true);
            }

            return inflate;
        }
    }
}
