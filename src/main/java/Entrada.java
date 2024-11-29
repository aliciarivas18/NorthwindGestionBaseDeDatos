import com.google.gson.Gson;
import dao.EmpleadoDAO;

import dao.ProductoDAO;
import database.DBConnection;
import model.Empleado;

import model.Producto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;


public class Entrada {
    EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public static void agregarPedidos(int id_producto, String descripcion, double precio, int cantidad) throws SQLException {
        double precio_total = precio * cantidad;
        Connection dbConnection = DBConnection.getDbConnection();

        String dbquery = "INSERT INTO pedidos (id_producto, descripcion, precio_total) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dbConnection.prepareStatement(dbquery);
            preparedStatement.setInt(1, id_producto);
            preparedStatement.setString(2, descripcion);
            preparedStatement.setDouble(3, precio_total);
            preparedStatement.execute();
            System.out.println("El pedido ha sido agregado a la base de datos");
        } catch (SQLException e) {
            System.out.println("La query está mal ejecutada " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (dbConnection != null) dbConnection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión " + e.getMessage());
            }
        }
    }



    // Método para agregar productos desde la API
    public static void agregarProductos() {
        String urlString = "https://dummyjson.com/products";
        BufferedReader bufferedReader = null;

        try {
            // Conexión a la API
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String linea = bufferedReader.readLine();

            // Parsear JSON
            JSONObject response = new JSONObject(linea);
            JSONArray resultado = response.getJSONArray("products");

            // Recorrer productos
            for (Object item : resultado) {
                if (item instanceof JSONObject) {
                    Gson gson = new Gson();
                    Producto producto = gson.fromJson(item.toString(), Producto.class);

                    // Mostrar datos (opcional)
                    mostrarDatos("productos");


                    // Insertar en la base de datos
                    ProductoDAO.agregarProducto(producto);
                }
            }
        } catch (IOException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar buffer: " + e.getMessage());
            }
        }
    }

    public void registrarEmpleado() {

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Ingrese nombre del empleado:");
            String nombre = scanner.nextLine();

            System.out.println("Ingrese apellido del empleado:");
            String apellido = scanner.nextLine();

            System.out.println("Ingrese correo del empleado:");
            String correo = scanner.nextLine();


            Empleado empleado = new Empleado(nombre, apellido, correo);

            if (empleadoDAO.registraEmpleado(empleado)) {
                System.out.println("Empleado registrado correctamente.");
            } else {
                System.out.println("Error al registrar el empleado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el empleado: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    public static void mostrarDatos(String tabla) {
        Connection dbConnection = null;
        java.sql.Statement statement = null;

        try {
            dbConnection = DBConnection.getDbConnection();
            if (dbConnection == null) {
                throw new SQLException("Error en la conexión con la base de datos");
            }

            statement = dbConnection.createStatement();
            String query = "SELECT * FROM " + tabla;
            java.sql.ResultSet resultSet = statement.executeQuery(query);

            System.out.println("Datos de la tabla " + tabla + ":");
            while (resultSet.next()) {
                if (tabla.equalsIgnoreCase("empleados")) {
                    System.out.println("ID: " + resultSet.getInt("idEmpleado") +
                            ", Nombre: " + resultSet.getString("nombre") +
                            ", Apellido: " + resultSet.getString("apellido") +
                            ", Correo: " + resultSet.getString("correo"));
                } else if (tabla.equalsIgnoreCase("productos")) {
                    System.out.println("ID: " + resultSet.getInt("idProducto") +
                            ", Nombre: " + resultSet.getString("nombre") +
                            ", Descripción: " + resultSet.getString("descripcion") +
                            ", Cantidad: " + resultSet.getInt("cantidad") +
                            ", Precio: " + resultSet.getDouble("precio"));
                } else if (tabla.equalsIgnoreCase("pedidos")) {
                    System.out.println("ID: " + resultSet.getInt("idPedido") +
                            ", ID Producto: " + resultSet.getInt("id_producto") +
                            ", Descripción: " + resultSet.getString("descripcion") +
                            ", Precio Total: " + resultSet.getDouble("precio_total"));
                } else {
                    System.out.println("Tabla no reconocida.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (dbConnection != null) dbConnection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    public static void precioInferiorASeiscientos() {
        Connection dbConnection = null;
        java.sql.Statement statement = null;

        try {
            dbConnection = DBConnection.getDbConnection();
            if (dbConnection == null) {
                throw new SQLException("Error en la conexión con la base de datos");
            }

            statement = dbConnection.createStatement();
            String dbQuery = "SELECT * FROM productos WHERE precio < 600";
            java.sql.ResultSet resultSet = statement.executeQuery(dbQuery);

            System.out.println("Productos con precio inferior a 600:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("idProducto") +
                        ", Nombre: " + resultSet.getString("nombre") +
                        ", Descripción: " + resultSet.getString("descripcion") +
                        ", Cantidad: " + resultSet.getInt("cantidad") +
                        ", Precio: " + resultSet.getDouble("precio"));
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (dbConnection != null) dbConnection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    public static void insertarProductosFav() {
        Connection dbConnection = null;

        try {
            dbConnection = DBConnection.getDbConnection();
            if (dbConnection == null) {
                throw new SQLException("Error en la conexión con la base de datos");
            }
            String dbQuery = "INSERT INTO productos_fav (id_producto) SELECT idProducto FROM productos WHERE precio > 1000";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(dbQuery);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Productos favoritos insertados: " + rowsAffected + " filas afectadas.");
        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        } finally {
            try {
                if (dbConnection != null) dbConnection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Producto producto = new Producto();
       //Entrada entrada = new Entrada();
        //agregarProductos();
        //entrada.registrarEmpleado();

        //mostrarDatos("productos");
        //mostrarDatos("empleados");
       //mostrarDatos("pedidos");
        //agregarPedidos(81, "Crisp and hydrating cucumbers, ideal for salads, snacks, or as a refreshing side.",22.35, 15);
        //precioInferiorASeiscientos();
        //insertarProductosFav();







    }


}
