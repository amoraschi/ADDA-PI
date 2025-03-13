package ejercicio2;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import us.lsi.ag.BinaryData;
import us.lsi.ag.agchromosomes.ChromosomeFactory.ChromosomeType;

public class Ejercicio2AG implements BinaryData<SolucionCursos> {
	public Integer size () {
		return DatosCursos.getNumCursos();
	}
	
	public ChromosomeType type () {
		return ChromosomeType.Binary;
	}
	
	public Double fitnessFunction (List<Integer> ls) {
		Double goal = 0.;
		Double error = 0.;
		
		Double eachAreaRestriction = 0.;
		
		Integer numAreas = DatosCursos.getNumAreas();
		Integer numCourses = DatosCursos.getNumCursos();
		
		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i) > 0) {
				goal += ls.get(i);
			}
		}
		
		// Restriction: Each area has to be covered by a course
		for (int area = 0; area < numAreas; area++) {
			eachAreaRestriction = 0.;
			
			for (int course = 0; course < numCourses; course++) {
				if (DatosCursos.getArea(course) == area) {
					if (ls.get(course) == 1) {
						eachAreaRestriction++;
					}
				}
			}
			
			if (eachAreaRestriction < numAreas) {
				error++;
			}
		}
		
		Map<Integer, Integer> nonTechCoursesPerArea = new HashMap<>();
		
		for (int course = 0; course < numCourses; course++) {
			Integer area = DatosCursos.getArea(course);
			
			// Skip tech courses
			if (area == 0) {
				continue;
			}
			
			if (nonTechCoursesPerArea.containsKey(area)) {
				nonTechCoursesPerArea.put(area, nonTechCoursesPerArea.get(area) + 1);
			} else {
				nonTechCoursesPerArea.put(area, 0);
			}
		}
		
		// Max number of selected courses from a non-tech area
		Integer maxFromANonTechArea = nonTechCoursesPerArea
				.values()
				.stream()
				.max(Comparator.naturalOrder())
				.get();
		
		// Number of selected tech courses
		Integer numTechSelected = (int) IntStream
				.range(0, ls.size())
				.filter(i -> ls.get(i) == 1 && DatosCursos.getArea(i) == 0)
				.count();
		
		// Restriction: Number of selected tech courses has to be greater than the number of selected courses of each of the other areas
		if (numTechSelected < maxFromANonTechArea) {
			error++;
		}
		
		// Average duration of selected courses
		Double avgDuration = IntStream
				.range(0, ls.size())
				.filter(i -> ls.get(i) == 1)
				.mapToDouble(i -> DatosCursos.getDuracion(i))
				.sum() / ls.stream().filter(sel -> sel == 1).count();
		
		// Restriction: Average duration of selected courses has to be at least 20 hours
		if (avgDuration < 20) {
			error++;
		}
		
		Double totalCost = IntStream
				.range(0, ls.size())
				.filter(i -> ls.get(i) == 1)
				.mapToDouble(i -> DatosCursos.getCoste(i))
				.sum();
		
		// Restriction: Total cost cannot exceed total budget
		if (totalCost > DatosCursos.getPresupuestoTotal()) {
			error++;
		}
		
		return goal - 10000 * Math.pow(error, 2);
	}
	
	public Ejercicio2AG (String file) {
		DatosCursos.iniDatos(file);
	}

	public SolucionCursos solucion(List<Integer> ls) {
		return SolucionCursos.create(ls);
	}
}
