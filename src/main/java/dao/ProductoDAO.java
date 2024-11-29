package dao;

import database.DBConnection;
import model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductoDAO {

    // Método para agregar un producto a la base de datos
    public static void agregarProducto(Producto producto) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {

            dbConnection = DBConnection.getDbConnection();
            String dbquery = "INSERT INTO productos (nombre, descripcion, cantidad, precio) VALUES (?, ?, ?, ?)";
            preparedStatement = dbConnection.prepareStatement(dbquery);

            preparedStatement.setString(1, producto.getTitle());
            preparedStatement.setString(2, producto.getDescription());
            preparedStatement.setInt(3, producto.getStock());
            preparedStatement.setDouble(4, producto.getPrice());

            preparedStatement.executeUpdate();
            System.out.println("Producto agregado correctamente a la base de datos");

        } catch (SQLException e) {
            System.out.println("Error en la conexión con la base de datos: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (dbConnection != null && !dbConnection.isClosed()) dbConnection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar los recursos: " + e.getMessage());
            }
        }
    }
}
