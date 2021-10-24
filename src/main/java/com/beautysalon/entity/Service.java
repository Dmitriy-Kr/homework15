package com.beautysalon.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Time;

@Setter
@Getter
@Accessors(chain = true)
public class Service extends BaseEntity{
    private String name;
    private Double price;
    private Time spendTime;
    private Profession profession;

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", spendTime=" + spendTime +
                ", profession=" + profession +
                "} " + super.toString();
    }
}
