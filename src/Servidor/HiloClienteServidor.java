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
        } catch (IOException ex) {
            //Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Cerrando cliente " + nombre);
        }
        System.out.println(this.nombre + " desconectado.");
    }

}
