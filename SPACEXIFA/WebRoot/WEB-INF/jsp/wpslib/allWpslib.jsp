<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>工艺管理</title>
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
	<script type="text/javascript" src="resources/js/datagrid-detailview.js" charset="utf-8"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/allWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/addeditWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/removeWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/giveWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/differentMachine.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/control.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/comboboxCheck.js"></script>
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
  
  <body>
  	<div class="functiondiv">
		<div>
			<a href="javascript:addWpslib();" class="easyui-linkbutton" iconCls="icon-newadd">新增工艺库</a>&nbsp;&nbsp;&nbsp;&nbsp;
<!-- 			<a href="javascript:openCondlg();" class="easyui-linkbutton" iconCls="icon-newadd">控制命令下发</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:openHistorydlg();" class="easyui-linkbutton" iconCls="icon-newadd"> 下发历史查询</a>&nbsp;&nbsp;&nbsp;&nbsp; -->
			<a href="javascript:importclick();" class="easyui-linkbutton" iconCls="icon-newadd"> 工艺导入</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
	</div>
  	<div id="body" >
	    <table id="wpslibTable" style="table-layout: fixed; width:100%;"></table>

	    <!-- 添加修改工艺库 -->
		<div id="wltdlg" class="easyui-dialog" style="width: 400px; height: 225px; padding:10px 20px" closed="true" buttons="#wltdlg-buttons">
			<form id="wltfm" class="easyui-form" method="post" data-options="novalidate:true">
				<div class="fitem">
					<lable><span class="required">*</span>工艺库名称</lable>
					<input type="hidden" id="validwl">
					<input class="easyui-textbox" name="wpslibName" id="wpslibName"  data-options="validType:['wpslibValidate'],required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>焊机型号</lable>
					<select class="easyui-combobox" name="model" id="model" data-options="required:true,editable:false""></select>
				</div>
				<div class="fitem">
					<lable>状态</lable>
	   				<span id="radios"></span>
				</div>
			</form>
		</div>
		<div id="wltdlg-buttons">
			<a href="javascript:saveWpslib();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:closeDialog('wltdlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 删除工艺库 -->
		<div id="rmwltdlg" class="easyui-dialog" style="width: 400px; height: 170px; padding:10px 20px" closed="true" buttons="#rmwltdlg-buttons">
			<form id="rmwltfm" class="easyui-form" method="post" data-options="novalidate:true">
				<div class="fitem">
					<lable><span class="required">*</span>工艺库名称</lable>
					<input type="hidden" id="validwl">
					<input class="easyui-textbox" name="wpslibName" id="wpslibName"/>
				</div>
				<div class="fitem">
					<lable>状态</lable>
	   				<span id="radios"></span>
				</div>
			</form>
		</div>
		<div id="rmwltdlg-buttons">
			<a href="javascript:removeWpslib();" class="easyui-linkbutton" iconCls="icon-ok">删除</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 添加修改工艺 -->
		<div id="mwdlg" class="easyui-dialog" style="width: 700px; height: 400px; padding:10px 20px" closed="true" buttons="#mwdlg-buttons">
			<form id="mwfm" class="easyui-form" method="post" data-options="novalidate:true">
	        	<div class="fitem">
					<lable><span class="required">*</span>焊层号</lable>
					<input class="easyui-numberbox" id="fsolder_layer"  name="fsolder_layer" data-options="required:true" />
					<lable><span class="required">*</span>焊道号</lable>
					<input class="easyui-numberbox" name="fweld_bead" id="fweld_bead" data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>通道号</lable>
					<select class="easyui-combobox" id="fwpsnum"  name="fwpsnum" data-options="editable:false,required:true"></select>
					<lable><span class="required">*</span>脉冲</lable>
					<select class="easyui-combobox" id="fprocessid"  name="fprocessid" data-options="editable:false,required:true"></select>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>焊丝材质</lable>
					<select class="easyui-combobox" id="fmaterial"  name="fmaterial" data-options="editable:false,required:true"></select>
					<lable><span class="required">*</span>焊丝直径</lable>
					<select class="easyui-combobox" id="fdiameter"  name="fdiameter" data-options="editable:false,required:true"></select>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>电流下限</lable>
					<input class="easyui-numberbox" id="fpreset_ele_bottom"  name="fpreset_ele_bottom" data-options="required:true" />
					<lable><span class="required">*</span>电流上限</lable>
					<input class="easyui-numberbox" id="fpreset_ele_top"  name="fpreset_ele_top" data-options="required:true" />
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>电压下限</lable>
					<input class="easyui-numberbox" id="fpreset_vol_bottom"  name="fpreset_vol_bottom" data-options="required:true" />
					<lable><span class="required">*</span>电压上限</lable>
					<input class="easyui-numberbox" id="fpreset_vol_top"  name="fpreset_vol_top" data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required"></span>气体流量</lable>
					<input class="easyui-textbox" id="fgas_flow"  name="fgas_flow" />
					<lable><span class="required"></span>焊接速度</lable>
					<input class="easyui-textbox" id="fweld_speed"  name="fweld_speed" />
				</div>
				<div class="fitem">
					<lable><span class="required"></span>电源极性</lable>
					<input class="easyui-textbox" id="fpower_polarity"  name="fpower_polarity" />
					<lable><span class="required"></span>焊接方法</lable>
					<input class="easyui-textbox" id="fweld_method"  name="fweld_method" />
				</div>
			</form>
		</div>
		<div id="mwdlg-buttons">
			<!-- <a href="javascript:selectMachineList(0);" class="easyui-linkbutton" iconCls="icon-getwps" id="otcgetWpsBut">索取规范</a> -->
			<a href="javascript:saveMainWps();" class="easyui-linkbutton" iconCls="icon-ok" id="otcsaveWpsBut">保存</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 删除工艺 -->
		<div id="rmmwdlg" class="easyui-dialog" style="width: 700px; height: 400px; padding:10px 20px" closed="true" buttons="#rmmwdlg-buttons">
			<form id="rmmwfm" class="easyui-form" method="post" data-options="novalidate:true">
	        	<div class="fitem">
					<lable><span class="required">*</span>焊层号</lable>
					<input class="easyui-numberbox" id="fsolder_layer"  name="fsolder_layer" data-options="required:true" />
					<lable><span class="required">*</span>焊道号</lable>
					<input class="easyui-numberbox" name="fweld_bead" id="fweld_bead" data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>通道号</lable>
					<select class="easyui-combobox" id="fchanel"  name="fchanel" data-options="editable:false"></select>
					<lable><span class="required">*</span>脉冲</lable>
					<select class="easyui-combobox" id="fprocessid"  name="fprocessid" data-options="editable:false"></select>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>焊丝材质</lable>
					<select class="easyui-combobox" id="fmaterial"  name="fmaterial" data-options="editable:false"></select>
					<lable><span class="required">*</span>焊丝直径</lable>
					<select class="easyui-combobox" id="fdiameter"  name="fdiameter" data-options="editable:false"></select>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>焊材型号</lable>
					<input class="easyui-textbox" id="fweld_material_model"  name="fweld_material_model" />
					<lable><span class="required">*</span>工艺设计</lable>
					<input class="easyui-textbox" id="ftechnological_design"  name="ftechnological_design" />
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>电流下限</lable>
					<input class="easyui-numberbox" id="fpreset_ele_bottom"  name="fpreset_ele_bottom" />
					<lable><span class="required">*</span>电流上限</lable>
					<input class="easyui-numberbox" id="fpreset_ele_top"  name="fpreset_ele_top" />
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>电压下限</lable>
					<input class="easyui-numberbox" id="fpreset_vol_bottom"  name="fpreset_vol_bottom" />
					<lable><span class="required">*</span>电压上限</lable>
					<input class="easyui-numberbox" id="fpreset_vol_top"  name="fpreset_vol_top" />
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>气体流量</lable>
					<input class="easyui-textbox" id="fgas_flow"  name="fgas_flow" />
					<lable><span class="required">*</span>焊接速度</lable>
					<input class="easyui-textbox" id="fweld_speed"  name="fweld_speed" />
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>电源极性</lable>
					<input class="easyui-textbox" id="fpower_polarity"  name="fpower_polarity" />
					<lable><span class="required">*</span>焊接方法</lable>
					<input class="easyui-textbox" id="fweld_method"  name="fweld_method" />
				</div>
			</form>
		</div>
		<div id="rmmwdlg-buttons">
			<a href="javascript:removeMainwps();" class="easyui-linkbutton" iconCls="icon-ok">删除</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 选择工艺 -->
		<div id="smwdlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true" buttons="#smwdlg-buttons">
			<form id="smwfm" class="easyui-form" method="post" data-options="novalidate:true">
				<table id="mainWpsTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
		<div id="smwdlg-buttons">
			<a href="javascript:selectMachineList(1);" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 选择焊机 -->
		<div id="smdlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true" buttons="#smdlg-buttons">
			<form id="smfm" class="easyui-form" method="post" data-options="novalidate:true">
				<table id="weldingmachineTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
		<div id="smdlg-buttons">
			<a href="javascript:selectModel();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 下发结果表格 -->
		<div id="resultdlg" class="easyui-dialog" style="width: 1120px; height: 600px; padding:10px 20px" closed="true" buttons="#resultdlg-buttons">
			<form id="resultfm" class="easyui-form" method="post" data-options="novalidate:true">
				<table id="giveResultTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
		<div id="resultdlg-buttons">
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 控制命令下发 -->
		<div id="condlg" class="easyui-dialog" style="width: 600px; height: 300px; padding:10px 20px" closed="true" buttons="#condlg-buttons">
			<form id="confm" class="easyui-form" method="post" data-options="novalidate:true">
				<table width="100%" height="94%" border="1" style="text-align: center;">
					  <tr height="30px">
					    <td colspan="2" align="center">
					    	<font face="黑体" size="5">控制命令</font>
					    </td>
					  </tr>
					  <tr height="30px">
					    <td align="center" bgcolor="#FFFAF0">工作：</td>
					    <td>
					    	<input id ="free" name="free" type="radio" value="1" checked="checked"/>工作不可自由调节
			  				<input id ="free" name="free" type="radio" value="0"/>工作自由调节
			  			</td>
					  </tr>
					  <tr height="30px">
					    <td colspan="2" align="center">					
							<a href="javascript:selectMachineList(3);" class="easyui-linkbutton" iconCls="icon-ok">下发控制命令</a>
							<a href="javascript:openPassDlg();" class="easyui-linkbutton" iconCls="icon-ok">密码下发</a>			
						</td>
					  </tr>
				</table>
			</form>
		</div>
		<div id="condlg-buttons">
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 密码框 -->
		<div id="pwd" class="easyui-dialog" style="text-align:center;width:400px;height:200px" closed="true" buttons="#dlg-pwd"algin="center">
	        <br><br><lable><span class="required">*</span>密码：</lable>
	        <input name="passwd" id="passwd" type="password" class="easyui-numberbox"><br/>
	        <lable style="color:red;">（注：密码范围是1~999）</lable>
        </div>
        <div id="dlg-pwd">
			<a href="javascript:selectMachineList(2);" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 选择松下工艺 -->
		<div id="sxSelectdlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true" buttons="#sxSelectdlg-buttons">
			<form id="smwfm" class="easyui-form" method="post" data-options="novalidate:true">
				<table id="sxSelectWpsTab" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
		<div id="sxSelectdlg-buttons">
			<a href="javascript:selectSxMachineList(1);" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 选择松下焊机 -->
		<div id="sxMachinedlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true" buttons="#sxmachinedlg-buttons">
			<form id="sxmachinefm" class="easyui-form" method="post" data-options="novalidate:true">
				<table id="sxMachineTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div> 
		<div id="sxmachinedlg-buttons">
			<a href="javascript:selectSxModel();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 下发历史查询 -->
		<div id="wmhistorydlg" class="easyui-dialog" style="width: 950px; height: 520px; padding:10px 20px" closed="true">
			<form id="wmhistoryfm" class="easyui-form" method="post" data-options="novalidate:true">
			  	<div id="dg_btn">
					<div style="margin-bottom: 5px;">
			 			焊机编号：
						<input class="easyui-numberbox" name="machineNum" id="machineNum">
						工艺库名称：
						<input class="easyui-textbox" name="theWpslibName" id="theWpslibName">
						时间：
						<input class="easyui-datetimebox" name="dtoTime1" id="dtoTime1">--
						<input class="easyui-datetimebox" name="dtoTime2" id="dtoTime2">
						<a href="javascript:searchHistory();" class="easyui-linkbutton" iconCls="icon-select" >搜索</a>
					</div>
				</div>
				<table id="historyTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
		
		<div id="importdiv" class="easyui-dialog" style="width:300px; height:200px;" closed="true">
			<form id="importfm" method="post" class="easyui-form" data-options="novalidate:true" enctype="multipart/form-data"> 
				<div>
					<span><input type="file" name="file" id="file"></span>
					<input type="button" value="上传" onclick="importWpsExcel()" class="upButton"/>
				</div>
			</form>
		</div>
		<div id="load" style="width:100%;height:100%;"></div>
	</div>
	<style type="text/css">
	    #load{ display: none; position: absolute; left:0; top:0;width: 100%; height: 40%; background-color: #555753; z-index:10001; -moz-opacity: 0.4; opacity:0.5; filter: alpha(opacity=70);}
		#show{display: none; position: absolute; top: 45%; left: 45%; width: 180px; height: 5%; padding: 8px; border: 8px solid #E8E9F7; background-color: white; z-index:10002; overflow: auto;}
	</style>
  </body>
</html>