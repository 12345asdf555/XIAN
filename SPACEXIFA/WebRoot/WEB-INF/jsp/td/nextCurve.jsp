<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>实时界面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/css/main.css">
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/iconfont.css">
	
	<script type="text/javascript" src="resources/js/loading.js"></script>
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/session-overdue.js"></script>
	<script type="text/javascript" src="resources/js/highcharts.js"></script>
	<script type="text/javascript" src="resources/js/exporting.js"></script>
	<script type="text/javascript" src="resources/js/td/nextCurve.js"></script>
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
<!-- 	<script type="text/javascript" src="resources/js/web_socket.js"></script> -->
<!-- 	<script type="text/javascript" src="resources/js/paho-mqtt.js"></script> -->
<!-- 	<script type="text/javascript" src="resources/js/paho-mqtt-min.js"></script> -->
	<style type="text/css">
		table tr td{
			font-size: 14px;
		}
		#attrtable tr td{
			height:30px;
		}
		.wsp{
			width:0;
			hieght:120px;
			margin:-5px 3px;
			border-left:3px solid black;
			}
			li{padding-top: 9px;
				}
	</style>
  </head>
  
<body class="easyui-layout">
	<input  name="machineid" id="machineid" type="hidden" value="${value }"/>
	<input id="type" type="hidden" value="${type }"/>
	<input  name="afresh" id="afresh" type="hidden" value="${afreshLogin }"/>
	<div id="bodys" region="center"  hide="true"  split="true">
		<div style="float:left; width:100%;height:30px;background-color: #145d92;color:#ffffff;font-size:20px;">设备运行参数监控
		<div style="float:right;"><a href="td/AllTd"><img src="resources/images/history.png" style="height:30px;width:40px;padding-top:5px;"></a></div></div>
		<div style="width:25%;height:180px;float:left;margin-left:20px;position: relative;">
			<fieldset>
				<legend>设备信息</legend>
				<div style="float:left;width:40%;height:180px;margin-left:10px;"><a href="td/AllTd"><img id="mrjpg" src="resources/images/welder_03.png" style="height:90%;width:85%;padding-top:10px;"></a></div>
				<div style="float:left;width:60%;height:130px;top:10px;left:35%;margin:auto;position:absolute;">
					<ul>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						设备编号：<input type="text" id="l1" value="${valuename}" readonly style="border-radius:5px;border:1px solid #ffd700;width:100px;height:20px;text-align:center;"></li>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						设备类型：<input type="text" id="l2" readonly style="border-radius:5px;border:1px solid #ffd700;width:100px;height:20px;text-align:center;"></li>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						设备厂商：<input type="text" id="l3" readonly style="border-radius:5px;border:1px solid #ffd700;width:100px;height:20px;text-align:center;"></li>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						设备状态：<input type="text" id="l5" value="关机" readonly style="border-radius:5px;border:1px solid #ffd700;width:100px;height:20px;text-align:center;"></li>
					</ul>
				</div>
			</fieldset>
		</div>
		<div style="width:70%;height:180px;float:left;margin-left:20px;position: relative;">
			<fieldset>
				<legend>参数信息</legend>
				<div class="wsp" style="float:left;width:30%;height:170px;padding-top:20px;margin-left:20px;">
					<ul>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						产品图号：<input type="text" id="r1"  value="AS-T1" readonly style="border-radius:5px;border:1px solid #ffd700;width:180px;height:20px;text-align:center;"></li>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						&nbsp;&nbsp;&nbsp;工序号：<input type="text" id="r2"  value="01" readonly style="border-radius:5px;border:1px solid #ffd700;width:180px;height:20px;text-align:center;"></li>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						焊工工号：<input type="text" id="r3"  value="0017" readonly style="border-radius:5px;border:1px solid #ffd700;width:180px;height:20px;text-align:center;"></li>
					</ul>
				</div>
				<div class="wsp" style="float:left;width:30%;height:170px;padding-top:20px;margin-left:20px;">
					<ul>
						<li style="line-height: 16px;width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						电子跟踪卡号：<input type="text" id="r5"  value="AX00G7" readonly style="border-radius:5px;border:1px solid #ffd700;width:150px;height:20px;text-align:center;"></li>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						&emsp;&emsp;&emsp;工步号：<input type="text" id="r6"  value="02" readonly style="border-radius:5px;border:1px solid #ffd700;width:150px;height:20px;text-align:center;"></li>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						&emsp;&emsp;&emsp;焊层号：<input type="text" id="r7"  value="01" readonly style="border-radius:5px;border:1px solid #ffd700;width:150px;height:20px;text-align:center;"></li>
					</ul>
				</div>
				<div class="wsp" style="float:left;width:30%;height:170px;padding-top:20px;margin-left:20px;">
					<ul>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						产品序号：<input type="text" id="r9"  value="20200202" readonly style="border-radius:5px;border:1px solid #ffd700;width:150px;height:20px;text-align:center;"></li>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						焊缝编号：<input type="text" id="r10"  value="h61" readonly style="border-radius:5px;border:1px solid #ffd700;width:150px;height:20px;text-align:center;"></li>
						<li style="width:100%;height:22px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
						&nbsp;&nbsp;&nbsp;焊道号：<input type="text" id="r11"  value="02" readonly style="border-radius:5px;border:1px solid #ffd700;width:150px;height:20px;text-align:center;"></li>
					</ul>
				</div>
				<div id="livediv" style="width:100%;height:40%;float:left;top:26%;">
				</div>
			</fieldset>
		</div>
	</div>
</body>
</html>
 
 
 
 