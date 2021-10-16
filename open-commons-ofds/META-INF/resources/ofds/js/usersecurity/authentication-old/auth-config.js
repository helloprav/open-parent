routerApp.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
    .state('login', {
        url: "/login",
        templateUrl: "templates/usersecurity/login-form.html",
        controller: 'LoginCtrl',
        controllerAs: 'login'
    })
    .state('register', {
        url: "/register",
        templateUrl: "templates/usersecurity/register-form.html",
        controller: 'RegisterCtrl',
        controllerAs: 'register'
    })
    .state('auth-logout', {
        url: '/auth-logout',
        templateUrl: 'templates/usersecurity/login-form.html',
        controller: 'LogoutCtrl'
    })
});
