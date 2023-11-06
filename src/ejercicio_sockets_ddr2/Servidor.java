package ejercicio_sockets_ddr2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el servidor de chat.
 */
public class Servidor {
    static List<ClienteHandler> clientes = new ArrayList<>();

    /**
     * Método principal para iniciar el servidor y manejar conexiones de clientes.
     *
     * @param args Argumentos de línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Servidor iniciado. Esperando conexiones...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket);

                // Crea un nuevo hilo para manejar la comunicación con el cliente
                ClienteHandler clienteHandler = new ClienteHandler(clientSocket);
                clientes.add(clienteHandler);
                Thread thread = new Thread(clienteHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para enviar un mensaje a todos los clientes excepto al remitente.
     *
     * @param message Mensaje a transmitir.
     * @param sender  Cliente que envía el mensaje.
     */
    public static void broadcastMessage(String message, ClienteHandler sender) {
        for (ClienteHandler cliente : clientes) {
            if (cliente != sender) {
                cliente.sendMessage(sender.getUsername() + ": " + message);
            }
        }
    }
}

/**
 * Clase que representa un manejador de cliente que se ejecuta en un hilo separado.
 */
class ClienteHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private String username;

    /**
     * Constructor para crear un nuevo manejador de cliente.
     *
     * @param clientSocket Socket del cliente.
     */
    public ClienteHandler(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            output.println("Ingrese su nombre de usuario:");
            this.username = input.readLine();
            output.println("Bienvenido al chat, " + this.username + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el nombre de usuario del cliente.
     *
     * @return Nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Envía un mensaje al cliente.
     *
     * @param message Mensaje a enviar.
     */
    public void sendMessage(String message) {
        output.println(message);
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = input.readLine()) != null) {
                Servidor.broadcastMessage(message, this);
            }

            System.out.println("Cliente desconectado: " + clientSocket);
            output.println("El usuario ha abandonado el chat.");
            Servidor.clientes.remove(this);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
