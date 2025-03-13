package ejercicio1;

import java.io.IOException;
import java.util.Locale;

import us.lsi.gurobi.GurobiLp;
import us.lsi.gurobi.GurobiSolution;
import us.lsi.solve.AuxGrammar;

public class Ejercicio1PLE {
	
	public static SolucionAlmacen solucion(String file) throws IOException {
		DatosAlmacenes.iniDatos("resources/ejercicio1/" + file);

		AuxGrammar.generate(DatosAlmacenes.class,"models/exercise1.lsi","gurobi_models/exercise1.lp");
		GurobiSolution solution = GurobiLp.gurobi("gurobi_models/exercise1.lp");
		Locale.setDefault(Locale.of("en", "US"));
		System.out.println(solution.toString((s,d)->d>0.));
		
		return null;	
	}
	
	public static void main (String[] args) throws IOException {
		solucion("DatosEntrada1.txt");
		solucion("DatosEntrada2.txt");
		solucion("DatosEntrada3.txt");
	}
}
