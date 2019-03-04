package edu.ucam;

import java.io.Serializable;
import java.util.HashMap;

// Clase que controla los metodos a realizar para los objetos de tipo Curso 

public class ControladorCurso implements Serializable {
	// ATRIBUTOS
	private HashMap<Integer, Curso> tablaCursos; // Declaracion de tabla de cursos de tipo HashMap manejando como clave
													// el identificador del curso y como valor un objeto de tipo Curso

	// CONSTRUCTOR
	public ControladorCurso() {
		this.tablaCursos = new HashMap<Integer, Curso>();
	}

	// Metodo para introducir un curso en la tabla de cursos definida
	public void addCurso(Curso curso) {
		this.tablaCursos.put(curso.getId(), curso);

	}

	// Metodo para eliminar un curso en funcion de su id en la tabla de cursos
	public void deleteCurso(int id) {
		this.tablaCursos.remove(id);
	}

	// Metodo para actualizar un cuso de la tabla
	public void updateCurso(Curso curso) {
		this.tablaCursos.replace(curso.getId(), curso);

	}

	// Metodo para obtener un curso en funcion de su id
	public Curso getCurso(int id) {
		return this.tablaCursos.get(id);
	}

	// Metodo que devuelve el numero de cursos de la tabla
	public int countCurso() {
		return this.tablaCursos.size();
	}

	// Metodo para comprobar si un curso ha sido introducido en la tabla previamente
	// y esta disponible
	public boolean checkCurso(int id) {
		if (tablaCursos.containsKey(id))
			return true;
		else
			return false;

	}

	// Metodo para obtener la tabla de cursos
	public HashMap<Integer, Curso> getTablaCursos() {
		return tablaCursos;
	}

}
