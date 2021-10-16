function getContextName(moduleName) {
    var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var ctxPath = base.substring(0, base.indexOf(moduleName));
    return ctxPath.substring(ctxPath.lastIndexOf("/"));
};

var gconfig = gconfig || {};
let gconfigModuleName = "/gconfig";
let contextName = getContextName(gconfigModuleName);

window.gconfig = {
	contextName: contextName, 
	moduleName: gconfigModuleName,
	moduleAppPath: contextName + gconfigModuleName + "/app",
	moduleApiPrefix: contextName + gconfigModuleName + "/api",
}

function onAppLoad() {
	$(document).ready(function() {
		$("#msgDash").click(function() {
			$.get(gconfig.moduleAppPath + "/messages/dashboard", function(data) {
				$(".main").html(data);
				alert("Load was performed." + data);
			});
		});
	});
}

function loadPage(url) {
	url = gconfig.moduleAppPath + url;
	$.ajax({
		type : 'GET',
		url : url,
		headers : {
			Accept : "text/plain; charset=utf-8",
			"Content-Type" : "text/plain; charset=utf-8"
		},
		// contentType: 'application/json',
		data : {
			"paramName" : "paramValue"
		},
		success : function(response) {
			$('#content').html(response);
		}
	});
}

function getSystemStatus(url) {
	url = gconfig.moduleAppPath + url;
	$.ajax({
		type : 'GET',
		url : url,
		headers : {
			Accept : "text/plain; charset=utf-8",
			"Content-Type" : "text/plain; charset=utf-8"
		},
		success : function(response) {
			$('#content').html(response);
		}
	});
}

function getSystemProperties(url) {
	url = gconfig.moduleAppPath + url;
	$.ajax({
		type : 'GET',
		url : url,
		headers : {
			Accept : "text/plain; charset=utf-8",
			"Content-Type" : "text/plain; charset=utf-8"
		},
		success : function(response) {
			$('#content').html(response);
		}
	});
}

function loadLogger(url) {
	url = gconfig.moduleAppPath + url;
	$.ajax({
		type : 'GET',
		url : url,
		headers : {
			Accept : "text/plain; charset=utf-8",
			"Content-Type" : "text/plain; charset=utf-8"
		},
		// contentType: 'application/json',
		data : {
			"paramName" : "paramValue"
		},
		success : function(response) {
			$('#content').html(response);
		}
	});
}


function setLogLevel() {
	var levels = $("#levels").val();
	var logger = $("#logger").val();

	if (levels != undefined && levels.length > 0) {

		var url = gconfig.moduleAppPath +'/logger?levels=' + levels + '&logger=' + logger;
		$
				.ajax({
					type : "POST",
					url : url,
					contentType : "text/html; charset=utf-8",
					dataType : "text",
					success : function(data) {
						 $("#levels").val('');
						showTimeoutDialog('Log level changed to <b><font color="green">'
								+ levels + '</font></b>');
						loadLogger(url);
					}
				});
	} else {
		showAlertDialog("Please select any one log level.");
	}

}

function loadLogs() {
	var fileName = $("#selectLogFile").find( "option:selected" ).prop("value");
	if (-1 == fileName) {
		return false;
	}
	var url = gconfig.moduleAppPath +'/logger/logfiles/getLogs';
	$.ajax({
		type : 'GET',
		url : url,
		headers : {
			Accept : "text/plain; charset=utf-8",
			"Content-Type" : "text/plain; charset=utf-8"
		},
		// contentType: 'application/json',
		data : {
			"fileName" : fileName
		},
		success : function(response) {
			$('#messages').html(response);
		}
	});
}

function downloadFile() {
	var fileName = $("#selectLogFile").find( "option:selected" ).prop("value");
	if (-1 == fileName) {
		return false;
	}
	var url = gconfig.moduleAppPath +'/logger/logfiles/downloadLogs?fileName='+fileName;
	$.ajax({
		type : 'GET',
		xhrFields: {
            responseType: 'blob'
        },
        url : url,
		headers : {
			//Accept : "text/plain; charset=utf-8", "Content-Type" : "text/plain; charset=utf-8"
		},
		success : function(data) {
			console.log("File downloaded successfully");
			var a = document.createElement('a');
            var url = window.URL.createObjectURL(data);
            a.href = url;
            a.download = fileName;
            document.body.append(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        }
	});
}

function loadMessages() {
	var selectedLanguage = $("#selectLanguage").find( "option:selected" ).prop("value");
	if (-1 == selectedLanguage) {
		return false;
	}
	var messageType = $("#messageType").val();
	var url = gconfig.moduleAppPath +'/messages/'+messageType+'/'+selectedLanguage;
	$.ajax({
		type : 'GET',
		url : url,
		headers : {
			Accept : "text/plain; charset=utf-8",
			"Content-Type" : "text/plain; charset=utf-8"
		},
		// contentType: 'application/json',
		data : {
			"paramName" : "paramValue"
		},
		success : function(response) {
			$('#messages').html(response);
		}
	});
}

function saveMessage(index, action) {
	var selectedLanguage = $("#selectLanguage").find( "option:selected" ).prop("value");
	var messageType = $("#messageType").val();
	var url = gconfig.moduleAppPath +'/messages/'+messageType+'/'+selectedLanguage;
	var key1 = $("#key"+index).val();
	var value1 = $("#textArea"+index).val();

	saveMessageAndConfig(index, action, url, key1, value1);
}


function saveConfig(index, action) {

	var configName = $("#configName").val();
	var url = gconfig.moduleAppPath +'/config/'+configName;
	var key1 = $("#key"+index).val();
	var value1 = $("#textArea"+index).val();

	saveMessageAndConfig(index, action, url, key1, value1);
}

function saveMessageAndConfig(index, action, url, key1, value1) {

	if (key1.length == 0) {
		$('#confirrmDialog').dialogBox({
			hasClose : true,
			hasBtn : true,
			confirmValue : 'OK',
			content : '<b><font color="green">Key is Empty.</font></b> Please provide valid key.'
		});
		return false;
	}
	$.post(url, {
		key : key1,
		value : value1
	}, function(response, status) {
		var msg = '';
		if (action === 'update') {
			msg = '<b><font color="green">' + key1 + '</font></b>'
					+ ' value has been updated.'
		} else {
			msg = '<b><font color="green">' + key1 + '</font></b>'
					+ ' value has been created.'
		}
		showTimeoutDialog(msg);
		$('#messages').html(response);
	});
}


function showTimeoutDialog(msg) {
	$('#timeoutDialog').dialogBox({
		content : msg,
		hasClose : true,
		autoHide : true,
		effect : 'sign',
		time : 3000
	})
}
function showAlertDialog(msg) {
	$('#alertDialog').dialogBox({
		hasClose : true,
		hasBtn : true,
		confirmValue : 'OK',
		content : msg
	});
}

function getContextPath(){
	var path =window.location.pathname;
	return path.substring(0,path.indexOf('/',1));
}
