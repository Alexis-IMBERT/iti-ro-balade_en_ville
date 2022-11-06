package balade;

import graphro.GrapheListe;
import graphro.GrapheMatrice;
import graphro.Sommet;

import java.util.*;

/**
 * Calcule le plus court cycle d'un graphe ayant pour origine le Sommet origine et devant passé par tous les POIs
 */
public class TSPRO {

	private Sommet origine;
	private GrapheListe graphe;
	private Set<Sommet> POIs;
	private Map<String, List<Sommet>> chemins;
	private Sommet[] plusCourtChemin;

	private Sommet[] plusCourtCheminDetaille;
	private int longueur;

	public TSPRO( GrapheListe graphe, Set<Sommet> POIs, Sommet origine ){
		this.graphe = graphe;
		this.POIs = POIs;
		this.origine = origine;
		this.chemins = new HashMap<>();
		this.longueur = 0;
		resoudre();
	}

	/**
	 * Résolution du problème
	 */
	private void resoudre(){

		// On créé le graphe des distances entre les points d'intérêts
		GrapheMatrice grapheDistance = new GrapheMatrice( POIs.size() );
		for(Sommet sommet : POIs){
			grapheDistance.ajouterSommet( sommet );
		}

		// On parcourt tous les points d'intérêts
		for( Sommet sommet : POIs){
			// Les destinations voulues sont les autres points d'intérêts
			Set<Sommet> destinations = new HashSet<>(POIs);
			destinations.remove(sommet);

			// Calcul des plus cours chemin entre le sommet et les autres POIs
			HashMap<Sommet,List<Sommet>> cheminsDepuisSommet = DijkstraRO.calculerCheminsAdressesDepuisSource( graphe, sommet, destinations );
			// On récupère les distances et les chemins entre le sommet et les autres POIs
			for(Sommet destination : cheminsDepuisSommet.keySet()){
				grapheDistance.ajouterArc( sommet, destination, cheminsDepuisSommet.get( destination ).size() );

				chemins.put(cle(sommet,destination), cheminsDepuisSommet.get( destination ));
			}

			// On indique que la distance entre un somemt et lui-même vaut 0
			grapheDistance.ajouterArc( sommet, sommet, 0 );
		}

		// Calcul du plus court chemin
		this.plusCourtChemin = HamiltonienRO.calculerCycle( grapheDistance, new Sommet( "depot", 0 ) );

		// On génère le plus court chemin détaillé
		List<Sommet> tmp = new ArrayList<>();
		tmp.add(this.origine);
		for(int i = 0; i < this.plusCourtChemin.length-1; i++){
			List<Sommet> chemin = cheminEntre( this.plusCourtChemin[i], this.plusCourtChemin[i+1] );
			this.longueur += chemin.size();

			tmp.addAll( chemin );
		}

		this.plusCourtCheminDetaille = tmp.toArray( new Sommet[0] );
	}

	/**
	 * Donne le plus court chemin
	 * @return Le plus court chemin
	 */
	public Sommet[] getPlusCourtChemin(){
		return this.plusCourtChemin;
	}

	/**
	 * Donne le plus court chemin détaillé
	 * @return Le plus court chemin détaillé
	 */
	public Sommet[] getPlusCourtCheminDetaille() {
		return this.plusCourtCheminDetaille;
	}

	/**
	 * Donne la longueur du plus court chemin
	 * @return La longueur du plus court chemin
	 */
	public int getLongueur(){
		return this.longueur;
	}

	/**
	 * Donne les sommets à parcourir depuis le sommet s1 pour atteindre le sommet s2
	 * @param s1 L'origine
	 * @param s2 La destination
	 * @return Les sommets à parcourir
	 */
	public List<Sommet> cheminEntre(Sommet s1, Sommet s2){
		return chemins.get( cle(s1,s2) );
	}

	/**
	 * Interface pour accéder au chemin entre le sommet s1 et le sommet s2
	 * @param s1 L'origine
	 * @param s2 La destination
	 * @return la clé permettant d'accéder au chemin en question
	 */
	private static String cle(Sommet s1, Sommet s2){
		return String.format( "%s-%s", s1.toString(), s2.toString() );
	}
}
