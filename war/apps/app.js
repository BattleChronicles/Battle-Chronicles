// Angular application for Battle Chronicles
// Authors : Bien-CV, E1drad, Freyj, Licherm, Marie-Donnie

var CLIENT = '120697713163-blktasl5j3pd980em8j076mtno9c81i4.apps.googleusercontent.com';

var app = angular.module('BattleChronicles', [ 'angular-google-gapi', 'ngCookies', 'ngRoute']);

var BASE = 'https://battle-chronicles.appspot.com/_ah/api'; 

app.run([
		'$rootScope',
		'$cookies',
		'GApi',
		'GAuth',
		'GData',
		function($rootScope, $cookies, GApi, GAuth, GData) {
			
			GApi.load('endpoint', 'v1', BASE).then(
					function(resp) {
						if (resp)
						console.log('API: ' + resp.api + ', version: '
								+ resp.version + ' loaded');
					},
					function(resp) {
						console.log('An error occurred while loading API: '
								+ resp.api + ', resp.version: ' + version);
					}
			);
			GAuth.setClient(CLIENT);
			GAuth.setScope('https://www.googleapis.com/auth/userinfo.profile');
			GAuth.load();
			
			if ($cookies.get("google_id")) {
				GData.setUserId($cookies.get("google_id"));
			}
			
			$rootScope.login = function() {
				GAuth.login().then(function(user) {
					console.log(user);
					$rootScope.user = user;
					$cookies.put("google_id", user.id);
				}, function() {
					console.log("Failure to connect");
				});
			};

			$rootScope.logout = function() {
				GAuth.logout();
				$rootScope.user = null;
				$cookies.remove("google_id");
			};
			
	        GAuth.checkAuth().then(
	        		function(user) {
	        			$rootScope.user = user;
	                    if ($rootScope.auth_callback_success)
	                        $rootScope.auth_callback_success();
	        		},
	        		function() {
	                    $rootScope.not_logged = true;
	        			//console.log('error');
	                    console.log('not logged in');
	                    if ($rootScope.auth_callback_error)
	                        $rootScope.auth_callback_error();
	        		}
	    );

		$rootScope.Game = null;
		}
]);



// cf https://scotch.io/tutorials/single-page-apps-with-angularjs-routing-and-templating
app.config(function($routeProvider) {
	$routeProvider
	.when("/", {
		templateUrl: "pages/home.html",
        controller: "MainController"
	})
    .when("/game/question/:battle_index/:question_type", {
        templateUrl: "pages/game.html",
        controller: "GameController"
    })
    .when("/endgame", {
        templateUrl: "pages/endgame.html",
        controller: "EndGameController"
    })
    .when("/highscores", {
        templateUrl: "pages/highscores.html",
        controller: "HighscoresController"
    })
    .when("/about", {
        templateUrl: "pages/about.html",
        controller: "aboutController"
    })
});


//Defines the controller ; scope is the object defined for the controller to contain 
//as attributes every variables need ; the array format for .controller() is better if we are to minify the file

// MainController
app.controller('MainController', [ '$scope', '$location',
		function($scope, $location) {
			$scope.play = function(){
				if (!$scope.user){
					$scope.login(function(){
						$location.path('/game/question/0/who');
											}
								);					
				}
				else {
					$location.path('/game/question/0/who');
					
				}
				
			};
		}
]);
	
// GameController

app.controller('GameController', ['$rootScope', '$scope', '$routeParams', '$location', 'GApi',
    function($rootScope, $scope, $routeParams, $location, GApi) {
//	    // Redirect if refreshed
//	    if (!$routeParams.battle_index ||
//	        !$routeParams.question_type ||
//	        !$rootScope.game) {
//	        $location.path('/');
//	        return;	
//	    }
		var count = 3;
	    $rootScope.game = {
	            count: count,
	            score: 0,
	            battles: null
	    };
    
        
        GApi.execute('endpoint', 'Endpoint.listQuestions',{number:count}).then( function(resp) {
            $rootScope.game.battles = resp.items;
            console.log(resp.items);            
		}, function() {
            $scope.loading = false;
			console.log("Error while loading questions:(");
		});	
        
        var battle = $rootScope.game.battles[$routeParams.battle_index];

        if (!battle) {
            $location.path('/endgame');
            return;
        }

        $scope.battle = battle;
        $scope.question_type = $routeParams.question_type;

        $scope.chooseAnswer = function(index) {
            if ($scope.question.answered !== null)
                return;
            $scope.question.answered = index;
            $scope.timer.stop();
            if ($scope.question.answered === $scope.question.right_answer) {
                $rootScope.game.score += $scope.timer.percentage*10;
            }
            if ($scope.question_type == 'who')
                battle.name = $scope.question.right_answer;
            else if ($scope.question_type == 'when')
                battle.date = $scope.question.right_answer;
            else if ($scope.question_type == 'where')
                battle.location = $scope.question.right_answer;
        };

        $scope.next = function() {
            switch($scope.question_type) {
                case 'who':
                    $routeParams.question_type = 'when';
                    break;
                case 'when':
                    $routeParams.question_type = 'where';
                    break;
                case 'where':
                    $routeParams.question_type = 'rightanswers';
                    break;
            }

            $location.path('/game/question/'+$routeParams.battle_index+'/'+$routeParams.question_type);
        };

        $scope.nextBattle = function() {
            $location.path('/game/question/'+(parseInt($routeParams.battle_index)+1)+'/who');
        };

        $scope.setMapPick = function(pick) {
            $scope.map_pick = pick;
            $scope.$apply();
        };

        // https://developers.google.com/maps/documentation/javascript/adding-a-google-map
        $scope.initMap = function() {
        	var center = {lat: 29, lng: -12};
            var map = new google.maps.Map(document.getElementById("map"), {
            		center: myCenter, 
            		zoom: 1
            		});
            var infowindow = new google.maps.InfoWindow();
            
            var marker = new google.maps.Marker({
                position: latlng,
                map: map                    
		    }); //end marker
            var distance = null;
 		    var p2 = new google.maps.LatLng(46.0438317, 9.75936230000002);
            
		    //Add listener
		    google.maps.event.addListener(marker, "dragend", function (event) {
			    latLng = new google.maps.LatLng(marker.position.lat(),marker.position.lng());
		        distance = calcDistance(latLng, p2)
		    }); //end addListener
		   
		    //calculates distance between two points in km's
		    function calcDistance(p1, p2) {
		    	return (google.maps.geometry.spherical.computeDistanceBetween(p1, p2) / 1000).toFixed(2);
		    }
        }

        $scope.timer = {
            percentage: 100,
            id: null,
            start: function() {
                $scope.timer.id = setInterval(function() {
                    $scope.timer.percentage -= 2;
                    $scope.$apply();
                    if ($scope.timer.percentage <= 0)
                        $scope.timer.stop(true);
                },200);
            },
            stop: function(out) {
                if ($scope.timer.id != null) {
                    clearInterval($scope.timer.id);
                    $scope.timer.id = null;
                    $scope.chooseAnswer("No answer");
                    if (out)
                        $scope.$apply();
                }
            }
        };
	        
        if ($scope.question_type != 'rightanswers') {
            $scope.question = {
                title: battle[$scope.question_type].question,
                right_answer: battle[$scope.question_type].answers[0],
                answers: $.randomize(battle[$scope.question_type].answers),
                answered: null
            };
            $scope.timer.start();
        }
        if ($scope.question_type == 'where') {
            $scope.initMap();
        }
}
        
]);



// EndGameController

app.controller('EndGameController', ['$rootScope', '$scope', '$location', 'GApi',
	    function($rootScope, $scope, $location, GApi) {
	
		    GApi.execute('endpoint', 'Endpoint.insertScore', {id: $rootScope.user_id, name: $rootScope.user.name, score: $rootScope.game.score}).then( function(resp) {
		        $scope.loading = false;
		    }, function() {
		        $scope.loading = false;
		        console.log("error :(");
		    });
		
			// Redirect if refreshed
		    if (!$rootScope.game) {
		        $location.path('/');
		        return;
		    }
		}
]);

// HighscoresController

app.controller('HighscoresController', ['$rootScope', '$scope', '$location', 'GApi',
    function($rootScope, $scope, $location, GApi) {
		$scope.loading = true;

		// Executes the GApi call then give back the answer to the function that called it 
		//(then (function(resp) is called if the ajax request was successful, function() if not))
		$scope.listScores = function(callback) {
			GApi.execute('endpoint','Endpoint.gethighscores').then(function(resp) {
				callback(resp.items);
			}, function() {
				console.log('Error!');
			}
														);
		}
	}	
]);
