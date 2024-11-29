package dao;

import database.DBConnection;
import database.SchemaDB;
import model.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmpleadoDAO {
    //TODAS LAS ACCIONES CONTRABASE DE DATOS
    protected Connection connection;
    protected PreparedStatement preparedStatement;
    //private ResultSet resultSet;


    public boolean registraEmpleado(Empleado empleado) throws SQLException {
        connection = DBConnection.getDbConnection();
        String query = String.format( "INSERT INTO %s (%s, %s, %s) VALUE(?, ?, ?)",
                SchemaDB.TAB_USER, SchemaDB.COL_NAME, SchemaDB.COL_SURNAME, SchemaDB.COL_MAIL);

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,empleado.getNombre());
        preparedStatement.setString(2, empleado.getApellido());
        preparedStatement.setString(3, empleado.getCorreo());

        return preparedStatement.executeUpdate() >0;
    }
}

