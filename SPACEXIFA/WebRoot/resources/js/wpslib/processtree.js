var node11;
$(function(){
	insftrameworkTree();
	insframeworkTree();
})

//组织机构树
function insftrameworkTree(){
	 $("#processTree").tree({  
         url:'wps/getWpsTree?fid='+$("#flagId").val(),//请求路径
         onLoadSuccess:function(node,data){  
              var tree = $(this);  
              if(data){  
                  $(data).each(function(index,d) {  
                      if (this.state=='closed') {  
                          tree.tree('expandAll');  
                      }  
                  });  
              }  
         }
     });
}

//点击组织机构树触发
function insframeworkTree() {
	$("#processTree").tree({
		onClick : function(node) {
			if (node.attributes) {
				$('#wpsDetailTable').datagrid("options").url="wps/getInfo?search=" + node.id +"&valueFlag=3";
				$('#wpsDetailTable').datagrid('reload');
				$.ajax({
					type : "post",
					async : false,
					url : "wps/getInfo?search=" + node.id +"&valueFlag=2",
					dataType : "json",
					data : {},
					success : function(result) {
						if (result) {
							var optionstring = "";
							//循环遍历 下拉框绑定
							for (var k = 0; k < result.rows.length; k++) {
								optionstring += "<option value=\"" + result.rows[k].fid + "\" >" + result.rows[k].fjunction + "</option>";
							}
							$("#fjunction").html(optionstring);
						} else {
							alert('焊缝加载失败');
						}
						$("#fjunction").combobox();
					},
					error : function() {
						alert('error');
					}
				});
				}
		}
	})
}