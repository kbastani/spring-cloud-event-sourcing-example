'use strict';

/* App Module */

var contentApp = angular.module('contentApp', [
    'ngRoute',
    'contentappControllers'
]);

contentApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.when('/products', {
            templateUrl: 'assets/partials/home.html',
            controller: 'ProductListCtrl'
        }).when('/products/:productId', {
            templateUrl: 'assets/partials/product-detail.html',
            controller: 'ProductItemCtrl'
        }).when('/cart', {
            templateUrl: 'assets/partials/cart.html',
            controller: 'CartCtrl'
        }).otherwise({
            redirectTo: '/products'
        });
    }]);



