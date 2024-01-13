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
    public static List<HiloCliente> CLIENTES_CONECTADOS;
    public static HashMap<String, Integer> HISTORICO_CONEXIONES;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String encender;
        do {
            System.out.print("> ");
            encender = sc.nextLine();
            if (!encender.trim().equalsIgnoreCase(ENCENDER_SERVIDOR)) {
                System.out.println("[ERROR] No existe ese comando");
            }else{
                encendido = true;
            }
        } while (!encender.trim().equalsIgnoreCase(ENCENDER_SERVIDOR));

        try {
            CLIENTES_CONECTADOS = new ArrayList();
            HISTORICO_CONEXIONES = new HashMap();

            server = new ServerSocket(Conexion.PUERTO());
            System.out.println("Servidor escuchando en " + server.getLocalSocketAddress());

            //Recibimos clientes mientras el servidor este "encendido".
            while (encendido) {
                Socket cliente = server.accept();
                //Abrimos un hilo para procesar cada cliente
                HiloCliente hiloSocket = new HiloCliente(cliente);
                hiloSocket.start();
            }

        } catch (IOException ex) {
            //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Apagando...");
    }
}
