package fr.battlechronicles;

import java.util.ArrayList;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.Entity;

import fr.battlechronicles.api.Datastore;
import fr.battlechronicles.api.Score;
import fr.battlechronicles.question.GenerateurQuestion;

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
