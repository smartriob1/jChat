package Servidor;

import Cliente.ClienteChat;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase maneja las propiedades de la conexión.
 *
 * @author Samuel
 */
public class Conexion {

    private Integer puerto;
    private String servidor;
    private static Conexion singleton = null;
    public final static String FIN_CLIENTE = "fin_cliente ";
    public final static String FIN = "fin";
    public final static String SHUTDOWN = "shutdown";

    private Conexion() {
        puerto = 9;
        servidor = "localhost";
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("opciones.conf"));
            puerto = Integer.valueOf(props.getProperty("PUERTO"));
            servidor = props.getProperty(ClienteChat.IP, "localhost");
        } catch (IOException| NumberFormatException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Devuelve el puerto por el que escucha el servidor.
     * @return puerto del servidor
     */
    public static Integer PUERTO() {
        return getConexion().puerto;
    }

    /**
     * Devuelve la dirección ip o el host en el que escucha el servidor.
     * @return host del servidor.
     */
    public static String SERVIDOR() {
        return getConexion().servidor;
    }

    private static Conexion getConexion() {
        if (singleton == null) {
            singleton = new Conexion();
        }
        return singleton;
    }

}
