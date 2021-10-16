angular.module('routerApp')
.factory('groupFactory', [
	'$http', 
	'$state',
	function($http, $state) {
		var o = {
			entityList: [],
			group: {}
		};

		o.resetAll = function() {
			console.log("reset All called");
			o.entityList = [];
			o.group = {};
		}

		o.get = function(id) {
			//console.log("get called for id:"+id);
		    var url = window.ofds.ofdsApiCtx+"/groups/"+id;
		    return $http({
		    	  method: 'GET',
		    	  url: url
		    	}).then(function successCallback(res) {
		    	    // this callback will be called asynchronously
		    	    // when the response is available
    				//console.log("Group Details: "+JSON.stringify(res));
    				angular.copy(res.data.data, o.group);
		    	  }, function errorCallback(response) {
		    	    // called asynchronously if an error occurs
		    	    // or server returns response with an error status.
		    	  });
		};

		o.getAll = function(status) {
			console.log('getting all groups: status'+status);

			if(undefined == status) {
				status = 'true';
			}
	    	var url = window.ofds.ofdsApiCtx+"/groups";
	    	if( 'all' != status) {
	    		url = url + "/status/"+status;
	    	}
			//console.log('getting all groups for status: '+status);
		    return $http({
	    		  method: 'GET',
	    		  url: url
	    		}).then(function successCallback(response) {
		    	    // this callback will be called asynchronously
		    	    // when the response is available
	    			// console.log('getting all users for data: '+JSON.stringify(response.data.data));
					o.entityList = [];
					angular.copy(response.data, o.entityList);
					//console.log("getAll: "+JSON.stringify(response.data));
					//console.log("getAll: "+JSON.stringify(o.entityList.data));
		    	  }, function errorCallback(response) {
		    	    // called asynchronously if an error occurs
		    	    // or server returns response with an error status.
		    	  });
		};

		o.create = function(group) {
			console.log("Creating Group: "+JSON.stringify(group));
			group.version = new Date().getTime();

			return $http.post(window.ofds.ofdsApiCtx+'/groups', group).then(function successCallback(response) {
			    // this callback will be called asynchronously
			    // when the response is available
				o.setAlert("success", "Successfully created group!");
				$state.go('group-mgmt.groups.list');
			  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
				console.log("Error creating! status = "+status+", data = "+data);
				o.setAlert("danger", "Error creating group!");
			  });
		}

		o.update = function(group) {
			console.log("Updating Group: "+JSON.stringify(group));
			return $http.put(window.ofds.ofdsApiCtx+'/groups/'+group.id, group).then(function successCallback(response) {
				    // this callback will be called asynchronously
				    // when the response is available
					o.setAlert("success", "Successfully updated Group!");
					$state.go('group-mgmt.groups.list');
				  }, function errorCallback(response) {
				    // called asynchronously if an error occurs
				    // or server returns response with an error status.
					console.log("Error updating! status = "+status+", data = "+data);
					o.setAlert("danger", "Error updating Group!");
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
			$state.go('group-mgmt.groups.list');
			$state.reload();
		}
		
		o.setAlert = function(type, msg) {
			//console.log("Setting alert type %s, msg %s", type, msg);
			//reset the alert
			o.alerts=[];
			o.alerts.push({type: type, msg: msg});
		}
		return o;
	}
]) //end group factory
; 