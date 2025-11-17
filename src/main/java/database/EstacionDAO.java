package database;

import estructura.Estacion;
import estructura.TipoEstacion;
import javafx.scene.paint.Color;

import java.sql.*;
import java.util.*;

/*
Clase: EstacionDAO
Objetivo: Clase data-access-object para las estaciones, maneja el trabajo de base de datos de las estaciones.
*/
public class EstacionDAO {

    public static final EstacionDAO INSTANCE = new EstacionDAO();

    private EstacionDAO() {

    }

    public static EstacionDAO getInstance() {
        return INSTANCE;
    }

    // Metodo para guardar una estacion en la base de datos.
    public void save(Estacion estacion) {
        final String sql = "INSERT INTO estaciones (id, nombre, zona, latitud, longitud, costo, velocidad, tipo, color) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?::tipo, ?)";
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, estacion.getId());
            preparedStatement.setString(2, estacion.getNombre());
            preparedStatement.setString(3, estacion.getZona());
            preparedStatement.setDouble(4, estacion.getLatitud());
            preparedStatement.setDouble(5, estacion.getLongitud());
            preparedStatement.setDouble(6, estacion.getCostoBase());
            preparedStatement.setInt(7, estacion.getVelocidad());
            preparedStatement.setString(8, estacion.getTipo().getDisplayName().toLowerCase());
            preparedStatement.setString(9, estacion.getColor().toString());
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println("No se pudo guardar la estación: " + e.getMessage());
        }
    }

    // Metodo para actualizar una estacion en la base de datos.
    public void update(Estacion estacion) {
        final String sql = "UPDATE estaciones SET nombre = ?, zona = ?, latitud = ?, longitud = ?, costo = ?, velocidad = ?, tipo = ?::tipo, color = ? " +
                "WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, estacion.getNombre());
            preparedStatement.setString(2, estacion.getZona());
            preparedStatement.setDouble(3, estacion.getLatitud());
            preparedStatement.setDouble(4, estacion.getLongitud());
            preparedStatement.setDouble(5, estacion.getCostoBase());
            preparedStatement.setDouble(6, estacion.getVelocidad());
            preparedStatement.setString(7, estacion.getTipo().getDisplayName().toLowerCase());
            preparedStatement.setString(8, estacion.getColor().toString());
            preparedStatement.setObject(9, estacion.getId());
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println("No se pudo actualizar la estación: " + e.getMessage());
        }
    }

    // Metodo para eliminar una estacion de la base de datos.
    public void delete(UUID id) {
        final String sql = "DELETE FROM estaciones WHERE id = ? ";
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println("No se pudo eliminar la estación: " +  e.getMessage());
        }
    }

    // Metodo para conseguir todas las estaciones de la base de dato. Retorna un mapa de estaciones.
    public Map<UUID, Estacion> findAll() {
        Map<UUID, Estacion> lista = new HashMap<>();
        final String sql = "SELECT * FROM estaciones";
        try (Connection connection = DatabaseConnection.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                Estacion estacion = new Estacion();
                estacion.setId(resultSet.getObject("id", UUID.class));
                estacion.setNombre(resultSet.getString("nombre"));
                estacion.setZona(resultSet.getString("zona"));
                estacion.setLatitud(resultSet.getDouble("latitud"));
                estacion.setLongitud(resultSet.getDouble("longitud"));
                estacion.setCostoBase(resultSet.getDouble("costo"));
                estacion.setVelocidad(resultSet.getInt("velocidad"));
                String tipoString = resultSet.getString("tipo");
                estacion.setTipo(TipoEstacion.valueOf(tipoString.toUpperCase()));
                String colorString = resultSet.getString("color");
                estacion.setColor(Color.valueOf(colorString));
                lista.put(estacion.getId(), estacion);
            }

        } catch(SQLException e) {
            System.out.println("No se pudo obtener la lista de estaciones: " + e.getMessage());
        }

        return lista;
    }

}
