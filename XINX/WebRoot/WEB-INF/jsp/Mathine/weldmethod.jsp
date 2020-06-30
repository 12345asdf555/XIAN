<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>焊接方法绑定等级</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
<link rel="stylesheet" type="text/css" href="resources/css/common.css">
<link rel="stylesheet" type="text/css" href="resources/css/iconfont.css">

<script type="text/javascript" src="resources/js/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="resources/js/Mathine/weldmethod.js"></script>
</head>

<body>
	<div id="body">
		<form id="form" class="easyui-form" method="post"
			data-options="novalidate:true" buttons="#dlg-buttons">
			<div region="left">
				<lable>资质等级：</lable>
				<select class="easyui-combobox"  id="descid" name="desc" data-options="editable:false"></select>
			</div>
			<div region="left">
				<table id="tt" title="焊接方法等级列表" checkbox="true" style="width:100%"></table>
			</div>
		</form>
		<div align="center">
			<a href="javascript:save();" class="easyui-linkbutton" style="border: 1px solid green;" iconCls="icon-ok">保存</a>
		</div>

	</div>
</body>
</html>
