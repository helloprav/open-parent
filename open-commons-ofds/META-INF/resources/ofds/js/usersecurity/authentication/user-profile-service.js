// move auth-controller's $http code to this service layer
angular.module('routerApp')
.factory('userProfile', [function() {
		var o = {
			user: {},
			userFunctions: []
		};

		o.saveUserProfile = function(userDetails) {
			console.log("saveUserProfile called");
			o.user = userDetails;
		}

		return o;
	}
]) //end userProfile
;
