package Control;

import java.text.SimpleDateFormat;
import assistant.BeijingTime;

public class Timer {

  public static void main(String args[]) throws InterruptedException {
    Crawl crawl = new Crawl();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateStr = null;
    int hour = 0;

    while (true) {
      Thread.sleep(30 * 60 * 1000);
      dateStr = sdf.format(BeijingTime.getWebsiteDatetime());
      hour = Integer.parseInt(dateStr.substring(11, 13));
      if (hour >= 0 && hour < 1) {
        crawl.crawlMusic();
        crawl.crawlVideo();
      }
    }

  }

}
