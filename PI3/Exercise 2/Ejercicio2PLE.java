package ejercicio2;

import java.io.IOException;
import java.util.Locale;

import us.lsi.gurobi.GurobiLp;
import us.lsi.gurobi.GurobiSolution;
import us.lsi.solve.AuxGrammar;

public class Ejercicio2PLE {
    
    public static SolucionCursos solucion(String file) throws IOException {
		DatosCursos.iniDatos("resources/ejercicio2/" + file);

		AuxGrammar.generate(DatosCursos.class,"models/exercise2.lsi","gurobi_models/exercise2.lp");
		GurobiSolution solution = GurobiLp.gurobi("gurobi_models/exercise2.lp");
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
