package balade;

import graphro.Arc;
import graphro.GrapheListe;
import graphro.Sommet;

import java.util.*;


public class DijkstraRO {

	/**
	 *
	 * @param graphe le graphe
	 * @param source le sommet d'origine de l'algorithme
	 * @param destinations les destinations à atteindre
	 * @return
	 */
	public static HashMap<Sommet,Integer> calculerDistanceAdressesDepuisSource( GrapheListe graphe, Sommet source, Set<Sommet> destinations ) {

		// Map de retour indiquant la distance entre le sommet et un noeud défini
		HashMap<Sommet,Integer> distanceDepuisSommet = new HashMap<>();

		// On récupère la liste des sommets sans la source
		Collection<Sommet> sommets = graphe.sommets();
		sommets.remove( source );
		Set<Sommet> setSource = new HashSet<>();
		setSource.add(source);
		Set<Sommet> copieDestinations = new HashSet<>(destinations);

		calculerLesDistances( graphe, distanceDepuisSommet, setSource, copieDestinations, 0 );

		HashMap<Sommet,Integer> distanceAdresses = new HashMap<>();

		for(Sommet destination : destinations){
			distanceAdresses.put( destination, distanceDepuisSommet.get( destination ) );
		}

		return distanceAdresses;
	}

	private static void calculerLesDistances( GrapheListe graphe, HashMap<Sommet,Integer> distances, Set<Sommet> niveauActuel, Set<Sommet> destinationsRestantes, int distance ){
		// si on est déjà passé par toutes les adresses ou qu'il n'y a pas de sommet pour le niveau actuel, on s'arrête
		if(destinationsRestantes.size() == 0 || niveauActuel.size() == 0 ){
			return;
		}

		Set<Sommet> prochaineNiveau = new HashSet<>();

		// On parcourt tous les sommets du niveau actuel
		for( Sommet sommetActuel : niveauActuel) {
			List<Arc> voisins = graphe.voisins( sommetActuel );
			Sommet voisin;
			// Pour chaque sommet du niveau actuel, on parcourt ses voisins
			for( Arc arc : voisins ) {
				// On cherche qui est le voisin dans l'arc
				if( arc.origine() == sommetActuel ) {
					voisin = arc.destination();
				} else {
					voisin = arc.origine();
				}

				if( !distances.containsKey( voisin ) ) {
					// Si le voisin n'est pas encore marqué, on l'ajoute en signalant sa distance
					distances.put( voisin, distance );

					if( destinationsRestantes.contains( voisin ) ) {
						// Si le voisin est un des addresses, alors nous n'avons plus besoin de rechercher cette adresse
						destinationsRestantes.remove( voisin );
					}
					else{
						//  On ajoute le voisin au prochain niveau si ce n'est pas une destination
						// Cela permet de ne pas avoir de plus court chemin passant par une autre adresse
						prochaineNiveau.add( voisin );
					}
				}
			}
		}

		calculerLesDistances( graphe, distances, prochaineNiveau, destinationsRestantes, distance+1 );
	}






}