package centroVacunacion;

public class Pfizer extends Vacuna{

	public Pfizer(String nombre, Fecha fechaIngreso) {
		super(nombre, fechaIngreso);
		temperatura = -18;
	}
	
	public boolean vencimiento() {
		Fecha vencimiento = new Fecha(getFechaIngreso().dia(),getFechaIngreso().mes(),getFechaIngreso().anio());
		vencimiento.avanzarDias(30);
		return Fecha.hoy().posterior(vencimiento) /*|| Fecha.hoy().equals(vencimiento)*/;
	}

}
