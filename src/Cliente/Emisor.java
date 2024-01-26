/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;
import Servidor.Conexion;
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
    private Scanner sc;
    private String respuesta;

    public Emisor(DataOutputStream dos) {
        this.dos = dos;
        sc = new Scanner(System.in);
    }

    @Override
    public void run() {
        try {
            while (!Conexion.FIN_CLIENTE.equalsIgnoreCase(respuesta)) {
                String mensaje = sc.nextLine();
                //Enviamos el comando
                dos.writeUTF(mensaje);
            }
        } catch (IOException ex) {
            Logger.getLogger(Emisor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
