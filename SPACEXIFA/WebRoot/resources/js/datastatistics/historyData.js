var ipurl="";
var xAxisData = new Array(),yAxisData = new Array(),fieldArr = new Array(),divArr = new Array();
$(function(){
	historyDatagrid();
	$.ajax({
		type : "post",
		async : true,
		url : "hierarchy/getUserInsframework",
		data : {},
		dataType : "json",
		success : function(result){
			ipurl = result.ipurl;
		},
		error : function(errorMsg){
			alert("数据请求失败，请联系系统管理员!");
		}
	})
})

function historyDatagrid(){
	$("#dg").datagrid( {
		fitColumns : true,
		height : $("#tableDiv").height(),
		width : $("#tableDiv").width(),
		idField : 'fid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "/",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [ {
			field : 'fcard_id',
			title : '电子跟踪卡id',
			width : 50,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'fcard_no',
			title : '电子跟踪卡号',
			width : 250,
			halign : "center",
			align : "left"
		}, {
			field : 'fproduct_id',
			title : '产品id',
//			width : 200,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fproduct_no',
			title : '产品序号',
			width : 200,
			halign : "center",
			align : "left"
		}, {
			field : 'fequipment_id',
			title : '设备id',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fmodel',
			title : '型号',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fequipment_no',
			title : '设备编号',
			width : 200,
			halign : "center",
			align : "center"
		}, {
			field : 'fwelder_id',
			title : '焊工id',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fwelder_no',
			title : '焊工编号',
			width : 200,
			halign : "center",
			align : "center"
		}, {
			field : 'femployee_id',
			title : '工序id',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'femployee_no',
			title : '工序号',
			width : 200,
			halign : "center",
			align : "center"
		}, {
			field : 'fstep_id',
			title : '工步id',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fstep_no',
			title : '工步号',
			width : 200,
			halign : "center",
			align : "center"
		}, {
			field : 'fjunction_id',
			title : '焊缝id',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fjunction_no',
			title : '焊缝编号',
			width : 200,
			halign : "center",
			align : "center"
		}, {
			field : 'fwelding_area',
			title : '焊接部位',
			width : 200,
			halign : "center",
			align : "center"
		}, {
			field : 'fstart_time',
			title : '开始时间',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'fend_time',
			title : '结束时间',
//			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}] ],
		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
        onDblClickRow: function(rowIndex, rowData){
        	document.getElementById("historyCurveDiv").innerHTML="";
        	divArr.length=0;
        	fieldArr.length=0;
        	var startTime = $("#dtoTime1").textbox('getValue');
        	var endTime = $("#dtoTime2").textbox('getValue');
        	//处理ie不支持indexOf
        	if (!Array.prototype.indexOf){
          		Array.prototype.indexOf = function(elt /*, from*/){
        	    var len = this.length >>> 0;
        	    var from = Number(arguments[1]) || 0;
        	    from = (from < 0)
        	         ? Math.ceil(from)
        	         : Math.floor(from);
        	    if (from < 0)
        	      from += len;
        	    for (; from < len; from++)
        	    {
        	      if (from in this &&
        	          this[from] === elt)
        	        return from;
        	    }
        	    return -1;
        	  };
        	}
        	var object = loadxmlDoc(ipurl+"ConfigFile/machine.xml");
        	var menuinfo = object.getElementsByTagName("Typeinfo");
        	for (var i = 0; i < menuinfo.length; i++) {
        		var name = menuinfo[i].getElementsByTagName("TypeName");//型号
        		var value = menuinfo[i].getElementsByTagName("TypeValue");//value值
        		if (document.all) {
        			name = name[0].text;
        			value = value[0].text;
        		} else {
        			name = name[0].textContent;
        			value = value[0].textContent;
        		}
        		if(value == 15){
        			display = menuinfo[i].getElementsByTagName("Display");//显示内容
        			for (var d = 0; d < display.length; d++) {
        				var curveName = display[d].getElementsByTagName("CurveName");//曲线名称
        				var divId = display[d].getElementsByTagName("DivId");//div的id
        				var Unit = display[d].getElementsByTagName("Unit");//曲线单位
        				var Color = display[d].getElementsByTagName("Color");//曲线颜色
        				var MaxValue = display[d].getElementsByTagName("MaxValue");//曲线最大值
        				var MinValue = display[d].getElementsByTagName("MinValue");//曲线最小值
        				var filed = display[d].getElementsByTagName("filed");//数据库字段
        				if (document.all) {
        					curveName = curveName[0].text;
        					divId = divId[0].text;
        					Unit = Unit[0].text;
        					Color = Color[0].text;
        					MaxValue = MaxValue[0].text;
        					MinValue = MinValue[0].text;
        					filed = filed[0].text;
        				} else {
        					curveName = curveName[0].textContent;
        					divId = divId[0].textContent;
        					Unit = Unit[0].textContent;
        					Color = Color[0].textContent;
        					MaxValue = MaxValue[0].textContent;
        					MinValue = MinValue[0].textContent;
        					filed = filed[0].textContent;
        				}
        				var str = '<div style="float:left;width:50%;height:50%;">'+
    		       		'<div style="float:left; padding-top:1%;width:3%;height:80%;background-color: #37d512;border-radius: 6px;font-size:14pt;color:#ffffff;margin:10px;text-align: center;">'+
    		       		curveName+'<div style="width:90%;height:18%;border-radius: 60%;font-size:14pt;background-color: #ffffff;color: #000;">'+Unit+'</div></div>'+
    					'<div id="'+divId+'" style="float:left;width:94%;height:95%;"></div></div>';
        				$("#historyCurveDiv").append(str);
//            				eval("series"+d+"=[]");
        				eval("chart"+d+"={}");
        				var s = "series"+d;
        	            window[s] = [];
        				var cur = {div:divId,name:curveName,color:Color,max:MaxValue,min:MinValue,se:"series"+d,ch:"chart"+d}
        				curve(cur);
//        				fieldArr.push(filed);
//        				divArr.push(divId);
        				fieldArr.push(filed);
        				divArr.splice(0, 0, divId);
        				//eval("var "+divId.toString()+"Arr=new Array()");//动态生成的存放数据的数组
        				//eval(divId.toString()+"Arr").push("xxx");//数组赋值
//        				alert(eval(divId.toString()+"Arr[0]"))//数组调用
        			}
        			$.ajax({
    	        		type : "post",
    	        		async : true,
    	        		url : "datastatistics/getHistoryData?startTime="+startTime+"&endTime="+endTime,
    	        		data : {row:JSON.stringify(rowData),
    	        				fieldArr:JSON.stringify(fieldArr)},
    	        		dataType : "json",
    	        		success : function(result){
    	        			xAxisData.length=0;
    	        			yAxisData.length=0;
    	        			xAxisData = eval(result.aryX);
    	        			yAxisData = eval(result.aryY);
    	        			for(var x=0;x<xAxisData.length;x++){
    	        				eval("myChart"+divArr.pop()).setOption({
    	        					xAxis:{
    	        						data:xAxisData[x]
    	        					},
    	        					series:[{
    	        						data:yAxisData[x]
    	        					}
    	        					]
    	        				});
    	        			}
    	        		},
    	        		error : function(errorMsg){
    	        			alert("数据请求失败，请联系系统管理员!");
    	        		}
    	        	});
        		}
        		break;
        	}
		}
	});
}

function searchHistory(){
	var searchStr = "";
	var fcard_id = $("#fcard_id").textbox('getValue');
	var fprefix_number = $("#fprefix_number").textbox('getValue');
	var fsuffix_number = $("#fsuffix_number").textbox('getValue');
	var fproduct_id = $("#fproduct_id").textbox('getValue');
	var fequipment_id = $("#fequipment_id").textbox('getValue');
	var fwelder_id = $("#fwelder_id").textbox('getValue');
	var femployee_id = $("#femployee_id").textbox('getValue');
	var fstep_id = $("#fstep_id").textbox('getValue');
	var fjunction_id = $("#fjunction_id").textbox('getValue');
	var fwelding_area = $("#fwelding_area").textbox('getValue');
	var dtoTime1 = $("#dtoTime1").textbox('getValue');
	var dtoTime2 = $("#dtoTime2").textbox('getValue');
	if(fcard_id != ""){
		searchStr += " AND c.fwelded_junction_no LIKE "+"'%" + fcard_id + "%'";
	}
	if(fproduct_id != ""){
		searchStr += " AND p.fproduct_number LIKE "+"'%" + fproduct_id + "%'";
	}
	if(fprefix_number!=""){
		searchStr += " AND p.fprefix_number LIKE "+"'%" + fprefix_number + "%'";
	}
	if(fsuffix_number!=""){
		searchStr += " AND p.fsuffix_number LIKE "+"'%" + fsuffix_number + "%'";
	}
	if(fequipment_id != ""){
		searchStr += " AND m.fequipment_no LIKE "+"'%" + fequipment_id + "%'";
	}
	if(fwelder_id != ""){
		searchStr += " AND r.fname LIKE "+"'%" + fwelder_id + "%'";
	}
	if(femployee_id != ""){
		searchStr += " AND e.femployee_id LIKE "+"'%" + femployee_id + "%'";
	}
	if(fstep_id != ""){
		searchStr += " AND s.fstep_number LIKE "+"'%" + fstep_id + "%'";
	}
	if(fjunction_id != ""){
		searchStr += " AND j.fjunction LIKE "+"'%" + fjunction_id + "%'";
	}
	if(fwelding_area != ""){
		searchStr += " AND j.fwelding_area LIKE "+"'%" + fwelding_area + "%'";
	}
	if(dtoTime1 != ""){
		searchStr += " AND w.fstarttime>="+"to_date('" + dtoTime1 + "', 'yyyy-mm-dd hh24:mi:ss')";
	}
	if(dtoTime2 != ""){
		searchStr += " AND w.fendtime<="+"to_date('" + dtoTime2 + "', 'yyyy-mm-dd hh24:mi:ss')";
	}
	$('#dg').datagrid("options").url="datastatistics/getHistoryDatagridList";
	$('#dg').datagrid('load', {
		"searchStr" : searchStr
	});
}

function loadxmlDoc(file) {
	try {
		//IE
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
	} catch (e) {
		//Firefox, Mozilla, Opera, etc
		xmlDoc = document.implementation.createDocument("", "", null);
	}

	try {
		xmlDoc.async = false;
		xmlDoc.load(file); //chrome没有load方法
	} catch (e) {
		//针对Chrome,不过只能通过http访问,通过file协议访问会报错
		var xmlhttp = new window.XMLHttpRequest();
		xmlhttp.open("GET", file, false);
		xmlhttp.send(null);
		xmlDoc = xmlhttp.responseXML.documentElement;
	}
	return xmlDoc;
}

function curve(value) {
	eval("myChart"+value.div+"="+"echarts.init(document.getElementById('"+value.div+"'))");
    var option = {
        backgroundColor: '#fff',
//        title : {
//            text : value.name
//        },
        tooltip : {
            trigger : 'axis'
        },
        toolbox : {
            show : true,
            feature : {
                mark : {
                    show : false
                },
                dataView : {
                    show : false,
                    readOnly : false
                },
                restore : {
                    show : false
                },
                saveAsImage: {}
            }
        },
        dataZoom : [
        	{
                type: 'slider',
                show: true,
                xAxisIndex: [0]
            },
            {
                type: 'inside',
                xAxisIndex: [0]
            }
        ],
        grid : {
			left:'8%',//组件距离容器左边的距离
			right:'5%',
			top:"5%",
			bottom:60
        },
        xAxis : [ {
			type:'category',
			data: []
        } ],
        yAxis : [ {
            type : 'value',
            max : parseFloat(value.max),
            min : parseFloat(value.min)
        } ],
        series : [ {
            symbolSize : 5,//气泡大小
      		name : value.name,
      		type :'line',//折线图
      		data : [], 
      		itemStyle: {
      	        normal: {
      	            color: "#A020F0",
      	            lineStyle: {
      	                color: "#A020F0"
      	            }
      	        }
      	    }
        } ]
    };
    eval("myChart"+value.div).setOption(option);
}