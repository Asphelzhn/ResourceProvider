package Control;

import crawler.MusicCrawler;
import crawler.VideoCrawler;

public class Crawl {

  public String crawlMusic() {
    System.out.print("music");
    MusicCrawler musicCrawler = new MusicCrawler();
    Thread musicThread = new Thread(musicCrawler);
    musicThread.start();
    return "true";
  }

  public String crawlVideo() {

    System.out.print("video");
    VideoCrawler videoCrawler = new VideoCrawler();
    Thread videoThread = new Thread(videoCrawler);
    videoThread.start();
    return "true";
  }

  public String crawlAll() {
    crawlMusic();
    crawlVideo();
    return "true";
  }

  public static void main(String args[]) {
     Crawl c = new Crawl();
     c.crawlMusic();
  }
}
