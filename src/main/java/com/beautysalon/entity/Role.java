package com.beautysalon.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Role extends BaseEntity{
    private RoleEnum name;

    public Role(){

    }

    public Role(RoleEnum name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name=" + name +
                "} " + super.toString();
    }
}
