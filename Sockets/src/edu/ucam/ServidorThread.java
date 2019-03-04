package edu.ucam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

// Clase para implementar funcionalidad principal del servidor en modo multicliente. Hereda de la clase Thread
public class ServidorThread extends Thread {
	// ATRIBUTOS
	private Socket socket;

	private ControladorAlumno alumnControl;
	private ControladorUsuario usuarioControl;
	private ControladorCurso cursoControl;

	private boolean isLogged = false;
	private EstadoServidor estadoServidor = null;
	private Usuario usuarioNuevo = null;
	public static boolean controlComando = false;

	// CONSTRUCTOR
	// Se introducen los usuarios autorizados y sus passwords

	public ServidorThread(Socket socket, ControladorAlumno controladorAlumno, ControladorCurso controladorCurso) {
		this.socket = socket;

		this.usuarioControl = new ControladorUsuario();
		this.alumnControl = controladorAlumno;
		this.cursoControl = controladorCurso;

		Usuario usuario1 = new Usuario();
		usuario1.setId(48660030);
		usuario1.setPass("1");
		usuarioControl.addUsuario(usuario1);

		Usuario usuario2 = new Usuario();
		usuario2.setId(23307168);
		usuario2.setPass("2");
		usuarioControl.addUsuario(usuario2);
	}

	// FUNCIONALIDAD
	public void run() {

		BufferedReader bufferedReader; // Creo bufferedreader
		try {

			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			estadoServidor = EstadoServidor.ESPERANDO_USER; // Estado Esperando

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())); // Buffer Escritura

			while (true) {

				String line = bufferedReader.readLine(); // Leemos array procedente de cliente
				System.out.println("Contenido orden: " + line);
				String[] array = line.split(" "); // Se parte el array
				String num = array[0]; // Posicion 0 contiene numero de peticion
				String comando = array[1]; // Posicion 1 contiene el comando

				if (comando.toUpperCase().equals("EXIT")) { // Control para comando EXIT
					processExit(num, socket);
				}

				else if (this.comprobarComando(comando.toUpperCase())) { // Si el comando es valido
					if (estadoServidor == EstadoServidor.ESPERANDO_USER) { // Si el servidor esta esperando comando USER

						processUser(num, Integer.parseInt(array[2]), socket); // posicion 2 procedente de cliente es el
																				// id de usuario

					} else if ((estadoServidor == EstadoServidor.ESPERANDO_PASS) // comprobacion de PASS correcta
							&& comando.toUpperCase().equals("PASS")) {

						processPassword(num, array[2], socket);

					}

					else if (estadoServidor == EstadoServidor.ESPERANDO_COMANDO) {
						if (isLogged) {
							switch (comando.toUpperCase()) {
							case "ADDALU":
								processAddAlumno(num, socket);
								break;
							case "UPDATEALU":
								processUpdateAlumno(num, socket, Integer.parseInt(array[2]));
								break;
							case "GETALU":
								processGetAlumno(num, socket, Integer.parseInt(array[2]));
								break;

							case "REMOVEALU":
								processRemoveAlumno(num, socket, Integer.parseInt(array[2]));
								break;

							case "LISTALU":
								processListAlumno(num, socket);
								break;

							case "COUNTALU":
								processCountAlumno(num, socket);
								break;

							case "ADDCURSO":
								processAddCurso(num, socket);
								break;

							case "UPDATECURSO":
								processUpdateCurso(num, socket, Integer.parseInt(array[2]));
								break;
							case "GETCURSO":
								processGetCurso(num, socket, Integer.parseInt(array[2]));
								break;

							case "REMOVECURSO":
								processRemoveCurso(num, socket, Integer.parseInt(array[2]));
								break;

							case "LISTCURSO":
								processListCurso(num, socket);
								break;

							case "COUNTCURSO":
								processCountCurso(num, socket);
								break;
							case "EXIT":
								processExit(num, socket);
								break;
							default:

								break;
							}
						}

						else
							System.out.println("FAILED " + num + " NO AUTORIZADO");
					}
				} else {
					System.out.println("Comando incorrecto");
					pw.println("Comando incorrecto, introduzca uno valido");
					pw.flush();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Funcion para procesar usuario
	private void processUser(String num, int usuario, Socket socket) throws UnknownHostException, IOException {
		Usuario usuarioTemp = this.usuarioControl.findUsuario(usuario); // usuarioTemp = usuario introducido en sistema

		PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true); // buffer escritura

		if (usuarioTemp != null) { // Usuario temporal existe
			pw.println("Ok " + num + " Envie password");
			estadoServidor = EstadoServidor.ESPERANDO_PASS;
			usuarioNuevo = usuarioTemp; // la variable usuarioNuevo ahora vale usuarioTemp
		} else { // UsuarioTemporal no existe
			pw.println("FAILED " + num + "Not USER");
			estadoServidor = EstadoServidor.ESPERANDO_USER; // espera el comando USER valido
		}
	}

	// Procesar password
	private void processPassword(String num, String password, Socket socket) throws UnknownHostException, IOException {

		PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true); // Buffer escritura
		if (usuarioNuevo.getPass().equals(password)) { // Si la pass del usuario coincide con la almacenada en servidor
			isLogged = true; // variable por defecto iniciada a false pasa a true
			pw.println("Ok " + num + " Welcome ");

			estadoServidor = EstadoServidor.ESPERANDO_COMANDO; // Volvemos a pedir Comando

		} else {
			isLogged = false;
			pw.println("FAILED " + num + "prueba de nuevo");
			estadoServidor = EstadoServidor.ESPERANDO_PASS;

		}
		System.out.println("");
	}

	// Metodo para comprobar si el comando que el usuario ha introducido es correcto
	private boolean comprobarComando(String comando) {
		switch (comando) {
		case "USER":
			return true;
		case "PASS":
			return true;
		case "ADDALU":
			return true;
		case "UPDATEALU":
			return true;
		case "GETALU":
			return true;
		case "REMOVEALU":
			return true;
		case "LISTALU":
			return true;
		case "COUNTALU":
			return true;
		case "ADDCURSO":
			return true;
		case "UPDATECURSO":
			return true; // LO METEMOS EXTRA
		case "GETCURSO":
			return true;
		case "REMOVECURSO":
			return true;
		case "LISTCURSO":
			return true;
		case "COUNTCURSO":
			return true;
		case "EXIT":
			return true;
		default:
			return false;
		}
	}

	// Metodo que implementa la funcionalidad a realizar por el comando ADDALU
	private void processAddAlumno(String num, Socket socket) {
		try {

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			int puerto = Cliente.port_data++;
			pw.println("OK " + num + " " + "127.0.0.1 " + puerto); // se envia al cliente, conformacion de ip y puerto
			pw.flush();

			ServerSocket serverSocketObjeto = new ServerSocket(puerto);
			Socket socketObjeto = serverSocketObjeto.accept();
			System.out.println("Conexion socket datos realizada");

			ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());
			Alumno alumno;
			try {
				alumno = (Alumno) ios.readObject(); // lee objeto que llega del cliente

				alumnControl.addAlumno(alumno); // Se introduce el alumno en la tabla
				socketObjeto.close(); // Se cierra el Socket
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	// Metodo para implementar la funcionalidad del comando UPDATEALU en el servidor
	private void processUpdateAlumno(String num, Socket socket, int id) {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			if (alumnControl.checkAlumno(id)) { // Comprueba si el alumno existe en la tabla de alumnos creada

				int puerto = Cliente.port_data++;
				pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
				pw.flush();

				ServerSocket serverSocketObjeto = new ServerSocket(puerto);
				Socket socketObjeto = serverSocketObjeto.accept(); // Comunicacion canal de datos establecida
				System.out.println("Conexion socket datos realizada");

				ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());
				Alumno alumno;
				try {

					alumno = (Alumno) ios.readObject(); // Se lee el objeto recibido a traves del canal de datos

					alumnControl.updateAlumno(alumno);

					socketObjeto.close(); // Se cierra el socket
				} catch (ClassNotFoundException e) {

					e.printStackTrace();
				}
			} else { // Si el usuario no esta en la tabla, se manda por el canal de comandos la orden
						// FAILED
				pw.println(" FAILED " + num + " NO AUTORIZADO");
				pw.flush();

			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// Metodo que implementa la funcionalidad a realizar del comando GETALU
	private void processGetAlumno(String num, Socket socket, int id) {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			if (alumnControl.checkAlumno(id)) { // Se comprueba que el alumno se encuentre en la tabla de alumnos

				int puerto = Cliente.port_data++;
				pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
				pw.flush();

				ServerSocket serverSocketObjeto = new ServerSocket(puerto);
				Socket socketObjeto = serverSocketObjeto.accept(); // Conexion canal de datos
				System.out.println("Conexion socket datos realizada");

				ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());
				oos.writeObject(alumnControl.getAlumno(id)); // Se manda el objeto Alumno por el canal de datos hacia el
																// cliente
				oos.flush();
				socketObjeto.close();

			} else {
				pw.println(" FAILED " + num + "NO AUTORIZADO"); // Si la tabla no contiene al alumno solicitado por
																// comando, se envia al cliente un FAILED
				pw.flush();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// Metodo para implementar funcionalidad del comando LISTALU obtenido a traves
	// del cliente
	private void processListAlumno(String num, Socket socket) {
		try {

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			int puerto = Cliente.port_data++;
			pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
			pw.flush();

			ServerSocket serverSocketObjeto = new ServerSocket(puerto);
			Socket socketObjeto = serverSocketObjeto.accept();
			System.out.println("Conexion socket datos realizada");

			ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());

			oos.writeObject(alumnControl.getTablaAlumnos()); // Recibe listado de alumnos
			oos.flush();
			socketObjeto.close(); // Se cierra el socket de datos

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// Metodo para implementar funcionalidad del comando COUNTALU obtenido a traves
	// del cliente
	private void processCountAlumno(String num, Socket socket) {
		try {

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			int puerto = Cliente.port_data++;
			pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
			pw.flush();

			ServerSocket serverSocketObjeto = new ServerSocket(puerto);
			Socket socketObjeto = serverSocketObjeto.accept();
			System.out.println("Conexion socket datos realizada");

			ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());
			oos.writeObject(alumnControl.countAlumno()); // Se recibe el numero de alumnos y se envia al cliente por el
															// socket de datos
			oos.flush();

			socketObjeto.close(); // Se cierra el socket de datos

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	// Metodo para implementar funcionalidad del comando REMOVEALU obtenido a traves
	// del cliente
	private void processRemoveAlumno(String num, Socket socket, int id) {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			if (alumnControl.checkAlumno(id)) {

				int puerto = Cliente.port_data++;
				pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
				pw.flush();

				ServerSocket serverSocketObjeto = new ServerSocket(puerto);
				Socket socketObjeto = serverSocketObjeto.accept();
				System.out.println("Conexion socket datos realizada");

				alumnControl.deleteAlumno(id);

				socketObjeto.close();

			} else {
				pw.println(" FAILED " + num + " NO AUTORIZADO");
				pw.flush();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// FUNCIONALIDAD CURSO

	// Metodo para implementar la funcionalidad del comando ADDCURSO recibido a
	// traves del cliente
	private void processAddCurso(String num, Socket socket) {
		try {

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			int puerto = Cliente.port_data++;
			pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
			pw.flush();

			ServerSocket serverSocketObjeto = new ServerSocket(puerto);
			Socket socketObjeto = serverSocketObjeto.accept();
			System.out.println("Conexion socket datos realizada");

			// LEER OBJETO ALUMNO

			ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());
			Curso curso;
			try {

				curso = (Curso) ios.readObject();

				cursoControl.addCurso(curso);
				socketObjeto.close(); // Se cierra el socket de datos
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	// Metodo para implementar funcionalidad del comando UPDATECURSO obtenido del
	// cliente a traves del canal de comandos
	private void processUpdateCurso(String num, Socket socket, int id) {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			if (cursoControl.checkCurso(id)) { // Se comprueba que el curso que se intenta modificar existe en la tabla

				int puerto = Cliente.port_data++;
				pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
				pw.flush();

				ServerSocket serverSocketObjeto = new ServerSocket(puerto);
				Socket socketObjeto = serverSocketObjeto.accept(); // Se establece comunicacion con cliente mediante
																	// canal de datos
				System.out.println("Conexion socket datos realizada");

				ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());
				Curso curso;
				try {

					curso = (Curso) ios.readObject();

					cursoControl.updateCurso(curso);

					socketObjeto.close(); // Se cierra el socket de objetos
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				pw.println(" FAILED " + num + " NO AUTORIZADO");
				pw.flush();

			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// Metodo para implementar funcionalidad del comando LISTCURSO recibido a traves
	// del cliente por el canal de comandos
	private void processListCurso(String num, Socket socket) {
		try {

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			int puerto = Cliente.port_data++;
			pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
			pw.flush();

			ServerSocket serverSocketObjeto = new ServerSocket(puerto);
			Socket socketObjeto = serverSocketObjeto.accept();
			System.out.println("Conexion socket datos realizada");

			ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());
			oos.writeObject(cursoControl.getTablaCursos());
			oos.flush();
			socketObjeto.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// Metodo para implementar funcionalidad del comando GETCURSO recibido a traves
	// del canal de comandos procedente del cliente
	private void processGetCurso(String num, Socket socket, int id) {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			if (cursoControl.checkCurso(id)) {

				int puerto = Cliente.port_data++;
				pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
				pw.flush();

				ServerSocket serverSocketObjeto = new ServerSocket(puerto);
				Socket socketObjeto = serverSocketObjeto.accept();
				System.out.println("Conexion socket datos realizada");

				ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());
				oos.writeObject(cursoControl.getCurso(id));
				oos.flush();
				socketObjeto.close(); // Se cierra el socket de datos

			} else {
				pw.println(" FAILED " + num + " NO AUTORIZADO");
				pw.flush();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// Metodo que implemnta funcionalidad del comando COUNTCURSO recibido a traves
	// del canal de comandos, procedente del cliente
	private void processCountCurso(String num, Socket socket) {
		try {

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			int puerto = Cliente.port_data++;
			pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
			pw.flush();

			ServerSocket serverSocketObjeto = new ServerSocket(puerto);
			Socket socketObjeto = serverSocketObjeto.accept();
			System.out.println("Conexion socket datos realizada");

			ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());
			oos.writeObject(cursoControl.countCurso());
			oos.flush();

			socketObjeto.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	// Metodo que implementa la funcionalidad del comando REMOVECURSO recibido desde el cliente a traves del canal de comandos
	private void processRemoveCurso(String num, Socket socket, int id) {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			if (cursoControl.checkCurso(id)) {

				int puerto = Cliente.port_data++;
				pw.println("OK " + num + " " + "127.0.0.1 " + puerto);
				pw.flush();

				ServerSocket serverSocketObjeto = new ServerSocket(puerto);
				Socket socketObjeto = serverSocketObjeto.accept();
				System.out.println("Conexion socket datos realizada");

				cursoControl.deleteCurso(id);

				socketObjeto.close();

			} else {
				pw.println(" FAILED " + num + " NO AUTORIZADO");
				pw.flush();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// Metodo para implementar funcionalidad del comando EXIT, que finaliza la ejecucion de la aplicacion
	private void processExit(String num, Socket socket) {

		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			
			pw.println("OK " + num + " BYE BYE");
			pw.flush();
			
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
