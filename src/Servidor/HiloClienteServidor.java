package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Silvia
 */
public class HiloClienteServidor extends Thread {

    private final static String[] COMANDOS = {"#ayuda", "#listar", "#charlar ", "#salir"};
    private String nombre;
    private boolean charlando = false;
    private Socket cliente;
    private HiloClienteServidor usuario = null;

    public HiloClienteServidor(String nombre) {
        this.nombre = nombre;
    }

    public HiloClienteServidor(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        String mensaje;
        try {
            //Obtenemos el nic
            dis = new DataInputStream(cliente.getInputStream());
            nombre = dis.readUTF();
            System.out.println("Recibido cliente " + nombre + " con dirección " + cliente.getRemoteSocketAddress());
            if (addCliente()) {
                synchronized (this) {
                    dos = new DataOutputStream(cliente.getOutputStream());
                    dos.writeUTF("Estás conectado con el nic de " + nombre);
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
                        usuario = new HiloClienteServidor(campos[1]);
                        charlar();
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
                                dos = new DataOutputStream(cliente.getOutputStream());
                                dos.writeUTF("[ERROR] El usuario " + usuario.nombre + " ya no se encuentra conectado. Utiliza el comando #listar para ver los usuarios conectados.");
                                charlando = false;
                            }
                        }
                    } else {
                        dos = new DataOutputStream(cliente.getOutputStream());
                        dos.writeUTF("[ERROR] '" + mensaje + "' no se reconoce como comando. Si quieres iniciar una conversación o responder a un usuario utiliza el comando #charlar <nic>.");
                    }
                }
            }

        } catch (IOException ex) {
            //Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Cerrando cliente " + nombre + " con dirección " + cliente.getRemoteSocketAddress());
        } finally {
            if (ServidorChat.CONEXIONES_CLIENTES.contains(this)) {
                ServidorChat.CONEXIONES_CLIENTES.remove(this);
            }

        }
        System.out.println(this.nombre + " con dirección " + cliente.getRemoteSocketAddress() + " desconectado.");
    }

    public synchronized void enviarMensaje(String mensaje) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(cliente.getOutputStream());
            dos.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(HiloClienteServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    private void listarUsuarios() {
        StringBuilder sb = new StringBuilder();
        synchronized (ServidorChat.CONEXIONES_CLIENTES) {
            int usuarios = ServidorChat.CONEXIONES_CLIENTES.size();
            if (usuarios > 0) {
                sb.append("En este momento están conectados ").append(usuarios).append(" usuarios:\n");
                for (HiloClienteServidor usuario : ServidorChat.CONEXIONES_CLIENTES) {
                    sb.append(usuario.nombre).append("\n");
                }
            }
        }
        enviarMensaje(sb.toString());
    }

    private void ayuda() {
        StringBuilder sb = new StringBuilder();
        sb.append("#listar: lista todos los usuarios conectados.\n");
        sb.append("#charlar <usuario>: comienza la comunicación con el usuario <usuario>.\n");
        sb.append("#salir: se desconecta del chat.");
        enviarMensaje(sb.toString());
    }

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

    public synchronized void enviarMensajeAUsuario(String mensaje) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(usuario.cliente.getOutputStream());
            dos.writeUTF(">" + nombre + ": " + mensaje);
        } catch (IOException ex) {
            Logger.getLogger(HiloClienteServidor.class.getName()).log(Level.SEVERE, null, ex);
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
        final HiloClienteServidor other = (HiloClienteServidor) obj;
        return Objects.equals(this.nombre, other.nombre);
    }

}
