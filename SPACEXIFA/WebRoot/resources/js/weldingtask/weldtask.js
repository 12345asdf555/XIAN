$(function(){
	weldedJunctionDatagrid();
//	dayinDatagrid();
//	exporttable();
	statusChange();
	resultCombobox();
	wpslibCombobox();
	welderCombobox();
});

function statusChange(){
	$("#status").combobox({
		onChange : function(newValue,oldValue){
			var searchStr = "";
			if(newValue==1){
				searchStr = " foperatetype=1";
			}else if(newValue==0){
				searchStr = " (foperatetype=0 or foperatetype=2)";
			}else if(newValue==3){
				searchStr = " foperatetype is null"
			}
			$("#weldTaskTable").datagrid('reload',{
				"searchStr" : searchStr
			})
		}
	})
	
}

function weldedJunctionDatagrid(){
	$("#weldTaskTable").datagrid( {
//		fitColumns : true,
//		view: detailview,
		height : $("#body").height(),
		width : $("#body").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getWeldTaskList",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [ {
			field : 'id',
			title : '序号',
			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'weldedJunctionno',
			title : '任务编号',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'startTime',
			title : '计划开始时间',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fengineering_symbol',
			title : '工程符号',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'fweld_method',
			title : '焊接方法',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fweld_position',
			title : '焊接位置',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'fbase_material_type',
			title : '母材型号',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'fweld_material_model',
			title : '焊材型号',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'fwelder_name',
			title : '分配焊工',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fwelder_id',
			title : '焊工id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'ftechnological_design',
			title : '工艺设计',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fwarm_up_requirement',
			title : '预热要求',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'finter_channel_temperature',
			title : '道间温度',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fcarbon_requirement',
			title : '碳刨要求',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fpost_heat_requirement',
			title : '后热要求',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fannealed_weld',
			title : '退火焊道',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fassembly_clearance',
			title : '装配间隙',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fcarbon_depth',
			title : '碳刨深度',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fcarbon_width',
			title : '碳刨宽度',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fpost_heat_temperature',
			title : '后热温度',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fafter_hot_time',
			title : '后热时间',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fwps_lib_name',
			title : '工艺库名称',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'fwpslib_id',
			title : '工艺库id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'iid',
			title : '项目id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'iname',
			title : '所属班组',
//			width : 150,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'status',
			title : '状态值',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},/*{
			field : 'operatetype',
			title : '任务状态',
			width : 90,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = "";
				if(row.status==0){
					str = '<a id="confirm" class="easyui-linkbutton" href="javascript:confirm()" disabled="true"/>';
				}
				if(row.status==1){
					str = '<a id="confirm1" class="easyui-linkbutton" href="javascript:confirm()" disabled="true"/>';
				}
				if(row.status==2){
					str = '<a id="confirm2" class="easyui-linkbutton" href="javascript:confirm()" disabled="true"/>';
				}
				return str;
			}
		},*/{
			field : 'edit',
			title : '编辑',
			width : 300,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = '<a id="edit" class="easyui-linkbutton" href="javascript:editWeldedjunction()"/>';
				str += '<a id="remove" class="easyui-linkbutton" href="javascript:removeWeldedjunction()"/>';
				if(row.status==0){
					str += '<a id="confirm" class="easyui-linkbutton" href="javascript:confirm() disabled="true"/>';
				}
				if(row.status==1){
					str += '<a id="confirm1" class="easyui-linkbutton" href="javascript:confirm()"/>';
				}
				if(row.status==2){
					str += '<a id="confirm2" class="easyui-linkbutton" href="javascript:confirm()" disabled="true"/>';
				}
//				str += '<a id="evaluation" class="easyui-linkbutton" href="javascript:evaluation()"/>';
				return str;
			}
		}] ],
		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		onLoadSuccess: function(data){
	        $("a[id='edit']").linkbutton({text:'修改',plain:true,iconCls:'icon-update'});
	        $("a[id='remove']").linkbutton({text:'删除',plain:true,iconCls:'icon-delete'});
	        if($("#confirm").length!=0){
				$("a[id='confirm']").linkbutton({text:'进行中',plain:true,iconCls:'icon-unfinished'});
			};
			if($("#confirm1").length!=0){
				$("a[id='confirm1']").linkbutton({text:'已完成，可取消',plain:true,iconCls:'icon-finish'});
			};
			if($("#confirm2").length!=0){
				$("a[id='confirm2']").linkbutton({text:'未领取',plain:true,iconCls:'icon-assign'});
			};
//			$("a[id='evaluation']").linkbutton({text:'评价',plain:true,iconCls:'icon-newadd'});
		}
/*		detailFormatter:function(index,row2){//严重注意喔
			return '<div"><table id="ddv-' + index + '" style=""></table></div>';
		},
		onExpandRow: function(index,row){//嵌套第一层，严重注意喔
			var ddv = $(this).datagrid('getRowDetail',index).find('#ddv-'+index);//严重注意喔
			ddv.datagrid({
//				fitColumns : true,
				idField : 'id',
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ],
				url : "weldtask/getRealWelder?searchStr="+row.id,
				singleSelect : true,
				rownumbers : true,
				showPageList : false,
				columns : [ [ { 
					field : 'id',
					title : '序号',
					width : 30,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'welderno',
					title : '焊工编号',
					halign : "center",
					align : "left",
					width : 200
				}, {
					field : 'weldername',
					title : '焊工姓名',
					halign : "center",
					align : "left",
					width : 200
				}
				] ],
				pagination : true,
				onResize:function(){
					$('#weldTaskTable').datagrid('fixDetailRowHeight',index);
				},
				onLoadSuccess:function(){
					$('#weldTaskTable').datagrid("selectRow", index)
					setTimeout(function(){
						$('#weldTaskTable').datagrid('fixDetailRowHeight',index);
					},0);
				}
			});
			$('#weldTaskTable').datagrid('fixDetailRowHeight',index);
		}*/
	});
}

function dayinDatagrid(){
	$("#dayintable").datagrid( {
//		fitColumns : true,
		height : $("#dayin").height(),
		width : $("#dayin").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getWeldTaskList",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [ {
			field : 'id',
			title : '序号',
			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'weldedJunctionno',
			title : '任务编号',
//			width : 90,
			halign : "center",
			align : "left"
		}, /*{
			field : 'serialNo',
			title : '任务描述',
//			width : 90,
			halign : "center",
			align : "left"
		}, */{
			field : 'levelname',
			title : '任务等级',
//			width : 150,
			halign : "center",
			align : "left"
		}, /*{
			field : 'pipelineNo',
			title : '预设焊工',
//			width : 90,
			halign : "center",
			align : "left"
		},*/{
			field : 'realwelder',
			title : '实际焊工',
//			width : 90,
			halign : "center",
			align : "left"
		}, /*{
			field : 'roomNo',
			title : '焊工资质',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'welderid',
			title : '焊工id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'quali',
			title : '资质id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},*/{
			field : 'itemid',
			title : '项目id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'itemname',
			title : '所属班组',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'levelid',
			title : '任务等级id',
//			width : 150,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'dtoTime1',
			title : '计划开始时间',
//			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'dtoTime2',
			title : '计划结束时间',
//			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'dyne',
			title : '焊工id',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'status',
			title : '状态值',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}] ],
		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        }
	});
}

function exporttable(){
	$("#exporttable").datagrid( {
//		fitColumns : true,
		height : $("#exportdlg").height(),
		width : $("#exportdlg").width(),
		idField : 'id',
//		pageSize : 10,
//		pageList : [ 10, 20, 30, 40, 50 ],
		url : "import/",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [ {
			field : 'taskNo',
			title : '任务编号',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'start',
			title : '计划开始时间',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fengineering_symbol',
			title : '工程符号',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fweld_method',
			title : '焊接方法',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fweld_position',
			title : '焊接位置',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fbase_material_type',
			title : '母材型号',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fweld_material_model',
			title : '焊材型号',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'personnum',
			title : '焊工编号',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'personid',
			title : '焊工id',
//			width : 150,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'itemid',
			title : '组织机构id',
//			width : 150,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'ftechnological_design',
			title : '工艺设计',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fwarm_up_requirement',
			title : '预热要求',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'finter_channel_temperature',
			title : '道间温度',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fcarbon_requirement',
			title : '碳刨要求',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fpost_heat_requirement',
			title : '后热要求',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fannealed_weld',
			title : '退火焊道',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fassembly_clearance',
			title : '装配间隙',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fcarbon_depth',
			title : '碳刨深度',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fcarbon_width',
			title : '碳刨宽度',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fpost_heat_temperature',
			title : '后热温度',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'fafter_hot_time',
			title : '后热时间',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'wpsname',
			title : '工艺库名称',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'wpsid',
			title : '工艺库id',
//			width : 150,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'str',
			title : '错误描述',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'flag',
			title : '标志位',
//			width : 150,
			halign : "center",
			align : "left",
			hidden : true
		}] ]
	});
}

function openDayin(){
	dayinDatagrid();
	$('#dayin').dialog('open');
}

//打印
function printWeldedjunction(){
	CreateFormPage("datagrid",$("#dayintable"),"任务列表");
	$('#dayin').dialog('close');
}

//导入
function importclick(){
	$("#importdiv").dialog("open").dialog("setTitle","从excel导入数据");
}

function importWeldingMachine(){
	var file = $("#file").val();
	if(file == null || file == ""){
		$.messager.alert("提示", "请选择要上传的文件！");
		return false;
	}else{
		document.getElementById("load").style.display="block";
		var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
		$("#body").append(sh);
		document.getElementById("show").style.display="block";
		$('#importfm').form('submit', {
			url : "import/importWeldTask",
			success : function(result) {
				if(result){
					var result = eval('(' + result + ')');
					if (result) {
			    		document.getElementById("load").style.display ='none';
			    		document.getElementById("show").style.display ='none';
						$('#importdiv').dialog('close');
						$('#exportdlg').window( {
							title : "任务确认与导入",
							modal : true
						});
						if(result.biaozhi==1){
							$('#imexcel').linkbutton('disable');
						}else{
							$('#imexcel').linkbutton('enable');
						}
						$('#exportdlg').window('open');
						exporttable();
						$("#exporttable").datagrid("loadData", result.rows);
					}
				}
				
			},  
		    error : function(errorMsg) {  
		        alert("数据请求失败，请联系系统管理员!");  
		    } 
		});
		
	}
}

//确认完成
function confirm(){
	var url2="";
	var temp=3;
	$.messager.confirm('提示', '此操作将使任务完成状态变更为未领取状态，且部分数据会变为脏数据，是否确认?', function(flag) {
		if(flag){
			document.getElementById("load").style.display="block";
			var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
			$("#body").append(sh);
			document.getElementById("show").style.display="block";
			var row = $('#weldTaskTable').datagrid('getSelected');
			url = "weldtask/getEvaluate?id="+row.id+"&result="+""+"&taskid="+row.id+"&welderid="+null+"&machineid="+null;
			url2=url+"&resultid="+""+"&welderNo="+null+"&operateid="+temp+"&taskNo="+encodeURIComponent(row.weldedJunctionno)+"&machineNo="+null;
			$.ajax({  
			      type : "post",  
			      async : false,
			      url : url2,  
			      data : {},  
			      dataType : "json", //返回数据形式为json  
			      success : function(result) {
			          if (result) {
							var result = eval(result);
							if (!result.success) {
								document.getElementById("load").style.display ='none';
					    		document.getElementById("show").style.display ='none';
								$.messager.show( {
									title : 'Error',
									msg : result.msg
								});
							} else {
								document.getElementById("load").style.display ='none';
					    		document.getElementById("show").style.display ='none';
								$('#weldTaskTable').datagrid('reload');
							}
			          }  
			      },  
			      error : function(errorMsg) {  
			          alert("数据请求失败，请联系系统管理员!");  
			      }  
			 }); 

		}
	});
}

//获取当前时间并格式化
function getNowFormatDate() {
	  var date = new Date();
	  var seperator1 = "-";
	  var seperator2 = ":";
	  var month = date.getMonth() + 1;
	  var strDate = date.getDate();
	  if (month >= 1 && month <= 9) {
	      month = "0" + month;
	  }
	  if (strDate >= 0 && strDate <= 9) {
	      strDate = "0" + strDate;
	  }
	  var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	          + " " + date.getHours() + seperator2 + date.getMinutes()
	          + seperator2 + date.getSeconds();
	  return currentdate;
}

function evaluation(){
	var flag = 1;
	var row = $('#weldTaskTable').datagrid('getSelected');
	if(row.status!=1){
		 alert("任务未完成，无法进行评价"); 
	}else{
		if (row) {
			$('#mdlg').window( {
				title : "工作评价",
				modal : true
			});
			$('#mdlg').window('open');
			$('#mfm').form('load', row);
			if(row.resultid==0||row.resultid==""||row.resultid==null){
				var data = $('#resultid').combobox('getData');
				$('#resultid').combobox('select',data[0].value);
			}
			//$('#resultid').combobox('select', row.resultName);
			url = "weldtask/getEvaluate?id="+row.taskResultId+"&taskid="+row.id+"&welderid="+null+"&machineid="+null+"&starttime="+row.realStartTime+"&endtime="+row.realEndTime;
		}
	}
}

//评价等级下拉框
function resultCombobox(){
	$.ajax({  
      type : "post",  
      async : false,
      url : "weldtask/getStatusAll",  
      data : {},  
      dataType : "json", //返回数据形式为json  
      success : function(result) {
          if (result) {
              var optionStr = '';  
              for (var i = 0; i < result.ary.length; i++) { 
                  optionStr += "<option value=\"" + result.ary[i].id + "\" >"  
                          + result.ary[i].name + "</option>";  
              } 
              $("#resultid").append(optionStr);
          }  
      },  
      error : function(errorMsg) {  
          alert("数据请求失败，请联系系统管理员!");  
      }  
 }); 
	$("#resultid").combobox();
}

//评价的保存
function saveconment(){
	var temp;
	var url2;
	//提示转圈等待
	document.getElementById("load").style.display="block";
	var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
	$("#body").append(sh);
	document.getElementById("show").style.display="block";
	//var resultname=resultName.options[this.selectedIndex];
	var resultName = $('#resultid').combobox('getValue');
/*	alert(resultName.value);*/
	var result=document.getElementById("result").value;
	//alert(result.length);
  var rows = $("#weldTaskTable").datagrid("getSelections");
	temp=1;
	url2=url+"&result="+encodeURI(result)+"&resultid="+resultName+"&welderNo="+""+"&operateid="+temp+"&taskNo="+""+"&machineNo="+"";
	$('#mfm').form('submit', {
		url : url2,
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if (result) {
				var result = eval('(' + result + ')');
				if (!result.success) {
					document.getElementById("load").style.display ='none';
		    		document.getElementById("show").style.display ='none';
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					document.getElementById("load").style.display ='none';
		    		document.getElementById("show").style.display ='none';
					if(!result.msg==null){
						$.messager.alert("提示", messager);
					}
					$('#mdlg').dialog('close');
					$('#weldTaskTable').datagrid('reload');
				}
			}

		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

//任务批量完成
function complete(){
	
	$('#sdlg').window({
		title : "任务状态更改",
		modal : true
	});
	$('#sdlg').window('open');
	WeldingMachineDatagrid();
}
function WeldingMachineDatagrid() {
	$("#weg").datagrid( {
//		fitColumns : true,
		height : $("#sdlg").height(),
		width : $("#sdlg").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getTaskResultList?searchStr= foperatetype!=1",
		rownumbers : false,
		showPageList : false,
		checkOnSelect:true,
		selectOnCheck:true,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		},{ 
			field : 'id',
			title : '序号',
			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'taskNo',
			title : '任务编号',
			width : 70,
			halign : "center",
			align : "left"
		},{
			field : 'welderNo',
			title : '焊工编号',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
				
		}, {
			field : 'welderid',
			title : '焊工编号id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'machineNo',
			title : '焊机编号',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'machineid',
			title : '焊机编号id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
	},{
		field : 'taskid',
		title : '任务编号id',
		width : 90,
		halign : "center",
		align : "left",
		hidden:true
	},{
		field : 'operateid',
		title : '状态id',
		width : 90,
		halign : "center",
		align : "left",
		hidden:true
	},{
		field : 'starttime',
		title : '开始时间',
		width : 100,
		halign : "center",
		align : "left",
		hidden:true
    },{
		field : 'endtime',
		title : '结束时间',
		width : 100,
		halign : "center",
		align : "left",
		hidden:true
    }/*,{
			field : 'operatetype',
			title : '任务状态',
			width : 100,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = '<a id="confirm" href="javascript:dgConfirm()" class="easyui-linkbutton">';
				return str;
			}
		}*/
		] ],/*
		onLoadSuccess: function(data){
			$("a[id='confirm']").linkbutton({text:'确认完成',plain:true,iconCls:'icon-unfinished'});
			
		},*/
		toolbar : '#dlgSearch',
		pagination : true,
		fitColumns : true
	});
}

function saveWeldingnumber(){
	var url2="";
	var temp=1;
	$.messager.confirm('提示', '此操作不可撤销，是否确认?', function(flag) {
		if(flag){
//			document.getElementById("load").style.display="block";
//			var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
//			$("#body").append(sh);
//			document.getElementById("show").style.display="block";
			var row = $('#weg').datagrid('getSelections');
			var jsonStr = JSON.stringify(row)
			$.ajax({  
			      type : "post",  
			      async : false,
			      url : "weldtask/taskImportion", 
			      data : {taskstr:jsonStr},  
			      dataType : "json", //返回数据形式为json  
			      success : function(result) {
			          if (result) {
							var result = eval(result);
							if (!result.success) {
//								document.getElementById("load").style.display ='none';
//					    		document.getElementById("show").style.display ='none';
								$.messager.show( {
									title : 'Error',
									msg : result.errorMsg
								});
							} else {
//								document.getElementById("load").style.display ='none';
//					    		document.getElementById("show").style.display ='none';
								$('#weldTaskTable').datagrid('reload');
							}
						
			          }  
			      },  
			      error : function(errorMsg) {  
			          alert("数据请求失败，请联系系统管理员!");  
			      }  
			 }); 

		}
	});
	$('#sdlg').dialog('close');
}

//导出到excel
function exportDg(){
	$.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！",function(result){
		if(result){
			var url = "export/exporWeldTask";
			var img = new Image();
		    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
		    url = img.src;  // 此时相对路径已经变成绝对路径
		    img.src = null; // 取消请求
			window.location.href = encodeURI(url);
		}
	});
}

//焊工选择下拉框
function welderCombobox(){
	$.ajax({  
    type : "post",  
    async : false,
    url : "weldtask/getWelderAll",  
    data : {},  
    dataType : "json", //返回数据形式为json  
    success : function(result) {
        if (result) {
            var optionStr = '';  
            for (var i = 0; i < result.ary.length; i++) { 
                optionStr += "<option value=\"" + result.ary[i].id + "\" >"  
                        + result.ary[i].name + "</option>";  
            } 
            $("#fwelder_id").append(optionStr);
        }  
    },  
    error : function(errorMsg) {  
        alert("数据请求失败，请联系系统管理员!");  
    }  
}); 
	$("#fwelder_id").combobox();
}

//工艺选择选择下拉框
function wpslibCombobox(){
	$.ajax({  
  type : "post",  
  async : false,
  url : "weldtask/getWpslibAll",  
  data : {},  
  dataType : "json", //返回数据形式为json  
  success : function(result) {
      if (result) {
          var optionStr = '';  
          for (var i = 0; i < result.ary.length; i++) { 
              optionStr += "<option value=\"" + result.ary[i].id + "\" >"  
                      + result.ary[i].name + "</option>";  
          } 
          $("#fwpslib_id").append(optionStr);
      }  
  },  
  error : function(errorMsg) {  
      alert("数据请求失败，请联系系统管理员!");  
  }  
}); 
	$("#fwpslib_id").combobox();
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
	$("#weldTaskTable").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}

