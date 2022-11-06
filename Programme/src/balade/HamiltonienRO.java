package balade;

import com.sun.security.jgss.GSSUtil;
import graphro.Arc;
import graphro.GrapheMatrice;
import graphro.Sommet;

import java.util.*;

/**
 * Calcule le plus court chemin hamiltonien depuis un sommet source
 */
public class HamiltonienRO {

	private static Sommet[] plusCourtChemin;
	private static int longueurMin;

	/**
	 * Interface
	 * @param graphe Le graphe
	 * @param source Le sommet source du cycle
	 * @return le plus court cycle hamiltonien
	 */
	public static Sommet[] calculerCycle( GrapheMatrice graphe, Sommet source ) {

		longueurMin = Integer.MAX_VALUE;

		int taille = graphe.taille();

		Collection<Sommet> sommets = graphe.sommets();
		sommets.remove( source );

		List<Sommet> cycle = new LinkedList<>();
		cycle.add( source );

		resolution( graphe, cycle, source, source, taille, 0 );

		return plusCourtChemin;
	}

	/**
	 * Calcul du plus court cycle hamiltonien.
	 * Pour se faire, on créé tous les cycles hamiltoniens et on conserve le plus court
	 * @param graphe le graphe
	 * @param cycle L'état actuel du cycle
	 * @param sommetActuel Le sommet actuel
	 * @param origine Le sommet d'origine
	 * @param nbSommets Le nombre de sommets
	 * @param longueurActuelle La longueur actuelle
	 */
	private static void resolution(GrapheMatrice graphe, List<Sommet> cycle, Sommet sommetActuel, Sommet origine, int nbSommets, int longueurActuelle){
		// On s'arrête une fois que le sommet actuel est le dernier
		if(cycle.size() == nbSommets){
			// on ajoute la longueur entre le dernier sommet et l'origine
			longueurActuelle += graphe.valeurArc( sommetActuel, origine );

			// On retourne à l'origine
			cycle.add(origine);

			// On regarde si ce cycle est plus court que le précédent
			if( longueurActuelle < longueurMin ){
				longueurMin = longueurActuelle;
				plusCourtChemin = cycle.toArray(new Sommet[0]);
			}

			// On n'oublie pas de supprimer le dernier sommet (le retour à l'origine) pour la récursivité
			cycle.remove( cycle.size()-1 );

			return;
		}

		// Si on n'est pas au dernier sommet, on parcourt tous ses arcs
		for( Arc arc : graphe.voisins( sommetActuel )){
			// On ne gère pas les arcs vers soi
			if(arc.origine() != arc.destination()) {
				Sommet voisin;
				// On cherche qui est le voisin dans l'arc
				if( arc.origine().equals( sommetActuel ) ) {
					voisin = arc.destination();
				} else {
					voisin = arc.origine();
				}

				// Si le voisin n'a pas déjà été parcouru
				if( !cycle.contains( voisin ) ) {
					// Récupération de la distance entre le sommet actuel et son voisin
					int distance = graphe.valeurArc( sommetActuel, voisin );

					// Ajout du voisin au cycle
					cycle.add( voisin );
					// Récursivité pour la suite du cycle
					resolution( graphe, cycle, voisin, origine, nbSommets, longueurActuelle+distance );
					// On n'oublie pas de retirer le voisin du cycle pour passé au voisin suivant
					cycle.remove( cycle.size()-1 );

				}
			}
		}
	}

}
