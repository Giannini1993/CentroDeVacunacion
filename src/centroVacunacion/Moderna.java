package centroVacunacion;

public class Moderna extends Pfizer{

	public Moderna(String nombre, Fecha fechIngr) {
		super(nombre, fechIngr);
	}
	
	@Override
	public boolean vencimiento() {
		Fecha vencimiento = new Fecha(getFechaIngreso().dia(),getFechaIngreso().mes(),getFechaIngreso().anio());
		vencimiento.avanzarDias(60);
		return Fecha.hoy().posterior(vencimiento)/* || Fecha.hoy().equals(vencimiento)*/;
	}

}
