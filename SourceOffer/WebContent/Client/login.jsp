<%@ page language="java"
	import="java.io.*,java.sql.*,java.util.*,org.apache.http.*,
	org.apache.http.client.*,org.apache.http.client.methods.HttpGet.*,
	org.apache.http.impl.client.DefaultHttpClient.*,org.apache.http.util.EntityUtils.*,
	net.sf.json.JSONObject.*"
	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>欢迎登陆！</title>
  <meta name="keywords" content="index">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <link rel="icon" type="image/png" href="assets/i/favicon.png">
  <link rel="stylesheet" href="assets/css/amazeui.min.css" />
  <link rel="stylesheet" href="assets/css/admin.css">
  <link rel="stylesheet" href="assets/css/app.css">
</head>

<body data-type="login">
  <div class="am-g myapp-login">
	<div class="myapp-login-logo-block  tpl-login-max">
		<div class="myapp-login-logo-text">
			<div class="myapp-login-logo-text">
				SourceOffer<span> 登录</span> <i class="am-icon-skyatlas"></i>
			</div>
		</div>
		<div class="am-u-sm-10 login-am-center" style="margin:20% auto;">
			<form class="am-form" action="login" method="get">
				<fieldset>
					<div class="am-form-group">
						<input type="email" class="" id="doc-ipt-email-1" name="username" placeholder="用户名">	
					</div>
					<div class="am-form-group">
						<input type="password" class="" id="doc-ipt-pwd-1" name="password" placeholder="密码">
					</div>
					<p><button type="submit" class="am-btn am-btn-default">确认登录</button></p>
				</fieldset>
			</form>
		</div>
	</div>
</div>

  <script src="http://www.jq22.com/jquery/jquery-2.1.1.js"></script>
  <script src="assets/js/amazeui.min.js"></script>
  <script src="assets/js/app.js"></script>
</body>

</html>