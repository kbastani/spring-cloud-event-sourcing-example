'use strict';

/* Controllers */

angular.module('SharedServices', [])
    .config(function ($httpProvider) {
        $httpProvider.responseInterceptors.push('myHttpInterceptor');
        var spinnerFunction = function (data, headersGetter) {
            $('#mydiv, .ajax-loader').show();
            return data;
        };
        $httpProvider.defaults.transformRequest.push(spinnerFunction);
    })
    .factory('myHttpInterceptor', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                $('#mydiv, .ajax-loader').hide();
                $('.hidden-content').removeClass('hidden-content');
                return response;
            }, function (response) {
                $('#mydiv, .ajax-loader').hide();
                $('.hidden-content').removeClass('hidden-content');
                return $q.reject(response);
            });
        };
    });

var contentappControllers = angular.module('contentappControllers', ['SharedServices']);

contentApp.directive('carousel', function () {
    var res = {
        restrict: 'A',
        link: function (scope, element, attrs) {
            scope.$watch(attrs.carousel, function (products) {
                if (scope.products.length > 0) {
                    products = scope.products;
                    var html = '';
                    for (var i = 0; i < products.length; i++) {
                        var productTitleLink = '/assets/img/posters/' + products[i].productId + '.png';
                        html += '<div class="item">' +
                            '<div class="thumbnail carousel-products">' +
                            '<a href="index.html#/products/' + products[i].productId + '"><img alt="100%x180" src="' + productTitleLink + '"></a>' +
                            '</div>' +
                            '<span><a href="index.html#/products/' + products[i].productId + '">' + products[i].name + '</a></span>' +
                            '</div>';
                    }

                    element[0].innerHTML = html;

                    setTimeout(function () {
                        $(element).owlCarousel({
                            items: 4,
                            itemsDesktop: [1199, 4],
                            itemsDesktopSmall: [980, 3],
                            itemsTablet: [768, 3],
                            itemsMobile: [479, 2]
                        });

                        $("#owl-example").owlCarousel({
                            items: 3,
                            itemsDesktop: [1199, 3],
                            itemsDesktopSmall: [980, 3],
                            itemsTablet: [768, 2]
                        });
                    }, 0);
                }

            });
        }
    };
    return res;
});

contentApp.factory('orderService', function () {
    var completedOrder = {};

    function set(data) {
        completedOrder = data;
    }

    function get() {
        return completedOrder;
    }

    return {
        set: set,
        get: get
    }
});


contentApp.controller('ProductListCtrl', ['$scope', '$http', '$templateCache',
    function ($scope, $http, $templateCache) {
        $scope.url = '/api/catalog/v1/catalog';
        $scope.products = [];

        var fetchProducts = function () {
            $http({
                method: 'GET',
                url: $scope.url,
                cache: $templateCache
            }).success(function (data) {
                $scope.products = data.products;
            }).error(function (data, status, headers, config) {
            });
        };

        fetchProducts();
    }]);

contentApp.controller('AccountCtrl', ['$scope', '$http', '$templateCache',
    function ($scope, $http) {
        $scope.url = '/api/account/v1/accounts';
        $scope.accounts = {};

        var fetchAccounts = function () {
            $http({
                method: 'GET',
                url: $scope.url
            }).success(function (data) {
                $scope.accounts = data;
            }).error(function (data, status, headers, config) {
            });
        };

        fetchAccounts();
    }]);

contentApp.controller('CartCtrl', ['$scope', '$http', '$templateCache',
    function ($scope, $http) {
        $scope.url = '/api/shoppingcart/v1/cart';
        $scope.cart = {};

        $scope.$on('cartEvents', function (event, msg) {
            fetchCart();
        });

        var fetchCart = function () {
            $http({
                method: 'GET',
                url: $scope.url
            }).success(function (data) {
                $scope.cart = data;
                $scope.cart.total = 0;
                $scope.cart.totalItems = 0;
                for (var i = 0; i < $scope.cart.lineItems.length; i++) {
                    $scope.cart.lineItems[i].posterImage = '/assets/img/posters/' + $scope.cart.lineItems[i].product.productId + '.png';
                    $scope.cart.lineItems[i].originalQuantity = $scope.cart.lineItems[i].quantity;
                    $scope.cart.total += $scope.cart.lineItems[i].quantity * $scope.cart.lineItems[i].product.unitPrice;
                    $scope.cart.totalItems += $scope.cart.lineItems[i].quantity;
                }
            }).error(function (data, status, headers, config) {
            });
        };

        fetchCart();
    }]);

contentApp.controller('HeaderCtrl', ['$scope', '$http',
    function ($scope, $http) {
        $scope.authUrl = '/api/user/uaa/v1/me';
        $scope.meUrl = '/api/user/uaa/v1/me';
        $scope.user = {};

        $scope.logout = function () {
            $http.post('logout', {}).success(function () {
                $rootScope.authenticated = false;
                $scope.user = {};
                $location.path("/");
                $location.reload($location.path);
                $rootScope.$broadcast('logout', "update");
            }).error(function (data) {
                $scope.user = {};
                $rootScope.$broadcast('logout', "update");
            });
        };


        var fetchUser = function () {
            $http({
                method: 'GET',
                url: $scope.authUrl
            }).success(function (data, status, headers, config) {
                $http({
                    method: 'GET',
                    url: $scope.meUrl
                }).success(function (data, status, headers, config) {
                    $scope.user = data;
                }).error(function (data, status, headers, config) {
                });
                $scope.user = data;
            }).error(function (data, status, headers, config) {
                scope.user = {};
            });
        };

        fetchUser();
    }]);

contentApp.directive('carouselrelatedproducts', function () {
    var res = {
        restrict: 'A',
        link: function (scope, element, attrs) {
            scope.$watch(attrs.carouselrelatedproducts, function (product) {
                if (scope.product != undefined ? scope.product.related != undefined ? scope.product.related.length > 0 : false : false) {
                    product = scope.product;
                    var html = '';
                    for (var i = 0; i < product.related.length; i++) {
                        var relatedProductTitleLink = product.related[i].related.poster_image || '/assets/img/posters/' + product.related[i].related.title.replace('/', ' ') + '.jpg';
                        html += '<div class="item">' +
                            '<div class="thumbnail">' +
                            '<a href="index.html#/products/' + product.related[i].related.title.replace('/', '%252F') + '"><img src="' + relatedProductTitleLink + '"/></a>' +
                            '</div>' +
                            '<span><a href="index.html#/products/' + product.related[i].related.title.replace('/', '%252F') + '">' + product.related[i].related.title + '</a></span>' +
                            '</div>';

                    }

                    element[0].innerHTML = html;

                    setTimeout(function () {
                        $(element).owlCarousel({
                            items: 7,
                            itemsDesktop: [1199, 6],
                            itemsDesktopSmall: [980, 5],
                            itemsTablet: [768, 5],
                            itemsMobile: [479, 3]
                        });
                        Holder.run();
                    }, 0);
                }

            });
        }
    };
    return res;
});

contentApp.controller('OrderListCtrl', ['$scope', '$http', '$location', 'orderService', '$routeParams',
    function ($scope, $http, $location, orderService, $routeParams) {
        // Get account
        $scope.accountsUrl = '/api/account/v1/accounts';
        $scope.accounts = {};

        var fetchAccounts = function () {
            $http({
                method: 'GET',
                url: $scope.accountsUrl
            }).success(function (accountData) {
                $scope.accounts = accountData;
                var defaultAccount = {};
                // Get default account
                for (var i = 0; i < $scope.accounts.length; i++) {
                    if ($scope.accounts[i].defaultAccount) {
                        defaultAccount = $scope.accounts[i];
                    }
                }

                $scope.orderListUrl = '/api/order/v1/accounts/' + defaultAccount.accountNumber + "/orders";

                var fetchOrders = function () {
                    $http({
                        method: 'GET',
                        url: $scope.orderListUrl
                    }).success(function (orderData, status, headers, config) {
                        $scope.orders = orderData;
                        // Calculate total
                        for (var j = 0; j < $scope.orders.length; j++) {
                            $scope.orders[j].total = 0.0;
                            for (var i = 0; i < $scope.orders[j].lineItems.length; i++) {
                                $scope.orders[j].total += (($scope.orders[j].lineItems[i].price)
                                    + ($scope.orders[j].lineItems[i].price * $scope.orders[j].lineItems[i].tax))
                                    * $scope.orders[j].lineItems[i].quantity;
                            }
                        }
                    }).error(function (data, status, headers, config) {
                        $location.path('/');
                    });
                };

                fetchOrders();

            }).error(function (data, status, headers, config) {
            });
        };

        fetchAccounts();


    }]);

contentApp.controller('OrderCtrl', ['$scope', '$http', '$location', 'orderService', '$routeParams',
    function ($scope, $http, $location, orderService, $routeParams) {
        $scope.orderItemUrl = '/api/order/v1/orders/' + $routeParams.orderId;
        var fetchOrder = function () {
            $http({
                method: 'GET',
                url: $scope.orderItemUrl
            }).success(function (data, status, headers, config) {
                $scope.order = data;
                // Calculate total
                $scope.order.total = 0.0;
                for (var i = 0; i < $scope.order.lineItems.length; i++) {
                    $scope.order.total += (($scope.order.lineItems[i].price)
                        + ($scope.order.lineItems[i].price * $scope.order.lineItems[i].tax))
                        * $scope.order.lineItems[i].quantity;
                }
            }).error(function (data, status, headers, config) {
                $location.path('/');
            });
        };

        fetchOrder();
    }]);

contentApp.controller('CheckoutCtrl', ['$scope', '$http', '$location', 'orderService', function ($scope, $http, $location, orderService) {
    $scope.checkout = function () {
        var req = {
            method: 'POST',
            url: '/api/shoppingcart/v1/checkout',
            headers: {
                'Content-Type': "application/json"
            },
            data: {}
        };

        $http(req).success(function (data, status, headers, config) {
            if (data.order == null) {
                alert(data.resultMessage);
            } else {
                $scope.order = data.order;
                console.log(data.order);
                orderService.set(data.order);
                $location.path('/orders/' + $scope.order.orderId);
            }
        }).error(function (data, status, headers, config) {
            alert("Checkout could not be completed");
        });
    };
}]);


contentApp.controller('AddToCartCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.qty = 0;
    $scope.productId = "";
    $scope.addToCart = function () {
        if ($scope.qty && $scope.qty > 0) {
            var req = {
                method: 'POST',
                url: '/api/shoppingcart/v1/events',
                headers: {
                    'Content-Type': "application/json"
                },
                data: {
                    "cartEventType": "ADD_ITEM",
                    "productId": $scope.product.productId,
                    "quantity": $scope.qty
                }
            };

            $http(req).then(function () {
                $scope.qty = 0;
                function showAlert() {
                    $("#addProductAlert").addClass("in");
                }

                function hideAlert() {
                    $("#addProductAlert").removeClass("in");
                }

                window.setTimeout(function () {
                    showAlert();
                    window.setTimeout(function () {
                        hideAlert();
                    }, 2000);
                }, 20);
            });
        }
    };
}]);

contentApp.controller('UpdateCartCtrl', ['$rootScope', '$scope', '$http', '$location', function ($rootScope, $scope, $http, $location) {
    $scope.productId = "";

    $scope.updateCart = function () {
        var delta = 0;
        console.log($scope);
        if ($scope.item.quantity >= 0 && $scope.item.originalQuantity > 0 &&
            $scope.item.quantity != $scope.item.originalQuantity) {
            var updateCount = $scope.item.quantity - $scope.item.originalQuantity;
            delta = Math.abs(updateCount);
            if (delta >= 0) {
                var req = {
                    method: 'POST',
                    url: '/api/shoppingcart/v1/events',
                    headers: {
                        'Content-Type': "application/json"
                    },
                    data: {
                        "cartEventType": updateCount <= 0 ? "REMOVE_ITEM" : "ADD_ITEM",
                        "productId": $scope.item.productId,
                        "quantity": delta
                    }
                };

                var selector = "#updateProductAlert." + $scope.item.productId;

                $http(req).then(function () {

                    if (updateCount <= 0) {
                        $rootScope.$broadcast('cartEvents', "update");
                    }

                    $scope.item.originalQuantity = $scope.item.quantity;

                    function showAlert() {
                        $(selector).find("p").text("Cart updated");
                        $(selector).removeClass("alert-error")
                            .addClass("alert-success")
                            .addClass("in");
                    }

                    function hideAlert() {
                        $(selector).removeClass("in");
                    }

                    window.setTimeout(function () {
                        showAlert();
                        window.setTimeout(function () {
                            hideAlert();
                        }, 2000);
                    }, 20);
                });
            }
        } else {
            $rootScope.item.quantity = $scope.item.originalQuantity;
            if ($scope.item.quantity <= 0) {
                $scope.$broadcast('cartEvents', "update");
            }
            window.setTimeout(function () {
                $(selector).find("p").text("Invalid quantity");
                $(selector).removeClass("alert-success")
                    .addClass("alert-error")
                    .addClass("in");
                window.setTimeout(function () {
                    $(selector).removeClass("in");
                }, 2000);
            }, 20);
        }
    };
}]);

contentApp.controller('ProductItemCtrl', ['$scope', '$routeParams', '$http',
    function ($scope, $routeParams, $http) {
        $scope.productItemUrl = '/api/catalog/v1/products/' + $routeParams.productId;
        $scope.productsUrl = '/api/catalog/v1/catalog';
        $scope.products = [];

        $scope.$on('logout', function (event, msg) {
            fetchProduct();
        });

        var fetchProduct = function () {

            $http({
                method: 'GET',
                url: $scope.productsUrl
            }).success(function (data, status, headers, config) {
                $scope.products = data.products;
            }).error(function (data, status, headers, config) {
            });

            $http({
                method: 'GET',
                url: $scope.productItemUrl
            }).success(function (data, status, headers, config) {
                $scope.product = data;
                $scope.product.poster_image = '/assets/img/posters/' + $scope.product.productId + '.png';

            }).error(function (data, status, headers, config) {
            });
        };

        fetchProduct();
    }]);

contentApp.filter('rawHtml', ['$sce', function ($sce) {
    return function (val) {
        return $sce.trustAsHtml(val);
    };
}]);

