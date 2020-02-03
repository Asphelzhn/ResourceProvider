/*
 * Copyright (c) 2016.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos.sourceget;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.aspsine.multithreaddownload.demo.ui.activity.AppListActivity;
import com.loopj.android.http.HttpGet;
import com.weebly.linzhaoqin.booksearch.HttpManagerDouban;
import com.weebly.linzhaoqin.model.Book;
import com.weebly.linzhaoqin.model.BookAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hitamigos.alonepicture;
import hitamigos.picure.util.ImageLoader;
import hitamigos.picure.util.Search;
import hitamigos.sourceget.adapter.MyPagerAdapter;
import hitamigos.sourceget.data.DBhelper;

import static hitamigos.sourceget.R.layout.result;


public class ResultActivity extends AppCompatActivity {
    private Intent intent;
    List<View> views=new ArrayList<>();
    private ListView listView=null;
    public static String message;
    public static final String EXTRA_PIC = "picture";
    public String liststr = new String();
    private GridView gridView;
    private String[] urlArray;
    private ImageLoader mImageLoader;
    private Handler UIHandler = new Handler();
    List<com.weebly.linzhaoqin.model.Book> bookList;
    String uri;
    String s;
    /*
    远程连接
     */
    private static  String processURL="http://123.206.93.251:8080/SourceOffer/getAll.action?";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        ///在Android2.2以后必须添加以下代码
        //本应用采用的Android4.0
        //设置线程的策略
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        //设置虚拟机的策略
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                //.detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        super.onCreate(savedInstanceState);
        setContentView(result);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        intent = getIntent();
        String mess = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if(mess != null){
            message = mess;
        }
        GetData(message);
//        System.out.println(liststr);
        final String[] str = liststr.split("\\,");
        for(int i =0;i<str.length;i++){
            System.out.println(str[i]);
        }
        int boundary = 0;
        for(int i =0;i<str.length;i++){
            if(str[i].equals("music")) boundary = i;
        }
        final int boun = boundary;
        /*
        视频
         */

            listView=new ListView(this);
        if(!str[1].equals("music")) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (int j = 1; j < boundary; j++) {
                if (str[j].equals("video")) j++;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("image", R.drawable.video);
                map.put("title", str[j]);
                map.put("info", str[++j]);
                list.add(map);
            }
            //生成SimpleAdapter适配器对象
            SimpleAdapter mySimpleAdapter = new SimpleAdapter(this, list,//数据源
                    R.layout.list,//ListView内部数据展示形式的布局文件
                    new String[]{"image", "title", "info"},//HashMap中的两个key值
                    new int[]{R.id.image, R.id.title, R.id.info});/*布局文件
                                                            布局文件的各组件分别映射到HashMap的各元素上，完成适配*/
            listView.setAdapter(mySimpleAdapter);
            //添加点击事件
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //获得选中项的HashMap对象
                    // Map<String,Object>  map=(Map<String,Object>)listView.getItemAtPosition(arg2);
                    String title = str[arg2 * 2 + 1];
                    String info = str[arg2 * 2 + 2];
                    String sinfo = info.substring(0, 4);
                    if (sinfo.equals("http") || sinfo.equals("HTTP")) {

                        DBhelper db = new DBhelper(ResultActivity.this, "downloadinfo.db", null, 1);
                        SQLiteDatabase mydb = db.getWritableDatabase();

                        Cursor fila = mydb.rawQuery("select * from download where link=\'" + info + "\'", null);
                        if (!fila.moveToFirst()) {  //devuelve true o false
                            ContentValues registro = new ContentValues();  //es una clase para guardar datos
                            registro.put("title", title);
                            registro.put("link", info);
                            mydb.insert("download", null, registro);
                            mydb.close();
                            Toast.makeText(getApplicationContext(),
                                    "正在下载" + title + "！", Toast.LENGTH_SHORT).show();
                            Jump();
                        } else {
                            mydb.close();
                            Toast.makeText(getApplicationContext(),
                                    "已经在下载" + title + "！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "无法下载" + title + "！",
                                Toast.LENGTH_SHORT).show();
                        String link = info;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        intent.addCategory("android.intent.category.DEFAULT");
                        startActivity(intent);
                    }

                }
            });
        }
        views.add(listView);
        /*
        音乐
         */
        listView=new ListView(this);
        if(!str[str.length-1].equals("music")) {
            List<Map<String, Object>> list1 = new ArrayList<>();
            for (int j = boundary + 1; j < str.length; j++){
                if (str[j].equals("music")) j++;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("image", R.drawable.music);
                map.put("title", str[j]);
                map.put("info", str[++j]);
                list1.add(map);
            }
            //生成SimpleAdapter适配器对象
            SimpleAdapter myAdapter = new SimpleAdapter(this, list1,//数据源
                    R.layout.list,//ListView内部数据展示形式的布局文件
                    new String[]{"image", "title", "info"},//HashMap中的两个key值
                    new int[]{R.id.image, R.id.title, R.id.info});/*布局文件
                                                            布局文件的各组件分别映射到HashMap的各元素上，完成适配*/
            listView.setAdapter(myAdapter);
            //添加点击事件
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //获得选中项的HashMap对象
                    String title = str[boun + arg2 * 2 + 1];
                    String info = str[boun + arg2 * 2 + 2];
                    System.out.println(title);
                    System.out.println(info);
                    String sinfo = info.substring(0, 4);
                    if (sinfo.equals("http") || sinfo.equals("HTTP")) {
                        DBhelper db = new DBhelper(ResultActivity.this, "downloadinfo.db", null, 1);
                        SQLiteDatabase mydb = db.getWritableDatabase();

                        Cursor fila = mydb.rawQuery("select * from download where link=\'" + info + "\'", null);
                        if (!fila.moveToFirst()) {  //devuelve true o false
                            ContentValues registro = new ContentValues();  //es una clase para guardar datos

                            registro.put("title", title);
                            registro.put("link", info);
                            mydb.insert("download", null, registro);
                            mydb.close();
                            Toast.makeText(getApplicationContext(),
                                    "正在下载" + title + "！", Toast.LENGTH_SHORT).show();
                            Jump();
                        } else {
                            mydb.close();
                            Toast.makeText(getApplicationContext(),
                                    "已经在下载" + title + "！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "无法下载" + title + "！",
                                Toast.LENGTH_SHORT).show();
                        String link = info;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        intent.addCategory("android.intent.category.DEFAULT");
                        startActivity(intent);
                    }
                }
            });
        }
        views.add(listView);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.picture, null);
        gridView = (GridView) view.findViewById(R.id.id_gridview);
        Search search = new Search(message, UIHandler);
        urlArray = search.GetUrl().toString().split("\n");
        gridView.setAdapter(new ListImgItemAdapetr(ResultActivity.this, 0, urlArray));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(ResultActivity.this, alonepicture.class);
                intent.putExtra(EXTRA_PIC,urlArray[position]);
                startActivity(intent);
            }
        });
        views.add(view);

        /*
        图书
         */

        listView=new ListView(this);
        try {
            s = URLEncoder.encode(message, "utf-8");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        uri = "https://api.douban.com/v2/book/search?q="+s+"&fields=title,author,publisher,pubdate,image,rating";
        requestAndDisplayData(uri);
        views.add(listView);

        ViewPager viewPager= (ViewPager) findViewById(R.id.id_vp);
        MyPagerAdapter myPagerAdapter=new MyPagerAdapter(views);
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout= (TabLayout) findViewById(R.id.id_tl);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }
    public void GetData(String message){
       // String result=null;
        try {
            //创建一个HttpClient对象
            HttpClient httpclient = new DefaultHttpClient();
            //远程登录URL
            processURL=processURL+"key="+message;
            //创建HttpGet对象
            HttpGet request=new HttpGet(processURL);
            //请求信息类型MIME每种响应类型的输出（普通文本、html 和 XML，json）。允许的响应类型应当匹配资源类中生成的 MIME 类型
            //资源类生成的 MIME 类型应当匹配一种可接受的 MIME 类型。如果生成的 MIME 类型和可接受的 MIME 类型不 匹配，那么将
            //生成 com.sun.jersey.api.client.UniformInterfaceException。例如，将可接受的 MIME 类型设置为 text/xml，而将
            //生成的 MIME 类型设置为 application/xml。将生成 UniformInterfaceException。
            request.addHeader("Accept","text/json");
            //获取响应的结果
            HttpResponse response =httpclient.execute(request);
            //获取HttpEntity
            HttpEntity entity=response.getEntity();
            //获取响应的结果信息
            String json = EntityUtils.toString(entity,"UTF-8");
            //JSON的解析过程
            if(json!=null){
//                JSONObject jsonObject=new JSONObject(json);
//                liststr=jsonObject.get("jsonBytes").toString();
                liststr = json;
            }else{
                liststr = "video,music";
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    public void Jump(){
        Intent in = new Intent(this, AppListActivity.class);
        startActivity(in);
       // System.out.println("前往下载页");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.result_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case R.id.download:
                Toast.makeText(ResultActivity.this, ""+"正在前往下载页！", Toast.LENGTH_SHORT).show();
                Jump();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

/*
  图片
 */

    private class ListImgItemAdapetr extends ArrayAdapter<String> {
        public ListImgItemAdapetr(Context context, int resouce, String[] datas) {
            super(ResultActivity.this, 0, datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = ResultActivity.this.getLayoutInflater().inflate(R.layout.item_fragment_list_imgs, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.id_img);
            imageView.setImageResource(R.drawable.pictures_no);
            mImageLoader.loadImage(getItem(position), imageView, true);
            return convertView;
        }
    }


    //获得数据
    public void requestAndDisplayData(String uri) {
        ResultActivity.NetworkTask task = new ResultActivity.NetworkTask();
        task.execute(uri);
    }

    //后台连网获取数据
    private class NetworkTask extends AsyncTask<String, String, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... params) {
            String content = HttpManagerDouban.getData(params[0]);
            bookList = HttpManagerDouban.bookJSONParser(content);
            return bookList;
        }

        @Override
        protected void onPostExecute(List<Book> result) {
            bookList = result;
            //展示数据
            BookAdapter adapter = new BookAdapter(ResultActivity.this, R.layout.item_book, bookList);
           // setListAdapter(adapter);
            listView.setAdapter(adapter);
            Log.d("DATA", "adapter seted");
        }
    }
}
