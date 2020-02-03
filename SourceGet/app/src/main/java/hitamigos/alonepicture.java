/*
 * Copyright (c) 2017.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hitamigos.picure.util.ImageLoader;
import hitamigos.sourceget.R;
import hitamigos.sourceget.ResultActivity;

import static hitamigos.sourceget.R.layout.alonepicture;

/**
 * Created by kuangmeng on 2017/1/1.
 */

public class alonepicture extends AppCompatActivity {
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String url = intent.getStringExtra(ResultActivity.EXTRA_PIC);
        super.onCreate(savedInstanceState);
        setContentView(alonepicture);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(getBitmap(url));
    }

    public static Bitmap getBitmap(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}