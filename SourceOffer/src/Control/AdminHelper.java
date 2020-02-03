package Control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import MySQL.hibernate.Admin;
import MySQL.hibernate.Music;
import MySQL.specificTable.AdminTable;
import MySQL.specificTable.MusicTable;
import assistant.Encode;
import net.sf.json.JSONObject;

public class AdminHelper extends ActionSupport
    implements ServletRequestAware, ServletResponseAware {

  private static final long serialVersionUID = 1L;

  HttpServletRequest request;
  HttpServletResponse response;

  private String adminname;
  private String password;

  public String getAdminname() {
    return adminname;
  }

  public void setAdminname(String adminname) {
    this.adminname = adminname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public void setServletResponse(HttpServletResponse response) {
    this.response = response;
  }

  /**
   * 登陆
   * 
   * @param adminname
   * @param password
   * @return
   */
  public void login() {
    try {
      /*
       * 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
       * request=ServletActionContext.getRequest(); HttpServletResponse
       * response=ServletActionContext.getResponse();
       */
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");
      JSONObject json = new JSONObject();

      Admin admin = new Admin();
      admin.setAdminname(adminname);
      admin.setPassword(Encode.getMD5(password));
      if (AdminTable.vertify(admin)) {
        json.put("result", true);
      } else {
        json.put("result", false);
      }

      byte[] jsonBytes = json.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getAdminSet() {
    try {
      /*
       * 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
       * request=ServletActionContext.getRequest(); HttpServletResponse
       * response=ServletActionContext.getResponse();
       */
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      List<String> adminSet = new ArrayList<String>();
      for (Admin admin : AdminTable.getAdminSet()) {
        adminSet.add(admin.getAdminname());
      }

      byte[] jsonBytes = adminSet.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 添加新管理员
   * 
   * @param adminname
   * @param password
   * @return
   */
  public void addAdmin() {
    try {
      /*
       * 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
       * request=ServletActionContext.getRequest(); HttpServletResponse
       * response=ServletActionContext.getResponse();
       */
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");
      JSONObject json = new JSONObject();

      Admin admin = new Admin();
      admin.setAdminname(adminname);
      if (Encode.getMD5(password) == null) {
        json.put("result", false);
      } else {
        admin.setPassword(Encode.getMD5(password));
        if (AdminTable.addAdmin(admin)) {
          json.put("result", true);
        }
      }

      byte[] jsonBytes = json.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 根据管理员姓名修改密码
   * 
   * @param adminname
   * @param password
   * @return
   */
  public void changePassword() {
    try {
      /*
       * 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
       * request=ServletActionContext.getRequest(); HttpServletResponse
       * response=ServletActionContext.getResponse();
       */
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");
      JSONObject json = new JSONObject();

      Admin admin = AdminTable.getAdmin(adminname);
      if (Encode.getMD5(password) == null) {
        json.put("result", false);
      } else {
        admin.setPassword(Encode.getMD5(password));
        if (AdminTable.changePassword(admin)) {
          json.put("result", true);
        }
      }

      byte[] jsonBytes = json.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String args[]) {
    AdminHelper a = new AdminHelper();
    a.setAdminname("lyx");
    a.setPassword("882776");
    a.addAdmin();
  }

}
