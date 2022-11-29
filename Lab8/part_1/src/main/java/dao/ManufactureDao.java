package dao;

import connection.Connector;
import models.Manufacture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ManufactureDao {
    public Optional<Manufacture> findById(int id) {
        final String sql = "SELECT * FROM manufactures WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            Optional<Manufacture> result = Optional.empty();

            try (ResultSet rs = statement.executeQuery();) {
                if (rs.next()) {
                    Manufacture manufacture = new Manufacture();
                    manufacture.setId(rs.getInt("id"));
                    manufacture.setName(rs.getString("name"));
                    result = Optional.of(manufacture);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Manufacture> findAll() {
        final String sql = "SELECT * FROM manufactures";

        try (Connection connection = Connector.getConnection();
             Statement statement = connection.createStatement()) {

            List<Manufacture> result = new ArrayList<>();

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    Manufacture manufacture = new Manufacture();
                    manufacture.setId(rs.getInt("id"));
                    manufacture.setName(rs.getString("name"));
                    result.add(manufacture);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Manufacture updated) {
        final String sql = "UPDATE manufactures SET name = ? WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, updated.getName());
            statement.setInt(2, updated.getId());
            int updatedRecords = statement.executeUpdate();

            return updatedRecords > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        final String sql = "DELETE FROM manufactures WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int deletedRecords = statement.executeUpdate();

            return deletedRecords > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(Manufacture toInsert) {
        final String sql = "INSERT INTO manufactures(name) VALUES(?)";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, toInsert.getName());
            int insertedCount = statement.executeUpdate();

            return insertedCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}