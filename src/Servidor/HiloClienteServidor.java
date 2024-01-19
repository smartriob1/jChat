package Servidor;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Silvia
 */
public class HiloClienteServidor extends Thread {

    private String nombre;
    private Socket cliente;

    public HiloClienteServidor(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(cliente.getInputStream());
            nombre = dis.readUTF();
            System.out.println("Recibido cliente " + nombre + " con dirección " + cliente.getRemoteSocketAddress());
            if (!addCliente()) {

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
                //ENVIAR MENSAJE DE QUE NO SE PUDO ACEPTAR LA CONEXION
                return false;
            }
            ServidorChat.CONEXIONES_CLIENTES.add(this);
        }
        
        synchronized (ServidorChat.HISTORIAL_CLIENTES) {
                Integer conexiones = ServidorChat.HISTORIAL_CLIENTES.get(nombre);
                conexiones = conexiones == null ? 1 : ++conexiones;
                ServidorChat.HISTORIAL_CLIENTES.put(nombre, conexiones);
            }
            return true;
    }

}
