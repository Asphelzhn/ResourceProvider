package crawler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import MySQL.hibernate.Music;
import MySQL.specificTable.MusicTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicCrawler implements Runnable {
  /**
   * get 方法
   * 
   * @param url
   * @return
   */
  public static Map<String, String> doGet(String url) {
    Map<String, String> result = new LinkedHashMap<String, String>();
    try {
      CloseableHttpClient httpclient = HttpClients.createDefault();
      HttpGet httpGet = new HttpGet(url);
      CloseableHttpResponse response = httpclient.execute(httpGet);
      try {

        int httpStatus = response.getStatusLine().getStatusCode();
        result.put("HttpStatus", new Integer(httpStatus).toString());
        if (response != null && httpStatus == HttpStatus.SC_OK) {
          HttpEntity entity = response.getEntity();
          result.put("content", readResponse(entity, "utf-8"));
        } else {
          result.put("content", null);
        }

      } finally {
        httpclient.close();
        response.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * stream读取内容，可以传入字符格式
   * 
   * @param resEntity
   * @param charset
   * @return
   */
  private static String readResponse(HttpEntity resEntity, String charset) {
    StringBuffer res = new StringBuffer();
    BufferedReader reader = null;
    try {
      if (resEntity == null) {
        return null;
      }
      reader = new BufferedReader(new InputStreamReader(resEntity.getContent(), charset));
      String line = null;

      while ((line = reader.readLine()) != null) {
        res.append(line);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
      }
    }
    return res.toString();
  }

  public static String regexString(String targetStr, String patternStr) {
    // 获取Pattern对象
    Pattern pattern = Pattern.compile(patternStr);
    // 定义一个matcher用来做匹配
    Matcher matcher = pattern.matcher(targetStr);
    if (matcher.find()) {
      return matcher.group(0);
    }
    return null;
  }

  public static void execute() {
    int count = 1;
    int _404_count = 0;
    final String basedURL = "http://home.9ku.com/share/danqu.php?id=";
    final String titleRegex = "<title>.*</title>";
    final String linkRegex = "<textarea.*http:.*.mp3";
    Map<String, String> infoSet = null;
    String title = null;
    String link = null;
    String type = null;
    while (count > 0) {
      infoSet = doGet(basedURL + count);
      System.out.println("id="+count);
      if (!infoSet.get("HttpStatus").equals("200")) {
        count++;
        _404_count++;
        if (_404_count >= 300) {
          break;
        }
        continue;
      } else {
        _404_count = 0;
      }
      title = regexString(infoSet.get("content"), titleRegex);
      link = regexString(infoSet.get("content"), linkRegex);
      if (title != null) {
        title = title.substring(7, title.length() - 1).split("歌曲链接")[0];

        if (link != null && !MusicTable.isExist(title)) {
          link = link.split(">")[1];
          type = link.split("\\.")[link.split("\\.").length - 1];

          if (title != null && link != null && type != null) {
            Music music = new Music();
            music.setTitle(title);
            music.setLink(link);
            music.setType(type);
            MusicTable.addMusic(music);
          }
        }
      }
      count++;
    }
  }

  @Override
  public void run() {
    MusicCrawler.execute();
  }

  public static void main(String args[]) {
    MusicCrawler.execute();
  }
}
