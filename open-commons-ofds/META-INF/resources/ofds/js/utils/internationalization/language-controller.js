(function() {
    'use strict';

	angular.module('routerApp').controller('LanguageCtrl', ['$rootScope', '$scope','$translate',
		function($rootScope, $scope, $translate) {
	
			//$rootScope.selectLanguage = {};
	
			$scope.changeLanguage = function (lang) {
		        $translate.use(lang);

			    angular.forEach($rootScope.languages,function(language,index){
	                if(lang == language.locale) {
	                	$rootScope.selectLanguage = language;
	                }
	            })
		    }
		}
	])// end Language controller
})();
