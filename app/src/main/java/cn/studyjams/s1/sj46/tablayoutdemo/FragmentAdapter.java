package cn.studyjams.s1.sj46.tablayoutdemo;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * Created by Administrator on 2016-07-18.
 */
public class FragmentAdapter extends FragmentPagerAdapter {


    private Fragment[] mViewPagerFragment;
    private String[] mTitle;
    public FragmentAdapter(FragmentManager fm,Fragment[] mViewPagerFragment,String[] mTitle) {
        super(fm);
        this.mViewPagerFragment = mViewPagerFragment;
        this.mTitle = mTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return mViewPagerFragment[position];
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    //页卡标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
