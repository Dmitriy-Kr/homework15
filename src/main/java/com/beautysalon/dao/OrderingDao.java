package com.beautysalon.dao;

import com.beautysalon.entity.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class OrderingDao {
    static final String URL = "jdbc:mysql://localhost:3306/beautysalon";
    static final String SQL_ADD_ORDERING
            = "INSERT ordering(ordering_date_time, status, service_id, employee_id, client_id) " +
            "VALUE(?, ?, ?, ?, ?)";
    static final String SQL_DELETE_ORDERING = "DELETE FROM ordering WHERE id = ?";
    static final String SQL_FIND_ORDERING_BY_ID =
            "SELECT ordering_date_time, status, service_id, employee_id, client_id, create_time, update_time," +
                    "service.name, service.price, service.spend_time," +
                    "employee.name, employee.surname, employee.rating, employee.profession_id, " +
                    "profession.name, " +
                    "client.name, client.surname " +
                    "FROM ordering " +
                    "JOIN service ON service.id = ordering.service_id " +
                    "JOIN employee ON employee.id = ordering.employee_id " +
                    "JOIN client ON client.id = ordering.client_id " +
                    "JOIN profession ON profession.id = employee.profession_id " +
                    "WHERE ordering.id = ?";
    static final String SQL_UPDATE_ORDERING
            = "UPDATE ordering SET ordering_date_time = ?, status = ? WHERE id = ?";

    public void create(Ordering ordering) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_ORDERING, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(ordering.getOrderDateTime()));
            preparedStatement.setString(i++, ordering.getStatus().toString());
            preparedStatement.setLong(i++, ordering.getService().getId());
            preparedStatement.setLong(i++, ordering.getEmployee().getId());
            preparedStatement.setLong(i++, ordering.getClient().getId());
            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    ordering.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table ordering");
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

    public void update(Ordering ordering) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ORDERING);
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(ordering.getOrderDateTime()));
            preparedStatement.setString(i++, ordering.getStatus().toString());
            preparedStatement.setLong(i++, ordering.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table ordering");
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
            preparedStatement = connection.prepareStatement(SQL_DELETE_ORDERING);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table ordering");
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

    public Ordering find(long id) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Ordering resultOrdering = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_FIND_ORDERING_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                resultOrdering = new Ordering();
                resultOrdering.setService(new Service());
                resultOrdering.setEmployee(new Employee());
                resultOrdering.setClient(new Client());
                resultOrdering.getEmployee().setProfession(new Profession());

                resultOrdering.setId(id);
                resultOrdering.setOrderDateTime(resultSet.getTimestamp("ordering_date_time").toLocalDateTime());
                resultOrdering.setStatus(StatusEnum.valueOf(resultSet.getString("status").toUpperCase()));
                resultOrdering.getService().setId(resultSet.getLong("service_id"));
                resultOrdering.getEmployee().setId(resultSet.getLong("employee_id"));
                resultOrdering.getClient().setId(resultSet.getLong("client_id"));
                resultOrdering.setCreateTime(resultSet.getTimestamp("create_time").toLocalDateTime());
                resultOrdering.setUpdateTime(resultSet.getTimestamp("update_time").toLocalDateTime());
                resultOrdering.getService().setName(resultSet.getString("service.name"));
                resultOrdering.getService().setPrice(resultSet.getDouble("service.price"));
                resultOrdering.getService().setSpendTime(resultSet.getTime("service.spend_time"));
                resultOrdering.getEmployee().setName(resultSet.getString("employee.name"));
                resultOrdering.getEmployee().setSurname(resultSet.getString("employee.surname"));
                resultOrdering.getEmployee().setRating(resultSet.getDouble("employee.rating"));
                resultOrdering.getEmployee().getProfession().setId(resultSet.getLong("employee.profession_id"));
                resultOrdering.getEmployee().getProfession().setName(resultSet.getString("profession.name"));
                resultOrdering.getClient().setName(resultSet.getString("client.name"));
                resultOrdering.getClient().setSurname(resultSet.getString("client.surname"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table ordering");
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
        return resultOrdering;
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
