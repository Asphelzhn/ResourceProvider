package MySQL.specificTable;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import MySQL.hibernate.Admin;
import MySQL.hibernate.HibernateUtil;
import MySQL.hibernate.SearchRecord;

public class SearchRecordTable extends Table {
  
  public static List<SearchRecord> getSearchRecordSet(){
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    List<SearchRecord> searchRecordSet = null;
    try {
      tran = session.beginTransaction();
      searchRecordSet = session.createQuery("FROM SearchRecord").list();
    } catch (HibernateException e) {
      System.out.println("getSearchRecordSet");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return searchRecordSet;
  }
}
