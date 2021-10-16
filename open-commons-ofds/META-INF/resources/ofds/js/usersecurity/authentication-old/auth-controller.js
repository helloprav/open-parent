(function () {
    'use strict';

    angular.module('routerApp').controller('LogoutCtrl', function ($rootScope, $scope, $state, $http, $window, $location) {

    	console.log("logout requested");
    	authService.logout();
    });

    angular.module('routerApp').controller('LoginCtrl', function ($rootScope, $scope, $state, $http, $window, $location) {

        $scope.loginUser = function () {

            if ($scope.login.username == ''
                || $scope.login.password == '') {

            	$scope.error = "Please enter username and password";
                return;
            }

            var loginJson = {};
            loginJson['username'] = $scope.login.username;
            loginJson['password'] = $scope.login.password;

            console.log(JSON.stringify(loginJson));
            $scope.loginDetail = loginJson;
        	authService.login($scope.loginDetail).then(function(authServiceResponse) {
				console.log("authServiceResponse called: "+JSON.stringify(authServiceResponse));
        		$scope.statusCode = authServiceResponse.statusCode;
        		console.log("authService.statusCode: "+authService.statusCode);
	        	alert("StatusCode: "+authServiceResponse.statusCode);
	        	if(authServiceResponse.statusCode == 201) {
	                $state.go('home');
	        	} else {
	        		$scope.errorList = authServiceResponse.errorList;
	        	}
        	});
        };
    })
})();