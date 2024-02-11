package Cliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hilo para enviar los mensajes al servidor.
 *
 * @author Silvia
 */
public class Emisor extends Thread {

    private DataOutputStream dos;

    public Emisor(DataOutputStream dos) {
        this.dos = dos;
    }

    @Override
    public void run() {
        try {
            Scanner sc = new Scanner(System.in);
            String mensaje = "";
            while (!mensaje.equalsIgnoreCase("#salir")) {
                mensaje = sc.nextLine();
                dos.writeUTF(mensaje);
            }
        } catch (IOException ex) {
            Logger.getLogger(Emisor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
