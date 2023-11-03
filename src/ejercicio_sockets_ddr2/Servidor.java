package ejercicio_sockets_ddr2;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Clase que representa el servidor de chat que acepta conexiones de clientes.
 */
public class Servidor {
    private static final int PORT = 12345;
    private static List<Socket> clientSockets = new ArrayList<>();
    private static Map<Socket, String> connectedClients = new HashMap<>();

    /**
     * Método principal que inicia el servidor y acepta conexiones de clientes.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este ejemplo).
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT, 0, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Servidor en línea. Esperando conexiones...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clase interna que maneja la comunicación con un cliente específico.
     */
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println("Bienvenido al chat. Por favor, introduce tu nombre de usuario:");
                username = in.readLine();
                connectedClients.put(clientSocket, username);
                broadcast(username + " se ha unido al chat.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("chao")) {
                        break;
                    }
                    broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSockets.remove(clientSocket);
                    connectedClients.remove(clientSocket);
                    broadcast(username + " ha abandonado el chat.");
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Método para enviar un mensaje a todos los clientes conectados.
         *
         * @param message El mensaje a enviar.
         */
        private void broadcast(String message) {
            for (Socket socket : clientSockets) {
                if (socket != clientSocket) {
                    try {
                        PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
                        socketOut.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


