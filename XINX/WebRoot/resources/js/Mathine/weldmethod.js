var quarray = new Array();

$(function(){
	//typeCombobox();
	manuModel();
	Qualifications();
	weldmachine();
	//DictionaryDataGrid();
//	$('#dlg').dialog( {
//		onClose : function() {
//			$('#desc').combobox('clear');
//			$("#fm").form("disableValidation");
//		}
//	})
	$("#fm").form("disableValidation");
});

function weldmachine(){
	$("#tt").datagrid( {
		fitColumns : true,
		height : $("#body").height(),
		width : $("#body").width(),
		idField : 'id',
		url:"weldingMachine/getlibararylist2",
		singleSelect : false,
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
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
              //  color.class="rowColor";
                return color;
            }
		},
		onBeforeLoad:function(data){
			 $('#tt').datagrid('clearChecked');
		},
		onLoadSuccess:function(data){
			$("#descid").combobox({
				onChange : function(newValue, oldValue) {
					$("#tt").datagrid('clearSelections');
					for(var i=0;i<quarray.ary.length;i++){
						if(parseInt(quarray.ary[i].level)==newValue){
							for(var j=0;j<data.rows.length;j++){
								if(parseInt(quarray.ary[i].method)==data.rows[j].id){
									$("#tt").datagrid('selectRow',j);
								}
							}
						}
					}
				}
			})
		}
	});
}


//提交
function save(){
	var back = $("#descid").combobox("getValue");
	var rows = $('#tt').datagrid('getSelections');
	var str="";
		for(var i=0; i<rows.length; i++){
			str += rows[i].id+",";
		}
	$('#form').form('submit', {
		url : "weldingMachine/getweldmethod?back="+back+"&str="+str,
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
					manuModel();
					$.messager.alert("提示", "绑定成功！");
					//$('#dlg').dialog('close');
//					$('#dg').datagrid('reload');
				}
			}
			
		},  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    } 
	});
}

//资质等级
function Qualifications(){
	$.ajax({  
	  type : "post",  
	  async : false,
	  url : "Dictionary/getValueByTypeid?type="+ 7,  
	  data : {},  
	  dataType : "json", //返回数据形式为json  
	  success : function(result) {  
	      if (result) {
	          var optionStr = '';
	          for (var i = 0; i < result.ary.length; i++) {  
	              optionStr += "<option value=\"" + result.ary[i].value + "\" >"  
	                      + result.ary[i].name + "</option>";
	          }
	        //  $("#descid").html(optionStr);
	          $("#descid").append(optionStr);
	          //$("#desc").combobox('setvalue',result.ary[0].name);
	      }  
	  },  
	  error : function(errorMsg) {  
	      alert("数据请求失败，请联系系统管理员!");  
	  }  
	}); 
	$("#descid").combobox();
}

//资质和焊接方法
function manuModel(){
	$.ajax({  
		  type : "post",  
		  async : false,
		  url : "weldingMachine/getqua",  
		  data : {},  
		  dataType : "json", //返回数据形式为json  
		  success : function(result) {  
		      if (result) {
		    	  quarray = eval(result);
		      }  
		  },  
		  error : function(errorMsg) {  
		      alert("数据请求失败，请联系系统管理员!");  
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