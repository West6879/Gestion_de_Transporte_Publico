package database;

import estructura.Estacion;
import estructura.Ruta;
import java.sql.*;
import java.util.*;

/*
Clase: RutaDAO
Objetivo: Clase data-access-object para las rutas, maneja el trabajo de base de datos de las rutas.
*/
public class RutaDAO {

    public static final RutaDAO INSTANCE = new RutaDAO();

    private RutaDAO() {

    }

    public static RutaDAO getInstance() {
        return INSTANCE;
    }

    // Metodo para guardar una ruta en la base de datos.
    public void save(Ruta ruta) {
        final String sql = "INSERT INTO rutas (id, distancia, tiempo, costo, ponderacion, id_origen, id_destino) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, ruta.getId());
            preparedStatement.setInt(2, ruta.getDistancia());
            preparedStatement.setInt(3, ruta.getTiempo());
            preparedStatement.setDouble(4, ruta.getCosto());
            preparedStatement.setFloat(5, ruta.getPonderacion());
            preparedStatement.setObject(6, ruta.getOrigen().getId());
            preparedStatement.setObject(7, ruta.getDestino().getId());
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println("No se pudo guardar la ruta: "  + e.getMessage());
        }
    }

    // Metodo para actualizar una ruta en la base de datos.
    public void update(Ruta ruta) {
        final String sql = "UPDATE rutas SET distancia = ?, tiempo = ?, costo = ?, ponderacion = ?, id_origen = ?, id_destino = ? " +
                "WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ruta.getDistancia());
            preparedStatement.setInt(2, ruta.getTiempo());
            preparedStatement.setDouble(3, ruta.getCosto());
            preparedStatement.setFloat(4, ruta.getPonderacion());
            preparedStatement.setObject(5, ruta.getOrigen().getId());
            preparedStatement.setObject(6, ruta.getDestino().getId());
            preparedStatement.setObject(7, ruta.getId());
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println("No se pudo actualizar la ruta: " + e.getMessage());
        }
    }

    // Metodo para eliminar una ruta de la base de datos.
    public void delete(UUID id) {
        final String sql = "DELETE FROM rutas WHERE id = ? ";
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println("No se pudo eliminar la ruta: " +  e.getMessage());
        }
    }

    // Metodo para conseguir todas las rutas de la base de dato. Retorna un mapa de rutas.
    public Map<UUID, Ruta> findAll(Map<UUID, Estacion> estaciones) {
        Map<UUID, Ruta> lista = new HashMap<>();
        final String sql = "SELECT * FROM rutas";
        try (Connection connection = DatabaseConnection.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                Ruta ruta = new Ruta();
                ruta.setId(resultSet.getObject("id", UUID.class));
                ruta.setDistancia(resultSet.getInt("distancia"));
                ruta.setTiempo(resultSet.getInt("tiempo"));
                ruta.setCosto(resultSet.getDouble("costo"));
                ruta.setPonderacion(resultSet.getFloat("ponderacion"));
                Estacion origen = estaciones.get(resultSet.getObject("id_origen", UUID.class));
                Estacion destino = estaciones.get(resultSet.getObject("id_destino", UUID.class));
                if(origen == null || destino == null) {
                    System.out.println("Error al cargar rutas, debido a que una estación no existe en el sistema.");
                }
                ruta.setOrigen(origen);
                ruta.setDestino(destino);

                lista.put(ruta.getId(), ruta);
            }

        } catch(SQLException e) {
            System.out.println("No se pudo obtener la lista de rutas: " + e.getMessage());
        }

        return lista;
    }


}
