package edu.ucam;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
	ControladorAlumno controladorAlumno = new ControladorAlumno();
	ControladorCurso controladorCurso = new ControladorCurso();
	ArrayList<ServidorThread> arrayHilos = new ArrayList<ServidorThread>();

	public static void main(String[] args) {
		Servidor servidor = new Servidor();

		servidor.startServer();

	}

	private void startServer() {
		try {
			ServerSocket serverSocket = new ServerSocket(Cliente.PORT_COMMAND); // Se crea ServerSocket para el pueto de
																				// comandos

			while (true) {

				Socket socket = serverSocket.accept(); // Se abre el socket

				System.out.println("Conexion recibida");

				ServidorThread st = new ServidorThread(socket, controladorAlumno, controladorCurso); // Creacion de un
																										// hilo para
																										// implementacion
																										// multicliente
				st.start();
				arrayHilos.add(st); 
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
