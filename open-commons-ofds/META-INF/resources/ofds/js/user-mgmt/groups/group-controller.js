(function() {
    'use strict';

    angular.module('routerApp').controller(
	    'GroupsController',
	    function($rootScope, $scope, $state, $http, $window, $location,
		    $stateParams, groupFactory, functionFactory) {

		console.log('within GroupsController');

		// Default Column Name on which sorting will apply
		$scope.columnName = 'modifiedDate';
		$scope.reverse = false;

		$scope.currentPage = 1;
		//$scope.pageSize = $scope.defaultPageSize;

		$scope.entityList = groupFactory.entityList.data;
		$scope.group = groupFactory.group;
		$scope.displayedCollection = [].concat($scope.entityList);
		$scope.functions = functionFactory.allFunctionList;

		// change value for status into boolean (true/false)
		if ($stateParams.status != undefined) {
			$scope.selectedStatus = {
				value : $stateParams.status
			};
		}

		var groupFunctions = $scope.group.functionList;
		//console.log('group: '+ JSON.stringify($scope.group));
		$scope.functionids = [];
		var len = 0;
		if(groupFunctions != undefined) {
		    len = groupFunctions.length;
		}
		if (len > 0 && groupFunctions != undefined) {
		    for (var i = 0; i < len; i++) {
		    	$scope.functionids.push(groupFunctions[i].id);
		    }
		}

		$scope.toggleSelection = function(functionId) {
			//console.log('toggleSelection=Called; id=' + functionId);
			var idx = $scope.functionids.indexOf(functionId);
			if (idx > -1) {
				$scope.functionids.splice(idx, 1);
			} else {
				$scope.functionids.push(functionId);
			}
		};

		$scope.sortBy = function(columnName) {
			//console.log('SortController.columnName ' + columnName);
			//console.log('SortController.$stateParams.status ' + $stateParams.status);
		    if($stateParams.status == 'active') {
		    	$scope.requestedStatus = 'true';
		    } else if($stateParams.status == 'inactive') {
		    	$scope.requestedStatus = 'false';
		    } else if($stateParams.status == 'all') {
		    	$scope.requestedStatus = 'all';
		    }

			groupFactory.getAll($scope.requestedStatus);
			$scope.entityList = groupFactory.entityList.data;
		    $scope.reverse = ($scope.columnName === columnName) ? !$scope.reverse : false;
		    $scope.columnName = columnName;
			$scope.currentPage = 1;
		};

		$scope.createGroup = function() {
		    //console.log('createGroup Called');
		    $scope.updateGroupList();
		    groupFactory.create($scope.group);
		};

		$scope.updateGroup = function() {
		    //console.log('updateGroup Called');
		    $scope.updateGroupList();
		    groupFactory.update($scope.group);
		};

		$scope.updateGroupList = function() {
		    //console.log('updateGroupList Called');
		    $scope.group.functionList = [];
		    var len = $scope.functionids.length;
		    if (len > 0) {
				for (var i = 0; i < len; i++) {
				    var jsonData = {};
				    jsonData["id"] = $scope.functionids[i];
				    $scope.group.functionList.push(jsonData);
				}
		    }
		    //console.log('Group JSON: '+JSON.stringify($scope.group));
		}
    })
})();
