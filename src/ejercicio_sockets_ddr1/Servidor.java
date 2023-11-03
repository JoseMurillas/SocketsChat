package ejercicio_sockets_ddr1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    public static void main (String[] args) {

        ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in;
        DataOutputStream out;
        final int PUERTO = 5000;

        try {
            servidor  = new ServerSocket(PUERTO);
            System.out.println("ejercicio_sockets_ddr1.Servidor iniciado");

            while(true){

                sc = servidor.accept();


                System.out.println("ejercicio_sockets_ddr1.Cliente Conectado");
                in = new DataInputStream(sc.getInputStream()); //recibir los mensajes del cliente
                out = new DataOutputStream(sc.getOutputStream()); //

                String mensaje = in.readUTF();

                System.out.println(mensaje);

                out.writeUTF("Hola Mundo desde el servidor");

                sc.close();
                System.out.println("ejercicio_sockets_ddr1.Cliente desconectado");

            }

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

}
