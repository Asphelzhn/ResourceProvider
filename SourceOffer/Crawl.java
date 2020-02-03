package Control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import MySQL.hibernate.Video;
import MySQL.specificTable.VideoTable;
import crawler.MusicCrawler;
import crawler.VideoCrawler;

public class Crawl extends ActionSupport implements ServletRequestAware, ServletResponseAware {

  private static final long serialVersionUID = 1L;

  private HttpServletRequest request;
  private HttpServletResponse response;

  private String musicTitle;
  private String videoTitle;
  private String key;

  public void setServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public void setServletResponse(HttpServletResponse response) {
    this.response = response;
  }

  public String crawlMusic() {
    try {
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      System.out.print("music");
      MusicCrawler musicCrawler = new MusicCrawler();
      Thread musicThread = new Thread(musicCrawler);
      musicThread.start();

      byte[] jsonBytes = new String("music thread build success\n").getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "true";
  }

  public String crawlVideo() {
    try {
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      System.out.print("video");
      VideoCrawler videoCrawler = new VideoCrawler();
      Thread videoThread = new Thread(videoCrawler);
      videoThread.start();
      
      byte[] jsonBytes = new String("video thread build success\n").getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "true";
  }

  public String crawlAll() {
    crawlMusic();
    crawlVideo();
    return "true";
  }

  public static void main(String args[]) {
//    Crawl c = new Crawl();
//    c.crawlVideo();   
  }
}
