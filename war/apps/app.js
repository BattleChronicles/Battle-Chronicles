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

		} ]);



// cf https://scotch.io/tutorials/single-page-apps-with-angularjs-routing-and-templating
app.config(function($routeProvider) {
	$routeProvider
	.when("/", {
		templateUrl: "pages/home.html",
        controller: "MainController"
	})
    .when("/game", {
        templateUrl: "pages/game.html",
        controller: "GameController"
    })
    .when("/game/end", {
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
						$location.path('/game');
											}
								);
					
				}
				else {
					$location.path('/game');
					
				}
				
			};
		}
]);
	
// GameController

app.controller('GameController', ['$rootScope', '$scope', '$routeParams', '$location',
    function($rootScope, $scope, $routeParams, $location) {
	    // Redirect if refreshed
	    if (!$routeParams.index_round ||
	        !$routeParams.question_type ||
	        !$rootScope.currentGame) {
	        $location.path('/categories');
	        return;	
	}


	$scope.question = function(){
		    // Checks if the question has been answered to increase the counter of questions answered and checks wether the answer given is the expected one to increase the counter of good answers
		    $scope.answerClick = function(index) {
			if ($scope.question.answered === false) {
			    $scope.question.answered = index;
			    $scope.questions.answered++;
			    if($scope.question.good_answer === $scope.question.answered) {
				$scope.questions.well_answered++;
			    }
			}
		    }

		    // Resets the questions attributes and shows the next question
		    $scope.restart = function() {

				$scope.questions = {
				    answered: 0,
				    well_answered: 0,
				    name: null
				}

				$scope.nextQuestion();
		    }

		    // Enables the loop for the n questions (which are all the same), open the GameOver page when done
		    $scope.nextQuestion = function() {

			if ($scope.questions.answered < 3) {

			    $scope.question = {
				text: "Which answer is correct ?",
				answers: [
				    "A",
				    "B",
				    "C",
				    "D"
				],
				good_answer: Math.floor(Math.random()*4),
				answered: false
			    }

			} else {
			    $scope.insertScore({id: Math.floor(Math.random()*1000000)+1, name: $scope.questions.name, score: $scope.questions.well_answered*10});
			    $location.page('game/end');
			}

		    }
	}


// EndGameController

app.controller('EndGameController', ['$rootScope', '$scope', '$location', 'GApi',
	    function($rootScope, $scope, $location, GApi) {
	
		    GApi.execute('endpoint', 'Endpoint.insertScore', {id: $rootScope.user_id, name: $rootScope.user.name, pic: $rootScope.google_user.picture, score: $rootScope.Game.score}).then( function(resp) {
		        $scope.loading = false;
		    }, function() {
		        $scope.loading = false;
		        console.log("error :(");
		    });
		
			// Redirect if refreshed
		    if (!$rootScope.currentGame) {
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
			GApi.execute('endpoint','gethighscores').then(function(resp) {
				callback(resp.items);
			}, function() {
				console.log('Error!');
			}
		);
		}
}

]);


		} ]);