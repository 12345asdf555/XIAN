<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>历史数据</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />

	<script type="text/javascript" src="resources/js/weldingtask/json2.js"></script>
	<script type="text/javascript" src="resources/js/echarts.js"></script>
	<!-- <script type="text/javascript" src="resources/js/load.js"></script> -->
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
	<script type="text/javascript" src="resources/js//datastatistics/historyData.js"></script>
  </head>
  
  <body>
    	<div id="body" style="height: 46%">
	    	<div class="functiondiv">
				<div>
					<div style="float: left;">
							<label>电子跟踪卡号：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="fcard_id" id="fcard_id" />
					</div>
					<div style="float: left;">
							<label>产品序号：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="fprefix_number" id="fprefix_number" style="width: 90px"/> -- 
							<input class="easyui-textbox" name="fproduct_id" id="fproduct_id" style="width: 45px"/> -- 
							<input class="easyui-textbox" name="fsuffix_number" id="fsuffix_number" style="width: 90px"/>
					</div>
					<div style="float: left;">
							<label>设备编号：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="fequipment_id" id="fequipment_id" />
					</div>
					<div style="float: left;">
							<label>焊工号：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="fwelder_id" id="fwelder_id" />
					</div>
					<div>
							<label>工序号：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="femployee_id" id="femployee_id" />
					</div>
				</div>
				<div>
					<div style="float: left;">
							<label>&nbsp;&nbsp;&nbsp;&nbsp;工&nbsp;&nbsp;&nbsp;&nbsp;步&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="fstep_id" id="fstep_id" />
					</div>
					<div style="float: left;">
							<label>焊缝编号：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="fjunction_id" id="fjunction_id" />
					</div>
					<div style="float: left;">
							<label>焊接部位：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="fwelding_area" id="fwelding_area" />
					</div>
					<div style="float: left;">
							<label>时间：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-datetimebox" name="dtoTime1" id="dtoTime1"> -- 
							<input class="easyui-datetimebox" name="dtoTime2" id="dtoTime2">
					</div>
					<div style="float: left;">
						<a href="javascript:searchHistory();" class="easyui-linkbutton" iconCls="icon-select">查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
			</div>
			<div id="tableDiv" style="height: 70%">
				<div id="chartLoading" style="width:100%;height:100%;">
					<div id="chartShow" style="width:160px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...</div>
				</div>
			    <table id="dg" style="table-layout: fixed; width:100%;"></table>
			</div>
    	</div>
	    <div  id="historyCurveDiv" style="height: 46%;overflow: auto;"></div>
  </body>
</html>
