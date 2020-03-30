/**
 * 
 */
$(function(){
	initTables();
	cardDatagrid();
})

function cardDatagrid(){
	$("#cardTable").datagrid( {
		fitColumns : false,
		height : $("#body").height(),
		width : $("#body").width(),
		idField : 'fid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getCardList",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [ {
			field : 'fid',
			title : '序号',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'fwelded_junction_no',
			title : '电子跟踪卡号',
			width : 200,
			halign : "center",
			align : "left"
		}, {
			field : 'ftask_no',
			title : '任务编号',
			width : 200,
			halign : "center",
			align : "left"
		}, {
			field : 'fitemId',
			title : '组织机构id',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fitemName',
			title : '组织机构',
			width : 100,
			halign : "center",
			align : "center"
		}, {
			field : 'fproduct_details',
			title : '产品详情',
			width : 100,
			halign : "center",
			align : "center",
			formatter: function(value,row,index){
				var str = '<a id="prodetail" class="easyui-linkbutton" href="javascript:openProductDetails('+row.fid+')"/>';
				return str;
			}
		}, /*{
			field : 'fwps_lib_name',
			title : '工艺规程编号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fwps_lib_version',
			title : '工艺规程版本号',
			width : 100,
			halign : "center",
			align : "left"
		}, */{
			field : 'flag',
			title : '卡号来源标志',
//			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'flag_name',
			title : '卡号来源',
			width : 80,
			halign : "center",
			align : "center"
		}, {
			field : 'dyne',
			title : '任务是否完成标志',
//			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'fchange_wps',
			title : '临时切换工艺',
			width : 120,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = "";
				if(row.dyne==1){
					str += '<a id="change_wps" class="easyui-linkbutton" href="javascript:changeWps()"/>';
				}else{
					str += '<a id="change_wps" class="easyui-linkbutton" href="javascript:changeWps()" disabled="true"/>';
				}
				return str;
			}
		}, {
			field : 'fstatus',
			title : '状态id',
//			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'fstatus_name',
			title : '审核状态',
			width : 100,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = "";
				if(row.fstatus==0){
					str += '<a id="wait" class="easyui-linkbutton"/>';
				}
				if(row.fstatus==2){
					str += '<a id="down" class="easyui-linkbutton"/>';
				}
				if(row.fstatus==1){
					str += '<a id="finish" class="easyui-linkbutton"/>';
				}
				return str;
			}
		}, {
			field : 'fback',
			title : '驳回原因',
			width : 400,
			halign : "center",
			align : "left"
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
			if($("#wait").length!=0){
				$("a[id='wait']").linkbutton({text:'待审核',plain:true,iconCls:'icon-newcancel'});
			}
			if($("#down").length!=0){
				$("a[id='down']").linkbutton({text:'被驳回',plain:true,iconCls:'icon-next'});
			}
			if($("#finish").length!=0){
				$("a[id='finish']").linkbutton({text:'已通过',plain:true,iconCls:'icon-over'});
			}
			if($("#prodetail").length!=0){
				$("a[id='prodetail']").linkbutton({text:'详情',plain:true,iconCls:'icon-navigation'});
			}
			if($("#change_wps").length!=0){
				$("a[id='change_wps']").linkbutton({text:'临时切换工艺',plain:true,iconCls:'icon-update'});
			}
		}
	});
}

function openProductDetails(){
	var row = $('#cardTable').datagrid('getSelected'); 
	if (row) {
		var fid = row.fid;
		var search = " AND p.fcard_id="+fid;
		$('#productDetailsDlg').window({
			title : "产品详情",
			modal : true
		});
		$('#productDetailsDlg').window('open');
		$("#productDetailsTable").datagrid( {
			fitColumns : true,
			height : $("#productDetailsDlg").height(),
			width : $("#productDetailsDlg").width(),
			idField : 'fid',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			url : "weldtask/getProductList?search="+search,
			singleSelect : true,
			rownumbers : true,
			showPageList : false,
			columns : [ [ {
				field : 'fid',
				title : '序号',
//			width : 30,
				halign : "center",
				align : "left",
				hidden:true
			}, {
				field : 'fproduct_number',
				title : '产品序号',
				width : 100,
				halign : "center",
				align : "left"
			}, {
				field : 'fproduct_drawing_no',
				title : '产品图号',
				width : 100,
				halign : "center",
				align : "left"
			}, {
				field : 'fproduct_name',
				title : '产品名称',
				width : 100,
				halign : "center",
				align : "left"
			}, {
				field : 'fproduct_version',
				title : '产品版本号',
				width : 100,
				halign : "center",
				align : "left"
			}, {
				field : 'fwps_lib_name',
				title : '工艺规程编号',
				width : 100,
				halign : "center",
				align : "left"
			}, {
				field : 'fwps_lib_version',
				title : '工艺规程版本号',
				width : 100,
				halign : "center",
				align : "left"
			}, {
				field : 'fwps_lib_id',
				title : '工艺id',
//			width : 30,
				halign : "center",
				align : "left",
				hidden:true
			}, {
				field : 'flag',
				title : '自建id',
//			width : 30,
				halign : "center",
				align : "left",
				hidden:true
			}, {
				field : 'fstatus',
				title : '状态id',
//			width : 100,
				halign : "center",
				align : "left",
				hidden : true
			}, {
				field : 'fstatus_name',
				title : '任务状态',
				width : 100,
				halign : "center",
				align : "left",
				formatter: function(value,row,index){
					var str = "";
					if(row.fstatus==2){
						str += '<a id="wait" class="easyui-linkbutton"/>';
					}
					if(row.fstatus==0){
						str += '<a id="doing" class="easyui-linkbutton" href="javascript:finishWork('+row.fid+')"/>';
					}
					if(row.fstatus==1){
						str += '<a id="finish" class="easyui-linkbutton"/>';
					}
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
				if($("#wait").length!=0){
					$("a[id='wait']").linkbutton({text:'未领取',plain:true,iconCls:'icon-assign'});
				}
				if($("#doing").length!=0){
					$("a[id='doing']").linkbutton({text:'进行中',plain:true,iconCls:'icon-unfinished'});
				}
				if($("#finish").length!=0){
					$("a[id='finish']").linkbutton({text:'已完成',plain:true,iconCls:'icon-finish'});
				}
			},
			onDblClickRow: function(rowIndex, rowData){
				$('#wpsDetailsForm').form('clear');
				$("#femployeeTable").datagrid("loadData", { total: 0, rows: [] });
				$("#fstepTable").datagrid("loadData", { total: 0, rows: [] });
				$("#fjunctionTable").datagrid("loadData", { total: 0, rows: [] });
				$("#wpsDetailTable").datagrid("loadData", { total: 0, rows: [] });
				$('#wpsDetailsDialog').window({
					title : "工艺详情",
					modal : true
				});
				$('#wpsDetailsForm').form('load', rowData);
				$('#wps_flag').combobox('select', rowData.flag);
				$('#wpsDetailsDialog').window('open');
				employeeUrl = "wps/getInfo?search=" + rowData.fwps_lib_id +"&valueFlag=0";
				$('#femployeeTable').datagrid("options").url=employeeUrl;
				$('#femployeeTable').datagrid('reload');
			}
		});
	}
}

function searchWps(){
	var search = "";
	var card_no = $("#card_no").textbox('getValue');
	var task_no = $("#task_no").textbox('getValue');
	var item = $("#item").combobox("getValue");
	var wflag = $("#wflag").combobox("getValue");
	var status = $("#status").combobox("getValue");
	if(card_no != ""){
		if(search == ""){
			search += " fwelded_junction_no LIKE "+"'%" + card_no + "%'";
		}else{
			search += " AND fwelded_junction_no LIKE "+"'%" + card_no + "%'";
		}
	}
	if(task_no != ""){
		if(search == ""){
			search += " ftask_no LIKE "+"'%" + task_no + "%'";
		}else {
			search += " AND ftask_no LIKE "+"'%" + task_no + "%'";
		}
	}
	if(item != ""){
		if(search == ""){
			search += " fitemId LIKE "+"'%" + item + "%'";
		}else{
			search += " AND fitemId LIKE "+"'%" + item + "%'";
		}
	}
	if(wflag != ""){
		if(search == ""){
			search += " j.flag=" + wflag;
		}else{
			search += " AND j.flag=" + wflag;
		}
	}
	if(status != ""){
		if(search == ""){
			search += " j.fstatus=" + status;
		}else{
			search += " AND j.fstatus=" + status;
		}
	}
	$('#cardTable').datagrid('load', {
		"search" : search
	});
}

function wpsDetails(){
	var row = $('#cardTable').datagrid('getSelected'); 
	if (row) {
		window.location.href = encodeURI("weldtask/goTrackCard"+"?fid="+row.fid+"&fwelded_junction_no="+row.fwelded_junction_no+"&status="+row.fstatus+"&fitemName="+row.fitemName);
	}else{
		alert("请先选择一条数据。");
	}
}

function closeDlg(){
	if(!$("#addOrUpdate").parent().is(":hidden")){
		$('#addOrUpdate').window('close');
	}
	if(!$("#changeWpsDiv").parent().is(":hidden")){
		$('#changeWpsDiv').window('close');
	}
}

function changeWps(){
	var row = $('#cardTable').datagrid('getSelected'); 
	if (row) {
		$("#fwps_lib_id_ch").combogrid( {
//			height : $("#body").height(),
//			width : $("#body").width(),
			panelWidth:1000,
			idField : 'fid',
			textField:'fwps_lib_name',
			pagination: true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			url : "wps/getWpsList?search=fid NOT IN (SELECT DISTINCT fwps_lib_id FROM tb_product_number WHERE fwps_lib_id IS NOT NULL) AND fstatus=1",
//			singleSelect : true,
			rownumbers : true,
			showPageList : false,
			fitColumns : true,
			columns : [ [ {
				field : 'fid',
				title : '序号',
//				width : 30,
				halign : "center",
				align : "left",
				hidden:true
			},{
				field : 'fproduct_drawing_no',
				title : '产品图号',
				width : 200,
				halign : "center",
				align : "left"
			}, {
				field : 'fproduct_name',
				title : '产品名称',
				width : 200,
				halign : "center",
				align : "left"
			}, {
				field : 'fproduct_version',
				title : '产品版本号',
				width : 200,
				halign : "center",
				align : "left"
			}, {
				field : 'fwps_lib_name',
				title : '工艺规程编号',
				width : 200,
				halign : "center",
				align : "left"
			}, {
				field : 'fwps_lib_version',
				title : '工艺规程版本号',
				width : 200,
				halign : "center",
				align : "left"
			}, {
				field : 'flag',
				title : '工艺来源标志',
//				width : 100,
				halign : "center",
				align : "left",
				hidden : true
			}, {
				field : 'flag_name',
				title : '工艺来源',
				width : 80,
				halign : "center",
				align : "center"
			}] ],
			rowStyler: function(index,row){
	            if ((index % 2)!=0){
	            	//处理行代背景色后无法选中
	            	var color=new Object();
	                return color;
	            }
	        },
		});
//		$.ajax({
//			type : "post",
//			async : false,
//			url : "wps/getWpsCombobox",
//			data : {},
//			dataType : "json", //返回数据形式为json  
//			success : function(result) {
//				if (result) {
//					var optionStr = '';
//					for (var i = 0; i < result.ary.length; i++) {
//						optionStr += "<option value=\"" + result.ary[i].id + "\" >"
//							+ result.ary[i].name + "</option>";
//					}
//					$("#fwps_lib_id_ch").html(optionStr);
//				}
//			},
//			error : function(errorMsg) {
//				alert("数据请求失败，请联系系统管理员!");
//			}
//		});
//		$("#fwps_lib_id_ch").combobox();
		$('#changeWpsDiv').window({
			title : "临时切换工艺",
			modal : true
		});
		$('#changeWpsDiv').window('open');
		changeUrl = "weldtask/cardChangeWps?fid="+row.fid;
	}
}

function saveChange(){
	var fwps_lib_id_ch = $('#fwps_lib_id_ch').combogrid('getValue');
	$.ajax({
		type : "post",
		async : false,
		url : changeUrl+"&wpsId="+fwps_lib_id_ch,
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				if (!result.success) {
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					alert("保存成功");
				}
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

function historyDetails(){
	$("#historyDetailsTable").datagrid( {
		fitColumns : true,
		height : $("#historyDetailsDlg").height(),
		width : $("#historyDetailsDlg").width(),
		idField : 'fid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getProductWpsHistory",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [ {
			field : 'fid',
			title : '序号',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'fwelded_junction_no',
			title : '电子跟踪卡号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fproduct_number',
			title : '产品序号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fwps_lib_name',
			title : '工艺规程编号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fwps_lib_version',
			title : '工艺规程版本号',
			width : 100,
			halign : "center",
			align : "left"
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
	$('#historyDetailsDlg').window({
		title : "临时切换工艺历史记录",
		modal : true
	});
	$('#historyDetailsDlg').window('open');
}

var employeeUrl = "/",stepUrl = "/",junctionUrl = "/",detailUrl = "/";
function initTables(){
	$("#femployeeTable").datagrid( {
		fitColumns : true,
		height : $("#wpsDetailsDialog").height()*0.48,
		width : $("#wpsDetailsDialog").width()*0.64*0.428,
		idField : 'fid',
//		pageSize : 10,
//		pageList : [ 10, 20, 30, 40, 50 ],
		url : employeeUrl,
		singleSelect : true,
		rownumbers : true,
//		showPageList : false,
		nowrap : false,
		columns : [ [ {
			field : 'fid',
			title : '序号',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'femployee_id',
			title : '工序号',
			width : 100,
//			halign : "center",
			align : "center",
			editor:'text'
		}, {
			field : 'femployee_version',
			title : '工序版本',
			width : 100,
//			halign : "center",
			align : "center",
			editor:'text'
		}, {
			field : 'femployee_name',
			title : '工序名称',
			width : 100,
//			halign : "center",
			align : "center",
			editor:'text'
		}] ],
//		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		onLoadSuccess: function(data){
			if(data.rows.length!=0){
				$('#femployeeTable').datagrid('selectRow', 0);
			}
		},
		onSelect: function(rowIndex, rowData){
			if(rowData.fid){
				employeeId = rowData.fid;
			}else{
				employeeId = "";
			}
			stepUrl = "wps/getInfo?search=" + rowData.fid +"&valueFlag=1";
			$('#fstepTable').datagrid("options").url=stepUrl;
			$('#fstepTable').datagrid('reload');
		},
		onDblClickCell: function(index,field,value){
			if (endEditing(0)){
//				$('#femployeeTable').datagrid('selectRow', index)
//						.datagrid('editCell', {index:index,field:field});
				$(this).datagrid('beginEdit', index);
				var ed = $(this).datagrid('getEditor', {index:index,field:field});
				$(ed.target).focus();
				editIndex = index;
			}
		}
//	    onAfterEdit: onAfterEdit
	});
	
	$("#fstepTable").datagrid( {
		height : $("#wpsDetailsDialog").height()*0.48,
		width : $("#wpsDetailsDialog").width()*0.64*0.33,
		idField : 'fid',
//		pageSize : 10,
//		pageList : [ 10, 20, 30, 40, 50 ],
		url : stepUrl,
		singleSelect : true,
		rownumbers : true,
//		showPageList : false,
		fitColumns : true,
		columns : [ [ {
			field : 'fid',
			title : '序号',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fstep_number',
			title : '工步号',
			width : 100,
			halign : "center",
			align : "left",
			editor:'text'
		}, {
			field : 'fstep_name',
			title : '工步名称',
			width : 100,
			halign : "center",
			align : "left",
			editor:'text'
		}] ],
//		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		onLoadSuccess: function(data){
			if(data.rows.length!=0){
				$('#fstepTable').datagrid('selectRow', 0);
			}
		},
		onSelect: function(rowIndex, rowData){
			if(rowData.fid){
				stepId = rowData.fid;
			}else{
				stepId = "";
			}
			junctionUrl = "wps/getInfo?search=" + rowData.fid +"&valueFlag=2";
			$('#fjunctionTable').datagrid("options").url=junctionUrl;
			$('#fjunctionTable').datagrid('reload');
			detailUrl = "wps/getInfo?search=" + rowData.fid +"&valueFlag=3";
			$('#wpsDetailTable').datagrid("options").url=detailUrl;
			$('#wpsDetailTable').datagrid('reload');
		},
		onDblClickCell: function(index,field,value){
			if (endEditing(1)){
//				$('#femployeeTable').datagrid('selectRow', index)
//						.datagrid('editCell', {index:index,field:field});
				$(this).datagrid('beginEdit', index);
				var ed = $(this).datagrid('getEditor', {index:index,field:field});
				$(ed.target).focus();
				editIndex = index;
			}
		}
	});
	
	$("#fjunctionTable").datagrid( {
		height : $("#wpsDetailsDialog").height()*0.48,
		width : $("#wpsDetailsDialog").width()*0.64*0.33,
		idField : 'fid',
//		pageSize : 10,
//		pageList : [ 10, 20, 30, 40, 50 ],
		url : "/",
		singleSelect : true,
		rownumbers : true,
//		showPageList : false,
		fitColumns : true,
		columns : [ [ {
			field : 'fid',
			title : '序号',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fjunction',
			title : '焊缝编号',
			width : 100,
			halign : "center",
			align : "left",
			editor:'text'
		}, {
			field : 'fwelding_area',
			title : '焊接部位',
			width : 100,
			halign : "center",
			align : "left",
			editor:'text'
		}] ],
//		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		onDblClickCell: function(index,field,value){
			if (endEditing(2)){
//				$('#femployeeTable').datagrid('selectRow', index)
//						.datagrid('editCell', {index:index,field:field});
				$(this).datagrid('beginEdit', index);
				var ed = $(this).datagrid('getEditor', {index:index,field:field});
				$(ed.target).focus();
				editIndex = index;
			}
		}
	});
	
	$("#wpsDetailTable").datagrid( {
		height : $("#wpsDetailsDialog").height()*0.48,
		width : $("#wpsDetailsDialog").width()*0.64*1.09,
		idField : 'fid',
//		pageSize : 10,
//		pageList : [ 10, 20, 30, 40, 50 ],
		url : "/",
		singleSelect : true,
		rownumbers : true,
//		showPageList : false,
		fitColumns : true,
		columns : [ [ {
			field : 'fid',
			title : '序号',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fquantitative_project',
			title : '量化项目',
			width : 100,
			halign : "center",
			align : "left",
			editor:'text'
		}, {
			field : 'frequired_value',
			title : '要求值',
			width : 100,
			halign : "center",
			align : "left",
			editor:'text'
		}, {
			field : 'fupper_deviation',
			title : '上偏差',
			width : 100,
			halign : "center",
			align : "left",
			editor:'text'
		}, {
			field : 'flower_deviation',
			title : '下偏差',
			width : 100,
			halign : "center",
			align : "left",
			editor:'text'
		}, {
			field : 'funit_of_measurement',
			title : '计量单位',
			width : 100,
			halign : "center",
			align : "left",
			editor:'text'
		}] ],
//		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		onDblClickCell: function(index,field,value){
			if (endEditing(3)){
//				$('#femployeeTable').datagrid('selectRow', index)
//						.datagrid('editCell', {index:index,field:field});
				$(this).datagrid('beginEdit', index);
				var ed = $(this).datagrid('getEditor', {index:index,field:field});
				$(ed.target).focus();
				editIndex = index;
			}
		}
	});
}

function finishWork(product_id){
	var con = confirm("此操作不可撤销，是否确认？");
	if(con == true){
		$.ajax({
			type : "post",
			async : false,
			url : "wps/finishWork"+"?fid="+product_id,
			data : {},
			dataType : "json", //返回数据形式为json  
			success : function(result) {
				if (result) {
					if (!result.success) {
						$.messager.show({
							title : 'Error',
							msg : result.errorMsg
						});
					} else {
						alert("保存成功");
						$('#productDetailsTable').datagrid('reload');
					}
				}
			},
			error : function(errorMsg) {
				alert("数据请求失败，请联系系统管理员!");
			}
		});
	}
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize(), 500);
}

//改变表格高宽
function domresize() {
	$("#cardTable").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}