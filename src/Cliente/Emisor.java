package Cliente;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Silvia
 */
public class Emisor extends Thread {

    private DataOutputStream dos;
    private boolean fin;

    public Emisor(DataOutputStream dos) {
        this.dos = dos;
        this.fin = false;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    @Override
    public void run() {
        try {
            Scanner sc = new Scanner(System.in);
            while (!fin) {
                //CORREGIR QUE SE QUEDA PILLADO EN EL SCANNER
                String mensaje = sc.nextLine();
                //Enviamos el comando
                dos.writeUTF(mensaje);
            }
        } catch (IOException ex) {
            Logger.getLogger(Emisor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
