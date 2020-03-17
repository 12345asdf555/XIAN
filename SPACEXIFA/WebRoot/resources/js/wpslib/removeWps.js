/**
 * 
 */
function deleteWps(){
	var deleteRows = $("#wpslibTable").datagrid('getSelected'); //获取删除的数据
	if(deleteRows){
		if(deleteRows.flag == 1){
			alert("该参数从MES获取，无法进行修改删除操作！！！");
			return;
		}
		var c=confirm("该操作将删除所选数据及其关联数据，并且无法撤销，是否继续？")
		if (c == true) {
			$.ajax({  
		        type : "post",  
		        async : false,
		        url : "wps/deleteWps",  
		        data : {deleteRows:JSON.stringify(deleteRows)},  
		        dataType : "json", //返回数据形式为json  
		        success : function(result) {
		            if (result) {
		            	if (!result.success) {
							$.messager.show( {
								title : 'Error',
								msg : result.msg
							});
						} else {
							alert("删除成功！");
							$('#wpslibTable').datagrid('reload');
						}
		            }  
		        },  
		        error : function(errorMsg) {  
		            alert("数据请求失败，请联系系统管理员!");  
		        }  
		   }); 
		}
	}else{
		alert("请先选择一条数据。");
	}
}

function openRemoveMainWps(row){
	$('#rmmwfm').form('clear');
	if (row) {
		$('#rmmwdlg').window( {
			title : "删除工艺",
			modal : true
		});
		$('#rmmwdlg').window('open');
		$('#rmmwfm').form('load', row);
		url = "wps/removeMainWps?fid="+row.fid;
	}
}

function removeMainwps(){
	$.messager.confirm('提示', '此操作不可撤销，是否确认删除?', function(flag) {
		if (flag) {
			$.ajax({  
		        type : "post",  
		        async : false,
		        url : url,  
		        data : {},  
		        dataType : "json", //返回数据形式为json  
		        success : function(result) {
		            if (result) {
		            	if (!result.success) {
							$.messager.show( {
								title : 'Error',
								msg : result.msg
							});
						} else {
							$.messager.alert("提示", "删除成功！");
							$('#rmmwdlg').dialog('close');
							$('#wpslibTable').datagrid('reload');
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

var rurl;
function rmSxWps(row){
	$('#sxfm').form('clear');
	if (row) {
		$('#editSxDlg').window( {
			title : "删除工艺",
			modal : true
		});
		$("#sxRemoveWpsBut").show();
		$("#sxgetWpsBut").hide();
		$("#sxSaveWpsBut").hide();
		$('#editSxDlg').window('open');
		$('#sxfm').form('load', row);
		$("input[name='sxfcharacter']").eq(row.sxfcharacter).prop("checked", true);
		rurl = "wps/removeMainWps?fid="+row.fid;
	}
}

function removeSxwps(){
	$.messager.confirm('提示', '此操作不可撤销，是否确认删除?', function(flag) {
		if (flag) {
			$.ajax({  
		        type : "post",  
		        async : false,
		        url : rurl,  
		        data : {},  
		        dataType : "json", //返回数据形式为json  
		        success : function(result) {
		            if (result) {
		            	if (!result.success) {
							$.messager.show( {
								title : 'Error',
								msg : result.msg
							});
						} else {
							$.messager.alert("提示", "删除成功！");
							$('#editSxDlg').dialog('close');
							$('#wpslibTable').datagrid('reload');
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