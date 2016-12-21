// Angular application for Battle Chronicles
// Authors : Bien-CV, E1drad, Freyj, Licherm, Marie-Donnie

var CLIENT = '120697713163-blktasl5j3pd980em8j076mtno9c81i4.apps.googleusercontent.com';

var app = angular.module('appName', [ 'angular-google-gapi', 'ngCookies' ]);

app.run([
		'GApi',
		'GAuth',
		function(GApi, GAuth) {
			var BASE = ''; 
			GApi.load('scoreEntityEndpoint', 'v1', BASE).then(
					function(resp) {
						console.log('API: ' + resp.api + ', version: '
								+ resp.version + ' loaded');
					},
					function(resp) {
						console.log('An error occurred while loading API: '
								+ resp.api + ', resp.version: ' + version);
					});
			GAuth.setClient(CLIENT);
			GAuth.setScope('https://www.googleapis.com/auth/userinfo.profile');
			GAuth.load();
		} ]);


//Defines the controller ; scope is the object defined for the controller to contain 
//as attributes every variables need ; the array format for .controller() is better if we are to minify the file
app.controller('MainController', [ '$scope', 'GApi', 'GAuth', '$cookies', 'GData',
		function($scope, Gapi, GAuth, $cookies, GData) {

			$scope.user = null;
			$scope.page = null;
			$scope.highscores = null;

			if ($cookies.get("google_id")) {
				GData.setUserId($cookies.get("google_id"));

				GAuth.checkAuth().then(function(user) {
					$scope.user = user;
				}, function() {
					console.log("Failure to connect");
				})
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
			    $scope.openPage('gameOver');
			}

		    }

		    $scope.openPage = function(page) {
			$scope.page = page;

			if (page == 'play') {
			    $scope.restart();
			} else if (page == 'highscores') {
			    $scope.highscores = null;
			    // Calls the listScores function in an asynchronous manner, enabling a callback (cf listScores)
			    $scope.listScores(function(data) {
				$scope.highscores = data;
			    });
			}
		    }

		    // Executes the GApi call then give back the answer to the function that called it 
		    //(then (function(resp) is called if the ajax request was successful, function() if not))
		    $scope.listScores = function(callback) {
			GApi.execute('hSEndpoint','highscores').then(function(resp) {
			    callback(resp.items);
			}, function() {
			    console.log('Error!');
			});
		    }


		    $scope.insertScore = function(data) {
			GApi.execute('hSEndpoint','insertScoreEntity',data).then(function(resp) {
			    console.log('Good!');
			},function(e) {
			    console.log('Error!');
			    console.log(e);
			});
		    }

		    $scope.openPage('play');
			

		} ]);