package com.beautysalon;

import com.beautysalon.dao.*;
import com.beautysalon.entity.*;

import java.sql.Time;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Account account = new Account();
        AccountDao accountDao = new AccountDao();

        account.setLogin("Nastya")
                .setPassword("4586")
                .setRole(new Role(RoleEnum.CLIENT));

        try {
            accountDao.create(account);
            System.out.println(account);
            account.setLogin("Anton");
            accountDao.update(account);
            System.out.println(accountDao.find(account.getId()));
            accountDao.delete(account.getId());
        } catch (DBException e) {
            e.printStackTrace();
        }

//---------------------------------------------------------------------------------------
        Role role = new Role(RoleEnum.ADMIN);
        RoleDao roleDao = new RoleDao();

        try {
            roleDao.create(role);
            System.out.println(role);
            role.setName(RoleEnum.CLIENT);
            roleDao.update(role);
            System.out.println(roleDao.find(role.getId()));
            roleDao.delete(role.getId());
        } catch (DBException e) {
            e.printStackTrace();
        }

//---------------------------------------------------------------------------------------
        Admin admin = new Admin();
        AdminDao adminDao = new AdminDao();

        admin.setName("Maria")
                        .setSurname("Petrova")
                                .setAccount(new Account()
                                        .setLogin("Mary23")
                                        .setRole(new Role()));

        try {
            adminDao.create(admin);
            System.out.println(admin);
            admin.setName("John");
            adminDao.update(admin);
            System.out.println(adminDao.find(admin.getId()));
            adminDao.delete(admin.getId());
        } catch (DBException e) {
            e.printStackTrace();
        }

//-----------------------------------------------------------------------------------------
        Client client = new Client();
        ClientDao clientDao = new ClientDao();

        client.setName("Irina")
                .setSurname("Markova")
                .setAccount(new Account()
                        .setLogin("Irina26")
                        .setRole(new Role()));

        try {
            clientDao.create(client);
            System.out.println(client);
            client.setName("Jenny");
            clientDao.update(client);
            System.out.println(clientDao.find(client.getId()));
            clientDao.delete(client.getId());
        } catch (DBException e) {
            e.printStackTrace();
        }

//-----------------------------------------------------------------------------------------
        Employee employee = new Employee();
        EmployeeDao employeeDaoDao = new EmployeeDao();

        employee.setName("Mika")
                .setSurname("Ukova")
                .setRating(4.8)
                .setAccount(new Account()
                        .setLogin("Mika")
                        .setRole(new Role()))
                .setProfession(new Profession().setName("Мастер маникюра"));

        try {
            employeeDaoDao.create(employee);
            System.out.println(employee);
            employee.setName("Vika");
            employeeDaoDao.update(employee);
            System.out.println(employeeDaoDao.find(employee.getId()));
            employeeDaoDao.delete(employee.getId());
        } catch (DBException e) {
            e.printStackTrace();
        }

//-----------------------------------------------------------------------------------
        Profession profession = new Profession().setName("Косметолог");
        ProfessionDao professionDao = new ProfessionDao();

        try {
            professionDao.create(profession);
            System.out.println(profession);
            profession.setName("Бровист");
            professionDao.update(profession);
            System.out.println(professionDao.find(profession.getId()));
            professionDao.delete(profession.getId());
        } catch (DBException e) {
            e.printStackTrace();
        }

//-------------------------------------------------------------------------------------
        Service service = new Service();
        ServiceDao serviceDao = new ServiceDao();

        service.setName("Массаж")
                        .setPrice(650.0)
                                .setSpendTime(new Time(60*60))
                .setProfession(new Profession().setName("Массажист"));

        try {
            serviceDao.create(service);
            System.out.println(service);
            service.setName("Макияж");
            serviceDao.update(service);
            System.out.println(serviceDao.find(service.getId()));
            serviceDao.delete(service.getId());
        } catch (DBException e) {
            e.printStackTrace();
        }

//-------------------------------------------------------------------------------------
        Ordering ordering = new Ordering();
        OrderingDao orderingDao = new OrderingDao();

        ordering.setService(new Service());
        ordering.setEmployee(new Employee());
        ordering.setClient(new Client());
        ordering.getEmployee().setProfession(new Profession());
        ordering.setOrderDateTime(LocalDateTime.parse("2021-11-06T13:45:00"));
        ordering.setStatus(StatusEnum.ACTIVE);
        ordering.getService().setId(1l);
        ordering.getEmployee().setId(2L);
        ordering.getClient().setId(1L);

        try {
            orderingDao.create(ordering);
            System.out.println(ordering);
            ordering.setOrderDateTime(LocalDateTime.parse("2021-11-16T09:00:00"));
            orderingDao.update(ordering);
            System.out.println(orderingDao.find(ordering.getId()));
            orderingDao.delete(ordering.getId());
        } catch (DBException e) {
            e.printStackTrace();
        }

    }
}
