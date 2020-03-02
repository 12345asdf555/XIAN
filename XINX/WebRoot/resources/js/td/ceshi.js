var time = new Array();
var series;
var chart;
var display;
$(function() {
	$.ajax({
		type : "post",
		async : true,
		url : "hierarchy/getUserInsframework",
		data : {},
		dataType : "json",
		success : function(result){
//			var str = "<span>"+result.uname+"</span>";
//			$("#username").append(str);
//			for(var r=0;r<result.ary.length;r++){
//				resourceary.push(result.ary[r].resource);
//			}
			anaylsis(result.ipurl);
		},
		error : function(errorMsg){
			alert("数据请求失败，请联系系统管理员!");
		}
	})
	return;
})

function anaylsis(ipurl){
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
		var name = menuinfo[i].getElementsByTagName("TypeName");//设备型号
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
				if (document.all) {
					curveName = curveName[0].text;
					divId = divId[0].text;
					Unit = Unit[0].text;
					Color = Color[0].text;
				} else {
					curveName = curveName[0].textContent;
					divId = divId[0].textContent;
					Unit = Unit[0].textContent;
					Color = Color[0].textContent;
				}
				eval("var "+divId.toString()+"Arr=new Array()");//动态生成的存放数据的数组
				eval(divId.toString()+"Arr").push("xxx");//数组赋值
//				alert(eval(divId.toString()+"Arr[0]"))//数组调用
				var str = '<div id="'+divId+'" style="float:left;width:90%;height:48%;">xxx</div>';
				$("#livediv").append(str);
				eval("series"+d+"=\"\"");
				eval("chart"+d+"=\"\"");
				var cur = {div:divId,name:curveName,color:Color,se:eval("series"+d),ch:eval("chart"+d)}
				curve(cur);
			}
		}
		break;
	}
}

for (var d = 0; d < display.length; d++) {
	var firstPosition = display[d].getElementsByTagName("FirstPosition");//曲线单位
	var lastPosition = display[d].getElementsByTagName("LastPosition");//曲线颜色
	if (document.all) {
		firstPosition = firstPosition[0].text;
		lastPosition = lastPosition[0].text;
	} else {
		firstPosition = firstPosition[0].textContent;
		lastPosition = lastPosition[0].textContent;
	}
	eval("series"+d).addPoint([ Date.parse(new Date(ttme)), parseInt(redata.substring(parseInt(firstPosition) + i, parseInt(lastPosition) + i), 10) ], true, true);
	activeLastPointToolip(eval("chart"+d));
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
	Highcharts.setOptions({
		global : {
			useUTC : false
		}
	});

	$('#'+value.div+'').highcharts({
		chart : {
			type : 'spline',
			animation : false, // don't animate in old IE
			marginRight : 70,
			events : {
				load : function() {
					// set up the updating of the chart each second
					value.se = this.series[0],
					value.ch = this;
				}
			}
		},
		title : {
			text : false
		},
		xAxis : {
			type : 'datetime',
			tickPixelInterval : 150 /*,
	  		        tickWidth:0,
		  		    labels:{
		  		    	enabled:false
		  		    }*/
		},
		yAxis : [ {
			max : 50, // 定义Y轴 最大值  
			min : 0, // 定义最小值  
			minPadding : 0.2,
			maxPadding : 0.2,
			tickInterval : 10,
			color : value.color,
			title : {
				text : '',
				style : {
					color : value.color
				}
			}
		} ],
		tooltip : {
			formatter : function() {
				return '<b>' + this.series.name + '</b><br/>' +
					Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
					Highcharts.numberFormat(this.y, 2);
			}
		},
		legend : {
			enabled : false
		},
		exporting : {
			enabled : false
		},
		credits : {
			enabled : false // 禁用版权信息
		},
		series : [ {
			name : value.name,
			data : (function() {
				// generate an array of random data
				var data = [],
					i;
				for (i = -19; i <= 0; i += 1) {
					data.push({
						x : time[0] - 1000 + i * 1000,
						y : 0
					});
				}
				return data;
			}())
		} ]
	}, function(c) {
//		activeLastPointToolip(c)
	});

//	activeLastPointToolip(chart);

}

function activeLastPointToolip(chart) {
	var points = chart.series[0].points;
	chart.yAxis[0].removePlotLine('plot-line-8');
	chart.yAxis[0].removePlotLine('plot-line-9');
		chart.yAxis[0].addPlotLine({ //在y轴上增加 
		value : warnvol_up, //在值为2的地方 
		width : 2, //标示线的宽度为2px 
		color : 'black', //标示线的颜色 
		dashStyle : 'longdashdot',
		id : 'plot-line-8', //标示线的id，在删除该标示线的时候需要该id标示 });
		label : {
			text : '报警电压上限', //标签的内容
			align : 'center', //标签的水平位置，水平居左,默认是水平居中center
			x : 10
		}
	})
	chart.yAxis[0].addPlotLine({ //在y轴上增加 
		value : warnvol_down, //在值为2的地方 
		width : 2, //标示线的宽度为2px 
		color : 'black', //标示线的颜色 
		dashStyle : 'longdashdot',
		id : 'plot-line-9', //标示线的id，在删除该标示线的时候需要该id标示 });
		label : {
			text : '报警电压下限', //标签的内容
			align : 'center', //标签的水平位置，水平居左,默认是水平居中center
			x : 10 //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
		}
	})
}