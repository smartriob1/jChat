package Servidor;

import Conexion.Conexion;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servidor del chat.
 *
 * @author Silvia
 */
public class ServidorChat {

    private static ServerSocket server;
    public static List<HiloCliente> CONEXIONES_CLIENTES;

    public static void main(String[] args) {
        try {
            CONEXIONES_CLIENTES = new ArrayList();
            server = new ServerSocket(Conexion.PUERTO());
            System.out.println("Servidor escuchando en " + server.getLocalSocketAddress());

            while (true) {
                Socket cliente = server.accept();
                HiloCliente hiloSocket = new HiloCliente(cliente);
                hiloSocket.start();
            }

        } catch (IOException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Apagando...");
    }
}
