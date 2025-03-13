package ejercicio3;

import java.io.IOException;
import java.util.Locale;

import us.lsi.gurobi.GurobiLp;
import us.lsi.gurobi.GurobiSolution;
import us.lsi.solve.AuxGrammar;

public class Ejercicio3PLE {
    
    public static SolucionFestival solucion(String file) throws IOException {
    		DatosFestival.iniDatos("resources/ejercicio3/" + file);

		AuxGrammar.generate(DatosFestival.class,"models/exercise3.lsi","gurobi_models/exercise3.lp");
		GurobiSolution solution = GurobiLp.gurobi("gurobi_models/exercise3.lp");
		Locale.setDefault(Locale.of("en", "US"));
		System.out.println(solution.toString((s,d)->d>0.));
		
		return null;	
	}
	
	public static void main (String[] args) throws IOException {
		solucion("DatosEntrada1.txt");
		// solucion("DatosEntrada2.txt");
		// solucion("DatosEntrada3.txt");
	}
}
