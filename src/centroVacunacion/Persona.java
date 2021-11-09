package centroVacunacion;

public class Persona implements Comparable<Persona>{
	private Integer dni;
	private Fecha fechaNacimiento;
	private boolean trabajaEnSalud;
	private boolean enfermedadPreExistente;
	private boolean turnoAsignado;
	
	public Persona(Integer dni, Fecha fechaNacimiento, boolean enfermedadPreExistente, boolean trabajaEnSalud) {
		this.dni = dni;
		this.fechaNacimiento = fechaNacimiento;
		this.trabajaEnSalud = trabajaEnSalud;
		this.enfermedadPreExistente = enfermedadPreExistente;
	}
	
	public boolean mayorDeEdad() {
		int edad = Fecha.hoy().anio() - fechaNacimiento.anio();
		return edad >= 18;
	}
	
	public boolean mayorDeSesenta() {
		int edad = Fecha.hoy().anio() - fechaNacimiento.anio();
		return edad >= 60;
	}

	public Integer getDni() {
		return dni;
	}

	public void setDni(Integer dni) {
		this.dni = dni;
	}

	public Fecha getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Fecha fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public boolean isTrabajaEnSalud() {
		return trabajaEnSalud;
	}

	public void setTrabajaEnSalud(boolean trabajaEnSalud) {
		this.trabajaEnSalud = trabajaEnSalud;
	}

	public boolean isEnfermedadPreExistente() {
		return enfermedadPreExistente;
	}

	public void setEnfermedadPreExistente(boolean enfermedadPreExistente) {
		this.enfermedadPreExistente = enfermedadPreExistente;
	}
	
	public boolean isturnoAsignado() {
		return turnoAsignado;
	}
	
	public void setturnoAsignado(boolean turnoAsignado) {
		this.turnoAsignado = turnoAsignado;
	}
	
	 
	@Override
	public String toString() {
		return "Persona [dni=" + dni + ", fechaNacimiento=" + fechaNacimiento + ", trabajaEnSalud=" + trabajaEnSalud
				+ ", enfermedadPreExistente=" + enfermedadPreExistente + "]";
	}
	
	@Override
	public boolean equals(Object otra) {
		boolean ret = true;
		if(otra == null) {
			return false;
		}
		if(!(otra instanceof Persona)) {
			return false;
		}
		Persona otraPersona = (Persona) otra;
		ret = ret && dni.equals(otraPersona.dni);
		ret = ret && fechaNacimiento.equals(otraPersona.fechaNacimiento);
		ret = ret && trabajaEnSalud == otraPersona.trabajaEnSalud;
		ret = ret && enfermedadPreExistente == otraPersona.enfermedadPreExistente;
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
		result = prime * result + (enfermedadPreExistente ? 1231 : 1237);
		result = prime * result + ((fechaNacimiento == null) ? 0 : fechaNacimiento.hashCode());
		result = prime * result + (trabajaEnSalud ? 1231 : 1237);
		return result;
	}
	
	@Override
	public int compareTo(Persona otra) {
		return dni.compareTo(otra.dni);
	}
	
	
}
