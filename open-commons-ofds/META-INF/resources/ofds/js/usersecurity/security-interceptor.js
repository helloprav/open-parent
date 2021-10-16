function securityInterceptor($window) {
	return {
		request : function(config) {
			//console.log("Requesting URL: "+JSON.stringify(config.url));
			return config;
		},
		requestError : function(config) {
			console.log(JSON.stringify(config));
			return config;
		},
		response : function(res) {
			//console.log("Success Response: "+JSON.stringify(res.data));
			return res;
		},
		responseError : function(res, $window) {
			console.log(JSON.stringify(res));
			console.log(":::::::error Response: " + res.status);
			if (res.status == 401) {
				alert("Session expired...Please login.");
				document.location.href = window.ofds.contextName + window.ofds.ofdsModuleName +"/index.html";
			}
			if (res.status == 403) {
				alert("Access Denied...You don't have permission to access.");
				document.location.href = window.ofds.contextName + window.ofds.ofdsModuleName +"/index.html";
			}
			return res;
		}
	}
}

var routerApp = angular.module('routerApp');

routerApp.factory('securityInterceptor', securityInterceptor).config(
	function($httpProvider) {
		$httpProvider.interceptors.push('securityInterceptor');
	}
)
