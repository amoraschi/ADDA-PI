package ejercicio4;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jgrapht.Graph;

import us.lsi.ag.agchromosomes.AlgoritmoAG;
import us.lsi.ag.agstopping.StoppingConditionFactory;
import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.views.SubGraphView;

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
		
		// Add last station
		Integer lastStation = ls.get(ls.size() - 1);
		
		this.camino.add(DatosEstaciones.getEstacion(lastStation));
		this.numEstaciones++;

		// Add first station for full loop
		Integer firstStation = ls.get(0);
		
		this.camino.add(DatosEstaciones.getEstacion(firstStation));
		this.tiempoTotal += DatosEstaciones.tiempoTramo(lastStation, firstStation);
		
		this.tiempoMedio = this.tiempoTotal / this.numEstaciones;
		
		// Thanks to @RodriguezLaraRafa for the idea
		Graph<Estacion, Tramo> graph = DatosEstaciones.getGrafo();
		
		List<Tramo> edges = IntStream
				.range(0, this.camino.size() - 1)
				.mapToObj(i -> graph.getEdge(this.camino.get(i), this.camino.get(i + 1)))
				.toList();
		
		Graph<Estacion, Tramo> subGraph = SubGraphView.of(
				graph,
				v -> this.camino.contains(v),
				e -> edges.contains(e)
				);
		
		// Generates each solution but only the last one gets saved because the others get overwritten (DatosEntrada3.txt)
		GraphColors.toDot(
				graph,
				"resources/salida/ejercicio4/graph.gv",
				x -> x.nombre() + " - " + this.camino.indexOf(x),
				x -> x.toString(),
				v -> GraphColors.colorIf(Color.red, subGraph.containsVertex(v)),
				e -> GraphColors.colorIf(Color.red, subGraph.containsEdge(e))
				);
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
