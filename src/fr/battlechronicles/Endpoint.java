package fr.battlechronicles;

import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;

import fr.battlechronicles.api.Datastore;
import fr.battlechronicles.api.Score;
import fr.battlechronicles.question.Question;
import fr.battlechronicles.question.Reponse;
import jena.sparql;

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
        
	@ApiMethod(name= "listQuestion")
	public void listQuestion() {
		this.datastore.listReponse();
		
	}
	
	@ApiMethod(name= "fill", path="/fill")
	public void insertQuestions(@Named("category") String category) throws Exception{
		try {
			Question question = new Question();
			Servlet serv = new Servlet();
			serv.updateDatastore();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
