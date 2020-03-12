$(function(){
	$("#rfm").form("disableValidation");
})
var url = "";
var flag = "";
 function removeWelder(){
	 	flag = 2;
		$('#rfm').form('clear');
		var row = $('#welderTable').datagrid('getSelected');
		if (row) {
			$('#rdlg').window( {
				title : "删除焊工",
				modal : true
		});
		$('#rdlg').window('open');
		library();
		$('#rfm').form('load', row);
		url = "welders/destroyWelder?fid="+row.id;
	}
}
 function library(){
		var urls="";
		var row = $('#welderTable').datagrid('getSelected');
		if(flag==2){
			urls="weldingMachine/getlibararylist1?id="+row.id;
		}else{
			urls="weldingMachine/getlibararylist";
		}
		$("#rdg").datagrid( {
			fitColumns:true,
			height : '250px',
			width : '80%',
			idField:'id',
			pageSize:10,
			pageList : [ 10, 20, 30, 40, 50 ],
			url:urls,
			rownumbers : false,
			showPageList : false,
			checkOnSelect:true,
			selectOnCheck:true,
			columns : [ [ {
			    field:'ck',
				checkbox:true
			},{
				field : 'id',
				title : 'id',
				width : 300,
				halign : "center",
				align : "left",
				hidden: true
			},{
				field : 'mvaluename',
				title : '焊接方法',
				width : 300,
				halign : "center",
				align : "center"
			},{
				field : 'model',
				title : '等级',
				width : 300,
				halign : "center",
				align : "left"
				//hidden:true
			}]],
			pagination : true,
			nowrap : false,
			rowStyler: function(index,row){
	            if ((index % 2)!=0){
	            	//处理行代背景色后无法选中
	            	var color=new Object();
	              //  color.class="rowColor";
	                return color;
	            }
			},
			onBeforeLoad:function(data){
				 $('#rdg').datagrid('clearChecked');
			},
			onLoadSuccess:function(data){
				 if(data){
					 $.each(data.rows, function(index, item){
						 if(item.symbol==1){
					         $('#rdg').datagrid('checkRow', index);
						 }
					 })
				 }
			}
		});
	}
	function remove(){
	$.messager.confirm('提示', '此操作不可撤销，是否确认删除?', function(flag) {
		if (flag) {
		    $('#rfm').form('submit',{
		        url: url,
		        onSubmit: function(){
		             return $(this).form('enableValidation').form('validate');
		        },
		        success: function(result){
		            var result = eval('('+result+')');
		            if (result.errorMsg){
		                $.messager.show({
		                    title: 'Error',
		                    msg: result.errorMsg
		                });
		            } else {
		      			$.messager.alert("提示", "删除成功");
						$('#rdlg').dialog('close');
						$('#welderTable').datagrid('reload');
//						var url = "welders/AllWelder";
//						var img = new Image();
//					    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
//					    url = img.src;  // 此时相对路径已经变成绝对路径
//					    img.src = null; // 取消请求
//						window.location.href = encodeURI(url);
		            }
		        }
		    })
		}
	});
}
         