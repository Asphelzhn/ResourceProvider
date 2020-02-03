package Control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import MySQL.hibernate.Music;
import MySQL.hibernate.Video;
import MySQL.specificTable.MusicTable;
import MySQL.specificTable.VideoTable;
import net.sf.json.JSONObject;

/*
 * try { /* 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
 * request=ServletActionContext.getRequest(); HttpServletResponse
 * response=ServletActionContext.getResponse();
 *
 * this.response.setContentType("text/json;charset=utf-8");
 * this.response.setCharacterEncoding("UTF-8"); JSONObject json = new JSONObject();
 * 
 * 
 * 
 * byte[] jsonBytes = json.toString().getBytes("utf-8");
 * response.setContentLength(jsonBytes.length); response.getOutputStream().write(jsonBytes);
 * response.getOutputStream().flush(); response.getOutputStream().close(); } catch (Exception e) {
 * e.printStackTrace(); }
 */


/**
 * 获取资源时，先分词，然后模糊查找
 * 
 * @author liuyx
 *
 */
public class SourceHelper extends ActionSupport
    implements ServletRequestAware, ServletResponseAware {

  private static final long serialVersionUID = 1L;

  private HttpServletRequest request;
  private HttpServletResponse response;

  private String musicTitle;
  private String videoTitle;
  private String key;

  public String getMusicTitle() {
    return musicTitle;
  }

  public void setMusicTitle(String musicTitle) {
    this.musicTitle = musicTitle;
  }

  public String getVideoTitle() {
    return videoTitle;
  }

  public void setVideoTitle(String videoTitle) {
    this.videoTitle = videoTitle;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public void setServletResponse(HttpServletResponse response) {
    this.response = response;
  }

  private ArrayList<String> split(String key) {
    ArrayList<String> resultSet = new ArrayList<String>();
    int keyLen = key.length();
    for (int i = keyLen; i >= i / 2; i--) {
      for (int j = 0; j <= keyLen - i; j++) {
        resultSet.add(key.substring(j, j + i));
      }
    }
    return resultSet;
  }

  public void getMusicCount() {
    try {
      /*
       * 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
       * request=ServletActionContext.getRequest(); HttpServletResponse
       * response=ServletActionContext.getResponse();
       */
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      /*
       * List<Map<String, String>> musicSet = new ArrayList<Map<String, String>>(); for (Music music
       * : MusicTable.getMusicSet()) { Map<String, String> tmp = new LinkedHashMap<String,
       * String>(); tmp.put(music.getTitle(), music.getLink()); musicSet.add(tmp); }
       */

      byte[] jsonBytes = new Long(MusicTable.getMusicCount()).toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getVideoCount() {

  }

  public void getMusic() {
    try {
      /*
       * 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
       * request=ServletActionContext.getRequest(); HttpServletResponse
       * response=ServletActionContext.getResponse();
       */
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      /*
       * List<Map<String, String>> musicSet = new ArrayList<Map<String, String>>(); for (Music music
       * : MusicTable.getMusicSet()) { Map<String, String> tmp = new LinkedHashMap<String,
       * String>(); tmp.put(music.getTitle(), music.getLink()); musicSet.add(tmp); }
       */

      byte[] jsonBytes = new Long(VideoTable.getVideoCount()).toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getVideo() {
    try {
      /*
       * 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
       * request=ServletActionContext.getRequest(); HttpServletResponse
       * response=ServletActionContext.getResponse();
       */
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      /*
       * List<Map<String, String>> musicSet = new ArrayList<Map<String, String>>(); for (Music music
       * : MusicTable.getMusicSet()) { Map<String, String> tmp = new LinkedHashMap<String,
       * String>(); tmp.put(music.getTitle(), music.getLink()); musicSet.add(tmp); }
       */

      List<String> videoSet = new ArrayList<String>();
      videoSet.add("video");
      for (String str : split(key)) {
        for (Video video : VideoTable.fuzzySearchByTitle(str)) {
          if (videoSet.contains(video.getLink())) {
            continue;
          }
          videoSet.add(video.getTitle());
          videoSet.add(video.getLink());
        }
      }

      byte[] jsonBytes = videoSet.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getAll() {
    try {
      /*
       * 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
       * request=ServletActionContext.getRequest(); HttpServletResponse
       * response=ServletActionContext.getResponse();
       */
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      /*
       * List<Map<String, String>> musicSet = new ArrayList<Map<String, String>>(); for (Music music
       * : MusicTable.getMusicSet()) { Map<String, String> tmp = new LinkedHashMap<String,
       * String>(); tmp.put(music.getTitle(), music.getLink()); musicSet.add(tmp); }
       */

      int count = 0;

      StringBuffer resultSet = new StringBuffer();
      resultSet.append("video");
      for (String str : split(key)) {
        for (Video video : VideoTable.fuzzySearchByTitle(str)) {
          if (resultSet.toString().contains(video.getLink())) {
            continue;
          }
          resultSet.append("," + video.getTitle());
          resultSet.append("," + video.getLink());
          count++;
          if (count > 20) {
            count = 0;
            break;
          }
        }
      }
      resultSet.append(",music");
      for (String str : split(key)) {
        for (Music music : MusicTable.fuzzySearchByTitle(str)) {
          if (resultSet.toString().contains(music.getLink())) {
            continue;
          }
          resultSet.append("," + music.getTitle());
          resultSet.append("," + music.getLink());
          count++;
          if (count > 20) {
            count = 0;
            break;
          }
        }
      }

      byte[] jsonBytes = resultSet.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getMusicSet() {
    try {
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      /*
       * List<Map<String, String>> musicSet = new ArrayList<Map<String, String>>(); for (Music music
       * : MusicTable.getMusicSet()) { Map<String, String> tmp = new LinkedHashMap<String,
       * String>(); tmp.put(music.getTitle(), music.getLink()); musicSet.add(tmp); }
       */

      List<String> musicSet = new ArrayList<String>();
      for (Music music : MusicTable.getMusicSet()) {
        musicSet.add(music.getTitle());
        musicSet.add(music.getLink());
      }

      byte[] jsonBytes = musicSet.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getVideoSet() {
    try {
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      /*
       * List<Map<String, String>> videoSet = new ArrayList<Map<String, String>>(); for (Video video
       * : VideoTable.getVideoSet()) { Map<String, String> tmp = new LinkedHashMap<String,
       * String>(); tmp.put(video.getTitle(), video.getLink()); musicSet.add(tmp); }
       */

      List<String> videoSet = new ArrayList<String>();
      for (Video video : VideoTable.getVideoSet()) {
        videoSet.add(video.getTitle());
        videoSet.add(video.getLink());
      }

      byte[] jsonBytes = videoSet.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteMusic() {
    try {
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");
      JSONObject json = new JSONObject();

      json.put("result", MusicTable.deletemusic(musicTitle));

      byte[] jsonBytes = json.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteVideo() {
    try {
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");
      JSONObject json = new JSONObject();

      json.put("result", MusicTable.deletemusic(videoTitle));

      byte[] jsonBytes = json.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
