package com.beautysalon.dao;

import com.beautysalon.entity.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class EmployeeDao {
    static final String URL = "jdbc:mysql://localhost:3306/beautysalon";
    static final String SQL_ADD_EMPLOYEE
            = "INSERT employee(name, surname, rating, account_id, profession_id) " +
            "VALUE(?, ?, ?, (SELECT id FROM account WHERE login = ?), (SELECT id FROM profession WHERE name = ?))";
    static final String SQL_DELETE_EMPLOYEE = "DELETE FROM employee WHERE id = ?";
    static final String SQL_FIND_EMPLOYEE_BY_ID =
            "SELECT employee.name, surname, rating, login, password, role.name, create_time, employee.id," +
                    " account.id, role.id FROM employee" +
                    " JOIN account ON account.id = employee.account_id" +
                    " JOIN role ON role.id = account.role_id WHERE employee.id = ?";
    static final String SQL_UPDATE_EMPLOYEE = "UPDATE employee SET name = ?, surname = ? WHERE id = ?";

    public void create(Employee employee) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_EMPLOYEE, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            preparedStatement.setString(i++, employee.getName());
            preparedStatement.setString(i++, employee.getSurname());
            preparedStatement.setDouble(i++, employee.getRating());
            preparedStatement.setString(i++, employee.getAccount().getLogin());
            preparedStatement.setString(i++, employee.getProfession().getName());
            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    employee.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table employee");
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

    public void update(Employee employee) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_EMPLOYEE);
            int i = 1;
            preparedStatement.setString(i++, employee.getName());
            preparedStatement.setString(i++, employee.getSurname());
            preparedStatement.setLong(i++, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table employee");
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
            preparedStatement = connection.prepareStatement(SQL_DELETE_EMPLOYEE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table employee");
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

    public Employee find(long id) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee resultEmployee = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_FIND_EMPLOYEE_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resultEmployee = new Employee()
                        .setProfession(new Profession())
                        .setAccount(new Account()
                                .setRole(new Role()));
                resultEmployee.setName(resultSet.getString("name"));
                resultEmployee.setSurname(resultSet.getString("surname"));
                resultEmployee.setRating(resultSet.getDouble("rating"));
                resultEmployee.getAccount().setLogin(resultSet.getString("login"));
                resultEmployee.getAccount().setPassword(resultSet.getString("password"));
                resultEmployee.getAccount().getRole().setName(RoleEnum.valueOf(resultSet.getString("role.name").toUpperCase()));
                resultEmployee.getAccount().setCreateTime(Timestamp.valueOf(resultSet.getString("create_time")).toLocalDateTime());
                resultEmployee.setId(resultSet.getLong("employee.id"));
                resultEmployee.getAccount().setId(resultSet.getLong("account.id"));
                resultEmployee.getAccount().getRole().setId(resultSet.getLong("role.id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Failed to connect table employee");
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
        return resultEmployee;
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
