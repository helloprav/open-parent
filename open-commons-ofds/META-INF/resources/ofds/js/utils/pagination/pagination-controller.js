
routerApp.controller('PaginationController', function($scope) {

	$scope.pageChangeHandler = function(num) {
		console.log('PaginationController page changed to ' + num);
	};
});
