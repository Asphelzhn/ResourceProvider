/*
 * Copyright (c) 2016.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos.picure.util;

import android.os.Handler;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by blue on 2016/7/1.
 */
public class Search {
    private ExecutorService mThreadPool;

    private LinkedList<Runnable> mTaskQueue;

    private Semaphore mSemapthoreThreadPool;

    private StringBuilder Urlbuilder;


    private ArrayList<String> mUrlList;

    private State mState = State.baidu;

    private String word = null;

    private int current = 0;

    private Handler handler;
    private static final int DEAFULT_THREAD_COUNT = 3;

    private int count = 0;

    public Search(String word, Handler handler) {

        this.word = URLEncoder.encode(word);
        init(DEAFULT_THREAD_COUNT);
        this.handler = handler;

    }

    public enum State {
        baidu, bing, sougou,
    }

    private void init(int threadCount) {
        mThreadPool = Executors.newFixedThreadPool(threadCount);//创建线程池
        Urlbuilder = new StringBuilder();
        mUrlList = new ArrayList<String>();
        mTaskQueue = new LinkedList<Runnable>();
        mSemapthoreThreadPool = new Semaphore(threadCount);
        for (int i = 0; i < 3; i++) {
            if (i == 0)
                mState = State.baidu;
            if (i == 1)
                mState = State.bing;
            if (i == 2)
                mState = State.sougou;
            addTask(buildTask());

        }

    }

    private Runnable getTask() {
        return mTaskQueue.removeLast();
    }

    private void Union(StringBuilder builder) {
        current++;
        Urlbuilder.append(builder.toString());
    }

    public static void sop(Object obj) {
      //  System.out.println(obj);
    }

    public StringBuilder GetUrl() {
        while (!mTaskQueue.isEmpty()) {
            mThreadPool.execute(getTask());
            try {
                mSemapthoreThreadPool.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mSemapthoreThreadPool.release();
        }
        while (current < 3) {
        }
        return Urlbuilder;

    }

    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
    }

    private Runnable buildTask() {
        if (mState == State.baidu) {
            return new Runnable() {
                @Override
                public void run() {
                    String finalURL = null;
                    StringBuilder builder = new StringBuilder();
                    for (int page = 0; page < 4; page++) {
                        sop("正在下载第" + page + "页面");
                        Document document = null;
                        String url = "http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word="
                                + word
                                + "&cg=star&pn="
                                + page
                                * 30
                                + "&rn=30&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm="
                                + Integer.toHexString(page * 30);
                        try {
                            document = Jsoup
                                    .connect(url)
                                    .data("query", "Java")
                                    .userAgent(
                                            "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")// 设置urer-agent
                                    // //
                                    // get();
                                    .timeout(5000).get();
                            String xmlSource = document.toString();
                            xmlSource = StringEscapeUtils.unescapeHtml(xmlSource);
                            String reg = "objURL\":\"http://.+?\\.jpg";
                            Pattern pattern = Pattern.compile(reg);
                            Matcher m = pattern.matcher(xmlSource);
                            while (m.find()) {
                                finalURL = m.group().substring(9);
                                builder.append(finalURL + "\n");
                                count++;
                                sop(finalURL);
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    Union(builder);
                }
            };
        }
        if (mState == State.bing) {
            return new Runnable() {
                @Override
                public void run() {
                    String finalURL;
                    StringBuilder builder = new StringBuilder();
                    String url = "http://cn.bing.com/images/search?q=" + word + "&qs=n&form=QBILPG&pq=%E6%89%8B%E6%9C%BA&sc=0-0&sp=-1&sk=";
                    Document document;
                    try {
                        document = Jsoup
                                .connect(url)
                                .data("query", "Java")
                                .userAgent(
                                        "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")// 设置urer-agent
                                // get();
                                .timeout(5000).get();
                        String xmlSource = document.toString();
                        xmlSource = StringEscapeUtils.unescapeHtml(xmlSource);
                        String reg = "\"http://.+?\\.jpg";
                        Pattern pattern = Pattern.compile(reg);
                        Matcher m = pattern.matcher(xmlSource);
                        while (m.find()) {
                            String temp = m.group();

                            if (temp.contains(",")) {
                                finalURL = temp.substring(temp.indexOf("imgurl"));
                                builder.append(finalURL.substring(8) + "\n");
                                count++;
                                sop(temp.indexOf("imgurl"));
                            } else {
                                builder.append(temp.substring(1) + "\n");
                                sop(temp.substring(1));
                            }
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Union(builder);
                }
            };
        }
        if (mState == State.sougou) {
            final StringBuilder builder = new StringBuilder();
            return new Runnable() {
                @Override
                public void run() {
                    String finalURL;
                    String url = "http://pic.sogou.com/pics?query=" + word + "&w=05009900&p=40030500&_asf=pic.sogou.com&_ast=1467681830&sc=index&sut=4150&sst0=1467681830162";
                    Document document;
                    try {
                        document = Jsoup
                                .connect(url)
                                .data("query", "Java")
                                .userAgent(
                                        "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")// 设置urer-agent
                                // get();
                                .timeout(5000).get();
                        String xmlSource = document.toString();
                        xmlSource = StringEscapeUtils.unescapeHtml(xmlSource);
                        String reg = "\"http://.+?\\.jpg";
                        Pattern pattern = Pattern.compile(reg);
                        Matcher m = pattern.matcher(xmlSource);
                        while (m.find()) {
                            String temp = m.group();
                            if (temp.contains("thumbUrl")) {
                                builder.append(temp.substring(temp.lastIndexOf("\"thumbUrl\":\"")).substring(12));
                                count++;
                             //   System.out.println(temp.substring(temp.lastIndexOf("\"thumbUrl\":\"")).substring(12));
                            } else {
                                builder.append(temp.substring(1));
                                count++;
                             //   System.out.println(temp.substring(1));

                            }
                        }
                        Union(builder);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };
        }
        return null;
    }

}
