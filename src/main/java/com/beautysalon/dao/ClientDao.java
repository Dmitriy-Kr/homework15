package com.beautysalon.dao;

import com.beautysalon.entity.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ClientDao {
    static final String URL = "jdbc:mysql://localhost:3306/beautysalon";
    static final String SQL_ADD_CLIENT
            = "INSERT client(name, surname, account_id) VALUE(?, ?, (SELECT id FROM account WHERE login = ?))";
    static final String SQL_DELETE_CLIENT = "DELETE FROM client WHERE id = ?";
    static final String SQL_FIND_CLIENT_BY_ID =
            "SELECT client.name, surname, login, password, role.name, create_time, client.id, account.id, role.id FROM client" +
                    " JOIN account ON account.id = client.account_id " +
                    "JOIN role ON role.id = account.role_id WHERE client.id = ?";
    static final String SQL_UPDATE_CLIENT = "UPDATE client SET name = ?, surname = ? WHERE id = ?";

    public void create(Client client) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_CLIENT, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            preparedStatement.setString(i++, client.getName());
            preparedStatement.setString(i++, client.getSurname());
            preparedStatement.setString(i++, client.getAccount().getLogin());
            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    client.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table client");
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

    public void update(Client client) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_CLIENT);
            int i = 1;
            preparedStatement.setString(i++, client.getName());
            preparedStatement.setString(i++, client.getSurname());
            preparedStatement.setLong(i++, client.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table client");
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
            preparedStatement = connection.prepareStatement(SQL_DELETE_CLIENT);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table client");
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

    public Client find(long id) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client resultClient = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_FIND_CLIENT_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resultClient = new Client()
                        .setAccount(new Account()
                                .setRole(new Role()));
                resultClient.setName(resultSet.getString("name"));
                resultClient.setSurname(resultSet.getString("surname"));
                resultClient.getAccount().setLogin(resultSet.getString("login"));
                resultClient.getAccount().setPassword(resultSet.getString("password"));
                resultClient.getAccount().getRole().setName(RoleEnum.valueOf(resultSet.getString("role.name").toUpperCase()));
                resultClient.getAccount().setCreateTime(Timestamp.valueOf(resultSet.getString("create_time")).toLocalDateTime());
                resultClient.setId(resultSet.getLong("client.id"));
                resultClient.getAccount().setId(resultSet.getLong("account.id"));
                resultClient.getAccount().getRole().setId(resultSet.getLong("role.id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table client");
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
        return resultClient;
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
