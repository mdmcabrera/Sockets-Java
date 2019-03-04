package edu.ucam;

import java.io.Serializable;

// Clase que define el objeto Curso y que debe implementar la interfaz Serializable para poder
// ser enviado el objeto a traves del canal de datos
public class Curso implements Serializable {
	// ATRIBUTOS
	private final int id;
	private String nombre;
	private String descripcion;

	// CONSTRUCTOR
	public Curso(int id, String nombre, String descripcion) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	// METODOS DE OBTENCION
	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	// Metodo toString redefinido para mostrarlo con este formado al listar los
	// cursos de la tabla
	@Override
	public String toString() {
		return "Curso: id = " + id + "  nombre = " + nombre + "  descripcion = " + descripcion + " ";
	}

}
