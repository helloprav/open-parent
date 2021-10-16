/**
 * 
 */
routerApp.config(function($stateProvider, $urlRouterProvider) {

    //$urlRouterProvider.otherwise('/login');

    $stateProvider

        // HOME STATES AND NESTED VIEWS ========================================
        .state('user-mgmt', {
            url: '/user-mgmt',
            templateUrl: 'templates/user-mgmt/placeHolder.html',
            abstract: true
        })
    
        .state('user-mgmt.users', {
            url: '/users',
            templateUrl: 'templates/user-mgmt/users/index.html',
        })

        // nested list with custom controller
        .state('user-mgmt.users.list', {
            url: '/list/{status}',
            templateUrl: 'templates/user-mgmt/users/users-list.html',
            controller: 'UsersController',
            params: {
            	status: {
            		value: 'active'
            	}
            },
            resolve: {
            	configsPromise: ['userFactory', '$stateParams', function(userFactory, $stateParams) {

					//console.log('Hello: ');
				    var selectedStatus = 'false';
				    //console.log('group-mgmt.groups.list: '+selectedStatus);
				    if($stateParams.status == 'active') {
				    	selectedStatus = 'true';
				    } else if($stateParams.status == 'inactive') {
				    	selectedStatus = 'false';
				    } else if($stateParams.status == 'all') {
				    	selectedStatus = 'all';
				    }
				    //console.log('group-mgmt.groups.list: '+selectedStatus);
				    return userFactory.getAll(selectedStatus, false);
		    	}]
            }
        })

        // nested view item
        .state('user-mgmt.users.view', {
            url: '/{userId}/view',
            templateUrl: 'templates/user-mgmt/users/user-view.html',
            controller: 'UsersController',
            resolve: {
				userPromise: ['userFactory', '$stateParams', function(userFactory, $stateParams) {
				    //console.log('user-mgmt.users.view::: '+$stateParams.userId);
				    return userFactory.get($stateParams.userId);
				}]
            }
        })

        // nested view item
        .state('user-mgmt.users.edit', {
            url: '/{userId}/edit',
            templateUrl: 'templates/user-mgmt/users/edit.html',
            controller: 'UsersController',
            resolve: {
				userPromise: ['userFactory', '$stateParams', function(userFactory, $stateParams) {
				    //console.log('user-mgmt.users.edit::: '+$stateParams.userId);
				    return userFactory.get($stateParams.userId);
				}],
				configsPromise: ['groupFactory', function(groupFactory) {
				    //console.log('groupFactory.getAll ');
				    return groupFactory.getAll();
				}],
		        userGenderPromise: ['userFactory', function(userFactory) {
		        	console.log('user-mgmt.users.edit:::getUserGender ');
		        	return userFactory.getUserGender();
		        }],
		        userStatusPromise: ['userFactory', function(userFactory) {
		        	//console.log('user-mgmt.users.edit:::getUserGender ');
		        	return userFactory.getUserStatus();
		        }]
            }
        })

        // nested list with custom controller
        .state('user-mgmt.users.new', {
            url: '/new',
            templateUrl: 'templates/user-mgmt/users/new.html',
            controller: 'UsersController',
            resolve: {
				resetUserPromise: ['userFactory', function(userFactory) {
					return userFactory.resetAll();
				}],
	        	userGenderPromise: ['userFactory', function(userFactory) {
	        	    console.log('user-mgmt.users.new:::getUserGender ');
	        		return userFactory.getUserGender();
	        	}],
		        userStatusPromise: ['userFactory', function(userFactory) {
		        	//console.log('user-mgmt.users.edit:::getUserGender ');
		        	return userFactory.getUserStatus();
		        }],
				configsPromise: ['groupFactory', function(groupFactory) {
				    //console.log('groupFactory.getAll ');
				    return groupFactory.getAll();
				}],
            }
        })

        // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
});
