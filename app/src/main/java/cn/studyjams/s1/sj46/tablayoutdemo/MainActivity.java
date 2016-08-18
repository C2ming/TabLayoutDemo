package cn.studyjams.s1.sj46.tablayoutdemo;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;


public class MainActivity extends FragmentActivity {

    private FragmentAdapter mFragmentAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    //页卡标题集合
    private String[] mTitle = new String[]{
            "登陆",
            "注册",
    };

    //页卡视图集合
    private Fragment mViewPagerFragment[] = new Fragment[]{
            new LoginFragment(),
            new RegisterFragment()
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mViewPagerFragment, mTitle);

        //设置tab模式，当前为系统默认模式,可选fixed和scrollable fixed是指固定个数，scrollable是可以横行滚动的
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        //添加tab选项卡
        for (int i = 0; i < mTitle.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mFragmentAdapter.getPageTitle(i)));
        }

        //给ViewPager设置适配器(Tabs也会自动设置该适配器)
        mViewPager.setAdapter(mFragmentAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);



//        loginButton.setOnClickListener(this);
    }



}