
var CLIENT = '120697713163-blktasl5j3pd980em8j076mtno9c81i4.apps.googleusercontent.com';

var app = angular.module('appName',['angular-google-gapi','ngCookies']);

app.run(['GAuth', function(GAuth) {

    GAuth.setClient(CLIENT);
    GAuth.setScope('https://www.googleapis.com/auth/userinfo.profile');

    GAuth.load();

}]);

app.controller('MainController', ['$scope','GAuth','$cookies','GData', function($scope, GAuth, $cookies, GData) {

    $scope.user = null;

    if ($cookies.get("google_id")) {
        GData.setUserId($cookies.get("google_id"));

        GAuth.checkAuth().then(
            function(user) {
                $scope.user = user;
            },
            function() {
                console.log("Failure to connect");
            }
        )
    }

    $scope.login = function() {
        GAuth.login().then(function(user) {
            console.log(user);
            $scope.user = user;
            $cookies.put("google_id", user.id);
        }, function() {
            console.log("Failure to connect");
        });
    };

    $scope.logout = function() {
        GAuth.logout();
        $scope.user = null;
        $cookies.remove("google_id");
    };

}]);