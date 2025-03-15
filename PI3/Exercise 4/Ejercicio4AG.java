package ejercicio4;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import us.lsi.ag.SeqNormalData;
import us.lsi.ag.agchromosomes.ChromosomeFactory.ChromosomeType;

public class Ejercicio4AG implements SeqNormalData<SolucionEstaciones> {
	public Ejercicio4AG (String file) {
		DatosEstaciones.iniDatos(file);
	}
	
	public ChromosomeType type () {
		return ChromosomeType.Permutation;
	}
	
	public SolucionEstaciones solucion(List<Integer> ls) {
		return SolucionEstaciones.create(ls);
	}
	
	public Integer itemsNumber () {
		return DatosEstaciones.numEstaciones();
	}
	
	public Double fitnessFunction (List<Integer> ls) {
		Double goal = 0.;
		Double error = 0.;
		
		Integer numStations = DatosEstaciones.numEstaciones();
		
		// Default values
		Integer station = 0;
		Integer nextStation = 0;
		
		for (int i = 0; i < (ls.size() - 1); i++) {
			station = ls.get(i);
			nextStation = ls.get(i + 1);
			
			if (DatosEstaciones.estanConectados(station, nextStation)) {
				goal += DatosEstaciones.tiempoTramo(station, nextStation);
			}	
		}
		
		// Add loop to first station
		station = ls.get(ls.size() - 1);
		nextStation = ls.get(0);
		
		if (DatosEstaciones.estanConectados(station, nextStation)) {
			goal += DatosEstaciones.tiempoTramo(station, nextStation);
		}
		
		// Extra Restricion: Pairwise adjacent
		for (int i = 0; i < (numStations - 1); i++) {
			station = ls.get(i);
			nextStation = ls.get(i + 1);
			
			if (!DatosEstaciones.estanConectados(station, nextStation)) {
				error += 1000;
			}
		}
		
		// Add loop to first station
		station = ls.get(ls.size() - 1);
		nextStation = ls.get(0);
		
		if (!DatosEstaciones.estanConectados(station, nextStation)) {
			error += 1000;
		}
		
		// Restriction: Pass through each station only once
		Set<Integer> visitedStations = new HashSet<>();
		
		for (int i = 0; i < numStations; i++) {
			station = ls.get(i);
			
			if (!visitedStations.add(station)) {
				error++;
			}
		}
		
		// Restriction: Selected section costs cannot exceed 3 / 4 of the totalCost
		Double totalCost = DatosEstaciones.costeTotal();
		Double selectedCost = 0.;
		
		for (int i = 0; i < (ls.size() - 1); i++) {
			station = ls.get(i);
			nextStation = ls.get(i + 1);
			
			if (DatosEstaciones.estanConectados(station, nextStation)) {
				selectedCost += DatosEstaciones.costeTramo(station, nextStation);
			}
		}
		
		// Add loop to first station
		station = ls.get(ls.size() - 1);
		nextStation = ls.get(0);
		
		if (DatosEstaciones.estanConectados(station, nextStation)) {
			selectedCost += DatosEstaciones.costeTramo(station, nextStation);
		}
		
		if (selectedCost > ((3 / 4) * totalCost)) {
			error++;
		}
		
		// Restriction: There must be at least two adjacent stations with a satisfaction greater or equal than 7
		Boolean passesSatisfaction = false;
		
		for (int i = 0; i < (ls.size() - 1); i++) {
			station = ls.get(i);
			nextStation = ls.get(i + 1);
			
			if (DatosEstaciones.estanConectados(station, nextStation)) {
				if (DatosEstaciones.satisfaccionClientes(station) >= 7
						&& DatosEstaciones.satisfaccionClientes(nextStation) >= 7) {
					passesSatisfaction = true;
					break;
				}
			}
		}

		// Add loop to first station
		station = ls.get(ls.size() - 1);
		nextStation = ls.get(0);
		
		if (DatosEstaciones.estanConectados(station, nextStation)) {
			if (DatosEstaciones.satisfaccionClientes(station) >= 7
					&& DatosEstaciones.satisfaccionClientes(nextStation) >= 7) {
				passesSatisfaction = true;
			}
		}
		
		if (!passesSatisfaction) {
			error++;
		}
		
		return - goal - 10000 * error;
	}
}
