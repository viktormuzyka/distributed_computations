package dao;

import connection.Connector;
import models.Brand;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BrandDao {
    public Optional<Brand> findById(int id) {
        final String sql = "SELECT * FROM brands WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            Optional<Brand> result = Optional.empty();

            try (ResultSet rs = statement.executeQuery();) {
                if (((ResultSet) rs).next()) {
                    Brand brand = new Brand();
                    brand.setId(rs.getInt("id"));
                    brand.setManufactureId(rs.getInt("manufacture_id"));
                    brand.setPrice(rs.getInt("price"));
                    brand.setName(rs.getString("name"));
                    result = Optional.of(brand);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Brand> findAll() {
        final String sql = "SELECT * FROM brands";

        try (Connection connection = Connector.getConnection();
             Statement statement = connection.createStatement()) {

            List<Brand> result = new ArrayList<>();

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    Brand brand = new Brand();
                    brand.setId(rs.getInt("id"));
                    brand.setManufactureId(rs.getInt("manufacture_id"));
                    brand.setName(rs.getString("name"));
                    brand.setPrice(rs.getInt("price"));
                    result.add(brand);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Brand updated) {
        final String sql = "UPDATE brands SET manufacture_id = ?, name = ?, price = ? WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, updated.getManufactureId());
            statement.setString(2, updated.getName());
            statement.setInt(3, updated.getPrice());
            statement.setInt(4, updated.getId());
            int updatedRecords = statement.executeUpdate();

            return updatedRecords > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        final String sql = "DELETE FROM brands WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            int deletedRecords = statement.executeUpdate();

            return deletedRecords > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(Brand toInsert) {
        final String sql = "INSERT INTO showsalon.brands(manufacture_id, name, price) VALUES(?, ?, ?)";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, toInsert.getManufactureId());
            statement.setString(2, toInsert.getName());
            statement.setInt(3, toInsert.getPrice());
            int insertedCount = statement.executeUpdate();

            return insertedCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Brand> findByManufactureName(String manufactureName) {
        final String sql = """
            SELECT * FROM brands WHERE manufacture_id = (
                SELECT id FROM manufactures WHERE manufactures.name = ?
            )
        """;

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, manufactureName);
            List<Brand> result;

            try (ResultSet rs = statement.executeQuery()) {
                result = getBrandsFromResultSet(rs);
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean moveToAnotherManufacture(int brandId, int newManufactureID) {
        Optional<Brand> optionalBrand = findById(brandId);

        if (optionalBrand.isEmpty()) {
            return false;
        }

        Brand brand = optionalBrand.get();
        brand.setManufactureId(newManufactureID);
        update(brand);

        return true;
    }

    private List<Brand> getBrandsFromResultSet(ResultSet rs) throws SQLException {
        List<Brand> result = new ArrayList<>();

        while (rs.next()) {
            Brand brand = new Brand();
            brand.setId(rs.getInt("id"));
            brand.setManufactureId(rs.getInt("manufacture_id"));
            brand.setPrice(rs.getInt("price"));
            brand.setName(rs.getString("name"));;
            result.add(brand);
        }

        return result;
    }
}