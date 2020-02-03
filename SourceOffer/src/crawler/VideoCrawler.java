package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import MySQL.hibernate.Video;
import MySQL.specificTable.MusicTable;
import MySQL.specificTable.VideoTable;

public class VideoCrawler implements Runnable {

  public static final String DB_URL =
      "jdbc:mysql://localhost:3306/media?useUnicode=true&characterEncoding=utf8&&useSSL=false";
  public static final String USER = "root";
  public static final String PASS = "123456789";

  /**
   * 判断是否是2015年的电影列表页面
   * 
   * @param url 待检查URL
   * @return 状态
   */
  public static boolean checkUrl(String url) {
    Pattern pattern = Pattern.compile("http://www.80s.la/movie/list/-2015----p\\d*");
    Matcher matcher = pattern.matcher(url);
    if (matcher.find())
      return true; // 2015年的列表
    else
      return false;
  }

  /**
   * 判断页面是否是电影详情页面
   * 
   * @param url 页面链接
   * @return 状态
   */
  public static boolean isMoviePage(String url) {
    Pattern pattern = Pattern.compile("http://www.80s.la/movie/\\d+");
    Matcher matcher = pattern.matcher(url);
    if (matcher.find())
      return true; // 电影页面
    else
      return false;
  }

  /**
   * 抓取一个网站所有可以抓取的网页链接，在思路上使用了广度优先算法 对未遍历过的新链接不断发起GET请求， 一直到遍历完整个集合都没能发现新的链接 则表示不能发现新的链接了，任务结束
   * 
   * 对一个链接发起请求时，对该网页用正则查找我们所需要的视频链接，找到后存入集合videoLinkMap
   * 
   * @param oldLinkHost 域名，如：http://www.zifangsky.cn
   * @param oldMap 待遍历的链接集合
   * 
   * @return 返回所有抓取到的视频下载链接集合
   */
  private static Map<String, String> crawlLinks(String oldLinkHost, Map<String, Boolean> oldMap) {
    Map<String, Boolean> newMap = new LinkedHashMap<String, Boolean>(); // 每次循环获取到的新链接
    Map<String, String> videoLinkMap = new LinkedHashMap<String, String>(); // 视频下载链接
    String oldLink = "";

    for (Map.Entry<String, Boolean> mapping : oldMap.entrySet()) {
      // System.out.println("link:" + mapping.getKey() + "--------check:"
      // + mapping.getValue());
      // 如果没有被遍历过
      if (!mapping.getValue()) {
        oldLink = mapping.getKey();
        // 发起GET请求
        try {
          URL url = new URL(oldLink);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("GET");
          connection.setConnectTimeout(2500);
          connection.setReadTimeout(25000);

          if (connection.getResponseCode() == 200) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            Pattern pattern = null;
            Matcher matcher = null;
            // 电影详情页面，取出其中的视频下载链接，不继续深入抓取其他页面
            if (isMoviePage(oldLink)) {
              boolean checkTitle = false;
              String title = "";
              while ((line = reader.readLine()) != null) {
                // 取出页面中的视频标题
                if (!checkTitle) {
                  pattern = Pattern.compile("([^\\s]+).*?</title>");
                  matcher = pattern.matcher(line);
                  if (matcher.find()) {
                    title = matcher.group(1);
                    checkTitle = true;
                    continue;
                  }
                }
                // 取出页面中的视频下载链接
                pattern = Pattern.compile("(thunder:[^\"]+).*thunder[rR]es[tT]itle=\"[^\"]*\"");
                matcher = pattern.matcher(line);
                if (matcher.find()) {
                  videoLinkMap.put(title, matcher.group(1));
                  System.out.print("视频名称： " + title + "  ------  视频链接：" + matcher.group(1));
                  if (!VideoTable.isExist(title)) {
                    Video video = new Video();
                    video.setTitle(title);
                    video.setLink(matcher.group(1));
                    video.setType("mp4");
                    if (VideoTable.addVideo(video)) {
                      System.out.println("\ttrue");;
                    }
                  }
                  break; // 当前页面已经检测完毕
                }
              }
            }
            // 电影列表页面
            else if (checkUrl(oldLink)) {
              while ((line = reader.readLine()) != null) {

                pattern = Pattern.compile("<a href=\"([^\"\\s]*)\"");
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                  String newLink = matcher.group(1).trim(); // 链接
                  // 判断获取到的链接是否以http开头
                  if (!newLink.startsWith("http")) {
                    if (newLink.startsWith("/"))
                      newLink = oldLinkHost + newLink;
                    else
                      newLink = oldLinkHost + "/" + newLink;
                  }
                  // 去除链接末尾的 /
                  if (newLink.endsWith("/"))
                    newLink = newLink.substring(0, newLink.length() - 1);
                  // 去重，并且丢弃其他网站的链接
                  if (!oldMap.containsKey(newLink) && !newMap.containsKey(newLink)
                      && (checkUrl(newLink) || isMoviePage(newLink))) {
                    System.out.println("temp: " + newLink);
                    newMap.put(newLink, false);
                  }
                }
              }
            }

            reader.close();
            inputStream.close();
          }
          connection.disconnect();
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        oldMap.replace(oldLink, false, true);
      }
    }
    // 有新链接，继续遍历
    if (!newMap.isEmpty()) {
      oldMap.putAll(newMap);
      videoLinkMap.putAll(crawlLinks(oldLinkHost, oldMap)); // 由于Map的特性，不会导致出现重复的键值对
    }
    return videoLinkMap;
  }

  /**
   * 将获取到的数据保存在数据库中
   * 
   * @param baseUrl 爬虫起点
   * @return null
   */
  public static void saveData(String baseUrl) {
    Map<String, Boolean> oldMap = new LinkedHashMap<String, Boolean>(); // 存储链接-是否被遍历

    Map<String, String> videoLinkMap = new LinkedHashMap<String, String>(); // 视频下载链接
    String oldLinkHost = ""; // host

    Pattern p = Pattern.compile("(https?://)?[^/\\s]*"); // 比如：http://www.zifangsky.cn
    Matcher m = p.matcher(baseUrl);
    if (m.find()) {
      oldLinkHost = m.group();
    }

    oldMap.put(baseUrl, false);
    videoLinkMap = crawlLinks(oldLinkHost, oldMap);
    // 遍历，然后将数据保存在数据库中
    // try {
    // Class.forName("com.mysql.jdbc.Driver").newInstance();
    // Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
    // for (Map.Entry<String, String> mapping : videoLinkMap.entrySet()) {
    // Video video = new Video();
    // video.setTitle(mapping.getKey());
    // video.setLink(mapping.getValue());
    // VideoTable.addVideo(video);
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
  }

  @Override
  public void run() {
    VideoCrawler.saveData("http://www.80s.la/movie/list/-2015----p");
  }
  
  public static void main(String args[]) {
    VideoCrawler.saveData("http://www.80s.la/movie/list/-2015----p");
  }

}
