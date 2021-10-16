//var routerApp = angular.module('routerApp');
routerApp.config([ '$translateProvider', function($translateProvider) {

	$translateProvider.useUrlLoader(window.ofds.configApiCtx + '/l10n/messages/dashboard');
	$translateProvider.preferredLanguage(preferredLanguage.locale);
} ]);