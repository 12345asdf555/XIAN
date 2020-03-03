/**
 * 
 */
$(function(){
	cardDatagrid();
})

function cardDatagrid(){
	$("#cardTable").datagrid( {
		fitColumns : true,
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
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'ftask_no',
			title : '任务编号',
			width : 100,
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
			align : "left"
		}, {
			field : 'fproduct_details',
			title : '产品详情',
			width : 100,
			halign : "center",
			align : "left",
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
			width : 100,
			halign : "center",
			align : "left"
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
		}
	});
}

function openProductDetails(){
//	$("#productDetailsTable").datagrid("loadData", { total: 0, rows: [] });
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
		url : "weldtask/getProductList",
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
					str += '<a id="doing" class="easyui-linkbutton"/>';
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
		}
	});
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
	}
}

function closeDlg(){
	if(!$("#addOrUpdate").parent().is(":hidden")){
		$('#addOrUpdate').window('close');
	}
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize(), 500);
}

//改变表格高宽
function domresize() {
	$("#wpslibTable").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
	
	$("#femployeeTable").datagrid('resize', {
		height : $("#addOrUpdate").height()*0.4,
		width : $("#addOrUpdate").width()*0.64*0.33,
	});
}