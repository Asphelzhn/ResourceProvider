/*
 * Copyright (c) 2016.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos.sourceget;

import android.app.Application;
import android.content.Context;

import com.aspsine.multithreaddownload.demo.CrashHandler;
import com.gewaradown.DownloadConfiguration;
import com.gewaradown.DownloadManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.xutils.x;

import java.io.File;

import cn.hufeifei.mediaplayer.service.MusicPlayService;
public class SourceGet extends Application {
    private static Context sContext;
    public MusicPlayService getMusicService() {
        return musicService;
    }

    public void setMusicService(MusicPlayService musicService) {
        this.musicService = musicService;
    }

    private MusicPlayService musicService;


    public boolean isMusicPlayed() {
        return isMusicPlayed;
    }

    public void setMusicPlayed(boolean musicPlayed) {
        isMusicPlayed = musicPlayed;
    }

    //用户判断视频播放之前是否播放音乐了，
    // 如果播放了音乐，则恢复之前播放的音乐
    private boolean isMusicPlayed;
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
        sContext = getApplicationContext();
        initDownloader();
        CrashHandler.getInstance(sContext);
        x.Ext.init(this);
        // 允许直接访问文件
    }
    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
    }
    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }

    public static Context getContext() {
        return sContext;
    }
}
