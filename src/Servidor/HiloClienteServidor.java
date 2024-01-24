package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Silvia
 */
public class HiloClienteServidor extends Thread {

    private final static String[] COMANDOS = {"#ayuda", "#listar", "#charlar ", "#salir"};
    private String nombre;
    private Socket cliente;

    public HiloClienteServidor(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {
        DataInputStream dis = null;
        DataOutputStream out = null;
        StringBuilder sb = null;
        String mensaje;
        try {
            //Obtenemos el nic
            dis = new DataInputStream(cliente.getInputStream());
            nombre = dis.readUTF();
            System.out.println("Recibido cliente " + nombre + " con dirección " + cliente.getRemoteSocketAddress());
            if (addCliente()) {
                synchronized (this) {
                    out = new DataOutputStream(cliente.getOutputStream());
                    out.writeUTF("Estás conectado con el nic de " + nombre);
                }
            }

            while (true) {
                sb = new StringBuilder();
                mensaje = dis.readUTF();
                if (COMANDOS[3].equalsIgnoreCase(mensaje)) {
                    out = new DataOutputStream(cliente.getOutputStream());
                    out.writeUTF(Conexion.FIN_CLIENTE);
                    ServidorChat.CONEXIONES_CLIENTES.remove(nombre);
                    break;
                }
            }

        } catch (IOException ex) {
            //Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Cerrando cliente " + nombre);
        }
        System.out.println(this.nombre + " desconectado.");
    }

    private boolean addCliente() {
        synchronized (ServidorChat.CONEXIONES_CLIENTES) {
            if (ServidorChat.CONEXIONES_CLIENTES.contains(nombre)) {
                //rechazamos la conexion
                System.out.println("Rechazando conexion para " + nombre);
                try {
                    DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
                    out.writeUTF(Conexion.FIN_CLIENTE);
                } catch (IOException ex) {
                    Logger.getLogger(HiloClienteServidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            } else {
                ServidorChat.CONEXIONES_CLIENTES.add(nombre);
            }
        }

        synchronized (ServidorChat.HISTORIAL_CLIENTES) {
            Integer conexiones = ServidorChat.HISTORIAL_CLIENTES.get(nombre);
            conexiones = conexiones == null ? 1 : ++conexiones;
            ServidorChat.HISTORIAL_CLIENTES.put(nombre, conexiones);
        }
        return true;
    }

}
