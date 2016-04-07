'use strict';

/* App Module */

var contentApp = angular.module('contentApp', [
    'ngRoute',
    'contentappControllers'
]);

contentApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.when('/', {
            templateUrl: 'assets/partials/home.html',
            controller: 'ProductListCtrl'
        }).when('/products/:productId', {
            templateUrl: 'assets/partials/product-detail.html',
            controller: 'ProductItemCtrl'
        }).when('/cart', {
            templateUrl: 'assets/partials/cart.html',
            controller: 'CartCtrl'
        }).when('/settings', {
            templateUrl: 'assets/partials/account.html',
            controller: 'AccountCtrl'
        }).when('/orders', {
            templateUrl: 'assets/partials/orders.html',
            controller: 'OrderListCtrl'
        }).when('/orders/:orderId', {
            templateUrl: 'assets/partials/order.html',
            controller: 'OrderCtrl'
        }).otherwise({
            redirectTo: '/'
        });
    }]);



