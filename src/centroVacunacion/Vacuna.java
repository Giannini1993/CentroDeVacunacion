package centroVacunacion;

public abstract class Vacuna implements Comparable<Vacuna>{
	protected Integer temperatura;
	private String nombre;
	private Fecha fechaIngreso;
	
	public Vacuna(String nombre, Fecha fechaIngreso) {
		this.nombre = nombre;
		this.fechaIngreso = fechaIngreso;
	}
	
	

	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Integer getTemperatura() {
		return temperatura;
	}



	public Fecha getFechaIngreso() {
		return fechaIngreso;
	}



	public void setFechaIngreso(Fecha fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}



	@Override
	public String toString() {
		return "Vacuna [temperatura=" + temperatura + ", nombre=" + nombre + ", fechaIngreso=" + fechaIngreso + "]";
	}


	
	@Override
	public boolean equals(Object otro) {
		if(otro==null) {
			return false;
		}
		if(!(otro instanceof Vacuna)) {
			return false;
		}
		Vacuna aux = (Vacuna) otro;
		return temperatura.equals(aux.getTemperatura()) && nombre.equals(aux.getNombre()) && fechaIngreso.equals(aux.fechaIngreso);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaIngreso == null) ? 0 : fechaIngreso.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((temperatura == null) ? 0 : temperatura.hashCode());
		return result;
	}



	@Override
	public int compareTo(Vacuna otra) {
		return nombre.compareTo(otra.getNombre());
	}
//	
	

}
