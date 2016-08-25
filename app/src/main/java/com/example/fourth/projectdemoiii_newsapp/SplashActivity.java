package com.example.fourth.projectdemoiii_newsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
/*新闻客户端：客户端要求从服务器上拿数据，显示到应用的页面上，
所以首先要在服务器端安装一个servlet应用（将文件夹放在tomcat的webapps下）
      然后需要修改服务器的端口号（改为8080）*/

/*侧边栏：需要使用库 slidingMenu ，要先引入这个module作为库 */

//实现了splash页面的动画，和Guide页面里的viewpager初步使用
//实现了Guide页面里的viewPager 和 enter Button
//实现了Guide页面里的PageIndicator
//引入了SlideingMenuLibrary

//实现了HomePage的里面的viewpager里的五个Page的分离。
//实现了从服务器获取侧边栏的数据并显示在侧边栏的listview上。
public class SplashActivity extends Activity {

    private RelativeLayout rl_splashactivity_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rl_splashactivity_bg = (RelativeLayout) findViewById(R.id.rl_splashactivity_bg);

        //主要展示动画
        //当动画结束并进入GuideActivity页面
        showAnimation();
    }

    private void showAnimation(){
        //旋转 缩放  透明度  三种动画： 用组合动画做

        //传false，表示 这三种动画都使用自己的插值器；Pass false if each animation should use its own interpolator.
        AnimationSet animationSet = new AnimationSet(false);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);//设置动画时间 2秒
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);

        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        //给页面布局的背景图片设置动画
        rl_splashactivity_bg.setAnimation(animationSet);
        //开始动画
        animationSet.start();

        //给图片设置监听，当动画结束时，进入GuideActivity，并结束本页面
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //当动画开始的时候调用这个callback
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //当动画结束的时候，调用这个callback
                startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
