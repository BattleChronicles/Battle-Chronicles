package fr.battlechronicles;

import java.util.ArrayList;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;

import fr.battlechronicles.api.Datastore;
import fr.battlechronicles.api.Score;

@Api(name = "endpoint", version = "v1")
public class Endpoint {
    private Datastore datastore = new Datastore();
    
        
        @ApiMethod(name= "gethighscores", path="/highscores")
        public ArrayList<Score> gethighscores() {
                return datastore.listHighscores();
        }

        
        @ApiMethod(name= "insertscore", path="/insertscore")
        public void insertscore(@Named("id") String id, @Named("name") String name, @Named("score") int score){
                Score s = new Score(id, name, score);
                datastore.insertScore(s);
        }
        //a commenter après un remplissage 
        //(ou utiliser un truc pour remplir de temps à autre, sinon dbpedia va nous assassiner
        
        @ApiMethod(name= "updateDatastore")
        public void updateDatastore() {
        	datastore.listReponse();
    	}

}
