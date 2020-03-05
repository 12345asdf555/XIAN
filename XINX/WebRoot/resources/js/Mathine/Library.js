var manumodelArr = new Array();

$(function(){
	//typeCombobox();
	library();
	//DictionaryDataGrid();
//	$('#dlg').dialog( {
//		onClose : function() {
//			$('#desc').combobox('clear');
//			$("#fm").form("disableValidation");
//		}
//	})
	$("#fm").form("disableValidation");
});

function library(){
	$("#dg").datagrid( {
		fitColumns:true,
		height:$("#body").height(),
		width:$("#body").width(),
		idField:'id',
		pageSize:10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"weldingMachine/getlibararylist",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [{
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
		},{
			field:'edit',
			title:'编辑',
			width:250,
			halign:"center",
			align:"left",
			formatter:function(value,row,index){
				var str = "";
				str += '<a id="edit" class="easyui-linkbutton" href="javascript:editlibrary()"/>';
				str += '<a id="remove" class="easyui-linkbutton" href="javascript:removelibrary()"/>';
				return str;
			}
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
		onLoadSuccess:function(data){
	        $("a[id='edit']").linkbutton({text:'修改',plain:true,iconCls:'icon-update'});
	        $("a[id='remove']").linkbutton({text:'删除',plain:true,iconCls:'icon-delete'});
		}
	});
}

var url = "";
var flag = 1;
function addlibrary(){
	flag = 1;
	$('#dlg').window( {
		title : "新增焊接方法",
		modal : true
	});
	$('#dlg').window('open');
	$('#fm').form('clear');
	url = "weldingMachine/addlibrary";
}

function editlibrary(){
	flag = 2;
	$('#fm').form('clear');
	var row = $('#dg').datagrid('getSelected');
	if (row) {
		$('#dlg').window( {
			title : "修改焊接方法",
			modal : true
		});
		$('#dlg').window('open');
		$('#fm').form('load', row);
		url = "weldingMachine/editlibrary?id="+ row.id;
	}
}
var url3 ="";
function removelibrary(){
	$('#rfm').form('clear');
	var row = $('#dg').datagrid('getSelected');
	if (row) {
		$('#rdlg').window( {
			title : "删除焊接方法",
			modal : true
		});
		$('#rdlg').window('open');
		$('#rfm').form('load', row);
		url3 = "weldingMachine/removelibrary?id="+row.id;
	}
}

//提交
function save(){
	var url2= "";
	if(flag==1){
		messager = "新增成功！";
		url2 = url;
	}else{
		messager = "修改成功！";
		url2 =url;
	}
	$('#fm').form('submit', {
		url : url2,
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if(result){
				var result = eval('(' + result + ')');
				if (!result.success) {
					$.messager.show( {
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					$.messager.alert("提示", messager);
					$('#dlg').dialog('close');
					$('#dg').datagrid('reload');
				}
			}
		},  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    } 
	});
}

function remove(){
	$.messager.confirm('提示', '此操作不可撤销并同时解绑焊机设备，是否确认删除?', function(flag) {
		if (flag) {
			$.ajax({  
		        type : "post",  
		        async : false,
		        url : url3,  
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
							$('#rdlg').dialog('close');
							$('#dg').datagrid('reload');
//							var url = "gather/goGather";
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

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
	$("#tt").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}