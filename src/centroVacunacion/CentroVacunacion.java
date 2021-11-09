package centroVacunacion;

import java.util.*;

public class CentroVacunacion {
	private String nombreCentro;
	private Integer capacidadVacunacionDiaria;
	Integer capacidadHoy ;
	private Prioridad prioridad;

	private List<Vacuna> registroDeVacunasIngresadas;
	protected Map<Vacuna, Integer> vacunas;
	private Map<String, Integer> vacunasVencidas;
	private Map<Integer, String> personasVacunadas;
	protected HashMap<Fecha, List<Turno>> turnosPorFecha;
	

	public CentroVacunacion(String nombre, Integer capacidadDiaria) {
		if (nombre.length() != 0) {
			this.nombreCentro = nombre;
		} else {
			throw new RuntimeException("Debe asignarle un nombre.");
		}

		if (capacidadDiaria > 0) {
			this.capacidadVacunacionDiaria = capacidadDiaria;
			capacidadHoy = new Integer(capacidadDiaria);
		} else {
			throw new RuntimeException("la capacidad diaria no pueder ser menor a 1.");
		}
		prioridad = new Prioridad();
		registroDeVacunasIngresadas = new ArrayList<>();
		vacunas = new HashMap<>();
		vacunasVencidas = new HashMap<>();
		personasVacunadas = new HashMap<>();
		turnosPorFecha = new HashMap<>();
		
		
	}
	

	public void ingresarVacunas(String nombreVacuna, int cantidad, Fecha fechaIngreso) {
		if(cantidad > 0) {
			Vacuna nueva = tipoVacuna(nombreVacuna, fechaIngreso);
			if (!vacunas.containsKey(nueva)) {
				vacunas.put(nueva, cantidad);
				registroDeVacunasIngresadas.add(nueva);
			} 
			 else {
				int cantVacunas = vacunas.get(nueva) + cantidad;
				vacunas.put(nueva, cantVacunas);
			}
		}
		else {
			throw new RuntimeException("Permitió ingresar una vacuna con cantidad 0");
		}
	}

	private Vacuna tipoVacuna(String nombre, Fecha fechIngr) {
		if (nombre.equals("Pfizer")) {
			return new Pfizer("Pfizer", fechIngr);
		}
		if (nombre.equals("Sputnik")) {
			return new Sputnik("Sputnik", fechIngr);
		}
		if (nombre.equals("Sinopharm")) {
			return new Sinopharm("Sinopharm", fechIngr);
		}
		if (nombre.equals("Moderna")) {
			return new Moderna("Moderna", fechIngr);
		}
		if (nombre.equals("AstraZeneca")) {
			return new AstraZeneca("AstraZeneca", fechIngr);
		}
		throw new RuntimeException("Vacuna no valida.");
	}

	public int vacunasDisponibles() {
		int cont = 0;
		for (Integer n : vacunas.values()) {
			cont += n;
		}
		return cont;
	}

	public int vacunasDisponibles(String nombeVacuna) {
		int cont = 0;
		for (Vacuna v : vacunas.keySet()) {
			if (v.getNombre().equals(nombeVacuna)) {
				cont += vacunas.get(v);
			}
		}
		return cont;
	}

	public void inscribirPersona(int dni, Fecha nacimiento, boolean esEmpleadoSalud, boolean tienePadecimientos) {
		prioridad.agregarPersona(dni, nacimiento, esEmpleadoSalud, tienePadecimientos);
	}

	public void generarTurnos(Fecha fechaInicial) {
		 quitarTurnosVencidos();
		 quitarVacunasVencidas();
		if(!Fecha.hoy().posterior(fechaInicial)) {
			asignarTurnoTrabajadoresSalud(fechaInicial);
			asignarTurnoMayoresDeSesenta(fechaInicial);
			asignarTurnoConPadecimiento(fechaInicial);
			asignarTurnoSinPrioridad(fechaInicial);
			if(listaDeEspera().size()>=1 && vacunasDisponibles()>0) {
				Fecha fechaSiguiente = new Fecha(fechaInicial.dia(),fechaInicial.mes(),fechaInicial.anio());
				fechaSiguiente.avanzarUnDia();
				capacidadVacunacionDiaria = capacidadHoy;
				generarTurnos(fechaSiguiente);
			}
		}
		else {
			throw new RuntimeException("No se puede generar turno para una fecha pasada.");
		}
	}
	
	

	private Vacuna obtenerVacuna(String nombre) {
		for (Vacuna v : registroDeVacunasIngresadas) {
			if (nombre.equals(v.getNombre()) && vacunas.get(v)>0) {
				return v;
			}
		}
		return null;
	}

	private void restarVacuna(Vacuna v) {
		if (vacunas.get(v) != null && vacunas.get(v)!=0) {
			int cantVacunas = vacunas.get(v) - 1;
			vacunas.put(v, cantVacunas);
		}
	}
	
	private void asignarTurnoTrabajadoresSalud(Fecha fechaInicial) {
		List<Turno> turnos;
		Persona pers;
		Turno turn;
		Vacuna vac;
			if(turnosPorFecha.get(fechaInicial) == null) {
				turnos = new ArrayList<>();
			}
			else {
				turnos = turnosPorFecha.get(fechaInicial);
			}
			Iterator<Persona> it = prioridad.getTrabajadorasEnSalud().iterator();
			while(it.hasNext() && capacidadVacunacionDiaria > 0){
			pers = it.next();
				if(pers.mayorDeSesenta()) {
					if (vacunasDisponibles("Sputnik") > 0) {
						vac = obtenerVacuna("Sputnik"); 
					} else if (vacunasDisponibles("Pfizer") > 0) {
						vac = obtenerVacuna("Pfizer");
					} else {
						throw new RuntimeException("No hay vacunas disponibles.");
					}
					if (!pers.isturnoAsignado() && vac != null) {
						pers.setturnoAsignado(true);
						turn = new Turno(pers, vac);
						restarVacuna(vac);
						it.remove();
						turnos.add(turn);
						capacidadVacunacionDiaria--;
						}
				}
				else {
					if (vacunasDisponibles("Moderna") > 0) {
						vac = obtenerVacuna("Moderna"); 
					} else if (vacunasDisponibles("AstraZeneca") > 0) {
						vac = obtenerVacuna("AstraZeneca");
					} else if (vacunasDisponibles("Sinopharm") > 0) {
						vac = obtenerVacuna("Sinopharm");
					}
					else {
						throw new RuntimeException("No hay vacunas disponibles.");
					}
					if (!pers.isturnoAsignado() && vac != null) {
						pers.setturnoAsignado(true);
						turn = new Turno(pers, vac);
						restarVacuna(vac);
						it.remove();
						turnos.add(turn);
						capacidadVacunacionDiaria--;
					}
				}
			}
			turnosPorFecha.put(fechaInicial, turnos);
	}
	
	private void asignarTurnoMayoresDeSesenta(Fecha fechaInicial) {
		List<Turno>turnos;
		Persona pers;
		Turno turn;
		Vacuna vac;
			if(turnosPorFecha.get(fechaInicial) == null) {
				turnos = new ArrayList<>();
			}
			else {
				turnos = turnosPorFecha.get(fechaInicial);
			}
			Iterator<Persona> it = prioridad.getMayoresDeSesenta().iterator();
			while(it.hasNext() && capacidadVacunacionDiaria > 0){
				pers = it.next();
				if (vacunasDisponibles("Sputnik") > 0) { 
					vac = obtenerVacuna("Sputnik"); 
				} else if (vacunasDisponibles("Pfizer") > 0) {
					vac = obtenerVacuna("Pfizer");
				} 
				else {
					throw new RuntimeException("No hay vacunas disponibles.");
				}
				if (!pers.isturnoAsignado() && vac != null) {
					pers.setturnoAsignado(true);
					turn = new Turno(pers, vac);
					restarVacuna(vac);
					it.remove();
					turnos.add(turn);
					capacidadVacunacionDiaria--;
				}
			}
			turnosPorFecha.put(fechaInicial, turnos);
	}
	
	private void asignarTurnoConPadecimiento(Fecha fechaInicial) {
		List<Turno>turnos;
		Persona pers;
		Turno turn;
		Vacuna vac;
			if(turnosPorFecha.get(fechaInicial) == null) {
				turnos = new ArrayList<>();
			}
			else {
				turnos = turnosPorFecha.get(fechaInicial);
			}
			Iterator<Persona> it = prioridad.getConPadecimiento().iterator();
			while(it.hasNext() && capacidadVacunacionDiaria > 0){
				pers = it.next();
				if (vacunasDisponibles("Moderna") > 0) {
					vac = obtenerVacuna("Moderna"); 
				} else if (vacunasDisponibles("AstraZeneca") > 0) {
					vac = obtenerVacuna("AstraZeneca");
				} else if (vacunasDisponibles("Sinopharm") > 0) {
					vac = obtenerVacuna("Sinopharm");
				} else {
					throw new RuntimeException("No hay vacunas disponibles.");
				}
				if (!pers.isturnoAsignado() && vac != null) {
					pers.setturnoAsignado(true);
					turn = new Turno(pers, vac);
					restarVacuna(vac);
					it.remove();
					turnos.add(turn);
					capacidadVacunacionDiaria--;
				}
			}
			turnosPorFecha.put(fechaInicial, turnos);
	}
	
	private void asignarTurnoSinPrioridad(Fecha fechaInicial) {
		List<Turno>turnos;
		Persona pers;
		Turno turn;
		Vacuna vac;
			if(turnosPorFecha.get(fechaInicial) == null) {
				turnos = new ArrayList<>();
			}
			else {
				turnos = turnosPorFecha.get(fechaInicial);
			}
			Iterator<Persona> it = prioridad.getSinPrioridad().iterator();
			while(it.hasNext() && capacidadVacunacionDiaria > 0){
				pers = it.next();
				if (vacunasDisponibles("Moderna") > 0) {
					vac = obtenerVacuna("Moderna"); 
				} else if (vacunasDisponibles("AstraZeneca") > 0) {
					vac = obtenerVacuna("AstraZeneca");
				} else if (vacunasDisponibles("Sinopharm") > 0) {
					vac = obtenerVacuna("Sinopharm");
				} else {
					throw new RuntimeException("No hay vacunas disponibles.");
				}
				if (!pers.isturnoAsignado() && vac != null) {
					pers.setturnoAsignado(true);
					turn = new Turno(pers, vac);
					restarVacuna(vac);
					it.remove();
					turnos.add(turn);	
					capacidadVacunacionDiaria--;
				}
			}
			turnosPorFecha.put(fechaInicial, turnos);
	}
	
	
	public List<Integer> turnosConFecha(Fecha fecha){
		ArrayList<Integer> listaDNIPersonas = new ArrayList<>();
		for(Fecha f : turnosPorFecha.keySet()) {
			if(f.equals(fecha)) {
				agregarDNI(listaDNIPersonas, turnosPorFecha.get(f));
			}
//			else {
//				throw new RuntimeException("No hay turnos asignados para la fecha ingresada.");
//			}
		}
		return listaDNIPersonas;
	}
	
	private void agregarDNI(List<Integer>listaDNI, List<Turno>turnos) {
		for(Turno t : turnos) {
			listaDNI.add(t.getPersona().getDni());
		}
	}
	
	
	public void vacunarInscripto(Integer dni, Fecha fechaVacunacion) {
		if(turnosPorFecha.containsKey(fechaVacunacion)) {
			Iterator<Turno> it = turnosPorFecha.get(fechaVacunacion).iterator();
			while(it.hasNext()) {
				Turno turn = it.next();
				Integer dniPers = turn.getPersona().getDni();
				if(dniPers.equals(dni)) {
					personasVacunadas.put(dniPers, turn.getVacuna().getNombre());
					it.remove();
				}
			}
		}
		else {
			throw new RuntimeException("No hay turnos para la fecha ingresada.");
		}
	}
	
	public Map<Integer, String> reporteVacunacion(){
		return personasVacunadas;
	}
	
	public Map<String, Integer> reporteVacunasVencidas(){
		return vacunasVencidas;
	}
	
	public List<Integer> listaDeEspera(){
		return prioridad.listaEspera();
	}
	
	public void quitarTurnosVencidos() {
		Iterator<Fecha> it = turnosPorFecha.keySet().iterator();
		while(it.hasNext()) {
			Fecha fecha = it.next();
			if(Fecha.hoy().posterior(fecha)) {
				recuperarVacuna(turnosPorFecha.get(fecha));
				it.remove();
			}
		}
	}
	
	private void recuperarVacuna(List<Turno>turnos) {
		for(Turno t: turnos) {
			Vacuna vac = t.getVacuna();
			if(vacunas.containsKey(vac)) {
				Integer cant = vacunas.get(vac) + 1;
				vacunas.put(vac, cant);
			}
			else {
				vacunas.put(vac, 1);
			}	
		}
	}
	
	
	public void quitarVacunasVencidas() {
		Vacuna vac;
		Iterator<Vacuna > it = vacunas.keySet().iterator();
		while(it.hasNext()) {
			vac = it.next();
			if(vac instanceof Moderna) {
				Moderna m= (Moderna) vac;
				if(m.vencimiento()) {
					if(!vacunasVencidas.containsKey(vac)) {
						vacunasVencidas.put(vac.getNombre(), vacunas.get(vac));
						it.remove();
					}
					else {
						Integer cant = vacunas.get(vac)+ vacunasVencidas.get(vac.getNombre());
						vacunasVencidas.put(vac.getNombre(), cant);
						it.remove();
					}
				}
			}
			if(vac instanceof Pfizer) {
				Pfizer p= (Pfizer) vac;
				if(p.vencimiento()) {
					if(!vacunasVencidas.containsKey(vac)) {
						vacunasVencidas.put(vac.getNombre(), vacunas.get(vac));
						it.remove();
					}
					else {
						Integer cant = vacunas.get(vac)+ vacunasVencidas.get(vac.getNombre());
						vacunasVencidas.put(vac.getNombre(), cant);
						it.remove();
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return "CentroVacunacion [nombreCentro=" + nombreCentro + ", capacidadVacunacionDiaria="
				+ capacidadVacunacionDiaria + ", vacunas=" + vacunas + ", turnosConFecha= " + turnosPorFecha
				+ ", Prioridad= " + prioridad + ", Personas vacunadas=" + personasVacunadas + "]";
	}
	
	

}
