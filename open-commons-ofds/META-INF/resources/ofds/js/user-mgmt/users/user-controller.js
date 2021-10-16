(function() {
    'use strict';

    angular.module('routerApp').controller(
	    'UsersController',
	    function($rootScope, $scope, $state, $http, $window, $location, $stateParams,
		    userFactory, groupFactory) {

		console.log('Executing UsersController.....');

		// ############# Start: Server Side Pagination ######################################################
		// pagination properties, part of REST API Request
		$scope.pageIndex = 1;					// Current page number. First page is 1.-->
		$scope.pageSizeSelected = 5;			// Maximum number of items per page for this controller

		// pagination properties, part of REST API Response
		$scope.totalCount = 0;					// Total number of items in all pages. initialize as a zero
		$scope.size = 0;						// count of items returned for current pageIndex

	    // sorting properties
		$scope.columnName = 'id';				// Default sorting applied on column.
		$scope.reverse = false;					// Default sorting order

	    // filters
		$scope.isValid = 'true';
		if(undefined == $scope.requestedStatus) {
			$scope.requestedStatus = 'true';
		}

	    // properties used only for ui/bootstrap/pagination javascript
		$scope.maxSize = 5;						// number of pages in block

		if(userFactory.entityList.pagination) {
		    $scope.totalCount = userFactory.entityList.pagination.total;
		    $scope.pageIndex = userFactory.entityList.pagination.page;
		    $scope.pageSizeSelected = userFactory.entityList.pagination.limit;
		    $scope.size = userFactory.entityList.pagination.size;
		}

		$scope.entityList = userFactory.entityList.data;
		$scope.configs = userFactory.configs;
		$scope.entity = userFactory.user;
		$scope.groups = groupFactory.entityList.data;
		$scope.groupids = [];

		// assign default values 
		if(!$scope.entity.status) {
			$scope.entity.status = 'active';
		}
		if(!$scope.entity.isSuperAdmin) {
			$scope.entity.isSuperAdmin = false;
		}

		$scope.alerts = userFactory.alerts;
		$scope.userRoles = userFactory.userRoles;
		$scope.userGenders = userFactory.userGenders;
		$scope.userStatuses = userFactory.userStatuses;
		$scope.displayedCollection = [].concat($scope.configs);

		if($scope.entity.otherData != undefined) {
			var userGroups = $scope.entity.otherData.groupList;
			$scope.groupids = [];
			var len = 0;
			if(userGroups != undefined) {
			    len = userGroups.length;
			}
			if (len > 0) {
			    for (var i = 0; i < len; i++) {
			    	if(userGroups != undefined && userGroups[i] != undefined) {
				    	if(userGroups[i].id != undefined) {
				    		$scope.groupids.push(userGroups[i].id);
				    	} else {
				    		$scope.groupids.push(userGroups[i]);
				    	}
			    	}
			    }
			}
		}

		$scope.toggleSelection = function(groupId) {
			//console.log('toggleSelection=Called; id=' + functionId);
			var idx = $scope.groupids.indexOf(groupId);
			if (idx > -1) {
				$scope.groupids.splice(idx, 1);
			} else {
				$scope.groupids.push(groupId);
			}
		};

		$scope.getBoolean = function() {
			return "" + $scope.entity.isSuperAdmin;
		}; 

		// change value for status into boolean (true/false)
		if ($stateParams.status != undefined) {
			$scope.selectedStatus = {
				value : $stateParams.status
			};
		}

		$scope.createUser = function() {
		    $scope.updateGroupList();
		    userFactory.create($scope.entity);
		};

		$scope.updateUser = function() {
		    //console.log('updateUser Called');
		    $scope.updateGroupList();
		    userFactory.update($scope.entity);
		};

		$scope.updateGroupList = function() {
		    //console.log('updateGroupList Called');
			if($scope.entity.otherData == undefined) {
				$scope.entity.otherData = {};
			}
		    $scope.entity.otherData.groupList = [];
		    var len = $scope.groupids.length;
		    if (len > 0) {
				for (var i = 0; i < len; i++) {
				    if($scope.groupids[i] != null) {
				    	$scope.entity.otherData.groupList.push($scope.groupids[i]);
				    }
				}
		    }
		    //console.log('Group JSON: '+JSON.stringify($scope.group));
		};

		$scope.getEmployeeList = function () {
	    	userFactory.pageData.pageIndex = $scope.pageIndex
	    	userFactory.pageData.pageSizeSelected = $scope.pageSizeSelected
	    	userFactory.pageData.sortColSelected = $scope.columnName
	    	userFactory.pageData.sortOrder = $scope.reverse
			userFactory.getAll($scope.requestedStatus, true).then(function(trips) {
				//https://stackoverflow.com/questions/30369483/angularjs-controller-doesnt-get-data-from-service-when-using-http
				$scope.entityList = userFactory.entityList.data;
				$scope.totalCount = userFactory.entityList.pagination.total;
			    $scope.size = userFactory.entityList.pagination.size;
	        })
	    	console.log("totalCount: "+$scope.totalCount)
	    	console.log("entityList: "+$scope.entityList)
	    }

	    //This method is calling from pagination number
	    $scope.pageChanged = function () {
	    	printScopeVar($scope);
	        $scope.getEmployeeList();
	    };
	    //This method is calling from dropDown
	    $scope.changePageSize = function () {
	        $scope.pageIndex = 1;
	        $scope.getEmployeeList();
	    };

		$scope.sortBy = function(columnName) {

		    if($stateParams.status == 'active') {
		    	$scope.requestedStatus = 'true';
		    } else if($stateParams.status == 'inactive') {
		    	$scope.requestedStatus = 'false';
		    } else if($stateParams.status == 'all') {
		    	$scope.requestedStatus = 'all';
		    }

			//userFactory.getAll($scope.requestedStatus);
			$scope.entityList = userFactory.entityList.data;
		    $scope.reverse = ($scope.columnName === columnName) ? !$scope.reverse : false;
		    $scope.columnName = columnName;
			$scope.pageIndex = 1;
			$scope.getEmployeeList();
		};
    })
})();

function printScopeVar($scope) {
	console.log('currentPage:'+$scope.currentPage)
	console.log('pageIndex:'+$scope.pageIndex)
}