$(function(){
	wpsDetail();
	if($("#symbol").val() == "1"){
		if($("#status").val() == "0"){
			$("#pass").show();
		}else{
			$("#pass").hide();
		}
	}else{
		$("#pass").hide();
	}
});

function wpsDetail(){
	$("#wpsDetailTable").datagrid( {
		height : $("#body").height(),
		width : $("#body").width(),
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
        }
	});
}

function passReview(){
	$.ajax({
		type : "post",
		async : false,
		url : "wps/passReview?fid="+$("#flagId").val(),
		dataType : "json",
		data : {},
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
					$("#pass").hide();
				}
			}
		},
		error : function() {
			alert('error');
		}
	});
}