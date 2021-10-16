function getContextName() {
    var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var ctxPath = base.substring(0, base.indexOf("/ofds"));
    return ctxPath.substring(ctxPath.lastIndexOf("/"));
};

var ofds = ofds || {};
let contextName = getContextName();
let ofdsModuleName = "/ofds";
window.ofds = {
	contextName: contextName, 
	ofdsModuleName: ofdsModuleName,
	ofdsApiCtx: contextName + ofdsModuleName + "/api",
	configApiCtx: contextName + "/gconfig/api"
}
//var ofdsApiCtx = ofds.contextName + "/api";
var preferredLanguage = 
	{
		locale: "en",
		name: "English"
	}
var routerApp = angular.module('routerApp', ['ui.router','ui.bootstrap', 'angularUtils.directives.dirPagination', 'pascalprecht.translate']);

routerApp.config([ '$locationProvider', function($locationProvider) {
	// The following will remove any ! from URL (changing from #! -> #)
	$locationProvider.hashPrefix('');
} ]);

routerApp.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/login');
    
    $stateProvider
        
        // HOME STATES AND NESTED VIEWS ========================================
        .state('home', {
            url: '/home',
            templateUrl: 'partial-home.html'
        })
        ;

});

// The below code is executed once when angularjs is started
routerApp.run(['$http', '$rootScope', function($http, $rootScope) {

	console.log("routerApp is starting");
	$rootScope.entityStatus =['active', 'inactive'];
	$rootScope.pageSizeOptions =[1, 2, 3, 4, 5, 10, 15, 20];
	$rootScope.defaultPageSize = 5;
	$rootScope.pageSize = 5;
	$rootScope.selectLanguage = preferredLanguage;
	console.log("selectLanguage: "+ JSON.stringify($rootScope.selectLanguage));

	// load the languages supported by application
	loadLanguages($rootScope, $http);

	$rootScope.globalFn = function() {
	   alert('This function is available in all scopes, and views');
	}

}])

// The below code/factory is used to set the header name on item-header.html template
routerApp.factory('Page', function() {
	var title = 'default';
	return {
		title : function() {
			return title;
		},
		setTitle : function(newTitle) {
			title = newTitle
		}
	};
});

// This function is trigger from routerApp.run() method on app startup
function loadLanguages($rootScope, $http) {

	var url = window.ofds.configApiCtx+"/l10n/languages";
	$http({
		method: 'GET',
		url: url
	}).then(function successCallback(response) {

		$rootScope.languages = response.data;
	}, function errorCallback(response) {

	});
}
