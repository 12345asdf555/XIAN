var url = "";
function removeWeldedjunction(){
	$('#rfm').form('clear');
	var row = $('#weldTaskTable').datagrid('getSelected');
	$.ajax({  
	      type : "post",  
	      async : false,
	      url : "weldedjunction/getCouneByTaskid?taskid="+row.id+"&type="+"",  
	      data : {},  
	      dataType : "json", //返回数据形式为json  
	      success : function(result) {  
	          if (result==false) {
	        	  alert("任务已被执行或者已完成，无法进行操作！！");
	          }else{
	        		if (row) {
	        			$('#rdlg').window( {
	        				title : "删除任务",
	        				modal : true
	        			});
	        			$('#rdlg').window('open');
	        			$('#rfm').form('load', row);
	        			url = "weldtask/removeWeldTask?id="+row.id+"&insfid="+row.iid;
	        		}
	          }
	      },  
	      error : function(errorMsg) {  
	          alert("数据请求失败，请联系系统管理员!");  
	      }  
	}); 
}

function remove(){
	$.messager.confirm('提示', '此操作不可撤销，是否确认删除?', function(flag) {
		if (flag) {
			document.getElementById("load").style.display="block";
			var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
			$("#body").append(sh);
			document.getElementById("show").style.display="block";
			$.ajax({  
		        type : "post",  
		        async : false,
		        url : url,  
		        data : {},  
		        dataType : "json", //返回数据形式为json  
		        success : function(result) {
		            if (result) {
		            	if (!result.success) {
				    		document.getElementById("load").style.display ='none';
				    		document.getElementById("show").style.display ='none';
							$.messager.show( {
								title : 'Error',
								msg : result.msg
							});
						} else {
				    		document.getElementById("load").style.display ='none';
				    		document.getElementById("show").style.display ='none';
							$.messager.alert("提示", "删除成功！");
							$('#rdlg').dialog('close');
							$('#weldTaskTable').datagrid('reload');
//							var url = "weldedjunction/goWeldedJunction";
//							var img = new Image();
//						    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
//						    url = img.src;  // 此时相对路径已经变成绝对路径
//						    img.src = null; // 取消请求
//							window.location.href = encodeURI(url);
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