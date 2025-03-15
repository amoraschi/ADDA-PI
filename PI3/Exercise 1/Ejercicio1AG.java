package ejercicio1;

import java.util.List;

import us.lsi.ag.BinaryData;
import us.lsi.ag.agchromosomes.ChromosomeFactory.ChromosomeType;

public class Ejercicio1AG implements BinaryData<SolucionAlmacen> {
	public Ejercicio1AG (String file) {
		DatosAlmacenes.iniDatos(file);
	}
	
	public Integer size () {
		return DatosAlmacenes.getNumAlmacenes() * DatosAlmacenes.getNumProductos();
	}
	
	public ChromosomeType type () {
		return ChromosomeType.Binary;
	}
	
	public Double fitnessFunction (List<Integer> ls) {
		Double goal = 0.;
		Double error = 0.;

		Double singleRestriction = 0.;
		Double spaceRestriction = 0.;
		
		Integer numProducts = DatosAlmacenes.getNumProductos();
		Integer numWarehouses = DatosAlmacenes.getNumAlmacenes();
		
		// Goal Calculation
		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i) > 0) {
				goal += ls.get(i);
			}
		}
		
		// Single Restriction
		for (int i = 0; i < numProducts; i++) {
			singleRestriction = 0.;

			for (int j = 0; j < numWarehouses; j++) {
				Integer index = j * numProducts + i;
				
				if (ls.get(index) == 1) {
					singleRestriction++;
				}
			}
			
			if (singleRestriction > 1) {
				error += singleRestriction;
				// error++;
			}
		}
		
		// Space Restriction
		for (int j = 0; j < numWarehouses; j++) {
			spaceRestriction = 0.;
			
			for (int i = 0; i < numProducts; i++) {
				Integer index = j * numProducts + i;
				
				if (ls.get(index) == 1) {
					spaceRestriction += DatosAlmacenes.getMetrosCubicosProducto(i);
				}
			}
			
			if (spaceRestriction > DatosAlmacenes.getMetrosCubicosAlmacen(j)) {
				error += spaceRestriction;
				// error += spaceRestriction - DatosAlmacenes.getMetrosCubicosAlmacen(j);
				// error++;
			}
		}
		
		// Incompatibility Restriction
		for (int j = 0; j < numWarehouses; j++) {
			for (int i = 0; i < numProducts; i++) {
				Integer firstIndex = j * numProducts + i;
				
				if (ls.get(firstIndex) == 1) {
					for (int z = i + 1; z < numProducts; z++) {
						Integer secondIndex = j * numProducts + z;
						
						if (ls.get(secondIndex) == 1 &&
								DatosAlmacenes.esIncompatible(i, z) == 1) {
							error++;
						}
					}
				}
			}
		}
		
		return goal - 10000 * Math.pow(error, 2);
	}
	
	public SolucionAlmacen solucion(List<Integer> ls) {
		return SolucionAlmacen.create(ls);
	}
}
