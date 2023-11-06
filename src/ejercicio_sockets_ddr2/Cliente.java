package ejercicio_sockets_ddr2;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Clase que representa un cliente de chat que se conecta a un servidor.
 */
public class Cliente {
    /**
     * Método principal que inicia la aplicación del cliente.
     *
     * @param args Argumentos de línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        try {
            // Pedir al usuario que ingrese la dirección IP del servidor
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese la dirección IP del servidor: ");
            String serverIP = scanner.nextLine();

            Socket clientSocket = new Socket(serverIP, 1234);
            System.out.println("Conectado al servidor: " + clientSocket);

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            // Leer el mensaje de bienvenida del servidor
            String welcomeMessage = input.readLine();
            System.out.println(welcomeMessage);

            // Pedir al usuario que ingrese su nombre de usuario
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese su nombre de usuario: ");
            String username = userInput.readLine();
            output.println(username);

            // Crear un hilo para recibir mensajes del servidor
            Thread serverReaderThread = new Thread(new ServerReader(input));
            serverReaderThread.start();

            // Interacción con el servidor
            String userMessage;
            while (true) {
                userMessage = userInput.readLine();
                output.println(userMessage); // Enviar el mensaje al servidor
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Clase que representa un hilo para leer y mostrar mensajes del servidor.
 */
class ServerReader implements Runnable {
    private BufferedReader input;

    /**
     * Constructor para crear un hilo ServerReader.
     *
     * @param input BufferedReader utilizado para leer mensajes del servidor.
     */
    public ServerReader(BufferedReader input) {
        this.input = input;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = input.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
