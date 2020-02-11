<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>欢迎使用</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/css/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/modal-box.css" />
	
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/echarts.js"></script>
	<script type="text/javascript" src="resources/js/getTime.js"></script>
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
	<script type="text/javascript" src="resources/js/web_socket.js"></script>
	<script type="text/javascript" src="resources/js/first.js"></script>
	<style type="text/css">
	.WSP{
	width:48%;
	height:40%;
	border:1px solid #E1FFFF;
	border-radius:25px;
	}
	</style>
  </head>
  <body style="background:#4169E1;">
<!--   		<div align="right"><a href="javascript:closeScreen();" class="easyui-linkbutton" iconCls="icon-newadd">退出全屏</a>&nbsp;&nbsp;&nbsp;&nbsp;</div> -->
  		<p id="nowTime" style="color: #ffffff;position:absolute;left:10%;top:1%;"></p>
  		<p id="todayWeather" style="color: #ffffff;position:absolute;left:72%;top:1%;"></p>
  		<div style="background:url(resources/images/rlIndex2.png) no-repeat 3px 5px;position:absolute;left:35%;top:0.08%;height:100px; width:400px;line-height: 100%">
  			<p align="center" style="color:#000000; font-size:20px;">焊接质量参数在线监测系统</p>
  		</div>
<!--   		<div id="div1-1" style="background-color: #11325f;width:30%;height:39%;float:left;position:absolute;left:3%;top:14%;"></div> -->
  		<div id="div1-2" class="WSP" style="background-color:#6495ED;width:48%;height:40%;float:left;position:absolute;left:50%;top:13%;"></div>
<!--   		<div id="div1-3" style="background-color: #11325f;width:30%;height:39%;float:left;position:absolute;left:67%;top:14%;"></div> -->
  		<div id="div2-1" class="WSP" style="background-color:#6495ED;width:48%;height:40%;float:left;position:absolute;top:55%;"></div>
  		<div id="div2-2" class="WSP"  style="background-color:#6495ED;width:48%;height:40%;float:left;position:absolute;left:50%;top:55%;">
   			<div id="div2-21" style="width:100%;height:100%;float:left:40%;position:absolute;top:12%;"></div>
<!--  			<div id="div2-22" style="width:50%;height:100%;float:left;position:absolute;left:50%;top:12%;"></div> -->
  		</div>
  		<div id="div2-3" class="wsp" style="background-color:#6495ED;width:48%;height:40%;float:left;position:absolute;top:13%;"></div>
  </body>
</html>
