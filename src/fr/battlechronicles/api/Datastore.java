package fr.battlechronicles.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import fr.battlechronicles.question.Reponse;

public class Datastore {

        DatastoreService datastore;
        
        public Datastore(){
                 datastore = DatastoreServiceFactory.getDatastoreService();
        }
        
        public void insertScore(Score score){
                Entity entity = new Entity("Score");
                entity.setProperty("id", score.getId());
                entity.setProperty("score", score.getScore());
                entity.setProperty("name", score.getName());
                datastore.put(entity);
        }

        public ArrayList<Score> listHighscores() {
                ArrayList<Score> highscores = new ArrayList<Score>();
                Query query = new Query("Score").addSort("score", SortDirection.DESCENDING);
                List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
                
                for (Entity e : results){
                        String id = (String) e.getProperty("id");
                        String name = (String) e.getProperty("name");
                        int score = (int) e.getProperty("score");
                        highscores.add(new Score(id, name, score));
                }       
                return highscores;
        }
        
	public ArrayList<Reponse> listReponse() {
		ArrayList<Reponse> reponses = new ArrayList<Reponse>();
		Query query = new Query("Bataille").addSort("bataille", SortDirection.DESCENDING);
		List<Entity> results = this.datastore.prepare(query).asList(FetchOptions.Builder.withLimit(100));
		for (Entity e : results){
			String nomBataille = (String) e.getProperty("MilitaryConflict");
			String nomConflit = (String) e.getProperty("isPartOfMilitaryConflict");
			String result = (String) e.getProperty("result");
			String belligerantstemp = (String) e.getProperty("combatants");
			List<String> belligerants = Arrays.asList(belligerantstemp.split(" | "));
			String date = (String) e.getProperty("date");
			Double latitude = (Double) e.getProperty("lat");
			Double longitude = (Double) e.getProperty("long");
			String commandantstemp = (String) e.getProperty("commanders");
			List<String> commandants = Arrays.asList(commandantstemp.split(" | "));

			reponses.add(new Reponse(nomBataille, nomConflit, result, belligerants, date,
					latitude, longitude, commandants));
		}
		return reponses;
	}

	public ArrayList<String> listCommander(){
		Set<String> commander = new TreeSet<String>();
		Query query = new Query("Bataille").addSort("bataille", SortDirection.DESCENDING);
		List<Entity> results = this.datastore.prepare(query).asList(FetchOptions.Builder.withLimit(100));
		for (Entity e : results){
			String nomBataille = (String) e.getProperty("MilitaryConflict");
			String nomConflit = (String) e.getProperty("isPartOfMilitaryConflict");
			String result = (String) e.getProperty("result");
			String belligerantstemp = (String) e.getProperty("combatants");
			List<String> belligerants = Arrays.asList(belligerantstemp.split(" | "));
			String date = (String) e.getProperty("date");
			Double latitude = (Double) e.getProperty("lat");
			Double longitude = (Double) e.getProperty("long");
			//Les lignes au-dessus sont probablement inutiles TODO to remove in future commit.
			String commandantstemp = (String) e.getProperty("commanders");
			List<String> commandants = Arrays.asList(commandantstemp.split(" | "));

			commander.addAll(commandants);
		}
		ArrayList<String> sortie = new ArrayList<String>();
		sortie.addAll(commander);
		return sortie;
	}

	public ArrayList<String> listQuestion() {
		ArrayList<String> questions = new ArrayList<String>();
		questions.add("O� c'est d�roul� ");
		questions.add("Quand c'est d�roul� ");
		questions.add("Au cours de quelle conflit c'est d�roul� ");
		questions.add("Qu'elles �taient les b�lligerants de ");
		questions.add("Qui parmis ces personnes commandaient des arm�es durant ");
		questions.add("Qu'elle fut l'issue de ");
		return questions;
	}
	
}

