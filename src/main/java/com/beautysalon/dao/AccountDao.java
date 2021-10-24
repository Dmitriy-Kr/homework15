package com.beautysalon.dao;

import com.beautysalon.entity.Account;
import com.beautysalon.entity.Role;
import com.beautysalon.entity.RoleEnum;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class AccountDao {
    static final String URL = "jdbc:mysql://localhost:3306/beautysalon";
    static final String SQL_ADD_ACCOUNT
            = "INSERT account(login, password, role_id) VALUE(?, ?, (SELECT id FROM role WHERE name = ?))";
    static final String SQL_DELETE_ACCOUNT = "DELETE FROM account WHERE id = ?";
    static final String SQL_FIND_ACCOUNT_BY_ID =
            "SELECT login, password, name, create_time, account.id, role.id FROM account JOIN role ON role.id = account.role_id WHERE account.id = ?";
    static final String SQL_UPDATE_ACCOUNT
            = "UPDATE account SET login = ?, password = ?, role_id = (SELECT id FROM role WHERE name = ?) WHERE id = ?";

    public void create(Account account) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_ACCOUNT, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            preparedStatement.setString(i++, account.getLogin());
            preparedStatement.setString(i++, account.getPassword());
            preparedStatement.setString(i++, account.getRole().getName().toString());
            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    account.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table account");
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

    public void update(Account account) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ACCOUNT);
            int i = 1;
            preparedStatement.setString(i++, account.getLogin());
            preparedStatement.setString(i++, account.getPassword());
            preparedStatement.setString(i++, account.getRole().getName().toString());
            preparedStatement.setLong(i++, account.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table account");
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
            preparedStatement = connection.prepareStatement(SQL_DELETE_ACCOUNT);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new DBException("Failed to connect table account");
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

    public Account find(long id) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        long roleId = 0;
        Account resultAccount = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_FIND_ACCOUNT_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                resultAccount = new Account().setRole(new Role());
                resultAccount.setLogin(resultSet.getString("login"));
                resultAccount.setPassword(resultSet.getString("password"));
                resultAccount.getRole().setName(RoleEnum.valueOf(resultSet.getString("name").toUpperCase()));
                resultAccount.setCreateTime(Timestamp.valueOf(resultSet.getString("create_time")).toLocalDateTime());
                resultAccount.setId(resultSet.getLong("account.id"));
                resultAccount.getRole().setId(resultSet.getLong("role.id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table account");
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
        return resultAccount;
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
