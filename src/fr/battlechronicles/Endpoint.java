package fr.battlechronicles;

import java.util.ArrayList;

import fr.battlechronicles.api.Datastore;
import fr.battlechronicles.api.Score;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;

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

}
