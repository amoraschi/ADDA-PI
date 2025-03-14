package ejercicio4;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import ejercicio3.Ejercicio3AG;
import ejercicio3.SolucionFestival;
import us.lsi.ag.agchromosomes.AlgoritmoAG;
import us.lsi.ag.agstopping.StoppingConditionFactory;

public class SolucionEstaciones {

    public static SolucionEstaciones create(List<Integer> ls) {
        return new SolucionEstaciones(ls);
    }
    
    private Integer numEstaciones;
    private List<Estacion> camino;
    private Double tiempoTotal;
    private Double tiempoMedio;

    private SolucionEstaciones () {
    		this.numEstaciones = 0;
    		this.camino = new ArrayList<>();
    		this.tiempoTotal = 0.;
    		this.tiempoMedio = 0.;
    }
    
    private SolucionEstaciones(List<Integer> ls) {
		this.numEstaciones = 0;
		this.camino = new ArrayList<>();
		this.tiempoTotal = 0.;
		this.tiempoMedio = 0.;

		for (int i = 0; i < (ls.size() - 1); i++) {
			Integer station = ls.get(i);
			Integer nextStation = ls.get(i + 1);
			
			this.numEstaciones++;
			this.camino.add(DatosEstaciones.getEstacion(station));
			this.tiempoTotal += DatosEstaciones.tiempoTramo(station, nextStation);
		}
		
		this.tiempoMedio = this.tiempoTotal / this.numEstaciones;
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
			System.out.println("Solving: " + file);
			Ejercicio4AG alg = new Ejercicio4AG("resources/ejercicio4/" + file);
			
			AlgoritmoAG<List<Integer>, SolucionEstaciones> ap = AlgoritmoAG.of(alg);
			
			ap.ejecuta();
			
			System.out.println("\n" + file + " ================================ " + file);
			System.out.println(ap.bestSolution());
			System.out.println(file + " ================================ " + file + "\n");
		}
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Resumen del recorrido:\n");

        result.append("Camino seguido: ").append(camino.stream()
                .map(Estacion::nombre)
                .collect(Collectors.joining(" -> "))).append("\n");

        result.append(String.format("Tiempo total: %.2f min\n", tiempoTotal));
        result.append(String.format("Tiempo medio por estación: %.2f min\n", tiempoMedio));

        return result.toString();
    }

    public Integer getNumEstaciones() {
        return numEstaciones;
    }

    public List<Estacion> getCamino() {
        return camino;
    }

    public Double getTiempoTotal() {
        return tiempoTotal;
    }

    public Double getTiempoMedio() {
        return tiempoMedio;
    }
}
