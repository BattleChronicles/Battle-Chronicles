package fr.battlechronicles.question;

public class Question {
	protected String requete;
	protected int offset;
	
	public Question(){
		this.offset = (int) (Math.random() * 1000 + 1);
		this.requete = 	
			  "PREFIX dbo: <http://dbpedia.org/ontology/>"
			+ "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>"
			+ "select distinct ?MilitaryConflict ?isPartOfMilitaryConflict ?result (group_concat(distinct ?combatant;separator=\" | \") as ?combatants) (group_concat(distinct ?commander;separator=\" | \") as ?commanders) ?date ?lat ?long"
			+ "where {"
				+ "?MilitaryConflict a <http://dbpedia.org/ontology/MilitaryConflict>;"
					+ "dbo:result ?result;"
					+ "dbo:combatant ?combatant;"
					+ "dbo:commander ?commander;"
					+ "dbo:date ?date;"
					+ "geo:lat ?lat;"
					+ "geo:long ?long."
					+ "FILTER regex(?result, \"[vV]ictory\")" 
			+ "} LIMIT 200"
			+ "offset "
			+ Integer.toString(this.offset);
	}
	
	public String getRequest(){
		return this.requete;
	}
	
	public void setRequest(String requete){
		this.requete = requete;
	}
}
