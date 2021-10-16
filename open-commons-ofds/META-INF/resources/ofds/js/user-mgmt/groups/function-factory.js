angular.module('routerApp')
.factory('functionFactory', [
	'$http', 
	'$state',
	function($http, $state) {
		var o = {
			allFunctionList: [],
			functionn: {}
		};

		o.resetAll = function() {
			console.log("reset All called");
			o.allFunctionList = [];
			o.group = {};
		}

		o.get = function(id) {
			//console.log("get called for id:"+id);
		    var url = window.ofds.ofdsApiCtx+"/groups/"+id;
			return $http.get(url).then(function(res) {
				//console.log(JSON.stringify(res.data.data));
			    if(res.data.data.length ==1) {
			    	angular.copy(res.data.data[0], o.group);
			    }
			})
		};

		o.getAll = function(isValid) {
	    	var url = window.ofds.ofdsApiCtx+"/functions";
	    	if(isValid != undefined && (isValid == true || isValid == false)) {
	    		url = url + "/status/"+isValid;
	    	}
			console.log('getting all functions: '+url);
	    	return $http({
	    		  method: 'GET',
	    		  url: url
	    		}).then(function successCallback(res) {
	    		    // this callback will be called asynchronously
	    		    // when the response is available
	    			//console.log('functions.response:: '+JSON.stringify(res.data));
			    	o.allFunctionList = [];
					angular.copy(res.data.data, o.allFunctionList);
	    		  }, function errorCallback(response) {
	    		    // called asynchronously if an error occurs
	    		    // or server returns response with an error status.
	    		  });
		};

		return o;
	}
]) //end group factory
; 