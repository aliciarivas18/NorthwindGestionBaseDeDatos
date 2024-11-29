package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection implements SchemaDB {
    //permite llegar a la conexión
    private static Connection connection;


    public static Connection getDbConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            createConnection();
        }
        return connection;
    }


    private static void createConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%s/%s", SchemaDB.HOST, SchemaDB.PORT, SchemaDB.DB_NAME);
        connection = DriverManager.getConnection(url, "root", "");
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage());
        }
    }


}
