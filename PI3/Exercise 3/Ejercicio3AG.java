package ejercicio3;

import java.util.List;

import us.lsi.ag.ValuesInRangeData;
import us.lsi.ag.agchromosomes.ChromosomeFactory.ChromosomeType;

public class Ejercicio3AG implements ValuesInRangeData<Integer, SolucionFestival> {
	public Ejercicio3AG (String file) {
		DatosFestival.iniDatos(file);
	}
	
	public SolucionFestival solucion(List<Integer> ls) {
		return SolucionFestival.create(ls);
	}
	
	public ChromosomeType type () {
		return ChromosomeType.Range;
	}
	
	public Integer size () {
		return DatosFestival.getNumTiposEntrada() * DatosFestival.getNumAreas();
	}
	
	public Integer max (Integer i) {
		Integer curArea = i % DatosFestival.getNumAreas();
		
		return DatosFestival.getAforoMaximoArea(curArea) + 1;
	}
	
	public Integer min (Integer i) {
		return 0;
	}
	
	public Double fitnessFunction (List<Integer> ls) {
		Double goal = 0.;
		Double error = 0.;

		Double sumOfArea = 0.;
		Double sumOfType = 0.;
		
		Integer numTypes = DatosFestival.getNumTiposEntrada();
		Integer numAreas = DatosFestival.getNumAreas();
		
		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i) > 0) {
				Integer curType = i / numAreas;
				Integer curArea = i % numAreas;
				
				goal += ls.get(i) * DatosFestival.getCosteAsignacion(curType, curArea);
			}
		}
		
		// Restriction: Number of sold tickets per area cannot exceed maximum capacity of areas
		for (int area = 0; area < numAreas; area++) {
			sumOfArea = 0.;
			
			for (int type = 0; type < numTypes; type++) {
				Integer curType = area * numTypes + type;
				
				sumOfArea += ls.get(curType);
			}
			
			if (sumOfArea > DatosFestival.getAforoMaximoArea(area)) {
				error++;
			}
		}
		
		// Restriction: Number of sold tickets per type has to exceed minimum quota
		for (int type = 0; type < numTypes; type++) {
			sumOfType = 0.;
			
			for (int area = 0; area < numAreas; area++) {
				Integer curType = area * numTypes + type;
				
				sumOfType += ls.get(curType);
			}
			
			if (sumOfType < DatosFestival.getCuotaMinima(type)) {
				error++;
			}
		}
		
		return - goal - 10000 * error;
	}
}
