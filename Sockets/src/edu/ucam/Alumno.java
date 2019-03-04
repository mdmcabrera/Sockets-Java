package edu.ucam;

import java.io.Serializable;

// Clase para definir el objeto Alumno, debe implementar la interfaz Serializable
// para poder mandarse el objeto a traves del canal de datos
public class Alumno implements Serializable {
	// ATRIBUTOS
	private final int dni;
	private String nombre;
	private String curso;

	// CONSTRUCTOR
	public Alumno(int dni, String nombre, String curso) {
		this.dni = dni;
		this.nombre = nombre;
		this.curso = curso;
	}

	// METODOS OBTENCION
	public int getDni() {
		return dni;
	}

	public String getNombre() {
		return nombre;
	}

	public String getCurso() {
		return curso;
	}

	// Redefinicion del metodo toString para mostrarlo con ese formado al listar los alumnos
	@Override
	public String toString() {
		return "Alumno: dni = " + dni + "  nombre = " + nombre + "  curso = " + curso + " ";
	}

}
