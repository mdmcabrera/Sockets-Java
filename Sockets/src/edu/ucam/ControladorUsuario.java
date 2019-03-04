package edu.ucam;

import java.util.HashMap;

// Clase que implementa los metodos a realizar por los objetos de tipo Usuario

public class ControladorUsuario {
	// ATRIBUTOS
	private HashMap<Integer, Usuario> tablaUsuarios; 

	// CONSTRUCTOR
	public ControladorUsuario(){
		this.tablaUsuarios = new HashMap<Integer, Usuario>();
	}
	
	// Metodo para incluir un objeto de tipo Usuario en la tabla 
	public void addUsuario(Usuario usuario){
		this.tablaUsuarios.put(usuario.getId(), usuario);
		
	}
		
	// Metodo para encontrar y devolver un usuario en la tabla de usuarios
	public Usuario findUsuario(int id){
		return this.tablaUsuarios.get(id);
	}
	
}
