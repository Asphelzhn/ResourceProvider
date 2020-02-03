package Control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import MySQL.hibernate.Admin;
import MySQL.hibernate.SearchRecord;
import MySQL.specificTable.AdminTable;
import MySQL.specificTable.SearchRecordTable;

public class SearchRecordHelper extends ActionSupport
    implements ServletRequestAware, ServletResponseAware {

  private static final long serialVersionUID = 1L;

  private HttpServletRequest request;
  private HttpServletResponse response;

  public void setServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public void setServletResponse(HttpServletResponse response) {
    this.response = response;
  }

  public void getSearchRecordSet() {
    try {
      /*
       * 如果不采用接口注入的方式的获取HttpServletRequest，HttpServletResponse的方式 HttpServletRequest
       * request=ServletActionContext.getRequest(); HttpServletResponse
       * response=ServletActionContext.getResponse();
       */
      this.response.setContentType("text/json;charset=utf-8");
      this.response.setCharacterEncoding("UTF-8");

      // List<Map<String,String>> searchRecordSet = new ArrayList<Map<String,String>>();
      // for(SearchRecord searchRecord:SearchRecordTable.getSearchRecordSet()){
      // Map<String,String> tmp = new LinkedHashMap<String,String>();
      // tmp.put("ip", searchRecord.getIp());
      // tmp.put("link", searchRecord.getLink());
      // tmp.put("phoneModel", searchRecord.getPhoneModel());
      // tmp.put("type", searchRecord.getType());
      // tmp.put("date", searchRecord.getDate().toString());
      // searchRecordSet.add(tmp);
      // }

      List<String> searchRecordSet = new ArrayList<String>();
      for (SearchRecord searchRecord : SearchRecordTable.getSearchRecordSet()) {
        searchRecordSet.add(searchRecord.getIp());
        searchRecordSet.add(searchRecord.getLink());
        searchRecordSet.add(searchRecord.getPhoneModel());
        searchRecordSet.add(searchRecord.getType());
        searchRecordSet.add(searchRecord.getDate().toString());
      }

      byte[] jsonBytes = searchRecordSet.toString().getBytes("utf-8");
      response.setContentLength(jsonBytes.length);
      response.getOutputStream().write(jsonBytes);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
