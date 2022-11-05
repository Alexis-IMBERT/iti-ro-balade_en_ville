package balade;

import graphro.GrapheListe;
import graphro.Sommet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main {

	public static void main( String[] args ) {
		GrapheListe ville = GrapheListe.deFichier( "./Programme/data/ville_sans_metro.txt" );
		System.out.println(ville);

		Set<Sommet> POIs = new HashSet<>();
		POIs.add( new Sommet( "depot", 0 ) );
		POIs.add( new Sommet( "marron", 0 ) );
		POIs.add( new Sommet( "vert", 0 ) );
		POIs.add( new Sommet( "rouge", 0 ) );
		POIs.add( new Sommet( "mauve", 0 ) );

		HashMap<Sommet, HashMap<Sommet,Integer>> distancesDepuisSommet = new HashMap<>();

		for( Sommet sommet : POIs){
			Set<Sommet> destinations = new HashSet<>(POIs);
			destinations.remove( sommet );
			distancesDepuisSommet.put( sommet, DijkstraRO.calculerDistanceAdressesDepuisSource( ville, sommet, destinations ) );
		}

		System.out.println(distancesDepuisSommet);

		// brute force
	}
}