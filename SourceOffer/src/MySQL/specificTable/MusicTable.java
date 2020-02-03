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

public class MusicTable extends Table {
  public static List<Music> getMusicSet() {
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    List<Music> musicSet = null;
    try {
      tran = session.beginTransaction();
      musicSet = session.createQuery("FROM Music").list();
    } catch (HibernateException e) {
      System.out.println("getMusicSet");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return musicSet;
  }

  public static long getMusicCount() {
    long count = 0;

    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    String hql = "select count(*) from Music m";
    try {
      tran = session.beginTransaction();
      count = (long) session.createQuery(hql).uniqueResult();
    } catch (HibernateException e) {
      System.out.println("getMusicCount");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return count;
  }

  public static List<Music> getMusic(String musicTitle) {
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    List<Music> musicSet = new ArrayList<Music>();
    try {
      tran = session.beginTransaction();
      List<Music> allmusic = session.createQuery("FROM Music").list();
      Music tmp = null;
      for (Iterator<Music> iterator = allmusic.iterator(); iterator.hasNext();) {
        tmp = (Music) iterator.next();
        if (tmp.getTitle().equals(musicTitle)) {
          musicSet.add(tmp);
        }
      }
    } catch (HibernateException e) {
      System.out.println("getmusic");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return musicSet;
  }

  public static List<Music> fuzzySearchByTitle(String key) {
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;

    List<Music> musicSet = null;
    String sql = "from Music as m where m.title like '%" + key + "%'";

    try {
      tran = session.beginTransaction();
      Query query = session.createQuery(sql);
      musicSet = query.list();
    } catch (HibernateException e) {
      System.out.println("MusicfuzzySearch");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return musicSet;
  }

  public static boolean addMusic(Music music) {
    boolean result = true;
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    try {
      tran = session.beginTransaction();
      session.save(music);
      session.getTransaction().commit();
    } catch (HibernateException e) {
      System.out.println("addMusic");
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

  public static boolean deleteMusic(Music music) {
    boolean result = true;
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    try {
      tran = session.beginTransaction();
      session.delete(music);
      session.getTransaction().commit();
    } catch (HibernateException e) {
      System.out.println("deleteMusic(music)");
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

  public static boolean deletemusic(String musicTitle) {
    boolean result = true;
    List<Music> musicSet = getMusic(musicTitle);
    for (Music tmp : musicSet) {
      if (!deleteMusic(tmp)) {
        result = false;
        break;
      }
    }
    return result;
  }

  public static boolean isExist(String musicTitle) {
    if (getMusic(musicTitle).size() == 0) {
      return false;
    } else {
      return true;
    }
  }

}
