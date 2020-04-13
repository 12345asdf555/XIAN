<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>产品超标统计</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
 
	<script type="text/javascript" src="resources/js/load.js"></script>
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
	<script type="text/javascript" src="resources/js//datastatistics/unstandard.js"></script>
	<style type="text/css">
		.textbox-text {
			width:100px;
		}
	</style>
  </head>
  
  <body>
    <div id="body">
		<div id="chartLoading" style="width:100%;height:100%;">
			<div id="chartShow" style="width:160px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...</div>
		</div>
	  	<div class="functiondiv">
	  		<div>
				<div  style="float: left;">
					<div>
						<label>产品图号：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="product_drawing_no" id="product_drawing_no"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>产品名称：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="product_name" id="product_name"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>任务编号：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="taskno" id="taskno"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>工艺规程编号：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="fwps_lib_num" id="fwps_lib_num"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>电子跟踪卡号：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="fwelded_junction_no" id="fwelded_junction_no"/>
					</div>
				</div>
				
				<div  style="float: left;">
					<div>
						<label>工序号：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="femployee_num" id="femployee_num"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>工序名称：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="femployee_name" id="femployee_name"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>工步号：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="fstep_number" id="fstep_number"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>工步名称：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="fstep_name" id="fstep_name"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>焊缝编号：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="junction_name" id="junction_name"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>焊接部位：</label>
					</div>
					<div>
						<input class="easyui-textbox" style="width:100px;" name="fweldingarea" id="fweldingarea"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>组织机构：</label>
					</div>
					<div>
						<select class="easyui-combobox" style="width:100px;" name="item" id="item" data-options="editable:false"></select>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>焊工姓名：</label>
					</div>
					<div>
<!-- 						<input id="weldername_id" type="hidden"> -->
						<select class="easyui-combobox" style="width:100px;" name="weldername" id="weldername" ></select>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>焊机编号：</label>
					</div>
					<div>
						<select class="easyui-combobox" style="width:100px;" name="weldmachine" id="weldmachine" ></select>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>时间：</label>
					</div>
					<div>
						<input class="easyui-datetimebox" style="width:100px;" name="dtoTime1" id="dtoTime1">--
						<input class="easyui-datetimebox" style="width:100px;" name="dtoTime2" id="dtoTime2">&emsp;
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>产品序号：</label>
					</div>
					<div>
						<input class="easyui-textbox" name="fproduct_id" id="fproduct_id" style="width: 100px"/>--
						<input class="easyui-textbox" style="width:60px;" name="product_number" id="product_number"/>
					</div>
				</div>
				<div  style="float: left;">
					<div>
						<label>查询和导出：</label>
					</div>
					<div>
						<a href="javascript:serach();" class="easyui-linkbutton" iconCls="icon-select" >查询</a>&emsp;
						<a href="javascript:exportunstardExcel();" class="easyui-linkbutton" iconCls="icon-export">导出</a>
					</div>
				</div>
			</div>
		</div>
		<div id="wpsTableDiv" style="height:70%;">
	   		<table id="documenttable" style="table-layout: fixed; width:100%;"></table>
  		</div>
    </div>
  </body>
</html>
