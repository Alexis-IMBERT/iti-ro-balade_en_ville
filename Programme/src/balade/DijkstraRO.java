package balade;

import graphro.Arc;
import graphro.GrapheListe;
import graphro.Sommet;

import java.util.*;

/**
 * Calcule les distances entre un sommet source et les points d'intérêts POIs.
 * On utilise pour se faire un parcours en largeur puisque nous n'avont que des arcs de valeur 1.
 */
public class DijkstraRO {

	/**
	 * @param graphe le graphe
	 * @param source le sommet d'origine de l'algorithme
	 * @param destinations les destinations à atteindre
	 * @return un dictionnaire donnant la distance
	 */
	public static HashMap<Sommet,List<Sommet>> calculerCheminsAdressesDepuisSource( GrapheListe graphe, Sommet source, Set<Sommet> destinations ) {
		// Map de retour indiquant le chemin à parcourir pour aller du sommet source jusqu'à un sommet défini
		HashMap<Sommet,List<Sommet>> distanceDepuisSommet = new HashMap<>();
		distanceDepuisSommet.put( source, new ArrayList<>() );

		// On récupère la liste des sommets sans la source
		Collection<Sommet> sommets = graphe.sommets();
		sommets.remove( source );

		// Le sommet source doit être placé dans un ensemble pour coller à la signature de la fonction calculerLesDistances
		Set<Sommet> setSource = new HashSet<>();
		setSource.add(source);

		// On copie les destinations car la fonction calculerLesDistances est destructive
		Set<Sommet> copieDestinations = new HashSet<>(destinations);

		// On calcule le chemin pour aller à tous les points
		calculerLesDistances( graphe, distanceDepuisSommet, setSource, copieDestinations);

		// On ne retourne que les distances aux destinations
		HashMap<Sommet,List<Sommet>> cheminAddresse = new HashMap<>();

		for(Sommet destination : destinations){
			cheminAddresse.put( destination, distanceDepuisSommet.get( destination ) );
		}

		return cheminAddresse;
	}

	/**
	 *
	 * @param graphe le graphe
	 * @param routes les distances enregistrées
	 * @param niveauActuel les sommets du niveau actuel (parcourt en largeur)
	 * @param destinationsRestantes les destinations pas encore parcourues
	 */
	private static void calculerLesDistances( GrapheListe graphe, HashMap<Sommet,List<Sommet>> routes, Set<Sommet> niveauActuel, Set<Sommet> destinationsRestantes){
		// si on est déjà passé par toutes les destinations ou qu'il n'y a pas de sommet pour le niveau actuel, on s'arrête
		if(destinationsRestantes.size() == 0 || niveauActuel.size() == 0 ){
			return;
		}

		Set<Sommet> prochainNiveau = new HashSet<>();

		// On parcourt tous les sommets du niveau actuel
		for( Sommet sommetActuel : niveauActuel) {
			List<Arc> voisins = graphe.voisins( sommetActuel );
			Sommet voisin;
			// Pour chaque sommet du niveau actuel, on parcourt ses voisins
			for( Arc arc : voisins ) {
				// On cherche qui est le voisin dans l'arc
				if( arc.origine().equals( sommetActuel ) ) {
					voisin = arc.destination();
				} else {
					voisin = arc.origine();
				}

				if( !routes.containsKey( voisin ) ) {
					// Si le voisin n'est pas encore marqué, on ajoute son chemin
					List<Sommet> route = new ArrayList<>();
					route.addAll( routes.get( sommetActuel ) );
					route.add( voisin );
					routes.put( voisin, route );

					if( destinationsRestantes.contains( voisin ) ) {
						// Si le voisin est un des addresses, alors nous n'avons plus besoin de rechercher cette adresse
						destinationsRestantes.remove( voisin );
					}
					else{
						//  On ajoute le voisin au prochain niveau si ce n'est pas une destination
						// Cela permet de ne pas avoir de plus court chemin passant par une autre adresse
						prochainNiveau.add( voisin );
					}
				}
			}
		}

		// On passe au niveau suivant
		calculerLesDistances( graphe, routes, prochainNiveau, destinationsRestantes );
	}






}