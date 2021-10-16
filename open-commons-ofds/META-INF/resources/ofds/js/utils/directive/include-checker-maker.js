
// The below code/factory is used to set the header name on item-header.html template
routerApp.directive('checker', function() {
	return {

		replace : 		false,
		transclude :	true,
		templateUrl: 	'templates/common/checker-maker.html',
	};
});
