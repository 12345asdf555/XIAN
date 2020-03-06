/**
 * 
 */
function deleteWps(){
	var deleteRows = $("#cardTable").datagrid('getSelected'); //获取删除的数据
	if(deleteRows){
		if(deleteRows[0].flag == 1){
			alert("该参数从MES获取，无法进行修改删除操作！！！");
			return;
		}
		var c=confirm("该操作将删除所选数据及其关联数据，并且无法撤销，是否继续？")
		if (c == true) {
			$.ajax({  
				type : "post",  
				async : false,
				url : "weldtask/deleteCard?fid="+deleteRows[0].fid,  
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
							alert("删除成功！");
							$('#cardTable').datagrid('reload');
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