angular.module('routerApp')
.factory('userFactory', [
	'$http', 
	'$state',
	function($http, $state) {
		var o = {
			configs: [],
			entityList: [],
			user: {},
			alerts: [],
			userRoles: [],
			userGenders: [],
			userStatuses: [],
			// this instance o is used for pagination; initialized with default value
			pageData: {
				// pagination properties, part of REST API Request
				pageIndex: 1,   						// Current page number. First page is 1.-->
				pageSizeSelected: 5, 					// Maximum number of items per page.

				// pagination properties, part of REST API Response
				totalCount: 0,  						// Total number of items in all pages. initialize as a zero
				size : 0,								// count of items returned for current pageIndex

			    // sorting properties
			    sortColSelected: 'id', 					// Default sorting applied on column.
			    sortOrder: 'asc', 						// Default sorting order

			    // filters
			    status: 'true',

			    // properties used only for ui/bootstrap/pagination javascript
		    	maxSize: 5,     						// number of pages in block
			}
		};

		o.resetPage = function() {
			console.log("reset Page called");
			o.pageData = {
				pageIndex: 1,   						// Current page number. First page is 1.-->
				pageSizeSelected: 5, 					// Maximum number of items per page.
	
				// pagination properties, part of REST API Response
				totalCount: 0,  						// Total number of items in all pages. initialize as a zero
				size : 0,								// count of items returned for current pageIndex
	
			    // sorting properties
			    sortColSelected: 'id', 					// Default sorting applied on column.
			    sortOrder: 'asc', 						// Default sorting order
	
			    // filters
			    status: 'true',
	
			    // properties used only for ui/bootstrap/pagination javascript
		    	maxSize: 5,     						// number of pages in block
			};
		}

		o.resetAll = function() {
			console.log("reset All called");
			o.configs = [];
			o.entityList = [];
			o.user = {};
			o.alerts = [];
			o.userRoles = [];
			o.userGenders = [];
			o.userStatuses = [];
		}

		o.getUserGender = function() {
			console.log("getUserGender called");
			initGenders();
		}

		o.getUserStatus = function() {
			console.log("getUserStatus called");
			initStatuses();
		}

		function initGenders() {
			console.log("initGenders called");
			o.userGenders = [];
	    	var url = window.ofds.ofdsApiCtx+"/validvalues/genders";
	    	$http({
	    		  method: 'GET',
	    		  url: url
	    		}).then(function successCallback(response) {
	    		    // this callback will be called asynchronously
	    		    // when the response is available
					angular.copy(response.data, o.userGenders);
					console.log("initGenders called: "+JSON.stringify(o.userGenders));
	    		  }, function errorCallback(response) {
	    		    // called asynchronously if an error occurs
	    		    // or server returns response with an error status.
	    		  });
			/*return $http.get(url).success(function(res) {
				angular.copy(res, o.userGenders);
				console.log("initGenders called: "+JSON.stringify(o.userGenders));
			})*/
		}

		function initStatuses() {
			console.log("initStatuses called");
			o.userStatuses = [];
	    	var url = window.ofds.ofdsApiCtx+"/validvalues/userstatuses";
	    	$http({
	    		  method: 'GET',
	    		  url: url
	    		}).then(function successCallback(response) {
	    		    // this callback will be called asynchronously
	    		    // when the response is available
					angular.copy(response.data, o.userStatuses);
					console.log("initStatuses called: "+JSON.stringify(o.userStatuses));
	    		  }, function errorCallback(response) {
	    		    // called asynchronously if an error occurs
	    		    // or server returns response with an error status.
	    		  });
		}

		o.get = function(id) {
			console.log("get called for id:"+id);
		    	var url = window.ofds.ofdsApiCtx+"/users/"+id;
		    	$http({
	    		  method: 'GET',
	    		  url: url
	    		}).then(function successCallback(res) {
	    		    // this callback will be called asynchronously
	    		    // when the response is available
	    			console.log("user retrieved for id:"+JSON.stringify(res.data.data));
					angular.copy(res.data.data, o.user);
					o.user.myrole=o.user.role;
    		    }, function errorCallback(response) {
	    		    // called asynchronously if an error occurs
	    		    // or server returns response with an error status.
    		    });
		    	/*return $http.get(url).success(function(res) {
				angular.copy(res.data, o.user);
				o.user.myrole=o.user.role;
				console.log("user retrieved for id:"+JSON.stringify(res));
			})*/
		};

		o.getAllData = function(status) {
			console.log('getting all users: pageData: '+JSON.stringify(o.pageData));

			if(undefined == o.pageData.status) {
				o.pageData.status = 'true';
			}
	    	var url = window.ofds.ofdsApiCtx+"/users";
	    	if( 'all' != o.pageData.status) {
	    		url = url + "/status/"+o.pageData.status;
	    	}
	    	url = url + "?page=" + o.pageData.pageIndex + "&limit=" + o.pageData.pageSizeSelected;
	    	url = url + "&sort=" + o.pageData.sortColSelected;
	    	if(o.pageData.sortOrder == false) {
	    		o.pageData.sortOrder = 'asc'
	    	}
	    	if(o.pageData.sortOrder == true) {
	    		o.pageData.sortOrder = 'desc'
	    	}
	    	url = url + "&sortOrder=" + o.pageData.sortOrder;
			console.log('getting all groups for url: '+url);
	    	return $http({
	    		  method: 'GET',
	    		  url: url
	    		}).then(function successCallback(response) {
	    		    // this callback will be called asynchronously
	    		    // when the response is available
	    			// console.log('getting all users for data: '+JSON.stringify(response.data.data));
					o.entityList = [];
					angular.copy(response.data, o.entityList);
					angular.forEach(o.entityList.data, function(item, index) {
						//console.log(item, index);
						if(!item.modifiedDate) {
							item.modifiedDate = item.createdDate;
						}
					});
					return o.entityList;
					//console.log("getAll: "+JSON.stringify(response.data));
					//console.log("getAll: "+JSON.stringify(o.entityList.data));
	    		  }, function errorCallback(response) {
	    		    // called asynchronously if an error occurs
	    		    // or server returns response with an error status.
	    		  });
		};

		o.getAll = function(status, isControllerRequest) {
			console.log('getting all users: pageData: '+JSON.stringify(o.pageData));

			if(undefined == status) {
				o.pageData.status = 'true';
			}
	    	var url = window.ofds.ofdsApiCtx+"/users";

	    	if(!isControllerRequest) {
	    		o.resetPage();
	    		o.pageData.status = status;
	    	}
	    	if( 'all' != status) {
	    		url = url + "/status/" + o.pageData.status;
	    	}

			url = url + "?page=" + o.pageData.pageIndex + "&limit=" + o.pageData.pageSizeSelected;
	    	url = url + "&sort=" + o.pageData.sortColSelected;
	    	if(o.pageData.sortOrder == false) {
	    		o.pageData.sortOrder = 'asc'
	    	}
	    	if(o.pageData.sortOrder == true) {
	    		o.pageData.sortOrder = 'desc'
	    	}
	    	url = url + "&sortOrder=" + o.pageData.sortOrder;
			console.log('getting all groups for url: '+url);
	    	return $http({
	    		  method: 'GET',
	    		  url: url
	    		}).then(function successCallback(response) {
	    		    // this callback will be called asynchronously
	    		    // when the response is available
	    			// console.log('getting all users for data: '+JSON.stringify(response.data.data));
					o.entityList = [];
					angular.copy(response.data, o.entityList);
					angular.forEach(o.entityList.data, function(item, index) {
						//console.log(item, index);
						if(!item.modifiedDate) {
							item.modifiedDate = item.createdDate;
						}
					});
					if(isControllerRequest) {
						return o.entityList;
					}
					//console.log("getAll: "+JSON.stringify(response.data));
					//console.log("getAll: "+JSON.stringify(o.entityList.data));
	    		  }, function errorCallback(response) {
	    		    // called asynchronously if an error occurs
	    		    // or server returns response with an error status.
	    		  });
		};

		o.create = function(user) {
			console.log("Creating User "+JSON.stringify(user));
			user.version = new Date().getTime();		
			if(user.skipFirstLine == undefined) {
				user.skipFirstLine = false;
			}
			if(user.active == undefined) {
				user.active = false;
			}
			
			return $http({
				  method: 'POST',
				  url: window.ofds.ofdsApiCtx+'/users',
				  data: user
				}).then(function successCallback(response) {
				    // this callback will be called asynchronously
				    // when the response is available
					if(response.status == 201) {
						o.setAlert("success", "Successfully created config!");
						$state.go('user-mgmt.users.list');				
					} else if(response.status == 400) {
						console.log("Error creating! status = "+response.status+", data = "+JSON.stringify(response.data));
						o.setAlert("danger", "Error creating user!" + JSON.stringify(response.data));
					} else {
						console.log("Error in request status[ "+response.status+"], data = "+JSON.stringify(response.data));
					}
				  }, function errorCallback(response) {
				    // called asynchronously if an error occurs
				    // or server returns response with an error status.
					  console.log("Error creating! status = "+status+", data = "+JSON.stringify(data));
					  o.setAlert("danger", "Error creating user!");
				  });
		}

		o.update = function(user) {
			console.log("Updating User: "+JSON.stringify(user));
			return $http({
			  method: 'PUT',
			  url: window.ofds.ofdsApiCtx+'/users/'+user.id,
			  data: user
			}).then(function successCallback(response) {
			    // this callback will be called asynchronously
			    // when the response is available
				if(response.status == 200) {
					o.setAlert("success", "Successfully created config!");
					$state.go('user-mgmt.users.list');				
				} else if(response.status == 400) {
					console.log("Error creating! status = "+response.status+", data = "+JSON.stringify(response.data));
					o.setAlert("danger", "Error creating user!" + JSON.stringify(response.data));
				} else {
					console.log("Error in request status[ "+response.status+"], data = "+JSON.stringify(response.data));
				}
			  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			  });
		}
		
		o.delete = function(id) {
			var confirmed = window.confirm("Are you sure you want to delete?");
			console.log("Deleting config "+id+" confirmed = "+confirmed);
			
			if(confirmed) {
				$http.delete('/config/'+id).success(function(data) {
					console.log("deleted");
					o.setAlert("success", "Successfully deleted config!");

				})
				.error(function(data, status) {
					console.log("Error deleting! status = "+status+", data = "+data);
					o.setAlert("danger", "Error deleting config!");

				});
			}
			$state.go('user-mgmt.list');
			$state.reload();
		}
		
		o.setAlert = function(type, msg) {
			console.log("Setting alert type %s, msg %s", type, msg);
			//reset the alert
			o.alerts=[];
			o.alerts.push({type: type, msg: msg});
		}
		return o;
	}
]) //end config factory
;
