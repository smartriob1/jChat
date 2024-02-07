package Servidor;



import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Silvia
 */
public class Conexion {
    
    private Integer puerto;
    private String servidor;
    private static Conexion singleton = null;
    public final static String FIN_CLIENTE = "fin_cliente ";

    private Conexion() {
        puerto = 9;
        servidor = "localhost";
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(".\\Servidor\\opciones.conf"));
            puerto = Integer.valueOf(props.getProperty("PUERTO"));
            servidor = props.getProperty("SERVIDOR", "localhost");
        } catch (IOException| NumberFormatException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Integer PUERTO() {
        return getConexion().puerto;
    }

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
