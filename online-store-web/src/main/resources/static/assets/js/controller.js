'use strict';

/* Controllers */

angular.module('SharedServices', [])
    .config(function ($httpProvider) {
        $httpProvider.responseInterceptors.push('myHttpInterceptor');
        var spinnerFunction = function (data, headersGetter) {
            // todo start the spinner here
            //alert('start spinner');
            $('#mydiv').show();
            return data;
        };
        $httpProvider.defaults.transformRequest.push(spinnerFunction);
    })
    // register the interceptor as a service, intercepts ALL angular ajax http calls
    .factory('myHttpInterceptor', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                // do something on success
                // todo hide the spinner
                //alert('stop spinner');
                $('#mydiv').hide();
                $('.hidden-content').removeClass('hidden-content');
                return response;

            }, function (response) {
                // do something on error
                // todo hide the spinner
                //alert('stop spinner');
                $('#mydiv').hide();
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
                            '<a href="index.html#/products/' + products[i].id + '"><img alt="100%x180" src="' + productTitleLink + '"></a>' +
                            '</div>' +
                            '<span><a href="index.html#/products/' + products[i].id + '">' + products[i].name + '</a></span>' +
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

contentApp.controller('ProductListCtrl', ['$scope', '$http', '$templateCache',
    function ($scope, $http, $templateCache) {
        $scope.url = '/api/catalog/v1/catalog';
        $scope.products = [];

        var fetchProducts = function () {
            $http({
                method: 'GET',
                url: $scope.url,
                cache: $templateCache
            }).success(function (data, status, headers, config) {
                $scope.products = data.products;
            }).error(function (data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        };

        fetchProducts();
    }]);

contentApp.controller('HeaderCtrl', ['$scope', '$http', '$templateCache',
    function ($scope, $http, $templateCache) {
        $scope.authUrl = '/api/user/uaa/v1/me';
        $scope.meUrl = '/api/user/uaa/v1/me';
        $scope.user = {};

        var fetchUser = function () {
            $http({
                method: 'GET',
                url: $scope.authUrl,
                cache: $templateCache
            }).success(function (data, status, headers, config) {
                $http({
                    method: 'GET',
                    url: $scope.meUrl,
                    cache: $templateCache
                }).success(function (data, status, headers, config) {
                    $scope.user = data;
                }).error(function (data, status, headers, config) {
                });
                $scope.user = data;
            }).error(function (data, status, headers, config) {
            });
        };

        fetchUser();
    }]);

contentApp.directive('carouselactors', function () {
    var res = {
        restrict: 'A',
        link: function (scope, element, attrs) {
            scope.$watch(attrs.carouselactors, function (product) {
                if (scope.product != undefined ? scope.product.actors != undefined ? scope.product.actors.length > 0 : false : false) {
                    product = scope.product;
                    var html = '';
                    for (var i = 0; i < product.actors.length; i++) {
                        var actorTitleLink = product.actors[i].poster_image || '/assets/img/actors/' + product.actors[i].name.replace('/', ' ') + '.jpg';
                        html += '<div class="item">' +
                            '<div class="thumbnail">' +
                            '<a href="index.html#/people/' + product.actors[i].name + '"><img src="' + actorTitleLink + '"/></a>' +
                            '</div>' +
                            '<span><a href="index.html#/people/' + product.actors[i].name + '">' + product.actors[i].name + '</a></span>' +
                            '</div>';

                    }
                    //src="assets/img/actors/' + actorTitleLink + '.jpg"
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


contentApp.controller('ProductItemCtrl', ['$scope', '$routeParams', '$http', '$templateCache',
    function ($scope, $routeParams, $http, $templateCache) {
        $scope.productItemUrl = '/api/catalog/v1/products/' + $routeParams.productId;
        $scope.productsUrl = '/api/catalog/v1/catalog';
        $scope.products = [];

        var fetchProduct = function () {

            $http({
                method: 'GET',
                url: $scope.productsUrl,
                cache: $templateCache
            }).success(function (data, status, headers, config) {
                $scope.products = data.products;
            }).error(function (data, status, headers, config) {
            });

            $http({
                method: 'GET',
                url: $scope.productItemUrl,
                cache: $templateCache
            }).success(function (data, status, headers, config) {
                $scope.product = data;
                $scope.product.poster_image = '/assets/img/posters/' + $scope.product.productId + '.png';
            }).error(function (data, status, headers, config) {
            });
        };

        fetchProduct();
    }]);

contentApp.directive('carouselpeopleproducts', function () {
    var res = {
        restrict: 'A',
        link: function (scope, element, attrs) {
            scope.$watch(attrs.carouselpeopleproducts, function (people) {
                console.log(scope.people);
                if (scope.people != undefined ? scope.people.products != undefined ? scope.people.products.length > 0 : false : false) {
                    people = scope.people;
                    var html = '';
                    for (var i = 0; i < people.products.length; i++) {
                        var relatedProductTitleLink = people.products[i].poster_image || '/assets/img/posters/' + people.products[i].title.replace('/', ' ') + '.jpg';
                        html += '<div class="item">' +
                            '<div class="thumbnail">' +
                            '<a href="index.html#/products/' + people.products[i].title.replace('/', '%252F') + '"><img src="' + relatedProductTitleLink + '"/></a>' +
                            '</div>' +
                            '<span><a href="index.html#/products/' + people.products[i].title.replace('/', '%252F') + '">' + people.products[i].title + '</a></span>' +
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

contentApp.directive('carouselrelatedpeople', function () {
    var res = {
        restrict: 'A',
        link: function (scope, element, attrs) {
            scope.$watch(attrs.carouselrelatedpeople, function (people) {
                if (scope.people != undefined ? scope.people.related != undefined ? scope.people.related.length > 0 : false : false) {
                    people = scope.people;
                    var html = '';
                    for (var i = 0; i < people.related.length; i++) {
                        var actorTitleLink = people.related[i].related.poster_image || '/assets/img/actors/' + people.related[i].related.name.replace('/', ' ') + '.jpg';
                        html += '<div class="item">' +
                            '<div class="thumbnail">' +
                            '<a href="index.html#/people/' + people.related[i].related.name + '"><img src="' + actorTitleLink + '"/></a>' +
                            '</div>' +
                            '<span><a href="index.html#/people/' + people.related[i].related.name + '">' + people.related[i].related.name + '</a></span>' +
                            '</div>';

                    }
                    //src="assets/img/actors/' + actorTitleLink + '.jpg"
                    element[0].innerHTML = html;

                    setTimeout(function () {
                        $(element).owlCarousel({
                            items: 8,
                            itemsDesktop: [1199, 7],
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

contentApp.controller('PeopleItemCtrl', ['$scope', '$routeParams', '$http', '$templateCache',
    function ($scope, $routeParams, $http, $templateCache) {
        console.log('http://productapi-neo4j.herokuapp.com/api/v0/people/name/' + encodeURIComponent(decodeURI(decodeURI($routeParams.peopleId))) + '?api_key=special-key&neo4j=false');
        $scope.url = 'http://productapi-neo4j.herokuapp.com/api/v0/people/name/' + encodeURIComponent(decodeURI(decodeURI($routeParams.peopleId))) + '?api_key=special-key&neo4j=false';
        var fetchPeople = function () {
            $http({
                method: 'GET',
                url: $scope.url,
                cache: $templateCache
            }).success(function (data, status, headers, config) {
                $scope.people = data;
                $scope.people.poster_image = $scope.people.poster_image || '/assets/img/actors/' + $scope.people.name.replace('/', ' ') + '.jpg';
            }).error(function (data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }

        fetchPeople();
    }]);

contentApp.filter('rawHtml', ['$sce', function($sce){
    return function(val) {
        return $sce.trustAsHtml(val);
    };
}]);