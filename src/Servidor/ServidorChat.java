package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Silvia
 */
public class ServidorChat {

    private static boolean encendido = false;
    private static ServerSocket server;
    private final static String ENCENDER_SERVIDOR = "java ServidorChat";

    public static List<HiloClienteServidor> CONEXIONES_CLIENTES;
    public static HashMap<String, Integer> HISTORIAL_CLIENTES;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String encender;
        do {
            System.out.print("> ");
            encender = sc.nextLine();
            if (!encender.trim().equalsIgnoreCase(ENCENDER_SERVIDOR)) {
                System.out.println("[ERROR] No existe el comando '" + encender + "'.");
            } else {
                encendido = true;
            }
        } while (!encender.trim().equalsIgnoreCase(ENCENDER_SERVIDOR));

        try {
            CONEXIONES_CLIENTES = new ArrayList();
            HISTORIAL_CLIENTES = new HashMap();

            server = new ServerSocket(Conexion.PUERTO());
            System.out.println("Servidor escuchando en " + server.getLocalSocketAddress());

            while (encendido) {
                Socket cliente = server.accept();
                HiloClienteServidor hiloSocket = new HiloClienteServidor(cliente);
                hiloSocket.start();
            }

        } catch (IOException ex) {
            //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Apagando...");
    }
}