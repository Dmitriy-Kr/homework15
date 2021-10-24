package com.beautysalon.dao;

import com.beautysalon.entity.Account;
import com.beautysalon.entity.Admin;
import com.beautysalon.entity.Role;
import com.beautysalon.entity.RoleEnum;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class AdminDao {
    static final String URL = "jdbc:mysql://localhost:3306/beautysalon";
    static final String SQL_ADD_ADMIN
            = "INSERT admin(name, surname, account_id) VALUE(?, ?, (SELECT id FROM account WHERE login = ?))";
    static final String SQL_DELETE_ADMIN = "DELETE FROM admin WHERE id = ?";
    static final String SQL_FIND_ADMIN_BY_ID =
            "SELECT admin.name, surname, login, password, role.name, create_time, admin.id, account.id, role.id FROM admin" +
                    " JOIN account ON account.id = admin.account_id " +
                    "JOIN role ON role.id = account.role_id WHERE admin.id = ?";
    static final String SQL_UPDATE_ADMIN = "UPDATE admin SET name = ?, surname = ? WHERE id = ?";

    public void create(Admin admin) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_ADMIN, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            preparedStatement.setString(i++, admin.getName());
            preparedStatement.setString(i++, admin.getSurname());
            preparedStatement.setString(i++, admin.getAccount().getLogin());
            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    admin.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table admin");
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

    public void update(Admin admin) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ADMIN);
            int i = 1;
            preparedStatement.setString(i++, admin.getName());
            preparedStatement.setString(i++, admin.getSurname());
            preparedStatement.setLong(i++, admin.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table admin");
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
            preparedStatement = connection.prepareStatement(SQL_DELETE_ADMIN);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table admin");
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

    public Admin find(long id) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Admin resultAdmin = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_FIND_ADMIN_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resultAdmin = new Admin()
                        .setAccount(new Account()
                                .setRole(new Role()));
                resultAdmin.setName(resultSet.getString("name"));
                resultAdmin.setSurname(resultSet.getString("surname"));
                resultAdmin.getAccount().setLogin(resultSet.getString("login"));
                resultAdmin.getAccount().setPassword(resultSet.getString("password"));
                resultAdmin.getAccount().getRole().setName(RoleEnum.valueOf(resultSet.getString("role.name").toUpperCase()));
                resultAdmin.getAccount().setCreateTime(Timestamp.valueOf(resultSet.getString("create_time")).toLocalDateTime());
                resultAdmin.setId(resultSet.getLong("admin.id"));
                resultAdmin.getAccount().setId(resultSet.getLong("account.id"));
                resultAdmin.getAccount().getRole().setId(resultSet.getLong("role.id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table admin");
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
        return resultAdmin;
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
