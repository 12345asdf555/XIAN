var work = new Array();
var wait = new Array();
var weld = new Array();
var mall = new Array();
var warn = new Array();
var machineary = [],
	mallary = [];
var websocketURL;
var socket;
var redata;
var symbol = 0;
var machine;
var namex;
var worknum = 0,
	waitnum = 0,
	warnnum = 0,
	offnum = 0,
	weldnum = 0,
	personnum = 0,
	machineflag = 0,
	personfalg = 0;

$(function() {
	welder();
	machine();
	websocketurl();
	websocket();
})

$(document).ready(function() {
	showPersonChart();
	showWelderChart();
})

function welder() {
	//焊工总数name.length
	$.ajax({
		type : "post",
		async : false,
		url : "td/allWeldname",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				namex = eval(result.rows);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}
function machine() {
	//焊机总数machine.length
	$.ajax({
		type : "post",
		async : false,
		url : "td/getAllPosition",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				machine = eval(result.rows);
				for (var i = 0; i < machine.length; i++) {
					machineary.push(machine[i].fid);
				}
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

function websocketurl() {
	$.ajax({
		type : "post",
		async : false,
		url : "td/AllTdbf",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				websocketURL = eval(result.web_socket);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

function websocket() {
	if (typeof (WebSocket) == "undefined") {
		WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
		WEB_SOCKET_DEBUG = true;
	}
	webclient();
}
;
function webclient() {
	try {
		socket = new WebSocket(websocketURL);
	} catch (err) {
		alert("地址请求错误，请清除缓存重新连接！！！")
	}
	setTimeout(function() {
		if (socket.readyState != 1) {
//			alert("与服务器连接失败,请检查网络设置!");
		}
	}, 10000);
	socket.onopen = function() {
	};
	socket.onmessage = function(msg) {
		var xxx = msg.data;
		if (xxx.length == 333 || xxx.length == 111) {
			if (xxx.substring(0, 2) != "7E") {
				redata = msg.data;
				if (symbol == 0) {
					window.setTimeout(function() {
						showWelderChart();
						showPersonChart();
					}, 3000)
					symbol = 1;
				}

				for (var i = 0; i < redata.length; i += 111) {
					if (redata.substring(0 + i, 4 + i) != "0000") {
						//组织机构与焊工编号都与数据库中一致则录入
						if (weld.length == 0) {
							weld.push(redata.substring(0 + i, 4 + i));
						} else {
							if ($.inArray(redata.substring(0 + i, 4 + i), weld) == -1) {
								weld.push(redata.substring(0 + i, 4 + i));
							} else {
								break;
							}
						}
					}
					if ($.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), machineary) != -1) {
						if(mall.length == 0){
							mallary.length = 0;
						}
						if ($.inArray(redata.substring(4 + i, 8 + i), mallary) == -1) {
							var arr = {
								"fid" : redata.substring(4 + i, 8 + i),
								"fstatus" : redata.substring(36 + i, 38 + i)
							}
							mall.push(arr);
							mallary.push(redata.substring(4 + i, 8 + i));
						} else {
							break;
						}
					}
				}
			}
		}
		;
		//关闭事件
		socket.onclose = function(e) {
			if (e.code == 4001 || e.code == 4002 || e.code == 4003 || e.code == 4005 || e.code == 4006) {
				//如果断开原因为4001 , 4002 , 4003 不进行重连.
				return;
			} else {
				return;
			}
			// 重试3次，每次之间间隔5秒
			if (tryTime < 3) {
				setTimeout(function() {
					socket = null;
					tryTime++;
					var _PageHeight = document.documentElement.clientHeight,
						_PageWidth = document.documentElement.clientWidth;
					var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0,
						_LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;
					var _LoadingHtml = '<div id="loadingDiv" style="position:absolute;left:0;width:100%;height:' + _PageHeight + 'px;top:0;background:#f3f8ff;opacity:0.8;filter:alpha(opacity=80);z-index:10000;"><div style="position: absolute; cursor1: wait; left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; width: auto; height: 57px; line-height: 57px; padding-left: 50px; padding-right: 5px; background: #fff url(resources/images/load.gif) no-repeat scroll 5px 10px; border: 2px solid #95B8E7; color: #696969;">""正在尝试第"' + tryTime + '"次重连，请稍候..."</div></div>';
					document.write(_LoadingHtml);
					ws();
				}, 5000);
			} else {
				tryTime = 0;
			}
		};
		//发生了错误事件
		socket.onerror = function() {
//			aler("发生异常，正在尝试重新连接服务器！！！");
		}
	}
}

var personcharts;
function showPersonChart() {
	//初始化echart实例
	personcharts = echarts.init(document.getElementById("person"));
	//显示加载动画效果
	personcharts.showLoading({
		text : '稍等片刻,精彩马上呈现...',
		effect : 'whirling'
	});
	option = {
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b} : {c} ({d}%)"
		},
		tooltip : {
			trigger : 'item',
			formatter : function(param) {
				if (param.name == "其它") {
					return "";
				} else {
					return '焊工在线统计<br/>' + param.name + '：' + param.value;
				}
			}
		},
		legend : {
			orient : 'vertical',
			x : 'right',
			top : 70,
			data : [ '焊工', '在线' ],
			formatter : function(name) {
				var index = 0;
				var clientlabels = [ '焊工', '在线' ];
				var clientcounts = [ namex.length, weld.length ];
				$.each(clientlabels, function(i, value) {
					if (value == name) {
						index = i;
					}
				})
				return name + "：" + clientcounts[index];
			}
		},
		series : [
			{
				name : '焊工在线统计',
				type : 'pie',
				radius : [ '50%', '60%' ],
				center : [ '40%', '50%' ],
				color : [ '#abced2' ],
				data : [
					{
						value : namex.length,
						name : '焊工',
						itemStyle : {
							normal : {
								color : '#abced2'
							}
						}
					}
				].sort(function(a, b) {
					return a.value - b.value;
				}),
				label : {
					normal : {
						show : false,
						position : 'center'
					}
				},
				animationType : 'scale',
				animationEasing : 'elasticOut',
				animationDelay : function(idx) {
					return Math.random() * 200;
				}
			}, {
				name : '焊工在线统计',
				type : 'pie',
				radius : [ '30%', '40%' ],
				center : [ '40%', '50%' ],
				color : [ '#67b73e', '#fff' ],
				data : [
					{
						value : weld.length,
						name : '在线',
						itemStyle : {
							normal : {
								color : '#67b73e'
							}
						}
					},
					{
						value : namex.length - weld.length,
						name : '其它',
						itemStyle : {
							normal : {
								color : '#fff'
							}
						}
					}
				],
				hoverAnimation : false, //鼠标悬停区域不放大
				label : {
					normal : {
						show : false,
						position : 'center'
					}
				},
				itemStyle : {
					normal : {
						label : {
							formatter : function(param) {
								return param.name + "：" + param.value + "%";
							}
						}
					}
				}
			}
		]
	}
	//为echarts对象加载数据
	personcharts.setOption(option);
	//隐藏动画加载效果
	personcharts.hideLoading();
	weld.length = 0;
}

var weldercharts,flagnum = 0;
function showWelderChart() {
	for (var m = 0; m < mall.length; m++) {
		if (mall[m].fstatus == "00") {
			wait.push(mall[m]);
		} else if(mall[m].fstatus == "03" || mall[m].fstatus == "05" || mall[m].fstatus == "07") {
			work.push(mall[m]);
		}  else{
			warn.push(mall[m]);
		}
	}
	if(flagnum == 0){
		flagnum = 1;
		//初始化echart实例
		weldercharts = echarts.init(document.getElementById("machine"));
	}
	//显示加载动画效果
	weldercharts.showLoading({
		text : '稍等片刻,精彩马上呈现...',
		effect : 'whirling'
	});
	option = {
		title : {
			text : machine.length + '台',
			left : '35%',
			top : '45%', //标题显示在pie中间
			textStyle : {
				fontSize : 12,
				align : 'center'
			}
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b} : {c} ({d}%)"
		},
		legend : {
			orient : 'vertical',
			x : 'right',
			top : 50,
			data : [ '工作', '待机', '故障', '关机' ],
			formatter : function(name) {
				var index = 0;
				var clientlabels = [ '工作', '待机', '故障', '关机' ];
				var clientcounts = [ work.length, wait.length, warn.length,  machine.length - work.length - wait.length - warn.length ];
				$.each(clientlabels, function(i, value) {
					if (value == name) {
						index = i;
					}
				})
				return name + "：" + clientcounts[index];
			}
		},
		series : [
			{
				name : '焊机在线统计',
				type : 'pie',
				radius : [ '40%', '60%' ],
				center : [ '40%', '50%' ],
				data : [
					{
						value : work.length,
						name : '工作',
						itemStyle : {
							normal : {
								color : '#66b731'
							}
						}
					},
					{
						value : wait.length,
						name : '待机',
						itemStyle : {
							normal : {
								color : '#2da2f1'
							}
						}
					},
					{
						value : warn.length,
						name : '故障',
						itemStyle : {
							normal : {
								color : '#dc0201'
							}
						}
					},
					{
						value : machine.length - work.length - wait.length - warn.length,
						name : '关机',
						itemStyle : {
							normal : {
								color : '#ebebeb'
							}
						}
					}
				].sort(function(a, b) {
					return a.value - b.value;
				}),
				label : {
					normal : {
						show : false,
						position : 'center'
					}
				},
				labelLine : {
					normal : {
						length : 0,
						show : false
					}
				},
				animationType : 'scale',
				animationEasing : 'elasticOut',
				animationDelay : function(idx) {
					return Math.random() * 200;
				}
			}
		]
	}
	//为echarts对象加载数据
	weldercharts.setOption(option);
	//隐藏动画加载效果
	weldercharts.hideLoading();
	work.length = 0;
	wait.length = 0;
	warn.length = 0;
	mall.length = 0;
}

window.setInterval(function() {
	showWelderChart();
	showPersonChart();
}, 30000);