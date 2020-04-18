<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="height: 100%">
  <head>
    <base href="<%=basePath%>">
    
    <title>电子跟踪卡管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	
	<script type="text/javascript" src="resources/js/weldingtask/json2.js"></script>
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
<!-- 	<script type="text/javascript" src="resources/js/datagrid-detailview.js" charset="utf-8"></script> -->
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/allCard.js"></script>
 	<script type="text/javascript" src="resources/js/weldingtask/addeditCard.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/removeCard.js"></script>
<!--	<script type="text/javascript" src="resources/js/wpslib/giveWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/differentMachine.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/control.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/comboboxCheck.js"></script> -->
	<script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
	<script type="text/javascript" src="resources/js/web_socket.js"></script>
	<style type="text/css">
		table tr td{
			font-size:12px;
		}
		.leftTd{
			text-align: right;
		}
		.textbox-text{
			width:85px;
		}
	</style>
  </head>
  
  <body style="height: 100%">
  	  	<div class="functiondiv">
			<div>
				<div style="float: left;">
					<div>
						<label>电子跟踪卡号：</label>
					</div>
					<div>
						<input class="easyui-textbox" name="card_no" id="card_no" />
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>任务编号：</label>
					</div>
					<div>
						<input class="easyui-textbox" name="task_no" id="task_no" />
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>组织机构：</label>
					</div>
					<div>
						<select class="easyui-combobox" name="item" id="item" data-options="editable:false">
						</select>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>卡号来源：</label>
					</div>
					<div>
						<select class="easyui-combobox" name="wflag" id="wflag" data-options="editable:false">
							<option value="">无</option>
							<option value="0">自建</option>
							<option value="1">MES</option>
						</select>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>审核：</label>
					</div>
					<div>
						<select class="easyui-combobox" name="status" id="status" data-options="editable:false"">
							<option value="">无</option>
							<option value="0">待审核</option>
							<option value="1">已通过</option>
						</select>
					</div>
				</div>
				<div  style="float: left;">
					<a href="javascript:searchWps();" class="easyui-linkbutton" iconCls="icon-select">查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
			</div>
		</div>
  	<div id="body" style="height: 78%">
  	
  	<div id="wpsDetailsDialog" class="easyui-dialog" style="width: 1300px; height: 320px; padding:10px 20px" closed="true">
		<form id="wpsDetailsForm" class="easyui-form" method="post" data-options="novalidate:true">
			<div style="float: left;width: 22%">
				<div class="fitem">
					<lable><span class="required">*</span>产品图号</lable>
					<input type="hidden" id="validPdo">
					<input class="easyui-textbox" name="fproduct_drawing_no" id="fproduct_drawing_no"  data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>产品名称</lable>
					<input class="easyui-textbox" name="fproduct_name" id="fproduct_name"  data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>产品版本号</lable>
					<input type="hidden" id="validProduct">
					<input class="easyui-textbox" name="fproduct_version" id="fproduct_version"  data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>工艺规程编号</lable>
					<input type="hidden" id="validWpsname">
					<input class="easyui-textbox" name="fwps_lib_name" id="fwps_lib_name"  data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>工艺规程版本号</lable>
					<input type="hidden" id="validWpsversion">
					<input class="easyui-textbox" name="fwps_lib_version" id="fwps_lib_version"  data-options="validType:'xifawpsValidate',required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>工艺来源</lable>
					<select class="easyui-combobox" name="wps_flag" id="wps_flag" data-options="required:true,editable:false,disabled:true">
						<option value="0">自建</option>
						<option value="1">MES</option>
					</select>
				</div>
			</div>
			<div style="float: left;width: 6%">
				<hr size=200 width="1" color="black">
			</div>
			<div style="float: left;width: 70%">
				<div id="employeeDiv" style="float: left;width: 39%;height: 50%">
					<table id="femployeeTable" style="table-layout: fixed; width:100%;"></table>
				</div>
				<div id="stepDiv" style="float: left;width: 30%;height: 50%">
					<table id="fstepTable" style="table-layout: fixed; width:100%;"></table>
				</div>
				<div id="junctionDiv" style="float: left;width: 30%;height: 50%">
					<table id="fjunctionTable" style="table-layout: fixed; width:100%;"></table>
				</div>
				<div id="detailDiv" style="width: 100%;height: 50%">
					<table id="wpsDetailTable" style="table-layout: fixed; width:100%;"></table>
				</div>
			</div>
		</form>
	</div>
  	
  		<!-- 工艺台账列表 -->
  		<div id="cardTableDiv" style="height: auto;">
	   		<table id="cardTable" style="table-layout: fixed; width:100%;"></table>
  		</div>
	     <!-- 添加修改工艺台账 -->
		<div id="addOrUpdate" class="easyui-dialog" style="width: 380px; height: 400px; padding:10px 20px" closed="true" buttons="#tdd-buttons">
			<form id="addOrUpdatefm" class="easyui-form" method="post" data-options="novalidate:true">
				<div class="fitem">
					<lable><span class="required">*</span>电子跟踪卡号</lable>
					<input type="hidden" id="validCard">
					<input class="easyui-textbox" name="fwelded_junction_no" id="fwelded_junction_no"  data-options="validType:'cardValidate',required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>任务编号</lable>
					<input type="hidden" id="validTask">
					<input class="easyui-textbox" name="ftask_no" id="ftask_no"  data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>组织机构</lable>
					<select class="easyui-combobox" name="fitemId" id="fitemId"  data-options="required:true"></select>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>工艺规程编号</lable>
					<select class="easyui-combogrid" name="fwps_lib_id" id="fwps_lib_id"  data-options="required:true"></select>
				</div>
<!-- 					<div class="fitem">
						<lable><span class="required">*</span>工艺规程版本号</lable>
						<input class="easyui-textbox" name="fwps_lib_version" id="fwps_lib_version"  data-options="required:true"/>
					</div> -->
				<div class="fitem">
					<lable><span class="required">*</span>卡号来源</lable>
					<select class="easyui-combobox" name="flag" id="flag" data-options="required:true,editable:false,disabled:true">
						<option value="0">自建</option>
						<option value="1">MES</option>
					</select>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>产品数量</lable>
					<input class="easyui-numberbox" name="fproduct_number" id="fproduct_number"  data-options="required:true"/>
				</div>
				<div  class="fitem">
					<lable><span class="required">*</span>前缀号</lable>
					<input class="easyui-textbox" name="fprefix_number" id="fprefix_number"  data-options="required:true"/>
				</div>
				<div  class="fitem">
					<lable><span class="required">*</span>初始值</lable>
					<input class="easyui-numberbox" name="init_number" id="init_number"  data-options="required:true"/>
				</div>
			</form>
		</div>
	   	<div id="tdd-buttons">
			<a href="javascript:saveCard();" class="easyui-linkbutton" iconCls="icon-ok" id="saveCard">保存</a>
			<a href="javascript:saveReview();" class="easyui-linkbutton" iconCls="icon-ok" id="saveAndPassCard">保存并重新提交审核</a>
			<a href="javascript:closeDlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 临时切换工艺 -->
		<div id="changeWpsDiv" class="easyui-dialog" style="width: 360px; height: 140px; padding:10px 20px" closed="true" buttons="#cw-buttons">
			<div class="fitem">
				<lable><span class="required">*</span>工艺规程编号</lable>
				<select class="easyui-combobox" name="fwps_lib_id_ch" id="fwps_lib_id_ch"  data-options="required:true"></select>
			</div>
		</div>
	   	<div id="cw-buttons">
			<a href="javascript:saveChange();" class="easyui-linkbutton" iconCls="icon-ok" id="otcsaveWpsBut">保存</a>
			<a href="javascript:closeDlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
	    <div class="functiondiv">
			<div>
				<a href="javascript:addWps();" class="easyui-linkbutton" iconCls="icon-newadd">自建卡号</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:editWps();" class="easyui-linkbutton" iconCls="icon-update">修改卡号</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:deleteWps();" class="easyui-linkbutton" iconCls="icon-delete">删除卡号</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:wpsDetails();" class="easyui-linkbutton" iconCls="icon-navigation">产品审核</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:historyDetails();" class="easyui-linkbutton" iconCls="icon-history">临时工艺切换历史</a>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		<div id="productDetailsDlg" class="easyui-dialog" style="width: 800px; height: 400px; padding:10px 20px" closed="true">
			<table id="productDetailsTable" style="table-layout: fixed; width:100%;"></table>
		</div>
		<div id="historyDetailsDlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true">
			<table id="historyDetailsTable" style="table-layout: fixed; width:100%;"></table>
		</div>
		<div id="load" style="width:100%;height:100%;"></div>
	</div>
	
	<style type="text/css">
	    #load{ display: none; position: absolute; left:0; top:0;width: 100%; height: 40%; background-color: #555753; z-index:10001; -moz-opacity: 0.4; opacity:0.5; filter: alpha(opacity=70);}
		#show{display: none; position: absolute; top: 45%; left: 45%; width: 180px; height: 5%; padding: 8px; border: 8px solid #E8E9F7; background-color: white; z-index:10002; overflow: auto;}
	</style>
  </body>
</html>