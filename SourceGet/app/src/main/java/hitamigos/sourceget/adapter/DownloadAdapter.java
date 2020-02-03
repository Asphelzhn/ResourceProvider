/*
 * Copyright (c) 2016.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos.sourceget.adapter;

/**
 * Created by kuangmeng on 2016/12/19.
 */

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class DownloadAdapter extends PagerAdapter {
    List<View> views;
    String[] tabTitle = {"正在下载","已下载"};
    public DownloadAdapter(List<View> views) {

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
