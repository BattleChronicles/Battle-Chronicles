package fr.battlechronicles.api;

import java.util.ArrayList;
import java.util.List;

import fr.battlechronicles.api.Score;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class Datastore {

        DatastoreService datastore;
        
        public Datastore(){
                 datastore = DatastoreServiceFactory.getDatastoreService();
        }
        
        public void insertScore(Score score){
                Entity e = new Entity("Score");
                e.setProperty("id", score.getId());
                e.setProperty("score", score.getScore());
                e.setProperty("name", score.getName());
                datastore.put(e);
        }

        public ArrayList<Score> listHighscores() {
                ArrayList<Score> highscores = new ArrayList<Score>();
                Query query = new Query("Score").addSort("score", SortDirection.DESCENDING);;
                List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
                
                for (Entity e : results){
                        String id = (String) e.getProperty("id");
                        String name = (String) e.getProperty("name");
                        int score = (int) e.getProperty("score");
                        highscores.add(new Score(id, name, score));
                }       
                return highscores;
        }


}

