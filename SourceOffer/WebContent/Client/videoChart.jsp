<%@ page language="java" contentType="text/html; charset=UTF-8" 
	import="java.io.*,MySQL.specificTable.*,MySQL.SqlConst,java.sql.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="shortcut icon" href="images/favicon.ico" />
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>后台管理</title>
    <meta name="description" content="后台">
    <meta name="keywords" content="index">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
     <link rel="stylesheet" href="assets/css/amazeui.min.css" />
    <link rel="stylesheet" href="assets/css/admin.css">
    <link rel="stylesheet" href="assets/css/app.css">
    <script src="assets/js/echarts.min.js"></script>
    <style type="text/css">
    .link{
      width:60px;    
    }
    </style>
</head>
  <body>
<!-- 顶部加载进度条！ -->
<script src="public/js/preload.min.js"></script>
<script type="text/javascript">
//调用
$.QianLoad.PageLoading({
    sleep: 50
});
</script>

<body data-type="chart">

      <header class="am-topbar am-topbar-inverse admin-header">
          <div class="am-topbar-brand">
              <a href="admin.jsp" class="tpl-logo">
                  <img src="images/logo.png" alt="">
              </a>
          </div>
          <div class="am-icon-list tpl-header-nav-hover-ico am-fl am-margin-right">

          </div>

          <button class="am-topbar-btn am-topbar-toggle am-btn am-btn-sm am-btn-success am-show-sm-only" data-am-collapse="{target: '#topbar-collapse'}"><span class="am-sr-only">导航切换</span> <span class="am-icon-bars"></span></button>

          <div class="am-collapse am-topbar-collapse" id="topbar-collapse">

              <ul class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list tpl-header-list">

                  <li class="am-dropdown" data-am-dropdown data-am-dropdown-toggle>
                      <a class="am-dropdown-toggle tpl-header-list-link" href="javascript:;">
                          <span class="tpl-header-list-user-nick"><%=session.getAttribute("musicTitle")%></span><span class="tpl-header-list-user-ico"> <img src="assets/img/user01.png"></span>
                      </a>
                      <ul class="am-dropdown-content">

                          <li><a href="index.jsp"><span class="am-icon-power-off"></span> 退出</a></li>
                      </ul>
                  </li>
                  <li><a href="index.jsp" class="tpl-header-list-link"><span class="am-icon-sign-out tpl-header-list-ico-out-size"></span></a></li>
              </ul>
          </div>
      </header>
      <div class="tpl-page-container tpl-page-header-fixed">
        <div class="tpl-left-nav tpl-left-nav-hover">
            <div class="tpl-left-nav-title">
                操作
            </div>
            <div class="tpl-left-nav-list">
                <ul class="tpl-left-nav-menu">
                	<li class="tpl-left-nav-item">
                        <a href="admin.jsp" class="nav-link active">
                            <i class="am-icon-home"></i>
                            <span>主页</span>
                        </a>
                    </li>
                    <li class="tpl-left-nav-item">
                        <a href="adminChart.jsp" class="nav-link tpl-left-nav-link-list">
                            <i class="am-icon-bar-chart"></i>
                            <span>管理员</span>
                        </a>
                    </li>
					<li class="tpl-left-nav-item">
                        <a href="videoChart.jsp" class="nav-link active">
                            <i class="am-icon-home"></i>
                            <span>视频资源</span>
                        </a>
                    </li>
                    <li class="tpl-left-nav-item">
                        <a href="musicChart.jsp" class="nav-link active">
                            <i class="am-icon-home"></i>
                            <span>音乐资源</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>

          
	<%
		String sql = "select * from Video";
		SqlConst videoSqlConst = new SqlConst(sql);
		ResultSet videoResultSet = videoSqlConst.getPst().executeQuery(sql);
	%>

          <div class="tpl-content-wrapper">

                  <ol class="am-breadcrumb">
                      <li><a href="admin.jsp" class="am-icon-home">首页</a></li>
                      <li class="am-active">管理员</li>
                  </ol>
                  <div class="tpl-portlet-components">

                      <div class="tpl-block">
                          <div class="am-g">
                              <div class="am-u-sm-12">
                                 
                                      <table class="am-table am-table-striped am-table-hover table-main">
                                          <thead>
                                              <tr>
                                                  <th class="table-id">ID</th>
                                                  <th class="table-title" >title</th>
                                                  <th class="table-type link" >link</th>
                                                  <th class="table-author am-hide-sm-only">type</th>
                                                  <th class="table-set">操作</th>
                                                  <!-- <th class="table-author am-hide-sm-only">时间</th> -->
                                              </tr>
                                          </thead>
                                          <tbody>
                                          <%
                                            while(videoResultSet.next()){
                                          %>
                                              <tr>
                                                  <td><%=videoResultSet.getInt("id") %></td>
                                                  <td><%=videoResultSet.getString("title") %></td>
                                                  <td class="link"><%=videoResultSet.getString("link")%></td>
                                                  <td><%=videoResultSet.getString("type")%></td>
                                                  <td>
                                                  <form action="deleteVideo">
                                                 	<input type="hidden" name="id" value=<%=videoResultSet.getInt("id") %>>
                                                    <button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span class="am-icon-trash-o"></span> 删除</button>
                                                  </form>
                                                  </td>
                                                  <!-- <td class="am-hide-sm-only"><!--%=adminResultSet.getDate("date") %></td> -->
                                              </tr>
										<%
                                            }
										%>
                                          </tbody>
                                      </table>
                                      <hr>

                              </div>

                          </div>
                      </div>
                      <div class="tpl-alert"></div>
                  </div>
              </div>
          </div>
    <script src="http://www.jq22.com/jquery/jquery-2.1.1.js"></script>
    <script src="assets/js/amazeui.min.js"></script>
    <script src="assets/js/app.js"></script>
</body>
</html>