package fr.battlechronicles.api;

import java.util.List;

public class Highscores {
	private List<Score> scores;

	public Highscores(List<Score> scores) {
		this.scores = scores;
	}

	public List<Score> getScores() {
		return scores;
	}

	public void setScores(List<Score> scores) {
		this.scores = scores;
	}
	
}
