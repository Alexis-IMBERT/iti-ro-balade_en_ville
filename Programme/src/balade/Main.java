package balade;

import graphro.Arc;
import graphro.GrapheListe;
import graphro.GrapheMatrice;
import graphro.Sommet;

import java.lang.reflect.Array;
import java.security.KeyPair;
import java.util.*;

public class Main {

	public static void main( String[] args ) {
		// Récupération du graphe de la ville
		GrapheListe ville = GrapheListe.deFichier( "./Programme/data/ville.txt" );

		// Création des points d'intérêt
		Sommet origine = new Sommet( "depot", 0 );
		Set<Sommet> POIs = new HashSet<>();
		POIs.add( origine );
		POIs.add( new Sommet( "marron", 0 ) );
		POIs.add( new Sommet( "vert", 0 ) );
		POIs.add( new Sommet( "rouge", 0 ) );
		POIs.add( new Sommet( "mauve", 0 ) );

		// Génération du plus court chemin de la ville
		TSPRO TSPVille = new TSPRO( ville, POIs, origine );
		// Génération du plus court chemin de la ville sans métro
		supprimerMetro( ville );
		TSPRO TSPVilleSansMetro = new TSPRO( ville, POIs, origine );

		// Récupération des chemins
		Sommet[] cheminVille = TSPVille.getPlusCourtChemin();
		Sommet[] cheminVilleSansMetro = TSPVilleSansMetro.getPlusCourtChemin();

		// Récupération des chemins détaillés
		Sommet[] cheminVilleDetaille = TSPVille.getPlusCourtCheminDetaille();
		Sommet[] cheminVilleSansMetroDetaille = TSPVilleSansMetro.getPlusCourtCheminDetaille();

		// Affichage
		System.out.println("--- Affichage simple ---");
		System.out.println( String.format( "Avec le métro (%d) : %s", TSPVille.getLongueur(), Arrays.toString( cheminVille ) ) );
		System.out.println( String.format( "Sans le métro (%d) : %s", TSPVilleSansMetro.getLongueur(), Arrays.toString(cheminVilleSansMetro)));
		System.out.println();
		System.out.println("--- Affichage détaillé ---");
		System.out.println( String.format( "Avec le métro (%d) : %s", TSPVille.getLongueur(), Arrays.toString( cheminVilleDetaille ) ) );
		System.out.println( String.format( "Sans le métro (%d) : %s", TSPVilleSansMetro.getLongueur(), Arrays.toString(cheminVilleSansMetroDetaille)));


	}

	/**
	 * Supprime le métro du graphe passé en paramètre
	 * @param graphe le graphe
	 */
	public static void supprimerMetro(GrapheListe graphe){
		Collection<Sommet> sommets = graphe.sommets();

		for( Sommet sommet : sommets){
			if(estUnMetro( sommet )){
				for( Arc arc : graphe.voisins( sommet )){
					Sommet voisin = arc.destination();
					if(estUnMetro( voisin )){
						graphe.enleverArc( sommet, voisin );
						graphe.enleverArc( voisin, sommet );
					}
				}
			}
		}
	}

	/**
	 * Indique si un sommet est un métro ou non
	 * @param s le sommet
	 * @return Si le sommet est un métro ou non
	 */
	public static boolean estUnMetro(Sommet s){
		return s.toString().matches( "m[0-9]+" );
	}
}