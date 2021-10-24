package com.beautysalon.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Setter
@Getter
@Accessors(chain = true)
public class Employee extends BaseEntity{
    private String name;
    private String surname;
    private Double rating;
    private Account account;
    private Profession profession;

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", rank=" + rating +
                ", account=" + account +
                ", profession=" + profession +
                "} " + super.toString();
    }
}
