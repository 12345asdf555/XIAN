/**
 * 
 */
var oldchanel = 0,wpsId="",employeeId="",stepId="";
$(function() {
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
	wpsId = "";
	$('#addOrUpdatefm').form('clear');
	$('#addOrUpdate').window({
		title : "自建电子跟踪卡",
		modal : true
	});
	$('#addOrUpdate').window('open');
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
		$.ajax({
			type : "post",
			async : false,
			url : "weldtask/getProductByCardid?fid="+row.fid,
			data : {},
			dataType : "json", //返回数据形式为json  
			success : function(result) {
				if (result) {
					$("#fprefix_number").textbox('setValue', result.row[0].fprefix_number); 
					$("#fproduct_number").numberbox('setValue', result.row[0].fproduct_number); 
					$("#init_number").numberbox('setValue', result.row[0].finit_number); 
					$('#fwps_lib_id').combobox('select', result.row[0].fwps_lib_id);
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
		url = "weldtask/updateCard?fid=" + row.fid;
	}
}

function saveCard() {
	var cardFlag = $('#flag').combobox('getValue');
	var fitemId = $('#fitemId').combobox('getValue');
	var fwps_lib_id = $('#fwps_lib_id').combobox('getValue');
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
	$.ajax({
		type : "post",
		async : false,
		url : "wps/getWpsCombobox",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				var optionStr = '';
				for (var i = 0; i < result.ary.length; i++) {
					optionStr += "<option value=\"" + result.ary[i].id + "\" >"
						+ result.ary[i].name + "</option>";
				}
				$("#fwps_lib_id").html(optionStr);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
	$("#fwps_lib_id").combobox();
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