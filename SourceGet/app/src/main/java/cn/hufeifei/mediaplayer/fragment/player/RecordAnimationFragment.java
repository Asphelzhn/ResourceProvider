package cn.hufeifei.mediaplayer.fragment.player;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.hufeifei.mediaplayer.event.MusicServiceEvent;
import cn.hufeifei.mediaplayer.service.MusicPlayService;
import hitamigos.sourceget.R;

/**
 * 音乐播放界面中的唱片旋转子界面
 * Created by Holmofy on 2016/12/7.
 */
@ContentView(R.layout.fragment_record_animation)
public class RecordAnimationFragment extends Fragment {

    private View rootView;

    @ViewInject(R.id.music_record_img)
    private ImageView recordImg;


    private Animation inAnimation;
    private Animation outAnimation;

    private void loadAnimation() {
        inAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.music_record_in);
        outAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.music_record_out);
        outAnimation.setAnimationListener(listener);
    }

    private AnimationListener listener = new AnimationListener();

    private class AnimationListener implements Animation.AnimationListener {
        Drawable drawable;

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            recordImg.setImageDrawable(drawable);
            recordImg.startAnimation(inAnimation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = x.view().inject(this, inflater, container);
            //加载动画资源
            loadAnimation();
            //注册事件总线
            EventBus.getDefault().register(this);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        //注销事件总线
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onServiceEvent(MusicServiceEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MusicServiceEvent.ACTION_RESUME_BIND://Activity重新连上后台服务
            case MusicServiceEvent.ACTION_MUSIC_PREPARED: {
                //音乐准备好后，说明歌曲信息获取完成，此时即可获取歌曲唱片的海报图片
                MusicPlayService service = event.getService();
            }
        }
    }
}
