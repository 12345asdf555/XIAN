<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>规则信息</title>
    
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
	<script type="text/javascript" src="resources/js/wpslib/process.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/processtree.js"></script>
  </head>
  
  <body class="easyui-layout">
  <jsp:include  page="../wpslib/processtree.jsp"/>
	  	<div region="center"  hide="true"  split="true" >
	  		    <div class="functiondiv">
					<div  style="float: left;">
						<label>产品名称：</label><input class="easyui-textbox" name="fproduct_name" id="fproduct_name" value="${fproduct_name}" disabled="disabled"/>
					</div>
					<div  style="float: left;">
						<label>焊缝编号：</label><select class="easyui-combobox" name="fjunction" id="fjunction" data-options="editable:false"></select>
					</div>
				</div>
		    <div id="body">
	    	<input  name="flagId" id="flagId" type="hidden" value="${fid}"/>
	    	<input  name="status" id="status" type="hidden" value="${status}"/>
	    	<input  name="symbol" id="symbol" type="hidden" value="${symbol}"/>
			<!-- 工艺台账列表 -->
		   	<table id="wpsDetailTable" style="table-layout: fixed; width:100%;"></table>
		   	<div id="turnDownDialog" class="easyui-dialog" style="width: 1000px; height: 400px; padding:10px 20px" closed="true" buttons="#tdd-buttons">
		   		<lable>驳回原因：</lable>
          		<textarea name="downReason" id="downReason" style="height:90%;width:100%"></textarea>
		   	</div>
		   	<div id="tdd-buttons">
				<a href="javascript:saveReason();" class="easyui-linkbutton" iconCls="icon-ok" id="otcsaveWpsBut">保存</a>
				<a href="javascript:closeDlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
			</div>
		   	<div align="center">
		   		<a id="down" href="javascript:openTurnDownDialog();" class="easyui-linkbutton" iconCls="icon-ok">驳回</a>
			   	<a id="pass" href="javascript:passReview();" class="easyui-linkbutton" iconCls="icon-ok">通过审核</a>
			   	<a href="wps/goWpslib" class="easyui-linkbutton" iconCls="icon-ok">返回</a>
		   	</div>
	    </div>
    </div>
  </body>
</html>
