package Servidor;

import Conexion.Conexion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hilo para cada cliente del servidor.
 *
 * @author Silvia
 */
public class HiloCliente extends Thread {

    private final static String[] COMANDOS = {"#ayuda", "#listar", "#charlar ", "#salir"};
    private String nombre;
    private boolean charlando = false;
    private Socket cliente;
    private HiloCliente usuario = null;

    public HiloCliente(String nombre) {
        this.nombre = nombre;
    }

    public HiloCliente(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        String mensaje;

        try {
            dis = new DataInputStream(cliente.getInputStream());
            nombre = dis.readUTF();
            System.out.println("Recibido cliente " + nombre + " con dirección " + cliente.getRemoteSocketAddress());
            if (addCliente()) {
                synchronized (this) {
                    enviarMensaje("Estás conectado con el nic de " + nombre);
                }

                while (true) {
                    mensaje = dis.readUTF();

                    if (COMANDOS[0].equalsIgnoreCase(mensaje.trim())) {
                        ayuda();
                        continue;
                    }

                    if (COMANDOS[1].equalsIgnoreCase(mensaje.trim())) {
                        listarUsuarios();
                        continue;
                    }

                    if (mensaje.trim().startsWith(COMANDOS[2])) {
                        String[] campos = mensaje.split(" ");
                        if (campos.length == 2) {
                            usuario = new HiloCliente(campos[1]);
                            charlar();
                        }else{
                            enviarMensaje("[ERROR] El comando es incorrecto. Usa #charlar <nic>.");
                        }
                        continue;
                    }

                    if (COMANDOS[3].equalsIgnoreCase(mensaje.trim())) {
                        enviarMensaje(Conexion.FIN_CLIENTE);
                        ServidorChat.CONEXIONES_CLIENTES.remove(this);
                        break;
                    }

                    if (charlando) {
                        synchronized (ServidorChat.CONEXIONES_CLIENTES) {
                            if (ServidorChat.CONEXIONES_CLIENTES.contains(usuario)) {
                                enviarMensajeAUsuario(mensaje);
                            } else {
                                enviarMensaje("[ERROR] El usuario " + usuario.nombre + " ya no se encuentra conectado. Utiliza el comando #listar para ver los usuarios conectados.");
                                charlando = false;
                            }
                        }
                    } else {
                        enviarMensaje("[ERROR] '" + mensaje + "' no se reconoce como comando. Si quieres iniciar una conversación o responder a un usuario utiliza el comando #charlar <nic>.");
                    }
                }
            }

        } catch (IOException ex) {
            System.err.println("Cerrando cliente " + nombre + " con dirección " + cliente.getRemoteSocketAddress());
        } finally {
            if (ServidorChat.CONEXIONES_CLIENTES.contains(this)) {
                ServidorChat.CONEXIONES_CLIENTES.remove(this);
            }

        }
        System.out.println(this.nombre + " con dirección " + cliente.getRemoteSocketAddress() + " desconectado.");
    }

    /**
     * Método para enviar un mensaje al cliente.
     *
     * @param mensaje mensaje a enviar.
     */
    public synchronized void enviarMensaje(String mensaje) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(cliente.getOutputStream());
            dos.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para comprobar si hay un cliente con el mismo nombre.
     */
    private boolean addCliente() {
        synchronized (ServidorChat.CONEXIONES_CLIENTES) {
            if (ServidorChat.CONEXIONES_CLIENTES.contains(this)) {
                System.out.println("Rechazando conexion para " + nombre);
                enviarMensaje(Conexion.FIN_CLIENTE);
                return false;
            } else {
                ServidorChat.CONEXIONES_CLIENTES.add(this);
            }
        }
        return true;
    }

    /**
     * Método para listar los usuarios con el comando #listar.
     */
    private void listarUsuarios() {
        StringBuilder sb = new StringBuilder();
        synchronized (ServidorChat.CONEXIONES_CLIENTES) {
            int usuarios = ServidorChat.CONEXIONES_CLIENTES.size();
            if (usuarios > 0) {
                sb.append("En este momento están conectados ").append(usuarios).append(" usuarios:\n");
                for (HiloCliente usuario : ServidorChat.CONEXIONES_CLIENTES) {
                    sb.append(usuario.nombre).append("\n");
                }
            }
        }
        enviarMensaje(sb.toString());
    }

    /**
     * Método para la ayuda con el comando #ayuda.
     */
    private void ayuda() {
        StringBuilder sb = new StringBuilder();
        sb.append("#listar: lista todos los usuarios conectados.\n");
        sb.append("#charlar <usuario>: comienza la comunicación con el usuario <usuario>.\n");
        sb.append("#salir: se desconecta del chat.");
        enviarMensaje(sb.toString());
    }

    /**
     * Método para conectar con un cliente con el comando #charlar usuario si
     * este existe.
     */
    private void charlar() {
        StringBuilder sb = new StringBuilder();
        synchronized (ServidorChat.CONEXIONES_CLIENTES) {
            if (ServidorChat.CONEXIONES_CLIENTES.contains(usuario) && !usuario.equals(this)) {
                int index = ServidorChat.CONEXIONES_CLIENTES.indexOf(usuario);
                this.usuario = ServidorChat.CONEXIONES_CLIENTES.get(index);
                charlando = true;
                sb.append("Ahora estás conectado con ").append(usuario.nombre).append(". Escribe para hablarle.");
            } else {
                sb.append("[ERROR] El usuario ").append(usuario.nombre).append(" no se encuentra conectado o es usted mismo. Utiliza el comando #listar para ver los usuarios conectados.");
            }
        }
        enviarMensaje(sb.toString());
    }

    /**
     * Método para reenviar un mensaje a otro cliente.
     *
     * @param mensaje mensaje a enviar.
     */
    public synchronized void enviarMensajeAUsuario(String mensaje) {
        DataOutputStream dos = null;
        try {
            if (!mensaje.equalsIgnoreCase("") && mensaje.charAt(0) != '#') {
                dos = new DataOutputStream(usuario.cliente.getOutputStream());
                dos.writeUTF(">" + nombre + ": " + mensaje);
            } else {
                enviarMensaje("[ERROR] Los mensajes no pueden estar vacíos ni empezar por '#'.");
            }

        } catch (IOException ex) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HiloCliente other = (HiloCliente) obj;
        return Objects.equals(this.nombre, other.nombre);
    }

}
