<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> -->
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
	
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/datagrid-filter.js"></script>
	<script type="text/javascript" src="resources/js/specification/allSpe.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/specification/addSpe.js"></script>
	<script type="text/javascript" src="resources/js/specification/destroySpe.js"></script>
	<script type="text/javascript" src="resources/js/specification/specificationtree.js"></script>
	<script type="text/javascript" src="resources/js/specification/control.js"></script>
	<script type="text/javascript" src="resources/js/specification/differentMachine.js"></script>
	<script type="text/javascript" src="resources/js/specification/comboboxCheck.js"></script>
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
	<script type="text/javascript" src="resources/js/web_socket.js"></script>
	<style type="text/css">
		table tr td{
			font-size:12px;
			height:30px;
		}
		.leftTd{
			text-align: right;
			width : 150px;
		}
		.rightTd{
			text-align: left;
			width: 200px;
		}
	</style>
  </head>
  
  <body class="easyui-layout">
  	<jsp:include  page="../specificationtree.jsp"/>
  	<div  id="bodys" region="center"  hide="true"  split="true" >
  		<div id=bodyy style="text-align:center"><p>欢迎使用！请先选择焊机。。。</p></div>
  		<div id="body">
  			<a style="font-size:20px;" href="javascript:parameter();">参数管理</a><span style="font-size:20px;">|</span><a style="font-size:20px;" href="javascript:control();">控制管理</a>
  			<form id="fm" class="easyui-form" method="post" data-options="novalidate:true">
	        	<div region="left">
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable>通道号：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" name="fchanel" id="fchanel" data-options="editable:false">
				                	<option value="1">通道号1</option>
								    <option value="2">通道号2</option>
								    <option value="3">通道号3</option>
								    <option value="4">通道号4</option>
								    <option value="5">通道号5</option>
								    <option value="6">通道号6</option>
								    <option value="7">通道号7</option>
								    <option value="8">通道号8</option>
								    <option value="9">通道号9</option>
								    <option value="10">通道号10</option>
								    <option value="11">通道号11</option>
								    <option value="12">通道号12</option>
								    <option value="13">通道号13</option>
								    <option value="14">通道号14</option>
								    <option value="15">通道号15</option>
								    <option value="16">通道号16</option>
								    <option value="17">通道号17</option>
								    <option value="18">通道号18</option>
								    <option value="19">通道号19</option>
								    <option value="20">通道号20</option>
								    <option value="21">通道号21</option>
								    <option value="22">通道号22</option>
								    <option value="23">通道号23</option>
								    <option value="24">通道号24</option>
								    <option value="25">通道号25</option>
								    <option value="26">通道号26</option>
								    <option value="27">通道号27</option>
								    <option value="28">通道号28</option>
								    <option value="29">通道号29</option>
								    <option value="30">通道号30</option>
				                </select>
				            </td>
			  				<td></td>
			  				<td></td>
			  			</tr>
	            	</table>
	            </div>
        	    <div >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable>初期条件：</lable></td>
			  				<td class="rightTd"><input name="finitial" id="finitial" type="checkbox" value="1"/></td>
			  				<td class="leftTd"><lable>熔深控制：</lable></td>
			  				<td class="rightTd"><input name="fcontroller" id="fcontroller" type="checkbox" value="1"/></td>
			  			</tr>
	            	</table>
	            </div>
	            <div >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>一元/个别：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" name="fselect" id="fselect" data-options="editable:false" onChange="changeValue(current,old)">
				                    <option value="102">个别</option>
								    <option value="101">一元</option>
				                </select>
			  				</td>
			  				<td class="leftTd"><lable><span class="required">*</span>收弧：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" name="farc" id="farc" data-options="editable:false">
				                	<option value="111">无</option>
								    <option value="112">有</option>
								    <option value="113">反复</option>
								    <option value="114">点焊</option>
				                </select>
			  				</td>
			  			</tr>
	            	</table>
	            </div>
	            <div >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>电弧特性：</lable></td>
			  				<td class="rightTd"><input id="fcharacter" name="fcharacter" class="easyui-numberbox" data-options="required:true">(±1)</td>
			  				<td id="dmodel" class="leftTd"><lable>柔软电弧模式：</lable></td>
			  				<td id="imodel" class="rightTd"><input name="fmode" id="fmode" type="checkbox" value="1"></td>
			  				<td id="dtorch" class="leftTd"><lable>水冷焊枪：</lable></td>
			  				<td id="itorch" class="rightTd"><input name="ftorch" id="ftorch" type="checkbox" value="0"></td>
			  			</tr>
	            	</table>
	            </div>
	            <div >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>点焊时间：</lable></td>
			  				<td class="rightTd"><input name="ftime" id="ftime" value="30.0" class="easyui-numberbox" data-options="required:true,precision:1">(s)</td>
			  				<td class="leftTd"><lable><span class="required">*</span>焊丝材质：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" name="fmaterial" id="fmaterial" data-options="editable:false">
				                	<option value="91">低碳钢实心</option>
								    <option value="92">不锈钢实心</option>
								    <option value="93">低碳钢药芯</option>
								    <option value="94">不锈钢药芯</option>
				                </select>
				            </td>
			  			</tr>
	            	</table>
	            </div>
	            <div >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>提前送气：</lable></td>
			  				<td class="rightTd"> <input name="fadvance" id="fadvance" class="easyui-numberbox" data-options="required:true,precision:1">(s)</td>
			  				<td class="leftTd"><lable><span class="required">*</span>气体：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" name="fgas" id="fgas" data-options="editable:false">
				                	<option value="121">CO2</option>
								    <option value="122">MAG</option>
								    <option value="123">MIG</option>
				                </select>
				            </td>
				         </tr>
	            	</table>
	            </div>
	            <div >
	            	<table>
			            		<tr>
		  				<td class="leftTd"><lable><span class="required">*</span>滞后送气：</lable></td>
		  				<td class="rightTd"><input name="fhysteresis" id="fhysteresis" class="easyui-numberbox" data-options="required:true,precision:1">(s)</td>
		  				<td class="leftTd"><lable><span class="required">*</span>焊丝直径：</lable></td>
		  				<td class="rightTd">
		  					<select class="easyui-combobox" name="fdiameter" id="fdiameter" data-options="editable:false">
			                	<option value="131">Φ1.0</option>
			                	<option value="132">Φ1.2</option>
			                	<option value="133">Φ1.4</option>
			                	<option value="134">Φ1.6</option>
			                </select>
		  				</td>
		  			</tr>
	            	</table>
	            </div>
	            <div >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>初期电流：</lable></td>
			  				<td class="rightTd"><input name="fini_ele" id="fini_ele" class="easyui-numberbox" value="100.0" data-options="required:true">(A)</td>
			  				<td class="leftTd"><lable><span class="required">*</span>焊接电流：</lable></td>
			  				<td class="rightTd"><input name="fweld_ele" id="fweld_ele" class="easyui-numberbox" data-options="required:true" >(A)</td>
			  			</tr>
	            		<tr>
			  				<td class="leftTd"><lable><lable><span class="required">*</span>收弧电流：</lable></td>
			  				<td class="rightTd"><input name="farc_ele" id="farc_ele" class="easyui-numberbox" value="100.0" data-options="required:true">(A)</td>
			  				<td class="leftTd"></td>
			  				<td class="rightTd"></td>
			  			</tr>
	            	</table>
	            </div>
	            <div id="gebie1" >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>初期电压：</lable></td>
			  				<td class="rightTd"><input name="fini_vol" id="fini_vol" class="easyui-numberbox" value="19.0" data-options="required:true,precision:1">(V)</td>
			  				<td class="leftTd"><lable><span class="required">*</span>焊接电压：</lable></td>
			  				<td class="rightTd"><input name="fweld_vol" id="fweld_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
			  			</tr>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>收弧电压：</lable></td>
			  				<td class="rightTd"><input name="farc_vol" id="farc_vol" class="easyui-numberbox" value="19.0" data-options="required:true,precision:1">(V)</td>
			  				<!-- <td id="dratio" class="leftTd"><lable><span class="required">*</span>焊丝负极比率：</lable></td>
			  				<td id="iratio" class="rightTd"><input name="weldingratio" id="weldingratio" class="easyui-numberbox" data-options="required:true,precision:1">(%)</td> -->
			  				<td class="leftTd"></td>
			  				<td class="rightTd"></td>
			  			</tr>
	            	</table>
	            </div>
	            <div id="yiyuan1" >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>焊接电压（一元）：</lable></td>
			  				<td class="rightTd"><input name="fweld_vol1" id="fweld_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
			  				<td class="leftTd"><lable><span class="required">*</span>收弧电压（一元）：</lable></td>
			  				<td class="rightTd"><input name="farc_vol1" id="farc_vol1" class="easyui-numberbox" value="0" data-options="required:true">(±1)</td>
			  			</tr>
			  			<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>初期电压（一元）：</lable></td>
			  				<td class="rightTd"><input name="fini_vol1" id="fini_vol1" class="easyui-numberbox" value="0" data-options="required:true">(±1)</td>
			  				<td class="leftTd"></td>
			  				<td class="rightTd"></td>
			  			</tr>
	            	</table>
	            </div>
	            <div id="yiyuan2" >
		            <table>
		            	<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>焊接电流微调：</lable></td>
			  				<td class="rightTd"><input name="fweld_tuny_ele" id="fweld_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
			  				<td class="leftTd"><lable><span class="required">*</span>收弧电流微调：</lable></td>
			  				<td class="rightTd"><input name="farc_tuny_ele" id="farc_tuny_ele" class="easyui-numberbox" value="0.0" data-options="required:true">(A)</td>
			  			</tr>
		            </table>
	            </div>
	            <div id="gebie3" >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>焊接电压微调：</lable></td>
			  				<td class="rightTd"><input name="fweld_tuny_vol" id="fweld_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
			  				<td class="leftTd"><lable><span class="required">*</span>收弧电压微调：</lable></td>
			  				<td class="rightTd"><input name="farc_tuny_vol" id="farc_tuny_vol" class="easyui-numberbox" value="0.0" data-options="required:true,precision:1">(V)</td>
			  			</tr>
	            	</table>
	            </div>
	            <div id="yiyuan3" >
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>焊接电压微调(一元)：</lable></td>
			  				<td class="rightTd"><input name="fweld_tuny_vol1" id="fweld_tuny_vol1" class="easyui-numberbox" data-options="required:true">(%)</td>
			  				<td class="leftTd"><lable><span class="required">*</span>收弧电压微调（一元）：</lable></td>
			  				<td class="rightTd"><input name="farc_tuny_vol1" id="farc_tuny_vol1" class="easyui-numberbox" value="0" data-options="required:true">(%)</td>
			  			</tr>
	            	</table>
	            </div>
	            <div id="xinzeng" >
	            	<table>
	            		<tr>
			  				<td id="dfrequency" class="leftTd"><lable><span class="required">*</span>双脉冲频率：</lable></td>
			  				<td id="ifrequency" class="rightTd"><input name="frequency" id="frequency" class="easyui-numberbox" data-options="required:true,precision:1">(Hz)</td>
			  				<td id="dgasflow" class="leftTd"><lable><span class="required">*</span>气体流量：</lable></td>
			  				<td id="igasflow" class="rightTd"><input name="gasflow" id="gasflow" class="easyui-numberbox" value="0" data-options="required:true,precision:1">(L/min)</td>
<!-- 			  				<td class="leftTd"><lable><span class="required">*</span>焊接过程：</lable></td>
			  				<td class="rightTd"><select class="easyui-combobox" name="fweldprocess" id="fweldprocess" data-options="editable:false"></select></td> -->
			  			</tr>
	            	</table>
	            </div>
	            <div>
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable><span class="required">*</span>焊接过程：</lable></td>
			  				<td class="rightTd"><select class="easyui-combobox" name="fweldprocess" id="fweldprocess" data-options="editable:false"></select></td>
			  				<td id="dratio" class="leftTd"><lable><span class="required">*</span>焊丝负极比率：</lable></td>
			  				<td id="iratio" class="rightTd"><input name="weldingratio" id="weldingratio" class="easyui-numberbox" data-options="required:true">(%)</td>
			  			</tr>
	            	</table>
	            </div>
	            <div algin="center">
	            	<table aligin="center">
	            		<tr>
			  				<td class="leftTd"></td>
			  				<td style="text-align: center;width: 350px;">
			  					<a href="javascript:suoqu();" class="easyui-linkbutton" iconCls="icon-ok">索取规范</a>
			  					<a href="javascript:save(0);" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			  					<a href="javascript:xiafa();" class="easyui-linkbutton" iconCls="icon-ok">下发规范</a>
			  					<a href="javascript:chushihua(0);" class="easyui-linkbutton" iconCls="icon-ok">恢复默认值</a>
			  					<a href="javascript:copy(1);" class="easyui-linkbutton" iconCls="icon-ok">焊机参数复制</a>
			  					<a href="javascript:copy(0);" class="easyui-linkbutton" iconCls="icon-ok">单通道复制</a></td>
			  				<td class="rightTd"></td>
			  			</tr>
	            	</table>
				</div>
			</form>
			<form hidden="true" id="cfm" class="easyui-form" method="post" data-options="novalidate:true">
				<div region="left">
					<table width="50%" height="30%" border="1" style="text-align: center;">
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
							<a href="javascript:controlfun();" class="easyui-linkbutton" iconCls="icon-ok">下发控制命令</a>
							<a href="javascript:openPassDlg();" class="easyui-linkbutton" iconCls="icon-ok">密码下发</a>			
						</td>
					  </tr>
					</table>
				</div>
			</form>
	    </div>
	    <div id="divro" class="easyui-dialog" style="width:500px;height:490px" closed="true" buttons="#dlg-ro"algin="center">
	    	<div style="text-align:center;height:25px">
	    		<lable id="mu"></lable>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    		所属班组：<select class="easyui-combobox" name="item" id="item" data-options="editable:false" onChange="changeValue(current,old)"></select>
	    	</div>
	    	<div id="tab" style="text-align:center;height:385px;width:485px">
	    		<table id="ro" style="table-layout:fixed;width:100%;" ></table>
	    	</div>
        </div>
        <div id="dlg-ro">
			<a href="javascript:savecopy();" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
			<a href="javascript:$('#divro').dialog('close');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<div id="divro1" class="easyui-dialog" style="width:600px;height:400px" closed="true" buttons="#dlg-ro1"algin="center">
	        <table id="ro1" style="table-layout:fixed;width:100%;" ></table>
        </div>
        <div id="dlg-ro1">
			<a href="javascript:$('#divro1').dialog('close');" class="easyui-linkbutton" iconCls="icon-ok">确定</a>
			<a href="javascript:$('#divro1').dialog('close');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<div id="pwd" class="easyui-dialog" style="text-align:center;width:400px;height:200px" closed="true" buttons="#dlg-pwd"algin="center">
	        <br><br><lable><span class="required">*</span>密码：</lable>
	        <input name="passwd" id="passwd" class="easyui-numberbox" data-options="required:true,min:1,max:999"><br/>
	        <lable style="color:red;">（注：密码范围是1~999）</lable>
        </div>
        <div id="dlg-pwd">
			<a href="javascript:passfun();" class="easyui-linkbutton" iconCls="icon-ok">确定下发</a>
			<a href="javascript:$('#pwd').dialog('close');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<div id="fnsbody">
<!-- 			<div style="width:50%;float: left;">
				<a href="javascript:openAddJobNoDlg(0);" class="easyui-linkbutton" iconCls="icon-newadd">新增Job</a>
			</div> -->
			<div style="width:50%;">
				<a href="javascript:saveJobDetail(0);" class="easyui-linkbutton" iconCls="icon-ok">保存调整</a>
				<a href="javascript:deleteAdjustment();" class="easyui-linkbutton" iconCls="icon-delete">删除调整</a>
				<a href="javascript:openAddJobNoDlg(1);" class="easyui-linkbutton" iconCls="icon-save">另存为...</a>
				<!-- <a href="javascript:deleteJob();" class="easyui-linkbutton" iconCls="icon-delete">删除job</a> -->
			</div>
			<div id="joblistdiv" style="overflow-x: auto; overflow-y: auto;height: 500px;width:50%;float: left;">
				<table id="fnsjoblist" style="table-layout:fixed;width:95%;" ></table>
			</div>
			<div style="overflow-x: auto; overflow-y: auto;height: 500px;width:49%;float: left;">
				<form id="jobdetailform" class="easyui-form" method="post" data-options="novalidate:true">
					<table id="fnsjobdetail" style="table-layout:fixed;width:100%;height: 95%" border="1">
					<tr>
					    <th>参数</th>
					    <th>数值</th>
					    <th>将数值修改为</th>
					    <th>调节范围</th>
					</tr>
					<tr bgcolor="#ececec">
						<td>Job名称</td>
						<td id="f024"></td>
						<td><input id="f0241" name="f0241" class="easyui-textbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true"></td>
						<td></td>
					</tr>
					<tr>
						<td>Job号码</td>
						<td id="tdjobno"></td>
						<td><input id="injobno" name="injobno" class="easyui-textbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true"></td>
						<td></td>
					</tr>
					<tr>
						<td>焊接模式</td>
						<td id="f026"></td>
						<td></td>
						<td></td>
					</tr>
					<tr bgcolor="#ececec">
						<td>操作模式</td>
						<td id="f001"></td>
						<td><select class="easyui-combobox" name="f0011" id="f0011" style="width: 100%;height: 100%;" data-options="required:true,editable:false">
							  <option value ="_2-step">_2-step</option>
							  <option value ="_4-step">_4-step</option>
							  <option value="_S2-step">_S2-step</option>
							  <option value="_S4-step">_S4-step</option>
						</select></td>
						<td></td>
					</tr>
					<tr bgcolor="#ececec">
						<td>焊接材料</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td>焊丝直径</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr bgcolor="#ececec">
						<td>气体</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>		
					<tr>
						<td>性能</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr bgcolor="#ececec">
						<td>特性曲线-ID</td>
						<td id="f051"></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td>送丝速度</td>
						<td id="f025"></td>
						<td><input class="easyui-numberbox" name="f0251" id="f0251" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td id="f0252"></td>
					</tr>
					<tr bgcolor="#ececec">
						<td>电流</td>
						<td id="f051"></td>
						<td id="f0511"></td>
						<td>45 - 320</td>
					</tr>
					<tr>
						<td>电压</td>
						<td id="f053"></td>
						<td id="f0531"></td>
						<td>18.1 - 28.7</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>板厚</td>
						<td id="f052"></td>
						<td id="f0521"></td>
						<td>1.5 - 16.8</td>
					</tr>	
					<tr>
						<td>弧长修正</td>
						<td id="f002"></td>
						<td><input name="f0021" id="f0021" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:-10,max:10"></td>
						<td>-10 - 10</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>电弧挺度</td>
						<td id="f003"></td>
						<td><input name="f0031" id="f0031" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:-10,max:10"></td>
						<td>-10 - 10</td>
					</tr>
					<tr>
						<td>恒熔深控制</td>
						<td id="f027"></td>
						<td><input id="f0271" name="f0271" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0 - 10</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>弧长自适应</td>
						<td id="f028"></td>
						<td><input id="f0281" name="f0281" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0 - 5</td>
					</tr>
					<tr>
						<td>预送气</td>
						<td id="tdfadvance"></td>
						<td><input id="infadvance" name="infadvance" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1,min:0,max:9.9"></td>
						<td>0 - 9.9</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>滞后送气</td>
						<td id="tdfhysteresis"></td>
						<td><input id="infhysteresis" name="infhysteresis" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1,min:0,max:9.9"></td>
						<td>0 - 9.9</td>
					</tr>
					<tr>
						<td>点动送丝速度</td>
						<td id="f029"></td>
						<td><input id="f0291" name="f0291" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0.5 - 100</td>
					</tr>		
					<tr bgcolor="#ececec">
						<td>起弧电流</td>
						<td id="f004"></td>
						<td><input id="f0041" name="f0041" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:0,max:200"></td>
						<td>0 - 200</td>
					</tr>
					<tr>
						<td>起弧电流弧长修正</td>
						<td id="f005"></td>
						<td><input id="f0051" name="f0051" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:-10,max:10"></td>
						<td>-10 - 10</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>起弧电流时间</td>
						<td id="f006"></td>
						<td><select id="f0061" name="f0061" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1,min:0.1,max:10">
						<option value ="off">关</option>
						</select></td>
						<td>0.1 - 10</td>
					</tr>
					<tr>
						<td>电流渐变时间1</td>
						<td id="f007"></td>
						<td><input id="f0071" name="f0071" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1,min:0,max:9.9"></td>
						<td>0 - 9.9</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>电流渐变时间2</td>
						<td id="f008"></td>
						<td><input id="f0081" name="f0081" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1,min:0,max:9.9"></td>
						<td>0 - 9.9</td>
					</tr>
					<tr>
						<td>收弧电流</td>
						<td id="f009"></td>
						<td><input id="f0091" name="f0091" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:0,max:200"></td>
						<td>0 - 200</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>收弧电流弧长修正</td>
						<td id="f010"></td>
						<td><input id="f0101" name="f0101" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:-10,max:10"></td>
						<td>-10 - 10</td>
					</tr>
					<tr>
						<td>收弧电流时间</td>
						<td id="f011"></td>
						<td><select id="f0111" name="f0111" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1,min:0.1,max:10">
						<option value ="off">关</option>
						</select></td>
						<td>0.1 - 10</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>SFI</td>
						<td id="f012"></td>
						<td><select id="f0121" name="f0121" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,editable:false">
						<option value ="off">关</option>
						<option value ="on">开</option>
						</select></td>
						<td></td>
					</tr>
					<tr>
						<td>SFI 热起弧</td>
						<td id="f013"></td>
						<td><select id="f0131" name="f0131" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:2,min:0.01,max:2">
						<option value ="off">关</option>
						</select>
						</td>
						<td>0.01 - 2</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>焊丝回抽</td>
						<td id="f014"></td>
						<td><input id="f0141" name="f0141" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:0,max:10,precision:1"></td>
						<td>0 - 10</td>
					</tr>
					<tr>
						<td>Synchropulse 可用</td>
						<td id="f015"></td>
						<td><select id="f0151" name="f0151" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,editable:false">
						<option value ="off">关</option>
						<option value ="on">开</option>
						</select></td>
						<td></td>
					</tr>
					<tr bgcolor="#ececec">
						<td>双脉冲送丝速度幅值</td>
						<td id="f030"></td>
						<td><input id="f0301" name="f0301" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0.1 - 6</td>
					</tr>		
					<tr>
						<td>双脉冲频率</td>
						<td id="f031"></td>
						<td><input id="f0311" name="f0311" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0.5 - 3</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>双脉冲占空比</td>
						<td id="f032"></td>
						<td><input id="f0321" name="f0321" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>10 - 90</td>
					</tr>
					<tr>
						<td>双脉冲峰值弧长修正</td>
						<td id="f033"></td>
						<td><input id="f0331" name="f0331" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>-10 - 10</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>双脉冲谷值弧长修正</td>
						<td id="f034"></td>
						<td><input id="f0341" name="f0341" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>-10 - 10</td>
					</tr>
					<tr>
						<td>PMC MIX高电流时间修正</td>
						<td id="f016"></td>
						<td><input id="f0161" name="f0161" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:-10,max:10"></td>
						<td>-10 - 10</td>
					</tr>
					<tr bgcolor="#ececec">
						<td>低能量周期</td>
						<td id="f017"></td>
						<td><input id="f0171" name="f0171" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:-10,max:10"></td>
						<td>-10 - 10</td>
					</tr>	
					<tr>
						<td>低能量调节</td>
						<td id="f018"></td>
						<td><input id="f0181" name="f0181" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:-10,max:10"></td>
						<td>-10 - 10</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>能量上限</td>
						<td id="f035"></td>
						<td><input id="f0351" name="f0351" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0 - 20</td>
					</tr>	
					<tr>
						<td>低能量范围调节</td>
						<td id="f036"></td>
						<td><input id="f0361" name="f0361" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>-20 - 0</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>电弧长度上限</td>
						<td id="f037"></td>
						<td><input id="f0371" name="f0371" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0 - 10</td>
					</tr>	
					<tr>
						<td>电弧长度下限</td>
						<td id="f038"></td>
						<td><input id="f0381" name="f0381" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>-10 - 0</td>
					</tr>		
					<tr bgcolor="#ececec">
						<td>mig 气阀</td>
						<td id="f039"></td>
						<td><input id="f0391" name="f0391" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0.5 - 30</td>
					</tr>	
					<tr>
						<td>mig气体因素</td>
						<td id="f040"></td>
						<td><select id="f0401" name="f0401" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1">
						<option value ="auto">auto</option>
						</select></td>
						<td>0.9 - 20</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>Job渐变</td>
						<td id="f019"></td>
						<td><input id="f0191" name="f0191" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:0,max:10"></td>
						<td>0 - 10</td>
					</tr>	
					<tr>
						<td>数据归档间隔时间</td>
						<td id="f041"></td>
						<td><select id="f0411" name="f0411" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1">
						<option value ="off">关</option>
						</select></td>
						<td>0.1 - 100</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>lm_voltagecv</td>
						<td id="f042"></td>
						<td><input id="f0421" name="f0421" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0 - 100</td>
					</tr>	
					<tr>
						<td>lm_电压下限</td>
						<td id="f020"></td>
						<td><input id="f0201" name="f0201" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:-10,max:0,precision:1"></td>
						<td>-10 - 0</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>lm_电压上限</td>
						<td id="f021"></td>
						<td><input id="f0211" name="f0211" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:0,max:10,precision:1"></td>
						<td>0 - 10</td>
					</tr>	
					<tr>
						<td>lm_电压最大持续时间</td>
						<td id="f043"></td>
						<td><select id="f0431" name="f0431" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1">
						<option value ="off">关</option>
						</select></td>
						<td>0.1 - 10</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>lm_currentcv</td>
						<td id="f044"></td>
						<td><input id="f0441" name="f0441" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0 - 1000</td>
					</tr>	
					<tr>
						<td>lm_电流下限</td>
						<td id="f022"></td>
						<td><input id="f0221" name="f0221" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:-100,max:0"></td>
						<td>-100 - 0</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>lm_电流上限</td>
						<td id="f023"></td>
						<td><input id="f0231" name="f0231" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,min:0,max:100"></td>
						<td>0 - 100</td>
					</tr>	
					<tr>
						<td>lm_电流大持续时间</td>
						<td id="f045"></td>
						<td><select id="f0451" name="f0451" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1">
						<option value ="off">关</option>
						</select></td>
						<td>0.1 - 10</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>lm_feedercv</td>
						<td id="f046"></td>
						<td><input id="f0461" name="f0461" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0 - 50</td>
					</tr>	
					<tr>
						<td>lm_送丝速度下限</td>
						<td id="f047"></td>
						<td><input id="f0471" name="f0471" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>-10 - 0</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>lm_送丝速度上限</td>
						<td id="f048"></td>
						<td><input id="f0481" name="f0481" class="easyui-numberbox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1"></td>
						<td>0 - 10</td>
					</tr>				
					<tr>
						<td>lm_最大送丝速度持续时间</td>
						<td id="f049"></td>
						<td><select id="f0491" name="f0491" class="easyui-combobox" style="width: 100%;height: 100%;outline: none;" data-options="required:true,precision:1">
						<option value ="off">关</option>
						</select></td>
						<td>0.1 - 10</td>
					</tr>	
					<tr bgcolor="#ececec">
						<td>lm_超限动作</td>
						<td id="f050"></td>
						<td><select id="f0501" name="f0501" class="easyui-combobox" style="width: 100%;height: 100%;background:#ececec;" data-options="required:true,precision:1">
						<option value ="ignore">ignore</option>
						<option value ="warning">warning</option>
						<option value ="error">error</option>
						</select></td>
						<td></td>
					</tr>																																																																																					
				</table>
			</form>
			</div>	
		</div>
		
		<!-- 新增job -->
		<div id="addjobdg" class="easyui-dialog" style="width: 360px; height: 420px; padding:10px 20px" closed="true" buttons="#addjob-buttons">
			<form id="addjobfm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
	            <div class="fitem">
	            	<lable>选择Job号：</lable>
	                <select class="easyui-combobox" name="jobno" id="jobno" data-options="editable:false"></select>
	            </div>
	            <div class="fitem">
	            	<lable><span class="required">*</span>Job名称：</lable>
	                <input id="jobname" name="jobname" class="easyui-textbox" data-options="required:true">
	            </div>
	            <div class="fitem">
					<lable><span class="required">*</span>焊接材料</lable>
					<select class="easyui-combobox" name="tpsiMaterial" id="tpsiMaterial" data-options="required:true,editable:false"></select>
	        	</div>
	        	<div class="fitem">
					<lable><span class="required">*</span>焊丝直径</lable>
					<select class="easyui-combobox" name="tpsiWire" id="tpsiWire" data-options="required:true,editable:false"></select>
	        	</div>
	        	<div class="fitem">
					<lable><span class="required">*</span>气体</lable>
					<select class="easyui-combobox" name="tpsiGas" id="tpsiGas" data-options="required:true,editable:false"></select>
	        	</div>
	        	<div class="fitem">
					<lable><span class="required">*</span>特性曲线-ID</lable>
					<select class="easyui-combobox" name="Synergic" id="Synergic" data-options="required:true"></select>
	        	</div>
			</form>
		</div>
		<div id="addjob-buttons">
			<a href="javascript:selectFun();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:closeDialog('addjobdg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
    </div>
</body>
</html>
 