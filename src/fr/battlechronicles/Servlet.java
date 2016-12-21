package fr.battlechronicles;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import fr.battlechronicles.question.GenerateurQuestion;

public class Servlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3424689349443931512L;
	
	private DatastoreService dataService;
	
	public void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws IOException {
		dataService = DatastoreServiceFactory.getDatastoreService();
	}
    public void updateDatastore() {
		Entity p;
		int i = 0;
		Literal nomBataille;
		Literal nomConflit;
		Literal resultat;
		Literal belligerants;
		Literal date;
		Literal lat;
		Literal lon;
		Literal commandants;
		
    	//générateur de requête sparql
    	GenerateurQuestion gen = new GenerateurQuestion();
    	//execution de requête
    	QueryExecution qexec = 
    			QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",
    					gen.getQuestion().getRequest());
    	//on récup les résultats
    	ResultSet results = qexec.execSelect();
    	
    	while (results.hasNext())
		{
			QuerySolution binding = results.nextSolution();

	   		nomBataille = binding.getLiteral("MilitaryConflict");
    		nomConflit = binding.getLiteral("isPartOfMilitaryConflict");
    		resultat = binding.getLiteral("result");
    		belligerants = binding.getLiteral("combatants");
    		date = binding.getLiteral("date");
    		lat = binding.getLiteral("lat");
    		lon = binding.getLiteral("long");
    		commandants = binding.getLiteral("commanders");
			
			p = new Entity("Reponse", Integer.toString(i));
			
			p.setProperty("nomBataille", nomBataille.toString());
			p.setProperty("nomConflit", nomConflit.toString());
			p.setProperty("resultat", resultat.toString());
			p.setProperty("belligerants", belligerants.toString());
			p.setProperty("Date", date.toString());
			p.setProperty("latitude", lat);
			p.setProperty("longitude", lon);
			p.setProperty("commandants", commandants.toString());
			
			
			
		
			dataService.put(p);
			++i;
		}
		
		qexec.close() ;
    	
    }
}
