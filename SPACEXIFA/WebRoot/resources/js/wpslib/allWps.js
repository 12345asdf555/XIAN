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
		fitColumns : true,
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
			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'fproduct_drawing_no',
			title : '产品图号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fproduct_name',
			title : '产品名称',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fproduct_version',
			title : '产品版本号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fwps_lib_name',
			title : '工艺规程编号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fwps_lib_version',
			title : '工艺规程版本号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'flag',
			title : '工艺来源标志',
			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'flag_name',
			title : '工艺来源',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fstatus',
			title : '状态id',
			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'fstatus_name',
			title : '审核状态',
			width : 100,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = "";
				if(row.fstatus==0){
					str += '<a id="wait" class="easyui-linkbutton"/>';
				}
				if(row.fstatus==1){
					str += '<a id="finish" class="easyui-linkbutton"/>';
				}
				return str;
			}
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
			if($("#finish").length!=0){
				$("a[id='finish']").linkbutton({text:'已通过',plain:true,iconCls:'icon-over'});
			}
		}
	});
}

function searchWps(){
	
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
	
	$("#femployeeTable").datagrid('resize', {
		height : $("#addOrUpdate").height()*0.4,
		width : $("#addOrUpdate").width()*0.64*0.33,
	});
}