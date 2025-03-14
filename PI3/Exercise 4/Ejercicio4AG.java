package ejercicio4;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
		Integer numSections = DatosEstaciones.numTramos();
		
		List<Integer> visitedStations = new ArrayList<>();
		
		for (int i = 0; i < (ls.size() - 1); i++) {
			Integer station = ls.get(i);
			Integer nextStation = ls.get(i + 1);
			
			if (!DatosEstaciones.estanConectados(station, nextStation)) {
				continue;
			}
			
			goal += DatosEstaciones.tiempoTramo(station, nextStation);
		}
		
		// Extra Restricion: Pairwise adjacent
		for (int i = 0; i < (numStations - 1); i++) {
			Integer station = ls.get(i);
			Integer nextStation = ls.get(i + 1);
			
			if (!DatosEstaciones.estanConectados(station, nextStation)) {
				error += 100;
			}
		}
		
		// Get visited stations
		for (int i = 0; i < numStations; i++) {
			Integer station = ls.get(i);
			
			visitedStations.add(station);
//			
//			if (visitedStations.contains(station)) {
//				error++;
//			} else {
//				visitedStations.add(station);
//			}
		}
		
		Set<Integer> visitedStationsSet = visitedStations
				.stream()
				.collect(Collectors.toSet());
		
		// Restriction: Pass through each station only once
		if (visitedStations.size() != visitedStationsSet.size()) {
			error++;
		}
		
		Double totalCost = DatosEstaciones.costeTotal();
		Double selectedCost = 0.;
		
		for (int i = 0; i < (ls.size() - 1); i++) {
			Integer station = ls.get(i);
			Integer nextStation = ls.get(i + 1);
			
			if (DatosEstaciones.estanConectados(station, nextStation)) {
				selectedCost += DatosEstaciones.costeTramo(station, nextStation);
			}
		}
		
		if (selectedCost > ((3 / 4) * totalCost)) {
			error++;
		}
		
		Double calculatedSatisfaction = 0.;
		
		for (int i = 0; i < (ls.size() - 1); i++) {
			Integer station = ls.get(i);
			Integer nextStation = ls.get(i + 1);
			
			if (DatosEstaciones.estanConectados(station, nextStation)) {
				calculatedSatisfaction += DatosEstaciones.satisfaccionClientes(station) +
						DatosEstaciones.satisfaccionClientes(nextStation);
			}
		}
		
		if (calculatedSatisfaction < 14) {
			error++;
		}
		
		return - goal - 10000 * error;
	}
}
