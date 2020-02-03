package cn.hufeifei.mediaplayer.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.hufeifei.mediaplayer.fragment.main.LocalMusicFragment;
import cn.hufeifei.mediaplayer.fragment.main.LocalVideoFragment;
import hitamigos.sourceget.R;
@ContentView(R.layout.activity_main1)
public class MainActivity extends Activity {
    /**
     * 单选按钮组
     */
    @ViewInject(R.id.radioGroup)
    private RadioGroup radioGroup;


    /**
     * 子页面集合
     */
    private ArrayList<Fragment> fragments;
    public void Jump(){
        Intent intent =new Intent();
        intent = new Intent(this,hitamigos.sourceget.MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用xUtils进行view注入
        x.view().inject(this);
        initFragments();
        radioGroup.check(R.id.radioButton_localVideo);
        setTheme(android.R.style.Theme_Light);
        ImageButton btn = (ImageButton) findViewById(R.id.action_up_btn);
        btn .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //你要执行的代码
                Jump();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 初始化子页面
     */
    private void initFragments() {
        fragments = new ArrayList<>(2);
        fragments.add(new LocalVideoFragment());
        fragments.add(new LocalMusicFragment());
    }


    /**
     * 页面选项改变，更换不同的fragment
     *
     * @param group     单选按钮组
     * @param checkedId 点选按钮的id
     */
    @Event(value = R.id.radioGroup, type = RadioGroup.OnCheckedChangeListener.class)
    private void onCheckedChange(RadioGroup group, int checkedId) {
        int index = 0;
        switch (checkedId) {
            case R.id.radioButton_localVideo:
                index = 0;
                break;
            case R.id.radioButton_localMusic:
                index = 1;
                break;

        }
        //设置相应的fragment
        getFragmentManager().beginTransaction().replace(R.id.content, fragments.get(index)).commit();
    }

}
