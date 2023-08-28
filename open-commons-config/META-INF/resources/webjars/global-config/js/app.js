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
	moduleAppPath: contextName + gconfigModuleName,
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

function loadMessagePage(url) {
	loadPage('/languages?messageType=' + url);
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

	if ( (levels != undefined && levels.length > 0) && (logger != -1 && logger.length > 0) )  {

		var url = gconfig.moduleApiPrefix +'/logger?levels=' + levels + '&logger=' + logger;
		$
				.ajax({
					type : "POST",
					url : url,
					dataType : "text",
					success : function() {
						showTimeoutDialog('Log level changed to <b><font color="green">'
								+ levels + '</font></b>');
						setTimeout(function(){
							location.reload();
						}, 1500);
					}
				});
	} else {
		showAlertDialog("Please select any one log level and logger.");
	}

}

function loadLogs() {
	var fileName = $("#selectLogFile").find( "option:selected" ).prop("value");
	if (-1 == fileName) {
		return false;
	}
	var url = gconfig.moduleAppPath +'/logfiles/getLogs';
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
			$('#contentBody').html(response);
		}
	});
}

function downloadFile() {
	var fileName = $("#selectLogFile").find( "option:selected" ).prop("value");
	if (-1 == fileName) {
		return false;
	}
	var url = gconfig.moduleAppPath +'/logfiles/downloadLogs?fileName='+fileName;
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
	var url = gconfig.moduleApiPrefix +'/l10n/messages/'+messageType+'?language='+selectedLanguage;
	$.ajax({
		type : 'GET',
		url : url,
		headers : {
			//Accept : "text/plain; charset=utf-8",
			"Content-Type" : "application/json"
		},
		// contentType: 'application/json',
		data : {
			"paramName" : "paramValue"
		},
		success : function(response) {

			updateMessageTable(response);
		}
	});
}

function updateMessageTable(response) {
    $('#messages').hide();
    $('#messages').show();
    tableBody = $("#messages table tbody");
    tableBody.empty();
	let	markup = "<tr id='new'><td>NEW</td><td><input id='key0' type='text' value='' /></td>"+
            "<td><textarea id='textArea0' cols='70'></textarea></td>"+
            "<td><input type='button' onclick='saveMessage(0, \"create\")' value='CREATE'></td></tr>";
    tableBody.append(markup);
    let idx = 1;
	const sortedResponse = Object.keys(response).sort().reduce((accumulator, key) => { accumulator[key] = response[key]; return accumulator; }, {});
	$.each(sortedResponse, function(key, value) {
		markup = 
				"<tr><td>"+idx+"</td><td>" + 
				"<input id='key"+idx+"' type='text' value='"+key+"' readonly='readonly'/></td>"+
				"<td><textarea id='textArea"+idx+"' cols='70'>"+value+"</textarea></td>"+
				"<td><input type='button' onclick='saveMessage("+idx+", \"update\")' value='SAVE'></td></tr>";
        tableBody.append(markup);
        ++idx;
	});
}

function updateConfigTable(response) {
    $('#messages').hide();
    $('#messages').show();
    tableBody = $("#messages table tbody");
    tableBody.empty();
	let	markup = "<tr id='new'><td>NEW</td><td><input id='key0' type='text' value='' /></td>"+
            "<td><textarea id='textArea0' cols='70'></textarea></td>"+
            "<td><input type=\"button\" onclick=\"saveConfig(0, 'create')\" value=\"CREATE\"></td></tr>";
    console.log(markup);
    tableBody.append(markup);
    let idx = 1;
	const sortedResponse = Object.keys(response).sort().reduce((accumulator, key) => { accumulator[key] = response[key]; return accumulator; }, {});
	$.each(sortedResponse, function(key, value) {
		markup = 
				"<tr><td>"+idx+"</td><td>" + 
				"<input id='key"+idx+"' type='text' value='"+key+"' readonly='readonly'/></td>"+
				"<td><textarea id='textArea"+idx+"' cols='70'>"+value+"</textarea></td>"+
				"<td><input type='button' onclick='saveConfig("+idx+", \"update\")' value='SAVE'></td></tr>";
        tableBody.append(markup);
        ++idx;
	});
}

function saveMessage(index, action) {
	var selectedLanguage = $("#selectLanguage").find( "option:selected" ).prop("value");
	var messageType = $("#messageType").val();
	var url = gconfig.moduleApiPrefix +'/l10n/messages/'+messageType+'/'+selectedLanguage;
	var key1 = $("#key"+index).val();
	var value1 = $("#textArea"+index).val();

	saveMessageAndConfig(action, url, key1, value1, 'msg');
}


function saveConfig(index, action) {

	var configName = $("#configName").val();
	var url = gconfig.moduleApiPrefix +'/l10n/config/'+configName;
	var key1 = $("#key"+index).val();
	var value1 = $("#textArea"+index).val();

	saveMessageAndConfig(action, url, key1, value1, 'config');
}

function saveMessageAndConfig(action, url, key1, value1, type) {

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
		if(type === 'msg') {
			updateMessageTable(response);
		} else {
			updateConfigTable(response);
		}
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
