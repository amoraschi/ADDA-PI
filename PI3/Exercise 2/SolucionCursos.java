package ejercicio2;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import us.lsi.ag.agchromosomes.AlgoritmoAG;
import us.lsi.ag.agstopping.StoppingConditionFactory;

public class SolucionCursos {

    public static SolucionCursos create(List<Integer> ls) {
        return new SolucionCursos(ls);
    }

    private Integer numCursos;
    private Map<Integer, Integer> solucion;
    private Double puntuacionTotal;
    private Integer costeTotal;

    private SolucionCursos(List<Integer> ls) {
        this.numCursos = 0;
        this.solucion = new HashMap<>();
        this.puntuacionTotal = 0.;
        this.costeTotal = 0;
        
        for (int i = 0; i < ls.size(); i++) {
        		Integer courseSelected = ls.get(i);
        	
    			if (courseSelected == 1) {
    				this.numCursos++;
    				this.solucion.put(i, 1);
    				this.puntuacionTotal += DatosCursos.getRelevancia(i);
    				this.costeTotal += DatosCursos.getCoste(i);
    			}
        }
    }
    
    public static void main (String[] args) {
    		Locale.setDefault(Locale.of("en", "US"));
    		
    		AlgoritmoAG.ELITISM_RATE = 0.10;
    		AlgoritmoAG.CROSSOVER_RATE = 0.95;
    		AlgoritmoAG.MUTATION_RATE = 0.8;
    		AlgoritmoAG.POPULATION_SIZE = 1000;
    		
    		StoppingConditionFactory.NUM_GENERATIONS = 10000;
    		StoppingConditionFactory.MAX_ELAPSEDTIME = 30;
    		
    		StoppingConditionFactory.stoppingConditionType = StoppingConditionFactory.StoppingConditionType.ElapsedTime;
    		
    		List<String> files = List.of(
    				"DatosEntrada1.txt",
    				"DatosEntrada2.txt",
    				"DatosEntrada3.txt");
    		
    		for (String file : files) {
    			Ejercicio2AG alg = new Ejercicio2AG("resources/ejercicio2/" + file);
    			
    			AlgoritmoAG<List<Integer>, SolucionCursos> ap = AlgoritmoAG.of(alg);
    			
    			ap.ejecuta();
    			
    			System.out.println("\n" + file + " ================================ " + file);
    			System.out.println(ap.bestSolution());
    			System.out.println(file + " ================================ " + file + "\n");
    		}
    }

    @Override
    public String toString() {
        return solucion.entrySet().stream()
                .map(p -> "Curso " + p.getKey() + ": Seleccionado")
                .collect(Collectors.joining("\n", "Cursos seleccionados:\n", String.format("\nTotal de cursos seleccionados: %d\nPuntuación total: %.2f\nCoste total: %d", numCursos, puntuacionTotal, costeTotal)));
    }

    public Integer getNumCursos() {
        return numCursos;
    }

    public Map<Integer, Integer> getSolucion() {
        return solucion;
    }

    public Double getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public Integer getCosteTotal() {
        return costeTotal;
    }
}
