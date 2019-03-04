package edu.ucam;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Cliente {
	public static int number = 0;
	public final static int PORT_COMMAND = 2016;
	public static int port_data = 2017;

	public static void main(String[] args) {

		Cliente cliente = new Cliente();

		cliente.startClient();

	}

	// FUNCIONALIDAD
	private void startClient() {
		try {
			Socket socket = new Socket("127.0.0.1", PORT_COMMAND);

			String msg = "";
			Scanner keyboard = new Scanner(System.in);
			PrintWriter printWriterC = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(" Bienvenido a la practica de Sockets\n");
			System.out.println(" Para empezar a usar la aplicación debe loguearse usando el comando USER");
			System.out.println(" Si USER es correcto, deberá meter la pass usando el comando PASS");
			while (true) {

				number++;
				System.out.println("Por favor, introduzca comando: ");
				msg = keyboard.nextLine();

				msg = number + " " + msg;

				printWriterC.println(msg); // Se envia al servidor la orden

				printWriterC.flush();

				String arrayCliente[] = msg.split(" ");
				String comando = arrayCliente[1];

				String lineaServidor = bufferedReader.readLine();
				System.out.println("Respuesta del servidor: " + lineaServidor); // Llega la respuesta con la ip y
																				// el puerto
				// Ejecucion de funciones dependiendo del comando introducido para manejar la
				// informacion recibida a traves del canal de comandos desde el servidor
				switch (comando.toUpperCase()) {
				case "ADDALU":
					handleAddAlumno(lineaServidor);
					break;
				case "UPDATEALU":
					int dni = Integer.parseInt(arrayCliente[2]);
					handleUpdateAlumno(lineaServidor, dni, socket);
					break;
				case "LISTALU":
					handleListAlumno(lineaServidor);
					break;

				case "COUNTALU":
					handleCountAlumno(lineaServidor);
					break;

				case "GETALU":
					int dni2 = Integer.parseInt(arrayCliente[2]);
					handleGetAlumno(lineaServidor, dni2, socket);
					break;

				case "REMOVEALU":
					int dni3 = Integer.parseInt(arrayCliente[2]);
					handleRemoveAlumno(lineaServidor, dni3, socket);
					break;

				case "ADDCURSO":
					handleAddCurso(lineaServidor);
					break;
				case "UPDATECURSO":
					int id = Integer.parseInt(arrayCliente[2]);
					handleUpdateCurso(lineaServidor, id, socket);
					break;
				case "LISTCURSO":
					handleListCurso(lineaServidor);
					break;

				case "COUNTCURSO":
					handleCountCurso(lineaServidor);
					break;

				case "GETCURSO":
					int id2 = Integer.parseInt(arrayCliente[2]);
					handleGetCurso(lineaServidor, id2, socket);
					break;

				case "REMOVECURSO":
					int id3 = Integer.parseInt(arrayCliente[2]);
					handleRemoveCurso(lineaServidor, id3, socket);
					break;
				case "EXIT":
					handleExit(lineaServidor, socket);
					break;
				default:
					break;
				}

			}

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// Manejo informacion obtenida del servidor para manejar comando ADDALU
	private void handleAddAlumno(String lineaServidor) throws UnknownHostException, IOException {
		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto e ip
		int puerto = Integer.parseInt(arrayServidor[3]);
		String ip = arrayServidor[2];
		Socket socketObjeto = new Socket(ip, puerto); // Creacion de socket para comunicacion de objetos
		System.out.println("Introduzca dni, nombre y curso separados por espacios: ");
		Scanner keyboardObjeto = new Scanner(System.in);
		String msgObjeto = keyboardObjeto.nextLine();
		String lineaObjeto[] = msgObjeto.split(" ");

		int dni = Integer.parseInt(lineaObjeto[0]);
		String nombre = lineaObjeto[1];
		String curso = lineaObjeto[2];
		Alumno alumno = new Alumno(dni, nombre, curso);

		ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());
		oos.writeObject(alumno);
		oos.flush();
		
		System.out.println("Alumno introducido correctamente");
	}

	// Manejo informacion obtenida del servidor para manejar comando UPDATEALU <id>
	private void handleUpdateAlumno(String lineaServidor, int dni, Socket socket)
			throws UnknownHostException, IOException {

		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		if (arrayServidor[0].equals("OK")) { // Si la respuesta que se recibe es un OK se procede a hacer update del
												// alumno

			int puerto = Integer.parseInt(arrayServidor[3]); // Convertimos la posicion array[3] en entero (puerto)
			String ip = arrayServidor[2]; // nuestra cadena de texto perteneciente a la ip array[2]
			Socket socketObjeto = new Socket(ip, puerto);
			System.out.println("Introduzca nombre y curso separados por espacios: ");
			Scanner keyboardObjeto = new Scanner(System.in);
			String msgObjeto = keyboardObjeto.nextLine();
			String lineaObjeto[] = msgObjeto.split(" ");

			String nombre = lineaObjeto[0];
			String curso = lineaObjeto[1];
			Alumno alumno = new Alumno(dni, nombre, curso);

			ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());
			oos.writeObject(alumno);
			oos.flush();
		} else
			System.out.println("alumno inexistente, por favor introduzca un id correcto "); // Si la informacion
																							// obtenida del servidor no
																							// es un OK, el alumno no
																							// existe en la tabla de
																							// alumnos

	}

	// Manejo informacion obtenida comando GETALU para su envio al servidor
	private void handleGetAlumno(String lineaServidor, int dni, Socket socket)
			throws UnknownHostException, IOException {

		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		if (arrayServidor[0].equals("OK")) {
			int puerto = Integer.parseInt(arrayServidor[3]); // Convertimos la posicion array[3] en entero (puerto)
			String ip = arrayServidor[2]; // nuestra cadena de texto perteneciente a la ip array[2]
			Socket socketObjeto = new Socket(ip, puerto);

			try {

				ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());

				Alumno alumno = (Alumno) ios.readObject();

				System.out.println(alumno);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			System.out.println("alumno no encontrado ");
		}

	}

	// Manejo informacion obtenida comando LISTALU para su envio al servidor
	private void handleListAlumno(String lineaServidor) throws UnknownHostException, IOException {
		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		int puerto = Integer.parseInt(arrayServidor[3]);
		String ip = arrayServidor[2];
		Socket socketObjeto = new Socket(ip, puerto);

		ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());
		HashMap<Integer, Alumno> tablaAlumnos; // creo objeto HashMap

		try {

			tablaAlumnos = (HashMap<Integer, Alumno>) ios.readObject(); // Guardo HashMap de Alumnos en tablaAlumnos y
																		// leo objeto
			if (tablaAlumnos.size() != 0) {

				for (Map.Entry<Integer, Alumno> entry : tablaAlumnos.entrySet()) { // recorre la tabla de alumnos y la
																					// muestra por pantalla
					System.out.println("Dni: " + entry.getKey() + " , " + entry.getValue());
				}
			} else {
				System.out.println("Lista de alumnos vacia, por favor introduzca uno mediante comando ADDALU");
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Manejo informacion obtenida comando COUNTALU para su envio al servidor
	private void handleCountAlumno(String lineaServidor) throws UnknownHostException, IOException {
		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		int puerto = Integer.parseInt(arrayServidor[3]);
		String ip = arrayServidor[2];
		Socket socketObjeto = new Socket(ip, puerto);

		ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());
		try {
			Integer numero = (Integer) ios.readObject();
			System.out.println("El numero de alumnos es: " + numero);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Manejo informacion obtenida comando REMOVEALU para su envio al servidor
	private void handleRemoveAlumno(String lineaServidor, int dni, Socket socket)
			throws UnknownHostException, IOException {

		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		if (arrayServidor[0].equals("OK")) { // si servidor devuelve OK en posicion 0 entra

			int puerto = Integer.parseInt(arrayServidor[3]); // Convertimos la posicion array[3] en entero (puerto)
			String ip = arrayServidor[2]; // nuestra cadena de texto perteneciente a la ip array[2]
			Socket socketObjeto = new Socket(ip, puerto);

			System.out.println("Usuario borrado correctamente");
		} else
			System.out.println("alumno inexistente, por favor introduzca un id correcto ");
	}

	// Manejo informacion obtenida comando ADDCURSO para su envio al servidor
	private void handleAddCurso(String lineaServidor) throws UnknownHostException, IOException {
		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		int puerto = Integer.parseInt(arrayServidor[3]);
		String ip = arrayServidor[2];
		Socket socketObjeto = new Socket(ip, puerto);
		System.out.println("Introduzca id del curso, nombre y descripcion separados por espacios: ");
		Scanner keyboardObjeto = new Scanner(System.in);
		String msgObjeto = keyboardObjeto.nextLine();
		String lineaObjeto[] = msgObjeto.split(" ");

		int id = Integer.parseInt(lineaObjeto[0]);
		String nombre = lineaObjeto[1];
		String descripcion = lineaObjeto[2];
		Curso curso = new Curso(id, nombre, descripcion);

		ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());
		oos.writeObject(curso);
		oos.flush();
		
		System.out.println("Curso introducido correctamente");
	}

	// Manejo informacion obtenida comando UPDATECURSO para su envio al servidor
	private void handleUpdateCurso(String lineaServidor, int id, Socket socket)
			throws UnknownHostException, IOException {

		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		if (arrayServidor[0].equals("OK")) {

			int puerto = Integer.parseInt(arrayServidor[3]); // Convertimos la posicion array[3] en entero (puerto)
			String ip = arrayServidor[2]; // nuestra cadena de texto perteneciente a la ip array[2]
			Socket socketObjeto = new Socket(ip, puerto);
			System.out.println("Introduzca nombre y descripcion separados por espacios: ");
			Scanner keyboardObjeto = new Scanner(System.in);
			String msgObjeto = keyboardObjeto.nextLine();
			String lineaObjeto[] = msgObjeto.split(" ");

			String nombre = lineaObjeto[0];
			String descripcion = lineaObjeto[1];
			Curso curso = new Curso(id, nombre, descripcion);

			ObjectOutputStream oos = new ObjectOutputStream(socketObjeto.getOutputStream());
			oos.writeObject(curso);
			oos.flush();
		} else
			System.out.println("Curso inexistente, por favor introduzca un id correcto ");

	}


	// Manejo informacion obtenida comando LISTCURSO para su envio al servidor
	private void handleListCurso(String lineaServidor) throws UnknownHostException, IOException {
		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		int puerto = Integer.parseInt(arrayServidor[3]);
		String ip = arrayServidor[2];
		Socket socketObjeto = new Socket(ip, puerto);

		ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());
		HashMap<Integer, Curso> tablaCursos;

		try {

			tablaCursos = (HashMap<Integer, Curso>) ios.readObject();

			if(tablaCursos.size() != 0) {
				
			
			for (Map.Entry<Integer, Curso> entry : tablaCursos.entrySet()) {
				System.out.println("ID: " + entry.getKey() + " , " + entry.getValue());
			}
			}
			else
				System.out.println("Lista de cursos vacia, por favor introduzca uno mediante comando ADDCURSO");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Manejo informacion obtenida comando GETCURSO para su envio al servidor
	private void handleGetCurso(String lineaServidor, int dni, Socket socket) throws UnknownHostException, IOException {

		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		if (arrayServidor[0].equals("OK")) {
			int puerto = Integer.parseInt(arrayServidor[3]); // Convertimos la posicion array[3] en entero (puerto)
			String ip = arrayServidor[2]; // nuestra cadena de texto perteneciente a la ip array[2]
			Socket socketObjeto = new Socket(ip, puerto);

			try {

				ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());

				Curso curso = (Curso) ios.readObject();

				System.out.println(curso);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println(" Curso no encontrado ");
		}

	}

	// Manejo informacion obtenida comando COUNTCURSO para su envio al servidor
	private void handleCountCurso(String lineaServidor) throws UnknownHostException, IOException {
		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		int puerto = Integer.parseInt(arrayServidor[3]);
		String ip = arrayServidor[2];
		Socket socketObjeto = new Socket(ip, puerto);

		ObjectInputStream ios = new ObjectInputStream(socketObjeto.getInputStream());
		try {
			Integer numero = (Integer) ios.readObject();
			System.out.println("El numero de cursos es: " + numero);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Manejo informacion obtenida comando REMOVECURSO para su envio al servidor
	private void handleRemoveCurso(String lineaServidor, int dni, Socket socket)
			throws UnknownHostException, IOException {

		String arrayServidor[] = lineaServidor.split(" "); // Se separa la linea en partes para obtener puerto
		if (arrayServidor[0].equals("OK")) {

			int puerto = Integer.parseInt(arrayServidor[3]); // Convertimos la posicion array[3] en entero (puerto)
			String ip = arrayServidor[2]; // nuestra cadena de texto perteneciente a la ip array[2]
			Socket socketObjeto = new Socket(ip, puerto);

			System.out.println("Curso borrado correctamente");
		} else
			System.out.println("curso inexistente, por favor introduzca un id correcto ");
	}

	// Manejo informacion obtenida comando EXIT para su envio al servidor
	private void handleExit(String lineaServidor, Socket socket) throws UnknownHostException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		System.out.println(br.readLine());
		
		 System.out.println("FIN PROGRAMA, HASTA PRONTO");
		System.exit(0);
		
	}
}
