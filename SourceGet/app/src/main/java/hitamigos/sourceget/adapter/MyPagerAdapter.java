/*
 * Copyright (c) 2016.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos.sourceget.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
    List<View> views;
    String[] tabTitle = {"视频","音乐","图片","书籍"};
    public MyPagerAdapter(List<View> views) {
        this.views = views;
    }
    @Override
    public int getCount(){

        return views.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object){

        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=views.get(position);
        container.addView(view);
        return  view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
