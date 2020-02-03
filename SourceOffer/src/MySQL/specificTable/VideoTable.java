package MySQL.specificTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import MySQL.hibernate.HibernateUtil;
import MySQL.hibernate.Music;
import MySQL.hibernate.Video;

public class VideoTable extends Table {

  public static List<Video> getVideoSet() {
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    List<Video> videoSet = null;
    try {
      tran = session.beginTransaction();
      videoSet = session.createQuery("FROM Video").list();
    } catch (HibernateException e) {
      System.out.println("getVideoSet");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return videoSet;
  }

  public static long getVideoCount() {
    long count = 0;

    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    String hql = "select count(*) from Video v";
    try {
      tran = session.beginTransaction();
      count = (long) session.createQuery(hql).uniqueResult();
    } catch (HibernateException e) {
      System.out.println("getVideoCount");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return count;
  }

  public static List<Video> getVideo(String videoTitle) {
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    List<Video> videoSet = new ArrayList<Video>();
    try {
      tran = session.beginTransaction();
      List<Video> allVideo = session.createQuery("FROM Video").list();
      Video tmp = null;
      for (Iterator<Video> iterator = allVideo.iterator(); iterator.hasNext();) {
        tmp = (Video) iterator.next();
        if (tmp.getTitle().equals(videoTitle)) {
          videoSet.add(tmp);
        }
      }
    } catch (HibernateException e) {
      System.out.println("getVideo");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return videoSet;
  }

  public static List<Video> fuzzySearchByTitle(String key) {
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;

    List<Video> videoSet = null;
    String sql = "from Video as v where v.title like '%" + key + "%'";

    try {
      tran = session.beginTransaction();
      Query query = session.createQuery(sql);
      videoSet = query.list();
    } catch (HibernateException e) {
      System.out.println("VediofuzzySearch");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return videoSet;
  }

  public static boolean addVideo(Video video) {
    boolean result = true;
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    try {
      tran = session.beginTransaction();
      session.save(video);
      session.getTransaction().commit();
    } catch (HibernateException e) {
      System.out.println("addVideo");
      result = false;
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return result;
  }

  public static boolean deleteVideo(Video video) {
    boolean result = true;
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    try {
      tran = session.beginTransaction();
      session.delete(video);
      session.getTransaction().commit();
    } catch (HibernateException e) {
      System.out.println("deleteVideo(Video)");
      result = false;
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return result;
  }

  public static boolean deleteVideo(String videoTitle) {
    boolean result = true;
    List<Video> videoSet = getVideo(videoTitle);
    for (Video tmp : videoSet) {
      if (!deleteVideo(tmp)) {
        result = false;
        break;
      }
    }
    return result;
  }

  public static boolean isExist(String videoTitle) {
    if (getVideo(videoTitle).size() == 0) {
      return false;
    } else {
      return true;
    }
  }
  
}
