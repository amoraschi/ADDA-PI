package ejercicio4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.SimpleWeightedGraph;

import us.lsi.common.String2;
import us.lsi.graphs.Graphs2;
import us.lsi.graphs.GraphsReader;

public class DatosEstaciones {
    private static List<Estacion> estaciones = new ArrayList<>();
    private static List<Tramo> tramos = new ArrayList<>();
    
    private static SimpleWeightedGraph<Estacion, Tramo> graph = Graphs2.simpleWeightedGraph();
    
	public static void iniDatos (String file) {
		SimpleWeightedGraph<Estacion, Tramo> g = GraphsReader.newGraph(
				file,
				Estacion::ofFormat,
				Tramo::ofFormat,
				Graphs2::simpleWeightedGraph
			);
		
		estaciones = g.vertexSet().stream().toList();
		tramos = g.edgeSet().stream().toList();
		graph = g;
    }
	
	public static Estacion getEstacion (Integer i) {
		return estaciones.get(i);
	}
	
	public static Integer numEstaciones () {
		return estaciones.size();
	}
	
	public static Integer numTramos () {
		return tramos.size();
	}
	
	public static Integer pasajerosDiarios (Integer i) {
		return estaciones.get(i).pasajerosDiarios();
	}
	
	public static Integer numEmpleados (Integer i) {
		return estaciones.get(i).numeroEmpleados();
	}
	
	public static Double satisfaccionClientes (Integer i) {
		return estaciones.get(i).satisfaccionClientes();
	}
	
	public static Double tiempoTramo (Integer i, Integer j) {
		return graph.getEdge(estaciones.get(i), estaciones.get(j)).tiempo();
	}
	
	public static Double costeTramo (Integer i, Integer j) {
		return graph.getEdge(estaciones.get(i), estaciones.get(j)).costeBillete();
	}
	
	public static Double costeTotal () {
		return graph.edgeSet().stream().mapToDouble(e -> e.costeBillete()).sum();
	}
	
	public static Boolean estanConectados (Integer i, Integer j) {
		return graph.containsEdge(estaciones.get(i), estaciones.get(j));
	}
	
    public static void toConsole() {
        String2.toConsole(estaciones, "Estaciones");
        String2.toConsole(tramos, "Tramos");
        String2.toConsole(String2.linea());
    }

    public static void main(String[] args) throws IOException {
        iniDatos("resources/ejercicio4/DatosEntrada1.txt");
    }
}
