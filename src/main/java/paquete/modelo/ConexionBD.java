package paquete.modelo;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Clase encargada de gestionar la conexión a la base de datos MySQL.
 * Establece una conexión a la base de datos `el_charco` en el servidor local
 * utilizando las credenciales proporcionadas (usuario: root, sin contraseña).
 */
public class ConexionBD {

    /**
     * Establece una conexión con la base de datos MySQL.
     * Utiliza el driver JDBC para conectarse a la base de datos ubicada en
     * `localhost` en el puerto `3306` y con la base de datos `el_charco`.
     *
     * @return Un objeto `Connection` que representa la conexión establecida
     *         con la base de datos. Si ocurre un error, devuelve `null`.
     */
    public Connection getConnection() {
        // Objeto de conexión
        Connection con = null;

        // Intenta obtener la conexión a la base de datos
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spartangym", "root", "root");
        }
        // Atrapa cualquier excepción ocurrida durante el proceso de conexión
        catch (Exception e) {
            e.printStackTrace();
        }

        // Devuelve la conexión (o null si hubo un error)
        return con;
    }
}
