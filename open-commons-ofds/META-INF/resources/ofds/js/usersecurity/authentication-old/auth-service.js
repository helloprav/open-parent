// move auth-controller's $http code to this service layer
angular.module('routerApp')
.factory('authService', [
	'$http', 
	'$state', '$window', '$q',
	function($http, $state, $window, $q) {
		var o = {
			errorList: [],
			message: {},
			statusCode: {},
			user: {}
		};

		o.resetAll = function() {
			console.log("reset All called");
			o.errorList = [];
			o.message = {};
			o.statusCode = [];
			o.user = [];
		}

		o.login = function(loginDetail) {

			this.resetAll();
			response=[];
			var d = $q.defer();
            $http({
                url : window.ofds.ofdsApiCtx+"/auth/login",
                method : "POST",
                data : loginDetail
			}).then(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
                console.log("response: "+response.data);
                console.log(JSON.stringify(response.data));
                o.statusCode = response.status;
                console.log("statusCode: "+o.statusCode);
                if (o.statusCode == 201) {
                    $window.localStorage.setItem("saved", response.data);
                    $window.localStorage.setItem("loginuserid", response.data.userID);
                    $window.localStorage.setItem("username-deleteMe", response.config.data.username);
                    if($window.localStorage.userName!=""||$window.localStorage.password!="") {
                        $window.localStorage.userName = "";
                        $window.localStorage.password = "";
                        console.log("password reset completed");
                    }
                } else if(o.statusCode == 400) {
                	console.log("response.data.errorList: "+ response.data.errorList);
                    o.errorList = response.data.errorList;
                } else {
                	o.message = "This will never be displayed";
                }
                d.resolve(o);
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error
				// status.
                console.log("data: "+data);
                console.log("data.statusCode: "+data.statusCode);
                console.log("status: "+status);
                console.log("headers: "+headers);
                console.log("config: "+config);

                console.log("Within failure error ::::::::::::"+data.responseBody);
                if(undefined == data.errorList) {
                	
                    o.errorList = response.data.errorList;
                } else {
                	
                    o.errorList = response.data.errorList;
                }
			});
            d.resolve(o);
            return d.promise;
		}

		o.logout = function() {
	        $http({
	            url : window.ofds.ofdsApiCtx+"/auth/logout",
	            method : "DELETE"
			}).then(function successCallback(response) {

				if (response.status == 200) {
	            	console.log("logout successfull");
	            	o.message = "User logged out successfully"
	            } else {
	            	console.log("logout failed");
	            }
	            $state.go('login');
			}, function errorCallback(response) {

	        	console.log("logout error");
	            $state.go('login');
			});
		}

		return o;
	}]
);
