$(function(){
	wpsDetail();
	if($("#symbol").val() == "1"){
		if($("#status").val() == "0"){
			$("#pass").show();
			$("#down").show();
		}else if($("#status").val() == "2"){
			$("#pass").hide();
			$("#down").hide();
		}else{
			$("#pass").hide();
			$("#down").hide();
		}
	}else{
		$("#pass").hide();
		$("#down").hide();
	}
});

function wpsDetail(){
	var search = "";
	if($("#flagId").val()){
		search = " AND p.fcard_id="+$("#flagId").val();
	}
	$("#productDetailsTable").datagrid( {
		fitColumns : false,
		height : $("#body").height(),
		width : $("#body").width(),
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
			field : 'machineid',
			title : '焊机id',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'fproduct_number',
			title : '产品序号',
			width : 300,
			halign : "center",
			align : "left"
		}, {
			field : 'fwps_lib_name',
			title : '工艺规程编号',
			width : 300,
			halign : "center",
			align : "left"
		}, {
			field : 'fwps_lib_version',
			title : '工艺规程版本号',
			width : 300,
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
			width : 150,
			halign : "center",
			align : "center",
			formatter: function(value,row,index){
				var str = "";
				if(row.fstatus==2){
					str += '<a id="wait" class="easyui-linkbutton"/>';
				}
				if(row.fstatus==0){
					str += '<a id="doing" class="easyui-linkbutton" href="javascript:realtime()"/>';
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

function realtime(){
	var row = $('#productDetailsTable').datagrid('getSelected'); 
	if (row) {
		window.open("td/AllTdcard?machineid="+row.machineid);
	}
}

function passReview(){
	$.ajax({
		type : "post",
		async : false,
		url : "weldtask/passReview?fid="+$("#flagId").val()+"&value=1",
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
					$("#pass").hide();
					$("#down").hide();
				}
			}
		},
		error : function() {
			alert('error');
		}
	});
}

function openTurnDownDialog(){
	$('#turnDownDialog').window({
		title : "备注",
		modal : true
	});
	$('#turnDownDialog').window('open');
}

function saveReason(){
	$.ajax({
		type : "post",
		async : false,
		url : "weldtask/turnDown?fid="+$("#flagId").val()+"&downReason="+$("#downReason").val(),
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
					$('#turnDownDialog').window('close');
				}
			}
		},
		error : function() {
			alert('error');
		}
	});
}

function closeDlg(){
	if(!$("#turnDownDialog").parent().is(":hidden")){
		$('#turnDownDialog').window('close');
	}
}