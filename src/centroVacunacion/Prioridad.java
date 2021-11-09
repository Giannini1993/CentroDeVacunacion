package centroVacunacion;

import java.util.*;

public class Prioridad {
	private List<Persona>trabajadorasEnSalud;
	private List<Persona>mayoresDeSesenta;
	private List<Persona>conPadecimiento;
	private List<Persona>sinPrioridad;
	
	public Prioridad(){
		trabajadorasEnSalud = new ArrayList<>();
		mayoresDeSesenta = new ArrayList<>();
		conPadecimiento = new ArrayList<>();
		sinPrioridad = new ArrayList<>();
	}
	
	public void agregarPersona(int dni, Fecha nacimiento, boolean esEmpleadoSalud, boolean tienePadecimientos){
		Persona persona = new Persona(dni, nacimiento, esEmpleadoSalud, tienePadecimientos);
		if(!persona.mayorDeEdad()) {
			throw new RuntimeException("Solo se pueden inscribir personas mayores de 18.");
		}
		if(persona.isTrabajaEnSalud()) {
			if(!trabajadorasEnSalud.contains(persona)) {
				trabajadorasEnSalud.add(persona);
			}
			else {
				throw new RuntimeException("La persona ya se encuentra inscripta");
			}
		}
		else if(persona.mayorDeSesenta()) {
			if(!mayoresDeSesenta.contains(persona)) {
				mayoresDeSesenta.add(persona);
			}
			else {
				throw new RuntimeException("La persona ya se encuentra inscripta");
			}
		}
		else if(persona.isEnfermedadPreExistente()) {
			if(!conPadecimiento.contains(persona)) {
				conPadecimiento.add(persona);
			}
			else {
				throw new RuntimeException("La persona ya se encuentra inscripta");
			}
		}
		else {
			if(!sinPrioridad.contains(persona)) {
				sinPrioridad.add(persona);
			}
			else {
				throw new RuntimeException("La persona ya se encuentra inscripta");
			}
		}
	}
	
	

	public List<Persona> getTrabajadorasEnSalud() {
		return trabajadorasEnSalud;
	}

	public void setTrabajadorasEnSalud(List<Persona> trabajadorasEnSalud) {
		this.trabajadorasEnSalud = trabajadorasEnSalud;
	}

	public List<Persona> getMayoresDeSesenta() {
		return mayoresDeSesenta;
	}

	public void setMayoresDeSesenta(List<Persona> mayoresDeSesenta) {
		this.mayoresDeSesenta = mayoresDeSesenta;
	}

	public List<Persona> getConPadecimiento() {
		return conPadecimiento;
	}

	public void setConPadecimiento(List<Persona> conPadecimiento) {
		this.conPadecimiento = conPadecimiento;
	}

	public List<Persona> getSinPrioridad() {
		return sinPrioridad;
	}

	public void setSinPrioridad(List<Persona> sinPrioridad) {
		this.sinPrioridad = sinPrioridad;
	}
	
	public List<Integer> listaEspera(){
		ArrayList<Persona>listEsp = new ArrayList<>();
		ArrayList<Integer> DNIPersonasEnEspera = new ArrayList<>();
		listEsp.addAll(getTrabajadorasEnSalud());
		listEsp.addAll(getMayoresDeSesenta());
		listEsp.addAll(getConPadecimiento());
		listEsp.addAll(getSinPrioridad());
		for(Persona p : listEsp) {
			DNIPersonasEnEspera.add(p.getDni());
		}
		return DNIPersonasEnEspera;
	}
	

	@Override
	public String toString() {
		return "Prioridad [trabajadorasEnSalud=" + trabajadorasEnSalud + ", mayoresDeSesenta=" + mayoresDeSesenta
				+ ", conPadecimiento=" + conPadecimiento + ", sinPrioridad=" + sinPrioridad + "]";
	}
	
	

}
