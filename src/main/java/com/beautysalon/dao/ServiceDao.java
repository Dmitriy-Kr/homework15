package com.beautysalon.dao;

import com.beautysalon.entity.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ServiceDao {
    static final String URL = "jdbc:mysql://localhost:3306/beautysalon";
    static final String SQL_ADD_SERVICE
            = "INSERT service(name, price, spend_time, profession_id) VALUE(?, ?, ?, (SELECT id FROM profession WHERE name = ?))";
    static final String SQL_DELETE_SERVICE = "DELETE FROM service WHERE id = ?";
    static final String SQL_FIND_SERVICE_BY_ID =
            "SELECT service.name, price, spend_time, profession.name, service.id, profession.id " +
                    "FROM service JOIN profession ON profession.id = service.profession_id WHERE service.id = ?";
    static final String SQL_UPDATE_SERVICE
            = "UPDATE service SET name = ?, price = ?, spend_time = ? WHERE id = ?";

    public void create(Service service) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_SERVICE, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            preparedStatement.setString(i++, service.getName());
            preparedStatement.setDouble(i++, service.getPrice());
            preparedStatement.setTime(i++, service.getSpendTime());
            preparedStatement.setString(i++, service.getProfession().getName());
            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    service.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table service");
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void update(Service service) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_SERVICE);
            int i = 1;
            preparedStatement.setString(i++, service.getName());
            preparedStatement.setDouble(i++, service.getPrice());
            preparedStatement.setTime(i++, service.getSpendTime());
            preparedStatement.setLong(i++, service.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table service");
        } finally {

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void delete(long id) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_SERVICE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table service");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Service find(long id) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Service resultService = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_FIND_SERVICE_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                resultService = new Service().setProfession(new Profession());
                resultService.setName(resultSet.getString("service.name"));
                resultService.setPrice(resultSet.getDouble("price"));
                resultService.setSpendTime(resultSet.getTime("spend_time"));
                resultService.getProfession().setName(resultSet.getString("profession.name"));
                resultService.setId(resultSet.getLong("service.id"));
                resultService.getProfession().setId(resultSet.getLong("profession.id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table service");
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultService;
    }

    public static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(new File("db.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, properties);
    }

}
