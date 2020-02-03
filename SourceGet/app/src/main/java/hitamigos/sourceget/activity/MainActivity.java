package hitamigos.sourceget.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import hitamigos.sourceget.R;
import krelve.app.kuaihu.db.CacheDbHelper;
import krelve.app.kuaihu.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    private FrameLayout fl_content;
    private SwipeRefreshLayout sr;
    private long firstTime;
    private String curId;
    private CacheDbHelper dbHelper;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new CacheDbHelper(this, 1);
        initView();
        loadLatest();
    }
    public void loadLatest() {
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
                replace(R.id.fl_content, new MainFragment(), "latest").
                commit();
        curId = "latest";
    }
    public void setCurId(String id) {
        curId = id;
    }
    private void initView() {
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
        sr.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                replaceFragment();
                sr.setRefreshing(false);
            }
        });
        fl_content = (FrameLayout) findViewById(R.id.fl_content);
    }
    public void replaceFragment() {
        if (curId.equals("latest")) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                    .replace(R.id.fl_content,
                            new MainFragment(), "latest").commit();
        } else {

        }

    }
    public void setSwipeRefreshEnable(boolean enable) {
        sr.setEnabled(enable);
    }
    public CacheDbHelper getCacheDbHelper() {
        return dbHelper;
    }
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Snackbar sb = Snackbar.make(fl_content, "再按一次退出", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark ));
                sb.show();
                firstTime = secondTime;
            } else {
                finish();
            }
        }
}
