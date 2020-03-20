/**
 * 
 */
var oldchanel = 0,wpsId="",employeeId="",stepId="",cardId="";
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
	InsframeworkCombobox();
	getWpsCombobox();
})

var flag = 1;
function addWps() {
	flag = 1;
	$("#saveCard").show();
	$("#saveAndPassCard").hide();
	$("#addOrUpdatefm").form("disableValidation");
	wpsId = "";
	$('#addOrUpdate').window({
		title : "自建电子跟踪卡",
		modal : true
	});
	$('#addOrUpdate').window('open');
	$('#addOrUpdatefm').form('clear');
	$('#flag').combobox('select', 0);
	url = "weldtask/addCard";
}

function editWps() {
	flag = 2;
	wpsId = "";
	$('#addOrUpdatefm').form('clear');
	var row = $('#cardTable').datagrid('getSelected'); 
	if (row) {
		if(row.flag == 1){
			alert("该电子跟踪卡从MES获取，无法进行修改删除操作！！！");
			return;
		}
		if(row.fstatus == 1){
			alert("该电子跟踪卡已通过审核，无法进行修改操作！！！");
			return;
		}
		if(row.fstatus == 2){
			$("#saveCard").hide();
			$("#saveAndPassCard").show();
		}else{
			$("#saveCard").show();
			$("#saveAndPassCard").hide();
		}
		$.ajax({
			type : "post",
			async : false,
			url : "weldtask/getProductByCardid?fid="+row.fid,
			data : {},
			dataType : "json", //返回数据形式为json  
			success : function(result) {
				if (result.row.length!=0) {
					$("#fprefix_number").textbox('setValue', result.row[0].fprefix_number); 
					$("#fproduct_number").numberbox('setValue', result.row[0].fproduct_number); 
					$("#init_number").numberbox('setValue', result.row[0].finit_number); 
					$('#fwps_lib_id').combogrid('setValue', result.row[0].fwps_lib_id);
				}
			},
			error : function(errorMsg) {
				alert("数据请求失败，请联系系统管理员!");
			}
		});
		$('#addOrUpdate').window({
			title : "修改电子跟踪卡",
			modal : true
		});
		$('#addOrUpdate').window('open');
		$('#addOrUpdatefm').form('load', row);
		$('#validCard').val(row.fwelded_junction_no);
		$('#validTask').val(row.ftask_no);
		url = "weldtask/updateCard?fid=" + row.fid;
		cardId = row.fid;
	}else{
		alert("请先选择一条数据。");
	}
}

function saveCard() {
	var cardFlag = $('#flag').combobox('getValue');
	var fitemId = $('#fitemId').combobox('getValue');
	var fwps_lib_id = $('#fwps_lib_id').combogrid('getValue');
	var messager = "";
	var url2 = "";
	if (flag == 1) {
//		messager = "新增成功！";
		url2 = url + "?cardFlag=" + cardFlag + "&fitemId=" + fitemId + "&fwps_lib_id=" + fwps_lib_id;
	} else {
//		messager = "修改成功！";
		url2 = url + "&cardFlag=" + cardFlag + "&fitemId=" + fitemId + "&fwps_lib_id=" + fwps_lib_id;
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
					$('#addOrUpdate').window('close');
					$('#cardTable').datagrid('reload');
				}
			}

		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

//所属项目
function InsframeworkCombobox(){
	$.ajax({  
    type : "post",  
    async : false,
    url : "weldingMachine/getInsframeworkAll",  
    data : {},  
    dataType : "json", //返回数据形式为json  
    success : function(result) {  
        if (result) {
            var optionStr = '';
            for (var i = 0; i < result.ary.length; i++) {  
                optionStr += "<option value=\"" + result.ary[i].id + "\" >"  
                        + result.ary[i].name + "</option>";
            }
            $("#fitemId").html(optionStr);
            $("#item").html(optionStr);
        }  
    },  
    error : function(errorMsg) {  
        alert("数据请求失败，请联系系统管理员!");  
    }  
	}); 
	$("#fitemId").combobox();
	$("#item").combobox();
}

//工艺规程
function getWpsCombobox() {
	$("#fwps_lib_id").combogrid( {
//		height : $("#body").height(),
//		width : $("#body").width(),
		panelWidth:1000,
		idField : 'fid',
		textField:'fwps_lib_name',
		pagination: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "wps/getWpsList?search=fid NOT IN (SELECT DISTINCT fwps_lib_id FROM tb_product_number WHERE fwps_lib_id IS NOT NULL) AND fstatus=1",
//		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		fitColumns : true,
		columns : [ [ {
			field : 'fid',
			title : '序号',
//			width : 30,
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
//			width : 100,
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
	return;
//	$.ajax({
//		type : "post",
//		async : false,
//		url : "wps/getWpsCombobox",
//		data : {},
//		dataType : "json", //返回数据形式为json  
//		success : function(result) {
//			if (result) {
//				var optionStr = '';
//				for (var i = 0; i < result.ary.length; i++) {
//					optionStr += "<option value=\"" + result.ary[i].id + "\" >"
//						+ result.ary[i].name + "</option>";
//				}
//				$("#fwps_lib_id").html(optionStr);
//			}
//		},
//		error : function(errorMsg) {
//			alert("数据请求失败，请联系系统管理员!");
//		}
//	});
//	$("#fwps_lib_id").combobox();
}

function saveReview(){
	$.ajax({
		type : "post",
		async : false,
		url : "weldtask/passReview?fid="+cardId+"&value=0",
		dataType : "json",
		data : {},
		success : function(result) {
			if (result) {
				if (!result.success) {
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
				} 
			}
		},
		error : function() {
			alert('error');
		}
	});
	saveCard();
}