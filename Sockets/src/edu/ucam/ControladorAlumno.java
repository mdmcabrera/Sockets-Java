package edu.ucam;

import java.io.Serializable;
import java.util.HashMap;

// Clase que controla los metodos a realizar para los objetos de tipo Alumno
public class ControladorAlumno implements Serializable {
	// ATRIBUTOS
	private HashMap<Integer, Alumno> tablaAlumnos; // Declaracion de una tabla que contendra objetos del tipo Alumno

	// CONSTRUCTOR
	public ControladorAlumno() {
		this.tablaAlumnos = new HashMap<Integer, Alumno>();
	}

	// FUNCIONALIDAD

	// Metodo para incluir a un objeto del tipo Alumno en la tabla de alumnos
	public void addAlumno(Alumno alumno) {
		this.tablaAlumnos.put(alumno.getDni(), alumno);

	}

	// Metodo para eliminar un alumno de la tabla en funcion de su dni
	public void deleteAlumno(int dni) {
		this.tablaAlumnos.remove(dni);
	}

	// Metodo para actualizar el alumno que se le pasa como parametro en la tabla de
	// alumnos
	public void updateAlumno(Alumno alumno) {
		this.tablaAlumnos.replace(alumno.getDni(), alumno);

	}

	// Metodo para obtener un alumno en funcion de su dni de la tabla de alumnos
	public Alumno getAlumno(int dni) {
		return this.tablaAlumnos.get(dni);
	}

	// Metodo para contar el numero de alumnos de la tabla
	public int countAlumno() {
		return this.tablaAlumnos.size();
	}

	// Metodo para comprobar si un alumno existe en la tabla de alumnos creada
	public boolean checkAlumno(int id) {
		if (tablaAlumnos.containsKey(id))
			return true;
		else
			return false;

	}

	// Metodo para obtener la tabla de alumnos
	public HashMap<Integer, Alumno> getTablaAlumnos() {

		return tablaAlumnos;
	}
}
