$(function(){
	insframworkCombobox();
	leveCombobox();
	quaidCombobox();
	$('#dlg').dialog( {
		onClose : function() {
			$('#leveid').combobox('clear');
			$('#quali').combobox('clear');
			$('#owner').combobox('clear');
			$("#fm").form("disableValidation");
		}
	})
	$("#fm").form("disableValidation");
	$("#welderno").textbox('textbox').blur(function(){
		var welderno = $("#welderno").val();
		var len = welderno.length;
		if(len<8){
			for(var i=0;i<8-len;i++){
				welderno = "0"+welderno;
			}
			$("#welderno").textbox('setValue',welderno);
		}
	});
})

function insframworkCombobox(){
	   $.ajax({
	   type: "post", 
	   async : false,
	   url: "weldingMachine/getInsframeworkAll",
	   dataType: "json",
	   data: {},
	   success: function (result) {
	      if (result) {
	         var optionstring = "";
	         //循环遍历 下拉框绑定
             for (var i = 0; i < result.ary.length; i++) {  
            	 optionstring += "<option value=\"" + result.ary[i].id + "\" >" + result.ary[i].name + "</option>";
             }
	         $("#owner").html(optionstring);
	      } else {
	         alert('部门加载失败');
	      }
	      $("#owner").combobox();
	   },
	   error: function () {
	      alert('error');
	   }
	});
	
}

function leveCombobox(){
	var we=8;
	   $.ajax({
	   type: "post", 
	   async : false,
	   url: "welders/getLeve?we="+we,
	   dataType: "json",
	   data: {},
	   success: function (result) {
	      if (result) {
	         var optionstring = "";
	         //循环遍历 下拉框绑定
	         for(var k=0;k<result.rows.length;k++){
	         optionstring += "<option value=\"" + result.rows[k].leveid + "\" >" + result.rows[k].levename + "</option>";
	         }
	         $("#leveid").html(optionstring);
	      } else {
	         alert('数据加载失败');
	      }
	      $("#leveid").combobox();
	   },
	   error: function () {
	      alert('error');
	   }
	});
}
		
function quaidCombobox(){
	var wee=7;
		   $.ajax({
		   type: "post", 
		   async : false,
		   url: "welders/getLeve?we="+wee,
		   dataType: "json",
		   data: {},
		   success: function (result) {
		      if (result) {
		         var optionstring = "";
		         //循环遍历 下拉框绑定
		         for(var k=0;k<result.rows.length;k++){
		         optionstring += "<option value=\"" + result.rows[k].quaid + "\" >" + result.rows[k].quaname + "</option>";
		         }
		         $("#quali").html(optionstring);
		      } else {
		         alert('数据加载失败');
		      }
		      $("#quali").combobox();
		   },
		   error: function () {
		      alert('error');
		   }
		});
}

var url = "";
var flag = 1;
function saveWelder(){
	flag = 1;
	$('#dlg').window( {
		title : "新增焊工",
		modal : true
	});
	$('#dlg').window('open');
	$('#fm').form('clear');
	library1();
	url = "welders/addWelder";
}

function editWelder(){
	flag = 2;
	$('#fm').form('clear');
	var row = $('#welderTable').datagrid('getSelected');
	if (row) {
		$('#dlg').window( {
			title : "修改焊工",
			modal : true
		});
		$('#dlg').window('open');
		library1();
		$('#fm').form('load', row);
		$('#validName').val(row.welderno);
		url = "welders/updateWelder?FID="+ row.id;
	}
}
//提交
function save(){
	var welderno = $("#welderno").val();
	var len = welderno.length;
	if(len>8){
		alert("焊工编号超出指定的8位长度!!!");
		return;
	}
	if(welderno=="00000000"){
		alert("焊工编号不能全为0!!!");
		return;
	}
	var rows = $('#dg').datagrid('getSelections');
	var str="";
	for(var i=0; i<rows.length; i++){
		str += rows[i].id+",";
	}
    var insframework = $('#owner').combobox('getValue');
    //var leve = $('#leveid').combobox('getValue');
    var leve;
    var qua = $('#quali').combobox('getValue');
    //var method = $('#method').combobox('getValue');
    var method;
	var url2 = "";
	if(flag==1){
		messager = "新增成功！";
		url2 = url+"?ins="+insframework+"&leve="+""+"&qua="+qua+"&method="+""+"&str="+str;
	}else{
		messager = "修改成功！";
		url2 = url+"&ins="+insframework+"&leve="+""+"&qua="+qua+"&method="+""+"&str="+str;
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
					$('#welderTable').datagrid('reload');
				}
			}
			
		},  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    } 
	});
}

        