package Servidor;

import java.net.Socket;

/**
 *
 * @author Silvia
 */
public class HiloCliente extends Thread {

    private String nombre;
    private Socket cliente;

    public HiloCliente(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {

    }

}
