/**
 * 
 */
$(function(){
	wpslibDatagrid();
})

var chartStr = "";
function setParam(){
	var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
	var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
	var machineNum = $("#machineNum").numberbox('getValue');
	var wpslibName = $("#theWpslibName").textbox('getValue');
	chartStr += "?machineNum="+machineNum+"&wpslibName="+encodeURI(wpslibName)+"&dtoTime1="+dtoTime1+"&dtoTime2="+dtoTime2;
}

function wpslibDatagrid(){
	$("#wpslibTable").datagrid( {
		fitColumns : false,
		height : $("#body").height(),
		width : $("#body").width(),
		idField : 'fid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "wps/getWpsList",
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
		}, {
			field : 'fstatus',
			title : '状态id',
//			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'fstatus_name',
			title : '审核状态',
			width : 100,
			halign : "center",
			align : "center",
			formatter: function(value,row,index){
				var str = "";
				if(row.fstatus==0){
					str += '<a id="wait" class="easyui-linkbutton"/>';
				}
				if(row.fstatus==2){
					str += '<a id="down" class="easyui-linkbutton"/>';
				}
				if(row.fstatus==1){
					str += '<a id="finish" class="easyui-linkbutton"/>';
				}
				return str;
			}
		}, {
			field : 'fback',
			title : '驳回原因',
			width : 400,
			halign : "center",
			align : "left"
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
				$("a[id='wait']").linkbutton({text:'待审核',plain:true,iconCls:'icon-newcancel'});
			}
			if($("#down").length!=0){
				$("a[id='down']").linkbutton({text:'被驳回',plain:true,iconCls:'icon-next'});
			}
			if($("#finish").length!=0){
				$("a[id='finish']").linkbutton({text:'已通过',plain:true,iconCls:'icon-over'});
			}
		}
	});
}

function searchWps(){
	var search = "";
	var product_drawing_no = $("#product_drawing_no").textbox('getValue');
	var product_name = $("#product_name").textbox('getValue');
	var product_version = $("#product_version").textbox('getValue');
	var wps_lib_name = $("#wps_lib_name").textbox('getValue');
	var wps_lib_version = $("#wps_lib_version").textbox('getValue');
	var wflag = $("#wflag").combobox("getValue");
	var status = $("#status").combobox("getValue");
	if(product_drawing_no != ""){
		if(search == ""){
			search += " fproduct_drawing_no LIKE "+"'%" + product_drawing_no + "%'";
		}else{
			search += " AND fproduct_drawing_no LIKE "+"'%" + product_drawing_no + "%'";
		}
	}
	if(product_name != ""){
		if(search == ""){
			search += " fproduct_name LIKE "+"'%" + product_name + "%'";
		}else {
			search += " AND fproduct_name LIKE "+"'%" + product_name + "%'";
		}
	}
	if(product_version != ""){
		if(search == ""){
			search += " fproduct_version LIKE "+"'%" + product_version + "%'";
		}else{
			search += " AND fproduct_version LIKE "+"'%" + product_version + "%'";
		}
	}
	if(wps_lib_name != ""){
		if(search == ""){
			search += " fwps_lib_name LIKE "+"'%" + wps_lib_name + "%'";
		}else{
			search += " AND fwps_lib_name LIKE "+"'%" + wps_lib_name + "%'";
		}
	}
	if(wps_lib_version != ""){
		if(search == ""){
			search += " fwps_lib_version LIKE "+"'%" + wps_lib_version + "%'";
		}else{
			search += " AND fwps_lib_version LIKE "+"'%" + wps_lib_version + "%'";
		}
	}
	if(wflag != ""){
		if(search == ""){
			search += " flag=" + wflag;
		}else{
			search += " AND flag=" + wflag;
		}
	}
	if(status != ""){
		if(search == ""){
			search += " fstatus=" + status;
		}else{
			search += " AND fstatus=" + status;
		}
	}
	$('#wpslibTable').datagrid('load', {
		"search" : search
	});
}

function wpsDetails(){
	var row = $('#wpslibTable').datagrid('getSelected'); 
	if (row) {
		window.location.href = encodeURI("wps/goWpsdetails"+"?fid="+row.fid+"&fproduct_name="+row.fproduct_name+"&status="+row.fstatus);
	}else{
		alert("请先选择一条数据。");
	}
}

function closeDlg(){
	if(!$("#addOrUpdate").parent().is(":hidden")){
		$('#addOrUpdate').window('close');
	}
}

//打开文件导入dialog
function importclick(){
	$("#importdiv").dialog("open").dialog("setTitle","从excel导入数据");
}

//确认导入
function importWpsExcel(){
	var file = $("#file").val();
	if(file == null || file == ""){
		$.messager.alert("提示", "请选择要上传的文件！");
		return false;
	}else{
		document.getElementById("load").style.display="block";
		var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
		$("#body").append(sh);
		document.getElementById("show").style.display="block";
		$('#importfm').form('submit', {
			url : "import/importweldWps",
			success : function(result) {
				if(result){
					var result = eval('(' + result + ')');
		    		document.getElementById("load").style.display ='none';
		    		document.getElementById("show").style.display ='none';
					if (!result.success) {
						$.messager.show( {
							title : 'Error',
							msg : result.msg
						});
					} else {
						$('#importdiv').dialog('close');
						$('#wpslibTable').datagrid('reload');
						alert("导入成功");
					}
				}
				
			},  
		    error : function(errorMsg) {  
		        alert("数据请求失败，请联系系统管理员!");  
		    } 
		});
		
	}
}

//导出到excel
function exportclick(){
	$.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！",function(result){
		if(result){
			var url = "export/exporWeldwps";
			var img = new Image();
		    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
		    url = img.src;  // 此时相对路径已经变成绝对路径
		    img.src = null; // 取消请求
			window.location.href = encodeURI(url);
		}
	});
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize(), 500);
}

//改变表格高宽
function domresize() {
	$("#wpslibTable").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}