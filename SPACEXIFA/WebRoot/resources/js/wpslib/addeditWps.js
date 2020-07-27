/**
 * 
 */
var oldchanel = 0,wpsId="",employeeId="",stepId="";
$(function() {
	$('#addOrUpdate').dialog( {
		onClose : function() {
			$("#addOrUpdatefm").form("disableValidation");
		}
	})
	$("#addOrUpdatefm").form("disableValidation");
	$.extend($.fn.datagrid.methods, {
		editCell: function(jq,param){
			return jq.each(function(){
				var opts = $(this).datagrid('options');
				var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
				for(var i=0; i<fields.length; i++){
					var col = $(this).datagrid('getColumnOption', fields[i]);
					col.editor1 = col.editor;
					if (fields[i] != param.field){
						col.editor = null;
					}
				}
				$(this).datagrid('beginEdit', param.index);
				for(var i=0; i<fields.length; i++){
					var col = $(this).datagrid('getColumnOption', fields[i]);
					col.editor = col.editor1;
				}
			});
		}
	});
	initTables();
//	getDictionary(10,"sxfselect");
})

function initTables(){
	$("#femployeeTable").datagrid( {
		fitColumns : true,
		height : $("#addOrUpdate").height()*0.4,
		width : $("#addOrUpdate").width()*0.64*0.428,
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
		height : $("#addOrUpdate").height()*0.4,
		width : $("#addOrUpdate").width()*0.64*0.33,
		idField : 'fid',
//		pageSize : 10,
//		pageList : [ 10, 20, 30, 40, 50 ],
		url : stepUrl,
//		singleSelect : true,
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
		height : $("#addOrUpdate").height()*0.4,
		width : $("#addOrUpdate").width()*0.64*0.33,
		idField : 'fid',
//		pageSize : 10,
//		pageList : [ 10, 20, 30, 40, 50 ],
		url : "/",
//		singleSelect : true,
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
		height : $("#addOrUpdate").height()*0.4,
		width : $("#addOrUpdate").width()*0.64*1.09,
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

function addEmployeeRow(){
	if(endEditing(0)){
		$('#femployeeTable').datagrid('appendRow', {});
		var index = $('#femployeeTable').datagrid("getRows").length-1;
		$('#femployeeTable').datagrid('beginEdit', index);
		$('#femployeeTable').datagrid('selectRow', index);
	}
	editIndex = index; 
}

function deleteEmployeeRow(){
	if(endEditing(0)){
		var len = $('#femployeeTable').datagrid("getRows").length;
		if(len==0){
			return;
		}
		var rows = $('#femployeeTable').datagrid("getSelections");
		if(rows.length!=0){
			var con=confirm("该操作将删除所选数据及其关联数据，并且无法撤销，是否继续？")
			if (con==true){
				for(var r=0;r<rows.length;r++){
					var index = $('#femployeeTable').datagrid('getRowIndex',rows[r]);
					$('#femployeeTable').datagrid('deleteRow', index);
				}
			}
		}else{
			return;
		}
	}
}

function saveEmployeeRow(){
	if(wpsId == ""){
		alert("请先保存左侧相关信息！！！");
		return;
	}
	if(endEditing(0)){
//		var changeRows = $("#femployeeTable").datagrid('getChanges'); //获取所有变更数据
		var deleteRows = $("#femployeeTable").datagrid('getChanges', 'deleted'); //获取删除的数据
		var updateRows = $("#femployeeTable").datagrid('getChanges', 'updated'); //获取删除的数据
		var addRows = $("#femployeeTable").datagrid('getChanges', 'inserted'); //获取删除的数据
//		console.log(dd.every(val => changeRows.includes(val)));
//		if(deleteRows.length!=0){
//			changeRows.filter((item) => !deleteRows.some((items) => item === items));
//		}
		if(addRows.length!=0||updateRows.length!=0||deleteRows.length!=0){
			$.ajax({
				type : "post",
				async : false,
				url : "wps/saveEmployee?wpsId="+wpsId,
				data : {addRows:JSON.stringify(addRows),
						updateRows:JSON.stringify(updateRows),
						deleteRows:JSON.stringify(deleteRows)},
				dataType : "json", //返回数据形式为json  
				success : function(result) {
					$('#femployeeTable').datagrid("options").url="wps/getInfo?search=" + wpsId +"&valueFlag=0";
					$('#femployeeTable').datagrid('reload');
					if (!result.success) {
						alert("保存失败，请联系管理员！！！");
					}else{
						alert("保存成功")
						$("#femployeeTable").datagrid('acceptChanges');	
					}
				},
				error : function(errorMsg) {
					alert("数据请求失败，请联系系统管理员!");
				}
			});
		}else{
			alert("未检测到任何更改信息！！！");
			return;
		}
	}
}
function addStepRow(){
	if(endEditing(1)){
		$('#fstepTable').datagrid('appendRow', {});
		var index = $('#fstepTable').datagrid("getRows").length-1;
		$('#fstepTable').datagrid('beginEdit', index);
		$('#fstepTable').datagrid('selectRow', index);
	}
	editIndex = index; 
}

function deleteStepRow(){
	if(endEditing(1)){
		var len = $('#fstepTable').datagrid("getRows").length;
		if(len==0){
			return;
		}
		var rows = $('#fstepTable').datagrid("getSelections");
		if(rows.length!=0){
			var r=confirm("该操作将删除所选数据及其关联数据，并且无法撤销，是否继续？")
			if (r==true){
				for(var r=0;r<rows.length;r++){
					var index = $('#fstepTable').datagrid('getRowIndex',rows[r]);
					$('#fstepTable').datagrid('deleteRow', index);
				}
			}
		}else{
			return;
		}
	}
}

function saveStepRow(){
	if(employeeId == ""){
		alert("请先保存工序信息！！！");
		return;
	}
	if(endEditing(1)){
		var deleteRows = $("#fstepTable").datagrid('getChanges', 'deleted'); //获取删除的数据
		var updateRows = $("#fstepTable").datagrid('getChanges', 'updated'); //获取删除的数据
		var addRows = $("#fstepTable").datagrid('getChanges', 'inserted'); //获取删除的数据
//		console.log(dd.every(val => changeRows.includes(val)));
//		if(deleteRows.length!=0){
//			changeRows.filter((item) => !deleteRows.some((items) => item === items));
//		}
		if(addRows.length!=0||updateRows.length!=0||deleteRows.length!=0){
			$.ajax({
				type : "post",
				async : false,
				url : "wps/saveStep?employeeId="+employeeId,
				data : {addRows:JSON.stringify(addRows),
						updateRows:JSON.stringify(updateRows),
						deleteRows:JSON.stringify(deleteRows)},
				dataType : "json", //返回数据形式为json  
				success : function(result) {
					$('#fstepTable').datagrid("options").url="wps/getInfo?search=" + employeeId +"&valueFlag=1";
					$('#fstepTable').datagrid('reload');
					if (!result.success) {
						alert("保存失败，请联系管理员！！！");
					}else{
						$("#fstepTable").datagrid('acceptChanges');	
						alert("保存成功");
					}
				},
				error : function(errorMsg) {
					alert("数据请求失败，请联系系统管理员!");
				}
			});
		}else{
			alert("未检测到任何更改信息！！！");
			return;
		}
	}
}
function addJunctionRow(){
	if(endEditing(2)){
		$('#fjunctionTable').datagrid('appendRow', {});
		var index = $('#fjunctionTable').datagrid("getRows").length-1;
		$('#fjunctionTable').datagrid('beginEdit', index);
		$('#fjunctionTable').datagrid('selectRow', index);
	}
	editIndex = index; 
}

function deleteJunctionRow(){
	if(endEditing(2)){
		var len = $('#fjunctionTable').datagrid("getRows").length;
		if(len==0){
			return;
		}
		var rows = $('#fjunctionTable').datagrid("getSelections");
		if(rows.length!=0){
			var r=confirm("该操作将删除所选数据及其关联数据，并且无法撤销，是否继续？")
			if (r==true){
				for(var r=0;r<rows.length;r++){
					var index = $('#fjunctionTable').datagrid('getRowIndex',rows[r]);
					$('#fjunctionTable').datagrid('deleteRow', index);
				}
			}
		}else{
			return;
		}
	}
}

function saveJunctionRow(){
	if(stepId == ""){
		alert("请先保存工步信息！！！");
		return;
	}
	if(endEditing(2)){
		var deleteRows = $("#fjunctionTable").datagrid('getChanges', 'deleted'); //获取删除的数据
		var updateRows = $("#fjunctionTable").datagrid('getChanges', 'updated'); //获取删除的数据
		var addRows = $("#fjunctionTable").datagrid('getChanges', 'inserted'); //获取删除的数据
//		console.log(dd.every(val => changeRows.includes(val)));
//		if(deleteRows.length!=0){
//			changeRows.filter((item) => !deleteRows.some((items) => item === items));
//		}
		if(addRows.length!=0||updateRows.length!=0||deleteRows.length!=0){
			$.ajax({
				type : "post",
				async : false,
				url : "wps/saveJunction?stepId="+stepId,
				data : {addRows:JSON.stringify(addRows),
						updateRows:JSON.stringify(updateRows),
						deleteRows:JSON.stringify(deleteRows)},
				dataType : "json", //返回数据形式为json  
				success : function(result) {
					$('#fjunctionTable').datagrid("options").url="wps/getInfo?search=" + stepId +"&valueFlag=2";
					$('#fjunctionTable').datagrid('reload');
					if (!result.success) {
						alert("保存失败，请联系管理员！！！");
					}else{
						$("#fjunctionTable").datagrid('acceptChanges');	
						alert("保存成功");
					}
				},
				error : function(errorMsg) {
					alert("数据请求失败，请联系系统管理员!");
				}
			});
		}else{
			alert("未检测到任何更改信息！！！");
			return;
		}
	}
}
function addDetailRow(){
	if(endEditing(3)){
		$('#wpsDetailTable').datagrid('appendRow', {});
		var index = $('#wpsDetailTable').datagrid("getRows").length-1;
		$('#wpsDetailTable').datagrid('beginEdit', index);
		$('#wpsDetailTable').datagrid('selectRow', index);
	}
	editIndex = index; 
}

function deleteDetailRow(){
	if(endEditing(3)){
		var len = $('#wpsDetailTable').datagrid("getRows").length;
		if(len==0){
			return;
		}
		var rows = $('#wpsDetailTable').datagrid("getSelections");
		if(rows.length!=0){
			var r=confirm("该操作将删除所选数据及其关联数据，并且无法撤销，是否继续？")
			if (r==true){
				for(var r=0;r<rows.length;r++){
					var index = $('#wpsDetailTable').datagrid('getRowIndex',rows[r]);
					$('#wpsDetailTable').datagrid('deleteRow', index);
				}
			}
		}else{
			return;
		}
	}
}

function saveDetailRow(){
	if(stepId == ""){
		alert("请先保存工步信息！！！");
		return;
	}
	if(endEditing(3)){
		var deleteRows = $("#wpsDetailTable").datagrid('getChanges', 'deleted'); //获取删除的数据
		var updateRows = $("#wpsDetailTable").datagrid('getChanges', 'updated'); //获取删除的数据
		var addRows = $("#wpsDetailTable").datagrid('getChanges', 'inserted'); //获取删除的数据
//		console.log(dd.every(val => changeRows.includes(val)));
//		if(deleteRows.length!=0){
//			changeRows.filter((item) => !deleteRows.some((items) => item === items));
//		}
		if(addRows.length!=0||updateRows.length!=0||deleteRows.length!=0){
			$.ajax({
				type : "post",
				async : false,
				url : "wps/saveDetail?stepId="+stepId,
				data : {addRows:JSON.stringify(addRows),
						updateRows:JSON.stringify(updateRows),
						deleteRows:JSON.stringify(deleteRows)},
				dataType : "json", //返回数据形式为json  
				success : function(result) {
					$('#wpsDetailTable').datagrid("options").url="wps/getInfo?search=" + stepId +"&valueFlag=3";
					$('#wpsDetailTable').datagrid('reload');
					if (!result.success) {
						alert("保存失败，请联系管理员！！！");
					}else{
						$("#wpsDetailTable").datagrid('acceptChanges');	
						alert("保存成功");
					}
				},
				error : function(errorMsg) {
					alert("数据请求失败，请联系系统管理员!");
				}
			});
		}else{
			alert("未检测到任何更改信息！！！");
			return;
		}
	}
}

var editIndex = undefined;
function endEditing(value){
	if (editIndex == undefined){return true}
	//0工序；1工步；2焊缝；3参数
	if(value == 0){
		if ($('#femployeeTable').datagrid('validateRow', editIndex)){
			$('#femployeeTable').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	if(value == 1){
		if ($('#fstepTable').datagrid('validateRow', editIndex)){
			$('#fstepTable').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	if(value == 2){
		if ($('#fjunctionTable').datagrid('validateRow', editIndex)){
			$('#fjunctionTable').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	if(value == 3){
		if ($('#wpsDetailTable').datagrid('validateRow', editIndex)){
			$('#wpsDetailTable').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
}

/*function onClickCell(index, field){
	if (endEditing()){
//		$('#femployeeTable').datagrid('selectRow', index)
//				.datagrid('editCell', {index:index,field:field});
		$(this).datagrid('beginEdit', index);
		var ed = $(this).datagrid('getEditor', {index:index,field:field});
		$(ed.target).focus();
		editIndex = index;
	}
}

//单元格失去焦点执行的方法  
function onAfterEdit(index, row, changes) {  
	$('#femployeeTable').datagrid("endEdit", index);
    var updated = $('#femployeeTable').datagrid('getChanges');  
    if (updated.length < 1) {  
        editRow = undefined;  
        $('#femployeeTable').datagrid('unselectAll');  
        return;  
    } else {  
        // 传值  
		alert("xxx");
//      submitForm(index, row, changes);  
    }        
}  */

var employeeUrl = "/",stepUrl = "/",junctionUrl = "/",detailUrl = "/",url="";
var flag = 1;
function addWps() {
	$("#tdd-buttons").hide();
	flag = 1;
	wpsId = "";
	$("#addOrUpdatefm").form("disableValidation");
	$('#addOrUpdate').window({
		title : "自建工艺",
		modal : true
	});
	$('#addOrUpdate').window('open');
	$('#addOrUpdatefm').form('clear');
	$('#flag').combobox('select', 0);
	employeeUrl = "/";
//	$('#femployeeTable').datagrid("options").url=employeeUrl;
//	$('#femployeeTable').datagrid('reload');
	$("#femployeeTable").datagrid("loadData", { total: 0, rows: [] });
	$("#fstepTable").datagrid("loadData", { total: 0, rows: [] });
	$("#fjunctionTable").datagrid("loadData", { total: 0, rows: [] });
	$("#wpsDetailTable").datagrid("loadData", { total: 0, rows: [] });
	url = "wps/addWps";
//	$("#femployeeTable").datagrid('loadData',[{
//		femployee_id: '01',
//		femployee_version: 'name01'
//	},{
//		femployee_id: '02',
//		femployee_version: 'name02'
//	}]);
}

function editWps(addVersionRow) {
	flag = 2;
	wpsId = "";
	$('#addOrUpdatefm').form('clear');
	$("#femployeeTable").datagrid("loadData", { total: 0, rows: [] });
	$("#fstepTable").datagrid("loadData", { total: 0, rows: [] });
	$("#fjunctionTable").datagrid("loadData", { total: 0, rows: [] });
	$("#wpsDetailTable").datagrid("loadData", { total: 0, rows: [] });
	var row = $('#wpslibTable').datagrid('getSelected'); 
	if(addVersionRow!=""){
		row = addVersionRow;
	}
	if (row) {
		if(row.flag == 1){
			alert("该参数从MES获取，无法进行修改删除操作！！！");
			return;
		}
		if(row.fstatus == 1){
			alert("该参数已通过审核，无法进行修改操作！！！");
			return;
		}
		if(row.fstatus == 2){
			$("#tdd-buttons").show();
		}else{
			$("#tdd-buttons").hide();
		}
		$('#addOrUpdate').window({
			title : "修改工艺",
			modal : true
		});
		$('#addOrUpdate').window('open');
		$('#addOrUpdatefm').form('load', row);
		employeeUrl = "wps/getInfo?search=" + row.fid +"&valueFlag=0";
		$('#femployeeTable').datagrid("options").url=employeeUrl;
		$('#femployeeTable').datagrid('reload');
		$('#validProduct').val(row.fproduct_version);
		$('#validPdo').val(row.fproduct_drawing_no);
		$('#validWpsname').val(row.fwps_lib_name);
		$('#validWpsversion').val(row.fwps_lib_version);
		url = "wps/updateWps?fid=" + row.fid;
		wpsId = row.fid;
	}else{
		alert("请先选择一条数据。");
	}
}

function saveWps() {
	var wpsFlag = $('#flag').combobox('getValue');
	var messager = "";
	var url2 = "";
	if (flag == 1) {
//		messager = "新增成功！";
		url2 = url + "?wpsFlag=" + wpsFlag;
	} else {
//		messager = "修改成功！";
		url2 = url + "&wpsFlag=" + wpsFlag;
	}
	$('#addOrUpdatefm').form('submit', {
		url : url2,
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if (result) {
				var result = eval('(' + result + ')');
				if (!result.success) {
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					alert("保存成功");
					wpsId = result.wpsId;
					$('#wpslibTable').datagrid('reload');
				}
			}

		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

function saveReview(){
	$.ajax({
		type : "post",
		async : false,
		url : "wps/passReview?fid="+wpsId+"&value=0",
		dataType : "json",
		data : {},
		success : function(result) {
			if (result) {
				if (!result.success) {
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					alert("保存成功");
					$('#addOrUpdate').window('close');
					$('#wpslibTable').datagrid('reload');
				}
			}
		},
		error : function() {
			alert('error');
		}
	});
}

function addVersion() {
	flag = 1;
	wpsId = "";
	$('#addVersionfm').form('clear');
	var row = $('#wpslibTable').datagrid('getSelected'); 
	if (row) {
		$("#addVersionfm").form("disableValidation");
		$('#addVersionDiv').window({
			title : "新建版本",
			modal : true
		});
		$('#addVersionDiv').window('open');
		$("#hide_id").val(row.fid);
		$("#fproduct_drawing_no_v").textbox('setValue', row.fproduct_drawing_no);
		$("#fproduct_name_v").textbox('setValue', row.fproduct_name);
		$("#fproduct_version_v").textbox('setValue', row.fproduct_version);
		url = "wps/addVersion?fid=" + row.fid;
	}
}

function saveVersion(){
	var fwps_lib_version_v = $("#fwps_lib_version_v").textbox('getValue');
	var fwps_lib_name_v = $("#fwps_lib_name_v").textbox('getValue');
	var fproduct_drawing_no_v = $("#fproduct_drawing_no_v").textbox('getValue');
	var fproduct_name_v = $("#fproduct_name_v").textbox('getValue');
	var fproduct_version_v = $("#fproduct_version_v").textbox('getValue');
	$.ajax({
		type : "post",
		async : false,
		url : "wps/wpsversionvalidate?wpsversion="+fwps_lib_version_v+"&wln="+fwps_lib_name_v+"&pdn="+fproduct_drawing_no_v+"&pv="+fproduct_name_v,
		dataType : "json",
		data : {},
		success : function(result) {
			if (result) {
				if (result.success) {
					alert("该图号版本下工艺规程的版本已经存在");
					return;
				} else {
					var messager = "";
					$('#addVersionfm').form('submit', {
						url : url+"&fwps_lib_version_v="+fwps_lib_version_v+"&fwps_lib_name_v="+fwps_lib_name_v+"&fproduct_drawing_no_v="+fproduct_drawing_no_v+"&fproduct_name_v="+fproduct_name_v+"&fproduct_version_v="+fproduct_version_v,
						onSubmit : function() {
							return $(this).form('enableValidation').form('validate');
						},
						success : function(result) {
							if (result) {
								var result = eval('(' + result + ')');
								if (!result.success) {
									$.messager.show({
										title : 'Error',
										msg : result.errorMsg
									});
								} else {
									if(result.wpsId!=""){
										wpsId = result.wpsId;
									}
									$('#wpslibTable').datagrid('reload');
									$('#addVersionDiv').window('close');
									var addVersionRow = result.objRow;
									var con=confirm("保存成功,是否前往修改相关参数？")
									if (con == true) {
										editWps(addVersionRow);
									}
								}
							}

						},
						error : function(errorMsg) {
							alert("数据请求失败，请联系系统管理员!");
						}
					});
				}
			}
		},
		error : function() {
			alert('error');
		}
	});
}