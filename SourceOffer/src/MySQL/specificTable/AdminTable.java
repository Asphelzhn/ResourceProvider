package MySQL.specificTable;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import MySQL.hibernate.HibernateUtil;
import MySQL.hibernate.Admin;

public class AdminTable extends Table {

  public static List<Admin> getAdminSet() {
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    List<Admin> adminSet = null;
    try {
      tran = session.beginTransaction();
      adminSet = session.createQuery("FROM Admin").list();
    } catch (HibernateException e) {
      System.out.println("getAdminSet");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return adminSet;
  }
  
  public static long getAdminCount() {
    long count = 0;

    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    String hql = "select count(*) from Admin a";
    try {
      tran = session.beginTransaction();
      count = (long) session.createQuery(hql).uniqueResult();
    } catch (HibernateException e) {
      System.out.println("getAdminCount");
      if (tran != null) {
        tran.rollback();
      }
      e.printStackTrace();
    } finally {
      HibernateUtil.closeSession();
    }
    return count;
  }

  public static Admin getAdmin(String adminname) {
    List<Admin> AdminSet = getAdminSet();
    Admin admin = null;
    if (AdminSet != null) {
      for (Admin tmp : AdminSet) {
        if (tmp.getAdminname().equals(adminname)) {
          admin = tmp;
          break;
        }
      }
    }
    return admin;
  }

  public static boolean vertify(Admin admin) {
    boolean result = false;
    Admin target = getAdmin(admin.getAdminname());
    if (target != null && admin.getPassword().equals(target.getPassword())) {
      result = true;
    }
    return result;
  }

  public static boolean addAdmin(Admin admin) {
    boolean result = true;
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    try {
      tran = session.beginTransaction();
      session.save(admin);
      session.getTransaction().commit();
    } catch (HibernateException e) {
      System.out.println("addAdmin");
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

  public static boolean changePassword(Admin admin) {
    boolean result = true;
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    try {
      tran = session.beginTransaction();
      session.update(admin);
      session.getTransaction().commit();
    } catch (HibernateException e) {
      System.out.println("changePassword");
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

  public static boolean deleteAdmin(Admin admin) {
    boolean result = true;
    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    try {
      tran = session.beginTransaction();
      session.delete(admin);
      session.getTransaction().commit();
    } catch (HibernateException e) {
      System.out.println("deleteAdmin(Admin)");
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

  public static boolean deleteAdmin(String adminname) {
    boolean result = true;
    Admin admin = getAdmin(adminname);

    Session session = HibernateUtil.currentSession();
    Transaction tran = null;
    try {
      tran = session.beginTransaction();
      session.delete(admin);
      session.getTransaction().commit();
    } catch (HibernateException e) {
      System.out.println("deleteAdmin(String)");
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
}
