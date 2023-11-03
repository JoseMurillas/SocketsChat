package ejercicio_sockets_ddr2;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Clase que representa un cliente de chat que se conecta al servidor.
 */
public class Cliente {
    private static final String SERVER_IP = "Dirección_IP_pública_o_nombre_de_host"; // Reemplaza con la dirección IP pública o nombre de host válido
    private static final int SERVER_PORT = 12345;

    /**
     * Método principal que inicia el cliente y establece la conexión con el servidor.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este ejemplo).
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
            Scanner scanner = new Scanner(System.in);

            System.out.print("Introduce tu nombre de usuario: ");
            String username = scanner.nextLine();
            out.println(username);

            String message;
            while (true) {
                System.out.print("Mensaje: ");
                message = consoleIn.readLine();
                out.println(message);
                if (message.equalsIgnoreCase("chao")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

