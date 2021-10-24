package com.beautysalon.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class Profession extends BaseEntity{
    private String name;

    @Override
    public String toString() {
        return "Profession{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
